package xaero.common.category.rule;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import xaero.common.category.FilterObjectCategory;
import xaero.common.misc.ListFactory;

public final class ObjectCategoryExcludeList<E, P, S> extends ObjectCategoryListRule<E, P, S> {
   private ExcludeListMode excludeMode;

   private ObjectCategoryExcludeList(
      @Nonnull ObjectCategoryListRuleType<E, P, S> type, @Nonnull List<String> stringList, @Nonnull Set<S> set, @Nonnull ExcludeListMode excludeMode
   ) {
      super(type, "exclude list", stringList, set);
      this.excludeMode = excludeMode;
   }

   @Override
   public boolean isFollowedBy(E object, P context) {
      boolean inList = this.inList(object, context);
      return this.excludeMode == ExcludeListMode.ALL_BUT && inList || this.excludeMode == ExcludeListMode.ONLY && !inList;
   }

   public ExcludeListMode getExcludeMode() {
      return this.excludeMode;
   }

   public static final class Builder<E, P, S> extends ObjectCategoryListRule.Builder<E, P, S, ObjectCategoryExcludeList.Builder<E, P, S>> {
      private ExcludeListMode excludeMode;

      public Builder(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
         super(listFactory, type);
      }

      public ObjectCategoryExcludeList.Builder<E, P, S> setDefault() {
         super.setDefault();
         this.setExcludeMode(ExcludeListMode.ONLY);
         return this.self;
      }

      public ObjectCategoryExcludeList.Builder<E, P, S> setExcludeMode(ExcludeListMode excludeMode) {
         this.excludeMode = excludeMode;
         return this.self;
      }

      protected <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryExcludeList<E, P, S> build(
         List<C> subCategories,
         Function<C, ObjectCategoryListRule<E, P, S>> subListGetter,
         Function<C, ObjectCategoryListRule<E, P, S>> subListExceptionsGetter
      ) {
         return (ObjectCategoryExcludeList<E, P, S>)super.build(subCategories, subListGetter, subListExceptionsGetter);
      }

      public <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryExcludeList<E, P, S> build(List<C> subCategories) {
         return this.build(subCategories, null, null);
      }

      protected ObjectCategoryExcludeList<E, P, S> buildInternally(Set<S> effectiveSet) {
         return new ObjectCategoryExcludeList<>(this.type, this.stringList, effectiveSet, this.excludeMode);
      }

      public static <E, P, S> ObjectCategoryExcludeList.Builder<E, P, S> getDefault(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
         return new ObjectCategoryExcludeList.Builder<>(listFactory, type).setDefault();
      }
   }
}
