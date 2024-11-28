package xaero.common.gui;

public class WaypointEditForm {
   public String name = "";
   public String xText = "";
   public String yText = "";
   public String zText = "";
   public String yawText = "";
   public String initial = "";
   public boolean autoInitial;
   public int disabledOrTemporary;
   public int visibilityType;
   public int color;
   public boolean keepName;
   public boolean keepXText;
   public boolean keepYText;
   public boolean keepZText;
   public boolean keepYawText;
   public boolean keepInitial;
   public boolean keepDisabledOrTemporary;
   public boolean keepVisibilityType;
   public boolean defaultKeepYawText;
   public boolean defaultKeepDisabledOrTemporary;
   public boolean defaultKeepVisibilityType;

   public String getName() {
      return this.name;
   }

   public String getxText() {
      return this.xText;
   }

   public String getyText() {
      return this.yText;
   }

   public String getzText() {
      return this.zText;
   }

   public String getYawText() {
      return this.yawText;
   }

   public String getInitial() {
      return this.initial;
   }

   public int getVisibilityType() {
      return this.visibilityType;
   }

   public int getDisabledOrTemporary() {
      return this.disabledOrTemporary;
   }

   public int getColor() {
      return this.color;
   }
}
