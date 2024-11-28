package xaero.common.mods;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2378;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_5321;
import net.minecraft.class_7924;
import xaero.common.MinimapLogs;
import xaero.common.misc.Misc;

public class SupportFramedBlocks {
   private Class<?> framedTileBlockClass;
   private Method framedTileEntityCamoStateMethod;
   private Method framedTileEntityCamoMethod;
   private Method camoContainerStateMethod;
   private boolean usable;
   private Set<class_2248> framedBlocks;

   public SupportFramedBlocks() {
      try {
         this.framedTileBlockClass = Class.forName("xfacthd.framedblocks.common.tileentity.FramedTileEntity");
      } catch (ClassNotFoundException var8) {
         try {
            this.framedTileBlockClass = Class.forName("xfacthd.framedblocks.api.block.FramedBlockEntity");
         } catch (ClassNotFoundException var7) {
            MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", var7);
            return;
         }
      }

      try {
         this.framedTileEntityCamoStateMethod = this.framedTileBlockClass.getDeclaredMethod("getCamoState");
      } catch (SecurityException | NoSuchMethodException var6) {
         try {
            Class<?> camoContainerClass;
            try {
               camoContainerClass = Class.forName("xfacthd.framedblocks.api.data.CamoContainer");
            } catch (ClassNotFoundException var4) {
               camoContainerClass = Class.forName("xfacthd.framedblocks.api.camo.CamoContainer");
            }

            this.framedTileEntityCamoMethod = this.framedTileBlockClass.getDeclaredMethod("getCamo");
            this.camoContainerStateMethod = camoContainerClass.getDeclaredMethod("getState");
         } catch (NoSuchMethodException | SecurityException | ClassNotFoundException var5) {
            MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", var6);
            MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", var5);
         }
      }

      this.usable = this.framedTileBlockClass != null
         && (this.framedTileEntityCamoStateMethod != null || this.framedTileEntityCamoMethod != null && this.camoContainerStateMethod != null);
   }

   public void onWorldChange() {
      this.framedBlocks = null;
   }

   private void findFramedBlocks(class_1937 world, class_2378<class_2248> registry) {
      if (this.framedBlocks == null) {
         this.framedBlocks = new HashSet<>();
         if (registry == null) {
            registry = world.method_30349().method_30530(class_7924.field_41254);
         }

         registry.method_29722().forEach(entry -> {
            class_5321<class_2248> key = (class_5321<class_2248>)entry.getKey();
            if (key.method_29177().method_12836().equals("framedblocks") && key.method_29177().method_12832().startsWith("framed_")) {
               this.framedBlocks.add((class_2248)entry.getValue());
            }
         });
      }
   }

   public boolean isFrameBlock(class_1937 world, class_2378<class_2248> registry, class_2680 state) {
      if (!this.usable) {
         return false;
      } else {
         this.findFramedBlocks(world, registry);
         return this.framedBlocks.contains(state.method_26204());
      }
   }

   public class_2680 unpackFramedBlock(class_1937 world, class_2378<class_2248> registry, class_2680 original, class_2586 tileEntity) {
      if (!this.usable) {
         return original;
      } else if (this.framedTileBlockClass.isAssignableFrom(tileEntity.getClass())) {
         if (this.framedTileEntityCamoStateMethod != null) {
            return Misc.getReflectMethodValue(tileEntity, this.framedTileEntityCamoStateMethod);
         } else {
            Object camoContainer = Misc.getReflectMethodValue(tileEntity, this.framedTileEntityCamoMethod);
            return Misc.getReflectMethodValue(camoContainer, this.camoContainerStateMethod);
         }
      } else {
         return original;
      }
   }
}
