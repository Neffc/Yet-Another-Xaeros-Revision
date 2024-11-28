package xaero.common.minimap.info;

import java.util.List;
import java.util.stream.Stream;

@Deprecated
public class InfoDisplayManager extends xaero.hud.minimap.info.InfoDisplayManager {
   @Deprecated
   public void add(InfoDisplay<?> infoDisplay) {
      super.add(infoDisplay);
   }

   @Deprecated
   @Override
   public void setOrder(List<String> order) {
      super.setOrder(order);
   }

   @Deprecated
   public InfoDisplay<?> get(String id) {
      return (InfoDisplay<?>)super.get(id);
   }

   @Deprecated
   public Stream<InfoDisplay<?>> getStream() {
      return super.getOrderedStream();
   }

   @Deprecated
   @Override
   public int getCount() {
      return super.getCount();
   }

   @Deprecated
   @Override
   public void reset() {
      super.reset();
   }
}
