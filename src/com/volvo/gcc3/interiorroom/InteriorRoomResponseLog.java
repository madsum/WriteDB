package com.volvo.gcc3.interiorroom;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;

public class InteriorRoomResponseLog extends AbstractQuery {

    private static final String CLASS_NAME = InteriorRoomResponseLog.class.getName();

    String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String responseXml;
    private static Connection connection;
    private static List<InteriorRoomResponseLog> interiorRoomLogs = new ArrayList<InteriorRoomResponseLog>();

    private static final String INSERT_INTERIOR_ROOM_LOG = "INSERT INTO INTERIOR_ROOM_LOG"
        + "(PNO12, STR_WEEK_FROM, STR_WEEK_TO, RESPONSE_XML, MODIFIED_DATE, LOG_TIME ) " + "VALUES(?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)";

    private static final String SELECT_INTERIOR_ROOM_LOG = "SELECT * FROM INTERIOR_ROOM_LOG " + "WHERE PNO12 = ? AND STR_WEEK_FROM = ? AND STR_WEEK_TO = ?";

    private static final String SELECT_INTERIOR_ROOM_LOG_BY_PNO12 = "SELECT * FROM INTERIOR_ROOM_LOG " + "WHERE PNO12 = ? ";

    private static final String SELECT_INTERIOR_ROOM_LOG_ALL = "SELECT * FROM INTERIOR_ROOM_LOG ";

    private static final String DELETE_INTERIOR_ROOM_LOG = "DELETE FROM INTERIOR_ROOM_LOG WHERE PNO12 = ?";

    private static final String DELETE_INTERIOR_ROOMS_FEATURES = "DELETE FROM INTERIOR_ROOMS_FEATURES WHERE MASTER_ROOM_ID IN (SELECT ROOM_ID FROM INTERIOR_ROOMS_MASTER WHERE PNO12  = ? )";

    private static final String DELETE_INTERIOR_ROOMS_MASTER = "DELETE FROM INTERIOR_ROOMS_MASTER WHERE PNO12  = ?";

    private final static String PNO12 = "PNO12";
    private final static String STR_WEEK_FROM = "STR_WEEK_FROM";
    private final static String STR_WEEK_TO = "STR_WEEK_TO";
    private final static String RESPONSE_XML = "RESPONSE_XML";


    public InteriorRoomResponseLog() {

    }

    public InteriorRoomResponseLog(String pno12, long strWeekFrom, long strWeekTo, String responseXml) {
        super();
        this.pno12 = pno12;
        this.strWeekFrom = strWeekFrom;
        this.strWeekTo = strWeekTo;
        this.responseXml = responseXml;
    }

    public InteriorRoomResponseLog(InteriorResponse interiorResponse, String responseXml) {
        super();
        this.pno12 = interiorResponse.getPno12();
        this.strWeekFrom = interiorResponse.getStartWeek();
        this.strWeekTo = interiorResponse.getEndWeek();
        this.responseXml = responseXml;
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

    public static void insertIntoInteriorLog(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(INSERT_INTERIOR_ROOM_LOG);
            pst.setString(1, interiorRoomLog.getPno12());
            pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            pst.setLong(3, interiorRoomLog.getStrWeekTo());
            pst.setBytes(4, interiorRoomLog.getResponseXml().getBytes());
            pst.execute();
            connection.commit();
            System.out.println("Interior Log insert commited");
            close(pst);
        } catch (SQLException e) {
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
        } catch (Exception ex) {
            System.out.println("Error when insert in interior log. Handle error " + ex.getMessage());
        } finally {

        }
    }

    public static boolean isPno12ExistInInteriorRoomLog(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG_BY_PNO12);
            pst.setString(1, interiorRoomLog.getPno12());
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        return retVal;
    }

    public static boolean isInteriorRoomLogExist(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG);
            pst.setString(1, interiorRoomLog.getPno12());
            pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            pst.setLong(3, interiorRoomLog.getStrWeekTo());
            rset = pst.executeQuery();
            InteriorRoomResponseLog dbInteriorRoomLog = makeInteriorRoomLog(rset);
            if (interiorRoomLog.equals(dbInteriorRoomLog)) {
                retVal = true;
            }else{
                retVal = false;
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        return retVal;
    }

    public void compareInteriorRoomLog(InteriorRoomResponseLog inputInteriorRoomLog, InteriorRoomResponseLog dbInteriorRoomLog) {
        inputInteriorRoomLog.equals(dbInteriorRoomLog);
    }

    public static boolean deleteInteriorRoomLog(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(DELETE_INTERIOR_ROOM_LOG);
            pst.setString(1, interiorRoomLog.getPno12());
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        if (retVal) {
            retVal = deleteInteriorFeatures(connection, interiorRoomLog.getPno12());
            if (retVal) {
                retVal = deleteInteriorMaster(connection, interiorRoomLog.getPno12());
                System.out.println("Delete from Log, master and featuer table successful!");
            }
        }
        return retVal;
    }

    public static boolean deleteInteriorFeatures(Connection connection, String pno12) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(DELETE_INTERIOR_ROOMS_FEATURES);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
            close(rset);
            close(pst);

        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        return retVal;
    }

    public static boolean deleteInteriorMaster(Connection connection, String pno12) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(DELETE_INTERIOR_ROOMS_MASTER);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
            close(rset);
            close(pst);

        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        return retVal;
    }

    public static boolean deleteInteriorMaster2(Connection connection, String pno12) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(DELETE_INTERIOR_ROOMS_MASTER);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
            close(rset);
            close(pst);

        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        }
        return retVal;
    }

    public static boolean interiorRoomLogsContains(InteriorRoomResponseLog interiorRoomLog) {
        boolean retVal = false;
        for (InteriorRoomResponseLog roomLog : interiorRoomLogs) {
            if (interiorRoomLog.equals(roomLog)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public static void getInteriorRoomLogAll(Connection connection) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG_ALL);
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
    /*
     * public static List<InteriorResponse> InteriorResponseList = new ArrayList<InteriorResponse>();
     * 
     * public static void addInteriorResponseList(String responseXml) {
     * XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
     * InteriorResponse interiorResponse = new InteriorResponse();
     * interiorResponse = xmlUnmarshaller.getInteriorResponse(responseXml, interiorResponse);
     * InteriorResponseList.add(interiorResponse);
     * }
     * 
     * 
     * public static void compareInteriorResponseList() {
     * InteriorResponse nextInteriorResponse = null;
     * int index = 1;
     * for (InteriorResponse currentinteriorResponse : InteriorResponseList) {
     * // prevInteriorResponse = currentinteriorResponse;
     * if (index < InteriorResponseList.size()) {
     * nextInteriorResponse = InteriorResponseList.get(index);
     * }
     * System.out.println(" currentinteriorResponse.equals(nextInteriorResponse)  " + currentinteriorResponse.equals(nextInteriorResponse));
     * index++;
     * }
     * }
     */

    private static InteriorRoomResponseLog makeInteriorRoomLog(ResultSet rset) {
        InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog();
        try {
            while (rset.next()) {
                interiorRoomLog.setPno12(rset.getString(PNO12));
                interiorRoomLog.setStrWeekFrom(rset.getLong(STR_WEEK_FROM));
                interiorRoomLog.setStrWeekTo(rset.getLong(STR_WEEK_TO));
                Blob xmlBlob = rset.getBlob(RESPONSE_XML);
                String responseXml = new String(xmlBlob.getBytes(1l, (int) xmlBlob.length()));
                interiorRoomLog.setResponseXml(responseXml);
                // addInteriorRoomLogs(interiorRoomLog);
                // addInteriorResponseList(responseXml);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.format("SQL State: %s\n", ex.getMessage());
        }
        return interiorRoomLog;
    }

    private static void addInteriorResponseList(ResultSet rset) {
        try {
            while (rset.next()) {
                InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog();
                interiorRoomLog.setPno12(rset.getString(PNO12));
                interiorRoomLog.setStrWeekFrom(rset.getLong(STR_WEEK_FROM));
                interiorRoomLog.setStrWeekTo(rset.getLong(STR_WEEK_TO));
                Blob xmlBlob = rset.getBlob(RESPONSE_XML);
                String responseXml = new String(xmlBlob.getBytes(1l, (int) xmlBlob.length()));
                interiorRoomLog.setResponseXml(responseXml);
                addInteriorRoomLogs(interiorRoomLog);
                // addInteriorResponseList(responseXml);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.format("SQL State: %s\n", ex.getMessage());
        }
        // printInteriorRoomLogList();
    }


    private static void printInteriorRoomLogList() {
        for (InteriorRoomResponseLog interiorRoomLog : interiorRoomLogs) {
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

    public List<InteriorRoomResponseLog> getInteriorRoomLogs() {
        return interiorRoomLogs;
    }

    public void setInteriorRoomLogs(List<InteriorRoomResponseLog> interiorRoomLogs) {
        this.interiorRoomLogs = interiorRoomLogs;
    }

    public static void addInteriorRoomLogs(InteriorRoomResponseLog interiorRoomLog) {
        if (interiorRoomLogs == null) {
            interiorRoomLogs = new ArrayList<InteriorRoomResponseLog>();
        }
        interiorRoomLogs.add(interiorRoomLog);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pno12 == null) ? 0 : pno12.hashCode());
        result = prime * result + ((responseXml == null) ? 0 : responseXml.hashCode());
        result = prime * result + (int) (strWeekFrom ^ (strWeekFrom >>> 32));
        result = prime * result + (int) (strWeekTo ^ (strWeekTo >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InteriorRoomResponseLog other = (InteriorRoomResponseLog) obj;
        if (pno12 == null) {
            if (other.pno12 != null)
                return false;
        } else if (!pno12.equals(other.pno12))
            return false;
        if (responseXml == null) {
            if (other.responseXml != null)
                return false;
        } else if (!responseXml.equals(other.responseXml))
            return false;
        if (strWeekFrom != other.strWeekFrom)
            return false;
        if (strWeekTo != other.strWeekTo)
            return false;
        return true;
    }

}
