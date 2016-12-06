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

/**
 * Helper class for managing a MongoDatabase.
 * @author Emil
 */
public class MongoHelper {
	private static MongoClient mc = null;
	/**
	 * Sets up the default mongoDB client. Hard-coded values are used for now.
	 * If the client is already set up, the old client will be returned and re-used.
	 * @return Returns the set up mongoDB client. 
	 */
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
	/**
	 * Returns the MongoCollection of documents by name from the current mongoDB server.
	 * @param byName Name of the collection to retrieve.
	 * @return collection of documents.
	 */
	public static MongoCollection<Document> GetCollection(String byName)
	{
		MongoClient mongoClient = MongoHelper.SetupMongoDBClient();			// Now connect to your databases
		MongoDatabase mdb = mongoClient.getDatabase("labapi");
		MongoCollection<Document> resources = mdb.getCollection(byName);
		return resources;
	}
	/**
	 * Returns the document iterator for a given collection from the current MongoDB server.
	 * Relies on GetCollection(String byName).
	 * @param forCollection
	 * @return The iterator for documents in the collection.
	 */
	public static MongoCursor<Document> GetDocumentIterator(String forCollection)
	{
		MongoCollection<Document> resources = MongoHelper.GetCollection(forCollection);
		FindIterable<Document> docs = resources.find();
		return docs.iterator();
	}
	
}
