package xaero.common.minimap.render.radar.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_7923;
import xaero.hud.minimap.MinimapLogs;

public class EntityIconDefinitionReloader {
   private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

   public void reloadResources(Map<class_2960, EntityIconDefinition> iconDefinitions) {
      MinimapLogs.LOGGER.info("Reloading entity icon resources...");
      Set<class_2960> entityIds = class_7923.field_41177.method_10235();
      int attempts = 5;

      for (int i = 0; i < attempts; i++) {
         try {
            this.reloadResourcesAttempt(iconDefinitions, this.gson, entityIds);
            break;
         } catch (IOException var6) {
            if (i == attempts - 1) {
               throw new RuntimeException(var6);
            }
         }
      }

      MinimapLogs.LOGGER.info("Reloaded entity icon resources!");
   }

   private void reloadResourcesAttempt(Map<class_2960, EntityIconDefinition> iconDefinitions, Gson gson, Set<class_2960> entityIds) throws IOException {
      iconDefinitions.clear();

      for (class_2960 id : entityIds) {
         InputStream resourceInput = null;
         BufferedReader reader = null;
         String entityDefinitionJson = null;

         try {
            Optional<class_3298> oResource = class_310.method_1551()
               .method_1478()
               .method_14486(new class_2960("xaerominimap", "entity/icon/definition/" + id.method_12836() + "/" + id.method_12832() + ".json"));
            if (!oResource.isPresent()) {
               continue;
            }

            class_3298 resource = oResource.get();
            if (resource == null) {
               continue;
            }

            resourceInput = resource.method_14482();
            reader = new BufferedReader(new InputStreamReader(resourceInput));
            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(line -> {
               stringBuilder.append(line);
               stringBuilder.append('\n');
            });
            entityDefinitionJson = stringBuilder.toString();
         } finally {
            if (reader != null) {
               reader.close();
            }

            if (resourceInput != null) {
               resourceInput.close();
            }
         }

         try {
            EntityIconDefinition entityIconDefinition = (EntityIconDefinition)gson.fromJson(entityDefinitionJson, EntityIconDefinition.class);
            entityIconDefinition.onConstruct(id);
            iconDefinitions.put(id, entityIconDefinition);
         } catch (JsonSyntaxException var15) {
            MinimapLogs.LOGGER.error("Json syntax exception when loading the entity icon definition for " + id + ".", var15);
         }
      }
   }
}
