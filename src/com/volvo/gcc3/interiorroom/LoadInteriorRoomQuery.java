package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.InteriorRoom;

public class LoadInteriorRoomQuery extends AbstractQuery {

    private static final String CLASS_NAME = LoadInteriorRoomQuery.class.getName();


    private final static String ROOM_ID = "ROOM_ID";
    private final static String PROGRAM_MARKET = "PROGRAM_MARKET";
    private final static String STR_WEEK_FROM = "STR_WEEK_FROM";
    private final static String STR_WEEK_TO = "STR_WEEK_TO";
    private final static String PNO12 = "PNO12";
    private final static String COLOR = "COLOR";
    private final static String UPHOLSTERY = "UPHOLSTERY";
    private final static String MASTER_ROOM_ID = "MASTER_ROOM_ID";
    private final static String DATA_ELEMENT = "DATA_ELEMENT";
    private final static String STATE = "STATE";
    private final static String CODE = "CODE";
    private final static String MODIFIED_DATE = "MODIFIED_DATE";
    private final static String MODIFIED_BY = "MODIFIED_BY";

    private static final String SELECT_MASTER_AND_FEATURE_BY_ALL = "SELECT * FROM INTERIOR_ROOMS_MASTER master "
        + "JOIN INTERIOR_ROOMS_FEATURES feature on " + "feature.master_room_id = master.room_id " + "WHERE master.program_market = ? AND master.pno12 = ? "
        + "AND master.str_week_from = ? AND master.str_week_to = ? ";

    private static final String SELECT_INTEIOR_MASTER = "SELECT COUNT(*) FROM INTERIOR_ROOMS_MASTER "
        + "WHERE PROGRAM_MARKET = ? AND PNO12 = ? AND STR_WEEK_FROM = ? AND STR_WEEK_TO = ?";

    private static final String SELECT_INTEIOR_MASTER_BY_PNO12 = "SELECT COUNT(*) FROM INTERIOR_ROOMS_MASTER WHERE PNO12 = ? ";

    private static List<InteriorResponse> interiorResponseList = new ArrayList<InteriorResponse>();


    public static List<InteriorResponse> getInteriorRooms(Connection connection, String programMarket, String pno12, long str_week_from, long str_week_to,
        List<String> options) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(SELECT_MASTER_AND_FEATURE_BY_ALL);
            pst.setString(1, programMarket);
            pst.setString(2, pno12);
            pst.setLong(3, str_week_from);
            pst.setLong(4, str_week_to);
            rset = pst.executeQuery();
            addInteriorResponseList(rset);
            commit(connection);
            close(rset);
            close(pst);
            close(connection);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("SQL select excetion: %s" + e.getMessage());
        }
        return interiorResponseList;
    }
    
    public static List<InteriorResponse> getInteriorRooms(Connection connection, InteriorResponse interiorResponse) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(SELECT_MASTER_AND_FEATURE_BY_ALL);
            pst.setString(1, interiorResponse.getProgramMarket());
            pst.setString(2, interiorResponse.getPno12());
            pst.setLong(3, interiorResponse.getStartWeek());
            pst.setLong(4, interiorResponse.getEndWeek());
            rset = pst.executeQuery();
            makeInteriorResponse(rset);
            // addInteriorResponseList(rset);
            close(rset);
            close(pst);
            commit(connection);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("SQL select excetion: %s" + e.getMessage());
        }
        return interiorResponseList;
    }

    public static boolean isPno12Exist(Connection connection, String programMarket, String pno12, long str_week_from, long str_week_to) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = connection.prepareStatement(SELECT_INTEIOR_MASTER);
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
            close(rset);
            close(pst);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return retVal;
    }

    public static boolean isPno12Exist(Connection connection, String pno12) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        boolean retVal = false;
        try {
            pst = connection.prepareStatement(SELECT_INTEIOR_MASTER_BY_PNO12);
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
            close(rset);
            close(pst);
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return retVal;
    }

    private static void makeInteriorResponse(ResultSet rset) {
        try {
            InteriorResponse interiorResponse = new InteriorResponse();
            String color = null;
            String upholstrey = null;
            InteriorRoom interiorRoom = new InteriorRoom();
            // color = rset.getString(COLOR);
            while (rset.next()) {
                interiorResponse.setStartWeek(rset.getLong(STR_WEEK_FROM));
                interiorResponse.setEndWeek(rset.getLong(STR_WEEK_TO));
                interiorResponse.setPno12(rset.getString(PNO12));
                color = rset.getString(COLOR);
                upholstrey = rset.getString(UPHOLSTERY);
                int dataElement = rset.getInt(DATA_ELEMENT);
                interiorRoom.setColor(color);
                interiorRoom.setUpholstery(upholstrey);
                addList(rset, color, dataElement, interiorResponse, interiorRoom);
                while (rset.next()) {
                    String tcolor = rset.getString(COLOR);
                    String tupholstrey = rset.getString(UPHOLSTERY);
                    if (color.equalsIgnoreCase(rset.getString(COLOR)) && upholstrey.equalsIgnoreCase(rset.getString(UPHOLSTERY))) {
                        addList(rset, color, dataElement, interiorResponse, interiorRoom);
                    } else {
                        interiorRoom = new InteriorRoom();
                        break;
                    }
                }

                interiorRoom.setColor(color);
                interiorRoom.setUpholstery(upholstrey);
                // interiorResponse.addInteriorRoomList(interiorRoom);



                // interiorResponse.setDataElement(dataElement);
                interiorResponse.addInteriorRoomList(interiorRoom);

            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private static void addList(ResultSet rset, String color, int dataElement, InteriorResponse interiorResponse, InteriorRoom interiorRoom) {
        try {

            if (dataElement == 115 && color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                String commonfeature = rset.getString(CODE);
                interiorResponse.addCommonFeatureList(commonfeature);
                interiorResponse.setDataElement(dataElement);
            } else if (dataElement == 12 && color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                interiorResponse.addCommonOptionList(rset.getString(CODE));
            } else if (dataElement == 115 && !color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                String feature = rset.getString(CODE);
                interiorRoom.addFeatureList(feature);
            } else if (dataElement == 12 && !color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                String option = rset.getString(CODE);
                interiorRoom.addOptionList(option);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

    }



    /**
     * Internal helper mentod to set all value
     * 
     * @param rset
     * 
     */
    private static void addInteriorResponseList(ResultSet rset) {
        try {
            while (rset.next()) {
                InteriorResponse interiorResponse = new InteriorResponse();
                interiorResponse.setProgramMarket(rset.getString(PROGRAM_MARKET));
                interiorResponse.setStartWeek(rset.getLong(STR_WEEK_FROM));
                interiorResponse.setEndWeek(rset.getLong(STR_WEEK_TO));
                interiorResponse.setPno12(rset.getString(PNO12));

                InteriorRoom interiorRoom = new InteriorRoom();
                String color = rset.getString(COLOR);
                String upholstrey = rset.getString(UPHOLSTERY);
                interiorRoom.setColor(color);
                interiorRoom.setUpholstery(upholstrey);
                interiorResponse.addInteriorRoomList(interiorRoom);
                int dataElement = rset.getInt(DATA_ELEMENT);

                if (dataElement == 115 && color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                    String commonfeature = rset.getString(CODE);
                    interiorResponse.addCommonFeatureList(commonfeature);
                    interiorResponse.setDataElement(dataElement);
                } else if (dataElement == 12 && color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                    interiorResponse.addCommonOptionList(rset.getString(CODE));
                } else if (dataElement == 115 && !color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                    String feature = rset.getString(CODE);
                    interiorRoom.addFeatureList(feature);
                } else if (dataElement == 12 && !color.equalsIgnoreCase(InteriorRoomServiceDaoImpl.getCommon())) {
                    String option = rset.getString(CODE);
                    interiorRoom.addOptionList(option);
                }
                interiorResponse.setDataElement(dataElement);
                interiorResponse.setModifiedDate(rset.getDate(MODIFIED_DATE));
                interiorResponse.setModifiedBy(rset.getString(MODIFIED_BY));
                interiorResponse.addInteriorRoomList(interiorRoom);
                interiorResponseList.add(interiorResponse);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        printInteriorResponseList();
    }

    public static void printInteriorResponseList() {
        System.out.println("interiorResponseList size: " + interiorResponseList.size());
        for (InteriorResponse interiorResponse : interiorResponseList) {
            printData(interiorResponse);
        }
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
