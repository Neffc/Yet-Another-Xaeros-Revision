package xaero.common.minimap.radar.category.rule;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_7923;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.minimap.radar.category.EntityRadarCategoryConstants;

public class EntityRadarListRuleTypes {
   public static final List<ObjectCategoryListRuleType<class_1297, class_1657, ?>> TYPE_LIST = EntityRadarCategoryConstants.LIST_FACTORY.get();
   public static final Map<String, ObjectCategoryListRuleType<class_1297, class_1657, ?>> TYPE_MAP = EntityRadarCategoryConstants.MAP_FACTORY.get();
   public static final ObjectCategoryListRuleType<class_1297, class_1657, class_1299<?>> ENTITY_TYPE = new ObjectCategoryListRuleType<>(
      "entity",
      (e, p) -> e.method_5864(),
      () -> class_7923.field_41177,
      EntityRadarCategoryConstants.getDefaultElementResolver(
         class_7923.field_41177, s -> (class_1299<?>)class_1299.method_5898(s).orElse(null), class_1299::method_5890
      ),
      EntityRadarCategoryConstants.DEFAULT_LIST_SERIALIZER,
      EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR_FIXER,
      EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR,
      TYPE_LIST,
      TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, class_1792> ITEM_TYPE = new ObjectCategoryListRuleType<>(
      "item",
      (e, p) -> e instanceof class_1542 ? ((class_1542)e).method_6983().method_7909() : null,
      () -> class_7923.field_41178,
      EntityRadarCategoryConstants.getDefaultElementResolver(
         class_7923.field_41178, s -> (class_1792)class_7923.field_41178.method_17966(new class_2960(s)).orElse(null), class_7923.field_41178::method_10221
      ),
      item -> class_7923.field_41178.method_10221(item).toString(),
      EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR_FIXER,
      EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR,
      TYPE_LIST,
      TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, String> PLAYER_NAME = new ObjectCategoryListRuleType<>(
      "player",
      (e, p) -> e instanceof class_1657 ? ((class_1657)e).method_7334().getName() : null,
      () -> (Iterable<String>)(class_310.method_1551().method_1562() == null
            ? new ArrayList<>()
            : class_310.method_1551().method_1562().method_2880().stream().map(pi -> pi.method_2966().getName())::iterator),
      s -> Lists.newArrayList(new String[]{s}),
      Function.identity(),
      EntityRadarCategoryConstants.PLAYER_NAME_VALIDATOR_FIXER,
      EntityRadarCategoryConstants.PLAYER_NAME_VALIDATOR,
      TYPE_LIST,
      TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> LIVING = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_LIVING, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> HOSTILE = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_HOSTILE, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> TAMED = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_TAMED, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> SAME_TEAM = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_SAME_TEAM, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> BABY = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_BABY, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> VANILLA = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_VANILLA, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> ABOVE_GROUND = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_ABOVE_GROUND, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> MY_GROUND = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_MY_GROUND, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> LIT = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_LIT, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> CUSTOM_NAME = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.HAS_CUSTOM_NAME, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> IN_TEAM = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_IN_TEAM, TYPE_LIST, TYPE_MAP
   );
   public static final ObjectCategoryListRuleType<class_1297, class_1657, Boolean> TRACKED = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(
      EntityRadarCategoryHardRules.IS_TRACKED, TYPE_LIST, TYPE_MAP
   );
}
