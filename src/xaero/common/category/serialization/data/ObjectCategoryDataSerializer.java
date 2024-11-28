package xaero.common.category.serialization.data;

public abstract class ObjectCategoryDataSerializer<D extends ObjectCategoryData<D>> {
   protected ObjectCategoryDataSerializer() {
   }

   public abstract String serialize(D var1);

   public abstract D deserialize(String var1);

   public abstract static class Builder<D extends ObjectCategoryData<D>> {
      public ObjectCategoryDataSerializer.Builder<D> setDefault() {
         return this;
      }

      public abstract ObjectCategoryDataSerializer<D> build();
   }
}
