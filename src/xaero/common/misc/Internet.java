package xaero.common.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.patreon.Patreon;
import xaero.common.patreon.decrypt.DecryptInputStream;
import xaero.common.settings.ModSettings;

public class Internet {
   public static Cipher cipher = null;

   public static void checkModVersion(AXaeroMinimap modMain) {
      if (modMain.getSettings().allowInternetAccess) {
         String s = modMain.getVersionsURL();
         s = s.replaceAll(" ", "%20");

         try {
            if (cipher == null) {
               throw new Exception("Cipher instance is null!");
            }

            URL url = new URL(s);
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(900);
            conn.setConnectTimeout(900);
            if (conn.getContentLengthLong() > 524288L) {
               throw new IOException("Input too long to trust!");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new DecryptInputStream(conn.getInputStream(), cipher), "UTF8"));
            String line = reader.readLine();
            if (line != null) {
               modMain.setMessage("§e§l" + line);
            }

            line = reader.readLine();
            if (line != null) {
               modMain.setNewestUpdateID(Integer.parseInt(line));
            }

            modMain.setOutdated(true);
            boolean versionFound = false;
            String[] current = modMain.getVersionID().split("_");

            while ((line = reader.readLine()) != null) {
               if (!line.startsWith("data_widget") || line.length() <= 11) {
                  if (!ModSettings.updateNotification || modMain.getNewestUpdateID() == ModSettings.ignoreUpdate) {
                     modMain.setOutdated(false);
                     break;
                  }

                  if (line.equals(modMain.getVersionID())) {
                     modMain.setOutdated(false);
                     break;
                  }

                  if (Patreon.getHasAutoUpdates()) {
                     if (versionFound) {
                        if (line.startsWith("meta;")) {
                           String[] metadata = line.substring(5).split(";");
                           modMain.setLatestVersionMD5(metadata[0]);
                        }

                        versionFound = false;
                     }

                     if (line.startsWith(current[0] + "_")) {
                        String[] args = line.split("_");
                        if (args.length == current.length) {
                           boolean sameType = true;
                           if (current.length > 2) {
                              for (int i = 2; i < current.length && sameType; i++) {
                                 if (!args[i].equals(current[i])) {
                                    sameType = false;
                                 }
                              }
                           }

                           if (sameType) {
                              modMain.setLatestVersion(args[1]);
                              versionFound = true;
                           }
                        }
                     }
                  }
               } else {
                  modMain.getWidgetLoader().loadWidget(line.substring(12));
               }
            }

            reader.close();
         } catch (Throwable var11) {
            MinimapLogs.LOGGER.error("suppressed exception", var11);
            modMain.setOutdated(false);
         }
      }
   }

   static {
      try {
         cipher = Cipher.getInstance("RSA");
         KeyFactory factory = KeyFactory.getInstance("RSA");
         byte[] byteKey = Base64.getDecoder().decode(Patreon.getPublicKeyString2().getBytes());
         X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
         PublicKey publicKey = factory.generatePublic(X509publicKey);
         cipher.init(2, publicKey);
      } catch (Exception var4) {
         cipher = null;
         MinimapLogs.LOGGER.error("suppressed exception", var4);
      }
   }
}
