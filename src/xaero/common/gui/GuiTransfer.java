package xaero.common.gui;

import java.util.ArrayList;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.gui.dropdown.DropDownWidget;
import xaero.common.gui.dropdown.IDropDownWidgetCallback;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;

public class GuiTransfer extends ScreenBase implements IDropDownWidgetCallback {
   private MySmallButton transferButton;
   private GuiWaypointContainers containers1;
   private GuiWaypointWorlds worlds1;
   private GuiWaypointContainers containers2;
   private GuiWaypointWorlds worlds2;
   private DropDownWidget containers1DD;
   private DropDownWidget worlds1DD;
   private DropDownWidget containers2DD;
   private DropDownWidget worlds2DD;
   private XaeroMinimapSession minimapSession;
   private WaypointsManager waypointsManager;
   private final String frozenAutoContainerID;
   private final String frozenAutoWorldID;
   private boolean dropped = false;

   public GuiTransfer(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, class_437 par1, class_437 escapeScreen) {
      super(modMain, par1, escapeScreen, class_2561.method_43471("gui.xaero_transfer_all"));
      this.minimapSession = minimapSession;
      this.waypointsManager = minimapSession.getWaypointsManager();
      this.frozenAutoContainerID = this.waypointsManager.getAutoContainerID();
      this.frozenAutoWorldID = this.waypointsManager.getAutoWorldID();
      String currentContainer = this.waypointsManager.getCurrentContainerID(this.frozenAutoContainerID).split("/")[0];
      this.containers1 = new GuiWaypointContainers(modMain, this.waypointsManager, currentContainer, this.frozenAutoContainerID);
      this.containers2 = new GuiWaypointContainers(modMain, this.waypointsManager, currentContainer, this.frozenAutoContainerID);
      String currentWorld = this.waypointsManager.getCurrentContainerAndWorldID(this.frozenAutoContainerID, this.frozenAutoWorldID);
      this.worlds1 = new GuiWaypointWorlds(
         this.waypointsManager.getWorldContainer(this.containers1.getCurrentKey()),
         this.waypointsManager,
         currentWorld,
         this.frozenAutoContainerID,
         this.frozenAutoWorldID
      );
      this.worlds2 = new GuiWaypointWorlds(
         this.waypointsManager.getWorldContainer(this.containers2.getCurrentKey()),
         this.waypointsManager,
         currentWorld,
         this.frozenAutoContainerID,
         this.frozenAutoWorldID
      );
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.method_37063(
         this.transferButton = new MySmallButton(
            5, this.field_22789 / 2 - 155, this.field_22790 / 7 + 120, class_2561.method_43469("gui.xaero_transfer", new Object[0]), b -> this.transfer()
         )
      );
      this.transferButton.field_22763 = false;
      this.method_37063(
         new MySmallButton(
            6, this.field_22789 / 2 + 5, this.field_22790 / 7 + 120, class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> this.openParent()
         )
      );
      this.method_25429(this.worlds1DD = this.createWorlds1DD());
      this.method_25429(this.worlds2DD = this.createWorlds2DD());
      this.method_25429(this.containers1DD = this.createContainers1DD());
      this.method_25429(this.containers2DD = this.createContainers2DD());
   }

   private DropDownWidget createWorlds1DD() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.worlds1.options)
         .setX(this.field_22789 / 2 + 2)
         .setY(this.field_22790 / 7 + 20)
         .setW(200)
         .setSelected(this.worlds1.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_transfer_worlds1"))
         .build();
   }

   private DropDownWidget createWorlds2DD() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.worlds2.options)
         .setX(this.field_22789 / 2 + 2)
         .setY(this.field_22790 / 7 + 50)
         .setW(200)
         .setSelected(this.worlds2.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_transfer_worlds2"))
         .build();
   }

   private DropDownWidget createContainers1DD() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.containers1.options)
         .setX(this.field_22789 / 2 - 202)
         .setY(this.field_22790 / 7 + 20)
         .setW(200)
         .setSelected(this.containers1.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_transfer_containers1"))
         .build();
   }

   private DropDownWidget createContainers2DD() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.containers2.options)
         .setX(this.field_22789 / 2 - 202)
         .setY(this.field_22790 / 7 + 50)
         .setW(200)
         .setSelected(this.containers2.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_transfer_containers2"))
         .build();
   }

   private void openParent() {
      if (this.parent instanceof GuiWaypoints) {
         this.field_22787.method_1507(new GuiWaypoints(this.modMain, this.minimapSession, ((GuiWaypoints)this.parent).parent, this.escape));
      } else {
         this.goBack();
      }
   }

   public void transfer() {
      try {
         String[] keys1 = this.worlds1.getCurrentKeys();
         String[] keys2 = this.worlds2.getCurrentKeys();
         WaypointWorld from = this.waypointsManager.getWorld(keys1[0], keys1[1]);
         WaypointWorld to = this.waypointsManager.getWorld(keys2[0], keys2[1]);
         Object[] keys = from.getSets().keySet().toArray();
         Object[] values = from.getSets().values().toArray();

         for (int i = 0; i < keys.length; i++) {
            String setName = (String)keys[i];
            WaypointSet fromSet = (WaypointSet)values[i];
            WaypointSet toSet = to.getSets().get(setName);
            if (toSet == null) {
               toSet = new WaypointSet(setName);
            }

            ArrayList<Waypoint> list = fromSet.getList();

            for (int j = 0; j < list.size(); j++) {
               Waypoint w = list.get(j);
               Waypoint copy = new Waypoint(w.getX(), w.getY(), w.getZ(), w.getName(), w.getSymbol(), w.getColor(), w.getWaypointType());
               copy.setRotation(w.isRotation());
               copy.setDisabled(w.isDisabled());
               copy.setYaw(w.getYaw());
               toSet.getList().add(copy);
            }

            to.getSets().put(setName, toSet);
         }

         if (keys2[0] != null && !keys2[0].equals(this.waypointsManager.getCustomContainerID())) {
            this.waypointsManager.setCustomContainerID(keys2[0]);
         }

         if (keys2[1] != null && !keys2[1].equals(this.waypointsManager.getCustomWorldID())) {
            this.waypointsManager.setCustomWorldID(keys2[1]);
         }

         this.waypointsManager.updateWaypoints();
         this.openParent();
         this.modMain.getSettings().saveWaypoints(to);
      } catch (Exception var15) {
         MinimapLogs.LOGGER.error("suppressed exception", var15);
      }
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      super.method_25420(guiGraphics);
      super.method_25394(guiGraphics, par1, par2, par3);
   }

   @Override
   protected void renderPreDropdown(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
      guiGraphics.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 5, 16777215);
      guiGraphics.method_25300(
         this.field_22793,
         class_1074.method_4662("gui.xaero_from", new Object[0]).replace("§§", ":") + ":",
         this.field_22789 / 2,
         this.field_22790 / 7 + 10,
         16777215
      );
      guiGraphics.method_25300(
         this.field_22793,
         class_1074.method_4662("gui.xaero_to", new Object[0]).replace("§§", ":") + ":",
         this.field_22789 / 2,
         this.field_22790 / 7 + 40,
         16777215
      );
   }

   @Override
   public boolean onSelected(DropDownWidget menu, int selected) {
      if (menu == this.containers1DD) {
         this.containers1.current = selected;
         this.worlds1 = new GuiWaypointWorlds(
            this.waypointsManager.getWorldContainer(this.containers1.getCurrentKey()),
            this.waypointsManager,
            this.waypointsManager.getCurrentContainerAndWorldID(this.frozenAutoContainerID, this.frozenAutoWorldID),
            this.frozenAutoContainerID,
            this.frozenAutoWorldID
         );
         this.replaceWidget(this.worlds1DD, this.worlds1DD = this.createWorlds1DD());
      } else if (menu == this.containers2DD) {
         this.containers2.current = selected;
         this.worlds2 = new GuiWaypointWorlds(
            this.waypointsManager.getWorldContainer(this.containers2.getCurrentKey()),
            this.waypointsManager,
            this.waypointsManager.getCurrentContainerAndWorldID(this.frozenAutoContainerID, this.frozenAutoWorldID),
            this.frozenAutoContainerID,
            this.frozenAutoWorldID
         );
         this.replaceWidget(this.worlds2DD, this.worlds2DD = this.createWorlds2DD());
      } else if (menu == this.worlds1DD) {
         this.worlds1.current = selected;
      } else if (menu == this.worlds2DD) {
         this.worlds2.current = selected;
      }

      this.transferButton.field_22763 = this.containers1.current != this.containers2.current || this.worlds1.current != this.worlds2.current;
      return true;
   }
}
