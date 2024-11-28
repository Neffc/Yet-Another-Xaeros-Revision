package xaero.common.interfaces;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.class_310;
import net.minecraft.class_364;
import net.minecraft.class_3675;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.gui.GuiEditMode;
import xaero.common.interfaces.pushbox.BossHealthPushBox;
import xaero.common.interfaces.pushbox.BossHealthShiftPushBox;
import xaero.common.interfaces.pushbox.IBossHealthPushBox;
import xaero.common.interfaces.pushbox.IPotionEffectsPushBox;
import xaero.common.interfaces.pushbox.PotionEffectsPushBox;
import xaero.common.interfaces.pushbox.PotionEffectsShiftPushBox;
import xaero.common.interfaces.pushbox.PushBox;
import xaero.common.minimap.MinimapInterface;

public class InterfaceManager {
   private IXaeroMinimap modMain;
   private class_310 mc;
   private ArrayList<Preset> presets;
   private ArrayList<Interface> list;
   public ArrayList<PushBox> pushBoxes;
   private int actionTimer;
   private int selectedId;
   private int draggingId;
   private int draggingOffX;
   private int draggingOffY;
   private long lastFlip;
   public PotionEffectsPushBox normalPotionEffectsPushBox;
   public PotionEffectsShiftPushBox shiftPotionEffectsPushBox;
   private BossHealthPushBox normalBossHealthPushBox;
   private BossHealthShiftPushBox shiftBossHealthPushBox;

   public InterfaceManager(IXaeroMinimap modMain, IInterfaceLoader loader) throws IOException {
      this.modMain = modMain;
      this.presets = new ArrayList<>();
      this.list = new ArrayList<>();
      this.mc = class_310.method_1551();
      this.selectedId = -1;
      this.draggingId = -1;
      this.normalPotionEffectsPushBox = new PotionEffectsPushBox();
      this.shiftPotionEffectsPushBox = new PotionEffectsShiftPushBox();
      this.normalBossHealthPushBox = new BossHealthPushBox();
      this.shiftBossHealthPushBox = new BossHealthShiftPushBox();
      this.pushBoxes = Lists.newArrayList(
         new PushBox[]{this.normalPotionEffectsPushBox, this.shiftPotionEffectsPushBox, this.normalBossHealthPushBox, this.shiftBossHealthPushBox}
      );
      loader.loadPresets(this);
      loader.load(modMain, this);
   }

   public IPotionEffectsPushBox getPotionEffectPushBox() {
      int boxType = this.modMain.getSettings().potionEffectPushBox;
      return (IPotionEffectsPushBox)(boxType == 0 ? null : (boxType == 2 ? this.shiftPotionEffectsPushBox : this.normalPotionEffectsPushBox));
   }

   public IBossHealthPushBox getBossHealthPushBox() {
      int boxType = this.modMain.getSettings().bossHealthPushBox;
      return (IBossHealthPushBox)(boxType == 0 ? null : (boxType == 2 ? this.shiftBossHealthPushBox : this.normalBossHealthPushBox));
   }

   public MinimapInterface getMinimapInterface() {
      return (MinimapInterface)this.list.get(4);
   }

   public boolean overAButton(int mouseX, int mouseY) {
      if (this.mc.field_1755 instanceof GuiEditMode) {
         for (class_364 el : this.mc.field_1755.method_25396()) {
            if (el instanceof class_4185 b
               && mouseX >= b.method_46426()
               && mouseY >= b.method_46427()
               && mouseX < b.method_46426() + 150
               && mouseY < b.method_46427() + 20) {
               return true;
            }
         }
      }

      return false;
   }

   protected void updateBlinkingOverridable() {
   }

   public void updateInterfaces(XaeroMinimapSession minimapSession, int mouseX, int mouseY, int width, int height, double scale) {
      class_437 screen = (class_437)this.modMain.getEvents().getLastGuiOpen();
      if (this.actionTimer <= 0) {
         this.updateBlinkingOverridable();
         if (screen instanceof GuiEditMode editModeScreen) {
            if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 256)) {
               this.selectedId = -1;
            }

            int i = this.getInterfaceId(minimapSession, mouseX, mouseY, width, height, scale);
            if (i == -1) {
               i = this.selectedId;
            }

            if (i != -1) {
               if (editModeScreen.mouseDown && this.draggingId == -1) {
                  this.draggingId = i;
                  this.selectedId = i;
                  this.draggingOffX = this.list.get(i).getX() - mouseX;
                  this.draggingOffY = this.list.get(i).getY() - mouseY;
               } else if (!editModeScreen.mouseDown && this.draggingId != -1) {
                  this.draggingId = -1;
                  this.draggingOffX = 0;
                  this.draggingOffY = 0;
               }

               if (this.selectedId != -1) {
                  i = this.selectedId;
               }

               if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 70) && System.currentTimeMillis() - this.lastFlip > 300L) {
                  this.lastFlip = System.currentTimeMillis();
                  this.list.get(i).setFlipped(!this.list.get(i).isFlipped());
               }

               if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 67) && System.currentTimeMillis() - this.lastFlip > 300L) {
                  this.lastFlip = System.currentTimeMillis();
                  this.list.get(i).setCentered(!this.list.get(i).isCentered());
               }

               if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 83)) {
                  this.selectedId = -1;
                  this.draggingId = -1;
                  this.modMain.getGuiHelper().openInterfaceSettings(i);
               }
            }

            if (this.draggingId != -1) {
               Interface dragged = this.list.get(this.draggingId);
               if (!dragged.isCentered()) {
                  dragged.setActualx(mouseX + this.draggingOffX);
                  if (dragged.isFromRight()) {
                     dragged.setActualx(width - dragged.getActualx());
                  }
               }

               int centerX = dragged.getActualx() + dragged.getW() / 2 * (dragged.isFromRight() ? -1 : 1);
               if (dragged.isFromRight() && (width & 1) == 0) {
                  centerX++;
               }

               if (centerX > width / 2) {
                  dragged.setFromRight(!dragged.isFromRight());
                  dragged.setActualx(width - dragged.getActualx());
               }

               dragged.setActualy(mouseY + this.draggingOffY);
               if (dragged.isFromBottom()) {
                  dragged.setActualy(height - dragged.getActualy());
               }

               int centerY = dragged.getActualy() + dragged.getH() / 2 * (dragged.isFromBottom() ? -1 : 1);
               if (dragged.isFromBottom() && (height & 1) == 0) {
                  centerY++;
               }

               if (centerY > height / 2) {
                  dragged.setFromBottom(!dragged.isFromBottom());
                  dragged.setActualy(height - dragged.getActualy());
               }
            }
         }
      } else {
         this.actionTimer--;
      }

      for (Interface j : this.list) {
         InterfaceInstance ji = minimapSession.getInterfaceInstances().get(j);
         j.setX(j.getActualx());
         j.setY(j.getActualy());
         if (j.isFromRight()) {
            j.setX(width - j.getX());
         }

         if (j.isFromBottom()) {
            j.setY(height - j.getY());
         }

         if (j.isCentered()) {
            if (j.isMulti()) {
               j.setW(ji.getWC(scale));
               j.setH(ji.getHC(scale));
            }

            j.setX(width / 2 - ji.getW(scale) / 2);
         } else if (j.isMulti()) {
            j.setW(ji.getW0(scale));
            j.setH(ji.getH0(scale));
         }

         if (j.getX() < 5) {
            j.setX(0);
         }

         if (j.getY() < 5) {
            j.setY(0);
         }

         if (j.getX() + ji.getW(scale) > width - 5) {
            j.setX(width - ji.getW(scale));
         }

         if (j.getY() + ji.getH(scale) > height - 5) {
            j.setY(height - ji.getH(scale));
         }
      }

      for (PushBox pb : this.pushBoxes) {
         pb.update();
      }
   }

   public void pushInterface(Interface j, InterfaceInstance ji, double scale, int width, int height) {
      for (PushBox pb : this.pushBoxes) {
         if (pb.isActive()) {
            int interfaceX = j.getX();
            int interfaceY = j.getY();
            int interfaceW = ji.getW(scale);
            int interfaceH = ji.getH(scale);
            int pushBoxX = (int)((float)width * pb.getAnchorX()) + pb.getX(width, height);
            int pushBoxY = (int)((float)height * pb.getAnchorY()) + pb.getY(width, height);
            int pushBoxW = pb.getW(width, height);
            int pushBoxH = pb.getH(width, height);
            int minPushX = 0;
            int minPushY = 0;
            int overLeftSide = interfaceX + interfaceW - pushBoxX;
            int overRightSide = interfaceX - (pushBoxX + pushBoxW);
            int overTopSide = interfaceY + interfaceH - pushBoxY;
            int overBottomSide = interfaceY - (pushBoxY + pushBoxH);
            if (overLeftSide > 0 && overRightSide < 0 && overTopSide > 0 && overBottomSide < 0) {
               boolean leftBlocked = interfaceX - overLeftSide < 0;
               boolean rightBlocked = interfaceX - overRightSide + interfaceW > width;
               if (leftBlocked == rightBlocked) {
                  minPushX = -overRightSide < overLeftSide ? overRightSide : overLeftSide;
               } else {
                  minPushX = leftBlocked ? overRightSide : overLeftSide;
               }

               boolean topBlocked = interfaceY - overTopSide < 0;
               boolean bottomBlocked = interfaceY - overBottomSide + interfaceH > height;
               if (topBlocked == bottomBlocked) {
                  minPushY = -overBottomSide < overTopSide ? overBottomSide : overTopSide;
               } else {
                  minPushY = topBlocked ? overBottomSide : overTopSide;
               }

               if ((!leftBlocked || !rightBlocked || topBlocked && bottomBlocked) && Math.abs(minPushX) < Math.abs(minPushY) - pb.getVerticalBias()) {
                  pb.push(ji, j, interfaceX, interfaceY, interfaceW, interfaceH, -minPushX, 0, width, height);
               } else {
                  pb.push(ji, j, interfaceX, interfaceY, interfaceW, interfaceH, 0, -minPushY, width, height);
               }
            }
         }
      }
   }

   public int getInterfaceId(XaeroMinimapSession minimapSession, int mouseX, int mouseY, int width, int height, double scale) {
      int toReturn = -1;
      int size = 0;

      for (int i = 0; i < this.list.size(); i++) {
         Interface l = this.list.get(i);
         if (this.modMain.getSettings().getBooleanValue(l.getOption())) {
            InterfaceInstance li = minimapSession.getInterfaceInstances().get(l);
            int x = l.getX();
            int y = l.getY();
            int x2 = x + li.getW(scale);
            int y2 = y + li.getH(scale);
            int isize = li.getSize();
            if (!l.getIname().equals("dummy")
               && (size == 0 || isize < size)
               && !this.overAButton(mouseX, mouseY)
               && mouseX >= x
               && mouseX < x2
               && mouseY >= y
               && mouseY < y2) {
               size = isize;
               toReturn = i;
            }
         }
      }

      return toReturn;
   }

   public void add(Interface i) {
      this.list.add(i);
   }

   public Preset getDefaultPreset() {
      return this.presets.get(0);
   }

   public Preset getPreset(int id) {
      return this.presets.get(id);
   }

   public int getNextId() {
      return this.list.size();
   }

   public void addPreset(Preset preset) {
      this.presets.add(preset);
   }

   public int getSelectedId() {
      return this.selectedId;
   }

   public void setSelectedId(int selectedId) {
      this.selectedId = selectedId;
   }

   public int getDraggingId() {
      return this.draggingId;
   }

   public void setDraggingId(int draggingId) {
      this.draggingId = draggingId;
   }

   public Iterator<Interface> getInterfaceIterator() {
      return this.list.iterator();
   }

   public Iterator<Preset> getPresetsIterator() {
      return this.presets.iterator();
   }

   public int getActionTimer() {
      return this.actionTimer;
   }

   public void setActionTimer(int actionTimer) {
      this.actionTimer = actionTimer;
   }

   public void onPostGameOverlay() {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         for (Interface inter : this.list) {
            InterfaceInstance ii = minimapSession.getInterfaceInstances().get(inter);
            ii.onPostGameOverlay();
         }
      }
   }
}
