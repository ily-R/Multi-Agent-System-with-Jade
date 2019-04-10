package eu.su.mas.dedaleEtu.mas.behaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;

public class test2 extends SimpleBehaviour {
	private boolean finished = false;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		System.out.println("receive");
		finished = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
