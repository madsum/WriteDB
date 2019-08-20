package com.volvo.gcc3.interiorroom;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;
import com.volvo.gcc3.interiorroom.response.InteriorRoom;
import com.volvo.gcc3.interiorroom.response.XmlUnmarshaller;

public class DbTest {
	
    private static String file_name = "minxml3.xml";
    private static File file = new File("minxml2.xml");
    private static XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
    // private static InteriorResponse interiorResponse;

	static long uniqeIndexErrorCode = 23000l; 
    private static String programMarket = "212";
    private static String pno12 = "ABCDEEB";
    private static String color = "100";
    private static String upholstery = "RA0X";
    private static String code = "101";
    private static int startWeek = 202017;
    private static int endWeek = 202035;

    private static Connection connection = null;
    private static String url = "jdbc:oracle:thin:@GOTSVL2290.got.volvocars.net:1521:dpgccd";
    private static String user = "gcc_dbs_dev_admin";
    private static String pass = "gcc_dbs_dev_admin";

    private final static String xmlConent = "<Features_res><Error></Error><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>246A8130D119</Pno12><OptionList><Option>000356</Option><Option>900432</Option></OptionList><FeatureList><Feature>T002</Feature><Feature>T20H</Feature></FeatureList><CUList><CU><Col>019</Col><Uph>RA00</Uph><OptionList><Option>1111111</Option><Option>2222222</Option></OptionList><FeatureListCU><FeatureCU>NC0C</FeatureCU><FeatureCU>FH02</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC20</Uph><OptionList><Option>3333333</Option><Option>4444444</Option></OptionList><FeatureListCU><FeatureCU>NC06</FeatureCU><FeatureCU>FH01</FeatureCU></FeatureListCU></CU></CUList></Features_res>";
    private final static String xmlConent2 = "<Features_res><Error></Error><StartWeek>202017</StartWeek><EndWeek>202040</EndWeek><Pno12>246A8130D119</Pno12><OptionList><Option>000356</Option><Option>900432</Option></OptionList><FeatureList><Feature>T002</Feature><Feature>T20H</Feature></FeatureList><CUList><CU><Col>019</Col><Uph>RA00</Uph><OptionList><Option>1111111</Option><Option>2222222</Option></OptionList><FeatureListCU><FeatureCU>NC0C</FeatureCU><FeatureCU>FH02</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC20</Uph><OptionList><Option>3333333</Option><Option>4444444</Option></OptionList><FeatureListCU><FeatureCU>NC06</FeatureCU><FeatureCU>FH01</FeatureCU></FeatureListCU></CU></CUList></Features_res>";
    private final static String xmlConent3 = "<Features_res><Error></Error><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>246A8130D110</Pno12><OptionList><Option>000356</Option><Option>900432</Option></OptionList><FeatureList><Feature>T002</Feature><Feature>T20H</Feature></FeatureList><CUList><CU><Col>019</Col><Uph>RA00</Uph><OptionList><Option>1111111</Option><Option>2222222</Option></OptionList><FeatureListCU><FeatureCU>NC0C</FeatureCU><FeatureCU>FH02</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC20</Uph><OptionList><Option>3333333</Option><Option>4444444</Option></OptionList><FeatureListCU><FeatureCU>NC06</FeatureCU><FeatureCU>FH01</FeatureCU></FeatureListCU></CU></CUList></Features_res>";
    private final static String xmlConent4 = "<Features_res><Error></Error><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>246A8130D119</Pno12><OptionList><Option>000355</Option><Option>900432</Option></OptionList><FeatureList><Feature>T002</Feature><Feature>T20H</Feature></FeatureList><CUList><CU><Col>019</Col><Uph>RA00</Uph><OptionList><Option>1111111</Option><Option>2222222</Option></OptionList><FeatureListCU><FeatureCU>NC0C</FeatureCU><FeatureCU>FH02</FeatureCU></FeatureListCU></CU><CU><Col>019</Col><Uph>RC20</Uph><OptionList><Option>3333333</Option><Option>4444444</Option></OptionList><FeatureListCU><FeatureCU>NC06</FeatureCU><FeatureCU>FH01</FeatureCU></FeatureListCU></CU></CUList></Features_res>";


    public static void main(String[] args) {

        doSimulation(xmlConent);
        doSimulation(xmlConent2);
        doSimulation(xmlConent3);
        doSimulation(xmlConent4);
        doSimulation(xmlConent);
        interiorRoomLog.getInteriorRoomLogAll(connection);
        closeConnection();

        interiorRoomLog.compareInteriorResponseList();
       // xmlUnmarshaller = new XmlUnmarshaller();
       // interiorResponse = xmlUnmarshaller.UnmarshalXml(xmlConent);
       // printData();
        

        
        // interiorResponse = xmlUnmarshaller.getInteriorResponse();
       // printData();
        // InteriorRoomServiceDaoImpl interiorRoomServiceDaoImpl = new InteriorRoomServiceDaoImpl();
        // interiorRoomServiceDaoImpl.initialzeDatabaseConnection();

        // interiorRoomServiceDaoImpl.insertIntoriorRoomData();

        // interiorRoomServiceDaoImpl.getInteriorRooms(programMarket, pno12, startWeek, endWeek, null);

        //

        // LoadInteriorRoomQuery loadInteriorRoomQuery = LoadInteriorRoomQuery();
        // loadInteriorRoomQuery.getInteriorRooms(connection, programMarket, pno12, str_week_from, str_week_to, options)
        
        // insertIntoDB();
        // data = getDataByPno12(pno12);
        // data = getDataByAll(pno12, str_week_from, str_week_to, color, upholstery);
	}

    public static InteriorRoomLog interiorRoomLog = new InteriorRoomLog();

    public static void doSimulation(String cpamData) {
        InteriorResponse interiorResponse = new InteriorResponse();

        AddInteriorRoomQuery addInteriorRoomQuery = new AddInteriorRoomQuery();

        interiorResponse = initialzeDatabaseConnection(cpamData, interiorResponse);

        addInteriorRoomQuery.insertIntoriorRoomData(connection, interiorResponse);

        
        // interiorRoomLog.insertIntoInteriorMaster(connection, interiorResponse.getPno12(), interiorResponse.getStartWeek(), interiorResponse.getEndWeek(),
        // cpamData);
       /*  
        interiorRoomLog.setPno12("246A8130D119");
        interiorRoomLog.setStrWeekFrom(202017);
        interiorRoomLog.setStrWeekTo(202035);
        interiorRoomLog.getInteriorRoomLog(connection, interiorRoomLog);
        */

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
            // printData(interiorResponse);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interiorResponse;
    }

    public static void closeConnection() {
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
                } catch (SQLException e1) { /* Nothing */
                }
            }
        }
        // interiorResponse = null;
        // interiorResponse = new InteriorResponse();

    }
	
    static void printData(InteriorResponse interiorResponse) {
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
            System.out.println("Color: " + interiorRoom.getColor());
            System.out.println("Upholstery: " + interiorRoom.getUpholstery());
            System.out.println("Color " + interiorRoom.getColor() + " features:- ");
            for (String feature : interiorRoom.getFeatureList()) {
                System.out.println("Color " + interiorRoom.getColor() + " Feature code: " + feature);
            }
            for (String option : interiorRoom.getOptionList()) {
                System.out.println("Color " + interiorRoom.getColor() + " Optoine code: " + option);
            }
        }
    }
}
