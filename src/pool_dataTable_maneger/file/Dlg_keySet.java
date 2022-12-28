package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Dlg_keySet extends JFrame implements ActionListener {
	
	private ImageIcon Img_keytitle = new ImageIcon(getClass().getClassLoader().getResource("./Image_keytitle.png"));
	
	private JPanel Jp_input, Jp_Btn;
	
	private JTextField JTF_clientId,JTF_lockNum,JTF_coin;//할당할 클라id,락커번호(키번호),잔액
	
	private JComboBox<String> combo_payment;
	
	private JButton Btn_setKey;//적용 버튼
	
	private GridBagConstraints[][] gbc;//컴포넌트 레이아웃
	
	private String[] str_payment = {"선불","후불"};
	
	public Dlg_keySet() {
		
		set_component();
		
		setVisible(true);
	}
	
	public Dlg_keySet(String input) {
		
		set_component();
		
		JTF_clientId.setText(input);
		
		setVisible(true);
	}

	private void set_component() {
		setTitle("전자키 할당");
		setSize(500,350);
//		setLocation(600, 300);//실행시 윈도우 생성위치 설정
		setLocationRelativeTo(this);//파라미터 로 들어온 객채에 위치 상대적으로 윈도우 생성
		setResizable(false);//프레임 크기 변경금지!
		setLayout(new BorderLayout());
		
		add(new JLabel(Img_keytitle), BorderLayout.NORTH);
		
		gbc = new GridBagConstraints[4][2];
		
		for (int i = 0; i < gbc.length; i++) {
			for (int j = 0; j < gbc[i].length; j++) {//2줄로 배치
				gbc[i][j] = new GridBagConstraints();
				gbc[i][j].gridx = j;//자동적으로 라밸은 줄의 첫번째 위치에 들어가게 
				gbc[i][j].gridy = i;//그외 컴포는 줄의 두번째 위치에
				gbc[i][j].insets = new Insets(5, 10, 5, 10);//그외 컴포넌트 용 간격
			}
		}
		
		Jp_input = new JPanel(new GridBagLayout());
		
		Jp_input.add(new JLabel("*대상 클라이언트ID : "), gbc[0][0]);
		JTF_clientId = new JTextField(20);
		Jp_input.add(JTF_clientId, gbc[0][1]);
		
		Jp_input.add(new JLabel("*배정된 락커번호(전자키ID) : "), gbc[1][0]);
		JTF_lockNum = new JTextField(20);
		Jp_input.add(JTF_lockNum, gbc[1][1]);
		
		Jp_input.add(new JLabel("지불방식 : "), gbc[2][0]);
		combo_payment = new JComboBox<String>(str_payment);
		Jp_input.add(combo_payment, gbc[2][1]);
		
		Jp_input.add(new JLabel("할당 충전액  : "), gbc[3][0]);
		JTF_coin = new JTextField(20);
		Jp_input.add(JTF_coin, gbc[3][1]);
		
		add(Jp_input, BorderLayout.CENTER);
		
		Jp_Btn = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
		Btn_setKey = new JButton("전자키 할당");
		Btn_setKey.addActionListener(this);
		Jp_Btn.add(Btn_setKey);
		add(Jp_Btn,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton obj = (JButton) arg0.getSource();
		
		if(obj == Btn_setKey) {
			
			//필터링
			if(JTF_clientId.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "클라이언트 id 를 입력해 주세요","전자키 등록",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(JTF_lockNum.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "할당할 키 번호를 입력해 주세요","전자키 등록",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String clientId = "'" + JTF_clientId.getText() + "'";
			String lockNum = "'" + JTF_lockNum.getText() + "'";
			String payment = "'" + (String) combo_payment.getSelectedItem() + "'";
			String coin = "'" + JTF_coin.getText() + "'";
			
			String sql = "select clientid from digitalkey where clientid = " + clientId;
			System.out.println(clientId);
			ResultSet result = DB.getResultSet(sql);
			
			try {
				if(result.next() != false) {//이미 전자키가 있는 경우
					int option = JOptionPane.showConfirmDialog(null,"지금 전자키를 할당하려는 클라이언트는 이미 전자키가 존재 합니다\n기존전자키를 할당해제하고 새로운 전자키를 재할당 합니까?" 
						,"전자키 재 할당",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							
					if(JOptionPane.YES_OPTION == option) {//기존 전자키 에 새로운 전자키 할당
						
						sql = "delete from digitalkey where clientid = " + clientId;
						
						if(DB.Update(sql) == 1) {//삭제가 완료되면.
							insert_key(clientId, lockNum, payment, coin);
							
						} else {
							JOptionPane.showMessageDialog(null, "이미 할당 전자키 삭제중 에러가 발생하엿습니다","newCilent",JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else {return;}
					
				} else {//새로이 할당
					insert_key(clientId, lockNum, payment, coin);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void insert_key(String clientId, String lockNum, String payment, String coin) {
		String sql;
		sql = "insert into digitalkey (LOCKNUM ,CLIENTID ,PAYMENT_METHOD ,LEFT_MONEY )"//데이터 삽입
				+ "values (" + lockNum + "," + clientId + "," + payment + "," + coin + ")";
		System.out.println(sql);
		
		if(DB.Update(sql) == 1) {
			JOptionPane.showMessageDialog(null, "적용 완료!","keyset",JOptionPane.QUESTION_MESSAGE);
			Log_maneger.createLog("키 할당:id:" + lockNum.replace("'", ""));
		} else {
			JOptionPane.showMessageDialog(null, "할당할 전자키가 이미 할당되어있거나 DB 질의중 에러가 발생하였습니다.","keyset",JOptionPane.ERROR_MESSAGE);
		}
	}

}
