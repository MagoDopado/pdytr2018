import java.rmi.Naming;
import java.rmi.registry.Registry;

public class Client
{
	private static final String Usage = "Usage: Client <server> <message>";

	private static void validateArguments(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println(Usage);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		validateArguments(args);
		try
		{
			String remoteName = args[0];
			String message = args[1];
			String registry = "//" + remoteName + ":" + Registry.REGISTRY_PORT + "/remote";
			RemoteClassInterface remote = (RemoteClassInterface) Naming.lookup(registry);
			System.out.println("Server returned: " + remote.waitAndReturn(message));
		} catch (Exception e) { e.printStackTrace(); }
	}
}
