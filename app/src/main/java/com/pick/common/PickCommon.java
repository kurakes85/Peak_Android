package com.pick.common;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by pyoinsoo on 2016-08-13.
 * Pick Project에서 공통으로 사용할 정적변수 선언
 */
public class PickCommon {
    public static final String TARGET_URL = "http://52.78.95.102:3000";

    //매칭 개인 리스트 보기
    public static String personsPath = "/persons?page=%d";

    //매칭 팀 리스트 보기(GET)
    public static String teamPath = "/teams?page=%d";

    //개인및팀입력(POST)
    public static String pORbJoin = "/users/join";

    //로그인은 회원가입 후 Splash에서 진행
    public static String login = "/users/login";

    //각 회원정보를 가져오는 URL(post: userId=?)
    public static String userInfo = "/userInfo";

    //개인 구직 동영상 및 정보를 저장하는 곳
    public static String personVideoInfo = "/persons/write";



    private static OkHttpClient okHttpClient; //Singleton객체

    private PickCommon(){
        okHttpClient = new OkHttpClient().newBuilder()
                       .connectTimeout(15, TimeUnit.SECONDS)
                       .readTimeout(15, TimeUnit.SECONDS)
                       .build();
    }
    public static OkHttpClient getOkHttpClient(){
        if(okHttpClient != null){
            return okHttpClient;
        }else{
            new PickCommon();
        }
        return okHttpClient;
    }
}