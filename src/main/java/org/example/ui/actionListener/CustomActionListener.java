package org.example.ui.actionListener;

import org.telegram.telegrambots.meta.generics.BotSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomActionListener implements ActionListener{
    private JButton button;
    private BotSession botSession;

    public CustomActionListener(JButton button, BotSession botSession) {
        this.button = button;
        this.botSession = botSession;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if("Stop".equals(button.getText())) {
            button.setText("Start");
            botSession.stop();
            JOptionPane.showMessageDialog(null,
                    "Вы остановили бота",
                    "Stop", JOptionPane.ERROR_MESSAGE);
        } else if ("Start".equals(button.getText())) {
            button.setText("Stop");
            botSession.start();
            JOptionPane.showMessageDialog(null,
                    "Вы запустили бота",
                    "Start", JOptionPane.ERROR_MESSAGE);
        }


    }
}
