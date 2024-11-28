package xaero.common;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_3264;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.events.ClientEvents;
import xaero.common.events.ClientEventsFabric;
import xaero.common.events.CommonEvents;
import xaero.common.events.CommonEventsFabric;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModClientEventsFabric;
import xaero.common.events.ModCommonEvents;
import xaero.common.events.ModCommonEventsFabric;
import xaero.common.message.MinimapMessageHandler;
import xaero.common.message.MinimapMessageHandlerFabric;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.write.MinimapWriter;
import xaero.common.minimap.write.MinimapWriterFabric;
import xaero.common.mods.SupportMods;
import xaero.common.mods.SupportModsFabric;
import xaero.common.resource.ShaderResourceReloadListener;

public class PlatformContextFabric extends PlatformContext {
   private final HudMod modMain;
   public boolean loadLaterNeeded;
   public boolean loadLaterDone;
   public Throwable firstStageError;
   private PlatformContextLoaderClientOnlyFabric loaderClientOnly;
   private PlatformContextLoaderCommonFabric loaderCommon;

   public PlatformContextFabric(HudMod modMain) {
      this.modMain = modMain;
   }

   public void postLoadClient() {
      ResourceManagerHelper.get(class_3264.field_14188).registerReloadListener(new ShaderResourceReloadListener());
      this.loadLaterNeeded = true;
   }

   public void postLoadCommon() {
   }

   public void postLoadServer() {
      this.loadLaterNeeded = true;
   }

   public boolean preTryLoadLater() {
      if (this.loadLaterDone) {
         return true;
      } else if (this.firstStageError != null) {
         throw new RuntimeException(this.firstStageError);
      } else if (!this.loadLaterNeeded) {
         return true;
      } else {
         this.loadLaterDone = true;
         return false;
      }
   }

   public boolean preTryLoadLaterServer() {
      if (this.loadLaterDone) {
         return true;
      } else if (this.firstStageError != null) {
         throw new RuntimeException(this.firstStageError);
      } else if (!this.loadLaterNeeded) {
         return true;
      } else {
         this.loadLaterDone = true;
         return false;
      }
   }

   @Override
   public ClientEvents createClientEvents(IXaeroMinimap modMain) {
      return new ClientEventsFabric(modMain);
   }

   @Override
   public CommonEvents createCommonEvents(IXaeroMinimap modMain) {
      return new CommonEventsFabric(modMain);
   }

   @Override
   public MinimapMessageHandler createMessageHandler(IXaeroMinimap modMain) {
      return new MinimapMessageHandlerFabric();
   }

   public PlatformContextLoaderClientOnlyFabric getLoaderClientOnly() {
      if (this.loaderClientOnly == null) {
         this.loaderClientOnly = new PlatformContextLoaderClientOnlyFabric();
      }

      return this.loaderClientOnly;
   }

   public PlatformContextLoaderCommonFabric getLoaderCommon() {
      if (this.loaderCommon == null) {
         this.loaderCommon = new PlatformContextLoaderCommonFabric();
      }

      return this.loaderCommon;
   }

   @Override
   public ModClientEvents createModClientEvents(IXaeroMinimap modMain) {
      return new ModClientEventsFabric(modMain);
   }

   @Override
   public ModCommonEvents createModCommonEvents(IXaeroMinimap modMain) {
      return new ModCommonEventsFabric(modMain);
   }

   @Override
   public SupportMods createSupportMods(IXaeroMinimap modMain) {
      return new SupportModsFabric(modMain);
   }

   @Override
   public MinimapWriter createMinimapWriter(
      IXaeroMinimap modMain,
      XaeroMinimapSession xaeroMinimapSession,
      BlockStateShortShapeCache blockStateShortShapeCache,
      HighlighterRegistry highlighterRegistry
   ) {
      return new MinimapWriterFabric(modMain, xaeroMinimapSession, blockStateShortShapeCache, highlighterRegistry);
   }

   @Override
   public String getModInfoVersion() {
      ModContainer modContainer = (ModContainer)FabricLoader.getInstance().getModContainer(this.modMain.getModId()).get();
      return modContainer.getMetadata().getVersion().getFriendlyString() + "_fabric";
   }

   public static PlatformContextFabric get() {
      return (PlatformContextFabric)HudMod.INSTANCE.getPlatformContext();
   }
}
