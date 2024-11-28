package xaero.common.config;

import java.nio.file.Files;
import java.nio.file.Path;
import xaero.common.IXaeroMinimap;
import xaero.common.platform.Services;

public class CommonConfigInit {
   public void init(IXaeroMinimap modMain, String configFileName) {
      Path configDestinationPath;
      if (Services.PLATFORM.isDedicatedServer()) {
         configDestinationPath = Services.PLATFORM.getGameDir();
      } else {
         configDestinationPath = Services.PLATFORM.getConfigDir();
      }

      Path configPath = configDestinationPath.resolve(configFileName);
      CommonConfigIO io = new CommonConfigIO(configPath);
      modMain.setCommonConfigIO(io);
      if (Files.exists(configPath)) {
         modMain.setCommonConfig(io.load());
      } else {
         modMain.setCommonConfig(CommonConfig.Builder.begin().build());
      }

      io.save(modMain.getCommonConfig());
   }
}
