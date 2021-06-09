import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game extends JFrame implements ActionListener {
    // 그림판코드
    Container c;
    JPanel gui_panel, paint_panel;
    // 현 그림판 프레임은 GUI구성 패널, 그려지는 패널로 구성

    JButton pencil_bt, eraser_bt; // 연필,지우개 도구를 선택하는 버튼
    JButton colorSelect_bt1; // 색선택 버튼
    JButton colorSelect_bt2; // 색선택 버튼
    JButton colorSelect_bt3; // 색선택 버튼
    JButton colorSelect_bt4; // 색선택 버튼
    JButton sizeup;
    JButton sizedown;
    JLabel thicknessInfo_label; // 도구굵기 라벨
    JLabel colorSelect_label; // 색선택 라벨
    JLabel Question; // 문제 라벨

    JTextField thicknessControl_tf; // 도구굵기가 정해질 텍스트필드
    JTextField Question_tf; // 문제가 정해질 텍스트필드

    Color selectedColor;
    // 현 변수에 컬러가 저장되어 추후에 펜색상을 정해주는 변수의 매개변수로 사용된다.

    Graphics graphics; // Graphics2D 클래스의 사용을 위해 선언

    // Graphics2D는 쉽게 말해 기존 graphics의 상위버전이라고 생각하시먄 됩니다.
    // 버퍼드 이미지를 파일로 저장.
    Graphics2D g;
    int thickness = 10; // 현 변수는 그려지는 선의 굴기를 변경할때 변경값이 저장되는 변수
    int startX; // 마우스클릭시작의 X좌표값이 저장될 변수
    int startY; // 마우스클릭시작의 Y좌표값이 저장될 변수
    int endX; // 마우스클릭종료의 X좌표값이 저장될 변수
    int endY; // 마우스클릭종료의 Y좌표값이 저장될 변수

    boolean tf = false;
    /*
     * 변 boolean 변수는 처음에 연필로 그리고 지우개로 지운다음 다시 연필로 그릴때 기본색인 검은색으로 구분시키고 만약 프로그램 시작시
     * 색선택후 그 선택된 색이 지우개로 지우고 다시 연필로 그릴때 미리 정해진 색상으로 구분하는 변수인데.. 뭐 그리 중요한 변수는 아니다..
     */

    // 계정 정보 받아오기
    UserInfo userInfo = UserInfo.getInstance();
    private String ID = null;

    void setUserID() {
        ID = userInfo.getID();
    }

    // 퀴즈
    private JLabel jl_quiz;
    private JTextField jf_quiz;
    private String quiz = null;
    private JPanel test;

    void setQuizPanel() {
        jl_quiz = new JLabel("문제");
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

    // 유저 패널
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


    // 시작 패널
    private JButton btn_start;

    void setStartPanel() {
        btn_start = new JButton("시작");

        btn_start.setBounds(10, 615, 280, 50);
        btn_start.setEnabled(false);

        this.add(btn_start);
    }

    // 나가기 패널
    private JButton btn_quit;

    void setQuitPanel() {
        btn_quit = new JButton("나가기");

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

    // 클라이언트 소켓
    JPanel Chat_Window;
    private static Socket clientSocket = null;
    private static BufferedWriter os = null;
    private static BufferedReader is = null;
    private static boolean closed = false;

    private Receiver receiver = null; // JTextArea를 상속받고 Runnable 인터페이스를 구현한 클래스로서 받은 정보를 담는 객체
    private JTextField sender = null; // JTextField 객체로서 보내는 정보를 담는 객체

    // DB
    private UserDAO userDAO = UserDAO.getInstance();
    private UserDTO userDTO = UserDTO.getInstance();

    void Chat_Client() {
        setTitle("클라이언트 채팅 창"); // 프레임 타이틀
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 종료 버튼(X)을 클릭하면 프로그램 종료\

        Chat_Window = new JPanel();

        Chat_Window.setBounds(1200, 0, 350, 715);

        Chat_Window.setLayout(new BorderLayout()); // BorderLayout 배치관리자의 사용
        receiver = new Receiver(); // 서버에서 받은 메시지를 력할 컴퍼넌트
        receiver.setOpaque(true);
        receiver.setBackground(Color.white);
        receiver.setEditable(false); // 편집 불가

        sender = new JTextField();
        sender.addActionListener(this);

        Chat_Window.add(new JScrollPane(receiver), BorderLayout.CENTER); // 스크롤바를 위해 ScrollPane 이용
        Chat_Window.add(sender, BorderLayout.SOUTH);

        this.add(Chat_Window);

        // 창 닫기 눌렀을때
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("창 닫음");

                try {
                    // System.out.println("창닫고 try");
                    os.write("/quit" + "\r\n");
                    os.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        setVisible(true); // 프레임이 화면에 나타나도록 설정

        try {
            setupConnection();
        } catch (IOException e) {
            handleError(e.getMessage());
        }

        Thread th = new Thread(receiver); // 상대로부터 메시지 수신을 위한 스레드 생성
        th.start();
    }

    private void setupConnection() throws IOException {
        clientSocket = new Socket("localhost", 9090); // 클라이언트 소켓 생성

        int pos = receiver.getText().length();
        receiver.setCaretPosition(pos); // caret 포지션을 가장 마지막으로 이동

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
        g.setStroke(new BasicStroke(Pointwords[2], BasicStroke.CAP_ROUND, 0)); // 선굵기

        g.drawLine(Pointwords[0] - 1 + 310, Pointwords[1] - 1 + 121, Pointwords[0] + 310, Pointwords[1] + 121); // 라인이
        // 그려지게
        // 되는부분

        g.setColor(new Color(Pointwords[3]));
    }

    // 서버로 부터 받는 부분
    private class Receiver extends JTextArea implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("run 메소드 돌아감");

            String responseLine;
            try {
                while ((responseLine = is.readLine()) != null) {
                    // 사용자 아이디 나타내기
                    if (responseLine.startsWith("유저목록 ")) {
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

                        receiver.append(responseLine + "\n");// textarea에 계속 붙임
                        System.out.println(responseLine);
                        if (responseLine.indexOf("*** Bye") != -1)// 클라이언트가 /quit 입력해서 끝냈을 때
                            break;
                    }

                }
                closed = true;
            } catch (IOException e) {
                handleError(e.getMessage());
            }
        }
    }

    // 서버로 보내는 부분
    @Override
    public void actionPerformed(ActionEvent e) { // JTextField에 <Enter> 키 처리
        if (e.getSource() == sender) {
            String msg = sender.getText(); // 텍스트 필드에 사용자가 입력한 문자열
            try {
                os.write(msg + "\r\n");
                os.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // 문자열 전송
            sender.setText(null); // 입력창의 문자열 지움
        }
    }

    Game() { // Paint클래스의 디폴트(Default)생성자로 기본적인 GUI구성을 해주는 역할을 한다.
        setLayout(null); // 기본 프레임의 레이아웃을 초기화 시켜 패널을 개발자가 직접 다룰수 있게 됨
        setTitle("그림판"); // 프레임 타이틀 지정
        setSize(1600, 770); // 프레임 사이즈 지정
        setLocationRelativeTo(null); // 프로그램 실행시 화면 중앙에 출력
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setStartPanel();
        this.setQuitPanel();
        this.setQuizPanel();
        this.setUserPanel();


        // 프레임 우측상단에 X버튼을 눌렀을떄의 기능 정의
        c = getContentPane();
        gui_panel = new JPanel(); // 프레임 상단에 버튼, 텍스트필드, 라벨등이 UI가 들어갈 패널
        gui_panel.setBackground(Color.GRAY); // 패널의 배경색을 회색으로 지정
        gui_panel.setLayout(null);
        // gui_panel의 레이아웃을 null지정하여 컴포넌트들의 위치를 직접 지정해줄수 있다.

        pencil_bt = new JButton("연필"); // 연필 버튼 생성
        pencil_bt.setFont(new Font("함초롱돋움", Font.BOLD, 25)); // 버튼 폰트및 글씨 크기 지정
        pencil_bt.setBackground(Color.LIGHT_GRAY); // 연필버튼 배경색 밝은회색으로 지정

        eraser_bt = new JButton("지우개"); // 지우개 버튼 생성
        eraser_bt.setFont(new Font("함초롱돋움", Font.BOLD, 25)); // 버튼 폰트및 글씨 크기 지정
        eraser_bt.setBackground(Color.WHITE); // 지우개 버튼 배경색 희색으로 지정
        colorSelect_label = new JLabel("색선택"); // 선색상 버튼 생성
        colorSelect_label.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        colorSelect_bt1 = new JButton(); // 선색상 버튼 생성
        colorSelect_bt2 = new JButton(); // 선색상 버튼 생성
        colorSelect_bt3 = new JButton(); // 선색상 버튼 생성
        colorSelect_bt4 = new JButton(); // 선색상 버튼 생성

        Question = new JLabel("문제 : ");
        Question_tf = new JTextField(20);
        // 이미지 버튼으로 바꿀거임.
        sizeup = new JButton(new ImageIcon("bu1.png"));
        sizedown = new JButton(new ImageIcon("bu2.png"));
        // new ImageIcon("img/bu1.png")

        thicknessInfo_label = new JLabel("굵기");
        // 도구굴기 라벨 지정 / 밑에서 나올 텍스트필드의 역할을 설명
        thicknessInfo_label.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        // 도구굵기 라벨 폰트및 글씨 크기 지정

        thicknessControl_tf = new JTextField("10", 5); // 도구굵기 입력 텍스트필드 생성
        thicknessControl_tf.setHorizontalAlignment(JTextField.CENTER);
        // 텍스트필드 라인에 띄어지는 텍스트 중앙 정렬
        thicknessControl_tf.setFont(new Font("궁서체", Font.PLAIN, 25));
        // 텍스트필드 X길이 및 폰트 지정
        sizedown.setFont(new Font("함초롬돋움", Font.BOLD, 5));
        sizeup.setFont(new Font("함초롬돋움", Font.BOLD, 5));
        // 문제인터페이스 폰트설정.
        Question.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        Question_tf.setFont(new Font("함초롬돋움", Font.BOLD, 20));

        pencil_bt.setBounds(10, 10, 90, 55); // 연필 버튼 위치 지정
        eraser_bt.setBounds(105, 10, 109, 55); // 지우개 버튼 위치 지정
        colorSelect_label.setBounds(230, 10, 90, 55); // 선색상 버튼 위치 지정

        colorSelect_bt1.setBounds(297, 5, 45, 30); // 선색상 버튼 위치 지정
        colorSelect_bt2.setBounds(348, 5, 45, 30); // 선색상 버튼 위치 지정
        colorSelect_bt3.setBounds(297, 41, 45, 30); // 선색상 버튼 위치 지정
        colorSelect_bt4.setBounds(348, 41, 45, 30); // 선색상 버튼 위치 지정

        colorSelect_bt1.setOpaque(true);
        colorSelect_bt2.setOpaque(true);
        colorSelect_bt3.setOpaque(true);
        colorSelect_bt4.setOpaque(true);

        colorSelect_bt1.setBorderPainted(false);
        colorSelect_bt2.setBorderPainted(false);
        colorSelect_bt3.setBorderPainted(false);
        colorSelect_bt4.setBorderPainted(false);

        thicknessInfo_label.setBounds(410, 10, 70, 55); // 도구굵기 라벨 위치 지정
        thicknessControl_tf.setBounds(460, 22, 50, 35); // 도구굵기 텍스트필드 위치 지정
        sizeup.setBounds(530, 5, 40, 30); // 지우개 버튼 위치 지정
        sizedown.setBounds(530, 37, 40, 30); // 연필 버튼 위치 지정

        Question.setBounds(650, 22, 80, 35); // 도구굵기 텍스트필드 위치 지정
        Question_tf.setBounds(720, 22, 90, 35); // 도구굵기 텍스트필드 위치 지정

        gui_panel.add(pencil_bt); // gui_panel에 연필 버튼 추가
        gui_panel.add(eraser_bt); // gui_panel에 지우개 버튼 추가
        gui_panel.add(colorSelect_label); // gui_panel에 선색상 버튼 추가
        gui_panel.add(colorSelect_bt1); // gui_panel에 선색상 버튼 추가
        gui_panel.add(colorSelect_bt2); // gui_panel에 선색상 버튼 추가
        gui_panel.add(colorSelect_bt3); // gui_panel에 선색상 버튼 추가
        gui_panel.add(colorSelect_bt4); // gui_panel에 선색상 버튼 추가
        gui_panel.add(thicknessInfo_label); // gui_panel에 도구굵기 라벨 추가
        gui_panel.add(thicknessControl_tf); // gui_panel에 도구굵기 텍스트필드 추가
        gui_panel.add(sizedown); // gui_panel에 선색상 버튼 추가
        gui_panel.add(sizeup); // gui_panel에 선색상 버튼 추가
        gui_panel.add(Question);// gui_panel에 문제라벨 추가
        gui_panel.add(Question_tf);// gui_panel에 문제 텍스트필드 추가

        gui_panel.setBounds(300, 0, 900, 75); // gui_panel이 프레임에 배치될 위치 지정

        ////////////////////////////////////////////////// ↑ 패널 구분 ↓

        paint_panel = new JPanel(); // 그림이 그려질 패널 생성
        paint_panel.setBackground(Color.WHITE); // 패널의 배경색 하얀색
        paint_panel.setLayout(null);
        // paint_panel의 레이아웃을 null해줘 패널 자체를 setBounds로 위치를 조정할수 있다.

        paint_panel.setBounds(300, 90, 885, 620); // paint_panel의 위치 조정

        c.add(gui_panel); // 메인프레임에 gui패널 추가 - 위치는 위에서 다 정해줌
        c.add(paint_panel); // 메인프레임에 paint패널 추가 - 위치는 위에서 다 정해줌
        setUserID();
        setUserPanel();
        Chat_Client();

        setVisible(true); // 메인프레임을 보이게 한다.

        graphics = getGraphics(); // 그래픽초기화
        g = (Graphics2D) graphics;
        // 기존의 graphics변수를 Graphics2D로 변환후 Graphics2D에 초기화
        // 일반적인 Graphics가 아닌 Graphics2D를 사용한 이유는 펜의 굵기와 관련된 기능을
        // 수행하기 위하여 Graphics2D 클래스를 객체화함
        g.setColor(selectedColor);
        // 그려질 선(=선도 그래픽)의 색상을 selectedColor의 값으로 설정

        /////////////////////////////////////////////////// ↓ 액션 처리부분

        paint_panel.addMouseListener(new MouseListener() {
            // paint_panel에서의 MouseListener 이벤트 처리
            @Override
            public void mousePressed(MouseEvent e) {
                // paint_panel에 마우스 눌림의 액션이 있을떄 밑 메소드 실행
                startX = e.getX(); // 마우스가 눌렸을때 그때의 X좌표값으로 초기화
                startY = e.getY(); // 마우스가 눌렸을때 그때의 Y좌표값으로 초기화
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            } // 클릭이벤트 처리

            @Override
            public void mouseEntered(MouseEvent e) {
            } // paint_panel범위 내에 진입시 이벤트 처리

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
                // paint_panel에서 마우스 드래그 액션이처리될떄 밑 메소드 실행
                thickness = Integer.parseInt(thicknessControl_tf.getText());
                // 텍스트필드부분에서 값을 값고와 thickness변수에 대입

                endX = e.getX();
                // 드래그 되는 시점에서 X좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.

                endY = e.getY();
                // 드래그 되는 시점에서 Y좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.

                g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, 0)); // 선굵기

                // id변수를 하나 받아오고 그 아이디인사람만 DrawLine을 할 수 있게 설정하면됌.

                // 서버에서 보내면 값을 받아서 그려줌. 여기에도 아이디변수 넣어서 자신이 아닐경우만 그려지게

                // /지정해둔 범위내에서 그림이 그려지고 좌표를 서버로 보냄.
                if ((endX - thickness / 2 >= 10 && endY - thickness / 2 >= 10)
                        && (endX - thickness / 2 <= 860 && endY - thickness / 2 <= 610)) {
                    if ((startX - thickness / 2 >= 10 && startY - thickness / 2 >= 10)
                            && (startX - thickness / 2 <= 860 && startY - thickness / 2 <= 610)) {
                        g.drawLine(startX + 310, startY + 121, endX + 310, endY + 121); // 라인이 그려지게 되는부분

                        String Point = endX + " " + endY + " " + thickness + " " + g.getColor().getRGB(); // 텍스트 필드에
                        // 사용자가 입력한
                        // 문자열
                        try {
                            os.write("/Point" + Point + "\r\n");
                            os.flush();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } // 문자열 전송

                    }
                }

                startX = endX;
                // 시작부분이 마지막에 드래그된 X좌표로 찍혀야 다음에 이어 그려질수 있다.
                startY = endY;
                // 시작부분이 마지막에 드래그된 Y좌표로 찍혀야 다음에 이어 그려질수 있다.

                // 클라이언트에서 다시 받아서 그림

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

            /*
             * 인터페이스화 했기 때문에 그 인터페이스 에 정의된 메소드를 전부다 오버라이딩 해줘야 함으로 구지 사용되지지 않는 메소드도 서브 클래스에서
             * 전부다 오버라이딩 해줘야한다.
             */

        });
        // paint_panel에 마우스 모션리스너 추가
        pencil_bt.addActionListener(new ToolActionListener()); // 연필버튼 액션처리
        eraser_bt.addActionListener(new ToolActionListener()); // 지우개버튼 액션처리
        colorSelect_bt1.addActionListener(new ActionListener() {
            // 선색상버튼 액션처리를 익명클래스로 작성
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; // 위에서 변수 설명을 했으므로 스킵..
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor에 선택된색으로 초기화
                colorSelect_bt1.setBackground(selectedColor);
                g.setColor(selectedColor);
                // 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
        colorSelect_bt2.addActionListener(new ActionListener() {
            // 선색상버튼 액션처리를 익명클래스로 작성
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; // 위에서 변수 설명을 했으므로 스킵..
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor에 선택된색으로 초기화
                colorSelect_bt2.setBackground(selectedColor);
                g.setColor(selectedColor);
                // 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
        colorSelect_bt3.addActionListener(new ActionListener() {
            // 선색상버튼 액션처리를 익명클래스로 작성
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; // 위에서 변수 설명을 했으므로 스킵..
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor에 선택된색으로 초기화
                colorSelect_bt3.setBackground(selectedColor);
                g.setColor(selectedColor);
                // 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
        colorSelect_bt4.addActionListener(new ActionListener() {
            // 선색상버튼 액션처리를 익명클래스로 작성
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; // 위에서 변수 설명을 했으므로 스킵..
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
                // selectedColor에 선택된색으로 초기화
                colorSelect_bt4.setBackground(selectedColor);
                g.setColor(selectedColor);
                // 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
        sizeup.addActionListener(new ActionListener() {
            // 사이즈업 버튼 누를시 크기가 2만큼 증가
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                if (thickness < 100) {
                    thickness = Integer.parseInt(thicknessControl_tf.getText());
                    g.setStroke(new BasicStroke(thickness + 2, BasicStroke.CAP_ROUND, 0)); // 선굵기
                    thicknessControl_tf.setText(String.valueOf(thickness + 2));
                }
            }
        });
        sizedown.addActionListener(new ActionListener() {
            // 사이즈다운 버튼 누를시 크기가 2만큼 감소
            @Override
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                if (thickness >= 2) {
                    thickness = Integer.parseInt(thicknessControl_tf.getText());
                    g.setStroke(new BasicStroke(thickness - 2, BasicStroke.CAP_ROUND, 0)); // 선굵기
                    thicknessControl_tf.setText(String.valueOf(thickness - 2));
                }
            }
        });

    }

    public class ToolActionListener implements ActionListener {
        // 연필,지우개 버튼의 액션처리시 실행되는 클래스
        @Override
        public void actionPerformed(ActionEvent e) {
            // 오버라이딩된 actionPerformed메소드 실행
            if (e.getSource() == pencil_bt) { // 연필버튼이 눌렸을떄 밑 if문장 블록범위내 문장 실행
                if (tf == false)
                    g.setColor(Color.BLACK); // 그려지는 색상을 검은색 지정
                else
                    g.setColor(selectedColor); // 그려지는 색상을 selectedColor변수의 값으로 지정
            } else if (e.getSource() == eraser_bt) {
                // 지우개버튼이 눌렸을떄 밑 if문장 블록범위내 문장 실행
                g.setColor(Color.WHITE);
                // 그려지는 색상을 흰색으로 해줬기 때문에 흰색으로 펜이 그려져 지워지는 것처럼 보이게 한다.

            }
        }
    }
}