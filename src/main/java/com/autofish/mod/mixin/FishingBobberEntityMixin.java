package com.autofish.mod.mixin;

import com.autofish.mod.AutoFishMod;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(ItemStack usedStack, CallbackInfoReturnable<Integer> cir) {
        FishingBobberEntity self = (FishingBobberEntity) (Object) this;
        if (self.getHookedEntity() != null) {
            AutoFishMod.getInstance().getController().onBobberHooked();
        }
    }
}
