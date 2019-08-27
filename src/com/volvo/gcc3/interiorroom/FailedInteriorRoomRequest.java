package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;

public class FailedInteriorRoomRequest extends AbstractQuery {
    private String programMarket;
    private String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String dbErroLog;
    private long dbErrorCode;
    private InteriorResponse interiorResponse;

    static PreparedStatement prepareInsertFaildInteriorRoom = null;
    private static final String INSERT_FAILED_INTERIOR_ROOM = "INSERT INTO FAILED_INTERIOR_ROOM"
        + "(PROGRAM_MARKET, PNO12, STR_WEEK_FROM, STR_WEEK_TO, ERROR_LOG, ERROR_CODE, MODIFIED_DATE, LOG_TIME ) "
        + "VALUES(?, ?, ?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)";

    public FailedInteriorRoomRequest() {

    }

    public FailedInteriorRoomRequest(Connection connection, String programMarket, String pno12, long strWeekFrom, long strWeekTo, String dbErroLog,
        long dbErrorCode) {
        super();
        this.programMarket = programMarket;
        this.pno12 = pno12;
        this.strWeekFrom = strWeekFrom;
        this.strWeekTo = strWeekTo;
        this.dbErroLog = dbErroLog;
        this.dbErrorCode = dbErrorCode;
    }

    public FailedInteriorRoomRequest(Connection connection, InteriorResponse interiorResponse, String dbErroLog, long dbErrorCode) {
        super();
        this.interiorResponse = interiorResponse;
        this.dbErroLog = dbErroLog;
        this.dbErrorCode = dbErrorCode;
    }

    /**
     * Prepaer statment for interior faild interior room once aand ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareInsertFaildInterior(Connection connection) {
        if (prepareInsertFaildInteriorRoom == null) {
            try {
                prepareInsertFaildInteriorRoom = connection.prepareStatement(INSERT_FAILED_INTERIOR_ROOM);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareInsertFaildInteriorRoom;
    }

    public static void insertIntoFaildInteriorRoom(Connection connection, String programMaster, String pno12, long startWeek, long endWeek, String dbErroLog,
        long dbErrorCode) {
        PreparedStatement pst = null;
        try {
            pst = prepareInsertFaildInterior(connection);
            pst.setString(1, programMaster);
            pst.setString(2, pno12);
            pst.setLong(3, startWeek);
            pst.setLong(4, endWeek);
            // pst.setBytes(5, xmlContent.getBytes());
            pst.setString(5, dbErroLog);
            pst.setLong(6, dbErrorCode);
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

    public static void insertIntoFaildInteriorRoom(Connection connection, InteriorResponse interiorResponse, String dbErroLog, long dbErrorCode) {
        PreparedStatement pst = null;
        try {
            pst = prepareInsertFaildInterior(connection);
            pst.setString(1, interiorResponse.getProgramMarket());
            pst.setString(2, interiorResponse.getPno12());
            pst.setLong(3, interiorResponse.getStartWeek());
            pst.setLong(4, interiorResponse.getEndWeek());
            pst.setString(5, dbErroLog);
            pst.setLong(6, dbErrorCode);
            pst.executeUpdate();
            System.out.println("failed inteiror room insert commited");
            // close(pst);
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

    public static void closePrepareInsertFaildInteriorRoom() {
        close(prepareInsertFaildInteriorRoom);
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

    public String getDbErroLog() {
        return dbErroLog;
    }

    public void setDbErroLog(String dbErroLog) {
        this.dbErroLog = dbErroLog;
    }

    public long getDbErrorCode() {
        return dbErrorCode;
    }

    public void setDbErrorCode(long dbErrorCode) {
        this.dbErrorCode = dbErrorCode;
    }

    public InteriorResponse getInteriorResponse() {
        return interiorResponse;
    }

    public void setInteriorResponse(InteriorResponse interiorResponse) {
        this.interiorResponse = interiorResponse;
    }

}
