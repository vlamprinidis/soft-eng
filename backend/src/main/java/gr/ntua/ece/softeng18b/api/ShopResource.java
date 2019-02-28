package gr.ntua.ece.softeng18b.api;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Shop;
import gr.ntua.ece.softeng18b.data.model.Message;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Form;
import java.util.Optional;
import org.restlet.util.Series;
import javax.mail.Header;
import gr.ntua.ece.softeng18b.data.model.User;

public class ShopResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		protected Representation get() throws ResourceException {
			
			String format = getQueryValue("format");
                        if(format!=null && format.equals("xml"))
                                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

			String idAttr = getAttribute("id");

			if (idAttr == null) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}

			Optional<Shop> optional = dataAccess.getShop(id);
			Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));

			return new JsonShopRepresentation(shop);
		}

	@Override
		protected Representation delete() throws ResourceException {
			
			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to delete shop");
		
			Optional<User> optional = dataAccess.getUserByToken(auth);
			User user = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to delete shop"));

			String format = getQueryValue("format");
                        if(format!=null && format.equals("xml"))
                                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

			String idAttr = getAttribute("id");

			if (idAttr == null) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}

			int success;
			boolean admin = user.isAdmin();

			if (admin == false){
				success = dataAccess.withdrawShop(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
			}
			else{
				success = dataAccess.deleteShop(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
			}

			Message message = new Message("OK");
			return new JsonMessageRepresentation(message);
		}


	@Override
		protected Representation put(Representation entity) throws ResourceException {
		 
			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter shop");
		
			Optional<User> optional2 = dataAccess.getUserByToken(auth);
			User user = optional2.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter shop"));
		
			String format = getQueryValue("format");
                        if(format!=null && format.equals("xml"))
                                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

			String idAttr = getAttribute("id");

			if (idAttr == null) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}

			Shop shop;
			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			String name = form.getFirstValue("name");
			String address = form.getFirstValue("address");
			double lng = Double.valueOf(form.getFirstValue("lng"));
			double lat = Double.valueOf(form.getFirstValue("lat"));
			boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
			String tags = "";
			String[] mytags = form.getValuesArray("tags"); 
			
			if(name==null||address==null||form.getFirstValue("lng")==null||form.getFirstValue("lat")==null||mytags.length == 0)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Name,address,lng,lat and tags are compulsory fields");
		 try{
			String name_utf8 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
			name = name_utf8;
			String address_utf8 = new String(address.getBytes("ISO-8859-1"), "UTF-8");
			address = address_utf8;
		} catch (Exception E) {
			throw new AssertionError("UTF-8 is unknown");
			// or 'throw new AssertionError("Impossible things are happening today. " +
			//                              "Consider buying a lottery ticket!!");'
		  } 
			
			for(int i=0; i<mytags.length; i++){
				String[] str = mytags[i].split(",");
				for(int j=0; j<str.length; j++){
					if(!tags.contains(","+str[j]+",") && !(tags.split(",")[0]).equals(str[j]) && !(tags.split(",")[tags.split(",").length-1]).equals(str[j])){
						if(!(i==0 && j==0))tags = tags + ",";
						try{
						String tag_utf8 = new String(str[j].getBytes("ISO-8859-1"), "UTF-8");
						tags = tags + tag_utf8;
						} catch (Exception E) {
							throw new AssertionError("UTF-8 is unknown");
							// or 'throw new AssertionError("Impossible things are happening today. " +
							//                              "Consider buying a lottery ticket!!");'
						  } 
					}
				}
			
			}

			Optional<Shop> optional = dataAccess.fullUpdateShop(id,name, address, lng, lat, withdrawn, tags);
			shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));

			return new JsonShopRepresentation(shop);
		  
		}

	@Override
		protected Representation patch(Representation entity) throws ResourceException {
		  try{
			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter shop");
		
			Optional<User> optional2 = dataAccess.getUserByToken(auth);
			User user = optional2.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to alter shop"));
		
			String format = getQueryValue("format");
                        if(format!=null && format.equals("xml"))
                                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

			String idAttr = getAttribute("id");

			if (idAttr == null) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}
			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			String name = form.getFirstValue("name");
			String address = form.getFirstValue("address");
			String lng = form.getFirstValue("lng");
			String lat = form.getFirstValue("lat");
			String withdrawn = form.getFirstValue("withdrawn");                                             
			String tags = "";
			String[] mytags = form.getValuesArray("tags"); 
			
			Shop shop;
			String param = new String();
			Optional<Shop> optional;
			if (name!= null) {
					param = "name";
					String name_utf8 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
					name = name_utf8;
					optional = dataAccess.partialUpdateShop(id,param,name);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else if(address!=null){
					param = "address";
					String address_utf8 = new String(address.getBytes("ISO-8859-1"), "UTF-8");
					address = address_utf8;
					optional = dataAccess.partialUpdateShop(id,param,address);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else if(lng!=null){
					param = "lng";
					optional = dataAccess.partialUpdateShop(id,param,lng);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else if(lat!=null){
					param = "lat";
					optional = dataAccess.partialUpdateShop(id,param,lat);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else if(withdrawn!=null){
					param = "withdrawn";
					optional = dataAccess.partialUpdateShop(id,param,withdrawn);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else if(mytags.length != 0){
					param = "tags";
					for(int i=0; i<mytags.length; i++){
						String[] str = mytags[i].split(",");
						for(int j=0; j<str.length; j++){
							if(!tags.contains(","+str[j]+",") && !(tags.split(",")[0]).equals(str[j]) && !(tags.split(",")[tags.split(",").length-1]).equals(str[j])){
								if(!(i==0 && j==0))tags = tags + ",";
								String tag_utf8 = new String(str[j].getBytes("ISO-8859-1"), "UTF-8");
								tags = tags + tag_utf8;
							}
						}
					
					}
					optional = dataAccess.partialUpdateShop(id,param,tags);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else{
					param = "description";
					optional = dataAccess.partialUpdateShop(id,param, "");
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			return new JsonShopRepresentation(shop);
		  } catch (Exception E) {
			throw new AssertionError("UTF-8 is unknown");
			// or 'throw new AssertionError("Impossible things are happening today. " +
			//                              "Consider buying a lottery ticket!!");'
		  } 

		}



}
