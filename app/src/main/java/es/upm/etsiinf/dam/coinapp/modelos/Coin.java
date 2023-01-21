package es.upm.etsiinf.dam.coinapp.modelos;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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


    public Coin(){}


    public int sizeForList(){
        return 11;
    }
    public HashMap<String,Object> getElementForList(int position){
        HashMap<String, Object> result= new HashMap<String, Object>();
        switch (position){
            case 1:
                if(Double.valueOf(current_price) == null || Double.isNaN(current_price)) return null;
                result.put("Price", current_price);
                return result;
            case 2:
                if(Double.valueOf(low_24h) == null || Double.valueOf(high_24h) == null ||Double.isNaN(low_24h) || Double.isNaN(high_24h) || low_24h==0.0 ||high_24h==0.0) return null;
                result.put("24h Low", low_24h);
                result.put("24h High", high_24h);
                return result;
            case 3:
                if(Double.valueOf(total_volume) == null || Double.isNaN(total_volume) || total_volume==0.0) return null;
                result.put("Total Volume", total_volume);
                return result;
            case 4:
                result.put("Market Cap Rank", "#"+market_cap_rank);
                return result;
            case 5:
                if(Double.valueOf(market_cap) == null || Double.isNaN(market_cap)) return null;
                result.put("Market Cap", "$"+market_cap);
                return result;
            case 6:
                if(Double.valueOf(circulating_supply) == null || Double.isNaN(circulating_supply) || circulating_supply==0.0) return null;
                result.put("Circulating Supply", circulating_supply);
                return result;
            case 7:
                if(Double.valueOf(total_supply) == null || Double.isNaN(total_supply) || total_supply==0.0) return null;
                result.put("Total Supply", total_supply);
                return result;
            case 8:
                if(Double.valueOf(max_supply) == null || Double.isNaN(max_supply) || max_supply==0.0) return null;
                result.put("Max Supply", max_supply);
                return result;
            case 9:
                if(Double.valueOf(fully_diluted_valuation) == null || Double.isNaN(fully_diluted_valuation) || fully_diluted_valuation==0.0) return null;
                result.put("Fully Diluted Valuation", fully_diluted_valuation);
                return result;
            case 10:
                if(Double.valueOf(ath) == null || ath_date==null || Double.valueOf(ath_change_percentage) == null || Double.isNaN(ath) ||Double.isNaN(ath_change_percentage) || ath_date.isEmpty()) return null;
                result.put("All-Time High", ath);
                result.put("percentage ath",ath_change_percentage);
                result.put("Date ath",ath_date);

                return result;
            case 11:
                if(Double.valueOf(atl) == null || Double.valueOf(atl_change_percentage) == null || atl_date==null || Double.isNaN(atl) ||Double.isNaN(atl_change_percentage) || atl_date.isEmpty()) return null;
                result.put("All-Time Low", atl);
                result.put("percentage atl",atl_change_percentage);
                result.put("Date atl",atl_date);

                return result;
            default:
                return null;
        }
    }

    public List<HashMap<String,Object>> getListOfElementsForAdapter(){
        List<HashMap<String,Object>> result = new LinkedList<>();

        for(int i=1; i<sizeForList(); i++){
            HashMap<String,Object> element = (HashMap<String, Object>) getElementForList(i);
            if(element==null) result.add(null);
            else result.add(element);
        }

        return result;
    }


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

    public static class Roi {
        private double times;
        private String currency;
        private double percentage;

        public Roi () {
        }

        public double getTimes () {
            return times;
        }

        public void setTimes (double times) {
            this.times = times;
        }

        public String getCurrency () {
            return currency;
        }

        public void setCurrency (String currency) {
            this.currency = currency;
        }

        public double getPercentage () {
            return percentage;
        }

        public void setPercentage (double percentage) {
            this.percentage = percentage;
        }


        public String toJson () {
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
    }


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


