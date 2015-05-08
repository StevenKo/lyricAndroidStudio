package com.kosbrother.lyric.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class TopList {
	
	int           id;
    String        name;
    int topListId;

    static String message = "[{\"id\":1,\"name\":\"Hit FM \u6392\u884c\u699c\",\"top_list_id\":0},{\"id\":2,\"name\":\"Hito \u4e2d\u6587\u699c\",\"top_list_id\":1},{\"id\":4,\"name\":\"2011\u5e74\u5ea6\u55ae\u66f2\u699c\",\"top_list_id\":1},{\"id\":5,\"name\":\"2012\u5e74\u5ea6\u55ae\u66f2\u699c\",\"top_list_id\":1},{\"id\":31,\"name\":\"2013\u5e74\u5ea6\u55ae\u66f2\u699c\",\"top_list_id\":1},{\"id\":9,\"name\":\"Pop Radio\u6392\u884c\u699c\",\"top_list_id\":0},{\"id\":10,\"name\":\"\u5168\u7403\u83ef\u8a9e\u6b4c\u66f2\u6392\u884c\u699c\",\"top_list_id\":9},{\"id\":11,\"name\":\"Kiss Radio\u6392\u884c\u699c\",\"top_list_id\":0},{\"id\":12,\"name\":\"\u83ef\u8a9e\u6392\u884c\u699c\",\"top_list_id\":11},{\"id\":13,\"name\":\"\u6771\u6d0b\u6392\u884c\u699c\",\"top_list_id\":11},{\"id\":18,\"name\":\"\u9322\u6ac3\u9ede\u6b4c\u6392\u884c\u699c\",\"top_list_id\":0},{\"id\":19,\"name\":\"\u570b\u8a9e\u65b0\u6b4c\u6392\u884c\u699c\",\"top_list_id\":18},{\"id\":20,\"name\":\"\u53f0\u8a9e\u65b0\u6b4c\u6392\u884c\u699c\",\"top_list_id\":18},{\"id\":21,\"name\":\"\u570b\u8a9e\u9ede\u64ad\u7e3d\u6392\u884c\",\"top_list_id\":18},{\"id\":22,\"name\":\"\u53f0\u8a9e\u9ede\u64ad\u7e3d\u6392\u884c\",\"top_list_id\":18},{\"id\":23,\"name\":\"\u5168\u7403\u6d41\u884c\u97f3\u6a02\u91d1\u699c\",\"top_list_id\":1},{\"id\":24,\"name\":\"\u9999\u6e2f TVB \u6392\u884c\u699c\",\"top_list_id\":0},{\"id\":25,\"name\":\"TVB8 \u91d1\u66f2\u699c(\u666e)\",\"top_list_id\":24},{\"id\":26,\"name\":\"TVB \u52c1\u6b4c\u91d1\u699c(\u7cb5)\",\"top_list_id\":24},{\"id\":27,\"name\":\"Pop Radio\u6392\u884c\u699c\",\"top_list_id\":9},{\"id\":28,\"name\":\"\u597d\u7528\u5916\u90e8\u9023\u7d50\",\"top_list_id\":0},{\"id\":29,\"name\":\"YouTube\u81f3MP3\u8f49\u63db\u5668 \",\"top_list_id\":28},{\"id\":30,\"name\":\"ClipConverter.cc \",\"top_list_id\":28}]";
    public TopList() {
        this(1, "",1);
    }

    public TopList(int i, String name, int topListId) {
        this.id = i;
        this.name = name;
        this.topListId = topListId;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public static ArrayList<TopList> getMainTopLists() {
        ArrayList<TopList> lists = new ArrayList<TopList>();
        JSONArray jArray;
        try {
        	jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {
            	int top_list_id = jArray.getJSONObject(i).getInt("top_list_id");
                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");

				if(top_list_id==0){
					TopList list = new TopList(id, name,top_list_id);
					lists.add(list);
				}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }
    
    public static ArrayList<TopList> getSubTopLists(int mainTopListId) {
        ArrayList<TopList> lists = new ArrayList<TopList>();
        JSONArray jArray;
        try {
        	jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {
            	int top_list_id = jArray.getJSONObject(i).getInt("top_list_id");
                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");

				if(top_list_id== mainTopListId){
					TopList list = new TopList(id, name,top_list_id);
					lists.add(list);
				}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
