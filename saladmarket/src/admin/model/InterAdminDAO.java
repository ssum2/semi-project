package admin.model;

import java.sql.SQLException;

public interface InterAdminDAO {
//	#������ �α���
	public boolean AdminLoginCheck(String adminid, String adminpw) throws SQLException;
}
