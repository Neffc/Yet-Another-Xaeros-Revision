package xaero.common.minimap.radar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_270;
import net.minecraft.class_638;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.category.rule.resolver.ObjectCategoryRuleResolver;
import xaero.common.effect.Effects;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public class MinimapRadar {
   public static final int radarPlayers = -1;
   public static final int radarShadow = -16777216;
   private IXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   private EntityRadarCategoryManager entityCategoryManager;
   private class_1297 lastRenderViewEntity;
   private EntityRadarCategory listsGeneratedForConfig;
   private boolean listsReversedOrder;
   private List<MinimapRadarList> radarLists;
   private Map<EntityRadarCategory, MinimapRadarList> middleRadarListMap;

   public MinimapRadar(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, EntityRadarCategoryManager entityCategoryManager) throws IOException {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.entityCategoryManager = entityCategoryManager;
      this.radarLists = new ArrayList<>();
      this.middleRadarListMap = new HashMap<>();
   }

   private void ensureCategories(
      EntityRadarCategory rootCategory, List<MinimapRadarList> radarLists, Map<EntityRadarCategory, MinimapRadarList> middleRadarListMap
   ) {
      boolean reversedOrder = ModSettings.keyReverseEntityRadar.method_1434();
      if (this.listsGeneratedForConfig != rootCategory) {
         middleRadarListMap.clear();
         radarLists.clear();
         this.traceAddCategories(rootCategory, middleRadarListMap, radarLists);
         Collections.sort(radarLists);
         this.listsGeneratedForConfig = rootCategory;
         this.listsReversedOrder = false;
      }

      if (this.listsReversedOrder != reversedOrder) {
         Collections.reverse(radarLists);
         this.listsReversedOrder = reversedOrder;
      }
   }

   private void traceAddCategories(
      EntityRadarCategory category, Map<EntityRadarCategory, MinimapRadarList> middleRadarListMap, List<MinimapRadarList> radarLists
   ) {
      category.getDirectSubCategoryIterator().forEachRemaining(sb -> this.traceAddCategories(sb, middleRadarListMap, radarLists));
      MinimapRadarList radarList = MinimapRadarList.Builder.getDefault().build().setCategory(category);
      middleRadarListMap.put(category, radarList);
      radarLists.add(radarList);
   }

   public void updateRadar(class_638 world, class_1657 p, class_1297 renderEntity, MinimapProcessor minimap) {
      if (renderEntity == null) {
         renderEntity = this.lastRenderViewEntity;
      }

      List<MinimapRadarList> radarLists = this.radarLists;
      Map<EntityRadarCategory, MinimapRadarList> middleRadarListMap = this.middleRadarListMap;
      EntityRadarCategoryManager entityCategoryManager = this.entityCategoryManager;
      EntityRadarCategory rootCategory = entityCategoryManager.getRootCategory();
      this.ensureCategories(rootCategory, radarLists, middleRadarListMap);
      radarLists.forEach(l -> l.getEntities().clear());
      if (!this.modMain.isFairPlay()
         && (
            this.modMain.getSettings().getEntityRadar()
               || this.modMain.getSupportMods().worldmap() && this.modMain.getSupportMods().worldmapSupport.worldMapIsRenderingRadar()
         )
         && world != null
         && p != null
         && renderEntity != null
         && !Misc.hasEffect(p, Effects.NO_RADAR)
         && !Misc.hasEffect(p, Effects.NO_RADAR_HARMFUL)) {
         if (MinimapClientWorldDataHelper.getWorldData(world).getSyncedRules().allowRadarOnServer) {
            ObjectCategoryRuleResolver categoryRuleResolver = entityCategoryManager.getRuleResolver();
            Iterable<class_1297> worldEntities = world.method_18112();
            boolean hideInvisible = this.modMain.getSettings().radarHideInvisibleEntities;

            for (class_1297 e : worldEntities) {
               try {
                  if (e != null && (!hideInvisible || !e.method_5756(p) && !this.shouldHideForSneaking(e, p))) {
                     EntityRadarCategory entityCategory = categoryRuleResolver.resolve(rootCategory, e, p);
                     if (entityCategory != null && entityCategory.getSettingValue(EntityRadarCategorySettings.DISPLAYED)) {
                        double offh = renderEntity.method_23318() - e.method_23318();
                        double offheight2 = offh * offh;
                        int heightLimit = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT).intValue();
                        if (!(offheight2 > (double)(heightLimit * heightLimit))) {
                           List<class_1297> typeList = middleRadarListMap.get(entityCategory).getEntities();
                           int entityAmount = entityCategory.getSettingValue(EntityRadarCategorySettings.ENTITY_NUMBER).intValue();
                           if (entityAmount == 0 || typeList.size() < entityAmount) {
                              typeList.add(e);
                           }
                        }
                     }
                  }
               } catch (Exception var22) {
               }
            }
         }
      }
   }

   private boolean shouldHideForSneaking(class_1297 e, class_1657 p) {
      boolean sneaking = e.method_5715();
      if (!sneaking) {
         return false;
      } else {
         class_270 team = e.method_5781();
         return team == null || team != p.method_5781();
      }
   }

   public double getEntityX(class_1297 e, float partial) {
      double xOld = e.field_6012 > 0 ? e.field_6038 : e.method_23317();
      return xOld + (e.method_23317() - xOld) * (double)partial;
   }

   public double getEntityY(class_1297 e, float partial) {
      double yOld = e.field_6012 > 0 ? e.field_5971 : e.method_23318();
      return yOld + (e.method_23318() - yOld) * (double)partial;
   }

   public double getEntityZ(class_1297 e, float partial) {
      double zOld = e.field_6012 > 0 ? e.field_5989 : e.method_23321();
      return zOld + (e.method_23321() - zOld) * (double)partial;
   }

   public int getTeamColour(class_1297 e) {
      Integer teamColour = null;
      class_270 team = e.method_5781();
      if (team != null) {
         teamColour = team.method_1202().method_532();
      }

      return teamColour == null ? -1 : teamColour;
   }

   public int getEntityColour(
      class_1657 p,
      class_1297 e,
      float offh,
      boolean cave,
      EntityRadarCategory category,
      int heightLimit,
      int startFadingAt,
      boolean heightBasedFade,
      int colorIndex
   ) {
      int color = -1;
      if (colorIndex == -1) {
         int entityTeamColour = this.getTeamColour(e);
         if (entityTeamColour != -1) {
            color = 0xFF000000 | entityTeamColour;
         } else {
            EntityRadarCategory fallbackCategory = category;

            while (colorIndex == -1) {
               fallbackCategory = fallbackCategory.getSuperCategory();
               if (fallbackCategory == null) {
                  colorIndex = 15;
               } else {
                  colorIndex = fallbackCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue();
               }
            }
         }
      }

      if (colorIndex != -1) {
         color = ModSettings.COLORS[colorIndex];
      }

      float brightness = this.getEntityBrightness(offh, heightLimit, startFadingAt, heightBasedFade);
      if (brightness < 1.0F) {
         int r = color >> 16 & 0xFF;
         int g = color >> 8 & 0xFF;
         int b = color & 0xFF;
         int a = 255;
         if (cave) {
            a = (int)((float)a * brightness);
         } else {
            r = (int)((float)r * brightness);
            g = (int)((float)g * brightness);
            b = (int)((float)b * brightness);
         }

         color = a << 24 | r << 16 | g << 8 | b;
      }

      return color;
   }

   public float getEntityBrightness(float offh, int heightLimit, int startFadingAt, boolean heightBasedFade) {
      float level = (float)heightLimit - offh;
      if (level < 0.0F) {
         level = 0.0F;
      }

      float brightness = 1.0F;
      int threshold = startFadingAt == 0 ? heightLimit * 3 / 4 : heightLimit - startFadingAt;
      if (level <= (float)threshold && heightBasedFade) {
         brightness = 0.25F + 0.5F * level / (float)threshold;
      }

      return brightness;
   }

   public void setLastRenderViewEntity(class_1297 lastRenderViewEntity) {
      this.lastRenderViewEntity = lastRenderViewEntity;
   }

   public EntityRadarCategoryManager getEntityCategoryManager() {
      return this.entityCategoryManager;
   }

   public Iterator<MinimapRadarList> getRadarListsIterator() {
      return this.radarLists.iterator();
   }

   public double getMaxDistance(MinimapProcessor minimap, boolean circle) {
      int cullingSize = minimap.getMinimapSize() / 2 + 48;
      if (!circle) {
         cullingSize = (int)((double)cullingSize * Math.sqrt(2.0));
      }

      return (double)(cullingSize * cullingSize) / (minimap.getMinimapZoom() * minimap.getMinimapZoom());
   }
}
