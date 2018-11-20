import java.rmi.RemoteException;
import java.util.AbstractMap;

public interface IRemoteServer extends java.rmi.Remote {

    AbstractMap.SimpleEntry<byte[], Integer> read(String filename, int pos, int amount) throws RemoteException;

    int write(String filename, int amount, byte[] data) throws RemoteException;
}
