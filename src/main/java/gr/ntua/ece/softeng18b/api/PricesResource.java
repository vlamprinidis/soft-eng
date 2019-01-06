package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Price;
import gr.ntua.ece.softeng18b.data.model.ShowPrice;
import gr.ntua.ece.softeng18b.data.model.NoDistShowPrice;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.restlet.data.Status;
import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;

public class PricesResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
    protected Representation get() throws ResourceException {

	String geoDist = getQueryValue("geoDist");
	String geoLng = getQueryValue("geoLng");
	String geoLat = getQueryValue("geoLat");
	boolean dist;
	
	if(geoDist == null && geoLng == null && geoLat == null){
		dist = false;
	}
	else if(geoDist != null && geoLng != null && geoLat != null){
		dist = true;
	}
	else{
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "geoDist,geoLng,geoLat must either all or none have values");
	}
	
	String dateFrom = getQueryValue("dateFrom");
	String dateTo = getQueryValue("dateTo");
	LocalDate datefrom, dateto;
	Date temp;
	int res;
	
	//delete if exists tdate
	res = dataAccess.deletetDate();
	//create tdate field tempDate
	res = dataAccess.createtDate();
	if(dateFrom == null && dateTo == null){
		datefrom = LocalDate.now(ZoneId.of("Europe/Athens"));
		dateto = LocalDate.now(ZoneId.of("Europe/Athens"));
	}
	else if(dateFrom != null && dateTo != null){
		datefrom = LocalDate.parse(dateFrom);
		dateto = LocalDate.parse(dateTo);
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (LocalDate date = datefrom; date.isBefore(dateto); date = date.plusDays(1)){
			temp = java.sql.Date.valueOf(date);
			res = dataAccess.insertDate(temp);	
		}
	}		
	else{
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "dateFrom,dateTo must either all or none have values");
	}
	temp = java.sql.Date.valueOf(dateto);
	res = dataAccess.insertDate(temp);
	
	String sh;
	Form queryParams = getRequest().getResourceRef().getQueryAsForm(); 
    String[] shops = queryParams.getValuesArray("shops"); 
	if(shops.length==0){
		sh = "";
	}
	else{
		sh = " AND shop.id in (" ;
		for(int i=0; i<shops.length; i++){
			if(i!=0)sh = sh + ",";
			sh = sh + Long.valueOf(shops[i]);
		}
		sh = sh + ") ";
	}
	
	String pr;
	String[] products = queryParams.getValuesArray("products"); 
	if(products.length==0){
		pr = "";
	}
	else{
		pr = " AND product.id in (" ;
		for(int i=0; i<products.length; i++){
			if(i!=0)pr = pr + ",";
			pr = pr + Long.valueOf(products[i]);
		}
		pr = pr + ") ";
	}
	
	String tgs = " AND (";
	String[] tags = queryParams.getValuesArray("tags"); 
	if(tags.length==0){
		tgs = "";
	}
	else{
		for(int i=0; i<tags.length; i++){
			if (i!=0) tgs = tgs + " OR ";
			tgs = tgs + " product.tags LIKE '%" + tags[i] + "%' OR  shop.tags LIKE '%" + tags[i] + "%' ";
		}
		tgs = tgs + ") ";
	}
	
	String sort = getQueryValue("sort");

	if(sort == null)
		sort = "value ASC";
	else if(sort.equals("dist|DESC")&& dist)
		sort = "dist DESC";
	else if(sort.equals("dist|ASC")&& dist)
		sort = "dist ASC"; 
	else if( sort.equals("date|ASC"))	
		sort = "tempdate ASC";
	else if(sort.equals("date|DESC"))
		sort = "tempdate DESC";
	else if(sort.equals("price|DESC"))
		sort = "value DESC";
	else 
		sort = "value ASC";

	List<ShowPrice> prices = new  ArrayList<ShowPrice>();
	List<NoDistShowPrice> nodistprices = new ArrayList<NoDistShowPrice>();
	Limits limits;
	String start = getQueryValue("start");
    String count = getQueryValue("count");
	int st,cnt;

	if(start == null && count == null){
		limits = new Limits(0);
	 	if(dist)prices = dataAccess.getPrices(limits, sort, sh, pr, tgs, geoDist, geoLng, geoLat);
		else nodistprices = dataAccess.getPrices2(limits, sort, sh, pr, tgs);}
	else if(start == null){
		cnt = Integer.valueOf(count);
		limits = new Limits(0,cnt);
		if(dist)prices = dataAccess.getPrices(limits, sort, sh, pr, tgs, geoDist, geoLng, geoLat);
		else nodistprices = dataAccess.getPrices2(limits, sort, sh, pr, tgs);}
	else if(getQueryValue("count") == null){
		st = Integer.valueOf(start);
		limits = new Limits(st);
		if(dist)prices = dataAccess.getPrices(limits, sort, sh, pr, tgs, geoDist, geoLng, geoLat);
		else nodistprices = dataAccess.getPrices2(limits, sort, sh, pr, tgs);}
	else{
		cnt = Integer.valueOf(count);
		st = Integer.valueOf(start);
		limits = new Limits(st, cnt);
		if(dist)prices = dataAccess.getPrices(limits, sort, sh, pr, tgs, geoDist, geoLng, geoLat);
		else nodistprices = dataAccess.getPrices2(limits, sort, sh, pr, tgs);}

        Map<String, Object> map = new HashMap<>();
        map.put("start", limits.getStart());
        map.put("count", limits.getCount());
        map.put("total", limits.getTotal());
		if(dist)
			map.put("prices", prices);
		else
			map.put("prices", nodistprices);

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
