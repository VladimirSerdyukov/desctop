package org.example.ui.menu;

import javax.swing.*;
@TupeMenu(name="О программе...")
public class AboutProgrammMenu implements Menu{
    @Override
    public JMenu createMenu() {
        JMenu menu = new JMenu("О программе");

        return menu;
    }
}
