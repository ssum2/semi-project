package admin.model;

import java.sql.SQLException;

public interface InterAdminDAO {
//	#������ �α���
	public AdminVO AdminLoginCheck(String adminid, String adminpw) throws SQLException;
}
