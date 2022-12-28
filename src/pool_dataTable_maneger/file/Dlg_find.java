package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class Dlg_find extends JFrame implements ActionListener, ItemListener {
	
	private JPanel Jp_clientfind,Jp_keyfind,Jp_logfind;
	//각각 탭팬에 표시되는 검색판넬들을 표현. 아래에 input 와 select 를 borderlayout 으로 포함.
	
	private JPanel Jp_clientinput,Jp_keyinput,Jp_loginput;// 데이터 입력 컴포넌트 들을 배치하는 판넬
	
	private JPanel Jp_clientselect,Jp_keyselect,Jp_selectlog;// 입력할 데이터를 선택하는 체크박스들을 배치하는 판넬
	
	private JPanel Jp_btn;//검색, 취소 버튼을 담는 판넬
	
	private JButton btn_find, btn_cancel;//아래쪽에 검색, 취소 버튼
	
	private JPanel[] Jp_inputlineCli, Jp_inputlineKey, Jp_inputlinelog; //각 데이터입력 컴포넌트 들을 담는 판넬 배열.
	//컴포넌트를 그냥 input 펜넬에 배치하면 배치가 엉망이 될 수 있으니 검색되는 데이터 단위로 컴포넌트들을 묶어 먼전 이 판넬에 배치한뒤 input 판넬에 배치한다.
	
	private JTabbedPane JTP_find;//검색판넬들을 담은 탭팬
	
	private JTextField JTF_CclientId, JTF_CclientName, JTF_CintimeMin, JTF_CintimeMax;//클라이언트 검색 에서 사용되는 텍스트필드	
	
	private JTextField JTF_Klockid, JTF_Kuserid , JTF_KcoinMin, JTF_KcoinMax;//전자키 검색 에서 사용되는 텍스트필드
	
	private JTextField JTF_Lorderid, JTF_Lkeyid, JTF_LName, JTF_LPoint,
					   JTF_LTimeMin, JTF_LTimeMax, JTF_LcoinMin, JTF_LcoinMax;//서비스 기록 검색 에서 사용되는 텍스트필드
	
	private ButtonGroup clientRadioG, logRadioGname, logRadioGpoint;//클라이언트 이름 검색 옵션 라디오 버튼을 담는 그룹,기록 이름 검색 옵션을 담는 그룹, 서비스 지점 검색 옵션을 담는 그룹
	
	private JRadioButton RbtnC_or, RbtnC_and,//클라이언트 검색에서 사용되는 라디오 버튼
						 RbtnL_nameOr, RbtnL_nameAnd, RbtnL_pointOr, RbtnL_pointAnd;//서비스 기록 검색에서 사용되는 라디오 버튼  
	
	private JCheckBox[] CboxArrSelinclient,CboxArrSelinkey,CboxArrSelinlog;//검색할 데이터를 선택하는 체크박스를 담는 배열
	
	private JComboBox<String> combo_ageMin,combo_ageMax,combo_sex,combo_passTime;//클라이언트 검색 에서 사용되는 콤보박스
	private JComboBox<String> combo_payment;//전자키 검색 에서 사용되는 콤보박스
	
	private String[] arrAge;//클라이언트 테이블에 나이 콤보박스에 들어갈 문자열 배열
	
	private String[] str_sex = {"남성","여성"};
	private String[] str_pass = {"종일권","오후권(14:00~)"};
	private String[] str_payment = {"선불","후불"};
	
	private ImageIcon Img_findtitle = new ImageIcon(getClass().getClassLoader().getResource("./img_titleFind.png"));
	//new ImageIcon(Dlg_find.class.getResource("./img_titleFind.png"));
	
	public Dlg_find() {
		setTitle("검색...");
		setSize(500,530);
//		setLocation(600, 300);//실행시 윈도우 생성위치 설정
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setResizable(false);//프레임 크기 변경금지!
		
		//레이아웃
		setLayout(new BorderLayout());
		
		add(new JLabel(Img_findtitle),BorderLayout.NORTH);
		
		JTP_find = new JTabbedPane(JTabbedPane.NORTH);
		add(JTP_find,BorderLayout.CENTER);
		
		createClientfind();
		JTP_find.add("클라이언트 찾기",Jp_clientfind);
		createkeyfind();
		JTP_find.add("전자키 찾기", Jp_keyfind);
		createlogfind();
		JTP_find.add("서비스 기록 찾기", Jp_logfind);
		
		Jp_btn = new JPanel(new FlowLayout(FlowLayout.RIGHT));//맨 아래 검색,취소 버튼
		btn_find = new JButton("검색");
		btn_find.addActionListener(this);
		Jp_btn.add(btn_find);
		
		btn_cancel = new JButton("취소");
		btn_cancel.addActionListener(this);
		Jp_btn.add(btn_cancel);
		
		add(Jp_btn,BorderLayout.SOUTH);
		
		lineVisble(0);//체크박스 상태에 따라 해당 입력 컴포넌트 visible 설정
		lineVisble(1);
		lineVisble(2);
		
		setVisible(true);
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {//체크박스 값 변경시 호출됨
		// TODO Auto-generated method stub
		lineVisble(JTP_find.getSelectedIndex());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		//나이 최소 단위 콤보박스 값 변경시 
		if(obj == combo_ageMin) {
			JComboBox<String> combo = (JComboBox<String>) obj;
			int max = Integer.parseInt((String) combo.getSelectedItem());//min 콤보박스에서 선택된 값 가져오기
			combo_ageMax.removeAllItems();//Max 콤보박스 안을 비운 후
			combo_ageMax.addItem("단일검색");//첫번째 요소 추가
			
			for (int i = max; i <= 100; i++) {//최대값 기준으로 게속 추가
				combo_ageMax.addItem(Integer.toString(i));
			}
		}
		
		if(obj == btn_find) {
			String sqlQ = "";
			
			switch (JTP_find.getSelectedIndex()) {//현제 탭펜에 선택된 판넬 인덱스
			case 0://클라이언트 검색
				sqlQ = "select * from client where ";
				
				//검색 판넬에 들어있는 체크박스 들을 하나하나 값들을 체크하여 그에 해당하는 sql 명령어 삽입.
				if(CboxArrSelinclient[0].isSelected()) {//클라이언트 id
					String id = JTF_CclientId.getText();
					if(id.isEmpty()) {
						sqlQ += "clientid = null or ";
					} else {
						sqlQ += "clientid = " + id + " or ";
					}
				}
				
				if(CboxArrSelinclient[1].isSelected()) {//클라이언트 이름
					String name = JTF_CclientName.getText();
					if(RbtnC_or.isSelected()) {
						sqlQ += "name Like " + "'%" + name + "%' or ";
					} 
					else if(RbtnC_and.isSelected()) {
						sqlQ += "name Like " + "'" + name + "' or ";
					}
				}
				
				if(CboxArrSelinclient[2].isSelected()) {//클라이언트 나이
					String agemin = (String) combo_ageMin.getSelectedItem();
					if(combo_ageMax.getSelectedItem().equals("단일검색")) {
						sqlQ += "age = " + agemin + " or ";
					} else {
						String ageMax = (String) combo_ageMax.getSelectedItem();
						sqlQ += "age >= " + agemin + " and " + "age <= " + ageMax + " or ";
					}
				}
				
				if(CboxArrSelinclient[3].isSelected()) {//클라이언트 성별
					String sex = (String) combo_sex.getSelectedItem();
					sqlQ += "sex = " + "'" + sex + "'" + " or ";
				}
				
				if(CboxArrSelinclient[4].isSelected()) {//시비스 사용 시작 시간
					String timemin = JTF_CintimeMin.getText();
					if(JTF_CintimeMax.getText().equals("범위검색 시 이쪽까지 입력") || JTF_CintimeMax.getText().isEmpty()) {
						sqlQ += "intime = " + "'" + timemin + "'" + " or ";
					} else {
						String timemax = JTF_CintimeMax.getText();
						sqlQ += "intime >= " + "'" + timemin + "'" + " and " + "intime <= " + "'" + timemax + "'" + " or ";
					}
				}
				
				if(CboxArrSelinclient[5].isSelected()) {//사용권시간
					String passtime = (String) combo_passTime.getSelectedItem();
					sqlQ += "usetime = " + "'" + passtime + "'" + " or ";
				}
				
				sqlQ = sqlQ.substring(0, sqlQ.length() - 3);//마지막 부분 or 지우기
				System.out.println(sqlQ);
				//검색 결과를 새로운 테이블 뷰로 출력
				Maneger_view.list_jpTable.add(new Tableview(Maneger_view.COLUMN_CLIENT,true,"검색결과",sqlQ));
				Maneger_view.Pan_root.add("검색결과", Maneger_view.list_jpTable.elementAt(Maneger_view.list_jpTable.size() - 1));
				break;
				
			case 1://전자키 검색
				sqlQ = "select * from digitalkey where ";
				
				//락커번호(전자키 id)
				if(CboxArrSelinkey[0].isSelected()) {
					String lockid = JTF_Klockid.getText();
					if(lockid.isEmpty()) {
						lockid = "null";
					}
					sqlQ += "locknum = " + lockid + " or ";
				}
				
				//사용 클라이언트 id
				if(CboxArrSelinkey[1].isSelected()) {
					String clientid = JTF_Kuserid.getText();
					if(clientid.isEmpty()) {
						clientid = "null";
					}
					sqlQ += "clientid = " + clientid + " or ";
				}
				
				//지불방식
				if(CboxArrSelinkey[2].isSelected()) {
					String payment = (String) combo_payment.getSelectedItem();
					sqlQ += "payment_method = " + "'" + payment + "'" + " or ";
				}
				
				//잔액
				if(CboxArrSelinkey[3].isSelected()) {
					String coinmin = JTF_KcoinMin.getText();
					if(coinmin.isEmpty()) {
						coinmin = "null";
					}
					if(JTF_KcoinMin.getText().equals("범위 검색시 입력") || JTF_KcoinMin.getText().isEmpty()) {
						sqlQ += "left_money = " + coinmin + " or ";
					} else {
						String coinmax = JTF_KcoinMax.getText();
						if(coinmax.isEmpty()) {
							coinmax = "null";
						}
						sqlQ += "intime >= " + coinmin + " and " + "intime <= " + coinmax + " or ";
					}
				}
				
				sqlQ = sqlQ.substring(0, sqlQ.length() - 3);//마지막 부분 or 지우기
				System.out.println(sqlQ);
				//검색 결과를 새로운 테이블 뷰로 출력
				Maneger_view.list_jpTable.add(new Tableview(Maneger_view.COLUMN_KEY,true,"검색결과",sqlQ));
				Maneger_view.Pan_root.add("검색결과", Maneger_view.list_jpTable.elementAt(Maneger_view.list_jpTable.size() - 1));
				break;
				
			case 2://서비스 기록 검색
				sqlQ = "select * from servicelog where ";
				
				//주문번호
				if(CboxArrSelinlog[0].isSelected()) {
					String orderid = JTF_Lorderid.getText();
					if(orderid.isEmpty()) {
						orderid = "null";
					}
					sqlQ += "orderid = " + orderid + " or ";
				}
				
				//사용된 키 id
				if(CboxArrSelinlog[1].isSelected()) {
					String keyid = JTF_Lkeyid.getText();
					if(keyid.isEmpty()) {
						keyid = "null";
					}
					sqlQ += "keyid = " + keyid + " or "; 
				}
				
				//서비스 명
				if(CboxArrSelinlog[2].isSelected()) {
					String servicename = JTF_LName.getText();
					if(RbtnL_nameOr.isSelected()) {//부분일치
						sqlQ += "servicename like " + "'%" + servicename + "%'" + " or ";
					} else if(RbtnL_nameAnd.isSelected()) {//완전일치
						sqlQ += "servicename like " + "'" + servicename + "'" + " or ";
					}
				}
				
				//결재액
				if(CboxArrSelinlog[3].isSelected()) {
					String coinmin = JTF_LcoinMin.getText();
					if(JTF_LcoinMax.getText().equals("범위검색시 이쪽까지입력") || JTF_LcoinMax.getText().isEmpty()) {
						sqlQ += "payment = " + coinmin + " or ";
					} else {
						String coinmax = JTF_LcoinMax.getText(); 
						sqlQ += "payment >= " + coinmin + " and " + "payment <= " + coinmax + " or ";
					}
				}
				
				//서비스 지점
				if(CboxArrSelinlog[4].isSelected()) {
					String point = JTF_LPoint.getText();
					if(RbtnL_pointOr.isSelected()) {//부분일치
						sqlQ += "servicepoint like " + "'%" + point + "%'" + " or ";
					} else if(RbtnL_pointAnd.isSelected()) {//
						sqlQ += "servicepoint like " + "'" + point + "'" + " or ";
					}
				}
				
				//실행일
				if(CboxArrSelinlog[5].isSelected()) {
					String timemin = JTF_LTimeMin.getText();
					if(JTF_LTimeMax.getText().equals("범위검색시 이쪽까지입력") || JTF_LTimeMax.getText().isEmpty()) {//또는 아에 빈 경우
						sqlQ += "servicetime = " + timemin + " or ";
					} else {
						String timemax = JTF_LTimeMax.getText();
						sqlQ += "payment >= " + timemax + " and " + "payment <= " + timemax + " or ";
					}
				}
				
				sqlQ = sqlQ.substring(0, sqlQ.length() - 3);//마지막 부분 or 지우기
				System.out.println(sqlQ);
				//검색 결과를 새로운 테이블 뷰로 출력
				Maneger_view.list_jpTable.add(new Tableview(Maneger_view.COLUMN_LOG,true,"검색결과",sqlQ));
				Maneger_view.Pan_root.add("검색결과", Maneger_view.list_jpTable.elementAt(Maneger_view.list_jpTable.size() - 1));
				
				break;
				
			default:
				break;
			}
			Maneger_view.Pan_root.setSelectedIndex(Maneger_view.list_jpTable.size() - 3 );//새로 생성된 검색결과 판넬  
			this.setVisible(false);
		}
		
		if(obj == btn_cancel) {
			this.setVisible(false);
		}
	}
	
	private void lineVisble(int target) {//각 검색 판넬에 체크박스 값 변경시 호출됨
		//대상 판넬에 입력 데이터 선택 체크박스의 값에 여부에 따라 해당 inputline Visible 설정
		switch (target) {//대상 판넬을 나타넴
		case 0://클라이언트 검색
			for (int i = 0; i < CboxArrSelinclient.length; i++) {
				if(CboxArrSelinclient[i].isSelected()) {
					Jp_inputlineCli[i].setVisible(true);
				} else {
					Jp_inputlineCli[i].setVisible(false);
				}
			}
			break;
			
		case 1://전자키 검색
			for (int i = 0; i < CboxArrSelinkey.length; i++) {
				if(CboxArrSelinkey[i].isSelected()) {
					Jp_inputlineKey[i].setVisible(true);
				} else {
					Jp_inputlineKey[i].setVisible(false);
				}
			}
			break;
			
		case 2://서비스 기록 검색
			for (int i = 0; i < CboxArrSelinlog.length; i++) {
				if(CboxArrSelinlog[i].isSelected()) {
					Jp_inputlinelog[i].setVisible(true);
				} else {
					Jp_inputlinelog[i].setVisible(false);
				}
			}
			break;
			
		default:
			break;
		}
	}
	
	private void createlogfind() {
		//데이터 준비
		Jp_inputlinelog = new JPanel[6];//입력할 데이터에 따라 컴포넌트를 묶을 판넬 준비
		for (int i = 0; i < Jp_inputlinelog.length; i++) {
			Jp_inputlinelog[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		logRadioGname = new ButtonGroup();
		logRadioGpoint = new ButtonGroup();
		
		//레이아웃
		Jp_logfind = new JPanel(new BorderLayout());
		Jp_selectlog = new JPanel(new BorderLayout());
		Jp_loginput = new JPanel(new BorderLayout());
		
		//데이터 입력 선택부
		JLabel inputTitle = new JLabel("검색 할 데이터 선택");
		inputTitle.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle.setBackground(Color.white);
		Jp_selectlog.add(inputTitle, BorderLayout.NORTH);
		
		JPanel Jp_select = new JPanel(new GridLayout(2, 3, 10, 5));//체크박스를 배치할 판넬
		
		CboxArrSelinlog = new JCheckBox[6];//체크박스 준비
		CboxArrSelinlog[0] = new JCheckBox("주문번호");
		CboxArrSelinlog[1] = new JCheckBox("사용된 키 ID");
		CboxArrSelinlog[2] = new JCheckBox("서비스 명");
		CboxArrSelinlog[3] = new JCheckBox("결재액");
		CboxArrSelinlog[4] = new JCheckBox("서비스 지점");
		CboxArrSelinlog[5] = new JCheckBox("실행일");
		
		for (int j = 0; j < CboxArrSelinlog.length; j++) {
			CboxArrSelinlog[j].addItemListener(this);//리스너 설치
			Jp_select.add(CboxArrSelinlog[j]);
		}
		Jp_selectlog.add(Jp_select, BorderLayout.CENTER);
		Jp_logfind.add(Jp_selectlog, BorderLayout.NORTH);
		
		//데이터 입력부
		JLabel inputTitle2 = new JLabel("검색 할 데이터 입력");
		inputTitle2.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle2.setBackground(Color.white);
		Jp_loginput.add(inputTitle2,BorderLayout.NORTH);
		
		JPanel Jp_input = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//첫번째 줄
		Jp_inputlinelog[0].add(new JLabel("주문번호 : "));
		JTF_Lorderid = new JTextField(20);
		Jp_inputlinelog[0].add(JTF_Lorderid);
		
		Jp_input.add(Jp_inputlinelog[0]);
		
		//두번째 줄 
		Jp_inputlinelog[1].add(new JLabel("사용된 키 ID : "));
		JTF_Lkeyid = new JTextField(20);
		Jp_inputlinelog[1].add(JTF_Lkeyid);
		
		Jp_input.add(Jp_inputlinelog[1]);
		
		//세번째 줄 
		Jp_inputlinelog[2].add(new JLabel("서비스 명 : "));
		JTF_LName = new JTextField(15);
		Jp_inputlinelog[2].add(JTF_LName);
		
		RbtnL_nameOr = new JRadioButton("부분일치");
		logRadioGname.add(RbtnL_nameOr);
		Jp_inputlinelog[2].add(RbtnL_nameOr);
		
		RbtnL_nameAnd = new JRadioButton("완전일치");
		logRadioGname.add(RbtnL_nameAnd);
		Jp_inputlinelog[2].add(RbtnL_nameAnd);
		
		Jp_input.add(Jp_inputlinelog[2]);
		
		//네번째 줄 
		Jp_inputlinelog[3].add(new JLabel("결재액 : "));
		JTF_LcoinMin = new JTextField(15);
		Jp_inputlinelog[3].add(JTF_LcoinMin);
		
		Jp_inputlinelog[3].add(new JLabel(" ~ "));
		
		JTF_LcoinMax = new JTextField(15);
		JTF_LcoinMax.setText("범위검색시 이쪽까지입력");
		Jp_inputlinelog[3].add(JTF_LcoinMax);
		
		Jp_input.add(Jp_inputlinelog[3]);
				
		//다섯번째 줄 
		Jp_inputlinelog[4].add(new JLabel("서비스 지점 : "));
		JTF_LPoint = new JTextField(15);
		Jp_inputlinelog[4].add(JTF_LPoint);
		
		RbtnL_pointOr = new JRadioButton("부분일치");
		logRadioGpoint.add(RbtnL_pointOr);
		Jp_inputlinelog[4].add(RbtnL_pointOr);
		
		RbtnL_pointAnd = new JRadioButton("완전일치");
		logRadioGpoint.add(RbtnL_pointAnd);
		Jp_inputlinelog[4].add(RbtnL_pointAnd);
		
		Jp_input.add(Jp_inputlinelog[4]);
				
		//여섯번째 줄 
		Jp_inputlinelog[5].add(new JLabel("실행일 : "));
		JTF_LTimeMin = new JTextField(15);
		JTF_LTimeMin.setText("yy-mm-dd hh:mm:ss");
		Jp_inputlinelog[5].add(JTF_LTimeMin);
		
		Jp_inputlinelog[5].add(new JLabel(" ~ "));
		
		JTF_LTimeMax = new JTextField(15);
		JTF_LTimeMax.setText("범위검색시 이쪽까지입력");
		Jp_inputlinelog[5].add(JTF_LTimeMax);
		
		Jp_input.add(Jp_inputlinelog[5]);
		
		Jp_loginput.add(Jp_input,BorderLayout.CENTER);
		Jp_logfind.add(Jp_loginput,BorderLayout.CENTER);
	}
	
	private void createkeyfind() {
		//데이터 준비
		Jp_inputlineKey = new JPanel[4];
		for (int i = 0; i < Jp_inputlineKey.length; i++) {
			Jp_inputlineKey[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		//레이아웃
		Jp_keyfind = new JPanel(new BorderLayout());
		Jp_keyselect = new JPanel(new BorderLayout());
		Jp_keyinput = new JPanel(new BorderLayout());
		
		//데이터 입력 선택부
		JLabel inputTitle = new JLabel("검색 할 데이터 선택");
		inputTitle.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle.setBackground(Color.white);
		Jp_keyselect.add(inputTitle, BorderLayout.NORTH);
		
		JPanel Jp_select = new JPanel(new GridLayout(2, 2, 10, 5));
		
		CboxArrSelinkey = new JCheckBox[4];
		CboxArrSelinkey[0] = new JCheckBox("락커 번호(전자키 id)");
		CboxArrSelinkey[1] = new JCheckBox("사용 클라이언트 id");
		CboxArrSelinkey[2] = new JCheckBox("지불방식");
		CboxArrSelinkey[3] = new JCheckBox("잔액");		
		
		for (int j = 0; j < CboxArrSelinkey.length; j++) {
			CboxArrSelinkey[j].addItemListener(this);
			Jp_select.add(CboxArrSelinkey[j]);
		}
		Jp_keyselect.add(Jp_select, BorderLayout.CENTER);
		Jp_keyfind.add(Jp_keyselect,BorderLayout.NORTH);
		
		//데이터 입력부
		JLabel inputTitle2 = new JLabel("검색 할 데이터 입력");
		inputTitle2.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle2.setBackground(Color.white);
		Jp_keyinput.add(inputTitle2,BorderLayout.NORTH);
		
		JPanel Jp_input = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//첫번째 줄
		Jp_inputlineKey[0].add(new JLabel("락커 번호(전자키 id) : "));
		JTF_Klockid = new JTextField(20);
		Jp_inputlineKey[0].add(JTF_Klockid);
		Jp_input.add(Jp_inputlineKey[0]);
		
		//두번째 줄
		Jp_inputlineKey[1].add(new JLabel("사용 클라이언트 id : "));
		JTF_Kuserid = new JTextField(20);
		Jp_inputlineKey[1].add(JTF_Kuserid);
		Jp_input.add(Jp_inputlineKey[1]);
		
		//세번째 줄
		Jp_inputlineKey[2].add(new JLabel("지불 방식 : "));
		combo_payment = new JComboBox<String>(str_payment);
		Jp_inputlineKey[2].add(combo_payment);
		Jp_input.add(Jp_inputlineKey[2]);
		
		//네번째 줄
		Jp_inputlineKey[3].add(new JLabel("잔액 : "));
		JTF_KcoinMin = new JTextField(10);
		Jp_inputlineKey[3].add(JTF_KcoinMin);
		
		Jp_inputlineKey[3].add(new JLabel(" ~ "));
		
		JTF_KcoinMax = new JTextField(10);
		JTF_KcoinMax.setText("범위 검색시 입력");
		Jp_inputlineKey[3].add(JTF_KcoinMax);
		
		Jp_input.add(Jp_inputlineKey[3]);
		
		Jp_keyinput.add(Jp_input,BorderLayout.CENTER);
		Jp_keyfind.add(Jp_keyinput,BorderLayout.CENTER);
	}

	private void createClientfind() {
		
		//데이터 준비
		Jp_inputlineCli = new JPanel[6];
		for (int i = 0; i < Jp_inputlineCli.length; i++) {
			Jp_inputlineCli[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		arrAge = new String[96];
		for (int i = 5; i <= 100; i++) {
			arrAge[i - 5] = Integer.toString(i);
		}
		
		clientRadioG = new ButtonGroup();
		
		//라디오 버튼 준비
		RbtnC_or = new JRadioButton("부분일치");
		clientRadioG.add(RbtnC_or);
		
		RbtnC_and = new JRadioButton("완전일치");
		clientRadioG.add(RbtnC_and);
		
		//레이아웃 설정
		Jp_clientfind = new JPanel(new BorderLayout());
		
		//데이터 입력 선택부 
		Jp_clientselect = new JPanel(new BorderLayout());
		
		JLabel inputTitle = new JLabel("검색 할 데이터 선택");
		inputTitle.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle.setBackground(Color.white);
		Jp_clientselect.add(inputTitle, BorderLayout.NORTH);
		
		JPanel Jp_select = new JPanel(new GridLayout(2, 3, 10, 5));
		
		CboxArrSelinclient = new JCheckBox[6];
		CboxArrSelinclient[0] = new JCheckBox("클라이언트 id");
		CboxArrSelinclient[1] = new JCheckBox("이름");
		CboxArrSelinclient[2] = new JCheckBox("나이");
		CboxArrSelinclient[3] = new JCheckBox("성별");
		CboxArrSelinclient[4] = new JCheckBox("서비스 사용 시작 시간");
		CboxArrSelinclient[5] = new JCheckBox("사용권 시간");
		
		for (int j = 0; j < CboxArrSelinclient.length; j++) {
			CboxArrSelinclient[j].addItemListener(this);
			Jp_select.add(CboxArrSelinclient[j]);
		}
		Jp_clientselect.add(Jp_select, BorderLayout.CENTER);
		Jp_clientfind.add(Jp_clientselect,BorderLayout.NORTH);
		
		//데이터 입력부
		Jp_clientinput = new JPanel(new BorderLayout());
		
		JLabel inputTitle2 = new JLabel("검색 할 데이터 입력");
		inputTitle2.setOpaque(true);//라밸의 페인트 가능 여부 설정. false 일 경우 페인트가 되지않아 기본픽셀 만 표시됨.
		inputTitle2.setBackground(Color.white);
		Jp_clientinput.add(inputTitle2, BorderLayout.NORTH);
		
		JPanel Jp_input = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Jp_clientinput.add(Jp_input,BorderLayout.CENTER);
		
		//첫번째줄
		Jp_inputlineCli[0].add(new JLabel("클라이언트 id : "));
		JTF_CclientId = new JTextField(20);
		Jp_inputlineCli[0].add(JTF_CclientId);
		Jp_input.add(Jp_inputlineCli[0]);
		
		//두번째줄
		Jp_inputlineCli[1].add(new JLabel("클라이언트 이름 : "));
		JTF_CclientName = new JTextField(10);
		Jp_inputlineCli[1].add(JTF_CclientName);
		Jp_inputlineCli[1].add(RbtnC_or);
		Jp_inputlineCli[1].add(RbtnC_and);
		Jp_input.add(Jp_inputlineCli[1]);
		
		//세번째 줄
		//나이 영역
		Jp_inputlineCli[2].add(new JLabel("나이 : "));
		combo_ageMin = new JComboBox<String>(arrAge);
		combo_ageMin.addActionListener(this);
		Jp_inputlineCli[2].add(combo_ageMin);
		Jp_inputlineCli[2].add(new JLabel("~"));
		
		combo_ageMax = new JComboBox<String>();
		combo_ageMax.addItem("단일검색");
		Jp_inputlineCli[2].add(combo_ageMax);
		Jp_inputlineCli[2].add(new JLabel(" | "));
		
		Jp_input.add(Jp_inputlineCli[2]);
		
		//성별 영역
		Jp_inputlineCli[3].add(new JLabel("성별 : "));
		combo_sex = new JComboBox<String>(str_sex);
		Jp_inputlineCli[3].add(combo_sex);
		
		Jp_input.add(Jp_inputlineCli[3]);
		
		//네번째줄
		Jp_inputlineCli[4].add(new JLabel("시작시간 : "));
		JTF_CintimeMin = new JTextField(16);
		JTF_CintimeMin.setText("(단일검색)yy-mm-dd hh:mm:ss");
		Jp_inputlineCli[4].add(JTF_CintimeMin);
		
		Jp_inputlineCli[4].add(new JLabel(" ~ "));
		JTF_CintimeMax = new JTextField(16);
		JTF_CintimeMax.setText("범위검색 시 이쪽까지 입력");
		Jp_inputlineCli[4].add(JTF_CintimeMax);
		
		Jp_input.add(Jp_inputlineCli[4]);
		
		//여섯번째 줄
		Jp_inputlineCli[5].add(new JLabel("이용권 시간 : "));
		combo_passTime = new JComboBox<String>(str_pass);
		Jp_inputlineCli[5].add(combo_passTime);
		
		Jp_input.add(Jp_inputlineCli[5]);
		Jp_clientfind.add(Jp_clientinput,BorderLayout.CENTER);
		//데이터 입력부 end 
	}

}
