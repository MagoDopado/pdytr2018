import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.Map;

import static java.lang.System.exit;

public class ClientCopier {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 2) {
            System.out.println("usage: server_host file");
            exit(1);
        }
        String host = args[0];
        String filename = args[1];

        String remoteName = "//" + host + ":" + Registry.REGISTRY_PORT + "/remote";

        try {
            IRemoteServer server = (IRemoteServer) Naming.lookup(remoteName);
            readFromServer(server, filename);
            writeToServer(server, "copy_" + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readFromServer(IRemoteServer server, String filename) {

        System.out.println("[CLIENT][READ-FROM-SERVER] START - filename: " + filename);

        try (RandomAccessFile file = new RandomAccessFile("copy_" + filename, "rw")) {
            Map.Entry<byte[], Integer> readPair;
            int pos = 0;

            readPair = server.read(filename, pos, BUFFER_SIZE);
            file.write(readPair.getKey(), (int) file.getChannel().size(), readPair.getValue());

            while (readPair.getValue() == BUFFER_SIZE) {
                pos += readPair.getValue();
                readPair = server.read(filename, pos, BUFFER_SIZE);
                file.write(readPair.getKey(), 0, readPair.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("[CLIENT][READ-FROM-SERVER] END");
    }

    private static void writeToServer(IRemoteServer server, String filename) {
        try (FileInputStream file = new FileInputStream(filename)) {
            byte[] data = new byte[BUFFER_SIZE];

            int readBytes = file.read(data);
            server.write(filename, readBytes, data);
            while (readBytes == BUFFER_SIZE) {
                readBytes = file.read(data);
                server.write(filename, readBytes, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
