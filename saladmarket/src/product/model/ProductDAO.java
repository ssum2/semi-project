package product.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
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
			
			String sql = "select ctnum, ctname, count(pqty) as pqty\n"+
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
	
//	#물품 목록 가져오기(where 조건절 마다 메소드 분리)
	@Override
	public List<ProductVO> getProductListAdmin(int sizePerPage, int currentShowPageNo) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc ";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}
	
	
	@Override
	public List<ProductVO> getProductListByDname(int sizePerPage, int currentShowPageNo, String fk_name, String dname) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					"where 1=1 and "+fk_name+" like '%'|| ? || '%'\n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dname);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}
	

	
	@Override
	public List<ProductVO> getProductListBySearch(int sizePerPage, int currentShowPageNo, String searchType, String searchWord) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					"where 1=1 and "+searchType+" like '%'|| ? || '%'\n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}
	
	@Override
	public List<ProductVO> getProductListBySearchWithDname(int sizePerPage, int currentShowPageNo
			, String fk_name, String dname, String searchType, String searchWord) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					"where 1=1 and "+fk_name+" like '%'|| ? || '%'\n"+
					"      and "+ searchType+" like '%'|| ? || '%' \n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dname);
			pstmt.setString(2, searchWord);
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}

	@Override
	public List<ProductVO> getProductListBySearchAll(int sizePerPage, int currentShowPageNo, String searchWord) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					"where 1=1 and ( pname like '%'|| ? || '%' \n"+
					"    or fk_pacname like '%'|| ? || '%') \n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc";
			 
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			pstmt.setString(2, searchWord);
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}
	
	@Override
	public List<ProductVO> getProductListBySearchAllWithDname(int sizePerPage, int currentShowPageNo, String fk_name, String dname, String searchWord) throws SQLException{
		List<ProductVO> productList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, pnum, fk_pacname, fk_sdname, fk_ldname, pname, saleprice, point, pqty, salecount, plike, pdate, titleimg \n"+
					"from view_product_join_sd\n"+
					"where 1=1 and "+fk_name+" like '%'|| ? || '%'\n"+
					"      and ( pname like '%'|| ? || '%' \n"+
					"      or fk_pacname like '%'|| ? || '%')\n"+
					") V\n"+
					"where V.rno between ? and ? \n"+
					"order by rno asc";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dname);
			pstmt.setString(2, searchWord);
			pstmt.setString(3, searchWord);
			
			pstmt.setInt(4, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(5, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String pnum = rs.getString("pnum");
				String fk_pacname = rs.getString("fk_pacname");
				String fk_sdname = rs.getString("fk_sdname");
				String fk_ldname = rs.getString("fk_ldname");
				String pname = rs.getString("pname");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				String titleimg = rs.getString("titleimg");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setFk_sdname(fk_sdname);
				pvo.setFk_ldname(fk_ldname);
				pvo.setPname(pname);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPqty(pqty);
				pvo.setSalecount(salecount);
				pvo.setPlike(plike);
				pvo.setPdate(pdate);
				pvo.setTitleimg(titleimg);
				
				productList.add(pvo);
			}
		} finally {
			close();
		}
		return productList;
	}
	
	
	
	
	
	@Override
	public int getTotalProductCountAll(String searchWord, String ldname, String sdname) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "select count(*) as cnt\n"+
					"from view_product_join_sd\n"+
					"where 1=1 "+
					" and (fk_pacname like '%'|| ? ||'%'\n"+
					" or pname like '%'|| ? ||'%')\n"+
					"and fk_ldname like '%'|| ? ||'%'"+
					"and fk_sdname like '%'|| ? ||'%'";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			pstmt.setString(2, searchWord);
			pstmt.setString(3, ldname);
			pstmt.setString(4, sdname);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		}  finally {
			close();	
		}
		return count;
	}
	
	
	@Override
	public int getTotalProductCount(String searchType, String searchWord, String ldname, String sdname) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "select count(*) as cnt\n"+
					"from view_product_join_sd\n"+
					"where 1=1 "+
					" and "+ searchType +" like '%'|| ? ||'%'\n"+
					" and fk_ldname like '%'|| ? ||'%' \n"+
					" and fk_sdname like '%'|| ? ||'%' ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			pstmt.setString(2, ldname);
			pstmt.setString(3, sdname);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		}  finally {
			close();	
		}
		return count;
	}
	
	
	
//	#admin; 상품디테일
	@Override
	public ProductVO getOneProductDetail(String pnum) throws SQLException {
		ProductVO pvo = null;
		
		try {
			conn = ds.getConnection();
			
			
			String sql = "select pnum, fk_pacname, fk_sdname, fk_ldname, fk_ctname, fk_stname, fk_etname\n"+
					", pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate \n"+
					", allergy, weight, salecount, plike, to_char(pdate, 'yyyymmdd') as pdate, titleimg\n"+
					"from view_product_join_sd\n"+
					"where pnum = ?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			String v_pnum = rs.getString("pnum");
			String fk_pacname = rs.getString("fk_pacname");
			String fk_ldname = rs.getString("fk_ldname");
			String fk_sdname = rs.getString("fk_sdname");
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
			
			
			
			pvo = new ProductVO();
			
			pvo.setPnum(v_pnum);
			pvo.setFk_pacname(fk_pacname);
			pvo.setFk_ldname(fk_ldname);
			pvo.setFk_sdname(fk_sdname);
			pvo.setFk_ctname(fk_ctname);
			pvo.setFk_stname(fk_stname);
			pvo.setFk_etname(fk_etname);
			
			pvo.setPname(pname);
			pvo.setPrice(price);
			pvo.setSaleprice(saleprice);
			pvo.setPoint(point);
			pvo.setPqty(pqty);
			pvo.setPcontents(pcontents);
			pvo.setPcompanyname(pcompanyname);
			pvo.setPexpiredate(pexpiredate);
			pvo.setAllergy(allergy);
			pvo.setWeight(weight);
			pvo.setSalecount(salecount);
			pvo.setPlike(plike);
			pvo.setPdate(pdate);
			pvo.setTitleimg(titleimg);
			
			
		} finally {
			close();
		}
		
		return pvo;

	}
	
	
//	#admin; 상품 상세; 추가이미지 가져오기
	@Override
	public List<HashMap<String, String>> getAttachImgList(String pnum) throws SQLException{
		List<HashMap<String, String>> imgList =null;
		
		try {
			conn = ds.getConnection();
			
			
			String sql = " select pimgnum, pimgfilename "+
					" from product_images "+
					" where fk_pnum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					imgList = new ArrayList<HashMap<String, String>>();
				}
				HashMap<String, String> map = new HashMap<String, String>();
				
				String pimgnum = rs.getString("pimgnum");
				String pimgfilename = rs.getString("pimgfilename");
				
				
				map.put("pimgnum", pimgnum);
				map.put("pimgfilename",pimgfilename);
				
				imgList.add(map);
			}
			
		} finally {
			close();
		}
		return imgList;
	}

//	#상품수정; 추가이미지 삭제
	@Override
	public int deleteAttachProductImg(String pimgnum) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();

			String sql = " delete from product_images where pimgnum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pimgnum);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
	
		return result;
	}

	
	@Override
	public int updateProduct(ProductVO pvo) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			
			
			String sql = " update product set fk_pacname=?, fk_sdname=?, fk_ctname=?, fk_stname=?, fk_etname=?"
					+ "		, pname=?, price=?, saleprice=?, point=?, pqty=?, pcontents=?, pcompanyname=?, pexpiredate=?, allergy=?, weight=?, pdate=sysdate";
			if(pvo.getTitleimg()!="") {
				sql+=", titleimg=? ";
			}
					sql+=" where pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, pvo.getFk_pacname());
			pstmt.setString(2, pvo.getFk_sdname());
			pstmt.setString(3, pvo.getFk_ctname());
			pstmt.setString(4, pvo.getFk_stname());
			pstmt.setString(5, pvo.getFk_etname());
			pstmt.setString(6, pvo.getPname());
			pstmt.setInt(7, pvo.getPrice());
			pstmt.setInt(8, pvo.getSaleprice());
			pstmt.setInt(9, pvo.getPoint());
			pstmt.setInt(10, pvo.getPqty());
			pstmt.setString(11, pvo.getPcontents());
			pstmt.setString(12, pvo.getPcompanyname());
			pstmt.setString(13, pvo.getPexpiredate());
			pstmt.setString(14, pvo.getAllergy());
			pstmt.setInt(15, pvo.getWeight());
			if(pvo.getTitleimg()!="") {
				pstmt.setString(16, pvo.getTitleimg());
				pstmt.setString(17, pvo.getPnum());
			}
			else{
				pstmt.setString(16, pvo.getPnum());
			}
			result = pstmt.executeUpdate();
		} finally {
			close();
		}	
		return result;
	}
	
//	#상품 삭제
	@Override
	public int deleteProductByPnum(String pnum) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();

			String sql = " delete from product where pnum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
	
		return result;
	}

	@Override
	public int updateCtname(String ctnum, String ctname) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			
			
			String sql = " update category_tag set ctname=? where ctnum = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ctname);
			pstmt.setString(2, ctnum);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}	
		return result;
	}

	
//	#이벤트 태그 리스트 불러오기
	@Override
	public List<HashMap<String, String>> getEnvetTagList(String searchWord) throws SQLException {
		List<HashMap<String, String>> eventTagList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select etnum, etname, etimagefilename, count(pnum) as cnt\n"+
					"from \n"+
					"(\n"+
					"select etnum, etname, etimagefilename, pnum\n"+
					"from event_tag A left join product B\n"+
					"on etname = fk_etname\n"+
					") v\n"+
					" where etname like '%'|| ? ||'%' "+
					" group by etnum, etname, etimagefilename "
					+ " order by etnum ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			rs = pstmt.executeQuery();
			
			int n=0;
			while(rs.next()) {
				n++;
				if(n==1) {
					eventTagList = new ArrayList<HashMap<String, String>>();
				}
				
				String etnum = rs.getString("etnum");
				String etname = rs.getString("etname");
				String v_cnt = rs.getString("cnt");
				String etimagefilename = rs.getString("etimagefilename");
				if(v_cnt==null) {
					v_cnt="0";
				}
				
				String cnt = v_cnt;
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("etnum", etnum);
				map.put("etname", etname);
				map.put("etimagefilename",etimagefilename);
				map.put("cnt", cnt);
				
				eventTagList.add(map);
			}
						
		} finally {
			close();
		}

		return eventTagList;
	}

//	#이벤트 수정; 이벤트태그 정보 1개 가져오기
	@Override
	public HashMap<String, String> getOneEventTag(String etnum) throws SQLException {
		HashMap<String, String> map = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select etnum, etname, etimagefilename from event_tag where etnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, etnum);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				map = new HashMap<String, String>();
				
				map.put("etnum", rs.getString("etnum"));
				map.put("etname", rs.getString("etname"));
				map.put("etimagefilename", rs.getString("etimagefilename"));
			}
			
		} finally {
			close();
		}

		return map;
	}
	
//	#이벤트태그 수정
	@Override
	public int updateEventTag(HashMap<String, String> map) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = " update event_tag set etname=?";
			if(map.get("etimagefilename")!="") {
				sql+=", etimagefilename=? ";
			}
					sql+=" where etnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, map.get("etname"));
			
			if(map.get("etimagefilename")!="") {
				pstmt.setString(2, map.get("etimagefilename"));
				pstmt.setString(3, map.get("etnum"));
			}
			else{
				pstmt.setString(2, map.get("etnum"));
			}
			result = pstmt.executeUpdate();
		} finally {
			close();
		}	
		return result;
	}
	
//	#이벤트태그 시퀀스 채번 하는 메소드
	@Override
	public int getEtnum() throws SQLException {
		int etnum = 0;
		
		try {
			conn = ds.getConnection();
			String sql = " select seq_event_tag_etnum.nextval as seq"
						+ " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			etnum = rs.getInt("seq");
			
		} finally {
			close();
		}
		return etnum;
	}
	
//	#이벤트태그 추가하기
	@Override
	public int insertEventTag(HashMap<String, String> map) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = " insert into event_tag(etnum, etname, etimagefilename) values(?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, map.get("etnum"));
			pstmt.setString(2, map.get("etname"));
			pstmt.setString(3, map.get("etimagefilename"));
			
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}	
		return result;
	}

//	#이벤트 태그 삭제하기
	@Override
	public int deleteEventTagByEtnum(String etnum) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();

			String sql = " delete from event_tag where etnum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, etnum);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
	
		return result;
	}

	
	
//	#패키지 목록 불러오기
	@Override
	public List<PackageVO> getPackageList(int sizePerPage, int currentShowPageNo, String searchWord)
			throws SQLException {
		List<PackageVO> packageList = null;
		conn = ds.getConnection();
		
		try {
			String sql = "select rno, pacnum, pacname, pacimage, cnt\n"+
					"from\n"+
					"(\n"+
					"    select rownum as rno, pacnum, pacname, pacimage, cnt\n"+
					"    from\n"+
					"    (\n"+
					"        select pacnum, pacname, pacimage, count(pnum) as cnt\n"+
					"        from \n"+
					"        (\n"+
					"            select pacnum, pacname, pacimage, pnum\n"+
					"            from product_package A left join product B\n"+
					"            on pacname = fk_pacname\n"+
					"        ) v\n"+
					"        where pacname like '%'|| ? ||'%'\n"+
					"        group by pacnum, pacname, pacimage\n"+
					"        order by pacnum desc \n"+
					"    ) T\n"+
					") N\n"+
					"where N.rno between ? and ?";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();
			
			int n = 0;
			
			while(rs.next()) {
				n++;
				if(n==1) {
					packageList = new ArrayList<PackageVO>();
				}
				
				String pacnum = rs.getString("pacnum");
				String pacname = rs.getString("pacname");
				String pacimage = rs.getString("pacimage");
				String cnt = rs.getString("cnt");

				PackageVO pacvo = new PackageVO();
				
				pacvo.setPacnum(pacnum);
				pacvo.setPacname(pacname);
				pacvo.setPacimage(pacimage);
				pacvo.setCnt(cnt);

				packageList.add(pacvo);
			}
		} finally {
			close();
		}
		return packageList;
	}

//	#패키지 개수 가져오기
	@Override
	public int getTotalPackageCount(String searchWord) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "select count(*) as cnt\n"+
					"from product_package\n"+
					"where 1=1 "+
					" and pacname like '%'|| ? ||'%'\n";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchWord);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		}  finally {
			close();	
		}
		return count;
	}
	
	
//	#패키지 시퀀스 채번 하는 메소드
	@Override
	public int getPacnum() throws SQLException {
		int pacnum = 0;
		
		try {
			conn = ds.getConnection();
			String sql = " select seq_product_Package_pacnum.nextval as seq"
						+ " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			pacnum = rs.getInt("seq");
			
		} finally {
			close();
		}
		return pacnum;
	}
	
	
//	#패키지 추가하기
	@Override
	public int insertPackage(PackageVO pacvo) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = " insert into product_package(pacnum, pacname, paccontents, pacimage) values(?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, pacvo.getPacnum());
			pstmt.setString(2, pacvo.getPacname());
			pstmt.setString(3, pacvo.getPaccontents());
			pstmt.setString(4, pacvo.getPacimage());
			
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}	
		return result;
	}

//	#패키지 수정하기
	@Override
	public int updatePackage(PackageVO pacvo) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " update product_package set pacname=?, paccontents=?";
			if(pacvo.getPacimage() != "") {
				sql+=", pacimage=? ";
			}
					sql+=" where pacnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, pacvo.getPacname());
			pstmt.setString(2, pacvo.getPaccontents());
			if(pacvo.getPacimage() != "") {
				pstmt.setString(3, pacvo.getPacimage());
				pstmt.setString(4, pacvo.getPacnum());
			}
			else{
				pstmt.setString(3, pacvo.getPacnum());
			}
			result = pstmt.executeUpdate();
		} finally {
			close();
		}	
		return result;
	}

//	#패키지 1개 정보 가져오기
	@Override
	public PackageVO getOnePackage(String pacnum) throws SQLException {
		PackageVO pacvo = null;
		try {
			conn = ds.getConnection();
			String sql = " select pacnum, pacname, paccontents, pacimage from product_package where pacnum = ?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, pacnum);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pacvo = new PackageVO();
				
				pacvo.setPacnum(rs.getString("pacnum"));
				pacvo.setPacname(rs.getString("pacname"));
				pacvo.setPaccontents(rs.getString("paccontents"));
				pacvo.setPacimage(rs.getString("pacimage"));
			}
			
		} finally {
			close();
		}	
		
		return pacvo;
	}

//	#패키지 삭제하기(물품 없음으로 바꾸고 패키지 딜리트)
	@Override
	public int deletePackageByPacnum(String pacnum) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = " update product set fk_pacname = '없음' "
					+ " where fk_pacname = (select pacname from product_package where pacnum = ? )";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			
			pstmt.executeUpdate();
			
			sql = " delete from product_package where pacnum = ? ";	
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			result = pstmt.executeUpdate();
			
			if(result == 1) {
				conn.commit();
				conn.setAutoCommit(true);
			}
			else {
				conn.rollback();
				conn.setAutoCommit(true);
			}
		} finally {
			close();
		}

		return result;
	}
	
	
// -----------------------------------------------------------------------------
//	[물품목록. 고은]
	   // *** 제품 리스트 (대분류, 소분류(있을때만), 스펙별, 정렬기준)를 불러오는 추상 메소드 *** //
	   @Override
	   public List<ProductVO> getProductList(String fk_ldname, String sdname, String orderby) throws SQLException {
	      
	      List<ProductVO> productList = null;
	      
	      try {

	         conn = ds.getConnection();
	         
	         String sql = "select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, stname\n"+
	               "from view_productList\n"+
	               "where sdname in (select sdname from small_detail where fk_ldname = ? and sdname like ?)\n"+
	               "order by ? desc";
	         
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, fk_ldname);
	         pstmt.setString(2, sdname);
	         pstmt.setString(3, orderby);
	         
	         rs = pstmt.executeQuery();
	         
	         int cnt = 0;
	         while(rs.next()) {
	            cnt++;
	            
	            if(cnt == 1) {
	               productList = new ArrayList<ProductVO>();
	            }
	            
	            String pacnum = rs.getString("pacnum");
	            String pnum = rs.getString("pnum");
	            String pacname = rs.getString("pacname");
	            int saleprice = rs.getInt("saleprice");
	            String pimgfilename = rs.getString("pacimage");
	            String v_stname = rs.getString("stname");
	            
	            ProductVO productvo = new ProductVO();
	            
	            productvo.setPnum(pnum);
	            productvo.setFk_pacname(pacname);
	            productvo.setPacnum(pacnum); 
	            productvo.setSaleprice(saleprice);
	            productvo.setPimgfilename(pimgfilename);
	            productvo.setFk_stname(v_stname);
	            
	            productList.add(productvo);
	            
	         }// end of while()-------------------------------------------
	         
	      } finally {
	         close();
	      }
	      
	      return productList;
	      
	   }
	
	
	@Override
	public int getCountByfk_ldnameNword(String fk_ldname, String searchword) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();

			String sql = "select count(*) as CNT\n"+
						"from view_productList\n"+
						"where sdname in (select sdname from small_detail where fk_ldname = ?)\n"+
						"and lower(pname) like '%' || lower(?) || '%'";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_ldname);
			pstmt.setString(2, searchword);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalCount = rs.getInt("CNT");
			
		} finally {
			close();
		}
		
		return totalCount;
		
	}


	@Override
	public int getCountBysdnameNword(String sdname, String searchword) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();
	
			String sql = "select count(*) as CNT\n"+
					"from view_productList\n"+
					"where sdname = ?\n"+
					"and lower(pname) like '%' || lower(?) || '%'";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sdname);
			pstmt.setString(2, searchword);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalCount = rs.getInt("CNT");
			
		} finally {
			close();
		}
		
		return totalCount;
		
	}


	@Override
	public List<HashMap<String, Object>> getContentListbyfk_ldname(String fk_ldname, int sizePerPage,
			int currentShowPageNo, String searchword, String orderby) throws SQLException {
	
		List<HashMap<String, Object>> mapList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select rno, pacnum, pacname, pnum, pacimage, saleprice, stname, price\n"+
						"from\n"+
						"(\n"+
						"select rownum as rno, pacnum, pacname, pnum, pacimage, saleprice, stname, pdate, plike, price\n"+
						"from\n"+
						"    (\n"+
						"    select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, stname, pdate, plike, price\n"+
						"    from view_productList\n"+
						"    where sdname in (select sdname from small_detail where fk_ldname = ?)\n"+
						"	 order by "+orderby+
						"    ) V\n"+
						"where lower(pacname) like '%' || lower(?) || '%'\n"+
						") T\n"+
						"where T.rno between ? and ?\n";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, fk_ldname);
			pstmt.setString(2, searchword);
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) ); // 공식!!!
			pstmt.setInt(4, (currentShowPageNo*sizePerPage) ); // 공식!!!
		    
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					mapList = new ArrayList<HashMap<String, Object>>();
				}
				
				String pacname = rs.getString("pacname"); 
				String pacnum = rs.getString("pacnum"); 
			    String pnum = rs.getString("pnum"); 
			    String pacimage = rs.getString("pacimage"); 
			    int saleprice = rs.getInt("saleprice");
			    String stname = rs.getString("stname");
			    int price = rs.getInt("price");
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("pacname", pacname);
			    map.put("pacnum", pacnum);
			    map.put("pnum", pnum);
			    map.put("pimgfilename", pacimage);
			    map.put("saleprice", saleprice);
			    map.put("stname", stname);
			    map.put("price", price);
			      
			    mapList.add(map);  
			    
			}// end of while
		} finally {
			close();
		}
		return mapList;
	}


	@Override
	public List<HashMap<String, Object>> getContentListbysdname(String sdname, int sizePerPage, int currentShowPageNo,
			String searchword, String orderby) throws SQLException {
	
		List<HashMap<String, Object>> mapList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select rno, pacnum, pacname, pnum, pacimage, saleprice, stname, price \n"+
						"from\n"+
						"(\n"+
						"select rownum as rno, pacnum, pacname, pnum, pacimage, saleprice, stname, pdate, plike, price\n"+
						"from\n"+
						"    (\n"+
						"    select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, stname, pdate, plike, price \n"+
						"    from view_productList\n"+
						"    where sdname = ?\n"+
						"    order by "+orderby+
						"    ) V\n"+
						"where lower(pacname) like '%' || lower(?) || '%'\n"+
						") T\n"+
						"where T.rno between ? and ?\n";

			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, sdname);
			pstmt.setString(2, searchword);
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) ); // 공식!!!
			pstmt.setInt(4, (currentShowPageNo*sizePerPage) ); // 공식!!!
		    
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					mapList = new ArrayList<HashMap<String, Object>>();
				}
				
				String pacname = rs.getString("pacname"); 
				String pacnum = rs.getString("pacnum"); 
			    String pnum = rs.getString("pnum"); 
			    String pacimage = rs.getString("pacimage"); 
			    int saleprice = rs.getInt("saleprice");
			    String stname = rs.getString("stname");
			    int price = rs.getInt("price");
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("pacname", pacname);
			    map.put("pacnum", pacnum);
			    map.put("pnum", pnum);
			    map.put("pimgfilename", pacimage);
			    map.put("saleprice", saleprice);
			    map.put("stname", stname);
			    map.put("price", price);
			      
			    mapList.add(map);  
			    
			}// end of while
		} finally {
			close();
		}
		return mapList;
	
	}
	
	
	
	
// 이벤트
// ** AJAX를 이용한 index에서 스펙대로 제품 리스트를 보여주는 추상 메소드
	@Override
	public List<ProductVO> getProductsByStnameAppend(String stname, int startRno, int endRno) throws SQLException {
		List<ProductVO> productList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select rnum, pacnum, pacname, paccontents, pacimage, pnum \n"+
					"        , sdname, ctname, stname, etname, pname, price\n"+
					"        , saleprice, point, pqty, pcontents\n"+
					"        , pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate \n"+
					" from \n"+
					" (\n"+
					"    select rownum as rnum,pacnum,  case when pacname = '없음' then pname else pacname end as pacname,paccontents, pacimage, pnum \n"+
					"            , sdname, ctname, stname, etname, pname, price \n"+
					"            , saleprice, point, pqty, pcontents \n"+
					"            , pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate \n"+
					"    from view_event_product \n"+
					"        where stname = ? \n"+
					"        order by rnum asc, pname asc \n"+
					" ) F where rnum between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, stname);
			pstmt.setInt(2, startRno);
			pstmt.setInt(3, endRno);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					productList = new ArrayList<ProductVO>();
				}
				
			     int rnum = rs.getInt("rnum");
				 String pnum = rs.getString("pacnum");
				 String pacname = rs.getString("pacname");
				 String paccontents = rs.getString("paccontents");
				 String pacimage = rs.getString("pacimage");
				 String ctname = rs.getString("ctname");
				 String sdname = rs.getString("sdname");
				 int price = rs.getInt("price");
				 int plike = rs.getInt("plike");
				 int saleprice = rs.getInt("saleprice");
				 
				 ProductVO pvo = new ProductVO();
				 pvo.setRnum(rnum);
				 pvo.setPnum(pnum);
				 pvo.setFk_pacname(pacname);
				 pvo.setPaccontents(paccontents);
				 pvo.setPacimage(pacimage);
				 pvo.setFk_ctname(ctname);
				 pvo.setFk_sdname(sdname);
				 pvo.setPrice(price);
				 pvo.setPlike(plike);
				 pvo.setSaleprice(saleprice);
				 
				 productList.add(pvo);				 
				
			} // end of while-------------------
						
		} finally {
			close();
		}
		
		return productList;	
	}
	
//	장바구니
	// *** product, small_detai, product_images, product_package 테이블에서 상품리스트 정보
	@Override
	public List<HashMap<String, Object>> getProductListInfo() throws SQLException {
		
		List<HashMap<String, Object>> productList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select rno, pnum, fk_pacname, V.sdnum, V.fk_ldname, fk_sdname, fk_ctname, fk_stname, fk_etname "
				       + " , pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate "
				       + " , V.pimgnum, V.pimgfilename, T.pacnum, T.paccontents, T.pacimage "
				       + " from "
				       + " ( "
				       + " 		select rno, pnum, fk_pacname, C.sdnum, C.fk_ldname, fk_sdname, fk_ctname, fk_stname, fk_etname "
				       + " 			 , pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate "
				       + "			 , D.pimgnum, D.pimgfilename "
				       + " 		from "
				       + " 		( "
				       + " 			select row_number() over(order by pnum desc) AS RNO, pnum, fk_pacname, B.sdnum, B.fk_ldname, fk_sdname, fk_ctname, fk_stname, fk_etname "
				       + " 			     , pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate "
				       + " 			from product A JOIN small_detail B "
				       + " 			on A.fk_sdname=B.sdname "
				       + " 		) C JOIN product_images D "
				       + " 		  on C.pnum = D.fk_pnum "
				       + " ) V JOIN product_package T "
				       + " on V.fk_pacname = T.pacname ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				if(cnt==1) {
					productList = new ArrayList<HashMap<String, Object>>();
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rno", rs.getInt("rno"));						// 전체번호 총 81
				map.put("pnum", rs.getInt("pnum"));						// 제품번호
				map.put("pacname", rs.getString("fk_pacname"));			// 패키지명 
				map.put("sdnum", rs.getString("sdnum"));				// 소분류 번호 
				map.put("ldname", rs.getString("fk_ldname"));			// 대분류명 
				map.put("sdname", rs.getString("fk_sdname"));			// 소분류명 
				map.put("ctname", rs.getString("fk_ctname"));			// 카테고리테그명
				map.put("stname", rs.getString("fk_stname"));			// 스펙태그명 
				map.put("etname", rs.getString("fk_etname"));			// 이벤트태그명
				map.put("pname", rs.getString("pname"));				// 상품명 
				map.put("price", rs.getInt("price"));					// 원가 	
				map.put("saleprice", rs.getInt("saleprice"));			// 판매가 
				map.put("point", rs.getInt("point"));					// 포인트 
				map.put("pqty", rs.getInt("pqty"));						// 재고량 
				map.put("pcontents", rs.getString("pcontents"));		// 상세설명 
				map.put("pcompanyname", rs.getString("pcompanyname"));	// 회사명 
				map.put("pexpiredate", rs.getString("pexpiredate"));	// 유통기한 
				map.put("allergy", rs.getString("allergy"));			// 알레르기 
				map.put("weight", rs.getString("weight"));				// 용량 
				map.put("salecount", rs.getInt("salecount"));			// 판매량 
				map.put("plike", rs.getInt("plike"));					// 좋아요 
				map.put("pimgnum", rs.getInt("pimgnum"));				// 이미지번호
				map.put("pimgfilename", rs.getString("pimgfilename"));	// 이미지파일이름 
				map.put("pacnum", rs.getInt("pacnum"));					// 패키지번호 
				map.put("paccontents", rs.getString("paccontents"));	// 패키지 상세설명 
				map.put("pacimage", rs.getString("pacimage"));			// 패키지 이미지
			
				productList.add(map);
			}
			
		} finally {
			close();
		}
		return productList;
	} // List<HashMap<String, Object>> getProductListInfo()


	// *** view_productList 뷰에서 스펙태그이름별(stname) 제품 리스트
	@Override
	public List<HashMap<String, Object>> getStnameList(String stname) throws SQLException {
		
		List<HashMap<String, Object>> stnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select rnum, pacnum, pacname, pacimage, pnum, stname"
					   + " 		, case when pacname = '없음' then pname else pacname end AS pname "
					   + "      , price, saleprice "
					   + " from view_productList "
					   + " where stname = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, stname);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				if(cnt==1) {
					stnameList = new ArrayList<HashMap<String, Object>>();
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rnum", rs.getString("rnum"));			// 제품번호
				map.put("pacnum", rs.getInt("pacnum"));			// 제품번호
				map.put("pacname", rs.getString("pacname"));		// 패키지명 
				map.put("pacimage", rs.getString("pacimage"));
				map.put("pnum", rs.getInt("pnum"));				// 대분류명 
				map.put("stname", rs.getString("stname"));			// 스펙태그명 
				map.put("pname", rs.getString("pname"));			// 상품명 
				map.put("price", rs.getInt("price"));				// 원가 	
				map.put("saleprice", rs.getInt("saleprice"));		// 판매가 
				
				stnameList.add(map);
			}
			
		} finally {
			close();
		}
		return stnameList;

	} // List<HashMap<String, Object>> getStnameList(String stname) --------------------------------


	// *** view_productList 안에 있는 패키지번호(pacnum)로 제품 상세
	@Override
	public HashMap<String, Object> getProductDetailPacnum(String pacnum) throws SQLException {
		
		HashMap<String, Object> map = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pacnum, case when pacname = '없음' then pname else pacname end AS pname"
					   + "		, pacname, paccontents, pacimage, pnum, sdname, price, saleprice, pqty, pcontents "
					   + " , pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate "
					   + " from view_productList "
					   + " where pacnum = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			rs = pstmt.executeQuery();
			
		
			if(rs.next()) {

				int v_pacnum = rs.getInt("pacnum");
				String pname = rs.getString("pname");
				String pacname = rs.getString("pacname");
				String paccontents = rs.getString("paccontents");
				String pacimage = rs.getString("pacimage");
				int v_pnum = rs.getInt("pnum");
				String sdname = rs.getString("sdname");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				int pqty = rs.getInt("pqty");
				String pcontents = rs.getString("pcontents");
				String pcompanyname = rs.getString("pcompanyname");
				String pexpiredate = rs.getString("pexpiredate");
				String allergy = rs.getString("allergy");
				String weight = rs.getString("weight");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				
				map = new HashMap<String, Object>();
				
				map.put("pacnum", v_pacnum);
				map.put("pname", pname);
				map.put("pacname", pacname);
				map.put("paccontents", paccontents);
				map.put("pacimage", pacimage);
				map.put("pnum", v_pnum);
				map.put("sdname", sdname);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("pqty", pqty);
				map.put("pcontents", pcontents);
				map.put("pcompanyname", pcompanyname);
				map.put("pexpiredate", pexpiredate);
				map.put("allergy", allergy);
				map.put("weight", weight);
				map.put("salecount", salecount);
				map.put("plike", plike);
				map.put("pdate", pdate);
				
			}
			
		} finally {
			close();
		}
		return map;
		
	} // HashMap<String, Object> getProductDetailPacnum(String pacnum) ---------------------------

	
	// *** view_productList 안에 있는 패키지번호(pacnum)로 제품 상세
	@Override
	public HashMap<String, Object> getProductDetailPnum(String pnum) throws SQLException {
		
		HashMap<String, Object> map = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select fk_sdname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate "
					   + " from product "
					   + " where pnum = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			rs = pstmt.executeQuery();
			
		
			if(rs.next()) {

				String sdname = rs.getString("fk_sdname");
				String pname = rs.getString("pname");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				String pcontents = rs.getString("pcontents");
				String pcompanyname = rs.getString("pcompanyname");
				String pexpiredate = rs.getString("pexpiredate");
				String allergy = rs.getString("allergy");
				String weight = rs.getString("weight");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				
				map = new HashMap<String, Object>();
				
				map.put("sdname", sdname);
				map.put("pname", pname);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("point",point);
				map.put("pqty", pqty);
				map.put("pcontents", pcontents);
				map.put("pcompanyname", pcompanyname);
				map.put("pexpiredate", pexpiredate);
				map.put("allergy", allergy);
				map.put("weight", weight);
				map.put("salecount", salecount);
				map.put("plike", plike);
				map.put("pdate", pdate);
				
			}
			
		} finally {
			close();
		}
		return map;
		
	} // HashMap<String, Object> getProductDetailPnum(String pnum) ------------------------------


	// *** product_images 안에 있는 단품번호(pnum)로 제품이미지 
	@Override
	public List<HashMap<String, String>> getProductImagePnum(String pnum) throws SQLException {
		List<HashMap<String, String>> imgList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pimgfilename "
					   + " from product_images "
					   + " where fk_pnum = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				if(cnt == 1) {
					imgList = new ArrayList<HashMap<String, String>>();
				} 
				String pimgfilename = rs.getString("pimgfilename");
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("pimgfilename", pimgfilename);
				
				imgList.add(map);
				
			}
			
		} finally {
			close();
		}
		return imgList;
	} // List<HashMap<String, String>> getProductImagePnum(String pnum) ---------------------------------


	
	// *** view_productList 뷰에서 대분류별(ldname) 제품 리스트
	@Override
	public List<HashMap<String, Object>> getSdnameList(String sdname) throws SQLException {
		List<HashMap<String, Object>> sdnameList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select rnum, pacnum, pacname, pacimage, pnum, stname"
					   + " 		, case when pacname = '없음' then pname else pacname end AS pname "
					   + "      , price, saleprice "
					   + " from view_productList "
					   + " where sdname = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sdname);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				if(cnt==1) {
					sdnameList = new ArrayList<HashMap<String, Object>>();
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rnum", rs.getString("rnum"));			// 제품번호
				map.put("pacnum", rs.getInt("pacnum"));			// 제품번호
				map.put("pacname", rs.getString("pacname"));		// 패키지명 
				map.put("pacimage", rs.getString("pacimage"));
				map.put("pnum", rs.getInt("pnum"));				// 대분류명 
				map.put("stname", rs.getString("stname"));			// 스펙태그명 
				map.put("pname", rs.getString("pname"));			// 상품명 
				map.put("price", rs.getInt("price"));				// 원가 	
				map.put("saleprice", rs.getInt("saleprice"));		// 판매가 
				
				sdnameList.add(map);
			}
			
		} finally {
			close();
		}
		return sdnameList;
	} // List<HashMap<String, Object>> getLdnameList(String ldname) --------------------------------------


	// *** cart 테이블에서 로그인한 userid의 장바구니 리스트 
	@Override
	public List<HashMap<String, String>> getCartList(String userid) throws SQLException {
		List<HashMap<String, String>> cartList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cartno, fk_userid, fk_pnum, oqty, status, B.pname, B.price, B.saleprice, B.point, B.titleimg, C.pacname "
					   + " from product_cart A JOIN product B "
					   + " on A.fk_pnum=B.pnum "
					   + " JOIN product_package C " 
					   + " on B.fk_pacname= C.pacname "
					   + " where A.status=1 and fk_userid = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				if(cnt==1) {
					cartList = new ArrayList<HashMap<String, String>>();
				}
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("cartno", rs.getString("cartno"));			
				map.put("fk_userid", rs.getString("fk_userid"));	
				map.put("fk_pnum", rs.getString("fk_pnum"));	
				map.put("oqty", rs.getString("oqty"));			
				map.put("status", rs.getString("status"));
				map.put("pname", rs.getString("pname"));
				map.put("price", rs.getString("price"));
				map.put("saleprice", rs.getString("saleprice"));
				map.put("point", rs.getString("point"));
				map.put("titleimg", rs.getString("titleimg"));
				map.put("pacname", rs.getString("pacname"));
				
				
				cartList.add(map);
			}
			
		} finally {
			close();
		}
		return cartList;
	} // List<HashMap<String, String>> getCartList(String userid)
	

	
//	#admin; 주문목록 관리
	// **** 검색조건에 따른 배송상태 리스트 보기 **** //
	@Override
	public List<HashMap<String, String>> getDeliverList(String searchType, String searchWord, int currentShowPage,
			int sizePerPage) throws SQLException {
		List<HashMap<String, String>> deleverList = null;
		try {
			conn = ds.getConnection();
			if(!"".equals(searchWord)) {
				String sql = "select rno,odrdnum,odrcode,odrdate,fk_pnum,fk_userid,oqty,odrtotalprice,odrstatus, invoice, titleimg\n"+
						"from \n"+
						"(\n"+
						"    select row_number()over(order by B.odrdate desc)as rno,odrdnum,odrcode,B.odrdate as odrdate, fk_pnum, \n"+
						"           fk_userid, oqty, odrtotalprice,C.titleimg as titleimg, invoice,  \n"+
						"           case odrstatus \n"+
						"           when 0 then '주문완료' \n"+
						"			when 1 then '결제완료' "+
						"           when 2 then '배송중' \n"+
						"           when 3 then '배송완료' \n"+
						"           when 4 then '주문취소' \n"+
						"           else '교환환불' end odrstatus \n"+
						"    from product_order_detail A \n"+
						"    join product_order B\n"+
						"    on A.fk_odrcode = B.odrcode\n"+
						 "   join product C\r\n" + 
						"    on A.fk_pnum = C.pnum\n"+
						" where "+searchType+" like '%'|| ? ||'%' "+
						")V\n"+
						"where rno between ? and ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, (currentShowPage*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(3, (currentShowPage*sizePerPage));
			}
			else {
				String sql = "select rno,odrdnum,odrcode,odrdate,fk_pnum,fk_userid,oqty,odrtotalprice,odrstatus, invoice, titleimg\n"+
						"from \n"+
						"(\n"+
						"    select row_number()over(order by B.odrdate desc)as rno,odrdnum,odrcode,B.odrdate as odrdate, fk_pnum, \n"+
						"           fk_userid, oqty, odrtotalprice,C.titleimg as titleimg, invoice,\n"+
						"           case odrstatus \n"+
						"           when 0 then '주문완료' \n"+
						"			when 1 then '결제완료' "+
						"           when 2 then '배송중' \n"+
						"           when 3 then '배송완료' \n"+
						"           when 4 then '주문취소' \n"+
						"           else '교환환불' end odrstatus  \n"+
						"    from product_order_detail A \n"+
						"    join product_order B\n"+
						"    on A.fk_odrcode = B.odrcode"
						+ "  join product C\r\n" + 
						"    on A.fk_pnum = C.pnum\n"+
						")V\n"+
						"where rno between ? and ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (currentShowPage*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(2, (currentShowPage*sizePerPage));
			}
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					deleverList = new ArrayList<HashMap<String, String>>();
				}
				String odrdnum = rs.getString("odrdnum");
				String odrcode = rs.getString("odrcode");
				String odrdate = rs.getString("odrdate");
				String fk_pnum = rs.getString("fk_pnum");
				String fk_userid = rs.getString("fk_userid");
				String oqty = rs.getString("oqty");
				String odrtotalprice = rs.getString("odrtotalprice");
				String odrstatus = rs.getString("odrstatus");
				String invoice = rs.getString("invoice");
				String titleimg = rs.getString("titleimg");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("odrdnum", odrdnum);
				map.put("odrcode", odrcode);
				map.put("odrdate", odrdate);
				map.put("fk_pnum", fk_pnum);
				map.put("fk_userid", fk_userid);
				map.put("oqty", oqty);
				map.put("odrtotalprice", odrtotalprice);
				map.put("odrstatus", odrstatus);
				map.put("invoice", invoice);
				map.put("titleimg", titleimg);
				
				
				deleverList.add(map);
			}
			
		} finally {
			close();
		}
		return deleverList;
	}
	
	//** 검색타입별 갯수 알아오기
	@Override
	public int getTotalCoutBySType(String stype, String sword) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			
			if( !"".equals(sword)) {
				String sql = "select count(*)as CNT\n"+
						"from  product_order_detail A \n"+
						"join product_order B\n"+
						"on A.fk_odrcode = B.odrcode"
						+" where "+stype+" like '%'|| ? ||'%' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sword);
			}
			else {
				String sql = "select count(*)as CNT\n"+
						"from  product_order_detail A \n"+
						"join product_order B\n"+
						"on A.fk_odrcode = B.odrcode";
				pstmt = conn.prepareStatement(sql);
			}
			
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
			
		} finally {
			close();
		}
		return count;
	}
	
	// **** 결제완료로 변경시켜주는 추상 메소드 **** //
	@Override
	public int changePaymentComplete(String odrdnum) throws SQLException {
		int n=0;
		 try {
			conn = ds.getConnection();
			String sql = "update product_order_detail set ODRSTATUS = 1\n"+
					"where odrdnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrdnum);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return n;
	}
	
	
	// **** 배송시작으로 변경시켜주는 추상 메소드 **** //
	@Override
	public int changeDeliverStart(String odrdnum, String invoice) throws SQLException {
		int n=0;
		 try {
			conn = ds.getConnection();
			String sql = "update product_order_detail set ODRSTATUS = 2, deliverdate = sysdate, invoice = ?\n"+
					"where odrdnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, invoice);
			pstmt.setString(2, odrdnum);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return n;
	}
	
	
	// **** 배송완료로 변경시켜주는 추상 메소드 **** //
	@Override
	public int changeDeliverEnd(String odrdnum) throws SQLException {
		int n=0;
		 try {
			conn = ds.getConnection();
			String sql = "update product_order_detail set ODRSTATUS = 3\n"+
					"where odrdnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrdnum);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return n;
	}
	
	// ****주문취소로 변경시켜주는 추상 메소드 **** //
	@Override
	public int changeDeliverChange(String odrdnum) throws SQLException {
		
		int n=0;
		 try {
			conn = ds.getConnection();
			String sql = "update product_order_detail set ODRSTATUS = 4\n"+
					"where odrdnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrdnum);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return n;
	}
	
	
	
//	#store; mypage -- 주문 상세
	@Override
	public HashMap<String,String> getOneOrderDetail(String odrdnum) {
		HashMap<String,String> map =null;
		
		try {  
			
			conn=ds.getConnection();
			
			
			String sql = "    select odrdnum, odrcode, to_char(B.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate, fk_pnum,  \n"+
							"           fk_userid, odrprice, oqty, odrtotalprice, c.pname, C.titleimg as titleimg, c.point, c.saleprice, c.price, \n"+
							"           case odrstatus \n"+
							"           when 0 then '주문완료' \n"+
							"			when 1 then '결제완료' "+
							"           when 2 then '배송중' \n"+
							"           when 3 then '배송완료' \n"+
							"           when 4 then '주문취소' \n"+
							"           else '교환환불' end odrstatus\n"+
							"    from product_order_detail A \n"+
							"    join product_order B "+
							"    on A.fk_odrcode = B.odrcode"
							+ "  join product C " + 
							"    on A.fk_pnum = C.pnum "
							+ "	 where odrdnum = ? ";
			   
			
			  pstmt=conn.prepareStatement(sql);
			  pstmt.setString(1, odrdnum);
			
			  
			  rs = pstmt.executeQuery();
			  
			  rs.next();
			  	String v_odrdnum = rs.getString("odrdnum");
			  	String odrprice = rs.getString("odrprice");
				String odrcode = rs.getString("odrcode");
				String odrdate = rs.getString("odrdate");
				String fk_pnum = rs.getString("fk_pnum");
				String fk_userid = rs.getString("fk_userid");
				String oqty = rs.getString("oqty");
				String odrtotalprice = rs.getString("odrtotalprice");
				String odrstatus = rs.getString("odrstatus");
				String titleimg = rs.getString("titleimg");
				String pname = rs.getString("pname");
				String price = rs.getString("price");
				String saleprice = rs.getString("saleprice");
				String point = rs.getString("point");
				
				map = new HashMap<String, String>();
				map.put("odrdnum", v_odrdnum);
				map.put("odrprice", odrprice);
				map.put("odrcode", odrcode);
				map.put("odrdate", odrdate);
				map.put("fk_pnum", fk_pnum);
				map.put("fk_userid", fk_userid);
				map.put("oqty", oqty);
				map.put("odrtotalprice", odrtotalprice);
				map.put("odrstatus", odrstatus);
				map.put("titleimg", titleimg);
				map.put("oqty", oqty);
				map.put("pname", pname);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("point", point);
			
			
		} catch (SQLException e) {
				
				e.printStackTrace();
			
		}finally {
			close();
		}
		return map;
	} //주문 상세 끝

//	#store; mypage -- 주문 상세; totalprice 가져오는 메소드 
	@Override
	public int getTotalprice(String odrdnum) throws SQLException {
		int totalprice = 0;
		 
		 try {
			 
			 conn=ds.getConnection();
			 
			 String sql = " select saleprice, oqty "+
							 " from product join product_order_detail\n "+
							 " on pnum = fk_pnum\n "+
							 " where pnum = \n "+
							 " (\n "+
							 "    select fk_pnum\n "+
							 "    from product_order_detail\n "+
							 "    where odrdnum = ?\n "+
							 "  ) ";
			 
			 pstmt=conn.prepareStatement(sql);
			 pstmt.setString(1, odrdnum);
			 
			 rs=pstmt.executeQuery();
			 
			 boolean bool =rs.next();
			 
			 if(bool) {
				 int saleprice=rs.getInt("saleprice");
				 int oqty =rs.getInt("oqty");
				 totalprice= saleprice* oqty;
				 
			 }
		 }finally {
			 close();
		 }
		 
		return totalprice;
	}


/** #store; 회원 마이페이지 주문 내역
 * 주문목록을 가져오는 메소드
 * @param userid; 로그인한 유저 아이디
 * @return orderList
 * @throws SQLException
 */
	@Override
	public List<HashMap<String, String>> getOrderListByUserid(String userid) throws SQLException {
		List<HashMap<String, String>> orderList =null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select rno,odrdnum,odrcode,odrdate, fk_pnum,fk_userid,oqty,odrtotalprice,odrstatus,titleimg, pname, titleimg, point, saleprice, price, invoice \n"+
					"from \n"+
					"(\n"+
					"    select row_number()over(order by B.odrdate desc)as rno, odrdnum, odrcode, to_char(B.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate, fk_pnum,  \n"+
					"           fk_userid, oqty, odrtotalprice, c.pname, C.titleimg as titleimg, c.point, c.saleprice, c.price, invoice, \n"+
					"           case odrstatus \n"+
					"           when 0 then '주문완료' \n"+
					"			when 1 then '결제완료' "+
					"           when 2 then '배송중' \n"+
					"           when 3 then '배송완료' \n"+
					"           when 4 then '주문취소' \n"+
					"           else '교환환불' end odrstatus\n"+
					"    from product_order_detail A \n"+
					"    join product_order B\n"+
					"    on A.fk_odrcode = B.odrcode"
					+ "  join product C\r\n" + 
					"    on A.fk_pnum = C.pnum "
					+ "	where B.fk_userid = ? "+
					")V\n";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					orderList = new ArrayList<HashMap<String, String>>();
				}
				
				String odrdnum = rs.getString("odrdnum");
				String odrcode = rs.getString("odrcode");
				String odrdate = rs.getString("odrdate");
				String fk_pnum = rs.getString("fk_pnum");
				String fk_userid = rs.getString("fk_userid");
				String oqty = rs.getString("oqty");
				String odrtotalprice = rs.getString("odrtotalprice");
				String odrstatus = rs.getString("odrstatus");
				String titleimg = rs.getString("titleimg");
				String pname = rs.getString("pname");
				String price = rs.getString("price");
				String saleprice = rs.getString("saleprice");
				String point = rs.getString("point");
				String invoice = rs.getString("invoice");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("odrdnum", odrdnum);
				map.put("odrcode", odrcode);
				map.put("odrdate", odrdate);
				map.put("fk_pnum", fk_pnum);
				map.put("fk_userid", fk_userid);
				map.put("oqty", oqty);
				map.put("odrtotalprice", odrtotalprice);
				map.put("odrstatus", odrstatus);
				map.put("titleimg", titleimg);
				map.put("oqty", oqty);
				map.put("pname", pname);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("point", point);
				map.put("invoice", invoice);
				
				orderList.add(map);
				 
			}		
		} finally {
			close();
		}
		return orderList;
	}

//	#주문취소하기--> 주문테이블 상태 바꾸기
	@Override
	public int cancleOrderByOdrcode(String odrcode) throws SQLException {
	
		int result = 0;
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql = "update product_order_detail set odrstatus = 4 where fk_odrcode = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, odrcode);
			
			int n = pstmt.executeUpdate();
			int m = 0;
			if(n==1) {
				sql = "insert into order_cancel(odrcnum, odrccontents, fk_odrcode) values(seq_order_cancel_odrcnum.nextval, '관리자 확인', ? )";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, odrcode);
				
				m = pstmt.executeUpdate();
			}
			
			if(n*m==1) {
				conn.commit();
				conn.setAutoCommit(true);
				result = n*m;
			}
			else {
				conn.rollback();
				conn.setAutoCommit(true);
			}
		} finally {
			close();
		}
		
		return result;
	}
	
	
	
//	#store; header 전체검색
//	1) 검색결과 상품의 개수
	@Override
	public int getTotalSearchCount(String totalSearchWord) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();
			// 객체 ds 를 통해 아파치톰캣이 제공하는 DBCP(DB Connection pool)에서 생성된 커넥션을 빌려온다.	
			
			String sql = " select count(*) as CNT\n"+
						 " from view_productList "+
						 " where lower(pname) like '%' || lower(?) || '%'";
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, totalSearchWord);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalCount = rs.getInt("CNT");
			
		} finally {
			close();
		}
		
		return totalCount;
	}	

//	2) 검색결과 상품 정보 리스트
	@Override
	public List<HashMap<String, Object>> getSearchProduct(int sizePerPage, int currentShowPageNo, String totalSearchWord, String sort) throws SQLException {

		List<HashMap<String, Object>> productList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select rno, pacnum, pacname, pnum, pacimage, price, saleprice, stname\n"+
					 	 " from\n"+
					 	 " (\n"+
					 	 " select rownum as rno, pacnum, pacname, pnum, pacimage, price, saleprice, stname, pdate, plike\n"+
					 	 " from\n"+
					 	 "     (\n"+
					 	 "     select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, price, saleprice, stname, pdate, plike\n"+
					 	 "     from view_productList\n"+
						 "	   order by "+sort+
						 "     ) V\n"+
						 " where lower(pacname) like '%' || lower(?) || '%'\n"+
						 " ) T\n"+
						 " where T.rno between ? and ?\n";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, totalSearchWord);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					productList = new ArrayList<HashMap<String, Object>>();
				}
				
				String pacname = rs.getString("pacname"); 
				String pacnum = rs.getString("pacnum"); 
			    String pnum = rs.getString("pnum"); 
			    String pacimage = rs.getString("pacimage"); 
			    int saleprice = rs.getInt("saleprice");
			    int price = rs.getInt("price");
			    String stname = rs.getString("stname");
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("pacname", pacname);
			    map.put("pacnum", pacnum);
			    map.put("pnum", pnum);
			    map.put("pimgfilename", pacimage);
			    map.put("saleprice", saleprice);
			    map.put("price", price);
			    map.put("stname", stname);
				
				productList.add(map);
			}
			
		}finally {
			close();
		}
		
		return productList;
	}
	
	
//	#store; 상품 디테일 ----------------------------------------------
	// *** pnum을 기준으로 하나의 프로덕트 정보를 가져오는 메소드 *** //
	@Override
	public ProductVO getProductOneByPnum(int pnum) throws SQLException {
		
		ProductVO productvo = null;
		
		try {

			conn = ds.getConnection();
			
			String sql = "select pnum, pname, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, to_char(pdate, 'yyyy-mm-dd') as pdate, pimgfilename\n"+
					"from product A JOIN product_images B\n"+
					"on A.pnum = B.fk_pnum\n"+
					"where pnum = ?\n";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			
			rs = pstmt.executeQuery();
			
			boolean bool = rs.next();
			
			if(bool) {
				
				String v_pnum = rs.getString("pnum");
				String pname = rs.getString("pname");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				int pqty = rs.getInt("pqty");
				int point = rs.getInt("point");
				String pcontents = rs.getString("pcontents");
				String pcompanyname = rs.getString("pcompanyname");
				String pexpiredate = rs.getString("pexpiredate");
				String allergy = rs.getString("allergy");
				int weight = rs.getInt("weight");
				int salecount = rs.getInt("salecount");
				int plike = rs.getInt("plike");
				String pdate = rs.getString("pdate");
				
				productvo = new ProductVO();
				
				productvo.setPnum(v_pnum);
				productvo.setPname(pname);
				productvo.setPqty(pqty);
				productvo.setPrice(price); 
				productvo.setSaleprice(saleprice);
				productvo.setPoint(point);
				productvo.setPcontents(pcontents);
				productvo.setPcompanyname(pcompanyname);
				productvo.setPexpiredate(pexpiredate);
				productvo.setAllergy(allergy);
				productvo.setWeight(weight);
				productvo.setSalecount(salecount);
				productvo.setPlike(plike);
				productvo.setPdate(pdate);

				sql = " select pimgfilename from product_images where fk_pnum = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, pnum);
				
				rs = pstmt.executeQuery();
				
				List<String> pimgfilelist = null;
				
				int cnt = 0;
				while(rs.next()) {
					cnt++;
					
					if(cnt == 1) {
						pimgfilelist = new ArrayList<String>();
					}
					
					pimgfilelist.add(rs.getString("pimgfilename"));
				}
				
				productvo.setPimgfileList(pimgfilelist);
				
			}// end of if(bool)-------------------------------------------
			
		} finally {
			close();
		}
		
		return productvo;
		
	}

	// *** pacnum을 기준으로 패키지에 포함된 프로덕트 정보를 가져오는 추상 메소드 *** //
	@Override
	public List<ProductVO> getProductListbyPacnum(String pacnum) throws SQLException {
		
		List<ProductVO> productList = null;

		try {

			conn = ds.getConnection();
			
			String sql = "select fk_pacname, pnum, pname, price, saleprice, point, pcontents, pcompanyname, pexpiredate, allergy, weight, plike, paccontents\n"+
					"from product A JOIN product_package B\n"+
					"on A.fk_pacname = B.pacname\n"+
					"where A.fk_pacname = (select pacname from product_package where pacnum = ?)\n"+
					"order by pnum";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			
			rs = pstmt.executeQuery();
			
			String pnum = "";
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt == 1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String fk_pacname = rs.getString("fk_pacname");
				pnum = rs.getString("pnum");
				String pname = rs.getString("pname");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				String pcontents = rs.getString("pcontents");
				String pcompanyname = rs.getString("pcompanyname");
				String pexpiredate = rs.getString("pexpiredate");
				String allergy = rs.getString("allergy");
				int weight = rs.getInt("weight");
				int plike = rs.getInt("plike");
				String paccontents = rs.getString("paccontents");
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(pnum);
				pvo.setFk_pacname(fk_pacname);
				pvo.setPname(pname);
				pvo.setPrice(price);
				pvo.setSaleprice(saleprice);
				pvo.setPoint(point);
				pvo.setPcontents(pcontents);
				pvo.setPcompanyname(pcompanyname);
				pvo.setPexpiredate(pexpiredate);
				pvo.setAllergy(allergy);
				pvo.setWeight(weight);
				pvo.setPlike(plike);
				pvo.setPaccontents(paccontents);

				productList.add(pvo);
				
		   }// end of while()-------------------------------------------
	

		} finally {
			close();
		}
		
		return productList;
		
	}
	
	

	// plike 가 제일 많은것부터 순서대로 4개씩을 가져오는 추천상품리스트 메소드  
	@Override
	public List<ProductVO> getRecommdProdlist() throws SQLException {

		
		List<ProductVO> productList = null;
		
		try {

			conn = ds.getConnection();
			
			String sql = "select pacnum, pacname, pnum, pacimage, saleprice, plike, stname\n"+
					"from \n"+
					"(\n"+
					"select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, plike, stname\n"+
					"from view_productList\n"+
					"order by plike desc\n"+
					") V\n"+
					"where rownum between 1 and 4";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt == 1) {
					productList = new ArrayList<ProductVO>();
				}
				
				String fk_pacname = rs.getString("pacname");
				String pacnum = rs.getString("pacnum");
				String pnum = rs.getString("pnum");
				String pacimage = rs.getString("pacimage");
				int saleprice = rs.getInt("saleprice");
				String stname = rs.getString("stname");
				
				ProductVO productvo = new ProductVO();
				
				productvo.setFk_pacname(fk_pacname);
				productvo.setPacnum(pacnum);
				productvo.setPnum(pnum);
				productvo.setPimgfilename(pacimage);
				productvo.setSaleprice(saleprice);
				productvo.setFk_stname(stname);
				
				productList.add(productvo);
				
			}// end of while()-------------------------------------------
			
		} finally {
			close();
		}
		
		return productList;
		
		
	}


	// pnum을 기준으로 이미지를 가져오는 메소드 
	@Override
	public List<String> getImagesByPnum(String pnum) throws SQLException {
		
	  List<String> result = null;
      
      try {
         
         conn = ds.getConnection();

         String sql = "select distinct pimgfilename\n"+
        		 "from\n"+
        		 "(\n"+
        		 "select distinct pimgfilename from product_images where fk_pnum = ? \n"+
        		 "union all\n"+
        		 "select distinct titleimg from product where pnum = ?\n"+
        		 ")";
         
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, pnum);
         pstmt.setString(2, pnum);
         
         rs = pstmt.executeQuery();
         
         int cnt = 0;
         while(rs.next()) {
            cnt++;
            if(cnt == 1) {
               result = new ArrayList<String>();
            }
            
            String pimgfilename = rs.getString("pimgfilename");
            
            result.add(pimgfilename);
            
         }
         
      } finally {
         close();
      }
      
      return result;

		
	}

	// pacnum을 기준으로 이미지를 가졍는 메소드 
	@Override
	public List<String> getImagesByPacnum(String pacnum) throws SQLException {
		
	   List<String> result = null;
	      
	      try {
	         
	         conn = ds.getConnection();
	         
	         String sql = "select distinct pimgfilename, fk_pnum\n"+
	               "from product_images\n"+
	               "where fk_pnum in (select pnum from product where fk_pacname = (select pacname from product_package where pacnum = ?))\n"+
	               "union all\n"+
	               "select distinct titleimg, pnum\n"+
	               "from product\n"+
	               "where fk_pacname = (select pacname from product_package where pacnum = ?)\n"+
	               "order by fk_pnum";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, pacnum);
	         pstmt.setString(2, pacnum);
	         
	         rs = pstmt.executeQuery();
	         
	         int cnt = 0;
	         while(rs.next()) {
	            cnt++;
	            if(cnt == 1) {
	               result = new ArrayList<String>();
	            }
	            
	            String pimgfilename = rs.getString("pimgfilename");
	            
	            result.add(pimgfilename);
	            
	         }
	         
	      } finally {
	         close();
	      }
	      
	      return result;

		
	}


	// 패키지상품의 단품골라담기에서 특정제품을 선택했을때 밑에 공간에 append 해주기 위한 가격과 이름을 불러오는 메소드 

	@Override
	public HashMap<String, Object> getProductpriceNname(String pnum) throws SQLException {
	
		HashMap<String, Object> map = null;
	
		try {
			
			conn = ds.getConnection();
			
			String sql = " select pname, saleprice, point from product where pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);	
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			String pname = rs.getString("pname");
			int saleprice = rs.getInt("saleprice");
			int point = rs.getInt("point");
			
			map = new HashMap<String, Object>();
			
			map.put("pname", pname);
			map.put("saleprice", saleprice);
			map.put("point", point);
			
			
		} finally {
			close();
		}
		
		
	
		return map;
		
	}

	
	// 패키지에 딸려있는 상품의 갯수를 알려주는 메소드 (패키지 상품 좋아요에 씀)
	@Override
	public int getProductCountbyPacnum(String pacnum) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select count(*) as cnt from product where fk_pacname = (select pacname from product_package where pacnum = ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt("cnt");
			
		} finally {
			close();
		}
		
		
		return result;
		
	}


	// 패키지 상품 좋아요 메소드 
	@Override
	public int insertLikebypacnum(String userid, String pacnum, String len) throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();

			String sql = " update product set plike = plike + 1 where pnum in (select pnum "
					+ "														   from product "
					+ "														   where fk_pacname in (select pacname "
					+ "																			    from product_package "
					+ "																				where pacnum = ?)) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pacnum);
			int n = pstmt.executeUpdate();
			
			int num_len = Integer.parseInt(len);
			
			if(n == num_len) {

				sql = 	" select pnum "
				      + " from product "
					  + " where fk_pacname in (select pacname "
					  + "				       from product_package "
					  + "				       where pacnum = ?)";
							
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pacnum);
				rs = pstmt.executeQuery();
				
				List<String> pnumList = null;
				
				int cnt = 0;
				while(rs.next()) {
					
					cnt++;
					
					if(cnt == 1) {
						pnumList = new ArrayList<String>();
					}
					
					pnumList.add(rs.getString("pnum"));
					
				}
				
				
				for(String pnum : pnumList) {
					
					sql =  " insert into pick(picknum, fk_userid, fk_pnum)\r\n" + 
						   " values(seq_pick_picknum.nextval, ?, ?)";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.setString(2, pnum);
					result = pstmt.executeUpdate();
					
				}
			
			}
			else {
				return result;
			}
				
			
			
		} finally {
			close();
		}
		
		return result;
		

	}

	// 단품상품 좋아요 메소드 
	@Override
	public int insertLikebypnum(String userid, String pnum) throws SQLException {

		int result = 0;
		
		try {

			conn = ds.getConnection();
			
			String sql = " update product set plike = plike + 1 where pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pnum);
			int n = pstmt.executeUpdate();
			
			if(n == 1) {

				sql = " insert into pick(picknum, fk_userid, fk_pnum) " + 
				      " values(seq_pick_picknum.nextval, ?, ?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);
				result = pstmt.executeUpdate();
			
			}
			else {
				return result;
			}
			
		} finally {
			close();
		}
		
		return result;
	}	

	
	
//	#store; 주문하기 --------------------------------------
	 // *** 시퀀스 seq_jsp_order 값을 채번해오는 메소드 *** // 
    @Override 
    public int getSeq_jsp_order() throws SQLException { 

        int seq = 0; 

        try { 
            conn = ds.getConnection(); 
             
            String sql = " select seq_product_order_odrcode.nextval AS seq " +  
                         " from dual "; 
            pstmt = conn.prepareStatement(sql); 

            rs = pstmt.executeQuery(); 
             
            rs.next(); 
             
            seq = rs.getInt("seq"); 
        } finally { 
            close(); 
        } // end of try~finally 
         
        return seq; 
    } // end of public int getSeq_jsp_order() 

    // 주문하기 메서드
	@Override
	public int addOrder(String odrcode, String userid, int odrstatus, int sumtotalprice, int sumtotalpoint
				, String[] pnumArr, String[] oqtyArr, String[] salepriceArr, String[] cartnoArr, String couponNo) throws SQLException {
		
		int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0; 
		
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			// 1. 주문개요 테이블에 입력
			String sql = " insert into product_order(odrcode, fk_userid, odrtotalprice, odrtotalpoint) "
						+ " values(?, ?, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			pstmt.setString(2, userid);
			pstmt.setInt(3, sumtotalprice);
			pstmt.setInt(4, sumtotalpoint);
			
			n1 = pstmt.executeUpdate();
			System.out.println("n1 : "+n1);
			if(n1 != 1) {
				conn.rollback();
				conn.setAutoCommit(true);
				return 0;
			} // end of if
			
			// 2. 주문상세 테이블에 입력
			if(n1 == 1) {
				for(int i=0; i<pnumArr.length; i++) {
					sql = " insert into product_order_detail(odrdnum, fk_odrcode, fk_pnum, oqty, odrprice, odrstatus) "
						+ " values(seq_order_detail_odrdnum.nextval, ?, ?, ?, ?, ?) ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, odrcode);
					pstmt.setString(2, pnumArr[i]);
					pstmt.setString(3, oqtyArr[i]);
					pstmt.setString(4, salepriceArr[i]);
					pstmt.setInt(5, odrstatus);
					
					n2 = pstmt.executeUpdate();
					System.out.println("n2 : "+n2);
					if(n2 != 1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					} // end of if
				} // end of for
			} // end of if
			
			
			// 3. 구매자의 구매합계 올리기
			if(n2 == 1) {
				sql = "update member set summoney = summoney + ?, point = point + ? "
					+ " where userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, sumtotalprice);
				pstmt.setInt(2, sumtotalpoint);
				pstmt.setString(3, userid);
				
				n3 = pstmt.executeUpdate();
				System.out.println("n3 : "+n3);
				if(n3 != 1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				} // end of if
			} // end of if
			
			// 4. 구매자의 등급 올리기
			if(n3 == 1) {
	            sql = " update member set fk_lvnum = case when summoney >= 300000 then 3 " + 
	                    "                                  when summoney >= 100000 then 2 " + 
	                    "                                  else 1 end " + 
	                    " where userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				//System.out.println("sql: "+sql);
				n4 = pstmt.executeUpdate();
				System.out.println("n4 : "+n4);
				if(n4 != 1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				} // end of if
			} // end of if
			
			// 5. 주문한 제품의 잔고량 감하기
			if(n4 == 1) {
				for(int i=0; i<pnumArr.length; i++) {
					sql = " update product set pqty = pqty - ?"
						 + " where pnum = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, oqtyArr[i]);
					pstmt.setString(2, pnumArr[i]);
					
					n5 = pstmt.executeUpdate();
					System.out.println("n5 : "+n5);
					if(n5 != 1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					} // end of if
				} // end of for
			} // end of if

			// 5. 사용한 쿠폰 status를 0으로 바꾸기
			n6 = 1;
			if( (couponNo != null && !"".equals(couponNo)) && n5 == 1) {
				sql = " update my_coupon set cpstatus = 0 "
					+ " where fk_userid = ? and fk_cpnum = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, couponNo);
				
				n6 = pstmt.executeUpdate();
				System.out.println("n6 : "+n6);
				if(n6 != 1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				} // end of if
			} // end of if

			// 장바구니에서 주문한 경우
			if(cartnoArr != null && n6 == 1) {
				for(int i=0; i<cartnoArr.length; i++) {
					sql = " update product_cart set status = 0 "
							+ " where cartno = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, cartnoArr[i]);
					
					n7 = pstmt.executeUpdate();
					System.out.println("n7 : "+n7);
					if(n7 != 1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					} // end of if
				} // end of for
			} // end of if
			
			// 바로주문인 경우 commit
			if(cartnoArr == null && n1*n2*n3*n4*n5*n6 != 0) {
				conn.commit();
				conn.setAutoCommit(true);
				return 1;
			} // end fo if
			
			// 장바구니인 경우 commit
			else if(cartnoArr != null && n1*n2*n3*n4*n5*n6*n7 != 0) {
				conn.commit();
				conn.setAutoCommit(true);
				return 1;
			} else {
				conn.rollback();
				conn.setAutoCommit(true);
				return 0;
			} // end of if~else
		} finally {	
			close();
		} // end of try~finally
		
	} // end of 주문

	// *** 주문 완료 결과 보여주기 *** //
	@Override
	public List<HashMap<String, String>> selectOneOrder(String odrcode) throws SQLException {
		List<HashMap<String, String>> orderList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select a.odrcode, c.pname, b.oqty, b.odrprice, a.odrtotalprice, a.odrtotalpoint, d.name, c.price" + 
						 " from product_order a JOIN product_order_detail b " + 
						 " ON a.odrcode = b.fk_odrcode " + 
						 " JOIN product c " + 
						 " ON b.fk_pnum = c.pnum " +
						 " JOIN member d " + 
						 " ON a.fk_userid = d.userid" + 
						 " where odrcode = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt == 1) {
					orderList = new ArrayList<HashMap<String, String>>();
				}
				
				HashMap<String, String> orderMap = new HashMap<String, String>();
				orderMap.put("odrcode", rs.getString("odrcode"));
				orderMap.put("pname", rs.getString("pname"));
				orderMap.put("oqty", rs.getString("oqty"));
				orderMap.put("odrprice", rs.getString("odrprice"));
				orderMap.put("odrtotalprice", rs.getString("odrtotalprice"));
				orderMap.put("odrtotalpoint", rs.getString("odrtotalpoint"));
				orderMap.put("name", rs.getString("name"));
				orderMap.put("price", rs.getString("price"));
				
				orderList.add(orderMap);
			} // end of while
		} finally {
			close();
		}
		
		return orderList;
	} // end of public HashMap<String, String> selectOneOrder(String odrcode)

	// *** 쿠폰 조회 하기 *** //
	@Override
	public HashMap<String, String> selectOneCoupon(String coupon, String userid) throws SQLException {
		HashMap<String, String> couponMap = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select b.cpnum, a.cpstatus, b.cpname, b.discountper, b.cpusemoney, b.cpuselimit " + 
						 " from my_coupon a JOIN coupon b " + 
						 " ON a.fk_cpnum = b.cpnum " + 
						 " where fk_userid = ? and cpnum = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, coupon);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				couponMap = new HashMap<String, String>();
				
				couponMap.put("cpnum", rs.getString("cpnum"));
				couponMap.put("cpstatus", rs.getString("cpstatus"));
				couponMap.put("cpname", rs.getString("cpname"));
				couponMap.put("discountper", rs.getString("discountper"));
				couponMap.put("cpusemoney", rs.getString("cpusemoney"));
				couponMap.put("cpuselimit", rs.getString("cpuselimit"));
			} // end of if
		} finally {
			close();
		}
		
		return couponMap;
	} // end of public HashMap<String, String> selectOneCoupon()
	
	
//	#store; 장바구니 -------------------------------------------------
	// *** product_cart 테이블에서 oqty가 0보다 크면 update, 0이면 delete 장바구니변경 
		@Override
		public int updateDeleteCart(String cartno, String oqty) throws SQLException {
			
			int result = 0;
			
			try {
				conn = ds.getConnection();
			
				int v_oqty = Integer.parseInt(oqty);
				
				if(v_oqty==0) { // 장바구니 삭제
					String sql = " delete from product_cart "
							   + " where cartno = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, cartno);
					result = pstmt.executeUpdate();
				}
				else { // 장바구니 수량 변경
					String sql = " update product_cart set oqty = ? "
							   + " where cartno = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, v_oqty);
					pstmt.setString(2, cartno);
					result = pstmt.executeUpdate();
				}
				
			} finally {
				close();
			}
			
			return result;
			
		} // int updateDeleteCart(String cartno, String oqty) -----------------------------------------
/**
 * pnum에 해당하는 제품이 cart테이블에 없는 경우 insert, 있는 경우 oqty를 update
 * @param: userid; 로그인한 유저 아이디, pnum; 선택한 제품번호, oqty; 수량
 * @return insert 또는 update가 성공했을 경우 1, 실패한 경우 0 리턴 
 */
	@Override
	public int addCart(String userid, int pnum, int oqty) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
		
			String sql = " select cartno "
						+ " from product_cart"
						+ " where status = 1 and fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setInt(2, pnum);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int cartno = rs.getInt("cartno");
				sql = " update product_cart set oqty= oqty+? "
						+ "where cartno = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, oqty);
				pstmt.setInt(2, cartno);
				
				result = pstmt.executeUpdate();
			}
			else {
				sql = " insert into product_cart(cartno, fk_userid, fk_pnum, oqty, status) "
						+ " values(seq_product_cart_cartno.nextval, ?, ?, ?, default)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setInt(2, pnum);
				pstmt.setInt(3, oqty);
				
				result = pstmt.executeUpdate();
			}
					
		} finally {
			close();
		}
		return result;
	}
	
/**
 * pnum에 해당하는 제품이 cart테이블에 없는 경우 insert, 있는 경우 oqty를 update
 * @param: userid; 로그인한 유저 아이디, pnum; 선택한 제품번호, oqty; 수량
 * @return insert 또는 update가 성공했을 경우 1, 실패한 경우 0 리턴 
 */
	@Override
	public int addCart(String userid, String[] pnumArr, String[] oqtyArr) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			for(int i=0; i<pnumArr.length; i++) {
				String sql = " select cartno "
						+ " from product_cart"
						+ " where status = 1 and fk_userid = ? and fk_pnum = ? ";
			
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setInt(2, Integer.parseInt(pnumArr[i]));
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					int cartno = rs.getInt("cartno");
					sql = " update product_cart set oqty= oqty+? "
							+ "where cartno = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(oqtyArr[i]));
					pstmt.setInt(2, cartno);
					
					result = pstmt.executeUpdate();
				}
				else {
					sql = " insert into product_cart(cartno, fk_userid, fk_pnum, oqty, status) "
							+ " values(seq_product_cart_cartno.nextval, ?, ?, ?, default)";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.setInt(2, Integer.parseInt(pnumArr[i]));
					pstmt.setInt(3, Integer.parseInt(oqtyArr[i]));
					
					result = pstmt.executeUpdate();
				}
			}
			
					
		} finally {
			close();
		}
		return result;
	}
	
}
