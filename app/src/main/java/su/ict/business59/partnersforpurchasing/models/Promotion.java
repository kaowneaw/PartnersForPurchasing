package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;

/**
 * Created by kaowneaw on 1/15/2017.
 */

public class Promotion {
    @Expose
    String promotion_id;
    @Expose
    String promotion_name;

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getPromotion_name() {
        return promotion_name;
    }

    public void setPromotion_name(String promotion_name) {
        this.promotion_name = promotion_name;
    }

    @Override
    public String toString() {
        return this.promotion_name;
    }
}
