package xaero.common.minimap.write;

import java.util.List;
import net.minecraft.class_1058;
import net.minecraft.class_1087;
import net.minecraft.class_1921;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_4696;
import net.minecraft.class_773;
import net.minecraft.class_777;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.minimap.highlight.HighlighterRegistry;

public class MinimapWriterFabric extends MinimapWriter {
   public MinimapWriterFabric(
      IXaeroMinimap modMain, XaeroMinimapSession minimapSession, BlockStateShortShapeCache blockStateShortShapeCache, HighlighterRegistry highlighterRegistry
   ) {
      super(modMain, minimapSession, blockStateShortShapeCache, highlighterRegistry);
   }

   @Override
   protected boolean blockStateHasTranslucentRenderType(class_2680 blockState) {
      return class_4696.method_23679(blockState) == class_1921.method_23583();
   }

   @Override
   protected int getBlockStateLightEmission(class_2680 state, class_1937 world, class_2338 pos) {
      return state.method_26213();
   }

   @Override
   protected List<class_777> getQuads(class_1087 model, class_2680 state, class_2350 direction) {
      return model.method_4707(state, direction, this.usedRandom);
   }

   @Override
   protected class_1058 getParticleIcon(class_773 bms, class_1087 model, class_2680 state) {
      return bms.method_3339(state);
   }
}
