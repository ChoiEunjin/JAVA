import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Date extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JPanel set = new JPanel();
	JPanel date = new JPanel();
	JPanel msg = new JPanel();
	// 패널 생성

	JLabel putDate = new JLabel();
	JButton plus = new JButton("┼");
	JButton prevYear = new JButton("<<");
	JButton prevMon = new JButton("<");
	JButton nextMon = new JButton(">");
	JButton nextYear = new JButton(">>");
	JButton today = new JButton("Today");
	JButton list = new JButton("List");
	// Panel 1

	JLabel[] w = new JLabel[7];
	JButton[] btnArr = new JButton[42];
	int[] dateArr = new int[42];

	Calendar curMon = Calendar.getInstance();
	// Panel 2

	JLabel time = new JLabel();
	Calendar now = Calendar.getInstance();
	// Panel 3

	int m = 0;
	int y = 0;
	int scheduledDay = 0;

	private BufferedReader br = null;
	private BufferedWriter bw = null;
	
	private BufferedReader[] d = new BufferedReader[4];
	private BufferedWriter[] dd = new BufferedWriter[4];

	// 파일 입출력
	DefaultTableModel model;
	JTable table;
	JScrollPane scroll;
	// JTable 사용

	addSchedule sc;

	Date() {
		setTitle("Scheduler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Title & EXIT 설정

		set.setBackground(new Color(255, 185, 185));
		set.add(plus);
		set.add(prevYear);
		set.add(prevMon);
		set.add(putDate);
		set.add(nextMon);
		set.add(nextYear);
		set.add(today);
		set.add(list);

		plus.addActionListener(this);
		plus.setBackground(new Color(255, 235, 225));
		//plus
		today.addActionListener(this);
		today.setBackground(new Color(255, 235, 225));
		//today
		list.addActionListener(this);
		list.setBackground(new Color(255, 235, 225));
		//list
		prevMon.addActionListener(this);
		prevMon.setBackground(new Color(255, 243, 212));
		nextMon.addActionListener(this);
		nextMon.setBackground(new Color(255, 243, 212));
		//Mon
		prevYear.addActionListener(this);
		prevYear.setBackground(new Color(255, 243, 212));
		nextYear.addActionListener(this);
		nextYear.setBackground(new Color(255, 243, 212));
		//Year
		// Panel 1 : 설정(Set)

		date.setLayout(new GridLayout(7, 6));
		date.setBackground(Color.lightGray);
		String[] week = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu",
				"Fri", "Sat" };

		for (int i = 0; i < week.length; i++) {
			w[i] = new JLabel("       " + week[i] + "      ");
			if (i == 0)
				w[i].setForeground(Color.red);
			if (i == 6)
				w[i].setForeground(Color.blue);
			date.add(w[i]);
		}
		// 요일 추가

		for (int i = 0; i < btnArr.length; i++) {
			btnArr[i] = new JButton("");
			date.add(btnArr[i]);
		}
		// 버튼 추가
		// Panel 2 : 달력(Date)

		msg.add(time);
		// Panel 3 : 메시지 (Message)

		add(set, BorderLayout.NORTH);
		add(date, BorderLayout.CENTER);
		add(msg, BorderLayout.SOUTH);
		// 위치 설정
		
		
		ChangeColor(putDate, btnArr);
		
		setSize(550, 350);
		getButton(curMon);
		getDays(curMon);
		getToday(now);
		setVisible(true);
	}

	void getButton(Calendar date) {
		Calendar date1 = Calendar.getInstance();
		date1.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
		date1.add(Calendar.DATE, -date1.get(Calendar.DAY_OF_WEEK) + 1);

		for (int i = 0; i < btnArr.length; i++) {
			//int day = date1.get(Calendar.DATE);
			if (date1.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
				btnArr[i].addActionListener(sc);
				btnArr[i].addActionListener(this);
			}
			date1.add(Calendar.DATE, 1);
		}
	}
	// 버튼 액션리스너 설정
	
	void getDays(Calendar date) {
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);

		if (month < 9)
			putDate.setText(year + "년 0" + (month + 1) + "월");
		else
			putDate.setText(year + "년 " + (month + 1) + "월");

		Calendar sDay = Calendar.getInstance();
		sDay.set(year, month, 1);
		// sDay를 입력월의 1일로 설정
		sDay.add(Calendar.DATE, -sDay.get(Calendar.DAY_OF_WEEK) + 1);
		// sDay.get(Calendar.DAY_OF_WEEK) : 1일의 요일을 알아냄

		for (int i = 0; i < btnArr.length; i++) {
			int day = sDay.get(Calendar.DATE);
			btnArr[i].setText(day + "");
			dateArr[i] = day;
			if (i % 7 == 0)
				btnArr[i].setForeground(Color.red);
			else if (i % 7 == 6)
				btnArr[i].setForeground(Color.blue);
			else
				btnArr[i].setForeground(Color.BLACK);
			
			
			if (sDay.get(Calendar.MONTH) == month)
				btnArr[i].setBackground(Color.WHITE);
			else{
				btnArr[i].setBackground(Color.LIGHT_GRAY);
				btnArr[i].setForeground(Color.BLACK);
			}
			sDay.add(Calendar.DATE, 1);
		} // for

		// ChangeColor 메소드 호출해야함
		ChangeColor(putDate, btnArr);
		// 버튼에 날짜 삽입.
	}
	// 날짜 출력
	
	void getToday(Calendar date) {
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int today = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		time.setText("Now : " + year + "년 " + (month + 1) + "월 " + today + "일 " + hour + ":" + minute);
	}
	//날짜출력
	
	void ChangeColor(JLabel putDate, JButton[] btnArr) {

		String[] strLine = new String[4];
		Color[] foreColors = {new Color(206, 8, 138), new Color(8, 192, 25), new Color(55, 8, 206), new Color(205, 207, 21)};
		Color[] backColors = {new Color(255, 225, 235), new Color(229, 251, 195), new Color(229, 237, 255), new Color(254, 241, 185)};
		
		try {
			for (int i = 0; i < 4; i++) {
				d[i] = new BufferedReader(new FileReader("Data_"+(i+1)+".txt"));
				
				while ((strLine[i] = d[i].readLine()) != null) {
					String tok[] = strLine[i].split("/");
					//String strData[] = { tok[0], tok[1], tok[2], tok[3], tok[4], tok[5] };

					// 파일의 년월일 구하기 -- tok[1]
					String fyear = tok[1].substring(0, 4);
					String fmonth = tok[1].substring(4, 6);
					String fday = tok[1].substring(6, 8);
					
					// 스케줄러의 년월일 구하기 -- putDate에서 년월, btnArr에서 일
					String syear = putDate.getText().substring(0, 4);
					String smonth = putDate.getText().substring(6, 8);
					
					// 파일의 년월일, 스케줄러의 년월일 비교
					for (int index = 0; index < btnArr.length; index++) {
						String sday = btnArr[index].getText();
						if(Integer.parseInt(sday)<10)
							sday = "0"+sday;
						
						//스케줄러의 날짜 구해오기
						
						if (fyear.equals(syear)
								&& fmonth.equals(smonth)&& fday.equals(sday)
								&& btnArr[index].getBackground() != Color.LIGHT_GRAY) {
							
							
							if (tok[2].isEmpty()) { // 공백이면 (1일짜리 일정)
								// 해당일자에 setForeground();
								btnArr[index].setForeground(foreColors[i]);

							} else { // 연결일정 

								// 종료일자까지 해당일자에 setBackground();
								// 함정 : 이전달에서 다음달로 넘어가는 일정은 표시 안됨.
								String eday = tok[2].substring(6, 8);
								int intsday = Integer.parseInt(sday);
								int inteday = Integer.parseInt(eday);
								
								int add = 0;
								for(int j=0; j<10; j++){
									if(dateArr[j] == 1){
										add = j-1;
										break;
									}
								}
								for (int btnIndex = intsday; btnIndex <= inteday; btnIndex++) {
									btnArr[btnIndex+add].setBackground(backColors[i]);

								} // for
								
							} // if~else
							
						} // if
					} // for
				} // while

				d[i].close();
			} // for

		} catch (Exception e) {
		}

	}
	// 색상바꾸는 메소드

	public void actionPerformed(ActionEvent e) {
		JButton src = (JButton) e.getSource();
		int add = 0;
		if (src == prevMon) {
			curMon.add(Calendar.MONTH, -1);
			getDays(curMon);
			m--;
		}// 전 달로 이동
		if (src == nextMon) {
			curMon.add(Calendar.MONTH, 1);
			getDays(curMon);
			m++;
		}// 다음 달로 이동
		if (src == prevYear) {
			curMon.add(Calendar.MONTH, -12);
			getDays(curMon);
			y--;
		}// 저번 해로 이동
		if (src == nextYear) {
			curMon.add(Calendar.MONTH, 12);
			getDays(curMon);
			y++;
		}// 다음 해로 이동
		if (src == today) {
			curMon.add(Calendar.MONTH, -m);
			curMon.add(Calendar.YEAR, -y);
			m = 0;
			y = 0;
			
			btnArr[curMon.get(Calendar.DATE)].requestFocusInWindow();
			getDays(curMon);
			
			for(int i=0; i<10; i++){
				if(dateArr[i] == 1){
					add = i-1;
					break;
				}
			}
			btnArr[curMon.get(Calendar.DAY_OF_MONTH)+add].setBackground(new Color(177, 236, 243));
		}// 오늘 날짜로 이동 + 배경색 추가
		if (src == list)
			new openCatalog();
		// 목록열기
		if (src == plus)
			new addSchedules();
		// 일정추가하기
		for (int i = 0; i < 42; i++) {
			if (src == btnArr[i]) {
				int sYear = curMon.get(Calendar.YEAR);
				int sMonth = curMon.get(Calendar.MONTH) + 1;
				int sDay = Integer.parseInt(btnArr[i].getText());
				scheduledDay = sYear * 10000 + sMonth * 100 + sDay;
				new addSchedule();
			}
		}// 일정 입력 창 출력
		repaint();
	}

	class addSchedules extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;
		JPanel date = new JPanel();
		JPanel main = new JPanel();
		JPanel button = new JPanel();
		// 패널 설정

		JTextField tfStartTime = new JTextField();
		JTextField tfEndTime = new JTextField();
		JTextField tfMemo = new JTextField();
		// 텍스트필드 설정

		JTextField startYear = new JTextField(2);
		JTextField endYear = new JTextField(2);
		JTextField startMonth = new JTextField(2);
		JTextField endMonth = new JTextField(2);
		JTextField startDay = new JTextField(2);
		JTextField endDay = new JTextField(2);
		int start = 0;
		int end = 0;
		// 날짜 입력

		String[] st = new String[] { "♥", "♣", "♠", "◆" };
		JComboBox sticker = new JComboBox(st);

		JButton save = new JButton("Save");

		addSchedules() {
			setTitle("일정 추가");
			setLocation(550, 0);

			main.setBackground(Color.white);
			GridLayout grid1 = new GridLayout(2, 7);
			date.setLayout(grid1);

			GridLayout grid2 = new GridLayout(4, 2);
			main.setLayout(grid2);
			grid2.setVgap(5);
			// 화면설정

			date.add(new JLabel("시작 일자 : "));
			date.add(startYear);
			date.add(new JLabel("년"));
			date.add(startMonth);
			date.add(new JLabel("월"));
			date.add(startDay);
			date.add(new JLabel("일"));

			date.add(new JLabel("종료 일자 : "));
			date.add(endYear);
			date.add(new JLabel("년"));
			date.add(endMonth);
			date.add(new JLabel("월"));
			date.add(endDay);
			date.add(new JLabel("일"));
			date.setBackground((new Color(255, 185, 185)));
			// Panel 1

			main.add(new JLabel("선택 : "));
			main.add(sticker);
			main.add(new JLabel("내용 : "));
			main.add(tfMemo);
			main.setBackground(Color.white);
			// Panel 2
			save.addActionListener(this);
			button.add(save);
			// Panel 3

			add(date, BorderLayout.NORTH);
			add(main, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
			setSize(450, 250);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {

			int sy = Integer.parseInt(startYear.getText());
			int sm = Integer.parseInt(startMonth.getText());
			int sd = Integer.parseInt(startDay.getText());
			start = sy * 10000 + sm * 100 + sd;

			int ey = Integer.parseInt(endYear.getText());
			int em = Integer.parseInt(endMonth.getText());
			int ed = Integer.parseInt(endDay.getText());
			end = ey * 10000 + em * 100 + ed;

			try {
				if (sticker.getSelectedIndex() == 0)
					bw = new BufferedWriter(new FileWriter("Data_1.txt", true));
				else if (sticker.getSelectedIndex() == 1)
					bw = new BufferedWriter(new FileWriter("Data_2.txt", true));
				else if (sticker.getSelectedIndex() == 2)
					bw = new BufferedWriter(new FileWriter("Data_3.txt", true));
				else
					bw = new BufferedWriter(new FileWriter("Data_4.txt", true));
				bw.write(sticker.getSelectedItem() + "/");
				bw.write(start + "/");
				bw.write(end + "/");
				bw.write("/");
				bw.write("/");
				bw.write(tfMemo.getText() + "/"); // 슬래쉬 문자로 데이터를 구분해서 저장
				bw.write("\n");
				bw.close();
				//String strData[] = { st[sticker.getSelectedIndex()],
				//		Integer.toString(start), Integer.toString(end),"", "", tfMemo.getText() };
			} catch (IOException ex) { // 입출력 동작 실패 또는 인터럽트 시 발생
			}
			ChangeColor(putDate, btnArr);
			dispose();
		}
	}
	// 여러 일정을 추가하는 클래스
	
	class addSchedule extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;
		JPanel date = new JPanel();
		JPanel main = new JPanel();
		JPanel button = new JPanel();
		// 패널 설정
		JTextField tfStartTime = new JTextField();
		JTextField tfEndTime = new JTextField();
		JTextField tfMemo = new JTextField();
		// 텍스트필드 설정
		String[] st = new String[] { "♥", "♣", "♠", "◆" };
		JComboBox sticker = new JComboBox(st);
		JButton save = new JButton("Save");

		addSchedule() {
			setTitle("일정 추가");
			setLocation(550, 0);

			main.setBackground(Color.white);
			GridLayout grid = new GridLayout(4, 2);
			main.setLayout(grid);
			grid.setVgap(5);
			// 화면설정

			date.add(new JLabel(scheduledDay + ""));
			date.setBackground((new Color(255, 185, 185)));
			// Panel 1
			main.add(new JLabel("선택 : "));
			main.add(sticker);

			main.add(new JLabel("시작 시간 : "));
			main.add(tfStartTime);
			main.add(new JLabel("종료 시간 : "));
			main.add(tfEndTime);
			main.add(new JLabel("내용 : "));
			main.add(tfMemo);
			main.setBackground(Color.white);
			// Panel 2
			save.addActionListener(this);
			button.add(save);
			// Panel 3

			add(date, BorderLayout.NORTH);
			add(main, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
			setSize(300, 200);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			try {
				if (sticker.getSelectedIndex() == 0)
					bw = new BufferedWriter(new FileWriter("Data_1.txt", true));
				else if (sticker.getSelectedIndex() == 1)
					bw = new BufferedWriter(new FileWriter("Data_2.txt", true));
				else if (sticker.getSelectedIndex() == 2)
					bw = new BufferedWriter(new FileWriter("Data_3.txt", true));
				else
					bw = new BufferedWriter(new FileWriter("Data_4.txt", true));
				bw.write(sticker.getSelectedItem() + "/");
				bw.write(scheduledDay + "/");
				bw.write("/");
				bw.write(tfStartTime.getText() + "/");
				bw.write(tfEndTime.getText() + "/");
				bw.write(tfMemo.getText() + "/"); // 슬래쉬 문자로 데이터를 구분해서 저장
				bw.write("\n");
				bw.close();
				String strData[] = { st[sticker.getSelectedIndex()],
						Integer.toString(scheduledDay), "  ",
						tfStartTime.getText(), tfEndTime.getText(),
						tfMemo.getText() };
			} catch (IOException ex) { // 입출력 동작 실패 또는 인터럽트 시 발생
			}
			ChangeColor(putDate, btnArr);
			dispose();
		}
	}
	// 하루단위 일정 추가하는 클래스
	
	class openCatalog extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;
		JPanel main = new JPanel();
		JPanel message = new JPanel();
		// 패널 설정
		String[] st = new String[] { "♥", "♣", "♠", "◆" };
		JComboBox sticker = new JComboBox(st);
		JButton print = new JButton("출력");
		JButton delete = new JButton("삭제");
		
		ArrayList<String> al_1 = new ArrayList<String>();
		ArrayList<String> al_2 = new ArrayList<String>();
		ArrayList<String> al_3 = new ArrayList<String>();
		ArrayList<String> al_4 = new ArrayList<String>();
		
		openCatalog() {
			setTitle("일정 출력");
			setLocation(550, 0);
			main.setBackground(Color.white);
			// 화면 설정

			main.add(sticker);
			main.add(print);
			main.add(delete);
			// main 패널

			model = new DefaultTableModel();
			model.addColumn("분류");
			model.addColumn("시작 날짜");
			model.addColumn("종료 날짜");
			model.addColumn("시작 시간");
			model.addColumn("종료 시간");
			model.addColumn("내용");
			table = new JTable(model);
			scroll = new JScrollPane(table);
			// 테이블 출력

			print.addActionListener(this);
			delete.addActionListener(this);
			add(main, BorderLayout.NORTH);
			add(scroll, BorderLayout.CENTER);
			add(message, BorderLayout.SOUTH);
			setSize(450, 400);
			setVisible(true);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			JButton src = (JButton) ae.getSource();
			int n = sticker.getSelectedIndex() + 1;
			int size;
			
			if (src == print) {
				model.setNumRows(0);
				try {
					br = new BufferedReader(new FileReader("Data_"+n+".txt"));				
					String strLine;
					
					while ((strLine = br.readLine()) != null) {
						String tok[] = strLine.split("/");
						String strData[] = { tok[0], tok[1], tok[2], tok[3], tok[4], tok[5] };
						model.addRow(strData);
						if(n == 1) al_1.add(strLine + "\n");
						else if(n == 2) al_2.add(strLine + "\n");
						else if(n == 3) al_3.add(strLine + "\n");
						else al_4.add(strLine + "\n");
					}
					br.close();
				} catch (IOException ex) {
				}
			}
			//delete 눌렀을 경우
			if (src == delete) {
				int row = table.getSelectedRow();
	            if(row<0) return; // 선택이 안된 상태면 -1리턴
	            DefaultTableModel model = (DefaultTableModel)table.getModel();
	            //System.out.println(model.getValueAt(row, 0));
	            model.removeRow(row);
	            //테이블에서 제거
	            
	            if(n == 1){
					al_1.remove(row);
					size = al_1.size();
	            }
				else if(n == 2){
					al_2.remove(row);
					size = al_2.size();
				}
				else if(n == 3){
					al_3.remove(row);
					size = al_3.size();
				}
				else{
					al_4.remove(row);
					size = al_4.size();
				}
				//ArrayList에서 제거
	            
	            
	            //이제 여기서부터 파일에 어레이리스트 새로추가.
	            try {
	            	bw = new BufferedWriter(new FileWriter("Data_"+n+".txt", false));
					model.setNumRows(0);
					//bw.write("");
					bw.close();
					//파일비우기
					
					dd[n-1] = new BufferedWriter(new FileWriter("Data_"+n+".txt", true));	
					String strLine = null;
					//파일읽기및출력
					for(int i=0; i<size; i++) {
						if(n == 1) strLine = al_1.get(i);
						else if(n == 2) strLine = al_2.get(i);
						else if(n == 3) strLine = al_3.get(i);
						else strLine = al_4.get(i);
						
						String tok[] = strLine.split("/");
						String strData[] = { tok[0], tok[1], tok[2], tok[3], tok[4], tok[5] };
						dd[n-1].write(strLine);
						model.addRow(strData);
					}
					dd[n-1].close();
				} catch (IOException ex) {
				}
				getDays(curMon);
			} // if(delete)
		}
	}
	// 일정 목록 출력하는 클래스

	
	public static void main(String[] args) {
		new Date();
	}
}
