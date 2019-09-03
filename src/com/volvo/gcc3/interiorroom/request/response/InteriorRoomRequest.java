package com.volvo.gcc3.interiorroom.request.response;

import java.util.HashSet;
import java.util.Set;

public class InteriorRoomRequest {

    private String pno12;

    private long strWeekFrom;

    private long strWeekTo;

    private String programMarket;

    private Set<String> hundredPercentOptions = new HashSet<String>();

    public String getPno12() {
        return pno12;
    }

    public void setPno12(String pno12) {
        this.pno12 = pno12;
    }

    public long getStrWeekFrom() {
        return strWeekFrom;
    }

    public void setStrWeekFrom(long strWeekFrom) {
        this.strWeekFrom = strWeekFrom;
    }

    public long getStrWeekTo() {
        return strWeekTo;
    }

    public void setStrWeekTo(long strWeekTo) {
        this.strWeekTo = strWeekTo;
    }

    public String getProgramMarket() {
        return programMarket;
    }

    public void setProgramMarket(String programMarket) {
        this.programMarket = programMarket;
    }

    public Set<String> getHundredPercentOptions() {
        return hundredPercentOptions;
    }

    public void setHundredPercentOptions(Set<String> hundredPercentOptions) {
        this.hundredPercentOptions = hundredPercentOptions;
    }

    public void addHundredPercentOptions(String hundredPercentOption) {
        if (null == hundredPercentOptions) {
            hundredPercentOptions = new HashSet<String>();
        }
        hundredPercentOptions.add(hundredPercentOption);
    }

}
