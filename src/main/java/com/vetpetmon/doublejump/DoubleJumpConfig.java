package com.vetpetmon.doublejump;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = DoubleJump.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DoubleJumpConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue EXTRA_JUMPS = BUILDER
            .comment("Extra Jumps")
            .defineInRange("extraJumps", 1, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int extraJumps;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        extraJumps = EXTRA_JUMPS.get();
    }
}