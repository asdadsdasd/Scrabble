package model.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionButtonWidget extends JPanel {
    private List<String> actionButtons = Arrays.asList("Завершить ход", "Отмена", "Пропустить ход");

    private List<JButton> buttonList = new ArrayList<>();

    public void buildActionButtonsPanel(){
        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 40);

        setPreferredSize(dimension);

        JButton btn;
        for (String str : actionButtons){
            btn = new JButton(str);
            add(btn);
            buttonList.add(btn);
            btn.setEnabled(false);
        }
    }

    public void turnOffAllButtons(){
        for (JButton button : buttonList){
            button.setEnabled(false);
        }
    }

    public List<JButton> buttons() {
        return Collections.unmodifiableList(buttonList);
    }

    public void setEnabledButtonWithText(boolean flag, String buttonText){
        for (JButton button : buttonList){
            if(button.getText().equals(buttonText)){
                button.setEnabled(flag);
            }
        }
    }
}
