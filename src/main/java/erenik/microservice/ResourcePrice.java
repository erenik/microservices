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

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Path("resourcePrices")
public class ResourcePrice {
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
  //  		System.out.println("doc: "+doc);
//    		System.out.println("Connect to database successfully");
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

    	if (true) // Try and put into MongoDB
    	{
    		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
    		MongoDatabase mdb = mongoClient.getDatabase("labapi");
    		MongoCollection<Document> resources = mdb.getCollection("resourcePrices");
    		FindIterable<Document> docs = resources.find();
    		MongoCursor<Document> iterator = docs.iterator();
    		Document doc = iterator.next();
    		JsonObject fromDB = JsonHelper.GetJsonFromString(doc.toJson());
    		JsonObjectBuilder builder = Json.createObjectBuilder();

    		JsonHelper.AddObjects(doc, parsedInputJsonData); // Add more key-value pairs into the open document.
    		System.out.println("Updated doc: "+doc.toJson());
    		// try insert the new updated document.
    		resources.insertOne(doc);
    		
//    		JsonHelper.AddObjects(builder, fromDB);
  //  		JsonHelper.AddObjects(builder, parsedInputJsonData);
        	JsonObject resultingObject = builder.build();    	// finalize it
        	// Push it to DB, somehow..
        	String result = resultingObject.toString();
        	doc.parse(result);
    		return Response.ok().build();
    	}
    	// Save to file
     	JsonObject loadedJsonData = JsonHelper.GetJsonFromFile(JsonHelper.defaultFilePath); // Load saved data from file.
    	JsonObjectBuilder builder = Json.createObjectBuilder();    	/// Create new JSON
    	JsonHelper.AddObjects(builder, loadedJsonData); // Add old objects
    	JsonHelper.AddObjects(builder, parsedInputJsonData); // And the new parsed contents  
    	JsonObject resultingObject = builder.build();    	// finalize it
    	FileHelper.WriteFileContents(JsonHelper.defaultFilePath, resultingObject.toString());    	// Write it to file again.    	
        return Response.ok().build();    	/// Send response to the user.
    }


}
