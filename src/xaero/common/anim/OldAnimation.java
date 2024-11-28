package xaero.common.anim;

public class OldAnimation {
   public static long lastTick = System.currentTimeMillis();
   public static final double animationThing = 16.666666666666668;

   public static void tick() {
      lastTick = System.currentTimeMillis();
   }

   public static double animate(double a, double factor) {
      double times = (double)(System.currentTimeMillis() - lastTick) / 16.666666666666668;
      return a * Math.pow(factor, times);
   }
}
