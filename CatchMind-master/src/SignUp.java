import javax.swing.*;
import java.util.Arrays;

public class SignUp extends JFrame {

    //ID
    private JLabel jl_ID;
    private JTextField jf_ID;
    private JButton btn_ID_check;

    //�÷���
    private boolean flag_duplication = false; //ID �ߺ�üũ
    private boolean flag_submit = false; //ȸ������ �Ϸ�

    //PW
    private JLabel jl_PW;
    private JPasswordField jf_PW;
    private JLabel jl_PW_Check;
    private JPasswordField jf_PW_Check;

    //ȸ������ ��ư
    private JButton btn_SignUp;

    //�ڷΰ��� ��ư
    private JButton btn_Back;

    //������� �Է� �� ����
    private String id = null;
    private char[] pw = null;
    private char[] pw_check = null;

    //���̾�α� �޽���
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
        btn_ID_check = new JButton("�ߺ�Ȯ��");

        jl_ID.setBounds(50, 50, 100, 30);
        jf_ID.setBounds(150, 50, 170, 30);
        btn_ID_check.setBounds(350, 50, 100, 30);

        this.add(jl_ID);
        this.add(jf_ID);
        this.add(btn_ID_check);

        //�ߺ�Ȯ�� ��ư �̺�Ʈ
        btn_ID_check.addActionListener(e -> {

            //USER ID Duplication Check
            id = jf_ID.getText();
            userDTO.setUserid(id);

            int duplication = userDAO.duplicationID();

            switch (duplication) {
                case 0:
                    break;
                case 1:
                    msg = "���̵� �Է��ϼ���";
                    flag_duplication = false;
                    JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                    break;
                case 2:
                    msg = "�̹� ������� ���̵��Դϴ�.";
                    flag_duplication = false;
                    JOptionPane.showMessageDialog(null, msg, "", JOptionPane.WARNING_MESSAGE);
                    break;
                case 3:
                    msg = "����� ������ ���̵��Դϴ�. '" + id + "' ����Ͻðڽ��ϱ�?";
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

        //PW Ȯ��
        jl_PW_Check = new JLabel("PW Ȯ��");
        jf_PW_Check = new JPasswordField(10);

        jl_PW_Check.setBounds(50, 150, 100, 30);
        jf_PW_Check.setBounds(150, 150, 170, 30);

        this.add(jl_PW_Check);
        this.add(jf_PW_Check);

        //ȸ������ ��ư
        btn_SignUp = new JButton("Ȯ��");
        btn_SignUp.setBounds(150, 200, 170, 30);
        this.add(btn_SignUp);

        //ȸ������ ��ư �̺�Ʈ
        btn_SignUp.addActionListener( e -> {
            id = jf_ID.getText();
            pw = jf_PW.getPassword();
            pw_check = jf_PW_Check.getPassword();

            //���̵� �ߺ�Ȯ�� üũ
            if (flag_duplication == false)
                msg = "���̵� �ߺ�Ȯ���� ���ּ���.";
            else if (!Arrays.equals(pw, pw_check))
                msg = "��й�ȣ�� �ٸ��ϴ�.";
            else {
                userDTO.setUserid(id);
                userDTO.setPassword(pw);
                userDAO.submit();
                msg = "ȸ������ �Ǿ����ϴ�.";
                flag_submit = true;
            }

            //ȸ������ ��ư ���� �� ���̾�α�
            JOptionPane.showMessageDialog(null, msg, "", JOptionPane.PLAIN_MESSAGE);

            //ȸ������ �Ϸ��
            if(flag_submit) {
                this.dispose();
                new Login();
            }

        });

        //�ڷΰ��� ��ư
        btn_Back = new JButton("�ڷΰ���");
        btn_Back.setBounds(50, 200, 70, 30);
        this.add(btn_Back);

        //�ڷΰ��� ��ư �̺�Ʈ
        btn_Back.addActionListener( e -> {
            this.dispose();
            new Login();
        });



        setSize(500, 300);
        setResizable(false);
        setVisible(true);
    }
}