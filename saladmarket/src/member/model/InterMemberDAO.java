package member.model;

import java.sql.SQLException;

public interface InterMemberDAO {
	
	
//	#아이디중복검사 메소드
	int idDuplicateCheck(String userid) throws SQLException;

//	#회원가입 메소드
	int registerMember(MemberVO membervo) throws SQLException;

//	#회원 로그인 메소드
	MemberVO loginCheck(String userid, String pwd) throws SQLException;

//	#아이디 찾기
	String findUserid(String name, String phone) throws SQLException;

//	#비밀번호 찾기
//	1) userid와 email정보로 해당 유저가 존재하는 지 확인
	int isUserExists(String userid, String email) throws SQLException;
	
//	2) 새로운 비밀번호로 변경
	int changeNewPwd(String userid, String pwd) throws SQLException;
	
}
