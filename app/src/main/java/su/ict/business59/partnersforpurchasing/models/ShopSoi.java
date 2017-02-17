package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;

/**
 * Created by kaowneaw on 1/28/2017.
 */

public class ShopSoi {
    @Expose
    String soi_id;
    @Expose
    String soi_name;
    @Expose
    String soi_zone;
    @Expose
    String class_id;

    public ShopSoi(String soi_id, String soi_name, String soi_zone, String class_id) {
        this.soi_id = soi_id;
        this.soi_name = soi_name;
        this.soi_zone = soi_zone;
        this.class_id = class_id;
    }

    public String getSoi_id() {
        return soi_id;
    }

    public void setSoi_id(String soi_id) {
        this.soi_id = soi_id;
    }

    public String getSoi_name() {
        return soi_name;
    }

    public void setSoi_name(String soi_name) {
        this.soi_name = soi_name;
    }

    public String getSoi_zone() {
        return soi_zone;
    }

    public void setSio_zone(String sio_zone) {
        this.soi_zone = sio_zone;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }


    @Override
    public String toString() {
        if (this.soi_id.equals("-1")) {
            return this.soi_name;
        } else {
            return "Zone " + this.soi_zone + " " + this.soi_name;
        }

    }
}
