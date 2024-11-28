package xaero.common.category.ui.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import net.minecraft.class_5251;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorSimpleButtonData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingData;
import xaero.common.category.ui.data.options.range.setting.IGuiCategoryUIEditorSettingDataBuilder;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListEntryWidget;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButton;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;

public class GuiCategoryUIEditorSettingsData<SETTING_DATA extends GuiCategoryUIEditorOptionsData<?> & IGuiCategoryUIEditorSettingData<?>>
   extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   private final Map<ObjectCategorySetting<?>, SETTING_DATA> settings;
   private final List<SETTING_DATA> settingList;
   private boolean toBeDeleted;
   private final GuiCategoryUIEditorSimpleButtonData deleteButton;
   private final GuiCategoryUIEditorSimpleButtonData protectionButton;
   private final GuiCategoryUIEditorTextFieldOptionsData nameOption;
   private final ListFactory listFactory;
   private final boolean rootSettings;
   private boolean protection;

   protected GuiCategoryUIEditorSettingsData(
      @Nonnull Map<ObjectCategorySetting<?>, SETTING_DATA> settings,
      @Nonnull List<SETTING_DATA> settingList,
      @Nonnull GuiCategoryUIEditorSimpleButtonData deleteButton,
      @Nonnull GuiCategoryUIEditorSimpleButtonData protectionButton,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData nameOption,
      @Nonnull ListFactory listFactory,
      boolean rootSettings,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      boolean protection
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.settings = settings;
      this.settingList = settingList;
      this.listFactory = listFactory;
      this.rootSettings = rootSettings;
      this.deleteButton = deleteButton;
      this.protectionButton = protectionButton;
      this.nameOption = nameOption;
      this.protection = protection;
   }

   public Map<ObjectCategorySetting<?>, SETTING_DATA> getSettings() {
      return this.settings;
   }

   public IGuiCategoryUIEditorSettingData<?> getSettingData(ObjectCategorySetting<?> setting) {
      return this.settings.get(setting);
   }

   public boolean isRootSettings() {
      return this.rootSettings;
   }

   public boolean isToBeDeleted() {
      return this.toBeDeleted;
   }

   public void setToBeDeleted() {
      this.toBeDeleted = true;
   }

   public boolean getProtection() {
      return this.protection;
   }

   public void setProtected(boolean protection) {
      this.protection = protection;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      List<GuiCategoryUIEditorExpandableData<?>> result = this.listFactory.get();
      result.addAll(this.settingList);
      if (!this.protection) {
         result.add(this.nameOption);
      }

      result.add(this.deleteButton);
      result.add(this.protectionButton);
      return result;
   }

   @Override
   public String getDisplayName() {
      return class_1074.method_4662("gui.xaero_category_settings", new Object[0]);
   }

   public GuiCategoryUIEditorTextFieldOptionsData getNameOption() {
      return this.nameOption;
   }

   public abstract static class Builder<SD extends GuiCategoryUIEditorSettingsData<?>, SDB extends GuiCategoryUIEditorSettingsData.Builder<SD, SDB>>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorSettingsData.Builder<SD, SDB>> {
      protected final SDB self = (SDB)this;
      protected final Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingDataBuilder<?, ?>> settingMap;
      protected final List<IGuiCategoryUIEditorSettingDataBuilder<?, ?>> settingList;
      protected final GuiCategoryUIEditorSimpleButtonData.Builder deleteButtonBuilder;
      protected final GuiCategoryUIEditorSimpleButtonData.Builder protectionButtonBuilder;
      protected final GuiCategoryUIEditorTextFieldOptionsData.Builder nameOptionBuilder;
      protected final MapFactory mapFactory;
      protected final ListFactory listFactory;
      protected boolean rootSettings;
      protected boolean protection;

      protected Builder(MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings) {
         this.settingMap = mapFactory.get();
         this.settingList = listFactory.get();
         this.deleteButtonBuilder = GuiCategoryUIEditorSimpleButtonData.Builder.getDefault();
         this.protectionButtonBuilder = GuiCategoryUIEditorSimpleButtonData.Builder.getDefault();
         this.nameOptionBuilder = GuiCategoryUIEditorTextFieldOptionsData.Builder.getDefault(listFactory);
         this.mapFactory = mapFactory;
         this.listFactory = listFactory;

         for (ObjectCategorySetting<?> setting : allSettings) {
            this.addSetting(setting);
         }
      }

      private <V> void addSetting(ObjectCategorySetting<V> setting) {
         IGuiCategoryUIEditorSettingDataBuilder<V, ?> builder = setting.getSettingUIType()
            .getSettingDataBuilderFactory()
            .<V>apply(this.listFactory)
            .setSetting(setting);
         this.settingMap.put(setting, builder);
         this.settingList.add(builder);
      }

      public SDB setDefault() {
         super.setDefault();

         for (IGuiCategoryUIEditorSettingDataBuilder<?, ?> builder : this.settingList) {
            builder.setSettingValue(null);
         }

         this.setRootSettings(false);
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryWidget<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsButton(parent, () -> data.getDisplayName(), true, 216, 20, b -> data.getExpandAction(rowList).run(), rowList),
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               )
         );
         this.nameOptionBuilder.setDefault();
         this.deleteButtonBuilder
            .setDefault()
            .setDisplayName(class_1074.method_4662("gui.xaero_category_delete", new Object[0]))
            .setCallback(
               (parent, bd, rl) -> {
                  GuiCategoryUIEditorSettingsData<?> settings = (GuiCategoryUIEditorSettingsData<?>)parent;
                  class_310 mc = class_310.method_1551();
                  class_437 configScreen = mc.field_1755;
                  mc.method_1507(
                     new class_410(
                        result -> {
                           if (result) {
                              settings.setToBeDeleted();
                           }

                           mc.method_1507(configScreen);
                        },
                        class_2561.method_43471("gui.xaero_category_delete_confirm"),
                        class_2561.method_43471(settings.getNameOption().getResult())
                           .method_27696(class_2583.field_24360.method_27703(class_5251.method_27718(class_124.field_1061)))
                     )
                  );
               }
            )
            .setIsActiveSupplier((parent, data) -> !((GuiCategoryUIEditorSettingsData)parent).getProtection());
         this.protectionButtonBuilder
            .setDefault()
            .setDisplayName("")
            .setCallback(
               (parent, bd, rl) -> {
                  GuiCategoryUIEditorSettingsData<?> settings = (GuiCategoryUIEditorSettingsData<?>)parent;
                  boolean currentlyProtected = settings.getProtection();
                  class_310 mc = class_310.method_1551();
                  class_437 configScreen = mc.field_1755;
                  mc.method_1507(
                     new class_410(
                        result -> {
                           if (result) {
                              settings.setProtected(!settings.getProtection());
                           }

                           mc.method_1507(configScreen);
                        },
                        class_2561.method_43471(
                           currentlyProtected ? "gui.xaero_category_disable_protection_confirm" : "gui.xaero_category_enable_protection_confirm"
                        ),
                        class_2561.method_43471(settings.getNameOption().getResult())
                           .method_27696(
                              class_2583.field_24360.method_27703(class_5251.method_27718(currentlyProtected ? class_124.field_1061 : class_124.field_1060))
                           )
                     )
                  );
               }
            )
            .setMessageSupplier(
               (parent, bd) -> () -> ((GuiCategoryUIEditorSettingsData)parent).getProtection()
                        ? class_1074.method_4662("gui.xaero_category_disable_protection", new Object[0])
                        : class_1074.method_4662("gui.xaero_category_enable_protection", new Object[0])
            )
            .setIsActiveSupplier((parent, bd) -> !((GuiCategoryUIEditorSettingsData)parent).isRootSettings())
            .setTooltipSupplier((parent, bd) -> {
               CursorBox protectionTooltip = new CursorBox(class_1074.method_4662("gui.xaero_box_category_protection", new Object[0]));
               return () -> protectionTooltip;
            });
         this.setTooltipSupplier(
            (parent, data) -> {
               if (parent instanceof GuiCategoryUIEditorCategoryData<?, ?, ?> category) {
                  CursorBox tooltip = new CursorBox(
                     class_1074.method_4662("gui.xaero_box_category_settings", new Object[]{class_1074.method_4662(category.getDisplayName(), new Object[0])})
                  );
                  tooltip.setAutoLinebreak(false);
                  return () -> tooltip;
               } else {
                  return null;
               }
            }
         );
         return this.self;
      }

      public <T> SDB setSettingValue(ObjectCategorySetting<T> setting, T value) {
         IGuiCategoryUIEditorSettingDataBuilder<T, ?> settingBuilder = (IGuiCategoryUIEditorSettingDataBuilder<T, ?>)this.settingMap.get(setting);
         settingBuilder.setSettingValue(value);
         return this.self;
      }

      public SDB setRootSettings(boolean rootSettings) {
         this.rootSettings = rootSettings;
         return this.self;
      }

      public SDB setProtection(boolean protection) {
         this.protection = protection;
         return this.self;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder getNameOptionBuilder() {
         return this.nameOptionBuilder;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder getDeleteButtonBuilder() {
         return this.deleteButtonBuilder;
      }

      @Override
      protected GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> buildInternally() {
         if (this.nameOptionBuilder.needsInputStringValidator()) {
            this.nameOptionBuilder.setInputStringValidator(s -> true);
         }

         List<IGuiCategoryUIEditorSettingData<?>> builtSettingData = this.settingList
            .stream()
            .map(b -> b.setRootSettings(this.rootSettings))
            .map(IGuiCategoryUIEditorSettingDataBuilder::build)
            .collect(this.listFactory::get, List::add, List::addAll);
         Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingData<?>> builtSettingsDataMap = this.mapFactory.get();

         for (IGuiCategoryUIEditorSettingData<?> sd : builtSettingData) {
            if (!(sd instanceof GuiCategoryUIEditorOptionsData)) {
               throw new IllegalStateException("illegal setting data class! " + sd.getClass());
            }

            builtSettingsDataMap.put(sd.getSetting(), sd);
         }

         return this.buildInternally(builtSettingData, builtSettingsDataMap);
      }

      protected abstract SD buildInternally(
         List<IGuiCategoryUIEditorSettingData<?>> var1, Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingData<?>> var2
      );
   }

   public static final class FinalBuilder
      extends GuiCategoryUIEditorSettingsData.Builder<GuiCategoryUIEditorSettingsData<?>, GuiCategoryUIEditorSettingsData.FinalBuilder> {
      protected FinalBuilder(MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings) {
         super(mapFactory, listFactory, allSettings);
      }

      @Override
      protected GuiCategoryUIEditorSettingsData<?> buildInternally(
         List<IGuiCategoryUIEditorSettingData<?>> builtSettingData, Map<ObjectCategorySetting<?>, IGuiCategoryUIEditorSettingData<?>> builtSettingsDataMap
      ) {
         return new GuiCategoryUIEditorSettingsData(
            builtSettingsDataMap,
            builtSettingData,
            this.deleteButtonBuilder.build(),
            this.protectionButtonBuilder.build(),
            this.nameOptionBuilder.build(),
            this.listFactory,
            this.rootSettings,
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.protection
         );
      }
   }
}
