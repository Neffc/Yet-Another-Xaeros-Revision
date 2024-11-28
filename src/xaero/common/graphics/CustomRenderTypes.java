package xaero.common.graphics;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_4668;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4668.class_4671;
import net.minecraft.class_4668.class_4672;
import net.minecraft.class_4668.class_4675;
import net.minecraft.class_4668.class_4676;
import net.minecraft.class_4668.class_4677;
import net.minecraft.class_4668.class_4678;
import net.minecraft.class_4668.class_4679;
import net.minecraft.class_4668.class_4683;
import net.minecraft.class_4668.class_4684;
import net.minecraft.class_4668.class_4685;
import net.minecraft.class_4668.class_4686;
import net.minecraft.class_4668.class_5939;
import net.minecraft.class_4668.class_5942;
import xaero.common.graphics.shader.MinimapShaders;
import xaero.common.interfaces.render.InterfaceRenderer;

public class CustomRenderTypes extends class_1921 {
   private static final class_4678 KEEP_TARGET = new class_4678("xaero_keep_target", () -> {
   }, () -> {
   });
   protected static final class_4685 TRANSLUCENT_TRANSPARENCY = new class_4685("xaero_translucent_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final class_4685 LINES_TRANSPARENCY = new class_4685("xaero_lines_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final class_4685 PREMULTIPLIED_TRANSPARENCY = new class_4685("xaero_wm_premultiplied_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final class_4675 POLYGON_OFFSET_LAYERING = new class_4675("xaero_polygon_offset_layering", () -> {
      RenderSystem.polygonOffset(0.0F, 10.0F);
      RenderSystem.enablePolygonOffset();
   }, () -> {
      RenderSystem.polygonOffset(0.0F, 0.0F);
      RenderSystem.disablePolygonOffset();
   });
   public static final class_1921 GUI_BILINEAR;
   public static final class_1921 GUI_BILINEAR_PREMULTIPLIED;
   public static final class_1921 GUI_NEAREST;
   public static final class_1921 COLORED_WAYPOINTS_BGS;
   public static final class_1921 MAP_CHUNK_OVERLAY;
   public static final class_1921 MAP_LINES;
   public static final class_1921 RADAR_NAME_BGS;

   public static class_1921 entityIconRenderType(class_2960 texture, CustomRenderTypes.EntityIconLayerPhases layerPhases) {
      ImmutableList<class_4668> rendertype$state = new CustomRenderTypes.MultiPhaseBuilder()
         .texture(layerPhases.texture)
         .transparency(layerPhases.transparency)
         .shader(layerPhases.shader)
         .depthTest(layerPhases.depthTest)
         .writeMaskState(layerPhases.writeMask)
         .cull(layerPhases.cull)
         .lightmap(field_21383)
         .overlay(field_21385)
         .target(KEEP_TARGET)
         .build();
      return new CustomRenderTypes.MultiPhaseRenderType("xaero_entity_icon", class_290.field_1580, class_5596.field_27382, 256, true, true, rendertype$state);
   }

   private CustomRenderTypes(
      String name,
      class_293 vertexFormat,
      class_5596 drawMode,
      int expectedBufferSize,
      boolean hasCrumbling,
      boolean translucent,
      Runnable startAction,
      Runnable endAction
   ) {
      super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
   }

   public static class_4685 getTransparencyPhase(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
      return new class_4685("xaero_custom_transparency", () -> {
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
      }, () -> {
         RenderSystem.disableBlend();
         RenderSystem.defaultBlendFunc();
      });
   }

   public static CustomRenderTypes.EntityIconLayerPhases getBasicEntityIconLayerPhases(class_2960 texture) {
      return new CustomRenderTypes.EntityIconLayerPhases(
         new class_4683(texture, false, false), TRANSLUCENT_TRANSPARENCY, field_21348, field_21349, field_21345, field_29407
      );
   }

   static {
      ImmutableList<class_4668> multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .texture(new class_4683(InterfaceRenderer.guiTextures, false, false))
         .transparency(TRANSLUCENT_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR_TEX))
         .cull(field_21345)
         .target(KEEP_TARGET)
         .build();
      GUI_NEAREST = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_gui_nearest", class_290.field_20887, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .texture(new class_4683(InterfaceRenderer.guiTextures, true, false))
         .transparency(TRANSLUCENT_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR_TEX))
         .cull(field_21345)
         .target(KEEP_TARGET)
         .build();
      GUI_BILINEAR = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_gui_bilinear", class_290.field_20887, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .texture(new class_4683(InterfaceRenderer.guiTextures, true, false))
         .transparency(PREMULTIPLIED_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR_TEX_PRE))
         .cull(field_21345)
         .target(KEEP_TARGET)
         .build();
      GUI_BILINEAR_PREMULTIPLIED = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_gui_bilinear_pre", class_290.field_20887, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .transparency(TRANSLUCENT_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR))
         .target(KEEP_TARGET)
         .layering(POLYGON_OFFSET_LAYERING)
         .build();
      COLORED_WAYPOINTS_BGS = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_colored_waypoints", class_290.field_1576, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .transparency(TRANSLUCENT_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR))
         .target(KEEP_TARGET)
         .layering(POLYGON_OFFSET_LAYERING)
         .build();
      RADAR_NAME_BGS = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_radar_name_bg", class_290.field_1576, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .transparency(TRANSLUCENT_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.POSITION_COLOR))
         .target(KEEP_TARGET)
         .build();
      MAP_CHUNK_OVERLAY = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_chunk_overlay", class_290.field_1576, class_5596.field_27382, 256, false, false, multiPhaseParameters
      );
      multiPhaseParameters = new CustomRenderTypes.MultiPhaseBuilder()
         .transparency(LINES_TRANSPARENCY)
         .shader(new class_5942(() -> MinimapShaders.FRAMEBUFFER_LINES))
         .target(KEEP_TARGET)
         .build();
      MAP_LINES = new CustomRenderTypes.MultiPhaseRenderType(
         "xaero_lines", class_290.field_29337, class_5596.field_27377, 256, false, false, multiPhaseParameters
      );
   }

   public static class EntityIconLayerPhases {
      public class_4683 texture;
      public class_4685 transparency;
      public class_4672 depthTest;
      public class_4686 writeMask;
      public class_4671 cull;
      public class_5942 shader;

      public EntityIconLayerPhases(Object texture, Object transparency, Object depthTest, Object writeMask, Object cull, Object shader) {
         this.texture = (class_4683)texture;
         this.transparency = (class_4685)transparency;
         this.depthTest = (class_4672)depthTest;
         this.writeMask = (class_4686)writeMask;
         this.cull = (class_4671)cull;
         this.shader = (class_5942)shader;
      }
   }

   static class MultiPhaseBuilder extends class_4668 {
      private class_5939 texture = class_4668.field_21378;
      private class_5942 shader = class_4668.field_29434;
      private class_4685 transparency = class_4668.field_21364;
      private class_4672 depthTest = class_4668.field_21348;
      private class_4671 cull = class_4668.field_21344;
      private class_4676 lightmap = class_4668.field_21384;
      private class_4679 overlay = class_4668.field_21386;
      private class_4675 layering = class_4668.field_21352;
      private class_4678 target = class_4668.field_21358;
      private class_4684 texturing = class_4668.field_21379;
      private class_4686 writeMaskState = class_4668.field_21349;
      private class_4677 lineWidth = class_4668.field_21360;

      MultiPhaseBuilder() {
         super("weird access error workaround", null, null);
      }

      public CustomRenderTypes.MultiPhaseBuilder texture(class_5939 texture) {
         this.texture = texture;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder shader(class_5942 shader) {
         this.shader = shader;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder transparency(class_4685 transparency) {
         this.transparency = transparency;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder depthTest(class_4672 depthTest) {
         this.depthTest = depthTest;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder cull(class_4671 cull) {
         this.cull = cull;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder lightmap(class_4676 lightmap) {
         this.lightmap = lightmap;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder overlay(class_4679 overlay) {
         this.overlay = overlay;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder layering(class_4675 layering) {
         this.layering = layering;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder target(class_4678 target) {
         this.target = target;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder texturing(class_4684 texturing) {
         this.texturing = texturing;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder writeMaskState(class_4686 writeMaskState) {
         this.writeMaskState = writeMaskState;
         return this;
      }

      public CustomRenderTypes.MultiPhaseBuilder lineWidth(class_4677 lineWidth) {
         this.lineWidth = lineWidth;
         return this;
      }

      public ImmutableList<class_4668> build() {
         return ImmutableList.of(
            this.texture,
            this.shader,
            this.transparency,
            this.depthTest,
            this.cull,
            this.lightmap,
            this.overlay,
            this.layering,
            this.target,
            this.texturing,
            this.writeMaskState,
            this.lineWidth,
            new class_4668[0]
         );
      }
   }

   private static class MultiPhaseRenderType extends CustomRenderTypes {
      public MultiPhaseRenderType(
         String name,
         class_293 vertexFormat,
         class_5596 drawMode,
         int expectedBufferSize,
         boolean hasCrumbling,
         boolean translucent,
         ImmutableList<class_4668> phases
      ) {
         super(
            name,
            vertexFormat,
            drawMode,
            expectedBufferSize,
            hasCrumbling,
            translucent,
            () -> phases.forEach(class_4668::method_23516),
            () -> phases.forEach(class_4668::method_23518)
         );
      }
   }
}
