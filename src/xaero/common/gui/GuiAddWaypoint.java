package xaero.common.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5481;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.graphics.CursorBox;
import xaero.common.gui.dropdown.DropDownWidget;
import xaero.common.gui.dropdown.IDropDownWidgetCallback;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointVisibilityType;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.common.validator.NumericFieldValidator;

public class GuiAddWaypoint extends ScreenBase implements IDropDownWidgetCallback {
   private static final CursorBox VISIBILITY_TYPE_TOOLTIP = new CursorBox("gui.xaero_box_visibility_type");
   private static final CursorBox TYPE_TOOLTIP = new CursorBox("gui.xaero_box_waypoint_type");
   private WaypointsManager waypointsManager;
   protected String screenTitle;
   private class_4185 leftButton;
   private class_4185 rightButton;
   private class_4185 modeSwitchButton;
   private class_4185 resetButton;
   private class_342 nameTextField;
   private class_342 xTextField;
   private class_342 yTextField;
   private class_342 zTextField;
   private class_342 yawTextField;
   private class_342 initialTextField;
   private WaypointEditForm mutualForm;
   private ArrayList<WaypointEditForm> editForms;
   private int selectedWaypointIndex;
   private int defaultContainer;
   private WaypointWorld defaultWorld;
   private GuiWaypointContainers containers;
   private GuiWaypointWorlds worlds;
   private GuiWaypointSets sets;
   private DropDownWidget containersDD;
   private DropDownWidget worldsDD;
   private DropDownWidget setsDD;
   private DropDownWidget colorDD;
   private String fromSet;
   private ArrayList<Waypoint> waypointsEdited;
   private class_4185 disableButton;
   private class_4185 visibilityTypeButton;
   private NumericFieldValidator fieldValidator;
   private NumericFieldValidator fieldYValidator;
   private boolean adding;
   private boolean prefilled;
   private boolean startPrefilled;
   private String namePlaceholder;
   private String xPlaceholder;
   private String yPlaceholder;
   private String zPlaceholder;
   private String yawPlaceholder;
   private String initialPlaceholder;
   private String colorPlaceholder;
   private class_4185 defaultYawButton;
   private class_4185 defaultDisabledButton;
   private class_4185 defaultVisibilityTypeButton;
   protected class_4185 confirmButton;
   private boolean censorCoordsIfNeeded;
   private final String frozenAutoContainerID;
   private final String frozenAutoWorldID;
   private BiFunction<String, Integer, String> censoredTextFormatterString;
   private BiFunction<String, Integer, class_5481> censoredTextFormatter;
   private boolean hasForcedPlayerPos;
   private int forcedPlayerX;
   private int forcedPlayerY;
   private int forcedPlayerZ;
   private boolean ignoreEditBoxChanges = true;
   private boolean canBeLabyMod = true;

   public GuiAddWaypoint(
      AXaeroMinimap modMain,
      WaypointsManager waypointsManager,
      class_437 par1GuiScreen,
      class_437 escapeScreen,
      Waypoint point,
      String defaultParentContainer,
      WaypointWorld defaultWorld,
      String waypointSet
   ) {
      this(modMain, waypointsManager, par1GuiScreen, escapeScreen, point, defaultParentContainer, defaultWorld, waypointSet, false, 0, 0, 0);
   }

   public GuiAddWaypoint(
      AXaeroMinimap modMain,
      WaypointsManager waypointsManager,
      class_437 par1GuiScreen,
      class_437 escapeScreen,
      Waypoint point,
      String defaultParentContainer,
      WaypointWorld defaultWorld,
      String waypointSet,
      boolean hasForcedPlayerPos,
      int forcedPlayerX,
      int forcedPlayerY,
      int forcedPlayerZ
   ) {
      this(
         modMain,
         waypointsManager,
         par1GuiScreen,
         escapeScreen,
         point == null ? Lists.newArrayList() : Lists.newArrayList(new Waypoint[]{point}),
         defaultParentContainer,
         defaultWorld,
         waypointSet,
         point == null || point.getActualColor() == -1,
         hasForcedPlayerPos,
         forcedPlayerX,
         forcedPlayerY,
         forcedPlayerZ
      );
   }

   public GuiAddWaypoint(
      AXaeroMinimap modMain,
      WaypointsManager waypointsManager,
      class_437 par1GuiScreen,
      ArrayList<Waypoint> waypointsEdited,
      String defaultParentContainer,
      WaypointWorld defaultWorld,
      boolean adding
   ) {
      this(modMain, waypointsManager, par1GuiScreen, null, waypointsEdited, defaultParentContainer, defaultWorld, defaultWorld.getCurrent(), adding);
   }

   public GuiAddWaypoint(
      AXaeroMinimap modMain,
      WaypointsManager waypointsManager,
      class_437 par1GuiScreen,
      class_437 escapeScreen,
      ArrayList<Waypoint> waypointsEdited,
      String defaultParentContainer,
      WaypointWorld defaultWorld,
      String waypointSet,
      boolean adding
   ) {
      this(modMain, waypointsManager, par1GuiScreen, escapeScreen, waypointsEdited, defaultParentContainer, defaultWorld, waypointSet, adding, false, 0, 0, 0);
   }

   public GuiAddWaypoint(
      AXaeroMinimap modMain,
      WaypointsManager waypointsManager,
      class_437 par1GuiScreen,
      class_437 escapeScreen,
      ArrayList<Waypoint> waypointsEdited,
      String defaultParentContainer,
      WaypointWorld defaultWorld,
      String waypointSet,
      boolean adding,
      boolean hasForcedPlayerPos,
      int forcedPlayerX,
      int forcedPlayerY,
      int forcedPlayerZ
   ) {
      super(modMain, par1GuiScreen, escapeScreen, class_2561.method_43470(""));
      this.hasForcedPlayerPos = hasForcedPlayerPos;
      this.forcedPlayerX = forcedPlayerX;
      this.forcedPlayerY = forcedPlayerY;
      this.forcedPlayerZ = forcedPlayerZ;
      this.waypointsEdited = waypointsEdited;
      this.waypointsManager = waypointsManager;
      this.fromSet = waypointSet;
      this.defaultWorld = defaultWorld;
      this.frozenAutoContainerID = waypointsManager.getAutoContainerID();
      this.frozenAutoWorldID = waypointsManager.getAutoWorldID();
      this.containers = new GuiWaypointContainers(modMain, waypointsManager, defaultParentContainer, this.frozenAutoContainerID);
      this.defaultContainer = this.containers.current;
      this.worlds = new GuiWaypointWorlds(
         waypointsManager.getWorldContainer(defaultParentContainer),
         waypointsManager,
         defaultWorld.getFullId(),
         this.frozenAutoContainerID,
         this.frozenAutoWorldID
      );
      this.sets = new GuiWaypointSets(false, defaultWorld, this.fromSet);
      this.startPrefilled = this.prefilled = !waypointsEdited.isEmpty();
      this.createForms();
      this.fieldValidator = modMain.getFieldValidators().getNumericFieldValidator();
      this.fieldYValidator = modMain.getFieldValidators().getWpCoordFieldValidator();
      this.adding = adding;
      this.namePlaceholder = "- " + class_1074.method_4662("gui.xaero_waypoint_name", new Object[0]);
      this.xPlaceholder = "- x";
      this.yPlaceholder = "- y";
      this.zPlaceholder = "- z";
      this.yawPlaceholder = "- " + class_1074.method_4662("gui.xaero_yaw", new Object[0]);
      this.initialPlaceholder = "- " + class_1074.method_4662("gui.xaero_initial", new Object[0]);
      this.colorPlaceholder = "§8-";
      this.censorCoordsIfNeeded = true;
      this.censoredTextFormatterString = (p_195610_0_, p_195610_1_) -> {
         if (!this.censorCoordsIfNeeded) {
            return p_195610_0_;
         } else {
            int formatIndex = p_195610_0_.indexOf("§".charAt(0));
            return formatIndex == -1
               ? p_195610_0_.replaceAll(".", "#")
               : p_195610_0_.substring(0, formatIndex).replaceAll(".", "#") + p_195610_0_.substring(formatIndex);
         }
      };
      class_2583 defaultTextStyle = class_2583.field_24360;
      this.censoredTextFormatter = (s, i) -> {
         String censoredString = this.censoredTextFormatterString.apply(s, i);
         return cc -> {
            for (int j = 0; j < censoredString.length(); j++) {
               cc.accept(j, defaultTextStyle, censoredString.charAt(j));
            }

            return true;
         };
      };
      this.canSkipWorldRender = true;
   }

   private void fillFormWaypoint(WaypointEditForm form, Waypoint w) {
      form.name = w.getLocalizedName();
      form.xText = w.getX() + "";
      form.yText = w.isYIncluded() ? w.getY() + "" : "~";
      form.zText = w.getZ() + "";
      form.yawText = w.isRotation() ? w.getYaw() + "" : "";
      form.initial = StringConcatFactory.makeConcatWithConstants<"makeConcatWithConstants","\u0001">(w.getSymbol());
      form.disabledOrTemporary = w.isOneoffDestination() ? 3 : (w.isTemporary() ? 2 : (w.isDisabled() ? 1 : 0));
      form.color = 1 + (w.getActualColor() == -1 ? (int)(Math.random() * (double)(ModSettings.ENCHANT_COLORS.length - 1)) : w.getActualColor());
      form.visibilityType = w.getVisibilityType();
      if (form.initial.length() == 0) {
         form.autoInitial = true;
      }
   }

   private int getAutomaticX(double dimDiv) {
      int playerX = this.hasForcedPlayerPos ? this.forcedPlayerX : OptimizedMath.myFloor(this.field_22787.field_1719.method_23317());
      return OptimizedMath.myFloor((double)playerX * dimDiv);
   }

   private int getAutomaticY() {
      int playerY = this.hasForcedPlayerPos ? this.forcedPlayerY : OptimizedMath.myFloor(this.field_22787.field_1719.method_23318() + 0.0625);
      return OptimizedMath.myFloor((double)playerY);
   }

   private int getAutomaticZ(double dimDiv) {
      int playerZ = this.hasForcedPlayerPos ? this.forcedPlayerZ : OptimizedMath.myFloor(this.field_22787.field_1719.method_23321());
      return OptimizedMath.myFloor((double)playerZ * dimDiv);
   }

   private void fillFormAutomatic(WaypointEditForm form) {
      form.xText = "";
      form.yText = this.forcedPlayerY == 32767 ? "~" : "";
      form.zText = "";
      form.color = (int)(Math.random() * (double)(ModSettings.ENCHANT_COLORS.length - 1)) + 1;
      form.autoInitial = true;
   }

   private void createForms() {
      this.editForms = new ArrayList<>();
      this.mutualForm = new WaypointEditForm();

      for (int i = 0; i < this.waypointsEdited.size(); i++) {
         Waypoint w = this.waypointsEdited.get(i);
         WaypointEditForm form = new WaypointEditForm();
         this.fillFormWaypoint(form, w);
         this.editForms.add(form);
      }

      if (!this.startPrefilled) {
         WaypointEditForm createdForm = new WaypointEditForm();
         this.fillFormAutomatic(createdForm);
         this.editForms.add(createdForm);
      }

      this.updateMutual();
   }

   private void resetCurrentForm() {
      if (this.selectedWaypointIndex >= this.waypointsEdited.size()) {
         WaypointEditForm freshForm = new WaypointEditForm();
         this.fillFormAutomatic(freshForm);
         this.editForms.set(this.selectedWaypointIndex, freshForm);
      } else {
         Waypoint w = this.waypointsEdited.get(this.selectedWaypointIndex);
         WaypointEditForm freshForm = new WaypointEditForm();
         this.fillFormWaypoint(freshForm, w);
         this.editForms.set(this.selectedWaypointIndex, freshForm);
      }
   }

   private void updateMutual() {
      String nameTextMutual = "";
      String initialMutual = "";
      String yawMutual = "";
      String xTextMutual = "";
      String yTextMutual = "";
      String zTextMutual = "";
      int waypointDisabledOrTemporaryMutual = 0;
      int waypointVisibilityTypeMutual = 0;
      int colorMutual = 0;
      if (this.differentValues(WaypointEditForm::getColor)) {
         colorMutual = 0;
      } else {
         colorMutual = this.editForms.get(0).color;
      }

      xTextMutual = "";
      yTextMutual = "";
      zTextMutual = "";
      WaypointEditForm firstForm = this.editForms.get(0);
      this.mutualForm.keepName = this.differentValues(WaypointEditForm::getName);
      this.mutualForm.keepXText = this.editForms.size() > 1 && firstForm.xText.isEmpty() || this.differentValues(WaypointEditForm::getxText);
      this.mutualForm.keepYText = this.editForms.size() > 1 && firstForm.yText.isEmpty() || this.differentValues(WaypointEditForm::getyText);
      this.mutualForm.keepZText = this.editForms.size() > 1 && firstForm.zText.isEmpty() || this.differentValues(WaypointEditForm::getzText);
      this.mutualForm.defaultKeepYawText = this.mutualForm.keepYawText = this.differentValues(WaypointEditForm::getYawText);
      this.mutualForm.keepInitial = this.differentValues(WaypointEditForm::getInitial);
      this.mutualForm.autoInitial = this.editForms.size() == 1 && firstForm.autoInitial;
      this.mutualForm.defaultKeepDisabledOrTemporary = this.mutualForm.keepDisabledOrTemporary = this.differentValues(WaypointEditForm::getDisabledOrTemporary);
      this.mutualForm.defaultKeepVisibilityType = this.mutualForm.keepVisibilityType = this.differentValues(WaypointEditForm::getVisibilityType);
      if (!this.mutualForm.keepName) {
         nameTextMutual = firstForm.name;
      }

      if (!this.mutualForm.keepXText) {
         xTextMutual = firstForm.xText;
      }

      if (!this.mutualForm.keepYText) {
         yTextMutual = firstForm.yText;
      }

      if (!this.mutualForm.keepZText) {
         zTextMutual = firstForm.zText;
      }

      if (!this.mutualForm.keepYawText) {
         yawMutual = firstForm.yawText;
      }

      if (!this.mutualForm.keepInitial) {
         initialMutual = firstForm.initial;
      }

      if (!this.mutualForm.keepDisabledOrTemporary) {
         waypointDisabledOrTemporaryMutual = firstForm.disabledOrTemporary;
      }

      if (!this.mutualForm.keepVisibilityType) {
         waypointVisibilityTypeMutual = firstForm.visibilityType;
      }

      this.mutualForm.name = nameTextMutual;
      this.mutualForm.xText = xTextMutual;
      this.mutualForm.yText = yTextMutual;
      this.mutualForm.zText = zTextMutual;
      this.mutualForm.yawText = yawMutual;
      this.mutualForm.initial = initialMutual;
      this.mutualForm.disabledOrTemporary = waypointDisabledOrTemporaryMutual;
      this.mutualForm.visibilityType = waypointVisibilityTypeMutual;
      this.mutualForm.color = colorMutual;
   }

   private void confirmMutual() {
      for (int i = 0; i < this.editForms.size(); i++) {
         WaypointEditForm individualForm = this.editForms.get(i);
         if (!this.mutualForm.keepName) {
            individualForm.name = this.mutualForm.name;
         }

         if (!this.mutualForm.keepXText) {
            individualForm.xText = this.mutualForm.xText;
         }

         if (!this.mutualForm.keepYText) {
            individualForm.yText = this.mutualForm.yText;
         }

         if (!this.mutualForm.keepZText) {
            individualForm.zText = this.mutualForm.zText;
         }

         if (!this.mutualForm.keepYawText) {
            individualForm.yawText = this.mutualForm.yawText;
         }

         if (!this.mutualForm.keepInitial) {
            if (!individualForm.initial.equals(this.mutualForm.initial)) {
               individualForm.autoInitial = false;
            }

            individualForm.initial = this.mutualForm.initial;
         }

         if (!this.mutualForm.keepDisabledOrTemporary) {
            individualForm.disabledOrTemporary = this.mutualForm.disabledOrTemporary;
         }

         if (!this.mutualForm.keepVisibilityType) {
            individualForm.visibilityType = this.mutualForm.visibilityType;
         }

         if (this.mutualForm.color != 0) {
            individualForm.color = this.mutualForm.color;
         }
      }
   }

   private boolean differentValues(Function<WaypointEditForm, Object> s) {
      if (this.editForms.size() == 1) {
         return false;
      } else {
         WaypointEditForm testWaypoint = this.editForms.get(0);

         for (int i = 1; i < this.editForms.size(); i++) {
            WaypointEditForm w = this.editForms.get(i);
            if (!s.apply(w).equals(s.apply(testWaypoint))) {
               return true;
            }
         }

         return false;
      }
   }

   public String[] createColorOptions() {
      boolean unchangedOption = this.getCurrent().color == 0;
      String[] options = new String[ModSettings.ENCHANT_COLOR_NAMES.length + (unchangedOption ? 1 : 0)];
      if (unchangedOption) {
         options[0] = this.colorPlaceholder;
      }

      for (int i = 0; i < ModSettings.ENCHANT_COLOR_NAMES.length; i++) {
         if (i == 0) {
            options[i + (unchangedOption ? 1 : 0)] = class_1074.method_4662(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
         } else {
            options[i + (unchangedOption ? 1 : 0)] = "§"
               + ModSettings.ENCHANT_COLORS[i]
               + class_1074.method_4662(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
         }
      }

      return options;
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.ignoreEditBoxChanges = true;
      this.screenTitle = this.adding
         ? class_1074.method_4662("gui.xaero_new_waypoint", new Object[0])
         : class_1074.method_4662("gui.xaero_edit_waypoint", new Object[0]);
      if (this.editForms.size() > 1) {
         this.screenTitle = this.screenTitle
            + (
               this.editForms.size() > 1
                  ? " (" + (this.modMain.getSettings().waypointsMutualEdit ? "" : this.selectedWaypointIndex + 1 + "/") + this.editForms.size() + ")"
                  : ""
            );
      }

      this.modMain.getInterfaces().setSelectedId(-1);
      this.modMain.getInterfaces().setDraggingId(-1);
      this.nameTextField = this.applyEditBoxResponder(
         new class_342(this.field_22793, this.field_22789 / 2 - 100, 104, 200, 20, class_2561.method_43471("gui.xaero_waypoint_name"))
      );
      this.xTextField = this.applyEditBoxResponder(new class_342(this.field_22793, this.field_22789 / 2 - 109, 134, 50, 20, class_2561.method_43470("x")));
      this.yTextField = this.applyEditBoxResponder(new class_342(this.field_22793, this.field_22789 / 2 - 53, 134, 50, 20, class_2561.method_43470("y")));
      this.zTextField = this.applyEditBoxResponder(new class_342(this.field_22793, this.field_22789 / 2 + 3, 134, 50, 20, class_2561.method_43470("z")));
      if (this.modMain.getSettings().hideWaypointCoordinates) {
         this.xTextField.method_1854(this.censoredTextFormatter);
         this.yTextField.method_1854(this.censoredTextFormatter);
         this.zTextField.method_1854(this.censoredTextFormatter);
      }

      this.yawTextField = this.applyEditBoxResponder(
         new class_342(this.field_22793, this.field_22789 / 2 + 59, 134, 50, 20, class_2561.method_43471("gui.xaero_yaw"))
      );
      this.initialTextField = this.applyEditBoxResponder(
         new class_342(this.field_22793, this.field_22789 / 2 - 25, 164, 50, 20, class_2561.method_43471("gui.xaero_initial"))
      );
      this.method_25429(this.nameTextField);
      this.method_25429(this.xTextField);
      this.method_25429(this.yTextField);
      this.method_25429(this.zTextField);
      this.method_25429(this.yawTextField);
      this.method_25429(this.initialTextField);
      this.method_37063(
         this.confirmButton = new MySmallButton(0, this.field_22789 / 2 - 155, this.field_22790 / 6 + 168, class_2561.method_43471("gui.xaero_confirm"), b -> {
            if (this.modMain.getSettings().waypointsMutualEdit) {
               this.confirmMutual();
            }

            boolean creatingAWaypoint = this.adding && this.waypointsEdited.size() < this.editForms.size();
            double dimDiv = this.waypointsManager.getDimensionDivision(this.worlds.getCurrentKeys()[0]);
            int initialEditedWaypointsSize = this.waypointsEdited.size();

            for (int i = 0; i < this.editForms.size(); i++) {
               boolean shouldCreate = i >= initialEditedWaypointsSize;
               if (!creatingAWaypoint && shouldCreate) {
                  break;
               }

               WaypointEditForm waypointForm = this.editForms.get(i);
               String nameString = waypointForm.name;
               String xString = waypointForm.xText;
               String yString = waypointForm.yText;
               String zString = waypointForm.zText;
               String initialString = waypointForm.initial;
               int colorInt = waypointForm.color;
               boolean yIncluded = !yString.equals("~");
               int x = !xString.equals("-") && !xString.isEmpty() ? Integer.parseInt(xString) : this.getAutomaticX(dimDiv);
               int y = !yString.equals("-") && !yString.isEmpty() ? (!yIncluded ? 0 : Integer.parseInt(yString)) : this.getAutomaticY();
               int z = !zString.equals("-") && !zString.isEmpty() ? Integer.parseInt(zString) : this.getAutomaticZ(dimDiv);
               Waypoint w;
               if (shouldCreate) {
                  w = new Waypoint(x, y, z, nameString, initialString, colorInt - 1, 0, false, yIncluded);
                  this.waypointsEdited.add(w);
               } else {
                  w = this.waypointsEdited.get(i);
                  if (w.getWaypointType() != 1 || !nameString.equals(class_1074.method_4662("gui.xaero_deathpoint", new Object[0]))) {
                     w.setName(nameString);
                     if (w.getWaypointType() != 0) {
                        w.setType(0);
                        w.setOneoffDestination(false);
                     }
                  }

                  w.setX(x);
                  w.setY(y);
                  w.setZ(z);
                  w.setSymbol(initialString);
                  w.setColor(colorInt - 1);
                  w.setYIncluded(yIncluded);
               }

               String yawText = waypointForm.yawText;
               int disableOrTemporary = waypointForm.disabledOrTemporary;
               boolean yawIsUsable = yawText.length() > 0 && !yawText.equals("-");
               w.setRotation(yawIsUsable);
               if (yawIsUsable) {
                  w.setYaw(Integer.parseInt(yawText));
               }

               w.setOneoffDestination(disableOrTemporary == 3);
               w.setDisabled(disableOrTemporary == 1);
               if (disableOrTemporary == 2) {
                  w.setTemporary(true);
               }

               w.setVisibilityType(waypointForm.visibilityType);
            }

            WaypointWorld sourceWorld = this.defaultWorld;
            WaypointSet sourceSet = sourceWorld.getSets().get(this.fromSet);
            String[] destinationWorldKeys = this.worlds.getCurrentKeys();
            String destinationSetKey = this.sets.getCurrentSetKey();
            WaypointWorld destinationWorld = this.waypointsManager.getWorld(destinationWorldKeys[0], destinationWorldKeys[1]);
            WaypointSet destinationSet = destinationWorld.getSets().get(destinationSetKey);
            if (this.adding || sourceSet != destinationSet) {
               if (!this.modMain.getSettings().waypointsBottom) {
                  destinationSet.getList().addAll(0, this.waypointsEdited);
               } else {
                  destinationSet.getList().addAll(this.waypointsEdited);
               }
            }

            if (sourceSet != destinationSet) {
               sourceSet.getList().removeAll(this.waypointsEdited);
            }

            try {
               this.modMain.getSettings().saveWaypoints(sourceWorld);
               if (destinationWorld != sourceWorld) {
                  this.modMain.getSettings().saveWaypoints(destinationWorld);
               }
            } catch (IOException var23) {
               MinimapLogs.LOGGER.error("suppressed exception", var23);
            }

            this.goBack();
         })
      );
      this.method_37063(
         new MySmallButton(
            0, this.field_22789 / 2 + 5, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> this.goBack()
         )
      );
      this.leftButton = class_4185.method_46430(class_2561.method_43470("<"), b -> {
         this.selectedWaypointIndex--;
         if (this.selectedWaypointIndex < 0) {
            this.selectedWaypointIndex = 0;
         }

         boolean restoreFocus = this.method_25399() == b;
         this.method_25423(this.field_22787, this.field_22789, this.field_22790);
         if (restoreFocus) {
            boolean activeBU = this.leftButton.field_22763;
            this.leftButton.field_22763 = true;
            this.method_25395(this.leftButton);
            this.leftButton.field_22763 = activeBU;
         }
      }).method_46434(this.field_22789 / 2 - 203, 104, 20, 20).method_46431();
      this.rightButton = class_4185.method_46430(class_2561.method_43470(">"), b -> {
         this.selectedWaypointIndex++;
         if (this.selectedWaypointIndex >= this.editForms.size()) {
            this.selectedWaypointIndex = this.editForms.size() - 1;
         }

         boolean restoreFocus = this.method_25399() == b;
         this.method_25423(this.field_22787, this.field_22789, this.field_22790);
         if (restoreFocus) {
            boolean activeBU = this.rightButton.field_22763;
            this.rightButton.field_22763 = true;
            this.method_25395(this.rightButton);
            this.rightButton.field_22763 = activeBU;
         }
      }).method_46434(this.field_22789 / 2 + 183, 104, 20, 20).method_46431();
      this.modeSwitchButton = class_4185.method_46430(
            class_2561.method_43471(
               this.modMain.getSettings().waypointsMutualEdit ? "gui.xaero_waypoints_edit_mode_all" : "gui.xaero_waypoints_edit_mode_individually"
            ),
            b -> {
               this.modMain.getSettings().waypointsMutualEdit = !this.modMain.getSettings().waypointsMutualEdit;

               try {
                  this.modMain.getSettings().saveSettings();
               } catch (IOException var4) {
                  MinimapLogs.LOGGER.error("suppressed exception", var4);
               }

               if (this.modMain.getSettings().waypointsMutualEdit) {
                  this.prefilled = true;
                  this.updateMutual();
               } else {
                  this.confirmMutual();
               }

               boolean restoreFocus = this.method_25399() == b;
               this.method_25423(this.field_22787, this.field_22789, this.field_22790);
               if (restoreFocus) {
                  boolean activeBU = this.modeSwitchButton.field_22763;
                  this.modeSwitchButton.field_22763 = true;
                  this.method_25395(this.modeSwitchButton);
                  this.modeSwitchButton.field_22763 = activeBU;
               }
            }
         )
         .method_46434(this.field_22789 / 2 + 106, 56, 99, 20)
         .method_46431();
      if (this.editForms.size() > 1) {
         this.method_37063(this.leftButton);
         this.method_37063(this.rightButton);
         this.method_37063(this.modeSwitchButton);
      }

      this.method_37063(this.resetButton = class_4185.method_46430(class_2561.method_43471("gui.xaero_waypoints_edit_reset"), b -> {
         if (this.modMain.getSettings().waypointsMutualEdit) {
            this.createForms();
            boolean restoreFocus = this.method_25399() == b;
            this.method_25423(this.field_22787, this.field_22789, this.field_22790);
            if (restoreFocus) {
               boolean activeBU = this.resetButton.field_22763;
               this.resetButton.field_22763 = true;
               this.method_25395(this.resetButton);
               this.resetButton.field_22763 = activeBU;
            }
         } else {
            this.resetCurrentForm();
            boolean restoreFocus = this.method_25399() == b;
            this.method_25423(this.field_22787, this.field_22789, this.field_22790);
            if (restoreFocus) {
               boolean activeBU = this.resetButton.field_22763;
               this.resetButton.field_22763 = true;
               this.method_25395(this.resetButton);
               this.resetButton.field_22763 = activeBU;
            }
         }
      }).method_46434(this.field_22789 / 2 - 204, 56, 99, 20).method_46431());
      this.nameTextField.method_1852(this.getCurrent().name);
      this.xTextField.method_1852(this.getCurrent().xText);
      this.yTextField.method_1852(this.getCurrent().yText);
      this.zTextField.method_1852(this.getCurrent().zText);
      this.yawTextField.method_1852(this.getCurrent().yawText);
      this.initialTextField.method_1852(this.getCurrent().initial);
      this.method_37063(this.disableButton = new TooltipButton(this.field_22789 / 2 + 31, 164, 79, 20, this.getDisableButtonText(), b -> {
         this.getCurrent().disabledOrTemporary = (this.getCurrent().disabledOrTemporary + 1) % 4;
         this.disableButton.method_25355(this.getDisableButtonText());
         this.getCurrent().keepDisabledOrTemporary = false;
         if (this.defaultDisabledButton != null) {
            this.defaultDisabledButton.field_22763 = true;
         }
      }, () -> TYPE_TOOLTIP));
      this.method_37063(
         this.visibilityTypeButton = new TooltipButton(
            this.field_22789 / 2 - 109, 164, 79, 20, WaypointVisibilityType.values()[this.getCurrent().visibilityType].getTranslation(), b -> {
               this.getCurrent().visibilityType = (this.getCurrent().visibilityType + 1) % WaypointVisibilityType.values().length;
               this.visibilityTypeButton.method_25355(WaypointVisibilityType.values()[this.getCurrent().visibilityType].getTranslation());
               this.getCurrent().keepVisibilityType = false;
               if (this.defaultVisibilityTypeButton != null) {
                  this.defaultVisibilityTypeButton.field_22763 = true;
               }
            }, () -> VISIBILITY_TYPE_TOOLTIP
         )
      );
      if (this.getCurrent().defaultKeepYawText) {
         this.method_37063(this.defaultYawButton = class_4185.method_46430(class_2561.method_43470("-"), b -> {
            this.getCurrent().keepYawText = true;
            this.getCurrent().yawText = "";
            this.yawTextField.method_1852(this.getCurrent().yawText);
            b.field_22763 = false;
         }).method_46434(this.field_22789 / 2 + 111, 134, 20, 20).method_46431());
         this.defaultYawButton.field_22763 = !this.getCurrent().keepYawText;
      }

      if (this.getCurrent().defaultKeepDisabledOrTemporary) {
         this.method_37063(this.defaultDisabledButton = class_4185.method_46430(class_2561.method_43470("-"), b -> {
            this.getCurrent().keepDisabledOrTemporary = true;
            this.getCurrent().disabledOrTemporary = 0;
            this.disableButton.method_25355(this.getDisableButtonText());
            b.field_22763 = false;
         }).method_46434(this.field_22789 / 2 + 110, 164, 20, 20).method_46431());
         this.defaultDisabledButton.field_22763 = !this.getCurrent().keepDisabledOrTemporary;
      }

      if (this.getCurrent().defaultKeepVisibilityType) {
         this.method_37063(this.defaultVisibilityTypeButton = class_4185.method_46430(class_2561.method_43470("-"), b -> {
            this.getCurrent().keepVisibilityType = true;
            this.getCurrent().visibilityType = 0;
            this.visibilityTypeButton.method_25355(WaypointVisibilityType.values()[this.getCurrent().visibilityType].getTranslation());
            b.field_22763 = false;
         }).method_46434(this.field_22789 / 2 - 130, 164, 20, 20).method_46431());
         this.defaultVisibilityTypeButton.field_22763 = !this.getCurrent().keepVisibilityType;
      }

      if (this.modMain.getSettings().hideWaypointCoordinates) {
         this.method_37063(
            new MySuperTinyButton(
               this.field_22789 / 2 + 115,
               134,
               class_2561.method_43471(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide"),
               b -> {
                  this.censorCoordsIfNeeded = !this.censorCoordsIfNeeded;
                  b.method_25355(class_2561.method_43471(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide"));
               }
            )
         );
      }

      int currentColor = this.getCurrent().color;
      this.colorDD = DropDownWidget.Builder.begin()
         .setOptions(this.createColorOptions())
         .setX(this.field_22789 / 2 - 60)
         .setY(82)
         .setW(120)
         .setSelected(currentColor == 0 ? 0 : currentColor - 1)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_color"))
         .build();
      this.method_25429(this.colorDD);
      this.method_25429(this.setsDD = this.createSetsDropdown());
      this.method_25429(this.containersDD = this.createContainersDropdown());
      this.method_25429(this.worldsDD = this.createWorldsDropdown());
      this.method_25395(this.nameTextField);
      this.nameTextField.method_25365(true);
      this.updateConfirmButton();
   }

   public class_342 applyEditBoxResponder(class_342 box) {
      box.method_1863(s -> {
         if (!this.ignoreEditBoxChanges) {
            this.postType(box);
         }
      });
      return box;
   }

   private DropDownWidget createSetsDropdown() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.sets.getOptions())
         .setX(this.field_22789 / 2 - 101)
         .setY(60)
         .setW(201)
         .setSelected(this.sets.getCurrentSet())
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_set"))
         .build();
   }

   private DropDownWidget createContainersDropdown() {
      return DropDownWidget.Builder.begin()
         .setOptions(this.containers.options)
         .setX(this.field_22789 / 2 - 203)
         .setY(38)
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
         .setY(38)
         .setW(200)
         .setSelected(this.worlds.current)
         .setCallback(this)
         .setContainer(this)
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_waypoint_world"))
         .build();
   }

   private class_2561 getDisableButtonText() {
      return class_2561.method_43471(
         this.getCurrent().disabledOrTemporary == 3
            ? "gui.xaero_destination"
            : (
               this.getCurrent().disabledOrTemporary == 1
                  ? "gui.xaero_toggle_disabled"
                  : (this.getCurrent().disabledOrTemporary == 0 ? "gui.xaero_toggle_enabled" : "gui.xaero_temporary2")
            )
      );
   }

   private WaypointEditForm getCurrent() {
      return this.modMain.getSettings().waypointsMutualEdit ? this.mutualForm : this.editForms.get(this.selectedWaypointIndex);
   }

   public boolean method_25404(int par1, int par2, int par3) {
      class_364 focused = this.method_25399();
      this.preType(focused);
      this.ignoreEditBoxChanges = false;
      boolean result = super.method_25404(par1, par2, par3);
      if (this.ignoreEditBoxChanges) {
         this.canBeLabyMod = false;
      }

      if (!(focused instanceof class_342) || !this.canConfirm() || par1 != 257 && par1 != 335) {
         return result;
      } else {
         this.confirmButton.method_25348(0.0, 0.0);
         return true;
      }
   }

   public boolean method_25400(char par1, int par2) {
      class_364 focused = this.method_25399();
      this.preType(focused);
      this.ignoreEditBoxChanges = false;
      boolean result = super.method_25400(par1, par2);
      if (this.ignoreEditBoxChanges) {
         this.canBeLabyMod = false;
      }

      return result;
   }

   @Override
   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.ignoreEditBoxChanges = false;
      boolean result = super.method_25402(mouseX, mouseY, button);
      if (this.ignoreEditBoxChanges) {
         this.canBeLabyMod = false;
      }

      return result;
   }

   @Override
   public boolean method_25406(double mouseX, double mouseY, int button) {
      this.ignoreEditBoxChanges = false;
      boolean result = super.method_25406(mouseX, mouseY, button);
      if (this.ignoreEditBoxChanges) {
         this.canBeLabyMod = false;
      }

      return result;
   }

   private void preType(class_364 focused) {
      if (focused != null) {
         ;
      }
   }

   private void postType(class_364 focused) {
      this.ignoreEditBoxChanges = true;
      if (focused != null) {
         if (this.nameTextField == focused) {
            if (this.getCurrent().autoInitial
               && this.nameTextField.method_1882().length() > 0
               && (!this.getCurrent().keepInitial || !this.modMain.getSettings().waypointsMutualEdit)) {
               this.initialTextField.method_1852(this.nameTextField.method_1882().substring(0, 1).toUpperCase());
            }
         } else if (this.initialTextField == focused) {
            this.getCurrent().autoInitial = false;
         }

         this.checkFields(focused);
         this.updateConfirmButton();
      }
   }

   public void method_25395(class_364 l) {
      this.preType(l);
      class_364 currentFocused = this.method_25399();
      if (currentFocused != null && currentFocused != l && currentFocused instanceof class_342) {
         ((class_342)currentFocused).method_25365(false);
      }

      super.method_25395(l);
   }

   private boolean canConfirm() {
      WaypointEditForm current = this.getCurrent();
      return (current.keepName || current.name.length() > 0) && (current.keepInitial || current.initial.length() > 0);
   }

   private void updateConfirmButton() {
      this.confirmButton.field_22763 = this.modeSwitchButton.field_22763 = this.canConfirm();
      this.leftButton.field_22763 = !this.modMain.getSettings().waypointsMutualEdit && this.canConfirm() && this.selectedWaypointIndex > 0;
      this.rightButton.field_22763 = !this.modMain.getSettings().waypointsMutualEdit
         && this.canConfirm()
         && this.selectedWaypointIndex < this.editForms.size() - 1;
   }

   private void handleCoordinateInputSpaces(class_342 coordinateBox, class_342 nextBox) {
      String startingBoxValue = coordinateBox.method_1882();
      int indexOfFirstSpace = startingBoxValue.indexOf(32);
      if (indexOfFirstSpace != -1) {
         String subStringToCut = startingBoxValue.substring(indexOfFirstSpace + 1);
         coordinateBox.method_1852(startingBoxValue.substring(0, indexOfFirstSpace));
         coordinateBox.method_1870();
         nextBox.method_1852(nextBox.method_1882() + subStringToCut);
         if (this.method_25399() == coordinateBox) {
            coordinateBox.method_25365(false);
            nextBox.method_25365(true);
            this.method_25395(nextBox);
            nextBox.method_1872();
         }
      }
   }

   protected void checkFields(class_364 focused) {
      this.handleCoordinateInputSpaces(this.xTextField, this.yTextField);
      this.handleCoordinateInputSpaces(this.yTextField, this.zTextField);
      this.handleCoordinateInputSpaces(this.zTextField, this.yawTextField);
      this.fieldValidator.validate(this.yawTextField);
      if (this.yawTextField == focused) {
         this.getCurrent().keepYawText = false;
         if (this.defaultYawButton != null) {
            this.defaultYawButton.field_22763 = true;
         }
      }

      this.fieldValidator.validate(this.xTextField);
      this.fieldYValidator.validate(this.yTextField);
      this.fieldValidator.validate(this.zTextField);
      WaypointEditForm current = this.getCurrent();
      current.name = this.nameTextField.method_1882();
      current.xText = this.xTextField.method_1882();
      current.yText = this.yTextField.method_1882();
      current.zText = this.zTextField.method_1882();
      current.yawText = this.yawTextField.method_1882();
      current.initial = this.initialTextField.method_1882();
      if (current.initial.length() > 2) {
         current.initial = current.initial.substring(0, 2);
         this.initialTextField.method_1852(current.initial);
      }

      if (current.yawText.length() > 4) {
         current.yawText = current.yawText.substring(0, 4);
         this.yawTextField.method_1852(current.yawText);
      }

      if (this.prefilled && this.editForms.size() > 1 && this.modMain.getSettings().waypointsMutualEdit) {
         current.keepName = current.name.isEmpty();
         current.keepXText = current.xText.isEmpty();
         current.keepYText = current.yText.isEmpty();
         current.keepZText = current.zText.isEmpty();
         current.keepInitial = current.initial.isEmpty();
      }
   }

   public void method_25393() {
      if (this.field_22787.field_1719 == null) {
         this.field_22787.method_1507(null);
      } else {
         this.nameTextField.method_1865();
         this.xTextField.method_1865();
         this.yTextField.method_1865();
         this.zTextField.method_1865();
         this.yawTextField.method_1865();
         this.initialTextField.method_1865();
      }
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      this.renderEscapeScreen(guiGraphics, par1, par2, par3);
      this.method_25420(guiGraphics);
      super.method_25394(guiGraphics, par1, par2, par3);
      super.renderTooltips(guiGraphics, par1, par2, par3);
   }

   @Override
   protected void renderPreDropdown(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
      guiGraphics.method_25300(this.field_22793, this.screenTitle, this.field_22789 / 2, 20, 16777215);
      WaypointEditForm current = this.getCurrent();
      if (!this.canBeLabyMod) {
         this.ignoreEditBoxChanges = true;
      }

      if (this.ignoreEditBoxChanges) {
         if (!this.nameTextField.method_25370() && current.keepName) {
            Misc.setFieldText(this.nameTextField, this.namePlaceholder, -11184811);
            this.nameTextField.method_1883(0);
         }

         double dimDiv = this.waypointsManager.getDimensionDivision(this.worlds.getCurrentKeys()[0]);
         if (current.keepXText) {
            if (!this.xTextField.method_25370()) {
               Misc.setFieldText(this.xTextField, this.xPlaceholder, -11184811);
            }
         } else if (current.xText.isEmpty()) {
            Misc.setFieldText(this.xTextField, this.getAutomaticX(dimDiv) + "", -11184811);
            this.xTextField.method_1883(0);
         }

         if (current.keepYText) {
            if (!this.yTextField.method_25370()) {
               Misc.setFieldText(this.yTextField, this.yPlaceholder, -11184811);
            }
         } else if (current.yText.isEmpty()) {
            Misc.setFieldText(this.yTextField, this.getAutomaticY() + "", -11184811);
            this.yTextField.method_1883(0);
         }

         if (current.keepZText) {
            if (!this.zTextField.method_25370()) {
               Misc.setFieldText(this.zTextField, this.zPlaceholder, -11184811);
            }
         } else if (current.zText.isEmpty()) {
            Misc.setFieldText(this.zTextField, this.getAutomaticZ(dimDiv) + "", -11184811);
            this.zTextField.method_1883(0);
         }

         if (!this.yawTextField.method_25370() && current.yawText.isEmpty()) {
            if (current.keepYawText) {
               Misc.setFieldText(this.yawTextField, this.yawPlaceholder, -11184811);
            } else {
               Misc.setFieldText(this.yawTextField, class_1074.method_4662("gui.xaero_yaw", new Object[0]), -11184811);
            }

            this.yawTextField.method_1883(0);
         }

         if (!this.initialTextField.method_25370() && current.initial.isEmpty()) {
            if (current.keepInitial) {
               Misc.setFieldText(this.initialTextField, this.initialPlaceholder, -11184811);
            } else {
               Misc.setFieldText(this.initialTextField, class_1074.method_4662("gui.xaero_initial", new Object[0]), -11184811);
            }

            this.initialTextField.method_1883(0);
         }
      }

      this.nameTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.xTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.yTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.zTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.yawTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.initialTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      if (this.ignoreEditBoxChanges) {
         Misc.setFieldText(this.nameTextField, current.name);
         Misc.setFieldText(this.xTextField, current.xText);
         Misc.setFieldText(this.yTextField, current.yText);
         Misc.setFieldText(this.zTextField, current.zText);
         Misc.setFieldText(this.yawTextField, current.yawText);
         Misc.setFieldText(this.initialTextField, current.initial);
      }

      this.ignoreEditBoxChanges = true;
   }

   @Override
   public boolean onSelected(DropDownWidget menu, int selected) {
      if (menu == this.setsDD) {
         this.sets.setCurrentSet(selected);
         if (this.waypointsManager.getCurrentContainerAndWorldID().equals(this.worlds.getCurrentKey())) {
            this.waypointsManager.getCurrentWorld().setCurrent(this.sets.getCurrentSetKey());
            this.waypointsManager.updateWaypoints();

            try {
               this.modMain.getSettings().saveWaypoints(this.waypointsManager.getCurrentWorld());
            } catch (IOException var5) {
               MinimapLogs.LOGGER.error("suppressed exception", var5);
            }
         }
      } else if (menu == this.colorDD) {
         this.getCurrent().color = this.colorDD.size() > ModSettings.ENCHANT_COLORS.length ? selected : selected + 1;
      } else if (menu == this.containersDD) {
         this.containers.current = selected;
         WaypointWorld currentWorld;
         if (this.containers.current != this.defaultContainer) {
            currentWorld = this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
         } else {
            currentWorld = this.defaultWorld;
         }

         this.sets = new GuiWaypointSets(false, currentWorld, this.containers.current == this.defaultContainer ? this.fromSet : currentWorld.getCurrent());
         this.worlds = new GuiWaypointWorlds(
            this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()),
            this.waypointsManager,
            currentWorld.getFullId(),
            this.frozenAutoContainerID,
            this.frozenAutoWorldID
         );
         this.replaceWidget(this.setsDD, this.setsDD = this.createSetsDropdown());
         this.replaceWidget(this.worldsDD, this.worldsDD = this.createWorldsDropdown());
      } else if (menu == this.worldsDD) {
         this.worlds.current = selected;
         String[] worldKeys = this.worlds.getCurrentKeys();
         WaypointWorld currentWorld = this.waypointsManager.getWorld(worldKeys[0], worldKeys[1]);
         this.sets = new GuiWaypointSets(false, currentWorld, currentWorld == this.defaultWorld ? this.fromSet : currentWorld.getCurrent());
         this.replaceWidget(this.setsDD, this.setsDD = this.createSetsDropdown());
      }

      return true;
   }
}
