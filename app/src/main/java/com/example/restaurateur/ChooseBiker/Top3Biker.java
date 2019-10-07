package com.example.restaurateur.ChooseBiker;

import java.util.ArrayList;
import java.util.Collections;

public class Top3Biker {
    private ArrayList<BikerModel> dataB;

    public Top3Biker(){
        this.dataB = new ArrayList<>();
    }

    public void addBiker(BikerModel b){
        if(dataB.size() < 3){
            dataB.add(b);
            Collections.sort(dataB);
        } else {
            dataB.add(b);
            Collections.sort(dataB);
            dataB.remove(dataB.size() - 1 );
        }
    }

    public ArrayList<BikerModel> getDataB() {
        return dataB;
    }

    public void setDataB(ArrayList<BikerModel> dataB) {
        this.dataB = dataB;
    }
}
