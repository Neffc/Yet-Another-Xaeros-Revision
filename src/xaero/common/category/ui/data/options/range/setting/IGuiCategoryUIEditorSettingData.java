package xaero.common.category.ui.data.options.range.setting;

import xaero.common.category.setting.ObjectCategorySetting;

public interface IGuiCategoryUIEditorSettingData<V> {
   ObjectCategorySetting<V> getSetting();

   V getSettingValue();

   boolean isRootSettings();
}
