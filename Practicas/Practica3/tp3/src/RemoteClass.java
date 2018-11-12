/*
 * RemoteClass.java
 * Just implements the RemoteMethod interface as an extension to
 * UnicastRemoteObject
 *
 */
/* Needed for implementing remote method/s */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/* This class implements the interface with remote methods */
public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass
{
    protected RemoteClass() throws RemoteException
    {
        super();
    }
    /* Remote method implementation */
    //Read.
    public int getSize(String name) throws RemoteException
    {
      return 1024;
    }

    public char[] readBuffer(String name, int offset, int size) throws RemoteException
    {
      char[] array = new char[1];
      array[0] = 'a';
      return array;
    }

    //Write
    public int writeBuffer(String name, int size, char[] buffer) throws RemoteException
    {
      return 0;
    }
}
