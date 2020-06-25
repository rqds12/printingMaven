import java.net.*;
import java.io.*;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    boolean checkRemoteFile (String string) throws IOException {
        out.writeUTF("c " + string);
        boolean fileExist = input.readBoolean();
        if(fileExist){
            return true;
        }else{
            return false;
        }
    }
    boolean print(String string) throws IOException{
        out.writeUTF("p " + string);
        return true;
    }
}
