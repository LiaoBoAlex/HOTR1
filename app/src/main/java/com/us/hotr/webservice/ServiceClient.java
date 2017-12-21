package com.us.hotr.webservice;


import com.us.hotr.Constants;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.User;
import com.us.hotr.webservice.request.UpdateUserRequest;
import com.us.hotr.webservice.response.GetDoctorDetailResponse;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.webservice.response.GetHospitalDetailResponse;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.response.GetMassageDetailResponse;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.webservice.response.GetMasseurDetailResponse;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.response.GetSpaDetailResponse;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.storage.bean.SubjectDetail;
import com.us.hotr.storage.bean.TypeWithCount;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.BaseResponse;
import com.us.hotr.webservice.response.GetWechatAccessTokenResponse;
import com.us.hotr.webservice.response.GetWechatUserInfo;
import com.us.hotr.webservice.rxjava.ApiException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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

    public void getWechatUserInfo(DisposableObserver subscriber, String appid, String secret, String code, String grant_type){
        webService.getWechatAccessToken(Constants.WECHAT_LOGIN_URL, appid, secret, code, grant_type)
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetWechatAccessTokenResponse, Observable<GetWechatUserInfo>>() {
                    @Override
                    public Observable<GetWechatUserInfo> apply(GetWechatAccessTokenResponse getWechatAccessTokenResponse) throws Exception {
                        return webService.getWechatUserInfo(Constants.WECHAT_USER_INFO_URL,
                                getWechatAccessTokenResponse.getAccess_token(),
                                getWechatAccessTokenResponse.getOpenid());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAdvList(DisposableObserver subscriber, int width, int height, int type){
        webService.getAdvList(width, height, type)
                .map(new HttpResultFunc<List<Adv>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSubjectList(DisposableObserver subscriber){
        webService.getSubjectList()
                .map(new HttpResultFunc<List<Subject>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageTypeList(DisposableObserver subscriber){
        webService.getMassageTypeList()
                .map(new HttpResultFunc<BaseListResponse<List<Subject>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageTypeListBySpa(DisposableObserver subscriber, int spaId){
        webService.getMassageTypeListBySpa(spaId)
                .map(new HttpResultFunc<List<TypeWithCount>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCityList(DisposableObserver subscriber){
        webService.getCityList()
                .map(new HttpResultFunc<List<Provence>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getHospitalList(DisposableObserver subscriber, Integer city_code, Integer type, Integer type_id, Integer page_size, int page){
        webService.getHospitalList(city_code, type, type_id, page_size, page)
                .map(new HttpResultFunc<BaseListResponse<List<Hospital>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSpaList(DisposableObserver subscriber, Integer city_code, Integer type, Integer type_id, Double latitude, Double longitude, Integer page_size, int page){
        webService.getSpaList(city_code, type, type_id, latitude, longitude, page_size, page)
                .map(new HttpResultFunc<BaseListResponse<List<Spa>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSubjectDetail(DisposableObserver subscriber, int id){
        webService.getSubjectDetail(id)
                .map(new HttpResultFunc<SubjectDetail>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductList(DisposableObserver subscriber, Integer type, Integer hospitalId, Integer doctorId, Integer project_id, Integer city_id, Double pos_latitude, Double pos_longitude, Integer pageSize, int page){
        webService.getProductList(type, hospitalId, doctorId, project_id, city_id, pos_latitude, pos_longitude, pageSize, page)
                .map(new HttpResultFunc<BaseListResponse<List<Product>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageList(DisposableObserver subscriber, Integer type, Integer type_id, Integer city_id, Double pos_latitude, Double pos_longitude, Integer pageSize, int page){
        webService.getMassageList(type, type_id, city_id, pos_latitude, pos_longitude, pageSize, page)
                .map(new HttpResultFunc<BaseListResponse<List<Massage>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageListBySpa(DisposableObserver subscriber, Integer spaId, Integer subjectId, Integer pageSize, int page){
        webService.getMassageListBySpa(spaId,  subjectId, page, pageSize)
                .map(new HttpResultFunc<BaseListResponse<List<Massage>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void getDoctorList(DisposableObserver subscriber, Integer type, Integer hospitalId, Integer project_id, Integer city_id, Integer pageSize, int page){
        webService.getDoctorList(type, hospitalId, project_id, city_id, pageSize, page)
                .map(new HttpResultFunc<BaseListResponse<List<Doctor>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurList(DisposableObserver subscriber, Integer spaId, Integer cityId, Integer typeId, Double latitude, Double longitude, Integer type, Integer pageSize, int page){
        webService.getMasseurList(spaId, cityId,typeId, latitude, longitude, page, pageSize, type)
                .map(new HttpResultFunc<BaseListResponse<List<Masseur>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurListBySpa(DisposableObserver subscriber, Integer spaId, Integer pageSize, int page){
        webService.getMasseurListBySpa(spaId,  page, pageSize)
                .map(new HttpResultFunc<BaseListResponse<List<Masseur>>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getHospitalDetail(DisposableObserver subscriber, final int hospitalId){
        webService.getHospitalDetail(hospitalId)
                .map(new HttpResultFunc<GetHospitalDetailResponse>())
                .subscribeOn(Schedulers.io())
                .map(new Function<GetHospitalDetailResponse, GetHospitalDetailResponse>() {
                    @Override
                    public GetHospitalDetailResponse apply(GetHospitalDetailResponse hospitalDetail) throws Exception {
                        BaseListResponse<List<Product>> productResult = webService.getHospitalProductList1(hospitalId).execute().body().getResult();
                        hospitalDetail.setProductList(productResult.getRows());
                        hospitalDetail.setTotalProduct(productResult.getTotal());
                        BaseListResponse<List<Doctor>> doctorResult = webService.getDoctorList1(hospitalId).execute().body().getResult();
                        hospitalDetail.setDoctorList(doctorResult.getRows());
                        hospitalDetail.setTotalDoctor(doctorResult.getTotal());
                        return hospitalDetail;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDoctorDetail(DisposableObserver subscriber, final int doctorId){
        webService.getDoctorDetail(doctorId)
                .map(new HttpResultFunc<GetDoctorDetailResponse>())
                .subscribeOn(Schedulers.io())
                .map(new Function<GetDoctorDetailResponse, GetDoctorDetailResponse>() {
                    @Override
                    public GetDoctorDetailResponse apply(GetDoctorDetailResponse doctorDetail) throws Exception {
                        BaseListResponse<List<Product>> productResult = webService.getDoctorProductList1(doctorId).execute().body().getResult();
                        doctorDetail.setProductList(productResult.getRows());
                        doctorDetail.setTotalProduct(productResult.getTotal());
                        return doctorDetail;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMassageDetail(DisposableObserver subscriber, final int key){
        webService.getMassageDetail(key)
                .map(new HttpResultFunc<GetMassageDetailResponse>())
                .subscribeOn(Schedulers.io())
                .map(new Function<GetMassageDetailResponse, GetMassageDetailResponse>() {
                    @Override
                    public GetMassageDetailResponse apply(GetMassageDetailResponse massageDetail) throws Exception {
                        BaseListResponse<List<Masseur>> masseurResult = webService.getMasseurListBySpa1(massageDetail.getMassage().getId()).execute().body().getResult();
                        massageDetail.setMassageistList(masseurResult.getRows());
                        massageDetail.setTotalMasseurCount(masseurResult.getTotal());
                        return massageDetail;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProductDetail(DisposableObserver subscriber, int id) {
        webService.getProductDetail(id)
                .map(new HttpResultFunc<GetProductDetailResponse>())
                .subscribeOn(Schedulers.io())
                .map(new Function<GetProductDetailResponse, GetProductDetailResponse>() {
                    @Override
                    public GetProductDetailResponse apply(GetProductDetailResponse productDetailResponse) throws Exception {
                        BaseListResponse<List<Product>> productResult = webService.getHospitalProductList1(productDetailResponse.getHospital().getKey()).execute().body().getResult();
                        productDetailResponse.setProposedProduct(productResult.getRows());
                        productDetailResponse.setProposedProductCount(productResult.getTotal());
                        return productDetailResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMasseurDetail(DisposableObserver subscriber, int id) {
        webService.getMasseurDetail(id)
                .map(new HttpResultFunc<GetMasseurDetailResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSpaDetail(DisposableObserver subscriber, final int key){
        webService.getSpaDetail(key)
                .map(new HttpResultFunc<GetSpaDetailResponse>())
                .subscribeOn(Schedulers.io())
                .map(new Function<GetSpaDetailResponse, GetSpaDetailResponse>() {
                    @Override
                    public GetSpaDetailResponse apply(GetSpaDetailResponse spaDetail) throws Exception {
                        BaseListResponse<List<Masseur>> masseurList = webService.getMasseurListBySpa1(spaDetail.getMassage().getKey()).execute().body().getResult();
                        spaDetail.setMasseurList(masseurList.getRows());
                        spaDetail.setTotalMasseurCount(masseurList.getTotal());
                        BaseListResponse<List<Massage>> massageList = webService.getMassageListBySpa1(spaDetail.getMassage().getKey()).execute().body().getResult();
                        spaDetail.setMassageList(massageList.getRows());
                        spaDetail.setTotalMassageCount(massageList.getTotal());
                        return spaDetail;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void login(DisposableObserver subscriber, String userName, String password) {
        webService.login(userName, password)
                .map(new HttpResultFunc<GetLoginResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUserDetail(DisposableObserver subscriber, String ssesionId) {
        webService.getUserDetail(ssesionId)
                .map(new HttpResultFunc<User>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void updateUserDetail(DisposableObserver subscriber, String ssesionId, UpdateUserRequest request) {
        webService.updateUserDetail(ssesionId, request.getNickname(), request)
                .map(new HttpResultFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private class HttpResultFunc<T> implements Function<BaseResponse<T>, T>{

        @Override
        public T apply(BaseResponse<T> tBaseResponse) throws Exception {
            Thread.sleep(1000);
            if ((tBaseResponse.getStatus() != Constants.SUCCESS
                    && tBaseResponse.getStatus() != Constants.ERROR_WRONG_PASSWORD) || tBaseResponse.getResult() == null) {
                throw new ApiException(tBaseResponse.getStatus(), tBaseResponse.getMemo());
            }
            return tBaseResponse.getResult();
        }
    }
}
