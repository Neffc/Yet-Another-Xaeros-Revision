package xaero.common.category.setting;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.ui.setting.CategorySettingsUIEditorSettingType;
import xaero.common.graphics.CursorBox;

public final class ObjectCategorySetting<T> {
   private final String id;
   private final String displayName;
   private final T defaultValue;
   private CursorBox tooltip;
   private final CategorySettingsUIEditorSettingType settingUIType;
   private final int uiFirstOption;
   private final int uiLastOption;
   private final IntFunction<T> indexReader;
   private final Function<T, Integer> indexWriter;
   private final Function<T, String> uiValueNameProvider;

   private ObjectCategorySetting(
      @Nonnull String id,
      @Nonnull String displayName,
      @Nonnull T defaultValue,
      @Nonnull CategorySettingsUIEditorSettingType settingUIType,
      int uiFirstOption,
      int uiLastOption,
      IntFunction<T> indexReader,
      Function<T, Integer> indexWriter,
      Function<T, String> uiValueNameProvider,
      CursorBox tooltip
   ) {
      this.id = id;
      this.displayName = displayName;
      this.defaultValue = defaultValue;
      this.settingUIType = settingUIType;
      this.tooltip = tooltip;
      this.uiFirstOption = uiFirstOption;
      this.uiLastOption = uiLastOption;
      this.indexReader = indexReader;
      this.indexWriter = indexWriter;
      this.uiValueNameProvider = uiValueNameProvider;
   }

   public CategorySettingsUIEditorSettingType getSettingUIType() {
      return this.settingUIType;
   }

   public String getId() {
      return this.id;
   }

   public String getDisplayName() {
      return class_1074.method_4662(this.displayName, new Object[0]);
   }

   public T getDefaultValue() {
      return this.defaultValue;
   }

   public CursorBox getTooltip() {
      return this.tooltip;
   }

   public int getUiFirstOption() {
      return this.uiFirstOption;
   }

   public int getUiLastOption() {
      return this.uiLastOption;
   }

   public IntFunction<T> getIndexReader() {
      return this.indexReader;
   }

   public Function<T, Integer> getIndexWriter() {
      return this.indexWriter;
   }

   public Function<T, String> getWidgetValueNameProvider() {
      return this.uiValueNameProvider;
   }

   public static final class Builder<T> {
      private String id;
      private String displayName;
      private T defaultValue;
      private CategorySettingsUIEditorSettingType settingUIType;
      private CursorBox tooltip;
      private int uiFirstOption;
      private int uiLastOption;
      private IntFunction<T> indexReader;
      private Function<T, Integer> indexWriter;
      private Function<T, String> uiValueNameProvider;

      public ObjectCategorySetting.Builder<T> setDefault() {
         this.setId(null);
         this.setDisplayName(null);
         this.setSettingUIType(null);
         this.setTooltip(null);
         this.setUiFirstOption(0);
         this.setUiLastOption(0);
         this.setIndexReader(null);
         this.setIndexWriter(null);
         this.setUiValueNameProvider(null);
         return this;
      }

      public ObjectCategorySetting.Builder<T> setId(String id) {
         this.id = id;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setDisplayName(String displayName) {
         this.displayName = displayName;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setDefaultValue(T defaultValue) {
         this.defaultValue = defaultValue;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setSettingUIType(CategorySettingsUIEditorSettingType settingUIType) {
         this.settingUIType = settingUIType;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setTooltip(CursorBox tooltip) {
         this.tooltip = tooltip;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setUiFirstOption(int widgetFirstOption) {
         this.uiFirstOption = widgetFirstOption;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setUiLastOption(int widgetLastOption) {
         this.uiLastOption = widgetLastOption;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setIndexReader(IntFunction<T> widgetReader) {
         this.indexReader = widgetReader;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setIndexWriter(Function<T, Integer> widgetWriter) {
         this.indexWriter = widgetWriter;
         return this;
      }

      public ObjectCategorySetting.Builder<T> setUiValueNameProvider(Function<T, String> widgetValueNameProvider) {
         this.uiValueNameProvider = widgetValueNameProvider;
         return this;
      }

      public ObjectCategorySetting<T> build(Map<String, ObjectCategorySetting<?>> destination, List<ObjectCategorySetting<?>> destinationList) {
         if (this.id != null
            && this.displayName != null
            && this.defaultValue != null
            && this.settingUIType != null
            && (!this.settingUIType.isUsingIndices() || this.indexReader != null && this.indexWriter != null && this.uiValueNameProvider != null)) {
            ObjectCategorySetting<T> result = new ObjectCategorySetting<>(
               this.id,
               this.displayName,
               this.defaultValue,
               this.settingUIType,
               this.uiFirstOption,
               this.uiLastOption,
               this.indexReader,
               this.indexWriter,
               this.uiValueNameProvider,
               this.tooltip
            );
            if (destination != null) {
               destination.put(result.getId(), result);
               destinationList.add(result);
            }

            return result;
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      public static <T> ObjectCategorySetting.Builder<T> getDefault() {
         return new ObjectCategorySetting.Builder<T>().setDefault();
      }
   }
}
