package GUI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JPanel loginPane;
	private JLabel lbLogo;
	private JLabel lbID;
	private JLabel lbPW;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton loginBtn, joinBtn;
	private JButton ChangePWBtn;

	boolean check;
	boolean signal = false;
	loginClient o = null;

	public LoginFrame(loginClient opt) {
		o = opt;

		setTitle("Messenger");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Image/Logo.png")));
		
		Font font = new Font("맑은 고딕", Font.BOLD, 12);
		Font hint_font = new Font("Gulim", Font.ITALIC, 12);
		Font normal_font = new Font("Gulim", Font.PLAIN, 12);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 250, 274, 576);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon(LoginFrame.class.getResource("/Image/Logo.png")));
		lbLogo.setBounds(77, 51, 98, 97);
		lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lbLogo);

		JPanel loginPane = new JPanel();
		loginPane.setBackground(new Color(255, 255, 255));
		loginPane.setBounds(25, 205, 210, 211);
		contentPane.add(loginPane);
		loginPane.setLayout(null);

		tfID = new JTextField();
		tfID.setBounds(46, 0, 164, 49);
		tfID.setText("아이디 입력");
		tfID.setFont(hint_font);
		tfID.setForeground(Color.GRAY);
		loginPane.add(tfID);
		tfID.addFocusListener(new FocusListener() {	// id 필드 힌트 설정

			@Override
			public void focusLost(FocusEvent e) {
				if (tfID.getText().equals("")) {
					tfID.setText("아이디 입력");
					tfID.setFont(hint_font);
					tfID.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (tfID.getText().equals("아이디 입력")) {
					tfID.setText("");
					tfID.setFont(normal_font);
					tfID.setForeground(Color.BLACK);
				}
			}
		});

		tfPassword = new JPasswordField();
		tfPassword.setBounds(46, 54, 164, 49);
		loginPane.add(tfPassword);
		tfPassword.setColumns(10);
		tfPassword.setEchoChar('*');

		lbID = new JLabel("ID:");
		lbID.setFont(new Font("굴림", Font.BOLD, 16));
		lbID.setHorizontalAlignment(SwingConstants.CENTER);
		lbID.setBounds(0, 0, 34, 49);
		loginPane.add(lbID);

		lbPW = new JLabel("PW:");
		lbPW.setHorizontalAlignment(SwingConstants.CENTER);
		lbPW.setFont(new Font("굴림", Font.BOLD, 16));
		lbPW.setBounds(0, 54, 34, 49);
		loginPane.add(lbPW);

		loginBtn = new JButton("로그인");
		loginBtn.setBounds(0, 108, 210, 49);
		loginBtn.setBackground(new Color(255, 128, 0));
		loginPane.add(loginBtn);		

		joinBtn = new JButton("회원가입");
		joinBtn.setBounds(0, 162, 210, 49);
		joinBtn.setBackground(new Color(255, 185, 0));
		loginPane.add(joinBtn);

		ChangePWBtn = new JButton("비밀번호 변경");
		ChangePWBtn.setFont(new Font("굴림", Font.BOLD | Font.ITALIC, 10));
		ChangePWBtn.setBounds(126, 443, 109, 23);
		ChangePWBtn.setBorderPainted(false);
		ChangePWBtn.setFocusPainted(false);
		ChangePWBtn.setContentAreaFilled(false);
		contentPane.add(ChangePWBtn);	
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		joinBtn.addActionListener(new ActionListener() {	// 회원가입 버튼을 눌렀을 시
			@Override
			public void actionPerformed(ActionEvent e) {
				o.jf.setVisible(true);
			}
		});
		
		ChangePWBtn.addActionListener(new ActionListener() {	// 비밀번호 변경 버튼을 눌렀을 시
			@Override
			public void actionPerformed(ActionEvent e) {
				o.ckf.setVisible(true);
			}
		});
		
		loginBtn.addActionListener(new ActionListener() {	// 로그인 버튼을 눌렀을 시
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = tfID.getText();	// id 저장
				String upass = "";				// pw 저장
				char[] hidden_pw = tfPassword.getPassword();	// password 필드는 문자배열로 저장

				for (char temp : hidden_pw) {	// 문자배열을 문자열로 변환
					Character.toString(temp);
					upass += (upass.equals("")) ? "" + temp + "" : "" + temp + "";
				}

				if (uid.equals("") || upass.equals("")) {	// id와 pw 필드가 비어있다면
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호 모두 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("로그인 실패 > 로그인 정보 미입력");
				} else if (uid != null && upass != null) {// 둘다 입력

					String encryptedText = o.getSHA512(upass);	// pw암호화
					String temp = "lg" + " " + uid + " " + encryptedText;
					o.inputMes(temp);	// 서버로 id와 pw를 보냄
					System.out.println(78787878);
					while (true) {
						System.out.println(signal);
						if (signal) {
							break;
						}

					}
					signal = false;
					System.out.println(encryptedText);
					System.out.println(signal);
					if (check) { // 이 부분이 데이터베이스에 접속해 로그인 정보를 확인하는 부분이다.
						System.out.println("로그인 성공");

						o.us.ID = uid;
						String temp1 = "ui" + " " + uid;// userin 유저가 로그인 상태임을 서버에게 보냄
						o.inputMes(temp1);
						o.ID = uid;

						while (true) {// 서버로 부터 유저의 로그인상태가 로그인으로 바뀌었는지 신호가 올때까지 기다림
							System.out.println(signal + "1");
							if (signal) {
								break;
							}

						}
						signal = false;
						String temp2 = "us" + " " + uid;
						o.inputMes(temp2);// 유저 정보 받아오기 위해 서버로 보냄

						while (true) {// 서버로 부터 유저정보를 받아왔는지에 대한 신호가 올때까지 기다림
							System.out.println(signal + "2");
							if (signal) {
								break;
							}

						}
						signal = false;

						String temp3 = "nf" + " " + uid;//온라인 친구목록
						o.inputMes(temp3);

						while (true) {
							System.out.println(signal + "1" + "2");
							if (signal) {
								break;
							}

						}

						signal = false;

						String temp4 = "ff" + " " + uid;//오프라인 친구목록
						o.inputMes(temp4);

						while (true) {
							System.out.println(signal + "2" + "3");
							if (signal) {
								break;
							}

						}

						signal = false;
						o.mf = new MainFrame(o);
						dispose();

					} else {
						System.out.println(encryptedText);
						System.out.println("로그인 실패 > 로그인 정보 불일치");
						JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다");
						tfID.setText("");
						tfPassword.setText("");
					}
				}

			}
		});
	}
}