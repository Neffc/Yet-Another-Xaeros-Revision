package xaero.common.minimap.info.codec;

import java.util.function.Function;

@Deprecated
public class InfoDisplayStateCodec<T> extends xaero.hud.minimap.info.codec.InfoDisplayStateCodec<T> {
   @Deprecated
   public InfoDisplayStateCodec(Function<String, T> decoder, Function<T, String> encoder) {
      super(decoder, encoder);
   }

   @Deprecated
   @Override
   public T decode(String string) {
      return super.decode(string);
   }

   @Deprecated
   @Override
   public String encode(T value) {
      return super.encode(value);
   }
}
