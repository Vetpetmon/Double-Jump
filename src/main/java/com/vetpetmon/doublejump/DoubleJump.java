package com.vetpetmon.doublejump;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(com.vetpetmon.doublejump.DoubleJump.MODID)
public class DoubleJump
{
    // Resource name
    public static final String MODID = "doublejump";
    // logging
    public static final Logger LOGGER = LogUtils.getLogger();

    //We're not doing too much in here; worst we are going to do here is a singular mixin file
    public DoubleJump(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.COMMON, DoubleJumpConfig.SPEC);
    }
}
