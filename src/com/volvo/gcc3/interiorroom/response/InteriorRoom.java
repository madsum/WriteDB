package com.volvo.gcc3.interiorroom.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

public class InteriorRoom {

    @XmlTransient
    private long masterRoomId;

    @XmlElement(name = "Col")
    private String color;
    @XmlElement(name = "Uph")
    private String upholstery;

    @XmlElementWrapper(name = "OptionList")
    @XmlElement(name = "Option")
    private List<String> optionList = new ArrayList<String>();

    @XmlElementWrapper(name = "FeatureListCU")
    @XmlElement(name = "FeatureCU")
    private List<String> featureList = new ArrayList<String>();

    @XmlTransient
    private int dataElement;

    @XmlTransient
    public long getMasterRoomId() {
        return masterRoomId;
    }

    public void setMasterRoomId(long masterRoomId) {
        this.masterRoomId = masterRoomId;
    }

    @XmlTransient
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @XmlTransient
    public String getUpholstery() {
        return upholstery;
    }

    public void setUpholstery(String upholstery) {
        this.upholstery = upholstery;
    }

    @XmlTransient
    public List<String> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<String> featureList) {
        this.featureList = featureList;
    }

    @XmlTransient
    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }
    
    @XmlTransient
    public int getDataElement() {
        return dataElement;
    }

    public void setDataElement(int dataElement) {
        this.dataElement = dataElement;
    }

    public void addFeatureList(String feature) {
        if (featureList == null) {
            featureList = new ArrayList<String>();
        }
        featureList.add(feature);
    }

    public void addOptionList(String option) {
        if (optionList == null) {
            optionList = new ArrayList<String>();
        }
        optionList.add(option);
    }

    @Override
    public String toString() {
        return "InteriorRoom [masterRoomId=" + masterRoomId + ", color=" + color + ", upholstery=" + upholstery + ", optionList=" + optionList
            + ", featureList=" + featureList + ", dataElement=" + dataElement + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + dataElement;
        result = prime * result + ((featureList == null) ? 0 : featureList.hashCode());
        result = prime * result + (int) (masterRoomId ^ (masterRoomId >>> 32));
        result = prime * result + ((optionList == null) ? 0 : optionList.hashCode());
        result = prime * result + ((upholstery == null) ? 0 : upholstery.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InteriorRoom other = (InteriorRoom) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (dataElement != other.dataElement)
            return false;
        if (featureList == null) {
            if (other.featureList != null)
                return false;
        } else if (!featureList.equals(other.featureList))
            return false;
        if (masterRoomId != other.masterRoomId)
            return false;
        if (optionList == null) {
            if (other.optionList != null)
                return false;
        } else if (!optionList.equals(other.optionList))
            return false;
        if (upholstery == null) {
            if (other.upholstery != null)
                return false;
        } else if (!upholstery.equals(other.upholstery))
            return false;
        return true;
    }
}
