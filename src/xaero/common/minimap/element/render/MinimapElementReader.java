package xaero.common.minimap.element.render;

import net.minecraft.class_310;

@Deprecated
public abstract class MinimapElementReader<E, RC> extends xaero.hud.minimap.element.render.MinimapElementReader<E, RC> {
   @Override
   public abstract boolean isHidden(E var1, RC var2);

   @Override
   public abstract double getRenderX(E var1, RC var2, float var3);

   @Override
   public abstract double getRenderY(E var1, RC var2, float var3);

   @Override
   public abstract double getRenderZ(E var1, RC var2, float var3);

   @Override
   public abstract int getInteractionBoxLeft(E var1, RC var2, float var3);

   @Override
   public abstract int getInteractionBoxRight(E var1, RC var2, float var3);

   @Override
   public abstract int getInteractionBoxTop(E var1, RC var2, float var3);

   @Override
   public abstract int getInteractionBoxBottom(E var1, RC var2, float var3);

   @Override
   public abstract int getRenderBoxLeft(E var1, RC var2, float var3);

   @Override
   public abstract int getRenderBoxRight(E var1, RC var2, float var3);

   @Override
   public abstract int getRenderBoxTop(E var1, RC var2, float var3);

   @Override
   public abstract int getRenderBoxBottom(E var1, RC var2, float var3);

   @Override
   public abstract int getLeftSideLength(E var1, class_310 var2);

   @Override
   public abstract String getMenuName(E var1);

   @Override
   public abstract String getFilterName(E var1);

   @Override
   public abstract int getMenuTextFillLeftPadding(E var1);

   @Override
   public abstract int getRightClickTitleBackgroundColor(E var1);

   @Override
   public abstract boolean shouldScaleBoxWithOptionalScale();

   @Deprecated
   public boolean isInteractable(int location, E element) {
      return super.isInteractable(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), element);
   }

   @Deprecated
   public float getBoxScale(int location, E element, RC context) {
      return super.getBoxScale(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), element, context);
   }

   @Override
   public boolean isInteractable(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, E element) {
      return this.isInteractable(location.getIndex(), element);
   }

   @Override
   public float getBoxScale(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, E element, RC context) {
      return this.getBoxScale(location.getIndex(), element, context);
   }
}
