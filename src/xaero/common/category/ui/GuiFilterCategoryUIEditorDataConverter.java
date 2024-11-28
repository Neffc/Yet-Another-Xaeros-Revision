package xaero.common.category.ui;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.FilterObjectCategory;
import xaero.common.category.rule.ObjectCategoryExcludeList;
import xaero.common.category.rule.ObjectCategoryIncludeList;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.category.serialization.FilterObjectCategorySerializationHandler;
import xaero.common.category.ui.data.GuiCategoryUIEditorFilterCategoryData;
import xaero.common.category.ui.data.GuiCategoryUIEditorFilterSettingsData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSimpleDeletableWrapperData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorExcludeListData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorIncludeListData;

public abstract class GuiFilterCategoryUIEditorDataConverter<E, P, C extends FilterObjectCategory<E, P, ?, C>, ED extends GuiCategoryUIEditorFilterCategoryData<E, P, C, SD, ED>, CB extends FilterObjectCategory.Builder<E, P, C, CB>, SD extends GuiCategoryUIEditorFilterSettingsData<E, P, ?>, SDB extends GuiCategoryUIEditorFilterSettingsData.Builder<E, P, SD, SDB>, EDB extends GuiCategoryUIEditorFilterCategoryData.Builder<E, P, C, ED, SD, SDB, EDB>>
   extends GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> {
   private final ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
   private final Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
   private final String listRuleTypePrefixSeparator;
   private final Predicate<String> inputRuleTypeStringValidator;

   public GuiFilterCategoryUIEditorDataConverter(
      @Nonnull Supplier<CB> categoryBuilderFactory,
      @Nonnull Supplier<EDB> editorDataBuilderFactory,
      ObjectCategoryListRuleType<E, P, ?> defaultListRuleType,
      Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter,
      String listRuleTypePrefixSeparator,
      Predicate<String> inputRuleTypeStringValidator
   ) {
      super(categoryBuilderFactory, editorDataBuilderFactory);
      this.defaultListRuleType = defaultListRuleType;
      this.listRuleTypeGetter = listRuleTypeGetter;
      this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
      this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
   }

   protected EDB getConfiguredBuilder(C category, boolean canBeRoot) {
      EDB editorDataBuilder = super.getConfiguredBuilder(category, canBeRoot);
      SDB settingDataBuilder = editorDataBuilder.getSettingDataBuilder();
      settingDataBuilder.setBaseRule(category.getBaseRule());
      GuiCategoryUIEditorIncludeListData.Builder<E, P> includeListBuilder = settingDataBuilder.getIncludeListBuilder();
      GuiCategoryUIEditorExcludeListData.Builder<E, P> excludeListBuilder = settingDataBuilder.getExcludeListBuilder();

      for (ObjectCategoryIncludeList<E, P, ?> includeList : category.getIncludeLists()) {
         String prefix = includeList.getType() == this.defaultListRuleType ? "" : includeList.getType().getId() + this.listRuleTypePrefixSeparator;
         includeList.forEach(
            el -> includeListBuilder.getList().add(GuiCategoryUIEditorSimpleDeletableWrapperData.Builder.<String>getDefault().setElement(prefix + el))
         );
      }

      for (ObjectCategoryExcludeList<E, P, ?> excludeList : category.getExcludeLists()) {
         String prefix = excludeList.getType() == this.defaultListRuleType ? "" : excludeList.getType().getId() + this.listRuleTypePrefixSeparator;
         excludeList.forEach(
            el -> excludeListBuilder.getList().add(GuiCategoryUIEditorSimpleDeletableWrapperData.Builder.<String>getDefault().setElement(prefix + el))
         );
      }

      editorDataBuilder.setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator).setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
      includeListBuilder.getIncludeInSuperToggleDataBuilder().setCurrentValue(Boolean.valueOf(category.getIncludeInSuperCategory()));
      excludeListBuilder.setExcludeMode(category.getExcludeMode());
      return editorDataBuilder;
   }

   protected CB getConfiguredBuilder(ED editorData) {
      CB categoryBuilder = super.getConfiguredBuilder(editorData);
      SD settingsData = editorData.getSettingsData();
      categoryBuilder.setBaseRule(settingsData.getBaseRule());
      categoryBuilder.setIncludeInSuperCategory(settingsData.getIncludeList().getIncludeInSuper());
      categoryBuilder.setExcludeMode(settingsData.getExcludeList().getExcludeMode());
      settingsData.getIncludeList()
         .getList()
         .forEach(
            led -> FilterObjectCategorySerializationHandler.handleListRuleSerializedElement(
                  (String)led.getElement(),
                  categoryBuilder::getIncludeListBuilder,
                  this.defaultListRuleType,
                  this.listRuleTypeGetter,
                  this.listRuleTypePrefixSeparator
               )
         );
      settingsData.getExcludeList()
         .getList()
         .forEach(
            led -> FilterObjectCategorySerializationHandler.handleListRuleSerializedElement(
                  (String)led.getElement(),
                  categoryBuilder::getExcludeListBuilder,
                  this.defaultListRuleType,
                  this.listRuleTypeGetter,
                  this.listRuleTypePrefixSeparator
               )
         );
      return categoryBuilder;
   }

   public abstract static class Builder<E, P, C extends FilterObjectCategory<E, P, ?, C>, ED extends GuiCategoryUIEditorFilterCategoryData<E, P, C, SD, ED>, CB extends FilterObjectCategory.Builder<E, P, C, CB>, SD extends GuiCategoryUIEditorFilterSettingsData<E, P, ?>, SDB extends GuiCategoryUIEditorFilterSettingsData.Builder<E, P, SD, SDB>, EDB extends GuiCategoryUIEditorFilterCategoryData.Builder<E, P, C, ED, SD, SDB, EDB>, B extends GuiFilterCategoryUIEditorDataConverter.Builder<E, P, C, ED, CB, SD, SDB, EDB, B>>
      extends GuiCategoryUIEditorDataConverter.Builder<C, ED, CB, SD, SDB, EDB, B> {
      protected ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
      protected Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
      protected String listRuleTypePrefixSeparator;
      protected Predicate<String> inputRuleTypeStringValidator;

      protected Builder(Supplier<CB> categoryBuilderFactory, Supplier<EDB> editorDataBuilderFactory) {
         super(categoryBuilderFactory, editorDataBuilderFactory);
      }

      protected B setDefault() {
         this.setDefaultListRuleType(null);
         this.setListRuleTypeGetter(null);
         this.setListRuleTypePrefixSeparator(";");
         this.setInputRuleTypeStringValidator(s -> s.matches("[a-z_0-9\\-]+"));
         return super.setDefault();
      }

      public B setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
         this.defaultListRuleType = defaultListRuleType;
         return this.self;
      }

      public B setListRuleTypeGetter(Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter) {
         this.listRuleTypeGetter = listRuleTypeGetter;
         return this.self;
      }

      public B setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
         this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
         return this.self;
      }

      public B setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
         this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
         return this.self;
      }

      @Override
      public GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> build() {
         if (this.defaultListRuleType != null && this.listRuleTypeGetter != null) {
            return super.build();
         } else {
            throw new IllegalStateException();
         }
      }

      protected abstract GuiFilterCategoryUIEditorDataConverter<E, P, C, ED, CB, SD, SDB, EDB> buildInternally();
   }
}
