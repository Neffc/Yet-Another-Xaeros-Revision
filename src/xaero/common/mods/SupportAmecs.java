package xaero.common.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.class_304;
import org.apache.logging.log4j.Logger;

public class SupportAmecs {
   private Class<?> amecsClass_IKeyBinding;
   private Class<?> amecsClass_KeyModifiers;
   private Method amecsMethod_getKeyModifiers;
   private Method amecsMethod_isPressed;

   public SupportAmecs(Logger logger) {
      try {
         this.amecsClass_IKeyBinding = Class.forName("de.siphalor.amecs.impl.duck.IKeyBinding");
         this.amecsClass_KeyModifiers = Class.forName("de.siphalor.amecs.api.KeyModifiers");
         this.amecsMethod_getKeyModifiers = this.amecsClass_IKeyBinding.getDeclaredMethod("amecs$getKeyModifiers");
         this.amecsMethod_isPressed = this.amecsClass_KeyModifiers.getDeclaredMethod("isPressed");
      } catch (NoSuchMethodException | SecurityException | ClassNotFoundException var3) {
         logger.info("suppressed exception", var3);
      }
   }

   public boolean modifiersArePressed(class_304 keyBinding) {
      try {
         return (Boolean)this.amecsMethod_isPressed.invoke(this.amecsMethod_getKeyModifiers.invoke(keyBinding));
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }
}
