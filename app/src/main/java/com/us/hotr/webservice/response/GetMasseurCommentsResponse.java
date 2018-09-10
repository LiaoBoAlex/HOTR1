package com.us.hotr.webservice.response;

        import com.google.gson.annotations.SerializedName;
        import com.us.hotr.storage.bean.MasseurTag;

        import java.io.Serializable;
        import java.util.List;

public class GetMasseurCommentsResponse implements Serializable {
    private List<MasseurTag> tabInfoList;
    @SerializedName(value = "one", alternate = {"1"})
    private String one;
    @SerializedName(value = "two", alternate = {"2"})
    private String two;
    @SerializedName(value = "three", alternate = {"3"})
    private String three;
    @SerializedName(value = "four", alternate = {"4"})
    private String four;
    @SerializedName(value = "five", alternate = {"5"})
    private String five;

    public List<MasseurTag> getTabInfoList() {
        return tabInfoList;
    }

    public void setTabInfoList(List<MasseurTag> tabInfoList) {
        this.tabInfoList = tabInfoList;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }
}


