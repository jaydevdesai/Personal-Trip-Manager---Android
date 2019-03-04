package team.project.tripmanager.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import team.project.tripmanager.model.AuthResponse;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.model.TripResponse;

public interface APIService {
    @POST("login")
    @FormUrlEncoded
    Call<AuthResponse> login(@Field("email") String email,
                             @Field("password") String password);

    @POST("sign_up")
    @FormUrlEncoded
    Call<AuthResponse> signUp(@Field("email") String email,
                             @Field("password") String password);

    @POST("get_trips")
    Call<TripResponse> getTrips();

    @POST("create_trip")
    @FormUrlEncoded
    Call<TripResponse> createTrip(@Field("tripName") String tripName,
                                  @Field("placeName") String placeName,
                                  @Field("startDate") String startDate,
                                  @Field("endDate") String endDate);
}
