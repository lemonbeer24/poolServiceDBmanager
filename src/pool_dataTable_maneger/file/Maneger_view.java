package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Maneger_view extends JFrame implements ActionListener {
	
	public static final int COLUMN_CLIENT = 0;
	public static final int COLUMN_KEY = 1;
	public static final int COLUMN_LOG = 2;
	
	private JButton Btn_addNewClient, Btn_ClientExit, Btn_refresh, Btn_setDigitalKey, Btn_find;
	//툴바에 배치되는 기능 버튼
	
	private JPanel Jp_root_PanAndInfo;
	//pan_root 와 Ta_obj_info 를 담을 판넬 
	
	private Dlg_maneger Dlg_man;// dlg(대화상자) 관리자
	
	private JToolBar tb_Inbutton;//대화상자 들을 소환하는 버튼 들을 담는 툴-바
	
	public static Vector<Tableview> list_jpTable;//현제까지 생성된 테이블(판넬)을  담는 컬랙션, 새로고침에 사용됨.
	
	public static JTabbedPane Pan_root;//앱 에서 표시되는 모든 테이블을 담는 탭팬, 직접 메인 프레임에 추가되는 부분.
	
	public static JTextArea Ta_obj_info; // 표와 레이블의 정보를 표현할 택스트 에러어
	
	//버튼에 사용되는 이미지들
	/*
	private ImageIcon Img_ticket = new ImageIcon(Maneger_view.class.getResource("./icons_ticket_x32.png"));
	private ImageIcon Img_customerExit; //= new ImageIcon(Maneger_view.class.getResource("./icon_exit32px.png"));
	private ImageIcon Img_refresh; //= new ImageIcon(Maneger_view.class.getResource("./refresh32.png"));
	private ImageIcon Img_key; //= new ImageIcon(Maneger_view.class.getResource("./icons_key32x.png"));
	private ImageIcon Img_find; //= new ImageIcon(Maneger_view.class.getResource("./icon_search32x.png"));
	*/
	
	private ImageIcon Img_ticket = new ImageIcon(getClass().getClassLoader().getResource("icons_ticket_x32.png"));
	private ImageIcon Img_customerExit = new ImageIcon(getClass().getClassLoader().getResource("icon_exit32px.png"));
	private ImageIcon Img_refresh = new ImageIcon(getClass().getClassLoader().getResource("refresh32.png"));
	private ImageIcon Img_key = new ImageIcon(getClass().getClassLoader().getResource("icons_key32x.png"));
	private ImageIcon Img_find = new ImageIcon(getClass().getClassLoader().getResource("icon_search32x.png"));
	
	public Maneger_view() {
		setTitle("poolManeger App - made by inhatc kimjunseong");
		setSize(1000,800);
//		setLocation(600, 300);//실행시 윈도우 생성위치 설정
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setDefaultCloseOperation(EXIT_ON_CLOSE);//윈도우 나가기 버튼을 눌렀을시 무엇을 할지 설정
		
		//레이아웃
		setLayout(new BorderLayout());
		
		make_inButton_Toolbar();//기능 버튼 들을 담는 툴바객체 를 정해진 변수에 생성후 컴포넌트 add
		
		add(tb_Inbutton,BorderLayout.NORTH);//프래임의 왼쪽에 배치
		
		Jp_root_PanAndInfo = new JPanel(new BorderLayout());
		//클라이언트,전자키,서비스 사용내역을 표현할 표와 그 표와 레이블의 정보를 표현할 택스트 에러어 를 담을 판넬 
		Ta_obj_info = new JTextArea(10,30);
		Ta_obj_info.setFont(new Font("돋움", Font.PLAIN, 15));
		// 표와 레이블의 정보를 표현할 택스트 에러어
		
		JScrollPane sp_text_area = new JScrollPane(Ta_obj_info,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Jp_root_PanAndInfo.add(sp_text_area,BorderLayout.SOUTH);
		//스크롤바로 다시 생성
		
		list_jpTable = new Vector<Tableview>(1);
		
		//앱 에서 표시되는 모든 테이블을 담는 탭팬. 
		//기본적으로 전체 테이블을 표시하는 탭과. 테이블 하나하나씩 표현하는 탭 이 처음 앱 실행시 제공됨
		//검색결과나 특정 시기(ex : 오후 3시 부터 입장한 클라이언트들의 정보모음). 
		//에 있는 데이터 표현의 결과를 나타내는 테이블을 팬에 add 하여 표현하는 계획
		Pan_root = new JTabbedPane(JTabbedPane.NORTH);
		Pan_root.add("전체 정보",create_Jp_total_table());
		
		//컬랙션(탭팬(보여지는 부분아님)) 에 먼저 객체 생성후 추가. 파라미터 : 사용될 컬럼, 판넬 닫기 버튼 추가 여부, 판넬자체 타이틀에서 보여질 타이틀, 테이블에 들어갈 sql 명령어
		list_jpTable.add(new Tableview(COLUMN_CLIENT,true,"클라이언트 목록","select * from client"));
		Pan_root.add("클라이언트 목록", list_jpTable.elementAt(list_jpTable.size() - 1));//바로 방금 리스트에 추가된 테이블판넬을 삽입
		
		list_jpTable.add(new Tableview(COLUMN_KEY,true,"전자키 목록","select * from digitalkey"));
		Pan_root.add("전자키 목록", list_jpTable.elementAt(list_jpTable.size() - 1));
		
		list_jpTable.add(new Tableview(COLUMN_LOG,true,"서비스 목록","select * from servicelog"));
		Pan_root.add("서비스기록 목록", list_jpTable.elementAt(list_jpTable.size() - 1));
		
		Jp_root_PanAndInfo.add(Pan_root,BorderLayout.CENTER);
		
		add(Jp_root_PanAndInfo,BorderLayout.CENTER);
		
		Dlg_man = new Dlg_maneger();
		
		setVisible(true);
	}

	private JPanel create_Jp_total_table() {//기본 3개의 테이블 모두 하나의 판넬에 레이아웃 에따라 add
		
		//모든 테이블 판넬을 담아 그리드로 배치관리할 판넬
		JPanel Jp_total_table = new JPanel(new GridBagLayout());
		
		GridBagConstraints Jp_CilentPos = new GridBagConstraints();
		Jp_CilentPos.gridx = 0;//add시작 위치x좌표
		Jp_CilentPos.gridy = 0;
		Jp_CilentPos.weightx = 1;//가중치 부여
		Jp_CilentPos.weighty = 1;//지금 이 앱에선 가중치가 모두 동일하기에 영역을 채우는것 을 제외하곤 별의미가없음.
		Jp_CilentPos.fill = GridBagConstraints.BOTH;//남은 영역을 레이아웃 객체에서 정해진 한 에 꽉 채움.
		list_jpTable.add(new Tableview(COLUMN_CLIENT,false,"클라이언트 목록","select * from client"));
		Jp_total_table.add(list_jpTable.elementAt(list_jpTable.size() - 1),Jp_CilentPos);
		
		GridBagConstraints Jp_digitalKeyPos = new GridBagConstraints();
		Jp_digitalKeyPos.gridx = 0;
		Jp_digitalKeyPos.gridy = 1;
		Jp_digitalKeyPos.weightx = 1;
		Jp_digitalKeyPos.weighty = 1;
		Jp_digitalKeyPos.fill = GridBagConstraints.BOTH;
		list_jpTable.add(new Tableview(COLUMN_KEY,false,"전자키 목록","select * from digitalkey"));
		Jp_total_table.add(list_jpTable.elementAt(list_jpTable.size() - 1),Jp_digitalKeyPos);
		
		GridBagConstraints Jp_serViceLogPos = new GridBagConstraints();
		Jp_serViceLogPos.gridx = 1;
		Jp_serViceLogPos.gridy = 0;
		Jp_serViceLogPos.gridwidth = 1;//gridx,y 로 부터 얼마만큼 공간을 더 가질지 설정.
		Jp_serViceLogPos.gridheight = 2;
		Jp_serViceLogPos.weightx = 1;
		Jp_serViceLogPos.weighty = 1;
		Jp_serViceLogPos.fill = GridBagConstraints.BOTH;
		list_jpTable.add(new Tableview(COLUMN_LOG,false,"서비스 기록","select * from servicelog"));
		Jp_total_table.add(list_jpTable.elementAt(list_jpTable.size() - 1),Jp_serViceLogPos);
		
		return Jp_total_table;
	}
	
	private void make_inButton_Toolbar() {//기능 버튼 들을 담는 툴바객체 를 정해진 변수에 생성후 컴포넌트 add
		// TODO Auto-generated method stub
		tb_Inbutton = new JToolBar("buttonControlToolbar",JToolBar.HORIZONTAL);//수평으로 배치
		tb_Inbutton.setRollover(true);//아무래도 버튼 레이아웃을 바꾸는 메소드 같은데...
		
		Btn_addNewClient = new JButton("클라이언트 생성",Img_ticket);
		Btn_addNewClient.addActionListener(this);
		
		Btn_setDigitalKey = new JButton("전자키 할당",Img_key);
		Btn_setDigitalKey.addActionListener(this);
		
		Btn_ClientExit = new JButton("서비스 종료",Img_customerExit);
		Btn_ClientExit.addActionListener(this);
		
		Btn_refresh = new JButton("새로고침",Img_refresh);
		Btn_refresh.addActionListener(this);
		
		Btn_find = new JButton("테이블 검색",Img_find);
		Btn_find.addActionListener(this);
		
		tb_Inbutton.add(Btn_addNewClient);
		tb_Inbutton.add(Btn_ClientExit);
		tb_Inbutton.add(Btn_refresh);
		tb_Inbutton.add(Btn_setDigitalKey);
		tb_Inbutton.add(Btn_find);
		
		tb_Inbutton.setFloatable(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton obj = (JButton) arg0.getSource();
		
		if(obj == Btn_addNewClient) {//클라이언트 등록 프레임 소환
			Dlg_man.getJf_NewClient();
		}
		
		if(obj == Btn_refresh) {//모든 테이블 새로고침
			for (int i = 0; i < list_jpTable.size(); i++) {
				list_jpTable.elementAt(i).refresh();
			}
		}
		
		if(obj == Btn_ClientExit) {//클라이언트 서비스 사용종류
			Dlg_man.getJf_serviceExit();
		}
		
		if(obj == Btn_setDigitalKey) {//전자키 할당.
			Dlg_man.getJf_Keyset();
		}
		
		if(obj == Btn_find) {
			Dlg_man.getJf_find();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stu
		new StartingDBconnectView();
		//DB.init();
		//new Maneger_view();
	}

}
