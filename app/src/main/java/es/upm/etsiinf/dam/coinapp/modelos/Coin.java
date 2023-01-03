package es.upm.etsiinf.dam.coinapp.modelos;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Coin{
    private String id;
    private String symbol;
    private String name;
    private String image;
    private double current_price;
    private double market_cap;
    private int market_cap_rank;
    private double fully_diluted_valuation;
    private double total_volume;
    private double high_24h;
    private double low_24h;
    private double price_change_24h;
    private double price_change_percentage_24h;
    private double market_cap_change_24h;
    private double market_cap_change_percentage_24h;
    private double circulating_supply;
    private double total_supply;
    private double max_supply;
    private double ath;
    private double ath_change_percentage;
    private String ath_date;
    private double atl;
    private double atl_change_percentage;
    private String atl_date;
    private Roi roi;
    private String last_updated;

    //imagenes
    private Bitmap imageBitmap;
    private byte[] imageBytes;


    /*
    protected Coin(Parcel in) {
        this.id = in.readString();
        this.symbol = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.current_price = in.readDouble();
        this.market_cap = in.readDouble();
        this.market_cap_rank = in.readInt();
        this.fully_diluted_valuation = in.readDouble();
        this.total_volume = in.readDouble();
        this.high_24h = in.readDouble();
        this.low_24h = in.readDouble();
        this.price_change_24h = in.readDouble();
        this.price_change_percentage_24h = in.readDouble();
        this.market_cap_change_24h = in.readDouble();
        this.market_cap_change_percentage_24h = in.readDouble();
        this.circulating_supply = in.readDouble();
        this.total_supply = in.readDouble();
        this.max_supply = in.readDouble();
        this.ath = in.readDouble();
        this.ath_change_percentage = in.readDouble();
        this.ath_date = in.readString();
        this.atl = in.readDouble();
        this.atl_change_percentage = in.readDouble();
        this.atl_date = in.readString();
        this.roi = in.readParcelable(Roi.class.getClassLoader());
        this.last_updated = in.readString();
        this.imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.imageBytes = in.createByteArray();
    }*/

    public Coin(){}

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public double getCurrent_price () {
        return current_price;
    }

    public void setCurrent_price (double current_price) {
        this.current_price = current_price;
    }

    public double getMarket_cap () {
        return market_cap;
    }

    public void setMarket_cap (double market_cap) {
        this.market_cap = market_cap;
    }

    public int getMarket_cap_rank () {
        return market_cap_rank;
    }

    public void setMarket_cap_rank (int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public double getFully_diluted_valuation () {
        return fully_diluted_valuation;
    }

    public void setFully_diluted_valuation (double fully_diluted_valuation) {
        this.fully_diluted_valuation = fully_diluted_valuation;
    }

    public double getTotal_volume () {
        return total_volume;
    }

    public void setTotal_volume (double total_volume) {
        this.total_volume = total_volume;
    }

    public double getHigh_24h () {
        return high_24h;
    }

    public void setHigh_24h (double high_24h) {
        this.high_24h = high_24h;
    }

    public double getLow_24h () {
        return low_24h;
    }

    public void setLow_24h (double low_24h) {
        this.low_24h = low_24h;
    }

    public double getPrice_change_24h () {
        return price_change_24h;
    }

    public void setPrice_change_24h (double price_change_24h) {
        this.price_change_24h = price_change_24h;
    }

    public double getPrice_change_percentage_24h () {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h (double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public double getMarket_cap_change_24h () {
        return market_cap_change_24h;
    }

    public void setMarket_cap_change_24h (double market_cap_change_24h) {
        this.market_cap_change_24h = market_cap_change_24h;
    }

    public double getMarket_cap_change_percentage_24h () {
        return market_cap_change_percentage_24h;
    }

    public void setMarket_cap_change_percentage_24h (double market_cap_change_percentage_24h) {
        this.market_cap_change_percentage_24h = market_cap_change_percentage_24h;
    }

    public double getCirculating_supply () {
        return circulating_supply;
    }

    public void setCirculating_supply (double circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public double getTotal_supply () {
        return total_supply;
    }

    public void setTotal_supply (double total_supply) {
        this.total_supply = total_supply;
    }

    public double getMax_supply () {
        return max_supply;
    }

    public void setMax_supply (double max_supply) {
        this.max_supply = max_supply;
    }

    public double getAth () {
        return ath;
    }

    public void setAth (double ath) {
        this.ath = ath;
    }

    public double getAth_change_percentage () {
        return ath_change_percentage;
    }

    public void setAth_change_percentage (double ath_change_percentage) {
        this.ath_change_percentage = ath_change_percentage;
    }

    public double getAtl () {
        return atl;
    }

    public void setAtl (double atl) {
        this.atl = atl;
    }

    public double getAtl_change_percentage () {
        return atl_change_percentage;
    }

    public void setAtl_change_percentage (double atl_change_percentage) {
        this.atl_change_percentage = atl_change_percentage;
    }

    public Roi getRoi () {
        return roi;
    }

    public void setRoi (Roi roi) {
        this.roi = roi;
    }

    public String getAth_date () {
        return ath_date;
    }

    public void setAth_date (String ath_date) {
        this.ath_date = ath_date;
    }

    public String getAtl_date () {
        return atl_date;
    }

    public void setAtl_date (String atl_date) {
        this.atl_date = atl_date;
    }

    public String getLast_updated () {
        return last_updated;
    }

    public void setLast_updated (String last_updated) {
        this.last_updated = last_updated;
    }

    public Bitmap getImageBitmap () {
        return imageBitmap;
    }

    public void setImageBitmap (Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public byte[] getImageBytes () {
        return imageBytes;
    }

    public void setImageBytes (byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public static class Roi{
        private double times;
        private String currency;
        private double percentage;

        public Roi(){}

        /*
        protected Roi(Parcel in) {
            this.times = in.readDouble();
            this.currency = in.readString();
            this.percentage = in.readDouble();
        }*/

        public double getTimes(){
            return times;
        }

        public void setTimes(double times){
            this.times = times;
        }

        public String getCurrency(){
            return currency;
        }

        public void setCurrency(String currency){
            this.currency=currency;
        }

        public double getPercentage () {
            return percentage;
        }

        public void setPercentage (double percentage) {
            this.percentage = percentage;
        }


        public String toJson() {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("times", this.times)
                        .put("currency", this.currency)
                        .put("percentage", this.percentage);

                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel parcel, int i) {
            parcel.writeDouble(this.times);
            parcel.writeString(this.currency);
            parcel.writeDouble(this.percentage);
        }

        public static final Parcelable.Creator<Roi> CREATOR = new Parcelable.Creator<Roi>() {
            @Override
            public Roi createFromParcel(Parcel source) {
                return new Roi(source);
            }

            @Override
            public Roi[] newArray(int size) {
                return new Roi[size];
            }
        };*/

    }

    /*
    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.id);
        dest.writeString(this.symbol);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeDouble(this.current_price);
        dest.writeDouble(this.market_cap);
        dest.writeInt(this.market_cap_rank);
        dest.writeDouble(this.fully_diluted_valuation);
        dest.writeDouble(this.total_volume);
        dest.writeDouble(this.high_24h);
        dest.writeDouble(this.low_24h);
        dest.writeDouble(this.price_change_24h);
        dest.writeDouble(this.price_change_percentage_24h);
        dest.writeDouble(this.market_cap_change_24h);
        dest.writeDouble(this.market_cap_change_percentage_24h);
        dest.writeDouble(this.circulating_supply);
        dest.writeDouble(this.total_supply);
        dest.writeDouble(this.max_supply);
        dest.writeDouble(this.ath);
        dest.writeDouble(this.ath_change_percentage);
        dest.writeString(this.ath_date);
        dest.writeDouble(this.atl);
        dest.writeDouble(this.atl_change_percentage);
        dest.writeString(this.atl_date);
        dest.writeParcelable(this.roi, i);
        dest.writeString(this.last_updated);
        dest.writeByteArray(this.imageBytes);
    }
    public static final Parcelable.Creator<Coin> CREATOR = new Parcelable.Creator<Coin>() {
        @Override
        public Coin createFromParcel(Parcel source) {
            return new Coin(source);
        }

        @Override
        public Coin[] newArray(int size) {
            return new Coin[size];
        }
    };*/

    @Override
    public String toString () {
        return "Coin{" +
                "s='" + symbol + '\'' +
                ", n=" + market_cap_rank +
                ", p=" + current_price +
                '}';
    }

    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return name.equals(coin.name);
    }

    @Override
    public int hashCode () {
        return Objects.hash(name);
    }
}


