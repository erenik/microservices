package erenik.microservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
/**
 * Root resource (exposed at "resource" path)
 */
@Path("resource")
public class Resource 
{
	static int numRequests = 0;
	static String veryRequested = "abab";
	static String jsonBase = "{\n}";
	static String jsonPath = "resources.json";
	
	void WriteFileContents(String path, String contents)
	{
		try {
			FileOutputStream out = new FileOutputStream(path);
			byte[] bytes = contents.getBytes();
			out.write(bytes, 0, bytes.length);
			out.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	String GetFileContents(String path)
	{
		try {
			FileInputStream in = new FileInputStream(path);
			int maxLength = 10000;
			byte[] bytes = new byte[maxLength];
			int numBytesRead = in.read(bytes, 0, maxLength);
			in.close();
			char[] c = new char[numBytesRead];
			for (int i = 0; i < numBytesRead; ++i)
				c[i] = (char) bytes[i];
			String text = new String(c);
			return text;
		} catch (IOException e)
		{
			// Couldn't find it? Create it.
			System.out.println("Couldn't find contents, creating JSON");
			WriteFileContents(path, jsonBase);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "BAD";
	}
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() 
    {	    	    	
    	// Working.
    	return GetFileContents(jsonPath);
    }

	Random random = new Random(System.nanoTime());
    String RandomResourceStr()
    {
    	String[] resourceStr = {"Coal", "Iron", "Steel", "Gold", "Silver", "Copper", "Tin", "Bronze",
    			"Wood", "Food", "Stone", "Money"};
    	return resourceStr[random.nextInt(resourceStr.length) % resourceStr.length];	
    }
    /// Put! requests. Assume they put something useful.
    @PUT
    public Response putContainer() 
    {
    	System.out.println("PUT RECEIVED");
    	/// Check current saved data.    	
    	JsonObject obj = GetJson(jsonPath);
    	Set<String> keys = obj.keySet();
    	Collection<JsonValue> values = obj.values();
    	for (int i = 0; i < keys.size(); ++i)
    	{
    		String key = keys.toArray()[i].toString();
    		System.out.println("Keys: "+key);
    	}

    	/// Create new JSON
    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	// Add all old items first.
    	System.out.println("Adding old items.");
    	for (int i = 0; i < keys.size(); ++i)
    	{
    		String key = keys.toArray()[i].toString();
    		String value = obj.getString(key);
        	System.out.println("Adding old items: key "+key+" val: "+value);
    		builder.add(key,  value);
    	}
    	// Add the new randomly generated resouce?
    	builder.add(RandomResourceStr(), random.nextInt(99)+"");
    	// finalize JSON
    	JsonObject resultingObject = builder.build();
    	System.out.println("Result: \n"+resultingObject.toString());
    	// Write it to file again.
    	WriteFileContents(jsonPath, resultingObject.toString());
    	
    	/// Send respond to user.
		Response r;
		try {
	    	URI uri = new URI("yeah");
	        r = Response.created(uri).build();
	        return r;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        r = Response.noContent().build();
		}
        return r;
    }
	private JsonObject GetJson(String jsonPath2) 
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
    
}

