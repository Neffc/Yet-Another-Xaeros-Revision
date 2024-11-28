package xaero.common.category.ui.data;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.FilterObjectCategory;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public abstract class GuiCategoryUIEditorFilterCategoryData<E, P, C extends FilterObjectCategory<E, P, ?, C>, SD extends GuiCategoryUIEditorSettingsData<?>, ED extends GuiCategoryUIEditorFilterCategoryData<E, P, C, SD, ED>>
   extends GuiCategoryUIEditorCategoryData<C, SD, ED> {
   protected GuiCategoryUIEditorFilterCategoryData(
      @Nonnull SD settingOverrides,
      @Nonnull List<ED> subCategories,
      @Nonnull GuiCategoryUIEditorAdderData topAdder,
      @Nonnull Function<GuiCategoryUIEditorAdderData, ED> newCategorySupplier,
      boolean movable,
      int subIndex,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(settingOverrides, subCategories, topAdder, newCategorySupplier, movable, subIndex, listEntryFactory, tooltipSupplier);
   }

   public abstract static class Builder<E, P, C extends FilterObjectCategory<E, P, ?, C>, ED extends GuiCategoryUIEditorFilterCategoryData<E, P, C, SD, ED>, SD extends GuiCategoryUIEditorFilterSettingsData<E, P, ?>, SDB extends GuiCategoryUIEditorFilterSettingsData.Builder<E, P, SD, SDB>, EDB extends GuiCategoryUIEditorFilterCategoryData.Builder<E, P, C, ED, SD, SDB, EDB>>
      extends GuiCategoryUIEditorCategoryData.Builder<C, ED, SD, SDB, EDB> {
      private String listRuleTypePrefixSeparator;
      private Predicate<String> inputRuleTypeStringValidator;

      public Builder(ListFactory listFactory, SDB settingsDataBuilder) {
         super(listFactory, settingsDataBuilder);
      }

      public EDB setDefault() {
         super.setDefault();
         this.setListRuleTypePrefixSeparator(";");
         this.setInputRuleTypeStringValidator(s -> s.matches("[a-z_0-9\\-]+"));
         return this.self;
      }

      public EDB setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
         this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
         return this.self;
      }

      public EDB setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
         this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
         return this.self;
      }

      public ED build() {
         if (this.listRuleTypePrefixSeparator == null) {
            throw new IllegalStateException();
         } else {
            this.settingsDataBuilder
               .setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator)
               .setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
            return super.build();
         }
      }
   }
}
