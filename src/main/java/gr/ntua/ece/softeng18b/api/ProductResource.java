package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Form;

import java.util.Optional;

public class ProductResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        Optional<Product> optional = dataAccess.getProduct(id);
        Product product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));

        return new JsonProductRepresentation(product);
    }

    @Override
    protected Representation delete() throws ResourceException {
        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }
		
        int success;
	boolean User_Volunt = true;
		if (User_Volunt){
			success = dataAccess.withdrawProduct(id);
			if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
		}
		else if (User_Volunt){
			success = dataAccess.deleteProduct(id);
			if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
		}
		else {
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
		}

        return new JsonStringRepresentation("OK");
    }
	
	
	    @Override
    protected Representation put(Representation entity) throws ResourceException {
        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }
		boolean authorized = true;
		Product product;
		if (authorized){
			
			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			String name = form.getFirstValue("name");
			String description = form.getFirstValue("description");
			String category = form.getFirstValue("category");
			boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
			String tags = form.getFirstValue("tags");
			
			Optional<Product> optional = dataAccess.fullUpdateProduct(id,name, description, category, withdrawn, tags);
			product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
		}
		else {
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
		}

        return new JsonProductRepresentation(product);
    }
	
	
/*
    protected Representation patch() throws ResourceException {
        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }
		
		if (authorized){
			Optional<Product> optional = dataAccess.partialUpdateProduct(id);
			Product product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
		}
		else {
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
		}

        return new JsonProductRepresentation(product);
    }*/
}
