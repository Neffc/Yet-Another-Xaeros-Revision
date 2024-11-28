package xaero.common.category.ui.data.rule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.rule.ExcludeListMode;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSimpleDeletableWrapperData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.list.GuiCategoryUIEditorCompactListOptionsData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorExcludeListData extends GuiCategoryUIEditorListData {
   private GuiCategoryUIEditorCompactListOptionsData<ExcludeListMode> excludeMode;

   private GuiCategoryUIEditorExcludeListData(
      @Nonnull List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> list,
      ListFactory listFactory,
      @Nonnull GuiCategoryUIEditorCompactListOptionsData<ExcludeListMode> excludeMode,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData topAdder,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData bottomAdder,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      @Nonnull GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback,
      @Nonnull BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier
   ) {
      super(list, listFactory, topAdder, bottomAdder, movable, listEntryFactory, tooltipSupplier, deletionCallback, helpTooltipSupplier);
      this.excludeMode = excludeMode;
   }

   public ExcludeListMode getExcludeMode() {
      return this.excludeMode.getCurrentValue().getValue();
   }

   @Override
   public String getDisplayName() {
      return class_1074.method_4662("gui.xaero_category_exclude_list", new Object[0]);
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      List<GuiCategoryUIEditorExpandableData<?>> result = super.getSubExpandables();
      result.add(0, this.excludeMode);
      return result;
   }

   public static final class Builder<E, P>
      extends GuiCategoryUIEditorListData.Builder<E, P, GuiCategoryUIEditorExcludeListData, GuiCategoryUIEditorExcludeListData.Builder<E, P>> {
      private final GuiCategoryUIEditorCompactListOptionsData.Builder<ExcludeListMode> excludeModeBuilder;

      protected Builder(ListFactory listFactory) {
         super(listFactory);
         this.excludeModeBuilder = GuiCategoryUIEditorCompactListOptionsData.Builder.getDefault(listFactory);
      }

      public GuiCategoryUIEditorExcludeListData.Builder<E, P> setDefault() {
         this.excludeModeBuilder.setDefault().setDisplayName(class_1074.method_4662("gui.xaero_category_exclude_list_mode", new Object[0]));

         for (ExcludeListMode mode : ExcludeListMode.values()) {
            this.excludeModeBuilder.addOptionBuilder(GuiCategoryUIEditorOptionData.Builder.<ExcludeListMode>getDefault().setValue(mode));
         }

         this.setExcludeMode(ExcludeListMode.ONLY);
         return (GuiCategoryUIEditorExcludeListData.Builder<E, P>)super.setDefault();
      }

      public GuiCategoryUIEditorExcludeListData.Builder<E, P> setExcludeMode(ExcludeListMode excludeMode) {
         this.excludeModeBuilder.setCurrentValue(excludeMode);
         return this;
      }

      public GuiCategoryUIEditorExcludeListData build() {
         return (GuiCategoryUIEditorExcludeListData)super.build();
      }

      protected GuiCategoryUIEditorExcludeListData buildInternally() {
         return new GuiCategoryUIEditorExcludeListData(
            this.buildList(),
            this.listFactory,
            this.excludeModeBuilder.build(),
            this.adderBuilder.build(),
            this.adderBuilder.build(),
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.deletionCallback,
            this.helpTooltipSupplier
         );
      }

      public static <E, P> GuiCategoryUIEditorExcludeListData.Builder<E, P> getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorExcludeListData.Builder<E, P>(listFactory).setDefault();
      }
   }
}
