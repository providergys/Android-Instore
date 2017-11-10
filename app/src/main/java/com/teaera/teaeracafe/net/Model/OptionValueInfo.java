package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 12/09/2017.
 */

public class OptionValueInfo implements Serializable {

    private String id;
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getOptionValueDescription() {
        return description;
    }
    public void setOptionValueDescription(String description) {
        this.description= description;
    }

}