package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.User;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Status;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.restlet.util.Series;
import javax.mail.Header;
import java.util.Optional;
import java.lang.*;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class ProductsResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {
	
	
	String format = getQueryValue("format");
	if(format!=null && format.equals("xml"))
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

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
	
	List<Product> products;
	Limits limits;
	String start = getQueryValue("start");
        String count = getQueryValue("count");
	int st,cnt;

	if(start == null && count == null){
		limits = new Limits(0);
	 	products = dataAccess.getProducts(limits, status, sort);}
	else if(start == null){
		cnt = Integer.valueOf(count);
		limits = new Limits(0,cnt);
		products = dataAccess.getProducts(limits, status, sort);}
	else if(getQueryValue("count") == null){
		st = Integer.valueOf(start);
		limits = new Limits(st);
		products = dataAccess.getProducts(limits, status, sort);}
	else{
		cnt = Integer.valueOf(count);
		st = Integer.valueOf(start);
		limits = new Limits(st, cnt);
		products = dataAccess.getProducts(limits, status, sort);}

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("start", limits.getStart());
        map.put("count", limits.getCount());
        map.put("total", limits.getTotal());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {
	
	  try {
		Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
		String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
		if(auth==null)
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to add new product");
		
		Optional<User> optional = dataAccess.getUserByToken(auth);
		User user = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Please log in to add new product"));
		
		String format = getQueryValue("format");
        if(format!=null && format.equals("xml"))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String name = form.getFirstValue("name");
		//String fieldvalue_utf8 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		//System.out.println("name1st"+name);
		
		//System.out.println("name"+fieldvalue_utf8);
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
		
			 //name = URLDecoder.decode(name, "UTF-8");
			 //System.out.println("name2"+name);	 
		
        boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
		
        String tags = "";
		String[] mytags = form.getValuesArray("tags"); 
		
		if(name==null||category==null||mytags.length == 0)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Name,category and tags are compulsory fields");
		
		String name_utf8 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		name = name_utf8;
		String description_utf8 = new String(description.getBytes("ISO-8859-1"), "UTF-8");
		description = description_utf8;
		
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

		//getNext value

        //validate the values (in the general case)
        //...
		

        Product product = dataAccess.addProduct(name, description, category, withdrawn, tags);

        return new JsonProductRepresentation(product);
		
		} catch (Exception E) {
			throw new AssertionError("UTF-8 is unknown");
			// or 'throw new AssertionError("Impossible things are happening today. " +
			//                              "Consider buying a lottery ticket!!");'
		} 
    }
}
