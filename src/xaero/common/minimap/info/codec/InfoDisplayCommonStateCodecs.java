package xaero.common.minimap.info.codec;

public class InfoDisplayCommonStateCodecs {
   public static final InfoDisplayStateCodec<Boolean> BOOLEAN = new InfoDisplayStateCodec<>(s -> s.equals("true"), Object::toString);
   public static final InfoDisplayStateCodec<Integer> INTEGER = new InfoDisplayStateCodec<>(s -> Integer.parseInt(s), Object::toString);
}
