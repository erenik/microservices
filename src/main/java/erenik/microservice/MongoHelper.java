package erenik.microservice;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
	private static MongoClient mc = null;
	static MongoClient SetupMongoDBClient()
	{
		if (mc != null) // Client already setup? then return and use it now.
			return mc;
		String mongoDBUrl = "ds113938.mlab.com"; //		static String mongoDBUrl = "mongodb://avignak:Perccom3@ds113938.mlab.com:13938/labapi";
		mongoDBUrl = "ds113938.mlab.com";
		char[] password = "perccom3".toCharArray();
		MongoCredential creds = MongoCredential.createScramSha1Credential("erenik", "labapi", password);
		List<MongoCredential> lmc = new ArrayList<MongoCredential>(); 
		lmc.add(creds);
		ServerAddress sa = new ServerAddress(mongoDBUrl, 13938);
		try {
			mc = new MongoClient(sa, lmc,  MongoClientOptions.builder().serverSelectionTimeout(5000).build());
		}catch (Exception e)
		{
			System.out.println(e.toString());
		}		
		return mc;
	}
	
	public static MongoCollection<Document> GetCollection(String byName)
	{
		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
		MongoDatabase mdb = mongoClient.getDatabase("labapi");
		MongoCollection<Document> resources = mdb.getCollection(byName);
		return resources;
	}
	public static MongoCursor<Document> GetDocumentIterator(String forCollection)
	{
		MongoCollection<Document> resources = MongoHelper.GetCollection(forCollection);
		FindIterable<Document> docs = resources.find();
		return docs.iterator();
	}
	
}
