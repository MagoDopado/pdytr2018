import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap;

public class Server extends UnicastRemoteObject implements IRemoteServer {
    protected Server() throws RemoteException {
        super();
    }

    public AbstractMap.SimpleEntry<byte[], Integer> read(String filename, int position, int amount) {

        System.out.println("[SERVER][READ] START - filename: " + filename + " position: " + position + " amount: " + amount);

        byte[] data = new byte[amount];
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {

            int toRead = file.getChannel().size() - position < 1024 ? (int) file.getChannel().size() - position : amount;

            file.seek(position);
            Integer readBytes = file.read(data, 0, toRead);

            System.out.println("[SERVER][READ] END");
            return new AbstractMap.SimpleEntry<>(data, readBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AbstractMap.SimpleEntry<>(data,0);
    }

    public int write(String filename, int amount, byte[] data) {

        System.out.println("[SERVER][WRITE] START - Received write request - filename: " + filename + " amount: " + amount);
        String newFileName = "server_" + filename;

        try (FileOutputStream file = new FileOutputStream(newFileName, true)) {

            long oldSize = file.getChannel().size();

            file.write(data, 0, amount);

            long currentSize = file.getChannel().size();

            System.out.println("[SERVER][WRITE] END - new file: " + newFileName);
            return (int) (currentSize - oldSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
