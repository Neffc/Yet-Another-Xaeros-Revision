package xaero.common.resource;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import xaero.common.graphics.shader.MinimapShaders;

public class ShaderResourceReloadListener implements SimpleSynchronousResourceReloadListener {
   private class_2960 listenerId = new class_2960("xaerominimap", "shader_reload");

   public class_2960 getFabricId() {
      return this.listenerId;
   }

   public void method_14491(class_3300 manager) {
      MinimapShaders.onResourceReload(manager);
   }
}
