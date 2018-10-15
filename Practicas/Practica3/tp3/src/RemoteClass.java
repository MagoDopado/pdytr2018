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
    public String sendThisBack(String data) throws RemoteException
    {
        System.out.println(data);
        return data;
    }
}