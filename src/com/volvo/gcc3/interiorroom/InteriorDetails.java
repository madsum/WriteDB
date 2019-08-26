package com.volvo.gcc3.interiorroom;

public class InteriorDetails {

    // String pno12, long str_week_from, long str_week_to, String color, String upholstery
    private int roomID;
    private int dataElement;
    private String state;
    private String code;
    private String common;
    private String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String color;
    private String upholstery;
    private String programMarket;

    public InteriorDetails(String pno12, long strWeekFrom, long strWeekTo, String color, String upholstery) {

        this.pno12 = pno12;
        this.strWeekFrom = strWeekFrom;
        this.strWeekTo = strWeekTo;
        this.color = color;
        this.upholstery = upholstery;

    }

    public InteriorDetails() {
        // TODO Auto-generated constructor stub
    }

    public String getKey() {
        return pno12 + strWeekFrom + strWeekTo + color + upholstery;
    }

    public static String getKey(String pno12, long strWeekFrom, long strWeekTo, String color, String upholstery) {

        return pno12 + strWeekFrom + strWeekTo + color + upholstery;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUpholstery() {
        return upholstery;
    }

    public void setUpholstery(String upholstery) {
        this.upholstery = upholstery;
    }

    public int getId() {
        return roomID;
    }

    public void setId(int id) {
        this.roomID = id;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getDataElement() {
        return dataElement;
    }

    public void setDataElement(int dataElement) {
        this.dataElement = dataElement;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getProgramMarket() {
        return programMarket;
    }

    public void setProgramMarket(String programMarket) {
        this.programMarket = programMarket;
    }

}
