package xaero.common.category.ui.setting;

import xaero.common.category.ui.data.options.range.setting.GuiCategoryUIEditorCompactSettingData;
import xaero.common.category.ui.data.options.range.setting.GuiCategoryUIEditorExpandingSettingData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingDataBuilder;
import xaero.common.misc.ListFactory;

public final class CategorySettingsUIEditorSettingType {
   public static final CategorySettingsUIEditorSettingType ITERATION_BUTTON = new CategorySettingsUIEditorSettingType(
      true, new CategorySettingsUIEditorSettingType.SettingDataBuilderFactory() {
         @Override
         public <V> IGuiCategoryUIEditorSettingDataBuilder<V, ?> apply(ListFactory listFactory) {
            return GuiCategoryUIEditorCompactSettingData.Builder.getDefault();
         }
      }
   );
   public static final CategorySettingsUIEditorSettingType SLIDER = new CategorySettingsUIEditorSettingType(
      true, new CategorySettingsUIEditorSettingType.SettingDataBuilderFactory() {
         @Override
         public <V> IGuiCategoryUIEditorSettingDataBuilder<V, ?> apply(ListFactory listFactory) {
            return GuiCategoryUIEditorCompactSettingData.Builder.<V>getDefault().setSlider(true);
         }
      }
   );
   public static final CategorySettingsUIEditorSettingType EXPANDING = new CategorySettingsUIEditorSettingType(
      true, new CategorySettingsUIEditorSettingType.SettingDataBuilderFactory() {
         @Override
         public <V> IGuiCategoryUIEditorSettingDataBuilder<V, ?> apply(ListFactory listFactory) {
            return GuiCategoryUIEditorExpandingSettingData.Builder.getDefault(listFactory);
         }
      }
   );
   private final boolean usingIndices;
   private final CategorySettingsUIEditorSettingType.SettingDataBuilderFactory settingDataBuilderFactory;

   private CategorySettingsUIEditorSettingType(boolean usingIndices, CategorySettingsUIEditorSettingType.SettingDataBuilderFactory settingDataBuilderFactory) {
      this.usingIndices = usingIndices;
      this.settingDataBuilderFactory = settingDataBuilderFactory;
   }

   public CategorySettingsUIEditorSettingType.SettingDataBuilderFactory getSettingDataBuilderFactory() {
      return this.settingDataBuilderFactory;
   }

   public boolean isUsingIndices() {
      return this.usingIndices;
   }

   @FunctionalInterface
   public interface SettingDataBuilderFactory {
      <V> IGuiCategoryUIEditorSettingDataBuilder<V, ?> apply(ListFactory var1);
   }
}
