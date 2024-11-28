package xaero.common.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class ObfuscatedReflectionFabric implements IObfuscatedReflection {
   private static String fixFabricFieldMapping(Class<?> clazz, String name, String descriptor) {
      MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
      String owner = mappingResolver.unmapClassName("intermediary", clazz.getName());
      return mappingResolver.mapFieldName("intermediary", owner, name, descriptor);
   }

   private static String fixFabricMethodMapping(Class<?> clazz, String name, String descriptor) {
      MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
      String owner = mappingResolver.unmapClassName("intermediary", clazz.getName());
      return mappingResolver.mapMethodName("intermediary", owner, name, descriptor);
   }

   @Override
   public Class<?> getClassForName(String obfuscatedName, String deobfName) throws ClassNotFoundException {
      MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
      String name = mappingResolver.mapClassName("intermediary", obfuscatedName);
      return Class.forName(name);
   }

   @Override
   public Field getFieldReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge) {
      String name = fixFabricFieldMapping(c, obfuscatedNameFabric, descriptor);
      Field field = null;

      try {
         return c.getDeclaredField(name);
      } catch (NoSuchFieldException var9) {
         throw new RuntimeException(var9);
      }
   }

   @Override
   public Method getMethodReflection(
      Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge, Class<?>... parameters
   ) {
      String name = fixFabricMethodMapping(c, obfuscatedNameFabric, descriptor);
      Method method = null;

      try {
         return c.getDeclaredMethod(name, parameters);
      } catch (NoSuchMethodException var10) {
         throw new RuntimeException(var10);
      }
   }
}
