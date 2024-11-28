package xaero.hud.minimap.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class InfoDisplayManager {
   private final Map<String, InfoDisplay<?>> displays = new HashMap<>();
   private final List<String> defaultOrder = new ArrayList<>();
   private List<String> order;

   public void add(InfoDisplay<?> infoDisplay) {
      if (this.displays.put(infoDisplay.getId(), infoDisplay) == null) {
         this.defaultOrder.add(infoDisplay.getId());
      }
   }

   public void setOrder(List<String> order) {
      this.order = new ArrayList<>(order);
      int lastDefaultOrderIdIndex = -1;

      for (int i = 0; i < this.defaultOrder.size(); i++) {
         String defaultOrderId = this.defaultOrder.get(i);
         int defaultOrderIdIndex = this.order.indexOf(defaultOrderId);
         if (defaultOrderIdIndex == -1) {
            if (lastDefaultOrderIdIndex != -1) {
               defaultOrderIdIndex = lastDefaultOrderIdIndex + 1;
            } else {
               defaultOrderIdIndex = 0;
            }

            this.order.add(defaultOrderIdIndex, defaultOrderId);
         }

         lastDefaultOrderIdIndex = defaultOrderIdIndex;
      }
   }

   public InfoDisplay<?> get(String id) {
      return this.displays.get(id);
   }

   public Stream<InfoDisplay<?>> getOrderedStream() {
      Stream<InfoDisplay<?>> unfilteredResult = this.order.stream().map(this.displays::get);
      return unfilteredResult.filter(Objects::nonNull);
   }

   public int getCount() {
      return this.displays.size();
   }

   public void reset() {
      this.setOrder(new ArrayList<>());
      this.getOrderedStream().forEach(InfoDisplay::reset);
   }

   public static final class Builder {
      private Builder() {
      }

      private InfoDisplayManager.Builder setDefault() {
         return this;
      }

      public InfoDisplayManager build() {
         return new xaero.common.minimap.info.InfoDisplayManager();
      }

      public static InfoDisplayManager.Builder begin() {
         return new InfoDisplayManager.Builder().setDefault();
      }
   }
}
