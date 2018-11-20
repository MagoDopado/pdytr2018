import java.rmi.Naming;
import java.rmi.registry.Registry;

public class StartServer {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            String remoteName = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(remoteName, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
