package me.xa5.smoothbedrock.config;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModSettings {
    public static final Config COMMON_CONFIG;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        {
            final Pair<Config, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Config::new);
            COMMON_SPEC = serverSpecPair.getRight();
            COMMON_CONFIG = serverSpecPair.getLeft();
        }
    }

    public static boolean getIsInList(String dimType) {
        List<String> dimensionTypes = COMMON_CONFIG != null ?
                (List<String>) COMMON_CONFIG.dimensionFilter.get() : new ArrayList<>();

            return dimensionTypes.contains(dimType);
    }
}