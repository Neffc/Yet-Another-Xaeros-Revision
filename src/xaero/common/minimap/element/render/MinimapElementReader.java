package xaero.common.minimap.element.render;

import net.minecraft.class_310;

public abstract class MinimapElementReader<E, RC> {
   public abstract boolean isHidden(E var1, RC var2);

   public abstract double getRenderX(E var1, RC var2, float var3);

   public abstract double getRenderY(E var1, RC var2, float var3);

   public abstract double getRenderZ(E var1, RC var2, float var3);

   public abstract int getInteractionBoxLeft(E var1, RC var2, float var3);

   public abstract int getInteractionBoxRight(E var1, RC var2, float var3);

   public abstract int getInteractionBoxTop(E var1, RC var2, float var3);

   public abstract int getInteractionBoxBottom(E var1, RC var2, float var3);

   public abstract int getRenderBoxLeft(E var1, RC var2, float var3);

   public abstract int getRenderBoxRight(E var1, RC var2, float var3);

   public abstract int getRenderBoxTop(E var1, RC var2, float var3);

   public abstract int getRenderBoxBottom(E var1, RC var2, float var3);

   public abstract int getLeftSideLength(E var1, class_310 var2);

   public abstract String getMenuName(E var1);

   public abstract String getFilterName(E var1);

   public abstract int getMenuTextFillLeftPadding(E var1);

   public abstract int getRightClickTitleBackgroundColor(E var1);

   public abstract boolean shouldScaleBoxWithOptionalScale();

   public boolean isInteractable(int location, E element) {
      return false;
   }

   public float getBoxScale(int location, E element, RC context) {
      return 1.0F;
   }
}
