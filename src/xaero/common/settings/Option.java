package xaero.common.settings;

import net.minecraft.class_2561;
import net.minecraft.class_339;

public abstract class Option {
   protected final ModOptions option;
   private final class_2561 caption;

   public Option(ModOptions option) {
      this.option = option;
      this.caption = class_2561.method_43471(option.getEnumStringRaw());
   }

   public class_2561 getCaption() {
      return this.caption;
   }

   public abstract class_339 createButton(int var1, int var2, int var3);
}
