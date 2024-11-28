package xaero.common.minimap.waypoints;

import net.minecraft.class_1074;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import org.joml.Vector3f;
import xaero.common.settings.ModSettings;

public class Waypoint implements Comparable<Waypoint> {
   public static final int ONEOFF_DESTINATION_SAFE_FOR = 5000;
   public static final int ONEOFF_DESTINATION_REMOVE_DISTANCE = 4;
   private int x;
   private int y;
   private int z;
   private String name;
   private String symbol;
   private int color;
   private int visibilityType;
   private boolean disabled = false;
   private int type = 0;
   private boolean rotation = false;
   private int yaw = 0;
   private boolean temporary;
   private boolean yIncluded;
   public static class_243 RENDER_SORTING_POS = new class_243(0.0, 0.0, 0.0);
   private final long createdAt;
   private boolean oneoffDestination;

   public Waypoint(int x, int y, int z, String name, String symbol, int color) {
      this(x, y, z, name, symbol, color, 0, false);
   }

   public Waypoint(int x, int y, int z, String name, String symbol, int color, int type) {
      this(x, y, z, name, symbol, color, type, false);
   }

   public Waypoint(int x, int y, int z, String name, String symbol, int color, int type, boolean temp) {
      this(x, y, z, name, symbol, color, type, temp, true);
   }

   public Waypoint(int x, int y, int z, String name, String symbol, int color, int type, boolean temp, boolean yIncluded) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.symbol = symbol;
      this.color = color;
      this.type = type;
      this.name = name;
      this.temporary = temp;
      if (this.type == 1 || this.type == 2) {
         this.visibilityType = 1;
         this.oneoffDestination = true;
      }

      this.yIncluded = yIncluded;
      this.createdAt = System.currentTimeMillis();
   }

   public double getDistanceSq(double x, double y, double z) {
      double d3 = (double)this.x - x;
      double d4 = (double)this.y - y;
      double d5 = (double)this.z - z;
      return d3 * d3 + d4 * d4 + d5 * d5;
   }

   public String getLocalizedName() {
      return class_1074.method_4662(this.name, new Object[0]);
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean b) {
      this.temporary = false;
      this.disabled = b;
   }

   public int getWaypointType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
      if (this.type == 1 || this.type == 2) {
         this.visibilityType = 1;
         this.oneoffDestination = true;
      }
   }

   public int getX() {
      return this.x;
   }

   public int getX(double dimDiv) {
      return dimDiv == 1.0 ? this.x : (int)Math.floor((double)this.x / dimDiv);
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public int getZ(double dimDiv) {
      return dimDiv == 1.0 ? this.z : (int)Math.floor((double)this.z / dimDiv);
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public String getName() {
      return this.name;
   }

   public String getNameSafe(String replacement) {
      return this.getName().replace(":", replacement);
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isTemporary() {
      return this.temporary;
   }

   public void setTemporary(boolean temporary) {
      this.temporary = temporary;
      this.disabled = false;
   }

   public String getSymbol() {
      return this.symbol;
   }

   public void setSymbol(String symbol) {
      this.symbol = symbol;
   }

   public String getSymbolSafe(String replacement) {
      return this.getSymbol().replace(":", replacement);
   }

   public boolean isRotation() {
      return this.rotation;
   }

   public void setRotation(boolean rotation) {
      this.rotation = rotation;
   }

   public int getYaw() {
      return this.yaw;
   }

   public void setYaw(int yaw) {
      this.yaw = yaw;
   }

   public int getColor() {
      return this.color < 0 ? 0 : this.color % ModSettings.COLORS.length;
   }

   public int getActualColor() {
      return this.color % ModSettings.COLORS.length;
   }

   public void setColor(int c) {
      this.color = c;
   }

   public boolean isGlobal() {
      return this.visibilityType == 1 || this.visibilityType == 3;
   }

   public int getVisibilityType() {
      return this.visibilityType;
   }

   public void setVisibilityType(int visibilityType) {
      if (this.type != 1) {
         this.visibilityType = visibilityType;
      }
   }

   public static String getStringFromStringSafe(String stringSafe, String replacement) {
      return stringSafe.replace(replacement, ":");
   }

   public boolean isServerWaypoint() {
      return false;
   }

   public String getComparisonName() {
      String comparisonName = this.getLocalizedName().toLowerCase().trim();
      if (comparisonName.startsWith("the ")) {
         comparisonName = comparisonName.substring(4);
      }

      if (comparisonName.startsWith("a ")) {
         comparisonName = comparisonName.substring(2);
      }

      return comparisonName;
   }

   public double getComparisonDistance(class_4184 camera, double dimDiv) {
      class_243 cameraPos = camera.method_19326();
      double offX = (double)this.getX(dimDiv) - cameraPos.field_1352;
      double offY = !this.isYIncluded() ? 0.0 : (double)this.getY() - cameraPos.field_1351;
      double offZ = (double)this.getZ(dimDiv) - cameraPos.field_1350;
      return offX * offX + offY * offY + offZ * offZ;
   }

   public double getComparisonAngleCos(class_4184 camera, double dimDiv) {
      Vector3f lookVector = camera.method_19335();
      class_243 cameraPos = camera.method_19326();
      double offX = (double)this.getX(dimDiv) - cameraPos.field_1352;
      double offY = !this.isYIncluded() ? 0.0 : (double)this.getY() - cameraPos.field_1351;
      double offZ = (double)this.getZ(dimDiv) - cameraPos.field_1350;
      double distance = Math.sqrt(offX * offX + offY * offY + offZ * offZ);
      return (offX * (double)lookVector.x() + offY * (double)lookVector.y() + offZ * (double)lookVector.z()) / distance;
   }

   private double getRenderSortingDistanceSquared() {
      double fromCameraX = (double)this.x - RENDER_SORTING_POS.field_1352;
      double fromCameraY = this.yIncluded ? (double)this.y - RENDER_SORTING_POS.field_1351 : 0.0;
      double fromCameraZ = (double)this.z - RENDER_SORTING_POS.field_1350;
      return fromCameraX * fromCameraX + fromCameraY * fromCameraY + fromCameraZ * fromCameraZ;
   }

   public int compareTo(Waypoint other) {
      boolean isDeath = this.type == 1 || this.type == 2;
      if (isDeath != (other.type == 1 || other.type == 2)) {
         return isDeath ? 1 : -1;
      } else {
         double rsds = this.getRenderSortingDistanceSquared();
         double otherRsds = other.getRenderSortingDistanceSquared();
         return rsds > otherRsds ? -1 : (rsds == otherRsds ? 0 : 1);
      }
   }

   public boolean isYIncluded() {
      return this.yIncluded;
   }

   public void setYIncluded(boolean yIncluded) {
      this.yIncluded = yIncluded;
   }

   public long getCreatedAt() {
      return this.createdAt;
   }

   public boolean isOneoffDestination() {
      return this.oneoffDestination;
   }

   public void setOneoffDestination(boolean oneoffDestination) {
      this.oneoffDestination = oneoffDestination;
   }
}
