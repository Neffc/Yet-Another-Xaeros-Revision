package xaero.common.gui;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.class_1074;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.misc.KeySortableByOther;

public class GuiWaypointSets {
   private int currentSet;
   private String[] options;

   public GuiWaypointSets(boolean canCreate, WaypointWorld currentWorld) {
      this(canCreate, currentWorld, currentWorld.getCurrent());
   }

   public GuiWaypointSets(boolean canCreate, WaypointWorld currentWorld, String currentSetName) {
      int size = currentWorld.getSets().size() + (canCreate ? 1 : 0);
      String[] keys = currentWorld.getSets().keySet().toArray(new String[0]);
      ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>();

      for (int i = 0; i < keys.length; i++) {
         String key = keys[i];
         keysList.add(new KeySortableByOther<>(key, class_1074.method_4662(key, new Object[0]).toLowerCase()));
      }

      Collections.sort(keysList);
      this.options = new String[size];

      for (int i = 0; i < keysList.size(); i++) {
         this.options[i] = keysList.get(i).getKey();
         if (this.options[i].equals(currentSetName)) {
            this.currentSet = i;
         }
      }

      if (canCreate) {
         this.options[this.options.length - 1] = "§8" + class_1074.method_4662("gui.xaero_create_set", new Object[0]);
      }
   }

   public int getCurrentSet() {
      return this.currentSet;
   }

   public String getCurrentSetKey() {
      return this.options[this.currentSet];
   }

   public void setCurrentSet(int currentSet) {
      this.currentSet = currentSet;
   }

   public String[] getOptions() {
      return this.options;
   }
}
