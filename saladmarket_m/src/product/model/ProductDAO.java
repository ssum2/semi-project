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
	
	
//	#상품 상세; 추가이미지 가져오기
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
					"        order by pacnum\n"+
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
			
			String sql = "select rno, pacnum, pacname, pnum, pacimage, saleprice, stname\n"+
						"from\n"+
						"(\n"+
						"select rownum as rno, pacnum, pacname, pnum, pacimage, saleprice, stname, pdate, plike\n"+
						"from\n"+
						"    (\n"+
						"    select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, stname, pdate, plike\n"+
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
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("pacname", pacname);
			    map.put("pacnum", pacnum);
			    map.put("pnum", pnum);
			    map.put("pimgfilename", pacimage);
			    map.put("saleprice", saleprice);
			    map.put("stname", stname);
			      
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
			
			String sql = "select rno, pacnum, pacname, pnum, pacimage, saleprice, stname\n"+
						"from\n"+
						"(\n"+
						"select rownum as rno, pacnum, pacname, pnum, pacimage, saleprice, stname, pdate, plike\n"+
						"from\n"+
						"    (\n"+
						"    select pacnum, case pacname when '없음' then pname else pacname end as pacname, pnum, pacimage, saleprice, stname, pdate, plike\n"+
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
			    String pimgfilename = rs.getString("pacimage"); 
			    int saleprice = rs.getInt("saleprice");
			    String stname = rs.getString("stname");
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("pacname", pacname);
			    map.put("pacnum", pacnum);
			    map.put("pnum", pnum);
			    map.put("pimgfilename", pimgfilename);
			    map.put("saleprice", saleprice);
			    map.put("stname", stname);
			      
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
				 System.out.println(pacname); 
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
			
			String sql = " select cartno, fk_userid, fk_pnum, oqty, status, B.pname, B.price, B.saleprice, B.titleimg, C.pacname "
					   + " from product_cart A JOIN product B "
					   + " on A.fk_pnum=B.pnum "
					   + " JOIN product_package C " 
					   + " on B.fk_pacname= C.pacname "
					   + " where fk_userid = ? ";
					
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
				map.put("titleimg", rs.getString("titleimg"));
				map.put("pacname", rs.getString("pacname"));
				
				System.out.println(map.get("titleimg"));
				cartList.add(map);
			}
			
		} finally {
			close();
		}
		return cartList;
	} // List<HashMap<String, String>> getCartList(String userid) --------------------------------
		
}
