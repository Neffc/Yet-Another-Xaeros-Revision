package xaero.common.category.ui;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ObjectCategory;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.GuiCategoryUIEditorCategoryData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSettingsData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingData;

public abstract class GuiCategoryUIEditorDataConverter<C extends ObjectCategory<?, C>, ED extends GuiCategoryUIEditorCategoryData<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends GuiCategoryUIEditorSettingsData<?>, SDB extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB>, EDB extends GuiCategoryUIEditorCategoryData.Builder<C, ED, SD, SDB, EDB>> {
   private final Supplier<CB> categoryBuilderFactory;
   private final Supplier<EDB> editorDataBuilderFactory;

   public GuiCategoryUIEditorDataConverter(@Nonnull Supplier<CB> categoryBuilderFactory, @Nonnull Supplier<EDB> editorDataBuilderFactory) {
      this.categoryBuilderFactory = categoryBuilderFactory;
      this.editorDataBuilderFactory = editorDataBuilderFactory;
   }

   public ED convert(C category, boolean canBeRoot) {
      return this.getConfiguredBuilder(category, canBeRoot).build();
   }

   protected EDB getConfiguredBuilder(C category, boolean canBeRoot) {
      EDB editorDataBuilder = this.editorDataBuilderFactory.get();
      editorDataBuilder.setName(category.getName());
      SDB settingDataBuilder = editorDataBuilder.getSettingDataBuilder();
      category.getSettingOverridesIterator().forEachRemaining(e -> this.setSettingValue(settingDataBuilder, e.getKey(), e.getValue()));
      settingDataBuilder.setRootSettings(canBeRoot && category.getSuperCategory() == null);
      settingDataBuilder.setProtection(category.getProtection());
      category.getDirectSubCategoryIterator().forEachRemaining(sc -> editorDataBuilder.addSubCategoryBuilder(this.getConfiguredBuilder((C)sc, canBeRoot)));
      return editorDataBuilder;
   }

   private <T> void setSettingValue(SDB settingOverridesBuilder, ObjectCategorySetting<T> setting, Object value) {
      settingOverridesBuilder.setSettingValue(setting, (T)value);
   }

   public C convert(ED editorData) {
      return this.getConfiguredBuilder(editorData).build();
   }

   protected CB getConfiguredBuilder(ED editorData) {
      CB categoryBuilder = this.categoryBuilderFactory.get();
      categoryBuilder.setName(editorData.getName());
      categoryBuilder.setProtection(editorData.getSettingsData().getProtection());
      editorData.getSettingsData()
         .getSettings()
         .forEach((k, d) -> this.setSettingValue(categoryBuilder, k, ((IGuiCategoryUIEditorSettingData)d).getSettingValue()));
      editorData.getSubCategories().forEach(sed -> categoryBuilder.addSubCategoryBuilder(this.getConfiguredBuilder((ED)sed)));
      return categoryBuilder;
   }

   private <T> void setSettingValue(CB categoryBuilder, ObjectCategorySetting<T> setting, Object value) {
      categoryBuilder.setSettingValue(setting, (T)value);
   }

   public abstract static class Builder<C extends ObjectCategory<?, C>, ED extends GuiCategoryUIEditorCategoryData<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends GuiCategoryUIEditorSettingsData<?>, SDB extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB>, EDB extends GuiCategoryUIEditorCategoryData.Builder<C, ED, SD, SDB, EDB>, B extends GuiCategoryUIEditorDataConverter.Builder<C, ED, CB, SD, SDB, EDB, B>> {
      protected final B self = (B)this;
      protected final Supplier<CB> categoryBuilderFactory;
      protected final Supplier<EDB> editorDataBuilderFactory;

      protected Builder(Supplier<CB> categoryBuilderFactory, Supplier<EDB> editorDataBuilderFactory) {
         this.categoryBuilderFactory = categoryBuilderFactory;
         this.editorDataBuilderFactory = editorDataBuilderFactory;
      }

      protected B setDefault() {
         return this.self;
      }

      public GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> build() {
         return this.buildInternally();
      }

      protected abstract GuiCategoryUIEditorDataConverter<C, ED, CB, SD, SDB, EDB> buildInternally();
   }
}
