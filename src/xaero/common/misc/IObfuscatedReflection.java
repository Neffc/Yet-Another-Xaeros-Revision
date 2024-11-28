package xaero.common.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IObfuscatedReflection {
   Class<?> getClassForName(String var1, String var2) throws ClassNotFoundException;

   Field getFieldReflection(Class<?> var1, String var2, String var3, String var4, String var5);

   Method getMethodReflection(Class<?> var1, String var2, String var3, String var4, String var5, Class<?>... var6);
}
