package algonquin.cst2335.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SunriseItem implements Parcelable  {

    private long id; // Database ID
    private double latitude;
    private double longitude;
    private String sunriseTime;
    private String sunsetTime;

    // Constructors, getters, setters, and Parcelable implementation

    public SunriseItem() {
        // Default constructor for Room
    }

    public SunriseItem(double latitude, double longitude, String sunriseTime, String sunsetTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
    }

    protected SunriseItem(Parcel in) {
        id = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        sunriseTime = in.readString();
        sunsetTime = in.readString();
    }

    public static final Creator<SunriseItem> CREATOR = new Creator<SunriseItem>() {
        @Override
        public SunriseItem createFromParcel(Parcel in) {
            return new SunriseItem(in);
        }

        @Override
        public SunriseItem[] newArray(int size) {
            return new SunriseItem[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(sunriseTime);
        dest.writeString(sunsetTime);
    }

    // Parcelable implementation (you can generate this using Android Studio or implement it manually)
}
