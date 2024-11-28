package xaero.common.category.rule;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import xaero.common.category.FilterObjectCategory;
import xaero.common.misc.ListFactory;

public final class ObjectCategoryIncludeList<E, P, S> extends ObjectCategoryListRule<E, P, S> {
   private ObjectCategoryIncludeList(@Nonnull ObjectCategoryListRuleType<E, P, S> type, @Nonnull List<String> stringList, @Nonnull Set<S> set) {
      super(type, "include list", stringList, set);
   }

   @Override
   public boolean isFollowedBy(E object, P context) {
      return this.inList(object, context);
   }

   public static final class Builder<E, P, S> extends ObjectCategoryListRule.Builder<E, P, S, ObjectCategoryIncludeList.Builder<E, P, S>> {
      public Builder(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
         super(listFactory, type);
      }

      public ObjectCategoryIncludeList.Builder<E, P, S> setDefault() {
         super.setDefault();
         return this;
      }

      public static <E, P, S> ObjectCategoryIncludeList.Builder<E, P, S> getDefault(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
         return new ObjectCategoryIncludeList.Builder<>(listFactory, type).setDefault();
      }

      protected <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryIncludeList<E, P, S> build(
         List<C> subCategories,
         Function<C, ObjectCategoryListRule<E, P, S>> subListGetter,
         Function<C, ObjectCategoryListRule<E, P, S>> subListExceptionsGetter
      ) {
         return (ObjectCategoryIncludeList<E, P, S>)super.build(subCategories, subListGetter, subListExceptionsGetter);
      }

      public <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryIncludeList<E, P, S> build(List<C> subCategories) {
         return this.build(subCategories, sub -> sub.getIncludeList(this.type), sub -> sub.getExcludeList(this.type));
      }

      protected ObjectCategoryIncludeList<E, P, S> buildInternally(Set<S> effectiveSet) {
         return new ObjectCategoryIncludeList<>(this.type, this.stringList, effectiveSet);
      }
   }
}
