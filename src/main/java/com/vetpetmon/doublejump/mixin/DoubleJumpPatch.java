package com.vetpetmon.doublejump.mixin;

import com.mojang.authlib.GameProfile;
import com.vetpetmon.doublejump.DoubleJumpConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class DoubleJumpPatch extends AbstractClientPlayer {
    @Shadow public abstract void playSound(SoundEvent p_108651_, float p_108652_, float p_108653_);

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
        if (player.onGround() || player.onClimbable()) jumps = DoubleJumpConfig.extraJumps;
        else if (!lastJumped && jumps > 0 && player.getDeltaMovement().y < 0) {
            if (player.input.jumping && !player.getAbilities().flying) {
                if (canPerformJump(player)) {
                    // TODO: Currently breaks on dedicated servers
                    //if (DoubleJumpConfig.jumpSound && jumps > 0) playSound(SoundEvents.BAT_TAKEOFF);
                    --jumps;
                    player.jumpFromGround();
                }
            }
        }
        lastJumped = player.input.jumping;
    }

    @Unique
    private boolean canPerformJump(LocalPlayer player) {
        ItemStack chestItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        return  (chestItemStack.getItem() != Items.ELYTRA && !ElytraItem.isFlyEnabled(chestItemStack)) && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION);
    }
}
