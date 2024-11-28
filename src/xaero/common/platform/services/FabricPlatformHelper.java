package xaero.common.platform.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.fabricmc.loader.api.metadata.ModOrigin.Kind;
import xaero.common.controls.IKeyBindingHelper;
import xaero.common.controls.KeyBindingHelperFabric;
import xaero.common.hud.render.util.FabricRenderUtil;
import xaero.common.misc.IObfuscatedReflection;
import xaero.common.misc.ObfuscatedReflectionFabric;
import xaero.hud.render.util.IPlatformRenderUtil;

public class FabricPlatformHelper implements IPlatformHelper {
   private final IObfuscatedReflection obfuscatedReflectionFabric = new ObfuscatedReflectionFabric();
   private final KeyBindingHelperFabric keyBindingHelperFabric = new KeyBindingHelperFabric();
   private final FabricRenderUtil fabricRenderUtil = new FabricRenderUtil();

   @Override
   public String getPlatformName() {
      return "Fabric";
   }

   @Override
   public boolean isModLoaded(String modId) {
      return FabricLoader.getInstance().isModLoaded(modId);
   }

   @Override
   public boolean isDevelopmentEnvironment() {
      return FabricLoader.getInstance().isDevelopmentEnvironment();
   }

   @Override
   public IObfuscatedReflection getObfuscatedReflection() {
      return this.obfuscatedReflectionFabric;
   }

   @Override
   public IKeyBindingHelper getKeyBindingHelper() {
      return this.keyBindingHelperFabric;
   }

   @Override
   public IPlatformRenderUtil getPlatformRenderUtil() {
      return this.fabricRenderUtil;
   }

   @Override
   public boolean isDedicatedServer() {
      return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
   }

   @Override
   public Path getGameDir() {
      return FabricLoader.getInstance().getGameDir().normalize();
   }

   @Override
   public Path getConfigDir() {
      return FabricLoader.getInstance().getConfigDir();
   }

   @Override
   public Path getModFile(String modId) {
      ModContainer modContainer = (ModContainer)FabricLoader.getInstance().getModContainer(modId).orElse(null);
      ModOrigin origin = modContainer.getOrigin();
      Path modFile = origin.getKind() == Kind.PATH ? (Path)origin.getPaths().get(0) : null;
      if (modFile == null) {
         try {
            Class<?> quiltLoaderClass = Class.forName("org.quiltmc.loader.api.QuiltLoader");
            Method quiltGetModContainerMethod = quiltLoaderClass.getDeclaredMethod("getModContainer", String.class);
            Class<?> quiltModContainerAPIClass = Class.forName("org.quiltmc.loader.api.ModContainer");
            Method quiltGetSourcePathsMethod = quiltModContainerAPIClass.getDeclaredMethod("getSourcePaths");
            Object quiltModContainer = ((Optional)quiltGetModContainerMethod.invoke(null, modContainer.getMetadata().getId())).orElse(null);
            List<List<Path>> paths = (List<List<Path>>)quiltGetSourcePathsMethod.invoke(quiltModContainer);
            if (!paths.isEmpty() && !paths.get(0).isEmpty()) {
               modFile = paths.get(0).get(0);
            }
         } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException var11) {
         }
      }

      return modFile;
   }
}
