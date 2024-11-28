package xaero.common.category.ui.data.options;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public abstract class GuiCategoryUIEditorOptionsData<V> extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<V>> {
   protected GuiCategoryUIEditorOptionData<V> currentValue;
   protected Supplier<String> messageSupplier;
   protected final String displayName;
   private final GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier;

   protected GuiCategoryUIEditorOptionsData(
      @Nonnull String displayName,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<V>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.displayName = displayName;
      this.isActiveSupplier = isActiveSupplier;
   }

   public GuiCategoryUIEditorOptionData<V> getCurrentValue() {
      return this.currentValue;
   }

   public void setCurrentValue(GuiCategoryUIEditorOptionData<V> currentValue) {
      this.currentValue = currentValue;
   }

   public final Supplier<String> getMessageSupplier() {
      if (this.messageSupplier == null) {
         this.messageSupplier = () -> this.isExpanded() ? this.displayName : String.format("%s: %s", this.displayName, this.currentValue.getDisplayName());
      }

      return this.messageSupplier;
   }

   public GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier getIsActiveSupplier() {
      return this.isActiveSupplier;
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   public abstract static class Builder<V, B extends GuiCategoryUIEditorOptionsData.Builder<V, B>>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorOptionData<V>, B> {
      protected B self = (B)this;
      protected V currentValue;
      protected String displayName;
      protected GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier;

      protected Builder() {
      }

      public B setDefault() {
         super.setDefault();
         this.setCurrentValue(null);
         this.setDisplayName(null);
         this.setIsActiveSupplier((p, d) -> true);
         return this.self;
      }

      public B setCurrentValue(V currentValue) {
         this.currentValue = currentValue;
         return this.self;
      }

      public B setDisplayName(String displayName) {
         this.displayName = displayName;
         return this.self;
      }

      public B setIsActiveSupplier(GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier) {
         this.isActiveSupplier = isActiveSupplier;
         return this.self;
      }

      public GuiCategoryUIEditorOptionsData<V> build() {
         if (this.displayName == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorOptionsData<V>)super.build();
         }
      }

      protected abstract GuiCategoryUIEditorOptionsData<V> buildInternally();
   }

   @FunctionalInterface
   public interface IOptionsDataIsActiveSupplier {
      boolean get(GuiCategoryUIEditorExpandableData<?> var1, GuiCategoryUIEditorOptionsData<?> var2);
   }
}
