package xaero.common.gui.widget;

import net.minecraft.class_339;
import net.minecraft.class_437;

public interface WidgetScreen {
   <S extends class_437 & WidgetScreen> S getScreen();

   void addButtonVisible(class_339 var1);
}
