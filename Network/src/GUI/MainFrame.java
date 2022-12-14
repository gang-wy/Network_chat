package GUI;

import java.util.*;

import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import com.sun.tools.javac.Main;

public class MainFrame extends JFrame implements ActionListener, MouseListener {
   private JPanel contentPane;
   private JLabel lbLogo;
   private JButton RefreshBtn;
   private JButton LogoutBtn;
   private JLabel lbOnline;
   private DefaultListModel Onlinemodel;
   private JList OnlineList;
   private JLabel lbOffline;
   private DefaultListModel Offlinemodel;
   private JList OfflineList;
   private JButton SearchBtn;
   private JButton ChatBtn;
   private JButton ProfileBtn;
   private JLabel lbMovie;
   private JLabel lbData1;
   private JLabel lbData2;
   private JLabel lbData3;
   private JPopupMenu PM;
   private JMenuItem itemprofile;
   private JMenuItem itemchat;

   loginClient o = null;
   boolean signal = false;
   boolean signal1 = false;
   boolean signal2 = false;
   String selectedID;
   String temp1;
   String temp2;
   String trash;

   public MainFrame(loginClient opt) {
      o = opt;

      setTitle("Messenger");
      setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Image/Logo.png")));
      Font font = new Font("맑은 고딕", Font.BOLD, 12);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(700, 200, 274, 576);
      contentPane = new JPanel();
      contentPane.setBackground(new Color(255, 255, 255));
      contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      PM = new JPopupMenu();	// 팝업 메뉴 설정
      itemprofile = new JMenuItem("프로필 보기");
      itemchat = new JMenuItem("1:1 채팅하기");
      PM.add(itemprofile);
      PM.add(itemchat);

      RefreshBtn = new JButton("");
      RefreshBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Refresh.png")));
      RefreshBtn.setBounds(17, 10, 25, 25);
      RefreshBtn.setBorderPainted(false);
      RefreshBtn.setFocusPainted(false);
      RefreshBtn.setContentAreaFilled(false);
      contentPane.add(RefreshBtn);
      RefreshBtn.addActionListener(new ActionListener() {	// 새로고침 버튼을 누르면

         @Override
         public void actionPerformed(ActionEvent e) {
        	 String temp3 = "nfm" + " " + o.us.ID;// 온라인 친구목록
             o.inputMes(temp3);
             signal = false;
             while (true) {
                System.out.println(signal + "1" + "2");
                if (signal) {
                   break;
                }

             }

             signal = false;

             String temp4 = "ffm" + " " + o.us.ID;// 오프라인 친구목록
             o.inputMes(temp4);
             signal = false;
             while (true) {
                System.out.println(signal + "2" + "3");
                if (signal) {
                   break;
                }

             }

             signal = false;
             o.mf.setVisible(false);         
             o.mf = new MainFrame(o);
        	 JOptionPane.showMessageDialog(null, "새로고침");
         }
      });

      lbLogo = new JLabel("");
      lbLogo.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Logo.png")));
      lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
      lbLogo.setBounds(101, 10, 45, 45);
      contentPane.add(lbLogo);

      LogoutBtn = new JButton("");
      LogoutBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Logout.png")));
      LogoutBtn.setBounds(205, 10, 30, 30);
      LogoutBtn.setBorderPainted(false);
      LogoutBtn.setFocusPainted(false);
      LogoutBtn.setContentAreaFilled(false);
      contentPane.add(LogoutBtn);
      LogoutBtn.addActionListener(new ActionListener() {	// 로그아웃 버튼을 누르면

         @Override
         public void actionPerformed(ActionEvent e) {
            // store the logout log into DB
            String temp = "lo" + " " + o.us.ID;
            o.inputMes(temp);
            dispose();
            o.lf.setVisible(true);
         }
      });

      lbOnline = new JLabel("Online : ");
      lbOnline.setHorizontalAlignment(SwingConstants.CENTER);
      lbOnline.setBounds(12, 52, 57, 15);
      contentPane.add(lbOnline);

      Onlinemodel = new DefaultListModel();
      OnlineList = new JList(Onlinemodel);
      OnlineList.setBackground(new Color(192, 192, 192));
      OnlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      OnlineList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      OnlineList.setBounds(22, 77, 213, 153);

      contentPane.add(OnlineList);

      lbOffline = new JLabel("Offline : ");
      lbOffline.setHorizontalAlignment(SwingConstants.CENTER);
      lbOffline.setBounds(12, 240, 57, 15);
      contentPane.add(lbOffline);

      Offlinemodel = new DefaultListModel();
      OfflineList = new JList(Offlinemodel);
      OfflineList.setBackground(new Color(192, 192, 192));
      OfflineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      OfflineList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      OfflineList.setBounds(22, 265, 213, 153);
      contentPane.add(OfflineList);

      SearchBtn = new JButton();
      SearchBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Search.png")));
      SearchBtn.setBounds(29, 425, 40, 40);
      SearchBtn.setBorderPainted(false);
      SearchBtn.setFocusPainted(false);
      SearchBtn.setContentAreaFilled(false);
      contentPane.add(SearchBtn);

      SearchBtn.addActionListener(new ActionListener() {	// 검색 버튼을 누르면

         @Override
         public void actionPerformed(ActionEvent e) {
            o.srf.setVisible(true);
         }
      });

      ChatBtn = new JButton();
      ChatBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Chat.png")));
      ChatBtn.setBounds(106, 425, 40, 40);
      ChatBtn.setBorderPainted(false);
      ChatBtn.setFocusPainted(false);
      ChatBtn.setContentAreaFilled(false);
      contentPane.add(ChatBtn);
      ChatBtn.addActionListener(new ActionListener() {	// 채팅 버튼을 누르면
         @Override
         public void actionPerformed(ActionEvent e) {

            String freindName = JOptionPane.showInputDialog(o.ID + "님 초대할 id를 입력해주세요.");          
            o.inputMes("++inviteRequest++" + " " + o.ID + " " + freindName);
            
         }
      });

      ProfileBtn = new JButton();
      ProfileBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/Image/Profile.png")));
      ProfileBtn.setBounds(184, 425, 40, 40);
      ProfileBtn.setBorderPainted(false);
      ProfileBtn.setFocusPainted(false);
      ProfileBtn.setContentAreaFilled(false);
      contentPane.add(ProfileBtn);
      ProfileBtn.addActionListener(new ActionListener() {	// 프로필 버튼을 누르면

         @Override
         public void actionPerformed(ActionEvent e) {
            String temp2 = "usm" + " " + o.us.ID;
            o.inputMes(temp2);// 유저 정보 받아오기 위해 서버로 보냄
            signal = false;
            while (true) {// 서버로 부터 유저정보를 받아왔는지에 대한 신호가 올때까지 기다림
               System.out.println(signal + "2");
               if (signal) {
                  break;
               }

            }
            signal = false;
            o.prf = new ProfileFrame(o);
         }
      });
      
      JPanel DataPane = new JPanel();		// 공공 API 데이터를 출력할 Panel
      DataPane.setBackground(new Color(255, 128, 0));
      DataPane.setBounds(17, 475, 218, 60);
      contentPane.add(DataPane);
            DataPane.setLayout(null);
      
            lbMovie = new JLabel("추천 영화");
            lbMovie.setForeground(new Color(255, 255, 255));
            lbMovie.setFont(new Font("굴림", Font.BOLD, 12));
            lbMovie.setBounds(0, 0, 59, 54);
            DataPane.add(lbMovie);
            lbMovie.setHorizontalAlignment(SwingConstants.CENTER);
            
            lbData1 = new JLabel("");
            lbData1.setFont(new Font("굴림", Font.PLAIN, 10));
            lbData1.setBounds(63, 0, 155, 23);
            DataPane.add(lbData1);
            
            lbData2 = new JLabel("");
            lbData2.setFont(new Font("굴림", Font.PLAIN, 10));
            lbData2.setBounds(63, 18, 155, 23);
            DataPane.add(lbData2);
            
            lbData3 = new JLabel("");
            lbData3.setFont(new Font("굴림", Font.PLAIN, 10));
            lbData3.setBounds(63, 37, 155, 23);
            DataPane.add(lbData3);
           
      // 리스트 마우스 리스너 추가
      OnlineList.addMouseListener(this);
      OfflineList.addMouseListener(this);
      // 팝업 메뉴 아이템 액션리스너 추가
      itemprofile.addActionListener(this);
      itemchat.addActionListener(this);
      getAPI();
    
      //온라인 리스트 출력
      StringTokenizer st = new StringTokenizer(o.fd.onFriend, " ");
      trash = st.nextToken();
      while (st.hasMoreElements()) {
         Onlinemodel.addElement(st.nextToken());
      }
      // 오프라인 리스트 출력
      StringTokenizer st1 = new StringTokenizer(o.fd.offFriend, " ");
      trash = st1.nextToken();
      while (st1.hasMoreElements()) {
         Offlinemodel.addElement(st1.nextToken());
      }
      setVisible(true);

   }
   
   public void getAPI() {
         String key = "a5c963aea9b473a0b0b5cc864793ba95"; // 인증키       
          String result = ""; // 데이터 여기에 저장
          try {
             
             int min = 1;
             int max = 8;

             Random random = new Random(); // 랜덤 범위

             int value = random.nextInt(max + min) + min;
             
             URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key="
                   + key); // 해당 url로 접속해서 받아오기

             BufferedReader bf; // 데이터를 읽을 버퍼 reader

             bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

             result = bf.readLine(); 

              JSONParser jsonParser = new JSONParser(); // 자바 파서로 파싱하기
              JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
              JSONObject movieListResult = (JSONObject)jsonObject.get("movieListResult");
              JSONArray movieList = (JSONArray)movieListResult.get("movieList");
           
              JSONObject movieNm = (JSONObject)movieList.get(value);
              
              // 데이터 출력
              lbData1.setText("영화제작나라: " + movieNm.get("repNationNm"));
              lbData2.setText("영화장르: " + movieNm.get("repGenreNm"));
              lbData3.setText("영화이름: " + movieNm.get("movieNm"));
              
              bf.close();

          }catch(Exception e) {
             e.printStackTrace();
          }
   }
   
   private void showPopup(Component list, MouseEvent e) {	// 팝업 메뉴 실행
      PM.show(list, e.getX(), e.getY());

   }

   private void addPopup(MouseEvent e, int flag) {
      if (e.isPopupTrigger()) {
         return;
      }

      if (flag == 1) {	// 온라인 리스트에서 클릭했을 경우
         int clicked = OnlineList.locationToIndex(e.getPoint());	// 선택된 리스트 데이터의 인덱스 값 반환
         selectedID = (String) OnlineList.getSelectedValue();		// 선택된 리스트 데이터의 값 반환
         if (clicked != -1 && OnlineList.getCellBounds(clicked, clicked).contains(e.getPoint())) {	// 문제가 없으면
            OnlineList.setSelectedIndex(clicked);

            showPopup(OnlineList, e);
         }
         ;
      } else if (flag == 2) {	// 오프라인 리스트에서 클릭했을 경우
         int clicked = OfflineList.locationToIndex(e.getPoint());	// 선택된 리스트 데이터의 인덱스 값 반환
         selectedID = (String) OfflineList.getSelectedValue();		// 선택된 리스트 데이터의 값 반환
         if (clicked != -1 && OfflineList.getCellBounds(clicked, clicked).contains(e.getPoint())) {
            OfflineList.setSelectedIndex(clicked);

            showPopup(OfflineList, e);
         }
      }
   }

   public void mouseClicked(MouseEvent e) {	// 마우스를 클릭했을 경우
      int listnum = -1;

      if (SwingUtilities.isRightMouseButton(e)) {	// 오른쪽 클릭 했을 경우

         if (e.getSource() == OnlineList) {	// 온라인 리스트에서 클릭 했을 경우
            listnum = 1;
         } else if (e.getSource() == OfflineList) {	// 오프라인 리스트에서 클릭 했을 경우
            listnum = 2;
         }

         addPopup(e, listnum);
      }
   }

   public void mousePressed(MouseEvent e) {
   }

   public void mouseReleased(MouseEvent e) {
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }

   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();
      if (obj instanceof JMenuItem) {	// 팝업 메뉴 아이템을 클릭했을 경우
         String menu = e.getActionCommand();

         if (menu.equals("프로필 보기")) {	// 프로필 보기를 선택했을 경우
            String temp2 = "usf" + " " + selectedID;
            o.inputMes(temp2);// 유저 정보 받아오기 위해 서버로 보냄
            signal = false;
            while (true) {// 서버로 부터 유저정보를 받아왔는지에 대한 신호가 올때까지 기다림
               System.out.println(signal + "2");
               if (signal) {
                  break;
               }

            }
            signal = false;
            o.prff = new ProfileFriendFrame(o);
         } else if (menu.equals("1:1 채팅하기")) {	// 채팅하기를 선택했을 경우
        	  System.out.println(selectedID);        
             o.inputMes("++inviteRequest++" + " " + o.ID + " " + selectedID);	// 사용자의 id와 채팅할 사용자의 id를 서버로 보낸다
         }
      }
   }
}