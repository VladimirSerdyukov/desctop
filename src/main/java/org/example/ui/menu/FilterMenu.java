package org.example.ui.menu;

import org.example.commands.AllCommands;
import org.example.ui.actionListener.CheckBoxListener;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.Map;

//@TupeMenu(name = "Фильтр...")
public class FilterMenu implements Menu {

    @Override
    public JMenu createMenu() {
        JMenu menu = new JMenu("Фильтры");
        for (Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
            JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(m.getKey().getName());
            checkBox.setState(m.getValue());
            checkBox.addItemListener(new CheckBoxListener(checkBox));
            menu.add(checkBox);
        }
        return menu;
    }
}
