import java.sql.*;
public class UserDAO {

    private UserDTO userDTO = UserDTO.getInstance();

    private UserDAO() {}

    private static class Holder {
        public static final UserDAO INSTANCE = new UserDAO();
    }

    public static UserDAO getInstance() { return Holder.INSTANCE; }

    private static final String DRIVER = "com.mysql.jdbc.Driver"; //JDBC DRIVER
    private static final String URL = "jdbc:mysql://localhost:3306/catchmind?useSSL=false&serverTimezone=UTC"; //DB URL
    private static final String DB_ID = "root"; //DB ID
    private static final String DB_PW = "1234"; //DB 패스워드

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement ps = null;

    //private String sql_indexing = "ALTER TABLE user AUTO_INCREMENT=1; " + "SET @COUNT = 0;" + " UPDATE user SET id = @COUNT:=@COUNT+1; ";

    //DB Connect
    public void connectDB() {
        System.out.println("ConnectDB");

        try {
            Class.forName(DRIVER); //JDBC DRIVER LOADING
            conn = DriverManager.getConnection(URL, DB_ID, DB_PW); //DB Connection
            System.out.println("DB 연결 완료");
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (SQLException e) {
        	e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    //ID Duplication Check
    public int duplicationID() {
        System.out.println("Duplication Check");

        int duplication_case = 0;

        try {
            stmt = conn.createStatement(); // SQL문 처리용 Statement 객체 생성
            String sql = "SELECT userid FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            if (userDTO.getUserid().equals(""))
                duplication_case = 1; // 아이디를 입력하세요.

            else if (rs.next()) { // DB Null Check
                rs.previous(); // rs.getRow() -> 0

                while (rs.next()) // rs에 저장된 다음 행으로 커서를 옮긴다. rs.getRow() -> 1
                    if (rs.getString("userid").equals(userDTO.getUserid()))
                        duplication_case = 2; // 이미 사용중인 아이디입니다.
            }

            if(duplication_case == 0)
                duplication_case = 3; // 사용이 가능한 아이디입니다.

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return duplication_case; // return
    }

    //submit
    public void submit() {
        System.out.println("Submit");

        try {

            String sql = "INSERT INTO user (userid, password) VALUES (?, ?)";

            ps = conn.prepareStatement(sql);

            ps.setString(1, userDTO.getUserid());
            ps.setString(2, new String(userDTO.getPassword()));

            int result = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //login
    public int login() {
        System.out.println("Login");

        int login_case = 0;

        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) { // DB Null Check
                rs.previous(); // rs.getRow() -> 0

                while (rs.next()) { // rs에 저장된 다음 행으로 커서를 옮긴다.
                    if (rs.getString("userid").equals(userDTO.getUserid())) // 아이디 검사
                        if (rs.getString("password").equals(new String(userDTO.getPassword()))) // 비밀번호 검사
                            login_case = 3; // 로그인 성공.
                        else
                            login_case = 2; // 비밀번호가 틀렸습니다.
                }
            }

            if (login_case == 0)
                login_case = 1; // 없는 아이디입니다.

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return login_case; // return
    }
}