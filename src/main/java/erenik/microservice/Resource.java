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
 * Helper class for other classes. Used initially to test RESTlessness but changed later.
 */
public class Resource 
{
	static int numRequests = 0;
	static Random random = new Random(System.nanoTime());
    static String RandomResourceStr()
    {
    	String[] resourceStr = {"Coal", "Iron", "Steel", "Gold", "Silver", "Copper", "Tin", "Bronze",
    			"Wood", "Food", "Stone", "Money"};
    	return resourceStr[random.nextInt(resourceStr.length) % resourceStr.length];	
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

