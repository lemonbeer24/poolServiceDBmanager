package pool_dataTable_maneger.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Tableview extends JPanel implements MouseListener, ActionListener {//데이터 테이블과 테이블간 경계선역할과 테이블 구분 을 할 라밸을 담을 판넬 을 반환
	
	private JTable Table;//판넬 안에 데이터가 표현되는 테이블
	
	private DefaultTableModel tableModel;//테이블 데이터 모델
	
	private int columnSet;//현제 테이블의 칼럼이 어느 tablecolumns 배열의 요소 를 참조하고 있는지 표현
	
	private String sqlQ;//테이블로 데이터를 가져올 sql 쿼리
	
	private Vector<String> vector;//해당 테이블에 삽입할 레이블을 담음
	
	private Dlg_maneger Dlgman;//대화상자 생성기
	
	private JPopupMenu PopupMenu;//각각 테이블에 들어갈 오른쪽 클릭 팝업 메뉴
	
	private ImageIcon Image_close = new ImageIcon(getClass().getClassLoader().getResource("./icon_close_16x.png"));
	//Tableview.class.getResource
	
	private JButton Btn_close;//해당 탭을 닫기
	
	private JPanel labelAndBtn;// 판넬타이틀과 닫기버튼을 포함하는 판넬
	
	private JMenuItem[][] JmItems = {{new JMenuItem("서비스 사용 종류"),new JMenuItem("새로운 클라이언트"),new JMenuItem("전자키 할당")},
									{new JMenuItem("전자키 추가")},
									{new JMenuItem("이 기록 지우기")},};
	//오른쪽 클릭 팝업매뉴에 들어갈 아이탬들
	
	private String[][] tablecolumns = {{"클라이언트ID","고객성명","나이","성별","서비스 이용 시작 시간","이용권 시간"},//0 : 클라이언트 테이블 칼럼 모음
										{"락커번호","사용자ID","지불방식","잔액(원)"},// 1 : 디지털 키 테이블
										{"주문번호","사용된 키 ID","서비스 명","결재액","서비스 지점","실행일"}};
	
	public Tableview(int columnSet,boolean addBtn,String title,String sqlQ) {
		//인자 : 테이블 컬럼(배열을 호출할),닫기버튼 추가여부,판넬의 타이틀,테이블에 들어갈 명령어
		// TODO Auto-generated constructor stub
		
		this.columnSet = columnSet;
		this.sqlQ = sqlQ;//sql 쿼리
		
		tableModel = new DefaultTableModel(tablecolumns[columnSet], 0);//테이블 데이터 관리 모델
		vector = new Vector<String>(tablecolumns[columnSet].length);//테이블에 레이블들을 담아 실제 테이블에 삽입할 백터
		
		Dlgman = new Dlg_maneger();
		
		createPopupMenu();
		
		Table = new JTable(tableModel){//클라이언트 정보를담을 테이블
	         public boolean editCellAt(int row, int column, java.util.EventObject e) {//테이블을 읽기전용으로 만들기
		            return false;
		     }
		};
		
		Table.addMouseListener(this);
		
		//레이아웃
		
		setLayout(new BorderLayout());
		
		Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//자동으로 조정되는 컬럼의 크기조정을 해제
		setColumnLength(Table);//컬럼길이조정
		
		labelAndBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));//테이블 타이틀과 닫기 버튼을 담을 판넬, 왼쪽 정렬
		
		JLabel l1 = new JLabel(title);
		labelAndBtn.add(l1);
		
		if(addBtn == true) {//인수로 들어오는 addBtn이 참일 경우만(판넬 삭제 버튼 추가)
			Btn_close = new JButton(Image_close);
			Btn_close.addActionListener(this);
			labelAndBtn.add(Btn_close);
		}
		
		add(labelAndBtn,BorderLayout.NORTH);//레이블은 위에배치. 테이블은 그 아래에 배치
		
		JScrollPane sp_CilentTable = new JScrollPane(Table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		add(sp_CilentTable,BorderLayout.CENTER);
		
		//데이터 입력
		refresh();
	}
	
	private void createPopupMenu() {//객체 생성후 메뉴에 들어갈 아이탬 add 하기
		// TODO Auto-generated method stub
		
		PopupMenu = new JPopupMenu();
		
		switch (columnSet) {
		case Maneger_view.COLUMN_CLIENT:
			for (int i = 0; i < JmItems[Maneger_view.COLUMN_CLIENT].length; i++) {//배열에 미리 저장되어 있는 item
				JmItems[Maneger_view.COLUMN_CLIENT][i].addMouseListener(this);//매뉴 아이탬 리스너 할당.
				PopupMenu.add(JmItems[Maneger_view.COLUMN_CLIENT][i]);//매뉴에 배열에 있는 해당 아이탬 삽입 
			}
			PopupMenu.addMouseListener(this);//매뉴 리스너 삽입.
			break;
			
		case Maneger_view.COLUMN_KEY:
			for (int i = 0; i < JmItems[Maneger_view.COLUMN_KEY].length; i++) {
				PopupMenu.add(JmItems[Maneger_view.COLUMN_KEY][i]);
				JmItems[Maneger_view.COLUMN_KEY][i].addMouseListener(this);
			}
			PopupMenu.addMouseListener(this);
			break;
			
		case Maneger_view.COLUMN_LOG:
			for (int i = 0; i < JmItems[Maneger_view.COLUMN_LOG].length; i++) {
				PopupMenu.add(JmItems[Maneger_view.COLUMN_LOG][i]);
				JmItems[Maneger_view.COLUMN_LOG][i].addMouseListener(this);
			}
			PopupMenu.addMouseListener(this);
			break;
				
		default:
			break;
		}
	}

	public void refresh() {//새로고침
		
		if (tableModel.getRowCount() > 0) {//일단 모든 row 제거
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		        tableModel.removeRow(i);
		    }
		}
		
		ResultSet rs = DB.getResultSet(sqlQ);// 쿼리로 데이터 얻어옴
		try {
			while(rs.next()) {
				for (int i = 0; i < tablecolumns[columnSet].length; i++) {//배열에 명시되는 컬럼의 수대로
					vector.add(rs.getString(i + 1));//백터에 삽입(누락되는 컬럼이 없게 설계됨)
				}
				tableModel.addRow(vector);//레이블에 배치
				vector = new Vector<String>(tablecolumns[columnSet].length);//백터 비우기
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setColumnLength(Table);
	}
	
	private void setColumnLength(JTable target_Table) {//테이블의 컬럼 길이를 조정
		int column_size;
		for (int i = 0; i < target_Table.getColumnCount(); i++) {//컬럼 하나하나씩 문자열을 가져와 그 문자열 만큼 정해진 공식으로 컬럼의 크기를 할당함.
			column_size = 24 + (target_Table.getColumnName(i).length() * 12);
			if(column_size < 96) {//컬럼 길이가 일정 길이 이상을 넘지 못하는경우 그 일정길이로 컬럼길이를 조정 
				target_Table.getColumnModel().getColumn(i).setPreferredWidth(96);
			} else {
				target_Table.getColumnModel().getColumn(i).setPreferredWidth(column_size);
			}
		}// 공식 : 컬럼길이 = 24 + (컬럼문자열 길이 * 12)
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(SwingUtilities.isRightMouseButton(e)) {//오른쪽클릭 일 경우
				
			int r = Table.rowAtPoint(e.getPoint());//마우스 이밴트 객채 에 있는 좌표 가저와 어느 row 에 있는지 구하기
			
			if (r >= 0 && r < Table.getRowCount()) {//table 안에 들어 있는 경우
            	Table.setRowSelectionInterval(r, r);//row select 를 그 좌표에 설정.
        	} else {//뭔가 잘못됬을 경우
            	Table.clearSelection();//선택 해제
        	}

        	int rowindex = Table.getSelectedRow();
        	if (rowindex < 0)//선택된 row 의 값이 정상범위 밖으로 나간경우.
            	return;
        	
        	if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {//e가 참조하는것이 JTABLE 인지 and 이 마우스 이밴트가 팝업을 부르는지
        		PopupMenu.show(e.getComponent(), e.getX(), e.getY());
        	}
        	
		} else if(SwingUtilities.isLeftMouseButton(e)) {//왼쪽클릭일경우
			String addstr = "";
			int rowindex = Table.getSelectedRow();
			
			if (rowindex < 0)//선택된 row 의 값이 정상범위 밖으로 나간경우.
            	return;
			
			for (int i = 0; i < tablecolumns[columnSet].length; i++) {//메인프레임 아래에 정보표시 택스트에러어에 선택된 레이블 정보표시
				addstr += tablecolumns[columnSet][i] + ":";
				addstr += (String)Table.getValueAt(Table.getSelectedRow(), i);
				addstr += ", ";
			}
			Maneger_view.Ta_obj_info.setText(addstr);//실제로 칼럼 정보 표시부 에 반영
		}
		
		if (e.getSource() instanceof JMenuItem) {//팝업 메뉴 아이탬 클릭시
			switch (columnSet) {//현제 컬럼에 따라 매뉴가 바뀜
			
			case Maneger_view.COLUMN_CLIENT://client
				
				//서비스 사용종료
				if(e.getSource() == JmItems[Maneger_view.COLUMN_CLIENT][0]) {
					Dlgman.getJf_serviceExit((String) Table.getValueAt(Table.getSelectedRow(), 0));
					//현재 선택된 레이블의 id 를 넘김
				}
				
				//새로운 클라이언트
				if(e.getSource() == JmItems[Maneger_view.COLUMN_CLIENT][1]) {
					Dlgman.getJf_NewClient();
				}
				
				//전자키 할당
				if(e.getSource() == JmItems[Maneger_view.COLUMN_CLIENT][2]) {
					Dlgman.getJf_Keyset((String) Table.getValueAt(Table.getSelectedRow(), 0));
				}
				
				break;
				
			case Maneger_view.COLUMN_KEY://digitalkey
				
				//전자키 추가
				if(e.getSource() == JmItems[Maneger_view.COLUMN_KEY][0]) {
					Dlgman.getJf_Keyset();
					//현재 선택된 레이블의 id 를 넘김
				}
				break;
				
			case Maneger_view.COLUMN_LOG://service
				
				//대상 기록 지우기
				if(e.getSource() == JmItems[Maneger_view.COLUMN_LOG][0]) {
					//현재 선택된 레이블의 id 를 넘김
					String id = (String) Table.getValueAt(Table.getSelectedRow(), 0);
					int result = JOptionPane.showConfirmDialog(null, "id : " + id + "기록삭제"
							,"기록 삭제",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					
					if(result == JOptionPane.YES_OPTION) {
						String sqlQ = "Delete from servicelog where orderid = " + id;
						
						if(DB.Update(sqlQ) != 1) {
							JOptionPane.showMessageDialog(null, "기록 삭제 실패!","기록 삭제",JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "적용완료!","기록 삭제",JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				break;
				
			default:
				break;
			}
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if(obj == Btn_close) {//자신을 탭팬 에서 제거
			System.out.println("remove");
			Maneger_view.Pan_root.remove(this);//먼저 탭팬에서 이 객체를 remove
			Maneger_view.list_jpTable.remove(this);//마지막으로 list_jpTable 에서 제거하여 가비지 컬랙션에 대상이 됨.
		}
	}
}
