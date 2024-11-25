package com.vetpetmon.doublejump.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class DoubleJumpPatch extends AbstractClientPlayer {
    @Unique
    private int jumps=0;
    @Unique
    private boolean lastJumped=false;

    public DoubleJumpPatch(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel,gameProfile);
    }

    @Inject(method = "aiStep()V", at = @At("HEAD"))
    private void doubleJump(CallbackInfo info) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.onGround() || player.onClimbable())
            jumps = 1;
        else if (!lastJumped && jumps > 0 && player.getDeltaMovement().y < 0) {
            if (player.input.jumping && !player.getAbilities().flying) {
                if (canJump(player)) {
                    --jumps;
                    player.jumpFromGround();
                }
            }
        }
        lastJumped = player.input.jumping;
    }

    @Unique
    private boolean canJump(LocalPlayer player) {
        ItemStack chestItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        return  (chestItemStack.getItem() != Items.ELYTRA && !ElytraItem.isFlyEnabled(chestItemStack)) && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION);
    }
}
