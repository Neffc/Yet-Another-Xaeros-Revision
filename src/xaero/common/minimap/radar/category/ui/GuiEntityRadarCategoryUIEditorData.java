package xaero.common.minimap.radar.category.ui;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import xaero.common.category.ui.data.GuiCategoryUIEditorAdderData;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorFilterCategoryData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.common.minimap.radar.category.ui.data.GuiEntityRadarCategoryUIEditorSettingsData;

public final class GuiEntityRadarCategoryUIEditorData
   extends GuiCategoryUIEditorFilterCategoryData<class_1297, class_1657, EntityRadarCategory, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorData> {
   protected GuiEntityRadarCategoryUIEditorData(
      @Nonnull GuiEntityRadarCategoryUIEditorSettingsData<?> settingOverrides,
      @Nonnull List<GuiEntityRadarCategoryUIEditorData> subCategories,
      @Nonnull GuiCategoryUIEditorAdderData topAdder,
      @Nonnull Function<GuiCategoryUIEditorAdderData, GuiEntityRadarCategoryUIEditorData> newCategorySupplier,
      boolean movable,
      int subIndex,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(settingOverrides, subCategories, topAdder, newCategorySupplier, movable, subIndex, listEntryFactory, tooltipSupplier);
   }

   public static final class Builder
      extends GuiCategoryUIEditorFilterCategoryData.Builder<class_1297, class_1657, EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder> {
      public Builder() {
         super(EntityRadarCategoryConstants.LIST_FACTORY, GuiEntityRadarCategoryUIEditorSettingsData.Builder.getDefault());
      }

      public GuiEntityRadarCategoryUIEditorData.Builder setDefault() {
         super.setDefault();
         this.setNewCategorySupplier(ad -> getDefault().setName(ad.getNameField().getResult()).build());
         return this;
      }

      protected GuiEntityRadarCategoryUIEditorData buildInternally() {
         return new GuiEntityRadarCategoryUIEditorData(
            this.settingsDataBuilder.build(),
            this.buildSubCategories(),
            this.topAdderBuilder.build(),
            this.newCategorySupplier,
            this.movable,
            this.subIndex,
            this.listEntryFactory,
            this.tooltipSupplier
         );
      }

      public static GuiEntityRadarCategoryUIEditorData.Builder getDefault() {
         return new GuiEntityRadarCategoryUIEditorData.Builder().setDefault();
      }
   }
}
