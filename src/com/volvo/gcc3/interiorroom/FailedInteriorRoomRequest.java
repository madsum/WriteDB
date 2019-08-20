package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FailedInteriorRoomRequest {
    private String programMarket;
    private String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String log;
    private static final String INSERT_FAILED_INTERIOR_ROOM = "INSERT INTO FAILED_INTERIOR_ROOM"
        + "(PROGRAM_MARKET, PNO12, STR_WEEK_FROM, STR_WEEK_TO, LOG, MODIFIED_DATE, LOG_TIME ) " + "VALUES(?, ?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)";

    public FailedInteriorRoomRequest() {

    }

    public FailedInteriorRoomRequest(String programMarket, String pno12, long strWeekFrom, long strWeekTo, String log) {
        super();
        this.programMarket = programMarket;
        this.pno12 = pno12;
        this.strWeekFrom = strWeekFrom;
        this.strWeekTo = strWeekTo;
        this.log = log;
        // insertIntoFaildInteriorRoom(programMarket, pno12, strWeekFrom, strWeekTo, log);
    }

    public static void insertIntoFaildInteriorRoom(Connection connection, String programMaster, String pno12, long startWeek, long endWeek, String log) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(INSERT_FAILED_INTERIOR_ROOM);
            pst.setString(1, programMaster);
            pst.setString(2, pno12);
            pst.setLong(3, startWeek);
            pst.setLong(4, endWeek);
            pst.setString(5, log);
            pst.executeUpdate();
            System.out.println("failed inteiror room insert commited");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                /* Nothing */}
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
    }

    public static void insertIntoFaildInteriorRoom(Connection connection, FailedInteriorRoomRequest failedInteriorRoomRequest) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(INSERT_FAILED_INTERIOR_ROOM);
            pst.setString(1, failedInteriorRoomRequest.getProgramMarket());
            pst.setString(2, failedInteriorRoomRequest.getPno12());
            pst.setLong(3, failedInteriorRoomRequest.getStrWeekFrom());
            pst.setLong(4, failedInteriorRoomRequest.getStrWeekTo());
            pst.setString(5, failedInteriorRoomRequest.getLog());
            pst.executeUpdate();
            System.out.println("failed inteiror room insert commited");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                /* Nothing */}
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
    }

    public String getProgramMarket() {
        return programMarket;
    }
    public void setProgramMarket(String programMarket) {
        this.programMarket = programMarket;
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
    public String getLog() {
        return log;
    }
    public void setLog(String log) {
        this.log = log;
    }


}
