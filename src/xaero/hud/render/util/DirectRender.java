package xaero.hud.render.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

public class DirectRender {
   public static void coloredRectangle(class_4587 matrices, float x1, float y1, float x2, float y2, int color) {
      coloredRectangle(matrices.method_23760().method_23761(), x1, y1, x2, y2, color);
   }

   public static void coloredRectangle(Matrix4f matrix, float x1, float y1, float x2, float y2, int color) {
      float a = (float)(color >> 24 & 0xFF) / 255.0F;
      float r = (float)(color >> 16 & 0xFF) / 255.0F;
      float g = (float)(color >> 8 & 0xFF) / 255.0F;
      float b = (float)(color & 0xFF) / 255.0F;
      RenderSystem.setShader(class_757::method_34540);
      class_289 tessellator = class_289.method_1348();
      class_287 vertexBuffer = tessellator.method_1349();
      vertexBuffer.method_1328(class_5596.field_27382, class_290.field_1576);
      vertexBuffer.method_22918(matrix, x1, y2, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x2, y2, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x2, y1, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x1, y1, 0.0F).method_22915(r, g, b, a).method_1344();
      tessellator.method_1350();
   }
}
