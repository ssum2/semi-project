package admin.model;

import java.sql.SQLException;

public interface InterAdminDAO {
//	#관리자 로그인
	public boolean AdminLoginCheck(String adminid, String adminpw) throws SQLException;
}
