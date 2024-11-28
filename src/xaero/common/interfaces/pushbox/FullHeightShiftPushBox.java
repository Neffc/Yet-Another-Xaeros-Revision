package xaero.common.interfaces.pushbox;

import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceInstance;

public abstract class FullHeightShiftPushBox extends PushBox {
   protected int shift;

   public FullHeightShiftPushBox(int x, int w, float anchorX) {
      super(x, 0, w, 0, anchorX, 0.0F, 0);
   }

   @Override
   public void update() {
      super.update();
      this.shift = this.getShift();
   }

   @Override
   public int getH(int width, int height) {
      return height;
   }

   @Override
   public void push(
      InterfaceInstance ii, Interface i, int interfaceX, int interfaceY, int interfaceW, int interfaceH, int pushX, int pushY, int width, int height
   ) {
      int clampedShift = this.shift;
      if (clampedShift > 0) {
         int toBottom = height - interfaceY - interfaceH;
         if (toBottom < clampedShift) {
            clampedShift = toBottom;
         }
      } else if (clampedShift < 0) {
         int toTop = -interfaceY;
         if (toTop > clampedShift) {
            clampedShift = toTop;
         }
      }

      super.push(ii, i, interfaceX, interfaceY, interfaceW, interfaceH, 0, clampedShift, width, height);
   }

   protected abstract int getShift();
}
