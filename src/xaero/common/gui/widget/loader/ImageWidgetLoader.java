package xaero.common.gui.widget.loader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import org.apache.commons.codec.binary.Hex;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import xaero.common.gui.widget.ImageWidgetBuilder;
import xaero.common.gui.widget.Widget;
import xaero.common.misc.Misc;
import xaero.common.platform.Services;
import xaero.hud.minimap.MinimapLogs;

public class ImageWidgetLoader extends ScalableWidgetLoader {
   @Override
   public Widget load(Map<String, String> parsedArgs) throws IOException {
      ImageWidgetBuilder builder = new ImageWidgetBuilder();
      this.commonLoad(builder, parsedArgs);
      String image = parsedArgs.get("image");
      String image_url = parsedArgs.get("image_url");
      int textureId = 0;
      if (image != null) {
         if (!image.replaceAll("[^a-zA-Z0-9_]+", "").equals(image)) {
            MinimapLogs.LOGGER.info("Invalid widget image id!");
            return null;
         }

         String image_md5 = parsedArgs.get("image_md5");
         if (image_md5 == null) {
            MinimapLogs.LOGGER.info("No image md5.");
            RenderSystem.bindTexture(0);
            GL11.glDeleteTextures(textureId);
            return null;
         }

         MessageDigest digestMD5;
         try {
            digestMD5 = MessageDigest.getInstance("MD5");
         } catch (NoSuchAlgorithmException var39) {
            MinimapLogs.LOGGER.info("No algorithm for MD5.");
            RenderSystem.bindTexture(0);
            GL11.glDeleteTextures(textureId);
            return null;
         }

         builder.setImageId(image);
         textureId = GL11.glGenTextures();
         if (textureId <= 0) {
            return null;
         }

         builder.setGlTexture(textureId);
         RenderSystem.bindTexture(textureId);
         String tex_base_level = parsedArgs.get("tex_base_level");
         String tex_max_level = parsedArgs.get("tex_max_level");
         String tex_min_lod = parsedArgs.get("tex_min_lod");
         String tex_max_lod = parsedArgs.get("tex_max_lod");
         String tex_lod_bias = parsedArgs.get("tex_lod_bias");
         String tex_mag_filter = parsedArgs.get("tex_mag_filter");
         String tex_min_filter = parsedArgs.get("tex_min_filter");
         String tex_wrap_s = parsedArgs.get("tex_wrap_s");
         String tex_wrap_t = parsedArgs.get("tex_wrap_t");
         GL11.glTexParameteri(3553, 33084, tex_base_level != null ? Integer.parseInt(tex_base_level) : 0);
         GL11.glTexParameteri(3553, 33085, tex_max_level != null ? Integer.parseInt(tex_max_level) : 1);
         GL11.glTexParameterf(3553, 33082, tex_min_lod != null ? Float.parseFloat(tex_min_lod) : 0.0F);
         GL11.glTexParameterf(3553, 33083, tex_max_lod != null ? Float.parseFloat(tex_max_lod) : 1.0F);
         GL11.glTexParameterf(3553, 34049, tex_lod_bias != null ? Float.parseFloat(tex_lod_bias) : 0.0F);
         GL11.glTexParameteri(3553, 10240, tex_mag_filter != null ? Integer.parseInt(tex_mag_filter) : 9728);
         GL11.glTexParameteri(3553, 10241, tex_min_filter != null ? Integer.parseInt(tex_min_filter) : 9729);
         GL11.glTexParameteri(3553, 10242, tex_wrap_s != null ? Integer.parseInt(tex_wrap_s) : '脯');
         GL11.glTexParameteri(3553, 10243, tex_wrap_t != null ? Integer.parseInt(tex_wrap_t) : '脯');
         File cacheFolder = Services.PLATFORM.getGameDir().resolve("XaeroCache").toFile();
         Path cacheFolderPath = cacheFolder.toPath();
         if (!Files.exists(cacheFolderPath)) {
            Files.createDirectories(cacheFolderPath);
         }

         class_2960 resourceLocation = new class_2960("xaerobetterpvp", "gui/" + image + ".png");
         InputStream inputStream = null;
         DigestInputStream digestStream = null;
         BufferedImage bufferedImage = null;

         try {
            try {
               class_3298 resource = (class_3298)class_310.method_1551().method_1478().method_14486(resourceLocation).get();
               inputStream = resource.method_14482();
            } catch (NoSuchElementException var41) {
               MinimapLogs.LOGGER.info("Widget image not included in jar. Checking cache...");
               Path cacheFilePath = cacheFolderPath.resolve(image + ".cache");
               if (!Files.exists(cacheFilePath)) {
                  MinimapLogs.LOGGER.info("Widget image not in cache. Downloading...");
                  if (image_url == null) {
                     MinimapLogs.LOGGER.info("No image URL.");
                     RenderSystem.bindTexture(0);
                     GL11.glDeleteTextures(textureId);
                     return null;
                  }

                  URL url = new URL(image_url);
                  HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                  conn.setReadTimeout(900);
                  conn.setConnectTimeout(900);
                  conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
                  if (conn.getContentLengthLong() > 5242880L) {
                     throw new IOException("Image too big to trust!");
                  }

                  InputStream input = null;
                  BufferedOutputStream output = null;

                  try {
                     input = conn.getInputStream();
                     output = new BufferedOutputStream(new FileOutputStream(cacheFilePath.toFile()));
                     Misc.download(output, input);
                  } finally {
                     if (input != null) {
                        input.close();
                     }

                     if (output != null) {
                        output.close();
                     }
                  }
               }

               inputStream = new FileInputStream(cacheFilePath.toFile());
            }

            inputStream = new BufferedInputStream(inputStream);
            digestStream = new DigestInputStream(inputStream, digestMD5);
            bufferedImage = ImageIO.read(digestStream);

            while (digestStream.read() != -1) {
            }

            byte[] digest = digestMD5.digest();
            String fileMD5 = Hex.encodeHexString(digest);
            if (!image_md5.equals(fileMD5)) {
               MinimapLogs.LOGGER.info("Invalid image MD5: " + fileMD5);
               bufferedImage.flush();
               bufferedImage = null;
            }
         } finally {
            if (inputStream != null) {
               inputStream.close();
            }

            if (digestStream != null) {
               digestStream.close();
            }
         }

         if (bufferedImage == null) {
            RenderSystem.bindTexture(0);
            GL11.glDeleteTextures(textureId);
            return null;
         }

         int imageW = bufferedImage.getWidth();
         int imageH = bufferedImage.getHeight();
         ByteBuffer buffer = BufferUtils.createByteBuffer(imageW * imageH * 4);

         for (int y = 0; y < imageH; y++) {
            for (int x = 0; x < imageW; x++) {
               int color = bufferedImage.getRGB(x, y);
               buffer.putInt(color);
            }
         }

         buffer.flip();
         bufferedImage.flush();
         GL11.glTexImage2D(3553, 0, 6408, imageW, imageH, 0, 32993, 33639, buffer);
         GL30.glGenerateMipmap(3553);
         RenderSystem.bindTexture(0);
         builder.setImageW(imageW);
         builder.setImageH(imageH);
      }

      if (builder.validate()) {
         return builder.build();
      } else {
         if (textureId > 0) {
            GL11.glDeleteTextures(textureId);
         }

         return null;
      }
   }
}
