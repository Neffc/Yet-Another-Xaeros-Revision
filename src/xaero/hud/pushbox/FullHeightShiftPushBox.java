package xaero.hud.pushbox;

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
   public void push(PushboxHandler.State state, int pushX, int pushY) {
      super.push(state, 0, this.shift);
   }

   protected abstract int getShift();
}
