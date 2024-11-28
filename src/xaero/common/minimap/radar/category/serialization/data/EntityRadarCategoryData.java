package xaero.common.minimap.radar.category.serialization.data;

import java.util.List;
import java.util.Map;
import xaero.common.category.rule.ExcludeListMode;
import xaero.common.category.serialization.data.FilterObjectCategoryData;
import xaero.common.minimap.radar.category.EntityRadarCategoryConstants;

public final class EntityRadarCategoryData extends FilterObjectCategoryData<EntityRadarCategoryData> {
   private EntityRadarCategoryData(
      String name,
      String hardInclude,
      List<String> includeList,
      List<String> excludeList,
      ExcludeListMode excludeMode,
      Map<String, Object> settingOverrides,
      List<EntityRadarCategoryData> subCategories,
      boolean protection,
      boolean includeInSuperCategory
   ) {
      super(name, hardInclude, includeList, excludeList, excludeMode, settingOverrides, subCategories, protection, includeInSuperCategory);
   }

   public static final class Builder extends FilterObjectCategoryData.Builder<EntityRadarCategoryData, EntityRadarCategoryData.Builder> {
      public Builder() {
         super(EntityRadarCategoryConstants.LIST_FACTORY, EntityRadarCategoryConstants.MAP_FACTORY);
      }

      protected EntityRadarCategoryData buildInternally() {
         return new EntityRadarCategoryData(
            this.name,
            this.hardInclude,
            this.includeList,
            this.excludeList,
            this.excludeMode,
            this.settingOverrides,
            this.buildSubCategories(),
            this.protection,
            this.includeListInSuperCategory
         );
      }

      public static EntityRadarCategoryData.Builder getDefault() {
         return new EntityRadarCategoryData.Builder();
      }
   }
}
