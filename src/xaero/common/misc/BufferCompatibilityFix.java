package xaero.common.misc;

import java.nio.Buffer;

public class BufferCompatibilityFix {
   public static Buffer clear(Buffer buffer) {
      return buffer.clear();
   }

   public static Buffer flip(Buffer buffer) {
      return buffer.flip();
   }

   public static Buffer position(Buffer buffer, int pos) {
      return buffer.position(pos);
   }

   public static Buffer limit(Buffer buffer, int limit) {
      return buffer.limit(limit);
   }

   public static Buffer mark(Buffer buffer) {
      return buffer.mark();
   }

   public static Buffer reset(Buffer buffer) {
      return buffer.reset();
   }

   public static Buffer rewind(Buffer buffer) {
      return buffer.rewind();
   }
}
