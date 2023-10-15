package model.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionButtonWidget extends JPanel {
    private List<String> actionButtons = Arrays.asList("Завершить ход", "Отмена", "Пропустить ход");

    private List<JButton> buttons = new ArrayList<>();

    public void buildActionButtonsPanel(){
        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 40);

        setPreferredSize(dimension);

        JButton btn;
        for (String str : actionButtons){
            btn = new JButton(str);
            add(btn);
            buttons.add(btn);
            btn.setEnabled(false);
            //btn.addActionListener(new GamePanel.ButtonClickListener()); todo
        }
    }
}
