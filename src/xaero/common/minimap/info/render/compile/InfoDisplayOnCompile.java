package xaero.common.minimap.info.render.compile;

import net.minecraft.class_2338;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplay;

@FunctionalInterface
public interface InfoDisplayOnCompile<T> {
   void onCompile(
      InfoDisplay<T> var1,
      InfoDisplayCompiler var2,
      XaeroMinimapSession var3,
      MinimapProcessor var4,
      int var5,
      int var6,
      int var7,
      int var8,
      double var9,
      int var11,
      int var12,
      int var13,
      int var14,
      class_2338 var15
   );
}
