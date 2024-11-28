package xaero.common.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_2626;
import net.minecraft.class_2637;
import net.minecraft.class_2666;
import net.minecraft.class_2672;
import net.minecraft.class_2676;
import net.minecraft.class_2678;
import net.minecraft.class_2759;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5218;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_634;
import net.minecraft.class_6603;
import net.minecraft.class_2556.class_7602;
import net.minecraft.class_32.class_5143;
import org.joml.Matrix4f;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.render.radar.EntityIconPrerenderer;
import xaero.common.misc.Misc;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.pushbox.BuiltInPushBoxes;
import xaero.hud.pushbox.boss.IBossHealthPushBox;
import xaero.hud.pushbox.effect.IPotionEffectsPushBox;

public class XaeroMinimapCore {
   public static IXaeroMinimap modMain;
   public static Field chunkCleanField = null;
   public static XaeroMinimapSession currentSession;
   public static Matrix4f waypointsProjection = new Matrix4f();
   public static Matrix4f waypointModelView = new Matrix4f();

   public static void ensureField() {
      if (chunkCleanField == null) {
         try {
            chunkCleanField = class_2818.class.getDeclaredField("xaero_chunkClean");
         } catch (SecurityException | NoSuchFieldException var1) {
            throw new RuntimeException(var1);
         }
      }
   }

   public static void chunkUpdateCallback(int chunkX, int chunkZ) {
      ensureField();
      class_1937 world = class_310.method_1551().field_1687;
      if (world != null) {
         try {
            for (int x = chunkX - 1; x < chunkX + 2; x++) {
               for (int z = chunkZ - 1; z < chunkZ + 2; z++) {
                  class_2818 chunk = world.method_8497(x, z);
                  if (chunk != null) {
                     chunkCleanField.set(chunk, false);
                  }
               }
            }
         } catch (IllegalAccessException | IllegalArgumentException var6) {
            throw new RuntimeException(var6);
         }
      }
   }

   public static void onChunkData(int x, int z, class_6603 packetIn) {
      chunkUpdateCallback(x, z);
   }

   private static void onChunkLightData(int x, int z) {
      chunkUpdateCallback(x, z);
   }

   public static void onHandleLevelChunkWithLight(class_2672 packet) {
      onChunkLightData(packet.method_11523(), packet.method_11524());
   }

   public static void onHandleLightUpdatePacket(class_2676 packet) {
      onChunkLightData(packet.method_11558(), packet.method_11554());
   }

   public static void onQueueLightRemoval(class_2666 packet) {
      onChunkLightData(packet.method_11487(), packet.method_11485());
   }

   public static void onBlockChange(class_2626 packetIn) {
      chunkUpdateCallback(packetIn.method_11309().method_10263() >> 4, packetIn.method_11309().method_10260() >> 4);
   }

   public static void onMultiBlockChange(class_2637 packetIn) {
      IXaeroMinimapSMultiBlockChangePacket packetAccess = (IXaeroMinimapSMultiBlockChangePacket)packetIn;
      chunkUpdateCallback(packetAccess.xaero_mm_getSectionPos().method_10263(), packetAccess.xaero_mm_getSectionPos().method_10260());
   }

   public static void onPlayNetHandler(class_634 netHandler, class_2678 packet) {
      if (HudMod.INSTANCE != null) {
         HudMod.INSTANCE.tryLoadLater();
      }

      if (isModLoaded()) {
         if (modMain.getInterfaces().getMinimapInterface().getCrashedWith() == null) {
            try {
               IXaeroMinimapClientPlayNetHandler netHandlerAccess = (IXaeroMinimapClientPlayNetHandler)netHandler;
               if (netHandlerAccess.getXaero_minimapSession() != null) {
                  return;
               }

               if (currentSession != null) {
                  MinimapLogs.LOGGER.info("Previous hud session still active. Probably using MenuMobs. Forcing it to end...");
                  cleanupCurrentSession();
               }

               XaeroMinimapSession minimapSession = modMain.createSession();
               currentSession = minimapSession;
               minimapSession.init(netHandler);
               netHandlerAccess.setXaero_minimapSession(minimapSession);
            } catch (Throwable var4) {
               if (currentSession != null) {
                  cleanupCurrentSession();
               }

               RuntimeException wrappedException = new RuntimeException("Exception initializing Xaero's Minimap! ", var4);
               modMain.getInterfaces().getMinimapInterface().setCrashedWith(wrappedException);
            }
         }
      }
   }

   private static void cleanupCurrentSession() {
      try {
         currentSession.tryCleanup();
      } catch (Throwable var4) {
         MinimapLogs.LOGGER.error("suppressed exception", var4);
      } finally {
         currentSession = null;
      }
   }

   public static void onPlayNetHandlerCleanup(class_634 netHandler) {
      if (isModLoaded()) {
         try {
            XaeroMinimapSession netHandlerSession = ((IXaeroMinimapClientPlayNetHandler)netHandler).getXaero_minimapSession();
            if (netHandlerSession == null) {
               return;
            }

            try {
               netHandlerSession.tryCleanup();
            } finally {
               if (netHandlerSession == currentSession) {
                  currentSession = null;
               }

               ((IXaeroMinimapClientPlayNetHandler)netHandler).setXaero_minimapSession(null);
            }
         } catch (Throwable var6) {
            RuntimeException wrappedException = new RuntimeException("Exception finalizing Xaero's Minimap! ", var6);
            modMain.getInterfaces().getMinimapInterface().setCrashedWith(wrappedException);
         }
      }
   }

   public static void beforeRespawn(class_1657 player) {
      if (isModLoaded()) {
         if (player == class_310.method_1551().field_1724) {
            MinimapSession minimapSession = BuiltInHudModules.MINIMAP.getCurrentSession();
            if (minimapSession != null) {
               minimapSession.getWaypointSession().getDeathpointHandler().createDeathpoint(player);
            }
         }
      }
   }

   public static void onProjectionMatrix(Matrix4f matrixIn) {
      waypointsProjection.identity();
      waypointsProjection.mul(matrixIn);
   }

   public static void onWorldModelViewMatrix(class_4587 matrixStack) {
      waypointModelView.identity();
      waypointModelView.mul(matrixStack.method_23760().method_23761());
   }

   public static void beforeIngameGuiRender(class_332 guiGraphics, float partialTicks) {
      if (isModLoaded()) {
         HudMod.INSTANCE.getEvents().handleRenderGameOverlayEventPre(guiGraphics, partialTicks);
      }
   }

   public static void afterIngameGuiRender(class_332 guiGraphics, float partialTicks) {
      if (isModLoaded()) {
         HudMod.INSTANCE.getEvents().handleRenderGameOverlayEventPost();
      }
   }

   public static void onRenderStatusEffectOverlayPost(class_332 guiGraphics) {
      if (isModLoaded()) {
         IPotionEffectsPushBox potionEffectsPushBox = BuiltInPushBoxes.getPotionEffectPushBox(modMain);
         if (potionEffectsPushBox != null) {
            potionEffectsPushBox.setActive(true);
         }
      }
   }

   public static void onBossHealthRender(int h) {
      if (isModLoaded()) {
         IBossHealthPushBox bossHealthPushBox = BuiltInPushBoxes.getBossHealthPushBox(modMain);
         if (bossHealthPushBox != null) {
            bossHealthPushBox.setActive(true);
            bossHealthPushBox.setLastBossHealthHeight(h);
         }
      }
   }

   public static void onEntityIconsModelRenderDetection(class_583<?> model, class_4588 vertexConsumer, float red, float green, float blue, float alpha) {
      if (EntityIconPrerenderer.DETECTING_MODEL_RENDERS) {
         modMain.getInterfaces()
            .getMinimapInterface()
            .getMinimapFBORenderer()
            .onEntityIconsModelRenderDetection(model, vertexConsumer, red, green, blue, alpha);
      }
   }

   public static void onEntityIconsModelPartRenderDetection(class_630 modelRenderer, float red, float green, float blue, float alpha) {
      if (EntityIconPrerenderer.DETECTING_MODEL_RENDERS) {
         modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().onEntityIconsModelPartRenderDetection(modelRenderer, red, green, blue, alpha);
      }
   }

   public static void onDeleteWorld(class_5143 levelStorageAccess) {
      if (isModLoaded()) {
         String worldFolder = levelStorageAccess.method_27010(class_5218.field_24188).getParent().getFileName().toString();
         if (!worldFolder.isEmpty()) {
            String minimapWorldFolder = MinimapWorldContainerUtil.convertWorldFolderToContainerNode(worldFolder);
            Path minimapWorldFolderPath = HudMod.INSTANCE.getMinimapFolder().resolve(minimapWorldFolder);
            if (minimapWorldFolderPath.toFile().exists()) {
               try {
                  Misc.deleteFile(minimapWorldFolderPath, 20);
                  MinimapLogs.LOGGER.info(String.format("Deleted minimap world data at %s", minimapWorldFolderPath));
               } catch (IOException var5) {
                  MinimapLogs.LOGGER.error(String.format("Failed to delete minimap world data at %s!", minimapWorldFolderPath), var5);
               }
            }
         }
      }
   }

   public static void onSpawn(class_2759 packetIn) {
      if (isModLoaded()) {
         modMain.getEvents().handlePlayerSetSpawnEvent(packetIn.method_11870(), class_310.method_1551().field_1687);
      }
   }

   public static boolean onLocalPlayerCommand(String command) {
      return !isModLoaded() ? false : modMain.getEvents().handleClientSendChatEvent(command);
   }

   public static boolean onSystemChat(class_2561 component) {
      return !isModLoaded() ? false : modMain.getEvents().handleClientSystemChatReceivedEvent(component);
   }

   public static boolean onHandleDisguisedChatMessage(class_7602 chatType, class_2561 component) {
      return !isModLoaded() ? true : !modMain.getEvents().handleClientPlayerChatReceivedEvent(chatType, component, null);
   }

   public static boolean isModLoaded() {
      return modMain != null && modMain.isLoadedClient();
   }

   public static boolean onRenderStatusEffectOverlay(class_332 guiGraphics) {
      return !isModLoaded() ? false : modMain.getEvents().handleRenderStatusEffectOverlay(guiGraphics);
   }

   public static boolean onRenderCrosshair(class_332 guiGraphics) {
      return !isModLoaded() ? false : modMain.getEvents().handleRenderCrosshairOverlay(guiGraphics);
   }

   public static void handleRenderModOverlay(class_332 guiGraphics, float partialTicks) {
      if (isModLoaded()) {
         modMain.getModClientEvents().handleRenderModOverlay(guiGraphics, partialTicks);
      }
   }
}
