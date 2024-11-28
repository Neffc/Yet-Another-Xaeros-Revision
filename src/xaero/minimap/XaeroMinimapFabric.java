package xaero.minimap;

import java.io.IOException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import xaero.common.PlatformContext;
import xaero.common.PlatformContextFabric;

public class XaeroMinimapFabric extends XaeroMinimap implements ClientModInitializer, DedicatedServerModInitializer {
   public static String fileLayoutID = "minimap_fabric";
   private final PlatformContextFabric xaeroHudFabric = (PlatformContextFabric)this.platformContext;

   public void onInitializeClient() {
      try {
         this.loadCommon();
         this.loadClient();
      } catch (Throwable var2) {
         this.xaeroHudFabric.firstStageError = var2;
      }
   }

   public void onInitializeServer() {
      try {
         this.loadCommon();
         this.loadServer();
      } catch (Throwable var2) {
         this.xaeroHudFabric.firstStageError = var2;
      }
   }

   @Override
   protected void loadClient() throws IOException {
      super.loadClient();
      this.xaeroHudFabric.postLoadClient();
   }

   @Override
   protected void loadCommon() {
      super.loadCommon();
      this.xaeroHudFabric.postLoadCommon();
   }

   @Override
   public void loadServer() {
      super.loadServer();
      this.xaeroHudFabric.postLoadServer();
   }

   @Override
   public void tryLoadLater() {
      if (!this.xaeroHudFabric.preTryLoadLater()) {
         this.loadLater();
      }
   }

   @Override
   public void tryLoadLaterServer() {
      if (!this.xaeroHudFabric.preTryLoadLaterServer()) {
         this.loadLaterServer();
      }
   }

   @Override
   protected PlatformContext createPlatformContext() {
      return new PlatformContextFabric(this);
   }

   @Override
   public String getFileLayoutID() {
      return fileLayoutID;
   }
}
