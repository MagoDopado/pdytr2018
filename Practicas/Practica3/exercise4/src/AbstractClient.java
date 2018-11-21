import java.io.FileInputStream;

class AbstractClient {

    static final int BUFFER_SIZE = 1024;

    static void writeToServer(IRemoteServer server, String filename) {

        System.out.println("[CLIENT][WRITE-FROM-SERVER] START - filename: " + filename);
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

        System.out.println("[CLIENT][WRITE-FROM-SERVER] END");
    }
}
