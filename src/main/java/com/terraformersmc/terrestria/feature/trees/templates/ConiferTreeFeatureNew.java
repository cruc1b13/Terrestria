package com.terraformersmc.terrestria.feature.trees.templates;

import com.mojang.datafixers.Dynamic;
import com.terraformersmc.terraform.util.Shapes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ConiferTreeFeatureNew extends AbstractTreeFeature<BranchedTreeFeatureConfig> {
	public ConiferTreeFeatureNew(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> function) {
		super(function);
	}

	@Override
	public boolean generate(ModifiableTestableWorld world, Random rand, BlockPos origin, Set<BlockPos> logs, Set<BlockPos> leaves, BlockBox box, BranchedTreeFeatureConfig config) {
		int height = getLeafHeight(rand);
		int bareTrunkHeight = getBareTrunkHeight(rand);
		int maxLeafRadius = getMaxLeafRadius(rand);
		int leafLayers = getLeafLayers(rand);
		double shrinkAmount = getShrinkAmount();
		int trunkRadius = getMaxTrunkRadius(rand);

		//If the tree doesn't have enough room with it's current height before build limit
		if (origin.getY() + height + 1 > 256 || origin.getY() < 1) {
			return false;
		}

		//If the ground below the sapling is good enough for a tree
		if (!isNaturalDirtOrGrass(world, origin.down())) {
			return false;
		}

		//If there is room for the tree
		if (!checkForObstructions(world, origin, height, bareTrunkHeight, maxLeafRadius)) {
			return false;
		}

		//Set the block below the trunk to dirt (because vanilla does it)
		setToDirt(world, origin.down());
		growTrunk(world, rand, new BlockPos.Mutable(origin), logs, box, config, height + bareTrunkHeight, trunkRadius);
		growLeaves(world, rand, new BlockPos.Mutable(origin).setOffset(Direction.UP, bareTrunkHeight), leaves, box, config, height, maxLeafRadius, shrinkAmount, leafLayers);

		return true;
	}

	private void growLeaves(ModifiableTestableWorld world, Random rand, BlockPos.Mutable pos, Set<BlockPos> leaves, BlockBox box, BranchedTreeFeatureConfig config, int height, int maxRadius, double shrinkAmmount, int layers) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int layerHeight = (height / layers);
		int leafLayerHeight = 2 * (height / layers);

		for (int layer = 0; layer < layers; layer++) {
			if (layer == (layers - 1)) { //Limit the top layer to not be too pointy
				leafLayerHeight = layerHeight;
			}
			for (int currentHeight = 0; currentHeight < leafLayerHeight; currentHeight++) {

				pos.set(x, y + (layer * layerHeight) + currentHeight, z);

				Shapes.canopyCircle(pos,
						outerRadius(maxRadius - (layer * shrinkAmmount), currentHeight, leafLayerHeight),
						innerRadius(maxRadius - (layer * shrinkAmmount), currentHeight, layerHeight),
						position -> setLeavesBlockState(world, rand, pos, leaves, box, config)
				);
			}
		}
	}

	/**
	 * @param maxRadius     the maximum radius for the leaves to generate
	 * @param currentHeight the progress from 0 to the height that the leaves have generated (the x value of the polynomial)
	 * @param height        the target height for the leaf layer
	 * @return the radius of the outside leaf layer at it's current y-height
	 */
	private double outerRadius(double maxRadius, double currentHeight, double height) {
		double x = currentHeight / height;
		// A 3rd-degree polynomial approximating the shape of a Conifer tree. from 0-1
		return maxRadius * ((-0.6 * (x * x * x) + 1.96 * (x * x) - 2.37 * x) + 1);
	}

	/**
	 * @param maxRadius     the maximum radius for the leaves to generate
	 * @param currentHeight the progress from 0 to the height that the leaves have generated (the x value of the polynomial)
	 * @param height        the target height for the leaf layer
	 * @return the radius of the inner leaf layer at it's current y-height
	 */
	private double innerRadius(double maxRadius, double currentHeight, double height) {
		double x = currentHeight / height;
		x = maxRadius * ((-3.24 * (x * x * x) + .25 * (x * x) - 2.98 * x) + 1) - 2.5;
		return x < 0 ? 0 : x;
	}

	public void growTrunk(ModifiableTestableWorld world, Random rand, BlockPos.Mutable pos, Set<BlockPos> logs, BlockBox box, BranchedTreeFeatureConfig config, int height, int trunkRadius) {
		for (int i = 0; i < (height * 0.83); i++) {
			setLogBlockState(world, rand, pos, logs, box, config);
			pos.setOffset(Direction.UP);
		}
	}

	private boolean checkForObstructions(TestableWorld world, BlockPos origin, int height, int bareTrunkHeight, int radius) {
		BlockPos.Mutable pos = new BlockPos.Mutable(origin);

		for (int i = 0; i < bareTrunkHeight; i++) {
			if (!canTreeReplace(world, pos.setOffset(Direction.UP))) {
				return false;
			}
		}

		for (int dY = bareTrunkHeight; dY < height; dY++) {
			for (int dZ = -radius; dZ <= radius; dZ++) {
				for (int dX = -radius; dX <= radius; dX++) {
					pos.set(origin.getX() + dX, origin.getY() + dY, origin.getZ() + dZ);

					if (!canTreeReplace(world, pos)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public int getLeafHeight(Random rand) {
		return rand.nextInt(12) + 32;
	}

	public int getBareTrunkHeight(Random rand) {
		return 8 + rand.nextInt(12);
	}

	public int getMaxLeafRadius(Random rand) {
		return 6 + rand.nextInt(4);
	}

	public int getLeafLayers(Random rand) {
		return rand.nextInt(4) + 4;
	}

	public double getShrinkAmount() {
		return 1.0; //1.0 for 1 block
	}

	public int getMaxTrunkRadius(Random rand) {
		return 1;
	}
}
