package model.events;

import java.util.EventObject;

public class MenuEvent extends EventObject {

    String selectedSize;

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String selectedSize(){
        return selectedSize;
    }


    String fieldSizes[];

    public void setFieldSizes(String[] fieldSizes) {
        this.fieldSizes = fieldSizes;
    }

    public String[] fieldSizes(){
        return fieldSizes;
    }

    String selectedDiff;

    public void setSelectedDiff(String selectedDiff) {
        this.selectedDiff = selectedDiff;
    }

    public String selectedDiff(){
        return selectedDiff;
    }

    public MenuEvent(Object source) {
        super(source);
    }
}
