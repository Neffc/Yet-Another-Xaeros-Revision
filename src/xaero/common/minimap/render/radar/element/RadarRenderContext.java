package xaero.common.minimap.render.radar.element;

import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_327;
import net.minecraft.class_4588;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.render.MinimapRendererHelper;

public final class RadarRenderContext {
   public MinimapRadar minimapRadar;
   public class_4588 dotsBufferBuilder;
   public class_4588 nameBgBuilder;
   public MultiTextureRenderTypeRenderer iconsRenderer;
   public double nameScale;
   public boolean smoothDots;
   public boolean debugEntityIcons;
   public boolean debugEntityVariantIds;
   public int dotsStyle;
   public boolean reversedOrder;
   public class_1921 dotsRenderType;
   public EntityRadarCategory entityCategory;
   public double iconScale;
   public int dotSize;
   public int heightLimit;
   public boolean heightBasedFade;
   public int startFadingAt;
   public boolean displayNameWhenIconFails;
   public boolean alwaysNameTags;
   public int colorIndex;
   public int displayY;
   public boolean namesForList;
   public boolean iconsForList;
   public boolean name;
   public boolean icon;
   public class_1297 renderEntity;
   public class_327 font;
   public MinimapRendererHelper helper;
}
