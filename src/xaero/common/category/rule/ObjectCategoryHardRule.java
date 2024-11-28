package xaero.common.category.rule;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public final class ObjectCategoryHardRule<E, P> extends ObjectCategoryRule<E, P> {
   private final ObjectCategoryHardRule.Predicate<E, P> predicate;
   private final boolean reversed;

   private ObjectCategoryHardRule(@Nonnull String name, boolean reversed, @Nonnull ObjectCategoryHardRule.Predicate<E, P> predicate) {
      super(name);
      this.reversed = reversed;
      this.predicate = predicate;
   }

   @Override
   public boolean isFollowedBy(E object, P context) {
      return this.reversed ? !this.predicate.test(object, context) : this.predicate.test(object, context);
   }

   public static final class Builder<E, P> {
      private String name;
      private ObjectCategoryHardRule.Predicate<E, P> predicate;
      private boolean reversed;

      public ObjectCategoryHardRule.Builder<E, P> setDefault() {
         this.setName(null);
         this.setPredicate(null);
         this.setReversed(false);
         return this;
      }

      public ObjectCategoryHardRule.Builder<E, P> setName(String name) {
         this.name = name;
         return this;
      }

      public ObjectCategoryHardRule.Builder<E, P> setPredicate(ObjectCategoryHardRule.Predicate<E, P> predicate) {
         this.predicate = predicate;
         return this;
      }

      public ObjectCategoryHardRule.Builder<E, P> setReversed(boolean reversed) {
         this.reversed = reversed;
         return this;
      }

      public ObjectCategoryHardRule<E, P> build(Map<String, ObjectCategoryHardRule<E, P>> destinationMap, List<ObjectCategoryHardRule<E, P>> destinationList) {
         if (this.name != null && this.predicate != null) {
            ObjectCategoryHardRule<E, P> rule = new ObjectCategoryHardRule<>(this.name, this.reversed, this.predicate);
            destinationMap.put(rule.getName(), rule);
            destinationList.add(rule);
            return rule;
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }
   }

   public interface Predicate<E, P> {
      boolean test(E var1, P var2);
   }
}
