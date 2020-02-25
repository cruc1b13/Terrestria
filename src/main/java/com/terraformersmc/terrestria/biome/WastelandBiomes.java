package com.terraformersmc.terrestria.biome;

import com.terraformersmc.terraform.biome.builder.TerraformBiome;
import com.terraformersmc.terrestria.init.TerrestriaBiomes;
import com.terraformersmc.terrestria.init.TerrestriaSurfaces;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;

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
