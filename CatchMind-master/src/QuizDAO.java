
import java.sql.*;

public class QuizDAO {

    UserDAO userDAO = UserDAO.getInstance();
    QuizDTO quizDTO = QuizDTO.getInstance();

    private QuizDAO() {
        userDAO.connectDB();
    }

    private static class Holder {
        public static final QuizDAO INSTANCE = new QuizDAO();
    }

    private static final String DRIVER = "com.mysql.jdbc.Driver"; //JDBC DRIVER
    private static final String URL = "jdbc:mysql://13.125.10.241:3306/project?verifyServerCertificate=false&useSSL=true"; //DB URL
    private static final String DB_ID = "root"; //DB ID
    private static final String DB_PW = "0000"; //DB 패스워드

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement ps = null;


    public int getQuiz() {
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
}

