package xaero.common.interfaces.pushbox;

import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceInstance;

public class PushBox {
   private int x;
   private int y;
   protected int w;
   protected int h;
   private float anchorX;
   private float anchorY;
   protected boolean active;
   private int verticalBias;

   public PushBox(int x, int y, int w, int h, float anchorX, float anchorY, int verticalBias) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.anchorX = anchorX;
      this.anchorY = anchorY;
      this.verticalBias = verticalBias;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public int getX(int width, int height) {
      return this.x;
   }

   public int getY(int width, int height) {
      return this.y;
   }

   public int getW(int width, int height) {
      return this.w;
   }

   public int getH(int width, int height) {
      return this.h;
   }

   public float getAnchorX() {
      return this.anchorX;
   }

   public float getAnchorY() {
      return this.anchorY;
   }

   public int getVerticalBias() {
      return this.verticalBias;
   }

   public void update() {
   }

   public void postUpdate() {
   }

   public void push(
      InterfaceInstance ii, Interface i, int interfaceX, int interfaceY, int interfaceW, int interfaceH, int pushX, int pushY, int width, int height
   ) {
      i.setX(interfaceX + pushX);
      i.setY(interfaceY + pushY);
   }
}
