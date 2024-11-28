package xaero.common.minimap.radar.category;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.minimap.radar.category.serialization.EntityRadarCategorySerializationHandler;
import xaero.common.misc.Misc;

public final class EntityRadarCategoryFileIO {
   private final IXaeroMinimap modMain;
   private final Path saveLocationPath;
   private final EntityRadarCategorySerializationHandler serializationHandler;

   private EntityRadarCategoryFileIO(
      @Nonnull IXaeroMinimap modMain, @Nonnull Path saveLocationPath, @Nonnull EntityRadarCategorySerializationHandler serializationHandler
   ) {
      this.modMain = modMain;
      this.saveLocationPath = saveLocationPath;
      this.serializationHandler = serializationHandler;
   }

   public void saveRootCategory(EntityRadarCategory category) {
      Path saveLocationTempPath = this.saveLocationPath.resolveSibling(this.saveLocationPath.getFileName().toString() + ".temp");
      String serializedData = this.serializationHandler.serialize(category);
      this.saveRootCategory(saveLocationTempPath, serializedData, 10);
   }

   public void saveRootCategory(Path saveLocationTempPath, String serializedData, int attempts) {
      try {
         try (
            FileOutputStream fileOutput = new FileOutputStream(saveLocationTempPath.toFile());
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            OutputStreamWriter writer = new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8);
         ) {
            writer.write(serializedData);
            writer.close();
            Misc.safeMoveAndReplace(saveLocationTempPath, this.saveLocationPath, true);
         }
      } catch (IOException var16) {
         if (attempts > 0) {
            MinimapLogs.LOGGER.info("Failed to save entity radar categories. Retrying... " + attempts);

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var9) {
            }

            this.saveRootCategory(saveLocationTempPath, serializedData, --attempts);
         } else {
            MinimapLogs.LOGGER.error("suppressed exception", var16);
         }
      }
   }

   public EntityRadarCategory loadRootCategory() throws IOException {
      String serializedData;
      try (
         FileInputStream fileInput = new FileInputStream(this.saveLocationPath.toFile());
         BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput, "UTF8"));
      ) {
         StringBuilder stringBuilder = new StringBuilder();
         reader.lines().forEach(line -> {
            stringBuilder.append(line);
            stringBuilder.append('\n');
         });
         serializedData = stringBuilder.toString();
      }

      try {
         return this.serializationHandler.deserialize(serializedData);
      } catch (Throwable var8) {
         MinimapLogs.LOGGER.error("Minimap entity radar config file is not usable (is likely corrupt)! Resolving...");
         Path backupPath = Misc.quickFileBackupMove(this.saveLocationPath);
         MinimapLogs.LOGGER.error(String.format("The broken file was backed up to %s and ignored.", backupPath), var8);
         return null;
      }
   }

   public static final class Builder {
      private final IXaeroMinimap modMain;
      private Path saveLocationPath;
      private final EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder;

      public Builder(IXaeroMinimap modMain, EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder) {
         this.modMain = modMain;
         this.serializationHandlerBuilder = serializationHandlerBuilder;
      }

      private EntityRadarCategoryFileIO.Builder setDefault() {
         this.saveLocationPath = null;
         return this;
      }

      public EntityRadarCategoryFileIO.Builder setSaveLocationPath(Path saveLocationPath) {
         this.saveLocationPath = saveLocationPath;
         return this;
      }

      public EntityRadarCategoryFileIO build() {
         if (this.saveLocationPath != null && this.serializationHandlerBuilder != null) {
            return new EntityRadarCategoryFileIO(this.modMain, this.saveLocationPath, this.serializationHandlerBuilder.build());
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      public static EntityRadarCategoryFileIO.Builder getDefault(
         IXaeroMinimap modMain, EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder
      ) {
         return new EntityRadarCategoryFileIO.Builder(modMain, serializationHandlerBuilder).setDefault();
      }
   }
}
