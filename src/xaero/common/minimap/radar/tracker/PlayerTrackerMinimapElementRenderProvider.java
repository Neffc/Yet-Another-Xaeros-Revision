package xaero.common.minimap.radar.tracker;

import java.util.Iterator;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;

public class PlayerTrackerMinimapElementRenderProvider<C> extends MinimapElementRenderProvider<PlayerTrackerMinimapElement<?>, C> {
   private PlayerTrackerMinimapElementCollector collector;
   private Iterator<PlayerTrackerMinimapElement<?>> iterator;

   public PlayerTrackerMinimapElementRenderProvider(PlayerTrackerMinimapElementCollector collector) {
      this.collector = collector;
   }

   @Override
   public void begin(int location, C context) {
      this.iterator = this.collector.getElements().iterator();
   }

   @Override
   public boolean hasNext(int location, C context) {
      return this.iterator != null && this.iterator.hasNext();
   }

   public PlayerTrackerMinimapElement<?> getNext(int location, C context) {
      return this.iterator.next();
   }

   @Override
   public void end(int location, C context) {
      this.iterator = null;
   }
}
