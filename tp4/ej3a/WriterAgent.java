import jade.core.Agent;
import jade.core.ContainerID;
import jade.lang.acl.ACLMessage;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class WriterAgent extends Agent {
    private ContainerID server;
    private ContainerID client;
    private byte[] bytes;
    private int amountToWrite, amountWritten;
    private String filename;
    int pos;
    private boolean done, wasAtServer;
    @Override
    protected void setup() {
        ACLMessage msg = null;
        while (msg == null) {
            msg = receive();
        }

        client = new ContainerID("Main-Container", null);
        server = new ContainerID("Container-1", null);
        bytes = new byte[1024];

        filename = msg.getContent();
        System.out.println("llegó un msg: " + filename);

        try {
            writeFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String filename) throws Exception {
        this.filename = filename;
        pos = 0;
        done = false;
        client();
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
        RandomAccessFile clientFile = new RandomAccessFile(filename, "r");
        long left = clientFile.getChannel().size() - clientFile.getChannel().position();
        int amountToRead = left < 1024 ? (int) left : 1024;
        clientFile.seek(pos);
        amountToWrite = clientFile.read(bytes, 0, amountToRead);
        pos += amountToWrite;
        clientFile.close();
        wasAtServer = false;
        if (!done) {
            doMove(server);
        }
    }

    private void server() throws Exception {
        FileOutputStream serverFile = new FileOutputStream("server_" + filename, true);
        long oldSize = serverFile.getChannel().size();
        serverFile.write(bytes, 0, amountToWrite);
        long currentSize = serverFile.getChannel().size();
        amountWritten = (int) (currentSize - oldSize);
        serverFile.close();

        if (amountToWrite < 1024) {
            done = true;
        }
        wasAtServer = true;
        doMove(client);
    }
}

