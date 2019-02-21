package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.ShowPrice;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;

public class ShowPriceRowMapper implements RowMapper {
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		public ShowPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			double price = rs.getDouble("value");
			Date date = rs.getDate("tempdate");
			String productName = rs.getString("product.name");
			long productId = rs.getLong("product.id");
			//String productTags = rs.getString("product.tags");
			List<String> prod_tags = dataAccess.getProduct_tags(productId);
			long shopId = rs.getLong("shop.id");
			String shopName = rs.getString("shop.name");
			//String shopTags = rs.getString("shop.tags");
			List<String> shop_tags = dataAccess.getShop_tags(shopId);
			String shopAddress = rs.getString("shop.address");
			double shopDist = rs.getDouble("dist");

			return new ShowPrice(price, date, productName, productId, prod_tags,  shopId, shopName, shop_tags, shopAddress, shopDist);}

}
