package ChatC;

import java.sql.Connection ;
import java.sql.DriverManager ;
import java.sql.Statement ;


public class UserDBConnection{
/*
	// define MySQL database driver
	public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;

	// define MySQL database connection URL
	public static final String DBURL = "jdbc:mysql://localhost:3306/UserInfo" ;

	// username of MySQL database
	public static final String DBUSER = "root" ;

	// password of MySQL database
	public static final String DBPASS = "mysqladmin" ;

	// get user info from class SignIn
	SignIn si = new SignIn() ;
	int id = si.getID() ;
	String name = si.getName() ;
	String password = si.getPass() ;
	int age = si.getAge() ;
	String sex = si.getSex() ;
	String birthday = si.getBirth() ;
	String email = si.getEMail() ;

	public void insert() throws Exception {
		Connection conn = null ;
		Statement stmt = null ;
		Class.forName(DBDRIVER) ;
		String sqlInsert = "INSERT INTO user(id,name,password,age,sex,birthday,e-mail) VALUES ("+id+",'"+name+"','"+password+"',"+age+",'"+sex+"','"+birthday+"','"+email+"')" ;
		conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;
		stmt = conn.createStatement() ;
		stmt.executeUpdate(sqlInsert) ;
		stmt.close() ;
		conn.close() ;
	}

	public void update() throws Exception {
		Connection conn = null ;
		Statement stmt = null ;
		Class.forName(DBDRIVER) ;
		String sqlUpdate = "UPDATE user SET name='"+name+"',password='"+password+"' , age=" + age + ",sex='"+sex+"',birthday='"+birthday+"',e-mail='"+email+"' WHERE id="+id  ;
		conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;
		stmt = conn.createStatement() ;
		stmt.executeUpdate(sqlUpdate) ;
		stmt.close() ;
		conn.close() ;
	}
	*/
};
