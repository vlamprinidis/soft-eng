package gr.ntua.ece.softeng18b.data;
import java.io.*;
import gr.ntua.ece.softeng18b.data.model.NoDistShowPrice;
import gr.ntua.ece.softeng18b.data.model.ShowPrice;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.Shop;
import gr.ntua.ece.softeng18b.data.model.User;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import java.util.ArrayList;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class DataAccess {

	private static final Object[] EMPTY_ARGS = new Object[0];

	private static final int MAX_TOTAL_CONNECTIONS = 16;
	private static final int MAX_IDLE_CONNECTIONS = 8;

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public void setup(String driverClass, String url, String user, String pass) throws SQLException {

		//initialize the data source
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(driverClass);
		bds.setUrl(url);
		bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
		bds.setUsername(user);
		bds.setPassword(pass);
		bds.setValidationQuery("SELECT 1");
		bds.setTestOnBorrow(true);
		bds.setDefaultAutoCommit(true);

		//check that everything works OK
		bds.getConnection().close();

		//initialize the jdbc template utilitiy
		jdbcTemplate = new JdbcTemplate(bds);
	}

	public List<Product> getProducts(Limits limits,String status,String sort) {
		int count;
		count = jdbcTemplate.queryForObject("SELECT count(*) FROM product " + status + "", Integer.class);
		limits.setTotal(count);
		return jdbcTemplate.query("SELECT * FROM product " + status + " ORDER BY " + sort + " LIMIT " + limits.getStart() + "," + limits.getCount() + "", new ProductRowMapper());
	}
	
	public List<String> getProduct_tags(long id) {
                return jdbcTemplate.query("SELECT tag FROM product_tag WHERE pid=" + id + "", new StringRowMapper());
        }

	public String addProduct_tag(long id, String tag) {
                //Create the new product_tag record using a prepared statement
                PreparedStatementCreator psc = new PreparedStatementCreator() {
                        @Override
                                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                        PreparedStatement ps = con.prepareStatement(
                                                "insert into product_tag(pid,tag) values(?, ?)",Statement.RETURN_GENERATED_KEYS);
                                        ps.setLong(1, id);
                                        ps.setString(2, tag);
                                        return ps;
                                }
                };
                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
                int cnt = jdbcTemplate.update(psc, keyHolder);

                if (cnt == 1) {
                        
                        return tag;

                }
                else {
                        throw new RuntimeException("Creation of Product failed, please check your tags");
                }
        }

	public Product addProduct(String name, String description, String category, boolean withdrawn, String tags) {
		//Create the new product record using a prepared statement
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							"insert into product(name, description, category, withdrawn) values(?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS
							);
					ps.setString(1, name);
					ps.setString(2, description);
					ps.setString(3, category);
					ps.setBoolean(4, withdrawn);
					return ps;
				}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int cnt = jdbcTemplate.update(psc, keyHolder);

		if (cnt == 1) {
			//New row has been added
			List<String> product_tags= new ArrayList<String>();
			for (String tag: tags.split(",")) {
     		        	product_tags.add(addProduct_tag(keyHolder.getKey().longValue(),tag));
			}
			Product product = new Product(
					keyHolder.getKey().longValue(), //the newly created project id
					name,
					description,
					category,
					withdrawn,
					product_tags
					);
			return product;

		}
		else {
			throw new RuntimeException("Creation of Product failed");
		}
	}

	public Optional<Product> getProduct(long id) {
		Long[] params = new Long[]{id};
		List<Product> products = jdbcTemplate.query("select * from product where id = ?", params, new ProductRowMapper());
		if (products.size() == 1)  {
			return Optional.of(products.get(0));
		}
		else {
			return Optional.empty();
		}
	}

	public int deleteProduct(long id) {
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("delete from product where id = ?", params);
		return res;
	}

	public int withdrawProduct(long id) {
		int bit = 1;
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("update product set withdrawn = ? where id = ?", bit, params);
		return res;
	}

	public Optional<Product> fullUpdateProduct(long id, String name, String description, String category, boolean withdrawn, String tags) {
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("update product set name = ?, description = ?, category = ?, withdrawn = ? where id = ?", name, description, category, withdrawn, params);
		if (res==1){
			List<String> product_tags= new ArrayList<String>();
			res = jdbcTemplate.update("delete from product_tag where pid = ?", params);
                        for (String tag: tags.split(",")) {
				product_tags.add(addProduct_tag(id,tag));
       			}
			return getProduct(id);
			//pr.setTags(product_tags);
			//return pr;
		}
		return Optional.empty();
	}

	public Optional<Product> partialUpdateProduct(long id, String field, String value) {
		//Long[] params = new Long[]{id};
		long params = id;
		int res;
		if(field.equals("name"))res = jdbcTemplate.update("update product set name = ? where id = ?", value, params);
		else if(field.equals("description"))res = jdbcTemplate.update("update product set description = ? where id = ?", value, params);
		else if(field.equals("category"))res = jdbcTemplate.update("update product set category = ? where id = ?", value, params);
		//else if(field.equals("withdrawn"))res = jdbcTemplate.update("update product set withdrawn = ? where id = ?", Boolean.valueOf(value), params);
		else{
			 //res = jdbcTemplate.update("update product set tags = ? where id = ?", value, params);
                        List<String> product_tags= new ArrayList<String>();
                        res = jdbcTemplate.update("delete from product_tag where pid = ?", params);
                        for (String tag: value.split(",")) {
                                product_tags.add(addProduct_tag(id,tag));
                        }
                       // return getProduct(id);
			//pr.setTags(product_tags);
		}
		if(res!=0)return getProduct(id);
		else return Optional.empty();
	}


	public List<Shop> getShops(Limits limits,String status,String sort) {
		int count;
		count = jdbcTemplate.queryForObject("SELECT count(*) FROM shop " + status + "", Integer.class
				);
		limits.setTotal(count);
		return jdbcTemplate.query("SELECT * FROM shop " + status + " ORDER BY " + sort + " LIMIT " +
				limits.getStart() + "," + limits.getCount() + "", new ShopRowMapper());
	}

	public List<String> getShop_tags(long id) {
                return jdbcTemplate.query("SELECT tag FROM shop_tag WHERE sid=" + id + "", new StringRowMapper());
        }

	public String addShop_tag(long id, String tag) {
                //Create the new shop_tag record using a prepared statement
                PreparedStatementCreator psc = new PreparedStatementCreator() {
                        @Override
                                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                        PreparedStatement ps = con.prepareStatement(
                                                "insert into shop_tag(sid,tag) values(?, ?)",Statement.RETURN_GENERATED_KEYS);
                                        ps.setLong(1, id);
                                        ps.setString(2, tag);
                                        return ps;
                                }
                };
                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
                int cnt = jdbcTemplate.update(psc, keyHolder);

                if (cnt == 1) {
                        return tag;
                }
                else {
                        throw new RuntimeException("Creation of Shop_tag failed");
                }
        }

	public Shop addShop(String name, String address, double lng, double lat, boolean withdrawn, String tags ) {
		//Create the new shop record using a prepared statement
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							"insert into shop(name, address, lng, lat, withdrawn) values(?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS
							);
					ps.setString(1, name);
					ps.setString(2, address);
					ps.setDouble(3, lng);
					ps.setDouble(4, lat);
					ps.setBoolean(5, withdrawn);
					return ps;
				}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int cnt = jdbcTemplate.update(psc, keyHolder);

		if (cnt == 1) {
			//New row has been added
			List<String> shop_tags= new ArrayList<String>();
                        for (String tag: tags.split(",")) {
                                shop_tags.add(addShop_tag(keyHolder.getKey().longValue(),tag));
                        }
			Shop shop = new Shop(
					keyHolder.getKey().longValue(), //the newly created project id
					name,
					address,
					lng,
					lat,
					withdrawn,
					shop_tags
					);
			return shop;

		}
		else {
			throw new RuntimeException("Creation of Shop failed");
		}
	}

	public Optional<Shop> getShop(long id) {
		Long[] params = new Long[]{id};
		List<Shop> shops = jdbcTemplate.query("select * from shop where id = ?", params, new ShopRowMapper());
		if (shops.size() == 1)  {
			return Optional.of(shops.get(0));
		}
		else {
			return Optional.empty();
		}
	}

	public int deleteShop(long id) {
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("delete from shop where id = ?", params);
		return res;
	}

	public int withdrawShop(long id) {
		int bit = 1;
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("update shop set withdrawn = ? where id = ?", bit, params);
		return res;
	}

	public Optional<Shop> fullUpdateShop(long id, String name, String address, double lng, double lat, boolean withdrawn, String tags ) {
		//Long[] params = new Long[]{id};
		long params = id;
		int res = jdbcTemplate.update("update shop set name = ?, address = ?, lng = ?, lat = ?, withdrawn = ? where id = ?", name, address, lng, lat,  withdrawn, params);
		if (res==1){
			List<String> shop_tags= new ArrayList<String>();
                        res = jdbcTemplate.update("delete from shop_tag where sid = ?", params);
                        for (String tag: tags.split(",")) {
                                shop_tags.add(addShop_tag(id,tag));
                        }
                        //Shop sh = getShop(id);
                        //sh.setTags(shop_tags);
                        return getShop(id);
		}
		return Optional.empty();
	}

	public Optional<Shop> partialUpdateShop(long id, String field, String value) {
		long params = id;
		int res;
		if(field.equals("name"))res = jdbcTemplate.update("update shop set name = ? where id = ?", value, params);
		else if(field.equals("address"))res = jdbcTemplate.update("update shop set address = ? where id = ?", value, params);
		else if(field.equals("lng"))res = jdbcTemplate.update("update shop set lng = ? where id = ?", Long.valueOf(value), params);
		else if(field.equals("lat"))res = jdbcTemplate.update("update shop set lat = ? where id = ?", Long.valueOf(value), params);
		//else if(field.equals("withdrawn"))res = jdbcTemplate.update("update shop set withdrawn = ? where id = ?", Boolean.valueOf(value), params);
		else{
			List<String> shop_tags= new ArrayList<String>();
                        res = jdbcTemplate.update("delete from shop_tag where sid = ?", params);
                        for (String tag: value.split(",")) {
                                shop_tags.add(addShop_tag(id,tag));
                        }
		}
		
		if(res!=0)return getShop(id);
                else return Optional.empty();
	}

	public Price addPrice(double value, Date dateFrom, Date dateTo, long productId, long shopId) {
		//Create the new price record using a prepared statement
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							"insert into price(value, dateFrom, dateTo, productId, shopId) values(?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS
							);
					ps.setDouble(1, value);
					ps.setDate(2, new java.sql.Date(dateFrom.getTime()));
					ps.setDate(3, new java.sql.Date(dateTo.getTime()));
					ps.setLong(4, productId);
					ps.setLong(5, shopId);
					return ps;
				}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int cnt = jdbcTemplate.update(psc, keyHolder);

		if (cnt == 1) {
			//New row has been added
			Price price = new Price(
					keyHolder.getKey().longValue(), //the newly created project id
					value,
					dateFrom,
					dateTo,
					productId,
					shopId
					);
			return price;

		}
		else {
			throw new RuntimeException("Creation of Price failed");
		}
	}

	public User addUser(String username, String password, String name, String email, boolean admin, String token) {
		//Create the new price record using a prepared statement
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							"insert into users(username, password, name, email,admin,token) values(?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS
							);
					ps.setString(1, username);
					ps.setString(2, password);
					ps.setString(3,name);
					ps.setString(4, email);
					ps.setBoolean(5,admin);
					ps.setString(6,token);
					return ps;
				}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int cnt = jdbcTemplate.update(psc, keyHolder);

		if (cnt == 1) {
			//New row has been added
			User user = new User(
					//keyHolder.getKey().longValue(), //the newly created project id
					username,
					password,
					name,
					email,
					admin,
					token
					);
			return user;

		}
		else {
			throw new RuntimeException("Creation of Volunteer failed");
		}
	}

	public List<Price> getPrices(Limits limits) {
		//TODO: Support limits
		return jdbcTemplate.query("select * from price order by id", EMPTY_ARGS, new PriceRowMapper());
	}

	public Optional<String> getUser(String username) {
		String[] params = new String[]{username};
		String sql = "select password from users where username = '";
		List<String> user  = jdbcTemplate.query(sql + username + "'", new StringRowMapper());
		
		if (user.size() == 1)  {
			return Optional.of(user.get(0));
		}
		else {
			return Optional.empty();
		}
	}
	
	public boolean userExists(String username) {
		String[] params = new String[]{username};
		String sql = "select password from users where username = '";
		List<String> user  = jdbcTemplate.query(sql + username + "'", new StringRowMapper());
		
		if (user.size() == 1)  {
			return true;
		}
		else {
			return false;
		}
	}


	 public int deletetDate() {
		jdbcTemplate.execute("DROP TABLE IF EXISTS tdate");
		return 1;
	}

	 public int createtDate() {
		jdbcTemplate.execute("CREATE TABLE `tdate` ( `tempdate` date NOT NULL, PRIMARY KEY (`tempdate`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8");
		return 1;
	}


	public int insertDate(Date date) {
		//Create the new price record using a prepared statement
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							"insert into tdate(tempdate) values(?)",Statement.RETURN_GENERATED_KEYS);
					ps.setDate(1, new java.sql.Date(date.getTime()));
					return ps;
				}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int cnt = jdbcTemplate.update(psc, keyHolder);

		if (cnt == 1) {
			//New row has been added
			 return 1;
		}
		else {
			throw new RuntimeException("Insertion in tdate failed");
		}
	}

	
public List<ShowPrice> getPrices(Limits limits,String sort,String shops,String products, String tgs, String geoDist, String geoLng, String geoLat) {
		double maxdist = Double.valueOf(geoDist);
		double lng = Double.valueOf(geoLng);
		double lat = Double.valueOf(geoLat);
		int count;
		count = jdbcTemplate.queryForObject("SELECT count(*) from price,product,shop,tdate,(SELECT distanceOf(shop.lng,shop.lat," + lng + ", " + lat+ ") as dist,shop.id as tempId from shop)foo where productId = product.id and shopId = shop.id and tempId=shopId and tempdate>=dateFrom and tempdate<=dateTo " + shops + products + tgs + " and dist < " + maxdist + "", Integer.class);
		limits.setTotal(count);
		return jdbcTemplate.query("select value, tdate.tempdate, product.name, product.id, shop.id, shop.name, shop.address, dist from price,product,shop,tdate,(SELECT distanceOf(shop.lng,shop.lat," + lng + ", " + lat+ ") as dist,shop.id as tempId from shop)foo where product.withdrawn = 0 and shop.withdrawn = 0 and productId = product.id and shopId = shop.id and tempId=shopId and tempdate>=dateFrom and tempdate<=dateTo " + shops + products + tgs + " and dist < " + maxdist + " ORDER BY " + sort + " LIMIT " + limits.getStart() + "," + limits.getCount() + "", new ShowPriceRowMapper());
	}
	
public List<NoDistShowPrice> getPrices2(Limits limits,String sort,String shops,String products, String tgs) {
		int count;
		count = jdbcTemplate.queryForObject("SELECT count(*) from price,product,shop,tdate where productId = product.id and shopId = shop.id and tempdate>=dateFrom and tempdate<=dateTo " + shops + products + tgs + "", Integer.class);
		limits.setTotal(count);
		return jdbcTemplate.query("select value, tdate.tempdate, product.name, product.id, shop.id, shop.name, shop.address from price,product,shop,tdate where product.withdrawn = 0 and shop.withdrawn = 0 and productId = product.id and shopId = shop.id and tempdate>=dateFrom and tempdate<=dateTo " + shops + products + tgs + " ORDER BY " + sort + " LIMIT " + limits.getStart() + "," + limits.getCount() + "", new NoDistShowPriceRowMapper());
	}
	
	public Optional<User> getUserByToken(String auth) {
		String[] params = new String[]{auth};
		List<User> users = jdbcTemplate.query("select * from users where token = ?", params, new UserRowMapper());
		if (users.size() == 1)  {
			return Optional.of(users.get(0));
		}
		else {
			return Optional.empty();
		}
	}
	
	public int changeToken(String username, String token) {
		int res = jdbcTemplate.update("update users set token = ? where username = ?", token, username);
		return res;
	}
	
}
