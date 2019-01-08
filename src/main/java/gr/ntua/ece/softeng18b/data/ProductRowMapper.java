package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Product;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.conf.Configuration;

public class ProductRowMapper implements RowMapper {

	 private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
	@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {

			long id            = rs.getLong("id");
			String name        = rs.getString("name");
			String description = rs.getString("description");
			String category    = rs.getString("category");
			boolean withdrawn  = rs.getBoolean("withdrawn");
			//Product_tag tag    = new Product_tag(rs.getString("tag"));
			List<String> tags = dataAccess.getProduct_tags(id);
			return new Product(id, name, description, category, withdrawn, tags);
		}

}
