package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class StartingDBconnectView extends JFrame implements ActionListener {
	
	private JPanel Jp_guideText, Jp_DbinfoInput;
	private JTextField jtf_port, jtf_name, jtf_pw;
	private JButton DBexccBtn;

	public StartingDBconnectView() 
	{
		setTitle("DB 접속");
		setSize(500,340);
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setDefaultCloseOperation(EXIT_ON_CLOSE);//윈도우 나가기 버튼을 눌렀을시 무엇을 할지 설정
		setResizable(false);//프레임 크기 변경금지!
		
		//레이아웃
		setLayout(new BorderLayout());
		
		Jp_guideText = new JPanel(new GridLayout(3,1));
		Jp_guideText.add(createGuideText("수영장 DB 관리(가상 시나리오) 프로그램 made by kimjunseog "));
		Jp_guideText.add(createGuideText("수영장 DB 로 oracle 11g 를 사용합니다"));
		Jp_guideText.add(createGuideText("<html><body>read me 파일 에 나온대로 dmp 파일을 import 한 후 권한을<br>"
				+ " 가지고 있는 계정정보를 아래에 입력 해 주세요.</body></html>"));
		
		add(Jp_guideText, BorderLayout.CENTER);
		
		Jp_DbinfoInput = new JPanel(new GridLayout(4,1));
		
		jtf_name = new JTextField(20);
		Jp_DbinfoInput.add(createDBinfoInput("오라클 DB 계정이름 : ", jtf_name));
		
		jtf_pw = new JTextField(20);
		Jp_DbinfoInput.add(createDBinfoInput("페스워드 : ", jtf_pw));
		
		jtf_port = new JTextField(20);
		Jp_DbinfoInput.add(createDBinfoInput("오라클 DB 포트번호 : ", jtf_port));
		
		JPanel Jp_btn = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		DBexccBtn = new JButton("DB 접속");
		DBexccBtn.addActionListener(this);
		Jp_btn.add(DBexccBtn);
		Jp_DbinfoInput.add(Jp_btn);
		
		add(Jp_DbinfoInput, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	private JPanel createGuideText(String Text) {
		JPanel flowp = new JPanel(new FlowLayout(FlowLayout.LEFT,50,20));
		flowp.add(new JLabel(Text));
		return flowp;	
	}
	
	private JPanel createDBinfoInput(String name, JTextField reftf) {
		JPanel inputflow = new JPanel(new FlowLayout(FlowLayout.LEFT,15,0));
		JPanel inputgrid = new JPanel(new GridLayout(1,2,5,0));
		
		inputgrid.add(new JLabel(name));
		inputgrid.add(reftf);
		
		inputflow.add(inputgrid);
		
		return inputflow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		
		if(obj == DBexccBtn) {
			if(DB.init(jtf_port.getText(), jtf_name.getText(), jtf_pw.getText())) {
				new Maneger_view();
				setVisible(false);
			} else {
				return;
			}
		}
	}

}
