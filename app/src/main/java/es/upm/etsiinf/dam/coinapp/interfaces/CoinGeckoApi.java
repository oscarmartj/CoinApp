package es.upm.etsiinf.dam.coinapp.interfaces;

import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.CoinMarketData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGeckoApi {
    @GET("coins/markets")
    Call<List<CoinMarketData>> getTopCoins(@Query("vs_currency") String currency,
                                           @Query("order") String order,
                                           @Query("per_page") int limit,
                                           @Query("page") int page);
}

