import java.io.*;
import java.net.*;
import java.util.*;

class Client {
    public static int inPort = 9999;
    public static String address = "192.168.0.2";


    public static void main(String[] args) {
        try (Socket socket = new Socket(address, inPort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // ChatHandler 의 run() 메서드는 해당 클래스 안에 명시적으로 start() 메서드가 선언되어있지는 않지만
            // implements Runnable 에서 run interface 를 가지기 때문에 해당 클래스가 Thread() 의 인스턴스가 될 수 있다.
            // Thread instance 로 ChatHandler를 넘겨주게되면 ChatHanlder 내의 Run() 이 실행된다.
            // Thread() 의 constructor 는 Runnable 객체를 받는다.
            ChatHandler c = new ChatHandler(socket);
            Thread myThread = new Thread(c);
            myThread.start();
            Scanner sc = new Scanner(System.in);
            String line = null;

            // 여기서 클라이언트의 키보드 입력을 받아 서버로 보낸다.
            while (!"exit".equalsIgnoreCase(line)) {
                line = sc.nextLine();
                out.println("<Client>: " + line);   //클라이언트가 자신의 콘솔에 입력한 내용을 콘솔에 다시 보여준다.
                out.flush();
            }
            sc.close();
        } catch (Exception e) {
        }
    }

}

class ChatHandler implements Runnable {
    private final Socket socket;

    public ChatHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 여기서 서버에서 보내주는 Line 을 받아서 콘솔에 출력해줌
            String line;
            while ((line = in.readLine()) != null) {
//                System.out.println("("+ socket.getInetAddress()+ ") " + line);
                System.out.println(line);   // 서버가 보내주는 내용을 클라이언트 콘솔에 출력
            }
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
