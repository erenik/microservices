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
/**
 * A JsonHelper class to help build and parse Json data into JsonObject or from Strings.
 * @author Emil
 *
 */
public class JsonHelper {
	/**
	 * Adds objects to new Json object. Adds all key-value pairs from jsonObject to the builder object (toBuilder).
	 * @param toBuilder
	 * @param jsonObject
	 */
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
	/**
	 * Adds all key-value pairs from the JsonObject to the Document. Used to prepare filters or sending arguments to certain requests.
	 * @param doc
	 * @param jsonObject
	 */
	public static void AddObjects(Document doc, JsonObject jsonObject) {
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
	/**
	 * Retrieves JSON data in the form of a JsonObject by reading the contents of the file at given path.
	 * @param jsonPath
	 * @return Returns null if the file doesn't exist or if there was any exception while parsing it for Json data.
	 */
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
	/**
	 * Builds a Json object by parsing the given String.
	 * @param fromString
	 * @return A Json object based on the contents of the String.
	 */
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
    /**
     * Prints the key-value pairs of the given JsonObject.
     * @param jsonData
     */
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
