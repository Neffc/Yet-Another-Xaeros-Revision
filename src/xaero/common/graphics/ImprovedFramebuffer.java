package xaero.common.graphics;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.IntBuffer;
import net.minecraft.class_310;
import net.minecraft.class_6367;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import xaero.common.MinimapLogs;

public class ImprovedFramebuffer extends class_6367 {
   private int type;
   public int colorAttachment;
   private int depthAttachment;
   private boolean superConstructorWorks;
   private static boolean optifineChecked;
   private static boolean forceMainFBO;
   private static int forcedMainFBO;
   private static final int GL_FB_INCOMPLETE_ATTACHMENT = 36054;
   private static final int GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
   private static final int GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
   private static final int GL_FB_INCOMPLETE_READ_BUFFER = 36060;

   public ImprovedFramebuffer(int width, int height, boolean useDepthIn) {
      super(width, height, useDepthIn, class_310.field_1703);
      if (!this.superConstructorWorks) {
         this.method_1234(width, height, class_310.field_1703);
      }
   }

   public static void detectOptifineFBOs() {
      int actualResult = GL11.glGetInteger(36006);
      if (actualResult != class_310.method_1551().method_1522().field_1476) {
         MinimapLogs.LOGGER.info("(Minimap) Detected main FBO: " + actualResult);
         forceMainFBO = true;
         forcedMainFBO = actualResult;
      }
   }

   public void method_1234(int width, int height, boolean isMac) {
      if (!optifineChecked) {
         detectOptifineFBOs();
         optifineChecked = true;
      }

      this.superConstructorWorks = true;
      GlStateManager._enableDepthTest();
      if (this.field_1476 >= 0) {
         this.method_1238();
      }

      this.method_1231(width, height, isMac);
      beginWrite(this.type, 36160, 0);
   }

   public void method_1231(int width, int height, boolean isMac) {
      this.field_1480 = width;
      this.field_1477 = height;
      this.field_1482 = width;
      this.field_1481 = height;
      this.field_1476 = this.genFrameBuffers();
      if (this.field_1476 == -1) {
         this.method_1230(isMac);
      } else {
         this.field_1475 = TextureUtil.generateTextureId();
         if (this.field_1475 == -1) {
            this.method_1230(isMac);
         } else {
            if (this.field_1478) {
               this.field_1474 = this.genRenderbuffers();
               if (this.field_1474 == -1) {
                  this.method_1230(isMac);
                  return;
               }
            }

            this.method_1232(9728);
            GlStateManager._bindTexture(this.field_1475);
            GlStateManager._texImage2D(3553, 0, 32856, this.field_1482, this.field_1481, 0, 6408, 5121, (IntBuffer)null);
            beginWrite(this.type, 36160, this.field_1476);
            framebufferTexture2D(this.type, 36160, 36064, 3553, this.field_1475, 0);
            if (this.field_1478) {
               bindRenderbuffer(this.type, 36161, this.field_1474);
               renderbufferStorage(this.type, 36161, 33190, this.field_1482, this.field_1481);
               framebufferRenderbuffer(this.type, 36160, 36096, 36161, this.field_1474);
            }

            this.method_1239();
            this.method_1230(isMac);
            this.method_1242();
         }
      }
   }

   private int genFrameBuffers() {
      int fbo = -1;
      fbo = GlStateManager.glGenFramebuffers();
      this.type = 0;
      return fbo;
   }

   public int genRenderbuffers() {
      int rbo = -1;
      switch (this.type) {
         case 0:
            rbo = GlStateManager.glGenRenderbuffers();
         default:
            return rbo;
      }
   }

   public void method_1238() {
      this.method_1242();
      this.method_1240();
      if (this.field_1474 > -1) {
         this.deleteRenderbuffers(this.field_1474);
         this.field_1474 = -1;
      }

      if (this.field_1475 > -1) {
         TextureUtil.releaseTextureId(this.field_1475);
         this.field_1475 = -1;
      }

      if (this.field_1476 > -1) {
         beginWrite(this.type, 36160, 0);
         this.deleteFramebuffers(this.field_1476);
         this.field_1476 = -1;
      }
   }

   private void deleteFramebuffers(int framebufferIn) {
      switch (this.type) {
         case 0:
            GlStateManager._glDeleteFramebuffers(framebufferIn);
      }
   }

   private void deleteRenderbuffers(int renderbuffer) {
      switch (this.type) {
         case 0:
            GlStateManager._glDeleteRenderbuffers(renderbuffer);
      }
   }

   public void method_1239() {
      int i = this.checkFramebufferStatus(36160);
      if (i != 36053) {
         if (i == 36054) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
         } else if (i == 36055) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
         } else if (i == 36059) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
         } else if (i == 36060) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
         } else {
            throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
         }
      }
   }

   private int checkFramebufferStatus(int target) {
      switch (this.type) {
         case 0:
            return GlStateManager.glCheckFramebufferStatus(target);
         default:
            return -1;
      }
   }

   private static void beginWrite(int type, int target, int framebufferIn) {
      if (framebufferIn == -1) {
         framebufferIn = 0;
      }

      switch (type) {
         case 0:
            GlStateManager._glBindFramebuffer(target, framebufferIn);
      }
   }

   public void bindDefaultFramebuffer(class_310 mc) {
      beginWrite(this.getType(), 36160, forceMainFBO ? forcedMainFBO : mc.method_1522().field_1476);
      mc.method_1522().method_1235(false);
   }

   public static void framebufferTexture2D(int type, int target, int attachment, int textarget, int texture, int level) {
      switch (type) {
         case 0:
            GlStateManager._glFramebufferTexture2D(target, attachment, textarget, texture, level);
      }
   }

   public static void bindRenderbuffer(int type, int target, int renderbuffer) {
      switch (type) {
         case 0:
            GlStateManager._glBindRenderbuffer(target, renderbuffer);
      }
   }

   public static void renderbufferStorage(int type, int target, int internalFormat, int width, int height) {
      switch (type) {
         case 0:
            GlStateManager._glRenderbufferStorage(target, internalFormat, width, height);
      }
   }

   public static void framebufferRenderbuffer(int type, int target, int attachment, int renderBufferTarget, int renderBuffer) {
      switch (type) {
         case 0:
            GlStateManager._glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
      }
   }

   public void method_1235(boolean p_147610_1_) {
      beginWrite(this.type, 36160, this.field_1476);
      if (p_147610_1_) {
         GlStateManager._viewport(0, 0, this.field_1480, this.field_1477);
      }
   }

   public void method_1240() {
      beginWrite(this.type, 36160, 0);
   }

   public void method_35610() {
      GlStateManager._bindTexture(this.field_1475);
      RenderSystem.setShaderTexture(0, this.field_1475);
   }

   public void method_1242() {
      GlStateManager._bindTexture(0);
   }

   public void method_1232(int framebufferFilterIn) {
      this.field_1483 = framebufferFilterIn;
      GlStateManager._bindTexture(this.field_1475);
      GlStateManager._texParameter(3553, 10241, framebufferFilterIn);
      GlStateManager._texParameter(3553, 10240, framebufferFilterIn);
      GlStateManager._texParameter(3553, 10242, 33071);
      GlStateManager._texParameter(3553, 10243, 33071);
      GlStateManager._bindTexture(0);
   }

   public int getFramebufferTexture() {
      return this.field_1475;
   }

   public void setFramebufferTexture(int textureId) {
      if (textureId != this.field_1475) {
         this.field_1475 = textureId;
         if (textureId != 0) {
            framebufferTexture2D(this.type, 36160, 36064, 3553, this.field_1475, 0);
         }
      }
   }

   public void generateMipmaps() {
      switch (this.type) {
         case 0:
            GL30.glGenerateMipmap(3553);
      }
   }

   public int getType() {
      return this.type;
   }
}
