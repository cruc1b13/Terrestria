package net.coderbot.terrestria.feature.trees;

import java.util.Random;
import java.util.Set;

import net.coderbot.terrestria.init.TerrestriaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class TropicalIslandPalmTreeFeature extends AbstractTreeFeature<DefaultFeatureConfig> {
	private static final BlockState LOG = Blocks.JUNGLE_LOG.getDefaultState();
	private static final BlockState LEAVES = Blocks.OAK_LEAVES.getDefaultState();

	public TropicalIslandPalmTreeFeature() {
		super(DefaultFeatureConfig::deserialize, false);
	}

	@Override
	protected boolean generate(Set<BlockPos> set, ModifiableTestableWorld world, Random rand, BlockPos pos, MutableIntBoundingBox mibb) {
		int height = 6 + rand.nextInt(4);

		pos = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR, pos);

		if (!(pos.getY() >= 1 && pos.getY() + height + 1 <= 256 && (this.isTropicalSand(world, pos.down()) || (super.isNaturalDirtOrGrass(world, pos.down()))))) {
			return false;
		}
		for (int i = 0; i < height; ++i) {
			if (!canTreeReplace(world, pos.up(i))) {
				return false;
			}
		}

		BlockPos origin = pos.add(0, height - 1, 0);
		
		for (int i = -1; i < 2; ++i) {
			setLeaves(world, origin.add(1, i, 0));
			setLeaves(world, origin.add(-1, i, 0));
			setLeaves(world, origin.add(0, i, 1));
			setLeaves(world, origin.add(0, i, -1));
		}
		setLeaves(world, origin.add(1, 0, 1));
		setLeaves(world, origin.add(-1, 0, 1));
		setLeaves(world, origin.add(1, 0, -1));
		setLeaves(world, origin.add(-1, 0, -1));
		for (int i = -2; i < 2; ++i) {
			if (i == 0) {
				continue;
			}
			setLeaves(world, origin.add(2, i, 0));
			setLeaves(world, origin.add(-2, i, 0));
			setLeaves(world, origin.add(0, i, 2));
			setLeaves(world, origin.add(0, i, -2));
		}

		setLeaves(world, origin.add(0, 1, 0));

		for (int i = 0; i <= height - 1; ++i) {
			setLog(world, pos.add(0, i, 0));
		}
		
		return true;
	}

	private boolean isTropicalSand(TestableWorld world, BlockPos down) {
		return world.testBlockState(down, (blockState_1) -> {
			Block block_1 = blockState_1.getBlock();
			return block_1 == TerrestriaBlocks.TROPICAL_SAND;
		});
	}
	
	private void setLog(ModifiableWorld world, BlockPos pos) {
		this.setBlockState(world, pos, LOG);
	}
	private void setLeaves(ModifiableWorld world, BlockPos pos) {
		this.setBlockState(world, pos, LEAVES);
	}
}
