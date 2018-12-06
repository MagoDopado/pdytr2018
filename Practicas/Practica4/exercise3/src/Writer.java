import java.io.*;
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

	public void stop()
	{
		doDelete();
		print("Agent stopped.");
	}

	private SequentialBehaviour onTick(Writer agent)
	{
		SequentialBehaviour rw = (new SequentialBehaviour(agent)
		{
			Writer writer;
			int done = 0;
			public void onStart()
			{
				writer = (Writer) getAgent();
			}
			public int onEnd()
			{
				if (done++ == 10) {
					writer.stop();
				}
				else
				{
					reset();
					myAgent.addBehaviour(this);
				}
				return super.onEnd();
			}
		});
		//Read.
		rw.addSubBehaviour(new OneShotBehaviour(agent)
		{
			public void action()
			{
				print("Read");
			}
		});
		// Write
		rw.addSubBehaviour(new OneShotBehaviour(agent)
		{
			public void action()
			{
				print("Write");
			}
		});
		return rw;
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
