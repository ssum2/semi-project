package util;

import java.security.MessageDigest;

/**
 * �ܹ��� ��ȣȭ �˰����� SHA256 ��ȣȭ�� �����ϴ� Ŭ����
 */
public class SHA256 { 

	/**
     * SHA256 ���� ��ȣȭ �Ѵ�.
     * @param planText ��ȣȭ�� ���ڿ�
     * @return String
     */
	public static String encrypt(String planText) {
        
		try{
        	/*
		     1. SHA-256���� �ؽ�
	             - MessageDigest��ü ������ �˰����� "SHA-256"���� �ؼ� �����. 
	                            �ؽõ� �����ʹ� ����Ʈ �迭Ÿ���� ���̳ʸ� ������(���� ������)�̴�.
            */
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();
           

            /*
             2. �ؽõ� �����ʹ� ����Ʈ �迭Ÿ���� ���̳ʸ� ������(���� ������)�̹Ƿ�
                              �̰��� 16���� ���ڿ�(String)Ÿ������ ��ȯ���ش�.
            */
            StringBuffer sb = new StringBuffer();
            	
            for(int i=0; i < byteData.length; i++) {
            	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1)); 
            }
            
            StringBuffer hexsb = new StringBuffer();
            
            for (int i=0; i<byteData.length;i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1){
                    hexsb.append('0');
                }
                hexsb.append(hex);
            }

            return hexsb.toString();
            
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }// end of encrypt(String planText)------------------------
	 
	
}// end of class SHA256
