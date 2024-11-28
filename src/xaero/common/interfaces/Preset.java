package xaero.common.interfaces;

import net.minecraft.class_1074;

public class Preset {
   private int[][] coords;
   private boolean[][] types;
   private String name;

   public Preset(String name, int[][] coords, boolean[][] types) {
      this.coords = coords;
      this.types = types;
      this.name = name;
   }

   public String getName() {
      return class_1074.method_4662(this.name, new Object[0]);
   }

   public int[] getCoords(int i) {
      return this.coords[i];
   }

   public boolean[] getTypes(int i) {
      return this.types[i];
   }
}
