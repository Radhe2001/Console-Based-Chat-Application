import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

class ClientReader extends Thread {
    Socket s;
    static String msg = "";

    ClientReader(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream din = new DataInputStream(s.getInputStream());
            String str1 = "";
            while (!str1.equals("Close")) {
                str1 = din.readUTF();
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

class ClientWriter extends Thread {
    Socket s;

    ClientWriter(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            String str2 = "";
            String msg = ClientReader.message();
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

public class client {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("192.168.228.225", 6666);
        ServerSocket ss = new ServerSocket(6667);
        Socket ns = ss.accept();

        ClientReader cr = new ClientReader(ns);
        ClientWriter cw = new ClientWriter(s);

        cr.start();
        cw.start();
        ss.close();
    }
}