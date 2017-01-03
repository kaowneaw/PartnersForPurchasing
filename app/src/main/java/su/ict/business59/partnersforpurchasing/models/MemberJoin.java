package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kaowneaw on 1/1/2017.
 */

public class MemberJoin extends User implements Parcelable {

    int join_id;
    String date;
    int post_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.join_id);
        dest.writeString(this.date);
        dest.writeInt(this.post_id);
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.image_url);
        dest.writeString(this.role);
        dest.writeString(this.message);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }


    protected MemberJoin(Parcel in) {
        this.join_id = in.readInt();
        this.date = in.readString();
        this.post_id = in.readInt();
        this.user_id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.image_url = in.readString();
        this.role = in.readString();
        this.message = in.readString();
        this.status = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MemberJoin> CREATOR = new Parcelable.Creator<MemberJoin>() {
        @Override
        public MemberJoin createFromParcel(Parcel source) {
            return new MemberJoin(source);
        }

        @Override
        public MemberJoin[] newArray(int size) {
            return new MemberJoin[size];
        }
    };
}
