package GUI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import javax.swing.border.*;

class JoinFrameWindowClosingEventHandler extends WindowAdapter {	// 창을 닫을 때 이 창만 종료되게 설정
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getWindow();
		frame.setVisible(false);
		frame.dispose();
	}
}

public class JoinFrame extends JFrame {

	private JPanel contentPane;
	private JLabel lbJoin;
	private JButton joinCompleteBtn;
	private JTextField tfID;
	private JTextField tfPassword;
	private JTextField tfName;
	private JTextField tfEmail;
	private JTextField tfBirth;

	loginClient o = null;
	private boolean CheckID = false;
	boolean check;
	boolean signal;

	public JoinFrame(loginClient opt) {
		o = opt;

		setTitle("Messenger");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Image/Logo.png")));
		Font font = new Font("맑은 고딕", Font.BOLD, 12);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 274, 576);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbJoin = new JLabel("회원가입");
		lbJoin.setHorizontalAlignment(SwingConstants.CENTER);
		Font f1 = new Font("돋움", Font.BOLD, 20); // 궁서 바탕 돋움
		lbJoin.setFont(f1);
		lbJoin.setBounds(75, 109, 101, 20);
		contentPane.add(lbJoin);

		JLabel lbID = new JLabel("ID : ");
		lbID.setBounds(12, 173, 76, 20);
		contentPane.add(lbID);
		lbID.setFont(font);

		JLabel lbPassword = new JLabel("password : ");
		lbPassword.setBounds(12, 218, 76, 20);
		contentPane.add(lbPassword);
		lbPassword.setFont(font);

		JLabel lbName = new JLabel("name : ");
		lbName.setBounds(12, 263, 76, 20);
		contentPane.add(lbName);
		lbName.setFont(font);

		JLabel lbEmail = new JLabel("email : ");
		lbEmail.setBounds(12, 308, 76, 20);
		contentPane.add(lbEmail);
		lbEmail.setFont(font);

		JLabel lbBirthday = new JLabel("BirthDay : ");
		lbBirthday.setBounds(12, 353, 76, 20);
		contentPane.add(lbBirthday);
		lbBirthday.setFont(font);

		tfID = new JTextField();
		tfID.setColumns(10);
		tfID.setBounds(84, 169, 162, 35);
		contentPane.add(tfID);

		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(84, 214, 162, 35);
		contentPane.add(tfPassword);

		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(84, 259, 162, 35);
		contentPane.add(tfName);

		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(84, 304, 162, 35);
		contentPane.add(tfEmail);

		tfBirth = new JTextField();
		tfBirth.setColumns(10);
		tfBirth.setBounds(84, 349, 162, 35);
		contentPane.add(tfBirth);

		joinCompleteBtn = new JButton("완료");
		joinCompleteBtn.setBackground(new Color(255, 128, 0));
		joinCompleteBtn.setBounds(149, 404, 97, 29);
		contentPane.add(joinCompleteBtn);

		JLabel lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon(JoinFrame.class.getResource("/Image/Logo.png")));
		lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lbLogo.setBounds(76, 24, 98, 97);
		contentPane.add(lbLogo);
		Font f2 = new Font("돋움", Font.BOLD, 8);
		setLocationRelativeTo(null);
		setResizable(false);
		this.addWindowListener(new JoinFrameWindowClosingEventHandler());

		// 회원가입완료 액션
		joinCompleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = tfID.getText();
				String upass = tfPassword.getText();
				String uname = tfName.getText();
				String umail = tfEmail.getText();
				String ubirth = tfBirth.getText();

				if (uid.equals("") || upass.equals("") || uname.equals("") || umail.equals("") || ubirth.equals("")) {	// 하나라도 비어있다면
					JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("회원가입 실패 > 회원정보 미입력");
				}
				else if (!uid.equals("") && !upass.equals("") && !uname.equals("") && !umail.equals("")
						&& !ubirth.equals("")) {
					String encryptedText = o.getSHA512(upass);	// pw 암호화
					String temp = "su" + " " + uid + " " + encryptedText + " " + uname + " " + umail + " " + ubirth;
					System.out.println(umail);
					o.inputMes(temp);	// 서버로 회원가입하는 사용자의 정보를 보냄
					System.out.println(signal);
					while (true) {
						System.out.println(signal);
						if (signal)
							break;
					}
					signal = false;
					System.out.println(check);
					System.out.println(signal);
					if (check) {
						System.out.println("회원가입 성공");
						JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다");
						dispose();
						tfID.setText("");
						tfPassword.setText("");
					} else {
						System.out.println("중복된 아이디");
						JOptionPane.showMessageDialog(null, "중복된 아이디입니다");
						tfID.setText("");
						tfPassword.setText("");
					}
				}

			}
		});

	}
}