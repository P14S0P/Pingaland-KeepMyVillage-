package com.piasop.keepmyvillage.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

/**
 * Handles the configuration for the mod, allowing users to customize its behavior.
 */
public class ModConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_BREAKABLE_BLOCKS;

    static {
        BUILDER.push("Village Protection Settings");

        ALLOWED_BREAKABLE_BLOCKS = BUILDER
                .comment("A list of block IDs that can always be broken inside a village.",
                         "Examples: [\"minecraft:torch\", \"minecraft:chest\", \"minecraft:crafting_table\"]")
                .defineList("allowedBreakableBlocks",
                            List.of("minecraft:powder_snow", "Minecraft:snow_block"),
                            obj -> obj instanceof String);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}