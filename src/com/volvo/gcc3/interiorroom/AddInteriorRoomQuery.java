package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.volvo.gcc3.interiorroom.batch.InteriorDetails;
import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.InteriorRoom;

public class AddInteriorRoomQuery extends AbstractQuery {

    private static final String CLASS_NAME = AddInteriorRoomQuery.class.getName();

    static long uniqeIndexErrorCode = 23000l;

    private static final String INSERT_INTERIOR_ROOMS_MASTER = "INSERT INTO INTERIOR_ROOMS_MASTER"
        + "(PROGRAM_MARKET, STR_WEEK_FROM, STR_WEEK_TO, PNO12, COLOR, UPHOLSTERY, MODIFIED_DATE, MODIFIED_BY ) "
        + "VALUES(?, ?, ?, ?, ?, ?, SYSDATE,\'CPAMIMPORT\')";

    private static final String INSERT_INTERIOR_ROOMS_FEATURES = "INSERT INTO INTERIOR_ROOMS_FEATURES "
        + "(MASTER_ROOM_ID, DATA_ELEMENT, CODE, MODIFIED_DATE, MODIFIED_BY) " + "VALUES( ?, ?, ?, SYSDATE,\'CPAMIMPORT\')";

    private final static String ROOM_ID = "ROOM_ID";
    private static long masterKey = -1l;

    private static List<InteriorResponse> failedInteriorResponseList = new ArrayList<InteriorResponse>();
    private static List<InteriorDetails> failedInteriorDetailsList = new ArrayList<InteriorDetails>();

    /**
     * Calls helper method to add data in INTERIOR_ROOMS_MASTER table
     * 
     * @param connectoin
     * @param interiorResponse
     * 
     */
    public static long insertIntoriorRoomData(Connection connection, InteriorResponse interiorResponse) {
        try {
            masterKey = insertIntoInteriorMaster(connection, interiorResponse.getProgramMarket(), interiorResponse.getStartWeek(),
                interiorResponse.getEndWeek(), interiorResponse.getPno12(), InteriorRoomServiceDaoImpl.getCommon(), InteriorRoomServiceDaoImpl.getCommon());

            System.out.println("Master insert primary key: " + masterKey);
            if (masterKey == uniqeIndexErrorCode) {
                System.out.println("This row already exit in the table. Just inore it and retrun");
                return masterKey;
            }

            long retVal = insertCommonFeaturData(connection, interiorResponse, masterKey);
            if (retVal == -1) {
                System.out.println("Error to insert common data");
            }
            for (InteriorRoom interiorRoom : interiorResponse.getInteriorRoomList()) {

                masterKey = insertIntoInteriorMaster(connection, interiorResponse.getProgramMarket(), interiorResponse.getStartWeek(),
                    interiorResponse.getEndWeek(),
                    interiorResponse.getPno12(), interiorRoom.getColor(), interiorRoom.getUpholstery());

                System.out.println("Master insert primary key: " + masterKey);
                if (masterKey == uniqeIndexErrorCode) {
                    System.out.println("This row already exit in the table. Just inore it and retrun");
                    return masterKey;
                }

                retVal = insertIndividualFeatureData(connection, interiorRoom, masterKey);
                if (retVal == -1) {
                    System.out.println("Error to insert individual data");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        } finally {
            close(prepareInsertInteriorMaster);
        }
        return masterKey;
    }

    private static PreparedStatement prepareInsertInteriorMaster = null;

    static PreparedStatement prepareInsertInteriorMaster(Connection connection) {
        String key[] = { ROOM_ID };
        if (prepareInsertInteriorMaster == null) {
            try {
                prepareInsertInteriorMaster = connection.prepareStatement(INSERT_INTERIOR_ROOMS_MASTER, key);
            } catch (SQLException e) {
                String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                System.out.println(errorMsg);
            } catch (Exception ex) {
                System.out.println("Error when insert in master. Handle error " + ex.getMessage());
            }
        }
        return prepareInsertInteriorMaster;
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

    static public Long insertIntoInteriorMaster(Connection connection, String programMaster, long startWeek, long endWeek, String pno12, String color,
        String upholstrey) {
        // String key[] = { ROOM_ID };
        Long retValue = -1l;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = prepareInsertInteriorMaster(connection); /// connection.prepareStatement(INSERT_INTERIOR_ROOMS_MASTER, key);
            pst.setString(1, programMaster);
            pst.setLong(2, startWeek);
            pst.setLong(3, endWeek);
            pst.setString(4, pno12);
            pst.setString(5, color);
            pst.setString(6, upholstrey);
            pst.executeUpdate();
            rset = pst.getGeneratedKeys();
            connection.commit();
            System.out.println("master insert commited");
            if (rset.next()) {
                retValue = rset.getLong(1);
                System.out.println("Master Primary key: " + retValue);
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            /*
             * try {
             * connection.rollback();
             * } catch (SQLException e1) {
             * // nothing
             * }
             */
            // if e.getSQLState() returns 23000. It means integrity constraint violation of the unique index
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
            failedInteriorDetailsList.add(new InteriorDetails(pno12, startWeek, endWeek, color, upholstrey));
            /*
             * try {
             * InteriorRoomServiceDaoImpl.addFailedInteriorRoomRequests(makeFailedInteriorRoomRequest(connection, programMaster, pno12, startWeek, endWeek,
             * errorMsg));
             * } catch (Exception e1) {
             * System.out.println("Exception to insert FailedInteriorRoomRequest: " + e1.getMessage());
             * }
             */
            retValue = convertErroCode(e.getSQLState());
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * Adds data in INTERIOR_ROOMS_MASTER table
     * This method will be used by the InteriorRoomBatch
     * 
     * @param connection
     * @param interiorDetails
     * @throws DatabaseConnectionException
     * @throws SQLException
     * 
     */

    static public Long insertIntoInteriorMaster(Connection connection, InteriorDetails interiorDetails) {
        String key[] = { ROOM_ID };
        Long retValue = -1l;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(INSERT_INTERIOR_ROOMS_MASTER, key);
            pst.setString(1, interiorDetails.getProgramMarket());
            pst.setLong(2, interiorDetails.getStrWeekFrom());
            pst.setLong(3, interiorDetails.getStrWeekTo());
            pst.setString(4, interiorDetails.getPno12());
            pst.setString(5, "color");
            pst.setString(6, "upholstrey");
            pst.executeUpdate();
            rset = pst.getGeneratedKeys();
            connection.commit();
            System.out.println("master insert commited");
            if (rset.next()) {
                retValue = rset.getLong(1);
                System.out.println("Master Primary key: " + retValue);
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                // Nothing
            }
            // if e.getSQLState() returns 23000. It means integrity constraint violation of the unique index
            String errorMsg = String.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println(errorMsg);
            try {
                InteriorRoomServiceDaoImpl.addFailedInteriorRoomRequests(makeFailedInteriorRoomRequest(connection, interiorDetails, errorMsg));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            retValue = convertErroCode(e.getSQLState());

        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * Make FailedInteriorRoomRequest and insert into FAILED_INTERIOR_ROOM
     * 
     * @param connection
     * @param programMarket
     * @param pno12
     * @param startWeek
     * @param endWeek
     * @param xmlContent
     * @throws DatabaseConnectionException
     * @throws SQLException
     * 
     */

    private static FailedInteriorRoomRequest makeFailedInteriorRoomRequest(Connection connection, String programMarket, String pno12, long strWeekFrom,
        long strWeekTo, String xmlContent) {
        FailedInteriorRoomRequest failedInteriorRoomRequest = new FailedInteriorRoomRequest(connection, programMarket, pno12, strWeekFrom, strWeekTo,
            xmlContent);
        return failedInteriorRoomRequest;
    }

    private static FailedInteriorRoomRequest makeFailedInteriorRoomRequest(Connection connection, InteriorDetails interiorDetails, String xmlContent) {
        FailedInteriorRoomRequest failedInteriorRoomRequest = new FailedInteriorRoomRequest(connection, interiorDetails.getProgramMarket(),
            interiorDetails.getPno12(), interiorDetails.getStrWeekFrom(), interiorDetails.getStrWeekTo(), xmlContent);
        return failedInteriorRoomRequest;
    }

    /**
     * Calls helper method to add common data in INTERIOR_ROOMS_FEATURES table
     * 
     * @param interiorResponse
     * @param masterId
     * @return retVal
     * 
     */

    private static long insertCommonFeaturData(Connection connection, InteriorResponse interiorResponse, long masterId) {
        long retVal = -1l;
        for (String commonFeature : interiorResponse.getCommonFeatureList()) {
            retVal = insertFeatureData(connection, masterId, 115, commonFeature);
            System.out.println("Common feature insert: " + retVal);
            if (retVal == -1l) {
                System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
        }

        for (String commonOption : interiorResponse.getCommonOptionList()) {
            retVal = insertFeatureData(connection, masterId, 12, commonOption);
            System.out.println("Common option insert: " + retVal);
            if (retVal == -1l) {
                System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
        }
        if (retVal == -1) {
            System.out.println("Error to insert common data");
        }
        return retVal;
    }

    /**
     * Adds data in INTERIOR_ROOMS_FEATURES table
     * 
     * @param masterId
     * @param dataElement
     * @param state
     * @param color
     * @param common
     * 
     */

    private static long insertIndividualFeatureData(Connection connection, InteriorRoom interiorRoom, long masterId) {
        long retVal = -1;
        for (String feature : interiorRoom.getFeatureList()) {
            retVal = insertFeatureData(connection, masterId, 115, feature);
            System.out.println(interiorRoom.getColor() + " Coloer's " + " Individual feature insert : " + retVal);
            if (retVal == -1l) {
                System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
        }
        for (String option : interiorRoom.getOptionList()) {
            retVal = insertFeatureData(connection, masterId, 12, option);
            System.out.println(interiorRoom.getColor() + " Coloer's " + " individual option insert: " + retVal);
            if (retVal == -1l) {
                System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
        }

        return retVal;
    }

    /**
     * Adds data in INTERIOR_ROOMS_FEATURES table
     * 
     * @param masterId
     * @param dataElement
     * @param state
     * @param color
     * @param common
     * @throws SQLException
     * 
     */

    private static long insertFeatureData(Connection connection, long masterId, int dataElement, String code) {
        long retVal = -1l;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(INSERT_INTERIOR_ROOMS_FEATURES);
            pst.setLong(1, masterId);
            pst.setInt(2, dataElement);
            pst.setString(3, code);
            rset = pst.executeQuery();
            connection.commit();
            if (rset.next() == true) {
                retVal = 1l;
            } else {
                retVal = -1l;
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                /* Nothing */}
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
        return retVal;
    }

    /**
     * Adds data in INTERIOR_ROOMS_FEATURES table
     * 
     * @param masterId
     * @param dataElement
     * @param state
     * @param color
     * @param common
     * @throws SQLException
     * 
     */

    private static long insertFeatureData(Connection connection, long masterId, String color, String upholstrey, int dataElement, String state, String code) {
        long retVal = -1l;
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = connection.prepareStatement(INSERT_INTERIOR_ROOMS_FEATURES);
            pst.setLong(1, masterId);
            pst.setString(2, color);
            pst.setString(3, upholstrey);
            pst.setInt(4, dataElement);
            pst.setString(5, state);
            pst.setString(6, code);
            rset = pst.executeQuery();
            commit(connection);
            if (rset.next() == true) {
                retVal = 1l;
            } else {
                retVal = -1l;
            }
            close(rset);
            close(pst);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                /* Nothing */}
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception ex) {
            System.out.println("Error when insert in master. Handle error " + ex.getMessage());
        }
        return retVal;
    }

    private static Long convertErroCode(String error) {
        long errorCode = -1l;
        try {
            errorCode = (long) Long.parseLong(error);
        } catch (Exception ex) {
            System.out.println("Excepiton to convert sql error code string to long");
        }
        return errorCode;
    }
}
