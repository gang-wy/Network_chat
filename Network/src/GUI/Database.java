package GUI;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Database {
	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // dbteamproject 스키마
	String user = "root";
	String passwd = "12345"; // 본인이 설정한 root 계정의 비밀번호를 입력하면 된다.

	Database() { // 데이터베이스에 연결한다.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			System.out.println("MySQL 서버 연동 성공");
		} catch (Exception e) {
			System.out.println("MySQL 서버 연동 실패 > " + e.toString());
		}
	}

	/* 로그인 정보를 확인 */
	boolean logincheck(String _i, String _p) {
		boolean flag = false;

		String id = _i;
		String pw = _p;

		try {
			String checkingStr = "SELECT Password FROM member WHERE ID='" + id + "'";
			ResultSet result = stmt.executeQuery(checkingStr);

			int count = 0;
			while (result.next()) {
				if (pw.equals(result.getString("password"))) {
					flag = true;
				}

				else {
					flag = false;
					System.out.println("로그인 실패");
				}
				count++;
			}
		} catch (Exception e) {
			flag = false;
			System.out.println("로그인 실패 > " + e.toString());
		}

		return flag;
	}

//	회원가입
	boolean joinCheck(String _i, String _p, String _n, String _m, String _b) {
		boolean flag = false;

		String id = _i;
		String pw = _p;
		String name = _n;
		String mail = _m;
		String birth = _b;

		try {
			String insertStr = "INSERT INTO member (ID, Password, name, mail, birth) VALUES('" + id + "', '" + pw
					+ "', '" + name + "', '" + mail + "', '" + birth + "')";
			stmt.executeUpdate(insertStr);

			flag = true;
			System.out.println("회원가입 성공");
			// 해당 멤버 테이블 생성

		} catch (Exception e) {
			flag = false;
			System.out.println("회원가입 실패 > " + e.toString());
		}

		return flag;
	}

	// 비밀번호변경
	boolean changeCheck(String _i, String _p) {
		boolean flag = false;

		String id = _i;
		String pw = _p;

		try {
			String insertStr = "update member set Password = '" + pw + "' where ID = '" + id + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("변경하기 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("변경하기 실패 > " + e.toString());
		}

		return flag;
	}

	// 별명 변경
	boolean changeName(String myid, String myname) {
		boolean flag = false;

		try {
			String insertStr = "update member set name = '" + myname + "' where ID = '" + myid + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("별명 변경하기 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("별명 변경하기 실패 > " + e.toString());
		}

		return flag;
	}

	// 오늘의 한마디 작성
	boolean writecomment(String _i, String _c) {
		boolean flag = false;

		String id = _i;
		String comment = _c;

		try {
			String insertStr = "update member set comment = '" + comment + "' where ID = '" + id + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("코멘트 작성 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("코멘트 작성 실패 > " + e.toString());
		}

		return flag;
	}
	// 이름 작성
	boolean writename (String _i, String _n) {
		boolean flag = false;
		
		String id = _i;
		String name = _n;
		
		try {
			String inserStr = "update member set name = '" + name + "' where ID = '" + id + "'";
			stmt.execute(inserStr);
			
			flag = true;
			System.out.println("이름 작성 성공");
		} catch (Exception e) {
			flag = false;
			System.out.println("이름 작성 실패 > " + e.toString());
		}
		
		return flag;
	}

	// 친구추가
	   boolean plusFriend(String myid, String friendid) {
	      boolean flag = false;

	      try {
	         String checkingStr = "select * from friend where ID ='" + myid + "' and friendID = '" + friendid + "'";
	         ResultSet result = stmt.executeQuery(checkingStr);
	         
	         if(result.next() == true) {
	            throw new Exception();
	         }
	         
	         String insertStr1 = "insert into friend values('" + myid + "', '" + friendid + "')";
	         stmt.execute(insertStr1);

	         flag = true;
	         System.out.println("친구추가 성공");

	      } catch (Exception e) {
	         flag = false;
	         System.out.println("친구추가 실패 > " + e.toString());
	      }

	      return flag;
	   }

	// login으로 접속상태 변경
	boolean login(String _i) {
		boolean flag = false;

		String id = _i;

		try {
			String insertStr = "update member set login = 'login' where ID = '" + id + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("접속상태 변경 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("접속상태 변경 실패 > " + e.toString());
		}

		return flag;
	}

	// logout으로 접속상태 변경
	boolean logout(String _i) {
		boolean flag = false;

		String id = _i;

		try {
			String insertStr = "update member set login = 'logout' where ID = '" + id + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("로그아웃으로 변경하기 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("로그아웃으로 변경하기 실패 > " + e.toString());
		}

		return flag;
	}

	// logout시 로그아웃 시간 입력
	boolean logouttime(String _i) {
		boolean flag = false;

		String id = _i;
		LocalDate now = LocalDate.now();
		LocalTime now1 = LocalTime.now();

		int year = now.getYear();
		String month = now.getMonth().toString();
		int day = now.getDayOfMonth();
		int hour = now1.getHour();
		int minute = now1.getMinute();
		int second = now1.getSecond();

		String time = year + "-" + month + "-" + day + "/" + hour + ":" + minute + ":" + second;

		try {
			String insertStr = "update member set logouttime = '" + time + "' where ID = '" + id + "'";
			stmt.execute(insertStr);

			flag = true;
			System.out.println("로그아웃시간 변경하기 성공");

		} catch (Exception e) {
			flag = false;
			System.out.println("로그아웃시간 변경하기 실패 > " + e.toString());
		}

		return flag;
	}

	// 유저 정보 가져오기
	String userInfo(String myID) {
		String str = null;
		try {
			String query = "SELECT ID, name, mail, birth, comment, login, logouttime FROM member where ID=\"" + myID
					+ "\"";
			ResultSet result = stmt.executeQuery(query);

			result.next();
			str = result.getString("ID") + " " + result.getString("name") + " " + result.getString("mail") + " "
					+ result.getString("birth") + " " + result.getString("comment") + " " + result.getString("login")
					+ " " + result.getString("logouttime");

			System.out.println("유저정보 가져오기 성공");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("유저정보 가져오기 실패 " + e.toString());
		}
		return str;
	}

	/* 온라인 친구 목록 가져오기 */
	String onlineFriend(String id) {
		String str = "";

		try {
			String checkingStr = "SELECT friendID FROM member as A, friend as B WHERE B.ID='" + id
					+ "' and A.ID = B.friendID and A.login='login'";
			ResultSet result = stmt.executeQuery(checkingStr);

			while (result.next()) {
				str += result.getString("friendID") + " ";
				System.out.println(str);
			}

			System.out.println("온라인 친구목록 가져오기 성공");
		} catch (Exception e) {

			System.out.println("온라인 친구목록 가져오기 실패 > " + e.toString());
		}

		return str;
	}

	/* 오프라인 친구 목록 가져오기 */
	String offlineFriend(String id) {
		String str = "";

		try {
			String checkingStr = "SELECT friendID FROM member as A, friend as B WHERE B.ID='" + id
					+ "' and A.ID = B.friendID and A.login='logout'";
			ResultSet result = stmt.executeQuery(checkingStr);

			while (result.next()) {
				str += result.getString("friendID") + " ";
				System.out.println(str);
			}

			System.out.println("오프라인 친구목록 가져오기 성공");
		} catch (Exception e) {

			System.out.println("오프라인 친구목록 가져오기 실패 > " + e.toString());
		}

		return str;
	}

	/* 서치 목록 가져오기 */
	String searchID(String text) {
		String str = "";

		try {
			String checkingStr = "SELECT ID FROM member WHERE ID like '%" + text + "%'";
			ResultSet result = stmt.executeQuery(checkingStr);

			while (result.next()) {
				str += result.getString("ID") + " ";
				System.out.println(str);
			}

			String checkingStr1 = "SELECT name FROM member WHERE name like '%" + text + "%'";
			ResultSet result1 = stmt.executeQuery(checkingStr1);
			
			while (result1.next()) {
				str += result1.getString("name") + " ";
				System.out.println(str);
			}

			System.out.println("서치목록 가져오기 성공");
		} catch (Exception e) {

			System.out.println("서치목록 가져오기 실패 > " + e.toString());
		}

		return str;
	}

}