package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product extends Shop implements Parcelable {

    @SerializedName("product_id")
    @Expose
    int productId;
    @SerializedName("product_name")
    @Expose
    String productName;
    @SerializedName("product_desc")
    @Expose
    String productDesc;
    @SerializedName("product_price")
    @Expose
    double productPrice;
    @SerializedName("created")
    @Expose
    String created;
    @SerializedName("cat_id")
    @Expose
    String categoryId;
    @SerializedName("cat_name")
    @Expose
    String catName;
    @SerializedName("img_list")
    @Expose
    List<ProductImg> imgList;
    @SerializedName("promotion_id")
    @Expose
    String promotion_id;
    @SerializedName("promotion_name")
    @Expose
    String promotion_name;
    @SerializedName("product_promotion_desc")
    @Expose
    String promotion_desc;

    public Product(int productId, String productName, String productDesc, double productPrice, String created, String categoryId, String catName, List<ProductImg> imgList, String promotion_id, String promotion_name, String promotion_desc) {
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.created = created;
        this.categoryId = categoryId;
        this.catName = catName;
        this.imgList = imgList;
        this.promotion_id = promotion_id;
        this.promotion_name = promotion_name;
        this.promotion_desc = promotion_desc;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<ProductImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<ProductImg> imgList) {
        this.imgList = imgList;
    }

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

    public String getPromotion_desc() {
        return promotion_desc;
    }

    public void setPromotion_desc(String promotion_desc) {
        this.promotion_desc = promotion_desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productDesc);
        dest.writeDouble(this.productPrice);
        dest.writeString(this.created);
        dest.writeString(this.categoryId);
        dest.writeString(this.catName);
        dest.writeTypedList(this.imgList);
        dest.writeString(this.promotion_id);
        dest.writeString(this.promotion_name);
        dest.writeString(this.promotion_desc);
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
        dest.writeString(this.class_name);
        dest.writeString(this.room_no);
        dest.writeString(this.soi_name);
        dest.writeString(this.soi_zone);
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

    protected Product(Parcel in) {
        this.productId = in.readInt();
        this.productName = in.readString();
        this.productDesc = in.readString();
        this.productPrice = in.readDouble();
        this.created = in.readString();
        this.categoryId = in.readString();
        this.catName = in.readString();
        this.imgList = in.createTypedArrayList(ProductImg.CREATOR);
        this.promotion_id = in.readString();
        this.promotion_name = in.readString();
        this.promotion_desc = in.readString();
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
        this.class_name = in.readString();
        this.room_no = in.readString();
        this.soi_name = in.readString();
        this.soi_zone = in.readString();
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

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
