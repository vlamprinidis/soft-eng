package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Shop;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

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
			if (User_Volunt){
				success = dataAccess.withdrawShop(id);
				if(!success) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
			}
			else if (User_Volunt){
				success = dataAccess.deleteShop(id);
				if(!success) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Shop not found - id: " + idAttr);
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
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
			}

			Long id = null;
			try {
				id = Long.parseLong(idAttr);
			}
			catch(Exception e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
			}

			if (authorized){

				//Create a new restlet form
				Form form = new Form(entity);
				//Read the parameters
				String name = form.getFirstValue("name");
				String description = form.getFirstValue("description");
				String category = form.getFirstValue("category");
				boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
				String tags = form.getFirstValue("tags");

				Optional<Shop> optional = dataAccess.fullUpdateShop(id,name, description, category, withdrawn, tags);
				Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
			}
			else {
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
			}

			return new JsonShopRepresentation(shop);
		}


	/*
	   protected Representation patch() throws ResourceException {
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

	   if (authorized){
	   Optional<Shop> optional = dataAccess.partialUpdateShop(id);
	   Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));
	   }
	   else {
	   throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot alter data");
	   }

	   return new JsonShopRepresentation(shop);
	   }*/
}
}
