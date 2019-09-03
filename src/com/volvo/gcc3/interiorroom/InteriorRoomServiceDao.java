package com.volvo.gcc3.interiorroom;

import java.util.List;
import java.util.Map;

import com.volvo.gcc3.interiorroom.request.response.InteriorResponse;

public interface InteriorRoomServiceDao {

    InteriorResponse getInteriorRooms(String programMarket, String pno12, long strWeekFrom, long strWeekTo, List<String> options);

    void saveInteriorRooms(String programMarket, String pno12, long strWeekFrom, long strWeekTo, List<String> options);

    Map<String, List<String>> getInteriorRoomFeaturs(String programMarket, String pno12, long strWeekFrom, long strWeekTo, String color, String upholstery);

    void batchSaveInteriorRooms();

}
