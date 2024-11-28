package xaero.common.misc;

public class KeySortableByOther<T> implements Comparable<KeySortableByOther> {
   private T key;
   private Comparable[] dataToSortBy;

   public KeySortableByOther(T key, Comparable... dataToSortBy) {
      this.key = key;
      this.dataToSortBy = dataToSortBy;
   }

   public T getKey() {
      return this.key;
   }

   public Comparable[] getDataToSortBy() {
      return this.dataToSortBy;
   }

   public int compareTo(KeySortableByOther arg0) {
      Comparable[] otherData = arg0.getDataToSortBy();

      for (int i = 0; i < this.dataToSortBy.length; i++) {
         int comparison = this.dataToSortBy[i].compareTo(otherData[i]);
         if (comparison != 0) {
            return comparison;
         }
      }

      return 0;
   }
}
