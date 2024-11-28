package xaero.common.category.ui.data.options.text;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import net.minecraft.class_310;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorExpandingOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListEntryExpandingOptions;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsTextField;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorTextFieldOptionsData extends GuiCategoryUIEditorExpandingOptionsData<String> {
   private String input;
   private String result;
   private int cursorPos;
   private int highlightPos;
   private final int maxLength;
   private CategorySettingsTextField.UpdatedValueConsumer updatedValueConsumer;
   private List<GuiCategoryUIEditorOptionData<String>> suggestions;
   private final TextFieldSuggestionsResolver suggestionsResolver;
   private final boolean allowCustomInput;
   private final boolean autoConfirm;
   private final Predicate<String> inputStringValidator;

   protected GuiCategoryUIEditorTextFieldOptionsData(
      @Nonnull String displayName,
      @Nonnull String input,
      int maxLength,
      @Nonnull GuiCategoryUIEditorOptionData<String> currentValue,
      @Nonnull List<GuiCategoryUIEditorOptionData<String>> options,
      @Nonnull TextFieldSuggestionsResolver suggestionsResolver,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      boolean allowCustomInput,
      boolean autoConfirm,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<String>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier,
      Predicate<String> inputStringValidator
   ) {
      super(displayName, currentValue, options, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.maxLength = maxLength;
      this.resetInput(input);
      this.suggestionsResolver = suggestionsResolver;
      this.allowCustomInput = allowCustomInput;
      this.autoConfirm = autoConfirm;
      this.inputStringValidator = inputStringValidator;
   }

   public void resetInput(String input) {
      this.input = input;
      this.result = input;
      this.cursorPos = this.highlightPos = input.length();
   }

   @Override
   public void setCurrentValue(GuiCategoryUIEditorOptionData<String> currentValue) {
   }

   @Override
   public GuiCategoryUIEditorOptionData<String> getCurrentValue() {
      return GuiCategoryUIEditorOptionData.Builder.<String>getDefault().setValue(this.input).build();
   }

   public String getInput() {
      return this.input;
   }

   public String getResult() {
      return this.result;
   }

   public int getCursorPos() {
      return this.cursorPos;
   }

   public int getHighlightPos() {
      return this.highlightPos;
   }

   public int getMaxLength() {
      return this.maxLength;
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public void setExpanded(boolean expanded) {
      if (!expanded) {
         this.resetInput(this.result);
         this.suggestions = null;
      }

      super.setExpanded(expanded);
   }

   @Override
   public boolean onSelected(GuiCategoryUIEditorOptionData<String> option) {
      boolean result = super.onSelected(option);
      this.resetInput(option.getValue().toString());
      return result;
   }

   public final CategorySettingsTextField.UpdatedValueConsumer getUpdatedValueConsumer() {
      if (this.updatedValueConsumer == null) {
         this.updatedValueConsumer = (s, c, h, rl) -> {
            this.cursorPos = c;
            this.highlightPos = h;
            String oldInput = this.input;
            if (oldInput == null || !oldInput.equals(s)) {
               this.input = s;
               this.suggestions = this.suggestionsResolver.getSuggestions(s, this.options);
               if (this.autoConfirm) {
                  this.result = s;
               }

               if (!this.autoConfirm && this.allowCustomInput && !s.isEmpty() && this.inputStringValidator.test(s)) {
                  this.suggestions
                     .add(
                        0,
                        GuiCategoryUIEditorOptionData.Builder.<String>getDefault()
                           .setValue(s)
                           .setDisplayName(class_1074.method_4662("gui.xaero_category_add_to_list_custom", new Object[]{s}))
                           .build()
                     );
               }

               if (!this.suggestions.isEmpty()) {
                  this.setExpanded(true);
               } else if (s.isEmpty()) {
                  this.setExpanded(false);
               }

               if (this.autoConfirm) {
                  rl.restoreScrollAfterUpdate();
               }

               rl.setLastExpandedData(this);
               rl.updateEntries();
            }
         };
      }

      return this.updatedValueConsumer;
   }

   @Override
   public List<GuiCategoryUIEditorOptionData<String>> getSubExpandables() {
      return this.suggestions;
   }

   public static final class Builder extends GuiCategoryUIEditorExpandingOptionsData.Builder<String, GuiCategoryUIEditorTextFieldOptionsData.Builder> {
      private String input;
      private int maxLength;
      private final GuiCategoryUIEditorOptionData.Builder<String> currentInputOption = GuiCategoryUIEditorOptionData.Builder.getDefault();
      private final TextFieldSuggestionsResolver.Builder suggestionsResolverBuilder;
      private boolean allowCustomInput;
      private boolean autoConfirm;
      private Predicate<String> inputStringValidator;

      protected Builder(ListFactory listFactory) {
         super(listFactory);
         this.suggestionsResolverBuilder = TextFieldSuggestionsResolver.Builder.getDefault(listFactory);
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setDefault() {
         super.setDefault();
         this.setInput("");
         this.setMaxLength(100);
         this.currentInputOption.setDefault().setDisplayName("null holder");
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryExpandingOptions<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsTextField(
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).getUpdatedValueConsumer(),
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).getInput(),
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).getCursorPos(),
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).getHighlightPos(),
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).getMaxLength(),
                           class_310.method_1551().field_1772,
                           214,
                           18,
                           data.getDisplayName(),
                           ((GuiCategoryUIEditorTextFieldOptionsData)data).inputStringValidator,
                           rowList
                        ),
                        null,
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               )
         );
         this.addOptionBuilder(this.currentInputOption);
         this.setAllowCustomInput(true);
         this.setAutoConfirm(true);
         this.setInputStringValidator(null);
         this.suggestionsResolverBuilder.setDefault();
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setInput(String input) {
         this.input = input;
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setInputStringValidator(Predicate<String> inputStringValidator) {
         this.inputStringValidator = inputStringValidator;
         return this;
      }

      public boolean needsInputStringValidator() {
         return this.inputStringValidator == null;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setAllowCustomInput(boolean allowCustomInput) {
         this.allowCustomInput = allowCustomInput;
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setAutoConfirm(boolean autoConfirm) {
         this.autoConfirm = autoConfirm;
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setMaxLength(int maxLength) {
         this.maxLength = maxLength;
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData.Builder setCurrentValue(String currentValue) {
         return this;
      }

      public GuiCategoryUIEditorTextFieldOptionsData build() {
         if (this.input != null && this.inputStringValidator != null) {
            return (GuiCategoryUIEditorTextFieldOptionsData)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      protected GuiCategoryUIEditorTextFieldOptionsData buildInternally(
         GuiCategoryUIEditorOptionData<String> currentValueData, List<GuiCategoryUIEditorOptionData<String>> options
      ) {
         return new GuiCategoryUIEditorTextFieldOptionsData(
            this.displayName,
            this.input,
            this.maxLength,
            currentValueData,
            options,
            this.suggestionsResolverBuilder.build(),
            this.movable,
            this.listEntryFactory,
            this.allowCustomInput,
            this.autoConfirm,
            this.tooltipSupplier,
            this.isActiveSupplier,
            this.inputStringValidator
         );
      }

      public static GuiCategoryUIEditorTextFieldOptionsData.Builder getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorTextFieldOptionsData.Builder(listFactory).setDefault();
      }
   }
}
