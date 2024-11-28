package xaero.common.platform.services;

import java.nio.file.Path;
import xaero.common.controls.IKeyBindingHelper;
import xaero.common.misc.IObfuscatedReflection;

public interface IPlatformHelper {
   String getPlatformName();

   boolean isModLoaded(String var1);

   boolean isDevelopmentEnvironment();

   default String getEnvironmentName() {
      return this.isDevelopmentEnvironment() ? "development" : "production";
   }

   IObfuscatedReflection getObfuscatedReflection();

   IKeyBindingHelper getKeyBindingHelper();

   boolean isDedicatedServer();

   Path getGameDir();

   Path getConfigDir();

   Path getModFile(String var1);
}
