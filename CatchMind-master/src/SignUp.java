import javax.swing.*;
import java.util.Arrays;

public class SignUp extends JFrame {

    //ID
    private JLabel jl_ID;
    private JTextField jf_ID;
    private JButton btn_ID_check;

    //플래그
    private boolean flag_duplication = false; //ID 중복체크
    private boolean flag_submit = false; //회원가입 완료

    //PW
    private JLabel jl_PW;
    private JPasswordField jf_PW;
    private JLabel jl_PW_Check;
    private JPasswordField jf_PW_Check;

    //회원가입 버튼
    private JButton btn_SignUp;

    //뒤로가기 버튼
    private JButton btn_Back;

    //사용자의 입력 값 저장
    private String id = null;
    private char[] pw = null;
    private char[] pw_check = null;

    //다이얼로그 메시지
    private String msg = null;

    //DB
    UserDAO userDAO = UserDAO.getInstance();
    UserDTO userDTO = UserDTO.getInstance();

    SignUp() {
        //DB Connect
        userDAO.connectDB();

        //Set JFrame
        this.setTitle("Sign Up");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(null);

        //ID
        jl_ID = new JLabel("ID");
        jf_ID = new JTextField(10);
        btn_ID_check = new JButton("중복확인");

        jl_ID.setBounds(50, 50, 100, 30);
        jf_ID.setBounds(150, 50, 170, 30);
        btn_ID_check.setBounds(350, 50, 100, 30);

        this.add(jl_ID);
        this.add(jf_ID);
        this.add(btn_ID_check);

        //중복확인 버튼 이벤트
        btn_ID_check.addActionListener(e -> {

            //USER ID Duplication Check
            id = jf_ID.getText();
            userDTO.setUserid(id);

            int duplication = userDAO.duplicationID();

            switch (duplication) {
                case 0:
                    break;
                case 1:
                    msg = "아이디를 입력하세요";
                    flag_duplication = false;
                    JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                    break;
                case 2:
                    msg = "이미 사용중인 아이디입니다.";
                    flag_duplication = false;
                    JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                    break;
                case 3:
                    msg = "사용이 가능한 아이디입니다. '" + id + "' 사용하시겠습니까?";
                    int answer = JOptionPane.showConfirmDialog(null, msg, "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (answer == 0) {
                        jf_ID.setFocusable(false);
                        //btn_ID_check.setEnabled(false);
                        flag_duplication = true;
                    } else {
                        jf_ID.setFocusable(true);
                        flag_duplication = false;
                    }
                    break;
            }
        });

        //PW
        jl_PW = new JLabel("PW");
        jf_PW = new JPasswordField(10);

        jl_PW.setBounds(50, 100, 100, 30);
        jf_PW.setBounds(150, 100, 170, 30);

        this.add(jl_PW);
        this.add(jf_PW);

        //PW 확인
        jl_PW_Check = new JLabel("PW 확인");
        jf_PW_Check = new JPasswordField(10);

        jl_PW_Check.setBounds(50, 150, 100, 30);
        jf_PW_Check.setBounds(150, 150, 170, 30);

        this.add(jl_PW_Check);
        this.add(jf_PW_Check);

        //회원가입 버튼
        btn_SignUp = new JButton("확인");
        btn_SignUp.setBounds(150, 200, 170, 30);
        this.add(btn_SignUp);

        //회원가입 버튼 이벤트
        btn_SignUp.addActionListener( e -> {
            id = jf_ID.getText();
            pw = jf_PW.getPassword();
            pw_check = jf_PW_Check.getPassword();

            //아이디 중복확인 체크
            if (flag_duplication == false)
                msg = "아이디 중복확인을 해주세요.";
            else if (!Arrays.equals(pw, pw_check))
                msg = "비밀번호가 다릅니다.";
            else {
                userDTO.setUserid(id);
                userDTO.setPassword(pw);
                userDAO.submit();
                msg = "회원가입 되었습니다.";
                flag_submit = true;
            }

            //회원가입 버튼 누를 시 다이얼로그
            JOptionPane.showMessageDialog(null, msg, "", JOptionPane.PLAIN_MESSAGE);

            //회원가입 완료시
            if(flag_submit) {
                this.dispose();
                new Login();
            }

        });

        //뒤로가기 버튼
        btn_Back = new JButton("뒤로가기");
        btn_Back.setBounds(50, 200, 70, 30);
        this.add(btn_Back);

        //뒤로가기 버튼 이벤트
        btn_Back.addActionListener( e -> {
            this.dispose();
            new Login();
        });



        setSize(500, 300);
        setResizable(false);
        setVisible(true);
    }
}