package me.xa5.smoothbedrock.config;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static ForgeConfigSpec.BooleanValue isWhitelist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionFilter;

    public Config(ForgeConfigSpec.Builder builder) {
        System.out.println("doing something here");


//        builder.comment("General settings").push("General");
        isWhitelist = builder
                .comment("true: Dimensions in filter will have flat bedrock. false: Dimensions in list will not have flat bedrock")
                .define("isWhitelist", false);

        dimensionFilter = builder
                .comment("A list of dimension ids that this mod should filter based on the 'Act as whitelist' setting.")
                .defineList("dimensionFilter", Arrays.asList(
                        getDefaultFilter()
                ), o -> o instanceof String && !((String) o).isEmpty());
//        builder.pop();

        System.out.println("finished doing something here");
    }

    private String[] getDefaultFilter() {
        return new String[]{DimensionType.THE_END_ID.toString()};
    }
}