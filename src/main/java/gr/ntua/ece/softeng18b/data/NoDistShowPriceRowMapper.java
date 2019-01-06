package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.NoDistShowPrice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class NoDistShowPriceRowMapper implements RowMapper {

	@Override
		public NoDistShowPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			double price = rs.getDouble("value");
			Date date = rs.getDate("tempdate");
			String productName = rs.getString("product.name");
			long productId = rs.getLong("product.id");
			String productTags = rs.getString("product.tags");
			long shopId = rs.getLong("shop.id");
			String shopName = rs.getString("shop.name");
			String shopTags = rs.getString("shop.tags");
			String shopAddress = rs.getString("shop.address");

			return new NoDistShowPrice(price, date, productName, productId, productTags,  shopId, shopName, shopTags, shopAddress);
		}

}
