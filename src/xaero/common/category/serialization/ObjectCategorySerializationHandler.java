package xaero.common.category.serialization;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ObjectCategory;
import xaero.common.category.serialization.data.ObjectCategoryData;
import xaero.common.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.common.category.setting.ObjectCategorySetting;

public abstract class ObjectCategorySerializationHandler<D extends ObjectCategoryData<D>, C extends ObjectCategory<D, C>, B extends ObjectCategory.Builder<C, B>, DB extends ObjectCategoryData.Builder<D, DB>> {
   private final ObjectCategoryDataSerializer<D> serializer;
   private final Supplier<DB> dataBuilderFactory;
   private final Supplier<B> objectCategoryBuilderFactory;
   private final Function<String, ObjectCategorySetting<?>> settingTypeGetter;

   protected ObjectCategorySerializationHandler(
      @Nonnull ObjectCategoryDataSerializer<D> serializer,
      @Nonnull Supplier<DB> dataBuilderFactory,
      @Nonnull Supplier<B> objectCategoryBuilderFactory,
      @Nonnull Function<String, ObjectCategorySetting<?>> settingTypeGetter
   ) {
      this.serializer = serializer;
      this.dataBuilderFactory = dataBuilderFactory;
      this.objectCategoryBuilderFactory = objectCategoryBuilderFactory;
      this.settingTypeGetter = settingTypeGetter;
   }

   public String serialize(C category) {
      DB dataBuilder = this.getConfiguredDataBuilderForCategory(category);
      return this.serializer.serialize(dataBuilder.build());
   }

   protected DB getConfiguredDataBuilderForCategory(C category) {
      DB dataBuilder = this.dataBuilderFactory.get().setDefault();
      dataBuilder.setName(category.getName());
      dataBuilder.setProtection(category.getProtection());
      category.getSettingOverridesIterator().forEachRemaining(e -> dataBuilder.setSettingOverride(e.getKey().getId(), e.getValue()));
      category.getDirectSubCategoryIterator().forEachRemaining(c -> dataBuilder.addSubCategoryBuilder(this.getConfiguredDataBuilderForCategory((C)c)));
      return dataBuilder;
   }

   public C deserialize(String serializedData) {
      D data = this.serializer.deserialize(serializedData);
      B categoryBuilder = this.getConfiguredCategoryBuilderForData(data);
      return categoryBuilder.build();
   }

   protected B getConfiguredCategoryBuilderForData(D data) {
      B objectCategoryBuilder = this.objectCategoryBuilderFactory.get().setDefault();
      String dataName = data.getName();
      objectCategoryBuilder.setName(dataName);
      objectCategoryBuilder.setProtection(data.getProtection());
      data.getSettingOverrideIterator().forEachRemaining(e -> {
         ObjectCategorySetting<?> setting = this.settingTypeGetter.apply(e.getKey());
         if (setting != null) {
            this.setSettingValue(objectCategoryBuilder, setting, e.getValue());
         }
      });
      data.getSubCategoryIterator()
         .forEachRemaining(subCategory -> objectCategoryBuilder.addSubCategoryBuilder(this.getConfiguredCategoryBuilderForData((D)subCategory)));
      return objectCategoryBuilder;
   }

   private <T> void setSettingValue(B objectCategoryBuilder, ObjectCategorySetting<T> setting, Object value) {
      objectCategoryBuilder.setSettingValue(setting, (T)value);
   }

   public abstract static class Builder<D extends ObjectCategoryData<D>, C extends ObjectCategory<D, C>, B extends ObjectCategory.Builder<C, B>, DB extends ObjectCategoryData.Builder<D, DB>, SH extends ObjectCategorySerializationHandler<D, C, B, DB>, SHB extends ObjectCategorySerializationHandler.Builder<D, C, B, DB, SH, SHB>> {
      protected final SHB self = (SHB)this;
      protected final ObjectCategoryDataSerializer.Builder<D> serializerBuilder;
      protected Supplier<DB> dataBuilderFactory;
      protected Supplier<B> objectCategoryBuilderFactory;
      protected Function<String, ObjectCategorySetting<?>> settingTypeGetter;

      public Builder(ObjectCategoryDataSerializer.Builder<D> serializerBuilder) {
         this.serializerBuilder = serializerBuilder;
      }

      public SHB setDefault() {
         this.setDataBuilderFactory(null);
         this.setObjectCategoryBuilderFactory(null);
         this.setSettingTypeGetter(null);
         return this.self;
      }

      public SHB setDataBuilderFactory(Supplier<DB> dataBuilderFactory) {
         this.dataBuilderFactory = dataBuilderFactory;
         return this.self;
      }

      public SHB setObjectCategoryBuilderFactory(Supplier<B> objectCategoryBuilderFactory) {
         this.objectCategoryBuilderFactory = objectCategoryBuilderFactory;
         return this.self;
      }

      public SHB setSettingTypeGetter(Function<String, ObjectCategorySetting<?>> settingTypeGetter) {
         this.settingTypeGetter = settingTypeGetter;
         return this.self;
      }

      public SH build() {
         if (this.dataBuilderFactory != null && this.objectCategoryBuilderFactory != null && this.settingTypeGetter != null) {
            return this.buildInternally();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      protected abstract SH buildInternally();
   }
}
