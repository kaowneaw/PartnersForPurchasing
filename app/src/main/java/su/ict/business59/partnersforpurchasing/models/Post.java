package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public class Post extends User implements Parcelable {

    @SerializedName("post_id")
    @Expose
    String postId;
    @SerializedName("post_name")
    @Expose
    String postName;
    @SerializedName("post_desc")
    @Expose
    String postDesc;
    @SerializedName("post_time")
    @Expose
    String postTime;
    @SerializedName("post_img")
    @Expose
    String postImg;
    @SerializedName("post_img2")
    @Expose
    String postImg2;
    @SerializedName("post_img3")
    @Expose
    String postImg3;
    @SerializedName("cat_id")
    @Expose
    String catId;
    @SerializedName("cat_name")
    @Expose
    String catName;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("shop_class")
    @Expose
    String shopClass;
    @SerializedName("shop_soi")
    @Expose
    String shopSoi;
    @SerializedName("shop_room")
    @Expose
    String shopRoom;
    @SerializedName("promotion_id")
    @Expose
    String promotionId;
    @SerializedName("promotion_name")
    @Expose
    String promotionName;
    @SerializedName("promotion_desc")
    @Expose
    String promotionDesc;
    @SerializedName("memberJoin")
    @Expose
    List<MemberJoin> memberJoin;
    @SerializedName("amount_require")
    @Expose
    int amountRequire;
    @SerializedName("unit_require")
    @Expose
    String unitRequire;

    @SerializedName("post_view")
    @Expose
    int postView;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }


    public List<MemberJoin> getMemberJoin() {
        return memberJoin;
    }

    public void setMemberJoin(List<MemberJoin> memberJoin) {
        this.memberJoin = memberJoin;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getAddressPostShop() {
        return "ชั้นที่:  " + this.shopClass + "  ซอยที่:  " + this.shopSoi + "  ห้องที่:  " + this.shopRoom;
    }

    public String getPromotionDesc() {
        return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
        this.promotionDesc = promotionDesc;
    }

    public int getPostView() {
        return postView;
    }

    public void setPostView(int postView) {
        this.postView = postView;
    }


    public int getAmountRequire() {
        return amountRequire;
    }

    public void setAmountRequire(int amountRequire) {
        this.amountRequire = amountRequire;
    }

    public String getUnitRequire() {
        return unitRequire;
    }

    public void setUnitRequire(String unitRequire) {
        this.unitRequire = unitRequire;
    }

    public String getPostImg2() {
        return postImg2;
    }

    public void setPostImg2(String postImg2) {
        this.postImg2 = postImg2;
    }

    public String getPostImg3() {
        return postImg3;
    }

    public void setPostImg3(String postImg3) {
        this.postImg3 = postImg3;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeString(this.postName);
        dest.writeString(this.postDesc);
        dest.writeString(this.postTime);
        dest.writeString(this.postImg);
        dest.writeString(this.postImg2);
        dest.writeString(this.postImg3);
        dest.writeString(this.catId);
        dest.writeString(this.catName);
        dest.writeString(this.shopName);
        dest.writeString(this.shopClass);
        dest.writeString(this.shopSoi);
        dest.writeString(this.shopRoom);
        dest.writeString(this.promotionId);
        dest.writeString(this.promotionName);
        dest.writeString(this.promotionDesc);
        dest.writeTypedList(this.memberJoin);
        dest.writeInt(this.amountRequire);
        dest.writeString(this.unitRequire);
        dest.writeInt(this.postView);
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.image_url);
        dest.writeString(this.role);
        dest.writeString(this.sex);
        dest.writeString(this.message);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.postId = in.readString();
        this.postName = in.readString();
        this.postDesc = in.readString();
        this.postTime = in.readString();
        this.postImg = in.readString();
        this.postImg2 = in.readString();
        this.postImg3 = in.readString();
        this.catId = in.readString();
        this.catName = in.readString();
        this.shopName = in.readString();
        this.shopClass = in.readString();
        this.shopSoi = in.readString();
        this.shopRoom = in.readString();
        this.promotionId = in.readString();
        this.promotionName = in.readString();
        this.promotionDesc = in.readString();
        this.memberJoin = in.createTypedArrayList(MemberJoin.CREATOR);
        this.amountRequire = in.readInt();
        this.unitRequire = in.readString();
        this.postView = in.readInt();
        this.user_id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.image_url = in.readString();
        this.role = in.readString();
        this.sex = in.readString();
        this.message = in.readString();
        this.status = in.readByte() != 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}