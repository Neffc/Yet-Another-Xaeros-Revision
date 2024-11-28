package xaero.common.minimap.radar.tracker.system;

import java.util.UUID;
import net.minecraft.class_2960;

public interface ITrackedPlayerReader<P> {
   UUID getId(P var1);

   double getX(P var1);

   double getY(P var1);

   double getZ(P var1);

   class_2960 getDimension(P var1);
}
