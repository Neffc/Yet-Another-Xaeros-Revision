package xaero.common.category.ui.data;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorSimpleButtonData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListEntryWidget;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButton;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorAdderData extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   private final String displayName;
   private final GuiCategoryUIEditorTextFieldOptionsData nameField;
   private final GuiCategoryUIEditorSimpleButtonData confirmButton;
   private boolean confirmed;

   private GuiCategoryUIEditorAdderData(
      @Nonnull String displayName,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData nameField,
      @Nonnull GuiCategoryUIEditorSimpleButtonData confirmButton,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.displayName = displayName;
      this.confirmButton = confirmButton;
      this.nameField = nameField;
   }

   public boolean isConfirmed() {
      return this.confirmed;
   }

   @Override
   public void setExpanded(boolean expanded) {
      super.setExpanded(expanded);
      if (expanded) {
         this.reset();
      }
   }

   public void reset() {
      this.confirmed = false;
      this.nameField.resetInput("");
   }

   public GuiCategoryUIEditorTextFieldOptionsData getNameField() {
      return this.nameField;
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      return Lists.newArrayList(new GuiCategoryUIEditorExpandableData[]{this.nameField, this.confirmButton});
   }

   public static final class Builder
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorAdderData.Builder> {
      private String displayName;
      private final GuiCategoryUIEditorTextFieldOptionsData.Builder nameFieldBuilder;
      private final GuiCategoryUIEditorSimpleButtonData.Builder confirmButtonBuilder;

      private Builder(ListFactory listFactory) {
         this.nameFieldBuilder = GuiCategoryUIEditorTextFieldOptionsData.Builder.getDefault(listFactory);
         this.confirmButtonBuilder = GuiCategoryUIEditorSimpleButtonData.Builder.getDefault();
      }

      public GuiCategoryUIEditorAdderData.Builder setDefault() {
         super.setDefault();
         this.setDisplayName(null);
         this.nameFieldBuilder.setDefault().setDisplayName(class_1074.method_4662("gui.xaero_category_name", new Object[0]));
         this.confirmButtonBuilder.setDefault().setDisplayName(class_1074.method_4662("gui.xaero_category_confirm", new Object[0]));
         this.confirmButtonBuilder.setCallback((parent, bd, rl) -> {
            GuiCategoryUIEditorAdderData adder = (GuiCategoryUIEditorAdderData)parent;
            adder.confirmed = !adder.getNameField().getResult().isEmpty();
            adder.setExpanded(false);
            rl.setLastExpandedData(adder);
            rl.updateEntries();
         });
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryWidget<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsButton(parent, () -> data.getDisplayName(), true, 216, 20, b -> data.getExpandAction(rowList).run(), rowList),
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               )
         );
         return this;
      }

      public GuiCategoryUIEditorAdderData.Builder setDisplayName(String displayName) {
         this.displayName = displayName;
         return this.self;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder getNameFieldBuilder() {
         return this.nameFieldBuilder;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder getConfirmButtonBuilder() {
         return this.confirmButtonBuilder;
      }

      public static GuiCategoryUIEditorAdderData.Builder getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorAdderData.Builder(listFactory).setDefault();
      }

      public GuiCategoryUIEditorAdderData build() {
         if (this.displayName == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorAdderData)super.build();
         }
      }

      protected GuiCategoryUIEditorAdderData buildInternally() {
         if (this.nameFieldBuilder.needsInputStringValidator()) {
            this.nameFieldBuilder.setInputStringValidator(s -> true);
         }

         return new GuiCategoryUIEditorAdderData(
            this.displayName, this.nameFieldBuilder.build(), this.confirmButtonBuilder.build(), this.movable, this.listEntryFactory, this.tooltipSupplier
         );
      }
   }
}
