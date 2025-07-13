package com.piasop.keepmyvillage.core.init;

import com.piasop.keepmyvillage.KeepMyVillage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Handles the registration of all custom sounds for the mod.
 */
public class SoundRegistry {

    // Create a DeferredRegister for sound events
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KeepMyVillage.MOD_ID);

    // Register a list of angry villager sounds
    public static final RegistryObject<SoundEvent> VILLAGER_ANGRY_1 = registerSoundEvent("villager.angry1");
    public static final RegistryObject<SoundEvent> VILLAGER_ANGRY_2 = registerSoundEvent("villager.angry2");
    public static final RegistryObject<SoundEvent> VILLAGER_ANGRY_3 = registerSoundEvent("villager.angry3");
    public static final RegistryObject<SoundEvent> VILLAGER_ANGRY_4 = registerSoundEvent("villager.angry4");


    /**
     * Helper method to register a sound event.
     * @param name The name of the sound file (without extension).
     * @return A RegistryObject holding the SoundEvent.
     */
    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(KeepMyVillage.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}