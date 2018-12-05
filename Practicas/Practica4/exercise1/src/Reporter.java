import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import jade.content.lang.sl.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.introspection.*;
import jade.domain.JADEAgentManagement.*;

public class Reporter extends Agent {
	private long t0 = 0;
	private long time()
	{
		return System.currentTimeMillis() - t0;
	}

	private void print(String s)
	{
		System.out.println(time() + ": " + s);
	}

	private CyclicBehaviour onTick(Reporter agent)
	{
		return (new CyclicBehaviour(agent){
			Reporter reporter;
			Queue<ContainerID> currentContainers;
			ArrayList<String> accumultatedInfo = new ArrayList<>();
			private Queue<ContainerID> getContainers()
			{
				LinkedList<ContainerID> list = new LinkedList<ContainerID>(Containers.Instance.available);
				list.remove(Containers.mainContainer());
				list.addLast(Containers.mainContainer());
				return list;
			}
			private String getData()
			{
				OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
				java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
				runtime.gc();
				String host = null;
				try {
					host = InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				return "Computer name: " + host + " | Free memory: " + runtime.freeMemory() + "B | Processing load: " + osBean.getSystemLoadAverage();
			}
			public void onStart()
			{
				reporter = (Reporter) myAgent;
			}
			public void action()
			{
				if (here().equals(Containers.mainContainer())) {
					currentContainers = getContainers();
					//Print accumulated info.
					print("back at main.");
					for (String data : accumultatedInfo ) {
						print(data);
					}
					accumultatedInfo = new ArrayList<>();
				}
				// Get current container information and save into agent.
				accumultatedInfo.add(getData());
				print(getLocalName());
				//Move to another container.
				try { Thread.sleep(1000); } catch(InterruptedException ex) { }
				doMove(currentContainers.remove());
			}
		});
	}

	private WakerBehaviour onWake(Reporter agent)
	{
		return (new WakerBehaviour(agent, 10000)
		{
			Reporter reporter;
			public void onStart()
			{
				reporter = (Reporter) myAgent;
			}
			//agent = myAgent
			protected void handleElapsedTimeout()
			{
				print("Reporter started to run.");
				addBehaviour(onTick(reporter));
			}
		});
	}

	@Override
	protected void setup()
	{
		// Wait for containers to load.
		addBehaviour(onWake(this));
		t0 = System.currentTimeMillis();
	}
}
