package com.terraformersmc.terrestria.feature.volcano;

import com.mojang.datafixers.Dynamic;
import com.terraformersmc.terrestria.init.TerrestriaBiomes;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class VolcanoStructureFeature extends StructureFeature<DefaultFeatureConfig> {
	//
	private static final int VOLCANO_SPACING = 6;

	// How many chunks should be in between each volcano at least
	private static final int VOLCANO_SEPARATION = 3;
	private static final int SEED_MODIFIER = 0x0401C480;

	public VolcanoStructureFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function) {
		super(function);
	}

	protected ChunkPos getStart(ChunkGenerator<?> chunkGenerator_1, Random random_1, int chunkX, int chunkZ, int scaleX, int scaleZ) {
		chunkX += VOLCANO_SPACING * scaleX;
		chunkZ += VOLCANO_SPACING * scaleZ;

		// Adjust to grid position
		chunkX = chunkX < 0 ? chunkX - VOLCANO_SPACING + 1 : chunkX;
		chunkZ = chunkZ < 0 ? chunkZ - VOLCANO_SPACING + 1 : chunkZ;

		int finalChunkX = chunkX / VOLCANO_SPACING;
		int finalChunkZ = chunkZ / VOLCANO_SPACING;

		((ChunkRandom) random_1).setStructureSeed(chunkGenerator_1.getSeed(), finalChunkX, finalChunkZ, SEED_MODIFIER);

		// Get random position within grid area
		finalChunkX *= VOLCANO_SPACING;
		finalChunkZ *= VOLCANO_SPACING;
		finalChunkX += random_1.nextInt(VOLCANO_SPACING - VOLCANO_SEPARATION);
		finalChunkZ += random_1.nextInt(VOLCANO_SPACING - VOLCANO_SEPARATION);

		return new ChunkPos(finalChunkX, finalChunkZ);
	}

	@Override
	public boolean shouldStartAt(BiomeAccess biomeAccess, ChunkGenerator<?> generator, Random random, int chunkX, int chunkZ, Biome biomeIn) {
		ChunkPos start = this.getStart(generator, random, chunkX, chunkZ, 0, 0);

		if (chunkX == start.x && chunkZ == start.z) {
			Biome biome = biomeAccess.getBiome(new BlockPos(chunkX * 16 + 9, 0, chunkZ * 16 + 9));

			if (biome.getCategory() == Biome.Category.OCEAN && random.nextInt(4) != 0) {
				return false;
			} else if (biome == TerrestriaBiomes.VOLCANIC_ISLAND_SHORE && random.nextInt(2) != 0) {
				return false;
			}

			return generator.hasStructure(biome, this);
		}

		return false;
	}

	public StructureFeature.StructureStartFactory getStructureStartFactory() {
		return VolcanoStructureStart::new;
	}

	public String getName() {
		return "Volcano";
	}

	public int getRadius() {
		return 12;
	}

	public static class VolcanoStructureStart extends StructureStart {
		VolcanoStructureStart(StructureFeature<?> feature, int chunkX, int chunkZ, BlockBox box, int references, long baseSeed) {
			super(feature, chunkX, chunkZ, box, references, baseSeed);
		}

		public void initialize(ChunkGenerator<?> generator, StructureManager manager, int chunkX, int chunkZ, Biome biome) {
			VolcanoGenerator volcano = new VolcanoGenerator(this.random, chunkX * 16, chunkZ * 16, biome);

			this.children.add(volcano);
			this.setBoundingBoxFromChildren();
		}
	}
}
