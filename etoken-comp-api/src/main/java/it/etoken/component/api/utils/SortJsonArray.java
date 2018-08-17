package it.etoken.component.api.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class SortJsonArray {
	public static JSONArray sortJsonArray(JSONArray array, final String key) {

		JSONArray sortedJsonArray = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.size(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			// You can change "Name" with "ID" if you want to sort by ID
			@Override
			public int compare(JSONObject a, JSONObject b) {
				int aStr = 0;
				int bStr = 0;
				try {
					// 这里是a、b需要处理的业务，需要根据你的规则进行修改。
					aStr = a.getIntValue(key);
					bStr = b.getIntValue(key);
				} catch (JSONException e) {
					// do something
				}
				if (aStr < bStr) {
					return 1;
				} else if (aStr == bStr) {
					return 0;
				}
				return -1;
				// if you want to change the sort order, simply use the following:
				// return -valA.compareTo(valB);
			}
		});
		for (int i = 0; i < array.size(); i++) {
			sortedJsonArray.add(jsonValues.get(i));
		}
		return sortedJsonArray;

	}

	public static JSONArray sortJsonArrayString(JSONArray array, final String key) {

		JSONArray sortedJsonArray = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.size(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			// You can change "Name" with "ID" if you want to sort by ID
			@Override
			public int compare(JSONObject a, JSONObject b) {
				String aStr = "";
				String bStr = "";
				try {
					// 这里是a、b需要处理的业务，需要根据你的规则进行修改。
					aStr = a.getString(key);
					bStr = b.getString(key);
				} catch (JSONException e) {
					// do something
				}
				return -aStr.compareTo(bStr);
				// if you want to change the sort order, simply use the following:
				// return -valA.compareTo(valB);
			}
		});
		for (int i = 0; i < array.size(); i++) {
			sortedJsonArray.add(jsonValues.get(i));
		}
		return sortedJsonArray;

	}
}
