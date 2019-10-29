import jade.core.Agent;
import jade.core.ContainerID;
import jade.lang.acl.ACLMessage;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class ReaderAgent extends Agent {
    private ContainerID server;
    private ContainerID client;
    private byte[] bytes;
    private int amountRead;
    int pos;
    private String filename;
    private boolean done;
    private boolean wasAtServer;

    @Override
    protected void setup() {
        ACLMessage msg = null;
        while (msg == null) {
            msg = receive();
        }

        client = new ContainerID("Main-Container", null);
        server = new ContainerID("Container-1", null);
        bytes = new byte[1024];
        amountRead = 0;

        filename = msg.getContent();

        try {
            readFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(String filename) throws Exception {
        this.filename = filename;
        pos = 0;
        wasAtServer = false;
        done = false;
        doMove(server);
    }

    @Override
    protected void afterMove() {
        try {
            if (wasAtServer) {
                client();
            } else {
                server();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void client() throws Exception {
        FileOutputStream clientFile = new FileOutputStream("client_" + filename, true);
//        System.out.println("amountRead: " + amountRead + " | done: " + done);
        clientFile.write(bytes, 0, amountRead);
        clientFile.close();
        wasAtServer = false;
        if (!done) {
            doMove(server);
        }
    }

    private void server() throws Exception {
        RandomAccessFile serverFile = new RandomAccessFile(filename, "r");
        long left = serverFile.getChannel().size() - pos;
        int amountToRead = left < 1024 ? (int) left : 1024;
        serverFile.seek(pos);
        amountRead = serverFile.read(bytes, 0, amountToRead);
        pos += amountRead;
        serverFile.close();
        if (amountRead < 1024) {
            done = true;
        }
//        System.out.println("left: " + amountRead + " | done: " + done);
        wasAtServer = true;
        doMove(client);
    }
}
