package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceRowMapper implements RowMapper {

	@Override
		public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {

			long id            = rs.getLong("id");
			Date date_from        = rs.getDate("date_from");
			Date date_to = rs.getDate("date_to");
			long productId            = rs.getLong("productId");
			long shopId            = rs.getLong("shopId");

			return new Shop(id, date_from, date_to, productId, shopId);
		}

}
