package com.ninni.arthropoda.client.init;

import com.ninni.arthropoda.mixin.client.EntityModelLayersInvoker;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.ninni.arthropoda.Arthropoda.*;

public class ArthropodaEntityModelLayers {

    public static final EntityModelLayer ANT = registerMain("ant");

    private static EntityModelLayer registerMain(String id) {
        return EntityModelLayersInvoker.register(new Identifier(MOD_ID, id).toString(), "main");
    }

    private static EntityModelLayer register(String id, String layer) {
        return EntityModelLayersInvoker.register(new Identifier(MOD_ID, id).toString(), layer);
    }
}

