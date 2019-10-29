import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
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
        } else {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("");
            msg.addReceiver(findAgentMain());
            send(msg);
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

    private AID findAgentMain() {
        AMSAgentDescription[] agents = null;
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults ( new Long(-1) );
            agents = AMSService.search( this, new AMSAgentDescription (), c );
        }
        catch (Exception e) { }

        for (int i=0; i<agents.length;i++){
            AID agentID = agents[i].getName();
            if (agentID.getLocalName().equals("main")) {
                return agentID;
            }
        }

        return null;
    }
}

