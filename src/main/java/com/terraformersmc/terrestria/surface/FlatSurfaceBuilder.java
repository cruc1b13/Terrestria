package com.terraformersmc.terrestria.surface;

import com.mojang.datafixers.Dynamic;
import com.terraformersmc.terraform.noise.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class FlatSurfaceBuilder extends DefaultSurfaceBuilder {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	int targetHeight = 63;

	public FlatSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> deserializer) {
		super(deserializer);
	}

	@Override
	public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noiseVal, BlockState var9, BlockState var10, int var11, long seed, TernarySurfaceConfig config) {

		BlockPos.Mutable pos = new BlockPos.Mutable(x, height, z);

		//Make everything at sea Level and below solid
		do {
			chunk.setBlockState(new BlockPos.Mutable(x, height, z), config.getTopMaterial(), true);
			pos.setOffset(Direction.DOWN);
		} while (chunk.getBlockState(pos).getBlock().equals(Blocks.WATER));

		pos = new BlockPos.Mutable(x, targetHeight, z);

		//Add occasional islands
		double islandNoise = NOISE.sample(x * 0.1, z * 0.1);
		double islandHeight = islandNoise * 80 + targetHeight - 1;

		if (height < targetHeight -2) {
			for (int i = targetHeight; i < islandHeight; i++) {
				chunk.setBlockState(pos, config.getTopMaterial(), true);
				pos.setOffset(Direction.UP);
			}
		} else {
			super.generate(rand, chunk, biome, x, z, height, noiseVal, var9, var10, var11, seed, config);
		}
	}
}
