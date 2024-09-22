package org.example.ui.actionListener;

import org.example.commands.AllCommands;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Map;

public class FilterActionListener implements ActionListener {

    private JButton button;

    public FilterActionListener(JButton button) {
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] name = button.getText().split("\\s+");
        if("(yes)".equals(name[1])) {
            button.setText(name[0] + ("\n (no)"));
            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                if(m.getKey().getName().equals(name[0])) {
                    AllCommands.getCommandsFilter().put(m.getKey(), false);
                }
            }
            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                System.out.println(m.getKey().getName() + " : " + m.getValue());
            }
        } else if ("(no)".equals(name[1])) {
            button.setText(name[0] + ("\n (yes)"));
            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                if(m.getKey().getName().equals(name[0])) {
                    AllCommands.getCommandsFilter().put(m.getKey(), true);
                }
            }
        }

    }
}
