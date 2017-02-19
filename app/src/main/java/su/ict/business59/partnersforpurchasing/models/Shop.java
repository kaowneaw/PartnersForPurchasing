package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaowneaw on 12/11/2016.
 */

public class Shop extends User {
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
    @SerializedName("class_name")
    @Expose
    String class_name;
    @SerializedName("room_no")
    @Expose
    String room_no;
    @SerializedName("soi_name")
    @Expose
    String soi_name;
    @SerializedName("soi_zone")
    @Expose
    String soi_zone;

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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
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

    public void setSoi_zone(String soi_zone) {
        this.soi_zone = soi_zone;
    }

    public String getAddressShopString() {
        return "ชั้นที่:  " + this.class_name + " ซอยที่:  Zone " + this.soi_zone + " " + this.soi_name + " ห้องที่:  " + this.room_no;
    }
}
