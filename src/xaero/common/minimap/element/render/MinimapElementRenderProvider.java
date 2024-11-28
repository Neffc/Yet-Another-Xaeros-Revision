package xaero.common.minimap.element.render;

@Deprecated
public abstract class MinimapElementRenderProvider<E, RC> extends xaero.hud.minimap.element.render.MinimapElementRenderProvider<E, RC> {
   @Deprecated
   public abstract void begin(int var1, RC var2);

   @Deprecated
   public abstract boolean hasNext(int var1, RC var2);

   @Deprecated
   public abstract E getNext(int var1, RC var2);

   @Deprecated
   public E setupContextAndGetNext(int location, RC context) {
      return super.setupContextAndGetNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), context);
   }

   @Deprecated
   public abstract void end(int var1, RC var2);

   @Override
   public void begin(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
      this.begin(location.getIndex(), context);
   }

   @Override
   public boolean hasNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
      return this.hasNext(location.getIndex(), context);
   }

   @Override
   public E getNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
      return this.getNext(location.getIndex(), context);
   }

   @Override
   public E setupContextAndGetNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
      return this.setupContextAndGetNext(location.getIndex(), context);
   }

   @Override
   public void end(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
      this.end(location.getIndex(), context);
   }
}
