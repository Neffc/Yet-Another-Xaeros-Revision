package xaero.common.interfaces.pushbox;

public class PotionEffectsShiftPushBox extends FullHeightShiftPushBox implements IPotionEffectsPushBox {
   private boolean hasNegative;

   public PotionEffectsShiftPushBox() {
      super(0, 0, 1.0F);
   }

   @Override
   public int getX(int width, int height) {
      return super.getX(width, height) - this.getW(width, height);
   }

   @Override
   protected int getShift() {
      return this.hasNegative ? 53 : 27;
   }

   @Override
   public void update() {
      super.update();
      this.hasNegative = false;
      this.w = PotionEffectsPushBox.calculatePotionDisplayWidth(this);
   }

   @Override
   public void postUpdate() {
      super.postUpdate();
      this.active = false;
   }

   @Override
   public void setHasNegative(boolean b) {
      this.hasNegative = b;
   }
}
