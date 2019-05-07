package eu.su.mas.dedaleEtu.mas.behaviours;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class Migration extends SimpleBehaviour {

	private static final long serialVersionUID = -7687876987L;
	private boolean finished = false;
	
	public Migration(final Agent myAgent) {
		super(myAgent);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ContainerID cID = new ContainerID();
		cID.setName("PPTI-14-408-11_container2");
		cID.setPort("8888");
		cID.setAddress("PPTI-14-408-11"); // IP of the host of the targeted container
		this.myAgent.doMove(cID);
		System.out.println("send");
		
		finished = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}
	

}
