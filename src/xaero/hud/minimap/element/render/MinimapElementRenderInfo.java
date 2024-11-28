package xaero.hud.minimap.element.render;

import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_276;

public class MinimapElementRenderInfo {
   public final MinimapElementRenderLocation location;
   public final class_1297 renderEntity;
   public final class_1657 player;
   public final class_243 renderPos;
   public final boolean cave;
   public final float partialTicks;
   public final class_276 framebuffer;

   public MinimapElementRenderInfo(
      MinimapElementRenderLocation location,
      class_1297 renderEntity,
      class_1657 player,
      class_243 renderPos,
      boolean cave,
      float partialTicks,
      class_276 framebuffer
   ) {
      this.location = location;
      this.renderEntity = renderEntity;
      this.player = player;
      this.renderPos = renderPos;
      this.cave = cave;
      this.partialTicks = partialTicks;
      this.framebuffer = framebuffer;
   }
}
