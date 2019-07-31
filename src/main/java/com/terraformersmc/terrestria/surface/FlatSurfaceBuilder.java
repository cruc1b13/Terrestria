package com.terraformersmc.terrestria.surface;

import com.mojang.datafixers.Dynamic;
import com.terraformersmc.terraform.noise.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class FlatSurfaceBuilder extends DefaultSurfaceBuilder {
	private int targetHeight = 63;
	private long lastSeed = 0;
	private OpenSimplexNoise noise = new OpenSimplexNoise(0);
	private OpenSimplexNoise noise2 = new OpenSimplexNoise(1);

	public FlatSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> deserializer) {
		super(deserializer);
	}

	@Override
	public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noiseVal, BlockState var9, BlockState var10, int var11, long seed, TernarySurfaceConfig config) {
		if(lastSeed != seed) {
			lastSeed = seed;

			noise = new OpenSimplexNoise(seed);
			noise2 = new OpenSimplexNoise(seed + 1);
		}

		height = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(x & 15, z & 15);

		if(height < targetHeight) {
			BlockPos.Mutable pos = new BlockPos.Mutable(x & 15, height, z & 15);

			while(height < targetHeight) {
				pos.setY(height);

				chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);

				height++;
			}

			height = targetHeight;

			double noiseVal1 = noise.sample(x * 0.05, z * 0.05);
			double noiseVal2 = noise2.sample(x * 0.05, z * 0.05);

			if((noiseVal1 > 0 && noiseVal1 < 0.1 && noiseVal > -0.5) || (noiseVal2 > 0 && noiseVal2 < 0.1) && noiseVal < 0.5) {
				height -= 1;

				pos.setY(height);

				chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
			}
		} else if(height > targetHeight) {
			config = SurfaceBuilder.SAND_CONFIG;
		}

		super.generate(rand, chunk, biome, x, z, height, noiseVal, var9, var10, config.getTopMaterial(), config.getUnderMaterial(), config.getUnderwaterMaterial(), var11);
	}
}
