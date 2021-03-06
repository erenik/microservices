package erenik.microservice;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Class implementing the /resourcePrices/ url.
 * @author Emil
 */
@Path("resourcePrices")
public class ResourcePrice 
{
//	static String defaultFilePath = "resources.json";
	static String resourcePriceListPath = "resourcePrices.json"; // Used for back-up file save/load in-case DB fails.
    /**
     * Implements GET requests for the base url /resourcePrices/. 
     * Returns all prices in JSON format from the mongoDB database.
     * @return String in JSON format.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPrices() 
    {	    	   
    	try{
    		MongoCursor<Document> iterator = MongoHelper.GetDocumentIterator("resourcePrices");
    		if (iterator.hasNext() == false)
    			return null;
    		Document doc = iterator.next();
    		return doc.toJson();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		// Working.
    	return FileHelper.GetFileContents(resourcePriceListPath);
    }
    /**
     * Implements PUT requests for the base url /resourcePrices/. 
     * Any number of resources can be submitted, and their values will be put/updated in the database.
     * @param jsonInput
     * @return ok or noContent responses. noContent if it failed to connect and update the database values.
     */
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
			// Save it locally to file in-case DB connection isn't working.
/*	     	JsonObject loadedJsonData = JsonHelper.GetJsonFromFile(resourcePriceListPath); // Load saved data from file.
	    	JsonHelper.AddObjects(builder, loadedJsonData); // Add old objects
	    	JsonHelper.AddObjects(builder, parsedInputJsonData); // And the new parsed contents  
	    	JsonObject resultingObject = builder.build();    	// finalize it
	    	FileHelper.WriteFileContents(resourcePriceListPath, resultingObject.toString());    	// Write it to file again.    	
	    	*/
	        return Response.noContent().build();    	/// Send response to the user.
		}
    }
}
