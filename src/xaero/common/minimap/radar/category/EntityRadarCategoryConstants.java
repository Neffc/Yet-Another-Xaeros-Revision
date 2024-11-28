package xaero.common.minimap.radar.category;

import com.google.common.collect.Lists;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.PatternSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1657;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import xaero.common.category.rule.ObjectCategoryHardRule;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;

public final class EntityRadarCategoryConstants {
   public static final ListFactory LIST_FACTORY = ArrayList::new;
   public static final MapFactory MAP_FACTORY = HashMap::new;
   public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("xaerominimap_entities.json");
   public static final Path DEFAULT_CONFIG_PATH = CONFIG_PATH.getParent().resolveSibling("defaultconfigs").resolve(CONFIG_PATH.toFile().getName());
   public static final Supplier<EntityRadarCategoryData.Builder> DATA_BUILDER_FACTORY = EntityRadarCategoryData.Builder::getDefault;
   public static final Supplier<EntityRadarCategory.Builder> CATEGORY_BUILDER_FACTORY = EntityRadarCategory.Builder::getDefault;
   public static final Function<class_1299<?>, String> DEFAULT_LIST_SERIALIZER = t -> class_1299.method_5890(t).toString();
   private static final Function<String, String> WILDCARD_TO_REGEX = s -> s.replaceAll("([\\.\\-\\:\\/])", "\\\\$1").replace("*", ".*");
   public static final Predicate<String> DEFAULT_LIST_WILDCARD_VALIDATOR = s -> {
      try {
         "test string".matches(WILDCARD_TO_REGEX.apply(s));
         return true;
      } catch (PatternSyntaxException var2) {
         return false;
      }
   };
   private static final String DEFAULT_LIST_ALLOWED_CHARS = "a-z_0-9\\(\\)\\|:\\/\\-\\.\\*";
   public static final Predicate<String> DEFAULT_LIST_STRING_VALIDATOR = s -> {
      boolean result = s.matches("[a-z_0-9\\(\\)\\|:\\/\\-\\.\\*]*");
      if (result) {
         return class_2960.method_20207(s) ? true : DEFAULT_LIST_WILDCARD_VALIDATOR.test(s);
      } else {
         return false;
      }
   };
   public static final Function<String, String> DEFAULT_LIST_STRING_VALIDATOR_FIXER = s -> s.replaceAll("[^a-z_0-9\\(\\)\\|:\\/\\-\\.\\*]+", "");
   private static final String PLAYER_NAME_LIST_ALLOWED_CHARS = "A-Za-z_0-9\\_";
   public static final Predicate<String> PLAYER_NAME_VALIDATOR = s -> s.matches("[A-Za-z_0-9\\_]+");
   public static final Function<String, String> PLAYER_NAME_VALIDATOR_FIXER = s -> s.replaceAll("[^A-Za-z_0-9\\_]+", "");
   public static final Supplier<Iterable<Boolean>> PREDICATE_VALUE_ALL_SUPPLIER = () -> Lists.newArrayList(new Boolean[]{false, true});
   public static final Function<String, List<Boolean>> PREDICATE_VALUE_RESOLVER = s -> Lists.newArrayList(new Boolean[]{s.equals("yes")});
   public static final Function<Boolean, String> PREDICATE_VALUE_SERIALIZER = b -> b ? "yes" : "no";
   public static final Predicate<String> PREDICATE_VALUE_VALIDATOR = s -> s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no");
   public static final Function<String, String> PREDICATE_VALUE_VALIDATOR_FIXER = s -> s;
   public static final String CATEGORY_ROOT = "gui.xaero_entity_category_root";
   public static final String CATEGORY_LIVING = "gui.xaero_entity_category_living";
   public static final String CATEGORY_HOSTILE = "gui.xaero_entity_category_hostile";
   public static final String CATEGORY_FRIENDLY = "gui.xaero_entity_category_friendly";
   public static final String CATEGORY_HOSTILE_TAMED = "gui.xaero_entity_category_hostile_tamed";
   public static final String CATEGORY_FRIENDLY_TAMED = "gui.xaero_entity_category_friendly_tamed";
   public static final String CATEGORY_PLAYERS = "gui.xaero_entity_category_players";
   public static final String CATEGORY_FRIENDS = "gui.xaero_entity_category_friend";
   public static final String CATEGORY_TRACKED = "gui.xaero_entity_category_tracked";
   public static final String CATEGORY_SAME_TEAM = "gui.xaero_entity_category_same_team";
   public static final String CATEGORY_OTHER_TEAMS = "gui.xaero_entity_category_other_teams";
   public static final String CATEGORY_ITEMS = "gui.xaero_entity_category_items";
   public static final String CATEGORY_OTHER = "gui.xaero_entity_category_other_entities";
   public static final String HARD_NOTHING = "nothing";
   public static final String HARD_LIVING = "living";
   public static final String HARD_HOSTILE = "hostile";
   public static final String HARD_FRIENDLY = "friendly";
   public static final String HARD_TAMED = "tamed";
   public static final String HARD_PLAYERS = "players";
   public static final String HARD_SAME_TEAM = "same-team";
   public static final String HARD_OTHER_TEAMS = "other-teams";
   public static final String HARD_ITEMS = "items";
   public static final String HARD_ANYTHING = "anything";
   public static final String HARD_BABY = "baby";
   public static final String HARD_VANILLA = "vanilla";
   public static final String HARD_MODDED = "modded";
   public static final String HARD_ABOVE_GROUND = "above-ground";
   public static final String HARD_BELOW_GROUND = "below-ground";
   public static final String HARD_LIT = "block-lit";
   public static final String HARD_UNLIT = "block-unlit";
   public static final String HARD_CUSTOM_NAME = "has-custom-name";
   public static final String HARD_NO_CUSTOM_NAME = "no-custom-name";
   public static final String HARD_TRACKED = "tracked";
   public static final String HARD_IN_TEAM = "in-a-team";
   public static final String HARD_TEAMLESS = "teamless";

   public static <E> Function<String, List<E>> getDefaultElementResolver(
      class_2378<E> registry, Function<String, E> keyToElement, Function<E, class_2960> elementToKey
   ) {
      return s -> {
         if (class_2960.method_20207(s)) {
            E directReference = keyToElement.apply(s);
            return directReference == null ? null : Lists.newArrayList(new Object[]{directReference});
         } else {
            String regexPattern = WILDCARD_TO_REGEX.apply(s);
            List<E> result = new ArrayList<>();

            try {
               for (E et : registry) {
                  class_2960 entityTypeLocation = elementToKey.apply(et);
                  if (entityTypeLocation != null && entityTypeLocation.toString().matches(regexPattern)) {
                     result.add(et);
                  }
               }

               return result;
            } catch (PatternSyntaxException var9) {
               return null;
            }
         }
      };
   }

   public static ObjectCategoryListRuleType<class_1297, class_1657, Boolean> createHardRuleBasedPredicateListRuleType(
      ObjectCategoryHardRule<class_1297, class_1657> hardRule,
      List<ObjectCategoryListRuleType<class_1297, class_1657, ?>> typeList,
      Map<String, ObjectCategoryListRuleType<class_1297, class_1657, ?>> typeMap
   ) {
      return new ObjectCategoryListRuleType<>(
         hardRule.getName(),
         (e, p) -> hardRule.isFollowedBy(e, p),
         PREDICATE_VALUE_ALL_SUPPLIER,
         PREDICATE_VALUE_RESOLVER,
         PREDICATE_VALUE_SERIALIZER,
         PREDICATE_VALUE_VALIDATOR_FIXER,
         PREDICATE_VALUE_VALIDATOR,
         typeList,
         typeMap
      );
   }
}
