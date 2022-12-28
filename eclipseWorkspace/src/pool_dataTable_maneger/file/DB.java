package pool_dataTable_maneger.file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class DB {

	public static Connection conn;
	public static Statement stmt;
	public static String location;

	private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	public static boolean init(String port, String id, String pw) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 드라이버 설치

			if (strNumCheck(port)) {
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:" 
						+ port +":XE", // thin:로컬호스트:포트번호:db이름
						id, pw);// 드라이버 메니져 연결후 객체 생성

				stmt = conn.createStatement();// 문장을 만들어 쿼리를 보낼수 있는 객체 생성
				System.out.println("DB 연결 성공.");
				location = "창구";
				return true;
			}
			
			JOptionPane.showMessageDialog(null, "포트번호엔 숫자만 입력하세요.", "", JOptionPane.ERROR_MESSAGE);
			return false;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "jdbc 드라이버 로드 오류!", "", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "DB 연결 오류!", "", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
	}

	private static boolean strNumCheck(String str) {
		if (str == null) {
			return false;
		}
		return pattern.matcher(str).matches();
	}

	public static ResultSet getResultSet(String sql) {
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static int Update(String sql) {
		try {

			int var = stmt.executeUpdate(sql);

			for (int i = 0; i < Maneger_view.list_jpTable.size(); i++) {// refresh
				Maneger_view.list_jpTable.elementAt(i).refresh();
			}

			return var;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
