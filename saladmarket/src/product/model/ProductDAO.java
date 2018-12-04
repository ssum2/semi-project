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
	
	
	
//	#카테고리 코드로 물품 리스트를 select하는 메소드(패키지O 이미지 O)
	@Override
	public List<ProductVO> selectProductListBySdname(String fk_sdname) throws SQLException {
		List<ProductVO> productList = null;
		
		try {
			 conn = ds.getConnection();
			 
			 String sql = "select pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, \n"+
					 "       price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate, titleimg, \n"+
					 "	   pacnum, pacname, paccontents, pacimage,\n"+
					 "	   pimgfilename, fk_pnum\n"+
					 "from\n"+
					 "( select *\n"+
					 "from view_product_by_package union all select * from view_product_non_package )\n"+
					 "where fk_sdname like '%'||?||'%'";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, fk_sdname);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<ProductVO>();
				 }
				 
				 
				 String pnum = rs.getString("pnum");
				 String fk_pacname = rs.getString("fk_pacname");
				 String v_fk_sdname = rs.getString("fk_sdname");
				 String fk_ctname = rs.getString("fk_ctname");
				 String fk_stname = rs.getString("fk_stname");
				 String fk_etname = rs.getString("fk_etname");
				 
				 String pname = rs.getString("pname");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 int point = rs.getInt("point");
				 int pqty = rs.getInt("pqty");
				 String pcontents = rs.getString("pcontents");
				 String pcompanyname = rs.getString("pcompanyname");
				 
				 String pexpiredate = rs.getString("pexpiredate");
				 String allergy = rs.getString("allergy");
				 int weight = rs.getInt("weight");
				 int salecount = rs.getInt("salecount");
				 int plike = rs.getInt("plike");
				 String pdate = rs.getString("pdate");
				 String titleimg = rs.getString("titleimg");
				 
				 String pacnum = rs.getString("pacnum");
				 String pacname = rs.getString("pacname");
				 String paccontents = rs.getString("paccontents");
				 String pacimage = rs.getString("pacimage");
				 String pimgfilename = rs.getString("pimgfilename");
				 String fk_pnum = rs.getString("fk_pnum");
				 
				 ProductImageVO images = new ProductImageVO(pimgfilename, fk_pnum);
				 
				 PackageVO pac = new PackageVO(pacnum, pacname, paccontents, pacimage);
				 
				 ProductVO items = new ProductVO(pnum, fk_pacname, v_fk_sdname, fk_ctname, fk_stname, fk_etname, pname
						 			, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate
						 			, allergy, weight, salecount, plike, pdate, titleimg, images, pac);
				 
				 
				 
				
				 productList.add(items);
				 
			 }// end of while
			 
		} finally {
			close();
		}
		 
		return productList;
	}
}
