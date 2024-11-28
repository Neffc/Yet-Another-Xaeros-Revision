package xaero.common.category.ui.data.options.range.setting;

import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;

public interface IGuiCategoryUIEditorSettingDataBuilder<V, SD extends GuiCategoryUIEditorOptionsData<Integer> & IGuiCategoryUIEditorSettingData<V>> {
   IGuiCategoryUIEditorSettingDataBuilder<V, SD> setSetting(ObjectCategorySetting<V> var1);

   IGuiCategoryUIEditorSettingDataBuilder<V, SD> setSettingValue(V var1);

   IGuiCategoryUIEditorSettingDataBuilder<V, SD> setRootSettings(boolean var1);

   SD build();
}
