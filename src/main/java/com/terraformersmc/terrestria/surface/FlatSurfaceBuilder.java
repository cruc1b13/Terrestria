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
	private int targetHeight = 63;
	private long lastSeed = 0;
	private OpenSimplexNoise noise = new OpenSimplexNoise(0);
	private OpenSimplexNoise noise2 = new OpenSimplexNoise(1);

	public FlatSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> deserializer) {
		super(deserializer);
	}

	@Override
	public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noiseVal, BlockState var9, BlockState var10, int var11, long seed, TernarySurfaceConfig config) {

		BlockPos.Mutable pos = new BlockPos.Mutable(x, height, z);

		do {
			chunk.setBlockState(new BlockPos.Mutable(x, height, z), config.getTopMaterial(), true);
			pos.setOffset(Direction.DOWN);
		} while (chunk.getBlockState(pos).getBlock().equals(Blocks.WATER));
	}
}
