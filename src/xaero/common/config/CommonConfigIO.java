package xaero.common.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import xaero.hud.minimap.MinimapLogs;

public class CommonConfigIO {
   private final Path configFilePath;

   public CommonConfigIO(Path configFilePath) {
      this.configFilePath = configFilePath;
   }

   public void save(CommonConfig config) {
      try {
         Path parentFolder = this.configFilePath.getParent();
         if (parentFolder != null) {
            Files.createDirectories(parentFolder);
         }
      } catch (IOException var11) {
         MinimapLogs.LOGGER.error("suppressed exception", var11);
         return;
      }

      try (
         BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(this.configFilePath.toFile()));
         PrintWriter writer = new PrintWriter(bufferedOutput);
      ) {
         this.write(config, writer);
      } catch (IOException var10) {
         MinimapLogs.LOGGER.error("suppressed exception", var10);
      }
   }

   public CommonConfig load() {
      try {
         CommonConfig var5;
         try (
            BufferedInputStream bufferedOutput = new BufferedInputStream(new FileInputStream(this.configFilePath.toFile()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedOutput));
         ) {
            CommonConfig.Builder builder = CommonConfig.Builder.begin();

            String line;
            while ((line = reader.readLine()) != null) {
               this.readLine(builder, line.split(":"));
            }

            var5 = builder.build();
         }

         return var5;
      } catch (IOException var10) {
         throw new RuntimeException(var10);
      }
   }

   private void write(CommonConfig config, PrintWriter writer) {
      writer.println("allowCaveModeOnServer:" + config.allowCaveModeOnServer);
      writer.println("allowNetherCaveModeOnServer:" + config.allowNetherCaveModeOnServer);
      writer.println("allowRadarOnServer:" + config.allowRadarOnServer);
      writer.println("registerStatusEffects:" + config.registerStatusEffects);
   }

   private boolean readLine(CommonConfig.Builder configBuilder, String[] args) {
      if (args[0].equals("allowCaveModeOnServer")) {
         configBuilder.setAllowCaveModeOnServer(args[1].equals("true"));
         return true;
      } else if (args[0].equals("allowNetherCaveModeOnServer")) {
         configBuilder.setAllowNetherCaveModeOnServer(args[1].equals("true"));
         return true;
      } else if (args[0].equals("allowRadarOnServer")) {
         configBuilder.setAllowRadarOnServer(args[1].equals("true"));
         return true;
      } else if (args[0].equals("registerStatusEffects")) {
         configBuilder.setRegisterStatusEffects(args[1].equals("true"));
         return true;
      } else {
         return false;
      }
   }
}
