package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
/*
	[�߻� �޼ҵ� ����]
	1) �߻�޼ҵ� ����� public abstract �����ڰ� �ڵ����� ����
	2) �߻󺯼� ����� �ڵ����� public static final�� ���� 
 */
//	#�� ���� ����޼ҵ�	
	void execute(HttpServletRequest req, HttpServletResponse res) throws Exception;
	
}
