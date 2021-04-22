package me.xa5.smoothbedrock;

import me.xa5.smoothbedrock.config.ModSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(SmoothBedrock.MOD_ID)
public class SmoothBedrock {
    public static final String MOD_ID = "smoothbedrock";
    public static SBLogger LOGGER = new SBLogger();

    public SmoothBedrock () {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModSettings.COMMON_SPEC);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean shouldModifyBedrock(ResourceLocation dimType) {
//        return true;

        System.out.println("getting isInList for: " + dimType.toString());

        boolean isInList = ModSettings.getIsInList(dimType.toString());

        if (ModSettings.COMMON_CONFIG.isWhitelist.get()) {
//             Is a whitelist; only return true if the dimension is inside the list
            return isInList;
        } else {
//             Return true if the dimension is not in the list, as it is a blacklist.
            return !isInList;
        }
    }
}