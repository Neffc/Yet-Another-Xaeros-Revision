package xaero.common.gui;

public class GuiDropdownHelper {
   protected int current;
   protected int auto;
   protected String[] keys;
   protected String[] options;

   public String getCurrentKey() {
      return this.keys[this.current];
   }

   public String getCurrentName() {
      return this.options[this.current];
   }
}
