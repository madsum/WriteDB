package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.InteriorRoom;

public class LoadInteriorRoomQuery extends AbstractQuery {

    private static final String CLASS_NAME = LoadInteriorRoomQuery.class.getName();

    private final static String PROGRAM_MARKET = "PROGRAM_MARKET";
    private final static String STR_WEEK_FROM = "STR_WEEK_FROM";
    private final static String STR_WEEK_TO = "STR_WEEK_TO";
    private final static String PNO12 = "PNO12";
    private final static String COLOR = "COLOR";
    private final static String UPHOLSTERY = "UPHOLSTERY";
    private final static String DATA_ELEMENT = "DATA_ELEMENT";
    private final static String STATE = "STATE";
    private final static String CODE = "CODE";
    private final static String MODIFIED_DATE = "MODIFIED_DATE";
    private final static String MODIFIED_BY = "MODIFIED_BY";

    private static final String SELECT_MASTER_AND_FEATURE_BY_ALL = "SELECT * FROM INTERIOR_ROOMS_MASTER master " + "JOIN INTERIOR_ROOMS_FEATURES feature on "
        + "feature.master_room_id = master.room_id " + "WHERE master.program_market = ? AND master.pno12 = ? "
        + "AND master.str_week_from = ? AND master.str_week_to = ? ";

    private static final String COUNT_INTEIOR_MASTER = "SELECT COUNT(*) FROM INTERIOR_ROOMS_MASTER "
        + "WHERE PROGRAM_MARKET = ? AND PNO12 = ? AND STR_WEEK_FROM = ? AND STR_WEEK_TO = ?";

    private static final String COUNT_INTEIOR_MASTER_BY_PNO12 = "SELECT COUNT(*) FROM INTERIOR_ROOMS_MASTER WHERE PNO12 = ? ";

    private static final String SELECT_FEATURE_OPTION_BY_COLOR_UPHOLSTREY = "SELECT * FROM INTERIOR_ROOMS_MASTER master "
        + "JOIN INTERIOR_ROOMS_FEATURES feature on feature.master_room_id = master.room_id " + "WHERE (master.program_market = ? AND master.pno12 = ? AND "
        + "master.str_week_from = ? AND master.str_week_to = ?) AND "
        + "(master.COLOR = ? OR master.COLOR = 'common') AND (master.UPHOLSTERY = ? OR master.UPHOLSTERY = 'common')";

    private static PreparedStatement prepareFetchInteriorMasterFeature = null;
    private static PreparedStatement prepareCountInteriorMaster = null;
    private static PreparedStatement prepareCountInteriorMasterByPno12 = null;
    private static PreparedStatement prepareSelectAll = null;
    private static PreparedStatement prepareSelectFeatures = null;

    /**
     * Prepaer statment for select all interior master feature once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareSelectAll(Connection connection) {
        if (prepareSelectAll == null) {
            try {
                prepareSelectAll = connection.prepareStatement(SELECT_MASTER_AND_FEATURE_BY_ALL);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareSelectAll;
    }

    public static InteriorResponse getInteriorRooms(Connection connection, String programMarket, String pno12, long strWeekFrom, long strWeekTo,
        List<String> options) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        InteriorResponse interiorResponse = null;
        try {
            pst = prepareSelectAll(connection);
            pst.setString(1, programMarket);
            pst.setString(2, pno12);
            pst.setLong(3, strWeekFrom);
            pst.setLong(4, strWeekTo);
            rset = pst.executeQuery();
            interiorResponse = makeInteriorResponse(rset);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("SQL select excetion: %s" + e.getMessage());
        } finally {
            close(rset);
            close(connection);
        }
        return interiorResponse;
    }

    public static InteriorResponse getInteriorRooms(Connection connection, InteriorResponse interiorResponse) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = prepareSelectAll(connection);
            pst.setString(1, interiorResponse.getProgramMarket());
            pst.setString(2, interiorResponse.getPno12());
            pst.setLong(3, interiorResponse.getStartWeek());
            pst.setLong(4, interiorResponse.getEndWeek());
            rset = pst.executeQuery();
            interiorResponse = makeInteriorResponse(rset);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("SQL select excetion: %s" + e.getMessage());
        } finally {
            close(rset);
            close(connection);
        }
        return interiorResponse;
    }

    /**
     * Prepaer statment for select interior feature once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareSelectFeatures(Connection connection) {
        if (prepareSelectFeatures == null) {
            try {
                prepareSelectFeatures = connection.prepareStatement(SELECT_FEATURE_OPTION_BY_COLOR_UPHOLSTREY);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareSelectFeatures;
    }

    public static Map<String, List<String>> getInteriorRoomFeaturs(Connection connection, String programMarket, String pno12, long strWeekFrom, long strWeekTo,
        String color, String upholstery) {
        InteriorResponse interiorResponse = null;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = prepareSelectFeatures(connection);
            pst.setString(1, programMarket);
            pst.setString(2, pno12);
            pst.setLong(3, strWeekFrom);
            pst.setLong(4, strWeekTo);
            pst.setString(5, color);
            pst.setString(6, upholstery);
            rset = pst.executeQuery();
            interiorResponse = makeInteriorResponse(rset);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("SQL select excetion: %s" + e.getMessage());
        } finally {
            close(rset);
        }
        return makeInteriorFeatureResponse(interiorResponse);
    }

    /**
     * Prepaer statment for count interior master once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareCountInteriorMaster(Connection connection) {
        if (prepareCountInteriorMaster == null) {
            try {
                prepareCountInteriorMaster = connection.prepareStatement(COUNT_INTEIOR_MASTER);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareCountInteriorMaster;
    }

    public static boolean isPno12Exist(Connection connection, String programMarket, String pno12, long str_week_from, long str_week_to) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = prepareCountInteriorMaster(connection);
            pst.setString(1, programMarket);
            pst.setString(2, pno12);
            pst.setLong(3, str_week_from);
            pst.setLong(4, str_week_to);
            rset = pst.executeQuery();
            int size = 0;
            if (rset != null) {
                rset.last();
                size = rset.getRow();
                if (size > 0) {
                    retVal = true;
                }
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            close(rset);
        }
        return retVal;
    }

    /**
     * Prepaer statment for count interior master by pno12 once and ues the same always wehn it is done. It is expensive call.
     * 
     * @param connection
     * @throws SQLException
     * 
     */

    static PreparedStatement prepareCountInteriorMasterByPno12(Connection connection) {
        if (prepareCountInteriorMasterByPno12 == null) {
            try {
                prepareCountInteriorMasterByPno12 = connection.prepareStatement(COUNT_INTEIOR_MASTER_BY_PNO12);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareCountInteriorMasterByPno12;
    }

    public static boolean isPno12Exist(Connection connection, String pno12) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = prepareCountInteriorMasterByPno12(connection);
            pst.setString(1, pno12);
            rset = pst.executeQuery();
            int size = 0;
            if (rset != null) {
                rset.last();
                size = rset.getRow();
                if (size > 0) {
                    retVal = true;
                }
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            close(rset);
        }
        return retVal;
    }

    /**
     * We prepare each statment once and close all when finished.
     * 
     */

    public static void closeAllResource() {
        close(prepareFetchInteriorMasterFeature);
        prepareFetchInteriorMasterFeature = null;
        close(prepareCountInteriorMaster);
        prepareCountInteriorMaster = null;
        close(prepareCountInteriorMasterByPno12);
        prepareCountInteriorMasterByPno12 = null;
        close(prepareSelectAll);
        prepareSelectAll = null;
        close(prepareSelectFeatures);
        prepareSelectFeatures = null;
    }

    /**
     * We makeInteriorResponse from database row
     * 
     * @param rset
     * @return interiorResponseList
     * 
     */
    private static InteriorResponse makeInteriorResponse(ResultSet rset) {
        InteriorResponse interiorResponse = new InteriorResponse();
        try {
            while (rset.next()) {
                interiorResponse.setProgramMarket(rset.getString(PROGRAM_MARKET));
                interiorResponse.setStartWeek(rset.getLong(STR_WEEK_FROM));
                interiorResponse.setEndWeek(rset.getLong(STR_WEEK_TO));
                interiorResponse.setPno12(rset.getString(PNO12));

                String color = rset.getString(COLOR);
                String upholstrey = rset.getString(UPHOLSTERY);
                int dataElement = rset.getInt(DATA_ELEMENT);
                InteriorRoom interiorRoom = isInteriorRoomExist(interiorResponse, color);
                interiorRoom.setColor(color);
                interiorRoom.setUpholstery(upholstrey);
                interiorRoom.setDataElement(dataElement);

                if (dataElement == 115 && color.equalsIgnoreCase(interiorResponse.getCommon())) {
                    String commonfeature = rset.getString(CODE);
                    interiorResponse.addCommonFeatureList(commonfeature);
                } else if (dataElement == 12 && color.equalsIgnoreCase(interiorResponse.getCommon())) {
                    String commonOption = rset.getString(CODE);
                    interiorResponse.addCommonOptionList(commonOption);
                } else if (dataElement == 115 && !color.equalsIgnoreCase(interiorResponse.getCommon())) {
                    String individualFeature = rset.getString(CODE);
                    interiorRoom.addFeatureList(individualFeature);
                } else if (dataElement == 12 && !color.equalsIgnoreCase(interiorResponse.getCommon())) {
                    String individualOption = rset.getString(CODE);
                    interiorRoom.addOptionList(individualOption);
                }
                interiorResponse.setModifiedDate(rset.getDate(MODIFIED_DATE));
                interiorResponse.setModifiedBy(rset.getString(MODIFIED_BY));
                interiorResponse.addInteriorRoomList(interiorRoom);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return interiorResponse;
    };

    static InteriorRoom isInteriorRoomExist(InteriorResponse interiorResponse, String color) {
        for (InteriorRoom elementInteriorRoom : interiorResponse.getInteriorRoomList()) {
            if (elementInteriorRoom.getColor().equalsIgnoreCase(color)) {
                return elementInteriorRoom;
            }
        }
        return new InteriorRoom();
    }

    /**
     * VBS wants a map which contains list as value of each type of feature and options
     * 
     * @param interiorResponse
     * @return featureOptionMap
     * 
     */

    private static Map<String, List<String>> makeInteriorFeatureResponse(InteriorResponse interiorResponse) {
        Map<String, List<String>> featureOptionMap = new HashMap<String, List<String>>();
        String keyCommonFeature = "keyCommonFeature";
        String keyCommonOption = "keyCommonOption";
        String keyIndividualFeature = "keyIndividualFeature";
        String keyIndividualOption = "keyIndividualOption";

        featureOptionMap.put(keyCommonFeature, interiorResponse.getCommonFeatureList());
        featureOptionMap.put(keyCommonOption, interiorResponse.getCommonOptionList());
        for (InteriorRoom intriorRoom : interiorResponse.getInteriorRoomList()) {
            featureOptionMap.put(keyIndividualFeature, intriorRoom.getFeatureList());
            featureOptionMap.put(keyIndividualOption, intriorRoom.getOptionList());
        }
        return featureOptionMap;
    }

    public static void printData(InteriorResponse interiorResponse) {
        System.out.println("Priting read data from the DB\n\n");
        System.out.println("StartWeek: " + interiorResponse.getStartWeek());
        System.out.println("EndWeek: " + interiorResponse.getEndWeek());
        System.out.println("Pno12: " + interiorResponse.getPno12());

        List<String> commonFeatureList = interiorResponse.getCommonFeatureList();
        for (String feature : commonFeatureList) {
            System.out.println("Common feature code: " + feature);
        }

        List<String> commonOptionList = interiorResponse.getCommonOptionList();
        for (String option : commonOptionList) {
            System.out.println("Common Option: " + option);
        }

        List<InteriorRoom> interiorRoomList = interiorResponse.getInteriorRoomList();

        for (InteriorRoom interiorRoom : interiorRoomList) {
            System.out.println("CU Color: " + interiorRoom.getColor());
            System.out.println("CU Upholstery: " + interiorRoom.getUpholstery());
            System.out.println(interiorRoom.getColor() + "ColUph's features:- ");
            for (String feature : interiorRoom.getFeatureList()) {
                System.out.println("ColUph's feature code: " + feature);
            }
            System.out.println(interiorRoom.getColor() + "ColUph's Optoine:- ");
            for (String option : interiorRoom.getOptionList()) {
                System.out.println("ColUph's Optoine code: " + option);
            }
        }
        System.out.println("Modified Date: " + interiorResponse.getModifiedDate().toString());
        System.out.println("Modified By: " + interiorResponse.getModifiedBy());
    }
}
