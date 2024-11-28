package xaero.common.category.ui.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_5251;
import xaero.common.category.ObjectCategory;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.entry.CategorySettingsListEntryCategory;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public abstract class GuiCategoryUIEditorCategoryData<C extends ObjectCategory<?, C>, SD extends GuiCategoryUIEditorSettingsData<?>, ED extends GuiCategoryUIEditorCategoryData<C, SD, ED>>
   extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   private final ED self = (ED)this;
   private boolean cut;
   private final List<ED> subCategories;
   private final GuiCategoryUIEditorAdderData topAdder;
   private final Function<GuiCategoryUIEditorAdderData, ED> newCategorySupplier;
   private final SD settingsData;

   protected GuiCategoryUIEditorCategoryData(
      @Nonnull SD settingData,
      @Nonnull List<ED> subCategories,
      @Nonnull GuiCategoryUIEditorAdderData topAdder,
      @Nonnull Function<GuiCategoryUIEditorAdderData, ED> newCategorySupplier,
      boolean movable,
      int subIndex,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.settingsData = settingData;
      this.subCategories = subCategories;
      this.topAdder = topAdder;
      this.newCategorySupplier = newCategorySupplier;
   }

   public SD getSettingsData() {
      return this.settingsData;
   }

   public final List<ED> getSubCategories() {
      return this.subCategories;
   }

   public String getName() {
      return this.settingsData.getNameOption().getResult();
   }

   @Override
   public String getDisplayName() {
      return this.getName();
   }

   private BiConsumer<GuiCategoryUIEditorAdderData, Integer> getAdderHandler() {
      return (adder, i) -> {
         if (adder.isConfirmed()) {
            ED newCategory = this.newCategorySupplier.apply(adder);
            this.subCategories.add(i, newCategory);
            adder.reset();
         }
      };
   }

   private Runnable getDeletionHandler() {
      return () -> {
         Iterator<ED> subIterator = this.subCategories.iterator();

         while (subIterator.hasNext()) {
            ED subCategory = subIterator.next();
            if (subCategory.getSettingsData().isToBeDeleted()) {
               subIterator.remove();
            }
         }
      };
   }

   public Supplier<Boolean> getMoveAction(int subIndex, int direction, GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
      return () -> {
         int newSlot = subIndex + direction;
         ED subCategoryToMove = this.subCategories.get(subIndex);
         if (newSlot < 0) {
            this.subCategories.remove(subCategoryToMove);
            this.subCategories.add(subCategoryToMove);
         } else if (newSlot >= this.subCategories.size()) {
            this.subCategories.remove(subCategoryToMove);
            this.subCategories.add(0, subCategoryToMove);
         } else {
            rowList.restoreScrollAfterUpdate();
            ED subCategoryToReplace = this.subCategories.get(newSlot);
            this.subCategories.set(subIndex, subCategoryToReplace);
            this.subCategories.set(newSlot, subCategoryToMove);
         }

         rowList.setLastExpandedData(subCategoryToMove);
         return true;
      };
   }

   public Supplier<Boolean> getDuplicateAction(int subIndex, GuiCategorySettings<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
      return () -> {
         if (subIndex >= 0 && subIndex < this.subCategories.size()) {
            ED subCategoryToDuplicate = this.subCategories.get(subIndex);
            GuiCategorySettings screenToRestore = (GuiCategorySettings)class_310.method_1551().field_1755;
            class_310.method_1551()
               .method_1507(
                  new class_410(
                     result -> {
                        if (result) {
                           C convertedCategory = rowList.getDataConverter().convert(subCategoryToDuplicate);
                           ED reconstructedEditorData = rowList.getDataConverter().convert(convertedCategory, false);
                           reconstructedEditorData.removeProtectionRecursive();
                           this.subCategories.add(subIndex + 1, reconstructedEditorData);
                           class_310.method_1551().method_1507(screenToRestore);
                           GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList newRowList = screenToRestore.getRowList();
                           newRowList.setLastExpandedData(reconstructedEditorData);
                           newRowList.updateEntries();
                        } else {
                           class_310.method_1551().method_1507(screenToRestore);
                        }
                     },
                     class_2561.method_43471("gui.xaero_category_duplicate_confirm"),
                     class_2561.method_43471(subCategoryToDuplicate.getDisplayName())
                        .method_27696(class_2583.field_24360.method_27703(class_5251.method_27718(class_124.field_1054)))
                  )
               );
            return true;
         } else {
            return false;
         }
      };
   }

   public Supplier<Boolean> getCutAction(ED parent, GuiCategorySettings<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
      return () -> {
         rowList.setCutCategory(this.self, parent);
         rowList.setLastExpandedData(this);
         rowList.restoreScrollAfterUpdate();
         return true;
      };
   }

   public Supplier<Boolean> getPasteAction(GuiCategorySettings<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
      return () -> {
         rowList.pasteTo(this.self);
         rowList.restoreScrollAfterUpdate();
         return true;
      };
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      BiConsumer<GuiCategoryUIEditorAdderData, Integer> adderHandler = this.getAdderHandler();
      adderHandler.accept(this.topAdder, 0);
      this.getDeletionHandler().run();
      List<GuiCategoryUIEditorExpandableData<?>> result = new ArrayList<>(this.subCategories);
      result.add(0, this.topAdder);
      result.add(0, this.settingsData);
      return result;
   }

   public void removeProtectionRecursive() {
      this.getSettingsData().setProtected(false);

      for (ED sub : this.subCategories) {
         sub.removeProtectionRecursive();
      }
   }

   public abstract static class Builder<C extends ObjectCategory<?, C>, ED extends GuiCategoryUIEditorCategoryData<C, SD, ED>, SD extends GuiCategoryUIEditorSettingsData<?>, SDB extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB>, EDB extends GuiCategoryUIEditorCategoryData.Builder<C, ED, SD, SDB, EDB>>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, EDB> {
      protected final EDB self;
      protected String name;
      protected final SDB settingsDataBuilder;
      protected final List<EDB> subCategoryBuilders;
      protected final ListFactory listFactory;
      protected final GuiCategoryUIEditorAdderData.Builder topAdderBuilder;
      protected Function<GuiCategoryUIEditorAdderData, ED> newCategorySupplier;
      protected int subIndex;

      public Builder(ListFactory listFactory, SDB settingsDataBuilder) {
         if (settingsDataBuilder == null) {
            throw new IllegalStateException("settings data builder cannot be null!");
         } else {
            this.settingsDataBuilder = settingsDataBuilder;
            this.subCategoryBuilders = listFactory.get();
            this.listFactory = listFactory;
            this.topAdderBuilder = GuiCategoryUIEditorAdderData.Builder.getDefault(listFactory);
            this.self = (EDB)this;
         }
      }

      public EDB setDefault() {
         super.setDefault();
         this.setName(null);
         this.settingsDataBuilder.setDefault();
         this.subCategoryBuilders.clear();
         this.topAdderBuilder.setDisplayName(class_1074.method_4662("gui.xaero_category_add_subcategory", new Object[0]));
         this.setMovable(true);
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryCategory(
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  (GuiCategoryUIEditorCategoryData<?, ?, ?>)data,
                  (GuiCategoryUIEditorCategoryData<?, ?, ?>)parent,
                  data.getTooltipSupplier(parent),
                  isFinalExpanded
               )
         );
         this.setSubIndex(0);
         this.setTooltipSupplier(
            (parent, data) -> {
               CursorBox tooltip = new CursorBox(
                  class_1074.method_4662("gui.xaero_box_category", new Object[]{class_1074.method_4662(data.getDisplayName(), new Object[0])})
               );
               tooltip.setAutoLinebreak(false);
               return () -> tooltip;
            }
         );
         return this.self;
      }

      public EDB setNewCategorySupplier(Function<GuiCategoryUIEditorAdderData, ED> newCategorySupplier) {
         this.newCategorySupplier = newCategorySupplier;
         return this.self;
      }

      public EDB setSubIndex(int subIndex) {
         this.subIndex = subIndex;
         return this.self;
      }

      public EDB setName(String name) {
         this.name = name;
         return this.self;
      }

      public SDB getSettingDataBuilder() {
         return this.settingsDataBuilder;
      }

      public EDB addSubCategoryBuilder(EDB subCategory) {
         subCategory.setSubIndex(this.subCategoryBuilders.size());
         this.subCategoryBuilders.add(subCategory);
         return this.self;
      }

      protected List<ED> buildSubCategories() {
         return this.subCategoryBuilders.stream().map(GuiCategoryUIEditorCategoryData.Builder::build).collect(this.listFactory::get, List::add, List::addAll);
      }

      public ED build() {
         if (this.name != null && this.newCategorySupplier != null) {
            this.settingsDataBuilder.getNameOptionBuilder().setInput(this.name);
            this.settingsDataBuilder.getNameOptionBuilder().setDisplayName(class_1074.method_4662("gui.xaero_category_name", new Object[0]));
            this.settingsDataBuilder.getNameOptionBuilder().setMaxLength(200);
            return (ED)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }
   }
}
