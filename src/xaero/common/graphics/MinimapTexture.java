package xaero.common.graphics;

import com.mojang.blaze3d.platform.TextureUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.class_1049;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import org.lwjgl.BufferUtils;

public class MinimapTexture extends class_1049 {
   public ByteBuffer buffer = BufferUtils.createByteBuffer(786432);
   boolean loaded = false;

   public void loadIfNeeded() throws IOException {
      if (!this.loaded) {
         this.method_4625(class_310.method_1551().method_1478());
         this.loaded = true;
      }
   }

   public MinimapTexture(class_2960 location) throws IOException {
      super(location);
   }

   public void method_4625(class_3300 resourceManager_1) throws IOException {
      TextureUtil.prepareImage(this.method_4624(), 0, 512, 512);
   }
}
