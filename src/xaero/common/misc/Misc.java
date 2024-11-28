package xaero.common.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import net.minecraft.class_1041;
import net.minecraft.class_1291;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_2561;
import net.minecraft.class_2818;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_342;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_8251;
import net.minecraft.class_327.class_6415;
import net.minecraft.class_3675.class_307;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.controls.IKeyBindingHelper;
import xaero.common.gui.IScreenBase;
import xaero.common.platform.Services;

public class Misc {
   public static double getMouseX(class_310 mc, boolean raw) {
      return raw
         ? mc.field_1729.method_1603()
         : mc.field_1729.method_1603() * (double)mc.method_22683().method_4489() / (double)mc.method_22683().method_4480();
   }

   public static double getMouseY(class_310 mc, boolean raw) {
      return raw
         ? mc.field_1729.method_1604()
         : mc.field_1729.method_1604() * (double)mc.method_22683().method_4506() / (double)mc.method_22683().method_4507();
   }

   public static void drawNormalText(class_4587 matrices, String name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      class_310.method_1551()
         .field_1772
         .method_27521(name, x, y, color, shadow, matrices.method_23760().method_23761(), renderTypeBuffer, class_6415.field_33993, 0, 15728880);
   }

   public static void drawNormalText(class_4587 matrices, class_2561 name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      class_310.method_1551()
         .field_1772
         .method_30882(name, x, y, color, shadow, matrices.method_23760().method_23761(), renderTypeBuffer, class_6415.field_33993, 0, 15728880);
   }

   public static void drawPiercingText(class_4587 matrices, String name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      class_310.method_1551()
         .field_1772
         .method_27521(name, x, y, color, shadow, matrices.method_23760().method_23761(), renderTypeBuffer, class_6415.field_33994, 0, 15728880);
   }

   public static void drawPiercingText(class_4587 matrices, class_2561 name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      class_310.method_1551()
         .field_1772
         .method_30882(name, x, y, color, shadow, matrices.method_23760().method_23761(), renderTypeBuffer, class_6415.field_33994, 0, 15728880);
   }

   public static void drawCenteredPiercingText(class_4587 matrices, String name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      drawPiercingText(matrices, name, x - (float)(class_310.method_1551().field_1772.method_1727(name) / 2), y, color, shadow, renderTypeBuffer);
   }

   public static void drawCenteredPiercingText(class_4587 matrices, class_2561 name, float x, float y, int color, boolean shadow, class_4598 renderTypeBuffer) {
      drawPiercingText(matrices, name, x - (float)(class_310.method_1551().field_1772.method_27525(name) / 2), y, color, shadow, renderTypeBuffer);
   }

   public static void minecraftOrtho(class_310 mc, boolean raw) {
      class_1041 mainwindow = mc.method_22683();
      int width = raw ? mainwindow.method_4480() : mainwindow.method_4489();
      int height = raw ? mainwindow.method_4507() : mainwindow.method_4506();
      Matrix4f ortho = new Matrix4f()
         .setOrtho(0.0F, (float)((double)width / mainwindow.method_4495()), (float)((double)height / mainwindow.method_4495()), 0.0F, 1000.0F, 21000.0F);
      RenderSystem.setProjectionMatrix(ortho, class_8251.field_43361);
   }

   public static Path quickFileBackupMove(Path file) throws IOException {
      Path backupPath = null;
      int backupNumber = 0;

      while (Files.exists(backupPath = file.resolveSibling(file.getFileName().toString() + ".backup" + backupNumber))) {
         backupNumber++;
      }

      Files.move(file, backupPath);
      return backupPath;
   }

   public static void safeMoveAndReplace(Path from, Path to, boolean backupFrom) throws IOException {
      Path backupPath = null;
      Path fromBackupPath = null;
      if (backupFrom) {
         while (true) {
            try {
               fromBackupPath = quickFileBackupMove(from);
               break;
            } catch (IOException var8) {
               try {
                  Thread.sleep(10L);
               } catch (InterruptedException var7) {
               }
            }
         }
      } else {
         fromBackupPath = from;
      }

      if (Files.exists(to)) {
         backupPath = quickFileBackupMove(to);
      }

      Files.move(fromBackupPath, to);
      if (backupPath != null) {
         Files.delete(backupPath);
      }
   }

   public static void deleteFile(Path file, int attempts) throws IOException {
      attempts--;

      try {
         Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
               Files.delete(path);
               return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path path, IOException iOException) throws IOException {
               if (iOException != null) {
                  throw iOException;
               } else {
                  Files.delete(path);
                  return FileVisitResult.CONTINUE;
               }
            }
         });
      } catch (IOException var5) {
         if (attempts <= 0) {
            throw var5;
         }

         MinimapLogs.LOGGER.info("Failed to delete file/folder! Retrying... " + attempts);

         try {
            Thread.sleep(50L);
         } catch (InterruptedException var4) {
         }

         deleteFile(file, attempts);
      }
   }

   public static boolean inputMatchesKeyBinding(IXaeroMinimap modMain, class_307 type, int code, class_304 kb, int keyConflictContext) {
      IKeyBindingHelper keyBindingHelper = Services.PLATFORM.getKeyBindingHelper();
      return kb != null
         && code != -1
         && keyBindingHelper.getBoundKeyOf(kb).method_1442() == type
         && keyBindingHelper.getBoundKeyOf(kb).method_1444() == code
         && keyBindingHelper.modifiersAreActive(kb, keyConflictContext);
   }

   public static boolean screenShouldSkipWorldRender(IXaeroMinimap modMain, class_437 screen, boolean checkOtherMod) {
      return screen instanceof IScreenBase && ((IScreenBase)screen).shouldSkipWorldRender()
         || checkOtherMod && modMain.getSupportMods().worldmap() && modMain.getSupportMods().worldmapSupport.screenShouldSkipWorldRender(screen);
   }

   public static long getChunkPosAsLong(class_2818 chunk) {
      return chunk.method_12004().method_8324();
   }

   public static Class<?> getClassForName(String obfuscatedName, String deobfName) throws ClassNotFoundException {
      IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
      return obfuscatedReflection.getClassForName(obfuscatedName, deobfName);
   }

   public static Field getFieldReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge) {
      IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
      return obfuscatedReflection.getFieldReflection(c, deobfName, obfuscatedNameFabric, descriptor, obfuscatedNameForge);
   }

   public static <A, B> B getReflectFieldValue(A parentObject, Field field) {
      boolean accessibleBU = field.isAccessible();
      field.setAccessible(true);
      B result = null;

      try {
         result = (B)field.get(parentObject);
      } catch (Exception var5) {
         MinimapLogs.LOGGER.error("suppressed exception", var5);
      }

      field.setAccessible(accessibleBU);
      return result;
   }

   public static <A, B> void setReflectFieldValue(A parentObject, Field field, B value) {
      boolean accessibleBU = field.isAccessible();
      field.setAccessible(true);

      try {
         field.set(parentObject, value);
      } catch (Exception var5) {
         MinimapLogs.LOGGER.error("suppressed exception", var5);
      }

      field.setAccessible(accessibleBU);
   }

   public static Method getMethodReflection(
      Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge, Class<?>... parameters
   ) {
      IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
      return obfuscatedReflection.getMethodReflection(c, deobfName, obfuscatedNameFabric, descriptor, obfuscatedNameForge, parameters);
   }

   public static <A, B> B getReflectMethodValue(A parentObject, Method method, Object... arguments) {
      boolean accessibleBU = method.isAccessible();
      method.setAccessible(true);
      B result = null;

      try {
         result = (B)method.invoke(parentObject, arguments);
      } catch (Exception var6) {
         MinimapLogs.LOGGER.error("suppressed exception", var6);
      }

      method.setAccessible(accessibleBU);
      return result;
   }

   public static void download(BufferedOutputStream output, InputStream input) throws IOException {
      byte[] buffer = new byte[256];

      while (true) {
         int read = input.read(buffer, 0, buffer.length);
         if (read < 0) {
            output.flush();
            input.close();
            output.close();
            return;
         }

         output.write(buffer, 0, read);
      }
   }

   public static boolean hasItem(class_1657 player, class_1792 item) {
      return hasItem(player.method_31548().field_7544, -1, item)
         || hasItem(player.method_31548().field_7548, -1, item)
         || hasItem(player.method_31548().field_7547, 9, item);
   }

   public static boolean hasItem(class_2371<class_1799> inventory, int limit, class_1792 item) {
      for (int i = 0; i < inventory.size() && (limit == -1 || i < limit); i++) {
         if (inventory.get(i) != null && ((class_1799)inventory.get(i)).method_7909() == item) {
            return true;
         }
      }

      return false;
   }

   public static void setFieldText(class_342 field, String text) {
      setFieldText(field, text, -1);
   }

   public static void setFieldText(class_342 field, String text, int color) {
      field.method_1868(color);
      if (!field.method_1882().equals(text)) {
         field.method_1852(text);
      }
   }

   public static class_2561 getFixedDisplayName(class_1297 e) {
      class_2561 baseName = e.method_5477();
      if (baseName == null) {
         return null;
      } else {
         return e.method_5781() == null ? baseName.method_27661() : e.method_5781().method_1198(baseName.method_27661());
      }
   }

   public static boolean hasEffect(class_1657 player, class_1291 effect) {
      return effect != null && player.method_6059(effect);
   }

   public static boolean hasEffect(class_1291 effect) {
      return hasEffect(class_310.method_1551().field_1724, effect);
   }
}
