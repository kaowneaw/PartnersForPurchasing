package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;

/**
 * Created by kaowneaw on 1/28/2017.
 */

public class ShopClass {
    @Expose
    String class_id;
    @Expose
    String class_name;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public String toString() {
        return this.class_name;
    }
}
