package com.choonster.testmod3;

import com.choonster.testmod3.config.Config;
import com.choonster.testmod3.event.BucketFillHandler;
import com.choonster.testmod3.init.*;
import com.choonster.testmod3.proxy.CommonProxy;
import com.choonster.testmod3.recipe.ShapelessCuttingRecipe;
import com.choonster.testmod3.util.BiomeBlockReplacer;
import com.choonster.testmod3.world.gen.WorldGenOres;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

@Mod(modid = TestMod3.MODID, version = TestMod3.VERSION, guiFactory = "com.choonster.testmod3.config.GuiConfigFactoryTestMod3")
public class TestMod3 {
	public static final String MODID = "testmod3";
	public static final String VERSION = "1.0";

	public static CreativeTabs creativeTab;

	@SidedProxy(clientSide = "com.choonster.testmod3.proxy.ClientProxy", serverSide = "com.choonster.testmod3.proxy.ServerProxy")
	public static CommonProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		creativeTab = new CreativeTabExample();
		Config.load(event);

		RecipeSorter.register("testmod3:shapelesscutting", ShapelessCuttingRecipe.class, SHAPELESS, "after:minecraft:shapeless");

		MinecraftForge.EVENT_BUS.register(new BucketFillHandler());

		ModFluids.registerFluids();
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModFluids.registerBuckets();
		ModBiomes.registerBiomes();
		ModMapGen.registerMapGen();

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModItems.addRecipes();

		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeBlockReplacer());

		proxy.postInit();
	}
}
