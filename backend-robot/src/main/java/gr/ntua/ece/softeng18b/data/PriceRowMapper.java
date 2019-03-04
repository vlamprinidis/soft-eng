package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PriceRowMapper implements RowMapper {

	@Override
		public Price mapRow(ResultSet rs, int rowNum) throws SQLException {

			double value = rs.getDouble("price");
			Date mydate        = rs.getDate("mydate");
			long productId            = rs.getLong("productId");
			long shopId            = rs.getLong("shopId");

			return new Price(value, mydate, productId, shopId);
		}

}
