package xaero.common.minimap.render.radar;

import net.minecraft.class_1309;

public class LivingEntityRotationResetter {
   private float prevYaw;
   private float prevPitch;
   private float prevHeadYaw;
   private float lastLimbDistance;
   private float lastHandSwingProgress;
   private float prevBodyYaw;
   private float yaw;
   private float pitch;
   private float headYaw;
   private float limbDistance;
   private float handSwingProgress;
   private float bodyYaw;
   private int age;

   public void rememberAndResetValues(class_1309 livingEntity) {
      this.prevYaw = livingEntity.field_5982;
      this.prevPitch = livingEntity.field_6004;
      this.prevHeadYaw = livingEntity.field_6259;
      this.lastLimbDistance = livingEntity.field_42108.method_48570(0.0F);
      this.lastHandSwingProgress = livingEntity.field_6229;
      this.prevBodyYaw = livingEntity.field_6220;
      this.yaw = livingEntity.method_36454();
      this.pitch = livingEntity.method_36455();
      this.headYaw = livingEntity.field_6241;
      this.limbDistance = livingEntity.field_42108.method_48566();
      this.handSwingProgress = livingEntity.field_6251;
      this.bodyYaw = livingEntity.field_6283;
      this.age = livingEntity.field_6012;
      livingEntity.field_42108.method_48567(0.0F);
      livingEntity.field_5982 = 0.0F;
      livingEntity.field_6004 = 0.0F;
      livingEntity.field_6259 = 0.0F;
      livingEntity.field_42108.method_48568(0.0F, 0.0F);
      livingEntity.field_6229 = 0.0F;
      livingEntity.field_6220 = 0.0F;
      livingEntity.method_36456(0.0F);
      livingEntity.method_36457(0.0F);
      livingEntity.field_6241 = 0.0F;
      livingEntity.field_6251 = 0.0F;
      livingEntity.field_6283 = 0.0F;
      livingEntity.field_6012 = 10;
   }

   public void restore(class_1309 livingEntity) {
      livingEntity.field_42108.method_48567(0.0F);
      livingEntity.field_42108.method_48568(-this.lastLimbDistance, 1.0F);
      livingEntity.field_42108.method_48567(this.lastLimbDistance);
      livingEntity.field_42108.method_48568(0.0F, 0.0F);
      livingEntity.field_42108.method_48567(this.limbDistance);
      livingEntity.field_5982 = this.prevYaw;
      livingEntity.field_6004 = this.prevPitch;
      livingEntity.field_6259 = this.prevHeadYaw;
      livingEntity.field_6229 = this.lastHandSwingProgress;
      livingEntity.field_6220 = this.prevBodyYaw;
      livingEntity.method_36456(this.yaw);
      livingEntity.method_36457(this.pitch);
      livingEntity.field_6241 = this.headYaw;
      livingEntity.field_6251 = this.handSwingProgress;
      livingEntity.field_6283 = this.bodyYaw;
      livingEntity.field_6012 = this.age;
   }
}
