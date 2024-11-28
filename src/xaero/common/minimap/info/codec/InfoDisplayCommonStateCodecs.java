package xaero.common.minimap.info.codec;

@Deprecated
public class InfoDisplayCommonStateCodecs {
   @Deprecated
   public static final InfoDisplayStateCodec<Boolean> BOOLEAN = new InfoDisplayStateCodec<>(
      xaero.hud.minimap.info.codec.InfoDisplayCommonStateCodecs.BOOLEAN::decode, xaero.hud.minimap.info.codec.InfoDisplayCommonStateCodecs.BOOLEAN::encode
   );
   @Deprecated
   public static final InfoDisplayStateCodec<Integer> INTEGER = new InfoDisplayStateCodec<>(
      xaero.hud.minimap.info.codec.InfoDisplayCommonStateCodecs.INTEGER::decode, xaero.hud.minimap.info.codec.InfoDisplayCommonStateCodecs.INTEGER::encode
   );
}
