package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Dlg_NewClient extends JFrame implements ActionListener {
	
	private String[] str_sex = {"남성","여성"};
	private String[] str_pass = {"종일권","오후권(14:00~)"};
	
	private JPanel Jp_input,Jp_table_btn;
	
	private JTextField JTF_name,JTF_age;
	
	private JComboBox<String> combo_sex,combo_passTime;
	
	private ImageIcon Img_textImg = new ImageIcon(getClass().getClassLoader().getResource("./new_Client_textimg.png"));
	
	private JButton Btn_RegistClient,Btn_Cancel;
	
	private GridBagConstraints[][] gbc;//컴포넌트 레이아웃
	
	public Dlg_NewClient() {
		setTitle("이용권 구매(새로운 클라이언트 등록) ");
		setSize(500,350);
//		setLocation(600, 300);//실행시 윈도우 생성위치 설정
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setResizable(false);//프레임 크기 변경금지!
		//윈도우 나가기 버튼을 눌렀을시 무엇을 할지 설정
		
		setLayout(new BorderLayout());
		
		add(new JLabel(Img_textImg),BorderLayout.NORTH);
		
		Jp_input = new JPanel(new GridBagLayout());
		Jp_table_btn = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
		
		gbc = new GridBagConstraints[5][2];
		
		for (int i = 0; i < gbc.length; i++) {
			for (int j = 0; j < gbc[i].length; j++) {//2줄로 배치
				gbc[i][j] = new GridBagConstraints();
				gbc[i][j].gridx = j;//자동적으로 라밸은 줄의 첫번째 위치에 들어가게 
				gbc[i][j].gridy = i;//그외 컴포는 줄의 두번째 위치에
				gbc[i][j].insets = new Insets(5, 10, 5, 10);//그외 컴포넌트 용 간격
			}
		}
		
		Jp_input.add(new JLabel("이름입력 : "),gbc[0][0]);
		JTF_name = new JTextField(20);
		Jp_input.add(JTF_name,gbc[0][1]);
		
		Jp_input.add(new JLabel("나이* : "),gbc[1][0]);
		JTF_age = new JTextField(20);
		Jp_input.add(JTF_age,gbc[1][1]);
		
		Jp_input.add(new JLabel("성별* : "),gbc[2][0]);
		combo_sex = new JComboBox<String>(str_sex);
		Jp_input.add(combo_sex,gbc[2][1]);
		
		Jp_input.add(new JLabel("이용권 시간* : "),gbc[3][0]);
		combo_passTime = new JComboBox<String>(str_pass);
		Jp_input.add(combo_passTime,gbc[3][1]);
		
		add(Jp_input,BorderLayout.CENTER);
		
		Btn_RegistClient = new JButton("클라이언트 등록");
		Btn_RegistClient.addActionListener(this);
		Jp_table_btn.add(Btn_RegistClient);
		add(Jp_table_btn,BorderLayout.SOUTH);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object obj = arg0.getSource();
		
		if(obj == Btn_RegistClient) {
			
			//필터링
			if(JTF_age.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "나이를 입력하여 주십시오","newCilent",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String usetime = "'" + ((String) combo_passTime.getSelectedItem() + "'");
			String sex = "'" + ((String) combo_sex.getSelectedItem()) + "'";
			int age = Integer.parseInt((String)JTF_age.getText());
			String name = "'" + ((String) JTF_name.getText()) + "'";
			
			String sql = "insert into client (clientid,name,age,sex,intime,usetime)"//데이터 삽입
					+ "values (SEQ_CLIENTKEY.NEXTVAL," + name + "," + age + "," + sex + "," + "SYSDATE" + "," + usetime + ")";
			System.out.println(sql);
			
			if(DB.Update(sql) == 1) {
				JOptionPane.showMessageDialog(null, "적용 완료!","newCilent",JOptionPane.QUESTION_MESSAGE);
				//로그에 붙여질 데이터 : 클라이언트id
				ResultSet result = DB.getResultSet("SELECT SEQ_CLIENTKEY.CURRVAL FROM DUAL");//현재 꺼낸 id 값 가져오기
				
				try {//로그생성
					result.next();
					String info = "클라이언트 생성 :"+ "id:" + result.getString(1); 
					Log_maneger.createLog(info);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				JOptionPane.showMessageDialog(null, "클라이언트 등록에 실패 하였습니다.","newCilent",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

}
