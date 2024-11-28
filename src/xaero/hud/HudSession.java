package xaero.hud;

import java.io.IOException;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import xaero.common.HudMod;
import xaero.common.controls.ControlsHandler;
import xaero.common.controls.event.KeyEventHandler;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.minimap.MinimapLogs;

public class HudSession {
   protected final HudMod modMain;
   protected ControlsHandler controls;
   protected KeyEventHandler keyEventHandler;
   private final MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers;
   protected boolean usable;

   public HudSession(HudMod modMain) {
      this.modMain = modMain;
      this.multiTextureRenderTypeRenderers = new MultiTextureRenderTypeRendererProvider(2);
   }

   public void init(class_634 connection) throws IOException {
      this.keyEventHandler = new KeyEventHandler();
      this.modMain.getHud().getSessionHandler().resetSessions(this.modMain);
      this.usable = true;
      MinimapLogs.LOGGER.info("New Xaero hud session initialized!");
   }

   public final void tryCleanup() {
      try {
         this.cleanup();
         MinimapLogs.LOGGER.info("Xaero hud session finalized.");
      } catch (Throwable var2) {
         MinimapLogs.LOGGER.error("Xaero hud session failed to finalize properly.", var2);
      }

      this.usable = false;
   }

   protected void cleanup() {
      this.modMain.getHud().getSessionHandler().closeSessions(this.modMain);
   }

   public MultiTextureRenderTypeRendererProvider getMultiTextureRenderTypeRenderers() {
      return this.multiTextureRenderTypeRenderers;
   }

   public static HudSession getCurrentSession() {
      HudSession session = getForPlayer(class_310.method_1551().field_1724);
      if (session == null && XaeroMinimapCore.currentSession != null && XaeroMinimapCore.currentSession.usable) {
         session = XaeroMinimapCore.currentSession;
      }

      return session;
   }

   public static HudSession getForPlayer(class_746 player) {
      return player != null && player.field_3944 != null ? ((IXaeroMinimapClientPlayNetHandler)player.field_3944).getXaero_minimapSession() : null;
   }

   public HudMod getHudMod() {
      return this.modMain;
   }
}
