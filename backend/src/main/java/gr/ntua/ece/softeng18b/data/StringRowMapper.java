package gr.ntua.ece.softeng18b.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringRowMapper implements RowMapper {

	@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			return rs.getString(1);
		}

}
