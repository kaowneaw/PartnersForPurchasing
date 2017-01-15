package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class Category extends BaseResponse {
    @Expose
    String cat_id;
    @Expose
    String cat_name;
    @Expose
    String cat_parent_id;
    @Expose
    String cat_level;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_parent_id() {
        return cat_parent_id;
    }

    public void setCat_parent_id(String cat_parent_id) {
        this.cat_parent_id = cat_parent_id;
    }

    public String getCat_level() {
        return cat_level;
    }

    public void setCat_level(String cat_level) {
        this.cat_level = cat_level;
    }

    @Override
    public String toString() {
        return this.cat_name;
    }
}
