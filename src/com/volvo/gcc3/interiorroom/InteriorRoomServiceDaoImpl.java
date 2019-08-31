package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.volvo.gcc3.interiorroom.batch.InteriorDetails;
import com.volvo.gcc3.interiorroom.batch.InteriorRoomBatch;
import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.XmlUnmarshaller;

public class InteriorRoomServiceDaoImpl implements InteriorRoomServiceDao {

    private static final String CLASS_NAME = InteriorRoomServiceDaoImpl.class.getName();


    private static XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
    private static InteriorResponse interiorResponse;
    private LoadInteriorRoomQuery loadInteriorRoomQuery = new LoadInteriorRoomQuery();
    private AddInteriorRoomQuery addInteriorRoomQuery = new AddInteriorRoomQuery();

    private String pno12 = "ABCDEEX";
    private String color = "100";
    private String upholstery = "RA0X";
    private String code = "101";
    private int startWeek = 202017;
    private int endWeek = 202035;

    static String xmlContent = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU></CUList></Features_res>";

    static String tmp = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU><CU><Col>30000</Col><Uph>BBB</Uph><FeatureList><Feature><Code>30000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>optional</state></Option></OptionList></CU><CU><Col>40000</Col><Uph>CCC</Uph><FeatureList><Feature><Code>40000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>available</state></Option></OptionList></CU></CUList></Features_res>";

    private List<String> markets = new ArrayList<String>();
    private List<InteriorDetails> interiorDetailsList = new ArrayList<InteriorDetails>();

    private static Connection connection = null;
    private static String url = "jdbc:oracle:thin:@GOTSVL2290.got.volvocars.net:1521:dpgccd";
    private static String user = "gcc_dbs_dev_admin";
    private static String pass = "gcc_dbs_dev_admin";
    private static String programMarket = "213";


    public String getInteriorResponse() {
        // batchTest();
        InteriorRoomBatch interiorRoomBatch = new InteriorRoomBatch();
        interiorRoomBatch.fillInteriorDetailsFromCache();
        System.out.println("InteriorDetails size: " + interiorRoomBatch.getInteriorDetailsList().size());
        for (InteriorDetails interiorDetails : interiorRoomBatch.getInteriorDetailsList()) {
            AddInteriorRoomQuery.batchInsertIntoInteriorMaster(connection, interiorDetails, true);
            System.out.println("PNO12: " + interiorDetails.getPno12());
            System.out.println("Str week: " + interiorDetails.getStrWeekFrom());
            System.out.println("End week: " + interiorDetails.getStrWeekTo());
        }
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
        return LoadInteriorRoomQuery.getInteriorRooms(connection, programMarket, pno12, str_week_from, str_week_to, options);
    }

    public Map<String, List<String>> getInteriorRooms(String programMarket, String pno12, long str_week_from, long str_week_to, String color, String upholstery,
        List<String> options) {
        return null;

    }

    public void saveInteriorRooms(int programMarket, String pno12, long str_week_from, long str_week_to, List<String> options) {
        addInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
    }

    public static void doSimulationInteriroMaster(String cpamData) {
        InteriorResponse interiorResponse = new InteriorResponse();
        interiorResponse = initialzeDatabaseConnection(cpamData, interiorResponse);
        interiorResponse.setProgramMarket(programMarket);
        AddInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);

    }

    public static void inseertFailedInteriorRoomRequest() {
        ListIterator<FailedInteriorRoomRequest> iterator = AddInteriorRoomQuery.getFailedInteriorRoomRequestList().listIterator();
        System.out.println("FailedInteriorRoomRequestList before loop size: " + AddInteriorRoomQuery.getFailedInteriorRoomRequestList().size());
        while (iterator.hasNext()) {
            AddInteriorRoomQuery.insertIntoriorMasuterData(getConnection(), iterator.next().getInteriorResponse(), false);
            iterator.remove();
            System.out.println("FailedInteriorRoomRequestLis after remove size: " + AddInteriorRoomQuery.getFailedInteriorRoomRequestList().size());
        }
    }

    public static void doSimulationXmlLog(String cpamData) {
        InteriorRoomResponseLog interiorRoomLog = makeInteriorRoomLog(cpamData);
        // if log not exist check is pon12 exist. If pno12 exist, delete it and then insert new data.
        if (!InteriorRoomResponseLog.isInteriorRoomLogExist(connection, interiorRoomLog)) {
            if (InteriorRoomResponseLog.isPno12ExistInInteriorRoomLog(connection, interiorRoomLog)) {
                if (InteriorRoomResponseLog.deleteInteriorRoomLog(connection, interiorRoomLog)) {
                    InteriorRoomResponseLog.insertIntoInteriorLog(connection, interiorRoomLog);
                    AddInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
                    System.out.println("Log, master and featuer table inserted successful!");
                    inseertFailedInteriorRoomRequest();
                } else {
                    System.out.println("Delete log failed");
                }
            } else {
                InteriorRoomResponseLog.insertIntoInteriorLog(connection, interiorRoomLog);
                inseertFailedInteriorRoomRequest();
                AddInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
                inseertFailedInteriorRoomRequest();
            }
        } else {
            System.out.println("Cpam data altready in the log. No need to insert same entry");
        }
        InteriorRoomResponseLog.closeAllResource();
    }

    public static InteriorResponse initialzeDatabaseConnection(String xmlParam, InteriorResponse interiorResponse) {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
            interiorResponse = xmlUnmarshaller.getInteriorResponse(xmlParam, interiorResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interiorResponse;
    }

    public static InteriorRoomResponseLog makeInteriorRoomLog(String cpamData) {

        interiorResponse = initialzeDatabaseConnection(cpamData, interiorResponse);
        interiorResponse.setProgramMarket(programMarket);
        InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog(interiorResponse.getPno12(), interiorResponse.getStartWeek(),
            interiorResponse.getEndWeek(), cpamData);
        return interiorRoomLog;
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public String batchTest() {
        for (InteriorDetails interiorDetails : interiorDetailsList) {
            System.out.println("Pno12: " + interiorDetails.getPno12() + " StrWeekFrom: " + interiorDetails.getStrWeekFrom() + "StrWeekTo: "
                + interiorDetails.getStrWeekTo());
            boolean isExist = LoadInteriorRoomQuery.isPno12Exist(null, "212", interiorDetails.getPno12(), interiorDetails.getStrWeekFrom(),
                interiorDetails.getStrWeekTo());
            if (!isExist) {
                // actrual call will be to cpam. But now just inset in the admin DB.
                // AddInteriorRoomQuery.insertIntoInteriorMaster(null, "212", interiorDetails.getStrWeekFrom(), interiorDetails.getStrWeekTo(),
                // interiorDetails.getPno12(), "color", "upholstrey");
            }
        }


        return "ok";
    }

}
