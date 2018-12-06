import java.io.*;
import java.nio.file.*;
import java.util.*;
import jade.core.*;
import jade.core.behaviours.*;
import static java.lang.System.exit;

public class RWAgent extends Agent {
	private long t0 = 0;
	private long time()
	{
		return System.currentTimeMillis() - t0;
	}

	private void print(String s)
	{
		System.out.println(time() + ": " + s);
	}

	public void stop()
	{
		print("Agent stopped.");
		rwBehaviour.done = true;
	}

	public class RWBehaviour extends SequentialBehaviour
	{
	 RWAgent rwAgent;
	 public boolean done = false;
	 public RWBehaviour(Agent agent)
	 {
		 super(agent);
	 }
	 public void onStart()
	 {
		 rwAgent = (RWAgent)getAgent();
	 }
	 public int onEnd()
	 {
		 if (!done) {
			 reset();
			 myAgent.addBehaviour(this);
		 }
		 return super.onEnd();
	 }
	}

	public OneShotBehaviour readSubB(RWAgent agent)
	{
		return (new OneShotBehaviour(agent)
		{
			RWAgent rwAgent;
			public void onStart()
			{
				rwAgent = (RWAgent)getAgent();
			}
			public void action()
			{
				if (!rwAgent.onRead()) {
					System.out.println(">> 1UP <<");
					rwAgent.stop();
				}
			}
		});
	}

	public OneShotBehaviour writeSubB(RWAgent agent)
	{
		return (new OneShotBehaviour(agent)
		{
			RWAgent rwAgent;
			public void onStart()
			{
				rwAgent = (RWAgent)getAgent();
			}
			public void action()
			{
				if (!rwAgent.onWrite()) {
					rwAgent.stop();
				}
			}
		});
	}

	public OneShotBehaviour moveSubB(RWAgent agent, final Location container)
	{
		return (new OneShotBehaviour(agent)
		{
			RWAgent rwAgent;
			public void onStart()
			{
				rwAgent = (RWAgent)getAgent();
			}
			public void action()
			{
				//print(Utils.s("Moving to", container));
				rwAgent.doMove(container);
			}
		});
	}

	private RWBehaviour readAndWrite(RWAgent agent, boolean reader, ContainerID targetContainer)
	{
		RWBehaviour rw = new RWBehaviour(agent);
		if (reader) {
			rw.addSubBehaviour(readSubB(agent));
			rw.addSubBehaviour(moveSubB(agent, targetContainer));
			rw.addSubBehaviour(writeSubB(agent));
			rw.addSubBehaviour(moveSubB(agent, here()));
		}
		else
		{
			rw.addSubBehaviour(moveSubB(agent, targetContainer));
			rw.addSubBehaviour(readSubB(agent));
			rw.addSubBehaviour(moveSubB(agent, here()));
			rw.addSubBehaviour(writeSubB(agent));
		}
		return rw;
	}

	private WakerBehaviour waitAndRun(RWAgent agent, final boolean reader, final ContainerID targetContainer)
	{
		return (new WakerBehaviour(agent, 1000)
		{
			RWAgent rwAgent;
			public void onStart()
			{
				rwAgent = (RWAgent) getAgent();
			}
			protected void handleElapsedTimeout()
			{
				print("RWAgent started to run.");
				rwAgent.rwBehaviour = readAndWrite(rwAgent, reader, targetContainer);
				addBehaviour(rwAgent.rwBehaviour);
			}
		});
	}

	private static final String help = "Usage\n\tjava -cp ../lib/*:target jade.Boot -agents \"rwAgent:RWAgent(boolean reader, String filePath, String container);containers:Containers\"";
	public RWBehaviour rwBehaviour;
	boolean reader;
	String filePath;
	String serverFileName = null;
	ContainerID containerId;

	static final int BUFFERSIZE = 4096;
	byte[] buffer = new byte[BUFFERSIZE];
	int currentOffset = 0;
	int currentRead = 0;

	private void argumentValidation()
	{
		Object[] args = getArguments();
		if (args == null || args.length < 3) {
			System.out.println(RWAgent.help);
			stop();
		}

		reader = Boolean.parseBoolean(args[0].toString());
		filePath = (String)args[1];
		String containerName = (String)args[2];
		containerId = new ContainerID(containerName, null);
		if (containerId == null) {
			System.out.println(Utils.s("Container: ", containerName, " cannot be created."));
			stop();
		}
		/*
		if (!Containers.Instance.available.contains(containerId)) {
			System.out.println("Container: " + containerName + " is not running.");
			stop();
		}
		*/
	}

	@Override
	protected void setup()
	{
		argumentValidation();
		addBehaviour(waitAndRun(this, reader, containerId));
		t0 = System.currentTimeMillis();
	}

	public boolean onRead()
	{
		try (RandomAccessFile file = new RandomAccessFile(filePath, "r"))
		{
			file.seek(currentOffset);
			currentRead = file.read(buffer, 0, BUFFERSIZE);
			if (currentRead <= 0) {
				return false;
			}
			currentOffset += currentRead;
			System.out.print("\\");
			//print(Utils.s("Read ", currentRead));
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public boolean onWrite()
	{
		if (serverFileName == null) {
			Path path = Paths.get(filePath);
			String fileName = path.getFileName().toString();
			String folder = (reader)? "server/" : "client/";
			serverFileName = Utils.s(folder, System.currentTimeMillis(), fileName);
		}
		try (FileOutputStream file = new FileOutputStream(serverFileName, true))
		{
			if (currentRead <= 0) {
				return false;
			}
			file.write(buffer, 0, currentRead);
			//print(Utils.s("Wrote ", currentRead));
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}


}
