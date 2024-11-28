package xaero.common.graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

public class GuiHelper {
   public static void blit(class_4587 pose, int x, int y, float u, float v, int w, int h) {
      blit(pose, x, x + w, y, y + h, 0, w, h, u, v, 256, 256);
   }

   public static void blit(class_4587 pose, int x, int y, int z, float u, float v, int w, int h, int textureW, int textureH) {
      blit(pose, x, x + w, y, y + h, z, w, h, u, v, textureW, textureH);
   }

   static void blit(class_4587 pose, int left, int right, int top, int bottom, int z, int uw, int vh, float u, float v, int textureW, int textureH) {
      innerBlit(
         pose,
         left,
         right,
         top,
         bottom,
         z,
         (u + 0.0F) / (float)textureW,
         (u + (float)uw) / (float)textureW,
         (v + 0.0F) / (float)textureH,
         (v + (float)vh) / (float)textureH
      );
   }

   static void innerBlit(class_4587 pose, int left, int right, int top, int bottom, int z, float uLeft, float uRight, float vTop, float vBottom) {
      RenderSystem.setShader(class_757::method_34542);
      Matrix4f matrix4f = pose.method_23760().method_23761();
      class_287 bufferBuilder = class_289.method_1348().method_1349();
      bufferBuilder.method_1328(class_5596.field_27382, class_290.field_1585);
      bufferBuilder.method_22918(matrix4f, (float)left, (float)top, (float)z).method_22913(uLeft, vTop).method_1344();
      bufferBuilder.method_22918(matrix4f, (float)left, (float)bottom, (float)z).method_22913(uLeft, vBottom).method_1344();
      bufferBuilder.method_22918(matrix4f, (float)right, (float)bottom, (float)z).method_22913(uRight, vBottom).method_1344();
      bufferBuilder.method_22918(matrix4f, (float)right, (float)top, (float)z).method_22913(uRight, vTop).method_1344();
      class_286.method_43433(bufferBuilder.method_1326());
   }
}
