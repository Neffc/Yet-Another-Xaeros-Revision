package xaero.hud.minimap.world;

import net.minecraft.class_1937;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5321;
import net.minecraft.class_746;
import net.minecraft.class_7924;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;

public class MinimapDimensionHelper {
   public double getDimensionDivision(MinimapWorld minimapWorld) {
      if (class_310.method_1551().field_1687 == null) {
         return 1.0;
      } else {
         double dimCoordinateScale = this.getDimCoordinateScale(minimapWorld);
         return class_310.method_1551().field_1687.method_8597().comp_646() / dimCoordinateScale;
      }
   }

   public double getDimCoordinateScale(MinimapWorld minimapWorld) {
      if (minimapWorld == null) {
         return 1.0;
      } else {
         MinimapWorldRootContainer rootContainer = minimapWorld.getContainer().getRoot();
         class_5321<class_1937> dimKey = minimapWorld.getDimId();
         return dimKey == null ? 1.0 : rootContainer.getDimensionScale(dimKey);
      }
   }

   public String getDimensionDirectoryName(class_5321<class_1937> dimKey) {
      if (dimKey == class_1937.field_25179) {
         return "dim%0";
      } else if (dimKey == class_1937.field_25180) {
         return "dim%-1";
      } else if (dimKey == class_1937.field_25181) {
         return "dim%1";
      } else {
         class_2960 identifier = dimKey.method_29177();
         return "dim%" + identifier.method_12836() + "$" + identifier.method_12832().replace('/', '%');
      }
   }

   public class_5321<class_1937> findDimensionKeyForOldName(class_746 player, String oldName) {
      for (class_5321<class_1937> dk : player.field_3944.method_29356()) {
         if (oldName.equals(dk.method_29177().method_12832().replaceAll("[^a-zA-Z0-9_]+", ""))) {
            return dk;
         }
      }

      return null;
   }

   public class_5321<class_1937> getDimensionKeyForDirectoryName(String dirName) {
      String dimIdPart = dirName.substring(4);
      if (dimIdPart.equals("0")) {
         return class_1937.field_25179;
      } else if (dimIdPart.equals("1")) {
         return class_1937.field_25181;
      } else if (dimIdPart.equals("-1")) {
         return class_1937.field_25180;
      } else {
         String[] idArgs = dimIdPart.split("\\$");
         return idArgs.length < 2 ? null : class_5321.method_29179(class_7924.field_41223, new class_2960(idArgs[0], idArgs[1].replace('%', '/')));
      }
   }
}
