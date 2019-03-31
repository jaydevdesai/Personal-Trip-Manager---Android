package team.project.tripmanager.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("get_documents")
    Call<CommonResponse> getDocuments();

    @Multipart
    @POST("upload_document")
    Call<CommonResponse> uploadDocument(@Part("document_name") RequestBody documentName,
                                        @Part MultipartBody.Part documentImage);

    @POST("get_reservations")
    @FormUrlEncoded
    Call<CommonResponse> getReservations(@Field("trip_id") Integer tripId);

    @Multipart
    @POST("upload_reservation")
    Call<CommonResponse> uploadReservation(@Part("trip_id") RequestBody tripId,
                                           @Part("reservation_name") RequestBody reservationName,
                                           @Part MultipartBody.Part reservationImage);

    @POST("get_note")
    @FormUrlEncoded
    Call<CommonResponse> getNote(@Field("tripId") Integer tripId);

    @POST("update_note")
    @FormUrlEncoded
    Call<CommonResponse> updateNote(@Field("tripId") Integer tripId,
                                    @Field("noteId") Integer noteId,
                                    @Field("noteText") String noteText);
}
