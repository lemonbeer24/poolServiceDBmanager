package pool_dataTable_maneger.file;

import javax.swing.JOptionPane;

public class Log_maneger {
	
	private static String sqlQ;
	
	public static void createLog(String serviceinfo) {
		String info = "'" + serviceinfo + "'";
		String local = "'" + DB.location + "'";
		
		sqlQ = "insert into servicelog (ORDERID ,SERVICENAME ,SERVICEPOINT ,SERVICETIME)" +
				"values (SEQ_LOGID.NEXTVAL," + info + "," + local + "," + "SYSDATE)";
		System.out.println(sqlQ);
		
		if(1 != DB.Update(sqlQ)) {
			JOptionPane.showMessageDialog(null, "로그 등록 오류!","LogManeger",JOptionPane.ERROR_MESSAGE);
		}
	}
}
