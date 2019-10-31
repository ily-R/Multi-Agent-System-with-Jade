package eu.su.mas.dedaleEtu.mas.behaviours;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
/**
 * 
 * @author ilyas Aroui
 *
 */



public class PickTreasure extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private int exitValue ;
	private String MissionPosition;
	private Couple<String,List<Couple<Observation,Integer>>> TreasureNodeInfo; 
	private String[] listePrio = {"Explo1","Explo2"};
	public PickTreasure (final AbstractDedaleAgent myagent) {	
		super(myagent);

	}
	

	@Override
	public void action() {

		exitValue = 1;
		this.MissionPosition = ((AgentInterface) this.myAgent).getMissionPosition();
		this.TreasureNodeInfo =  new Couple<>(new String(), new ArrayList<>());
		
		// ---------------------------------------- wait
		try {
			this.myAgent.doWait(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		//------------------------------------------ pick treasure
		String myPosition= ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		if(myPosition.equals(this.MissionPosition))
		{
			boolean SuccessfulOpening =  ((AbstractDedaleAgent)this.myAgent).openLock(((AbstractDedaleAgent)this.myAgent).getMyTreasureType());
			int value = ((AbstractDedaleAgent)this.myAgent).pick();
			
			if (value > 0)
			{
			
			//-----------------------------------------------update nodeInfo
				
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			
			while(iter.hasNext()){				
				Couple<String, List<Couple<Observation, Integer>>> node =iter.next();
				if (myPosition.equals(node.getLeft()))
				{
					this.TreasureNodeInfo = node;
					break;
				}
			}
			((AgentInterface) this.myAgent).setTresorNodeInfo(this.TreasureNodeInfo);
			
			//-----------------------------------------------send Done
			
			ACLMessage msg_Done=new ACLMessage(ACLMessage.INFORM);
			msg_Done.setSender(this.myAgent.getAID());
			msg_Done.setProtocol("Done_protocol");
			for(String agent: listePrio)
			{
					msg_Done.addReceiver(new AID(agent,AID.ISLOCALNAME));
			}	
													
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_Done);
			}
			
			exitValue = 2;
			finished = true;
		}
			
					   			
		finished = true;
			}
	public int onEnd(){return exitValue;}
	

	@Override
	public boolean done() {
		return finished;
	}

}
