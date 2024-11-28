package xaero.common.minimap.element.render;

public abstract class MinimapElementRenderProvider<E, RC> {
   public abstract void begin(int var1, RC var2);

   public abstract boolean hasNext(int var1, RC var2);

   public abstract E getNext(int var1, RC var2);

   public E setupContextAndGetNext(int location, RC context) {
      return this.getNext(location, context);
   }

   public abstract void end(int var1, RC var2);
}
