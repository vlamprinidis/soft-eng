package gr.ntua.ece.softeng18b.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestfulApp extends Application {

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
/*
	//POST
        router.attach("/logout", LogoutResource.class);

	//POST*/
        router.attach("/signup", SignupResource.class);

        return router;
    }

}
