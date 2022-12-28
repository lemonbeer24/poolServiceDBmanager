package pool_dataTable_maneger.file;

import javax.swing.JFrame;

public class Dlg_maneger {//프로그램 내에서 생성되는 dlg 들을 관리하는 클래스
	
	public static JFrame Jf_NewClient;//dlg 변수들은 static 선언 되어 객채들을 담는다
	public static JFrame Jf_serviceExit;
	public static JFrame Jf_Keyset;
	public static JFrame Jf_find;
	//대화상자 를 호출할 클래스가 먼저 매니져 객채를 생성하고 그곳에서 getter,setter 들을 호출하여 해당되는 dlg 를 가져온다
	
	public static void getJf_find() {
		if(Jf_find != null) {
			Jf_find.setVisible(false);
		}
		Jf_find = null;
		Jf_find = new Dlg_find();
	}
	
	public static void getJf_NewClient() {
		if(Jf_NewClient != null) {
			Jf_NewClient.setVisible(false);
		}
		Jf_NewClient = null;
		Jf_NewClient = new Dlg_NewClient();
		
	}

	public static void getJf_serviceExit() {
		if(Jf_serviceExit != null) {
			Jf_serviceExit.setVisible(false);
		}
		Jf_serviceExit = null;
		Jf_serviceExit = new Dlg_serviceExit();
		
	}
	
	public static void getJf_serviceExit(String input) {
		if(Jf_serviceExit != null) {
			Jf_serviceExit.setVisible(false);
		}
		Jf_serviceExit = null;
		Jf_serviceExit = new Dlg_serviceExit(input);
		
	}

	public static void getJf_Keyset() {
		if(Jf_Keyset != null) {
			Jf_Keyset.setVisible(false);
		}
		Jf_Keyset = null;
		Jf_Keyset = new Dlg_keySet();
		
	}
	
	public static void getJf_Keyset(String input) {
		if(Jf_Keyset != null) {
			Jf_Keyset.setVisible(false);
		}
		Jf_Keyset = null;
		Jf_Keyset = new Dlg_keySet(input);
	
	}
	
}
