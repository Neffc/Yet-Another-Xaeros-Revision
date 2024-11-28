package xaero.common.minimap.highlight;

import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.common.minimap.info.render.compile.InfoDisplayCompiler;

public abstract class AbstractHighlighter {
   protected final boolean coveringOutsideDiscovered;
   protected final int[] resultStore = new int[256];

   protected AbstractHighlighter(boolean coveringOutsideDiscovered) {
      this.coveringOutsideDiscovered = coveringOutsideDiscovered;
   }

   public abstract boolean regionHasHighlights(class_5321<class_1937> var1, int var2, int var3);

   public abstract boolean chunkIsHighlit(class_5321<class_1937> var1, int var2, int var3);

   public abstract int[] getChunkHighlitColor(class_5321<class_1937> var1, int var2, int var3);

   public abstract void addBlockHighlightTooltips(InfoDisplayCompiler var1, class_5321<class_1937> var2, int var3, int var4, int var5);

   protected void setResult(int x, int z, int color) {
      this.resultStore[z << 4 | x] = color;
   }

   protected int getBlend(int color1, int color2) {
      if (color1 == color2) {
         return color1;
      } else {
         int red1 = color1 >> 8 & 0xFF;
         int green1 = color1 >> 16 & 0xFF;
         int blue1 = color1 >> 24 & 0xFF;
         int alpha1 = color1 & 0xFF;
         int red2 = color2 >> 8 & 0xFF;
         int green2 = color2 >> 16 & 0xFF;
         int blue2 = color2 >> 24 & 0xFF;
         int alpha2 = color2 & 0xFF;
         int red = red1 + red2 >> 1;
         int green = green1 + green2 >> 1;
         int blue = blue1 + blue2 >> 1;
         int alpha = alpha1 + alpha2 >> 1;
         return blue << 24 | green << 16 | red << 8 | alpha;
      }
   }

   public boolean isCoveringOutsideDiscovered() {
      return this.coveringOutsideDiscovered;
   }
}
