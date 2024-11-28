package xaero.common.patreon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.Cipher;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_742;
import xaero.common.IXaeroMinimap;
import xaero.common.patreon.decrypt.DecryptInputStream;
import xaero.common.platform.Services;
import xaero.hud.minimap.MinimapLogs;

public class Patreon {
   private static boolean hasAutoUpdates;
   private static int onlineWidgetLevel;
   private static boolean notificationDisplayed;
   private static boolean loaded = false;
   private static String updateLocation;
   private static HashMap<String, Object> mods = new HashMap<>();
   private static ArrayList<Object> outdatedMods = new ArrayList<>();
   private static Cipher cipher = null;
   private static int KEY_VERSION = 4;
   private static String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBeELcruvAEIeLF/UsWF/v5rxyRXIpCs+eORLCbDw5cz9jHsnoypQKx0RTk5rcXIeA0HbEfY0eREB25quHjhZKul7MnzotQT+F2Qb1bPfHa6+SPie+pj79GGGAFP3npki6RqoU/wyYkd1tOomuD8v5ytEkOPC4U42kxxvx23A7vH6w46dew/E/HvfbBvZF2KrqdJtwKAunk847C3FgyhVq8/vzQc6mqAW6Mmn4zlwFvyCnTOWjIRw/I93WIM/uvhE3lt6pmtrWA2yIbKIj1z4pgG/K72EqHfYLGkBFTh7fV1wwCbpNTXZX2JnTfmvMGqzHjq7FijwVfCpFB/dWR3wQIDAQAB";
   private static File optionsFile;

   public static void checkPatreon() {
      checkPatreon(null);
   }

   public static void checkPatreon(IXaeroMinimap modMain) {
      if (modMain == null || modMain.getSettings().allowInternetAccess) {
         synchronized (mods) {
            if (!loaded) {
               loadSettings();
               String s = "http://data.chocolateminecraft.com/Versions_" + KEY_VERSION + "/Patreon2.dat";
               s = s.replaceAll(" ", "%20");

               try {
                  URL url = new URL(s);
                  URLConnection conn = url.openConnection();
                  conn.setReadTimeout(900);
                  conn.setConnectTimeout(900);
                  if (conn.getContentLengthLong() > 524288L) {
                     throw new IOException("Input too long to trust!");
                  }

                  BufferedReader reader = new BufferedReader(new InputStreamReader(new DecryptInputStream(conn.getInputStream(), cipher)));
                  boolean parsingPatrons = false;
                  String localPlayerName = class_310.method_1551().method_1548().method_1677().getName();

                  String line;
                  while ((line = reader.readLine()) != null && !line.equals("LAYOUTS")) {
                     if (line.startsWith("PATREON")) {
                        parsingPatrons = true;
                     } else if (parsingPatrons) {
                        String[] rewards = line.split(";");
                        if (rewards.length > 1 && rewards[0].equalsIgnoreCase(localPlayerName)) {
                           for (int i = 1; i < rewards.length; i++) {
                              String rewardString = rewards[i].trim();
                              if ("updates".equals(rewardString)) {
                                 hasAutoUpdates = true;
                              } else {
                                 String[] keyAndValue = rewardString.split(":");
                                 if (keyAndValue.length >= 2 && keyAndValue[0].equals("widget_level")) {
                                    try {
                                       onlineWidgetLevel = Integer.parseInt(keyAndValue[1]);
                                    } catch (NumberFormatException var21) {
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  updateLocation = reader.readLine();

                  while ((line = reader.readLine()) != null) {
                     String[] args = line.split("\\t");
                     mods.put(args[0], new PatreonMod(args[0], args[1], args[2], args[3]));
                  }

                  reader.close();
               } catch (IOException var22) {
                  MinimapLogs.LOGGER.warn("io exception while checking patreon: {}", var22.getMessage());
                  mods.clear();
               } catch (Throwable var23) {
                  MinimapLogs.LOGGER.error("suppressed exception", var23);
                  mods.clear();
               } finally {
                  loaded = true;
               }
            }
         }
      }
   }

   public static void addOutdatedMod(Object mod) {
      synchronized (getOutdatedMods()) {
         getOutdatedMods().add(mod);
      }
   }

   @Deprecated
   public static int getPatronPledge(String name) {
      return -1;
   }

   public static void saveSettings() {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(optionsFile));
         writer.close();
      } catch (IOException var2) {
         MinimapLogs.LOGGER.error("suppressed exception", var2);
      }
   }

   public static void loadSettings() {
      try {
         if (!optionsFile.exists()) {
            saveSettings();
            return;
         }

         BufferedReader reader = new BufferedReader(new FileReader(optionsFile));

         String line;
         while ((line = reader.readLine()) != null) {
            String[] var2 = line.split(":");
         }

         reader.close();
      } catch (IOException var3) {
         MinimapLogs.LOGGER.error("suppressed exception", var3);
      }
   }

   @Deprecated
   public static class_2960 getPlayerCape(String modID, class_742 playerEntity) {
      return null;
   }

   @Deprecated
   public static Boolean isWearingCape(String modID, class_742 playerEntity) {
      return null;
   }

   public static ArrayList<Object> getOutdatedMods() {
      return outdatedMods;
   }

   public static boolean needsNotification() {
      return !notificationDisplayed && !outdatedMods.isEmpty();
   }

   @Deprecated
   public static int getPatronPledge() {
      return -1;
   }

   @Deprecated
   public static void setPatronPledge(int patronPledge) {
   }

   public static String getPublicKeyString2() {
      return publicKeyString;
   }

   public static boolean isNotificationDisplayed() {
      return notificationDisplayed;
   }

   public static void setNotificationDisplayed(boolean notificationDisplayed) {
      Patreon.notificationDisplayed = notificationDisplayed;
   }

   public static HashMap<String, Object> getMods() {
      return mods;
   }

   public static String getUpdateLocation() {
      return updateLocation;
   }

   @Deprecated
   public static boolean isShowCapes() {
      return false;
   }

   @Deprecated
   public static void setShowCapes(boolean showCapes) {
   }

   public static int getKEY_VERSION2() {
      return KEY_VERSION;
   }

   @Deprecated
   public static String getRendersCapes() {
      return "";
   }

   @Deprecated
   public static void setRendersCapes(String rendersCapes) {
   }

   public static boolean getHasAutoUpdates() {
      return hasAutoUpdates;
   }

   public static int getOnlineWidgetLevel() {
      return onlineWidgetLevel;
   }

   static {
      try {
         cipher = Cipher.getInstance("RSA");
         KeyFactory factory = KeyFactory.getInstance("RSA");
         byte[] byteKey = Base64.getDecoder().decode(getPublicKeyString2().getBytes());
         X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
         PublicKey publicKey = factory.generatePublic(X509publicKey);
         cipher.init(2, publicKey);
      } catch (Exception var4) {
         cipher = null;
         MinimapLogs.LOGGER.error("suppressed exception", var4);
      }

      optionsFile = Services.PLATFORM.getGameDir().resolve("config").resolve("xaeropatreon.txt").toFile();
   }
}
