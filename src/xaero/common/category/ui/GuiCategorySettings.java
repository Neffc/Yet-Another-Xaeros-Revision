package xaero.common.category.ui;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_410;
import net.minecraft.class_4185;
import net.minecraft.class_4280;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_6381;
import net.minecraft.class_6382;
import net.minecraft.class_4280.class_4281;
import xaero.common.AXaeroMinimap;
import xaero.common.category.ObjectCategory;
import xaero.common.category.ui.data.GuiCategoryUIEditorCategoryData;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSettingsData;
import xaero.common.category.ui.entry.CategorySettingsListEntry;
import xaero.common.category.ui.entry.CategorySettingsListMainEntry;
import xaero.common.category.ui.entry.ConnectionLineType;
import xaero.common.graphics.CursorBox;
import xaero.common.gui.ScreenBase;

public abstract class GuiCategorySettings<C extends ObjectCategory<?, C>, ED extends GuiCategoryUIEditorCategoryData<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends GuiCategoryUIEditorSettingsData<?>, SDB extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB>, EDB extends GuiCategoryUIEditorCategoryData.Builder<C, ED, SD, SDB, EDB>>
   extends ScreenBase {
   private static final int FRAME_TOP_SIZE = 32;
   private static final int FRAME_BOTTOM_SIZE = 48;
   public static final int ROW_HEIGHT = 24;
   public static final int ROW_WIDTH = 220;
   private GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList rowList;
   private final GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> dataConverter;
   private ED editorData;
   protected ED cutCategory;
   protected ED cutCategorySuper;

   protected GuiCategorySettings(
      AXaeroMinimap modMain, class_437 parent, class_437 escape, class_2561 title, GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> dataConverter
   ) {
      super(modMain, parent, escape, title);
      this.dataConverter = dataConverter;
      this.editorData = this.constructEditorData(dataConverter);
   }

   protected abstract ED constructEditorData(GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> var1);

   protected abstract ED constructDefaultData(GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> var1);

   protected abstract void onConfigConfirmed(C var1);

   @Override
   public void method_25426() {
      super.method_25426();
      this.method_37063(
         class_4185.method_46430(class_2561.method_43471("gui.xaero_category_settings_cancel"), b -> this.field_22787.method_1507(new class_410(result -> {
                  if (result) {
                     super.onExit(this.parent);
                  } else {
                     this.field_22787.method_1507(this);
                  }
               }, class_2561.method_43471("gui.xaero_category_settings_cancel_confirm"), class_2561.method_43470(""))))
            .method_46434(this.field_22789 / 2 + 5, this.field_22790 - 32, 150, 20)
            .method_46431()
      );
      this.method_37063(
         class_4185.method_46430(class_2561.method_43471("gui.xaero_category_settings_confirm"), b -> this.confirm())
            .method_46434(this.field_22789 / 2 - 155, this.field_22790 - 32, 150, 20)
            .method_46431()
      );
      this.method_37063(
         class_4185.method_46430(class_2561.method_43471("gui.xaero_category_settings_reset"), b -> this.field_22787.method_1507(new class_410(result -> {
                  if (result) {
                     this.editorData = this.constructDefaultData(this.dataConverter);
                  }

                  this.field_22787.method_1507(this);
               }, class_2561.method_43471("gui.xaero_category_settings_reset_confirm1"), class_2561.method_43471("gui.xaero_category_settings_reset_confirm2"))))
            .method_46434(6, 6, 120, 20)
            .method_46431()
      );
      this.rowList = new GuiCategorySettings.SettingRowList(this.dataConverter);
      this.method_25429(this.rowList);
   }

   private void confirm() {
      this.onConfigConfirmed(this.dataConverter.getConfiguredBuilder(this.editorData).build());
      super.onExit(this.parent);
   }

   @Override
   protected void onExit(class_437 screen) {
      this.field_22787.method_1507(new class_410(result -> {
         if (result) {
            this.confirm();
         }

         super.onExit(screen);
      }, class_2561.method_43471("gui.xaero_category_settings_save_confirm"), class_2561.method_43471("gui.xaero_category_settings_save_confirm_warning")) {
         public boolean method_25404(int i, int j, int k) {
            return i == 256 ? true : super.method_25404(i, j, k);
         }
      });
   }

   @Override
   public void method_25394(class_332 guiGraphics, int i, int j, float f) {
      class_4587 poseStack = guiGraphics.method_51448();
      this.rowList.method_25394(guiGraphics, i, j, f);
      guiGraphics.method_27534(this.field_22787.field_1772, this.field_22785, this.field_22789 / 2, 5, 16777215);
      super.method_25394(guiGraphics, i, j, f);
      if (this.rowList.hovered != null) {
         poseStack.method_22903();
         poseStack.method_22904(0.0, 0.0, 0.1);
         Supplier<CursorBox> tooltipSupplier = this.rowList.hovered.getTooltipSupplier();
         if (tooltipSupplier != null) {
            CursorBox tooltip = tooltipSupplier.get();
            if (tooltip != null) {
               tooltip.drawBox(guiGraphics, i, j, this.field_22789, this.field_22790);
            }
         }

         poseStack.method_22909();
      }
   }

   public boolean method_25404(int i, int j, int k) {
      return this.rowList.method_25370() && i == 257 && this.rowList.confirmSelection() ? true : super.method_25404(i, j, k);
   }

   public void method_25393() {
      this.rowList.tick();
      super.method_25393();
   }

   public GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList getRowList() {
      return this.rowList;
   }

   @Override
   public boolean method_25402(double d, double e, int i) {
      return super.method_25402(d, e, i) ? true : this.rowList.method_25402(d, e, i);
   }

   @Override
   public boolean method_25406(double d, double e, int i) {
      return super.method_25406(d, e, i) ? true : this.rowList.method_25406(d, e, i);
   }

   public boolean method_25403(double d, double e, int i, double f, double g) {
      return super.method_25403(d, e, i, f, g) ? true : this.rowList.method_25403(d, e, i, f, g);
   }

   @Override
   public boolean method_25401(double d, double e, double f) {
      return super.method_25401(d, e, f) ? true : this.rowList.method_25401(d, e, f);
   }

   public class SettingRowList extends class_4280<GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry> {
      private GuiCategoryUIEditorExpandableData<?> lastExpandedData;
      private boolean restoreScrollAfterUpdate;
      private CategorySettingsListEntry hovered;
      private final GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> dataConverter;
      private static final class_2561 USAGE_NARRATION = class_2561.method_43471("narration.selection.usage");
      private static final class_2561 LEFT_RIGHT_USAGE = class_2561.method_43471("narration.xaero_ui_list_left_right_usage");

      public SettingRowList(GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> dataConverter) {
         super(
            GuiCategorySettings.this.field_22787,
            GuiCategorySettings.this.field_22789,
            GuiCategorySettings.this.field_22790,
            32,
            Math.max(36, GuiCategorySettings.this.field_22790 - 48),
            24
         );
         this.dataConverter = dataConverter;
         this.updateEntries();
         this.method_29344(false);
      }

      public boolean hasCut() {
         if (GuiCategorySettings.this.cutCategory != null) {
            if (GuiCategorySettings.this.cutCategorySuper.getSubCategories().contains(GuiCategorySettings.this.cutCategory)) {
               return true;
            }

            this.setCutCategory(null, null);
         }

         return false;
      }

      public ED getCut() {
         return GuiCategorySettings.this.cutCategory;
      }

      public boolean isCut(ED category) {
         return GuiCategorySettings.this.cutCategory == category ? this.hasCut() : false;
      }

      public void setCutCategory(ED cutCategory, ED cutCategorySuper) {
         GuiCategorySettings.this.cutCategory = cutCategory;
         GuiCategorySettings.this.cutCategorySuper = cutCategorySuper;
      }

      public void pasteTo(ED destination) {
         if (GuiCategorySettings.this.cutCategory != null) {
            if (destination == GuiCategorySettings.this.cutCategory || destination == GuiCategorySettings.this.cutCategorySuper) {
               this.setCutCategory(null, null);
               this.updateEntries();
               return;
            }

            destination.getExpandAction(this).run();
            this.setLastExpandedData(GuiCategorySettings.this.cutCategory);
            GuiCategorySettings.this.cutCategorySuper.getSubCategories().remove(GuiCategorySettings.this.cutCategory);
            destination.getSubCategories().add(0, GuiCategorySettings.this.cutCategory);
            this.setCutCategory(null, null);
         }
      }

      public boolean method_25370() {
         return GuiCategorySettings.this.method_25399() == this;
      }

      public void setLastExpandedData(GuiCategoryUIEditorExpandableData<?> lastExpandedData) {
         this.lastExpandedData = lastExpandedData;
      }

      public void restoreScrollAfterUpdate() {
         this.restoreScrollAfterUpdate = true;
      }

      public void updateEntries() {
         double scrollBackup = this.method_25341();
         this.method_25339();
         GuiCategorySettings.this.editorData.setExpanded(true);
         this.addEntriesForExpandable(GuiCategorySettings.this.editorData, null);
         if (this.method_25334() != null) {
            this.method_25324((GuiCategorySettings.SettingRowList.Entry)this.method_25334());
         }

         if (this.restoreScrollAfterUpdate) {
            this.method_25307(scrollBackup);
            this.restoreScrollAfterUpdate = false;
         }
      }

      private <D extends GuiCategoryUIEditorExpandableData<?>> void addEntriesForExpandable(
         GuiCategoryUIEditorExpandableData<D> data, GuiCategoryUIEditorExpandableData<?> parent
      ) {
         int nextIndex = this.method_25396().size();
         List<D> subExpandables = data.getSubExpandables();
         if (subExpandables != null) {
            GuiCategoryUIEditorExpandableData<?> expandedData = null;

            for (GuiCategoryUIEditorExpandableData<?> sed : subExpandables) {
               if (sed.isExpanded()) {
                  expandedData = sed;
                  break;
               }
            }

            GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry currentEntry = new GuiCategorySettings.SettingRowList.Entry(
               data.getListEntryFactory()
                  .get(
                     data, parent, nextIndex, nextIndex == 0 ? ConnectionLineType.NONE : ConnectionLineType.PATH, this, this.field_22742, expandedData == null
                  ),
               nextIndex++
            );
            this.method_25321(currentEntry);
            if (data == this.lastExpandedData) {
               this.method_25395(currentEntry);
            }

            if (expandedData != null) {
               this.addEntriesForExpandable((GuiCategoryUIEditorExpandableData<D>)expandedData, data);
            } else {
               if (this.lastExpandedData == null && data.isExpanded()) {
                  this.method_25395(currentEntry);
               }

               boolean first = true;

               for (GuiCategoryUIEditorExpandableData<?> sedx : subExpandables) {
                  GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry leafEntry = new GuiCategorySettings.SettingRowList.Entry(
                     sedx.getListEntryFactory()
                        .get(sedx, data, nextIndex, first ? ConnectionLineType.HEAD_LEAF : ConnectionLineType.TAIL_LEAF, this, this.field_22742, false),
                     nextIndex++
                  );
                  this.method_25321(leafEntry);
                  if (sedx == this.lastExpandedData) {
                     this.method_25395(leafEntry);
                  }

                  first = false;
               }
            }
         }
      }

      public boolean method_25402(double d, double e, int i) {
         if (!this.method_25405(d, e)) {
            this.method_25395(null);
         }

         return super.method_25402(d, e, i);
      }

      public void method_16014(double d, double e) {
         if (this.method_25334() != null) {
            ((GuiCategorySettings.SettingRowList.Entry)this.method_25334()).method_16014(d, e);
         }

         super.method_16014(d, e);
      }

      public boolean method_16803(int i, int j, int k) {
         return this.method_25334() != null && ((GuiCategorySettings.SettingRowList.Entry)this.method_25334()).method_16803(i, j, k)
            ? true
            : super.method_16803(i, j, k);
      }

      public boolean method_25400(char c, int i) {
         if (this.method_25334() != null) {
            boolean result = ((GuiCategorySettings.SettingRowList.Entry)this.method_25334()).method_25400(c, i);
            if (result) {
               return true;
            }
         }

         return super.method_25400(c, i);
      }

      public void tick() {
         if (this.method_25334() != null) {
            ((GuiCategorySettings.SettingRowList.Entry)this.method_25334()).tick();
         }
      }

      public boolean confirmSelection() {
         GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry = (GuiCategorySettings.SettingRowList.Entry)this.method_25334();
         if (entry != null) {
            CategorySettingsListEntry selectedSubEntry = entry.wrappedEntry.confirmSelection();
            if (selectedSubEntry != null) {
               return true;
            }
         }

         return false;
      }

      public void method_25395(class_364 guiEventListener) {
         if (this.method_25396().contains(guiEventListener) && this.method_25336() != guiEventListener) {
            if (this.method_25334() != null) {
               ((GuiCategorySettings.SettingRowList.Entry)this.method_25334()).wrappedEntry.unfocusRecursively();
            }

            if (this.method_25336() != null) {
               ((GuiCategorySettings.SettingRowList.Entry)this.method_25336()).method_25365(false);
            }

            GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry = (GuiCategorySettings.SettingRowList.Entry)guiEventListener;
            if (entry != null) {
               entry.wrappedEntry.focusFirstRecursively();
            }

            super.method_25395(guiEventListener);
            if (guiEventListener == null) {
               this.setSelected(null);
            }

            this.narrateSelection();
         }
      }

      public void setSelected(GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry entry) {
         super.method_25313(entry);
      }

      public int method_25322() {
         return this.field_22742;
      }

      protected int method_25329() {
         return this.field_22742 / 2 + 164;
      }

      public void narrateSelection() {
         GuiCategorySettings.this.method_37070();
      }

      public void method_37020(class_6382 narrationElementOutput) {
         super.method_37020(narrationElementOutput);
         if (this.method_25370()) {
            narrationElementOutput.method_37035(class_6381.field_33791, new class_2561[]{USAGE_NARRATION, LEFT_RIGHT_USAGE});
         }
      }

      public void method_25394(class_332 guiGraphics, int i, int j, float f) {
         this.hovered = null;
         super.method_25394(guiGraphics, i, j, f);
      }

      public GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> getDataConverter() {
         return this.dataConverter;
      }

      public class Entry extends class_4281<GuiCategorySettings<C, ED, CB, SD, SDB, EDB>.SettingRowList.Entry> {
         private CategorySettingsListMainEntry<?> wrappedEntry;
         private int index;
         private int lastX;
         private int lastY;

         public Entry(CategorySettingsListMainEntry<?> entryInfo, int index) {
            this.wrappedEntry = entryInfo;
            this.index = index;
         }

         public void method_25343(
            class_332 guiGraphics, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks
         ) {
            class_4587 poseStack = guiGraphics.method_51448();
            this.lastX = x;
            this.lastY = y;
            poseStack.method_22903();
            poseStack.method_46416((float)x, (float)y, 0.0F);
            boolean includesSelected = SettingRowList.this.method_25334() == this;
            this.wrappedEntry.preRender(guiGraphics, includesSelected, true);
            CategorySettingsListEntry hoveredInRow = this.wrappedEntry
               .render(
                  guiGraphics,
                  index,
                  rowWidth,
                  rowHeight,
                  mouseX - x - this.wrappedEntry.getEntryRelativeX(),
                  mouseY - y - this.wrappedEntry.getEntryRelativeY(),
                  isMouseOver,
                  partialTicks,
                  GuiCategorySettings.this.field_22793,
                  mouseX,
                  mouseY,
                  includesSelected,
                  true
               );
            this.wrappedEntry.postRender(guiGraphics);
            poseStack.method_22909();
            if (hoveredInRow != null) {
               SettingRowList.this.hovered = hoveredInRow;
            }
         }

         public boolean method_25402(double mouseX, double mouseY, int i) {
            SettingRowList.this.method_25395(this);
            double relativeMouseX = mouseX - (double)this.lastX - (double)this.wrappedEntry.getEntryRelativeX();
            double relativeMouseY = mouseY - (double)this.lastY - (double)this.wrappedEntry.getEntryRelativeY();
            this.wrappedEntry.mouseClicked(this, relativeMouseX, relativeMouseY, i);
            return true;
         }

         public boolean method_25406(double mouseX, double mouseY, int i) {
            double relativeMouseX = mouseX - (double)this.lastX - (double)this.wrappedEntry.getEntryRelativeX();
            double relativeMouseY = mouseY - (double)this.lastY - (double)this.wrappedEntry.getEntryRelativeY();
            this.wrappedEntry.mouseReleased(relativeMouseX, relativeMouseY, i);
            return super.method_25406(mouseX, mouseY, i);
         }

         public boolean method_25401(double mouseX, double mouseY, double f) {
            double relativeMouseX = mouseX - (double)this.lastX - (double)this.wrappedEntry.getEntryRelativeX();
            double relativeMouseY = mouseY - (double)this.lastY - (double)this.wrappedEntry.getEntryRelativeY();
            return this.wrappedEntry.mouseScrolled(relativeMouseX, relativeMouseY, f) ? true : super.method_25401(mouseX, mouseY, f);
         }

         public boolean method_25403(double mouseX, double mouseY, int i, double f, double g) {
            double relativeMouseX = mouseX - (double)this.lastX - (double)this.wrappedEntry.getEntryRelativeX();
            double relativeMouseY = mouseY - (double)this.lastY - (double)this.wrappedEntry.getEntryRelativeY();
            return this.wrappedEntry.mouseDragged(relativeMouseX, relativeMouseY, i, f, g) ? true : super.method_25403(mouseX, mouseY, i, f, g);
         }

         public boolean method_25404(int i, int j, int k) {
            return this.wrappedEntry.keyPressed(i, j, k, true) ? true : super.method_25404(i, j, k);
         }

         public boolean method_16803(int i, int j, int k) {
            return this.wrappedEntry.keyReleased(i, j, k) ? true : super.method_16803(i, j, k);
         }

         public boolean method_25400(char c, int i) {
            return this.wrappedEntry.charTyped(c, i) ? true : super.method_25400(c, i);
         }

         public void method_25365(boolean bl) {
            this.wrappedEntry.setFocused(bl);
            super.method_25365(bl);
         }

         public void tick() {
            this.wrappedEntry.tick();
         }

         public class_2561 method_37006() {
            String selectedNarrationString = this.wrappedEntry.getSubNarration();
            return selectedNarrationString == null
               ? class_2561.method_43470("")
               : class_2561.method_43469("narrator.select", new Object[]{selectedNarrationString});
         }
      }
   }
}
