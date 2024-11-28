package xaero.common.config;

import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import xaero.common.AXaeroMinimap;

public class CommonConfigInit {
   public void init(AXaeroMinimap modMain, String configFileName) {
      Path configDestinationPath;
      if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
         configDestinationPath = modMain.getGameDir();
      } else {
         configDestinationPath = FabricLoader.getInstance().getConfigDirectory().toPath();
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
