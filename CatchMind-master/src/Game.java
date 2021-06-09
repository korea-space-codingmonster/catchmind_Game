import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game extends JFrame implements ActionListener {
    // �׸����ڵ�
    Container c;
    JPanel gui_panel, paint_panel;
    // �� �׸��� �������� GUI���� �г�, �׷����� �гη� ����

    JButton pencil_bt, eraser_bt; // ����,���찳 ������ �����ϴ� ��ư
    JButton colorSelect_bt1; // ������ ��ư
    JButton colorSelect_bt2; // ������ ��ư
    JButton colorSelect_bt3; // ������ ��ư
    JButton colorSelect_bt4; // ������ ��ư
    JButton sizeup;
    JButton sizedown;
    JLabel thicknessInfo_label; // �������� ��
    JLabel colorSelect_label; // ������ ��
    JLabel Question; // ���� ��

    JTextField thicknessControl_tf; // �������Ⱑ ������ �ؽ�Ʈ�ʵ�
    JTextField Question_tf; // ������ ������ �ؽ�Ʈ�ʵ�

    Color selectedColor;
    // �� ������ �÷��� ����Ǿ� ���Ŀ� ������� �����ִ� ������ �Ű������� ���ȴ�.

    Graphics graphics; // Graphics2D Ŭ������ ����� ���� ����

    // Graphics2D�� ���� ���� ���� graphics�� ���������̶�� �����ϽÐ� �˴ϴ�.
    // ���۵� �̹����� ���Ϸ� ����.
    Graphics2D g;
    int thickness = 10; // �� ������ �׷����� ���� ���⸦ �����Ҷ� ���氪�� ����Ǵ� ����
    int startX; // ���콺Ŭ�������� X��ǥ���� ����� ����
    int startY; // ���콺Ŭ�������� Y��ǥ���� ����� ����
    int endX; // ���콺Ŭ�������� X��ǥ���� ����� ����
    int endY; // ���콺Ŭ�������� Y��ǥ���� ����� ����

    boolean tf = false;
    /*
     * �� boolean ������ ó���� ���ʷ� �׸��� ���찳�� ������� �ٽ� ���ʷ� �׸��� �⺻���� ���������� ���н�Ű�� ���� ���α׷� ���۽�
     * �������� �� ���õ� ���� ���찳�� ����� �ٽ� ���ʷ� �׸��� �̸� ������ �������� �����ϴ� �����ε�.. �� �׸� �߿��� ������ �ƴϴ�..
     */

    // ���� ���� �޾ƿ���
    UserInfo userInfo = UserInfo.getInstance();
    private String ID = null;

    void setUserID() {
        ID = userInfo.getID();
    }

    // ����
    private JLabel jl_quiz;
    private JTextField jf_quiz;
    private String quiz = null;
    private JPanel test;

    void setQuizPanel() {
        jl_quiz = new JLabel("����");
        jf_quiz = new JTextField(10);
        jf_quiz.setFocusable(false);

        test = new JPanel();

        jl_quiz.setBounds(10, 10, 30, 30);
        jf_quiz.setBounds(40, 10, 100, 30);
        test.setBounds(0, 728, 10, 10);
        test.setBackground(Color.BLACK);

        this.add(test);

        this.add(jl_quiz);
        this.add(jf_quiz);
    }

    // ���� �г�
    private JPanel jp_user1;
    private JPanel jp_user2;
    private JPanel jp_user3;

    private JPanel jp_user1_north;
    private JPanel jp_user2_north;
    private JPanel jp_user3_north;
    private JPanel jp_user1_center;
    private JPanel jp_user2_center;
    private JPanel jp_user3_center;

    private JLabel jl_user1;
    private JLabel jl_user2;
    private JLabel jl_user3;

    private String id_user1 = "";
    private String id_user2 = "";
    private String id_user3 = "";

    private JLabel jl_score_user1;
    private JLabel jl_score_user2;
    private JLabel jl_score_user3;

    private int score_user1 = 0;
    private int score_user2 = 0;
    private int score_user3 = 0;

    void setUserPanel() {

        jp_user1 = new JPanel();
        jp_user2 = new JPanel();
        jp_user3 = new JPanel();

        jp_user1_north = new JPanel();
        jp_user2_north = new JPanel();
        jp_user3_north = new JPanel();
        jp_user1_center = new JPanel();
        jp_user2_center = new JPanel();
        jp_user3_center = new JPanel();

        jp_user1.setLayout(new BorderLayout());
        jp_user2.setLayout(new BorderLayout());
        jp_user3.setLayout(new BorderLayout());

        jp_user1_north.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp_user2_north.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp_user3_north.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp_user1_center.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp_user2_center.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp_user3_center.setLayout(new FlowLayout(FlowLayout.CENTER));

        jp_user1.setBounds(10, 45, 280, 180);
        jp_user2.setBounds(10, 235, 280, 180);
        jp_user3.setBounds(10, 425, 280, 180);

        jp_user1_north.setBackground(Color.WHITE);
        jp_user2_north.setBackground(Color.WHITE);
        jp_user3_north.setBackground(Color.WHITE);
        jp_user1_center.setBackground(Color.WHITE);
        jp_user2_center.setBackground(Color.WHITE);
        jp_user3_center.setBackground(Color.WHITE);

        this.add(jp_user1);
        this.add(jp_user2);
        this.add(jp_user3);

        jl_user1 = new JLabel();
        jl_user2 = new JLabel();
        jl_user3 = new JLabel();
        jl_score_user1 = new JLabel(Integer.toString(score_user1));
        jl_score_user2 = new JLabel(Integer.toString(score_user2));
        jl_score_user3 = new JLabel(Integer.toString(score_user3));

        jl_score_user1.setFont(jl_score_user1.getFont().deriveFont(30.0f));
        jl_score_user2.setFont(jl_score_user1.getFont().deriveFont(30.0f));
        jl_score_user3.setFont(jl_score_user1.getFont().deriveFont(30.0f));

        jp_user1.add(jp_user1_north, BorderLayout.NORTH);
        jp_user2.add(jp_user2_north, BorderLayout.NORTH);
        jp_user3.add(jp_user3_north, BorderLayout.NORTH);
        jp_user1.add(jp_user1_center, BorderLayout.CENTER);
        jp_user2.add(jp_user2_center, BorderLayout.CENTER);
        jp_user3.add(jp_user3_center, BorderLayout.CENTER);
        jp_user1_north.add(jl_user1);
        jp_user2_north.add(jl_user2);
        jp_user3_north.add(jl_user3);
        jp_user1_center.add(jl_score_user1);
        jp_user2_center.add(jl_score_user2);
        jp_user3_center.add(jl_score_user3);

    }


    // ���� �г�
    private JButton btn_start;

    void setStartPanel() {
        btn_start = new JButton("����");

        btn_start.setBounds(10, 615, 280, 50);
        btn_start.setEnabled(false);

        this.add(btn_start);
    }

    // ������ �г�
    private JButton btn_quit;

    void setQuitPanel() {
        btn_quit = new JButton("������");

        btn_quit.setBounds(10, 670, 280, 50);

        this.add(btn_quit);

        btn_quit.addActionListener(e -> {
            try {
                os.write("/quit"+"\r\n");
                os.flush();
            } catch (IOException btn_quit) {
                System.out.println(btn_quit.getMessage());
            }

            this.dispose();
        });
    }

    // Ŭ���̾�Ʈ ����
    JPanel Chat_Window;
    private static Socket clientSocket = null;
    private static BufferedWriter os = null;
    private static BufferedReader is = null;
    private static boolean closed = false;

    private Receiver receiver = null; // JTextArea�� ��ӹް� Runnable �������̽��� ������ Ŭ�����μ� ���� ������ ��� ��ü
    private JTextField sender = null; // JTextField ��ü�μ� ������ ������ ��� ��ü

    // DB
    private UserDAO userDAO = UserDAO.getInstance();
    private UserDTO userDTO = UserDTO.getInstance();

    void Chat_Client() {
        setTitle("Ŭ���̾�Ʈ ä�� â"); // ������ Ÿ��Ʋ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ���� ��ư(X)�� Ŭ���ϸ� ���α׷� ����\

        Chat_Window = new JPanel();

        Chat_Window.setBounds(1200, 0, 350, 715);

        Chat_Window.setLayout(new BorderLayout()); // BorderLayout ��ġ�������� ���
        receiver = new Receiver(); // �������� ���� �޽����� ���� ���۳�Ʈ
        receiver.setOpaque(true);
        receiver.setBackground(Color.white);
        receiver.setEditable(false); // ���� �Ұ�

        sender = new JTextField();
        sender.addActionListener(this);

        Chat_Window.add(new JScrollPane(receiver), BorderLayout.CENTER); // ��ũ�ѹٸ� ���� ScrollPane �̿�
        Chat_Window.add(sender, BorderLayout.SOUTH);

        this.add(Chat_Window);

        // â �ݱ� ��������
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("â ����");

                try {
                    // System.out.println("â�ݰ� try");
                    os.write("/quit" + "\r\n");
                    os.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        setVisible(true); // �������� ȭ�鿡 ��Ÿ������ ����

        try {
            setupConnection();
        } catch (IOException e) {
            handleError(e.getMessage());
        }

        Thread th = new Thread(receiver); // ���κ��� �޽��� ������ ���� ������ ����
        th.start();
    }

    private void setupConnection() throws IOException {
        clientSocket = new Socket("localhost", 9090); // Ŭ���̾�Ʈ ���� ����

        int pos = receiver.getText().length();
        receiver.setCaretPosition(pos); // caret �������� ���� ���������� �̵�

        // Open input and output streams.
        try {
            os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os.write(ID + "\r\n");
            os.flush();
            id_user1 = ID;
            System.out.println(id_user1);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host :" + "host");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host :" + "host");
        }
    }

    private static void handleError(String string) {
        System.out.println(string);
        System.exit(1);
    }

    public void PointRepaint(String responseLine) {

        String Point_Game;
        Point_Game = responseLine.substring(6);
        String[] words = Point_Game.split(" ");
        int Pointwords[] = new int[4];
        for (int i = 0; i < 4; i++) {

            Pointwords[i] = Integer.parseInt(words[i]);

        }
        g.setStroke(new BasicStroke(Pointwords[2], BasicStroke.CAP_ROUND, 0)); // ������

        g.drawLine(Pointwords[0] - 1 + 310, Pointwords[1] - 1 + 121, Pointwords[0] + 310, Pointwords[1] + 121); // ������
        // �׷�����
        // �Ǵºκ�

        g.setColor(new Color(Pointwords[3]));
    }

    // ������ ���� �޴� �κ�
    private class Receiver extends JTextArea implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("run �޼ҵ� ���ư�");

            String responseLine;
            try {
                while ((responseLine = is.readLine()) != null) {
                    // ����� ���̵� ��Ÿ����
                    if (responseLine.startsWith("������� ")) {
                        String[] getID = responseLine.split(" ");
                        System.out.println(getID[2]);
                        switch (getID[2]) {
                            case "0":
                                id_user1 = getID[1];
                                jl_user1.setText(id_user1);
                                break;
                            case "1":
                                id_user2 = getID[1];
                                jl_user2.setText(id_user2);
                                break;
                            case "2":
                                id_user3 = getID[1];
                                jl_user3.setText(id_user3);
                                break;
                            default:
                                break;
                        }
                    }

                    if (responseLine.startsWith("** ")) {
                        if (id_user2.equals("")) {
                            String[] getID = responseLine.split(" ");
                            id_user2 = getID[1];
                            jl_user2.setText(id_user2);
                        } else if (id_user3.equals("")) {
                            String[] getID = responseLine.split(" ");
                            id_user3 = getID[1];
                            jl_user3.setText(id_user3);
                        }
                    }

                    if (responseLine.startsWith("/Point")) {
                        PointRepaint(responseLine);
                    }

                    if (!responseLine.startsWith("/Point")) {

                        receiver.append(responseLine + "\n");// textarea�� ��� ����
                        System.out.println(responseLine);
                        if (responseLine.indexOf("*** Bye") != -1)// Ŭ���̾�Ʈ�� /quit �Է��ؼ� ������ ��
                            break;
                    }

                }
                closed = true;
            } catch (IOException e) {
                handleError(e.getMessage());
            }
        }
    }

    // ������ ������ �κ�
    @Override
    public void actionPerformed(ActionEvent e) { // JTextField�� <Enter> Ű ó��
        if (e.getSource() == sender) {
            String msg = sender.getText(); // �ؽ�Ʈ �ʵ忡 ����ڰ� �Է��� ���ڿ�
            try {
                os.write(msg + "\r\n");
                os.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // ���ڿ� ����
            sender.setText(null); // �Է�â�� ���ڿ� ����
        }
    }

    Game() { // PaintŬ������ ����Ʈ(Default)�����ڷ� �⺻���� GUI������ ���ִ� ������ �Ѵ�.
        setLayout(null); // �⺻ �������� ���̾ƿ��� �ʱ�ȭ ���� �г��� �����ڰ� ���� �ٷ�� �ְ� ��
        setTitle("�׸���"); // ������ Ÿ��Ʋ ����
        setSize(1600, 770); // ������ ������ ����
        setLocationRelativeTo(null); // ���α׷� ����� ȭ�� �߾ӿ� ���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setStartPanel();
        this.setQuitPanel();
        this.setQuizPanel();
        this.setUserPanel();


        // ������ ������ܿ� X��ư�� ���������� ��� ����
        c = getContentPane();
        gui_panel = new JPanel(); // ������ ��ܿ� ��ư, �ؽ�Ʈ�ʵ�, �󺧵��� UI�� �� �г�
        gui_panel.setBackground(Color.GRAY); // �г��� ������ ȸ������ ����
        gui_panel.setLayout(null);
        // gui_panel�� ���̾ƿ��� null�����Ͽ� ������Ʈ���� ��ġ�� ���� �������ټ� �ִ�.

        pencil_bt = new JButton("����"); // ���� ��ư ����
        pencil_bt.setFont(new Font("���ʷյ���", Font.BOLD, 25)); // ��ư ��Ʈ�� �۾� ũ�� ����
        pencil_bt.setBackground(Color.LIGHT_GRAY); // ���ʹ�ư ���� ����ȸ������ ����

        eraser_bt = new JButton("���찳"); // ���찳 ��ư ����
        eraser_bt.setFont(new Font("���ʷյ���", Font.BOLD, 25)); // ��ư ��Ʈ�� �۾� ũ�� ����
        eraser_bt.setBackground(Color.WHITE); // ���찳 ��ư ���� ������� ����
        colorSelect_label = new JLabel("������"); // ������ ��ư ����
        colorSelect_label.setFont(new Font("���ʷҵ���", Font.BOLD, 20));
        colorSelect_bt1 = new JButton(); // ������ ��ư ����
        colorSelect_bt2 = new JButton(); // ������ ��ư ����
        colorSelect_bt3 = new JButton(); // ������ ��ư ����
        colorSelect_bt4 = new JButton(); // ������ ��ư ����

        Question = new JLabel("���� : ");
        Question_tf = new JTextField(20);
        // �̹��� ��ư���� �ٲܰ���.
        sizeup = new JButton(new ImageIcon("bu1.png"));
        sizedown = new JButton(new ImageIcon("bu2.png"));
        // new ImageIcon("img/bu1.png")

        thicknessInfo_label = new JLabel("����");
        // �������� �� ���� / �ؿ��� ���� �ؽ�Ʈ�ʵ��� ������ ����
        thicknessInfo_label.setFont(new Font("���ʷҵ���", Font.BOLD, 20));
        // �������� �� ��Ʈ�� �۾� ũ�� ����

        thicknessControl_tf = new JTextField("10", 5); // �������� �Է� �ؽ�Ʈ�ʵ� ����
        thicknessControl_tf.setHorizontalAlignment(JTextField.CENTER);
        // �ؽ�Ʈ�ʵ� ���ο� ������� �ؽ�Ʈ �߾� ����
        thicknessControl_tf.setFont(new Font("�ü�ü", Font.PLAIN, 25));
        // �ؽ�Ʈ�ʵ� X���� �� ��Ʈ ����
        sizedown.setFont(new Font("���ʷҵ���", Font.BOLD, 5));
        sizeup.setFont(new Font("���ʷҵ���", Font.BOLD, 5));
        // �����������̽� ��Ʈ����.
        Question.setFont(new Font("���ʷҵ���", Font.BOLD, 20));
        Question_tf.setFont(new Font("���ʷҵ���", Font.BOLD, 20));

        pencil_bt.setBounds(10, 10, 90, 55); // ���� ��ư ��ġ ����
        eraser_bt.setBounds(105, 10, 109, 55); // ���찳 ��ư ��ġ ����
        colorSelect_label.setBounds(230, 10, 90, 55); // ������ ��ư ��ġ ����

        colorSelect_bt1.setBounds(297, 5, 45, 30); // ������ ��ư ��ġ ����
        colorSelect_bt2.setBounds(348, 5, 45, 30); // ������ ��ư ��ġ ����
        colorSelect_bt3.setBounds(297, 41, 45, 30); // ������ ��ư ��ġ ����
        colorSelect_bt4.setBounds(348, 41, 45, 30); // ������ ��ư ��ġ ����

        colorSelect_bt1.setOpaque(true);
        colorSelect_bt2.setOpaque(true);
        colorSelect_bt3.setOpaque(true);
        colorSelect_bt4.setOpaque(true);

        colorSelect_bt1.setBorderPainted(false);
        colorSelect_bt2.setBorderPainted(false);
        colorSelect_bt3.setBorderPainted(false);
        colorSelect_bt4.setBorderPainted(false);

        thicknessInfo_label.setBounds(410, 10, 70, 55); // �������� �� ��ġ ����
        thicknessControl_tf.setBounds(460, 22, 50, 35); // �������� �ؽ�Ʈ�ʵ� ��ġ ����
        sizeup.setBounds(530, 5, 40, 30); // ���찳 ��ư ��ġ ����
        sizedown.setBounds(530, 37, 40, 30); // ���� ��ư ��ġ ����

        Question.setBounds(650, 22, 80, 35); // �������� �ؽ�Ʈ�ʵ� ��ġ ����
        Question_tf.setBounds(720, 22, 90, 35); // �������� �ؽ�Ʈ�ʵ� ��ġ ����

        gui_panel.add(pencil_bt); // gui_panel�� ���� ��ư �߰�
        gui_panel.add(eraser_bt); // gui_panel�� ���찳 ��ư �߰�
        gui_panel.add(colorSelect_label); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(colorSelect_bt1); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(colorSelect_bt2); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(colorSelect_bt3); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(colorSelect_bt4); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(thicknessInfo_label); // gui_panel�� �������� �� �߰�
        gui_panel.add(thicknessControl_tf); // gui_panel�� �������� �ؽ�Ʈ�ʵ� �߰�
        gui_panel.add(sizedown); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(sizeup); // gui_panel�� ������ ��ư �߰�
        gui_panel.add(Question);// gui_panel�� ������ �߰�
        gui_panel.add(Question_tf);// gui_panel�� ���� �ؽ�Ʈ�ʵ� �߰�

        gui_panel.setBounds(300, 0, 900, 75); // gui_panel�� �����ӿ� ��ġ�� ��ġ ����

        ////////////////////////////////////////////////// �� �г� ���� ��

        paint_panel = new JPanel(); // �׸��� �׷��� �г� ����
        paint_panel.setBackground(Color.WHITE); // �г��� ���� �Ͼ��
        paint_panel.setLayout(null);
        // paint_panel�� ���̾ƿ��� null���� �г� ��ü�� setBounds�� ��ġ�� �����Ҽ� �ִ�.

        paint_panel.setBounds(300, 90, 885, 620); // paint_panel�� ��ġ ����

        c.add(gui_panel); // ���������ӿ� gui�г� �߰� - ��ġ�� ������ �� ������
        c.add(paint_panel); // ���������ӿ� paint�г� �߰� - ��ġ�� ������ �� ������
        setUserID();
        setUserPanel();
        Chat_Client();

        setVisible(true); // ������������ ���̰� �Ѵ�.

        graphics = getGraphics(); // �׷����ʱ�ȭ
        g = (Graphics2D) graphics;
        // ������ graphics������ Graphics2D�� ��ȯ�� Graphics2D�� �ʱ�ȭ
        // �Ϲ����� Graphics�� �ƴ� Graphics2D�� ����� ������ ���� ����� ���õ� �����
        // �����ϱ� ���Ͽ� Graphics2D Ŭ������ ��üȭ��
        g.setColor(selectedColor);
        // �׷��� ��(=���� �׷���)�� ������ selectedColor�� ������ ����

        /////////////////////////////////////////////////// �� �׼� ó���κ�

        paint_panel.addMouseListener(new MouseListener() {
            // paint_panel������ MouseListener �̺�Ʈ ó��
            @Override
            public void mousePressed(MouseEvent e) {
                // paint_panel�� ���콺 ������ �׼��� ������ �� �޼ҵ� ����
                startX = e.getX(); // ���콺�� �������� �׶��� X��ǥ������ �ʱ�ȭ
                startY = e.getY(); // ���콺�� �������� �׶��� Y��ǥ������ �ʱ�ȭ
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            } // Ŭ���̺�Ʈ ó��

            @Override
            public void mouseEntered(MouseEvent e) {
            } // paint_panel���� ���� ���Խ� �̺�Ʈ ó��

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        paint_panel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // paint_panel���� ���콺 �巡�� �׼���ó���ɋ� �� �޼ҵ� ����
                thickness = Integer.parseInt(thicknessControl_tf.getText());
                // �ؽ�Ʈ�ʵ�κп��� ���� ����� thickness������ ����

                endX = e.getX();
                // �巡�� �Ǵ� �������� X��ǥ�� ���� - �ؿ��� ������ǥ�� ����ǥ�� ���� ���־� ���� �׾����Եȴ�.

                endY = e.getY();
                // �巡�� �Ǵ� �������� Y��ǥ�� ���� - �ؿ��� ������ǥ�� ����ǥ�� ���� ���־� ���� �׾����Եȴ�.

                g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, 0)); // ������

                // id������ �ϳ� �޾ƿ��� �� ���̵��λ���� DrawLine�� �� �� �ְ� �����ϸ��.

                // �������� ������ ���� �޾Ƽ� �׷���. ���⿡�� ���̵𺯼� �־ �ڽ��� �ƴҰ�츸 �׷�����

                // /�����ص� ���������� �׸��� �׷����� ��ǥ�� ������ ����.
                if ((endX - thickness / 2 >= 10 && endY - thickness / 2 >= 10)
                        && (endX - thickness / 2 <= 860 && endY - thickness / 2 <= 610)) {
                    if ((startX - thickness / 2 >= 10 && startY - thickness / 2 >= 10)
                            && (startX - thickness / 2 <= 860 && startY - thickness / 2 <= 610)) {
                        g.drawLine(startX + 310, startY + 121, endX + 310, endY + 121); // ������ �׷����� �Ǵºκ�

                        String Point = endX + " " + endY + " " + thickness + " " + g.getColor().getRGB(); // �ؽ�Ʈ �ʵ忡
                        // ����ڰ� �Է���
                        // ���ڿ�
                        try {
                            os.write("/Point" + Point + "\r\n");
                            os.flush();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } // ���ڿ� ����

                    }
                }

                startX = endX;
                // ���ۺκ��� �������� �巡�׵� X��ǥ�� ������ ������ �̾� �׷����� �ִ�.
                startY = endY;
                // ���ۺκ��� �������� �巡�׵� Y��ǥ�� ������ ������ �̾� �׷����� �ִ�.

                // Ŭ���̾�Ʈ���� �ٽ� �޾Ƽ� �׸�

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

            /*
             * �������̽�ȭ �߱� ������ �� �������̽� �� ���ǵ� �޼ҵ带 ���δ� �������̵� ����� ������ ���� �������� �ʴ� �޼ҵ嵵 ���� Ŭ��������
             * ���δ� �������̵� ������Ѵ�.
             */

        });
        // paint_panel�� ���콺 ��Ǹ����� �߰�
        pencil_bt.addActionListener(new ToolActionListener()); // ���ʹ�ư �׼�ó��
        eraser_bt.addActionListener(new ToolActionListener()); // ���찳��ư �׼�ó��
        colorSelect_bt1.addActionListener(new ActionListener() {
            // �������ư �׼�ó���� �͸�Ŭ������ �ۼ�
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                tf = true; // ������ ���� ������ �����Ƿ� ��ŵ..
                JColorChooser chooser = new JColorChooser(); // JColorChooser Ŭ������üȭ
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor�� ���õȻ����� �ʱ�ȭ
                colorSelect_bt1.setBackground(selectedColor);
                g.setColor(selectedColor);
                // �׷����� ���� ������ selectedColor�� �Ű������� �Ͽ� ����
            }
        });
        colorSelect_bt2.addActionListener(new ActionListener() {
            // �������ư �׼�ó���� �͸�Ŭ������ �ۼ�
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                tf = true; // ������ ���� ������ �����Ƿ� ��ŵ..
                JColorChooser chooser = new JColorChooser(); // JColorChooser Ŭ������üȭ
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor�� ���õȻ����� �ʱ�ȭ
                colorSelect_bt2.setBackground(selectedColor);
                g.setColor(selectedColor);
                // �׷����� ���� ������ selectedColor�� �Ű������� �Ͽ� ����
            }
        });
        colorSelect_bt3.addActionListener(new ActionListener() {
            // �������ư �׼�ó���� �͸�Ŭ������ �ۼ�
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                tf = true; // ������ ���� ������ �����Ƿ� ��ŵ..
                JColorChooser chooser = new JColorChooser(); // JColorChooser Ŭ������üȭ
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor�� ���õȻ����� �ʱ�ȭ
                colorSelect_bt3.setBackground(selectedColor);
                g.setColor(selectedColor);
                // �׷����� ���� ������ selectedColor�� �Ű������� �Ͽ� ����
            }
        });
        colorSelect_bt4.addActionListener(new ActionListener() {
            // �������ư �׼�ó���� �͸�Ŭ������ �ۼ�
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                tf = true; // ������ ���� ������ �����Ƿ� ��ŵ..
                JColorChooser chooser = new JColorChooser(); // JColorChooser Ŭ������üȭ
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor�� ���õȻ����� �ʱ�ȭ
                colorSelect_bt4.setBackground(selectedColor);
                g.setColor(selectedColor);
                // �׷����� ���� ������ selectedColor�� �Ű������� �Ͽ� ����
            }
        });
        sizeup.addActionListener(new ActionListener() {
            // ������� ��ư ������ ũ�Ⱑ 2��ŭ ����
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                if (thickness < 100) {
                    thickness = Integer.parseInt(thicknessControl_tf.getText());
                    g.setStroke(new BasicStroke(thickness + 2, BasicStroke.CAP_ROUND, 0)); // ������
                    thicknessControl_tf.setText(String.valueOf(thickness + 2));
                }
            }
        });
        sizedown.addActionListener(new ActionListener() {
            // ������ٿ� ��ư ������ ũ�Ⱑ 2��ŭ ����
            @Override
            public void actionPerformed(ActionEvent e) { // �������̵�
                if (thickness >= 2) {
                    thickness = Integer.parseInt(thicknessControl_tf.getText());
                    g.setStroke(new BasicStroke(thickness - 2, BasicStroke.CAP_ROUND, 0)); // ������
                    thicknessControl_tf.setText(String.valueOf(thickness - 2));
                }
            }
        });

    }

    public class ToolActionListener implements ActionListener {
        // ����,���찳 ��ư�� �׼�ó���� ����Ǵ� Ŭ����
        @Override
        public void actionPerformed(ActionEvent e) {
            // �������̵��� actionPerformed�޼ҵ� ����
            if (e.getSource() == pencil_bt) { // ���ʹ�ư�� �������� �� if���� ��Ϲ����� ���� ����
                if (tf == false)
                    g.setColor(Color.BLACK); // �׷����� ������ ������ ����
                else
                    g.setColor(selectedColor); // �׷����� ������ selectedColor������ ������ ����
            } else if (e.getSource() == eraser_bt) {
                // ���찳��ư�� �������� �� if���� ��Ϲ����� ���� ����
                g.setColor(Color.WHITE);
                // �׷����� ������ ������� ����� ������ ������� ���� �׷��� �������� ��ó�� ���̰� �Ѵ�.

            }
        }
    }
}