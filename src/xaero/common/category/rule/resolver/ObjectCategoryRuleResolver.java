package xaero.common.category.rule.resolver;

import java.util.Iterator;
import java.util.List;
import xaero.common.category.FilterObjectCategory;
import xaero.common.category.rule.ExcludeListMode;
import xaero.common.category.rule.ObjectCategoryExcludeList;
import xaero.common.category.rule.ObjectCategoryIncludeList;

public final class ObjectCategoryRuleResolver {
   private ObjectCategoryRuleResolver() {
   }

   public <E, P, C extends FilterObjectCategory<E, P, ?, C>> C resolve(C category, E element, P context) {
      if (this.followsRules(category, element, context)) {
         Iterator<C> subCategoryIterator = category.getDirectSubCategoryIterator();

         while (subCategoryIterator.hasNext()) {
            C subCategory = (C)subCategoryIterator.next();
            C subResolve = this.resolve(subCategory, element, context);
            if (subResolve != null) {
               return subResolve;
            }
         }

         return category;
      } else {
         return null;
      }
   }

   private <E, P, C extends FilterObjectCategory<E, P, ?, C>> boolean followsRules(C category, E element, P context) {
      boolean result = category.getBaseRule().isFollowedBy(element, context);
      if (!result) {
         List<ObjectCategoryIncludeList<E, P, ?>> includeLists = category.getIncludeLists();

         for (int i = 0; i < includeLists.size(); i++) {
            ObjectCategoryIncludeList<E, P, ?> includeList = includeLists.get(i);
            if (includeList.isFollowedBy(element, context)) {
               result = true;
               break;
            }
         }
      }

      if (result) {
         List<ObjectCategoryExcludeList<E, P, ?>> excludeLists = category.getExcludeLists();
         if (category.getExcludeMode() == ExcludeListMode.ALL_BUT) {
            result = false;
         }

         for (int ix = 0; ix < excludeLists.size(); ix++) {
            ObjectCategoryExcludeList<E, P, ?> excludeList = excludeLists.get(ix);
            if (result != excludeList.isFollowedBy(element, context)) {
               result = !result;
               break;
            }
         }
      }

      return result;
   }

   public static final class Builder {
      private Builder() {
      }

      public ObjectCategoryRuleResolver.Builder setDefault() {
         return this;
      }

      public ObjectCategoryRuleResolver build() {
         return new ObjectCategoryRuleResolver();
      }

      public static ObjectCategoryRuleResolver.Builder getDefault() {
         return new ObjectCategoryRuleResolver.Builder().setDefault();
      }
   }
}
