package xaero.common.minimap.render.radar;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.class_4592;
import net.minecraft.class_4595;
import net.minecraft.class_583;
import xaero.common.misc.SeparatedKeysParser;
import xaero.hud.minimap.MinimapLogs;

public class EntityIconModelFieldResolver {
   public static final SeparatedKeysParser KEYS_PARSER = new SeparatedKeysParser(c -> c == ',' || c == ';');

   public static Object[] handleDeclaredField(Field f, Object currentChainNode, String matchedFilterElement, Object[] oneResultArray) throws IllegalArgumentException, IllegalAccessException {
      boolean accessibleBU = f.isAccessible();

      Object[] var22;
      try {
         f.setAccessible(true);
         Object referencedObject = f.get(currentChainNode);
         if (referencedObject == null) {
            return null;
         }

         EntityIconModelFieldResolver.FieldReferenceType<?> referenceType = getReferenceType(referencedObject);
         Object[] collectionArray = referenceType.getArray(referencedObject, oneResultArray);
         if (collectionArray.length <= 0) {
            throw new IllegalArgumentException("Empty collection referenced " + matchedFilterElement + "!");
         }

         if (matchedFilterElement == null || !matchedFilterElement.endsWith("]")) {
            return collectionArray;
         }

         int lastStartBracket = matchedFilterElement.lastIndexOf(91);
         if (lastStartBracket == -1) {
            throw new IllegalArgumentException("Field name " + matchedFilterElement + " ends with ] but is missing [!");
         }

         try {
            String keysString = matchedFilterElement.substring(lastStartBracket + 1, matchedFilterElement.length() - 1);
            String[] keys = KEYS_PARSER.parseKeys(keysString);
            Object[] result = keys.length == 1 ? oneResultArray : (Object[])Array.newInstance(oneResultArray.getClass().getComponentType(), keys.length);

            for (int i = 0; i < keys.length; i++) {
               String keyString = keys[i];
               Object element = referenceType.getElement(referencedObject, collectionArray, keyString);
               result[i] = element;
            }

            var22 = result;
         } catch (Exception var18) {
            throw new IllegalArgumentException("Invalid element index/indices in " + matchedFilterElement + "!", var18);
         }
      } finally {
         f.setAccessible(accessibleBU);
      }

      return var22;
   }

   public static void searchSuperclassFields(
      Object currentChainNode, List<String> filter, EntityIconModelFieldResolver.Listener listener, Object[] oneResultArray
   ) {
      Class<?> nodeClass = currentChainNode.getClass();

      while (nodeClass != class_583.class && nodeClass != class_4592.class && nodeClass != class_4595.class && nodeClass != Object.class) {
         Field[] declaredModelFields = nodeClass.getDeclaredFields();
         handleFields(currentChainNode, declaredModelFields, filter, listener, oneResultArray);
         if (listener.shouldStop() || (nodeClass = nodeClass.getSuperclass()) == null) {
            break;
         }
      }
   }

   public static void handleFields(
      Object currentChainNode, Field[] declaredModelFields, List<String> filter, EntityIconModelFieldResolver.Listener listener, Object[] oneResultArray
   ) {
      for (Field f : declaredModelFields) {
         if (listener.isFieldAllowed(f)) {
            try {
               String comparisonName = f.getDeclaringClass().getName() + ";" + f.getName();
               String matchedFilterElement = null;
               if (filter == null || (matchedFilterElement = passesFilter(comparisonName, filter)) != null) {
                  Object[] matchingObjects = handleDeclaredField(f, currentChainNode, matchedFilterElement, oneResultArray);
                  if (matchingObjects != null) {
                     listener.onFieldResolved(matchingObjects, matchedFilterElement);
                  }

                  if (listener.shouldStop()) {
                     break;
                  }
               }
            } catch (Exception var12) {
               MinimapLogs.LOGGER.error("suppressed exception", var12);
            }
         }
      }
   }

   private static String passesFilter(String entry, List<String> filter) {
      for (String f : filter) {
         if (f.equals(entry)) {
            return f;
         }

         int indexOfBracket = f.lastIndexOf(91);
         if (indexOfBracket != -1 && f.substring(0, indexOfBracket).equals(entry)) {
            return f;
         }
      }

      return null;
   }

   private static EntityIconModelFieldResolver.FieldReferenceType<?> getReferenceType(Object o) {
      if (o instanceof Object[]) {
         return EntityIconModelFieldResolver.FieldReferenceType.ARRAY;
      } else if (o instanceof Collection) {
         return EntityIconModelFieldResolver.FieldReferenceType.COLLECTION;
      } else {
         return o instanceof Map ? EntityIconModelFieldResolver.FieldReferenceType.MAP : EntityIconModelFieldResolver.FieldReferenceType.SINGLE;
      }
   }

   @FunctionalInterface
   private interface FieldReferenceElementGetter<T> {
      Object get(T var1, Object[] var2, String var3);
   }

   private static class FieldReferenceType<T> {
      public static EntityIconModelFieldResolver.FieldReferenceType<Object> SINGLE = new EntityIconModelFieldResolver.FieldReferenceType<>((o, a, k) -> {
         throw new RuntimeException(String.format("%s is not an array/collection!"));
      }, (o, ora) -> {
         ora[0] = o;
         return ora;
      });
      public static EntityIconModelFieldResolver.FieldReferenceType<Object[]> ARRAY = new EntityIconModelFieldResolver.FieldReferenceType<>(
         (o, a, k) -> o[Integer.parseInt(k.trim())], (o, ora) -> o
      );
      public static EntityIconModelFieldResolver.FieldReferenceType<Collection<?>> COLLECTION = new EntityIconModelFieldResolver.FieldReferenceType<>(
         (o, a, k) -> a[Integer.parseInt(k.trim())], (o, ora) -> o.toArray(ora)
      );
      public static EntityIconModelFieldResolver.FieldReferenceType<Map<?, ?>> MAP = new EntityIconModelFieldResolver.FieldReferenceType<>((o, a, k) -> {
         Object result = o.get(k);
         if (result == null) {
            try {
               int integerAttemptKey = Integer.parseInt(k.trim());
               result = o.get(integerAttemptKey);
            } catch (NumberFormatException var5) {
            }
         }

         return result;
      }, (o, ora) -> o.values().toArray(ora));
      private EntityIconModelFieldResolver.FieldReferenceElementGetter<T> elementGetter;
      private BiFunction<T, Object[], Object[]> arrayGetter;

      private FieldReferenceType(EntityIconModelFieldResolver.FieldReferenceElementGetter<T> elementGetter, BiFunction<T, Object[], Object[]> arrayGetter) {
         this.elementGetter = elementGetter;
         this.arrayGetter = arrayGetter;
      }

      public Object[] getArray(Object referencedObject, Object[] oneResultArray) {
         return this.arrayGetter.apply((T)referencedObject, oneResultArray);
      }

      public Object getElement(Object referencedObject, Object[] array, String key) {
         return this.elementGetter.get((T)referencedObject, array, key);
      }
   }

   public interface Listener {
      boolean isFieldAllowed(Field var1);

      boolean shouldStop();

      void onFieldResolved(Object[] var1, String var2);
   }
}
