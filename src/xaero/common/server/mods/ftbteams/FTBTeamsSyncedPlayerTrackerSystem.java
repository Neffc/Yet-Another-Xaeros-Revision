package xaero.common.server.mods.ftbteams;

import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.Team;
import dev.ftb.mods.ftbteams.data.TeamRank;
import net.minecraft.class_1657;
import xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem;

public class FTBTeamsSyncedPlayerTrackerSystem implements ISyncedPlayerTrackerSystem {
   @Override
   public int getTrackingLevel(class_1657 tracker, class_1657 tracked) {
      if (FTBTeamsAPI.arePlayersInSameTeam(tracker.method_5667(), tracked.method_5667())) {
         return 2;
      } else {
         Team trackerTeam = FTBTeamsAPI.getPlayerTeam(tracker.method_5667());
         if (trackerTeam == null) {
            return 0;
         } else {
            Team trackedTeam = FTBTeamsAPI.getPlayerTeam(tracked.method_5667());
            if (trackedTeam == null) {
               return 0;
            } else {
               return trackerTeam.getHighestRank(tracked.method_5667()) == TeamRank.ALLY && trackedTeam.getHighestRank(tracker.method_5667()) == TeamRank.ALLY
                  ? 1
                  : 0;
            }
         }
      }
   }

   @Override
   public boolean isPartySystem() {
      return true;
   }
}
