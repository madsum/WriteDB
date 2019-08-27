package com.volvo.gcc3.interiorroom.response;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "StdFeaturesCU_res")
public class InteriorResponse {

    @XmlElement(name = "Error")
    private String error;

    @XmlElement(name = "StartWeek")
    private long startWeek;

    @XmlElement(name = "EndWeek")
    private long endWeek;

    @XmlElement(name = "Pno12")
    private String pno12;

    @XmlElementWrapper(name = "OptionList")
    @XmlElement(name = "Option")
    private List<String> commonOptionList = new ArrayList<String>();

    @XmlElementWrapper(name = "FeatureList")
    @XmlElement(name = "Feature")
    private List<String> commonFeatureList = new ArrayList<String>();

    @XmlElementWrapper(name = "CUList")
    @XmlElement(name = "CU")
    private List<InteriorRoom> interiorRoomList = new ArrayList<InteriorRoom>();

    @XmlTransient
    private int dataElement;

    @XmlTransient
    private String programMarket;

    @XmlTransient
    private Date modifiedDate;

    @XmlTransient
    private String modifiedBy;

    @XmlTransient
    private String common = "common";

    public InteriorResponse() {
    }

    public InteriorResponse(int startWeek, int endWeek, String pno12) {
        super();
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.pno12 = pno12;
    }

    @XmlTransient
    public long getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(long startWeek) {
        this.startWeek = startWeek;
    }

    @XmlTransient
    public long getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(long endWeek) {
        this.endWeek = endWeek;
    }

    @XmlTransient
    public String getPno12() {
        return pno12;
    }

    public void setPno12(String pno12) {
        this.pno12 = pno12;
    }

    @XmlTransient
    public List<String> getCommonFeatureList() {
        return commonFeatureList;
    }

    public void setCommonFeatureList(List<String> commonFeatureList) {
        this.commonFeatureList = commonFeatureList;
    }

    public void addCommonFeatureList(String featutre) {
        if (commonFeatureList == null) {
            commonFeatureList = new ArrayList<String>();
        }
        commonFeatureList.add(featutre);
    }

    @XmlTransient
    public List<String> getCommonOptionList() {
        return commonOptionList;
    }

    public void setCommonOptionList(List<String> commonOptionList) {
        this.commonOptionList = commonOptionList;
    }

    public void addCommonOptionList(String option) {
        if (commonOptionList == null) {
            commonOptionList = new ArrayList<String>();
        }
        commonOptionList.add(option);
    }

    @XmlTransient
    public List<InteriorRoom> getInteriorRoomList() {
        return interiorRoomList;
    }

    public void setInteriorRoomList(List<InteriorRoom> interiorRoomList) {
        this.interiorRoomList = interiorRoomList;
    }

    public void addInteriorRoomList(InteriorRoom interiorRoom) {
        if (interiorRoomList == null) {
            interiorRoomList = new ArrayList<InteriorRoom>();
        }
        interiorRoomList.add(interiorRoom);
    }

    @XmlTransient
    public int getDataElement() {
        return dataElement;
    }

    public void setDataElement(int dataElement) {
        this.dataElement = dataElement;
    }

    @XmlTransient
    public String getProgramMarket() {
        return programMarket;
    }

    public void setProgramMarket(String programMarket) {
        this.programMarket = programMarket;
    }

    @XmlTransient
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @XmlTransient
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @XmlTransient
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @XmlTransient
    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commonFeatureList == null) ? 0 : commonFeatureList.hashCode());
        result = prime * result + ((commonOptionList == null) ? 0 : commonOptionList.hashCode());
        result = prime * result + dataElement;
        result = prime * result + (int) (endWeek ^ (endWeek >>> 32));
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + ((interiorRoomList == null) ? 0 : interiorRoomList.hashCode());
        result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
        result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
        result = prime * result + ((pno12 == null) ? 0 : pno12.hashCode());
        result = prime * result + ((programMarket == null) ? 0 : programMarket.hashCode());
        result = prime * result + (int) (startWeek ^ (startWeek >>> 32));
        return result;
    }

    public boolean equals(InteriorResponse otherInteriorResponse) {

        if (this == otherInteriorResponse)
            return true;
        if (otherInteriorResponse == null)
            return false;
        if (getClass() != otherInteriorResponse.getClass())
            return false;

        return true;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InteriorResponse other = (InteriorResponse) obj;
        if (commonFeatureList == null) {
            if (other.commonFeatureList != null)
                return false;
        } else if (!commonFeatureList.equals(other.commonFeatureList))
            return false;
        if (commonOptionList == null) {
            if (other.commonOptionList != null)
                return false;
        } else if (!commonOptionList.equals(other.commonOptionList))
            return false;
        if (dataElement != other.dataElement)
            return false;
        if (endWeek != other.endWeek)
            return false;
        if (error == null) {
            if (other.error != null)
                return false;
        } else if (!error.equals(other.error))
            return false;
        if (interiorRoomList == null) {
            if (other.interiorRoomList != null)
                return false;
        } else if (!interiorRoomList.equals(other.interiorRoomList))
            return false;
        if (modifiedBy == null) {
            if (other.modifiedBy != null)
                return false;
        } else if (!modifiedBy.equals(other.modifiedBy))
            return false;
        if (modifiedDate == null) {
            if (other.modifiedDate != null)
                return false;
        } else if (!modifiedDate.equals(other.modifiedDate))
            return false;
        if (pno12 == null) {
            if (other.pno12 != null)
                return false;
        } else if (!pno12.equals(other.pno12))
            return false;
        if (programMarket == null) {
            if (other.programMarket != null)
                return false;
        } else if (!programMarket.equals(other.programMarket))
            return false;
        if (startWeek != other.startWeek)
            return false;
        return true;
    }

}