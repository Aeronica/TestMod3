package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.fluid.PortalDisplacementFluid;
import choonster.testmod3.fluid.StaticFluid;
import choonster.testmod3.fluid.group.FluidGroup;
import choonster.testmod3.fluid.group.StandardFluidGroup;
import choonster.testmod3.world.item.block.FluidTankItem;
import choonster.testmod3.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFluids {
	private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TestMod3.MODID);
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod3.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final FluidGroup<FlowingFluid, FlowingFluid, LiquidBlock, Item> STATIC = new StandardFluidGroup.Builder("static", FLUIDS, BLOCKS, ITEMS)
			.stillFactory(StaticFluid.Source::new)
			.flowingFactory(StaticFluid.Flowing::new)
			.attributes(
					FluidAttributes.builder(new ResourceLocation(TestMod3.MODID, "block/fluid_static_still"), new ResourceLocation(TestMod3.MODID, "block/fluid_static_still"))
							.luminosity(10)
							.density(800)
							.viscosity(1500)
			)
			.blockMaterial(ModMaterials.STATIC)
			.build();

	// TODO: Implement gases
	public static final FluidGroup<FlowingFluid, FlowingFluid, LiquidBlock, Item> STATIC_GAS = new StandardFluidGroup.Builder("static_gas", FLUIDS, BLOCKS, ITEMS)
			.stillFactory(StaticFluid.Source::new)
			.flowingFactory(StaticFluid.Flowing::new)
			.attributes(
					FluidAttributes.builder(new ResourceLocation(TestMod3.MODID, "block/fluid_static_gas_still"), new ResourceLocation(TestMod3.MODID, "block/fluid_static_gas_still"))
							.luminosity(10)
							.density(-800)
							.viscosity(1500)
							.gaseous()
			)
			.blockMaterial(ModMaterials.STATIC_GAS)
			.build();

	public static final FluidGroup<FlowingFluid, FlowingFluid, LiquidBlock, Item> NORMAL = new StandardFluidGroup.Builder("normal", FLUIDS, BLOCKS, ITEMS)
			.attributes(
					FluidAttributes.builder(new ResourceLocation(TestMod3.MODID, "block/fluid_normal_still"), new ResourceLocation(TestMod3.MODID, "block/fluid_normal_flow"))
							.luminosity(10)
							.density(800)
							.viscosity(1500)
			)
			.blockMaterial(ModMaterials.NORMAL)
			.build();

	public static final FluidGroup<FlowingFluid, FlowingFluid, LiquidBlock, Item> NORMAL_GAS = new StandardFluidGroup.Builder("normal_gas", FLUIDS, BLOCKS, ITEMS)
			.attributes(
					FluidAttributes.builder(new ResourceLocation(TestMod3.MODID, "block/fluid_normal_gas_still"), new ResourceLocation(TestMod3.MODID, "block/fluid_normal_gas_flow"))
							.luminosity(10)
							.density(-1600)
							.viscosity(100)
							.gaseous()
			)
			.blockMaterial(ModMaterials.NORMAL_GAS)
			.build();

	public static final FluidGroup<FlowingFluid, FlowingFluid, LiquidBlock, Item> PORTAL_DISPLACEMENT = new StandardFluidGroup.Builder("portal_displacement", FLUIDS, BLOCKS, ITEMS)
			.stillFactory(PortalDisplacementFluid.Source::new)
			.flowingFactory(PortalDisplacementFluid.Flowing::new)
			.attributes(
					FluidAttributes.builder(new ResourceLocation(TestMod3.MODID, "block/fluid_portal_displacement_still"), new ResourceLocation(TestMod3.MODID, "block/fluid_portal_displacement_flow"))
							.luminosity(10)
							.density(1600)
							.viscosity(100)
			)
			.blockMaterial(ModMaterials.PORTAL_DISPLACEMENT)
			.build();

	// TODO: Finite fluid implementation?
//	public static final Fluid FINITE = createFluid("finite", false,
//			fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
//			fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.BLACK)));
//

	/**
	 * Registers the {@link DeferredRegister} instances with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		FLUIDS.register(modEventBus);
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);

		isInitialised = true;
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void commonSetup(final FMLCommonSetupEvent event) {
			event.enqueueWork(RegistrationHandler::registerFluidContainers);
		}

		/**
		 * Register this mod's tanks.
		 */
		private static void registerFluidContainers() {
			registerTank(() -> Fluids.WATER);
			registerTank(() -> Fluids.LAVA);

			FLUIDS.getEntries().forEach(RegistrationHandler::registerTank);
		}

		private static void registerTank(final Supplier<Fluid> fluid) {
			final Fluid fluid1 = fluid.get();
			final FluidStack fluidStack = new FluidStack(fluid1, FluidTankBlockEntity.CAPACITY);

			final Item item = ModBlocks.FLUID_TANK.get().asItem();
			assert item instanceof FluidTankItem;

			((FluidTankItem) item).addFluid(fluidStack);
		}
	}
}
