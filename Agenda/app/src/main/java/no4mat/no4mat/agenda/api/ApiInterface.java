package no4mat.no4mat.agenda.api;


import java.util.List;

import no4mat.no4mat.agenda.AData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    public final static String BASE_URL = "http://192.168.8.6:3000/";

    @GET("agenda")
    Call<List<AData>> getEntries();

    @POST("agenda")
    Call<AData> addEntry(@Body AData AData);

    @DELETE("agenda/{id}")
    Call<AData> deleteEntry(@Path("id") int id);
}
