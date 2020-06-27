import java.net.*;
import java.io.*;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    boolean checkRemoteFile (String file) throws IOException {
        out.writeUTF("c " + file);
        out.flush();
        String fileExist = null;
        fileExist = input.readUTF();
        if(fileExist.equals("true")) {
            out.writeBoolean(true);
        }else{
            out.writeBoolean(false);
        }
        out.flush();
        System.out.println(fileExist);
        return fileExist.equals("true");
    }
    boolean print(String file) throws IOException{
        out.writeUTF("p " + file);
        out.flush();
        return true;
    }
}
