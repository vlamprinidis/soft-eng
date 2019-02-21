package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.conf.Configuration;

public class UserRowMapper implements RowMapper {

	 private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
	@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {

			String username        = rs.getString("username");
			String password = rs.getString("password");
			String name    = rs.getString("name");
			String email    = rs.getString("email");
			boolean admin  = rs.getBoolean("admin");
			String token = rs.getString("token");
			
			return new User(username, password, name, email, admin, token);
		}

}
