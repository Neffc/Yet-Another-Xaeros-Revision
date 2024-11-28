package xaero.common.category.ui.data;

import com.google.common.base.Objects;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public class GuiCategoryUIEditorSimpleWrapperData<S extends Comparable<S>>
   extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>
   implements Comparable<GuiCategoryUIEditorSimpleWrapperData<S>> {
   private S element;

   protected GuiCategoryUIEditorSimpleWrapperData(
      @Nonnull S element,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.element = element;
   }

   public S getElement() {
      return this.element;
   }

   public void setElement(S element) {
      this.element = element;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      return null;
   }

   @Override
   public String getDisplayName() {
      return this.element.toString();
   }

   @Override
   public boolean equals(Object obj) {
      return obj != null && obj instanceof GuiCategoryUIEditorSimpleWrapperData<?> otherWrapper ? Objects.equal(this.element, otherWrapper.element) : false;
   }

   public int compareTo(GuiCategoryUIEditorSimpleWrapperData<S> o) {
      if (this.element == o.element) {
         return 0;
      } else if (this.element == null) {
         return -1;
      } else {
         return o.element == null ? 1 : this.element.compareTo(o.element);
      }
   }

   public abstract static class Builder<S extends Comparable<S>, B extends GuiCategoryUIEditorSimpleWrapperData.Builder<S, B>>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, B> {
      protected S element;

      protected Builder() {
      }

      public B setDefault() {
         super.setDefault();
         this.setElement(null);
         return this.self;
      }

      public B setElement(S element) {
         this.element = element;
         return this.self;
      }

      public GuiCategoryUIEditorSimpleWrapperData<S> build() {
         if (this.element == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorSimpleWrapperData<S>)super.build();
         }
      }
   }

   public static final class FinalBuilder<S extends Comparable<S>>
      extends GuiCategoryUIEditorSimpleWrapperData.Builder<S, GuiCategoryUIEditorSimpleWrapperData.FinalBuilder<S>> {
      public static <S extends Comparable<S>> GuiCategoryUIEditorSimpleWrapperData.FinalBuilder<S> getDefault() {
         return new GuiCategoryUIEditorSimpleWrapperData.FinalBuilder().setDefault();
      }

      protected GuiCategoryUIEditorSimpleWrapperData<S> buildInternally() {
         return new GuiCategoryUIEditorSimpleWrapperData<>(this.element, this.movable, this.listEntryFactory, this.tooltipSupplier);
      }
   }
}
