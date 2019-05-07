package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;
import java.util.Random;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import jade.core.behaviours.SimpleBehaviour;

public class RandomWalkTanker extends SimpleBehaviour{
	
	private static final long serialVersionUID = 9088209402507795289L;
	
	private boolean finished = false;
	private int exitValue;
	private List<String> list_nodes;

	public RandomWalkTanker (final AbstractDedaleAgent myagent) {
		super(myagent);
		this.list_nodes = ((AgentInterface) this.myAgent).getListNodes();
	}

	@Override
	public void action() {
		exitValue = 1;
		this.list_nodes = ((AgentInterface) this.myAgent).getListNodes();
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=null){
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
			Random r= new Random();
			int moveId=1+r.nextInt(lobs.size()-1);
			
			while(!this.list_nodes.contains(lobs.get(moveId).getLeft()))
			{

				moveId=1+r.nextInt(lobs.size()-1);
			}
			((AbstractDedaleAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
		}

		finished = true;

	}
	public int onEnd(){return this.exitValue;}
	@Override
	public boolean done() {
		return finished;
		}

}