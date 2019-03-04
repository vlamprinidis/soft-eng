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
import java.util.Optional;
import org.restlet.util.Series;
import javax.mail.Header;
import gr.ntua.ece.softeng18b.data.model.User;

public class ProductResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		protected Representation get() throws ResourceException {
			
			String format = getQueryValue("format");
       			if(format!=null && format.equals("xml"))
               			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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
			
			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to delete product");
		
			Optional<User> optional = dataAccess.getUserByToken(auth);
			User user = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to delete product"));

			String format = getQueryValue("format");
            if(format!=null && format.equals("xml"))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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
			boolean admin = user.isAdmin();

			if (admin == false){
				success = dataAccess.withdrawProduct(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
			}
			else{
				success = dataAccess.deleteProduct(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Product not found - id: " + idAttr);
			}

			Message message = new Message("OK");
			return new JsonMessageRepresentation(message);
		}


	@Override
		protected Representation put(Representation entity) throws ResourceException {

			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter product");
		
			Optional<User> optional2 = dataAccess.getUserByToken(auth);
			User user = optional2.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter product"));
		
			String format = getQueryValue("format");
                if(format!=null && format.equals("xml"))
                   throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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

			Product product;

			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			String name = form.getFirstValue("name");
			String description = form.getFirstValue("description");
			String category = form.getFirstValue("category");
			String withdrawn = form.getFirstValue("withdrawn");
			boolean with = Boolean.valueOf(withdrawn);
			
			String tags = "";
			String[] mytags = form.getValuesArray("tags");
			
			if(name==null||category==null||mytags.length == 0)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Name,category and tags are compulsory fields");
			
			for(int i=0; i<mytags.length; i++){
			String[] str = mytags[i].split(",");
			for(int j=0; j<str.length; j++){
				if(!tags.contains(","+str[j]+",") && !(tags.split(",")[0]).equals(str[j]) && !(tags.split(",")[tags.split(",").length-1]).equals(str[j])){
					if(!(i==0 && j==0))tags = tags + ",";
					tags = tags + str[j];
				}
			}
			
		}
			
			Optional<Product> optional;
			optional = dataAccess.fullUpdateProduct(id,name, description, category, with, tags);
			product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));


				return new JsonProductRepresentation(product);
			}


			@Override
				protected Representation patch(Representation entity) throws ResourceException {

					Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
					String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
					if(auth==null)
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter product");
		
					Optional<User> optional2 = dataAccess.getUserByToken(auth);
					User user = optional2.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter product"));
				
					String format = getQueryValue("format");
		                        if(format!=null && format.equals("xml"))
                		                 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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

					//Create a new restlet form
					Form form = new Form(entity);
					//Read the parameters
					String name = form.getFirstValue("name");
					String description = form.getFirstValue("description");
					String category = form.getFirstValue("category");
					String withdrawn = form.getFirstValue("withdrawn");
					boolean with = Boolean.valueOf(withdrawn);
					String tags = "";
					String[] mytags = form.getValuesArray("tags"); 
					
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
							optional = dataAccess.partialUpdateProduct(id,param,description);
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
					else if(mytags.length != 0){
							param = "tags";
							for(int i=0; i<mytags.length; i++){
								String[] str = mytags[i].split(",");
								for(int j=0; j<str.length; j++){
									if(!tags.contains(","+str[j]+",") && !(tags.split(",")[0]).equals(str[j]) && !(tags.split(",")[tags.split(",").length-1]).equals(str[j])){
										if(!(i==0 && j==0))tags = tags + ",";
										tags = tags + str[j];
									}
								}
								
							}
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
		}

