package com.terraformersmc.terrestria.biome;

import com.terraformersmc.terraform.biome.builder.TerraformBiome;
import com.terraformersmc.terrestria.init.TerrestriaBiomes;
import com.terraformersmc.terrestria.init.TerrestriaSurfaces;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.LakeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LakeFeatureConfig;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import static com.terraformersmc.terraform.biome.builder.DefaultFeature.*;

public class WastelandBiomes {
	public static void register() {
		TerraformBiome.Template template = new TerraformBiome.Template(TerraformBiome.builder()
				.precipitation(Biome.Precipitation.NONE).category(Biome.Category.DESERT)
				.temperature(2.0F)
				.downfall(0.0F)
				.waterColor(0xF08000)
				.waterFogColor(0x301800)
				.addDefaultFeatures(LAND_CARVERS, STRUCTURES, DUNGEONS, PLAINS_TALL_GRASS, MINEABLES, ORES, DISKS,
						DEFAULT_MUSHROOMS, DEFAULT_VEGETATION, SPRINGS, FROZEN_TOP_LAYER)
				.addCustomFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, Biome.configureFeature(
						Feature.LAKE,
						new LakeFeatureConfig(Blocks.WATER.getDefaultState()),
						Decorator.LAVA_LAKE,
						new LakeDecoratorConfig(80)
				))
				.addCustomFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, Biome.configureFeature(
						Feature.LAKE,
						new LakeFeatureConfig(Blocks.LAVA.getDefaultState()),
						Decorator.LAVA_LAKE,
						new LakeDecoratorConfig(160)
				))
				.addStructureFeature(Feature.STRONGHOLD)
				.addStructureFeature(Feature.MINESHAFT, new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL))
				.addDefaultSpawnEntries()
		);

		TerrestriaBiomes.WASTELAND = TerrestriaBiomes.register("wasteland", template.builder()
				.configureSurfaceBuilder(TerrestriaSurfaces.FLAT, TerrestriaSurfaces.CRACKED_SAND_CONFIG)
				.depth(-0.3F)
				.scale(0F)
				.build()
		);

		TerrestriaBiomes.WASTELAND_EDGE = TerrestriaBiomes.register("wasteland_edge", template.builder()
				.configureSurfaceBuilder(TerrestriaSurfaces.FLAT, TerrestriaSurfaces.CRACKED_SAND_CONFIG)
				.depth(0.6F)
				.scale(0.1F)
				.build()
		);
	}
}
