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

//	#마이페이지
//	1) 마이페이지 메인(비밀번호 확인)
	int memberPwdCheck(String userid, String pwd) throws SQLException;

//	2) mnum으로 회원 1명의 정보를 select하는 메소드; 회원정보 수정
	MemberVO getOneMemberBymnum(String mnum) throws SQLException;
//	3) 보유쿠폰 개수 구하기
	int getMyCouponCnt(String userid) throws SQLException;
//	3) 회원 정보 수정 메소드; 회원
	int updateMemberMyInfo(MemberVO membervo) throws SQLException;
	
}
