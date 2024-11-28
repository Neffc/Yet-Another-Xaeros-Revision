package xaero.common.minimap.render.radar;

import java.lang.reflect.Field;
import xaero.common.MinimapLogs;

public class ResolvedFieldModelRootPathListener implements EntityIconModelFieldResolver.Listener {
   private Object resolvedObject;
   private boolean stop;
   private boolean failed;

   public void prepare() {
      this.resolvedObject = null;
      this.stop = false;
      this.failed = false;
   }

   @Override
   public boolean isFieldAllowed(Field f) {
      return true;
   }

   @Override
   public boolean shouldStop() {
      return this.stop;
   }

   @Override
   public void onFieldResolved(Object[] resolved, String matchedFilterElement) {
      this.stop = true;
      if (resolved.length > 1) {
         MinimapLogs.LOGGER.info("Referencing multiple objects in the model root path is not allowed: " + matchedFilterElement);
         this.failed = true;
      } else {
         this.resolvedObject = resolved[0];
      }
   }

   public Object getCurrentNode() {
      return this.resolvedObject;
   }

   public boolean failed() {
      return this.failed;
   }
}
