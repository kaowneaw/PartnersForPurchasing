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

public class Post extends Shop implements Parcelable {

    @SerializedName("post_id")
    @Expose
    String postId;
    @SerializedName("post_name")
    @Expose
    String postName;
    @SerializedName("post_desc")
    @Expose
    String postDesc;
    @SerializedName("post_start")
    @Expose
    String postStart;
    @SerializedName("post_end")
    @Expose
    String postEnd;
    @SerializedName("post_img")
    @Expose
    String postImg;
    @SerializedName("cat_id")
    @Expose
    String catId;
    @SerializedName("cat_name")
    @Expose
    String catName;
    @SerializedName("memberJoin")
    @Expose
    List<MemberJoin> memberJoin;

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

    public String getPostStart() {
        return postStart;
    }

    public void setPostStart(String postStart) {
        this.postStart = postStart;
    }

    public String getPostEnd() {
        return postEnd;
    }

    public void setPostEnd(String postEnd) {
        this.postEnd = postEnd;
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

    public String getAddressShopString() {
        return "ชั้นที่ " + shopClass + " ซอยที่ " + shopSoi + " ห้องที่ " + shopRoom;
    }

    public Post() {
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
        dest.writeString(this.postStart);
        dest.writeString(this.postEnd);
        dest.writeString(this.postImg);
        dest.writeString(this.catId);
        dest.writeString(this.catName);
        dest.writeList(this.memberJoin);
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopDesc);
        dest.writeString(this.shopImg);
        dest.writeString(this.shopClass);
        dest.writeString(this.shopSoi);
        dest.writeString(this.shopRoom);
        dest.writeString(this.shopPromotion);
        dest.writeString(this.adminId);
        dest.writeInt(this.shopStatus);
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.image_url);
        dest.writeString(this.role);
        dest.writeString(this.message);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }

    protected Post(Parcel in) {
        this.postId = in.readString();
        this.postName = in.readString();
        this.postDesc = in.readString();
        this.postStart = in.readString();
        this.postEnd = in.readString();
        this.postImg = in.readString();
        this.catId = in.readString();
        this.catName = in.readString();
        this.memberJoin = new ArrayList<MemberJoin>();
        in.readList(this.memberJoin, MemberJoin.class.getClassLoader());
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.shopDesc = in.readString();
        this.shopImg = in.readString();
        this.shopClass = in.readString();
        this.shopSoi = in.readString();
        this.shopRoom = in.readString();
        this.shopPromotion = in.readString();
        this.adminId = in.readString();
        this.shopStatus = in.readInt();
        this.user_id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.image_url = in.readString();
        this.role = in.readString();
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