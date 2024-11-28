package xaero.common.platform;

import java.util.ServiceLoader;
import xaero.common.MinimapLogs;
import xaero.common.platform.services.IPlatformHelper;

public class Services {
   public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

   public static <T> T load(Class<T> clazz) {
      T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
      MinimapLogs.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
      return loadedService;
   }
}
