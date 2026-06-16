package com.autofish.mod.mixin;

import com.autofish.mod.AutoFishMod;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

    /**
     * Inject into use() — this is called when the bobber has caught something
     * and the player right-clicks to reel in. We intercept it to notify the
     * AutoFishController that a catch is ready.
     */
    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(net.minecraft.item.ItemStack usedStack, CallbackInfoReturnable<Integer> cir) {
        FishingBobberEntity self = (FishingBobberEntity) (Object) this;

        // Only trigger if there is something hooked
        if (self.getHookedEntity() != null || self.caughtFish) {
            AutoFishMod.getInstance().getController().onBobberHooked();
        }
    }
}
