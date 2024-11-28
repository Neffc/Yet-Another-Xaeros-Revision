package xaero.common.patreon;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_364;
import net.minecraft.class_410;
import net.minecraft.class_4185;
import org.apache.commons.codec.binary.Hex;
import xaero.common.MinimapLogs;

public class GuiUpdateAll extends class_410 {
   public GuiUpdateAll() {
      super(
         GuiUpdateAll::confirmResult,
         class_2561.method_43470("These mods are out-of-date: " + modListToNames(Patreon.getOutdatedMods())),
         class_2561.method_43470(
            Patreon.getHasAutoUpdates() ? "Would you like to automatically update them?" : "Would you like to update them (open the mod pages)?"
         )
      );
      Patreon.setNotificationDisplayed(true);
   }

   private static String modListToNames(List<Object> list) {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
         if (i != 0) {
            builder.append(", ");
         }

         builder.append(((PatreonMod)list.get(i)).modName);
      }

      return builder.toString();
   }

   public void method_25426() {
      super.method_25426();
      if (Patreon.getHasAutoUpdates()) {
         this.method_37063(class_4185.method_46430(class_2561.method_43469("Changelogs", new Object[0]), b -> {
            for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
               PatreonMod mod = (PatreonMod)Patreon.getOutdatedMods().get(i);

               try {
                  class_156.method_668().method_673(new URI(mod.changelogLink));
               } catch (URISyntaxException var4) {
                  MinimapLogs.LOGGER.error("suppressed exception", var4);
               }
            }
         }).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 6 + 120, 200, 20).method_46431());
      }

      this.method_37063(class_4185.method_46430(class_2561.method_43469("Don't show again for these updates", new Object[0]), b -> {
         for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
            PatreonMod mod = (PatreonMod)Patreon.getOutdatedMods().get(i);
            if (mod.onVersionIgnore != null) {
               mod.onVersionIgnore.run();
            }
         }

         this.field_22787.method_1507(null);
      }).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 6 + 144, 200, 20).method_46431());
   }

   private static void confirmResult(boolean p_confirmResult_1_) {
      if (p_confirmResult_1_) {
         boolean shouldExit = false;
         if (!Patreon.getHasAutoUpdates()) {
            shouldExit = true;

            for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
               PatreonMod m = (PatreonMod)Patreon.getOutdatedMods().get(i);

               try {
                  class_156.method_668().method_673(new URI(m.changelogLink));
                  if (m.modJar != null) {
                     class_156.method_668().method_672(m.modJar.getParentFile());
                  }
               } catch (Exception var5) {
                  MinimapLogs.LOGGER.error("suppressed exception", var5);
                  shouldExit = false;
               }
            }
         } else {
            for (class_364 b : class_310.method_1551().field_1755.method_25396()) {
               if (b instanceof class_4185) {
                  ((class_4185)b).field_22763 = false;
               }
            }

            shouldExit = autoUpdate();
         }

         if (shouldExit) {
            class_310.method_1551().method_1592();
         } else {
            class_310.method_1551().method_1507(null);
         }

         class_310.method_1551().method_1592();
      } else {
         class_310.method_1551().method_1507(null);
      }
   }

   private static void download(BufferedOutputStream output, InputStream input, boolean closeInput) throws IOException {
      byte[] buffer = new byte[256];

      while (true) {
         int read = input.read(buffer, 0, buffer.length);
         if (read < 0) {
            output.flush();
            if (closeInput) {
               input.close();
            }

            output.close();
            return;
         }

         output.write(buffer, 0, read);
      }
   }

   private static boolean autoUpdate() {
      try {
         MessageDigest digestMD5;
         try {
            digestMD5 = MessageDigest.getInstance("MD5");
         } catch (NoSuchAlgorithmException var20) {
            MinimapLogs.LOGGER.info("No algorithm for MD5.");
            return false;
         }

         PatreonMod autoupdater = (PatreonMod)Patreon.getMods().get("autoupdater30");
         String jarLink = autoupdater.changelogLink;
         String jarMD5 = autoupdater.latestVersionLayout;
         URL url = new URL(jarLink);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setReadTimeout(900);
         conn.setConnectTimeout(900);
         conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
         if (conn.getContentLengthLong() > 2097152L) {
            throw new IOException("Input too long to trust!");
         } else {
            InputStream input = conn.getInputStream();
            InputStream var23 = new BufferedInputStream(input);
            DigestInputStream digestInput = new DigestInputStream(var23, digestMD5);
            BufferedOutputStream output = new BufferedOutputStream(
               new FileOutputStream(FabricLoader.getInstance().getGameDir().resolve("xaero_autoupdater.jar").toFile())
            );
            download(output, digestInput, true);
            byte[] digest = digestMD5.digest();
            String fileMD5 = Hex.encodeHexString(digest);
            if (!jarMD5.equals(fileMD5)) {
               MinimapLogs.LOGGER.info("Invalid autoupdater MD5: " + fileMD5);
               return false;
            } else {
               ArrayList<String> command = new ArrayList<>();
               Path javaPath = new File(System.getProperty("java.home")).toPath().resolve("bin").resolve("java");
               command.add(javaPath.toString());
               command.add("-jar");
               command.add("./xaero_autoupdater.jar");
               command.add("6");
               command.add(Patreon.getUpdateLocation());

               for (int i = 0; i < Patreon.getOutdatedMods().size(); i++) {
                  PatreonMod m = (PatreonMod)Patreon.getOutdatedMods().get(i);
                  if (m.modJar != null) {
                     int canonicalPathAttempts = 10;
                     String jarPath = null;

                     while (canonicalPathAttempts-- > 0) {
                        try {
                           jarPath = m.modJar.getCanonicalPath();
                           break;
                        } catch (IOException var21) {
                           MinimapLogs.LOGGER.info("IO exception fetching the canonical path to the mod jar!");
                           if (canonicalPathAttempts == 0) {
                              throw var21;
                           }

                           MinimapLogs.LOGGER.error("suppressed exception", var21);
                           MinimapLogs.LOGGER.info("Retrying... (" + canonicalPathAttempts + ")");

                           try {
                              Thread.sleep(25L);
                           } catch (InterruptedException var19) {
                           }
                        }
                     }

                     command.add(jarPath);
                     command.add(m.latestVersionLayout);
                     command.add(m.currentVersion.split("_")[1]);
                     command.add(m.latestVersion);
                     command.add(m.currentVersion.split("_")[0]);
                     command.add(m.md5 == null ? "null" : m.md5);
                  }
               }

               MinimapLogs.LOGGER.info(String.join(", ", command));
               Runtime.getRuntime().exec(command.toArray(new String[0]));
               return true;
            }
         }
      } catch (IOException var22) {
         MinimapLogs.LOGGER.error("suppressed exception", var22);
         return false;
      }
   }
}
