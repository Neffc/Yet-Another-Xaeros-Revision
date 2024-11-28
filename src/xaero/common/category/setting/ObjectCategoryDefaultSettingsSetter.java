package xaero.common.category.setting;

import java.util.Map;
import javax.annotation.Nonnull;
import xaero.common.category.ObjectCategory;

public final class ObjectCategoryDefaultSettingsSetter {
   private final Map<String, ObjectCategorySetting<?>> settings;

   private ObjectCategoryDefaultSettingsSetter(@Nonnull Map<String, ObjectCategorySetting<?>> settings) {
      this.settings = settings;
   }

   public void setDefaultsFor(ObjectCategory<?, ?> category, boolean onlyNew) {
      this.settings.forEach((k, setting) -> {
         if (!onlyNew || category.getSettingValue(setting) == null) {
            this.setForSetting(category, (ObjectCategorySetting<?>)setting);
         }
      });
   }

   private <T> void setForSetting(ObjectCategory<?, ?> category, ObjectCategorySetting<T> setting) {
      category.setSettingValue(setting, setting.getDefaultValue());
   }

   public static final class Builder {
      private Map<String, ObjectCategorySetting<?>> settings;

      private Builder() {
      }

      public ObjectCategoryDefaultSettingsSetter.Builder setDefault() {
         this.setSettings(null);
         return this;
      }

      public ObjectCategoryDefaultSettingsSetter.Builder setSettings(Map<String, ObjectCategorySetting<?>> settings) {
         this.settings = settings;
         return this;
      }

      public ObjectCategoryDefaultSettingsSetter build() {
         if (this.settings == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return new ObjectCategoryDefaultSettingsSetter(this.settings);
         }
      }

      public static ObjectCategoryDefaultSettingsSetter.Builder getDefault() {
         return new ObjectCategoryDefaultSettingsSetter.Builder().setDefault();
      }
   }
}
