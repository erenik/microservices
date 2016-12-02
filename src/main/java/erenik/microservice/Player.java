package erenik.microservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import com.mongodb.client.model.Filters;

@Path("players")
public class Player 
{
	
	List<String> GetAllPlayerNames()
	{
		MongoCursor<Document> iterator = MongoHelper.GetDocumentIterator("players");
		JsonObjectBuilder builder = Json.createObjectBuilder();
		String json;
		// Build a single name-array with the names of all players.
		List<String> nameList = new ArrayList<String>();
		while(iterator.hasNext())
		{
			Document doc = iterator.next();
			String name = doc.getString("name");
			nameList.add(name);
		}
		return nameList;
	}
	
	boolean AlreadyExists(String name)
	{
		List<String> names = GetAllPlayerNames();
		for (int i = 0; i < names.size(); ++i)
		{
			if (names.get(i).equals(name))
				return true;
		}
		return false;
	}
	
    /// Getter for players.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPlayers() 
    {	    	   
    	try{
    		MongoCursor<Document> iterator = MongoHelper.GetDocumentIterator("players");
    		JsonObjectBuilder builder = Json.createObjectBuilder();
    		// Build a single name-array with the names of all players.
    		JsonArrayBuilder nameArrayBuilder = Json.createArrayBuilder();
    		while(iterator.hasNext())
    		{
    			Document doc = iterator.next();
    			String name = doc.getString("name");
//    			System.out.println("name: "+name);
    			nameArrayBuilder.add(name);
//        		JsonObject obj = JsonHelper.GetJsonFromString(doc.toJson());
    		}
    		builder.add("playerNames", nameArrayBuilder);
    		return builder.build().toString();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPlayerByName(@PathParam("name") String name) 
    {	    	   
    	try{
			MongoCollection<Document> resources = MongoHelper.GetCollection("players");
    		FindIterable<Document> docs = resources.find(Filters.eq("name",name));
    		MongoCursor<Document> iterator = docs.iterator();
    		if (iterator.hasNext() == false)
    			return null;
    		Document doc = iterator.next();
    		return doc.toJson();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    /// Getter for resources of a specific player.
    @GET
    @Path("{name}/{resource}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPlayerResource(@PathParam("name") String name, @PathParam("resource") String resource) 
    {	    	   
    	try{
			MongoCollection<Document> resources = MongoHelper.GetCollection("players");
    		FindIterable<Document> docs = resources.find(Filters.eq("name",name));
    		MongoCursor<Document> iterator = docs.iterator();
    		if (iterator.hasNext() == false)
    			return null;
    		Document doc = iterator.next();
    		JsonObject obj = JsonHelper.GetJsonFromString(doc.toJson());
    		Set<String> keys = obj.keySet();
    		JsonObjectBuilder builder = Json.createObjectBuilder();
    		for (int i = 0; i < keys.size(); ++i)
    		{
    			String key = (String) keys.toArray()[i];
    			JsonValue val = obj.get(key);
    			if (key.equals(resource))
    			{
    				// Return it.
    				builder.add(key, val);
    				return builder.build().toString();
    			}    			
//    			System.out.println(key + " : "+val.toString());
    		}
    		return null;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    /// Delete a specific player entry.
    @DELETE
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlayerByName(@PathParam("name") String name) 
    {	    	   
    	try{
    		MongoCollection<Document> resources = MongoHelper.GetCollection("players");
    		FindIterable<Document> docs = resources.find(Filters.eq("name",name));
    		MongoCursor<Document> iterator = docs.iterator();
    		if (iterator.hasNext() == false)
    			return Response.noContent().build();
    		Document doc = iterator.next();
    		Document filter = Document.parse("{\"name\" : \""+doc.get("name")+"\"}");
    		resources.deleteOne(filter);
    		return Response.ok().build();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return Response.noContent().build();
    }
    
    /// Put! requests. Assume they put something useful.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
   // @Path("/result")
    public Response postIt(String jsonInput) 
    {    		
    	// Try and put into MongoDB
		MongoCollection<Document> resources = MongoHelper.GetCollection("players");
		FindIterable<Document> docs = resources.find();
		Document doc = Document.parse(jsonInput);
		/// Check if it already exists?
		String name = doc.getString("name");
		if (AlreadyExists(name))
		{
			return Response.notModified().build(); // Signify that it already exists?
		}
		// try insert the new updated document.
		try {
			resources.insertOne(doc);
			return Response.ok().build();
		} catch (Exception e)
		{
			System.out.println("Error "+e.toString());			
			return Response.noContent().build();
		}
    }
    /// For deleting the arbitrary players that we have created.
    @DELETE
    public Response deleteAll()
    {
		// Try and delete all existing players.
		try {
			MongoCollection<Document> resources = MongoHelper.GetCollection("players");
			List<String> name = GetAllPlayerNames();
			for (int i = 0; i < name.size(); ++i)
			{				
				Document filter = Document.parse("{\"name\" : \""+name.get(i)+"\"}");
				resources.deleteOne(filter);
			}
			return Response.ok().build();
		} catch (Exception e)
		{
			e.printStackTrace();
			return Response.noContent().build();
		}
    }
}
