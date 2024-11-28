package xaero.common.controls.event;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_304;
import net.minecraft.class_310;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.hud.HudSession;

public class KeyEventHandler {
   public ArrayList<KeyEvent> keyEvents = new ArrayList<>();
   public ArrayList<KeyEvent> oldKeyEvents = new ArrayList<>();

   private boolean eventExists(class_304 kb) {
      for (KeyEvent o : this.keyEvents) {
         if (o.getKb() == kb) {
            return true;
         }
      }

      return this.oldEventExists(kb);
   }

   private boolean oldEventExists(class_304 kb) {
      for (KeyEvent o : this.oldKeyEvents) {
         if (o.getKb() == kb) {
            return true;
         }
      }

      return false;
   }

   public void handleEvents(class_310 mc, HudSession hudSession) {
      for (int i = 0; i < this.oldKeyEvents.size(); i++) {
         KeyEvent ke = this.oldKeyEvents.get(i);
         if (!hudSession.getControls().isDown(ke.getKb())) {
            hudSession.getControls().keyUp(ke.getKb(), ke.isTickEnd());

            while (ke.getKb().method_1436()) {
            }

            this.oldKeyEvents.remove(i);
            i--;
         }
      }

      for (int ix = 0; ix < this.keyEvents.size(); ix++) {
         KeyEvent ke = this.keyEvents.get(ix);
         if (mc.field_1755 == null && (!ke.wasFiredOnce() || hudSession.getControls().isDown(ke.getKb()))) {
            hudSession.getControls().keyDown(ke.getKb(), ke.isTickEnd(), ke.isRepeat());
         }

         if (!ke.isRepeat() || !hudSession.getControls().isDown(ke.getKb())) {
            if (!this.oldEventExists(ke.getKb())) {
               this.oldKeyEvents.add(ke);
            }

            this.keyEvents.remove(ix);
            ix--;
         }

         ke.setFiredOnce();
      }
   }

   public void onKeyInput(class_310 mc, IXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      if (mc.field_1755 == null) {
         List<class_304> kbs = modMain.getControlsRegister().keybindings;
         List<class_304> vkbs = modMain.getControlsRegister().vanillaKeyBindings;

         for (class_304 kb : kbs) {
            try {
               boolean pressed = false;
               if (vkbs.contains(kb)) {
                  pressed = kb.method_1434();
               } else {
                  pressed = kb.method_1436();

                  while (kb.method_1436()) {
                  }
               }

               if (pressed && !this.eventExists(kb)) {
                  this.keyEvents.add(new KeyEvent(kb, false, modMain.getSettings().isKeyRepeat(kb), true));
               }
            } catch (Exception var9) {
            }
         }
      }
   }
}
