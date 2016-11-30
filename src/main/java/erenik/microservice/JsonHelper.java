package erenik.microservice;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.bson.Document;

public class JsonHelper {

	static String defaultFilePath = "resources.json";

	static void AddObjects(JsonObjectBuilder toBuilder, JsonObject jsonObject)
	{
		Set<String> keys = jsonObject.keySet();
    	Collection<JsonValue> values = jsonObject.values();
    //	System.out.println("Adding items: "+jsonObject);
    	for (int i = 0; i < keys.size(); ++i)
    	{
    		String key = keys.toArray()[i].toString();
    		String value = jsonObject.getString(key);
//        	System.out.println("Adding old items: key "+key+" val: "+value);
        	toBuilder.add(key,  value);
    	}
	}
	public static void AddObjects(Document doc, JsonObject jsonObject) {
		// TODO Auto-generated method stub
		Set<String> keys = jsonObject.keySet();
    	Collection<JsonValue> values = jsonObject.values();
    //	System.out.println("Adding items: "+jsonObject);
    	for (int i = 0; i < keys.size(); ++i)
    	{
    		String key = keys.toArray()[i].toString();
    		String value = jsonObject.getString(key);
    		doc.append(key, value);
    	}
	}
	public static JsonObject GetJsonFromFile(String jsonPath) 
	{
		InputStream is;
		try {
			is = new FileInputStream(jsonPath);
			JsonReader rdr = Json.createReader(is);
			JsonObject obj = rdr.readObject();
			is.close();
			return obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Failed");
		return null;
	}
    public static JsonObject GetJsonFromString(String fromString)
    {
    	int charset = 0;
    	InputStream is = new ByteArrayInputStream( fromString.getBytes() );
    	JsonReader rdr = Json.createReader(is);
		JsonObject obj = rdr.readObject();
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
    }
	public static void PrintData(JsonObject jsonData) {
    	Set<String> keys = jsonData.keySet();
    	Collection<JsonValue> values = jsonData.values();
    	for (int i = 0; i < keys.size(); ++i)
    	{
    		String key = keys.toArray()[i].toString();
    		System.out.println("Key: "+key+" value: "+values.toArray()[i].toString());
    	}

	}
}
