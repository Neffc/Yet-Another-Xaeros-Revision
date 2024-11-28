package xaero.common.controls.event;

import net.minecraft.class_304;

public class KeyEvent {
   private class_304 kb;
   private boolean tickEnd;
   private boolean isRepeat;
   private boolean keyDown;
   private boolean firedOnce;

   public KeyEvent(class_304 kb, boolean tickEnd, boolean isRepeat, boolean keyDown) {
      this.kb = kb;
      this.tickEnd = tickEnd;
      this.isRepeat = isRepeat;
      this.keyDown = keyDown;
   }

   public class_304 getKb() {
      return this.kb;
   }

   public boolean isTickEnd() {
      return this.tickEnd;
   }

   public boolean isRepeat() {
      return this.isRepeat;
   }

   public boolean isKeyDown() {
      return this.keyDown;
   }

   public boolean wasFiredOnce() {
      return this.firedOnce;
   }

   public void setFiredOnce() {
      this.firedOnce = true;
   }
}
