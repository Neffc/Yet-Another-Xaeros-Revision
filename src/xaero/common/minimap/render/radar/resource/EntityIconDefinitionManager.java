package xaero.common.minimap.render.radar.resource;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_2960;

public class EntityIconDefinitionManager {
   private final Map<class_2960, EntityIconDefinition> iconDefinitions = new HashMap<>();
   private final EntityIconDefinitionReloader reloader = new EntityIconDefinitionReloader();

   public EntityIconDefinition get(class_2960 key) {
      return this.iconDefinitions.get(key);
   }

   public void reloadResources() {
      this.reloader.reloadResources(this.iconDefinitions);
   }
}
