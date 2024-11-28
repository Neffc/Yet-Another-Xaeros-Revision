package xaero.common.minimap.waypoints;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_1937;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_2558.class_2559;
import net.minecraft.class_2568.class_5247;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.gui.GuiAddWaypoint;
import xaero.hud.minimap.MinimapLogs;

public class WaypointSharingHandler {
   public static final String WAYPOINT_OLD_SHARE_PREFIX = "xaero_waypoint:";
   public static final String WAYPOINT_ADD_PREFIX = "xaero_waypoint_add:";
   public static final String WAYPOINT_SHARE_PREFIX = "xaero-waypoint:";
   private IXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   private class_437 parent;
   private Waypoint w;
   private WaypointWorld wWorld;

   public WaypointSharingHandler(IXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
   }

   public void shareWaypoint(class_437 parent, Waypoint w, WaypointWorld wWorld) {
      this.parent = parent;
      this.w = w;
      this.wWorld = wWorld;
      class_310.method_1551()
         .method_1507(new class_410(this::confirmResult, class_2561.method_43471("gui.xaero_share_msg1"), class_2561.method_43471("gui.xaero_share_msg2")));
   }

   public void onWaypointReceived(String playerName, String text) {
      text = text.replaceAll("§.", "");
      boolean newFormat = text.contains("xaero-waypoint:");
      String sharePrefix = newFormat ? "xaero-waypoint:" : "xaero_waypoint:";
      String[] args = text.substring(text.indexOf(sharePrefix)).split(":");
      class_5250 component = null;
      if (args.length < 9) {
         MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 0");
      } else {
         if (newFormat) {
            args[1] = this.restoreFormatting(args[1]);
            args[2] = this.restoreFormatting(args[2]);
            if (args.length > 9) {
               args[9] = this.restoreFormatting(args[9]);
            }
         }

         String waypointName = class_1074.method_4662(Waypoint.getStringFromStringSafe(args[1], "^col^"), new Object[0]);
         String dimensionName = null;
         if (args.length > 9 && args[9].startsWith("Internal_")) {
            try {
               String details = args[9].substring(9, args[9].lastIndexOf("_")).replace("^col^", ":");
               if (details.startsWith("dim%")) {
                  if (details.length() == 4) {
                     dimensionName = class_1074.method_4662("gui.xaero_waypoint_unknown_dimension", new Object[0]);
                  } else {
                     class_5321<class_1937> dimId = this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(details);
                     if (dimId == null) {
                        dimensionName = class_1074.method_4662("gui.xaero_waypoint_unknown_dimension", new Object[0]);
                     } else {
                        dimensionName = dimId.method_29177().method_12832();
                     }
                  }
               } else {
                  dimensionName = details;
               }
            } catch (IndexOutOfBoundsException var13) {
            }
         }

         String newText = class_1074.method_4662(
            dimensionName != null ? "gui.xaero_waypoint_shared_dimension" : "gui.xaero_waypoint_shared", new Object[]{playerName, waypointName, dimensionName}
         );
         newText = newText.replaceAll("§r", "§r§7").replaceAll("§f", "§7");
         component = class_2561.method_43470(newText);
         class_5250 hoverComponent = class_2561.method_43470(args[3] + ", " + args[4] + ", " + args[5]);
         StringBuilder addCommandBuilder = new StringBuilder();
         addCommandBuilder.append("xaero_waypoint_add:");
         addCommandBuilder.append(args[1]);

         for (int i = 2; i < args.length; i++) {
            addCommandBuilder.append(':').append(args[i]);
         }

         String addCommand = addCommandBuilder.toString();
         component.method_10862(
            component.method_10866()
               .method_27706(class_124.field_1080)
               .method_10958(new class_2558(class_2559.field_11750, "/" + addCommand))
               .method_10949(new class_2568(class_5247.field_24342, hoverComponent))
         );
      }

      if (component != null) {
         class_310.method_1551().field_1705.method_1743().method_1812(component);
      }
   }

   public void onWaypointAdd(String[] args) {
      String waypointName = Waypoint.getStringFromStringSafe(args[1], "^col^");
      if (waypointName.length() >= 1 && waypointName.length() <= 32) {
         String waypointSymbol = Waypoint.getStringFromStringSafe(args[2], "^col^");
         if (waypointSymbol.length() >= 1 && waypointSymbol.length() <= 3) {
            try {
               if (this.minimapSession.getWaypointsManager().getAutoContainerID() == null) {
                  MinimapLogs.LOGGER.info("Can't add a waypoint at this time!");
               } else {
                  boolean yIsIncluded = !args[4].equals("~");
                  int x = Integer.parseInt(args[3]);
                  int y = yIsIncluded ? Integer.parseInt(args[4]) : 0;
                  int z = Integer.parseInt(args[5]);
                  int color = Integer.parseInt(args[6]);
                  String yawString = args[8];
                  if (yawString.length() > 4) {
                     MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 4");
                  } else {
                     int yaw = Integer.parseInt(yawString);
                     boolean rotation = args[7].equals("true");
                     Waypoint w = new Waypoint(x, y, z, waypointName, waypointSymbol, color, 0, false, yIsIncluded);
                     w.setRotation(rotation);
                     w.setYaw(yaw);
                     String externalContainerId = this.minimapSession.getWaypointsManager().getCurrentContainerID().split("/")[0];
                     WaypointWorld externalWorld = this.minimapSession.getWaypointsManager().getCurrentWorld();
                     String parentContainerId = externalContainerId;
                     WaypointWorld currentWorld = externalWorld;
                     if (args.length > 9) {
                        String worldDetails = args[9];
                        if (worldDetails.length() > 9 && worldDetails.startsWith("Internal_")) {
                           int divider = worldDetails.lastIndexOf(95);
                           if (divider < 1 || divider == worldDetails.length() - 1) {
                              MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 5");
                              return;
                           }

                           String worldId = worldDetails.substring(divider + 1);
                           if (!worldId.replaceAll("[^a-zA-Z0-9,\\$-]+", "").equals(worldId)) {
                              MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 7");
                              return;
                           }

                           boolean destinationDimensionExists = true;

                           String subContainers;
                           try {
                              subContainers = worldDetails.substring(9, divider);
                           } catch (IndexOutOfBoundsException var30) {
                              subContainers = null;
                           }

                           parentContainerId = this.minimapSession.getWaypointsManager().getAutoRootContainerID();
                           String containerId = null;
                           class_5321<class_1937> dimId = null;
                           if (subContainers == null) {
                              containerId = parentContainerId;
                           } else {
                              subContainers = subContainers.replace("^col^", ":");
                              String[] subContainersArgs = subContainers.split("/");
                              if (subContainersArgs.length > 1) {
                                 MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 8");
                                 return;
                              }

                              for (int i = 0; i < subContainersArgs.length; i++) {
                                 String s = subContainersArgs[i];
                                 if (s.isEmpty()) {
                                    MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 11");
                                    return;
                                 }
                              }

                              String dimContainer = subContainersArgs[0];
                              if (!dimContainer.startsWith("dim%")) {
                                 if (!dimContainer.replaceAll("[^a-zA-Z0-9_]+", "").equals(dimContainer)) {
                                    MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 18");
                                    return;
                                 }

                                 dimId = this.minimapSession.getWaypointsManager().findDimensionKey(dimContainer);
                              } else {
                                 dimId = this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(dimContainer);
                              }

                              if (dimId == null) {
                                 MinimapLogs.LOGGER.info("Destination dimension doesn't exists! Handling waypoint as external.");
                                 parentContainerId = externalContainerId;
                                 currentWorld = externalWorld;
                                 destinationDimensionExists = false;
                              } else {
                                 subContainersArgs[0] = this.minimapSession.getWaypointsManager().getDimensionDirectoryName(dimId);
                                 subContainers = String.join("/", subContainersArgs);
                                 containerId = parentContainerId + "/" + subContainers;
                                 WaypointWorldContainer rootContainer = this.minimapSession.getWaypointsManager().getWorldContainer(parentContainerId);
                                 rootContainer.renameOldContainer(containerId);
                              }
                           }

                           if (destinationDimensionExists) {
                              WaypointWorldContainer worldContainer = this.minimapSession.getWaypointsManager().getWorldContainer(containerId);
                              WaypointWorld autoWorld = this.minimapSession.getWaypointsManager().getAutoWorld();
                              if (worldContainer == autoWorld.getContainer()) {
                                 worldId = autoWorld.getId();
                              } else {
                                 WaypointWorld firstWorld = worldContainer.getFirstWorldConnectedTo(autoWorld);
                                 if (firstWorld == null) {
                                    firstWorld = worldContainer.getFirstWorld();
                                 }

                                 if (firstWorld != null) {
                                    worldId = firstWorld.getId();
                                 } else {
                                    worldId = this.minimapSession.getWaypointsManager().getNewAutoWorldID(dimId, false);
                                 }
                              }

                              try {
                                 File securityTest = new File(this.modMain.getWaypointsFolder().getCanonicalFile(), containerId + "/" + worldId + "_1.txt");
                                 if (!securityTest.getPath().equals(securityTest.getCanonicalPath())) {
                                    MinimapLogs.LOGGER.info("Dangerously incorrect format of the shared waypoint! Error: 10");
                                    return;
                                 }
                              } catch (IOException var29) {
                                 MinimapLogs.LOGGER.error("IO error adding a shared waypoint!", var29);
                                 return;
                              }

                              if (this.modMain.getSupportMods().worldmap()
                                 && this.minimapSession.getWaypointsManager().isMultiplayer(containerId)
                                 && dimId != null) {
                                 for (String mw : this.modMain.getSupportMods().worldmapSupport.getMultiworldIds(dimId)) {
                                    this.minimapSession.getWaypointsManager().addWorld(containerId, mw);
                                 }
                              }

                              currentWorld = this.minimapSession.getWaypointsManager().getWorld(containerId, worldId);
                           }
                        } else if (!worldDetails.equals("External")) {
                           MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 12");
                           return;
                        }
                     }

                     class_310.method_1551()
                        .method_1507(
                           new GuiAddWaypoint(
                              this.modMain,
                              this.minimapSession.getWaypointsManager(),
                              null,
                              null,
                              Lists.newArrayList(new Waypoint[]{w}),
                              parentContainerId,
                              currentWorld,
                              currentWorld.getCurrent(),
                              true
                           )
                        );
                  }
               }
            } catch (NumberFormatException var31) {
               MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 3");
            }
         } else {
            MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 2");
         }
      } else {
         MinimapLogs.LOGGER.info("Incorrect format of the shared waypoint! Error: 1");
      }
   }

   public void confirmResult(boolean p_confirmResult_1_) {
      if (p_confirmResult_1_) {
         WaypointWorldContainer rootContainer = this.wWorld.getContainer().getRootContainer();
         WaypointWorldContainer autoRootContainer = this.minimapSession.getWaypointsManager().getAutoWorld().getContainer().getRootContainer();
         String worldDetails;
         if (rootContainer == autoRootContainer) {
            String containerId = this.wWorld.getContainer().getKey();
            int firstSlashIndex = containerId.indexOf("/");
            String details;
            if (firstSlashIndex != -1) {
               String subContainers = containerId.substring(firstSlashIndex + 1);
               String[] subContainersSplit = subContainers.split("/");
               if (subContainersSplit[0].equals("dim%0")) {
                  subContainersSplit[0] = "overworld";
               } else if (subContainersSplit[0].equals("dim%-1")) {
                  subContainersSplit[0] = "the_nether";
               } else if (subContainersSplit[0].equals("dim%1")) {
                  subContainersSplit[0] = "the_end";
               }

               subContainers = String.join("/", subContainersSplit);
               details = subContainers.replace(":", "^col^") + "_waypoints";
            } else {
               details = "waypoints";
            }

            worldDetails = "Internal_" + details;
         } else {
            worldDetails = "External";
         }

         String message = "xaero-waypoint:"
            + this.removeFormatting(this.w.getNameSafe("^col^"))
            + ":"
            + this.removeFormatting(this.w.getSymbolSafe("^col^"))
            + ":"
            + this.w.getX()
            + ":"
            + (this.w.isYIncluded() ? this.w.getY() : "~")
            + ":"
            + this.w.getZ()
            + ":"
            + this.w.getColor()
            + ":"
            + this.w.isRotation()
            + ":"
            + this.w.getYaw()
            + ":"
            + this.removeFormatting(worldDetails);
         class_310.method_1551().field_1705.method_1743().method_1803(message);
         class_310.method_1551().field_1724.field_3944.method_45729(message);
         class_310.method_1551().method_1507(null);
      } else {
         class_310.method_1551().method_1507(this.parent);
      }
   }

   private String removeFormatting(String s) {
      return s.replace("-", "^min^").replace("_", "-").replace("*", "^ast^");
   }

   private String restoreFormatting(String s) {
      return s.replace("^ast^", "*").replace("-", "_").replace("^min^", "-");
   }
}
