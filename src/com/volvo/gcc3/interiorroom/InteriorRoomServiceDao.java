package com.volvo.gcc3.interiorroom;

import java.util.List;

import com.volvo.gcc3.interiorroom.response.InteriorResponse;

public interface InteriorRoomServiceDao {

    String getInteriorResponse(String pno12);

    void closeConnection();

    String getInteriorResponse();

    long insertIntoriorRoomData();

    List<InteriorResponse> getInteriorRooms(String programMarket, String pno12, long str_week_from, long str_week_to, List<String> options);

    void saveInteriorRooms(int programMarket, String pno12, long str_week_from, long str_week_to, List<String> options);


}
