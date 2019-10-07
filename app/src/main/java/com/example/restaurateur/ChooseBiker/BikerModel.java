package com.example.restaurateur.ChooseBiker;

import com.google.firebase.firestore.GeoPoint;

public class BikerModel implements Comparable<BikerModel>{

    private String bikerID;
    private String bikerImageUrl;
    private String bikerName;
    private Double bikerDist;

    public BikerModel(String bikerID, String bikerImageUrl, String bikerName, GeoPoint biker, GeoPoint rest) {
        this.bikerID = bikerID;
        this.bikerImageUrl = bikerImageUrl;
        this.bikerName = bikerName;
        this.bikerDist = Haversine.getHaversineDistance(biker,rest);
    }

    public String getBikerImageUrl() {
        return bikerImageUrl;
    }

    public void setBikerImageUrl(String bikerImageUrl) {
        this.bikerImageUrl = bikerImageUrl;
    }

    public String getBikerName() {
        return bikerName;
    }

    public void setBikerName(String bikerName) {
        this.bikerName = bikerName;
    }

    public String getBikerDist() {
        if(bikerDist > 1){
            return String.format("%.2f", bikerDist).replaceAll("(\\.\\d+?)0*$", "$1") + " Km";
        } else {
            return String.format("%.0f", bikerDist * 1000).replaceAll("(\\.\\d+?)0*$", "$1") + " m";
        }
    }

    public Double getDist(){
        return bikerDist;
    }

    public void setBikerDist(Double bikerDist) {
        this.bikerDist = bikerDist;
    }

    public String getBikerID() {
        return bikerID;
    }

    public void setBikerID(String bikerID) {
        this.bikerID = bikerID;
    }

    @Override
    public int compareTo(BikerModel o) {
        return this.bikerDist.compareTo(o.getDist());
    }
}
