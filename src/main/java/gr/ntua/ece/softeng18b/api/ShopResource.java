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

public class ShopResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		protected Representation get() throws ResourceException {

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
			boolean User_Volunt = true;
			boolean root = false;
			if (User_Volunt){
				success = dataAccess.withdrawShop(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
			}
			else if (root){
				success = dataAccess.deleteShop(id);
				if(success==0) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
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
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}
			boolean authorized = true;
			Shop shop;
			if (authorized){

				//Create a new restlet form
				Form form = new Form(entity);
				//Read the parameters
				String name = form.getFirstValue("name");
				String address = form.getFirstValue("address");
				double lng = Double.valueOf(form.getFirstValue("lng"));
				double lat = Double.valueOf(form.getFirstValue("lat"));
				boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
				String tags = form.getFirstValue("tags");

				Optional<Shop> optional = dataAccess.fullUpdateShop(id,name, address, lng, lat, withdrawn, tags);
				shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else {
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
			}

			return new JsonShopRepresentation(shop);
		}

	@Override
		protected Representation patch(Representation entity) throws ResourceException {
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
			boolean authorized = true;
			if (authorized){

				//Create a new restlet form
				Form form = new Form(entity);
				//Read the parameters
				String name = form.getFirstValue("name");
				String address = form.getFirstValue("address");
				String lng = form.getFirstValue("lng");
				String lat = form.getFirstValue("lat");
				String withdrawn = form.getFirstValue("withdrawn");                                             
				String tags = form.getFirstValue("tags");

				Shop shop;
				String param = new String();
				Optional<Shop> optional;
				if (name!= null) {
					param = "name";
					optional = dataAccess.partialUpdateShop(id,param,name);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
				}
				else if(address!=null){
					param = "address";
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
				else if(tags!=null){
					param = "tags";
					optional = dataAccess.partialUpdateShop(id,param,tags);
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
				}
				else{
					param = "description";
					optional = dataAccess.partialUpdateShop(id,param, "");
					shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
				}
				return new JsonShopRepresentation(shop);
			}
			else {
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
			}

		}



}
