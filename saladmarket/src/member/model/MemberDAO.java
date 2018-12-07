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
		int n1=0, n2=0, n3=0;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false); // 수동 커밋으로 전환 (트랜젝션)
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
			
			n1 = pstmt.executeUpdate();
			if(n1!=1) {
				conn.rollback();
				conn.setAutoCommit(true);
				return 0;
			}
			
			if(n1==1) {
				sql = "insert into my_coupon(fk_userid, fk_cpnum, cpexpiredate) values(?, 1, add_months(sysdate, 1))";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, membervo.getUserid());
				n2 = pstmt.executeUpdate();
				
				if(n2!=1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				}
			}
			
			if(n2==1) {
				sql = "insert into my_coupon(fk_userid, fk_cpnum, cpexpiredate) values(?, 2, add_months(sysdate, 1))";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, membervo.getUserid());
				n3 = pstmt.executeUpdate();
				
				if(n3!=1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				}
			}
			if(n3==1) {
				if(n1*n2*n3 == 1) {
					conn.commit();
					conn.setAutoCommit(true);
					return 1;
				}
				else {
					return 0;
				}
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return n1*n2*n3;
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
	
//	#아이디 찾기 메소드
	@Override
	public String findUserid(String name, String phone) throws SQLException {
		String userid = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select userid "
					+ " from member"
					+ " where status=1 and name=? and phone = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, aes.encrypt(phone));
	
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userid = rs.getString("userid");
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			
			e.printStackTrace();
		}  finally {
			close();
		}	
		
		return userid;
	}
	
	
//	#회원 인증용; 패스워드 찾기
//	 유효한 이메일이고 인증메일이 발송되면 1, 메일 발송 실패시 -1 리턴
	@Override
	public int isUserExists(String userid, String email) throws SQLException{
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) as CNT "
					+ " from member"
					+ " where status=1 and userid=? and email=? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setString(2, aes.encrypt(email));
	
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n=rs.getInt("CNT");
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}	

		return n;
	}

	
//	#새로운 비밀번호로 변경하는 메소드 (비밀번호 찾기)
	@Override
	public int changeNewPwd(String userid, String pwd) throws SQLException{
		int n = 0;
		try {
			conn = ds.getConnection();
			String sql = " update member set pwd=?, last_changepwdate=sysdate"+
					 	  " where userid = ? and status=1 ";
		
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, SHA256.encrypt(pwd));
			pstmt.setString(2, userid);
			n = pstmt.executeUpdate();

		} finally {
			close();
		}
		
		return n;
	}

	
//	#마이페이지
	
//	1) 마이페이지 메인; 비밀번호 확인
	@Override
	public int memberPwdCheck(String userid, String pwd) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select mnum "
					+ "	from member "
					+ "	where userid=? and pwd=? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, SHA256.encrypt(pwd));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
				result = rs.getInt("mnum");
			
			}
			else {
				result = 0;
			}
		
		} finally {
			close();
		}
		return result;
	}

	
//	보유쿠폰 개수 구하기
	@Override
	public int getMyCouponCnt(String userid) throws SQLException {
		int cpcnt = 0;
		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) as cnt "
					+ " from my_coupon "
					+ " where fk_userid = ? and cpexpiredate>sysdate ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			cpcnt = rs.getInt("cnt");
			
		} finally {
			close();
		}
		
		
		return cpcnt;
		
	}
	
//	2) mnum으로 회원 1명의 정보를 select하는 메소드; 회원정보 수정
	@Override
	public MemberVO getOneMemberBymnum(String mnum) throws SQLException {
		MemberVO membervo = null;
		try {
			conn = ds.getConnection();
			String sql = " select mnum, userid, name, email, phone, to_char(birthday, 'yyyymmdd') as birthday, postnum "+
						 " ,address1, address2, point, to_char(registerdate, 'yyyymmdd') as  registerdate"+
					     " ,summoney ,fk_lvnum"+
					     " from member "+
					     " where status = 1 "+
					     " and mnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mnum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int v_mnum = rs.getInt("mnum");
				String userid = rs.getString("USERID");
				String name = rs.getString("NAME");
				String email = aes.decrypt(rs.getString("EMAIL"));  // AES256 복호화	
				String phone = aes.decrypt(rs.getString("phone"));	// AES256 복호화		
				String postnum = rs.getString("postnum");
				String address1 = rs.getString("address1");
				String address2 = rs.getString("address2");
				String birthday = rs.getString("birthday");
				int point = rs.getInt("point");
				String registerdate = rs.getString("registerdate");
				int summoney = rs.getInt("summoney");
				int fk_lvnum = rs.getInt("fk_lvnum");

				membervo = new MemberVO();
				
				membervo.setMnum(v_mnum);
				membervo.setName(name);
				membervo.setUserid(userid);
				membervo.setEmail(email);
				membervo.setPhone(phone);
				membervo.setBirthday(birthday);
				membervo.setPostnum(postnum);
				membervo.setAddress1(address1);
				membervo.setAddress2(address2);
				membervo.setPoint(point);
				membervo.setRegisterdate(registerdate);
				membervo.setSummoney(summoney);
				membervo.setFk_lvnum(fk_lvnum);
	
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();	
		}
		return membervo;
	}

	@Override
	public int updateMemberMyInfo(MemberVO membervo) throws SQLException {
		int result = 0;

		try {
			conn = ds.getConnection();
			String sql = " update member set pwd=?, name= ?, email=?, phone=?, "+
						 " postnum=?,address1=?, address2=?, last_changepwdate=sysdate"+
						 " where mnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			System.out.println("mnum: "+membervo.getMnum());
			pstmt.setString(1, SHA256.encrypt(membervo.getPwd()));	// SHA256 단방향 암호화
			pstmt.setString(2, membervo.getName());
			pstmt.setString(3, aes.encrypt(membervo.getEmail()));	// AES256 양방향 암호화
			pstmt.setString(4, aes.encrypt(membervo.getPhone()));	// AES256 양방향 암호화
			pstmt.setString(5, membervo.getPostnum());
			pstmt.setString(6, membervo.getAddress1());
			pstmt.setString(7, membervo.getAddress2());
			pstmt.setInt(8, membervo.getMnum());
			
			result = pstmt.executeUpdate();
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
	
	
//	#검색 및 날짜구간이 있는 총 회원수 구하는 메소드
	@Override
	public int getTotalCount(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from member "+
						 " where 1=1 ";
		
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) { // period가 전체(-1)일 떄 sql구문 그대로 실행
				sql+=" and "+ searchType + " like '%'||?||'%' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
			}
			else { // period가 전체(-1)설정이 아닐 때 기존 sql구문에 조건절 추가
				
				sql += " and "+ searchType + " like '%'||?||'%' "
						+"and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
			}
	
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {

		} finally {
			close();	
		}
		return count;
	}
	
//	#검색 및 날짜구간이 있고, 일반회원이 볼 수 있는 총 회원수 구하기
	@Override
	public int getTotalCountMember(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from jsp_member "+
						 " where status=1 "
						 + "and  MONTHS_BETWEEN (add_months(sysdate, -6), lastlogindate ) < 12 ";
		
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) { // period가 전체(-1)일 떄 sql구문 그대로 실행
				sql+=" and "+ searchType + " like '%'||?||'%' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
			}
			else { // period가 전체(-1)설정이 아닐 때 기존 sql구문에 조건절 추가
				
				sql += " and "+ searchType + " like '%'||?||'%' "
						+"and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
			}
	
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {

		} finally {
			close();	
		}
		return count;
	}
	
//	#페이징 처리가 완료된 상태에서 날짜구간기능까지 포함하여 memberList에 전체회원을 넣어주는 메소드
	@Override
	public List<MemberVO> getAllMember(int sizePerPage, int currentShowPageNo, String searchType, String searchWord,int period) throws SQLException {
		List<MemberVO> memberList = null;
		
		try {
			conn = ds.getConnection();
			String sql = "select   RNO,\n"+
					"              IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"              POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"              COIN, POINT, REGISTERDAY, STATUS, lastlogindate, lastPwdChangeDate,  lastlogingap "+
					"from\n"+
					"(\n"+
					"    select rownum AS RNO,\n"+
					"              IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"              POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"              COIN, POINT, REGISTERDAY, STATUS, lastlogindate, lastPwdChangeDate, lastlogingap\n"+
					"    from\n"+
					"        (\n"+
					"        select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"                  POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"                  COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS "+
					"				, lastlogindate, lastPwdChangeDate" +
					"				, trunc( months_between(sysdate, lastlogindate) ) as lastlogingap "+
					"        from jsp_member\n"+
					"        where 1=1 "+
							" and " + searchType + " like '%'||?||'%' ";

			String sql2 =	"        order by idx desc\n"+
							"        ) V\n"+
							") T\n"+
							"where T.RNO between ? and ?  ";  
			
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) {
				sql += sql2;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
				pstmt.setInt(3, (currentShowPageNo*sizePerPage));
				
			}
			else {
				sql += 	" and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? "+
						sql2;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
				pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
				pstmt.setInt(4, (currentShowPageNo*sizePerPage));
			}

			rs = pstmt.executeQuery();
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1)
					memberList = new ArrayList<MemberVO>();
				
				int idx = rs.getInt("IDX");
				String userid = rs.getString("USERID");
				String name = rs.getString("NAME");
				String pwd = rs.getString("PWD");
				String email = aes.decrypt(rs.getString("EMAIL"));  // AES256 복호화	
				
				String hp1 = rs.getString("HP1");
				String hp2 = aes.decrypt(rs.getString("HP2"));	// AES256 복호화
				String hp3 = aes.decrypt(rs.getString("HP3"));	// AES256 복호화
						
				String post1 = rs.getString("POST1");
				String post2 = rs.getString("POST2");
				String addr1 = rs.getString("ADDR1");
				String addr2 = rs.getString("ADDR2");
				String gender = rs.getString("GENDER");
				String birthday = rs.getString("BIRTHDAY");
				int coin = rs.getInt("COIN");
				int point = rs.getInt("POINT");
				String registerday = rs.getString("REGISTERDAY");
				int status = rs.getInt("STATUS");
				int lastlogingap = rs.getInt("lastlogingap");
				String lastlogindate = rs.getString("lastlogindate");
				String lastPwdChangeDate = rs.getString("lastPwdChangeDate");
				
				boolean requireCertify = false;
				if(lastlogingap>=12) { // 휴면계정일 때
					requireCertify = true;
				}
				// MemberVO 생성자; VO객체를 만들어서 회원정보를 담아줌
				MemberVO membervo = new MemberVO(idx, userid, name, pwd, email, hp1, hp2, hp3,
						post1, post2, addr1, addr2, gender,
						birthday.substring(0, 4), birthday.substring(4, 6), birthday.substring(6), birthday,
						coin, point, registerday, status, requireCertify, lastlogindate, lastPwdChangeDate) ;
				// 가입된 회원수 만큼 객체가 리스트업
				memberList.add(membervo);
			} // end of while
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();	
		}
		return memberList;
	}
	
	
	
	
	
}
