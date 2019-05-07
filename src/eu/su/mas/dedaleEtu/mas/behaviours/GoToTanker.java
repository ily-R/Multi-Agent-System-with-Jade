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


public class GoToTanker extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private MapRepresentation myMap;
	private Couple<String,List<Couple<Observation,Integer>>> TreasureNodeInfo; 
	private int exitValue ;
	private List<Couple<String, Integer>> capabilities;
	private String posTanker;
	private String nameAgent;
	public GoToTanker (final AbstractDedaleAgent myagent) {	
		super(myagent);

	}
	

	@Override
	public void action() {

		exitValue = 1;
		this.myMap = ((AgentInterface) this.myAgent).getMap();
		this.posTanker = ((AgentInterface) this.myAgent).getTankerPosition();
		this.TreasureNodeInfo =  ((AgentInterface) this.myAgent).getTresorNodeInfo();
		this.capabilities =  ((AgentInterface) this.myAgent).getCapacities();
		this.nameAgent = ((AbstractDedaleAgent)this.myAgent).getLocalName();
		//------------------------------------------------------Send Capacities
		
		ACLMessage msg_capabilities=new ACLMessage(ACLMessage.INFORM);
		msg_capabilities.setSender(this.myAgent.getAID());
		msg_capabilities.setProtocol("liste_cap_agent_protocol");
		
		
		if(this.capabilities != null) {
		try {
			msg_capabilities.setContentObject((Serializable) this.capabilities);
			msg_capabilities.addReceiver(new AID("Tanker1",AID.ISLOCALNAME));
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_capabilities);

		} catch (IOException e) {
			e.printStackTrace();
		}	
		}
		
		//------------------------------------------------------Receive Confirmation
		
		final MessageTemplate msgTemplate_confirmation = MessageTemplate.and(
				MessageTemplate.MatchProtocol("confirmation_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_confirmation = this.myAgent.receive(msgTemplate_confirmation);
		
		if (msg_confirmation != null) {
			System.out.println("Confirmation received from Tanker to " + ((AbstractDedaleAgent)this.myAgent).getLocalName());
			if(nameAgent.equals("Explo1") || nameAgent.equals("Explo1") )
			{
				exitValue = 3 ;
				finished = true;
			}

		}
		
		//------------------------------------------------------Move to tanker
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();	
		if (myPosition!=null){
			
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String nextNode= this.myMap.getShortestPath(myPosition,  this.posTanker).get(0);
			if((((AbstractDedaleAgent)this.myAgent).emptyMyBackPack("Tanker1")) && (nameAgent.equals("Collect1") || nameAgent.equals("Collect2")|| nameAgent.equals("Collect3")))
					{
				 exitValue = 3;
			     finished = true;
			}
			else if(!((AbstractDedaleAgent)this.myAgent).moveTo(nextNode)) {
					 exitValue = 2;
				     finished = true;
				}
			}
		
		//------------------------------------------------------Send Tresor update
		
		ACLMessage msg_tresorUpdate =new ACLMessage(ACLMessage.INFORM);
		msg_tresorUpdate.setSender(this.myAgent.getAID());
		msg_tresorUpdate.setProtocol("Tresor_update");
		
			
		if(this.TreasureNodeInfo != null) {
		try {
			msg_tresorUpdate.setContentObject((Serializable) this.TreasureNodeInfo);
			msg_tresorUpdate.addReceiver(new AID("Tanker1",AID.ISLOCALNAME));
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tresorUpdate);

		} catch (IOException e) {
			e.printStackTrace();
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
