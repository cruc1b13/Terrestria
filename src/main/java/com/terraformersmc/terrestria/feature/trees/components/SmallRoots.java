package com.terraformersmc.terrestria.feature.trees.components;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;

import java.util.Set;

public interface SmallRoots {
	void placeRoot(Set<BlockPos> blocks, ModifiableTestableWorld world, BlockPos.Mutable pos, int rootLength, BlockBox box);
}
