package org.example.ui.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

@TupeMenu(name="Файл")
public class FileMenu implements Menu {
    @Override
    public JMenu createMenu() {
        JMenu menu = new JMenu("Файл");
        JMenuItem exit = new JMenuItem(new ExitAction());
        menu.add(exit);

        return menu;
    }


    class ExitAction extends AbstractAction {
        ExitAction() {
            putValue(NAME, "Выход");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}