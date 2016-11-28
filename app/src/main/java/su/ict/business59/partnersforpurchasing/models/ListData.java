package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class ListData {
    @SerializedName("category")
    @Expose()
    private List<Category> itemsCategory;

    public List<Category> getItemsCategory() {
        return itemsCategory;
    }

    public void setItemsCategory(List<Category> itemsCategory) {
        this.itemsCategory = itemsCategory;
    }
}
