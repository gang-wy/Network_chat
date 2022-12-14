package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import GUI.loginClient.ChattingroomFrame;
import GUI.loginClient.ChattingroomFrame.SingleChattingRoomFrameWindowClosingEventHandler;





public class loginClient {
	User us = null;
	Friends fd = null;
	FriendUser fus = null;
	LoginFrame lf = null;
	CheckFrame ckf = null;
	ChangeFrame chf = null;
	JoinFrame jf = null;
	MainFrame mf = null;
	SearchFrame srf = null;
	ProfileFrame prf = null;
	ProfileFriendFrame prff = null; 
	ChattingroomFrame ctf;

	
	String ID;
	String friendID; // 친구 아이디

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	String type = null;
	boolean check = false;
	String mes = null;
	
	
	public void connectServer() {

		try {
			// socket 객체를 생성하여 IP 주소와 포트번호 전달->서버 접속시도
			socket = new Socket("localhost", 59876);

			// DataInputStream 객체 생성 후 전달받은 접속요청 결과 출력
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiveMessage();

	}

	public static String getSHA512(String input) {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	public void inputMes(String mes) {
		this.mes = mes;
		sendMessage();
		System.out.println(1);
	}

	public void inputCheck(boolean check) {
		this.check = check;
		sendCheck();
	}

	public void receiveMessage() {
		// 멀티 쓰레딩으로 메세지 수신 처리 작업 수행
		String temp = null;
		try {
			while (true) {
				temp = in.readUTF();
				StringTokenizer st = new StringTokenizer(temp, " ");
				type = st.nextToken();
				System.out.println(1);
				if (type.equals("lg")) {// login
					String istrue = st.nextToken();
					System.out.println(2);
					if (istrue.equals("true")) {
						lf.check = true;
						lf.signal = true;

					} else {
						lf.check = false;
						lf.signal = true;

					}
					System.out.println(3);
				} else if (type.equals("ck")) {// check
					String istrue = st.nextToken();
					System.out.println(3);
					if (istrue.equals("true")) {
						ckf.check = true;
						ckf.signal = true;

					} else {
						ckf.check = false;
						ckf.signal = true;
					}
					System.out.println(4);
				} else if (type.equals("cg")) {// change
					String istrue = st.nextToken();
					System.out.println(33);
					if (istrue.equals("true")) {
						chf.check = true;
						chf.signal = true;
						System.out.println(chf.signal + "왜");
					} else {
						chf.check = false;
						chf.signal = true;
					}
					System.out.println(4);
				} else if (type.equals("su")) {// join
					String istrue = st.nextToken();
					System.out.println(33);
					if (istrue.equals("true")) {
						jf.check = true;
						jf.signal = true;
						System.out.println(jf.signal + "왜");
					} else {
						jf.check = false;
						jf.signal = true;
					}
					System.out.println(4);
				} else if (type.equals("us")) {// 유저정보 저장
					us.ID = st.nextToken();
					us.name = st.nextToken();
					us.mail = st.nextToken();
					us.birth = st.nextToken();
					us.comment = st.nextToken();
					us.login = st.nextToken();
					us.logouttime = st.nextToken();
					lf.signal = true;
					

				} else if (type.equals("usm")) {// 유저정보 저장(메인프레임에서 사용)
					us.ID = st.nextToken();
					us.name = st.nextToken();
					us.mail = st.nextToken();
					us.birth = st.nextToken();
					us.comment = st.nextToken();
					us.login = st.nextToken();
					us.logouttime = st.nextToken();
					
					mf.signal = true;

				} else if (type.equals("usf")) {// 친구정보 저장
					fus.ID = st.nextToken();
					fus.name = st.nextToken();
					fus.mail = st.nextToken();
					fus.birth = st.nextToken();
					fus.comment = st.nextToken();
					fus.login = st.nextToken();
					fus.logouttime = st.nextToken();
					
					mf.signal = true;

				} else if (type.equals("ui")) {// 유저 접속했다고 알림
					String istrue = st.nextToken();
					System.out.println(3);
					if (istrue.equals("true")) {
						lf.signal = true;

					} else {
						lf.signal = true;
					}
					System.out.println(4);
				} else if (type.equals("sr")) {// 서치한 정보 가져오기
					srf.mes = temp;
					srf.signal = true;

				} else if (type.equals("si")){ // 서치한 사람 정보 가져오기
					fus.ID = st.nextToken();
					fus.name = st.nextToken();
					fus.mail = st.nextToken();
					fus.birth = st.nextToken();
					fus.comment = st.nextToken();
					fus.login = st.nextToken();
					fus.logouttime = st.nextToken();
					srf.signal = true;
				} else if (type.equals("fi")) {// 친구 정보 가져오기
					prf.ID = st.nextToken();
					prf.Name = st.nextToken();
					prf.Mail = st.nextToken();
					prf.Birth = st.nextToken();
					prf.Comment = st.nextToken();
					prf.Login = st.nextToken();
					prf.Logouttime = st.nextToken();
					prf.signal = true;

				} else if (type.equals("nf")) {// 온라인 친구 리스트 가져오기
					fd.onFriend = temp;
					lf.signal = true;
					System.out.println(4444);

				} else if (type.equals("ff")) {// 오프라인 친구 리스트 가져오기
					fd.offFriend = temp;
					lf.signal = true;

				}else if (type.equals("pf")) {// 친구추가
		            String istrue = st.nextToken();
		            if (istrue.equals("true")) {
		            	srf.check = true;
		                srf.signal = true;

		            } else {
		                srf.check = false;
		                srf.signal = true;
		            }
		               
		        }
				else if (type.equals("nfm")) {// 온라인 친구 리스트 가져오기 (메인프레임 리프레쉬 버튼)
		               fd.onFriend = temp;
		               
		               mf.signal = true;
		               System.out.println(4444);

		            } else if (type.equals("ffm")) {// 오프라인 친구 리스트 가져오기 (메인프레임 리프레쉬 버튼)
		               fd.offFriend = temp;
		               
		               mf.signal = true;

		        }
				
				else if(type.contains("++connect++")) { // 연결이 되었다는 식별코드
					String name = st.nextToken();
					ctf = new ChattingroomFrame(this, ID, name); // chattingframe 활성화
					
					
				}
				
				else if(type.contains("++invite++")) { // 초대 신청을 뜻하는 식별자
					String name = st.nextToken();
					
					int result = JOptionPane.showConfirmDialog(null, ID+"님 "+name+"님의 초대를 받겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) //예를 누르면
					{
						inputMes("++response++"+" "+"++yes++"+" "+name+" "+ID); 
						
						ctf = new ChattingroomFrame(this, ID, name); // 채팅창 시작
						
					}
					
					else
					{
						inputMes("++response++"+" "+"++no++"+name+" "+ID);  // 아니면 거절의 의미를 보낸다.
					}							
				}
				
				else if(type.contains("++endChat++")) // 채팅을 끝낸다는 신호
				{
					StringTokenizer ST= new StringTokenizer(temp, "//");
					String identifier = ST.nextToken();
					String msg = ST.nextToken();
		
					if(msg.contains(ctf.FriendName))
							ctf.AppendMessage(msg);
											
				}
				
				else if(type.contains("++fail++")) {
					JOptionPane.showConfirmDialog(null, "error occur", "error", JOptionPane.ERROR_MESSAGE);
				}
				
				else if(type.contains("++refuse++")) // 채팅거절로 실패
				{ // 변경점
					JOptionPane.showConfirmDialog(null, "연결 실패", "error", JOptionPane.ERROR_MESSAGE);
				}
				
				
				else if(type.contains("++msgSingle++")) // 채팅으로 인한 메시지 
				{
					StringTokenizer ST= new StringTokenizer(temp, "//");
					String identifier = ST.nextToken();
					String msg = ST.nextToken();
						
					
					if(msg.contains(ctf.FriendName)) 
						ctf.AppendMessage(msg);												
				}
				
				else if(type.contains("++fileRequest++")) // 파일을 받을지 요청하는 식별자
				{
					String name = st.nextToken();
					int result = JOptionPane.showConfirmDialog(null, ID+"님 "+name+"님의 파일을 받겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION)
					{
						inputMes("++fileResponse++"+" "+"++yes++"+" "+name+" "+ID); 
						
					}
					
					else
					{
						inputMes("++fileresponse++"+" "+"++no++"+name+" "+ID); 
					}							
				}
				
				else if(type.contains("++fileStart++")) { // 파일 전송을 시작한다.
					
					int data;
					byte[] buffer= new byte[2048]; // 이 버퍼로 데이터를 읽는다
					JFileChooser FC = new JFileChooser(); // 파일을 선택한다
					int result = FC.showOpenDialog(null);
					if(result == JFileChooser.APPROVE_OPTION) 
					{
						File selectedFile = FC.getSelectedFile();  // 선택한 파일을 보낼준비를 한다.
						FileInputStream fis = new FileInputStream(selectedFile); 
						BufferedInputStream bis = new BufferedInputStream(fis); // 이 소켓으로 파일을 보낸다.
						try
						{								
							System.out.println(selectedFile); 
							inputMes("++File++"+" "+selectedFile.getName()+" "+ctf.reciever); // 파일을 받을 친구를 저장해 놓느나.
							
							while((data = bis.read(buffer))!=-1) //데이터를 모두 읽어내기 까지
							{
								out.writeInt(data);
								out.write(buffer, 0, data);
								out.flush();
							
							}
							out.writeInt(-1); // 전부 끝났다고 -1을 반대편에 보내준다.
							fis.close();
							bis.close();
					
						}catch(Exception e){JOptionPane.showConfirmDialog(null, "파일전송 실패", "파일전송", JOptionPane.ERROR_MESSAGE);}
						
						JOptionPane.showConfirmDialog(null, ctf.reciever+"파일전송 완료", "파일전송", JOptionPane.PLAIN_MESSAGE);
						
					}
					
				}
				
				else if(type.contains("++file++")) { // 해당 식별자에서 서버에게 파일을 받는다.
					String FileName = st.nextToken(); 					
					getFile(FileName);													
					JOptionPane.showConfirmDialog(null, FileName+" download complete", "다운로드 완료", JOptionPane.PLAIN_MESSAGE);
				}  // 파일을 성공적으로 받았다고 알린다.
				
				
				
			}
		} catch (EOFException e) {
			// 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
			System.out.println("서버 접속이 해제되었습니다.\n");
			System.out.println("서버 연결 상태 : 미연결");
		} catch (SocketException e) {
			System.out.println("서버 접속이 해제되었습니다.\n");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			System.out.println("해제되었습니다.\n");

		}

	}
	
	public void getFile(String FileName) // 파일을 받는 함수
	{
		try 
		{
			File file = new File("c:/Users/"+FileName); //C: 사용자에 파일이 저장됨.
			byte[] buffer = new byte[2048]; // 데이터를 받을 버퍼
			int data;
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			System.out.println("file download"); 
			while((data=in.readInt())!=-1){ // 서버로 부터 -1을 받으면 데이터를 모두 보낸 것이니 while문을 종료한다.
				in.read(buffer, 0, data);
				bos.write(buffer, 0, data); 
				bos.flush();
			}
        
			fos.close();
			bos.close();
		}catch(Exception e) {e.printStackTrace();}
	}

	public void sendMessage() { // 서버에게 메시지를 보내는 중요한 함수
		try {
			String text = mes;
			System.out.println(1);

			// 클라이언트에게 메세지 전송
			out.writeUTF(text);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCheck() {
		try {
			boolean text = check;

			out.writeBoolean(text);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setChat(ChattingroomFrame ct) { // 채팅룸을 세팅
		this.ctf = ct;
	}

	public static void main(String[] args) {
		loginClient opt = new loginClient();
		opt.us = new User();
		opt.fd = new Friends();
		opt.fus = new FriendUser();
		opt.ckf = new CheckFrame(opt);
		opt.chf = new ChangeFrame(opt);
		opt.srf = new SearchFrame(opt);
		opt.jf = new JoinFrame(opt);
		
		opt.lf = new LoginFrame(opt);
		opt.connectServer();

	}
	
	class ChattingroomFrame extends JFrame implements ActionListener, KeyListener { // 메시지를 받는 class안쪽에 넣어야 append가 기능함
		
			MainFrame mf;
			String NickName;
			String FriendName;
			String msg;
			String reciever;
			private JPanel contentPane = new JPanel();
			private JLabel User = new JLabel(NickName);
			private JTextField outputField;
			private JButton SendBtn;
			private JButton FileBtn;
			private JTextArea inputArea;
			private JFileChooser fileChooser;
			
			boolean signal = false;
			loginClient O = null; // 변경점
			
			public ChattingroomFrame(loginClient o, String id, String fr)
			{
				NickName = id;
				FriendName =fr;
				O = o;
				setChat(this);
				setSize(276,350);
				setLocation(574, 426);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				inputArea = new JTextArea(); 
				inputArea.setBounds(12, 43, 234, 429);
				JScrollPane scroll = new JScrollPane(inputArea);
				scroll.setSize(234, 250);
				scroll.setLocation(12, 10);
				contentPane.add(scroll);
				
				outputField = new JTextField();
				outputField.setBounds(12, 270, 144, 34);
				contentPane.add(outputField);
				outputField.setColumns(10);
				outputField.addActionListener(this);

				setTitle(NickName +" client");
				setResizable(false);
							
				SendBtn = new JButton("o"); 
				SendBtn.setBounds(160, 270, 41, 34);
				contentPane.add(SendBtn);
				SendBtn.addActionListener(this);
				
				FileBtn = new JButton("F");
				FileBtn.setBounds(205, 270, 41, 34);
				contentPane.add(FileBtn);
				FileBtn.addActionListener(this);

				inputArea.setEditable(false); 
				outputField.addKeyListener(this);
																	
				contentPane.add(User);			
				setVisible(true);			
				this.addWindowListener(new SingleChattingRoomFrameWindowClosingEventHandler());
			}
			
			
			

			class SingleChattingRoomFrameWindowClosingEventHandler extends WindowAdapter 
			{
					public void windowClosing(WindowEvent e) 
					{
						JFrame frame = (JFrame)e.getWindow();
						frame.setVisible(false);
						O.inputMes("++endChat++"+" "+NickName+" "+FriendName);
						frame.dispose();
					}
			}

			public void actionPerformed(ActionEvent e) 
			{ 
				
				String Message = outputField.getText().trim(); //메시지를 보낸다
				if (e.getSource() == SendBtn && Message.length() > 0) 
				{
					O.inputMes("++msgSingle++"+"//"+NickName+"//"+FriendName+"//"+Message+"\n");
					outputField.setText(null);
					inputArea.append(NickName+" > "+Message+"\n");
				}
						
				else if(e.getSource() == FileBtn) //파일을 보내는 버튼
				{
					String friendName = JOptionPane.showInputDialog("파일을 보낼 친구를 입력하세요");					
					O.inputMes("++fileRequest++"+" "+NickName+" "+friendName); 
					reciever = friendName;				
				}				
			}

			public void keyPressed(KeyEvent e) // 메시지를 보내는 함수
			{ 				
				
				String Message = outputField.getText().trim();
				if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) 
				{
					O.inputMes("++msgSingle++"+"//"+NickName+"//"+FriendName+"//"+Message+"\n");
					outputField.setText(null);
					inputArea.append(NickName+" > "+Message+"\n");
				}
			}

			public void AppendMessage(String Message) // 메시지를 받아서 textarea에 출력하는 함수
			{
				
					inputArea.append(Message);		
					
				
			}
			 

			public void keyTyped(KeyEvent e){ 
			}

			public void keyReleased(KeyEvent e) { 
			}

			
				
		}
	

	
	
	
	
	
	
}
