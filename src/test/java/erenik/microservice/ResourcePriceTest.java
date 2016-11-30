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


public class ResourcePriceTest {
 
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
    	System.out.println("\nTesting /resourcePrices");
    	TestGetPrices();
    	TestPutPrices(Resource.RandomResourceAndAmountJSON());
    	TestGetPrices();
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
}