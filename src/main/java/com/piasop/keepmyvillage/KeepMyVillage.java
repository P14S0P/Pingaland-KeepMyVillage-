package com.piasop.keepmyvillage;

import com.mojang.logging.LogUtils;
import com.piasop.keepmyvillage.core.config.ModConfig;
import com.piasop.keepmyvillage.core.init.SoundRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mod(KeepMyVillage.MOD_ID)
public class KeepMyVillage {

    public static final String MOD_ID = "keepmyvillage";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random RANDOM = new Random();

    private static final List<String> VILLAGER_NAMES = new ArrayList<>();
    private static final List<String> ANGRY_PHRASES = new ArrayList<>();
    private static final List<SoundEvent> ANGRY_SOUNDS = new ArrayList<>();

    public KeepMyVillage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC, "keepmyvillage-common.toml");

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("KeepMyVillage: Loading custom data...");
        loadDataFromResources();
        populateSoundList();
        LOGGER.info("KeepMyVillage: Loaded {} villager names and {} angry phrases.", VILLAGER_NAMES.size(), ANGRY_PHRASES.size());
    }

    private void loadDataFromResources() {
        loadFile("data/villager_names.txt", VILLAGER_NAMES);
        loadFile("data/angry_phrases.txt", ANGRY_PHRASES);
    }

    private void populateSoundList() {
        ANGRY_SOUNDS.add(SoundRegistry.VILLAGER_ANGRY_1.get());
        ANGRY_SOUNDS.add(SoundRegistry.VILLAGER_ANGRY_2.get());
        ANGRY_SOUNDS.add(SoundRegistry.VILLAGER_ANGRY_3.get());
        ANGRY_SOUNDS.add(SoundRegistry.VILLAGER_ANGRY_4.get());
    }

    private void loadFile(String path, List<String> list) {
        try {
            ResourceLocation location = new ResourceLocation(MOD_ID, path);
            InputStream inputStream = KeepMyVillage.class.getClassLoader()
                    .getResourceAsStream("assets/" + location.getNamespace() + "/" + location.getPath());

            if (inputStream == null) {
                throw new IllegalStateException("Could not find resource: " + path);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                list.addAll(reader.lines().filter(line -> !line.trim().isEmpty()).collect(Collectors.toList()));
            }

        } catch (Exception e) {
            LOGGER.error("Failed to load data file: " + path, e);
        }
    }

    public static String getRandomName() {
        if (VILLAGER_NAMES.isEmpty()) return "Villager";
        return VILLAGER_NAMES.get(RANDOM.nextInt(VILLAGER_NAMES.size()));
    }

    public static String getRandomPhrase() {
        if (ANGRY_PHRASES.isEmpty()) return "You shouldn't do that!";
        return ANGRY_PHRASES.get(RANDOM.nextInt(ANGRY_PHRASES.size()));
    }

    public static SoundEvent getRandomSound() {
        if (ANGRY_SOUNDS.isEmpty()) {
            throw new IllegalStateException("Angry sounds list is not populated!");
        }
        return ANGRY_SOUNDS.get(RANDOM.nextInt(ANGRY_SOUNDS.size()));
    }
}