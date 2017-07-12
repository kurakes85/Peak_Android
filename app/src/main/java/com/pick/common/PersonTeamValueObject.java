package com.pick.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pyoinsoo on 2016-08-13.
 */
//Person or Team에 대한 정보
public class PersonTeamValueObject implements Serializable{
    public String _id;
    public int type;  // 0:team  , 1:person
    public ArrayList<String> genres = new ArrayList<>(); //장르:팝,인디등등
    public ArrayList<String> parts = new ArrayList<>(); //파트 : 악기
    public String cont; // ?
    public String videoKey; //유튜브 Video키
    public String uId ; //사용자 ID?
    public String check; // 즐겨찾기 유무(y,n)
    public String name;
    public String gender ; //젠더(m, f)
    public String age;
    public String areaDo; //시, 도
    public String areaGu; //구, 군

    public ArrayList<String> bookmarks; //북마크?

    //밴드일 경우만 사용하는 필드
    public String bandValue; //밴드의 이름
    public String bandTeamGroup; // 밴드 팀 구성
    //밴드정보
    public ArrayList<BandInfo> bandInfos; //각 밴드멤버정보

    public PersonTeamValueObject(){}
}
