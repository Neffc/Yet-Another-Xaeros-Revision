package xaero.common.minimap.radar;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;

public final class MinimapRadarList implements Comparable<MinimapRadarList> {
   private EntityRadarCategory category;
   private final List<class_1297> entities = new ArrayList<>();

   private MinimapRadarList() {
      this.category = null;
   }

   public EntityRadarCategory getCategory() {
      return this.category;
   }

   public MinimapRadarList setCategory(EntityRadarCategory category) {
      this.category = category;
      return this;
   }

   public List<class_1297> getEntities() {
      return this.entities;
   }

   public int compareTo(MinimapRadarList o) {
      return this.category
         .getSettingValue(EntityRadarCategorySettings.RENDER_ORDER)
         .compareTo(o.category.getSettingValue(EntityRadarCategorySettings.RENDER_ORDER));
   }

   public static final class Builder {
      private Builder() {
      }

      public MinimapRadarList.Builder setDefault() {
         return this;
      }

      public MinimapRadarList build() {
         return new MinimapRadarList();
      }

      public static MinimapRadarList.Builder getDefault() {
         return new MinimapRadarList.Builder().setDefault();
      }
   }
}
