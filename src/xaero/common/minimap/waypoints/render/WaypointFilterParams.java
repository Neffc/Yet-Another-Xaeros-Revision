package xaero.common.minimap.waypoints.render;

import org.joml.Vector3f;

public class WaypointFilterParams {
   public double cameraX;
   public double cameraY;
   public double cameraZ;
   public Vector3f lookVector;
   public double dimDiv;
   public boolean deathpoints;
   public boolean temporaryWaypointsGlobal;
   public double waypointsDistance;
   public double waypointsDistanceMin;
   public double playerY;

   public void setParams(
      double cameraX,
      double cameraY,
      double cameraZ,
      Vector3f lookVector,
      double dimDiv,
      boolean deathpoints,
      boolean temporaryWaypointsGlobal,
      double waypointsDistance,
      double waypointsDistanceMin,
      double playerY
   ) {
      this.cameraX = cameraX;
      this.cameraY = cameraY;
      this.cameraZ = cameraZ;
      this.lookVector = lookVector;
      this.dimDiv = dimDiv;
      this.deathpoints = deathpoints;
      this.temporaryWaypointsGlobal = temporaryWaypointsGlobal;
      this.waypointsDistance = waypointsDistance;
      this.waypointsDistanceMin = waypointsDistanceMin;
      this.playerY = playerY;
   }
}
