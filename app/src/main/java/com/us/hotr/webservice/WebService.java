package com.us.hotr.webservice;

/**
 * Created by liaobo on 4/27/2017.
 */

import android.content.Intent;

import com.us.hotr.storage.bean.Address;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.HotSearchTopic;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.storage.bean.SubjectDetail;
import com.us.hotr.storage.bean.Theme;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.storage.bean.User;
import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.webservice.request.AvailableVoucherRequest;
import com.us.hotr.webservice.request.BoundMobileRequest;
import com.us.hotr.webservice.request.CancelOrderRequest;
import com.us.hotr.webservice.request.ChangePasswordRequest;
import com.us.hotr.webservice.request.CreateMassageOrderRequest;
import com.us.hotr.webservice.request.CreatePartyOrderRequest;
import com.us.hotr.webservice.request.CreateProductOrderRequest;
import com.us.hotr.webservice.request.LoginAndRegisterRequest;
import com.us.hotr.webservice.request.LoginWithWechatRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.request.UpdateUserRequest;
import com.us.hotr.webservice.request.UploadCommentRequest;
import com.us.hotr.webservice.request.UploadReplyRequest;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.BaseResponse;
import com.us.hotr.webservice.response.GetAllGroupResponse;
import com.us.hotr.webservice.response.GetCaseDetailResponse;
import com.us.hotr.webservice.response.GetDoctorDetailResponse;
import com.us.hotr.webservice.response.GetGroupListbyUserResponse;
import com.us.hotr.webservice.response.GetGroupMainPageResponse;
import com.us.hotr.webservice.response.GetHomePageResponse;
import com.us.hotr.webservice.response.GetHospitalDetailResponse;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.response.GetMassageDetailResponse;
import com.us.hotr.webservice.response.GetMasseurDetailResponse;
import com.us.hotr.webservice.response.GetPartyDetailResponse;
import com.us.hotr.webservice.response.GetPartyOrderDetailResponse;
import com.us.hotr.webservice.response.GetPostDetailResponse;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.response.GetReceiptListResponse;
import com.us.hotr.webservice.response.GetSpaDetailResponse;
import com.us.hotr.webservice.response.GetWechatAccessTokenResponse;
import com.us.hotr.webservice.response.GetWechatUserInfo;
import com.us.hotr.webservice.response.UpdateUserAvatarRespone;
import com.us.hotr.webservice.response.UploadPostResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WebService {

    @POST
    Observable<GetWechatAccessTokenResponse> getWechatAccessToken(@Url String url,
                                                                  @Query("appid") String appid,
                                                                  @Query("secret") String secret,
                                                                  @Query("code") String code,
                                                                  @Query("grant_type") String grant_type);
    @POST
    Observable<GetWechatUserInfo> getWechatUserInfo(@Url String url,
                                                    @Query("access_token") String access_token,
                                                    @Query("openid") String openid);
    @POST("dictionary/splashScreen.do?")
    Observable<BaseResponse<List<Adv>>> getAdvList(@Query("width") int width,
                                                   @Query("height") int height,
                                                   @Query("type") int type);

    @POST("apiHomePage/homePageList.do?")
    Observable<BaseResponse<GetHomePageResponse>> getHomePage(@Header("jsessionid") String jsessionid,
                                                              @Query("provinceCode") long provinceCode,
                                                              @Query("cityCode") Long cityCode,
                                                              @Query("modelId") int modelId);

    @POST("type/getTypeAndProject.do")
    Observable<BaseResponse<List<Subject>>> getSubjectList();

    @POST("massage_type/list.do")
    Observable<BaseResponse<BaseListResponse<List<Subject>>>> getMassageTypeList();

    @POST("travel/getTravelState.do")
    Observable<BaseResponse<BaseListResponse<List<Integer>>>> getPartyStatusList();

    @POST("massage/type_list_by_massageId.do")
    Observable<BaseResponse<List<Type>>> getMassageTypeListBySpa(@Query("massage_id") Long massage_id);

    @POST("product/getTypeAndSumByHospitalId.do")
    Observable<BaseResponse<List<Type>>> getProductTypeListByHospital(@Query("hospital_id") Long hospital_id);

    @POST("product/getTypeAndSumByDoctorId.do")
    Observable<BaseResponse<List<Type>>> getProductTypeListByDoctor(@Query("doctor_id") Long doctor_id);


    @POST("area/listCity.do")
    Observable<BaseResponse<List<Provence>>> getCityList(@Query("model_id") int model_id);

    @POST("hospital/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Hospital>>>> getHospitalList(@Query("hospital_name") String hospital_name,
                                                                               @Query("city_code") Long city_code,
                                                                               @Query("type") Integer type,
                                                                               @Query("type_id") Long type_id,
                                                                               @Query("page_size") Integer page_size,
                                                                               @Query("page_number") int page_number);
    @POST("massage/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Spa>>>> getSpaList(@Query("massage_name") String massage_name,
                                                                     @Query("city_code") Long city_code,
                                                                     @Query("type") Integer type,
                                                                     @Query("type_id") Long type_id,
                                                                     @Query("pos_latitude") Double pos_latitude,
                                                                     @Query("pos_longitude") Double pos_longitude,
                                                                     @Query("page_size") Integer page_size,
                                                                     @Query("page_number") int page_number);

    @POST("product/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Product>>>> getProductList(@Query("product_name") String product_name,
                                                                             @Query("type") Integer type,
                                                                             @Query("hospitalId") Long hospitalId,
                                                                             @Query("doctorId") Long doctorId,
                                                                             @Query("projectId") Long project_id,
                                                                             @Query("city_code") Long city_id,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("page_number") int page_number);

    @POST("massage_product/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Massage>>>> getMassageList(@Query("product_name") String product_name,
                                                                             @Query("type") Integer type,
                                                                             @Query("type_id") Long type_id,
                                                                             @Query("city_code") Long city_code,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("page_number") int page_number);

    @POST("travel/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Party>>>> getPartyList(@Query("travel_name") String travel_name,
                                                                         @Query("type") int type,
                                                                         @Query("ticket_status[]") List<Integer> ticket_status,
                                                                         @Query("page_size") Integer page_size,
                                                                         @Query("page_number") int page_number);

    @POST("massage/product_list_by_massageId.do?")
    Observable<BaseResponse<BaseListResponse<List<Massage>>>> getMassageListBySpa(@Query("massage_id") long massage_id,
                                                                                  @Query("type_id") Long type_id,
                                                                                  @Query("page_number") int page_number,
                                                                                  @Query("page_size") Integer page_size);

    @POST("massage/product_list_by_massageId.do?page_number=1&page_size=3&type_id=0")
    Call<BaseResponse<BaseListResponse<List<Massage>>>> getMassageListBySpa1(@Query("massage_id") long massage_id);

    @POST("product/list.do?page_size=3&type=5")
    Call<BaseResponse<BaseListResponse<List<Product>>>> getHospitalProductList1(@Query("hospitalId") Long hospitalId);

    @POST("product/list.do?page_size=3&type=5")
    Call<BaseResponse<BaseListResponse<List<Product>>>> getDoctorProductList1(@Query("doctorId") Long doctorId);

    @POST("doctor/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Doctor>>>> getDoctorList(@Query("doctor_name") String doctor_name,
                                                                           @Query("type") Integer type,
                                                                           @Query("hospitalId") Long hospitalId,
                                                                           @Query("type_id") Long type_id,
                                                                           @Query("city_code") Long city_code,
                                                                           @Query("page_size") Integer page_size,
                                                                           @Query("page_number") int page_number);


    @POST("doctor/list.do?page_size=3&type=3")
    Call<BaseResponse<BaseListResponse<List<Doctor>>>> getDoctorList1(@Query("hospitalId") Long hospitalId);

    @POST("massagist/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurList(@Header("jsessionid") String jsessionid,
                                                                             @Query("massagist_name") String massagist_name,
                                                                             @Query("massage_id") Long massage_id,
                                                                             @Query("city_code") Long city_code,
                                                                             @Query("type_id") Long type_id,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_number") int page_number,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("type") Integer type);

    @POST("massage/massagist_list_by_massageId.do?page_number=1&page_size=4")
    Call<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurListBySpa1(@Header("jsessionid") String jsessionid,
                                                                             @Query("massage_id") Long massage_id);

    @POST("massage/massagist_list_by_massageId.do?")
    Observable<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurListBySpa(@Header("jsessionid") String jsessionid,
                                                                                  @Query("massage_id") long massage_id,
                                                                                  @Query("page_number") int page_number,
                                                                                  @Query("page_size") Integer page_size);

    @POST("project/detail.do?")
    Observable<BaseResponse<SubjectDetail>> getSubjectDetail(@Query("projectId") long projectId);

    @POST("massagist/detail.do?")
    Observable<BaseResponse<GetMasseurDetailResponse>> getMasseurDetail(@Query("massagist_id") long massagist_id,
                                                                        @Header("jsessionid") String jsessionid);

    @POST("massage_product/detail.do?")
    Observable<BaseResponse<GetMassageDetailResponse>> getMassageDetail(@Query("key") long key,
                                                                        @Header("jsessionid") String jsessionid);

    @POST("product/detail.do?")
    Observable<BaseResponse<GetProductDetailResponse>> getProductDetail(@Query("product_id") long product_id,
                                                                        @Header("jsessionid") String jsessionid);

    @POST("hospital/detail.do?")
    Observable<BaseResponse<GetHospitalDetailResponse>> getHospitalDetail(@Query("hospitalId") long hospitalId,
                                                                          @Header("jsessionid") String jsessionid);

    @POST("doctor/detail.do?")
    Observable<BaseResponse<GetDoctorDetailResponse>> getDoctorDetail(@Query("doctorId") long doctorId,
                                                                      @Header("jsessionid") String jsessionid);

    @POST("massage/detail.do?")
    Observable<BaseResponse<GetSpaDetailResponse>> getSpaDetail(@Query("key") long key,
                                                                @Header("jsessionid") String jsessionid);

    @POST("travel/detail.do?")
    Observable<BaseResponse<GetPartyDetailResponse>> getPartyDetail(@Query("travel_id") long travel_id,
                                                                    @Header("jsessionid") String jsessionid);

    @POST("travel/addAccessCountByTraveId.do?")
    Observable<BaseResponse<String>> increasePartyInterest(@Query("travel_id") long travel_id);

    @POST("user_method/coupon/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Voucher>>>> getAvaliableVoucher(@Header("jsessionid") String jsessionid,
                                                                                  @Body AvailableVoucherRequest request);

    @POST("user_method/coupon/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Voucher>>>> getAllVoucher(@Header("jsessionid") String jsessionid,
                                                                            @Query("status") int status,
                                                                            @Query("page_number") int page_number,
                                                                            @Query("page_size") Integer page_size);

    @POST("product/checkCount.do?")
    Observable<BaseResponse<Integer>> checkOrderCount(@Query("productId") long productId,
                                                      @Query("type") int type);

    @POST("travel/checkCount.do?")
    Call<BaseResponse<Integer>> checkPartyOrderCount(@Query("ticket_id") long ticket_id);

    @POST("product/checkPersonalCount.do?")
    Observable<BaseResponse<Integer>> checkPersonalCountProduct(@Header("jsessionid") String jsessionid,
                                                                @Query("productId") long productId);

    @POST("user_method/order/checkPersonalCount.do?")
    Observable<BaseResponse<Integer>> checkPersonalCountMassage(@Header("jsessionid") String jsessionid,
                                                                @Query("productId") long productId);

    @POST("user_method/product/order/create.do?")
    Observable<BaseResponse<ProductOrder>> createOrderProduct(@Header("jsessionid") String jsessionid,
                                                              @Body CreateProductOrderRequest request);

    @POST("user_method/massage/order/create.do?")
    Observable<BaseResponse<MassageOrder>> createOrderMassage(@Header("jsessionid") String jsessionid,
                                                              @Body CreateMassageOrderRequest request);

    @POST("user_method/travel/order/create.do?")
    Observable<BaseResponse<PartyOrder>> createOrderParty(@Header("jsessionid") String jsessionid,
                                                          @Body CreatePartyOrderRequest request);

    @POST("apiCoshow/recommendCoshowList.do?")
    Observable<BaseResponse<GetGroupMainPageResponse>> getGroupMainPage(@Header("jsessionid") String jsessionid);

    @POST("apiCoshow/myCoshowList.do?")
    Call<BaseResponse<GetGroupMainPageResponse>> getGroupSession(@Header("jsessionid") String jsessionid,
                                                                 @Query("userId") long userId);

    @POST("apiCoshow/user_method/attentionCoshow.do?")
    Observable<BaseResponse<String>> favoriteGroup(@Header("jsessionid") String jsessionid,
                                                   @Query("coshowId") long coshowId,
                                                   @Query("isAttention") int isAttention);

    @POST("apiTheme/listTheme.do")
    Observable<BaseResponse<List<Theme>>> getGroupTheme();

    @POST("apiCoshow/getCoshowList.do?")
    Observable<BaseResponse<List<Group>>> getGroup(@Query("themeId") long themeId,
                                                   @Query("coshowName") String coshowName,
                                                   @Header("jsessionid") String jsessionid);

    @POST("apiCoshow/allCoshowList.do?")
    Observable<BaseResponse<GetAllGroupResponse>> getAllGroup(@Header("jsessionid") String jsessionid);

    @POST("apiCoshow/findCoshowById.do?")
    Observable<BaseResponse<Group>> getGroupById(@Query("coshowId") long coshowId,
                                                 @Header("jsessionid") String jsessionid);


    @POST("apiTopic/findTopicByCoshowId.do")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getPostByGroup(@Query("coshow_id") long coshow_id,
                                                                          @Query("type") int type,
                                                                          @Query("page_number") int page_number,
                                                                          @Query("page_size") int page_size,
                                                                          @Header("jsessionid") String jsessionid);

    @POST("apiTopic/appTopicList.do")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getAllPost(@Header("jsessionid") String jsessionid,
                                                                      @Query("page_number") int page_number,
                                                                      @Query("page_size") int page_size);

    @POST("apiTopic/appTopicList.do")
    Call<BaseResponse<BaseListResponse<List<Post>>>> getAllPost1(@Header("jsessionid") String jsessionid,
                                                                 @Query("page_number") int page_number,
                                                                 @Query("page_size") int page_size);

    @POST("apiTopic/discovery.do")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getDiscoveryPost(@Header("jsessionid") String jsessionid,
                                                                            @Query("page_number") int page_number,
                                                                            @Query("page_size") int page_size);

    @POST("apiTopic/nearbyPeople.do")
    Observable<BaseResponse<BaseListResponse<List<User>>>> getNearbyPeople(@Query("currLon") String currLon,
                                                                           @Query("currLat") String currLat);

    @POST("apiTopic/nearbyHotTopic.do")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getNearbyPost(@Header("jsessionid") String jsessionid,
                                                                         @Query("currLon") String currLon,
                                                                         @Query("currLat") String currLat);

    @POST("apiTopic/hotTopicDetail.do")
    Observable<BaseResponse<GetPostDetailResponse>> getPostDetail(@Query("hotTopicId") long hotTopicId,
                                                                  @Header("jsessionid") String jsessionid);

    @Multipart
    @POST("image/user_method/upload_image_to_server.do?")
    Observable<BaseResponse<List<String>>> uploadMultiImage(@Query("targetPath") String targetPath,
                                                           @Header("jsessionid") String jsessionid,
                                                           @PartMap Map<String, RequestBody> images);

    @POST("apiTopic/user_method/addHotTopic.do?")
    Observable<BaseResponse<UploadPostResponse>> uploadPost(@Header("jsessionid") String jsessionid,
                                                            @Body Post hotTopic,
                                                            @Query("coshowId") List<Long> coshowId);

    @POST("apiTopic/user_method/userLikeTopic.do?")
    Observable<BaseResponse<String>> likePost(@Header("jsessionid") String jsessionid,
                                              @Query("hotTopicId") long hotTopicId,
                                              @Query("isLike") int isLike);

    @POST("apiTopic/userReadTopic.do?")
    Observable<BaseResponse<String>> readPost(@Query("hotTopicId") long hotTopicId);


    @POST("apiTopic/user_method/addComment.do?")
    Observable<BaseResponse<String>> uploadPostComment(@Header("jsessionid") String jsessionid,
                                                       @Body UploadCommentRequest request);

    @POST("apiTopic/user_method/deleComment.do?")
    Observable<BaseResponse<String>> deletePostComment(@Header("jsessionid") String jsessionid,
                                                       @Query("commentId") long commentId);

    @POST("apiTopic/user_method/userLikeComment.do?")
    Observable<BaseResponse<String>> likePostComment(@Header("jsessionid") String jsessionid,
                                                     @Query("commentId") long commentId,
                                                     @Query("isLike") int isLike);

    @POST("apiTopic/user_method/addReply.do?")
    Observable<BaseResponse<String>> uploadPostReply(@Header("jsessionid") String jsessionid,
                                                     @Body UploadReplyRequest request);

    @POST("apiTopic/user_method/deleReply.do?")
    Observable<BaseResponse<String>> deletePostReply(@Header("jsessionid") String jsessionid,
                                                     @Query("replyId") long replyId);

    @POST("apiYmContrastPhoto/appList.do")
    Observable<BaseResponse<BaseListResponse<List<Case>>>> getAllCase(@Header("jsessionid") String jsessionid,
                                                                      @Query("page_number") int page_number,
                                                                      @Query("page_size") int page_size);

    @POST("apiYmContrastPhoto/ymContrastPhotoDetail.do")
    Observable<BaseResponse<GetCaseDetailResponse>> getCaseDetail(@Query("ymContrastPhotoId") long ymContrastPhotoId,
                                                                  @Header("jsessionid") String jsessionid);

    @POST("apiYmContrastPhoto/user_method/addContrastPhoto.do?")
    Observable<BaseResponse<UploadPostResponse>> uploadCase(@Header("jsessionid") String jsessionid,
                                                            @Body Case hotTopic,
                                                            @Query("coshowId") List<Long> coshowId);

    @POST("apiYmContrastPhoto/user_method/userLikeContrastPhoto.do?")
    Observable<BaseResponse<String>> likeCase(@Header("jsessionid") String jsessionid,
                                              @Query("ymContrastPhotoId") long ymContrastPhotoId,
                                              @Query("isLike") int isLike);

    @POST("apiYmContrastPhoto/userReadContrastPhoto.do?")
    Observable<BaseResponse<String>> readCase(@Query("ymContrastPhotoId") long ymContrastPhotoId);


    @POST("apiYmContrastPhoto/user_method/addContrastPhotoComment.do?")
    Observable<BaseResponse<String>> uploadCaseComment(@Header("jsessionid") String jsessionid,
                                                       @Body UploadCommentRequest request);

    @POST("apiYmContrastPhoto/user_method/deleContrastPhotoComment.do?")
    Observable<BaseResponse<String>> deleteCaseComment(@Header("jsessionid") String jsessionid,
                                                       @Query("contrastPhotoCommentId") long contrastPhotoCommentId);

    @POST("apiYmContrastPhoto/user_method/userLikeContrastPhotoComment.do?")
    Observable<BaseResponse<String>> likeCaseComment(@Header("jsessionid") String jsessionid,
                                                     @Query("contrastPhotoCommentId") long contrastPhotoCommentId,
                                                     @Query("isLike") int isLike);

    @POST("apiYmContrastPhoto/user_method/addContrastPhotoReply.do?")
    Observable<BaseResponse<String>> uploadCaseReply(@Header("jsessionid") String jsessionid,
                                                     @Body UploadReplyRequest request);

    @POST("apiYmContrastPhoto/user_method/deleContrastPhotoReply.do?")
    Observable<BaseResponse<String>> deleteCaseReply(@Header("jsessionid") String jsessionid,
                                                     @Query("contrastPhotoReplyId") long contrastPhotoReplyId);

    @POST("apiYmContrastPhoto/purchasedProduct.do?")
    Observable<BaseResponse<BaseListResponse<List<Product>>>> getPurchasedProduct(@Header("jsessionid") String jsessionid);

    @POST("apiCoshow/myCoshowList.do?")
    Observable<BaseResponse<GetGroupListbyUserResponse>> getGroupListbyUser(@Query("userId") long userId);

    @POST("apiTopic/myTopicList.do?")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getPostListbyUser(@Header("jsessionid") String jsessionid,
                                                                             @Query("queryUserId") long queryUserId,
                                                                             @Query("page_number") int page_number,
                                                                             @Query("page_size") int page_size);

    @POST("apiYmContrastPhoto/myContrastPhotoList.do?")
    Observable<BaseResponse<BaseListResponse<List<Case>>>> getCaseListbyUser(@Header("jsessionid") String jsessionid,
                                                                             @Query("queryUserId") long queryUserId,
                                                                             @Query("page_number") int page_number,
                                                                             @Query("page_size") int page_size);

    @POST("user_method/verification/list.do?")
    Observable<BaseResponse<GetReceiptListResponse>> getReceiptList(@Header("jsessionid") String jsessionid,
                                                                    @Query("state") int state);

    @POST("user/login.do?")
    Observable<BaseResponse<GetLoginResponse>> login(@Query("username") String username,
                                                     @Query("password") String password);

    @POST("user/loginByValidCode.do?")
    Observable<BaseResponse<GetLoginResponse>> loginAndRegister(@Body LoginAndRegisterRequest request);

    @POST("user/loginByThird.do?")
    Observable<BaseResponse<GetLoginResponse>> loginWithWechat(@Body LoginWithWechatRequest request);

    @POST("user/validCode.do?")
    Observable<BaseResponse<String>> requestForValidationCode(@Body RequestForValidationCodeRequest request);

    @POST("user/sendChangePwdVerify.do?")
    Observable<BaseResponse<String>> requestForValidationCodePassword(@Body RequestForValidationCodeRequest request);

    @POST("user/sendbBoundlMobileVerify.do?")
    Observable<BaseResponse<String>> requestForValidationCodePhone(@Body RequestForValidationCodeRequest request);

    @POST("user/forgetPassword.do?")
    Observable<BaseResponse<String>> changePassword(@Body ChangePasswordRequest request);

    @POST("user/boundMobile.do?")
    Observable<BaseResponse<String>> boundMobile(@Header("jsessionid") String jsessionid,
                                                 @Body BoundMobileRequest request);


    @POST("user/user_method/detail.do")
    Observable<BaseResponse<User>> getMyDetail(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/updateUser.do")
    Observable<BaseResponse<String>> updateUserDetail(@Header("jsessionid") String jsessionid,
                                                      @Query("nickname") String nickname,
                                                      @Body UpdateUserRequest request);
    @Multipart
    @POST("user/user_method/uploadImage.do")
    Observable<BaseResponse<UpdateUserAvatarRespone>> updateUserAvatar(@Header("jsessionid") String jsessionid,
                                                                       @Part MultipartBody.Part file);

    @POST("user/user_method/addUserAttention.do?")
    Observable<BaseResponse<String>> favoritePeople(@Header("jsessionid") String jsessionid,
                                                    @Query("userToId") long userToId);

    @POST("user/user_method/deleUserAttention.do?")
    Observable<BaseResponse<String>> removeFavoritePeople(@Header("jsessionid") String jsessionid,
                                                          @Query("userToId") long userToId);

    @POST("user/user_method/followUserList.do?")
    Observable<BaseResponse<BaseListResponse<List<User>>>> getFavoritePeople(@Header("jsessionid") String jsessionid,
                                                                             @Query("page_number") int page_number,
                                                                             @Query("page_size") int page_size);

    @POST("user/user_method/fansUserList.do?")
    Observable<BaseResponse<BaseListResponse<List<User>>>> getFansPeople(@Header("jsessionid") String jsessionid,
                                                                         @Query("page_number") int page_number,
                                                                         @Query("page_size") int page_size);

    @POST("user/list.do?")
    Observable<BaseResponse<BaseListResponse<List<User>>>> getUserList(@Query("nick_name") String nick_name,
                                                                       @Query("page_number") int page_number,
                                                                       @Query("page_size") int page_size);

    @POST("user/userDetailById.do?")
    Observable<BaseResponse<User>> getUserDetail(@Header("jsessionid") String jsessionid,
                                                 @Query("user_id") long user_id);

    @POST("user/user_method/userCollectAction.do?")
    Observable<BaseResponse<String>> favoriteItem(@Header("jsessionid") String jsessionid,
                                                  @Query("collect_id") long collect_id,
                                                  @Query("collect_type") int collect_type);

    @POST("user/user_method/deleUserCollect.do?")
    Observable<BaseResponse<String>> removeFavoriteItem(@Header("jsessionid") String jsessionid,
                                                        @Query("collect_id_arr[]") List<Long> collect_id_arr,
                                                        @Query("collect_type") long collect_type);

    @POST("user/user_method/userCollectListByType.do?collect_type=0")
    Observable<BaseResponse<BaseListResponse<List<Party>>>> getCollectionParty(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=1")
    Observable<BaseResponse<BaseListResponse<List<Hospital>>>> getCollectionHospital(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=2")
    Observable<BaseResponse<BaseListResponse<List<Doctor>>>> getCollectionDoctor(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=3")
    Observable<BaseResponse<BaseListResponse<List<Product>>>> getCollectionProduct(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=4")
    Observable<BaseResponse<BaseListResponse<List<Spa>>>> getCollectionSpa(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=5")
    Observable<BaseResponse<BaseListResponse<List<Masseur>>>> getCollectionMasseur(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=6")
    Observable<BaseResponse<BaseListResponse<List<Massage>>>> getCollectionMassage(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=7")
    Observable<BaseResponse<BaseListResponse<List<Case>>>> getCollectionCase(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userCollectListByType.do?collect_type=8")
    Observable<BaseResponse<BaseListResponse<List<Post>>>> getCollectionPost(@Header("jsessionid") String jsessionid);

    @POST("user_method/order/hospitalList.do?")
    Observable<BaseResponse<BaseListResponse<List<ProductOrder>>>> getProductOrderList(@Header("jsessionid") String jsessionid,
                                                                                       @Query("payment_state") Integer payment_state,
                                                                                       @Query("page_number") int page_number,
                                                                                       @Query("page_size") Integer page_size);

    @POST("user_method/order/massageList.do?")
    Observable<BaseResponse<BaseListResponse<List<MassageOrder>>>> getMassageOrderList(@Header("jsessionid") String jsessionid,
                                                                                       @Query("payment_state") Integer payment_state,
                                                                                       @Query("page_number") int page_number,
                                                                                       @Query("page_size") Integer page_size);

    @POST("user_method/travel/order/travelList.do?")
    Observable<BaseResponse<BaseListResponse<List<PartyOrder>>>> getPartyOrderList(@Header("jsessionid") String jsessionid,
                                                                                   @Query("order_state") Integer order_state,
                                                                                   @Query("page_number") int page_number,
                                                                                   @Query("page_size") Integer page_size);

    @POST("user_method/order/hospitalDetail.do?")
    Observable<BaseResponse<ProductOrder>> getProductOrderDetail(@Header("jsessionid") String jsessionid,
                                                                 @Query("order_id") long order_id);

    @POST("user_method/order/massageDetail.do?")
    Observable<BaseResponse<MassageOrder>> getMassageOrderDetail(@Header("jsessionid") String jsessionid,
                                                                 @Query("order_id") long order_id);

    @POST("user_method/travel/order/travelDetail.do?")
    Observable<BaseResponse<GetPartyOrderDetailResponse>> getPartyOrderDetail(@Header("jsessionid") String jsessionid,
                                                                              @Query("order_id") long order_id);

    @POST("user_method/product/order/cancel.do?")
    Observable<BaseResponse<ProductOrder>> cancelProductOrder(@Header("jsessionid") String jsessionid,
                                                              @Body CancelOrderRequest request);

    @POST("user_method/massage/order/cancel.do?")
    Observable<BaseResponse<MassageOrder>> cancelMassageOrder(@Header("jsessionid") String jsessionid,
                                                              @Body CancelOrderRequest request);

    @POST("user_method/travel/order/cancel.do?")
    Observable<BaseResponse<PartyOrder>> cancelPartyOrder(@Header("jsessionid") String jsessionid,
                                                          @Body CancelOrderRequest request);

    @POST("user/user_method/addUserFeedBack.do?")
    Observable<BaseResponse<String>> userFeedback(@Header("jsessionid") String jsessionid,
                                                  @Query("feedback_content") String feedback_content,
                                                  @Query("user_telephone") String user_telephone);

    @POST("user/user_method/addressList.do?")
    Observable<BaseResponse<List<Address>>> getDeliveryAddressList(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/userShippingAddress.do?")
    Observable<BaseResponse<String>> addDeliveryAddress(@Header("jsessionid") String jsessionid,
                                                        @Body Address request);

    @POST("user/user_method/addressDefault.do?")
    Observable<BaseResponse<String>> setDefaultDeliveryAddress(@Header("jsessionid") String jsessionid,
                                                               @Query("address_id") String address_id);

    @POST("user/user_method/deleAddress.do?")
    Observable<BaseResponse<String>> deleteDeliveryAddress(@Header("jsessionid") String jsessionid,
                                                           @Query("address_id") long address_id);

    @POST("user/user_method/editAddress.do?")
    Observable<BaseResponse<String>> editDeliveryAddress(@Header("jsessionid") String jsessionid,
                                                         @Body Address request);

    @POST("project/isHotList.do?")
    Observable<BaseResponse<BaseListResponse<List<HotSearchTopic>>>> getHotSearchTopic(@Header("jsessionid") String jsessionid);
}
