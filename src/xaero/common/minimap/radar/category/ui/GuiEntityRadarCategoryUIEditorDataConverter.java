package xaero.common.minimap.radar.category.ui;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.category.ui.GuiFilterCategoryUIEditorDataConverter;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.common.minimap.radar.category.ui.data.GuiEntityRadarCategoryUIEditorSettingsData;

public final class GuiEntityRadarCategoryUIEditorDataConverter
   extends GuiFilterCategoryUIEditorDataConverter<class_1297, class_1657, EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, EntityRadarCategory.Builder, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder> {
   protected GuiEntityRadarCategoryUIEditorDataConverter(
      @Nonnull Supplier<EntityRadarCategory.Builder> categoryBuilderFactory,
      @Nonnull Supplier<GuiEntityRadarCategoryUIEditorData.Builder> editorDataBuilderFactory,
      ObjectCategoryListRuleType<class_1297, class_1657, ?> defaultListRuleType,
      Function<String, ObjectCategoryListRuleType<class_1297, class_1657, ?>> listRuleTypeGetter,
      String listRuleTypePrefixSeparator,
      Predicate<String> inputRuleTypeStringValidator
   ) {
      super(
         categoryBuilderFactory, editorDataBuilderFactory, defaultListRuleType, listRuleTypeGetter, listRuleTypePrefixSeparator, inputRuleTypeStringValidator
      );
   }

   public static final class Builder
      extends GuiFilterCategoryUIEditorDataConverter.Builder<class_1297, class_1657, EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, EntityRadarCategory.Builder, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder, GuiEntityRadarCategoryUIEditorDataConverter.Builder> {
      protected Builder() {
         super(EntityRadarCategory.Builder::getDefault, GuiEntityRadarCategoryUIEditorData.Builder::getDefault);
      }

      protected GuiEntityRadarCategoryUIEditorDataConverter.Builder setDefault() {
         super.setDefault();
         this.setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
         this.setListRuleTypeGetter(EntityRadarListRuleTypes.TYPE_MAP::get);
         return this;
      }

      protected GuiEntityRadarCategoryUIEditorDataConverter buildInternally() {
         return new GuiEntityRadarCategoryUIEditorDataConverter(
            this.categoryBuilderFactory,
            this.editorDataBuilderFactory,
            this.defaultListRuleType,
            this.listRuleTypeGetter,
            this.listRuleTypePrefixSeparator,
            this.inputRuleTypeStringValidator
         );
      }

      public static GuiEntityRadarCategoryUIEditorDataConverter.Builder getDefault() {
         return new GuiEntityRadarCategoryUIEditorDataConverter.Builder().setDefault();
      }
   }
}
