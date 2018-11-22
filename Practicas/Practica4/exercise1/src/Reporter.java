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
			// This should allow changes on the container list.
			// But doesnt work becuase the subscription breaks.
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

	private class Containers extends Agent
	{
		public static Containers Instance;
		public AMSSubscriber subscriberBehaviour;
		public ArrayList<ContainerID> available = new ArrayList<ContainerID>();

		public static ContainerID mainContainer()
		{
			return new ContainerID("Main-Container", null);
		}

		@Override
		protected void setup()
		{
			Instance = this;
			subscribeToContainerChanges();
		}

		private void subscribeToContainerChanges()
		{
			/*
			getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
			getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
			getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL1);
			getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL2);
			getContentManager().registerOntology(JADEManagementOntology.getInstance());
			getContentManager().registerOntology(jade.domain.introspection.IntrospectionOntology.getInstance());
			*/
			subscriberBehaviour = (new AMSSubscriber()
			{
				protected void installHandlers(Map handlers)
				{
					// Handle container addition.
					handlers.put(IntrospectionVocabulary.ADDEDCONTAINER, (new EventHandler()
					{
						public void handle(Event event)
						{
							AddedContainer addedContainer = (AddedContainer) event;
							available.add(addedContainer.getContainer());
							print(addedContainer.getContainer().toString() + " was added to the platform.");
						}
					}));

					// Handle container remotion.
					handlers.put(IntrospectionVocabulary.REMOVEDCONTAINER, (new EventHandler()
					{
						public void handle(Event event)
						{
							RemovedContainer removedContainer = (RemovedContainer) event;
							ArrayList<ContainerID> temp = new ArrayList<ContainerID>(available);
							for(ContainerID container : temp)
							{
								print(container.toString() + " was removed from the platform.");
								available.remove(container);
							}
						}
					}));
				}
			});
			addBehaviour(subscriberBehaviour);
		}
	}
}
