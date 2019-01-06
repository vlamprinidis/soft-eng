package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.ShowPrice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ShowPriceRowMapper implements RowMapper {

	@Override
		public ShowPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			double price = rs.getDouble("value");
			Date date = rs.getDate("tempdate");
			String productName = rs.getString("product.name");
			long productId = rs.getLong("product.id");
			String productTags = rs.getString("product.tags");
			long shopId = rs.getLong("shop.id");
			String shopName = rs.getString("shop.name");
			String shopTags = rs.getString("shop.tags");
			String shopAddress = rs.getString("shop.address");
			double shopDist = rs.getDouble("dist");

			return new ShowPrice(price, date, productName, productId, productTags,  shopId, shopName, shopTags, shopAddress, shopDist);
		}

}
