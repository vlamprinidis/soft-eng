package gr.ntua.ece.softeng18b.api;
import org.restlet.data.Status;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.restlet.data.Status;
import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;
import org.restlet.util.Series;
import javax.mail.Header;
import java.util.Optional;
import gr.ntua.ece.softeng18b.data.model.User;
import java.text.SimpleDateFormat;

public class PricesResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
    protected Representation get() throws ResourceException {

	String format = getQueryValue("format");
	if(format!=null && format.equals("xml"))
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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
	Date date1,date2;
	
	if(dateFrom == null && dateTo == null){
		datefrom = LocalDate.now(ZoneId.of("Europe/Athens"));
		dateto = LocalDate.now(ZoneId.of("Europe/Athens"));
		date1 = java.sql.Date.valueOf(datefrom);
		date2 = java.sql.Date.valueOf(dateto);
		
	}
	else if(dateFrom != null && dateTo != null){
		//System.out.println(dateFrom + " " + dateTo);
		date1 = java.sql.Date.valueOf(dateFrom);
		date2 = java.sql.Date.valueOf(dateTo);
		System.out.println(date1 + " " + date2);
	}		
	else{
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "dateFrom,dateTo must either all or none have values");
	}
	
	SimpleDateFormat FormatDate = new SimpleDateFormat("yyyy-MM-dd");
	dateFrom = FormatDate.format(date1);
	dateTo = FormatDate.format(date2);
	//datefrom = LocalDate.parse(dateFrom);
	//dateto = LocalDate.parse(dateTo);
	
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
			tgs = tgs + " product.id in (select pid from product_tag where tag LIKE '%" + tags[i] + "%') OR  shop.id in (select sid from shop_tag where tag LIKE '%" + tags[i] + "%') ";
		}
		tgs = tgs + ") ";
	}
	
	String srt="";
	String[] sort = queryParams.getValuesArray("sort"); 

	if(sort.length!=0){
		for(int i=0; i<sort.length; i++){
			if (i!=0) srt = srt + ", ";
			if(sort[i].equals("dist|DESC")&& dist)
				srt += "dist DESC";
			else if(sort[i].equals("dist|ASC")&& dist)
				srt += "dist ASC"; 
			else if( sort[i].equals("date|ASC"))	
				srt += "mydate ASC";
			else if(sort[i].equals("date|DESC"))
				srt += "mydate DESC";
			else if(sort[i].equals("price|DESC"))
				srt += "value DESC";
			else if(sort[i].equals("price|ASC"))
				srt += "value ASC";
			else
				srt = "value ASC";
		}
	}else 
		srt = "value ASC";

	List<ShowPrice> prices = new  ArrayList<ShowPrice>();
	List<NoDistShowPrice> nodistprices = new ArrayList<NoDistShowPrice>();
	Limits limits;
	String start = getQueryValue("start");
    String count = getQueryValue("count");
	int st,cnt;

	if(start == null && count == null){
		limits = new Limits(0);
	 	if(dist)prices = dataAccess.getPrices(limits, srt, sh, pr, tgs, geoDist, geoLng, geoLat, dateFrom, dateTo);
		else nodistprices = dataAccess.getPrices2(limits, srt, sh, pr, tgs, dateFrom, dateTo);}
	else if(start == null){
		cnt = Integer.valueOf(count);
		limits = new Limits(0,cnt);
		if(dist)prices = dataAccess.getPrices(limits, srt, sh, pr, tgs, geoDist, geoLng, geoLat, dateFrom, dateTo);
		else nodistprices = dataAccess.getPrices2(limits, srt, sh, pr, tgs, dateFrom, dateTo);}
	else if(getQueryValue("count") == null){
		st = Integer.valueOf(start);
		limits = new Limits(st);
		if(dist)prices = dataAccess.getPrices(limits, srt, sh, pr, tgs, geoDist, geoLng, geoLat, dateFrom, dateTo);
		else nodistprices = dataAccess.getPrices2(limits, srt, sh, pr, tgs, dateFrom, dateTo);}
	else{
		cnt = Integer.valueOf(count);
		st = Integer.valueOf(start);
		limits = new Limits(st, cnt);
		if(dist)prices = dataAccess.getPrices(limits, srt, sh, pr, tgs, geoDist, geoLng, geoLat, dateFrom, dateTo);
		else nodistprices = dataAccess.getPrices2(limits, srt, sh, pr, tgs, dateFrom, dateTo);}
		
		//System.out.println(nodistprices.get(0).getDate());
		//System.out.println(nodistprices.get(1).getDate());
		//System.out.println(nodistprices.get(2).getDate());

        Map<String, Object> map = new LinkedHashMap<>();
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

			Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
			String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
			if(auth==null)
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to add new price");
		
			Optional<User> optional = dataAccess.getUserByToken(auth);
			User user = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to add new price"));
		
			String format = getQueryValue("format");
       			if(format!=null && format.equals("xml"))
                		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

			//Create a new restlet form

			
			Form form = new Form(entity);
			//Read the parameters
			String value = form.getFirstValue("price");
			String date1 = form.getFirstValue("dateFrom");
			String date2 = form.getFirstValue("dateTo");
			String productId = form.getFirstValue("productId");
			String shopId = form.getFirstValue("shopId");
			
			//System.out.println(value + productId +)
			
			if(value==null||productId==null||shopId==null||date1==null||date2==null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Price,productId,shopId and both dates are compulsory fields");
			
			double val = Double.valueOf(value);
			long prodId = Long.valueOf(productId);
			long shId = Long.valueOf(shopId);			
			
			LocalDate datefrom, dateto;
			datefrom = LocalDate.parse(date1);
			dateto = LocalDate.parse(date2);
			
			List<NoDistShowPrice> nodistprices = new ArrayList<NoDistShowPrice>();
			Limits limits = new Limits(0,-1);
			Price price;
			Date temp;
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			for (LocalDate date = datefrom; date.isBefore(dateto); date = date.plusDays(1)){
				temp = java.sql.Date.valueOf(date);
				price = dataAccess.addPrice(val, temp, prodId, shId);	
			}
			temp = java.sql.Date.valueOf(dateto);
			price = dataAccess.addPrice(val, temp, prodId, shId);
			
			String sh = " AND shop.id in (" + shId + ") " ;
			String pr = " AND product.id in (" + prodId + ") " ;
			String tgs = "";
			String sort = "mydate ASC";
			
			Date dateFrom,dateTo;
			dateFrom = java.sql.Date.valueOf(date1);
			dateTo = java.sql.Date.valueOf(date2);
			SimpleDateFormat FormatDate = new SimpleDateFormat("yyyy-MM-dd");
			date1 = FormatDate.format(dateFrom);
			date2 = FormatDate.format(dateTo);
			System.out.println("all good");
			
			nodistprices = dataAccess.getPrices2(limits, sort, sh, pr, tgs, date1, date2);
			System.out.println("all good");

			Map<String, Object> map = new LinkedHashMap<>();
			map.put("start", limits.getStart());
			map.put("count", limits.getCount());
			map.put("total", limits.getTotal());
			map.put("prices", nodistprices);
			return new JsonMapRepresentation(map);
		}
}
