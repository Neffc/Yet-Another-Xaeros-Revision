package xaero.common.category.serialization;

import java.util.function.Function;
import java.util.function.Supplier;
import xaero.common.category.FilterObjectCategory;
import xaero.common.category.rule.ObjectCategoryExcludeList;
import xaero.common.category.rule.ObjectCategoryHardRule;
import xaero.common.category.rule.ObjectCategoryIncludeList;
import xaero.common.category.rule.ObjectCategoryListRule;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.category.rule.ObjectCategoryRule;
import xaero.common.category.serialization.data.FilterObjectCategoryData;
import xaero.common.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.common.category.setting.ObjectCategorySetting;

public abstract class FilterObjectCategorySerializationHandler<E, P, D extends FilterObjectCategoryData<D>, C extends FilterObjectCategory<E, P, D, C>, B extends FilterObjectCategory.Builder<E, P, C, B>, DB extends FilterObjectCategoryData.Builder<D, DB>>
   extends ObjectCategorySerializationHandler<D, C, B, DB> {
   private final Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter;
   private final ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
   private final Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
   private final String listRuleTypePrefixSeparator;

   protected FilterObjectCategorySerializationHandler(
      ObjectCategoryDataSerializer<D> serializer,
      Supplier<DB> dataBuilderFactory,
      Supplier<B> objectCategoryBuilderFactory,
      Function<String, ObjectCategorySetting<?>> settingTypeGetter,
      Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter,
      ObjectCategoryListRuleType<E, P, ?> defaultListRuleType,
      Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter,
      String listRuleTypePrefixSeparator
   ) {
      super(serializer, dataBuilderFactory, objectCategoryBuilderFactory, settingTypeGetter);
      this.hardRuleGetter = hardRuleGetter;
      this.defaultListRuleType = defaultListRuleType;
      this.listRuleTypeGetter = listRuleTypeGetter;
      this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
   }

   protected DB getConfiguredDataBuilderForCategory(C category) {
      DB dataBuilder = super.getConfiguredDataBuilderForCategory(category);
      ObjectCategoryRule<E, P> baseRule = category.getBaseRule();
      dataBuilder.setHardInclude(baseRule == null ? "nothing" : baseRule.getName());
      dataBuilder.setExcludeMode(category.getExcludeMode());
      dataBuilder.setIncludeListInSuperCategory(category.getIncludeInSuperCategory());

      for (ObjectCategoryIncludeList<E, P, ?> includeList : category.getIncludeLists()) {
         String prefix = includeList.getType() == this.defaultListRuleType ? "" : includeList.getType().getId() + this.listRuleTypePrefixSeparator;
         includeList.forEach(el -> dataBuilder.addToIncludeList(prefix + el));
      }

      for (ObjectCategoryExcludeList<E, P, ?> excludeList : category.getExcludeLists()) {
         String prefix = excludeList.getType() == this.defaultListRuleType ? "" : excludeList.getType().getId() + this.listRuleTypePrefixSeparator;
         excludeList.forEach(el -> dataBuilder.addToExcludeList(prefix + el));
      }

      return dataBuilder;
   }

   protected B getConfiguredCategoryBuilderForData(D data) {
      B objectCategoryBuilder = super.getConfiguredCategoryBuilderForData(data);
      String hardInclude = data.getHardInclude();
      ObjectCategoryHardRule<E, P> serializedHardRule = this.hardRuleGetter == null ? null : this.hardRuleGetter.apply(hardInclude);
      if (serializedHardRule != null) {
         objectCategoryBuilder.setBaseRule(serializedHardRule);
      }

      objectCategoryBuilder.setExcludeMode(data.getExcludeMode());
      objectCategoryBuilder.setIncludeInSuperCategory(data.getIncludeListInSuperCategory());
      data.getIncludeListIterator()
         .forEachRemaining(
            s -> handleListRuleSerializedElement(
                  s, objectCategoryBuilder::getIncludeListBuilder, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator
               )
         );
      data.getExcludeListIterator()
         .forEachRemaining(
            s -> handleListRuleSerializedElement(
                  s, objectCategoryBuilder::getExcludeListBuilder, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator
               )
         );
      return objectCategoryBuilder;
   }

   public static <E, P> void handleListRuleSerializedElement(
      String s,
      Function<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryListRule.Builder<E, P, ?, ?>> listBuilderGetter,
      ObjectCategoryListRuleType<E, P, ?> defaultListRuleType,
      Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter,
      String listRuleTypePrefixSeparator
   ) {
      ObjectCategoryListRuleType<E, P, ?> entryListRuleType = defaultListRuleType;
      if (s.contains(listRuleTypePrefixSeparator)) {
         ObjectCategoryListRuleType<E, P, ?> specifiedListRuleType = listRuleTypeGetter.apply(s.substring(0, s.indexOf(listRuleTypePrefixSeparator)));
         if (specifiedListRuleType != null) {
            entryListRuleType = specifiedListRuleType;
         }

         s = s.substring(s.indexOf(listRuleTypePrefixSeparator) + 1);
      }

      listBuilderGetter.apply(entryListRuleType).getList().add(s);
   }

   public abstract static class Builder<E, P, D extends FilterObjectCategoryData<D>, C extends FilterObjectCategory<E, P, D, C>, B extends FilterObjectCategory.Builder<E, P, C, B>, DB extends FilterObjectCategoryData.Builder<D, DB>, SH extends FilterObjectCategorySerializationHandler<E, P, D, C, B, DB>, SHB extends FilterObjectCategorySerializationHandler.Builder<E, P, D, C, B, DB, SH, SHB>>
      extends ObjectCategorySerializationHandler.Builder<D, C, B, DB, SH, SHB> {
      protected Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter;
      protected ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
      protected Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes;
      protected Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
      protected String listRuleTypePrefixSeparator;

      public Builder(ObjectCategoryDataSerializer.Builder<D> serializerBuilder) {
         super(serializerBuilder);
      }

      public SHB setDefault() {
         super.setDefault();
         this.setHardRuleGetter(null);
         this.setDefaultListRuleType(null);
         this.setListRuleTypes(null);
         this.setListRuleTypeGetter(null);
         this.setListRuleTypePrefixSeparator(";");
         return this.self;
      }

      public SHB setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
         this.defaultListRuleType = defaultListRuleType;
         return this.self;
      }

      public SHB setListRuleTypes(Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes) {
         this.listRuleTypes = listRuleTypes;
         return this.self;
      }

      public SHB setHardRuleGetter(Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter) {
         this.hardRuleGetter = hardRuleGetter;
         return this.self;
      }

      public SHB setListRuleTypeGetter(Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter) {
         this.listRuleTypeGetter = listRuleTypeGetter;
         return this.self;
      }

      public SHB setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
         this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
         return this.self;
      }

      public SH build() {
         if (this.hardRuleGetter != null && this.defaultListRuleType != null && this.listRuleTypes != null && this.listRuleTypeGetter != null) {
            return super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }
   }
}
