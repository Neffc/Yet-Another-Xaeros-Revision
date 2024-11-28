package xaero.common.mods.pac.highlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.highlight.ChunkHighlighter;
import xaero.common.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.common.misc.TextSplitter;
import xaero.common.settings.ModSettings;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientDimensionClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientRegionClaimsAPI;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;
import xaero.pac.common.server.player.config.PlayerConfig;

public class ClaimsHighlighter extends ChunkHighlighter {
   private final IClientClaimsManagerAPI<IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>>, IClientDimensionClaimsManagerAPI<IClientRegionClaimsAPI>> claimsManager;
   private final IXaeroMinimap modMain;
   private final ModSettings settings;
   private List<class_2561> cachedTooltip;
   private IPlayerChunkClaimAPI cachedTooltipFor;
   private int cachedForWidth;
   private String cachedForCustomName;
   private int cachedForClaimsColor;

   public ClaimsHighlighter(
      IXaeroMinimap modMain,
      IClientClaimsManagerAPI<IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>>, IClientDimensionClaimsManagerAPI<IClientRegionClaimsAPI>> claimsManager
   ) {
      super(true);
      this.modMain = modMain;
      this.settings = modMain.getSettings();
      this.claimsManager = claimsManager;
   }

   @Override
   public boolean regionHasHighlights(class_5321<class_1937> dimension, int regionX, int regionZ) {
      IClientDimensionClaimsManagerAPI<IClientRegionClaimsAPI> claimsDimension = this.claimsManager.getDimension(dimension.method_29177());
      return claimsDimension == null ? false : claimsDimension.getRegion(regionX, regionZ) != null;
   }

   @Override
   protected int[] getColors(class_5321<class_1937> dimension, int chunkX, int chunkZ) {
      if (!this.settings.getDisplayClaims()) {
         return null;
      } else {
         IPlayerChunkClaimAPI currentClaim = this.claimsManager.get(dimension.method_29177(), chunkX, chunkZ);
         if (currentClaim == null) {
            return null;
         } else {
            IPlayerChunkClaimAPI topClaim = this.claimsManager.get(dimension.method_29177(), chunkX, chunkZ - 1);
            IPlayerChunkClaimAPI rightClaim = this.claimsManager.get(dimension.method_29177(), chunkX + 1, chunkZ);
            IPlayerChunkClaimAPI bottomClaim = this.claimsManager.get(dimension.method_29177(), chunkX, chunkZ + 1);
            IPlayerChunkClaimAPI leftClaim = this.claimsManager.get(dimension.method_29177(), chunkX - 1, chunkZ);
            IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo = this.claimsManager
               .getPlayerInfo(currentClaim.getPlayerId());
            int claimColor = this.getClaimsColor(currentClaim, claimInfo);
            int claimColorFormatted = (claimColor & 0xFF) << 24 | (claimColor >> 8 & 0xFF) << 16 | (claimColor >> 16 & 0xFF) << 8;
            int borderOpacity = this.settings.getClaimsBorderOpacity();
            int fillOpacity = this.settings.getClaimsFillOpacity();
            int centerColor = claimColorFormatted | 255 * fillOpacity / 100;
            int sideColor = claimColorFormatted | 255 * borderOpacity / 100;
            this.resultStore[0] = centerColor;
            this.resultStore[1] = topClaim != currentClaim ? sideColor : centerColor;
            this.resultStore[2] = rightClaim != currentClaim ? sideColor : centerColor;
            this.resultStore[3] = bottomClaim != currentClaim ? sideColor : centerColor;
            this.resultStore[4] = leftClaim != currentClaim ? sideColor : centerColor;
            return this.resultStore;
         }
      }
   }

   @Override
   public boolean chunkIsHighlit(class_5321<class_1937> dimension, int chunkX, int chunkZ) {
      return this.claimsManager.get(dimension.method_29177(), chunkX, chunkZ) != null;
   }

   @Override
   public void addChunkHighlightTooltips(InfoDisplayCompiler compiler, class_5321<class_1937> dimension, int chunkX, int chunkZ, int width) {
      if (this.settings.displayCurrentClaim) {
         IPlayerChunkClaimAPI currentClaim = this.claimsManager.get(dimension.method_29177(), chunkX, chunkZ);
         if (currentClaim != null) {
            UUID currentClaimId = currentClaim.getPlayerId();
            IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo = this.claimsManager.getPlayerInfo(currentClaimId);
            String customName = this.getClaimsName(currentClaim, claimInfo);
            int actualClaimsColor = this.getClaimsColor(currentClaim, claimInfo);
            int claimsColor = actualClaimsColor | 0xFF000000;
            if (!Objects.equals(currentClaim, this.cachedTooltipFor)
               || this.cachedForWidth != width
               || this.cachedForClaimsColor != claimsColor
               || !Objects.equals(customName, this.cachedForCustomName)) {
               class_5250 tooltip = class_2561.method_43470("□ ").method_27694(s -> s.method_36139(claimsColor));
               if (Objects.equals(currentClaimId, PlayerConfig.SERVER_CLAIM_UUID)) {
                  tooltip.method_10855()
                     .add(
                        class_2561.method_43469(
                              "gui.xaero_pac_server_claim_tooltip",
                              new Object[]{currentClaim.isForceloadable() ? class_2561.method_43471("gui.xaero_pac_marked_for_forceload") : ""}
                           )
                           .method_27692(class_124.field_1068)
                     );
               } else if (Objects.equals(currentClaimId, PlayerConfig.EXPIRED_CLAIM_UUID)) {
                  tooltip.method_10855()
                     .add(
                        class_2561.method_43469(
                              "gui.xaero_pac_expired_claim_tooltip",
                              new Object[]{currentClaim.isForceloadable() ? class_2561.method_43471("gui.xaero_pac_marked_for_forceload") : ""}
                           )
                           .method_27692(class_124.field_1068)
                     );
               } else {
                  tooltip.method_10855()
                     .add(
                        class_2561.method_43469(
                              "gui.xaero_pac_claim_tooltip",
                              new Object[]{
                                 claimInfo.getPlayerUsername(),
                                 currentClaim.isForceloadable() ? class_2561.method_43471("gui.xaero_pac_marked_for_forceload") : ""
                              }
                           )
                           .method_27692(class_124.field_1068)
                     );
               }

               if (!customName.isEmpty()) {
                  tooltip.method_10855()
                     .add(0, class_2561.method_43470(class_1074.method_4662(customName, new Object[0]) + " - ").method_27692(class_124.field_1068));
               }

               this.cachedTooltip = new ArrayList<>();
               TextSplitter.splitTextIntoLines(this.cachedTooltip, width, width, tooltip, null);
               this.cachedTooltipFor = currentClaim;
               this.cachedForWidth = width;
               this.cachedForCustomName = customName;
               this.cachedForClaimsColor = claimsColor;
            }

            for (int i = 0; i < this.cachedTooltip.size(); i++) {
               compiler.addLine(this.cachedTooltip.get(i));
            }
         }
      }
   }

   private String getClaimsName(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo) {
      int subConfigIndex = currentClaim.getSubConfigIndex();
      String customName = claimInfo.getClaimsName(subConfigIndex);
      if (subConfigIndex != -1 && customName == null) {
         customName = claimInfo.getClaimsName();
      }

      return customName;
   }

   private int getClaimsColor(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo) {
      int subConfigIndex = currentClaim.getSubConfigIndex();
      Integer actualClaimsColor = claimInfo.getClaimsColor(subConfigIndex);
      if (subConfigIndex != -1 && actualClaimsColor == null) {
         actualClaimsColor = claimInfo.getClaimsColor();
      }

      return actualClaimsColor;
   }
}
