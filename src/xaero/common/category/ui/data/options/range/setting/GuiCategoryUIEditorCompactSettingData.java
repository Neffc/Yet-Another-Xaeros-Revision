package xaero.common.category.ui.data.options.range.setting;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import net.minecraft.class_1074;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.data.options.range.GuiCategoryUIEditorCompactRangeData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public final class GuiCategoryUIEditorCompactSettingData<V> extends GuiCategoryUIEditorCompactRangeData<V> implements IGuiCategoryUIEditorSettingData<V> {
   private final ObjectCategorySetting<V> setting;
   private final boolean rootSettings;

   private GuiCategoryUIEditorCompactSettingData(
      ObjectCategorySetting<V> setting,
      String displayName,
      V settingValue,
      boolean rootSettings,
      boolean hasNullOption,
      int currentIndex,
      int optionCount,
      int minNumber,
      IntFunction<V> numberReader,
      Function<V, String> valueNamer,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<Integer>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(
         displayName,
         settingValue,
         currentIndex,
         optionCount,
         minNumber,
         hasNullOption,
         numberReader,
         valueNamer,
         movable,
         listEntryFactory,
         tooltipSupplier,
         isActiveSupplier
      );
      this.setting = setting;
      this.rootSettings = rootSettings;
   }

   @Override
   public ObjectCategorySetting<V> getSetting() {
      return this.setting;
   }

   @Override
   public V getSettingValue() {
      return this.getCurrentRangeValue();
   }

   @Override
   public boolean isRootSettings() {
      return this.rootSettings;
   }

   public static <T> String getValueName(ObjectCategorySetting<T> setting, Object value) {
      return value == null ? class_1074.method_4662("gui.xaero_category_setting_inherit", new Object[0]) : setting.getWidgetValueNameProvider().apply((T)value);
   }

   public static final class Builder<V>
      extends GuiCategoryUIEditorCompactRangeData.Builder<V, GuiCategoryUIEditorCompactSettingData.Builder<V>>
      implements IGuiCategoryUIEditorSettingDataBuilder<V, GuiCategoryUIEditorCompactSettingData<V>> {
      private ObjectCategorySetting<V> setting;
      private boolean rootSettings;

      public GuiCategoryUIEditorCompactSettingData.Builder<V> setDefault() {
         this.setSetting(null);
         this.setRootSettings(false);
         return (GuiCategoryUIEditorCompactSettingData.Builder<V>)super.setDefault();
      }

      public GuiCategoryUIEditorCompactSettingData.Builder<V> setSetting(ObjectCategorySetting<V> setting) {
         this.setting = setting;
         if (setting == null) {
            this.setValueNamer(null);
            this.setNumberReader(null);
            this.setNumberWriter(null);
            this.setMinNumber(0);
            this.setMaxNumber(0);
            this.setTooltipSupplier(null);
         } else {
            this.setValueNamer(v -> GuiCategoryUIEditorCompactSettingData.getValueName(setting, v));
            this.setNumberReader(setting.getIndexReader());
            this.setNumberWriter(setting.getIndexWriter());
            this.setMinNumber(setting.getUiFirstOption());
            this.setMaxNumber(setting.getUiLastOption());
            this.setTooltipSupplier((parent, data) -> () -> setting.getTooltip());
         }

         return this;
      }

      public GuiCategoryUIEditorCompactSettingData.Builder<V> setSettingValue(V settingValue) {
         this.setCurrentRangeValue(settingValue);
         return this;
      }

      public GuiCategoryUIEditorCompactSettingData.Builder<V> setRootSettings(boolean rootSettings) {
         this.rootSettings = rootSettings;
         this.setHasNullOption(!rootSettings);
         return this;
      }

      public GuiCategoryUIEditorCompactSettingData.Builder<V> setSlider(boolean slider) {
         return (GuiCategoryUIEditorCompactSettingData.Builder<V>)super.setSlider(slider);
      }

      public GuiCategoryUIEditorCompactSettingData<V> build() {
         if (this.setting == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            if (this.displayName == null) {
               this.setDisplayName(this.setting.getDisplayName());
            }

            return (GuiCategoryUIEditorCompactSettingData<V>)super.build();
         }
      }

      public static <V> GuiCategoryUIEditorCompactSettingData.Builder<V> getDefault() {
         return new GuiCategoryUIEditorCompactSettingData.Builder<V>().setDefault();
      }

      @Override
      protected GuiCategoryUIEditorCompactRangeData<V> buildInternally(int currentIndex, int optionCount, CategorySettingsListMainEntryFactory listEntryFactory) {
         return new GuiCategoryUIEditorCompactSettingData<>(
            this.setting,
            this.displayName,
            this.currentRangeValue,
            this.rootSettings,
            this.hasNullOption,
            currentIndex,
            optionCount,
            this.minNumber,
            this.numberReader,
            this.valueNamer,
            this.movable,
            listEntryFactory,
            this.tooltipSupplier,
            this.isActiveSupplier
         );
      }
   }
}
