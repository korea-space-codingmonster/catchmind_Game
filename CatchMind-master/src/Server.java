import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 3;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String args[]) {

        int portNumber = 9090;

        try {
        	
        	System.out.println("서버를 시작합니다.");
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        // Create a client socket for each connection and pass it to a new client
        // thread.
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                // 들어오는 클라이언트 마다 쓰레드를 하나씩 줌
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        System.out.println("start");
                        break;
                    }
                }

                // 3명 다 들어왔을때, 게임 시작 버튼 활성화
                if (i == maxClientsCount - 1) {

                }

                // 클라이언트가 이미 3명이 들어와 있을 때
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("게임에 참여 할 수 없습니다.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */

// 클라이언트 각각의 thread를 연다
class clientThread extends Thread {

    private BufferedReader is = null;
    private BufferedWriter os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;

    static String name_array[] = new String[3];

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    @Override
    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {// Create input and output streams for this client.

            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            // 사용자 이름 입력받음

            String name = is.readLine().trim();

            //유저 이름을 배열에 순서대로 넣기
            //한 유저가 나가면 나간 자리에 채워짐
            for (int name_num = 0; name_num < 3; name_num++) {
                if (name_array[name_num] == null) {
                    name_array[name_num] = name;
                    System.out.println(name_num);
                    break;
                }
            }

            //처음 들어 온 사용자에게 안내 메세지
            os.write("안녕하세요 " + name + " 님" + "\r\n");
            os.flush();

            //처음 들어 온 사용자에게 전에 들어 온 사용자의 아이디를 넘겨줌
            for (int name_num = 0; name_num < 3; name_num++) {
                if (name_array[name_num] != null) {
                    os.write("유저목록 " + name_array[name_num] +" " + name_num+" "+"\r\n");
                    os.flush();
                }
            }

            // 사용자 이름 입력한 사용자 이외의 모든 사용자에게 새로운 사용자의 입장을 알림
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.write("** " + name + " 님이 방에 들어왔습니다. " + "\r\n");
                    threads[i].os.flush();
//               for (int name_num = 0; name_num < 3; name_num++) {
//                  if (name_array[name_num] != null) {
//                     os.write("유저목록 " + name_array[name_num] +" " + name_num+" "+"\r\n");
//                     os.flush();
//                  }
//               }
                }
            }
            while (true) {
                String line = is.readLine();
                System.out.println(line);
                String Point_Server;

                // /좌표값 받는 부분
                if (line.startsWith("/Point")) {
                    // 좌표값 받아서 클라이언트에 넘겨줌
                    Point_Server = line.substring(6);
                    os.write("/Point" + Point_Server + "\r\n");
                    os.flush();

                }

                // /quit 를 입력한 사용자의 대화창 중단
                if (line.startsWith("/quit")) {
                    // 퇴장한 사용자에게 띄우는 메세지
                    os.write("게임 종료" + "\r\n");
                    os.flush();
                    break;
                }

                // 사용자들에게 메세지 뿌리기
                for (int i = 0; i < maxClientsCount; i++) {


                    if (threads[i] != null && !line.startsWith("/Point")) {
                        threads[i].os.write(name + ": " + line + "\r\n");
                        threads[i].os.flush();

                    }
                    if (threads[i] != null && line.startsWith("/Point")) {
                        threads[i].os.write(line + "\r\n");
                        threads[i].os.flush();
                    }
                }
            }

            // 한 사용자가 나가면 다른 사용자들에게 나간 사용자를 알려줌
            // 사용자가 한명이라도 나가면 게임 종료
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.write(name + "님이 게임을 종료하셨습니다." + "\r\n");
                    threads[i].os.flush();
                    for (int name_num = 0; name_num < 3; name_num++) {
                        if (name_array[name_num].equals(name)) {
                            name_array[name_num] = null;
                        }

                    }
                    JOptionPane.showMessageDialog(null, "유저1 스코어" + "\n" + "유저2 스코어" + "\n" + "유저3 스코어", "학생 정보",
                            JOptionPane.PLAIN_MESSAGE);
                } else if (threads[i] == this) {
                    threads[i] = null;
                }
            }

            // Clean up. Set the current thread variable to null so that a new
            // client could be accepted by the server.

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

            // output stream, close the input stream, socket 닫기
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}