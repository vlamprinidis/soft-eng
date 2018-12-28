package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Shop;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopsResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		protected Representation get() throws ResourceException {

			 String status = getQueryValue("status");
		        String sort = getQueryValue("sort");

		        if(status == null)
		                status = "WHERE withdrawn = 0 ";
		        else if (status.equals("ALL"))
		                status = " ";
		        else if(status.equals("WITHDRAWN"))
		                status = "WHERE withdrawn = 1 ";
		        else
		                status = "WHERE withdrawn = 0 ";
		
		        if(sort == null)
		                sort = "id DESC";
		        else if(sort.equals("name|DESC"))
		                sort = "name DESC";
		        else if(sort.equals("id|ASC"))
		                sort = "id ASC";
		        else if( sort.equals("name|ASC"))
		                sort = "name ASC";
		        else
		                sort = "id DESC";

		        List<Shop> shops;
		        Limits limits;
		        String start = getQueryValue("start");
		        String count = getQueryValue("count");
		        int st,cnt;
		
		        if(start == null && count == null){
		                limits = new Limits(0);
		                shops = dataAccess.getShops(limits, status, sort);}
		        else if(start == null){
		                cnt = Integer.valueOf(count);
		                limits = new Limits(0,cnt);
		                shops = dataAccess.getShops(limits, status, sort);}
		        else if(getQueryValue("count") == null){
		                st = Integer.valueOf(start);
		                limits = new Limits(st);
		                shops = dataAccess.getShops(limits, status, sort);}
		        else{
		                cnt = Integer.valueOf(count);
	                	st = Integer.valueOf(start);
	        	        limits = new Limits(st, cnt);
		                shops = dataAccess.getShops(limits, status, sort);}			

			Map<String, Object> map = new HashMap<>();
			map.put("start", limits.getStart());
			map.put("count", limits.getCount());
			map.put("total", limits.getTotal());
			map.put("shops", shops);

			return new JsonMapRepresentation(map);
		}

	@Override
		protected Representation post(Representation entity) throws ResourceException {

			//Create a new restlet form
			Form form = new Form(entity);
			//Read the parameters
			String name = form.getFirstValue("name");
			String address = form.getFirstValue("address");
			double lng = Double.valueOf(form.getFirstValue("lng"));
			double lat = Double.valueOf(form.getFirstValue("lat"));
			boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
			String tags = form.getFirstValue("tags");

			//validate the values (in the general case)
			//...

			Shop shop = dataAccess.addShop(name, address, lng, lat, withdrawn, tags);

			return new JsonShopRepresentation(shop);
		}
}
