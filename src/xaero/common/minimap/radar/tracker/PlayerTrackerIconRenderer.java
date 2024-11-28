package xaero.common.minimap.radar.tracker;

import net.minecraft.class_1044;
import net.minecraft.class_1657;
import net.minecraft.class_1664;
import net.minecraft.class_287;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_922;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;

public class PlayerTrackerIconRenderer {
   public void renderIcon(
      class_310 mc,
      MultiTextureRenderTypeRenderer renderer,
      MinimapRendererHelper helper,
      class_4587 matrixStack,
      class_1657 player,
      class_2960 skinTextureLocation
   ) {
      boolean upsideDown = player != null && class_922.method_38563(player);
      int textureY = 8 + (!upsideDown ? 8 : 0);
      int textureH = 8 * (!upsideDown ? -1 : 1);
      class_1044 texture = mc.method_1531().method_4619(skinTextureLocation);
      if (texture != null) {
         int textureId = texture.method_4624();
         class_287 bufferbuilder = renderer.begin(textureId);
         helper.addTexturedColoredRectToExistingBuffer(
            matrixStack.method_23760().method_23761(), bufferbuilder, -4.0F, -4.0F, 8, textureY, 8, 8, 8, textureH, 1.0F, 1.0F, 1.0F, 1.0F, 64.0F
         );
         if (player != null && player.method_7348(class_1664.field_7563)) {
            textureY = 8 + (!upsideDown ? 8 : 0);
            textureH = 8 * (!upsideDown ? -1 : 1);
            helper.addTexturedColoredRectToExistingBuffer(
               matrixStack.method_23760().method_23761(), bufferbuilder, -4.0F, -4.0F, 40, textureY, 8, 8, 8, textureH, 1.0F, 1.0F, 1.0F, 1.0F, 64.0F
            );
         }
      }
   }
}
