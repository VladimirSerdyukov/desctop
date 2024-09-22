package org.example.ui.actionListener;

import org.example.commands.AllCommands;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.Map;

public class CheckBoxListener implements ItemListener {

    JCheckBoxMenuItem box;

    public CheckBoxListener(JCheckBoxMenuItem box) {
        this.box = box;
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        if(box.getState()) {

            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                if(m.getKey().getName().equals(box.getName())) {
                    m.setValue(false);
                }
            }
        } else {
            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                if (m.getKey().getName().equals(box.getName())) {
                    m.setValue(true);
                }
            }
        }
    }
}
