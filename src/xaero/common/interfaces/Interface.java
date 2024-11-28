package xaero.common.interfaces;

import net.minecraft.class_2561;
import xaero.common.XaeroMinimapSession;
import xaero.common.graphics.CursorBox;
import xaero.common.settings.ModOptions;

public abstract class Interface {
   public static final class_2561 CENTERED_COMPONENT = class_2561.method_43471("gui.xaero_centered");
   public static final class_2561 FLIPPED_COMPONENT = class_2561.method_43471("gui.xaero_flipped");
   public static final class_2561 TRUE_COMPONENT = class_2561.method_43471("gui.xaero_true");
   public static final class_2561 FALSE_COMPONENT = class_2561.method_43471("gui.xaero_false");
   public static final class_2561 PRESS_C_COMPONENT = class_2561.method_43471("gui.xaero_press_c");
   public static final class_2561 PRESS_F_COMPONENT = class_2561.method_43471("gui.xaero_press_f");
   public static final class_2561 EMPTY_COMPONENT = class_2561.method_43470("");
   public static final class_2561 SPACE_COMPONENT = class_2561.method_43470(" ");
   private CursorBox cBox;
   private String iname;
   private int id;
   private int bx;
   private int by;
   private int x;
   private int y;
   private int actualx;
   private int actualy;
   private int w0;
   private int h0;
   private int w;
   private int h;
   private int wc;
   private int hc;
   private boolean multisized;
   private boolean centered;
   private boolean bcentered;
   private boolean flipped;
   private boolean bflipped;
   private boolean flippedInitial;
   private boolean fromRight;
   private boolean bfromRight;
   private boolean fromBottom;
   private boolean bfromBottom;
   private ModOptions option;

   public Interface(InterfaceManager interfaceHandler, String name, int id, int w, int h, ModOptions option) {
      this(interfaceHandler, name, id, w, h, w, h, option);
   }

   public Interface(InterfaceManager interfaceHandler, String name, int id, int w, int h, int wc, int hc, ModOptions option) {
      this.id = id;
      this.iname = name;
      this.w0 = this.w = w;
      this.h0 = this.h = h;
      this.wc = wc;
      this.hc = hc;
      this.multisized = wc != w || hc != h;
      this.flippedInitial = this.flipped = this.bflipped = false;
      Preset preset = interfaceHandler.getDefaultPreset();
      this.bx = this.actualx = this.x = preset.getCoords(id)[0];
      this.by = this.actualy = this.y = preset.getCoords(id)[1];
      this.bcentered = this.centered = preset.getTypes(id)[0];
      this.bfromRight = this.fromRight = preset.getTypes(id)[1];
      this.bfromBottom = this.fromBottom = preset.getTypes(id)[2];
      this.option = option;
      this.cBox = (new CursorBox(3) {
         final class_2561 interfaceName = class_2561.method_43471(Interface.this.iname);

         @Override
         public class_2561 getLine(int line) {
            switch (line) {
               case 0:
                  return this.interfaceName;
               case 1: {
                  class_2561 result = class_2561.method_43470("");
                  result.method_10855().add(Interface.CENTERED_COMPONENT);
                  result.method_10855().add(Interface.SPACE_COMPONENT);
                  result.method_10855().add(Interface.this.centered ? Interface.TRUE_COMPONENT : Interface.FALSE_COMPONENT);
                  result.method_10855().add(Interface.SPACE_COMPONENT);
                  result.method_10855().add(Interface.PRESS_C_COMPONENT);
                  return result;
               }
               case 2: {
                  class_2561 result = class_2561.method_43470("");
                  result.method_10855().add(Interface.FLIPPED_COMPONENT);
                  result.method_10855().add(Interface.SPACE_COMPONENT);
                  result.method_10855().add(Interface.this.flipped ? Interface.TRUE_COMPONENT : Interface.FALSE_COMPONENT);
                  result.method_10855().add(Interface.SPACE_COMPONENT);
                  result.method_10855().add(Interface.PRESS_F_COMPONENT);
                  return result;
               }
               default:
                  return Interface.EMPTY_COMPONENT;
            }
         }
      }).withWidth(150);
   }

   public InterfaceInstance createInterfaceInstance(XaeroMinimapSession minimapSession) {
      return new InterfaceInstance(this);
   }

   public boolean shouldFlip(int width) {
      return this.flipped && this.x + this.w / 2 < width / 2 || !this.flipped && this.x + this.w / 2 > width / 2;
   }

   public void backup() {
      this.bx = this.actualx;
      this.by = this.actualy;
      this.bcentered = this.centered;
      this.bflipped = this.flipped;
      this.bfromRight = this.fromRight;
      this.bfromBottom = this.fromBottom;
   }

   public void restore() {
      this.actualx = this.bx;
      this.actualy = this.by;
      this.centered = this.bcentered;
      this.flipped = this.bflipped;
      this.fromRight = this.bfromRight;
      this.fromBottom = this.bfromBottom;
   }

   public void applyPreset(Preset preset) {
      this.actualx = preset.getCoords(this.id)[0];
      this.actualy = preset.getCoords(this.id)[1];
      this.centered = preset.getTypes(this.id)[0];
      this.flipped = this.flippedInitial;
      this.fromRight = preset.getTypes(this.id)[1];
      this.fromBottom = preset.getTypes(this.id)[2];
   }

   public ModOptions getOption() {
      return this.option;
   }

   public boolean isFromRight() {
      return this.fromRight;
   }

   public void setFromRight(boolean fromRight) {
      this.fromRight = fromRight;
   }

   public int getW(double scale) {
      return this.w;
   }

   public int getH(double scale) {
      return this.h;
   }

   int getWC(double scale) {
      return this.wc;
   }

   int getHC(double scale) {
      return this.hc;
   }

   int getW0(double scale) {
      return this.w0;
   }

   int getH0(double scale) {
      return this.h0;
   }

   int getSize() {
      return this.w * this.h;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public boolean isFlipped() {
      return this.flipped;
   }

   public void setFlipped(boolean flipped) {
      this.flipped = flipped;
   }

   public boolean isCentered() {
      return this.centered;
   }

   public void setCentered(boolean centered) {
      this.centered = centered;
   }

   public int getActualx() {
      return this.actualx;
   }

   public void setActualx(int actualx) {
      this.actualx = actualx;
   }

   public int getActualy() {
      return this.actualy;
   }

   public void setActualy(int actualy) {
      this.actualy = actualy;
   }

   public boolean isMulti() {
      return this.multisized;
   }

   public int getW() {
      return this.w;
   }

   public void setW(int w) {
      this.w = w;
   }

   public int getH() {
      return this.h;
   }

   public void setH(int h) {
      this.h = h;
   }

   public CursorBox getcBox() {
      return this.cBox;
   }

   public String getIname() {
      return this.iname;
   }

   public boolean isFromBottom() {
      return this.fromBottom;
   }

   public void setFromBottom(boolean fromBottom) {
      this.fromBottom = fromBottom;
   }
}
