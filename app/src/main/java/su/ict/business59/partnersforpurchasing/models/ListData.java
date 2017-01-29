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
    @SerializedName("product")
    @Expose()
    private List<Product> itemsProduct;
    @SerializedName("shop")
    @Expose()
    private List<Shop> itemsShop;
    @SerializedName("post")
    @Expose()
    private List<Post> itemsPost;

    @SerializedName("promotion")
    @Expose()
    private List<Promotion> itemsPromotion;

    @SerializedName("shop_class")
    @Expose()
    private List<ShopClass> itemsShopClass;

    @SerializedName("shop_room")
    @Expose()
    private List<ShopRoom> itemsShopRoom;

    @SerializedName("shop_soi")
    @Expose()
    private List<ShopSoi> itemsShopSoi;

    public List<Category> getItemsCategory() {
        return itemsCategory;
    }

    public List<Product> getItemsProduct() {
        return itemsProduct;
    }

    public List<Shop> getItemsShop() {
        return itemsShop;
    }

    public List<Post> getItemsPost() {
        return itemsPost;
    }

    public List<Promotion> getItemsPromotion() {
        return itemsPromotion;
    }

    public List<ShopClass> getItemsShopClass() {
        return itemsShopClass;
    }

    public List<ShopRoom> getItemsShopRoom() {
        return itemsShopRoom;
    }

    public List<ShopSoi> getItemsShopSoi() {
        return itemsShopSoi;
    }
}
