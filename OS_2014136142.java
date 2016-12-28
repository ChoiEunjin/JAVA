import java.awt.*;
import javax.swing.*;

public class OS_2014136142 extends JFrame{
	
	private static final long serialVersionUID = 1L;
	//static int n = 0;

	JPanel printTimer = new JPanel();
	JTextArea setting;
	Semaphore space = new Semaphore(8);
	
	JLabel getIn;
	JLabel getOut;
	JLabel timerLabel;
	JLabel timer;

	
	OS_2014136142(){
		setTitle("Semaphore1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		timerLabel = new JLabel();
		timerLabel.setFont(new Font("Gothic", Font.ITALIC, 20));
		TimerThread runnable = new TimerThread(timerLabel);
		
		timer = new JLabel();
		Thread th = new Thread(runnable);
		printTimer.add(timerLabel);
		
		setting();
		panel p = new panel();
		p.add(setting);
		add(p);
		add(printTimer, BorderLayout.SOUTH);
		// 위치 설정
			
		setSize(720, 570);
		setVisible(true);
		th.start();
	}
	
	/*
	 * 배경 사진 위에 글자를 출력
	 */
	void setting(){
		
		setting = new JTextArea(100, 64){
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g){
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString(space.nrFull+"", 270, 81);			// nrFull의 값을 출력
				
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString(space.nrEmpty+"", 430, 81);		// nrEmpty의 값을 출력
				
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString(space.mutexP+"", 270, 152);		// mutexP의 값을 출력
				
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString(space.mutexC+"", 430, 152);		// mutexC의 값을 출력
				
				g.setFont(new Font("Arial", Font.ITALIC, 20));
				g.drawString("IN : " + space.in + ", ", 80, 480);// IN의 값을 출력
				
				g.setFont(new Font("Arial", Font.ITALIC, 20));
				g.drawString("OUT : " + space.out, 150, 480);	// OUT의 값을 출력
				
				if(space.buffer[0] != 0){						// 버퍼의 0번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[0], 365, 235);// 버퍼의 0번지 값을 출력
				}
				if(space.buffer[1] != 0){						// 버퍼의 1번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[1], 425, 290);// 버퍼의 1번지 값을 출력	
				}
				if(space.buffer[2] != 0){						// 버퍼의 2번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[2], 427, 370);// 버퍼의 2번지 값을 출력	
				}
				if(space.buffer[3] != 0){						// 버퍼의 3번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[3], 365, 430);// 버퍼의 3번지 값을 출력	
				}
				if(space.buffer[4] != 0){						// 버퍼의 4번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[4], 295, 430);// 버퍼의 4번지 값을 출력	
				}
				if(space.buffer[5] != 0){						// 버퍼의 5번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[5], 240, 370);// 버퍼의 5번지 값을 출력
				}
				if(space.buffer[6] != 0){						// 버퍼의 6번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[6], 240, 290);// 버퍼의 6번지 값을 출력
				}
				if(space.buffer[7] != 0){						// 버퍼의 7번지에 값이 들어 있다면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("M"+space.buffer[7], 295, 235);// 버퍼의 7번지 값을 출력	
				}
				
				
				if(space.fullIsZero){							// 버퍼가 가득 차면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("P"+space.buffer[space.count], 180, 155);	
				
				}
				if(space.emptyIsZero){							// 버퍼에 내용이 없으면
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("C"+space.buffer[space.count], 490, 155);	
				
				}
				setOpaque(false);
				super.paintComponent(g);
			}
		};
	}
	
	/*
	 * 화면에 세마포어의 배경이 되는 그림을 출력
	 */
	class panel extends JPanel{
		
		private static final long serialVersionUID = 1L;
		
		ImageIcon icon = new ImageIcon("C:\\Users\\OWNER\\workspace\\2014136142\\Semaphore.jpg");
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(icon.getImage(), 0, 0, null);
		}
	}
	
	/*
	 * TImerThread 클래스
	 * 화면에 출력되는 시간을 관리한다.
	 */
	class TimerThread extends Thread{
		
		Person p1 = new Person(space, "M1", 5000, 1);		// 생성자 p1
		Person p2 = new Person(space, "M2", 7000, 1);		// 생성자 p1
		Person c1 = new Person(space, "C1", 15000, 0);		// 소비자 c1
		Person c2 = new Person(space, "C2", 12000, 0);		// 소비자 c1
		
		
		JLabel timerLabel;						// 타이머 값이 출력된 레이블
		public TimerThread(JLabel timerLabel){	// 생성자
			this.timerLabel = timerLabel;		// 레이블 컴포넌트를 받아서 기억
		}
		
		// 스레드 코드
		// run() 이 종료하면 스레드 종료
		public void run(){
			int n=0;
			while(true){	// 무한 루프
				timerLabel.setText("Timer : "+Integer.toString(n));	// 타이머 값을 레이블에 출력
				try{
					Thread.sleep(1000);
					if(n == 1)	p1.start();
					if(n == 7)	p2.start();
					if(n == 4)	c1.start();
					if(n == 10)	c2.start();
					n++;
					if(n == 41)	System.exit(0);
					
				}catch(InterruptedException e){
					return;
				}
			}
		}
	}
		
	
	
	class Semaphore
	{
		int mutexP = 1;						// 세마포어에서 mutexP. 초기값은 1
		int mutexC = 1;						// 세마포어에서 mutexC. 초기값은 1
		int nrEmpty = 8;					// 버퍼의 남아 있는 공간. 초기값은 8
		int nrFull = 0;						// 버퍼에서 차 있는 공간. 초기값은 0
		int count = 0;						// 배열 
		int[] buffer = new int[8];			// 버퍼에 내용을 담는 8칸짜리 배열
		int in = 0;							// 버퍼에 추가 가능한 위치값을 가진다.
		int out = 0;						// 버퍼에서 사용 가능한 위치값을 갖는다.
		
		boolean imWaiting = false;			// V 연산을 실행할 때 사용하기 위한 조건
		boolean emptyIsZero = false;		// empty가 0이므로 대기큐에 출력
		boolean fullIsZero = false;			// full이 0이므로 대기큐에 출력
		
		/*
		 * Semaphore 클래스의 생성자이며 배열로 생성한 buffer의 값을 0으로 초기화해준다.
		 */
		public Semaphore(int num)
		{
			for(int i=0; i<8; i++)
				buffer[i] = 0;
		}
		
		/*
		 * P 연산의 역할을 한다.
		 * 입력값으로 받은 n이 0보다 큰 경우 -1을 하며
		 * 0인 경우 대기 큐에 진입하게 된다.
		 */
		public synchronized int semGet(int n) throws InterruptedException		// P 함수
		{
			while(n == 0){
				if(nrEmpty == 0)	emptyIsZero = true;
				if(nrFull == 0)		fullIsZero = true;
				imWaiting = true;
				repaint();
				this.wait();
			}
			n--;
			return n;
		}
		
		/*
		 * V 연산의 역할을 한다.
		 * 대기 큐에 진입중인 쓰레드가 있다면 깨워주고
		 * 
		 */
		public synchronized int semRelease(int n) throws InterruptedException	// V 함수
		{
			if(imWaiting){
				emptyIsZero = false;
				fullIsZero = false;
				imWaiting = false;	
			}
			n++;
			repaint();
			notifyAll();
			return n;
		}
	}
	
	
	class Person extends Thread
	{
		private int waitTime;				// 대기 시간
		private Semaphore space;			// 세마포어 클래스의 객체를 사용
		private int porc;					// Producer인가 Consumer인가를 결정
		
		/*
		 * Person 클래스에 대한 생성자
		 */
		public Person(Semaphore sp, String name, int wt, int p)
		{
			this.setName(name);
			this.space = sp;
			waitTime = wt;
			porc = p;
		}
		
		/*
		 * Person 클래스의 작업을 수행.
		 * Producer인가 Consumer인가에 따라서
		 * run 함수의 작업 내용이 다르다.
		 */
		public void run()
		{
			try{
				while(true)
				{
					if(porc == 1)
					{
						space.mutexP = space.semGet(space.mutexP);		// P(mutexP)
						space.nrEmpty = space.semGet(space.nrEmpty);	// P(nrEmpty)
						repaint();
						
						space.buffer[space.in] = space.count+1;
						space.count++;
						space.in = (space.in + 1) % 8;
						
						Thread.sleep(500);
						
						space.nrFull = space.semRelease(space.nrFull);	// V(nrFull)
						space.mutexP = space.semRelease(space.mutexP);	// V(mutexP)
						repaint();
						Thread.sleep(waitTime);
					}
					if(porc == 0)
					{
						space.mutexC = space.semGet(space.mutexC);
						space.nrFull = space.semGet(space.nrFull);
						repaint();
						
						space.buffer[space.out] = 0;
						space.out = (space.out + 1) % 8;
						
						Thread.sleep(500);
						
						space.nrEmpty = space.semRelease(space.nrEmpty);
						space.mutexC = space.semRelease(space.mutexC);
						repaint();
						Thread.sleep(waitTime);
					}
				}
			}catch(InterruptedException e){e.printStackTrace();}
			
		}
	}

	
	public static void main(String[] args) {
		new OS_2014136142();
	}

}
