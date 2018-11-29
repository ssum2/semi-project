package common.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class FrontController
 */
@WebServlet(
		description = "����ڰ� ������ *.do�� ȣ������ �� ���� �����Ѵ�", 
		urlPatterns = { "*.do" }, 
		initParams = {
//		servlet�� ������ ������ initParams�� �� �ѹ��� �����Ǳ� ������ properties���� ���ο� .do�� ���� ���� �� �ν� X
//		>> properties�� �������� �� WAS �� ������ �ݵ�� �籸�� �ؾ���
				
				@WebInitParam(name = "propertyConfig", value = "C:/Git/semi-project/saladmarket/WebContent/WEB-INF/Command.properties", description = "*.do���� class Mapping Info")
		})
public class FrontController extends HttpServlet {
//	>>FrontController; ��ġ�������� ���� -> ��� *.do�� �� �޾ƿͼ� �޾ƿ� .do�� � ����� �ϴ��� �˷���

	private static final long serialVersionUID = 1L;

//	#init(config)���� ������ object���� �־��� HashMap<keyŸ��, ��üŸ��> ����
	HashMap<String, Object> cmdMap = new HashMap<String, Object>();
	
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// 	.do�� ȣ������ �� FrontController���� ���� ���� ȣ��Ǵ� �޼ҵ�
		//	>> �ʱ�ȭ �Ķ���� ������ �о�� Mapping ������ ������ .do�� �����ִ� Ŭ�������� ���� ��üȭ ���� -> �ѹ��� ����
		System.out.println(">> Ȯ�ο�: servlet FrontController�� init �޼ҵ� ����");
		
		String props = config.getInitParameter("propertyConfig"); // initParams�� �ִ� value�� ��θ� ������ name���� �ҷ���
		System.out.println(">> Ȯ�ο�: �ʱ�ȭ �Ķ���� �����Ͱ�; " + props);
		
		Properties pr = new Properties(); // FileInputStream���� properties������ �о�ͼ� ��üȭ
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(props);
			pr.load(fis);
			/*
			 	Properties��ü.load(initParmas���� �ҷ��� value��)
			 	> properties������ �о�ͼ� PropertiesŬ������ ��ü�� �����͸� �÷���
			 	> �о�� ������ ���뿡�� '='�� �������� ������ key�� �ν�, �������� value�� �ν�	
			*/
			/*String className = pr.getProperty("/test1.do"); // Properties��ü.getProperty("key") -> return value
			System.out.println(">> Ȯ�ο�: properties�� �ִ� key������ value �ҷ�����;"+className);
			// className=test.controller.Test1Controller
			
			Class<?> cls = Class.forName(className); // ClassŸ���� ��ü���赵
			// >> Class<?>; ��Ư�� ��üŸ�� 1��
			
			Object obj = cls.newInstance(); // className�� �ִ� value���� ���� ��üȭ
			
			cmdMap.put("/test1.do", obj);*/
			
//			#properties�� �ִ� key�� �ҷ�����
			Enumeration<Object> en = pr.keys();
// 			>> properties���Ͽ� ����Ǿ��ִ� mapping �������� '='�� �߽����� ���ʿ� �ִ� ��� key���� ������
			
			while(en.hasMoreElements()) {
//				>> Enumeration��ü�� �ִ� Element�� �ִ� ��� true, ������ false
				
				String key_urlcmd = (String)en.nextElement(); // ���� ����ִ� key�� �޾ƿ�
				String className = pr.getProperty(key_urlcmd);
				
				if(className != null) {
					className = className.trim();
					Class<?> cls = Class.forName(className);
					Object obj = cls.newInstance();
					
					cmdMap.put(key_urlcmd, obj);
					
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("StringŸ������ ��õ� Ŭ������ �����ϴ�.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println(props+"������ �����ϴ�.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestProcess(request, response);
	}

	protected void requestProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		#init�޼ҵ忡�� ����� HashMap�� �־�� ��ü�� �ش� ��ü�� Ŭ����Ÿ������ ĳ�����Ͽ� ������
/*		Test1Controller action = (Test1Controller)cmdMap.get("/test1.do");
		action.testPrint();*/
//		>> Interface�� �θ�Ŭ������ ���� ����, �θ�Ŭ������ ĳ�������ְ� �ڽ�Ŭ�������� override�� �޼ҵ带 �ҷ���
		
//		#���������� ������ �ּҸ� �̿��Ͽ� Hashmap�� key�� ���ϱ�(/~.do)
		String url = request.getRequestURL().toString();
		// >> http://localhost:9090/MyMVC/test2.do
		
		String uri = request.getRequestURI();
		// >> /MyMVC/test2.do 
		
		String ctxPath = request.getContextPath(); 
		// >> /MyMVC
		
		String mapKey = uri.substring(ctxPath.length());
		// >> /test2.do ==> key��!
		
//		#������ ���� key�� �̿��Ͽ� Hashmap���� ��ü�� ������
//		 >> �������� �̿��Ͽ� �θ�Ŭ������ �����ص� �߻�Ŭ������ ����� ĳ���� --> ����Ǿ��ִ� ��� ��ü�� ��������
		AbstractController action = (AbstractController)cmdMap.get(mapKey);
		
		if(action == null) {
			System.out.println(mapKey+" URL ���Ͽ� ���ε� ��ü�� �����ϴ�.");
			return;
		}
		else {
			try {
				action.execute(request, response); // >> �߻�Ŭ�������� ���ص� �޼ҵ带 ��ӹ��� �ڽ�Ŭ�������� �������� ������ ���
				
				
				String viewPage = action.getViewPage();
				boolean bool = action.isRedirect();
				
				if(bool) {
					// >> true�� �� sendRedirect������� page�� �̵�
					// redirect; ������(history, request, response ��)�� �ѱ��� �ʰ� �������� �̵���
					response.sendRedirect(viewPage);
				}
				else {
					// >> false�� �� forward������� page�̵�
					// forward; ������ �̵��� �����ͱ��� �Ѿ
					RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
					dispatcher.forward(request, response);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		
		
	}
}
