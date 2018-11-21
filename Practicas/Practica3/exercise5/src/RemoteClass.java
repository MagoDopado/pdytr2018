import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteClass extends UnicastRemoteObject implements RemoteClassInterface
{
	protected RemoteClass() throws RemoteException
	{
		super();
	}

	public String waitAndReturn(String message) throws RemoteException
	{
		int sleepTime = 3000;
		System.out.println("Received " + message);
		for (int i = 1; i <= 5; i++)
		{
				try
				{
					Thread.sleep(sleepTime);
					System.out.println("Stalled " + message + " for " + sleepTime + "ms.");
				}
				catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
		}
		System.out.println("Returning " + message);
		return message;
	}
}
