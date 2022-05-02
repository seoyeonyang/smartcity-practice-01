package practice.json;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import practice.util.JsonParsingUtil;

public class _01_JsonParsingTest01 {
	
	public static void main(String[] args) throws IOException {
		
		String filePath = "C:/Users/pine/Desktop/OT/1_smart_city/json/sample01.json";
		
		JSONObject json = JsonParsingUtil.jsonFileToJsonObject(filePath);
		
		JSONObject response = json.getJSONObject("response");
		JSONObject body = response.getJSONObject("body");
		JSONObject items = body.getJSONObject("items");
		JSONArray itemArr = items.getJSONArray("item");
		
		for(int i = 0; i < itemArr.length() ; i++) {
			JSONObject item = itemArr.getJSONObject(i);
			
			
			String buffer = "";
			buffer += "측정시간 : " + item.getString("mesurede") + ", ";
			buffer += "맛 : " + item.getString("item1") + ", ";
			buffer += "냄새 : " + item.getString("item2") + ", ";
			buffer += "색도 : " + item.getString("item3") + ", ";
			buffer += "pH : " + item.getString("item4") + ", ";
			buffer += "탁도 : " + item.getString("item5") + ", ";
			buffer += "잔류염소수 : " + item.getString("item6");
			
			System.out.println(buffer);
		}
		

	}
	
}
