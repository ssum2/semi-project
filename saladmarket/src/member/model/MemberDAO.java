package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.AES256;
import util.SHA256;
import util.MyKey;

public class MemberDAO implements InterMemberDAO {
//	#아파치톰캣이 제공하는 DBCP 객체변수 ds 생성 
	private DataSource ds = null; // import javax.sql.DataSource
	
//	#Connection, preparedStatement, ResultSet객체 생성
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
//	#암호화
	AES256 aes = null;

//	#MemberDAO 기본생성자
	public MemberDAO() {
		try {
			Context initContext = new InitialContext();	//	import javax.naming.*;
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
			
			String key =MyKey.key;
			aes = new AES256(key);
			
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println(">>> key값은 17자 이상이어야 합니다.");
			e.printStackTrace();
		} 
	} // end of default constructor
	
	//#사용한 자원을 반납하는 close() 메소드
	public void close() {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	} // end of close()

	
	
//	#아이디 중복검사 메소드
	@Override
	public int idDuplicateCheck(String userid) throws SQLException {
		
		try {
			conn = ds.getConnection();
			String sql = " select count(*) AS CNT "+
						 " from member "+
						 " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			
			int cnt = rs.getInt("CNT");
			if(cnt==1) {	// 입력한 아이디와 일치하는 기존아이디가 있는 경우
				return cnt;
			}
			else {
				return 0;
			}
			
		}  finally {
			close();	
		}
	
	} // end of idDuplicateCheck
	
	
//	#회원가입 메소드
	@Override
	public int registerMember(MemberVO membervo) throws SQLException{
//				>> 호출하는 곳에서 처리하면서 어떤 부분이 잘못됐는지 한번에 보기 위해서 throws
		
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "insert into member("
						+ "mnum, userid, pwd, name, email, phone, birthday,"
						+ " postnum, address1, address2,"
						+ " point,registerdate ,last_logindate ,last_changepwdate ,status,summoney ,fk_lvnum)\n"+
						"values(seq_member_mnum.nextval, ?, ?, \n"+
						"?, ?, ?, ?, ?, ?, ?,\n"+
						"default, default, default, default, default, default, default) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, membervo.getUserid());
			pstmt.setString(2, SHA256.encrypt(membervo.getPwd()));	// SHA256 단방향 암호화
			pstmt.setString(3, membervo.getName());
			pstmt.setString(4, aes.encrypt(membervo.getEmail()));	// AES256 양방향 암호화
			pstmt.setString(5, aes.encrypt(membervo.getPhone()));	// AES256 양방향 암호화
			pstmt.setString(6, membervo.getBirthday());
			pstmt.setString(7, membervo.getPostnum());
			pstmt.setString(8, membervo.getAddress1());
			pstmt.setString(9, membervo.getAddress2());
			
			result = pstmt.executeUpdate();
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of registerMember()
	
	
//	#로그인 메소드(로그인 성공시 MemberVO객체 반환)
	@Override
	public MemberVO loginCheck(String userid, String pwd) throws SQLException {
		MemberVO membervo = null;
		
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql = " select mnum, userid, name, point "+
						"        , trunc( months_between(sysdate, last_changepwdate) ) as pwdchangegap "+
						"        , trunc( months_between(sysdate, last_logindate) ) as lastlogingap "+
						" from member "+
						" where userid = ? and pwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, SHA256.encrypt(pwd));
			
			rs = pstmt.executeQuery();
			
			boolean bool = rs.next();

			if(bool) {
				// select된 회원이 존재하는 경우
				int mnum = rs.getInt("mnum");
				String v_userid = rs.getString("userid");
				String name = rs.getString("name");
				int point = rs.getInt("point");
				int pwdchangegap = rs.getInt("pwdchangegap");
				int lastlogingap = rs.getInt("lastlogingap");
				
				membervo = new MemberVO();
				membervo.setMnum(mnum);
				membervo.setUserid(v_userid);
				membervo.setName(name);
				membervo.setPoint(point);
				
				if(pwdchangegap >= 6) {
					membervo.setRequirePwdChange(true);
				}
				if(lastlogingap>=12) { // 휴면계정일 때
					membervo.setRequireCertify(true);
				}
				else {
	//				#마지막 로그인 한 일시 기록
					sql = " update member set last_logindate=sysdate "
					+ "where userid = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.executeUpdate();
				}
			}
			else {
				// 회원이 존재하지 않는 경우 or status가 0인 회원
			}
		} finally {
			close();
		}
		
		return membervo;
	}
	
	
	
}
