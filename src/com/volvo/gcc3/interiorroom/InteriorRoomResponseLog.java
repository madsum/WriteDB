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

    private String pno12;
    private long strWeekFrom;
    private long strWeekTo;
    private String responseXml;
    private static List<InteriorRoomResponseLog> interiorRoomLogs = new ArrayList<InteriorRoomResponseLog>();
    private final static String PNO12 = "PNO12";
    private final static String STR_WEEK_FROM = "STR_WEEK_FROM";
    private final static String STR_WEEK_TO = "STR_WEEK_TO";
    private final static String RESPONSE_XML = "RESPONSE_XML";
    private static PreparedStatement prepareInsertInteriorLog = null;
    private static PreparedStatement prepareSelectInteriorLogByPno12 = null;
    private static PreparedStatement prepareSelectInteriorLog = null;
    private static PreparedStatement prepareDeleteInteriorLog = null;
    private static PreparedStatement prepareDeleteInteriorFeature = null;
    private static PreparedStatement prepareDeleteInteriorMaster = null;
    private static PreparedStatement prepareSelectAllInteriorRoom = null;

    private static final String INSERT_INTERIOR_ROOM_LOG = "INSERT INTO INTERIOR_ROOM_LOG"
        + "(PNO12, STR_WEEK_FROM, STR_WEEK_TO, RESPONSE_XML, MODIFIED_DATE, LOG_TIME ) " + "VALUES(?, ?, ?, ?, SYSDATE, SYSTIMESTAMP)";

    private static final String SELECT_INTERIOR_ROOM_LOG = "SELECT * FROM INTERIOR_ROOM_LOG " + "WHERE PNO12 = ? AND STR_WEEK_FROM = ? AND STR_WEEK_TO = ?";

    private static final String SELECT_INTERIOR_ROOM_LOG_BY_PNO12 = "SELECT * FROM INTERIOR_ROOM_LOG " + "WHERE PNO12 = ? ";

    private static final String SELECT_INTERIOR_ROOM_LOG_ALL = "SELECT * FROM INTERIOR_ROOM_LOG ";

    private static final String DELETE_INTERIOR_ROOM_LOG = "DELETE FROM INTERIOR_ROOM_LOG WHERE PNO12 = ?";

    private static final String DELETE_INTERIOR_ROOMS_FEATURES = "DELETE FROM INTERIOR_ROOMS_FEATURES WHERE MASTER_ROOM_ID IN (SELECT ROOM_ID FROM INTERIOR_ROOMS_MASTER WHERE PNO12  = ? )";

    private static final String DELETE_INTERIOR_ROOMS_MASTER = "DELETE FROM INTERIOR_ROOMS_MASTER WHERE PNO12  = ?";

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
     * Prepaer statment for interior room log insert once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareInsertInteriorLog(Connection connection) {
        if (prepareInsertInteriorLog == null) {
            try {
                prepareInsertInteriorLog = connection.prepareStatement(INSERT_INTERIOR_ROOM_LOG);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareInsertInteriorLog;
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
            pst = prepareInsertInteriorLog(connection);
            pst.setString(1, interiorRoomLog.getPno12());
            pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            pst.setLong(3, interiorRoomLog.getStrWeekTo());
            pst.setBytes(4, interiorRoomLog.getResponseXml().getBytes());
            pst.execute();
            connection.commit();
            System.out.println("Interior Log insert commited");
        } catch (SQLException e) {
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
        } catch (Exception ex) {
            System.out.println("Error when insert in interior log. Handle error " + ex.getMessage());
        }
    }

    /**
     * Prepaer statment for interior room log select by pno12 once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareSelectInteriorLogByPno12(Connection connection) {
        if (prepareSelectInteriorLogByPno12 == null) {
            try {
                prepareSelectInteriorLogByPno12 = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG_BY_PNO12);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareSelectInteriorLogByPno12;
    }

    public static boolean isPno12ExistInInteriorRoomLog(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = prepareSelectInteriorLogByPno12(connection);
            pst.setString(1, interiorRoomLog.getPno12());
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
        }
        return retVal;
    }

    /**
     * Prepaer statment for interior room log select once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareSelectInteriorLog(Connection connection) {
        if (prepareSelectInteriorLog == null) {
            try {
                prepareSelectInteriorLog = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareSelectInteriorLog;
    }

    public static boolean isInteriorRoomLogExist(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = prepareSelectInteriorLog(connection);
            pst.setString(1, interiorRoomLog.getPno12());
            pst.setLong(2, interiorRoomLog.getStrWeekFrom());
            pst.setLong(3, interiorRoomLog.getStrWeekTo());
            rset = pst.executeQuery();
            InteriorRoomResponseLog dbInteriorRoomLog = makeInteriorRoomLog(rset);
            if (interiorRoomLog.equals(dbInteriorRoomLog)) {
                retVal = true;
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
        }
        return retVal;
    }

    public void compareInteriorRoomLog(InteriorRoomResponseLog inputInteriorRoomLog, InteriorRoomResponseLog dbInteriorRoomLog) {
        inputInteriorRoomLog.equals(dbInteriorRoomLog);
    }

    /**
     * Prepaer statment for interior room log delete once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareDeleteInteriorLog(Connection connection) {
        if (prepareDeleteInteriorLog == null) {
            try {
                prepareDeleteInteriorLog = connection.prepareStatement(DELETE_INTERIOR_ROOM_LOG);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareDeleteInteriorLog;
    }

    public static boolean deleteInteriorRoomLog(Connection connection, InteriorRoomResponseLog interiorRoomLog) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = prepareDeleteInteriorLog(connection);
            pst.setString(1, interiorRoomLog.getPno12());
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
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

    /**
     * Prepaer statment for interior room feature delete once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareDeleteInteriorFeature(Connection connection) {
        if (prepareDeleteInteriorFeature == null) {
            try {
                prepareDeleteInteriorFeature = connection.prepareStatement(DELETE_INTERIOR_ROOMS_FEATURES);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareDeleteInteriorFeature;
    }

    public static boolean deleteInteriorFeatures(Connection connection, String pno12) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = prepareDeleteInteriorFeature(connection);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
        }
        return retVal;
    }

    /**
     * Prepaer statment for interior room log delete once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareDeleteInteriorMaster(Connection connection) {
        if (prepareDeleteInteriorMaster == null) {
            try {
                prepareDeleteInteriorMaster = connection.prepareStatement(DELETE_INTERIOR_ROOMS_MASTER);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareDeleteInteriorMaster;
    }

    public static boolean deleteInteriorMaster(Connection connection, String pno12) {
        PreparedStatement pst = null;
        boolean retVal = false;
        ResultSet rset = null;
        try {
            pst = prepareDeleteInteriorMaster(connection);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            if (rset.next()) {
                retVal = true;
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
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

    /**
     * Prepaer statment for select all interior room log once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareSelectAllInteriorRoom(Connection connection) {
        if (prepareSelectAllInteriorRoom == null) {
            try {
                prepareSelectAllInteriorRoom = connection.prepareStatement(SELECT_INTERIOR_ROOM_LOG_ALL);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareSelectAllInteriorRoom;
    }


    public static void getInteriorRoomLogAll(Connection connection) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = prepareSelectAllInteriorRoom(connection);
            rset = pst.executeQuery();
            addInteriorResponseList(rset);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.format("SQL State: %s\n", e.getMessage());
        } finally {
            close(rset);
            close(connection);
        }
    }

    public static void closeAllResource() {
        close(prepareInsertInteriorLog);
        prepareInsertInteriorLog = null;
        close(prepareSelectInteriorLogByPno12);
        prepareSelectInteriorLogByPno12 = null;
        close(prepareSelectInteriorLog);
        prepareSelectInteriorLog = null;
        close(prepareDeleteInteriorLog);
        prepareDeleteInteriorLog = null;
        close(prepareDeleteInteriorFeature);
        prepareDeleteInteriorFeature = null;
        close(prepareDeleteInteriorMaster);
        prepareDeleteInteriorMaster = null;
        close(prepareSelectAllInteriorRoom);
        prepareSelectAllInteriorRoom = null;
        interiorRoomLogs.clear();
    }


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
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.format("SQL State: %s\n", ex.getMessage());
        }
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
