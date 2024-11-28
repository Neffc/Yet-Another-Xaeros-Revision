package xaero.common.interfaces.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.shader.MinimapShaders;
import xaero.common.gui.GuiEditMode;
import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceInstance;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.pushbox.PushBox;
import xaero.common.misc.Misc;

public class InterfaceRenderer {
   public static final class_2960 guiTextures = new class_2960("xaerobetterpvp", "gui/guis.png");
   public static final class_2960 minimapFrameTextures = new class_2960("xaerobetterpvp", "gui/minimap_frame.png");
   private CustomVertexConsumers customVertexConsumers;
   private IXaeroMinimap modMain;
   private final int disabled = 1354612157;
   private final int enabled = 1694498815;
   private final int selected = -2097152001;

   public InterfaceRenderer(IXaeroMinimap modMain) {
      this.modMain = modMain;
      this.customVertexConsumers = new CustomVertexConsumers();
   }

   public void renderInterfaces(XaeroMinimapSession minimapSession, class_332 guiGraphics, float partial) {
      guiGraphics.method_51452();

      while (GL11.glGetError() != 0) {
      }

      GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      MinimapShaders.ensureShaders();
      class_310 mc = class_310.method_1551();
      int width = mc.method_22683().method_4486();
      int height = mc.method_22683().method_4502();
      double scale = mc.method_22683().method_4495();
      int mouseX = (int)(Misc.getMouseX(mc, false) * (double)width / (double)mc.method_22683().method_4489());
      int mouseY = (int)(Misc.getMouseY(mc, false) * (double)height / (double)mc.method_22683().method_4506());
      InterfaceManager interfaceManager = this.modMain.getInterfaces();
      interfaceManager.updateInterfaces(minimapSession, mouseX, mouseY, width, height, scale);
      Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();

      while (iter.hasNext()) {
         Interface l = iter.next();
         InterfaceInstance ii = minimapSession.getInterfaceInstances().get(l);
         ii.prePotentialRender();
         if (this.modMain.getSettings().getBooleanValue(l.getOption())) {
            int xBU = l.getX();
            int yBU = l.getY();
            interfaceManager.pushInterface(l, ii, scale, width, height);
            ii.render(guiGraphics, width, height, scale, partial, this.customVertexConsumers);
            l.setX(xBU);
            l.setY(yBU);
         }
      }

      for (PushBox pb : interfaceManager.pushBoxes) {
         pb.postUpdate();
      }

      RenderSystem.enableDepthTest();
   }

   public void renderBoxes(class_332 guiGraphics, int mouseX, int mouseY, int width, int height, double d) {
      if (this.modMain.getEvents().getLastGuiOpen() instanceof GuiEditMode) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         int mouseOverId = this.modMain.getInterfaces().getInterfaceId(minimapSession, mouseX, mouseY, width, height, d);
         InterfaceManager interfaces = this.modMain.getInterfaces();
         Iterator<Interface> iter = interfaces.getInterfaceIterator();
         int i = -1;

         while (iter.hasNext()) {
            i++;
            Interface l = iter.next();
            if (this.modMain.getSettings().getBooleanValue(l.getOption())) {
               InterfaceInstance li = minimapSession.getInterfaceInstances().get(l);
               int x = l.getX();
               int y = l.getY();
               int w = li.getW(d);
               int h = li.getH(d);
               int x2 = x + w;
               int y2 = y + h;
               if (interfaces.getSelectedId() != i
                  && (interfaces.overAButton(mouseX, mouseY) || mouseX < x || mouseX > x2 || mouseY < y || mouseY > y2)
                  && i != interfaces.getDraggingId()) {
                  guiGraphics.method_25294(x, y, x2, y2, 1354612157);
               } else {
                  guiGraphics.method_25294(x, y, x2, y2, interfaces.getSelectedId() == i ? -2097152001 : 1694498815);
                  if (interfaces.getDraggingId() == -1 && i == mouseOverId) {
                     l.getcBox().drawBox(guiGraphics, mouseX, mouseY, width, height);
                  }
               }
            }
         }
      }
   }

   public CustomVertexConsumers getCustomVertexConsumers() {
      return this.customVertexConsumers;
   }
}
