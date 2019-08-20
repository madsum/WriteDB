package com.volvo.gcc3.interiorroom.batch;

import java.util.ArrayList;
import java.util.List;

/*
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.volvo.gcc3.dl.servercache.BasicFeatures;
import com.volvo.gcc3.dl.servercache.BrandInterface;
import com.volvo.gcc3.dl.servercache.ItemInterface;
import com.volvo.gcc3.dl.servercache.MarketInterface;
import com.volvo.gcc3.dl.servercache.ModelInterface;
import com.volvo.gcc3.dl.servercache.SalesAuthInterface;
import com.volvo.gcc3.dl.servercache.impl.BasicFeaturesImpl;
import com.volvo.gcc3.dl.servercache.impl.ServerCacheImpl;
*/
public class InteriorRoomBatch {

    private static final String CLASS_NAME = InteriorRoomBatch.class.getName();

    private List<InteriorDetails> interiorDetailsList = new ArrayList<InteriorDetails>();

    public List<InteriorDetails> getInteriorDetailsList() {
        return interiorDetailsList;
    }

    public void setInteriorDetailsList(List<InteriorDetails> interiorDetailsList) {
        this.interiorDetailsList = interiorDetailsList;
    }

    public void fillInteriorDetailsFromCache() {

    }
    }
