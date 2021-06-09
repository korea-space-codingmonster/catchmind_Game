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
        	
        	System.out.println("������ �����մϴ�.");
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
                // ������ Ŭ���̾�Ʈ ���� �����带 �ϳ��� ��
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        System.out.println("start");
                        break;
                    }
                }

                // 3�� �� ��������, ���� ���� ��ư Ȱ��ȭ
                if (i == maxClientsCount - 1) {

                }

                // Ŭ���̾�Ʈ�� �̹� 3���� ���� ���� ��
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("���ӿ� ���� �� �� �����ϴ�.");
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

// Ŭ���̾�Ʈ ������ thread�� ����
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
            // ����� �̸� �Է¹���

            String name = is.readLine().trim();

            //���� �̸��� �迭�� ������� �ֱ�
            //�� ������ ������ ���� �ڸ��� ä����
            for (int name_num = 0; name_num < 3; name_num++) {
                if (name_array[name_num] == null) {
                    name_array[name_num] = name;
                    System.out.println(name_num);
                    break;
                }
            }

            //ó�� ��� �� ����ڿ��� �ȳ� �޼���
            os.write("�ȳ��ϼ��� " + name + " ��" + "\r\n");
            os.flush();

            //ó�� ��� �� ����ڿ��� ���� ��� �� ������� ���̵� �Ѱ���
            for (int name_num = 0; name_num < 3; name_num++) {
                if (name_array[name_num] != null) {
                    os.write("������� " + name_array[name_num] +" " + name_num+" "+"\r\n");
                    os.flush();
                }
            }

            // ����� �̸� �Է��� ����� �̿��� ��� ����ڿ��� ���ο� ������� ������ �˸�
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.write("** " + name + " ���� �濡 ���Խ��ϴ�. " + "\r\n");
                    threads[i].os.flush();
//               for (int name_num = 0; name_num < 3; name_num++) {
//                  if (name_array[name_num] != null) {
//                     os.write("������� " + name_array[name_num] +" " + name_num+" "+"\r\n");
//                     os.flush();
//                  }
//               }
                }
            }
            while (true) {
                String line = is.readLine();
                System.out.println(line);
                String Point_Server;

                // /��ǥ�� �޴� �κ�
                if (line.startsWith("/Point")) {
                    // ��ǥ�� �޾Ƽ� Ŭ���̾�Ʈ�� �Ѱ���
                    Point_Server = line.substring(6);
                    os.write("/Point" + Point_Server + "\r\n");
                    os.flush();

                }

                // /quit �� �Է��� ������� ��ȭâ �ߴ�
                if (line.startsWith("/quit")) {
                    // ������ ����ڿ��� ���� �޼���
                    os.write("���� ����" + "\r\n");
                    os.flush();
                    break;
                }

                // ����ڵ鿡�� �޼��� �Ѹ���
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

            // �� ����ڰ� ������ �ٸ� ����ڵ鿡�� ���� ����ڸ� �˷���
            // ����ڰ� �Ѹ��̶� ������ ���� ����
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.write(name + "���� ������ �����ϼ̽��ϴ�." + "\r\n");
                    threads[i].os.flush();
                    for (int name_num = 0; name_num < 3; name_num++) {
                        if (name_array[name_num].equals(name)) {
                            name_array[name_num] = null;
                        }

                    }
                    JOptionPane.showMessageDialog(null, "����1 ���ھ�" + "\n" + "����2 ���ھ�" + "\n" + "����3 ���ھ�", "�л� ����",
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

            // output stream, close the input stream, socket �ݱ�
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}