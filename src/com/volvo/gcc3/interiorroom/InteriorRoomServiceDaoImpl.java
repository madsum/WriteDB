package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.volvo.gcc3.interiorroom.batch.InteriorDetails;
import com.volvo.gcc3.interiorroom.batch.InteriorRoomBatch;
import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.XmlUnmarshaller;


public class InteriorRoomServiceDaoImpl implements InteriorRoomServiceDao {

    private static final String CLASS_NAME = InteriorRoomServiceDaoImpl.class.getName();

    private static Connection connection = null;

    private XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
    private InteriorResponse interiorResponse;
    private LoadInteriorRoomQuery loadInteriorRoomQuery = new LoadInteriorRoomQuery();
    private AddInteriorRoomQuery addInteriorRoomQuery = new AddInteriorRoomQuery();

    private String programMarket = "123";
    private String pno12 = "ABCDEEX";
    private String color = "100";
    private String upholstery = "RA0X";
    private String code = "101";
    private int startWeek = 202017;
    private int endWeek = 202035;
    private static String common = "common";

    private static List<FailedInteriorRoomRequest> FailedInteriorRoomRequests = new ArrayList<FailedInteriorRoomRequest>();

    static String xmlContent = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU></CUList></Features_res>";

    static String tmp = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU><CU><Col>30000</Col><Uph>BBB</Uph><FeatureList><Feature><Code>30000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>optional</state></Option></OptionList></CU><CU><Col>40000</Col><Uph>CCC</Uph><FeatureList><Feature><Code>40000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>available</state></Option></OptionList></CU></CUList></Features_res>";

    private List<String> markets = new ArrayList<String>();
    private List<InteriorDetails> interiorDetailsList = new ArrayList<InteriorDetails>();


    public String getInteriorResponse() {
        // batchTest();
        InteriorRoomBatch interiorRoomBatch = new InteriorRoomBatch();
        interiorRoomBatch.fillInteriorDetailsFromCache();
        System.out.println("InteriorDetails size: " + interiorRoomBatch.getInteriorDetailsList().size());
        for (InteriorDetails interiorDetails : interiorRoomBatch.getInteriorDetailsList()) {
            addInteriorRoomQuery.batchInsertIntoInteriorMaster(connection, interiorDetails);
            System.out.println("PNO12: " + interiorDetails.getPno12());
            System.out.println("Str week: " + interiorDetails.getStrWeekFrom());
            System.out.println("End week: " + interiorDetails.getStrWeekTo());
        }

        /*
         * if (interiorResponse == null) {
         * try {
         * System.out.println("Reqeusted pno12: " + pno12);
         * initialzeDatabaseConnection();
         * } catch (DatabaseConnectionException ex) {
         * DatabaseConnectionException gccEx = new DatabaseConnectionException("Could not connect gcc database", ex);
         * logger.throwing(CLASS_NAME, "Could not connect gcc database", gccEx);
         * throw gccEx;
         * }
         * }
         * return interiorResponse.toString();
         */
        return null;
    }

    public long insertIntoriorRoomData(InteriorResponse interiorResponse) {

        long masterRetVal = -1l;

        masterRetVal = addInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
        return masterRetVal;
    }

    public void closeConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                /* Nothing */}
        } finally {
            if (null != connection) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e1) { // Nothing
                }
            }
        }
    }

    public List<InteriorResponse> getInteriorRooms(String programMarket, String pno12, long str_week_from, long str_week_to, List<String> options) {
        return loadInteriorRoomQuery.getInteriorRooms(connection, programMarket, pno12, str_week_from, str_week_to, options);
    }

    public void saveInteriorRooms(int programMarket, String pno12, long str_week_from, long str_week_to, List<String> options) {
        addInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
    }

    public static String getCommon() {
        return common;
    }

    public static void setCommon(String common) {
        InteriorRoomServiceDaoImpl.common = common;
    }

    public static List<FailedInteriorRoomRequest> getFailedInteriorRoomRequests() {
        return FailedInteriorRoomRequests;
    }

    public static void setFailedInteriorRoomRequests(List<FailedInteriorRoomRequest> failedInteriorRoomRequests) {
        FailedInteriorRoomRequests = failedInteriorRoomRequests;
    }

    public static void addFailedInteriorRoomRequests(FailedInteriorRoomRequest failedInteriorRoomRequest) {
        if (FailedInteriorRoomRequests == null) {
            FailedInteriorRoomRequests = new ArrayList<FailedInteriorRoomRequest>();
        }
        FailedInteriorRoomRequests.add(failedInteriorRoomRequest);
    }


    public String batchTest() {
        /*
         * try {
         * connection = MainConnection.open();
         * if (connection != null) {
         * System.out.println("Connected to the database!");
         * } else {
         * System.out.println("Failed to make connection!");
         * }
         * 
         * } catch (GCCResourceException exp) {
         * DatabaseConnectionException gccEx = new DatabaseConnectionException("Could not connect gcc database", exp);
         * logger.throwing(CLASS_NAME, "Could not connect gcc database", gccEx);
         * throw gccEx;
         * }
         */
        for (InteriorDetails interiorDetails : interiorDetailsList) {
            System.out.println("Pno12: " + interiorDetails.getPno12() + " StrWeekFrom: " + interiorDetails.getStrWeekFrom() + "StrWeekTo: "
                + interiorDetails.getStrWeekTo());
            boolean isExist = LoadInteriorRoomQuery.isPno12Exist(null, "212", interiorDetails.getPno12(), interiorDetails.getStrWeekFrom(),
                interiorDetails.getStrWeekTo());
            if (!isExist) {
                // actrual call will be to cpam. But now just inset in the admin DB.
                AddInteriorRoomQuery.insertIntoInteriorMaster(null, "212", interiorDetails.getStrWeekFrom(), interiorDetails.getStrWeekTo(),
                    interiorDetails.getPno12(), "color", "upholstrey");
            }
        }


        return "ok";
    }

    @Override
    public String getInteriorResponse(String pno12) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long insertIntoriorRoomData() {
        // TODO Auto-generated method stub
        return 0;
    }
}
