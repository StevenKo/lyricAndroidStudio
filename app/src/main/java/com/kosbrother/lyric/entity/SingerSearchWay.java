package com.kosbrother.lyric.entity;

import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;

public class SingerSearchWay {
    int           id;
    int           singer_category_id;
    String        name;

    static String message = "[{\"id\":1,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":1},{\"id\":2,\"name\":\"\u6ce8\u97f3\",\"singer_category_id\":1},{\"id\":3,\"name\":\"\u7b46\u5283\",\"singer_category_id\":1},{\"id\":4,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":2},{\"id\":5,\"name\":\"\u6ce8\u97f3\",\"singer_category_id\":2},{\"id\":6,\"name\":\"\u7b46\u5283\",\"singer_category_id\":2},{\"id\":7,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":3},{\"id\":8,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":3},{\"id\":9,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":3},{\"id\":10,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":4},{\"id\":11,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":4},{\"id\":12,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":4},{\"id\":13,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":5},{\"id\":14,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":5},{\"id\":15,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":5},{\"id\":16,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":6},{\"id\":17,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":6},{\"id\":18,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":6},{\"id\":19,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":7},{\"id\":20,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":7},{\"id\":21,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":7},{\"id\":22,\"name\":\"\u7f85\u99ac\u62fc\u97f3\",\"singer_category_id\":8},{\"id\":23,\"name\":\"\u5b57\u6578\u53ca\u82f1\u6587\",\"singer_category_id\":8},{\"id\":24,\"name\":\"\u82f1\u6587\u53ca\u7b46\u5283\",\"singer_category_id\":8}]";

    public SingerSearchWay() {
        this(1, 1, "");
    }

    public SingerSearchWay(int id, int singer_category_id, String name) {
        this.id = id;
        this.singer_category_id = singer_category_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getSingerCategoryId() {
        return singer_category_id;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<SingerSearchWay> getSingerCategoryWays(int singerCategoryId) {

        TreeMap map = new TreeMap<Integer, ArrayList<SingerSearchWay>>();

        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(message.toString());

            for (int i = 0; i < categoryArray.length(); i++) {
                int id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                int singer_category_id = categoryArray.getJSONObject(i).getInt("singer_category_id");
                SingerSearchWay cat = new SingerSearchWay(id, singer_category_id, name);
                if (map.containsKey(cat.getSingerCategoryId())) {
                    ((ArrayList<SingerSearchWay>) map.get(cat.getSingerCategoryId())).add(cat);
                } else {
                    ArrayList<SingerSearchWay> ways = new ArrayList<SingerSearchWay>();
                    ways.add(cat);
                    map.put(cat.getSingerCategoryId(), ways);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (ArrayList<SingerSearchWay>) map.get(singerCategoryId);
    }

}
