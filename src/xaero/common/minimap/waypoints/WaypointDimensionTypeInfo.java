package xaero.common.minimap.waypoints;

public class WaypointDimensionTypeInfo {
   private final String name;
   private final boolean skyLight;
   private final float ambientLight;
   private final int height;
   private final int logicalHeight;
   private final boolean nether;
   private final boolean surfaceWorld;
   private final boolean end;
   private final float noonCelestialAngle;
   private final double coordinateScale;
   private final boolean forceBrightLightmap;

   public WaypointDimensionTypeInfo(
      String name,
      boolean skyLight,
      float ambientLight,
      int height,
      int logicalHeight,
      boolean nether,
      boolean surfaceWorld,
      boolean end,
      float noonCelestialAngle,
      double coordinateScale,
      boolean forceBrightLightmap
   ) {
      this.name = name;
      this.skyLight = skyLight;
      this.ambientLight = ambientLight;
      this.height = height;
      this.logicalHeight = logicalHeight;
      this.nether = nether;
      this.surfaceWorld = surfaceWorld;
      this.end = end;
      this.noonCelestialAngle = noonCelestialAngle;
      this.coordinateScale = coordinateScale;
      this.forceBrightLightmap = forceBrightLightmap;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasSkyLight() {
      return this.skyLight;
   }

   public float getAmbientLight() {
      return this.ambientLight;
   }

   public int getHeight() {
      return this.height;
   }

   public int getLogicalHeight() {
      return this.logicalHeight;
   }

   public boolean isNether() {
      return this.nether;
   }

   public boolean isSurfaceWorld() {
      return this.surfaceWorld;
   }

   public boolean isEnd() {
      return this.end;
   }

   public float getNoonCelestialAngle() {
      return this.noonCelestialAngle;
   }

   public double getCoordinateScale() {
      return this.coordinateScale;
   }

   public boolean isForceBrightLightmap() {
      return this.forceBrightLightmap;
   }

   @Override
   public String toString() {
      return this.name
         + ":"
         + this.skyLight
         + "$"
         + this.ambientLight
         + "$"
         + this.height
         + "$"
         + this.logicalHeight
         + "$"
         + this.nether
         + "$"
         + this.surfaceWorld
         + "$"
         + this.end
         + "$"
         + this.noonCelestialAngle
         + "$"
         + this.coordinateScale
         + "$"
         + this.forceBrightLightmap;
   }

   public static WaypointDimensionTypeInfo fromString(String name, String s) {
      if (s == null) {
         return null;
      } else {
         try {
            String[] args = s.split("\\$");
            boolean skyLight = args[0].equals("true");
            float ambientLight = Float.parseFloat(args[1]);
            int height = Integer.parseInt(args[2]);
            int logicalHeight = Integer.parseInt(args[3]);
            boolean nether = args[4].equals("true");
            boolean surfaceWorld = args[5].equals("true");
            boolean end = args[6].equals("true");
            float noonCelestialAngle = Float.parseFloat(args[7]);
            double coordinateScale = Double.parseDouble(args[8]);
            boolean forceBrightLightmap = args[9].equals("true");
            return new WaypointDimensionTypeInfo(
               name, skyLight, ambientLight, height, logicalHeight, nether, surfaceWorld, end, noonCelestialAngle, coordinateScale, forceBrightLightmap
            );
         } catch (Throwable var14) {
            return null;
         }
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         WaypointDimensionTypeInfo other = (WaypointDimensionTypeInfo)obj;
         if (this.ambientLight != other.ambientLight) {
            return false;
         } else if (this.coordinateScale != other.coordinateScale) {
            return false;
         } else if (this.end != other.end) {
            return false;
         } else if (this.height != other.height) {
            return false;
         } else if (this.logicalHeight != other.logicalHeight) {
            return false;
         } else {
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.nether != other.nether) {
               return false;
            } else if (this.noonCelestialAngle != other.noonCelestialAngle) {
               return false;
            } else if (this.skyLight != other.skyLight) {
               return false;
            } else {
               return this.surfaceWorld != other.surfaceWorld ? false : this.forceBrightLightmap == other.forceBrightLightmap;
            }
         }
      }
   }
}
