package com.ninni.arthropoda.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.ninni.arthropoda.Arthropoda.*;

public interface ArthropodaSoundEvents {


    SoundEvent ENTITY_ANT_AMBIENT   = ant("idle");
    SoundEvent ENTITY_ANT_HURT      = ant("hurt");
    SoundEvent ENTITY_ANT_DEATH     = ant("death");
    SoundEvent ENTITY_ANT_EAT       = ant("eat");
    SoundEvent ENTITY_ANT_ATTACK    = ant("attack");
    SoundEvent ENTITY_ANT_WALK      = ant("walk");
    private static SoundEvent ant(String type) {
        return createEntitySound("ant", type);
    }

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }

    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }
}
