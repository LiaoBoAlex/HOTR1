package com.us.hotr.eventbus;


/**
 * Created by Mloong on 2017/9/5.
 */

public class Events {
    public static class CitySelected{
        private String city;
        private int cityId;

        public CitySelected(String city, int cityId){
            this.city = city;
            this.cityId = cityId;
        }

        public String getSelectedCity() {
            return city;
        }

        public int getSelectedCityId(){
            return cityId;
        }
    }

    public static class SubjectSelected{
        private String subject;
        private int subjectId;

        public int getSubjectId() {
            return subjectId;
        }

        public SubjectSelected(String subject, int subjectId){
            this.subject = subject;
            this.subjectId = subjectId;
        }

        public String getSelectedSubject() {
            return subject;
        }
    }

    public static class TypeSelected{
        private int typeId;
        private String type;

        public String getType() {
            return type;
        }

        public int getTypeId() {
            return typeId;
        }

        public TypeSelected(int typeId, String type){
            this.typeId = typeId;
            this.type = type;
        }
    }

    public static class Refresh{
        public Refresh(){

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
