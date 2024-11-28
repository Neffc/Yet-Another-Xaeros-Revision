package xaero.common.minimap.info.render.compile;

import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplay;
import xaero.hud.minimap.BuiltInHudModules;

@Deprecated
public class InfoDisplayCompiler extends xaero.hud.minimap.info.render.compile.InfoDisplayCompiler {
   @Deprecated
   public <T> List<class_2561> compile(
      InfoDisplay<T> infoDisplay,
      XaeroMinimapSession minimapSession,
      MinimapProcessor minimap,
      int x,
      int y,
      int width,
      int height,
      double scale,
      int size,
      int playerBlockX,
      int playerBlockY,
      int playerBlockZ,
      class_2338 playerPos
   ) {
      return super.compile(infoDisplay, minimapSession.getSession(BuiltInHudModules.MINIMAP), size, playerPos);
   }

   @Deprecated
   public void addWords(int size, String text) {
      int sizeBU = this.size;
      this.size = size;
      super.addWords(text);
      this.size = sizeBU;
   }

   @Deprecated
   @Override
   public void addLine(class_2561 line) {
      super.addLine(line);
   }

   @Deprecated
   @Override
   public void addLine(String line) {
      super.addLine(line);
   }
}
