package com.pick;

/**
 * Created by 10 on 2016-07-21.
 */
public class NetworkDefineConstant {

    public static final String HOST_URL = "52.78.95.102";
    public static final int PORT_NUMBER= 3000;

    public static final String SERVER_URL_PERSON_LIST_VIEW=
            "http://"+HOST_URL+":"+ PORT_NUMBER + "/persons?page=%d";
    public static final String GET_CONCERT_LIST =
            "http://"+HOST_URL+":"+PORT_NUMBER  + "/shows?page=%d";


    public static final String Get_CONCERT_LIST_DATA=
            "http://"+HOST_URL+":"+PORT_NUMBER  +"/shows?page=%d";

}
