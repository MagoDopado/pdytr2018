import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteClassInterface extends Remote
{
	public String waitAndReturn(String message) throws RemoteException;
}
