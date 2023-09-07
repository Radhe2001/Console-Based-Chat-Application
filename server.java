import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

class ServerReader extends Thread {
    Socket s;
    static String msg = "";

    ServerReader(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream din = new DataInputStream(s.getInputStream());
            String str1 = "";
            while (!str1.equals("Close")) {
                str1 = din.readUTF();
                msg = str1;
                System.out.println("Client : " + str1);
            }
            din.close();

            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String message() {
        return msg;
    }
}

class ServerWriter extends Thread {
    Socket s;

    ServerWriter(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            String str2 = "";
            String msg = ServerReader.message();
            while (!msg.equals("Close") && s.isConnected()) {
                str2 = br.readLine();
                dout.writeUTF(str2);
                dout.flush();
            }
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

public class server {
    String msg = "";

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(6666);
        Socket s = ss.accept();
        Socket ns = new Socket("192.168.134.237", 6667);

        ServerReader sr = new ServerReader(s);
        ServerWriter sw = new ServerWriter(ns);

        sr.start();
        sw.start();

        ss.close();
    }
}