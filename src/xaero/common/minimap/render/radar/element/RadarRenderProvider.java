package xaero.common.minimap.render.radar.element;

import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.minimap.radar.MinimapRadarList;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.settings.ModSettings;

public final class RadarRenderProvider extends MinimapElementRenderProvider<class_1297, RadarRenderContext> {
   private double maxDistanceSquared;
   private Iterator<MinimapRadarList> entityLists;
   private MinimapRadarList currentList;
   private MinimapRadarList listForContext;
   private int currentListIndex;
   private boolean playerListDown;

   public void begin(int location, RadarRenderContext context) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      MinimapProcessor minimap = minimapSession.getMinimapProcessor();
      context.minimapRadar = minimap.getEntityRadar();
      context.reversedOrder = ModSettings.keyReverseEntityRadar.method_1434();
      class_437 screenBU = class_310.method_1551().field_1755;
      class_310.method_1551().field_1755 = null;
      this.playerListDown = class_310.method_1551().field_1690.field_1907.method_1434() || ModSettings.keyAlternativeListPlayers.method_1434();
      class_310.method_1551().field_1755 = screenBU;
      double playerDimDiv = minimapSession.getModMain().getInterfaces().getMinimapInterface().getMinimapFBORenderer().getLastPlayerDimDiv();
      this.maxDistanceSquared = context.minimapRadar.getMaxDistance(minimap, minimapSession.getModMain().getSettings().minimapShape == 1)
         * playerDimDiv
         * playerDimDiv;
      this.entityLists = context.minimapRadar.getRadarListsIterator();
      this.currentList = null;
      this.listForContext = null;
      this.currentListIndex = 0;
   }

   private void ensureList(int location, RadarRenderContext context) {
      while (this.currentList == null || this.currentListIndex >= this.currentList.getEntities().size() || this.currentListIndex < 0) {
         while (this.entityLists.hasNext()) {
            this.currentList = this.entityLists.next();
            this.currentListIndex = context.reversedOrder ? this.currentList.getEntities().size() - 1 : 0;
            if (this.currentList == null || location != 0 && location != 1 || location == 0 != this.shouldRenderOverMinimap(this.currentList.getCategory())) {
            }
         }

         this.currentList = null;
         this.currentListIndex = 0;
         break;
      }
   }

   private boolean shouldRenderOverMinimap(EntityRadarCategory category) {
      int settingValue = this.currentList.getCategory().getSettingValue(EntityRadarCategorySettings.RENDER_OVER_MINIMAP).intValue();
      return settingValue == 2 || settingValue == 1 && this.playerListDown;
   }

   public boolean hasNext(int location, RadarRenderContext context) {
      this.ensureList(location, context);
      return this.currentList != null
         && (!context.reversedOrder && this.currentListIndex < this.currentList.getEntities().size() || context.reversedOrder && this.currentListIndex >= 0);
   }

   public class_1297 setupContextAndGetNext(int location, RadarRenderContext context) {
      if (this.listForContext != this.currentList) {
         EntityRadarCategory entityCategory = this.currentList.getCategory();
         context.entityCategory = entityCategory;
         context.iconScale = entityCategory.getSettingValue(EntityRadarCategorySettings.ICON_SCALE);
         context.dotSize = entityCategory.getSettingValue(EntityRadarCategorySettings.DOT_SIZE).intValue();
         context.heightLimit = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT).intValue();
         context.heightBasedFade = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_FADE);
         context.startFadingAt = entityCategory.getSettingValue(EntityRadarCategorySettings.START_FADING_AT).intValue();
         context.displayNameWhenIconFails = entityCategory.getSettingValue(EntityRadarCategorySettings.ICON_NAME_FALLBACK);
         context.alwaysNameTags = entityCategory.getSettingValue(EntityRadarCategorySettings.ALWAYS_NAMETAGS);
         context.colorIndex = entityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue();
         context.displayY = entityCategory.getSettingValue(EntityRadarCategorySettings.DISPLAY_Y).intValue();
         int icons = entityCategory.getSettingValue(EntityRadarCategorySettings.ICONS).intValue();
         int names = entityCategory.getSettingValue(EntityRadarCategorySettings.NAMES).intValue();
         context.namesForList = names == 1 && this.playerListDown || names == 2;
         context.iconsForList = icons == 1 && this.playerListDown || icons == 2;
         this.listForContext = this.currentList;
      }

      class_1297 result = this.getNext(location, context);
      if (result == null) {
         return null;
      } else {
         if (location == 0) {
            double offx = result.method_23317() - context.renderEntity.method_23317();
            double offx2 = offx * offx;
            if (offx2 > this.maxDistanceSquared) {
               return null;
            }

            double offy = result.method_23321() - context.renderEntity.method_23321();
            double offy2 = offy * offy;
            if (offy2 > this.maxDistanceSquared) {
               return null;
            }
         }

         boolean name = context.namesForList;
         boolean icon = context.iconsForList;
         if (!name && !(result instanceof class_1657)) {
            name = context.alwaysNameTags && result.method_16914();
         }

         context.name = name;
         context.icon = icon;
         return result;
      }
   }

   public class_1297 getNext(int location, RadarRenderContext context) {
      class_1297 result = null;
      result = this.currentList.getEntities().get(this.currentListIndex);
      this.currentListIndex = this.currentListIndex + (context.reversedOrder ? -1 : 1);
      return context.renderEntity == result ? null : result;
   }

   public void end(int location, RadarRenderContext context) {
      context.minimapRadar = null;
   }
}
