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

@Path("resourcePrices")
public class ResourcePrice {
	static String resourcePriceListPath = "resourcePrices.json"; // Used for back-up file save/load in-case DB fails.
    /// Getter for prices.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPrices() 
    {	    	   
    	try{
    		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
    		MongoDatabase mdb = mongoClient.getDatabase("labapi");
    		MongoCollection<Document> resources = mdb.getCollection("resourcePrices");
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
	     	JsonObject loadedJsonData = JsonHelper.GetJsonFromFile(resourcePriceListPath); // Load saved data from file.
	    	JsonHelper.AddObjects(builder, loadedJsonData); // Add old objects
	    	JsonHelper.AddObjects(builder, parsedInputJsonData); // And the new parsed contents  
	    	JsonObject resultingObject = builder.build();    	// finalize it
	    	FileHelper.WriteFileContents(resourcePriceListPath, resultingObject.toString());    	// Write it to file again.    	
	        return Response.ok().build();    	/// Send response to the user.
		}
    }
}
