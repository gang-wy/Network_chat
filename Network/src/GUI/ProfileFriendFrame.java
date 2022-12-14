package GUI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

class ProfileFriendFrameWindowClosingEventHandler extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getWindow();
		frame.setVisible(false);
		frame.dispose();
	}
}

public class ProfileFriendFrame extends JFrame{

	private JPanel contentPane;
	private JLabel lbID;
	private JLabel lbName;
	private JLabel lbMail;
	private JButton ChangeBtn;
	private JLabel lbBirth;
	private JLabel lbComment;
	private JLabel lbLogin;
	private JLabel lbLogo;
	private JLabel lbProfile;

	String ID;
	String Name;
	String Mail;
	String Birth;
	String Comment;
	String Login = "a";
	String Logouttime;

	loginClient o = null;
	boolean check;
	boolean signal = false;
	String mes;

	public ProfileFriendFrame(loginClient opt) {
		o = opt;
		// 친구의 정보를 받아옴
		ID = o.fus.ID;
		Name = o.fus.name;
		Mail = o.fus.mail;
		Birth = o.fus.birth;
		Comment = o.fus.comment;
		Login = o.fus.login;
		Logouttime = o.fus.logouttime;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(276, 350);
		setLocation(420, 426);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon(ProfileFrame.class.getResource("/Image/Logo.png")));
		lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lbLogo.setBounds(94, 10, 60, 60);
		contentPane.add(lbLogo);

		lbProfile = new JLabel("프로필");
		lbProfile.setHorizontalAlignment(SwingConstants.CENTER);
		lbProfile.setFont(new Font("굴림", Font.BOLD, 16));
		lbProfile.setBounds(81, 68, 93, 35);
		contentPane.add(lbProfile);

		lbID = new JLabel("ID : ");
		lbID.setHorizontalAlignment(SwingConstants.LEFT);
		lbID.setBounds(12, 113, 175, 23);
		contentPane.add(lbID);

		lbName = new JLabel("Name : ");
		lbName.setHorizontalAlignment(SwingConstants.LEFT);
		lbName.setBounds(12, 146, 220, 23);
		contentPane.add(lbName);

		lbMail = new JLabel("Introduce : ");
		lbMail.setHorizontalAlignment(SwingConstants.LEFT);
		lbMail.setBounds(12, 179, 220, 23);
		contentPane.add(lbMail);

		lbBirth = new JLabel("Birth : <dynamic>");
		lbBirth.setHorizontalAlignment(SwingConstants.LEFT);
		lbBirth.setBounds(12, 212, 220, 23);
		contentPane.add(lbBirth);

		lbComment = new JLabel("Comment : <dynamic>");
		lbComment.setHorizontalAlignment(SwingConstants.LEFT);
		lbComment.setBounds(12, 245, 220, 23);
		contentPane.add(lbComment);

		lbLogin = new JLabel("Login : <dynamic>");
		lbLogin.setHorizontalAlignment(SwingConstants.LEFT);
		lbLogin.setBounds(12, 278, 220, 23);
		contentPane.add(lbLogin);

		lbID.setText("ID : " + ID);
		lbName.setText("Name : " + Name);
		lbMail.setText("Mail : " + Mail);
		lbBirth.setText("Birth : " + Birth);
		lbComment.setText("Comment : " + Comment);
		if (Login.equalsIgnoreCase("login")) {	// 로그인 중이라면
			lbLogin.setText("Login : Yes");
		} else {			// 로그아웃 중이라면
			lbLogin.setText("Login : No (" + Logouttime + ")");
		}

		this.addWindowListener(new ProfileFrameWindowClosingEventHandler());
		setVisible(true);
	}

	void setmyProfile() {

		o.mf.signal = true;
	}

	void setFriendProfile(String ID) {
		ID = ID;
		String temp = "fi" + " " + ID;
		o.inputMes(temp);

		StringTokenizer st = new StringTokenizer(mes, " ");

		while (true) {// 서버로 부터 친구정보를 받아왔는지에 대한 신호가 올때까지 기다림
			System.out.println(signal);
			if (signal) {
				break;
			}

		}
		signal = false;

	}
}