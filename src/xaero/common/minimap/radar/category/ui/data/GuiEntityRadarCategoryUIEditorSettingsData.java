package xaero.common.minimap.radar.category.ui.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import xaero.common.category.rule.ObjectCategoryRule;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorFilterSettingsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorExpandingOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorSimpleButtonData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorExcludeListData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorIncludeListData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.common.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.common.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.misc.ListFactory;

public class GuiEntityRadarCategoryUIEditorSettingsData<SETTING_DATA extends GuiCategoryUIEditorOptionsData<?> & IGuiCategoryUIEditorSettingData<?>>
   extends GuiCategoryUIEditorFilterSettingsData<class_1297, class_1657, SETTING_DATA> {
   public GuiEntityRadarCategoryUIEditorSettingsData(
      Map<ObjectCategorySetting<?>, SETTING_DATA> settings,
      List<SETTING_DATA> settingList,
      @Nonnull GuiCategoryUIEditorSimpleButtonData deleteButton,
      @Nonnull GuiCategoryUIEditorSimpleButtonData protectionButton,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData nameOption,
      ListFactory listFactory,
      boolean rootSettings,
      GuiCategoryUIEditorExpandingOptionsData<ObjectCategoryRule<class_1297, class_1657>> baseRule,
      GuiCategoryUIEditorIncludeListData includeList,
      GuiCategoryUIEditorExcludeListData excludeList,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      boolean protection
   ) {
      super(
         settings,
         settingList,
         deleteButton,
         protectionButton,
         nameOption,
         listFactory,
         rootSettings,
         baseRule,
         includeList,
         excludeList,
         movable,
         listEntryFactory,
         tooltipSupplier,
         protection
      );
   }

   public static final class Builder
      extends GuiCategoryUIEditorFilterSettingsData.Builder<class_1297, class_1657, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder> {
      protected Builder() {
         super(
            EntityRadarCategoryConstants.MAP_FACTORY,
            EntityRadarCategoryConstants.LIST_FACTORY,
            EntityRadarCategorySettings.SETTINGS_LIST,
            EntityRadarCategoryHardRules.HARD_RULES_LIST
         );
      }

      public GuiEntityRadarCategoryUIEditorSettingsData.Builder setDefault() {
         super.setDefault();
         this.getIncludeListBuilder().setTooltipSupplier((parent, bd) -> {
            CursorBox listTooltip = new CursorBox(class_1074.method_4662("gui.xaero_box_category_include_list2", new Object[0]));
            return () -> listTooltip;
         });
         this.getExcludeListBuilder().setTooltipSupplier((parent, bd) -> {
            CursorBox listTooltip = new CursorBox(class_1074.method_4662("gui.xaero_box_category_exclude_list2", new Object[0]));
            return () -> listTooltip;
         });
         this.baseRuleBuilder.setTooltipSupplier((parent, bd) -> {
            CursorBox hardIncludeTooltip = new CursorBox(class_1074.method_4662("gui.xaero_box_category_hard_include2", new Object[0]));
            return () -> hardIncludeTooltip;
         });
         this.baseRuleBuilder.setCurrentValue(EntityRadarCategoryHardRules.IS_NOTHING);
         this.getIncludeListBuilder().getIncludeInSuperToggleDataBuilder().setTooltipSupplier((parent, data) -> {
            CursorBox tooltip = new CursorBox(class_1074.method_4662("gui.xaero_box_category_include_list_include_in_super2", new Object[0]));
            return () -> tooltip;
         });
         CursorBox listHelp = new CursorBox("gui.xaero_box_category_list_add");
         BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier = (parent, data) -> data.isExpanded()
               ? () -> listHelp
               : null;
         this.getIncludeListBuilder().setHelpTooltipSupplier(helpTooltipSupplier);
         this.getExcludeListBuilder().setHelpTooltipSupplier(helpTooltipSupplier);
         GuiCategoryUIEditorTextFieldOptionsData.Builder includeListAdderBuilder = this.getIncludeListBuilder().getAdderBuilder();
         GuiCategoryUIEditorTextFieldOptionsData.Builder excludeListAdderBuilder = this.getExcludeListBuilder().getAdderBuilder();
         includeListAdderBuilder.setAllowCustomInput(true);
         excludeListAdderBuilder.setAllowCustomInput(true);
         this.getIncludeListBuilder().setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
         this.getIncludeListBuilder().setListRuleTypes(EntityRadarListRuleTypes.TYPE_LIST);
         this.getExcludeListBuilder().setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
         this.getExcludeListBuilder().setListRuleTypes(EntityRadarListRuleTypes.TYPE_LIST);
         return this;
      }

      public static GuiEntityRadarCategoryUIEditorSettingsData.Builder getDefault() {
         return new GuiEntityRadarCategoryUIEditorSettingsData.Builder().setDefault();
      }

      protected GuiEntityRadarCategoryUIEditorSettingsData<?> buildInternally(
         List<IGuiCategoryUIEditorSettingData<?>> builtSettingData, Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingData<?>> builtSettingsDataMap
      ) {
         return new GuiEntityRadarCategoryUIEditorSettingsData(
            builtSettingsDataMap,
            builtSettingData,
            this.deleteButtonBuilder.build(),
            this.protectionButtonBuilder.build(),
            this.nameOptionBuilder.build(),
            this.listFactory,
            this.rootSettings,
            this.baseRuleBuilder.build(),
            this.buildIncludeList(),
            this.buildExcludeList(),
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.protection
         );
      }
   }
}
