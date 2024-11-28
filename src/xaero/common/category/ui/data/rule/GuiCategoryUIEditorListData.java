package xaero.common.category.ui.data.rule;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import xaero.common.category.rule.ObjectCategoryListRuleType;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSimpleDeletableWrapperData;
import xaero.common.category.ui.data.options.text.GuiCategoryUIEditorTextFieldOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListEntryWidget;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButton;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

abstract class GuiCategoryUIEditorListData extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   protected final List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> list;
   private final GuiCategoryUIEditorTextFieldOptionsData topAdder;
   private final GuiCategoryUIEditorTextFieldOptionsData bottomAdder;
   private final ListFactory listFactory;
   private final GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback;
   private final BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier;

   protected GuiCategoryUIEditorListData(
      @Nonnull List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> list,
      @Nonnull ListFactory listFactory,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData topAdder,
      @Nonnull GuiCategoryUIEditorTextFieldOptionsData bottomAdder,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      @Nonnull GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback,
      @Nonnull BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.list = list;
      this.listFactory = listFactory;
      this.topAdder = topAdder;
      this.bottomAdder = bottomAdder;
      this.deletionCallback = deletionCallback;
      this.helpTooltipSupplier = helpTooltipSupplier;
   }

   public List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> getList() {
      return this.list;
   }

   public GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback getDeletionCallback() {
      return this.deletionCallback;
   }

   private Consumer<GuiCategoryUIEditorTextFieldOptionsData> getAdderHandler() {
      return adder -> {
         String adderRequest = adder.getResult();
         if (!adderRequest.isEmpty()) {
            GuiCategoryUIEditorSimpleDeletableWrapperData<String> element = GuiCategoryUIEditorSimpleDeletableWrapperData.Builder.<String>getDefault()
               .setElement(adderRequest)
               .setDeletionCallback(this.getDeletionCallback())
               .build();
            int sortedIndex = Collections.binarySearch(this.list, element);
            if (sortedIndex < 0) {
               sortedIndex = ~sortedIndex;
               this.list.add(sortedIndex, element);
            }

            adder.resetInput("");
         }
      };
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      Consumer<GuiCategoryUIEditorTextFieldOptionsData> adderHandler = this.getAdderHandler();
      adderHandler.accept(this.topAdder);
      adderHandler.accept(this.bottomAdder);
      List<GuiCategoryUIEditorExpandableData<?>> result = this.listFactory.get();
      if (this.list.size() > 0) {
         result.add(this.topAdder);
      }

      result.addAll(this.list);
      result.add(this.bottomAdder);
      return result;
   }

   public abstract static class Builder<E, P, ED extends GuiCategoryUIEditorListData, B extends GuiCategoryUIEditorListData.Builder<E, P, ED, B>>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, B> {
      private final B self = (B)this;
      protected final List<GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<String>> list;
      protected final GuiCategoryUIEditorTextFieldOptionsData.Builder adderBuilder;
      protected ListFactory listFactory;
      protected GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback;
      private Predicate<String> inputRuleTypeStringValidator;
      protected BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier;
      private ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
      private Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes;
      private String listRuleTypePrefixSeparator;

      protected Builder(ListFactory listFactory) {
         this.list = listFactory.get();
         this.listFactory = listFactory;
         this.adderBuilder = GuiCategoryUIEditorTextFieldOptionsData.Builder.getDefault(listFactory);
      }

      public B setDefault() {
         super.setDefault();
         this.list.clear();
         this.setDeletionCallback(null);
         this.adderBuilder
            .setDefault()
            .setAllowCustomInput(false)
            .setAutoConfirm(false)
            .setDisplayName(class_1074.method_4662("gui.xaero_category_list_add", new Object[0]));
         this.setDeletionCallback((parent, element, rowList) -> {
            GuiCategoryUIEditorListData listData = (GuiCategoryUIEditorListData)parent;
            if (listData.getList().remove(element)) {
               rowList.restoreScrollAfterUpdate();
               return true;
            } else {
               return false;
            }
         });
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
                  data,
                  ((GuiCategoryUIEditorListData)data).helpTooltipSupplier.apply(parent, (GuiCategoryUIEditorListData)data)
               )
         );
         this.setHelpTooltipSupplier((parent, data) -> null);
         this.setDefaultListRuleType(null);
         this.setListRuleTypes(null);
         this.setListRuleTypePrefixSeparator(null);
         this.setInputRuleTypeStringValidator(null);
         return this.self;
      }

      public List<GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<String>> getList() {
         return this.list;
      }

      public B setDeletionCallback(GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback) {
         this.deletionCallback = deletionCallback;
         return this.self;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder getAdderBuilder() {
         return this.adderBuilder;
      }

      protected List<GuiCategoryUIEditorSimpleDeletableWrapperData<String>> buildList() {
         return this.list.stream().map(builder -> {
            builder.setDeletionCallback(this.deletionCallback);
            return builder.build();
         }).sorted().collect(this.listFactory::get, List::add, List::addAll);
      }

      public B setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
         this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
         return this.self;
      }

      public B setHelpTooltipSupplier(
         BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> helpTooltipSupplier
      ) {
         this.helpTooltipSupplier = helpTooltipSupplier;
         return this.self;
      }

      public B setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
         this.defaultListRuleType = defaultListRuleType;
         return this.self;
      }

      public B setListRuleTypes(Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes) {
         this.listRuleTypes = listRuleTypes;
         return this.self;
      }

      public B setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
         this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
         return this.self;
      }

      @Override
      public GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> build() {
         if (this.deletionCallback != null
            && this.helpTooltipSupplier != null
            && this.defaultListRuleType != null
            && this.listRuleTypes != null
            && this.listRuleTypePrefixSeparator != null) {
            String listRuleTypePrefixSeparator = this.listRuleTypePrefixSeparator;
            Predicate<String> inputRuleTypeStringValidator = this.inputRuleTypeStringValidator;
            Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes = this.listRuleTypes;
            Predicate<String> inputStringValidator = s -> {
               for (ObjectCategoryListRuleType<E, P, ?> listRuleTypex : listRuleTypes) {
                  if (listRuleTypex.getStringValidator().test(s)) {
                     return true;
                  }
               }

               return false;
            };
            if (this.adderBuilder.needsInputStringValidator()) {
               this.adderBuilder.setInputStringValidator(s -> {
                  int separatorIndex = s.indexOf(listRuleTypePrefixSeparator);
                  if (separatorIndex == -1) {
                     return inputStringValidator.test(s);
                  } else {
                     String listRuleTypeString = s.substring(0, separatorIndex);
                     if (inputRuleTypeStringValidator != null && !inputRuleTypeStringValidator.test(listRuleTypeString)) {
                        return false;
                     } else {
                        String elementString = s.substring(separatorIndex + 1);
                        return inputStringValidator.test(elementString);
                     }
                  }
               });
            }

            for (ObjectCategoryListRuleType<E, P, ?> listRuleType : listRuleTypes) {
               String prefix = listRuleType == this.defaultListRuleType ? "" : listRuleType.getId() + listRuleTypePrefixSeparator;
               this.addSuggestionsForListRuleType(listRuleType, prefix);
            }

            return super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      private <S> void addSuggestionsForListRuleType(ObjectCategoryListRuleType<E, P, S> listRuleType, String prefix) {
         listRuleType.getAllElementSupplier().get().forEach(e -> this.adderBuilder.addOptionBuilderFor(prefix + listRuleType.getSerializer().apply((S)e)));
      }

      protected abstract ED buildInternally();
   }
}
