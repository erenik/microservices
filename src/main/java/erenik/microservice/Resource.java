package erenik.microservice;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
 
/**
 * Root resource (exposed at "resource" path)
 */
@Path("resource")
public class Resource 
{
	static int numRequests = 0;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() 
    {	    	   
    	try{
    		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
    		MongoDatabase mdb = mongoClient.getDatabase("labapi");
    		MongoCollection<Document> resources = mdb.getCollection("resources");
    		FindIterable<Document> docs = resources.find();
    		MongoCursor<Document> iterator = docs.iterator();
    		Document doc = iterator.next();
    		return doc.toJson();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		// Working.
    	return FileHelper.GetFileContents(JsonHelper.defaultFilePath);
    }

	static Random random = new Random(System.nanoTime());
    static String RandomResourceStr()
    {
    	String[] resourceStr = {"Coal", "Iron", "Steel", "Gold", "Silver", "Copper", "Tin", "Bronze",
    			"Wood", "Food", "Stone", "Money"};
    	return resourceStr[random.nextInt(resourceStr.length) % resourceStr.length];	
    }
    /// Put! requests. Assume they put something useful.
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
   // @Path("/result")
    public Response putIt(String jsonInput) 
    {    	
    	
    	JsonObject parsedInputJsonData = JsonHelper.GetJsonFromString(jsonInput);    	// Parse it.  	

    	// Try and put into MongoDB
		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
		MongoDatabase mdb = mongoClient.getDatabase("labapi");
		MongoCollection<Document> resources = mdb.getCollection("resourcePrices");
		FindIterable<Document> docs = resources.find();
		MongoCursor<Document> iterator = docs.iterator();
		Document doc = iterator.next();
		Document doc2 = new Document();
		JsonObject fromDB = JsonHelper.GetJsonFromString(doc.toJson());
		JsonObjectBuilder builder = Json.createObjectBuilder();

		JsonHelper.AddObjects(doc, parsedInputJsonData); // Add more key-value pairs into the open document.
		// try insert the new updated document.
		try {
			DBObject filterDB = new BasicDBObject();
			filterDB.put( "name", "ResourcePriceList" );
			resources.replaceOne((Bson) filterDB, doc);
			return Response.ok().build();
		} catch (Exception e)
		{
			System.out.println("Error "+e.toString());
	        return Response.ok().build();    	/// Send response to the user.
		}
/*
    	
    	// Use JsonHelper.PrintData to debug.
    	JsonObject parsedInputJsonData = JsonHelper.GetJsonFromString(jsonInput);    	// Parse it.  	
     	JsonObject loadedJsonData = JsonHelper.GetJsonFromFile(JsonHelper.defaultFilePath); // Load saved data from file.
    	JsonObjectBuilder builder = Json.createObjectBuilder();    	/// Create new JSON
    	JsonHelper.AddObjects(builder, loadedJsonData); // Add old objects
    	JsonHelper.AddObjects(builder, parsedInputJsonData); // And the new parsed contents  
    	JsonObject resultingObject = builder.build();    	// finalize it
    	FileHelper.WriteFileContents(JsonHelper.defaultFilePath, resultingObject.toString());    	// Write it to file again.    	
        return Response.ok().build();    	/// Send response to the user.
    }
  */
    }
	/// Returns e.g. {"Iron":"234"}
	public static String RandomResourceAndAmountJSON()
	{
		Random r = new Random();
		int quantity = r.nextInt() % 2000;
		if (quantity < 0)
			quantity *= -1;
		return "{\""+RandomResourceStr()+"\":\""+quantity+"\"}";
	}
	// Returns e.g. "Coal":"141"
	public static String RandomResourceAndAmountPair()
	{
		Random r = new Random();
		int quantity = r.nextInt() % 2000;
		if (quantity < 0)
			quantity *= -1;
		return "\""+RandomResourceStr()+"\":\""+quantity+"\"";		
	}
    
}

