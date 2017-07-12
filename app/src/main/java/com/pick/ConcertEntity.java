package com.pick;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-17.
 */
public class ConcertEntity {
    int total;
    String page;
    ArrayList<ConcertDataEntity>  data;

    ConcertEntity(ArrayList<ConcertDataEntity>  data) {
        this.data = data;
    }

}
