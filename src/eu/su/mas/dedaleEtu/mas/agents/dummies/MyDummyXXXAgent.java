package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;

import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalk;
import eu.su.mas.dedaleEtu.mas.behaviours.Migration;
import jade.core.behaviours.Behaviour;



public class MyDummyXXXAgent extends AbstractDedaleAgent{

	private static final long serialVersionUID = -5686331366676803589L;
	


	protected void setup(){
		super.setup();
		
		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		//lb.add();
		addBehaviour(new Migration(this));
		//addBehaviour(new startMyBehaviours(this,lb));
	}


	protected void beforeMove(){
		super.beforeMove();
		System.out.println("I migrate");
	}

	protected void afterMove(){
		super.afterMove();		
		System.out.println("I migrated");
	
	}

}