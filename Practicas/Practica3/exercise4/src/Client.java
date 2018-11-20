import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.AbstractMap;

import static java.lang.System.exit;

public class Client {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("usage: server_host operation<read/write> file");
            exit(1);
        }
        String host = args[0];
        String operation = args[1];
        String filename = args[2];

        String remoteName = "//" + host + ":" + Registry.REGISTRY_PORT + "/remote";
        try {
            IRemoteServer server = (IRemoteServer) Naming.lookup(remoteName);
            switch (operation) {
                case "read": readFromServer(server, filename); break;
                case "write": writeToServer(server, filename); break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readFromServer(IRemoteServer server, String filename) throws Exception {
        RandomAccessFile file = new RandomAccessFile("client_" + filename, "rw");
        AbstractMap.SimpleEntry<byte[], Integer> readPair;
        int pos = 0;

        readPair = server.read(filename, pos, BUFFER_SIZE);
        file.write(readPair.getKey(), (int) file.getChannel().size(), readPair.getValue());
        while (readPair.getValue() == BUFFER_SIZE) {
            pos += readPair.getValue();
            readPair = server.read(filename, pos, BUFFER_SIZE);
            file.write(readPair.getKey(), 0, readPair.getValue());
        }
        file.close();
    }

    private static void writeToServer(IRemoteServer server, String filename) throws Exception {
        FileInputStream file = new FileInputStream(filename);
        byte[] data = new byte[BUFFER_SIZE];

        int readBytes = file.read(data);
        server.write(filename, readBytes, data);
        while (readBytes == BUFFER_SIZE) {
            readBytes = file.read(data);
            server.write(filename, readBytes, data);
        }
        file.close();
    }
}
