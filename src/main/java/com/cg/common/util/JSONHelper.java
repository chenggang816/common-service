package com.cg.common.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONHelper {

	public static String toJSONString(JSONObject jsonObj){
		if(jsonObj == null) return null;
		StringWriter out = new StringWriter();
		try {
			jsonObj.writeJSONString(out);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    return out.toString();
	}
	
	public static JSONObject parse(String jsonString){
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject)parser.parse(jsonString);
			return obj;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveJSONFile(JSONObject json,File file){
		if(json == null || file == null || !file.exists()) throw new RuntimeException("±£´æJSONÎÄ¼þÊ§°Ü");
		String str = toJSONString(json);
		FileHelper.WriteToFile(file, str);
	}
}
