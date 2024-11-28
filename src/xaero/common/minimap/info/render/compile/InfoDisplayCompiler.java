package xaero.common.minimap.info.render.compile;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplay;

public class InfoDisplayCompiler {
   private boolean compiling;
   private List<class_2561> compiledLines = new ArrayList<>();

   public <T> List<class_2561> compile(
      InfoDisplay<T> infoDisplay,
      XaeroMinimapSession minimapSession,
      MinimapProcessor minimap,
      int x,
      int y,
      int width,
      int height,
      double scale,
      int size,
      int playerBlockX,
      int playerBlockY,
      int playerBlockZ,
      class_2338 playerPos
   ) {
      if (this.compiling) {
         throw new IllegalStateException();
      } else {
         this.compiling = true;
         this.compiledLines.clear();
         infoDisplay.getOnCompile()
            .onCompile(infoDisplay, this, minimapSession, minimap, x, y, width, height, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos);
         this.compiling = false;
         return this.compiledLines;
      }
   }

   public void addWords(int size, String text) {
      if (!this.compiling) {
         throw new IllegalStateException();
      } else {
         class_310 mc = class_310.method_1551();
         if (mc.field_1772.method_1727(text) <= size) {
            this.compiledLines.add(class_2561.method_43470(text));
         } else {
            String[] words = text.split(" ");
            StringBuilder lineBuilder = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
               int wordStart = lineBuilder.length();
               if (i > 0) {
                  lineBuilder.append(' ');
               }

               lineBuilder.append(words[i]);
               if (i != 0) {
                  int lineWidth = mc.field_1772.method_1727(lineBuilder.toString());
                  if (lineWidth > size) {
                     lineBuilder.delete(wordStart, lineBuilder.length());
                     this.compiledLines.add(class_2561.method_43470(lineBuilder.toString()));
                     lineBuilder.delete(0, lineBuilder.length());
                     lineBuilder.append(words[i]);
                  }
               }
            }

            this.compiledLines.add(class_2561.method_43470(lineBuilder.toString()));
         }
      }
   }

   public void addLine(class_2561 line) {
      if (!this.compiling) {
         throw new IllegalStateException();
      } else {
         this.compiledLines.add(line);
      }
   }

   public void addLine(String line) {
      this.addLine(class_2561.method_43470(line));
   }
}
