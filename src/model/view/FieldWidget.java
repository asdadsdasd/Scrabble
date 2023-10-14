package model.view;

import model.entity.GameField;

import javax.swing.*;
import java.awt.*;

public class FieldWidget extends JPanel {
    private GameField field;

    private WidgetFactory factory;

    private final int FIELD_SIZE = 400;

    public FieldWidget(GameField field, WidgetFactory factory) {
        this.field = field;
        this.factory = factory;

        setLayout(new GridLayout(field.height(), field.width()));

        Dimension dimension = new Dimension(FIELD_SIZE, FIELD_SIZE);

        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        setVisible(true);

        for (int row = 0; row < field.height(); row++) {
            for (int col = 0; col < field.width(); col++) {
                JButton button = new JButton("");
                button.setFocusable(false);

                //repaintField();
            }
        }
    }

    public void repaintField(){
        removeAll();
        setLayout(new GridLayout(field.height(), field.height()));

        for (int row = 0; row < field.height(); row++) {
            for (int col = 0; col < field.width(); col++) {
                CellWidget button = factory.createCellWidget(field.cell(new Point(col, row)));
                add(button);
                //button.addActionListener(new GamePanel.FieldClickListener()); //todo
            }
        }

        validate();
    }
}
