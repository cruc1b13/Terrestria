package com.terraformersmc.terrestria;

import com.terraformersmc.terrestria.command.LocateAny;
import com.terraformersmc.terrestria.config.BiomeConfig;
import com.terraformersmc.terrestria.config.BiomeConfigHandler;
import com.terraformersmc.terrestria.init.*;
import com.terraformersmc.terrestria.item.LogTurnerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Terrestria implements ModInitializer {
	public static final String MOD_ID = "terrestria";
	public static ItemGroup ITEM_GROUP;

	@Override
	public void onInitialize() {
		ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "items"), () -> new ItemStack(TerrestriaItems.RUBBER_SAPLING));

		BiomeConfig config = BiomeConfigHandler.getBiomeConfig();

		TerrestriaBlocks.init();
		TerrestriaItems.init();
		TerrestriaFeatures.init();
		TerrestriaSurfaces.init();
		TerrestriaBiomes.init();
		TerrestriaGeneration.init(config);

		BiomeConfigHandler.save();

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "log_turner"), new LogTurnerItem(new Item.Settings().group(ITEM_GROUP)));
		LocateAny.register();
	}
}