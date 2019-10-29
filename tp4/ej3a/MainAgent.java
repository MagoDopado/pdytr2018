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
        String operation, filename;

        while (true) {
            System.out.println("\n\nEsperando operación. Formato: [read | write] [file]\n\n");
            msg = null;
            while (msg == null) {
                msg = receive();
            }
            String[] splitMessage = msg.getContent().split(" ");
            operation = splitMessage[0];
            filename = splitMessage[1];
//            System.out.println(operation);
//            System.out.println(filename);
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(filename);
            switch (operation) {
                case "read":
                    System.out.println(readerAgentID);
                    msg.addReceiver(readerAgentID);
                    break;
                case "write":
                    System.out.println(writerAgentID);
                    msg.addReceiver(writerAgentID);
                    break;
            }
            send(msg);
        }
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
