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
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

public final class RadarRenderProvider extends MinimapElementRenderProvider<class_1297, RadarRenderContext> {
   private boolean used;
   private Iterator<MinimapRadarList> entityLists;
   private MinimapRadarList currentList;
   private MinimapRadarList listForContext;
   private int currentListIndex;

   public void begin(int location, RadarRenderContext context) {
      this.begin(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public boolean hasNext(int location, RadarRenderContext context) {
      return this.hasNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public class_1297 setupContextAndGetNext(int location, RadarRenderContext context) {
      return this.setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public class_1297 getNext(int location, RadarRenderContext context) {
      return this.getNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public void end(int location, RadarRenderContext context) {
      this.end(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public void begin(MinimapElementRenderLocation location, RadarRenderContext context) {
      this.used = true;
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      MinimapProcessor minimap = minimapSession.getMinimapProcessor();
      context.minimapRadar = minimap.getEntityRadar();
      context.reversedOrder = ModSettings.keyReverseEntityRadar.method_1434();
      context.renderEntity = class_310.method_1551().method_1560();
      class_437 screenBU = class_310.method_1551().field_1755;
      class_310.method_1551().field_1755 = null;
      context.playerListDown = class_310.method_1551().field_1690.field_1907.method_1434() || ModSettings.keyAlternativeListPlayers.method_1434();
      class_310.method_1551().field_1755 = screenBU;
      this.entityLists = context.minimapRadar.getRadarListsIterator();
      this.currentList = null;
      this.listForContext = null;
      this.currentListIndex = 0;
   }

   private void ensureList(MinimapElementRenderLocation location, RadarRenderContext context) {
      while (this.currentList == null || this.currentListIndex >= this.currentList.getEntities().size() || this.currentListIndex < 0) {
         while (this.entityLists.hasNext()) {
            this.currentList = this.entityLists.next();
            this.currentListIndex = context.reversedOrder ? this.currentList.getEntities().size() - 1 : 0;
            if (this.currentList == null
               || location != MinimapElementRenderLocation.IN_MINIMAP && location != MinimapElementRenderLocation.OVER_MINIMAP
               || location == MinimapElementRenderLocation.IN_MINIMAP != this.shouldRenderOverMinimap(this.currentList.getCategory(), context)) {
            }
         }

         this.currentList = null;
         this.currentListIndex = 0;
         break;
      }
   }

   private boolean shouldRenderOverMinimap(EntityRadarCategory category, RadarRenderContext context) {
      int settingValue = this.currentList.getCategory().getSettingValue(EntityRadarCategorySettings.RENDER_OVER_MINIMAP).intValue();
      return settingValue == 2 || settingValue == 1 && context.playerListDown;
   }

   public boolean hasNext(MinimapElementRenderLocation location, RadarRenderContext context) {
      this.ensureList(location, context);
      return this.currentList != null
         && (!context.reversedOrder && this.currentListIndex < this.currentList.getEntities().size() || context.reversedOrder && this.currentListIndex >= 0);
   }

   public class_1297 setupContextAndGetNext(MinimapElementRenderLocation location, RadarRenderContext context) {
      if (this.listForContext != this.currentList) {
         this.setupContextForCategory(this.currentList.getCategory(), context);
         this.listForContext = this.currentList;
      }

      class_1297 result = this.getNext(location, context);
      if (result == null) {
         return null;
      } else {
         this.setupContextForEntity(result, context);
         return result;
      }
   }

   public void setupContextForCategory(EntityRadarCategory entityCategory, RadarRenderContext context) {
      context.entityCategory = entityCategory;
      context.iconScale = entityCategory.getSettingValue(EntityRadarCategorySettings.ICON_SCALE);
      context.dotSize = entityCategory.getSettingValue(EntityRadarCategorySettings.DOT_SIZE).intValue();
      context.dotScale = 1.0 + 0.5 * (double)(context.dotSize - 1);
      context.heightLimit = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT).intValue();
      context.heightBasedFade = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_FADE);
      context.startFadingAt = entityCategory.getSettingValue(EntityRadarCategorySettings.START_FADING_AT).intValue();
      context.displayNameWhenIconFails = entityCategory.getSettingValue(EntityRadarCategorySettings.ICON_NAME_FALLBACK);
      context.alwaysNameTags = entityCategory.getSettingValue(EntityRadarCategorySettings.ALWAYS_NAMETAGS);
      context.colorIndex = entityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue();
      context.displayY = entityCategory.getSettingValue(EntityRadarCategorySettings.DISPLAY_Y).intValue();
      int icons = entityCategory.getSettingValue(EntityRadarCategorySettings.ICONS).intValue();
      context.nameSettingForList = entityCategory.getSettingValue(EntityRadarCategorySettings.NAMES).intValue();
      context.namesForList = context.nameSettingForList == 1 && context.playerListDown || context.nameSettingForList == 2;
      context.iconsForList = icons == 1 && context.playerListDown || icons == 2;
   }

   public void setupContextForEntity(class_1297 entity, RadarRenderContext context) {
      boolean name = context.namesForList;
      boolean icon = context.iconsForList;
      if (!name && !(entity instanceof class_1657)) {
         name = context.alwaysNameTags && entity.method_16914();
      }

      context.name = name;
      context.icon = icon;
   }

   public class_1297 getNext(MinimapElementRenderLocation location, RadarRenderContext context) {
      class_1297 result = null;
      result = this.currentList.getEntities().get(this.currentListIndex);
      this.currentListIndex = this.currentListIndex + (context.reversedOrder ? -1 : 1);
      return context.renderEntity == result ? null : result;
   }

   public void end(MinimapElementRenderLocation location, RadarRenderContext context) {
      this.used = false;
      context.minimapRadar = null;
      context.renderEntity = null;
   }

   public boolean isUsed() {
      return this.used;
   }
}
