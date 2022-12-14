package GUI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

class SearchFrameWindowClosingEventHandler extends WindowAdapter {
   public void windowClosing(WindowEvent e) {
      JFrame frame = (JFrame) e.getWindow();
      frame.setVisible(false);
      frame.dispose();
   }
}

public class SearchFrame extends JFrame implements ActionListener, MouseListener {

   private JPanel contentPane;
   private JTextField tfSearch;
   private JButton SearchBtn;
   private JScrollPane Searchscroll;
   private JList SearchList;
   private JPopupMenu PM;
   private JMenuItem itemprofile;
   private JMenuItem itemaddfriend;
   private DefaultListModel SearchModel;
   boolean check;
   boolean signal;
   String trash, mes, selectedID;
   loginClient o = null;
   private JLabel lbID;

   /**
    * Create the frame.
    */
   public SearchFrame(loginClient opt) {
      o = opt;

      setTitle("Messenger");
      setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Image/Logo.png")));
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(276, 300);
      setLocation(420, 200);
      contentPane = new JPanel();
      contentPane.setBackground(new Color(255, 255, 255));
      contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      // 팝업 메뉴 설정
      PM = new JPopupMenu();
      itemprofile = new JMenuItem("프로필 보기");
      itemaddfriend = new JMenuItem("친구 추가하기");
      PM.add(itemprofile);
      PM.add(itemaddfriend);

      lbID = new JLabel("ID:");
      lbID.setHorizontalAlignment(SwingConstants.CENTER);
      lbID.setFont(new Font("굴림", Font.BOLD, 12));
      lbID.setBounds(12, 10, 24, 31);
      contentPane.add(lbID);
      
      tfSearch = new JTextField();
      tfSearch.setBounds(48, 10, 141, 31);
      contentPane.add(tfSearch);
      tfSearch.setColumns(10);
      tfSearch.addActionListener(this);

      SearchBtn = new JButton("");
      SearchBtn.setIcon(new ImageIcon(SearchFrame.class.getResource("/Image/Enter.png")));
      SearchBtn.setBounds(201, 10, 32, 31);
      SearchBtn.setBorderPainted(false);
      contentPane.add(SearchBtn);
      SearchBtn.addActionListener(this);

      SearchModel = new DefaultListModel();
      SearchList = new JList(SearchModel);
      SearchList.setBackground(new Color(192, 192, 192));
      SearchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      SearchList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      SearchList.setBounds(12, 51, 234, 197);
      Searchscroll = new JScrollPane(SearchList);
      Searchscroll.setSize(234, 197);
      Searchscroll.setLocation(12, 51);
      contentPane.add(Searchscroll);
      
      // 리스트 마우스리스너 추가
      SearchList.addMouseListener(this);
      // 팝업 메뉴 아이템 액션리스너 추가
      itemprofile.addActionListener(this);
      itemaddfriend.addActionListener(this);

      this.addWindowListener(new SearchFrameWindowClosingEventHandler());
   }

   public void addList(String id) {
      SearchModel.addElement(id);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();

      if (e.getSource() == tfSearch || e.getSource() == SearchBtn) {	// 검색필드에서 엔터를 치거나 검색 버튼을 눌렀을 경우
    	 SearchModel.removeAllElements();	// 검색 리스트 초기화
    	 if (tfSearch.getText().equals("")) {	// 검색창이 비어있다면
            JOptionPane.showMessageDialog(null, "찾을 내용을 입력해주세요", "서치 실패", JOptionPane.ERROR_MESSAGE);
            System.out.println("서치 실패 > 서치 정보 미입력");
         } else {
            // 서버로 찾는 id혹은 별명을 보내고 받아옴
            String search = tfSearch.getText();
            String temp = "sr" + " " + search;
            o.inputMes(temp);
            System.out.println(78787878);
            while (true) {
               System.out.println(signal);
               if (signal) {
                  break;
               }

            }
            signal = false;

            StringTokenizer st = new StringTokenizer(mes, " ");
            trash = st.nextToken();
            while (st.hasMoreTokens()) {
               addList(st.nextToken());	// 리스트에 검색한 내용을 추가
            }

            tfSearch.setText("");	// 검색 창 초기화
         }
      } 
      else if (obj instanceof JMenuItem) {	// 팝업 메뉴 아이템을 선택했다면
         String menu = e.getActionCommand();

         if (menu.equals("프로필 보기")) {	// 프로필 보기를 선택했다면
             String temp2 = "si" + " " + selectedID;
             o.inputMes(temp2);// 유저 정보 받아오기 위해 서버로 보냄
             signal = false;
             while (true) {// 서버로 부터 유저정보를 받아왔는지에 대한 신호가 올때까지 기다림
                System.out.println(signal + "2");
                if (signal) {
                   break;
                }
             }
             signal = false;
             o.prff = new ProfileFriendFrame(o);	// 친구프로필 프레임 실행
         } else if (menu.equals("친구 추가하기")) {		// 친구 추가를 선택했다면
             String temp = "pf" + " " + o.us.ID + " " + selectedID;
             o.inputMes(temp);		// 유저 정보 받아오기 위해 서버로 전달
             signal = false;
             while (true) {	// 서버로부터 유저정보를 받아왔는지에 대한 신호가 올때까지 기다림
                System.out.println(signal);
                if (signal) {
                   break;
                }
             }
             signal = false;
             
             if(check) {	// 친구 추가에 성공했다면	
                JOptionPane.showMessageDialog(null, "친구 추가에 성공하였습니다");
             }else {	// 실패했다면
                JOptionPane.showMessageDialog(null, "친구 추가에 실패하였습니다");
             }
       }
      }
   }

   public void mouseClicked(MouseEvent e) {		// 마우스 클릭을 했을때
      if (e.isPopupTrigger()) {
         return;
      }
      if (SwingUtilities.isRightMouseButton(e)) {	// 오른쪽 클릭을 했을 경우
         int clicked = SearchList.locationToIndex(e.getPoint());	// 선택된 리스트 데이터의 인덱스 값 반환
         selectedID = (String) SearchList.getSelectedValue();		// 선택된 리스트 데이터의 값 반환
         if (clicked != -1 && SearchList.getCellBounds(clicked, clicked).contains(e.getPoint())) {	// 문제가 없다면
            SearchList.setSelectedIndex(clicked);

            PM.show(SearchList, e.getX(), e.getY());	// 팝업 메뉴 실행
         }
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
}