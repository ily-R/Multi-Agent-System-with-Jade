package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class GoToGoalCollect extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private MapRepresentation myMap;
	private List<Couple<String, Integer>> capabilities= null;
	private int exitValue ;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private String obj;

	public GoToGoalCollect (final AbstractDedaleAgent myagent, MapRepresentation myMap, String positionObj) {
		
		super(myagent);
		this.myMap=myMap;
		this.obj = positionObj;
		//this.capabilities.add(new Couple <> ("BackPackFreeSpace",5));
	}

	@Override
	public void action() {


		
		exitValue = 1 ;
		

		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		if (myPosition!=null){
			
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(myPosition.equals(this.obj)) {
				exitValue = 3;
				//on va dans attente des pret 
				finished = true;
			}else {
			System.out.println("____________________PosTanker Null"+ this.obj);
			String nextNode= this.myMap.getShortestPath(myPosition, this.obj).get(0);	
			if(!((AbstractDedaleAgent)this.myAgent).moveTo(nextNode)) {
					 exitValue = 2;
					 //on va dans déblocage
				     finished = true;
				}
			}
			}
				
		finished = true;
			}
	public int onEnd(){return exitValue;}
	

	@Override
	public boolean done() {
		return finished;
	}

}
