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
	
	}
}
