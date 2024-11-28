package xaero.common.category.ui.data.rule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSimpleDeletableWrapperData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorCompactBooleanOptionsData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorIncludeListData extends GuiCategoryUIEditorListData {
   private final GuiCategoryUIEditorCompactBooleanOptionsData includeInSuperToggleData;

   private GuiCategoryUIEditorIncludeListData(
      @Nonnull List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> list,
      ListFactory listFactory,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData topAdder,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData bottomAdder,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      @Nonnull GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback,
      @Nonnull BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier,
      GuiCategoryUIEditorCompactBooleanOptionsData includeInSuperToggleData
   ) {
      super(list, listFactory, topAdder, bottomAdder, movable, listEntryFactory, tooltipSupplier, deletionCallback, helpTooltipSupplier);
      this.includeInSuperToggleData = includeInSuperToggleData;
   }

   @Override
   public String getDisplayName() {
      return class_1074.method_4662("gui.xaero_category_include_list", new Object[0]);
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      List<GuiCategoryUIEditorExpandableData<?>> result = super.getSubExpandables();
      result.add(0, this.includeInSuperToggleData);
      return result;
   }

   public boolean getIncludeInSuper() {
      return this.includeInSuperToggleData.getCurrentValue().getValue();
   }

   public static final class Builder<E, P>
      extends GuiCategoryUIEditorListData.Builder<E, P, GuiCategoryUIEditorIncludeListData, GuiCategoryUIEditorIncludeListData.Builder<E, P>> {
      private final GuiCategoryUIEditorCompactBooleanOptionsData.Builder includeInSuperToggleDataBuilder = GuiCategoryUIEditorCompactBooleanOptionsData.Builder.getDefault();

      protected Builder(ListFactory listFactory) {
         super(listFactory);
      }

      public GuiCategoryUIEditorIncludeListData.Builder<E, P> setDefault() {
         super.setDefault();
         this.includeInSuperToggleDataBuilder
            .setDefault()
            .setDisplayName(class_1074.method_4662("gui.xaero_category_include_list_include_in_super", new Object[0]));
         return this;
      }

      public GuiCategoryUIEditorCompactBooleanOptionsData.Builder getIncludeInSuperToggleDataBuilder() {
         return this.includeInSuperToggleDataBuilder;
      }

      public GuiCategoryUIEditorIncludeListData build() {
         return (GuiCategoryUIEditorIncludeListData)super.build();
      }

      protected GuiCategoryUIEditorIncludeListData buildInternally() {
         return new GuiCategoryUIEditorIncludeListData(
            this.buildList(),
            this.listFactory,
            this.adderBuilder.build(),
            this.adderBuilder.build(),
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.deletionCallback,
            this.helpTooltipSupplier,
            this.includeInSuperToggleDataBuilder.build()
         );
      }

      public static <E, P> GuiCategoryUIEditorIncludeListData.Builder<E, P> getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorIncludeListData.Builder<E, P>(listFactory).setDefault();
      }
   }
}
