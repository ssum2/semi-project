package product.model;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.AES256;
import util.MyKey;

public class ProductDAO implements InterProductDAO {
//	#아파치톰캣이 제공하는 DBCP 객체변수 ds 생성 
	private DataSource ds = null; // import javax.sql.DataSource
	
//	#Connection, preparedStatement, ResultSet객체 생성
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
//	#암호화
	AES256 aes = null;

//	#MemberDAO 기본생성자
	public ProductDAO() {
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
	
	
	
	
//	#카테고리 코드로 물품 리스트를 select하는 메소드
	@Override
	public List<ProductVO> selectProductListBySdname(String fk_sdname) throws SQLException {
		List<ProductVO> productList = null;
		
		try {
			 conn = ds.getConnection();
			 
			 String sql = "select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point\n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_product\n"+
					 "where pcategory_fk = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, fk_sdname);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<ProductVO>();
				 }
				 
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String v_pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 ProductVO productvo = new ProductVO(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
				
				 productList.add(productvo);
				 
			 }// end of while-----------------------
			 
		} finally {
			close();
		}
		 
		return productList;
	}
}
