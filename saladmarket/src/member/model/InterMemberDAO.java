package member.model;

import java.sql.SQLException;

public interface InterMemberDAO {
	
	
//	#���̵��ߺ��˻� �޼ҵ�
	int idDuplicateCheck(String userid) throws SQLException;
	
}
