package xaero.common.mods;

import de.siphalor.amecs.api.KeyBindingUtils;
import de.siphalor.amecs.api.KeyModifiers;
import net.minecraft.class_304;
import org.apache.logging.log4j.Logger;

public class SupportAmecs {
   public SupportAmecs(Logger logger) {
   }

   public boolean modifiersArePressed(class_304 keyBinding) {
      KeyModifiers modifiers = KeyBindingUtils.getBoundModifiers(keyBinding);
      return modifiers.isPressed();
   }
}
