package xaero.hud.minimap.info;

import java.util.ArrayList;
import xaero.hud.minimap.info.render.InfoDisplayRenderer;

public class InfoDisplays {
   private final InfoDisplayManager manager = InfoDisplayManager.Builder.begin().build();
   private final InfoDisplayIO io;
   private final InfoDisplayRenderer renderer;

   public InfoDisplays() {
      BuiltInInfoDisplays.forEach(this.manager::add);
      xaero.common.minimap.info.BuiltInInfoDisplays.addToManager((xaero.common.minimap.info.InfoDisplayManager)this.manager);
      this.manager.setOrder(new ArrayList<>());
      this.io = new InfoDisplayIO(this.manager);
      this.renderer = InfoDisplayRenderer.Builder.begin().build();
   }

   public InfoDisplayManager getManager() {
      return this.manager;
   }

   public InfoDisplayIO getIo() {
      return this.io;
   }

   public InfoDisplayRenderer getRenderer() {
      return this.renderer;
   }
}
