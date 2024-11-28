package xaero.common.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2585;
import net.minecraft.class_310;
import net.minecraft.class_5250;
import net.minecraft.class_5348;
import net.minecraft.class_5348.class_5246;

public class TextSplitter {
   public static int splitTextIntoLines(List<class_2561> dest, int minWidth, int widthLimit, class_5348 formattedText, StringBuilder plainTextBuilder) {
      TextSplitter.SplitProgress progress = new TextSplitter.SplitProgress();
      int spaceWidth = class_310.method_1551().field_1772.method_1727(" ");
      progress.resultWidth = minWidth;
      class_5246<Object> consumer = (style, text) -> {
         boolean isEnd = style == null;
         if (!isEnd && plainTextBuilder != null) {
            plainTextBuilder.append(text);
         }

         boolean endsWithSpace = text.endsWith(" ");
         if (endsWithSpace) {
            text = text + ".";
         }

         String[] parts = text.split(" ");

         for (int i = 0; i < parts.length; i++) {
            boolean canAddMultiword = isEnd || i < parts.length - 1;
            String part = !isEnd && (!endsWithSpace || i != parts.length - 1) ? parts[i] : "";
            int partWidth = class_310.method_1551().field_1772.method_1727(part);
            if (!canAddMultiword) {
               progress.buildMultiword(part, partWidth, style);
            } else {
               int wordWidth = partWidth + progress.multiwordWidth;
               int wordTakesWidth = wordWidth + (!progress.firstWord ? spaceWidth : 0);
               if (progress.lineWidth + wordTakesWidth <= widthLimit) {
                  progress.resultWidth = Math.max(progress.resultWidth, Math.min(widthLimit, progress.lineWidth + wordTakesWidth));
               }

               if (progress.firstWord && progress.lineWidth + wordTakesWidth > progress.resultWidth) {
                  progress.resultWidth = progress.lineWidth + wordTakesWidth;
               }

               boolean isNewLine = progress.multiword == null && part.equals("\n");
               if (!isNewLine && progress.lineWidth + wordTakesWidth <= progress.resultWidth) {
                  progress.confirmWord(part, style, wordTakesWidth);
               } else {
                  progress.confirmComponent();
                  dest.add(progress.line);
                  progress.nextLine();
                  if (!isNewLine) {
                     i--;
                  }
               }
            }
         }

         return Optional.empty();
      };
      formattedText.method_27658(consumer, class_2583.field_24360.method_10977(class_124.field_1068));
      if (progress.multiword != null) {
         consumer.accept(null, "end");
      } else if (progress.stringBuilder.length() > 0) {
         progress.confirmComponent();
      }

      if (progress.line != null) {
         dest.add(progress.line);
      }

      if (progress.resultWidth > minWidth) {
         progress.resultWidth--;
      }

      return progress.resultWidth;
   }

   public static class SplitProgress {
      int multiwordWidth;
      List<class_5250> multiword = null;
      boolean firstWord = true;
      class_2561 line = null;
      StringBuilder stringBuilder = new StringBuilder();
      int lineWidth;
      class_2583 lastStyle;
      int resultWidth;

      public void buildMultiword(String wordPart, int width, class_2583 style) {
         class_5250 wordPartComponent = class_2561.method_43470(wordPart).method_27696(style);
         if (this.multiword == null) {
            this.multiword = new ArrayList<>();
         }

         this.multiword.add(wordPartComponent);
         this.multiwordWidth += width;
      }

      private void confirmWordPart(String part, class_2583 style) {
         if (this.lastStyle != null && !Objects.equals(style, this.lastStyle)) {
            this.confirmComponent();
         }

         this.stringBuilder.append(part);
         this.lastStyle = style;
      }

      public void confirmWord(String lastPart, class_2583 lastPartStyle, int width) {
         if (!this.firstWord) {
            this.stringBuilder.append(" ");
         }

         if (this.multiword != null) {
            for (class_2561 component : this.multiword) {
               String text = ((class_2585)component.method_10851()).comp_737();
               class_2583 style = component.method_10866();
               this.confirmWordPart(text, style);
            }

            this.multiword = null;
            this.multiwordWidth = 0;
         }

         this.confirmWordPart(lastPart, lastPartStyle);
         this.lineWidth += width;
         this.firstWord = false;
      }

      public void confirmComponent() {
         class_2561 comp = class_2561.method_43470(this.stringBuilder.toString())
            .method_27696(this.lastStyle == null ? class_2583.field_24360 : this.lastStyle);
         if (this.line != null) {
            if (this.stringBuilder.length() > 0) {
               this.line.method_10855().add(comp);
            }
         } else {
            this.line = comp;
         }

         this.stringBuilder.delete(0, this.stringBuilder.length());
      }

      public void nextLine() {
         this.firstWord = true;
         this.line = null;
         this.lastStyle = null;
         this.lineWidth = 0;
      }
   }
}
