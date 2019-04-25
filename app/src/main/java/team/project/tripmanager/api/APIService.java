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
import team.project.tripmanager.model.Note;
import team.project.tripmanager.model.Profile;
import team.project.tripmanager.model.Trip;

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

    @POST("get_user_queries")
    Call<CommonResponse> getUserQueries();

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
    @POST("get_shopping_list")
    @FormUrlEncoded
    Call<CommonResponse> getShoppingList(@Field("trip_id") Integer tripId);

    @POST("add_shopping_item")
    @FormUrlEncoded
    Call<CommonResponse> addShoppingItem(@Field("trip_id") Integer tripId,
                                    @Field("item_name") String itemName,
                                    @Field("bought") Boolean bought);

    @POST("get_explore_trips")
    @FormUrlEncoded
    Call<CommonResponse> getExploreTrips(@Field("last_created_at") String lastCreatedAt);

    @POST("get_profile")
    Call<CommonResponse> getProfile();

    @POST("get_expense")
    @FormUrlEncoded
    Call<CommonResponse> getExpense(@Field("tripId") Integer tripId);

    @POST("add_expense")
    @FormUrlEncoded
    Call<CommonResponse> addExpense(@Field("name") String name,
                                    @Field("tripId") Integer tripId,
                                    @Field("price") String price,
                                    @Field("cash") Integer cash,
                                    @Field("purchaseDate") String purchaseDate);

    @POST("add_cash")
    @FormUrlEncoded
    Call<CommonResponse> addCash(@Field("cashBalance") Double cashBalance,
                                    @Field("tripId") Integer tripId);

    @Multipart
    @POST("set_profile")
    Call<CommonResponse> setProfile(@Part("userName") RequestBody userName,
                                           @Part("birthDate") RequestBody birthDate,
                                           @Part MultipartBody.Part profile_image);

    @POST("update_profile")
    @FormUrlEncoded
    Call<CommonResponse> updateProfile(@Field("userName") String userName,
                                 @Field("birthDate") String birthDate);

    @POST("change_password")
    @FormUrlEncoded
    Call<CommonResponse> changePassword(@Field("oldPassword") String oldPassword,
                                       @Field("newPassword") String newPassword);

    @POST("edit_trip")
    @FormUrlEncoded
    Call<CommonResponse> editTrip(@Field("tripId") Integer tripId,
                                    @Field("tripName") String tripName,
                                    @Field("placeName") String placeName,
                                    @Field("startDate") String startDate,
                                    @Field("endDate") String endDate);

    @POST("delete_trip")
    @FormUrlEncoded
    Call<CommonResponse> deleteTrip(@Field("tripId") Integer tripId);

    @POST("delete_query")
    @FormUrlEncoded
    Call<CommonResponse> deleteQuery(@Field("queryId") Integer queryId);

    @POST("delete_query_reply")
    @FormUrlEncoded
    Call<CommonResponse> deleteQueryReply(@Field("queryReplyId") Integer queryReplyId);

    @POST("delete_shopping_item")
    @FormUrlEncoded
    Call<CommonResponse> deleteItem(@Field("item_id") Integer item_id);
}
