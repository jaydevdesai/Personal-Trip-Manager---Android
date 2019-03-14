package team.project.tripmanager.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import team.project.tripmanager.model.AuthResponse;
import team.project.tripmanager.model.CommonResponse;

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
    Call<CommonResponse> getTrips();

    @POST("create_trip")
    @FormUrlEncoded
    Call<CommonResponse> createTrip(@Field("tripName") String tripName,
                                    @Field("placeName") String placeName,
                                    @Field("startDate") String startDate,
                                    @Field("endDate") String endDate);

    @POST("get_queries")
    Call<CommonResponse> getQueries();

    @POST("post_query")
    @FormUrlEncoded
    Call<CommonResponse> postQuery(@Field("query_text") String query_text);

    @POST("get_query_replies")
    @FormUrlEncoded
    Call<CommonResponse> getQueriesReplies(@Field("query_id") Integer query_id);

    @POST("post_query_reply")
    @FormUrlEncoded
    Call<CommonResponse> postQueryReply(@Field("query_id") Integer query_id,
                                        @Field("reply_text") String reply_text);
}
