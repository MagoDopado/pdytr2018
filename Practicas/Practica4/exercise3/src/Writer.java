import jade.core.*;
import jade.core.behaviours.*;

public class Writer extends Agent {
	private long t0 = 0;
	private long time()
	{
		return System.currentTimeMillis() - t0;
	}

	private void print(String s)
	{
		System.out.println(time() + ": " + s);
	}

	private CyclicBehaviour onTick(Writer agent)
	{
		return (new CyclicBehaviour(agent)
		{
			Writer writer;
			int done = 0;
			private void stop()
			{
				writer.doDelete();
			}
			public void onStart()
			{
				writer = (Writer) getAgent();
			}
			public void action()
			{
				SequentialBehaviour rw = new SequentialBehaviour(writer);
				//Read.
				rw.addSubBehaviour(new OneShotBehaviour()
				{
					public void action()
					{
						print("Read");
					}
				});
				// Write
				rw.addSubBehaviour(new OneShotBehaviour()
				{
					public void action()
					{
						print("Write");
					}
				});
				addBehaviour(rw);
				if (done++ == 10) {
					stop();
				}
			}
		});
	}

	private WakerBehaviour onWake(Writer agent)
	{
		return (new WakerBehaviour(agent, 1000)
		{
			Writer writer;
			public void onStart()
			{
				writer = (Writer) getAgent();
			}
			//agent = myAgent
			protected void handleElapsedTimeout()
			{
				print("Writer started to run.");
				addBehaviour(onTick(writer));
			}
		});
	}

	@Override
	protected void setup()
	{
		addBehaviour(onWake(this));
		t0 = System.currentTimeMillis();
	}
}
