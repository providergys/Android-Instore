package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/09/2017.
 */

public class OptionInfo implements Serializable {

    private String id;
    private String description;
    private ArrayList<OptionValueInfo> optionValues;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getOptionDescription() {
        return description;
    }
    public void setOptionDescription(String description) {
        this.description= description;
    }

    public ArrayList<OptionValueInfo> getOptionValues() {
        return optionValues;
    }
    public void setOptionValues(ArrayList<OptionValueInfo> optionValues) {
        this.optionValues = optionValues;
    }

}
