package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Message;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Form;
import org.restlet.util.Series;
//import org.restlet.engine.header.Header;
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
			boolean root = false;
			//Form headers = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
			//String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
			//Series<Header> series = (Series<Header>)getRequestAttributes().get("org.restlet.http.headers");
    			//System.out.println(series.getFirst("X-OBSERVATORY-AUTH"));
			//System.out.println(user_token);
			if (User_Volunt){
				success = dataAccess.withdrawProduct(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
			}
			else if (root){
				success = dataAccess.deleteProduct(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
			}
			else {
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
			}
			Message message = new Message("OK");
			return new JsonMessageRepresentation(message);
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
				String withdrawn = form.getFirstValue("withdrawn");
				String tags = form.getFirstValue("tags");
				boolean with = Boolean.valueOf(withdrawn);

				String param = new String();
				Optional<Product> optional;

				optional = dataAccess.fullUpdateProduct(id,name, description, category, with, tags);
				product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
			}
				else {
					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
				}

				return new JsonProductRepresentation(product);
			}


			@Override
				protected Representation patch(Representation entity) throws ResourceException {
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
					if (authorized){
						//Create a new restlet form
						Form form = new Form(entity);
						//Read the parameters
						String name = form.getFirstValue("name");
						String description = form.getFirstValue("description");
						String category = form.getFirstValue("category");
						String withdrawn = form.getFirstValue("withdrawn");
						String tags = form.getFirstValue("tags");
						boolean with = Boolean.valueOf(withdrawn);
						Product product;
						String param = new String();
						Optional<Product> optional;
						if (name!= null) {
							param = "name";
							optional = dataAccess.partialUpdateProduct(id,param,name);
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						else if(description!=null){
							param = "description";
							optional = dataAccess.partialUpdateProduct(id,param,description)
								;
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						else if(category!=null){
							param = "category";
							optional = dataAccess.partialUpdateProduct(id,param,category);
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						else if(withdrawn!=null){
							param = "withdrawn";
							optional = dataAccess.partialUpdateProduct(id,param,withdrawn);
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						else if(tags!=null){
							param = "tags";
							optional = dataAccess.partialUpdateProduct(id,param,tags);
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						else{
							param = "description";
							optional = dataAccess.partialUpdateProduct(id,param, "");
							product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));
						}
						return new JsonProductRepresentation(product);
					}
					else {
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
					}

				}
		}

