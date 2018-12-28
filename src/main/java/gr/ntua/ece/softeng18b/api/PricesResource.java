package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
//import java.sql.Date; 

public class PricesResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		protected Representation get() throws ResourceException {

			List<Price> prices = dataAccess.getPrices(new Limits(0, 10));

			Map<String, Object> map = new HashMap<>();
			//map.put("start", xxx);
			//map.put("count", xxx);
			//map.put("total", xxx);
			map.put("prices", prices);

			return new JsonMapRepresentation(map);
		}

	@Override
		protected Representation post(Representation entity) throws ResourceException {

			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			double value = Double.valueOf(form.getFirstValue("value"));
			Date dateFrom = java.sql.Date.valueOf(form.getFirstValue("dateFrom"));
			Date dateTo = java.sql.Date.valueOf(form.getFirstValue("dateTo"));
			long productId = Long.valueOf(form.getFirstValue("productId"));
			long shopId = Long.valueOf(form.getFirstValue("shopId"));

			//validate the values (in the general case)
			//...

			Price price = dataAccess.addPrice(value, dateFrom, dateTo, productId, shopId);

			return new JsonPriceRepresentation(price);
		}
}
