package xaero.common.core;

import org.joml.Matrix4f;

public class XaeroMinimapCoreFabric {
   private static boolean renderingWorld = false;

   public static void beforeRenderWorld() {
      renderingWorld = true;
   }

   public static void onResetProjectionMatrix(Matrix4f matrixIn) {
      if (renderingWorld) {
         XaeroMinimapCore.onProjectionMatrix(matrixIn);
         renderingWorld = false;
      }
   }
}
