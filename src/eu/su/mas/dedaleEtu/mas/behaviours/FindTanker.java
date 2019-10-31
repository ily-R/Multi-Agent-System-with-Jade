package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class FindTanker extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private MapRepresentation myMap;
	private List<Couple<String, Integer>> capabilities;
	private int exitValue ;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private String posTanker;

	public FindTanker (final AbstractDedaleAgent myagent) {
		
		super(myagent);

	}
	

	@Override
	public void action() {

		exitValue = 1;
		this.myMap = ((AgentInterface) this.myAgent).getMap();
		this.ListeTresor = ((AgentInterface) this.myAgent).getListTresor();
		this.posTanker = ((AgentInterface) this.myAgent).getTankerPosition();
		this.capabilities =  ((AgentInterface) this.myAgent).getCapacities();
		
		ACLMessage msg_tresorList=new ACLMessage(ACLMessage.INFORM);
		msg_tresorList.setSender(this.myAgent.getAID());
		msg_tresorList.setProtocol("liste_final_tresor_protocol");
		
		ACLMessage msg_capabilities=new ACLMessage(ACLMessage.INFORM);
		msg_capabilities.setSender(this.myAgent.getAID());
		msg_capabilities.setProtocol("liste_cap_agent_protocol");
		
		if(this.ListeTresor != null) {
		try {
			msg_tresorList.setContentObject((Serializable) this.ListeTresor);
			msg_tresorList.addReceiver(new AID("Tanker1",AID.ISLOCALNAME));
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tresorList);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}
		
		
		if(this.capabilities != null) {
		try {
			msg_capabilities.setContentObject((Serializable) this.capabilities);
			msg_capabilities.addReceiver(new AID("Tanker1",AID.ISLOCALNAME));
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_capabilities);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}

		
		final MessageTemplate msgTemplate_confirmation = MessageTemplate.and(
				MessageTemplate.MatchProtocol("confirmation_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_confirmation = this.myAgent.receive(msgTemplate_confirmation);
		
		if (msg_confirmation != null) {
			exitValue = 3 ;
			finished = true;
		}
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		if (myPosition!=null){
			
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String nextNode= this.myMap.getShortestPath(myPosition,  this.posTanker).get(0);	
			if(!((AbstractDedaleAgent)this.myAgent).moveTo(nextNode)) {
					 exitValue = 2;
				     finished = true;
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
