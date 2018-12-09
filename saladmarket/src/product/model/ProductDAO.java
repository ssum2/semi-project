package product.model;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

//	#admin; 카테고리 태그 리스트
	@Override
	public List<HashMap<String, String>> getCategoryTagList(String searchWord) throws SQLException {
		List<HashMap<String, String>> categoryTagList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select ctnum, ctname, sum(pqty) as pqty\n"+
					"from \n"+
					"(\n"+
					"select ctnum, ctname, pqty\n"+
					"from category_tag A left join product B\n"+
					"on ctname = fk_ctname\n"+
					") v\n"+
					" where ctname like '%'|| ? ||'%' "+
					" group by ctnum, ctname "
					+ " order by ctnum ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			rs = pstmt.executeQuery();
			
			int cnt=0;
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					categoryTagList = new ArrayList<HashMap<String, String>>();
				}
				
				String ctnum = rs.getString("ctnum");
				String ctname = rs.getString("ctname");
				String v_pqty = rs.getString("pqty");
				if(v_pqty==null) {
					v_pqty="0";
				}
				String pqty = v_pqty;
				
				System.out.println(pqty);
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ctnum", ctnum);
				map.put("ctname", ctname);
				map.put("pqty", pqty);
				
				
				categoryTagList.add(map);
			}
						
		} finally {
			close();
		}

		return categoryTagList;
	}

//	#admin; 카테고리태그 추가하기
	@Override
	public int addCategoryTag(String ctname) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql=" select count(*) as cnt from category_tag where ctname = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ctname);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			if(rs.getInt("cnt")>0) {
				return 0;
			}
			else {
				sql = " insert into category_tag(ctnum, ctname) values(seq_category_tag_ctnum.nextval, ? )";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ctname);
				
				result = pstmt.executeUpdate();
			}
		} finally {
			close();
		}
		
		return result;
	}
	
//	#admin; 카테고리태그 삭제하기
	@Override
	public int deleteCategoryTag(String ctnum) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();

			String sql = " delete from category_tag where ctnum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ctnum);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}
	
	
	
//	admin; 제품등록
//	1) 패키지 불러오기
	@Override
	public List<HashMap<String, String>> getPacnameList() 
			throws SQLException {
			
		List<HashMap<String, String>> pacnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pacnum, pacname"
					   + " from product_package  "
					   + " order by pacnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					pacnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String pacnum = rs.getString("pacnum"); 
			    String pacname = rs.getString("pacname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("pacnum", pacnum);
			    map.put("pacname", pacname);
			      
			    pacnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return pacnameList;
	}
	
//	2) 소분류명 불러오기
	@Override
	public List<HashMap<String, String>> getSdnameList() 
			throws SQLException {
			
		List<HashMap<String, String>> sdnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select sdnum, fk_ldname, sdname"
					   + " from small_detail  "
					   + " order by sdnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					sdnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String sdnum = rs.getString("sdnum"); 
			    String fk_ldname = rs.getString("fk_ldname"); 
			    String sdname = rs.getString("sdname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("sdnum", sdnum);
			    map.put("fk_ldname", fk_ldname);
			    map.put("sdname", sdname);
			      
			    sdnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return sdnameList;
	}
	
	
//	3) 카테고리명 불러오기
	@Override
	public List<HashMap<String, String>> getCtnameList() 
			throws SQLException {
			
		List<HashMap<String, String>> ctnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select ctnum, ctname"
					   + " from category_tag  "
					   + " order by ctnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					ctnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String ctnum = rs.getString("ctnum"); 
			    String ctname = rs.getString("ctname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("ctnum", ctnum);
			    map.put("ctname", ctname);
			      
			    ctnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return ctnameList;
	}
	
	
//	4) 스펙명 불러오기
	@Override
	public List<HashMap<String, String>> getStnameList() 
			throws SQLException {
			
		List<HashMap<String, String>> stnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select stnum, stname"
					   + " from spec_tag  "
					   + " order by stnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					stnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String stnum = rs.getString("stnum"); 
			    String stname = rs.getString("stname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("stnum", stnum);
			    map.put("stname", stname);
			      
			    stnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return stnameList;
	}
	
	
//	5) 이벤트태그 불러오기
	@Override
	public List<HashMap<String, String>> getEtnameList() 
			throws SQLException {
			
		List<HashMap<String, String>> etnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select etnum, etname, etimagefilename"
					   + " from event_tag  "
					   + " order by etnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					etnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String etnum = rs.getString("etnum"); 
			    String etname = rs.getString("etname"); 
			    String etimagefilename = rs.getString("etimagefilename"); 
			    
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("etnum", etnum);
			    map.put("etname", etname);
			    map.put("etimagefilename", etimagefilename);
			    
			    etnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return etnameList;
	}
	
	
//	#제품번호(시퀀스) 채번 하는 메소드
	@Override
	public int getPnumOfProduct() throws SQLException {
		int pnum = 0;
		
		try {
			conn = ds.getConnection();
			String sql = " select seq_product_pnum.nextval as seq"
						+ " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			pnum = rs.getInt("seq");
			
		} finally {
			close();
		}
		return pnum;
	}


//	#제품등록 insert 메소드
	@Override
	public int productInsert(ProductVO pvo) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = "insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, \n"+
					"                    pname, price, saleprice, point, pqty, pcontents, pcompanyname,\n"+
					"                    pexpiredate, allergy, weight, pdate, titleimg)\n"+
					"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?)";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, pvo.getPnum());
			pstmt.setString(2, pvo.getFk_pacname());
			pstmt.setString(3, pvo.getFk_sdname());
			pstmt.setString(4, pvo.getFk_ctname());
			pstmt.setString(5, pvo.getFk_stname());
			pstmt.setString(6, pvo.getFk_etname());
			
			pstmt.setString(7, pvo.getPname());
			pstmt.setInt(8, pvo.getPrice());
			pstmt.setInt(9, pvo.getSaleprice());
			pstmt.setInt(10, pvo.getPoint());
			pstmt.setInt(11, pvo.getPqty());
			pstmt.setString(12, pvo.getPcontents());
			pstmt.setString(13, pvo.getPcompanyname());
			pstmt.setString(14, pvo.getPexpiredate());
			pstmt.setString(15, pvo.getAllergy());
			pstmt.setInt(16, pvo.getWeight());
			pstmt.setString(17, pvo.getTitleimg());
			
			
			n = pstmt.executeUpdate();

		} finally {
			close();
		}
		return n;
	}

//	#제품 이미지정보를 product_images 테이블에 insert하는 메소드
	@Override
	public int product_images_Insert(int pnum, String attachFilename) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "insert into product_images(pimgnum, pimgfilename, fk_pnum)\n"+
						"values(seq_product_images_pimgnum.nextval, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);

			
			pstmt.setString(1, attachFilename);
			pstmt.setInt(2, pnum);
			
			result = pstmt.executeUpdate();

		} finally {
			close();
		}
		return result;
	}

//	admin; 물품목록
	@Override
	public List<HashMap<String, String>> getLdnameList() throws SQLException {
		List<HashMap<String, String>> ldnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select ldnum, ldname"
					   + " from large_detail  "
					   + " order by ldnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					ldnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String ldnum = rs.getString("ldnum"); 
			    String ldname = rs.getString("ldname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("ldnum", ldnum);
			    map.put("ldname", ldname);
			      
			    ldnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return ldnameList;
	}
	
	
	
	
//	#대분류명에 따른 소분류명 목록 가져오기
	@Override
	public List<HashMap<String, String>> getSdnameListByLdname(String ldname) throws SQLException {
		List<HashMap<String, String>> sdnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select sdnum, fk_ldname, sdname"
					   + " from small_detail  "
					   + " where fk_ldname like '%'|| ? || '%' "
					   + " order by sdnum asc ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, ldname);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					sdnameList = new ArrayList<HashMap<String, String>>();
				}
				
				String sdnum = rs.getString("sdnum"); 
			    String fk_ldname = rs.getString("fk_ldname"); 
			    String sdname = rs.getString("sdname"); 
			    
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("sdnum", sdnum);
			    map.put("fk_ldname", fk_ldname);
			    map.put("sdname", sdname);
			      
			    sdnameList.add(map);  
			}
			
		} finally{
			close();
		}
		
		return sdnameList;
	}
	
	
	
}
