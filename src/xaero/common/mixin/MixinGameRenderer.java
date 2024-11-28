package xaero.common.mixin;

import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.HudMod;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.core.XaeroMinimapCoreFabric;
import xaero.common.events.ClientEvents;

@Mixin({class_757.class})
public class MixinGameRenderer {
   @Shadow
   private class_310 field_4015;

   @Inject(
      at = {@At("HEAD")},
      method = {"render"}
   )
   public void onRenderStart(float float_1, long long_1, boolean boolean_1, CallbackInfo info) {
      if (XaeroMinimapCore.isModLoaded()) {
         ClientEvents fmlEvents = HudMod.INSTANCE.getEvents();
         if (fmlEvents != null) {
            fmlEvents.handleRenderTickStart();
         }
      }
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"render"}
   )
   public void onRenderEnd(float float_1, long long_1, boolean boolean_1, CallbackInfo info) {
      if (XaeroMinimapCore.isModLoaded()) {
         if (!this.field_4015.field_1743 && this.field_4015.method_18506() == null && this.field_4015.field_1755 != null) {
            ClientEvents events = HudMod.INSTANCE.getEvents();
            if (events != null) {
               events.handleDrawScreenEventPost(this.field_4015.field_1755);
            }
         }
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"resetProjectionMatrix"}
   )
   public void onLoadProjectionMatrixStart(Matrix4f matrix, CallbackInfo info) {
      XaeroMinimapCoreFabric.onResetProjectionMatrix(matrix);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderLevel"}
   )
   public void onRenderWorldStart(float tickDelta, long limitTime, class_4587 matrix, CallbackInfo info) {
      XaeroMinimapCoreFabric.beforeRenderWorld();
   }

   @Inject(
      at = {@At(
         value = "FIELD",
         opcode = 180,
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderHand:Z",
         ordinal = 0
      )},
      method = {"renderLevel"}
   )
   public void onRenderWorldHand(float tickDelta, long limitTime, class_4587 matrixStack, CallbackInfo info) {
      XaeroMinimapCore.onWorldModelViewMatrix(matrixStack);
   }
}
