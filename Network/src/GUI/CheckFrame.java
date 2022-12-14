package GUI;

import java.util.*;


import java.awt.*;
import java.awt.event.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class CheckFrameWindowClosingEventHandler extends WindowAdapter {	// 창을 닫을 때 이 창만 종료되게 설정
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getWindow();
		frame.setVisible(false);
		frame.dispose();
	}
}

public class CheckFrame extends JFrame {

	private JPanel contentPane;
	private JLabel lbID;
	private JLabel lbPW;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton CheckBtn;

	boolean check;
	boolean signal = false;
	loginClient o = null;

	public CheckFrame(loginClient opt) {
		o = opt;

		setTitle("Messenger");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Image/Logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 274, 576);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon(CheckFrame.class.getResource("/Image/Logo.png")));
		lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lbLogo.setBounds(76, 24, 98, 97);
		contentPane.add(lbLogo);

		JLabel lbCheck = new JLabel("사용자 확인");
		lbCheck.setHorizontalAlignment(SwingConstants.CENTER);
		lbCheck.setFont(new Font("굴림", Font.BOLD, 20));
		lbCheck.setBounds(66, 109, 120, 30);
		contentPane.add(lbCheck);

		tfID = new JTextField();
		tfID.setText("아이디 입력");
		tfID.setForeground(Color.GRAY);
		tfID.setFont(new Font("굴림", Font.ITALIC, 12));
		tfID.setBounds(66, 190, 168, 49);
		contentPane.add(tfID);
		tfID.addFocusListener(new FocusListener() {	// id 필드 힌트 설정
			@Override
			public void focusLost(FocusEvent e) {
				if (tfID.getText().equals("")) {
					tfID.setText("아이디 입력");
					tfID.setFont(new Font("굴림", Font.ITALIC, 12));
					tfID.setForeground(Color.GRAY);
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (tfID.getText().equals("아이디 입력")) {
					tfID.setText("");
					tfID.setFont(new Font("Gulim", Font.PLAIN, 12));
					tfID.setForeground(Color.BLACK);
				}
			}
		});

		tfPassword = new JPasswordField();
		tfPassword.setEchoChar('*');
		tfPassword.setColumns(10);
		tfPassword.setBounds(66, 244, 168, 49);
		contentPane.add(tfPassword);

		lbID = new JLabel("ID:");
		lbID.setFont(new Font("굴림", Font.BOLD, 16));
		lbID.setHorizontalAlignment(SwingConstants.CENTER);
		lbID.setBounds(12, 190, 42, 49);
		contentPane.add(lbID);

		lbPW = new JLabel("PW:");
		lbPW.setHorizontalAlignment(SwingConstants.CENTER);
		lbPW.setFont(new Font("굴림", Font.BOLD, 16));
		lbPW.setBounds(12, 244, 42, 49);
		contentPane.add(lbPW);

		CheckBtn = new JButton("확인");
		CheckBtn.setBackground(new Color(255, 128, 0));
		CheckBtn.setBounds(24, 303, 210, 49);
		contentPane.add(CheckBtn);
		setLocationRelativeTo(null);
		setResizable(false);
		this.addWindowListener(new CheckFrameWindowClosingEventHandler());
		CheckBtn.addActionListener(new ActionListener() {	// 확인 버튼을 눌렀을 경우
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = tfID.getText();	// id 저장
				String upass = "";				// pw 저장
				char[] hidden_pw = tfPassword.getPassword();	// password 필드는 문자배열로 저장됨

				for (char temp : hidden_pw) {	// 문자배열을 문자열로 변환
					Character.toString(temp);
					upass += (upass.equals("")) ? "" + temp + "" : "" + temp + "";
				}

				if (uid.equals("") || upass.equals("")) {	// id와 pw가 비어있다면
					JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "사용자 확인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("사용자 확인 실패 > 사용자 정보 미입력");
				}
				else if (uid != null && upass != null) {
					String encryptedText = o.getSHA512(upass);	// pw 암호화
					String temp = "ck" + " " + uid + " " + encryptedText;
					o.inputMes(temp);	// 서버로 id와 pw를 보내서 맞는지 확인
					System.out.println(signal);
					while (true) {
						System.out.println(signal);
						if (signal)
							break;
					}
					signal = false;
					System.out.println(check);
					System.out.println(signal);
					if (check) { // 이 부분이 데이터베이스에 접속해 로그인 정보를 확인하는 부분이다.
						System.out.println("사용자 확인 성공");
						o.chf.setVisible(true);// 비밀번호 변경창 오픈
						dispose();
						tfID.setText("");
						tfPassword.setText("");
					} else {
						System.out.println("사용자 확인 실패");
						JOptionPane.showMessageDialog(null, "사용자 확인에 실패하였습니다");
						tfID.setText("");
						tfPassword.setText("");
					}
				}
			}
		});
	}
}
