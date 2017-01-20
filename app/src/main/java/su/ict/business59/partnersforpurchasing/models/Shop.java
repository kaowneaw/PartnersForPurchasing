package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaowneaw on 12/11/2016.
 */

public class Shop extends User{
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("shop_desc")
    @Expose
    String shopDesc;
    @SerializedName("shop_img_url")
    @Expose
    String shopImg;
    @SerializedName("shop_class")
    @Expose
    String shopClass;
    @SerializedName("shop_soi")
    @Expose
    String shopSoi;
    @SerializedName("shop_room")
    @Expose
    String shopRoom;
    @SerializedName("shop_promotion")
    @Expose
    String shopPromotion;
    @SerializedName("admin_id")
    @Expose
    String adminId;
    @SerializedName("shop_status")
    @Expose
    int shopStatus;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopClass() {
        return shopClass;
    }

    public void setShopClass(String shopClass) {
        this.shopClass = shopClass;
    }

    public String getShopSoi() {
        return shopSoi;
    }

    public void setShopSoi(String shopSoi) {
        this.shopSoi = shopSoi;
    }

    public String getShopRoom() {
        return shopRoom;
    }

    public void setShopRoom(String shopRoom) {
        this.shopRoom = shopRoom;
    }

    public String getShopPromotion() {
        return shopPromotion;
    }

    public void setShopPromotion(String shopPromotion) {
        this.shopPromotion = shopPromotion;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(int shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getAddressShopString() {
        return "ชั้นที่ " + shopClass + " ซอยที่ " + shopSoi + " ห้องที่ " + shopRoom;
    }
}
