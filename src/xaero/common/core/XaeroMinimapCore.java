package xaero.common.core;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import net.minecraft.class_1041;
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
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5218;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_634;
import net.minecraft.class_6603;
import net.minecraft.class_8251;
import net.minecraft.class_2556.class_7602;
import net.minecraft.class_32.class_5143;
import org.joml.Matrix4f;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.interfaces.pushbox.IBossHealthPushBox;
import xaero.common.interfaces.pushbox.IPotionEffectsPushBox;
import xaero.common.minimap.render.radar.EntityIconPrerenderer;
import xaero.common.misc.Misc;

public class XaeroMinimapCore {
   public static IXaeroMinimap modMain;
   public static Field chunkCleanField = null;
   public static XaeroMinimapSession currentSession;
   private static Matrix4f waypointsProjection = new Matrix4f();
   private static Matrix4f waypointModelView = new Matrix4f();

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
                  MinimapLogs.LOGGER.info("Previous minimap session still active. Probably using MenuMobs. Forcing it to end...");
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
         currentSession.cleanup();
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
               netHandlerSession.cleanup();
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
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            minimapSession.getWaypointsManager().createDeathpoint(player);
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

   public static void beforeIngameGuiRender(float partialTicks) {
      if (isModLoaded()) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            class_1041 mainwindow = class_310.method_1551().method_22683();
            Matrix4f projectionMatrixBU = RenderSystem.getProjectionMatrix();
            class_8251 vertexSortingBU = RenderSystem.getVertexSorting();
            Matrix4f ortho = new Matrix4f().setOrtho(0.0F, (float)mainwindow.method_4489(), (float)mainwindow.method_4506(), 0.0F, 1000.0F, 3000.0F);
            RenderSystem.setProjectionMatrix(ortho, class_8251.field_43361);
            RenderSystem.getModelViewStack().method_22903();
            RenderSystem.getModelViewStack().method_34426();
            RenderSystem.applyModelViewMatrix();
            modMain.getInterfaces()
               .getMinimapInterface()
               .getWaypointsIngameRenderer()
               .render(minimapSession, partialTicks, minimapSession.getMinimapProcessor(), waypointsProjection, waypointModelView);
            RenderSystem.getModelViewStack().method_22909();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setProjectionMatrix(projectionMatrixBU, vertexSortingBU);
         }
      }
   }

   public static void onPotionEffectsRender() {
      if (isModLoaded()) {
         IPotionEffectsPushBox potionEffectsPushBox = modMain.getInterfaces().getPotionEffectPushBox();
         if (potionEffectsPushBox != null) {
            potionEffectsPushBox.setActive(true);
         }
      }
   }

   public static void onBossHealthRender(int h) {
      if (isModLoaded()) {
         IBossHealthPushBox bossHealthPushBox = modMain.getInterfaces().getBossHealthPushBox();
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
         Path worldName = levelStorageAccess.method_27010(class_5218.field_24188).getParent().getFileName();
         if (!worldName.toString().isEmpty()) {
            Path worldMinimapDataFolder = HudMod.INSTANCE.getWaypointsFolder().toPath().resolve(worldName);
            if (worldMinimapDataFolder.toFile().exists()) {
               try {
                  Misc.deleteFile(worldMinimapDataFolder, 20);
                  MinimapLogs.LOGGER.info(String.format("Deleted minimap world data at %s", worldMinimapDataFolder));
               } catch (IOException var4) {
                  MinimapLogs.LOGGER.error(String.format("Failed to delete minimap world data at %s!", worldMinimapDataFolder), var4);
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
}
