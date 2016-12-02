package erenik.microservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Random;


public class PlayerTest {
 
    private HttpServer server;
    private WebTarget target;
 
    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
 
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }
 
    @After
    public void tearDown() throws Exception {
        server.stop();
    }
 
    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() 
    {
    	System.out.println("\nTesting /players");
    	
    	// Try fetch 3 random resources from player Erenik.
    	TestGet("erenik/"+Resource.RandomResourceStr());
    	TestGet("erenik/"+Resource.RandomResourceStr());
    	TestGet("erenik/"+Resource.RandomResourceStr());
    	TestGet();
    	TestDelete();
    	TestDelete("erenik");
    	TestGet("avignak");
    	TestGet("erenik");
    	String request = "{\"name\":\"erenik\",";
    	int maxResourcesToAdd = 8;
    	for (int i = 0; i < maxResourcesToAdd; ++i)
    	{
    		request += Resource.RandomResourceAndAmountPair();
    		if (i < maxResourcesToAdd - 1)
    		{
    			request += ",";
    		}
    	}
    	request += "}";
    	// Old request: "{\"name\":\"erenik\","+Resource.RandomResourceAndAmountPair()+"}"
    	TestPost(request);
    	TestPost("{\"name\":\"avignak\","+Resource.RandomResourceAndAmountPair()+"}");
    	TestGet();
    	TestGet("avignak");
    	TestGet("erenik");
    	int random = (new Random()).nextInt() % 10000;
    	String randomName = "VeryRandom"+random;
    	TestPost("{\"name\":\""+randomName+"\","+Resource.RandomResourceAndAmountPair()+"}");
    	TestDelete(randomName);
    }
    void TestGet()
    {    	
    	TestGet("");
    }
    void TestGet(String subPath)
    {    	
    	String path = "players"+(subPath.length() > 0 ? "/"+subPath : "");
    	String responseMsg = target.path(path).request().get(String.class);
        System.out.println("GET /"+path+" response: "+responseMsg);
    }
    void TestPost(String contents)
    {
    	Response r = target.path("players").request().post(Entity.json(contents));
    	System.out.println("POST /players \""+contents+"\", response: "+r);
    }
    void TestDelete()
    {
    	Response r = target.path("players").request().delete(); // Clear it.
    	System.out.println("DELETE /players, response: "+r);
    }
    void TestDelete(String player)
    {
    	Response r = target.path("players/"+player).request().delete(); // Clear it.
    	System.out.println("DELETE /players/"+player+", response: "+r);    	
    }
}