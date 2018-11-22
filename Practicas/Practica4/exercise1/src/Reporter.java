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
			private Queue<ContainerID> getContainers()
			{
				LinkedList<ContainerID> list = new LinkedList<ContainerID>(Containers.Instance.available);
				list.remove(Containers.mainContainer());
				list.addLast(Containers.mainContainer());
				return list;
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
				}
				//Move to another container.
				print(getLocalName());
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
