package member.model;

import java.sql.SQLException;

public interface InterMemberDAO {
	
	
//	#아이디중복검사 메소드
	int idDuplicateCheck(String userid) throws SQLException;

//	#회원가입 메소드
	int registerMember(MemberVO membervo) throws SQLException;

//	#회원 로그인 메소드
	MemberVO loginCheck(String userid, String pwd) throws SQLException;
	
}
