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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.restlet.util.Series;
import javax.mail.Header;
import java.util.Optional;

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

        Map<String, Object> map = new HashMap<>();
        map.put("start", limits.getStart());
        map.put("count", limits.getCount());
        map.put("total", limits.getTotal());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {
	
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
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
        boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
        String tags = form.getFirstValue("tags");

        //validate the values (in the general case)
        //...
		if(name==null||category==null||tags==null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Name,category and tags are compulsory fields");

        Product product = dataAccess.addProduct(name, description, category, withdrawn, tags);

        return new JsonProductRepresentation(product);
    }
}
