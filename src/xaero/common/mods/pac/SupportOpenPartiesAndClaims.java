package xaero.common.mods.pac;

import java.util.Iterator;
import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import xaero.common.AXaeroMinimap;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.mods.pac.highlight.ClaimsHighlighter;
import xaero.common.mods.pac.party.OPACPlayerTrackerSystem;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientDimensionClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientRegionClaimsAPI;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;
import xaero.pac.client.parties.party.api.IClientPartyMemberDynamicInfoSyncableStorageAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;
import xaero.pac.client.player.config.api.IPlayerConfigStringableOptionClientStorageAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;
import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

public class SupportOpenPartiesAndClaims {
   private final AXaeroMinimap modMain;
   private final OpenPACClientAPI api;
   private final IClientClaimsManagerAPI<IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>>, IClientDimensionClaimsManagerAPI<IClientRegionClaimsAPI>> claimsManager;
   private final IClientPartyStorageAPI<IClientPartyAPI<IPartyMemberAPI, IPartyPlayerInfoAPI, IPartyAllyAPI>, IClientPartyMemberDynamicInfoSyncableStorageAPI<IPartyMemberDynamicInfoSyncableAPI>> partyStorage;
   private final IPlayerConfigClientStorageManagerAPI<IPlayerConfigClientStorageAPI<IPlayerConfigStringableOptionClientStorageAPI<?>>> playerConfigs;

   public SupportOpenPartiesAndClaims(AXaeroMinimap modMain) {
      this.modMain = modMain;
      this.api = OpenPACClientAPI.get();
      this.claimsManager = this.api.getClaimsManager();
      this.partyStorage = this.api.getClientPartyStorage();
      this.playerConfigs = this.api.getPlayerConfigClientStorageManager();
   }

   public void register() {
      this.claimsManager.getTracker().register(new ClientClaimChangeListener());
      this.modMain.getPlayerTrackerSystemManager().register("openpartiesandclaims", new OPACPlayerTrackerSystem(this));
   }

   public IPlayerChunkClaimAPI claimAt(class_2960 dimension, int chunkX, int chunkZ) {
      return this.claimsManager.get(dimension, chunkX, chunkZ);
   }

   public void onMapRender(
      class_310 mc,
      class_4587 matrixStack,
      int scaledMouseX,
      int scaledMouseY,
      float partialTicks,
      class_2960 dimension,
      int highlightChunkX,
      int highlightChunkZ
   ) {
   }

   public boolean isFromParty(UUID playerId) {
      IClientPartyAPI<IPartyMemberAPI, IPartyPlayerInfoAPI, IPartyAllyAPI> party = this.partyStorage.getParty();
      return party == null ? false : this.partyStorage.getParty().getMemberInfo(playerId) != null;
   }

   public void registerHighlighters(HighlighterRegistry highlightRegistry) {
      highlightRegistry.register(new ClaimsHighlighter(this.modMain, this.claimsManager));
   }

   public AXaeroMinimap getModMain() {
      return this.modMain;
   }

   public Iterator<IPartyMemberDynamicInfoSyncableAPI> getAllyIterator() {
      return this.partyStorage.getPartyMemberDynamicInfoSyncableStorage().getAllStream().iterator();
   }
}
