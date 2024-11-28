package xaero.hud.minimap.waypoint.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointVisibilityType;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.DestinationHandler;
import xaero.hud.minimap.world.MinimapWorld;

public abstract class AbstractWaypointRenderProvider<C extends AbstractWaypointRenderContext> extends MinimapElementRenderProvider<Waypoint, C> {
   private final List<Waypoint> collectingList = new ArrayList<>();
   private Iterator<Waypoint> iterator;
   private boolean deathpoints;
   private DestinationHandler destinationHandler;
   public final Predicate<Waypoint> filter = w -> {
      if (w.isDisabled()) {
         return false;
      } else if (w.getVisibility() == WaypointVisibilityType.WORLD_MAP_LOCAL) {
         return false;
      } else {
         return w.getVisibility() == WaypointVisibilityType.WORLD_MAP_GLOBAL ? false : this.deathpoints || !w.getPurpose().isDeath();
      }
   };

   public void begin(MinimapElementRenderLocation location, C context) {
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      this.collectingList.clear();
      session.getWaypointSession().getCollector().collect(this.collectingList);
      ModSettings settings = HudMod.INSTANCE.getSettings();
      MinimapWorld currentWorld = session.getWorldManager().getCurrentWorld();
      this.destinationHandler = session.getWaypointSession().getDestinationHandler();
      class_1297 renderEntity = class_310.method_1551().method_1560();
      this.destinationHandler.begin(renderEntity, currentWorld, settings.renderAllSets, settings.deleteReachedDeathpoints);
      this.deathpoints = settings.getDeathpoints();
      class_4184 activeRender = class_310.method_1551().field_1773.method_19418();
      class_243 cameraPos = activeRender.method_19326();
      context.dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(currentWorld);
      double cameraPosMultiplier = class_310.method_1551().field_1687.method_8597().comp_646() / context.dimCoordinateScale;
      Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * cameraPosMultiplier, cameraPos.field_1351, cameraPos.field_1350 * cameraPosMultiplier);
      this.iterator = this.collectingList.stream().filter(this.filter).sorted().iterator();
   }

   public boolean hasNext(MinimapElementRenderLocation location, C context) {
      return this.iterator.hasNext();
   }

   public Waypoint getNext(MinimapElementRenderLocation location, C context) {
      Waypoint result = this.iterator.next();
      this.destinationHandler.handle(result);
      return result;
   }

   public void end(MinimapElementRenderLocation location, C context) {
      this.iterator = null;
      this.deathpoints = false;
      this.destinationHandler.end();
      this.destinationHandler = null;
   }

   public Waypoint setupContextAndGetNext(MinimapElementRenderLocation location, C context) {
      return this.getNext(location, context);
   }
}
