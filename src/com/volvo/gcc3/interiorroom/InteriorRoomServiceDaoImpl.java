package com.volvo.gcc3.interiorroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.volvo.gcc3.interiorroom.batch.InteriorDetails;
import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.XmlUnmarshaller;

public class InteriorRoomServiceDaoImpl implements InteriorRoomServiceDao {

    private static final String CLASS_NAME = InteriorRoomServiceDaoImpl.class.getName();

    private static XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
    // private static InteriorResponse interiorResponse;

    private String pno12 = "ABCDEEX";
    private String color = "100";
    private String upholstery = "RA0X";
    private String code = "101";
    private int startWeek = 202017;
    private int endWeek = 202035;

    static String xmlContent = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU></CUList></Features_res>";

    static String tmp = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>5050</Code></Feature></FeatureList><OptionList><Option>9090</Option></OptionList><CUList><CU><Col>1000</Col><Uph>XXXX</Uph><FeatureList><Feature><Code>1000</Code></Feature></FeatureList><OptionList><Option><Code>104888</Code><state>available</state></Option></OptionList></CU><CU><Col>20000</Col><Uph>AAA</Uph><FeatureList><Feature><Code>20000</Code></Feature></FeatureList><OptionList><Option><Code>2222</Code><state>optional</state></Option></OptionList></CU><CU><Col>30000</Col><Uph>BBB</Uph><FeatureList><Feature><Code>30000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>optional</state></Option></OptionList></CU><CU><Col>40000</Col><Uph>CCC</Uph><FeatureList><Feature><Code>40000</Code></Feature></FeatureList><OptionList><Option><Code>4444</Code><state>available</state></Option></OptionList></CU></CUList></Features_res>";

    private List<InteriorDetails> interiorDetailsList = new ArrayList<InteriorDetails>();

    private static Connection connection = null;
    private static String url = "jdbc:oracle:thin:@GOTSVL2290.got.volvocars.net:1521:dpgccd";
    private static String user = "gcc_dbs_dev_admin";
    private static String pass = "gcc_dbs_dev_admin";
    private static String programMarket = "213";

    private final static String cpamData = "<StdFeaturesCU_res><Error></Error><StartWeek>202020</StartWeek><EndWeek>202035</EndWeek><Pno12>246A8130D119</Pno12><OptionList><Option>000356</Option><Option>900432</Option></OptionList><FeatureList><Feature>CG02</Feature><Feature>CQ02</Feature><Feature>CS0C</Feature><Feature>DB01</Feature><Feature>DD01</Feature><Feature>DI02</Feature><Feature>ED01</Feature><Feature>EE02</Feature><Feature>EJ02</Feature><Feature>EO02</Feature><Feature>EQ02</Feature><Feature>FR02</Feature><Feature>FT02</Feature><Feature>GR03</Feature><Feature>G102</Feature><Feature>G303</Feature><Feature>G402</Feature><Feature>G601</Feature><Feature>JB0A</Feature><Feature>JT02</Feature><Feature>KB02</Feature><Feature>KD12</Feature><Feature>KG02</Feature><Feature>KJ03</Feature><Feature>KK03</Feature><Feature>KR02</Feature><Feature>KZ06</Feature><Feature>KU02</Feature><Feature>K202</Feature><Feature>K502</Feature><Feature>K702</Feature><Feature>LA02</Feature><Feature>LB03</Feature><Feature>LF01</Feature><Feature>LM01</Feature><Feature>LV03</Feature><Feature>L103</Feature><Feature>L201</Feature><Feature>L302</Feature><Feature>L702</Feature><Feature>L902</Feature><Feature>MC02</Feature><Feature>MJ02</Feature><Feature>MM02</Feature><Feature>MO0E</Feature><Feature>MS0C</Feature><Feature>MV3C</Feature><Feature>NB03</Feature><Feature>ND01</Feature><Feature>NE05</Feature><Feature>NF02</Feature><Feature>NI02</Feature><Feature>NO02</Feature><Feature>NP02</Feature><Feature>NR03</Feature><Feature>NV02</Feature><Feature>NY02</Feature><Feature>N202</Feature><Feature>N502</Feature><Feature>PB02</Feature><Feature>PD02</Feature><Feature>PE02</Feature><Feature>PI03</Feature><Feature>PK03</Feature><Feature>PN03</Feature><Feature>PU02</Feature><Feature>RA03</Feature><Feature>RD02</Feature><Feature>RE05</Feature><Feature>RG08</Feature><Feature>RK01</Feature><Feature>RL02</Feature><Feature>RN02</Feature><Feature>RO02</Feature><Feature>RS02</Feature><Feature>RU02</Feature><Feature>RZ02</Feature><Feature>R11D</Feature><Feature>R41A</Feature><Feature>R507</Feature><Feature>R603</Feature><Feature>SR02</Feature><Feature>SV02</Feature><Feature>TB05</Feature><Feature>TC05</Feature><Feature>TD01</Feature><Feature>TF02</Feature><Feature>TJ01</Feature><Feature>TL04</Feature><Feature>TM02</Feature><Feature>TP02</Feature><Feature>TQ02</Feature><Feature>TR03</Feature><Feature>TS02</Feature><Feature>T101</Feature><Feature>T206</Feature><Feature>T601</Feature><Feature>VA02</Feature><Feature>VB02</Feature><Feature>VP02</Feature><Feature>V102</Feature><Feature>2601</Feature><Feature>3X01</Feature><Feature>6A04</Feature><Feature>6F01</Feature><Feature>8G04</Feature><Feature>0007</Feature><Feature>0008</Feature><Feature>0028</Feature><Feature>0049</Feature><Feature>0068</Feature><Feature>0072</Feature><Feature>0074</Feature><Feature>0083</Feature><Feature>0084</Feature><Feature>0085</Feature><Feature>0087</Feature><Feature>0088</Feature></FeatureList><CUList><CU><Col>019</Col><Uph>RA00</Uph><OptionList></OptionList><FeatureListCU><FeatureCU>FH02</FeatureCU><FeatureCU>NC0C</FeatureCU><FeatureCU>NJ01</FeatureCU><FeatureCU>NG02</FeatureCU><FeatureCU>8H01</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RA30</Uph><OptionList></OptionList><FeatureListCU><FeatureCU>FH01</FeatureCU><FeatureCU>NC06</FeatureCU><FeatureCU>NJ01</FeatureCU><FeatureCU>NG02</FeatureCU><FeatureCU>8H01</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC00</Uph><OptionList><Option>000010</Option><Option>000011</Option><Option>000385</Option></OptionList><FeatureListCU><FeatureCU>FH02</FeatureCU><FeatureCU>K802</FeatureCU><FeatureCU>NC0C</FeatureCU><FeatureCU>NJ02</FeatureCU><FeatureCU>NK02</FeatureCU><FeatureCU>NM02</FeatureCU><FeatureCU>NS02</FeatureCU><FeatureCU>NG04</FeatureCU><FeatureCU>8H01</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC20</Uph><OptionList><Option>000010</Option><Option>000011</Option><Option>000385</Option></OptionList><FeatureListCU><FeatureCU>FH01</FeatureCU><FeatureCU>K802</FeatureCU><FeatureCU>NC06</FeatureCU><FeatureCU>NJ02</FeatureCU><FeatureCU>NK02</FeatureCU><FeatureCU>NM02</FeatureCU><FeatureCU>NS02</FeatureCU><FeatureCU>NG04</FeatureCU><FeatureCU>8H01</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC30</Uph><OptionList><Option>000010</Option><Option>000011</Option><Option>000385</Option></OptionList><FeatureListCU><FeatureCU>FH01</FeatureCU><FeatureCU>K802</FeatureCU><FeatureCU>NC06</FeatureCU><FeatureCU>NJ02</FeatureCU><FeatureCU>NK02</FeatureCU><FeatureCU>NM02</FeatureCU><FeatureCU>NS02</FeatureCU><FeatureCU>NG04</FeatureCU><FeatureCU>8H01</FeatureCU></FeatureListCU></CU></CUList></StdFeaturesCU_res>";

    public static Connection getConnection() {
        if (connection == null) {
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
        }
        return connection;
    }

    public InteriorResponse getInteriorRooms(String programMarket, String pno12, long strWeekForm, long strWeekTo, List<String> options) {
        InteriorResponse interiorResponse = LoadInteriorRoomQuery.getInteriorRooms(getConnection(), programMarket, pno12, strWeekForm, strWeekTo, options);
        LoadInteriorRoomQuery.closeAllResource();
        closeConnection();
        return interiorResponse;
    }

    public Map<String, List<String>> getInteriorRoomFeaturs(String programMarket, String pno12, long strWeekFrom, long strWeekTo, String color,
        String upholstery) {
        Map<String, List<String>> responseMap = LoadInteriorRoomQuery.getInteriorRoomFeaturs(getConnection(), programMarket, pno12, strWeekFrom, strWeekTo,
            color, upholstery);
        for (String key : responseMap.keySet()) {
            String str = String.format("Map's key %s List size: %d", key, responseMap.get(key).size());
            System.out.println(str);
            System.out.println();
            for (String val : responseMap.get(key)) {
                System.out.println("Key: " + key + " val: " + val);
            }
        }
        LoadInteriorRoomQuery.closeAllResource();
        closeConnection();
        return responseMap;
    }

    public void saveInteriorRooms(String programMarket, String pno12, long strWeekFrom, long strWeekTo, List<String> options) {
        InteriorResponse interiorResponse = initialzeInteriorResponse(cpamData);
        interiorResponse.setProgramMarket(programMarket);
        AddInteriorRoomQuery.insertIntoriorMasuterData(getConnection(), interiorResponse, true);
        inseertFailedInteriorRoomRequest();
        AddInteriorRoomQuery.closeAllResource();
        closeConnection();
    }

    public void batchSaveInteriorRooms() {
/*
        InteriorRoomResponseLog interiorRoomLog = makeInteriorRoomLog(cpamData);
        InteriorRoomBatch interiorRoomBatch = new InteriorRoomBatch();
        interiorRoomBatch.fillInteriorDetailsFromCache();
        System.out.println("InteriorDetails size: " + interiorRoomBatch.getInteriorDetailsList().size());

        for (InteriorDetails interiorDetails : interiorRoomBatch.getInteriorDetailsList()) {
            InteriorResponse interiorResponse = new InteriorResponse(interiorDetails.getProgramMarket(), interiorDetails.getStrWeekFrom(),
                interiorDetails.getStrWeekTo(), interiorDetails.getPno12());
            AddInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
            System.out.println("PNO12: " + interiorDetails.getPno12());
            System.out.println("Str week: " + interiorDetails.getStrWeekFrom());
            System.out.println("End week: " + interiorDetails.getStrWeekTo());
        }
*/
        InteriorResponse interiorResponse = initialzeInteriorResponse(cpamData);
        InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog(interiorResponse.getPno12(), interiorResponse.getStartWeek(),
            interiorResponse.getEndWeek(), cpamData);

        // if log not exist check is pon12 exist. If pno12 exist, delete it and then insert new data.
        if (!InteriorRoomResponseLog.isInteriorRoomLogExist(getConnection(), interiorRoomLog)) {
            if (InteriorRoomResponseLog.isPno12ExistInInteriorRoomLog(getConnection(), interiorRoomLog)) {
                if (InteriorRoomResponseLog.deleteInteriorRoomLog(getConnection(), interiorRoomLog)) {
                    InteriorRoomResponseLog.insertIntoInteriorLog(getConnection(), interiorRoomLog);
                    AddInteriorRoomQuery.insertIntoriorMasuterData(getConnection(), interiorResponse, true);
                    inseertFailedInteriorRoomRequest();
                    System.out.println("Log, master and featuer table inserted successful!");
                } else {
                    System.out.println("Delete log failed");
                }
            } else {
                InteriorRoomResponseLog.insertIntoInteriorLog(getConnection(), interiorRoomLog);
                AddInteriorRoomQuery.insertIntoriorMasuterData(getConnection(), interiorResponse, true);
                inseertFailedInteriorRoomRequest();
            }
        } else {
            System.out.println("Cpam data altready in the log. No need to insert same entry");
        }
        InteriorRoomResponseLog.closeAllResource();
        AddInteriorRoomQuery.closeAllResource();
        closeConnection();
    }

    public static void inseertFailedInteriorRoomRequest() {
        ListIterator<FailedInteriorRoomRequest> iterator = AddInteriorRoomQuery.getFailedInteriorRoomRequestList().listIterator();
        while (iterator.hasNext()) {
            AddInteriorRoomQuery.insertIntoriorMasuterData(getConnection(), iterator.next().getInteriorResponse(), false);
            iterator.remove();
        }
    }

    public static InteriorResponse initialzeInteriorResponse(String xmlParam) {
        InteriorResponse interiorResponse = null;
        interiorResponse = XmlUnmarshaller.getInteriorResponse(xmlParam);
        return interiorResponse;
    }

    public void closeConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (Exception e) {
            // Nothing
        } finally {
            connection = null;
        }
    }

    public static InteriorRoomResponseLog insertInteriorRoomLog(String cpamData) {
        InteriorResponse interiorResponse = initialzeInteriorResponse(cpamData);
        interiorResponse.setProgramMarket(programMarket);
        InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog(interiorResponse.getPno12(), interiorResponse.getStartWeek(),
            interiorResponse.getEndWeek(), cpamData);
        return interiorRoomLog;
    }

    public static void doSimulationInteriroMaster(String cpamData) {
        InteriorResponse interiorResponse = new InteriorResponse();
        interiorResponse = initialzeInteriorResponse(cpamData);
        interiorResponse.setProgramMarket(programMarket);
        AddInteriorRoomQuery.insertIntoriorMasuterData(connection, interiorResponse, true);
    }

    public static void doSimulationXmlLog(String cpamData) {
        InteriorResponse interiorResponse = initialzeInteriorResponse(cpamData);
        InteriorRoomResponseLog interiorRoomLog = insertInteriorRoomLog(cpamData);
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

    /*
     * public static InteriorRoomResponseLog makeInteriorRoomLog(String cpamData) {
     * InteriorResponse interiorResponse = initialzeInteriorResponse(cpamData);
     * interiorResponse.setProgramMarket(programMarket);
     * InteriorRoomResponseLog interiorRoomLog = new InteriorRoomResponseLog(interiorResponse.getPno12(), interiorResponse.getStartWeek(),
     * interiorResponse.getEndWeek(), cpamData);
     * return interiorRoomLog;
     * }
     */

}
