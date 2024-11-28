package xaero.common.minimap.radar.tracker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1068;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;
import net.minecraft.class_742;
import net.minecraft.class_4597.class_4598;
import xaero.common.AXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;

public final class PlayerTrackerMinimapElementRenderer extends MinimapElementRenderer<PlayerTrackerMinimapElement<?>, PlayerTrackerMinimapElementRenderContext> {
   private final PlayerTrackerMinimapElementCollector elementCollector;
   private final PlayerTrackerIconRenderer playerTrackerIconRenderer;
   private final AXaeroMinimap modMain;

   private PlayerTrackerMinimapElementRenderer(
      PlayerTrackerMinimapElementCollector elementCollector,
      AXaeroMinimap modMain,
      PlayerTrackerMinimapElementRenderContext context,
      PlayerTrackerMinimapElementRenderProvider<PlayerTrackerMinimapElementRenderContext> provider,
      PlayerTrackerMinimapElementReader reader,
      PlayerTrackerIconRenderer playerTrackerIconRenderer
   ) {
      super(reader, provider, context);
      this.elementCollector = elementCollector;
      this.modMain = modMain;
      this.playerTrackerIconRenderer = playerTrackerIconRenderer;
   }

   public class_2960 getPlayerSkin(class_1657 player, class_640 info) {
      class_2960 skinTextureLocation = player instanceof class_742 ? ((class_742)player).method_3117() : info.method_2968();
      if (skinTextureLocation == null) {
         skinTextureLocation = class_1068.method_4648(player.method_5667());
      }

      return skinTextureLocation;
   }

   @Override
   public void preRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      AXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider rendererProvider
   ) {
      this.context.outlineConsumer = renderTypeBuffers.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.context.uniqueTextureUIObjectRenderer = rendererProvider.getRenderer(
         t -> RenderSystem.setShaderTexture(0, t), MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_NEAREST
      );
      if (modMain.getSupportMods().worldmap()) {
         this.context.mapDimId = modMain.getSupportMods().worldmapSupport.getMapDimension();
         this.context.mapDimDiv = modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().getLastPlayerDimDiv();
      } else {
         this.context.mapDimId = class_310.method_1551().field_1687.method_27983();
         this.context.mapDimDiv = 1.0;
      }
   }

   @Override
   public void postRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      AXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider rendererProvider
   ) {
      rendererProvider.draw(this.context.uniqueTextureUIObjectRenderer);
      renderTypeBuffers.method_22993();
      this.elementCollector.resetRenderedOnRadarFlags();
   }

   public boolean renderElement(
      int location,
      boolean highlit,
      boolean outOfBounds,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers,
      class_327 font,
      class_276 framebuffer,
      MinimapRendererHelper helper,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      int elementIndex,
      double optionalDepth,
      float optionalScale,
      PlayerTrackerMinimapElement<?> e,
      double partialX,
      double partialY,
      boolean cave,
      float partialTicks
   ) {
      if (!outOfBounds && e.wasRenderedOnRadar()) {
         return false;
      } else {
         class_4587 matrixStack = guiGraphics.method_51448();
         class_310 mc = class_310.method_1551();
         class_640 info = mc.method_1562().method_2871(e.getPlayerId());
         if (info != null) {
            class_1657 clientPlayer = mc.field_1687.method_18470(e.getPlayerId());
            matrixStack.method_22903();
            matrixStack.method_22904(0.0, 0.0, optionalDepth);
            matrixStack.method_22905(optionalScale, optionalScale, 1.0F);
            helper.addColoredRectToExistingBuffer(
               matrixStack.method_23760().method_23761(), this.context.outlineConsumer, -5.0F, -5.0F, 10, 10, 1.0F, 1.0F, 1.0F, 1.0F
            );
            matrixStack.method_22904(0.0, 0.0, 0.01);
            this.playerTrackerIconRenderer
               .renderIcon(mc, this.context.uniqueTextureUIObjectRenderer, helper, matrixStack, clientPlayer, this.getPlayerSkin(clientPlayer, info));
            matrixStack.method_22909();
         }

         return false;
      }
   }

   @Override
   public boolean shouldRender(int location) {
      return this.modMain.getSettings().displayTrackedPlayers;
   }

   @Override
   public int getOrder() {
      return 100;
   }

   public PlayerTrackerMinimapElementCollector getCollector() {
      return this.elementCollector;
   }

   public static final class Builder {
      private final AXaeroMinimap modMain;

      private Builder(AXaeroMinimap modMain) {
         this.modMain = modMain;
      }

      private PlayerTrackerMinimapElementRenderer.Builder setDefault() {
         return this;
      }

      public PlayerTrackerMinimapElementRenderer build() {
         PlayerTrackerMinimapElementCollector collector = new PlayerTrackerMinimapElementCollector(this.modMain.getPlayerTrackerSystemManager());
         return new PlayerTrackerMinimapElementRenderer(
            collector,
            this.modMain,
            new PlayerTrackerMinimapElementRenderContext(),
            new PlayerTrackerMinimapElementRenderProvider<>(collector),
            new PlayerTrackerMinimapElementReader(),
            new PlayerTrackerIconRenderer()
         );
      }

      public static PlayerTrackerMinimapElementRenderer.Builder begin(AXaeroMinimap modMain) {
         return new PlayerTrackerMinimapElementRenderer.Builder(modMain).setDefault();
      }
   }
}
