package xaero.common.category.ui.data.options;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListEntryExpandingOptions;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButton;
import xaero.common.graphics.CursorBox;

public final class GuiCategoryUIEditorSimpleButtonData extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   protected final String displayName;
   private GuiCategoryUIEditorSimpleButtonData.ISimpleButtonCallback callback;
   private CategorySettingsButton.PressActionWithContext pressAction;
   private GuiCategoryUIEditorSimpleButtonData.ISimpleButtonMessageSupplier messageSupplier;
   private final GuiCategoryUIEditorSimpleButtonData.ISimpleButtonIsActiveSupplier isActiveSupplier;

   private GuiCategoryUIEditorSimpleButtonData(
      @Nonnull String displayName,
      @Nonnull BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      boolean movable,
      GuiCategoryUIEditorSimpleButtonData.ISimpleButtonCallback callback,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      GuiCategoryUIEditorSimpleButtonData.ISimpleButtonMessageSupplier messageSupplier,
      GuiCategoryUIEditorSimpleButtonData.ISimpleButtonIsActiveSupplier isActiveSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.displayName = displayName;
      this.callback = callback;
      this.messageSupplier = messageSupplier;
      this.isActiveSupplier = isActiveSupplier;
   }

   public Supplier<String> getMessageSupplier(GuiCategoryUIEditorExpandableData<?> parent, GuiCategoryUIEditorSimpleButtonData data) {
      return this.messageSupplier.get(parent, data);
   }

   public boolean getIsActiveSupplier(GuiCategoryUIEditorExpandableData<?> parent, GuiCategoryUIEditorSimpleButtonData data) {
      return this.isActiveSupplier.get(parent, data);
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   public CategorySettingsButton.PressActionWithContext getPressAction() {
      if (this.pressAction == null) {
         this.pressAction = new CategorySettingsButton.PressActionWithContext() {
            @Override
            public void onPress(
               CategorySettingsButton button, GuiCategoryUIEditorExpandableData<?> parent, GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList
            ) {
               if (GuiCategoryUIEditorSimpleButtonData.this.callback != null) {
                  GuiCategoryUIEditorSimpleButtonData.this.callback.onButtonPress(parent, GuiCategoryUIEditorSimpleButtonData.this, rowList);
               }
            }
         };
      }

      return this.pressAction;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      return null;
   }

   public static final class Builder
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorSimpleButtonData.Builder> {
      private String displayName;
      protected GuiCategoryUIEditorSimpleButtonData.ISimpleButtonCallback callback;
      protected GuiCategoryUIEditorSimpleButtonData.ISimpleButtonMessageSupplier messageSupplier;
      protected GuiCategoryUIEditorSimpleButtonData.ISimpleButtonIsActiveSupplier isActiveSupplier;

      public GuiCategoryUIEditorSimpleButtonData.Builder setDefault() {
         super.setDefault();
         this.setDisplayName(null);
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> {
               Supplier<String> messageSupplier = ((GuiCategoryUIEditorSimpleButtonData)data)
                  .getMessageSupplier(parent, (GuiCategoryUIEditorSimpleButtonData)data);
               return new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryExpandingOptions<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsButton(
                           parent,
                           messageSupplier,
                           ((GuiCategoryUIEditorSimpleButtonData)data).getIsActiveSupplier(parent, (GuiCategoryUIEditorSimpleButtonData)data),
                           216,
                           20,
                           ((GuiCategoryUIEditorSimpleButtonData)data).getPressAction(),
                           rowList
                        ),
                        messageSupplier,
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               );
            }
         );
         this.setCallback(null);
         this.setMessageSupplier((parent, data) -> () -> data.getDisplayName());
         this.setIsActiveSupplier((p, d) -> true);
         return this;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder setDisplayName(String displayName) {
         this.displayName = displayName;
         return this;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder setCallback(GuiCategoryUIEditorSimpleButtonData.ISimpleButtonCallback callback) {
         this.callback = callback;
         return this;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder setMessageSupplier(GuiCategoryUIEditorSimpleButtonData.ISimpleButtonMessageSupplier messageSupplier) {
         this.messageSupplier = messageSupplier;
         return this;
      }

      public GuiCategoryUIEditorSimpleButtonData.Builder setIsActiveSupplier(GuiCategoryUIEditorSimpleButtonData.ISimpleButtonIsActiveSupplier isActiveSupplier) {
         this.isActiveSupplier = isActiveSupplier;
         return this;
      }

      public GuiCategoryUIEditorSimpleButtonData build() {
         if (this.displayName != null && this.callback != null) {
            return (GuiCategoryUIEditorSimpleButtonData)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      @Override
      protected GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> buildInternally() {
         return new GuiCategoryUIEditorSimpleButtonData(
            this.displayName, this.tooltipSupplier, this.movable, this.callback, this.listEntryFactory, this.messageSupplier, this.isActiveSupplier
         );
      }

      public static GuiCategoryUIEditorSimpleButtonData.Builder getDefault() {
         return new GuiCategoryUIEditorSimpleButtonData.Builder().setDefault();
      }
   }

   @FunctionalInterface
   public interface ISimpleButtonCallback {
      void onButtonPress(
         GuiCategoryUIEditorExpandableData<?> var1, GuiCategoryUIEditorSimpleButtonData var2, GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList var3
      );
   }

   @FunctionalInterface
   public interface ISimpleButtonIsActiveSupplier {
      boolean get(GuiCategoryUIEditorExpandableData<?> var1, GuiCategoryUIEditorSimpleButtonData var2);
   }

   @FunctionalInterface
   public interface ISimpleButtonMessageSupplier {
      Supplier<String> get(GuiCategoryUIEditorExpandableData<?> var1, GuiCategoryUIEditorSimpleButtonData var2);
   }
}
