package xaero.common.gui;

import com.google.common.base.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_3675.class_307;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.gui.widget.WidgetScreen;
import xaero.common.misc.KeySortableByOther;
import xaero.common.misc.Misc;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.MinimapLogs;

public abstract class GuiSettings extends ScreenBase implements WidgetScreen {
   protected int entriesPerPage = 12;
   protected ISettingEntry[] entries;
   protected String entryFilter = "";
   private boolean foundSomething;
   protected class_2561 screenTitle;
   protected int page;
   protected int maxPage;
   private MyTinyButton nextButton;
   private MyTinyButton prevButton;
   private class_342 searchField;
   protected boolean canSearch = true;
   private boolean shouldRefocusSearch;
   private boolean shouldSaveRadar;

   public GuiSettings(IXaeroMinimap modMain, class_2561 title, class_437 backScreen, class_437 escScreen) {
      super(modMain, backScreen, escScreen, title);
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.screenTitle = this.field_22785;
      this.method_37063(
         class_4185.method_46430(class_2561.method_43469("gui.xaero_back", new Object[0]), b -> this.goBack())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 6 + 168, 200, 20)
            .method_46431()
      );
      int verticalOffset = this.canSearch ? 24 : 0;
      if (this.entries != null) {
         boolean canEditIngameSettings = ModSettings.canEditIngameSettings();
         ArrayList<KeySortableByOther<ISettingEntry>> sortingList = new ArrayList<>();
         String comparisonFilter = this.entryFilter.toLowerCase();

         for (int i = 0; i < this.entries.length; i++) {
            ISettingEntry entry = this.entries[i];
            String entrySearchString = entry.getStringForSearch().toLowerCase();
            if (entrySearchString != null) {
               int positionInEntryString = entrySearchString.indexOf(comparisonFilter);
               if (positionInEntryString != -1) {
                  KeySortableByOther<ISettingEntry> sortableEntry = new KeySortableByOther<>(entry, positionInEntryString);
                  sortingList.add(sortableEntry);
               }
            }
         }

         ArrayList<ISettingEntry> filteredEntries = sortingList.stream()
            .sorted()
            .map(KeySortableByOther::getKey)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
         if (!filteredEntries.isEmpty()) {
            this.foundSomething = true;
            this.maxPage = (int)Math.ceil((double)filteredEntries.size() / (double)this.entriesPerPage) - 1;
            if (this.page > this.maxPage) {
               this.page = this.maxPage;
            }

            int firstEntry = this.entriesPerPage * this.page;
            int entryCount = Math.min(filteredEntries.size() - firstEntry, this.entriesPerPage);

            for (int ix = 0; ix < entryCount; ix++) {
               ISettingEntry entry = filteredEntries.get(firstEntry + ix);
               class_339 optionWidget = entry.createWidget(
                  this.field_22789 / 2 - 205 + ix % 2 * 210, this.field_22790 / 7 + verticalOffset + 24 * (ix >> 1), 200, canEditIngameSettings
               );
               this.method_37063(optionWidget);
               if (entry instanceof ConfigSettingEntry) {
                  ConfigSettingEntry settingEntry = (ConfigSettingEntry)entry;
                  if (settingEntry.usesWorldMapHardValue(this.modMain)) {
                     optionWidget.field_22763 = false;
                  }
               }
            }
         } else {
            this.foundSomething = false;
            this.page = 0;
            this.maxPage = 0;
         }
      }

      this.screenTitle = this.screenTitle.method_27662().method_27693(" (" + (this.page + 1) + "/" + (this.maxPage + 1) + ")");
      this.nextButton = new MyTinyButton(
         this.field_22789 / 2 + 131,
         this.field_22790 / 7 + 144 + verticalOffset,
         class_2561.method_43469("gui.xaero_next", new Object[0]),
         b -> this.onNextButton()
      );
      this.prevButton = new MyTinyButton(
         this.field_22789 / 2 - 205,
         this.field_22790 / 7 + 144 + verticalOffset,
         class_2561.method_43469("gui.xaero_previous", new Object[0]),
         b -> this.onPrevButton()
      );
      if (this.maxPage > 0) {
         this.method_37063(this.nextButton);
         this.method_37063(this.prevButton);
         this.nextButton.field_22763 = this.page < this.maxPage;
         this.prevButton.field_22763 = this.page > 0;
      }

      this.modMain.getWidgetScreenHandler().initialize(this, this.field_22789, this.field_22790);
      if (this.canSearch) {
         boolean shouldFocusSearch = this.shouldRefocusSearch;
         this.shouldRefocusSearch = false;
         int cursorPos = 0;
         if (shouldFocusSearch) {
            cursorPos = this.searchField.method_1881();
         }

         this.searchField = new class_342(
            this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 7, 200, 20, class_2561.method_43471("gui.xaero_settings_search")
         );
         this.searchField.method_1852(this.entryFilter);
         if (shouldFocusSearch) {
            this.method_25395(this.searchField);
            this.searchField.method_25365(true);
            this.searchField.method_1875(cursorPos);
            this.searchField.method_1884(cursorPos);
         }

         this.searchField.method_1863(s -> {
            if (this.canSearch) {
               this.updateSearch();
            }
         });
         this.method_25429(this.searchField);
      }
   }

   public void method_25393() {
      super.method_25393();
      if (this.canSearch) {
         this.searchField.method_1865();
      }
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      this.method_25420(guiGraphics);
      this.modMain
         .getWidgetScreenHandler()
         .render(guiGraphics, this, this.field_22789, this.field_22790, par1, par2, this.field_22787.method_22683().method_4495());
      guiGraphics.method_27534(this.field_22787.field_1772, this.screenTitle, this.field_22789 / 2, 5, 16777215);
      super.method_25394(guiGraphics, par1, par2, par3);
      if (this.canSearch) {
         if (!this.foundSomething) {
            guiGraphics.method_25300(
               this.field_22787.field_1772,
               class_1074.method_4662("gui.xaero_settings_not_found", new Object[0]),
               this.field_22789 / 2,
               this.field_22790 / 7 + 29,
               16777215
            );
         }

         if (!this.searchField.method_25370() && this.searchField.method_1882().isEmpty()) {
            Misc.setFieldText(this.searchField, class_1074.method_4662("gui.xaero_settings_search_placeholder", new Object[0]), -11184811);
            this.searchField.method_1883(0);
         }

         this.searchField.method_25394(guiGraphics, par1, par2, par3);
         if (!this.searchField.method_25370()) {
            Misc.setFieldText(this.searchField, this.entryFilter);
         }
      }

      this.renderTooltips(guiGraphics, par1, par2, par3);
   }

   public void restoreFocus(int index) {
      if (index != -1) {
         try {
            class_364 child = (class_364)this.method_25396().get(index);
            this.method_25395(child);
         } catch (IndexOutOfBoundsException var3) {
         }
      }
   }

   public int getIndex(class_364 child) {
      List<? extends class_364> children = this.method_25396();

      for (int i = 0; i < children.size(); i++) {
         if (children.get(i) == child) {
            return i;
         }
      }

      return -1;
   }

   @Override
   public void addButtonVisible(class_339 button) {
      this.method_37063(button);
   }

   @Override
   public <S extends class_437 & WidgetScreen> S getScreen() {
      return (S)this;
   }

   @Override
   protected void onExit(class_437 screen) {
      try {
         this.modMain.getSettings().saveSettings();
      } catch (IOException var3) {
         MinimapLogs.LOGGER.error("suppressed exception", var3);
      }

      if (this.shouldSaveRadar) {
         this.modMain.getEntityRadarCategoryManager().save();
      }

      super.onExit(screen);
   }

   public boolean method_25404(int par1, int par2, int par3) {
      if (!super.method_25404(par1, par2, par3) && (!(this.method_25399() instanceof class_342) || !((class_342)this.method_25399()).method_25370())) {
         if (Misc.inputMatchesKeyBinding(
            this.modMain, par1 != -1 ? class_307.field_1668 : class_307.field_1671, par1 != -1 ? par1 : par2, (class_304)this.modMain.getSettingsKey(), 0
         )) {
            this.method_25419();
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public boolean method_25400(char c, int i) {
      return super.method_25400(c, i);
   }

   @Override
   public boolean method_25402(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
      this.modMain
         .getWidgetScreenHandler()
         .handleClick(this, this.field_22789, this.field_22790, (int)p_mouseClicked_1_, (int)p_mouseClicked_3_, this.field_22787.method_22683().method_4495());
      if (!super.method_25402(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
         if (Misc.inputMatchesKeyBinding(this.modMain, class_307.field_1672, p_mouseClicked_5_, (class_304)this.modMain.getSettingsKey(), 0)) {
            this.goBack();
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   protected void onNextButton() {
      this.page++;
      this.method_25423(this.field_22787, this.field_22789, this.field_22790);
   }

   protected void onPrevButton() {
      this.page--;
      this.method_25423(this.field_22787, this.field_22789, this.field_22790);
   }

   protected static ISettingEntry[] entriesFromOptions(ModOptions[] options) {
      ISettingEntry[] result = new ISettingEntry[options.length];

      for (int i = 0; i < options.length; i++) {
         result[i] = new ConfigSettingEntry(options[i]);
      }

      return result;
   }

   protected void resetConfirmResult(boolean result, class_437 parent, class_437 escScreen) {
      if (result) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            minimapSession.getMinimapProcessor().setToResetImage(true);
         }

         try {
            this.modMain.resetSettings();
            this.modMain.getSettings().saveSettings();
         } catch (IOException var6) {
            MinimapLogs.LOGGER.error("suppressed exception", var6);
         }

         this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
      }

      this.modMain.getGuiHelper().onResetCancel(parent, escScreen);
   }

   public ISettingEntry[] getEntriesCopy() {
      if (this.entries == null) {
         return null;
      } else {
         ISettingEntry[] result = new ISettingEntry[this.entries.length];
         System.arraycopy(this.entries, 0, result, 0, this.entries.length);
         return result;
      }
   }

   private void updateSearch() {
      if (this.searchField.method_25370()) {
         String newValue = this.searchField.method_1882();
         if (!Objects.equal(this.entryFilter, newValue)) {
            this.entryFilter = this.searchField.method_1882();
            this.shouldRefocusSearch = true;
            this.page = 0;
            this.method_25423(this.field_22787, this.field_22789, this.field_22790);
         }
      }
   }

   public void setShouldSaveRadar() {
      this.shouldSaveRadar = true;
   }
}
