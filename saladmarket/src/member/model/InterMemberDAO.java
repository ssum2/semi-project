package member.model;

import java.sql.SQLException;

public interface InterMemberDAO {
	
	
//	#아이디중복검사 메소드
	int idDuplicateCheck(String userid) throws SQLException;
	
}
