package gr.ntua.ece.softeng18b.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import org.restlet.service.CorsService;


public class RestfulApp extends Application {
	
	public RestfulApp() {
		CorsService corsService = new CorsService();         
		corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
		corsService.setAllowedCredentials(false);
		corsService.setAllowingAllRequestedHeaders(true);
		Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("X-OBSERVATORY-AUTH");
        allowHeaders.add("Content-Type");
		corsService.setAllowedHeaders(allowHeaders);
		corsService.setSkippingResourceForCorsOptions(true);
		getServices().add(corsService);
	}

    @Override
    public synchronized Restlet createInboundRoot() {

        Router router = new Router(getContext());

        //GET, POST
        router.attach("/products", ProductsResource.class);

        //GET, PUT, PATCH, DELETE
        router.attach("/products/{id}", ProductResource.class);
		
		//GET, POST
        router.attach("/shops", ShopsResource.class);

        //GET, PUT, PATCH, DELETE
        router.attach("/shops/{id}", ShopResource.class);
		
		//GET, POST
        router.attach("/prices", PricesResource.class);
	
	//POST
	router.attach("/login", LoginResource.class);

	//POST
        router.attach("/logout", LogoutResource.class);

	//POST
        router.attach("/signup", SignupResource.class);

        return router;
    }

}
