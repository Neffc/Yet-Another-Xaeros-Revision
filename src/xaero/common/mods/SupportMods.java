package xaero.common.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.Effects;
import xaero.common.mods.pac.SupportOpenPartiesAndClaims;
import xaero.hud.minimap.MinimapLogs;

public abstract class SupportMods {
   public SupportXaeroWorldmap worldmapSupport = null;
   public SupportOpenPartiesAndClaims xaeroPac;
   private IXaeroMinimap modMain;
   public boolean optifine;
   public boolean vivecraft;
   public boolean iris;
   public boolean ftbTeams;
   public SupportIris supportIris;
   public SupportFramedBlocks supportFramedBlocks;

   public boolean worldmap() {
      return this.worldmapSupport != null;
   }

   public boolean pac() {
      return this.xaeroPac != null;
   }

   public boolean shouldUseWorldMapChunks() {
      return this.worldmap() && this.modMain.getSettings().getUseWorldMap();
   }

   public boolean shouldUseWorldMapCaveChunks() {
      return this.shouldUseWorldMapChunks() && this.worldmapSupport.caveLayersAreUsable();
   }

   public boolean framedBlocks() {
      return this.supportFramedBlocks != null;
   }

   public static void checkForMinimapDuplicates(String otherModMainClass) {
      try {
         Class.forName(otherModMainClass);
         throw new RuntimeException("Better PVP contains Xaero's Minimap by default. Do not install Better PVP and Xaero's Minimap together!");
      } catch (ClassNotFoundException var2) {
      }
   }

   public SupportMods(IXaeroMinimap modMain) {
      this.modMain = modMain;

      try {
         Class wmClassTest = Class.forName("xaero.map.WorldMap");
         this.worldmapSupport = new SupportXaeroWorldmap(modMain);
         MinimapLogs.LOGGER.info("Xaero's Minimap: World Map found!");
      } catch (ClassNotFoundException var15) {
      }

      try {
         Class pacClassTest = Class.forName("xaero.pac.OpenPartiesAndClaims");
         this.xaeroPac = new SupportOpenPartiesAndClaims(modMain);
         this.xaeroPac.register();
         MinimapLogs.LOGGER.info("Xaero's Minimap: Open Parties And Claims found!");
      } catch (ClassNotFoundException var14) {
      }

      try {
         Class optifineClassTest = Class.forName("optifine.Patcher");
         this.optifine = true;
         MinimapLogs.LOGGER.info("Optifine!");
      } catch (ClassNotFoundException var13) {
         this.optifine = false;
         MinimapLogs.LOGGER.info("No Optifine!");
      }

      try {
         Class vivecraftClassTest = Class.forName("org.vivecraft.api.VRData");
         this.vivecraft = true;

         try {
            Class<?> vrStateClass = Class.forName("org.vivecraft.VRState");
            Method checkVRMethod = vrStateClass.getDeclaredMethod("checkVR");
            this.vivecraft = (Boolean)checkVRMethod.invoke(null);
         } catch (ClassNotFoundException var7) {
         } catch (NoSuchMethodException var8) {
         } catch (IllegalAccessException var9) {
         } catch (IllegalArgumentException var10) {
         } catch (InvocationTargetException var11) {
         }
      } catch (ClassNotFoundException var12) {
      }

      if (this.vivecraft) {
         MinimapLogs.LOGGER.info("Xaero's Minimap: Vivecraft!");
      } else {
         MinimapLogs.LOGGER.info("Xaero's Minimap: No Vivecraft!");
      }

      try {
         Class mmClassTest = Class.forName("xfacthd.framedblocks.FramedBlocks");
         this.supportFramedBlocks = new SupportFramedBlocks();
         MinimapLogs.LOGGER.info("Xaero's Minimap: Framed Blocks found!");
      } catch (ClassNotFoundException var6) {
      }

      try {
         Class.forName("net.irisshaders.iris.api.v0.IrisApi");
         this.supportIris = new SupportIris();
         this.iris = true;
         MinimapLogs.LOGGER.info("Xaero's Minimap: Iris found!");
      } catch (Exception var5) {
      }

      if (this.worldmap() && this.worldmapSupport.shouldAlwaysInitEffects()) {
         Effects.init();
      }
   }
}
