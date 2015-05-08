package com.kosbrother.lyric.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class SingerCategory {

    int           id;
    String        name;

    static String message = "[{\"id\":1,\"name\":\"\u7537\u751f\"},{\"id\":2,\"name\":\"\u5973\u751f\"},{\"id\":3,\"name\":\"\u5718\u9ad4\"},{\"id\":4,\"name\":\"\u65e5\u97d3\"},{\"id\":5,\"name\":\"\u897f\u6d0b\"},{\"id\":6,\"name\":\"\u5176\u4ed6\"},{\"id\":7,\"name\":\"\u5408\u8f2f\"},{\"id\":8,\"name\":\"\u539f\u8072\u5e36\"}]";

    public SingerCategory() {
        this(1, "");
    }

    public SingerCategory(int i, String name) {
        this.id = i;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<SingerCategory> getCategories() {
        ArrayList<SingerCategory> cateogries = new ArrayList<SingerCategory>();
        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(message.toString());
            for (int i = 0; i < categoryArray.length(); i++) {
                int category_id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                SingerCategory cat = new SingerCategory(category_id, name);
                cateogries.add(cat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cateogries;
    }

}
