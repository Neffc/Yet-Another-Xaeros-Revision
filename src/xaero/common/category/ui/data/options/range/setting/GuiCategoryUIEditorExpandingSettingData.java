package xaero.common.category.ui.data.options.range.setting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.data.options.range.GuiCategoryUIEditorExpandingRangeData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorExpandingSettingData<V> extends GuiCategoryUIEditorExpandingRangeData<V> implements IGuiCategoryUIEditorSettingData<V> {
   private final ObjectCategorySetting<V> setting;
   private final boolean rootSettings;

   private GuiCategoryUIEditorExpandingSettingData(
      ObjectCategorySetting<V> setting,
      String displayName,
      V settingValue,
      boolean rootSettings,
      IntFunction<V> numberReader,
      GuiCategoryUIEditorOptionData<Integer> currentValue,
      List<GuiCategoryUIEditorOptionData<Integer>> options,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<Integer>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, settingValue, numberReader, currentValue, options, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
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

   public static final class Builder<V>
      extends GuiCategoryUIEditorExpandingRangeData.Builder<V, GuiCategoryUIEditorExpandingSettingData.Builder<V>>
      implements IGuiCategoryUIEditorSettingDataBuilder<V, GuiCategoryUIEditorExpandingSettingData<V>> {
      private ObjectCategorySetting<V> setting;
      private boolean rootSettings;

      protected Builder(ListFactory listFactory) {
         super(listFactory);
      }

      public GuiCategoryUIEditorExpandingSettingData.Builder<V> setDefault() {
         this.setSetting(null);
         this.setRootSettings(false);
         return (GuiCategoryUIEditorExpandingSettingData.Builder<V>)super.setDefault();
      }

      public GuiCategoryUIEditorExpandingSettingData.Builder<V> setSetting(ObjectCategorySetting<V> setting) {
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

      public GuiCategoryUIEditorExpandingSettingData.Builder<V> setSettingValue(V settingValue) {
         this.setCurrentRangeValue(settingValue);
         return this;
      }

      public GuiCategoryUIEditorExpandingSettingData.Builder<V> setRootSettings(boolean rootSettings) {
         this.rootSettings = rootSettings;
         this.setHasNullOption(!rootSettings);
         return this;
      }

      public GuiCategoryUIEditorExpandingSettingData<V> build() {
         if (this.setting == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            if (this.displayName == null) {
               this.setDisplayName(this.setting.getDisplayName());
            }

            this.optionBuilders.clear();
            return (GuiCategoryUIEditorExpandingSettingData<V>)super.build();
         }
      }

      protected GuiCategoryUIEditorExpandingSettingData<V> buildInternally(
         GuiCategoryUIEditorOptionData<Integer> currentValueData, List<GuiCategoryUIEditorOptionData<Integer>> options
      ) {
         return new GuiCategoryUIEditorExpandingSettingData<>(
            this.setting,
            this.displayName,
            this.currentRangeValue,
            this.rootSettings,
            this.numberReader,
            currentValueData,
            options,
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.isActiveSupplier
         );
      }

      public static <V> GuiCategoryUIEditorExpandingSettingData.Builder<V> getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorExpandingSettingData.Builder<V>(listFactory).setDefault();
      }
   }
}
