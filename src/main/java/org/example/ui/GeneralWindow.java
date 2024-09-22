package org.example.ui;

import org.example.Main;
import org.example.commands.AllCommands;
import org.example.ui.actionListener.CustomActionListener;
import org.example.ui.actionListener.FilterActionListener;
import org.example.ui.classToAnnotation.GetClassesToAnnotation;
import org.example.ui.menu.Menu;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class GeneralWindow extends JFrame {

    private BotSession botSession;
    private JCheckBox checkBox;

    private DefaultTableModel tableModel;
    private JTable table;

    public GeneralWindow() {
        super("Системное меню");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setBounds(200, 200, 500, 200);
    }

    public void init() {
        createTable();
        setJMenuBar(createMenuBar());
        Container container = getContentPane();
        createLayout(container);
        pack();
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        ArrayList<Class<?>> classes = GetClassesToAnnotation.reflectionGetClass();
        // Добавление в главное меню выпадающих пунктов меню

        for (Class<?> c : classes) {
            Menu newMenu = null;
            try {
                newMenu = (Menu) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }

            if (newMenu != null) {
                menuBar.add(newMenu.createMenu());
            }
        }
        return menuBar;
    }

    private void createLayout(Container container) {
        // Всего 5 колонок...
        container.setLayout(new GridBagLayout());
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.weightx = 1;
        constraints.gridy = 0;
        container.add(createButton(), constraints);

        constraints.gridheight = 3;
        constraints.gridwidth = 4;
        constraints.gridx = 1;
        constraints.gridy = 0;
        container.add(new JScrollPane(table), constraints);

        int index = 0;
        int lineZiro = 5;

        for (Map.Entry<Method, Boolean> s : AllCommands.getCommandsFilter().entrySet()) {
            if (index > 4) {
                index = 0;
                lineZiro ++;
            }
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.gridx = index;
            constraints.gridy = lineZiro;
            JButton button = createFilterButton(s.getKey().getName(), s.getValue().booleanValue());
            container.add(button, constraints);
            index++;
        }
    }

    private JButton createFilterButton(String nameFilter, boolean flag) {
        String title = nameFilter + (flag ? "\n (yes)" : "\n (no)");
        JButton button = new JButton(title);
        button.addActionListener(new FilterActionListener(button));
        return button;
    }

    private JButton createButton() {
        JButton button = new JButton("Stop");
        button.addActionListener(new CustomActionListener(button, botSession));
        return button;
    }

    private void createTable() {
        this.tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ChatId", "Filter", "NameFile"});
        this.table = new JTable(tableModel);
    }

    public void setBotSession(BotSession botSession) {
        this.botSession = botSession;
    }

    public DefaultTableModel getTable(){
        return this.tableModel;
    }
}
