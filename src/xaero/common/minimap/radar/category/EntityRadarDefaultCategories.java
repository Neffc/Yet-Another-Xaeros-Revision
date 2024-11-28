package xaero.common.minimap.radar.category;

import net.minecraft.class_1299;
import xaero.common.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.common.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.settings.ModSettings;

public final class EntityRadarDefaultCategories {
   public EntityRadarCategory setupDefault(ModSettings settings) {
      EntityRadarBackwardsCompatibilityConfig compatibilityConfig = settings.getEntityRadarBackwardsCompatibilityConfig();
      EntityRadarCategory.Builder builder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_root")
         .setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING)
         .setProtection(true);
      if (!settings.foundOldRadarSettings() || !compatibilityConfig.itemFramesOnRadar) {
         builder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).getList().add(class_1299.method_5890(class_1299.field_6043).toString());
         builder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).getList().add(class_1299.method_5890(class_1299.field_28401).toString());
      }

      if (settings.foundOldRadarSettings()) {
         builder.setSettingValue(EntityRadarCategorySettings.ENTITY_NUMBER, Double.valueOf((double)compatibilityConfig.entityAmount * 100.0));
         builder.setSettingValue(EntityRadarCategorySettings.DOT_SIZE, Double.valueOf((double)compatibilityConfig.dotsSize));
         builder.setSettingValue(EntityRadarCategorySettings.ICON_SCALE, Double.valueOf(compatibilityConfig.headsScale));
         builder.setSettingValue(EntityRadarCategorySettings.HEIGHT_FADE, Boolean.valueOf(compatibilityConfig.showEntityHeight));
         builder.setSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT, Double.valueOf((double)compatibilityConfig.heightLimit));
         builder.setSettingValue(EntityRadarCategorySettings.ALWAYS_NAMETAGS, Boolean.valueOf(compatibilityConfig.alwaysEntityNametags));
         builder.setSettingValue(EntityRadarCategorySettings.ICON_NAME_FALLBACK, Boolean.valueOf(compatibilityConfig.displayNameWhenIconFails));
      }

      EntityRadarCategory.Builder livingBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_living")
         .setBaseRule(EntityRadarCategoryHardRules.IS_LIVING)
         .setProtection(true);
      livingBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(2.0));
      livingBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(14.0));
      livingBuilder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).getList().add(class_1299.method_5890(class_1299.field_6131).toString());
      EntityRadarCategory.Builder hostileBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_hostile")
         .setBaseRule(EntityRadarCategoryHardRules.IS_HOSTILE)
         .setProtection(true);
      hostileBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(3.0));
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showHostile) {
            hostileBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         if (compatibilityConfig.hostileColor != 14) {
            hostileBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.hostileColor));
         }

         if (compatibilityConfig.hostileIcons != 1) {
            hostileBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf((double)compatibilityConfig.hostileIcons));
         }

         if (compatibilityConfig.hostileMobNames != 0) {
            hostileBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.hostileMobNames));
         }
      }

      EntityRadarCategory.Builder friendlyBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_friendly")
         .setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING)
         .setProtection(true);
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showMobs) {
            friendlyBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         if (compatibilityConfig.mobsColor != 14) {
            friendlyBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.mobsColor));
         }

         if (compatibilityConfig.mobIcons != 1) {
            friendlyBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf((double)compatibilityConfig.mobIcons));
         }

         if (compatibilityConfig.friendlyMobNames != 0) {
            friendlyBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.friendlyMobNames));
         }
      }

      EntityRadarCategory.Builder playersBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_players")
         .setBaseRule(EntityRadarCategoryHardRules.IS_PLAYER)
         .setProtection(true);
      playersBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(6.0));
      playersBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(15.0));
      playersBuilder.setSettingValue(
         EntityRadarCategorySettings.HEIGHT_LIMIT,
         EntityRadarCategorySettings.HEIGHT_LIMIT.getIndexReader().apply(EntityRadarCategorySettings.HEIGHT_LIMIT.getUiLastOption())
      );
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showPlayers) {
            playersBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         if (compatibilityConfig.playersColor != 14) {
            playersBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.playersColor));
         }

         if (compatibilityConfig.playerIcons != 1) {
            playersBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf((double)compatibilityConfig.playerIcons));
         }

         if (compatibilityConfig.playerNames != 0) {
            playersBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.playerNames));
         }
      }

      EntityRadarCategory.Builder friendsBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_friend")
         .setBaseRule(EntityRadarCategoryHardRules.IS_NOTHING)
         .setProtection(true);
      EntityRadarCategory.Builder playersTrackedBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_tracked")
         .setBaseRule(EntityRadarCategoryHardRules.IS_TRACKED)
         .setProtection(true);
      playersTrackedBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(2.0));
      EntityRadarCategory.Builder playersTeamBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_same_team")
         .setBaseRule(EntityRadarCategoryHardRules.IS_SAME_TEAM)
         .setProtection(true);
      EntityRadarCategory.Builder playersOtherTeamsBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_other_teams")
         .setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING)
         .setProtection(true);
      playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(7.0));
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showOtherTeam) {
            playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         if (compatibilityConfig.otherTeamColor != -1) {
            playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.otherTeamColor));
         }

         if (compatibilityConfig.otherTeamsNames != 3) {
            playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.otherTeamsNames));
         }
      }

      EntityRadarCategory.Builder tamedHostileBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_hostile_tamed")
         .setBaseRule(EntityRadarCategoryHardRules.IS_TAMED)
         .setProtection(true);
      tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(5.0));
      EntityRadarCategory.Builder tamedFriendlyBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_friendly_tamed")
         .setBaseRule(EntityRadarCategoryHardRules.IS_TAMED)
         .setProtection(true);
      tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(4.0));
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showTamed) {
            tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
            tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         if (compatibilityConfig.tamedMobsColor != -1) {
            tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.tamedMobsColor));
            tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.tamedMobsColor));
         }

         if (compatibilityConfig.tamedIcons != 3) {
            tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf((double)compatibilityConfig.tamedIcons));
            tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf((double)compatibilityConfig.tamedIcons));
         }

         if (compatibilityConfig.tamedMobNames != 3) {
            tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.tamedMobNames));
            tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.tamedMobNames));
         }
      }

      EntityRadarCategory.Builder itemsBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_items")
         .setBaseRule(EntityRadarCategoryHardRules.IS_ITEM)
         .setProtection(true);
      itemsBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(1.0));
      itemsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(12.0));
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showItems) {
            itemsBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         itemsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.itemsColor));
         if (compatibilityConfig.itemNames != 0) {
            itemsBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.itemNames));
         }
      }

      EntityRadarCategory.Builder otherBuilder = EntityRadarCategory.Builder.getDefault()
         .setName("gui.xaero_entity_category_other_entities")
         .setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING)
         .setProtection(true);
      otherBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(5.0));
      if (settings.foundOldRadarSettings()) {
         if (!compatibilityConfig.showOther) {
            otherBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, Boolean.valueOf(false));
         }

         otherBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf((double)compatibilityConfig.otherColor));
         if (compatibilityConfig.otherNames != 0) {
            otherBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf((double)compatibilityConfig.otherNames));
         }
      }

      builder.addSubCategoryBuilder(livingBuilder);
      builder.addSubCategoryBuilder(itemsBuilder);
      builder.addSubCategoryBuilder(otherBuilder);
      livingBuilder.addSubCategoryBuilder(playersBuilder);
      livingBuilder.addSubCategoryBuilder(hostileBuilder);
      livingBuilder.addSubCategoryBuilder(friendlyBuilder);
      hostileBuilder.addSubCategoryBuilder(tamedHostileBuilder);
      friendlyBuilder.addSubCategoryBuilder(tamedFriendlyBuilder);
      playersBuilder.addSubCategoryBuilder(friendsBuilder);
      playersBuilder.addSubCategoryBuilder(playersTrackedBuilder);
      playersBuilder.addSubCategoryBuilder(playersTeamBuilder);
      playersBuilder.addSubCategoryBuilder(playersOtherTeamsBuilder);
      return builder.build();
   }

   public static final class Builder {
      public EntityRadarDefaultCategories build() {
         return new EntityRadarDefaultCategories();
      }

      public static EntityRadarDefaultCategories.Builder getDefault() {
         return new EntityRadarDefaultCategories.Builder();
      }
   }
}
