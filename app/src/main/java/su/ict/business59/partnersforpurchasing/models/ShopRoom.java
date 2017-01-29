package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;

/**
 * Created by kaowneaw on 1/28/2017.
 */

public class ShopRoom {
    @Expose
    String room_id;
    @Expose
    String room_no;
    @Expose
    String soi_id;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getSoi_id() {
        return soi_id;
    }

    public void setSoi_id(String soi_id) {
        this.soi_id = soi_id;
    }

    @Override
    public String toString() {
        return this.room_no;
    }
}
