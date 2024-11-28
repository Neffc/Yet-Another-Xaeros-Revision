package xaero.common.minimap.radar.tracker;

import java.util.UUID;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.common.minimap.radar.tracker.system.IPlayerTrackerSystem;

public class PlayerTrackerMinimapElement<P> {
   private P player;
   private IPlayerTrackerSystem<P> system;
   private boolean renderedOnRadar;

   public PlayerTrackerMinimapElement(P player, IPlayerTrackerSystem<P> system) {
      this.player = player;
      this.system = system;
   }

   public UUID getPlayerId() {
      return this.system.getReader().getId(this.player);
   }

   public double getX() {
      return this.system.getReader().getX(this.player);
   }

   public double getY() {
      return this.system.getReader().getY(this.player);
   }

   public double getZ() {
      return this.system.getReader().getZ(this.player);
   }

   public class_5321<class_1937> getDimension() {
      return this.system.getReader().getDimension(this.player);
   }

   public P getPlayer() {
      return this.player;
   }

   public void setRenderedOnRadar(boolean renderedOnRadar) {
      this.renderedOnRadar = renderedOnRadar;
   }

   public boolean wasRenderedOnRadar() {
      return this.renderedOnRadar;
   }
}
