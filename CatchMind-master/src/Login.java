import javax.swing.*;
public class Login extends JFrame {

    UserInfo userInfo = UserInfo.getInstance();
    UserDAO userDAO = UserDAO.getInstance();
    UserDTO userDTO = UserDTO.getInstance();

    //ID
    private JLabel jl_ID;
    private JTextField jf_ID;

    //PW
    private JLabel jl_PW;
    private JPasswordField jf_PW;

    //BTN
    private JButton btn_Login;
    private JButton btn_SignUp;

    //����
    private String id = null;
    private char[] pw = null;
    String message = null;

    //���̾�α� �޽���
    private String msg = null;

    Login() {
        userDAO.connectDB();
        System.out.println("db���Ἲ��");

        this.setTitle("Login");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(null);

        //ID
        jl_ID = new JLabel("ID");
        jf_ID = new JTextField(10);

        jl_ID.setBounds(85,50,50,30);
        jf_ID.setBounds(135, 50, 170, 30);

        this.add(jl_ID);
        this.add(jf_ID);

        //PW
        jl_PW = new JLabel("PW");
        jf_PW = new JPasswordField(10);

        jl_PW.setBounds(85,90,50,30);
        jf_PW.setBounds(135, 90, 170, 30);

        this.add(jl_PW);
        this.add(jf_PW);

        //BTN
        btn_Login = new JButton("�α���");
        btn_SignUp = new JButton("ȸ������");

        this.add(btn_Login);
        this.add(btn_SignUp);

        btn_Login.setBounds(85, 130, 100, 30);
        btn_SignUp.setBounds(200, 130, 100, 30);

        //�α��� ��ư �̺�Ʈ
        btn_Login.addActionListener( e -> {

            id = jf_ID.getText();
            pw = jf_PW.getPassword();

            userDTO.setUserid(id);
            userDTO.setPassword(pw);


            if(id.equals("")) {
                message = "���̵� �Է��ϼ���.";
                JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE);
            } else if(pw.length == 0) {
                jf_PW.requestFocusInWindow();
            } else {
                int login = userDAO.login();

                switch (login) {
                    case 0:
                        break;
                    case 1:
                        msg = "���� ���̵��Դϴ�.";
                        JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 2:
                        msg = "��й�ȣ�� Ʋ�Ƚ��ϴ�.";
                        JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 3:
                        msg = "�α��� ����.";
                        JOptionPane.showMessageDialog(null, msg, "", JOptionPane.PLAIN_MESSAGE);
                        this.dispose();
                        userInfo.setID(id);
                        new Game();
                        break;
                }
            }
        });

        //ȸ������ ��ư �̺�Ʈ
        btn_SignUp.addActionListener( e -> {
            this.dispose();
            new SignUp();
        });


        this.setSize(400,250);
        this.setResizable(false);
        this.setVisible(true);
    }

    public String getId() {
        return id;
    }

    public static void main(String[] args) {
        new Login();
    }
}