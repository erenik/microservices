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


public class ResourceTest {
 
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
    	// Not relevant. Tested in ResourcePriceTest.java.
    	/*
    	TestGetPrices();
    	TestPutPrices(Resource.RandomResourceAndAmountJSON());
    	TestGetPrices();

    	if (true)
    		return;
    	/// Below not really relevant anymore.
    	
    	TestGet();
    	System.out.println();
        // Send a put request.
    	TestPut("Very cool");
    	TestGet();

        // Add some random resources.
        for (int i = 0; i < 3; ++i)
        {
        	System.out.println();
        	TestPut(Resource.RandomResourceAndAmountJSON());
        	TestGet();
        }
*/
//        assertEquals("", responseMsg);
    }
    void TestGetPrices()
    {    	
    	String responseMsg = target.path("resourcePrices").request().get(String.class);
        System.out.println("GET /resourcePrices response: "+responseMsg);
    }
    void TestPutPrices(String contents)
    {
    	Response r = target.path("resourcePrices").request().put(Entity.json(contents));
    	System.out.println("PUT /resourcePrices \""+contents+"\", response: "+r);
    }

    void TestGet()
    {
    	String responseMsg = target.path("resource").request().get(String.class);
        System.out.println("GET /resource response: "+responseMsg);
    }
    void TestPut(String contents)
    {
    	Response r = target.path("resource").request().put(Entity.json(contents));
    	System.out.println("PUT \""+contents+"\", response: "+r);
    }
}