package com.us.hotr.webservice;


import android.app.Activity;
import android.content.Context;

import com.alipay.sdk.app.PayTask;
import com.us.hotr.Constants;
import com.us.hotr.storage.bean.Address;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.HotSearchTopic;
import com.us.hotr.storage.bean.Info;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.MassageReceipt;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.storage.bean.ProductReceipt;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.storage.bean.SubjectDetail;
import com.us.hotr.storage.bean.Theme;
import com.us.hotr.storage.bean.Ticket;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.storage.bean.User;
import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.storage.bean.WechatBill;
import com.us.hotr.util.AliPayResult;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.request.AvailableVoucherRequest;
import com.us.hotr.webservice.request.BoundMobileRequest;
import com.us.hotr.webservice.request.CancelOrderRequest;
import com.us.hotr.webservice.request.ChangePasswordRequest;
import com.us.hotr.webservice.request.CreateMassageOrderRequest;
import com.us.hotr.webservice.request.CreatePartyOrderRequest;
import com.us.hotr.webservice.request.CreateProductOrderRequest;
import com.us.hotr.webservice.request.GetAppVersionRequest;
import com.us.hotr.webservice.request.LoginAndRegisterRequest;
import com.us.hotr.webservice.request.LoginWithWechatRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.request.UpdateUserRequest;
import com.us.hotr.webservice.request.UploadCommentRequest;
import com.us.hotr.webservice.request.UploadReplyRequest;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.BaseResponse;
import com.us.hotr.webservice.response.GetAllGroupResponse;
import com.us.hotr.webservice.response.GetAppVersionResponse;
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
import com.us.hotr.webservice.response.GetSpaDetailResponse;
import com.us.hotr.webservice.response.GetWechatAccessTokenResponse;
import com.us.hotr.webservice.response.GetWechatUserInfo;
import com.us.hotr.webservice.response.UpdateUserAvatarRespone;
import com.us.hotr.webservice.response.UploadPostResponse;
import com.us.hotr.webservice.rxjava.ApiException;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by liaobo on 4/27/2017.
 */

public class ServiceClient {

    private WebService webService;

    private ServiceClient() {
        webService = RetrofitClient.getClient(Constants.SERVER_URL).create(WebService.class);
    }

    private static class SingletonHolder{
        private static final ServiceClient INSTANCE = new ServiceClient();
    }

    public static ServiceClient getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getHomePage(DisposableObserver subscriber, String jsessionid, long provinceCode, Long cityCode, int modelId){
        webService.getHomePage(jsessionid, provinceCode, cityCode, modelId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetHomePageResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPostHomePage(DisposableObserver subscriber, final String jsessionid, final Long userId){
        webService.getHomePage(jsessionid, 2, null, 2)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetHomePageResponse>())
                .map(new Function<GetHomePageResponse, GetHomePageResponse>() {
                    @Override
                    public GetHomePageResponse apply(GetHomePageResponse result) throws Exception {
                        GetGroupMainPageResponse response = webService.getGroupSession(jsessionid, userId).execute().body().getResult();
                        List<Group> myGroup = new ArrayList<>();
                        if(response.getMyCoshow()!=null)
                            myGroup.addAll(response.getMyCoshow());
                        if(response.getRestOfMyCoshow()!=null)
                            myGroup.addAll(response.getRestOfMyCoshow());
                        result.setMyGrouList(myGroup);
                        return result;
                    }
                })
                .map(new Function<GetHomePageResponse, GetHomePageResponse>() {
                    @Override
                    public GetHomePageResponse apply(GetHomePageResponse result) throws Exception {
                        BaseListResponse<List<Post>> response = webService.getAllPost1(jsessionid, 1, Constants.MAX_PAGE_ITEM).execute().body().getResult();
                        result.setRecommendHotTopicList(response.getRows());
                        result.setTotal(response.getTotal());
                        return result;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSubjectList(DisposableObserver subscriber){
        webService.getSubjectList()
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Subject>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageTypeList(DisposableObserver subscriber){
        webService.getMassageTypeList()
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Subject>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPartyStatusList(DisposableObserver subscriber){
        webService.getPartyStatusList()
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Integer>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageTypeListBySpa(DisposableObserver subscriber, long spaId){
        webService.getMassageTypeListBySpa(spaId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Type>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductTypeListByHospital(DisposableObserver subscriber, long hospitalId){
        webService.getProductTypeListByHospital(hospitalId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Type>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductTypeListByDoctor(DisposableObserver subscriber, long DoctorId){
        webService.getProductTypeListByDoctor(DoctorId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Type>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCityList(DisposableObserver subscriber, int type){
        webService.getCityList(type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Provence>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getHospitalList(DisposableObserver subscriber, String keyword, Long city_code, Long type, Long type_id, Integer page_size, int page){
        webService.getHospitalList(keyword, city_code, type, type_id, page_size, page)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Hospital>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSpaList(DisposableObserver subscriber, String keyword, Long city_code, Long type, Long type_id, Double latitude, Double longitude, Integer page_size, int page){
        webService.getSpaList(keyword, city_code, type, type_id, latitude, longitude, page_size, page)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Spa>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSubjectDetail(DisposableObserver subscriber, long id){
        webService.getSubjectDetail(id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<SubjectDetail>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductList(DisposableObserver subscriber, String keyword, Long type, Long typeId, Long hospitalId, Long doctorId, Long project_id, Long city_id, Double pos_latitude, Double pos_longitude, Integer pageSize, int page){
        webService.getProductList(keyword, type, typeId, hospitalId, doctorId, project_id, city_id, pos_latitude, pos_longitude, pageSize, page)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Product>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageList(DisposableObserver subscriber, String keyword, Long type, Long type_id, Long city_id, Double pos_latitude, Double pos_longitude, Integer pageSize, int page){
        webService.getMassageList(keyword, type, type_id, city_id, pos_latitude, pos_longitude, pageSize, page)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Massage>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageListBySpa(DisposableObserver subscriber, Long spaId, Long subjectId, Integer pageSize, int page){
        webService.getMassageListBySpa(spaId,  subjectId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Massage>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void getDoctorList(DisposableObserver subscriber, String keyword, Long type, Long hospitalId, Long project_id, Long city_id, Integer pageSize, int page){
        webService.getDoctorList(keyword, type, hospitalId, project_id, city_id, pageSize, page)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Doctor>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurList(DisposableObserver subscriber, String ssesionId, String keyword, Long spaId, Long cityId, Long typeId, Double latitude, Double longitude, Long type, Integer pageSize, int page){
        webService.getMasseurList(ssesionId, keyword, spaId, cityId,typeId, latitude, longitude, page, pageSize, type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Masseur>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPartyList(DisposableObserver subscriber, String keyword, int type, List<Integer> status, Integer pageSize, int page){
        webService.getPartyList(keyword, type, status, pageSize, page)
                .map(new HttpResultFunc<BaseListResponse<List<Party>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurListBySpa(DisposableObserver subscriber, String ssesionId, Long spaId, Integer pageSize, int page){
        webService.getMasseurListBySpa(ssesionId, spaId,  page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Masseur>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getHospitalDetail(DisposableObserver subscriber, final long hospitalId, final String ssesionId){
        webService.getHospitalDetail(hospitalId, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetHospitalDetailResponse>())
                .map(new Function<GetHospitalDetailResponse, GetHospitalDetailResponse>() {
                    @Override
                    public GetHospitalDetailResponse apply(GetHospitalDetailResponse hospitalDetail) throws Exception {
                        BaseResponse<BaseListResponse<List<Product>>> response = webService.getHospitalProductList1(hospitalId).execute().body();
                        if (response.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            hospitalDetail.setProductList(response.getResult().getRows());
                            hospitalDetail.setTotalProduct(response.getResult().getTotal());
                        }
                        BaseResponse<BaseListResponse<List<Doctor>>> response1 = webService.getDoctorList1(hospitalId).execute().body();
                        if (response1.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            hospitalDetail.setDoctorList(response1.getResult().getRows());
                            hospitalDetail.setTotalDoctor(response1.getResult().getTotal());
                        }
                        BaseResponse<BaseListResponse<List<Case>>> response2 = webService.getCaseByType1(ssesionId, 3, hospitalId).execute().body();
                        if (response2.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            hospitalDetail.setCaseList(response2.getResult().getRows());
                            hospitalDetail.setTotalCase(response2.getResult().getTotal());
                        }
                        BaseResponse<List<Type>> response3 = webService.getCaseTypeCount(3, hospitalId).execute().body();
                        if (response3.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            hospitalDetail.setCaseTypeList(response3.getResult());
                        }

                        return hospitalDetail;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDoctorDetail(DisposableObserver subscriber, final long doctorId, final String ssesionId){
        webService.getDoctorDetail(doctorId, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetDoctorDetailResponse>())
                .map(new Function<GetDoctorDetailResponse, GetDoctorDetailResponse>() {
                    @Override
                    public GetDoctorDetailResponse apply(GetDoctorDetailResponse doctorDetail) throws Exception {
                        BaseResponse<BaseListResponse<List<Product>>> response = webService.getDoctorProductList1(doctorId).execute().body();
                        if (response.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            doctorDetail.setProductList(response.getResult().getRows());
                            doctorDetail.setTotalProduct(response.getResult().getTotal());
                        }
                        BaseResponse<BaseListResponse<List<Case>>> response1 = webService.getCaseByType1(ssesionId, 4, doctorId).execute().body();
                        if (response1.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            doctorDetail.setCaseList(response1.getResult().getRows());
                            doctorDetail.setTotalCase(response1.getResult().getTotal());
                        }
                        BaseResponse<List<Type>> response2 = webService.getCaseTypeCount(4, doctorId).execute().body();
                        if (response2.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            doctorDetail.setCaseTypeList(response2.getResult());
                        }
                        return doctorDetail;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageDetail(DisposableObserver subscriber, final long key, final String ssesionId){
        webService.getMassageDetail(key, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetMassageDetailResponse>())
                .map(new Function<GetMassageDetailResponse, GetMassageDetailResponse>() {
                    @Override
                    public GetMassageDetailResponse apply(GetMassageDetailResponse massageDetail) throws Exception {
                        BaseResponse<BaseListResponse<List<Masseur>>> response = webService.getMasseurListBySpa1(ssesionId, massageDetail.getMassage().getKey()).execute().body();
                        if (response.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            massageDetail.setMassageistList(response.getResult().getRows());
                            massageDetail.setTotalMasseurCount(response.getResult().getTotal());
                        }
                        return massageDetail;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductDetail(DisposableObserver subscriber, long id, String ssesionId) {
        webService.getProductDetail(id, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetProductDetailResponse>())
                .map(new Function<GetProductDetailResponse, GetProductDetailResponse>() {
                    @Override
                    public GetProductDetailResponse apply(GetProductDetailResponse productDetailResponse) throws Exception {
                        BaseResponse<BaseListResponse<List<Product>>> response = webService.getHospitalProductList1(productDetailResponse.getHospital().getKey()).execute().body();
                        if (response.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            productDetailResponse.setProposedProduct(response.getResult().getRows());
                            productDetailResponse.setProposedProductCount(response.getResult().getTotal());
                        }
                        return productDetailResponse;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurDetail(DisposableObserver subscriber, long id, String ssesionId) {
        webService.getMasseurDetail(id, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetMasseurDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSpaDetail(DisposableObserver subscriber, final long key, final String ssesionId){
        webService.getSpaDetail(key, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetSpaDetailResponse>())
                .map(new Function<GetSpaDetailResponse, GetSpaDetailResponse>() {
                    @Override
                    public GetSpaDetailResponse apply(GetSpaDetailResponse spaDetail) throws Exception {
                        BaseResponse<BaseListResponse<List<Masseur>>> response = webService.getMasseurListBySpa1(ssesionId, spaDetail.getMassage().getKey()).execute().body();
                        if (response.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            spaDetail.setMasseurList(response.getResult().getRows());
                            spaDetail.setTotalMasseurCount(response.getResult().getTotal());
                        }
                        BaseResponse<BaseListResponse<List<Massage>>> response1 = webService.getMassageListBySpa1(spaDetail.getMassage().getKey()).execute().body();
                        if (response1.getStatus() != Constants.SUCCESS) {
                            throw new ApiException(response.getStatus(), response.getMemo());
                        } else{
                            spaDetail.setMassageList(response1.getResult().getRows());
                            spaDetail.setTotalMassageCount(response1.getResult().getTotal());
                        }
                        return spaDetail;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPartyDetail(DisposableObserver subscriber, long id, String ssesionId){
        webService.getPartyDetail(id, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetPartyDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void increasePartyInterest(DisposableObserver subscriber, long id){
        webService.increasePartyInterest(id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void reserveParty(DisposableObserver subscriber, long id, String ssesionId){
        webService.reserveParty(id, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAvaliableVoucher(DisposableObserver subscriber, String jsessionid, AvailableVoucherRequest request){
        webService.getAvaliableVoucher(jsessionid, request.getProduct_price(), request.getType())
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Voucher>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAllVoucher(DisposableObserver subscriber, String jsessionid, int status, int page_number, Integer page_size){
        webService.getAllVoucher(jsessionid, status, page_number, page_size)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Voucher>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addAllVoucher(DisposableObserver subscriber, String jsessionid, List<Long> couponIdArr){
        webService.addAllVoucher(jsessionid, couponIdArr)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkOrderCount(DisposableObserver subscriber, long productId, int type){
        webService.checkOrderCount(productId, type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<Integer>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkPartyOrderCount(DisposableObserver subscriber, final List<Ticket> tickets){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Boolean result = true;
                for(Ticket t:tickets){
                    int count = webService.checkPartyOrderCount(t.getId()).execute().body().getResult();
                    if(count<t.getCount())
                        result = false;
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkPersonalCountProduct(DisposableObserver subscriber, String jsessionid, long productId){
        webService.checkPersonalCountProduct(jsessionid, productId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<Integer>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkPersonalCountMassage(DisposableObserver subscriber, String jsessionid, long productId){
        webService.checkPersonalCountMassage(jsessionid, productId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<Integer>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createOrderProduct(DisposableObserver subscriber, String jsessionid, CreateProductOrderRequest request){
        webService.createOrderProduct(jsessionid, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<ProductOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createOrderMassage(DisposableObserver subscriber, String jsessionid, CreateMassageOrderRequest request){
        webService.createOrderMassage(jsessionid, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<MassageOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createOrderParty(DisposableObserver subscriber, String jsessionid, CreatePartyOrderRequest request){
        webService.createOrderParty(jsessionid, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<PartyOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createWechatBill(final DisposableObserver subscriber, final String jsessionid, final long order_id, final int type, final Context mContext){
        SubscriberWithFinishListener l = new SubscriberWithFinishListener<String>() {
            @Override
            public void onNext(String s) {
                if(s.isEmpty())
                    s = "127.0.0.1";
                switch (type) {
                    case Constants.TYPE_PRODUCT:
                        createWechatProductBillEx(subscriber, jsessionid, order_id, s);
                        break;
                    case Constants.TYPE_MASSAGE:
                        createWechatMassageBillEx(subscriber, jsessionid, order_id, s);
                        break;
                    case Constants.TYPE_PARTY:
                        createWechatPartyBillEx(subscriber, jsessionid, order_id, s);
                        break;
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                switch (type) {
                    case Constants.TYPE_PRODUCT:
                        createWechatProductBillEx(subscriber, jsessionid, order_id, "127.0.0.1");
                        break;
                    case Constants.TYPE_MASSAGE:
                        createWechatMassageBillEx(subscriber, jsessionid, order_id, "127.0.0.1");
                        break;
                    case Constants.TYPE_PARTY:
                        createWechatPartyBillEx(subscriber, jsessionid, order_id, "127.0.0.1");
                        break;
                }
            }
        };
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(Tools.getIpAddress(mContext));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<String>(l, mContext));
    }

    public void createAlipayBill(final DisposableObserver subscriber, final String jsessionid, final long order_id, final int type, Activity mActivity){
        switch (type) {
            case Constants.TYPE_PRODUCT:
                createAlipayProductBill(subscriber, jsessionid, order_id, mActivity);
                break;
            case Constants.TYPE_MASSAGE:
                createAlipayMassageBill(subscriber, jsessionid, order_id, mActivity);
                break;
            case Constants.TYPE_PARTY:
                createAlipayPartyBill(subscriber, jsessionid, order_id, mActivity);
                break;
        }
    }

    public void createWechatProductBillEx(DisposableObserver subscriber, String jsessionid, long order_id, String spbill_create_ip){
        webService.createWechatProductBill(jsessionid, order_id, spbill_create_ip)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<WechatBill>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createWechatMassageBillEx(DisposableObserver subscriber, String jsessionid, long order_id, String spbill_create_ip){
        webService.createWechatMassageBill(jsessionid, order_id, spbill_create_ip)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<WechatBill>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createWechatPartyBillEx(DisposableObserver subscriber, String jsessionid, long order_id, String spbill_create_ip){
        webService.createWechatPartyBill(jsessionid, order_id, spbill_create_ip)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<WechatBill>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createAlipayProductBill(DisposableObserver subscriber, String jsessionid, long order_id, final Activity mActivity){
        webService.createAlipayProductBill(jsessionid, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .map(new Function<String, AliPayResult>() {
                    @Override
                    public AliPayResult apply(String order) throws Exception {
                        PayTask alipay = new PayTask(mActivity);
                        Map<String, String> result = alipay.payV2(order,true);
                        return new AliPayResult(result);
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createAlipayMassageBill(DisposableObserver subscriber, String jsessionid, long order_id, final Activity mActivity){
        webService.createAlipayMassageBill(jsessionid, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .map(new Function<String, AliPayResult>() {
                    @Override
                    public AliPayResult apply(String order) throws Exception {
                        PayTask alipay = new PayTask(mActivity);
                        Map<String, String> result = alipay.payV2(order,true);
                        return new AliPayResult(result);
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void createAlipayPartyBill(DisposableObserver subscriber, String jsessionid, long order_id, final Activity mActivity){
        webService.createAlipayPartyBill(jsessionid, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .map(new Function<String, AliPayResult>() {
                    @Override
                    public AliPayResult apply(String order) throws Exception {
                        PayTask alipay = new PayTask(mActivity);
                        Map<String, String> result = alipay.payV2(order,true);
                        return new AliPayResult(result);
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getGroupMainPage(DisposableObserver subscriber, String jsessionid){
        webService.getGroupMainPage(jsessionid)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetGroupMainPageResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void favoriteGroup(DisposableObserver subscriber, String jsessionid, long coshowId, int isAttention){
        webService.favoriteGroup(jsessionid, coshowId, isAttention)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getGroupTheme(DisposableObserver subscriber){
        webService.getGroupTheme()
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Theme>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getGroup(DisposableObserver subscriber,  String ssesionId, Long themeId, String keyword){
        webService.getGroup(themeId, keyword, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Group>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getGroupById(DisposableObserver subscriber, long groupId, String ssesionId){
        webService.getGroupById(groupId, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<Group>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAllGroup(DisposableObserver subscriber, String ssesionId){
        webService.getAllGroup(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetAllGroupResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPostByGroup(DisposableObserver subscriber, long coshow_id, Long type, Integer pageSize, int page, String ssesionId){
        webService.getPostByGroup(coshow_id, type, page, pageSize, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAllPost(DisposableObserver subscriber, String ssesionId, int page, Integer pageSize, String keyword){
        webService.getAllPost(ssesionId, page, pageSize, keyword)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDiscoveryPost(DisposableObserver subscriber, String ssesionId, int page, Integer pageSize){
        webService.getDiscoveryPost(ssesionId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNearbyPeople(DisposableObserver subscriber, String currLon, String currLat){
        webService.getNearbyPeople(currLon, currLat)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<User>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNearbyPost(DisposableObserver subscriber, String ssesionId,String currLon, String currLat){
        webService.getNearbyPost(ssesionId, currLon, currLat)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPostDetail(DisposableObserver subscriber, long hotTopicId, String ssesionId){
        webService.getPostDetail(hotTopicId, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetPostDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadPost(DisposableObserver subscriber, final String ssesionId, List<String> filePaths, final Post request, final List<Long> coshowId){
        Map<String, RequestBody> bodyMap = new HashMap<>();
        for(int i=0;i<filePaths.size();i++){
            File file = new File(filePaths.get(i));
            bodyMap.put("file"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));

        }
        webService.uploadMultiImage("hotTopic", ssesionId, bodyMap)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<String>>())
                .flatMap(new Function<List<String>, ObservableSource<BaseResponse<UploadPostResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<UploadPostResponse>> apply(List<String> strings) throws Exception {
                        String image = "";
                        for(String s:strings)
                            image = image + s + ",";
                        request.setContentImg(image);
                        return webService.uploadPost(ssesionId, request, coshowId);
                    }
                })
                .map(new HttpResultFunc<UploadPostResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void likePost(DisposableObserver subscriber, String ssesionId, long hotTopicId){
        webService.likePost(ssesionId, hotTopicId, 1)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void readPost(DisposableObserver subscriber, long hotTopicId){
        webService.readPost(hotTopicId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void favoriteItem(DisposableObserver subscriber, String ssesionId, long hotTopicId, int type){
        webService.favoriteItem(ssesionId, hotTopicId, type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void removeFavoriteItem(DisposableObserver subscriber, String ssesionId, List<Long> hotTopicId, int type){
        webService.removeFavoriteItem(ssesionId, hotTopicId, type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadPostComment(DisposableObserver subscriber, final String ssesionId, final long hotTopicId, String content){
        UploadCommentRequest request = new UploadCommentRequest();
        request.setContent(content);
        request.setHotTopicId(hotTopicId);
        webService.uploadPostComment(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetPostDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetPostDetailResponse>> apply(String respone) throws Exception {
                        return webService.getPostDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetPostDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deletePostComment(DisposableObserver subscriber, final String ssesionId, long commentId, final long hotTopicId){
        webService.deletePostComment(ssesionId, commentId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetPostDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetPostDetailResponse>> apply(String respone) throws Exception {
                        return webService.getPostDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetPostDetailResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void likePostComment(DisposableObserver subscriber, final String ssesionId, long commentId){
        webService.likePostComment(ssesionId, commentId, 1)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadPostReply(DisposableObserver subscriber, final String ssesionId, final UploadReplyRequest replyRequest){
        webService.uploadPostReply(ssesionId, replyRequest)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetPostDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetPostDetailResponse>> apply(String respone) throws Exception {
                        return webService.getPostDetail(replyRequest.getHotTopicId(), ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetPostDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deletePostReply(DisposableObserver subscriber, final String ssesionId, final long replyId, final long hotTopicId){
        webService.deletePostReply(ssesionId, replyId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetPostDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetPostDetailResponse>> apply(String respone) throws Exception {
                        return webService.getPostDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetPostDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAllCase(DisposableObserver subscriber, String ssesionId, int page, Integer pageSize, String keyword){
        webService.getAllCase(ssesionId, page, pageSize, keyword)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Case>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCaseByType(DisposableObserver subscriber, String ssesionId, int type, long id, Long subjectId, int page, Integer pageSize){
        webService.getCaseByType(ssesionId, type, id, subjectId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Case>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCaseDetail(DisposableObserver subscriber, long hotTopicId, String ssesionId){
        webService.getCaseDetail(hotTopicId, ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetCaseDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadCase(DisposableObserver subscriber, final String ssesionId, final Case request){
        Map<String, RequestBody> bodyMap = new HashMap<>();
        File fileBefore = new File(request.getFilePathBefore());
        bodyMap.put("file"+"\"; filename=\""+fileBefore.getName(), RequestBody.create(MediaType.parse("image/jpeg"), fileBefore));
        File fileAfter = new File(request.getFilePathAfter());
        bodyMap.put("file"+"\"; filename=\""+fileAfter.getName(), RequestBody.create(MediaType.parse("image/jpeg"), fileAfter));
        webService.uploadMultiImage("hotTopic", ssesionId, bodyMap)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<String>>())
                .flatMap(new Function<List<String>, ObservableSource<BaseResponse<Long>>>() {
                    @Override
                    public ObservableSource<BaseResponse<Long>> apply(List<String> strings) throws Exception {
                        request.setBeforeOperationPhoto(strings.get(0));
                        request.setAfterOperationPhoto(strings.get(1));
                        request.setFilePathAfter(null);
                        request.setFilePathBefore(null);
                        return webService.uploadCase(ssesionId, request);
                    }
                })
                .map(new HttpResultFunc<Long>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void likeCase(DisposableObserver subscriber, String ssesionId, long hotTopicId){
        webService.likeCase(ssesionId, hotTopicId, 1)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void readCase(DisposableObserver subscriber, long hotTopicId){
        webService.readCase(hotTopicId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadCaseComment(DisposableObserver subscriber, final String ssesionId, final long hotTopicId, String content){
        UploadCommentRequest request = new UploadCommentRequest();
        request.setContent(content);
        request.setContrastPhotoId(hotTopicId);
        webService.uploadCaseComment(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetCaseDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetCaseDetailResponse>> apply(String respone) throws Exception {
                        return webService.getCaseDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetCaseDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteCaseComment(DisposableObserver subscriber, final String ssesionId, long commentId, final long hotTopicId){
        webService.deleteCaseComment(ssesionId, commentId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetCaseDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetCaseDetailResponse>> apply(String respone) throws Exception {
                        return webService.getCaseDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetCaseDetailResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void likeCaseComment(DisposableObserver subscriber, final String ssesionId, long commentId){
        webService.likeCaseComment(ssesionId, commentId, 1)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadCaseReply(DisposableObserver subscriber, final String ssesionId, final UploadReplyRequest replyRequest){
        webService.uploadCaseReply(ssesionId, replyRequest)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetCaseDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetCaseDetailResponse>> apply(String respone) throws Exception {
                        return webService.getCaseDetail(replyRequest.getContrastPhotoId(), ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetCaseDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteCaseReply(DisposableObserver subscriber, final String ssesionId, final long replyId, final long hotTopicId){
        webService.deleteCaseReply(ssesionId, replyId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .flatMap(new Function<String, ObservableSource<BaseResponse<GetCaseDetailResponse>>>() {
                    @Override
                    public ObservableSource<BaseResponse<GetCaseDetailResponse>> apply(String respone) throws Exception {
                        return webService.getCaseDetail(hotTopicId, ssesionId);
                    }
                })
                .map(new HttpResultFunc<GetCaseDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPurchasedProduct(DisposableObserver subscriber, String ssesionId){
        webService.getPurchasedProduct(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Product>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getGroupListByUser(DisposableObserver subscriber, long userId){
        webService.getGroupListbyUser(userId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetGroupListbyUserResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPostListbyUser(DisposableObserver subscriber, String ssesionId, long queryUserId, Integer pageSize, int page){
        webService.getPostListbyUser(ssesionId, queryUserId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductReceiptList(DisposableObserver subscriber, String ssesionId, int state, int page, Integer pageSize){
        webService.getProductReceiptList(ssesionId, state, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<ProductReceipt>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageReceiptList(DisposableObserver subscriber, String ssesionId, int state, int page, Integer pageSize){
        webService.getMassageReceiptList(ssesionId, state, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<MassageReceipt>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductReceiptDetailbyId(DisposableObserver subscriber, String ssesionId, long verificationId){
        webService.getProductReceiptDetailbyId(ssesionId, verificationId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<ProductReceipt>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageReceiptDetailbyId(DisposableObserver subscriber, String ssesionId, long verificationId){
        webService.getMassageReceiptDetailbyId(ssesionId, verificationId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<MassageReceipt>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductReceiptDetailbyOrderId(DisposableObserver subscriber, String ssesionId, long orderDetailId){
        webService.getProductReceiptDetailbyOrderId(ssesionId, orderDetailId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<ProductReceipt>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageReceiptDetailbyOrderId(DisposableObserver subscriber, String ssesionId, long orderDetailId){
        webService.getMassageReceiptDetailbyOrderId(ssesionId, orderDetailId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<MassageReceipt>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteProductReceipt(DisposableObserver subscriber, String ssesionId, long verification_id){
        webService.deleteProductReceipt(ssesionId, verification_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteMassageReceipt(DisposableObserver subscriber, String ssesionId, long verification_id){
        webService.deleteMassageReceipt(ssesionId, verification_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void refundProductReceipt(DisposableObserver subscriber, String ssesionId, long verification_id){
        webService.refundProductReceipt(ssesionId, verification_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void refundMassageReceipt(DisposableObserver subscriber, String ssesionId, long verification_id){
        webService.refundMassageReceipt(ssesionId, verification_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getCaseListbyUser(DisposableObserver subscriber, String ssesionId, long queryUserId, Integer pageSize, int page){
        webService.getCaseListbyUser(ssesionId, queryUserId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Case>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loginAndRegister(DisposableObserver subscriber, final LoginAndRegisterRequest request) {
//        Observable.create(new ObservableOnSubscribe<BaseResponse<GetLoginResponse>>() {
//            @Override
//            public void subscribe(final ObservableEmitter<BaseResponse<GetLoginResponse>> emitter) throws Exception {
//                final BaseResponse<GetLoginResponse> response =  webService.loginAndRegister(request).execute().body();
//                if(response.getResult()!=null && response.getStatus()==200) {
//                    JMessageClient.register("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            if (i == 0 || i == 898001) {
//                                JMessageClient.login("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                                    @Override
//                                    public void gotResult(int i, String s) {
//                                        if (i == 0) {
//                                            UserInfo userInfo = JMessageClient.getMyInfo();
//                                            userInfo.setNickname(response.getResult().getUser().getNickname());
//                                            userInfo.setAddress(response.getResult().getUser().getHead_portrait());
//                                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//
//                                                }
//                                            });
//
//                                            JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//                                                    emitter.onNext(response);
//                                                    emitter.onComplete();
//                                                }
//                                            });
//
//
//                                        } else
//                                            emitter.onError(new ApiException(500, ""));
//                                    }
//                                });
//                            } else
//                                emitter.onError(new ApiException(500, ""));
//                        }
//                    });
//                }else{
//                    emitter.onNext(response);
//                    emitter.onComplete();
//                }
//
//            }
        webService.loginAndRegister(request)
                .map(new HttpResultFunc<GetLoginResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loginWithWechat(DisposableObserver subscriber, final String appid, final String secret, final String code, final String grant_type){
        Observable.create(new ObservableOnSubscribe<BaseResponse<GetLoginResponse>>() {
            @Override
            public void subscribe(final ObservableEmitter<BaseResponse<GetLoginResponse>> emitter) throws Exception {
                GetWechatAccessTokenResponse getWechatAccessTokenResponse =  webService.getWechatAccessToken(Constants.WECHAT_LOGIN_URL, appid, secret, code, grant_type).execute().body();
                GetWechatUserInfo getWechatUserInfo = webService.getWechatUserInfo(Constants.WECHAT_USER_INFO_URL,
                        getWechatAccessTokenResponse.getAccess_token(),
                        getWechatAccessTokenResponse.getOpenid()).execute().body();
                int sex;
                if(getWechatUserInfo.getSex().equals("1"))
                    sex = 0;
                else if (getWechatUserInfo.getSex().equals("2"))
                    sex = 1;
                else
                    sex = 2;
                final BaseResponse<GetLoginResponse> response = webService.loginWithWechat(new LoginWithWechatRequest(
                        getWechatUserInfo.getNickname(),
                        getWechatUserInfo.getHeadimgurl(),
                        getWechatUserInfo.getOpenid(),
                        sex)).execute().body();

//                if(response.getResult()!=null && response.getStatus() == 200 && response.getResult().getUser() != null) {
//                    JMessageClient.register("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            if (i == 0 || i == 898001) {
//                                JMessageClient.login("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                                    @Override
//                                    public void gotResult(int i, String s) {
//                                        if (i == 0) {
//                                            UserInfo userInfo = JMessageClient.getMyInfo();
//                                            userInfo.setNickname(response.getResult().getUser().getNickname());
//                                            userInfo.setAddress(response.getResult().getUser().getHead_portrait());
//                                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//
//                                                }
//                                            });
//
//                                            JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//                                                    emitter.onNext(response);
//                                                    emitter.onComplete();
//                                                }
//                                            });
//
//
//                                        } else
//                                            emitter.onError(new ApiException(500));
//                                    }
//                                });
//                            } else
//                                emitter.onError(new ApiException(500));
//                        }
//                    });
//                }else{
//                    emitter.onNext(response);
//                    emitter.onComplete();
//                }
                if(response.getResult()!=null){
                    response.getResult().setWechatUser(new GetLoginResponse.WechatUser(getWechatUserInfo.getOpenid(),
                            getWechatUserInfo.getNickname(),
                            getWechatUserInfo.getHeadimgurl(),
                            sex));
                }
                emitter.onNext(response);
                emitter.onComplete();
            }
        })
                .map(new HttpResultFunc<GetLoginResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void registerWithWechat(DisposableObserver subscriber, LoginWithWechatRequest request){
        webService.registerWithWechat(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetLoginResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void requestForValidationCode(DisposableObserver subscriber, RequestForValidationCodeRequest request) {
        webService.requestForValidationCode(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void requestForValidationCodePassword(DisposableObserver subscriber, RequestForValidationCodeRequest request) {
        webService.requestForValidationCodePassword(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void changePassword(DisposableObserver subscriber, ChangePasswordRequest request) {
        webService.changePassword(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void requestForValidationCodePhone(DisposableObserver subscriber, RequestForValidationCodeRequest request) {
        webService.requestForValidationCodePhone(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void boundMobile(DisposableObserver subscriber, String ssesionId, BoundMobileRequest request) {
        webService.boundMobile(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void login(DisposableObserver subscriber, final String userName, final String password) {
//        Observable.create(new ObservableOnSubscribe<BaseResponse<GetLoginResponse>>() {
//            @Override
//            public void subscribe(final ObservableEmitter<BaseResponse<GetLoginResponse>> emitter) throws Exception {
//                final BaseResponse<GetLoginResponse> response =  webService.login(userName, password).execute().body();
//                if(response.getResult()!=null && response.getStatus() == 200) {
//                    JMessageClient.register("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            if (i == 0 || i == 898001) {
//                                JMessageClient.login("user" + response.getResult().getUser().getUserId(), "123456", new BasicCallback() {
//                                    @Override
//                                    public void gotResult(int i, String s) {
//                                        if (i == 0) {
//                                            UserInfo userInfo = JMessageClient.getMyInfo();
//                                            userInfo.setNickname(response.getResult().getUser().getNickname());
//                                            userInfo.setAddress(response.getResult().getUser().getHead_portrait());
//                                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//
//                                                }
//                                            });
//
//                                            JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
//                                                @Override
//                                                public void gotResult(int i, String s) {
//                                                    emitter.onNext(response);
//                                                    emitter.onComplete();
//                                                }
//                                            });
//
//
//                                        } else
//                                            emitter.onError(new ApiException(500, ""));
//                                    }
//                                });
//                            } else
//                                emitter.onError(new ApiException(500, ""));
//                        }
//                    });
//                }else {
//                    emitter.onNext(response);
//                    emitter.onComplete();
//                }
//
//            }
//        })
        webService.login(userName, password)
                .map(new HttpResultFunc<GetLoginResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMyDetail(DisposableObserver subscriber, String ssesionId) {
        webService.getMyDetail(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<User>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void updateUserDetail(DisposableObserver subscriber, final String ssesionId, final String filePath, final UpdateUserRequest updateUserRequest){
        if(filePath!=null && !filePath.isEmpty()){
            final File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            webService.updateUserAvatar(ssesionId, body)
                    .subscribeOn(Schedulers.io())
                    .map(new HttpResultFunc<UpdateUserAvatarRespone>())
                    .flatMap(new Function<UpdateUserAvatarRespone, ObservableSource<BaseResponse<String>>>() {
                        @Override
                        public ObservableSource<BaseResponse<String>> apply(UpdateUserAvatarRespone respone) throws Exception {
                            updateUserRequest.setHead_image_path(respone.getResult());
                            return webService.updateUserDetail(ssesionId, updateUserRequest.getNickname(), updateUserRequest);
                        }
                    })
                    .map(new HttpResultFunc<String>())
                    .flatMap(new Function<String, ObservableSource<BaseResponse<User>>>() {
                        @Override
                        public ObservableSource<BaseResponse<User>> apply(String s) throws Exception {
                            return webService.getMyDetail(ssesionId);
                        }
                    })
                    .map(new HttpResultFunc<User>())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }else{
            webService.updateUserDetail(ssesionId, updateUserRequest.getNickname(), updateUserRequest)
                    .subscribeOn(Schedulers.io())
                    .map(new HttpResultFunc<String>())
                    .flatMap(new Function<String, ObservableSource<BaseResponse<User>>>() {
                        @Override
                        public ObservableSource<BaseResponse<User>> apply(String s) throws Exception {
                            return webService.getMyDetail(ssesionId);
                        }
                    })
                    .map(new HttpResultFunc<User>())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void favoritePeople(DisposableObserver subscriber, String ssesionId, long userId){
        webService.favoritePeople(ssesionId, userId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteFavoritePeople(DisposableObserver subscriber, String ssesionId, long userId){
        webService.removeFavoritePeople(ssesionId, userId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFavoritePeople(DisposableObserver subscriber, String ssesionId, Integer pageSize, int page){
        webService.getFavoritePeople(ssesionId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<User>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFanPeople(DisposableObserver subscriber, String ssesionId, Integer pageSize, int page){
        webService.getFansPeople(ssesionId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<User>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUserList(DisposableObserver subscriber, String nick_name, Integer pageSize, int page){
        webService.getUserList(nick_name, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<User>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUserDetail(DisposableObserver subscriber, String ssesionId, long userId){
        webService.getUserDetail(ssesionId, userId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<User>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionParty(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionParty(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Party>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionDoctor(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionDoctor(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Doctor>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionHospital(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionHospital(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Hospital>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionProduct(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionProduct(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Product>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionSpa(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionSpa(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Spa>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionMasseur(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionMasseur(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Masseur>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionMassage(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionMassage(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Massage>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionCase(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionCase(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Case>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCollectionPost(DisposableObserver subscriber, String ssesionId){
        webService.getCollectionPost(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<Post>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductOrderList(DisposableObserver subscriber, String ssesionId, Integer payment_state, int page_number, Integer page_size){
        webService.getProductOrderList(ssesionId, payment_state, page_number, page_size)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<ProductOrder>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageOrderList(DisposableObserver subscriber, String ssesionId, Integer payment_state, int page_number, Integer page_size){
        webService.getMassageOrderList(ssesionId, payment_state, page_number, page_size)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<MassageOrder>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPartyOrderList(DisposableObserver subscriber, String ssesionId, Integer payment_state, int page_number, Integer page_size){
        webService.getPartyOrderList(ssesionId, payment_state, page_number, page_size)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<PartyOrder>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUnpiadOrderCount(final SubscriberListener mListener, final String ssesionId){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int result = 0;
                BaseListResponse response = webService.getUnpaidProductOrderCount(ssesionId).execute().body().getResult();
                if(response!=null)
                    result = result + response.getTotal();
                response = webService.getUnpaidMassageOrderCount(ssesionId).execute().body().getResult();
                if(response!=null)
                    result = result + response.getTotal();
                response = webService.getUnpaidPartyOrderCount(ssesionId).execute().body().getResult();
                if(response!=null)
                    result = result + response.getTotal();
                emitter.onNext(result);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        mListener.onNext(integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void getMassageOrderDetail(DisposableObserver subscriber, String ssesionId, long id){
        webService.getMassageOrderDetail(ssesionId, id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<MassageOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductOrderDetail(DisposableObserver subscriber, String ssesionId, long id){
        webService.getProductOrderDetail(ssesionId, id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<ProductOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getPartyOrderDetail(DisposableObserver subscriber, String ssesionId, long id){
        webService.getPartyOrderDetail(ssesionId, id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetPartyOrderDetailResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void cancelProductOrder(DisposableObserver subscriber, String ssesionId, CancelOrderRequest request){
        webService.cancelProductOrder(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<ProductOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void cancelMassageOrder(DisposableObserver subscriber, String ssesionId, CancelOrderRequest request){
        webService.cancelMassageOrder(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<MassageOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void cancelPartyOrder(DisposableObserver subscriber, String ssesionId, CancelOrderRequest request){
        webService.cancelPartyOrder(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<PartyOrder>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteProductOrder(DisposableObserver subscriber, String ssesionId, long order_id){
        webService.deleteProductOrder(ssesionId, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteMassageOrder(DisposableObserver subscriber, String ssesionId, long order_id){
        webService.deleteMassageOrder(ssesionId, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deletePartyOrder(DisposableObserver subscriber, String ssesionId, long order_id){
        webService.deletePartyOrder(ssesionId, order_id)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void userFeedback(DisposableObserver subscriber, String ssesionId, String phoneNumber, String Content){
        webService.userFeedback(ssesionId, Content, phoneNumber)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDeliveryAddressList(DisposableObserver subscriber, String ssesionId){
        webService.getDeliveryAddressList(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Address>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addDeliveryAddress(DisposableObserver subscriber, final String ssesionId, final Address request){
        if(request.getDefaultAddress() == 1) {
            webService.addDeliveryAddress(ssesionId, request)
                    .subscribeOn(Schedulers.io())
                    .map(new HttpResultFunc<String>())
                    .flatMap(new Function<String, ObservableSource<BaseResponse<String>>>() {
                        @Override
                        public ObservableSource<BaseResponse<String>> apply(String response) throws Exception {
                            return webService.setDefaultDeliveryAddress(ssesionId, response);
                        }
                    })
                    .map(new HttpResultFunc<String>())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }else{
            webService.addDeliveryAddress(ssesionId, request)
                    .subscribeOn(Schedulers.io())
                    .map(new HttpResultFunc<String>())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void setDefaultDeliveryAddress(DisposableObserver subscriber, String ssesionId, long addressId){
        webService.setDefaultDeliveryAddress(ssesionId, String.valueOf(addressId))
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteDeliveryAddress(DisposableObserver subscriber, String ssesionId, long addressId){
        webService.deleteDeliveryAddress(ssesionId, addressId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void editDeliveryAddress(DisposableObserver subscriber, final String ssesionId, final Address request){
        webService.editDeliveryAddress(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getHotSearchTopic(DisposableObserver subscriber, final String ssesionId){
        webService.getHotSearchTopic(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<BaseListResponse<List<HotSearchTopic>>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSearchCount(DisposableObserver subscriber, String search_keyword){
        webService.getSearchCount(search_keyword)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<HashMap<String, Integer>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFAQs(DisposableObserver subscriber, String ssesionId){
        webService.getFAQs(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Info>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getBusinessPartener(DisposableObserver subscriber, String ssesionId){
        webService.getBusinessPartener(ssesionId)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Info>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getStartVideoUrl(final SubscriberWithFinishListener mListener){
        webService.getStartVideoUrl()
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String response) {
                        mListener.onNext(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mListener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mListener.onComplete();
                    }
                });
    }

    public void getAppVersion(final SubscriberWithFinishListener mListener, GetAppVersionRequest request){
        webService.getAppVersion(request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<GetAppVersionResponse>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GetAppVersionResponse>() {
                    @Override
                    public void onNext(GetAppVersionResponse response) {
                        mListener.onNext(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mListener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void isFirstOrder(DisposableObserver subscriber, String ssesionId, GetAppVersionRequest request){
        webService.isFirstOrder(ssesionId, request)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<String>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAdvList(final SubscriberListener mListener, int width, int height, int type){
        webService.getAdvList(width, height, type)
                .subscribeOn(Schedulers.io())
                .map(new HttpResultFunc<List<Adv>>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Adv>>() {
                    @Override
                    public void onNext(List<Adv> response) {
                        mListener.onNext(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void downloadFileWithDynamicUrlSync(DisposableObserver subscriber, String url, final String filePath){
        webService.downloadFileWithDynamicUrlSync(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        writeResponseBodyToDisk(filePath, responseBody);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private class HttpResultFunc<T> implements Function<BaseResponse<T>, T>{

        @Override
        public T apply(BaseResponse<T> tBaseResponse) throws Exception {
//            try
//            {
//                Thread.currentThread().sleep(5000);//毫秒
//            }
//            catch(Exception e){}
            if (tBaseResponse.getStatus() != Constants.SUCCESS) {
                throw new ApiException(tBaseResponse.getStatus(), tBaseResponse.getMemo());
            }
            return tBaseResponse.getResult();
        }
    }

    private boolean writeResponseBodyToDisk(String filePath, ResponseBody body) {
        try {
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
