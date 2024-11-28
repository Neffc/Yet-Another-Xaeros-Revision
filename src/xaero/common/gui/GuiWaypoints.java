package xaero.common.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_4185;
import net.minecraft.class_4280;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_3675.class_307;
import net.minecraft.class_4280.class_4281;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IAbstractSelectionList;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.gui.dropdown.DropDownWidget;
import xaero.common.gui.dropdown.IDropDownWidgetCallback;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.common.misc.KeySortableByOther;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;

public class GuiWaypoints extends ScreenBase implements IDropDownWidgetCallback {
   private static final int FRAME_TOP_SIZE = 58;
   private static final int FRAME_BOTTOM_SIZE = 61;
   public static double distanceDivided;
   private GuiWaypoints.List list;
   private MinimapWorld displayedWorld;
   private ConcurrentSkipListSet<Integer> selectedListSet;
   private GuiWaypointContainers containers;
   private GuiWaypointWorlds worlds;
   private GuiWaypointSets sets;
   private DropDownWidget containersDD;
   private DropDownWidget worldsDD;
   private DropDownWidget setsDD;
   private MinimapSession session;
   private MinimapWorldManager manager;
   private int draggingFromX;
   private int draggingFromY;
   private int draggingFromSlot;
   private Waypoint draggingWaypoint;
   private boolean displayingTeleportableWorld;
   private int shiftSelectFirst;
   private ArrayList<Waypoint> waypointsSorted;
   private final XaeroPath frozenAutoWorldPath;
   private class_4185 deleteButton;
   private class_4185 editButton;
   private class_4185 teleportButton;
   private class_4185 disableEnableButton;
   private class_4185 clearButton;
   private class_4185 shareButton;

   @Deprecated
   public GuiWaypoints(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, class_437 par1GuiScreen) {
      this(modMain, minimapSession, par1GuiScreen, null);
   }

   @Deprecated
   public GuiWaypoints(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, class_437 par1GuiScreen, class_437 escapeScreen) {
      this((HudMod)modMain, BuiltInHudModules.MINIMAP.getCurrentSession(), par1GuiScreen, escapeScreen);
   }

   public GuiWaypoints(HudMod modMain, MinimapSession session, class_437 par1GuiScreen, class_437 escapeScreen) {
      super(modMain, par1GuiScreen, escapeScreen, class_2561.method_43471("gui.xaero_waypoints"));
      this.session = session;
      this.manager = session.getWorldManager();
      this.selectedListSet = new ConcurrentSkipListSet<>();
      this.draggingFromX = -1;
      this.draggingFromY = -1;
      this.draggingFromSlot = -1;
      this.frozenAutoWorldPath = session.getWorldState().getAutoWorldPath();
      this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
      XaeroPath currentContainer = this.displayedWorld.getContainer().getRoot().getPath();
      this.containers = new GuiWaypointContainers(modMain, this.manager, currentContainer, this.frozenAutoWorldPath);
      this.worlds = new GuiWaypointWorlds(
         this.manager.getRootWorldContainer(this.containers.getCurrentKey()), session, this.displayedWorld.getFullPath(), this.frozenAutoWorldPath
      );
      this.displayingTeleportableWorld = session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
      this.waypointsSorted = new ArrayList<>();
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.updateSortedList();
      this.list = new GuiWaypoints.List();
      this.sets = new GuiWaypointSets(true, this.displayedWorld, this.displayedWorld.getCurrentWaypointSetId());
      this.method_25429(this.list);
      this.method_37063(
         this.deleteButton = new MyTinyButton(this.field_22789 / 2 + 129, this.field_22790 - 53, class_2561.method_43471("gui.xaero_delete"), b -> {
            if (this.isSomethingSelected()) {
               this.undrag();
               boolean shouldRestore = true;

               for (int i : this.selectedListSet) {
                  Waypoint w = this.list.getWaypoint(i);
                  if (!w.isTemporary()) {
                     shouldRestore = false;
                     w.setTemporary(true);
                  }
               }

               if (shouldRestore) {
                  for (int ix : this.selectedListSet) {
                     Waypoint w = this.list.getWaypoint(ix);
                     w.setTemporary(false);
                  }
               }

               try {
                  this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
               } catch (IOException var6) {
                  MinimapLogs.LOGGER.error("suppressed exception", var6);
               }
            }
         })
      );
      this.method_37063(
         class_4185.method_46430(class_2561.method_43469("gui.done", new Object[0]), b -> this.goBack())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 - 29, 200, 20)
            .method_46431()
      );
      this.method_37063(
         this.editButton = new MyTinyButton(
            this.field_22789 / 2 - 203,
            this.field_22790 - 53,
            class_2561.method_43469("gui.xaero_add_edit", new Object[0]),
            b -> {
               if (this.isAddEditEnabled()) {
                  ArrayList<Waypoint> selectedWaypoints = this.getSelectedWaypointsList()
                     .stream()
                     .filter(w -> !w.isServerWaypoint())
                     .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                  this.field_22787
                     .method_1507(
                        new GuiAddWaypoint(
                           (HudMod)this.modMain,
                           this.session,
                           this,
                           this.escape,
                           selectedWaypoints,
                           this.displayedWorld.getContainer().getRoot().getPath(),
                           this.displayedWorld,
                           this.displayedWorld.getCurrentWaypointSetId(),
                           selectedWaypoints.isEmpty()
                        )
                     );
                  this.list.setSelected(null);
               }
            }
         )
      );
      this.method_37063(
         this.teleportButton = new MyTinyButton(
            this.field_22789 / 2 - 120,
            this.field_22790 - 53,
            class_2561.method_43470(class_1074.method_4662("gui.xaero_waypoint_teleport", new Object[0]) + " (T)"),
            b -> {
               if (this.canTeleport()) {
                  this.displayingTeleportableWorld = this.session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
                  this.session
                     .getWaypointSession()
                     .getTeleport()
                     .teleportToWaypoint(this.list.getWaypoint(this.selectedListSet.first()), this.displayedWorld, this);
               }
            }
         )
      );
      this.method_37063(
         this.disableEnableButton = new MyTinyButton(
            this.field_22789 / 2 + 46, this.field_22790 - 53, class_2561.method_43469("gui.xaero_disable_enable", new Object[0]), b -> {
               if (this.isSomethingSelected()) {
                  ArrayList<Waypoint> selectedWaypoints = this.getSelectedWaypointsList();
                  if (allWaypointsAre(selectedWaypoints, Waypoint::isTemporary)) {
                     for (Waypoint selected : selectedWaypoints) {
                        this.displayedWorld.getCurrentWaypointSet().remove(selected);
                     }

                     this.selectedListSet.clear();
                  } else if (allWaypointsAre(selectedWaypoints, Waypoint::isDisabled)) {
                     for (Waypoint selected : selectedWaypoints) {
                        selected.setDisabled(false);
                     }
                  } else {
                     for (Waypoint selected : selectedWaypoints) {
                        selected.setDisabled(true);
                     }
                  }

                  this.updateSortedList();

                  try {
                     this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
                  } catch (IOException var5) {
                     MinimapLogs.LOGGER.error("suppressed exception", var5);
                  }
               }
            }
         )
      );
      this.method_37063(
         this.clearButton = new MyTinyButton(
            this.field_22789 / 2 + 130,
            32,
            class_2561.method_43469("gui.xaero_clear", new Object[0]),
            b -> {
               XaeroPath worldKeys = this.worlds.getCurrentKey();
               String name = this.sets.getOptions()[this.sets.getCurrentSet()];
               if (this.shouldDeleteSet()) {
                  this.field_22787
                     .method_1507(new GuiDeleteSet(class_1074.method_4662(name, new Object[0]), worldKeys, name, this, this.escape, this.modMain, this.session));
               } else {
                  this.field_22787
                     .method_1507(new GuiClearSet(class_1074.method_4662(name, new Object[0]), worldKeys, name, this, this.escape, this.modMain, this.session));
               }
            }
         )
      );
      this.method_37063(
         new MyTinyButton(
            this.field_22789 / 2 - 203,
            32,
            class_2561.method_43469("gui.xaero_options", new Object[0]),
            b -> this.field_22787
                  .method_1507(new GuiWaypointsOptions(this.modMain, this.session, this, this.escape, this.displayedWorld, this.frozenAutoWorldPath))
         )
      );
      this.method_37063(
         this.shareButton = new MyTinyButton(
            this.field_22789 / 2 - 37, this.field_22790 - 53, class_2561.method_43469("gui.xaero_share", new Object[0]), b -> {
               if (this.isOneSelected()) {
                  Waypoint selected = this.selectedListSet.isEmpty() ? null : this.list.getWaypoint(this.selectedListSet.first());
                  if (selected != null) {
                     this.session.getWaypointSession().getSharing().shareWaypoint(this, selected, this.displayedWorld);
                  }
               }
            }
         )
      );
      this.method_25429(this.containersDD = this.createContainersDropdown());
      this.method_25429(this.worldsDD = this.createWorldsDropdown());
      this.method_25429(this.setsDD = this.createSetsDropdown());
   }

   private DropDownWidget createSetsDropdown() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.sets.getOptions())
         .setX(this.field_22789 / 2 - 100)
         .setY(33)
         .setW(200)
         .setSelected(this.sets.getCurrentSet())
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_set"))
         .build();
   }

   private DropDownWidget createContainersDropdown() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.containers.options)
         .setX(this.field_22789 / 2 - 202)
         .setY(17)
         .setW(200)
         .setSelected(this.containers.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_container"))
         .build();
   }

   private DropDownWidget createWorldsDropdown() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.worlds.options)
         .setX(this.field_22789 / 2 + 2)
         .setY(17)
         .setW(200)
         .setSelected(this.worlds.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_world"))
         .build();
   }

   private ArrayList<Waypoint> getSelectedWaypointsList() {
      return this.selectedListSet.stream().map(i -> this.list.getWaypoint(i)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
   }

   public static boolean allWaypointsAre(ArrayList<Waypoint> waypoints, Predicate<Waypoint> predicate) {
      boolean allTrue = true;

      for (Waypoint w : waypoints) {
         if (!predicate.test(w)) {
            allTrue = false;
            break;
         }
      }

      return allTrue;
   }

   public boolean shouldDeleteSet() {
      return !this.sets.getOptions()[this.sets.getCurrentSet()].equals("gui.xaero_default") && this.displayedWorld.getCurrentWaypointSet().isEmpty();
   }

   private void undrag() {
      this.draggingFromX = -1;
      this.draggingFromY = -1;
      this.draggingFromSlot = -1;
      this.draggingWaypoint = null;
   }

   @Override
   public boolean method_25402(double par1, double par2, int par3) {
      if (this.openDropdown == null) {
         if (Misc.inputMatchesKeyBinding(this.modMain, class_307.field_1672, par3, ModSettings.keyWaypoints, 0)) {
            this.goBack();
            return true;
         }

         if (par3 == 0) {
            if (par2 >= 58.0 && par2 < (double)(this.field_22790 - 61) && this.displayedWorld.getRootConfig().getSortType() == WaypointsSort.NONE) {
               this.draggingFromX = (int)par1;
               this.draggingFromY = (int)par2;
               this.draggingFromSlot = this.list.getEntryAt(par1, par2);
               if (this.draggingFromSlot >= this.displayedWorld.getCurrentWaypointSet().size()) {
                  this.draggingFromSlot = -1;
               }
            }
         } else {
            this.list.setSelected(null);
         }
      }

      return super.method_25402(par1, par2, par3);
   }

   @Override
   public boolean method_25406(double par1, double par2, int par3) {
      try {
         if (this.draggingWaypoint != null) {
            this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
         }
      } catch (IOException var7) {
         MinimapLogs.LOGGER.error("suppressed exception", var7);
      }

      this.undrag();
      return !super.method_25406(par1, par2, par3) ? this.list.method_25406(par1, par2, par3) : true;
   }

   public boolean method_16803(int par1, int par2, int par3) {
      switch (par1) {
         case 84:
            if (this.teleportButton.field_22763) {
               this.teleportButton.method_25348(0.0, 0.0);
            }

            return true;
         case 261:
            if (this.disableEnableButton.field_22763) {
               for (int i : this.selectedListSet) {
                  this.list.getWaypoint(i).setTemporary(true);
               }

               this.disableEnableButton.method_25348(0.0, 0.0);
            }

            return true;
         default:
            return super.method_16803(par1, par2, par3);
      }
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      if (this.field_22787.field_1724 == null) {
         this.field_22787.method_1507(null);
      } else {
         this.updateButtons();
         this.list.method_25394(guiGraphics, par1, par2, par3);
         super.method_25394(guiGraphics, par1, par2, par3);
      }
   }

   @Override
   protected void renderPreDropdown(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
      guiGraphics.method_25300(this.field_22793, class_1074.method_4662("gui.xaero_world_server", new Object[0]), this.field_22789 / 2 - 102, 5, 16777215);
      guiGraphics.method_25300(this.field_22793, class_1074.method_4662("gui.xaero_subworld_dimension", new Object[0]), this.field_22789 / 2 + 102, 5, 16777215);
      if (this.draggingFromSlot != -1) {
         int distance = (int)Math.sqrt(Math.pow((double)(mouseX - this.draggingFromX), 2.0) + Math.pow((double)(mouseY - this.draggingFromY), 2.0));
         int toSlot = Math.min(this.displayedWorld.getCurrentWaypointSet().size() - 1, this.list.getEntryAt((double)mouseX, (double)mouseY));
         if (distance > 4 && this.draggingWaypoint == null) {
            this.draggingWaypoint = this.displayedWorld.getCurrentWaypointSet().get(this.draggingFromSlot);
            this.list.setSelected(null);
         }

         if (this.draggingWaypoint != null && this.draggingFromSlot != toSlot && toSlot != -1) {
            int direction = toSlot > this.draggingFromSlot ? 1 : -1;

            for (int i = this.draggingFromSlot; i != toSlot; i += direction) {
               this.displayedWorld.getCurrentWaypointSet().set(i, this.displayedWorld.getCurrentWaypointSet().get(i + direction));
            }

            this.displayedWorld.getCurrentWaypointSet().set(toSlot, this.draggingWaypoint);
            this.draggingFromSlot = toSlot;
            this.updateSortedList();
         }

         int fromCenter = this.draggingFromX - ((IAbstractSelectionList)this.list).getWidth() / 2;
         this.list.drawWaypointSlot(guiGraphics, this.draggingWaypoint, mouseX - 108 - fromCenter, mouseY - this.list.getItemHeight() / 4);
      }
   }

   private void updateButtons() {
      this.deleteButton.field_22763 = this.disableEnableButton.field_22763 = this.isSomethingSelected();
      this.shareButton.field_22763 = this.isOneSelected();
      this.teleportButton.field_22763 = this.canTeleport();
      this.editButton.field_22763 = this.isAddEditEnabled();
      this.clearButton.method_25355(class_2561.method_43469(this.shouldDeleteSet() ? "gui.xaero_delete_set" : "gui.xaero_clear", new Object[0]));
      ArrayList<Waypoint> selectedWaypointsList = this.getSelectedWaypointsList();
      if (this.isSomethingSelected() && allWaypointsAre(selectedWaypointsList, Waypoint::isTemporary)) {
         this.disableEnableButton.method_25355(class_2561.method_43471("gui.xaero_delete"));
         this.deleteButton.method_25355(class_2561.method_43471("gui.xaero_restore"));
      } else {
         this.deleteButton.method_25355(class_2561.method_43471("gui.xaero_delete"));
         String[] enabledisable = class_1074.method_4662("gui.xaero_disable_enable", new Object[0]).split("/");
         this.disableEnableButton.method_25355(class_2561.method_43470(enabledisable[!allWaypointsAre(selectedWaypointsList, Waypoint::isDisabled) ? 0 : 1]));
      }
   }

   private boolean isAddEditEnabled() {
      ArrayList<Waypoint> selectedWaypointsList = this.getSelectedWaypointsList();
      return selectedWaypointsList.isEmpty() || !allWaypointsAre(selectedWaypointsList, Waypoint::isServerWaypoint);
   }

   private boolean isSomethingSelected() {
      return !this.selectedListSet.isEmpty();
   }

   private boolean isOneSelected() {
      return this.selectedListSet.size() == 1;
   }

   private boolean canTeleport() {
      return this.isOneSelected()
         && (this.modMain.getSettings().allowWrongWorldTeleportation || this.displayingTeleportableWorld)
         && this.displayedWorld.getRootConfig().isTeleportationEnabled();
   }

   @Override
   public boolean onSelected(DropDownWidget menu, int selectedIndex) {
      if (menu != this.containersDD && menu != this.worldsDD) {
         if (menu == this.setsDD) {
            this.list.setSelected(null);
            if (selectedIndex == menu.size() - 1) {
               MinimapLogs.LOGGER.info("New waypoint set gui");
               this.field_22787.method_1507(new GuiNewSet(this.modMain, this.session, this, this.escape, this.displayedWorld));
               return false;
            } else {
               this.sets.setCurrentSet(selectedIndex);
               this.displayedWorld.setCurrentWaypointSetId(this.sets.getCurrentSetKey());
               this.updateSortedList();

               try {
                  this.session.getWorldManagerIO().saveWorld(this.displayedWorld);
               } catch (IOException var4) {
                  MinimapLogs.LOGGER.error("suppressed exception", var4);
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         if (menu == this.containersDD) {
            this.containers.current = selectedIndex;
            if (this.containers.current != this.containers.auto) {
               MinimapWorld firstWorld = this.manager.getRootWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
               this.session.getWorldState().setCustomWorldPath(firstWorld.getFullPath());
            } else {
               this.session.getWorldState().setCustomWorldPath(null);
            }

            this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
            this.updateSortedList();
            this.worlds = new GuiWaypointWorlds(
               this.manager.getRootWorldContainer(this.containers.getCurrentKey()), this.session, this.displayedWorld.getFullPath(), this.frozenAutoWorldPath
            );
            this.replaceWidget(this.worldsDD, this.worldsDD = this.createWorldsDropdown());
         } else {
            this.worlds.current = selectedIndex;
            if (this.worlds.current != this.worlds.auto) {
               XaeroPath selectedWorldPath = this.worlds.getCurrentKey();
               this.session.getWorldState().setCustomWorldPath(selectedWorldPath);
            } else {
               this.session.getWorldState().setCustomWorldPath(null);
            }

            this.displayedWorld = this.manager.getCurrentWorld(this.frozenAutoWorldPath);
            this.updateSortedList();
         }

         this.displayingTeleportableWorld = this.session.getWaypointSession().getTeleport().isWorldTeleportable(this.displayedWorld);
         this.list.setSelected(null);
         this.sets = new GuiWaypointSets(true, this.displayedWorld, this.displayedWorld.getCurrentWaypointSetId());
         this.replaceWidget(this.setsDD, this.setsDD = this.createSetsDropdown());
         return true;
      }
   }

   private void updateSortedList() {
      WaypointsSort sortType = this.displayedWorld.getRootConfig().getSortType();
      this.waypointsSorted = new ArrayList<>();
      if (sortType == WaypointsSort.NONE) {
         for (Waypoint waypoint : this.displayedWorld.getCurrentWaypointSet().getWaypoints()) {
            this.waypointsSorted.add(waypoint);
         }
      } else {
         distanceDivided = this.session.getDimensionHelper().getDimensionDivision(this.displayedWorld);
         ArrayList<KeySortableByOther<Waypoint>> sortableKeys = new ArrayList<>();
         class_4184 camera = this.field_22787.field_1773.method_19418();

         for (Waypoint w : this.displayedWorld.getCurrentWaypointSet().getWaypoints()) {
            sortableKeys.add(
               new KeySortableByOther<>(
                  w,
                  (Comparable)(sortType == WaypointsSort.COLOR
                     ? w.getWaypointColor()
                     : (
                        sortType == WaypointsSort.ANGLE
                           ? -w.getComparisonAngleCos(camera, distanceDivided)
                           : (
                              sortType == WaypointsSort.NAME
                                 ? w.getComparisonName()
                                 : (sortType == WaypointsSort.SYMBOL ? w.getInitials() : w.getComparisonDistance(camera, distanceDivided))
                           )
                     ))
               )
            );
         }

         Collections.sort(sortableKeys);

         for (KeySortableByOther<Waypoint> k : sortableKeys) {
            this.waypointsSorted.add(k.getKey());
         }

         if (this.displayedWorld.getRootConfig().isSortReversed()) {
            Collections.reverse(this.waypointsSorted);
         }
      }
   }

   public boolean method_25404(int par1, int par2, int par3) {
      if (!super.method_25404(par1, par2, par3)) {
         if (Misc.inputMatchesKeyBinding(
            this.modMain, par1 != -1 ? class_307.field_1668 : class_307.field_1671, par1 != -1 ? par1 : par2, ModSettings.keyWaypoints, 0
         )) {
            this.goBack();
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   class List extends class_4280<GuiWaypoints.List.WaypointEntry> {
      private int createdCount;

      public List() {
         super(
            GuiWaypoints.this.field_22787,
            GuiWaypoints.this.field_22789,
            GuiWaypoints.this.field_22790,
            58,
            Math.max(62, GuiWaypoints.this.field_22790 - 61),
            18
         );
         this.createEntries(this.getWaypointCount());
      }

      protected int getWaypointCount() {
         int size = GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size();
         return size + GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().size();
      }

      private Waypoint getWaypoint(int slotIndex) {
         if (slotIndex < GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size()) {
            return GuiWaypoints.this.waypointsSorted.get(slotIndex);
         } else {
            int serverWPIndex = slotIndex - GuiWaypoints.this.displayedWorld.getCurrentWaypointSet().size();
            return serverWPIndex < GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().size()
               ? GuiWaypoints.this.displayedWorld.getContainer().getServerWaypointManager().getBySlot(serverWPIndex)
               : null;
         }
      }

      protected boolean method_25332(int p_148131_1_) {
         return !GuiWaypoints.this.selectedListSet.isEmpty() && GuiWaypoints.this.selectedListSet.contains(p_148131_1_);
      }

      protected void method_25325(class_332 guiGraphics) {
         GuiWaypoints.this.method_25420(guiGraphics);
      }

      private void createEntries(int count) {
         this.method_25339();
         this.createdCount = count;

         for (int i = 0; i < count; i++) {
            GuiWaypoints.List.WaypointEntry entry = new GuiWaypoints.List.WaypointEntry(i);
            this.method_25321(entry);
         }
      }

      public void method_25394(class_332 guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
         int currentCount = this.getWaypointCount();
         if (currentCount != this.createdCount) {
            this.createEntries(currentCount);
            this.method_25307(this.method_25341());
         }

         super.method_25394(guiGraphics, p_render_1_, p_render_2_, p_render_3_);
      }

      public boolean method_25370() {
         return GuiWaypoints.this.openDropdown == null && GuiWaypoints.this.draggingWaypoint == null ? GuiWaypoints.this.method_25399() == this : false;
      }

      public void setSelected(GuiWaypoints.List.WaypointEntry e) {
         if (e == null) {
            GuiWaypoints.this.selectedListSet.clear();
            GuiWaypoints.this.shiftSelectFirst = -1;
         } else {
            Waypoint waypoint = this.getWaypoint(e.index);
            int currentSize = GuiWaypoints.this.selectedListSet.size();
            boolean shiftPressed = class_437.method_25442();
            if ((currentSize > 1 || currentSize == 1 && GuiWaypoints.this.selectedListSet.first() != e.index) && !class_437.method_25441() && !shiftPressed) {
               GuiWaypoints.this.selectedListSet.clear();
            }

            if (currentSize > 0 && shiftPressed) {
               int direction = e.index > GuiWaypoints.this.shiftSelectFirst ? 1 : -1;
               GuiWaypoints.this.selectedListSet.clear();

               for (int i = GuiWaypoints.this.shiftSelectFirst; i != e.index + direction; i += direction) {
                  GuiWaypoints.this.selectedListSet.add(i);
               }
            } else if (GuiWaypoints.this.selectedListSet.contains(e.index)) {
               GuiWaypoints.this.selectedListSet.remove(e.index);
            } else {
               GuiWaypoints.this.shiftSelectFirst = e.index;
               GuiWaypoints.this.selectedListSet.add(e.index);
            }

            super.method_25313(GuiWaypoints.this.selectedListSet.isEmpty() ? null : e);
         }
      }

      public int getItemHeight() {
         return this.field_22741;
      }

      public void drawWaypointSlot(class_332 guiGraphics, Waypoint w, int p_180791_2_, int p_180791_3_) {
         class_4587 matrixStack = guiGraphics.method_51448();
         if (w != null) {
            matrixStack.method_22903();
            matrixStack.method_46416(0.0F, 0.0F, 1.0F);
            guiGraphics.method_25300(
               GuiWaypoints.this.field_22793,
               w.getLocalizedName()
                  + (
                     w.isDisabled()
                        ? " §4" + class_1074.method_4662("gui.xaero_disabled", new Object[0])
                        : (w.isTemporary() ? " §4" + class_1074.method_4662("gui.xaero_temporary", new Object[0]) : "")
                  ),
               p_180791_2_ + 110,
               p_180791_3_ + 1,
               16777215
            );
            int rectX = p_180791_2_ + 8 + 4;
            int rectY = p_180791_3_ + 6;
            if (w.isGlobal()) {
               guiGraphics.method_25300(GuiWaypoints.this.field_22793, "*", rectX - 25, rectY - 3, 16777215);
            }

            class_4598 renderTypeBuffers = GuiWaypoints.this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
            GuiWaypoints.this.modMain
               .getMinimap()
               .getWaypointGuiRenderer()
               .drawIconOnGUI(
                  guiGraphics,
                  GuiWaypoints.this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().getHelper(),
                  w,
                  GuiWaypoints.this.modMain.getSettings(),
                  rectX,
                  rectY,
                  renderTypeBuffers,
                  renderTypeBuffers.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS)
               );
            renderTypeBuffers.method_22993();
            matrixStack.method_22909();
         }
      }

      public int getEntryAt(double x, double y) {
         GuiWaypoints.List.WaypointEntry entry = (GuiWaypoints.List.WaypointEntry)this.method_25308(x, y);
         return entry == null ? -1 : entry.index;
      }

      public class WaypointEntry extends class_4281<GuiWaypoints.List.WaypointEntry> {
         private int index;

         public WaypointEntry(int index) {
            this.index = index;
         }

         public void method_25343(
            class_332 guiGraphics,
            int index,
            int p_render_2_,
            int p_render_3_,
            int p_render_4_,
            int p_render_5_,
            int p_render_6_,
            int p_render_7_,
            boolean p_render_8_,
            float p_render_9_
         ) {
            Waypoint w = List.this.getWaypoint(index);
            if (w != GuiWaypoints.this.draggingWaypoint) {
               List.this.drawWaypointSlot(guiGraphics, w, p_render_3_, p_render_2_);
            }
         }

         public boolean method_25402(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            return p_mouseClicked_5_ == 0;
         }

         public class_2561 method_37006() {
            Waypoint w = List.this.getWaypoint(this.index);
            String narration = "";
            if (w != null) {
               narration = narration
                  + class_1074.method_4662("narrator.select", new Object[]{w.getName()})
                  + (w.isDisabled() ? " " + class_1074.method_4662("gui.xaero_disabled", new Object[0]) : "")
                  + (w.isTemporary() ? " " + class_1074.method_4662("gui.xaero_temporary", new Object[0]) : "")
                  + ", ";
            }

            if (GuiWaypoints.this.selectedListSet.size() != 1) {
               narration = narration
                  + class_1074.method_4662(
                     "narrator.select",
                     new Object[]{class_1074.method_4662("gui.xaero_waypoints", new Object[0]) + " " + GuiWaypoints.this.selectedListSet.size()}
                  );
            }

            return class_2561.method_43470(narration);
         }
      }
   }
}
