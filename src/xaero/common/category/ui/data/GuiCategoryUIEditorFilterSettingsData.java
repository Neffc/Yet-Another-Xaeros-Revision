package xaero.common.category.ui.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.rule.ObjectCategoryHardRule;
import xaero.common.category.rule.ObjectCategoryRule;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorExpandingOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorSimpleButtonData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorExcludeListData;
import xaero.common.category.ui.data.rule.GuiCategoryUIEditorIncludeListData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;

public class GuiCategoryUIEditorFilterSettingsData<E, P, SETTING_DATA extends GuiCategoryUIEditorOptionsData<?> & IGuiCategoryUIEditorSettingData<?>>
   extends GuiCategoryUIEditorSettingsData<SETTING_DATA> {
   private final GuiCategoryUIEditorExpandingOptionsData<ObjectCategoryRule<E, P>> baseRule;
   private final GuiCategoryUIEditorIncludeListData includeList;
   private final GuiCategoryUIEditorExcludeListData excludeList;

   protected GuiCategoryUIEditorFilterSettingsData(
      Map<ObjectCategorySetting<?>, SETTING_DATA> settings,
      List<SETTING_DATA> settingList,
      @Nonnull GuiCategoryUIEditorSimpleButtonData deleteButton,
      @Nonnull GuiCategoryUIEditorSimpleButtonData protectionButton,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData nameOption,
      ListFactory listFactory,
      boolean rootSettings,
      @Nonnull GuiCategoryUIEditorExpandingOptionsData<ObjectCategoryRule<E, P>> baseRule,
      @Nonnull GuiCategoryUIEditorIncludeListData includeList,
      @Nonnull GuiCategoryUIEditorExcludeListData excludeList,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      boolean protection
   ) {
      super(
         settings, settingList, deleteButton, protectionButton, nameOption, listFactory, rootSettings, movable, listEntryFactory, tooltipSupplier, protection
      );
      this.baseRule = baseRule;
      this.includeList = includeList;
      this.excludeList = excludeList;
   }

   public ObjectCategoryRule<E, P> getBaseRule() {
      return this.baseRule.getCurrentValue().getValue();
   }

   public GuiCategoryUIEditorIncludeListData getIncludeList() {
      return this.includeList;
   }

   public GuiCategoryUIEditorExcludeListData getExcludeList() {
      return this.excludeList;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      List<GuiCategoryUIEditorExpandableData<?>> result = super.getSubExpandables();
      result.add(4, this.excludeList);
      result.add(4, this.includeList);
      result.add(4, this.baseRule);
      return result;
   }

   public abstract static class Builder<E, P, SD extends GuiCategoryUIEditorFilterSettingsData<E, P, ?>, SDB extends GuiCategoryUIEditorFilterSettingsData.Builder<E, P, SD, SDB>>
      extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB> {
      protected final GuiCategoryUIEditorExpandingOptionsData.Builder<ObjectCategoryRule<E, P>, ?> baseRuleBuilder;
      private final GuiCategoryUIEditorIncludeListData.Builder<E, P> includeListBuilder;
      private final GuiCategoryUIEditorExcludeListData.Builder<E, P> excludeListBuilder;
      private final List<ObjectCategoryHardRule<E, P>> allRules;
      private String listRuleTypePrefixSeparator;
      private Predicate<String> inputRuleTypeStringValidator;

      protected Builder(MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings, List<ObjectCategoryHardRule<E, P>> allRules) {
         super(mapFactory, listFactory, allSettings);
         this.baseRuleBuilder = GuiCategoryUIEditorExpandingOptionsData.FinalBuilder.getDefault(listFactory);
         this.includeListBuilder = GuiCategoryUIEditorIncludeListData.Builder.getDefault(listFactory);
         this.excludeListBuilder = GuiCategoryUIEditorExcludeListData.Builder.getDefault(listFactory);
         this.allRules = allRules;
      }

      public SDB setDefault() {
         super.setDefault();
         this.setBaseRule(null);
         this.baseRuleBuilder
            .setDefault()
            .setDisplayName(class_1074.method_4662("gui.xaero_category_hard_include", new Object[0]))
            .setIsActiveSupplier((parent, data) -> !((GuiCategoryUIEditorSettingsData)parent).getProtection());

         for (ObjectCategoryRule<E, P> rule : this.allRules) {
            this.baseRuleBuilder.addOptionBuilderFor(rule);
         }

         this.includeListBuilder.setDefault();
         this.includeListBuilder.getIncludeInSuperToggleDataBuilder().setCurrentValue(Boolean.valueOf(true));
         this.excludeListBuilder.setDefault();
         this.setListRuleTypePrefixSeparator(null);
         this.setInputRuleTypeStringValidator(null);
         return this.self;
      }

      public void setBaseRule(ObjectCategoryRule<E, P> baseRule) {
         this.baseRuleBuilder.setCurrentValue(baseRule);
      }

      public SDB setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
         this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
         return this.self;
      }

      public SDB setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
         this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
         return this.self;
      }

      public GuiCategoryUIEditorIncludeListData.Builder<E, P> getIncludeListBuilder() {
         return this.includeListBuilder;
      }

      public GuiCategoryUIEditorExcludeListData.Builder<E, P> getExcludeListBuilder() {
         return this.excludeListBuilder;
      }

      protected GuiCategoryUIEditorIncludeListData buildIncludeList() {
         return this.includeListBuilder.build();
      }

      protected GuiCategoryUIEditorExcludeListData buildExcludeList() {
         return this.excludeListBuilder.build();
      }

      public SD build() {
         if (this.baseRuleBuilder != null && this.listRuleTypePrefixSeparator != null) {
            this.includeListBuilder
               .setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator)
               .setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
            this.excludeListBuilder
               .setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator)
               .setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
            return (SD)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }
   }

   public static final class FinalBuilder<E, P>
      extends GuiCategoryUIEditorFilterSettingsData.Builder<E, P, GuiCategoryUIEditorFilterSettingsData<E, P, ?>, GuiCategoryUIEditorFilterSettingsData.FinalBuilder<E, P>> {
      protected FinalBuilder(
         MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings, List<ObjectCategoryHardRule<E, P>> allRules
      ) {
         super(mapFactory, listFactory, allSettings, allRules);
      }

      protected GuiCategoryUIEditorFilterSettingsData<E, P, ?> buildInternally(
         List<IGuiCategoryUIEditorSettingData<?>> builtSettingData, Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingData<?>> builtSettingsDataMap
      ) {
         return new GuiCategoryUIEditorFilterSettingsData<>(
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

      public static <E, P, S> GuiCategoryUIEditorFilterSettingsData.FinalBuilder<E, P> getDefault(
         MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings, List<ObjectCategoryHardRule<E, P>> allRules
      ) {
         return new GuiCategoryUIEditorFilterSettingsData.FinalBuilder<>(mapFactory, listFactory, allSettings, allRules).setDefault();
      }
   }
}
