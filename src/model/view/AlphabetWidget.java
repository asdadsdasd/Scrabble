package model.view;

import model.entity.AbstractAlphabet;
import model.entity.ComplicatedAlphabet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AlphabetWidget extends JPanel {
    private AbstractAlphabet alphabet;

    private List<JButton> buttons = new ArrayList<>();

    private List<Character> activeLetters;

    public AlphabetWidget(AbstractAlphabet alphabet){
        this.alphabet = alphabet;
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
            btn.setEnabled(false);
            buttons.add(btn);
            add(btn);
            //btn.addActionListener(new GamePanel.ButtonClickListener()); //todo
        }
    }
}
