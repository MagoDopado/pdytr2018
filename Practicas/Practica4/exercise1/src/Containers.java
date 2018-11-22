import java.util.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.introspection.*;
import jade.domain.JADEAgentManagement.*;

public class Containers extends Agent
{
	public static Containers Instance = null;
	public AMSSubscriber subscriberBehaviour;
	public ArrayList<ContainerID> available = new ArrayList<ContainerID>();

	public static ContainerID mainContainer()
	{
		return new ContainerID("Main-Container", null);
	}

	private long t0 = 0;
	private long time()
	{
		return System.currentTimeMillis() - t0;
	}
	private void print(String s)
	{
		System.out.println(time() + ": " + s);
	}

	@Override
	protected void setup()
	{
		Instance = this;
		subscribeToContainerChanges();
		t0 = System.currentTimeMillis();
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
							available.remove(removedContainer);
						}
					}
				}));
			}
		});
		addBehaviour(subscriberBehaviour);
	}
}
