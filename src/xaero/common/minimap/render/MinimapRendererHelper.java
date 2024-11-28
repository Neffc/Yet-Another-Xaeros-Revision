package xaero.common.minimap.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import net.minecraft.class_276;
import net.minecraft.class_277;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_757;
import net.minecraft.class_8251;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.shader.MinimapShaders;
import xaero.common.graphics.shader.PositionTexAlphaTestShader;
import xaero.common.misc.Misc;

public class MinimapRendererHelper {
   private static class_277 defaultShaderDisabledBlendState = new class_277();
   private static class_277 defaultShaderBlendState = new class_277(770, 771, 32774);

   public void drawMyTexturedModalRect(
      class_4587 matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor
   ) {
      this.drawMyTexturedModalRect(matrixStack, x, y, textureX, textureY, width, height, theight, factor, 0.0F, true);
   }

   public void drawMyTexturedModalRect(
      class_4587 matrixStack,
      float x,
      float y,
      int textureX,
      int textureY,
      float width,
      float height,
      float theight,
      float factor,
      float discardAlpha,
      boolean blend
   ) {
      if (discardAlpha < 0.0F) {
         RenderSystem.setShader(blend ? () -> MinimapShaders.POSITION_TEX_NO_ALPHA_TEST : () -> MinimapShaders.POSITION_TEX_NO_ALPHA_TEST_NO_BLEND);
      } else {
         RenderSystem.setShader(blend ? () -> MinimapShaders.POSITION_TEX_ALPHA_TEST : () -> MinimapShaders.POSITION_TEX_ALPHA_TEST_NO_BLEND);
         ((PositionTexAlphaTestShader)RenderSystem.getShader()).setDiscardAlpha(discardAlpha);
      }

      this.drawMyTexturedModalRectInternal(matrixStack, x, y, textureX, textureY, width, height, theight, factor);
   }

   public void drawIconOutline(
      class_4587 matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor, float discardAlpha
   ) {
      MinimapShaders.POSITION_TEX_ICON_OUTLINE.setDiscardAlpha(discardAlpha);
      RenderSystem.setShader(() -> MinimapShaders.POSITION_TEX_ICON_OUTLINE);
      this.drawMyTexturedModalRectInternal(matrixStack, x, y, textureX, textureY, width, height, theight, factor);
   }

   private void drawMyTexturedModalRectInternal(
      class_4587 matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor
   ) {
      float f = 1.0F / factor;
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      class_289 tessellator = class_289.method_1348();
      class_287 vertexBuffer = tessellator.method_1349();
      vertexBuffer.method_1328(class_5596.field_27382, class_290.field_1585);
      vertexBuffer.method_22918(matrix, x + 0.0F, y + height, 0.0F).method_22913((float)(textureX + 0) * f, (float)(textureY + 0) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(((float)textureX + width) * f, (float)(textureY + 0) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + 0.0F, 0.0F).method_22913(((float)textureX + width) * f, ((float)textureY + theight) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + 0.0F, y + 0.0F, 0.0F).method_22913((float)(textureX + 0) * f, ((float)textureY + theight) * f).method_1344();
      tessellator.method_1350();
   }

   public void prepareMyTexturedColoredModalRect(
      Matrix4f matrix,
      float x,
      float y,
      int textureX,
      int textureY,
      float width,
      float height,
      float theight,
      float factor,
      int textureId,
      float r,
      float g,
      float b,
      float a,
      MultiTextureRenderTypeRenderer renderer
   ) {
      float f = 1.0F / factor;
      class_287 vertexBuffer = renderer.begin(textureId);
      vertexBuffer.method_22918(matrix, x + 0.0F, y + height, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)(textureX + 0) * f, (float)(textureY + 0) * f)
         .method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + height, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913(((float)textureX + width) * f, (float)(textureY + 0) * f)
         .method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + 0.0F, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913(((float)textureX + width) * f, ((float)textureY + theight) * f)
         .method_1344();
      vertexBuffer.method_22918(matrix, x + 0.0F, y + 0.0F, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)(textureX + 0) * f, ((float)textureY + theight) * f)
         .method_1344();
   }

   public void prepareMyTexturedModalRect(
      Matrix4f matrix,
      float x,
      float y,
      int textureX,
      int textureY,
      float width,
      float height,
      float theight,
      float factor,
      int textureId,
      MultiTextureRenderTypeRenderer renderer
   ) {
      float f = 1.0F / factor;
      class_287 vertexBuffer = renderer.begin(textureId);
      vertexBuffer.method_22918(matrix, x + 0.0F, y + height, 0.0F).method_22913((float)(textureX + 0) * f, (float)(textureY + 0) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(((float)textureX + width) * f, (float)(textureY + 0) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + width, y + 0.0F, 0.0F).method_22913(((float)textureX + width) * f, ((float)textureY + theight) * f).method_1344();
      vertexBuffer.method_22918(matrix, x + 0.0F, y + 0.0F, 0.0F).method_22913((float)(textureX + 0) * f, ((float)textureY + theight) * f).method_1344();
   }

   void drawTexturedElipseInsideRectangle(
      class_4587 matrixStack, double startAngle, int sides, float x, float y, int textureX, int textureY, float width, float widthFactor
   ) {
      this.drawTexturedElipseInsideRectangle(matrixStack, startAngle, sides, x, y, textureX, textureY, width, width, widthFactor);
   }

   void drawTexturedElipseInsideRectangle(
      class_4587 matrixStack, double startAngle, int sides, float x, float y, int textureX, int textureY, float width, float theight, float widthFactor
   ) {
      float f = 1.0F / widthFactor;
      float f1 = f;
      RenderSystem.setShader(class_757::method_34542);
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      class_289 tessellator = class_289.method_1348();
      class_287 vertexBuffer = tessellator.method_1349();
      float halfWidth = width / 2.0F;
      double centerX = (double)(x + halfWidth);
      double centerY = (double)(y + halfWidth);
      float centerU = ((float)textureX + halfWidth) * f;
      float centerV = (float)(((double)textureY + (double)theight * 0.5) * (double)f);
      double fullCircle = Math.PI * 2;
      float prevVertexLocalX = 0.0F;
      float prevVertexLocalY = 0.0F;
      float prevVertexLocalV = 0.0F;
      vertexBuffer.method_1328(class_5596.field_27379, class_290.field_1585);

      for (int i = 0; i <= sides; i++) {
         double angle = startAngle + (double)i / (double)sides * fullCircle;
         double sin = Math.sin(angle);
         double cos = Math.cos(angle);
         float vertexLocalX = halfWidth + (float)((double)halfWidth * sin);
         float vertexLocalY = (float)((double)halfWidth * (1.0 - cos));
         float vertexLocalV = (float)((double)theight * (1.0 - 0.5 * (1.0 - cos)));
         if (i > 0) {
            vertexBuffer.method_22918(matrix, x + vertexLocalX, y + vertexLocalY, 0.0F)
               .method_22913(((float)textureX + vertexLocalX) * f, ((float)textureY + vertexLocalV) * f1)
               .method_1344();
            vertexBuffer.method_22918(matrix, x + prevVertexLocalX, y + prevVertexLocalY, 0.0F)
               .method_22913(((float)textureX + prevVertexLocalX) * f, ((float)textureY + prevVertexLocalV) * f1)
               .method_1344();
            vertexBuffer.method_22918(matrix, (float)centerX, (float)centerY, 0.0F).method_22913(centerU, centerV).method_1344();
         }

         prevVertexLocalX = vertexLocalX;
         prevVertexLocalY = vertexLocalY;
         prevVertexLocalV = vertexLocalV;
      }

      tessellator.method_1350();
   }

   void drawTexturedElipseInsideRectangleFrame(
      class_4587 matrixStack,
      boolean resetTexture,
      boolean reverseTexture,
      double startAngle,
      int startIndex,
      int endIndex,
      int sides,
      float thickness,
      float x,
      float y,
      int textureX,
      int textureY,
      float width,
      float twidth,
      float theight,
      int seamWidth,
      float widthFactor
   ) {
      float f = 1.0F / widthFactor;
      float f1 = f;
      RenderSystem.setShader(class_757::method_34542);
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      class_289 tessellator = class_289.method_1348();
      class_287 vertexBuffer = tessellator.method_1349();
      float halfWidth = width / 2.0F;
      double fullCircle = Math.PI * 2;
      float prevVertexLocalX = 0.0F;
      float prevVertexLocalY = 0.0F;
      float prevVertexLocalOuterX = 0.0F;
      float prevVertexLocalOuterY = 0.0F;
      float prevSegmentTextureX = 0.0F;
      vertexBuffer.method_1328(class_5596.field_27382, class_290.field_1585);
      float outerRadius = halfWidth + thickness;
      float segmentOuterWidth = (float)(fullCircle / (double)sides * (double)outerRadius);
      startIndex = Math.max(Math.min(startIndex, sides), 0);
      endIndex = Math.max(Math.min(endIndex, sides), startIndex);
      int textureStartIndex = resetTexture ? (reverseTexture ? endIndex : startIndex) : 0;
      float seamThreshold = reverseTexture ? (float)seamWidth + segmentOuterWidth : (float)seamWidth;

      for (int i = startIndex; i <= endIndex; i++) {
         double angle = startAngle + (double)i / (double)sides * fullCircle;
         double sin = Math.sin(angle);
         double cos = Math.cos(angle);
         float vertexLocalX = halfWidth + (float)((double)halfWidth * sin);
         float vertexLocalY = (float)((double)halfWidth * (1.0 - cos));
         float vertexLocalOuterX = halfWidth + (float)((double)outerRadius * sin);
         float vertexLocalOuterY = (float)((double)halfWidth - (double)outerRadius * cos);
         float segmentTextureStartX = (float)textureX;
         float offsetX = Math.abs(segmentOuterWidth * (float)(i - textureStartIndex));
         if (offsetX >= seamThreshold) {
            segmentTextureStartX = (float)(textureX + seamWidth);
            offsetX -= seamThreshold;
            if (offsetX >= twidth) {
               offsetX %= twidth;
            }
         }

         float segmentTextureX = segmentTextureStartX + offsetX;
         if (i > startIndex) {
            vertexBuffer.method_22918(matrix, x + prevVertexLocalX, y + prevVertexLocalY, 0.0F)
               .method_22913(prevSegmentTextureX * f, ((float)textureY + theight) * f1)
               .method_1344();
            vertexBuffer.method_22918(matrix, x + vertexLocalX, y + vertexLocalY, 0.0F)
               .method_22913(segmentTextureX * f, ((float)textureY + theight) * f1)
               .method_1344();
            vertexBuffer.method_22918(matrix, x + vertexLocalOuterX, y + vertexLocalOuterY, 0.0F)
               .method_22913(segmentTextureX * f, (float)textureY * f1)
               .method_1344();
            vertexBuffer.method_22918(matrix, x + prevVertexLocalOuterX, y + prevVertexLocalOuterY, 0.0F)
               .method_22913(prevSegmentTextureX * f, (float)textureY * f1)
               .method_1344();
         }

         prevVertexLocalX = vertexLocalX;
         prevVertexLocalY = vertexLocalY;
         prevVertexLocalOuterX = vertexLocalOuterX;
         prevVertexLocalOuterY = vertexLocalOuterY;
         prevSegmentTextureX = segmentTextureX;
      }

      tessellator.method_1350();
   }

   public void addTexturedRectToExistingBuffer(Matrix4f matrix, class_4588 vertexBuffer, float x, float y, int u, int v, int w, int h) {
      float f = 0.00390625F;
      float normalizedU1 = (float)u * f;
      float normalizedV1 = (float)v * f;
      float normalizedU2 = (float)(u + w) * f;
      float normalizedV2 = (float)(v + h) * f;
      vertexBuffer.method_22918(matrix, x, y + (float)h, 0.0F).method_22913(normalizedU1, normalizedV2).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y + (float)h, 0.0F).method_22913(normalizedU2, normalizedV2).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y, 0.0F).method_22913(normalizedU2, normalizedV1).method_1344();
      vertexBuffer.method_22918(matrix, x, y, 0.0F).method_22913(normalizedU1, normalizedV1).method_1344();
   }

   public void addTexturedColoredRectToExistingBuffer(
      Matrix4f matrix, class_4588 vertexBuffer, float x, float y, int u, int v, int w, int h, float r, float g, float b, float a, float factor
   ) {
      this.addTexturedColoredRectToExistingBuffer(matrix, vertexBuffer, x, y, u, v, w, h, w, h, r, g, b, a, factor);
   }

   public void addTexturedColoredRectToExistingBuffer(
      Matrix4f matrix, class_4588 vertexBuffer, float x, float y, int u, int v, int w, int h, int tw, int th, float r, float g, float b, float a, float factor
   ) {
      float f = 1.0F / factor;
      float normalizedU1 = (float)u * f;
      float normalizedV1 = (float)v * f;
      float normalizedU2 = (float)(u + tw) * f;
      float normalizedV2 = (float)(v + th) * f;
      vertexBuffer.method_22918(matrix, x, y + (float)h, 0.0F).method_22915(r, g, b, a).method_22913(normalizedU1, normalizedV1).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y + (float)h, 0.0F).method_22915(r, g, b, a).method_22913(normalizedU2, normalizedV1).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y, 0.0F).method_22915(r, g, b, a).method_22913(normalizedU2, normalizedV2).method_1344();
      vertexBuffer.method_22918(matrix, x, y, 0.0F).method_22915(r, g, b, a).method_22913(normalizedU1, normalizedV2).method_1344();
   }

   public void addColoredRectToExistingBuffer(Matrix4f matrix, class_4588 vertexBuffer, float x, float y, int w, int h, int color) {
      float a = (float)(color >> 24 & 0xFF) / 255.0F;
      float r = (float)(color >> 16 & 0xFF) / 255.0F;
      float g = (float)(color >> 8 & 0xFF) / 255.0F;
      float b = (float)(color & 0xFF) / 255.0F;
      this.addColoredRectToExistingBuffer(matrix, vertexBuffer, x, y, w, h, r, g, b, a);
   }

   public void addColoredRectToExistingBuffer(Matrix4f matrix, class_4588 vertexBuffer, float x, float y, int w, int h, float r, float g, float b, float a) {
      vertexBuffer.method_22918(matrix, x, y + (float)h, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y + (float)h, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x + (float)w, y, 0.0F).method_22915(r, g, b, a).method_1344();
      vertexBuffer.method_22918(matrix, x, y, 0.0F).method_22915(r, g, b, a).method_1344();
   }

   public void drawMyColoredRect(class_4587 matrixStack, float x1, float y1, float x2, float y2) {
      RenderSystem.setShader(class_757::method_34539);
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      class_289 tessellator = class_289.method_1348();
      class_287 vertexBuffer = tessellator.method_1349();
      vertexBuffer.method_1328(class_5596.field_27382, class_290.field_1592);
      vertexBuffer.method_22918(matrix, x1, y2, 0.0F).method_1344();
      vertexBuffer.method_22918(matrix, x2, y2, 0.0F).method_1344();
      vertexBuffer.method_22918(matrix, x2, y1, 0.0F).method_1344();
      vertexBuffer.method_22918(matrix, x1, y1, 0.0F).method_1344();
      tessellator.method_1350();
   }

   public void addColoredLineToExistingBuffer(
      class_4665 matrices, class_4588 vertexBuffer, float x1, float y1, float x2, float y2, float r, float g, float b, float a
   ) {
      vertexBuffer.method_22918(matrices.method_23761(), x1, y1, 0.0F)
         .method_22915(r, g, b, a)
         .method_23763(matrices.method_23762(), x2 - x1, y2 - y1, 0.0F)
         .method_1344();
      vertexBuffer.method_22918(matrices.method_23761(), x2, y2, 0.0F)
         .method_22915(r, g, b, a)
         .method_23763(matrices.method_23762(), x2 - x1, y2 - y1, 0.0F)
         .method_1344();
   }

   public void drawMyColoredRect(Matrix4f matrix, float x1, float y1, float x2, float y2, int color) {
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

   void bindTextureBuffer(ByteBuffer image, int width, int height, int par0) {
      GlStateManager._bindTexture(par0);
      GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, image);
      RenderSystem.setShaderTexture(0, par0);
   }

   void putColor(byte[] bytes, int x, int y, int red, int green, int blue, int size) {
      int pixel = (y * size + x) * 3;
      bytes[pixel] = (byte)red;
      pixel++;
      bytes[pixel] = (byte)green;
      pixel++;
      bytes[pixel] = (byte)blue;
   }

   void gridOverlay(int[] result, int grid, int red, int green, int blue) {
      result[0] = (red * 3 + (grid >> 16 & 0xFF)) / 4;
      result[1] = (green * 3 + (grid >> 8 & 0xFF)) / 4;
      result[2] = (blue * 3 + (grid & 0xFF)) / 4;
   }

   void slimeOverlay(int[] result, int red, int green, int blue) {
      result[0] = (red + 82) / 2;
      result[1] = (green + 241) / 2;
      result[2] = (blue + 64) / 2;
   }

   public void defaultOrtho(class_276 framebuffer, boolean raw) {
      if (framebuffer != null) {
         Matrix4f ortho = new Matrix4f().setOrtho(0.0F, (float)framebuffer.field_1482, (float)framebuffer.field_1481, 0.0F, 1000.0F, 21000.0F);
         RenderSystem.setProjectionMatrix(ortho, class_8251.field_43361);
      } else {
         Misc.minecraftOrtho(class_310.method_1551(), raw);
      }
   }

   public static void restoreDefaultShaderBlendState() {
      defaultShaderDisabledBlendState.method_1244();
      defaultShaderBlendState.method_1244();
      RenderSystem.defaultBlendFunc();
   }
}
