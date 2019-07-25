package com.madebyk.android.mdchatapp.DataModel;

import android.os.Parcel;
import android.os.Parcelable;
import com.madebyk.android.mdchatapp.R;
import org.jetbrains.annotations.NotNull;

public class UserInfo implements Parcelable {

    private String username, avatar, location, age;
    private String email;
    private String id;
    private long dateCreated;

    public UserInfo() {
        populateToNull();
    }

    public UserInfo(@NotNull String username, @org.jetbrains.annotations.Nullable String uid, @NotNull String email) {
        populateToNull();
        this.username = username;
        this.id = uid;
        this.email = email;
    }

    public UserInfo(String username, String location, String age, String id, String email) {
        populateToNull();
        this.username = username;
        this.location = location;
        this.age = age;
        this.id = id;
        this.email = email;
    }

    public UserInfo(String username, String id, String email, long date) {
        populateToNull();
        this.username = username;
        this.id = id;
        this.email = email;
        dateCreated = date;
        //avatar = To generic avatar
    }

    protected UserInfo(Parcel in) {
        username = in.readString();
        avatar = in.readString();
        location = in.readString();
        age = in.readString();
        email = in.readString();
        id = in.readString();
        dateCreated = in.readLong();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    private void populateToNull(){
        this.username = null;
        this.avatar = "drawable://" + R.drawable.avatar;
        this.location = null;
        this.age = null;
        this.id = null;
        this.email = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", location='" + location + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(avatar);
        parcel.writeString(location);
        parcel.writeString(age);
        parcel.writeString(email);
        parcel.writeString(id);
        parcel.writeLong(dateCreated);
    }
}