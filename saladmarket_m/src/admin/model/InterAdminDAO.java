package admin.model;

import java.sql.SQLException;

public interface InterAdminDAO {
//	#관리자 로그인
	public AdminVO AdminLoginCheck(String adminid, String adminpw) throws SQLException;
}
