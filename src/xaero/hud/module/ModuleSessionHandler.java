package xaero.hud.module;

import xaero.common.HudMod;

public class ModuleSessionHandler {
   private final ModuleManager manager;

   public ModuleSessionHandler(ModuleManager manager) {
      this.manager = manager;
   }

   public void resetSessions(HudMod modMain) {
      for (HudModule<?> module : this.manager.getModules()) {
         this.resetSession(module, modMain);
      }
   }

   public void closeSessions(HudMod modMain) {
      for (HudModule<?> module : this.manager.getModules()) {
         this.closeSession(module, modMain);
      }
   }

   private <MS extends ModuleSession<MS>> void resetSession(HudModule<MS> module, HudMod modMain) {
      this.closeSession(module, modMain);
      module.setCurrentSession(module.getSessionFactory().apply(modMain, module));
      HudMod.LOGGER.debug("Initialized new session for module {}!", module.getId());
   }

   private <MS extends ModuleSession<MS>> void closeSession(HudModule<MS> module, HudMod modMain) {
      if (module.getCurrentSession() != null) {
         try {
            module.getCurrentSession().close();
            HudMod.LOGGER.debug("Finalized session for module {}!", module.getId());
         } catch (Throwable var4) {
            HudMod.LOGGER.error("Failed to finalize session for module {}!", module.getId(), var4);
         }
      }

      module.setRenderer(null);
      module.setCurrentSession(null);
   }
}
