/*
 * IfaceRemoteClass.java
 * Interface defining only one method which can be invoked remotely
 *
 */
/* Needed for defining remote method/s */
import java.rmi.Remote;
import java.rmi.RemoteException;
/* This interface will need an implementing class */
public interface IfaceRemote extends Remote
{
    /* It will be possible to invoke this method from an application in other JVM */
    //Read.
    public int getSize(String name) throws RemoteException;
    public char[] readBuffer(String name, int offset, int size) throws RemoteException;

    //Write
    public int writeBuffer(String name, int size, char[] buffer) throws RemoteException;
}
