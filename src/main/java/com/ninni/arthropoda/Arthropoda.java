package com.ninni.arthropoda;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;

public class Arthropoda implements ModInitializer {
	public static final String MOD_ID = "arthropoda";

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onInitialize() {
		Reflection.initialize();
	}
}
