package xaero.hud.module;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.HudMod;
import xaero.hud.pushbox.PushboxHandler;
import xaero.hud.render.module.IModuleRenderer;

public final class HudModule<MS extends ModuleSession<MS>> {
   private final class_2960 id;
   private final class_2561 displayName;
   private final BiFunction<HudMod, HudModule<MS>, MS> sessionFactory;
   private final Supplier<IModuleRenderer<MS>> rendererFactory;
   private final Function<class_437, class_437> configScreenFactory;
   private IModuleRenderer<MS> renderer;
   private MS currentSession;
   private ModuleTransform transform;
   private ModuleTransform unconfirmedTransform;
   private PushboxHandler.State pushState;
   private boolean active;

   public HudModule(
      class_2960 id,
      class_2561 displayName,
      BiFunction<HudMod, HudModule<MS>, MS> sessionFactory,
      Supplier<IModuleRenderer<MS>> rendererFactory,
      Function<class_437, class_437> configScreenFactory
   ) {
      this.displayName = displayName;
      this.active = true;
      this.id = id;
      this.sessionFactory = sessionFactory;
      this.rendererFactory = rendererFactory;
      this.configScreenFactory = configScreenFactory;
      this.transform = new ModuleTransform();
      this.pushState = new PushboxHandler.State();
   }

   public class_2960 getId() {
      return this.id;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public MS getCurrentSession() {
      return this.currentSession;
   }

   public IModuleRenderer<MS> getRenderer() {
      if (this.renderer == null) {
         this.renderer = this.rendererFactory.get();
      }

      return this.renderer;
   }

   public ModuleTransform getUsedTransform() {
      if (class_310.method_1551().field_1755 != null) {
         return this.getUnconfirmedTransform();
      } else {
         if (this.unconfirmedTransform != null) {
            this.cancelTransform();
         }

         return this.transform;
      }
   }

   public ModuleTransform getUnconfirmedTransform() {
      if (this.unconfirmedTransform == null) {
         this.unconfirmedTransform = this.transform.copy();
      }

      return this.unconfirmedTransform;
   }

   public void confirmTransform() {
      if (this.unconfirmedTransform != null) {
         this.transform = this.unconfirmedTransform;
         this.unconfirmedTransform = null;
      }
   }

   public ModuleTransform getConfirmedTransform() {
      return this.transform;
   }

   public void setTransform(ModuleTransform transform) {
      this.transform = transform;
      this.unconfirmedTransform = null;
   }

   public void cancelTransform() {
      this.unconfirmedTransform = null;
   }

   public PushboxHandler.State getPushState() {
      return this.pushState;
   }

   public class_2561 getDisplayName() {
      return this.displayName;
   }

   public Function<class_437, class_437> getConfigScreenFactory() {
      return this.configScreenFactory;
   }

   BiFunction<HudMod, HudModule<MS>, MS> getSessionFactory() {
      return this.sessionFactory;
   }

   void setCurrentSession(MS currentSession) {
      this.currentSession = currentSession;
   }

   void setRenderer(IModuleRenderer<MS> renderer) {
      this.renderer = renderer;
   }
}
