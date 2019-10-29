import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class MainAgent extends Agent {
    AID readerAgentID, writerAgentID;

    @Override
    protected void setup() {
        try {
            System.out.println("\n\nEsperando... Tenés 10 segundos para levantar el container del servidor\n\n");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nBasta de esperar\n\n");

        findAgents();

        ACLMessage msg;
        String filename;

        System.out.println("\n\nEsperando operación. Formato: [read | write] [file]\n\n");
        msg = null;
        while (msg == null) {
            msg = receive();
        }
        filename = msg.getContent();

//      indico al writer que envíe el archivo al servidor
        msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent(filename);
        msg.addReceiver(writerAgentID);
        send(msg);

//      espero que termine el writer
        msg = null;
        while (msg == null) {
            msg = receive();
        }

        msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent("server_" + filename);
        msg.addReceiver(readerAgentID);
        send(msg);
    }

    private void findAgents() {
        AMSAgentDescription[] agents = null;

        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults ( new Long(-1) );
            agents = AMSService.search( this, new AMSAgentDescription (), c );
        }
        catch (Exception e) {

        }

        for (int i=0; i<agents.length;i++){
            AID agentID = agents[i].getName();
//            System.out.println(agentID.getLocalName());
            if (agentID.getLocalName().equals("reader")) {
//                System.out.println("reader");
                readerAgentID = agentID;
            } else if (agentID.getLocalName().equals("writer")) {
//                System.out.println("writer");
                writerAgentID = agentID;

            }
        }
    }
}

