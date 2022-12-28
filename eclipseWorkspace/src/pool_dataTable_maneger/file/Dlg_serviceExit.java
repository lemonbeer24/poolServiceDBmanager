package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Dlg_serviceExit extends JFrame implements ActionListener {
	
	//private ImageIcon Image_textExit = new ImageIcon(Dlg_serviceExit.class.getResource("./serviceExit_Image.png"));
	
	private JPanel Jp_inputId, Jp_btn;//정보 입력부, 버튼 레이아웃용
	
	private JTextArea JTA_deleteId;//삭제할 id 입력 모음
	
	private JButton btn_delete;
	
	private String[] deleteArr;//텍스트 에리어 에서 스크랩한 문자열들을 담은 배열
	
	public Dlg_serviceExit() {
		
		set_layout();
		
		setVisible(true);
	}
	
	public Dlg_serviceExit(String input) {
		
		set_layout();
		
		JTA_deleteId.setText(input);
		
		setVisible(true);
	}

	private void set_layout() {
		setTitle("클라이언트 서비스 종류");
		setSize(500,350);
//		setLocation(600, 300);//실행시 윈도우 생성위치 설정
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setResizable(false);//프레임 크기 변경금지!
		
		//레이아웃
		setLayout(new BorderLayout());
		
		//add(new JLabel(Image_textExit),BorderLayout.NORTH);
		
		Jp_inputId = new JPanel(new FlowLayout(FlowLayout.CENTER,30,20));
		
		Jp_inputId.add(new JLabel("대상 ID 입력 (복수 입력 가능 ','로 구분) : "));
		
		JTA_deleteId = new JTextArea(5,30);
		JTA_deleteId.setFont(new Font("돋움", Font.PLAIN, 20));
		Jp_inputId.add(JTA_deleteId);
		
		add(Jp_inputId,BorderLayout.CENTER);
		
		btn_delete = new JButton("서비스 종료");
		btn_delete.addActionListener(this);
		Jp_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		Jp_btn.add(btn_delete);
		
		add(Jp_btn,BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton obj = (JButton) arg0.getSource();
		if(obj == btn_delete) {
			deleteArr = JTA_deleteId.getText().split(",");//, 기준으로 잘라 배열로 반환
			
			int result = JOptionPane.showConfirmDialog(null, "클라이언트 서비스를 종료 합니다."
					,"서비스 종료",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					
			if(JOptionPane.YES_OPTION == result) {
			
				for (String string : deleteArr) {//순서대로 삭제
					String sqlQ = "Delete from client where clientid = " + string;
					
					if(DB.Update(sqlQ) != 1) {
						JOptionPane.showMessageDialog(null, "서비스 종료 처리중 에러가 발생하였습니다.","서비스 종료",JOptionPane.ERROR_MESSAGE);
						break;
					}
					Log_maneger.createLog("클라이언트 서비스 종료:"+string);
				}
				JOptionPane.showMessageDialog(null, "적용 완료!","서비스 종료",JOptionPane.QUESTION_MESSAGE);
			}
		}
	}
}
