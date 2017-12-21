package com.us.hotr.webservice;

/**
 * Created by liaobo on 4/27/2017.
 */

import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.storage.bean.SubjectDetail;
import com.us.hotr.storage.bean.TypeWithCount;
import com.us.hotr.storage.bean.User;
import com.us.hotr.webservice.request.UpdateUserRequest;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.BaseResponse;
import com.us.hotr.webservice.response.GetDoctorDetailResponse;
import com.us.hotr.webservice.response.GetHospitalDetailResponse;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.response.GetMassageDetailResponse;
import com.us.hotr.webservice.response.GetMasseurDetailResponse;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.response.GetSpaDetailResponse;
import com.us.hotr.webservice.response.GetWechatAccessTokenResponse;
import com.us.hotr.webservice.response.GetWechatUserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WebService {

    @GET
    Observable<GetWechatAccessTokenResponse> getWechatAccessToken(@Url String url,
                                                                  @Query("appid") String appid,
                                                                  @Query("secret") String secret,
                                                                  @Query("code") String code,
                                                                  @Query("grant_type") String grant_type);
    @GET
    Observable<GetWechatUserInfo> getWechatUserInfo(@Url String url,
                                                    @Query("access_token") String access_token,
                                                    @Query("openid") String openid);

    @GET("dictionary/splashScreen.do?")
    Observable<BaseResponse<List<Adv>>> getAdvList(@Query("width") int width,
                                                   @Query("height") int height,
                                                   @Query("type") int type);

    @GET("type/getTypeAndProject.do")
    Observable<BaseResponse<List<Subject>>> getSubjectList();

    @GET("massage_type/list.do")
    Observable<BaseResponse<BaseListResponse<List<Subject>>>> getMassageTypeList();

    @GET("massage/type_list_by_massageId.do")
    Observable<BaseResponse<List<TypeWithCount>>> getMassageTypeListBySpa(@Query("massage_id") Integer massage_id);

    @GET("area/listCity.do")
    Observable<BaseResponse<List<Provence>>> getCityList();

    @GET("hospital/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Hospital>>>> getHospitalList(@Query("city_code") Integer city_code,
                                                                               @Query("type") Integer type,
                                                                               @Query("type_id") Integer type_id,
                                                                               @Query("page_size") Integer page_size,
                                                                               @Query("page_number") int page_number);

    @GET("massage/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Spa>>>> getSpaList(@Query("city_code") Integer city_code,
                                                                     @Query("type") Integer type,
                                                                     @Query("type_id") Integer type_id,
                                                                     @Query("pos_latitude") Double pos_latitude,
                                                                     @Query("pos_longitude") Double pos_longitude,
                                                                     @Query("page_size") Integer page_size,
                                                                     @Query("page_number") int page_number);

    @GET("product/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Product>>>> getProductList(@Query("type") Integer type,
                                                                             @Query("hospitalId") Integer hospitalId,
                                                                             @Query("doctorId") Integer doctorId,
                                                                             @Query("projectId") Integer project_id,
                                                                             @Query("city_id") Integer city_id,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("page_number") int page_number);

    @GET("massage_product/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Massage>>>> getMassageList(@Query("type") Integer type,
                                                                             @Query("type_id") Integer type_id,
                                                                             @Query("city_code") Integer city_code,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("page_number") int page_number);
    @GET("massage/product_list_by_massageId.do?")
    Observable<BaseResponse<BaseListResponse<List<Massage>>>> getMassageListBySpa(@Query("massage_id") int massage_id,
                                                                                    @Query("type_id") int type_id,
                                                                                    @Query("page_number") int page_number,
                                                                                    @Query("page_size") Integer page_size);

    @GET("massage/product_list_by_massageId.do?page_number=1&page_size=4&type_id=0")
    Call<BaseResponse<BaseListResponse<List<Massage>>>> getMassageListBySpa1(@Query("massage_id") int massage_id);

    @GET("product/list.do?page_size=3&type=5")
    Call<BaseResponse<BaseListResponse<List<Product>>>> getHospitalProductList1(@Query("hospitalId") Integer hospitalId);

    @GET("product/list.do?page_size=3&type=5")
    Call<BaseResponse<BaseListResponse<List<Product>>>> getDoctorProductList1(@Query("doctorId") Integer doctorId);

    @GET("doctor/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Doctor>>>> getDoctorList(@Query("type") Integer type,
                                                                           @Query("hospitalId") Integer hospitalId,
                                                                           @Query("project_id") Integer project_id,
                                                                           @Query("city_id") Integer city_id,
                                                                           @Query("page_size") Integer page_size,
                                                                           @Query("page_number") int page_number);


    @GET("doctor/list.do?page_size=3&type=3")
    Call<BaseResponse<BaseListResponse<List<Doctor>>>> getDoctorList1(@Query("hospitalId") Integer hospitalId);

    @GET("massagist/list.do?")
    Observable<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurList(@Query("massage_id") Integer massage_id,
                                                                             @Query("city_code") Integer city_code,
                                                                             @Query("type_id") Integer type_id,
                                                                             @Query("pos_latitude") Double pos_latitude,
                                                                             @Query("pos_longitude") Double pos_longitude,
                                                                             @Query("page_number") int page_number,
                                                                             @Query("page_size") Integer page_size,
                                                                             @Query("type") Integer type);

    @GET("massage/massagist_list_by_massageId.do?page_number=1&page_size=4")
    Call<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurListBySpa1(@Query("massage_id") Integer massage_id);

    @GET("massage/massagist_list_by_massageId.do?")
    Observable<BaseResponse<BaseListResponse<List<Masseur>>>> getMasseurListBySpa(@Query("massage_id") int massage_id,
                                                                                    @Query("page_number") int page_number,
                                                                                    @Query("page_size") Integer page_size);

    @GET("project/detail.do?")
    Observable<BaseResponse<SubjectDetail>> getSubjectDetail(@Query("projectId") int projectId);

    @GET("massagist/detail.do?")
    Observable<BaseResponse<GetMasseurDetailResponse>> getMasseurDetail(@Query("massagist_id") int massagist_id);

    @GET("massage_product/detail.do?")
    Observable<BaseResponse<GetMassageDetailResponse>> getMassageDetail(@Query("key") int key);

    @GET("product/detail.do?")
    Observable<BaseResponse<GetProductDetailResponse>> getProductDetail(@Query("product_id") int product_id);

    @GET("hospital/detail.do?")
    Observable<BaseResponse<GetHospitalDetailResponse>> getHospitalDetail(@Query("hospitalId") int hospitalId);

    @GET("doctor/detail.do?")
    Observable<BaseResponse<GetDoctorDetailResponse>> getDoctorDetail(@Query("doctorId") int doctorId);

    @GET("massage/detail.do?")
    Observable<BaseResponse<GetSpaDetailResponse>> getSpaDetail(@Query("key") int key);


    @GET("user/login.do?")
    Observable<BaseResponse<GetLoginResponse>> login(@Query("username") String username,
                                                     @Query("password") String password);

    @GET("user/user_method/detail.do")
    Observable<BaseResponse<User>> getUserDetail(@Header("jsessionid") String jsessionid);

    @POST("user/user_method/updateUser.do")
    Observable<BaseResponse<String>> updateUserDetail(@Header("jsessionid") String jsessionid,
                                                   @Query("nickname") String nickname,
                                                   @Body UpdateUserRequest request);

//    @GET("services/public/jb/job/featuredjobs")
//    Call<GetFeaturedJobsListResponse> getFeatureJobs();
//    @GET("services/public/jb/job/weeklyjobs")
//    Call<GetHighlightedJobsListResponse> getHighlightedJobs();
//    @GET("services/public/jb/job/jobdirectory/search")
//    Call<GetHighlightedJobsListResponse> getSearchJobs(@Query("query") String solrQuery);

    //    @GET("tex/tags")
//    Call<GetCourseTagsResponse> getCourseTags();
//    @GET("services/tex/individual/course-detail?action=get-featured-courses")
//    Call<GetCoursesListResponse> getFeaturedCourses(@Query("start") int start,
//                                                    @Query("rows") int rows);
//    @GET("services/tex/individual/course-detail?action=get-popular-courses")
//    Call<GetCoursesListResponse> getPopularCourses(@Query("start") int start,
//                                                   @Query("rows") int rows);
//    @GET("services/tex/individual/course-search")
//    Call<GetCoursesSearchResponse> getSearchCourses(@Query("query") String solrQuery);

//    @POST("tex/course/browse-category")
//    Call<GetCourseCategoryResponse> getCourseCategory(@Body GetCourseCategoryRequest request);
//    @GET
//    Call<GetCourseSubCategoryResponse> getCourseSubCategory(@Url String url);

//    @GET("services/public/ind/getAllIndustries")
//    Call<List<Sector>> getAllSectors();
//
//    @GET
//    Call<GetForceUpdateAndMaintainceRespone> getForceUpdateAndMaintaince(@Url String url);
}
