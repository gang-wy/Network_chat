package GUI;

import java.util.*;



import java.io.*;
import java.net.*;

public class loginServer {
   private ServerSocket serverSocket;
   private Socket socket;
   static Database db = null;
   HashMap<String, Socket> Clients = new HashMap<String, Socket>();
   

   public loginServer() {
      System.out.println("서버가 시작되었습니다.");

   }

   public void connectServer() {
      try {
         serverSocket = new ServerSocket(59876);
         serverSocket.setReuseAddress(true);
         System.out.println("서버 준비 완료");
         

         while (true) {
            socket = serverSocket.accept();
            writefile(socket.getInetAddress(), socket.getPort());
            MyThread thread = new MyThread(this, socket, db);
            thread.start();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void writefile(InetAddress temp1, int temp2) {
      try {
         // 1. 파일 객체 생성
         File file = new File("sample.txt");

         // 2. 파일 존재여부 체크 및 생성
         if (!file.exists()) {
            file.createNewFile();
         }

         // 3. Writer 생성
         FileWriter fw = new FileWriter(file);
         PrintWriter writer = new PrintWriter(fw);

         // 4. 파일에 쓰기
         writer.print(temp1 + " ");
         writer.println(temp2);

         // 5. PrintWriter close
         writer.close();

      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public static void main(String[] args) {

      loginServer server = new loginServer();
      server.db = new Database();
      server.connectServer();
   }

}

//thread 생성
class MyThread extends Thread {
   private DataInputStream in;
   private DataOutputStream out;
   private Socket socket;
   private loginServer server;
   
   private DataOutputStream chatingOut;
   private DataInputStream chatingIn;

   static Database db = null;
   String type = null;
   boolean check = false;
   String mes = null;
   File sendingfile;
   
   public MyThread(loginServer server, Socket socket, Database db) {
      this.server = server;
      this.socket = socket;
      this.db = db;
   }

   @Override
   public void run() {
      try {

         // DataInputStream 객체 생성 후 전달받은 접속요청 결과 출력
         in = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
         chatingOut = new DataOutputStream(socket.getOutputStream());
         

         // 클라이언트로부터 전송되는 메세지를 처리할 receiveMessage() 메서드 호출
         receiveMessage();

      } catch (IOException e) {
         e.printStackTrace();
      }

   }
   
   
   public void receiveMessage() throws IOException {
      // 멀티 쓰레딩으로 메세지 수신 처리 작업 수행
      // boolean 타입 멤버변수 stopSignal 이 false 일 동안 반복해서 메세지 수신

      String ID;
      String PW;
      String name;
      String mail;
      String birth;
      String temp;
      

      try {
         while (true) {
            // 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
            temp = in.readUTF();
            StringTokenizer st = new StringTokenizer(temp, " ");
            type = st.nextToken();
            System.out.println(1);
            if (type.equals("lg")) {// 로그인
               ID = st.nextToken();
               PW = st.nextToken();
               System.out.println(ID);
               check = db.logincheck(ID, PW);
               sendcheck();
               
               
               
            } else if (type.equals("ck")) {// 사용자 확인
               ID = st.nextToken();
               PW = st.nextToken();
               System.out.println(ID);
               check = db.logincheck(ID, PW);
               sendcheck();
            } else if (type.equals("cg")) {// 비밀번호 변경
               ID = st.nextToken();
               PW = st.nextToken();
               System.out.println(ID);
               check = db.changeCheck(ID, PW);
               sendcheck();
            } else if (type.equals("su")) {// 비밀번호 변경
               ID = st.nextToken();
               PW = st.nextToken();
               name = st.nextToken();
               mail = st.nextToken();
               System.out.println(mail);
               birth = st.nextToken();
               System.out.println(ID);
               check = db.joinCheck(ID, PW, name, mail, birth);
               sendcheck();
            } else if (type.equals("us")) {// 유저정보 가져오기
               ID = st.nextToken();
               mes = db.userInfo(ID);
               sendMessage();
            } else if (type.equals("usm")) {// 메인프레임 유저정보 가져오기
               ID = st.nextToken();
               mes = db.userInfo(ID);
               sendMessage();
            } else if (type.equals("usf")) {// 친구 유저정보 가져오기
               ID = st.nextToken();
               System.out.println(ID);
               mes = db.userInfo(ID);
               sendMessage();
            } else if (type.equals("ui")) {// 유저가 접속함
               ID = st.nextToken();
               check = db.login(ID);
               sendcheck();
               server.Clients.put(ID,this.socket); //로그인 했을 때 Id를 키값으로 삼는 hashmap에 해당 socket을 넣는다.
               
               
            } else if (type.equals("sr")) {// 서치에 대한 정보를 받아옴
               ID = st.nextToken();
               mes = db.searchID(ID);
               sendMessage();
               
            } 
            
            else if(type.equals("si")) {
               ID = st.nextToken();
               System.out.println(ID);
               mes = db.userInfo(ID);
               sendMessage();
            }
            
            else if (type.equals("lo")) {// 로그아웃 했을 때
               ID = st.nextToken();
               check = db.logout(ID);
               check = db.logouttime(ID);
            } else if (type.equals("nc")) {// 새로운 코멘트
               ID = st.nextToken();
               temp = st.nextToken();
               check = db.writecomment(ID, temp);

            } else if (type.equals("fi")) {// 친구 정보 가져오기
               ID = st.nextToken();
               mes = db.userInfo(ID);
               sendMessage();

            } else if (type.equals("nf")) {// 온라인 친구 리스트 가져오기
               ID = st.nextToken();
               mes = db.onlineFriend(ID);
               sendMessage();

            } else if (type.equals("ff")) {// 오프라인 친구 리스트 가져오기
               ID = st.nextToken();
               mes = db.offlineFriend(ID);
               sendMessage();

            }else if (type.equals("pf")) {// 친구추가하기
               ID = st.nextToken();
               temp = st.nextToken();
               check = db.plusFriend(ID, temp);
               sendcheck();
            }
            else if (type.equals("nfm")) {// 온라인 친구 리스트 가져오기(메인프레임 새로고침버튼)
                ID = st.nextToken();
                mes = db.onlineFriend(ID);
                sendMessage();

             } else if (type.equals("ffm")) {// 오프라인 친구 리스트 가져오기(메인프레임 새로고침버튼)
                ID = st.nextToken();
                mes = db.offlineFriend(ID);
                sendMessage();

             }
            else if (type.contains("++inviteRequest++")) { // 초대요청이 왔을때         
               String myID = st.nextToken();
               String friendID = st.nextToken();
               
               if(server.Clients.get(friendID)!= null) // 해쉬맵에 해당 이름과 매칭되는 소켓이 있을 시.
               {
                  System.out.println("sucess to "+friendID);
                  chatingOut = new DataOutputStream(server.Clients.get(friendID).getOutputStream());
                  chatingOut.writeUTF("++invite++"+" "+myID); // 초대할 id를 가진 소켓에 1:1채팅 초대 신청을 통신
                  chatingOut.flush();
               }
               
               else { // 해쉬맵에 해당 id 없을 때
                  System.out.println("can't find ID");
                  chatingOut = new DataOutputStream(server.Clients.get(myID).getOutputStream());
                  chatingOut.writeUTF("++fail++"); // 연결에 실패했다고 전송
                  chatingOut.flush();
               }
                  
               
            } else if(type.contains("++response++")) { // 1:1 초대 신청에 대한 응답
               String accept = st.nextToken();
               String host= st.nextToken();
               String guest = st.nextToken();
               chatingOut = new DataOutputStream(server.Clients.get(host).getOutputStream()); // 초대한 사람에게 전송
               if(accept.contains("++yes++")) // 답이 긍정일 때
               {
                  chatingOut.writeUTF("++connect++"+" "+guest); // 초대한 사람이 채팅방에 들어왔다고 전송
                  chatingOut.flush();
                  chatingOut.writeUTF("++msgSingle++"+"//"+guest+"님이 입장했습니다.\n");
                  chatingOut.flush(); // 
               }
               
               else 
               {
                  chatingOut.writeUTF("++refuse++"); // 거절했다고 전송
                  chatingOut.flush();
               }
               
            }else if(type.contains("++endChat++")) { // 상대방이 채팅방에 나갔다고 전송
               String host= st.nextToken();
               String guest = st.nextToken();
               chatingOut = new DataOutputStream(server.Clients.get(guest).getOutputStream());
               chatingOut.writeUTF("++endChat++"+"//"+host+"님이 퇴장했습니다.\n");
               chatingOut.flush();
               
               
            }
            
            else if(type.contains("++msgSingle++")) { // 일반 메시지 전송
               
               StringTokenizer ST= new StringTokenizer(temp, "//"); // 여기는 // 기준으로 분할
               String Identifier = ST.nextToken();
               String myName = ST.nextToken();
               
               String friend = ST.nextToken();
               
               String message = ST.nextToken();
               
               chatingOut = new DataOutputStream(server.Clients.get(friend).getOutputStream()); // 채팅 메시지를 상대방의 소켓에 전송
               chatingOut.writeUTF("++msgSingle++"+"//"+myName+" > "+message);
               chatingOut.flush();
            }
            
            else if(type.contains("++fileRequest++")) { // 파일 전송 요청 식별자
               String myID = st.nextToken();
               String friendID = st.nextToken();
               System.out.println("file to "+friendID);
               if(server.Clients.get(friendID)!= null)
               {
                  System.out.println("sucess to "+friendID);
                  chatingOut = new DataOutputStream(server.Clients.get(friendID).getOutputStream());
                  chatingOut.writeUTF("++fileRequest++"+" "+myID); // hashmap에 해당 id있으면 파일을 받을건지 요청
                  chatingOut.flush();
               }
               
               else { // 아니면 실패했다고전송
                  System.out.println("can't find ID");
                  chatingOut = new DataOutputStream(server.Clients.get(myID).getOutputStream());
                  chatingOut.writeUTF("++fail++");
                  chatingOut.flush();
               }
                              
            }
            
            else if(type.contains("++fileResponse++")) { // 파일을 받을지에 대한 응답
               String accept = st.nextToken();
               String host= st.nextToken();
               String guest = st.nextToken();
               chatingOut = new DataOutputStream(server.Clients.get(host).getOutputStream());
               if(accept.contains("++yes++")) // 답이 긍정이면
               {
                  chatingOut.writeUTF("++fileStart++"+" "+guest); // 파일을 보내는 전송자에게 시작하라고 알림
                  chatingOut.flush();
                  
               }
               
               else // 부정의 답이면 fail 전송
               {
                  chatingOut.writeUTF("++fail++");
                  chatingOut.flush();
               }

            }
            
            else if(type.contains("++File++")) { // 서버가 파일을 송,수신할 준비
               String FileName = st.nextToken();
               String friendID = st.nextToken();
               
               getFile(FileName); // 파일을 받는 함수
               chatingOut = new DataOutputStream(server.Clients.get(friendID).getOutputStream());
               chatingOut.writeUTF("++file++"+" "+FileName); // client애개 파일을 받으라고 알려줌
               chatingOut.flush();
               
               try
               {
                  int data;
                  byte[] buffer= new byte[2048]; // 버퍼로 파일을 받는다
                  FileInputStream fis = new FileInputStream(sendingfile); // 파일을 버퍼에 입력할 스트림
                  BufferedInputStream bis = new BufferedInputStream(fis);

                  while((data = bis.read(buffer))!=-1) // 버퍼에 모든데이터를 보낼 때까지
                  {
                     chatingOut.writeInt(data); // 데이터 전송
                     chatingOut.write(buffer, 0, data);
                     chatingOut.flush();
                  
                  }
                  chatingOut.writeInt(-1); //전송이 끝나면 클라이언트에게 -1을 보낸다
                  fis.close();
                  bis.close();
            
               }catch(Exception e){e.printStackTrace();}
               
               System.out.println("다운로드 완료");
               
            }
                        

         }
      } catch (EOFException e) {
         // 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
         System.out.println("클라이언트 접속이 해제되었습니다.\n");
         System.out.println("클라이언트 연결 상태 : 미연결");

      } catch (SocketException e) {
         System.out.println("서버 접속이 해제되었습니다.\n");
         server.Clients.values().remove(this.socket); // 연결이 끊기면 hashmap에서 제거
         System.out.println(server.Clients.keySet()); 
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }
   
   
   public void getFile(String fileName) // 데이터를 받을 함수
   {
      try 
      {
         sendingfile = new File("C:/"+fileName); // C경로에 저장
         byte[] buffer = new byte[2048]; // 데이터를 받을 버퍼
         int data;
         FileOutputStream fos = new FileOutputStream(sendingfile); // 버퍼를 읽어올 스트림
         BufferedOutputStream bos = new BufferedOutputStream(fos);

         System.out.println("file download");
         while((data=in.readInt())!=-1) // 클라이언트에게 -1을 받을 때까지
         {
            in.read(buffer, 0, data); // 데이터를 읽어들인다.
            bos.write(buffer, 0, data);
            bos.flush();
         }
            
         System.out.println("end");
         fos.close();
         bos.close();
      }catch(Exception e) {e.printStackTrace();}
   }
   
   
   // 메시지 전달
   public void sendMessage() {
      try {

         if (type.equals("us")) {// 유저정보

            String text = type + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
         } else if (type.equals("usm")) {// 메인프레임 유저정보

            String text = type + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
         } else if (type.equals("usf")) {// 친구 유저정보

            String text = type + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
         }else if (type.equals("sr")) {// 유저정보
            
            String text = type + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
            
         } 
         else if(type.equals("si")) { // 서치리스트 유저정보 가져오기
            String text = type + " " + mes;
            out.writeUTF(text);
            out.flush();
         }
         
         
         else if (type.equals("nf")) {// 유저정보

            String text = "nf" + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
         } else if (type.equals("ff")) {// 유저정보

            String text = "ff" + " " + mes;
            // 클라이언트에게 메세지 전송
            out.writeUTF(text);
            out.flush();
         }
         else if (type.equals("nfm")) {// 온라인 친구 정보(메인프레임 리프레쉬버튼)

             String text = "nfm" + " " + mes;
             // 클라이언트에게 메세지 전송
             out.writeUTF(text);
             out.flush();
          } else if (type.equals("ffm")) {// 오프라인 친구 정보(메인프레임 리프레쉬버튼)

             String text = "ffm" + " " + mes;
             // 클라이언트에게 메세지 전송
             out.writeUTF(text);
             out.flush();
          }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   // 참,거짓 전달
   public void sendcheck() {
      try {

         System.out.println(1);
         if (type.equals("lg")) {// 로그인
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
         } else if (type.equals("ck")) {// 사용자 확인
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
         } else if (type.equals("cg")) {// 비밀번호 변경
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
         } else if (type.equals("su")) {// 회원가입
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
         } else if (type.equals("ui")) {// 유저가 접속함
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
         } else if (type.equals("pf")) {// 친구추가여부
            String istrue;
            if (check)
               istrue = "true";
            else
               istrue = "false";

            String text = type + " " + istrue;

            out.writeUTF(text);
            out.flush();
          }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}