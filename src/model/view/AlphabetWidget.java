package model.view;

import model.entity.AbstractAlphabet;
import model.entity.ComplicatedAlphabet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlphabetWidget extends JPanel {
    private AbstractAlphabet alphabet;

    private ArrayList<JButton> buttonList = new ArrayList<>();

    private List<Character> activeLetters;

    public AlphabetWidget(AbstractAlphabet alphabet){
        this.alphabet = alphabet;
    }

    public List<JButton> buttons() {
        return Collections.unmodifiableList(buttonList);
    }

    public void buildLetterPanel(){
        List<Character> letters;
        if (alphabet.getClass() == ComplicatedAlphabet.class)
            letters = ((ComplicatedAlphabet)alphabet).returnAvailableLetters(-1);
        else letters = alphabet.returnAvailableLetters();

        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 220);
        setPreferredSize(dimension);
        JButton btn;
        for (Character letter : letters){
            btn = new JButton(Character.toString(letter));
            add(btn);
            buttonList.add(btn);
            btn.setEnabled(false);
            //btn.addActionListener(new GamePanel.ButtonClickListener()); //todo
        }
    }

    public void setEnabledButtons(boolean flag){
        for (JButton button : buttonList){
            button.setEnabled(flag);
        }
    }
}
