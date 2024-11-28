package xaero.common.graphics.renderer.multitexture;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import net.minecraft.class_1921;
import net.minecraft.class_285;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_293;
import net.minecraft.class_287.class_7433;
import net.minecraft.class_293.class_5596;

public class MultiTextureRenderTypeRenderer {
   private boolean used;
   private class_287 bufferBuilder = new class_287(256);
   private List<class_7433> buffersForDrawCalls = new ArrayList<>();
   private IntArrayList texturesForDrawCalls = new IntArrayList();
   private IntConsumer textureBinderShader;
   private IntConsumer textureBinder;
   private Runnable textureFinalizer;
   private int prevTextureId;
   private class_293 prevFormat;
   private class_1921 renderType;

   MultiTextureRenderTypeRenderer() {
   }

   void init(IntConsumer textureBinderShader, IntConsumer textureBinder, Runnable textureFinalizer, class_1921 renderType) {
      if (this.used) {
         throw new IllegalStateException("Multi-texture renderer already in use!");
      } else {
         this.used = true;
         this.textureBinderShader = textureBinderShader;
         this.textureBinder = textureBinder;
         this.textureFinalizer = textureFinalizer;
         this.prevTextureId = -1;
         this.renderType = renderType;
      }
   }

   void draw() {
      if (!this.used) {
         throw new IllegalStateException("Multi-texture renderer is not in use!");
      } else {
         if (!this.texturesForDrawCalls.isEmpty()) {
            class_287 bufferBuilder = this.bufferBuilder;
            IntConsumer textureBinder = this.textureBinder;
            IntConsumer textureBinderShader = this.textureBinderShader;
            Runnable textureFinalizer = this.textureFinalizer;
            boolean hasTextureFinalizer = textureFinalizer != null;
            this.renderType.method_23516();
            this.endBuffer(bufferBuilder);
            boolean first = true;
            int shaderProgram = RenderSystem.getShader().method_1270();

            for (int i = 0; i < this.texturesForDrawCalls.size(); i++) {
               int texture = this.texturesForDrawCalls.getInt(i);
               class_7433 buffer = this.buffersForDrawCalls.get(i);
               if (texture == -1) {
                  texture = 0;
               }

               if (first) {
                  textureBinderShader.accept(texture);
                  class_286.method_43433(buffer);
                  class_285.method_22094(shaderProgram);
               } else {
                  textureBinder.accept(texture);
                  class_286.method_43437(buffer);
               }

               if (hasTextureFinalizer) {
                  if (first) {
                     textureBinder.accept(texture);
                  }

                  textureFinalizer.run();
               }

               first = false;
            }

            textureBinder.accept(0);
            this.renderType.method_23518();
         }

         class_285.method_22094(0);
         this.texturesForDrawCalls.clear();
         this.buffersForDrawCalls.clear();
         this.used = false;
         this.renderType = null;
      }
   }

   private void endBuffer(class_287 builder) {
      this.buffersForDrawCalls.add(builder.method_1326());
   }

   public class_287 begin(class_5596 mode, class_293 format, int textureId) {
      if (!this.used) {
         throw new IllegalStateException("Multi-texture renderer is not in use!");
      } else if (textureId == -1) {
         throw new IllegalStateException("Attempted to use the multi-texture renderer with texture id -1!");
      } else {
         if (textureId != this.prevTextureId || !Objects.equals(this.prevFormat, format)) {
            if (this.prevTextureId != -1) {
               this.endBuffer(this.bufferBuilder);
            }

            this.bufferBuilder.method_1328(mode, format);
            this.prevTextureId = textureId;
            this.prevFormat = format;
            this.texturesForDrawCalls.add(textureId);
         }

         return this.bufferBuilder;
      }
   }
}
