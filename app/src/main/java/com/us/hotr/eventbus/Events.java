package com.us.hotr.eventbus;


import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.webservice.response.GetLoginResponse;

/**
 * Created by Mloong on 2017/9/5.
 */

public class Events {
    public static class CitySelected{
        private String city;
        private long cityId;

        public CitySelected(String city, long cityId){
            this.city = city;
            this.cityId = cityId;
        }

        public String getSelectedCity() {
            return city;
        }

        public long getSelectedCityId(){
            return cityId;
        }
    }

    public static class SubjectSelected{
        private String subject;
        private long subjectId;
        private long ftId;

        public long getSubjectId() {
            return subjectId;
        }

        public long getFtId(){
            return  ftId;
        }

        public SubjectSelected(String subject, long subjectId, long ftId){
            this.subject = subject;
            this.subjectId = subjectId;
            this.ftId = ftId;
        }

        public String getSelectedSubject() {
            return subject;
        }
    }

    public static class TypeSelected{
        private long typeId;
        private String type;

        public String getType() {
            return type;
        }

        public long getTypeId() {
            return typeId;
        }

        public TypeSelected(long typeId, String type){
            this.typeId = typeId;
            this.type = type;
        }
    }

    public static class MassageTypeSelected{
        private long typeId;
        private String type;

        public String getType() {
            return type;
        }

        public long getTypeId() {
            return typeId;
        }

        public MassageTypeSelected(long typeId, String type){
            this.typeId = typeId;
            this.type = type;
        }
    }

    public static class Refresh{
        public Refresh(){

        }
    }

    public static class CreateVoucher{
        public CreateVoucher(){

        }
    }

    public static class PaymentSuccess{
        public PaymentSuccess(){

        }
    }

    public static class GotoMainPageNumber{
        private int page;
        public GotoMainPageNumber(int page){
            this.page = page;
        }

        public int getPage() {
            return page;
        }
    }

    public static class Login{
        private GetLoginResponse getLoginResponse;
        public Login(GetLoginResponse response){
            getLoginResponse = response;
        }

        public GetLoginResponse getGetLoginResponse() {
            return getLoginResponse;
        }
    }

    public static class VoucherSelected{
        private Voucher voucher;

        public Voucher getVoucher() {
            return voucher;
        }

        public void setVoucher(Voucher voucher) {
            this.voucher = voucher;
        }

        public VoucherSelected(Voucher voucher){
            this.voucher = voucher;
        }
    }

    public static class SearchKeywordHint{
        private String hint;
        public SearchKeywordHint(String hint){
            this.hint = hint;
        }
        public String getSearchKeywordHint(){
            return hint;
        }
    }

    public static class SearchKeywordSearch{
        private String keyword;
        public SearchKeywordSearch(String keyword){
            this.keyword = keyword;
        }
        public String getSearchKeywordSearch(){
            return keyword;
        }
    }

    public static class SearchTypeChosen{
        private int type;
        public SearchTypeChosen(int type){
            this.type = type;
        }
        public int getSearchTypeChosen(){
            return type;
        }
    }

    public static class GetSearchCount{
        private int count;
        public GetSearchCount(int count){
            this.count = count;
        }
        public int getSearchCount(){
            return count;
        }
    }

    public static class GetVoucherCount{
        private int count, type;
        public GetVoucherCount(int count, int type){
            this.count = count;
            this.type = type;
        }
        public int getVoucherCount(){
            return count;
        }

        public int getVoucherType(){return type;}
    }

    public static class GetNoticeCount{
        private int messageCount = 0, noticeCount = 0;
        public GetNoticeCount(int messageCount, int noticeCount){
            this.messageCount = messageCount;
            this.noticeCount = noticeCount;
        }
        public int getTotalCount(){
            return noticeCount + messageCount;
        }

        public int getMessageCount(){return messageCount;}

        public int getNoticeCount(){
            return noticeCount;
        }
    }

    public static class JPushSetTag{
        private boolean result;
        public JPushSetTag(boolean result){
            this.result = result;
        }

        public boolean isResult() {
            return result;
        }
    }

    public static class EnableEdit{
        public static final int ACTION_EDIT = 501;
        public static final int ACTION_DONE = 502;
        public static final int ACTION_DELETE = 503;

        private int action;
        public EnableEdit(int action){
            this.action = action;
        }
        public int getEnableEdit(){
            return action;
        }
    }
}
