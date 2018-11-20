import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap;

public class Server extends UnicastRemoteObject implements IRemoteServer {
    protected Server() throws RemoteException {
        super();
    }

    public AbstractMap.SimpleEntry<byte[], Integer> read(String filename, int position, int amount) {

        byte[] data = new byte[amount];
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            int toRead = file.getChannel().size() - position < 1024 ? (int) file.getChannel().size() - position : amount;
            System.out.println(toRead);
            file.seek(position);
            Integer readBytes = file.read(data, 0, toRead);
            file.close();
            return new AbstractMap.SimpleEntry<>(data, readBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AbstractMap.SimpleEntry<>(data,0);
    }

    public int write(String filename, int amount, byte[] data) {
        System.out.println("Received write request");

        try {
            FileOutputStream file = new FileOutputStream("server_" + filename, true);
            long oldSize = file.getChannel().size();
            file.write(data, 0, amount);
            long currentSize = file.getChannel().size();
            file.close();
            return (int) (currentSize - oldSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
