package com.ninni.arthropoda;

import com.google.common.reflect.Reflection;
import com.ninni.arthropoda.block.ArthropodaBlocks;
import com.ninni.arthropoda.item.ArthropodaItems;
import com.ninni.arthropoda.sound.ArthropodaSoundEvents;
import net.fabricmc.api.ModInitializer;

public class Arthropoda implements ModInitializer {
	public static final String MOD_ID = "arthropoda";

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onInitialize() {
		Reflection.initialize(
			ArthropodaItems.class,
			ArthropodaBlocks.class,
			ArthropodaSoundEvents.class
		);
	}
}
