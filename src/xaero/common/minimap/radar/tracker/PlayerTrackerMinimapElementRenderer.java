package xaero.common.minimap.radar.tracker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1068;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;
import net.minecraft.class_742;
import net.minecraft.class_327.class_6415;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.entity.EntityUtils;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;

public final class PlayerTrackerMinimapElementRenderer extends MinimapElementRenderer<PlayerTrackerMinimapElement<?>, PlayerTrackerMinimapElementRenderContext> {
   private final double WORLD_MINIMUM_DISTANCE = 10.0;
   private final double WORLD_FADING_LENGTH = 10.0;
   private class_4598 minimapBufferSource;
   private final PlayerTrackerMinimapElementCollector elementCollector;
   private final PlayerTrackerIconRenderer playerTrackerIconRenderer;
   private final IXaeroMinimap modMain;
   private float nameScale;

   private PlayerTrackerMinimapElementRenderer(
      PlayerTrackerMinimapElementCollector elementCollector,
      IXaeroMinimap modMain,
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
   public void preRender(MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider rendererProvider) {
      RenderSystem.disableDepthTest();
      vanillaBufferSource.method_22993();
      this.minimapBufferSource = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
      this.context.coloredBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.context.uniqueTextureUIObjectRenderer = rendererProvider.getRenderer(
         t -> RenderSystem.setShaderTexture(0, t), MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_NEAREST
      );
      this.context.renderEntityDimId = renderInfo.renderEntityDimension;
      this.context.mapDimId = renderInfo.mapDimension;
      this.context.helper = this.modMain.getMinimap().getMinimapFBORenderer().getHelper();
      this.context.iconScale = renderInfo.location == MinimapElementRenderLocation.IN_WORLD
         ? HudMod.INSTANCE.getSettings().getTrackedPlayerWorldIconScale()
         : HudMod.INSTANCE.getSettings().getTrackedPlayerMinimapIconScale();
      this.nameScale = HudMod.INSTANCE.getSettings().getTrackedPlayerWorldNameScale();
   }

   @Override
   public void postRender(MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider rendererProvider) {
      rendererProvider.draw(this.context.uniqueTextureUIObjectRenderer);
      this.minimapBufferSource.method_22993();
      RenderSystem.enableDepthTest();
      RenderSystem.depthFunc(515);
      this.elementCollector.resetRenderedOnRadarFlags();
   }

   public boolean renderElement(
      PlayerTrackerMinimapElement<?> e,
      boolean highlighted,
      boolean outOfBounds,
      double optionalDepth,
      float optionalScale,
      double partialX,
      double partialY,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 vanillaBufferSource
   ) {
      if (!outOfBounds && renderInfo.location != MinimapElementRenderLocation.IN_WORLD && e.wasRenderedOnRadar()) {
         return false;
      } else {
         class_4587 matrixStack = guiGraphics.method_51448();
         class_310 mc = class_310.method_1551();
         class_640 info = mc.method_1562().method_2871(e.getPlayerId());
         if (info != null) {
            class_1657 clientPlayer = mc.field_1687.method_18470(e.getPlayerId());
            double trackedX = clientPlayer == null ? e.getX() : EntityUtils.getEntityX(clientPlayer, renderInfo.partialTicks);
            double trackedY = clientPlayer == null ? e.getY() : EntityUtils.getEntityY(clientPlayer, renderInfo.partialTicks);
            double trackedZ = clientPlayer == null ? e.getZ() : EntityUtils.getEntityZ(clientPlayer, renderInfo.partialTicks);
            double offX = trackedX - renderInfo.renderEntityPos.field_1352;
            double offY = trackedY - renderInfo.renderEntityPos.field_1351;
            double offZ = trackedZ - renderInfo.renderEntityPos.field_1350;
            double distance = Math.sqrt(offX * offX + offY * offY + offZ * offZ);
            if (distance < 10.0) {
               return false;
            }

            matrixStack.method_22903();
            matrixStack.method_22904(0.0, 0.0, optionalDepth);
            boolean inWorld = renderInfo.location == MinimapElementRenderLocation.IN_WORLD;
            float alpha = inWorld ? 0.5F : 1.0F;
            if (highlighted && inWorld) {
               alpha = 0.8F;
            }

            if (!highlighted && inWorld && distance < 20.0) {
               alpha *= (float)((distance - 10.0) / 10.0);
            }

            matrixStack.method_22904(0.0, 0.0, 0.01);
            matrixStack.method_22903();
            matrixStack.method_22905(this.context.iconScale, this.context.iconScale, 1.0F);
            this.context
               .helper
               .addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(), this.context.coloredBackgroundConsumer, -5.0F, -5.0F, 10, 10, 1.0F, 1.0F, 1.0F, alpha
               );
            this.playerTrackerIconRenderer
               .renderIcon(
                  mc, this.context.uniqueTextureUIObjectRenderer, this.context.helper, matrixStack, clientPlayer, this.getPlayerSkin(clientPlayer, info), alpha
               );
            matrixStack.method_22909();
            if (highlighted && inWorld) {
               matrixStack.method_46416(-5.0F * this.context.iconScale, 0.0F, 0.0F);
               matrixStack.method_22905(this.nameScale, this.nameScale, 1.0F);
               String playerName = info.method_2966().getName();
               int playerNameWidth = mc.field_1772.method_1727(playerName);
               float labelAlpha = 0.3529412F;
               this.context
                  .helper
                  .addColoredRectToExistingBuffer(
                     matrixStack.method_23760().method_23761(),
                     this.context.coloredBackgroundConsumer,
                     (float)(-playerNameWidth - 1),
                     -5.0F,
                     playerNameWidth + 1,
                     10,
                     0.0F,
                     0.0F,
                     0.0F,
                     labelAlpha
                  );
               mc.field_1772
                  .method_27521(
                     playerName,
                     (float)(-playerNameWidth),
                     -4.0F,
                     -1,
                     false,
                     matrixStack.method_23760().method_23761(),
                     this.minimapBufferSource,
                     class_6415.field_33993,
                     0,
                     15728880
                  );
            }

            matrixStack.method_22909();
         }

         return true;
      }
   }

   @Override
   public boolean shouldRender(MinimapElementRenderLocation location) {
      return location != MinimapElementRenderLocation.IN_WORLD && this.modMain.getSettings().displayTrackedPlayersOnMap
         || location == MinimapElementRenderLocation.IN_WORLD && this.modMain.getSettings().displayTrackedPlayersInWorld;
   }

   @Override
   public int getOrder() {
      return 100;
   }

   public PlayerTrackerMinimapElementCollector getCollector() {
      return this.elementCollector;
   }

   public static final class Builder {
      private final IXaeroMinimap modMain;

      private Builder(IXaeroMinimap modMain) {
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

      public static PlayerTrackerMinimapElementRenderer.Builder begin(IXaeroMinimap modMain) {
         return new PlayerTrackerMinimapElementRenderer.Builder(modMain).setDefault();
      }
   }
}
