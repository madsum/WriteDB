package com.volvo.gcc3.interiorroom;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.XmlUnmarshaller;

public class InteriorRoomLog extends AbstractQuery {

    private static final String CLASS_NAME = InteriorRoomLog.class.getName();

    String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String responseXml;
    private static Connection connection;
    private static List<InteriorRoomLog> interiorRoomLogs = new ArrayList<InteriorRoomLog>();

    private static final String INSERT_INTERIOR_ROOM_LOG = "INSERT INTO INTERIOR_ROOM_LOG"
        + "(PNO12, STR_WEEK_FROM, STR_WEEK_TO, RESPONSE_XML, MODIFIED_DATE, LOG_TIME ) " + "VALUES(?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)";

    private static final String SELECT_INTERIOR_ROOM_LOG = "SELECT * FROM INTERIOR_ROOM_LOG " + "WHERE PNO12 = ? AND STR_WEEK_FROM = ? AND STR_WEEK_TO = ?";

    private static final String SELECT_INTERIOR_ROOM_LOG_ALL = "SELECT * FROM INTERIOR_ROOM_LOG ";

    private final static String PNO12 = "PNO12";
    private final static String STR_WEEK_FROM = "STR_WEEK_FROM";
    private final static String STR_WEEK_TO = "STR_WEEK_TO";
    private final static String RESPONSE_XML = "RESPONSE_XML";


    public InteriorRoomLog() {

    }

    public InteriorRoomLog(String pno12, long strWeekFrom, long strWeekTo, String responseXml, Connection connection) {
        super();
        this.pno12 = pno12;
        this.strWeekFrom = strWeekFrom;
        this.strWeekTo = strWeekTo;
        this.responseXml = responseXml;
        this.connection = connection;
    }


    /**
     * Adds data in INTERIOR_ROOMS_MASTER table
     * 
     * @param startWeek
     * @param endWeek
     * @param pno12
     * @param color
     * @param upholstery
     * @throws DatabaseConnectionException
     * @throws SQLException
     * 
     */

    static public Long insertIntoInteriorMaster(Connection connection, String pno12, long startWeek, long endWeek, String responseXml) {
        Long retValue = -1l;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(INSERT_INTERIOR_ROOM_LOG);
            pst.setString(1, pno12);
            pst.setLong(2, startWeek);
            pst.setLong(3, endWeek);
            pst.setBytes(4, responseXml.getBytes());
            pst.execute();
            connection.commit();
            System.out.println("Interior Log insert commited");
            rset.close();
            pst.close();
            // connection.close();
        } catch (SQLException e) {
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
        } catch (Exception ex) {
            System.out.println("Error when insert in interior log. Handle error " + ex.getMessage());
        } finally {

        }
        return retValue;
    }

    public static void getInteriorRoomLog(Connection connection, InteriorRoomLog interiorRoomLog) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG);
            pst.setString(1, interiorRoomLog.getPno12());
            pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            pst.setLong(3, interiorRoomLog.getStrWeekTo());
            rset = pst.executeQuery();
            addInteriorResponseList(rset);
            commit(connection);
            close(connection);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
    }

    public static void getInteriorRoomLogAll(Connection connection) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG_ALL);
            // pst.setString(1, interiorRoomLog.getPno12());
            // pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            // pst.setLong(3, interiorRoomLog.getStrWeekTo());
            rset = pst.executeQuery();
            addInteriorResponseList(rset);
            commit(connection);
            close(connection);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
    }

    /**
     * Internal helper mentod to set all value
     * 
     * @param rset
     * 
     */
    public static List<InteriorResponse> InteriorResponseList = new ArrayList<InteriorResponse>();

    public static void makeInteriorResponseList(String responseXml) {
        XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
        InteriorResponse interiorResponse = new InteriorResponse();
        interiorResponse = xmlUnmarshaller.getInteriorResponse(responseXml, interiorResponse);
        InteriorResponseList.add(interiorResponse);
    }

    public static void compareInteriorResponseList() {
        InteriorResponse nextInteriorResponse = null;
        int index = 1;
        for (InteriorResponse currentinteriorResponse : InteriorResponseList) {
            // prevInteriorResponse = currentinteriorResponse;
            if (index < InteriorResponseList.size()) {
                nextInteriorResponse = InteriorResponseList.get(index);
            }
            System.out.println(" currentinteriorResponse.equals(nextInteriorResponse)  " + currentinteriorResponse.equals(nextInteriorResponse));
            index++;
        }
        /*
         * InteriorResponse interiorResponse0 = InteriorResponseList.get(0);
         * InteriorResponse interiorResponse1 = InteriorResponseList.get(1);
         * InteriorResponse interiorResponse2 = InteriorResponseList.get(2);
         * InteriorResponse interiorResponse3 = InteriorResponseList.get(3);
         * InteriorResponse interiorResponse4 = InteriorResponseList.get(4);
         * 
         * System.out.println(" interiorResponse0.equals(interiorResponse1)  " + interiorResponse0.equals(interiorResponse1));
         * System.out.println(" interiorResponse1.equals(interiorResponse2)  " + interiorResponse1.equals(interiorResponse2));
         * System.out.println(" interiorResponse2.equals(interiorResponse3)  " + interiorResponse2.equals(interiorResponse3));
         * System.out.println(" interiorResponse0.equals(interiorResponse0)  " + interiorResponse0.equals(interiorResponse0));
         * 
         * System.out.println(" interiorResponse0.equals(interiorResponse4)  " + interiorResponse0.equals(interiorResponse4));
         */

    }

    private static void addInteriorResponseList(ResultSet rset) {
        try {
            while (rset.next()) {
                InteriorRoomLog interiorRoomLog = new InteriorRoomLog();
                interiorRoomLog.setPno12(rset.getString(PNO12));
                interiorRoomLog.setStrWeekFrom(rset.getLong(STR_WEEK_FROM));
                interiorRoomLog.setStrWeekTo(rset.getLong(STR_WEEK_TO));
                Blob xmlBlob = rset.getBlob(RESPONSE_XML);
                String responseXml = new String(xmlBlob.getBytes(1l, (int) xmlBlob.length()));
                interiorRoomLog.setResponseXml(responseXml);
                addInteriorRoomLogs(interiorRoomLog);
                makeInteriorResponseList(responseXml);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.format("SQL State: %s\n", ex.getMessage());
        }
        // printInteriorRoomLogList();
    }

    private static void printInteriorRoomLogList() {
        for (InteriorRoomLog interiorRoomLog : interiorRoomLogs) {
            System.out.println("interiorRoomLog Pno12: " + interiorRoomLog.getPno12());
            System.out.println("interiorRoomLog StrWeekFrom: " + interiorRoomLog.getStrWeekFrom());
            System.out.println("interiorRoomLog StrWeekTo: " + interiorRoomLog.getStrWeekTo());
            System.out.println("interiorRoomLog ResponseXml: " + interiorRoomLog.getResponseXml());
        }
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

    public String getResponseXml() {
        return responseXml;
    }

    public void setResponseXml(String responseXml) {
        this.responseXml = responseXml;
    }

    public List<InteriorRoomLog> getInteriorRoomLogs() {
        return interiorRoomLogs;
    }

    public void setInteriorRoomLogs(List<InteriorRoomLog> interiorRoomLogs) {
        this.interiorRoomLogs = interiorRoomLogs;
    }

    public static void addInteriorRoomLogs(InteriorRoomLog interiorRoomLog) {
        if (interiorRoomLogs == null) {
            interiorRoomLogs = new ArrayList<InteriorRoomLog>();
        }
        interiorRoomLogs.add(interiorRoomLog);
    }

}
