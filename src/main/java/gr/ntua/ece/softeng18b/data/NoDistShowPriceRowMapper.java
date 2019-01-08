package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.NoDistShowPrice;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.conf.Configuration;

public class NoDistShowPriceRowMapper implements RowMapper {
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

	@Override
		public NoDistShowPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			double price = rs.getDouble("value");
			Date date = rs.getDate("tempdate");
			String productName = rs.getString("product.name");
			long productId = rs.getLong("product.id");
			List<String> productTags = dataAccess.getProduct_tags(productId);
			long shopId = rs.getLong("shop.id");
			String shopName = rs.getString("shop.name");
			List<String> shopTags = dataAccess.getShop_tags(shopId);
			String shopAddress = rs.getString("shop.address");

			return new NoDistShowPrice(price, date, productName, productId, productTags,  shopId, shopName, shopTags, shopAddress);
		}

}
