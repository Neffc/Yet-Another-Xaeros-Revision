package xaero.common.minimap.radar.category;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import xaero.common.AXaeroMinimap;
import xaero.common.category.rule.resolver.ObjectCategoryRuleResolver;
import xaero.common.category.serialization.data.ObjectCategoryDataGsonSerializer;
import xaero.common.category.setting.ObjectCategoryDefaultSettingsSetter;
import xaero.common.minimap.radar.category.serialization.EntityRadarCategorySerializationHandler;
import xaero.common.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;

public final class EntityRadarCategoryManager {
   private final AXaeroMinimap modMain;
   private final Path mainFilePath;
   private final Path secondaryFilePath;
   private EntityRadarCategoryFileIO mainFileIO;
   private EntityRadarCategoryFileIO secondaryFileIO;
   private EntityRadarCategory rootCategory;
   private EntityRadarDefaultCategories defaultCategoryConfigurator;
   private ObjectCategoryRuleResolver ruleResolver;

   private EntityRadarCategoryManager(@Nonnull AXaeroMinimap modMain, @Nonnull Path mainFilePath, @Nonnull Path secondaryFilePath) {
      this.modMain = modMain;
      this.mainFilePath = mainFilePath;
      this.secondaryFilePath = secondaryFilePath;
   }

   public void init() throws IOException {
      ObjectCategoryDataGsonSerializer.Builder<EntityRadarCategoryData> dataSerializerBuilder = ObjectCategoryDataGsonSerializer.Builder.getDefault(
         new GsonBuilder().setPrettyPrinting().create(), EntityRadarCategoryData.class
      );
      EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder = EntityRadarCategorySerializationHandler.Builder.getDefault(
         dataSerializerBuilder
      );
      EntityRadarCategoryFileIO.Builder fileIOBuilder = EntityRadarCategoryFileIO.Builder.getDefault(this.modMain, serializationHandlerBuilder)
         .setSaveLocationPath(this.mainFilePath);
      this.mainFileIO = fileIOBuilder.build();
      fileIOBuilder = EntityRadarCategoryFileIO.Builder.getDefault(this.modMain, serializationHandlerBuilder).setSaveLocationPath(this.secondaryFilePath);
      this.secondaryFileIO = fileIOBuilder.build();
      this.defaultCategoryConfigurator = EntityRadarDefaultCategories.Builder.getDefault().build();
      this.ruleResolver = ObjectCategoryRuleResolver.Builder.getDefault().build();
      ObjectCategoryDefaultSettingsSetter defaultSettings = ObjectCategoryDefaultSettingsSetter.Builder.getDefault()
         .setSettings(EntityRadarCategorySettings.SETTINGS)
         .build();
      EntityRadarCategory root = null;
      if (Files.exists(this.mainFilePath)) {
         root = this.mainFileIO.loadRootCategory();
      }

      if (root == null && Files.exists(this.secondaryFilePath)) {
         root = this.secondaryFileIO.loadRootCategory();
      }

      if (root == null) {
         root = this.defaultCategoryConfigurator.setupDefault(this.modMain.getSettings());
      }

      defaultSettings.setDefaultsFor(root, true);
      this.mainFileIO.saveRootCategory(root);
      this.modMain.getSettings().resetEntityRadarBackwardsCompatibilityConfig();
      this.rootCategory = root;
   }

   public ObjectCategoryRuleResolver getRuleResolver() {
      return this.ruleResolver;
   }

   public EntityRadarCategory getRootCategory() {
      return this.rootCategory;
   }

   public EntityRadarDefaultCategories getDefaultCategoryConfigurator() {
      return this.defaultCategoryConfigurator;
   }

   public void setRootCategory(EntityRadarCategory rootCategory) {
      this.rootCategory = rootCategory;
   }

   public Path getSecondaryFilePath() {
      return this.secondaryFilePath;
   }

   public EntityRadarCategoryFileIO getSecondaryFileIO() {
      return this.secondaryFileIO;
   }

   public void save() {
      this.mainFileIO.saveRootCategory(this.rootCategory);
   }

   public static final class Builder {
      private AXaeroMinimap modMain;
      private Path mainFilePath;
      private Path secondaryFilePath;

      public EntityRadarCategoryManager.Builder setModMain(AXaeroMinimap modMain) {
         this.modMain = modMain;
         return this;
      }

      public EntityRadarCategoryManager.Builder setMainFilePath(Path mainFilePath) {
         this.mainFilePath = mainFilePath;
         return this;
      }

      public EntityRadarCategoryManager.Builder setSecondaryFilePath(Path secondaryFilePath) {
         this.secondaryFilePath = secondaryFilePath;
         return this;
      }

      public EntityRadarCategoryManager.Builder setDefault() {
         this.setMainFilePath(EntityRadarCategoryConstants.CONFIG_PATH);
         this.setSecondaryFilePath(EntityRadarCategoryConstants.DEFAULT_CONFIG_PATH);
         return this;
      }

      public EntityRadarCategoryManager build() {
         if (this.modMain != null && this.mainFilePath != null && this.secondaryFilePath != null) {
            return new EntityRadarCategoryManager(this.modMain, this.mainFilePath, this.secondaryFilePath);
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      public static EntityRadarCategoryManager.Builder getDefault() {
         return new EntityRadarCategoryManager.Builder().setDefault();
      }
   }
}
