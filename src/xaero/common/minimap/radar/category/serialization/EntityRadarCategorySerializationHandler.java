package xaero.common.minimap.radar.category.serialization;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import xaero.common.category.rule.ObjectCategoryHardRule;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.category.serialization.FilterObjectCategorySerializationHandler;
import xaero.common.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.common.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.common.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.common.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;

public final class EntityRadarCategorySerializationHandler
   extends FilterObjectCategorySerializationHandler<class_1297, class_1657, EntityRadarCategoryData, EntityRadarCategory, EntityRadarCategory.Builder, EntityRadarCategoryData.Builder> {
   protected EntityRadarCategorySerializationHandler(
      ObjectCategoryDataSerializer<EntityRadarCategoryData> serializer,
      Supplier<EntityRadarCategoryData.Builder> dataBuilderFactory,
      Supplier<EntityRadarCategory.Builder> objectCategoryBuilderFactory,
      Function<String, ObjectCategorySetting<?>> settingTypeGetter,
      Function<String, ObjectCategoryHardRule<class_1297, class_1657>> hardRuleGetter,
      ObjectCategoryListRuleType<class_1297, class_1657, ?> defaultListRuleType,
      Iterable<ObjectCategoryListRuleType<class_1297, class_1657, ?>> listRuleTypes,
      Function<String, ObjectCategoryListRuleType<class_1297, class_1657, ?>> listRuleTypeGetter,
      String listRuleTypePrefixSeparator
   ) {
      super(
         serializer,
         dataBuilderFactory,
         objectCategoryBuilderFactory,
         settingTypeGetter,
         hardRuleGetter,
         defaultListRuleType,
         listRuleTypeGetter,
         listRuleTypePrefixSeparator
      );
   }

   public static final class Builder
      extends FilterObjectCategorySerializationHandler.Builder<class_1297, class_1657, EntityRadarCategoryData, EntityRadarCategory, EntityRadarCategory.Builder, EntityRadarCategoryData.Builder, EntityRadarCategorySerializationHandler, EntityRadarCategorySerializationHandler.Builder> {
      private Builder(ObjectCategoryDataSerializer.Builder<EntityRadarCategoryData> serializerBuilder) {
         super(serializerBuilder);
      }

      public EntityRadarCategorySerializationHandler.Builder setDefault() {
         super.setDefault();
         this.setHardRuleGetter(EntityRadarCategoryHardRules.HARD_RULES::get);
         this.setDataBuilderFactory(EntityRadarCategoryConstants.DATA_BUILDER_FACTORY);
         this.setObjectCategoryBuilderFactory(EntityRadarCategoryConstants.CATEGORY_BUILDER_FACTORY);
         this.setSettingTypeGetter(EntityRadarCategorySettings.SETTINGS::get);
         this.setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
         this.setListRuleTypes(EntityRadarListRuleTypes.TYPE_LIST);
         this.setListRuleTypeGetter(EntityRadarListRuleTypes.TYPE_MAP::get);
         return this;
      }

      protected EntityRadarCategorySerializationHandler buildInternally() {
         return new EntityRadarCategorySerializationHandler(
            this.serializerBuilder.build(),
            this.dataBuilderFactory,
            this.objectCategoryBuilderFactory,
            this.settingTypeGetter,
            this.hardRuleGetter,
            this.defaultListRuleType,
            this.listRuleTypes,
            this.listRuleTypeGetter,
            this.listRuleTypePrefixSeparator
         );
      }

      public static EntityRadarCategorySerializationHandler.Builder getDefault(ObjectCategoryDataSerializer.Builder<EntityRadarCategoryData> serializerBuilder) {
         return new EntityRadarCategorySerializationHandler.Builder(serializerBuilder).setDefault();
      }
   }
}
