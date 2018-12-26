package gr.ntua.ece.softeng18b.data;


import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.Shop;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

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

    public List<Product> getProducts(Limits limits) {
        //TODO: Support limits
        return jdbcTemplate.query("select * from product order by id", EMPTY_ARGS, new ProductRowMapper());
    }

    public Product addProduct(String name, String description, String category, boolean withdrawn, String tags ) {
        //Create the new product record using a prepared statement
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "insert into product(name, description, category, withdrawn, tags) values(?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, category);
                ps.setBoolean(4, withdrawn);
                ps.setString(5, tags);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if (cnt == 1) {
            //New row has been added
            Product product = new Product(
                keyHolder.getKey().longValue(), //the newly created project id
                name,
                description,
                category,
                withdrawn,
                tags
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
	int res = jdbcTemplate.update("update product set name = ?, description = ?, category = ?, withdrawn = ?, tags = ? where id = ?", name, description, category, withdrawn, tags, params);
		if (res==1){
			/*List<Product> products = jdbcTemplate.query("select * from product where id = ?", params, new ProductRowMapper());
			if (products.size() == 1)  {
				return Optional.of(products.get(0));
			}
			else {
				return Optional.empty();
			}	*/
			return getProduct(id);
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
	else if(field.equals("withdrawn"))res = jdbcTemplate.update("update product set withdrawn = ? where id = ?", Boolean.valueOf(value), params);
	else res = jdbcTemplate.update("update product set tags = ? where id = ?", value, params);
		if (res==1){
			return getProduct(id);
		}
		return Optional.empty();
    }
	
		public List<Shop> getShops(Limits limits) {
        //TODO: Support limits
        return jdbcTemplate.query("select * from shop order by id", EMPTY_ARGS, new ShopRowMapper());
    }

    public Shop addShop(String name, String address, double lng, double lat, boolean withdrawn, String tags ) {
        //Create the new shop record using a prepared statement
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "insert into shop(name, address, lng, lat, withdrawn, tags) values(?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setDouble(3, lng);
                ps.setDouble(4, lat);
                ps.setBoolean(5, withdrawn);
		ps.setString(6, tags);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if (cnt == 1) {
            //New row has been added
            Shop shop = new Shop(
                keyHolder.getKey().longValue(), //the newly created project id
                name,
                address,
                lng,
				lat,
                withdrawn,
                tags
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
        int res = jdbcTemplate.update("update shop set name = ?, address = ?, lng = ?, lat = ?, withdrawn = ?, tags = ? where id = ?", name, address, lng, lat,  withdrawn, tags, params);
		if (res==1){
			/*List<Shop> shops = jdbcTemplate.query("select * from shop where id = ?", params, new ShopRowMapper());
			if (shops.size() == 1)  {
				return Optional.of(shops.get(0));
			}
			else {
				return Optional.empty();
			}	*/
			return getShop(id);
		}
		return Optional.empty();
    }

	/*public Optional<Shop> partialUpdateShop(long id, String name, String address, double lng, double lng, boolean withdrawn, String tags ) {
        Long[] params = new Long[]{id};
        int res = jdbcTemplate.update("update shop set name = ?, address = ?, lng = ?, lat = ?, withdrawn = ?, tags = ? where id = ?", name, address, lng, lat,  withdrawn, tags, params);
		if (res){
			List<Shop> shops = jdbcTemplate.query("select * from shop where id = ?", params, new ShopRowMapper());
			if (shops.size() == 1)  {
				return Optional.of(shops.get(0));
			}
			else {
				return Optional.empty();
			}
		}
		return Optional.empty();
    }*/

	public Price addPrice(double value, Date date_from, Date date_to, long productId, long shopId) {
        //Create the new price record using a prepared statement
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "insert into price(value, date_from, date_to, productId, shopId) values(?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
		ps.setDouble(1, value);
                ps.setDate(2, new java.sql.Date(date_from.getTime()));
                ps.setDate(3, new java.sql.Date(date_to.getTime()));
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
		date_from,
                date_to,
                productId,
		shopId
            );
            return price;

        }
        else {
            throw new RuntimeException("Creation of Price failed");
        }
    }
	
	public List<Price> getPrices(Limits limits) {
        //TODO: Support limits
        return jdbcTemplate.query("select * from price order by id", EMPTY_ARGS, new PriceRowMapper());
    }
	
}
