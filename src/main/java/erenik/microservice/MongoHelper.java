package erenik.microservice;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

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
//		System.out.println("Yeah.");
		try {
			mc = new MongoClient(sa, lmc,  MongoClientOptions.builder().serverSelectionTimeout(5000).build());
		}catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
//		mc = new MongoClient( new MongoClientURI(mongoDBUrl), creds);

//		MongoCredential mc = new MongoCredential();
	//	mongoClient.
//		MongoClient(ServerAddress addr, List<MongoCredential> credentialsList)		mongoClient.
		return mc;
	}
}