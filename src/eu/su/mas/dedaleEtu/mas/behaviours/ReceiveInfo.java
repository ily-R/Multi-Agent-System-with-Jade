package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class ReceiveInfo extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622078551998L;

	private boolean finished = false;
	private int exitValue;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String, List<Couple<String, Integer>>>> list_agent;
	private MapRepresentation mapReceiver;
	private Map<String[], LinkedList<String>> sgSender ;
	private String[] listePrio = {"Explo1","Explo2","Collect1","Collect2","Collect3"};
	
	public ReceiveInfo (final Agent myagent ) {
		super(myagent);

	}

	@Override
	public void action() {
		
		exitValue = 1; 
		this.ListeTresor = ((AgentInterface) this.myAgent).getListTresor();
		this.list_agent = ((AgentInterface) this.myAgent).getListAgentCap();

		//------------------------------------------------------------- receive map	
		
		final MessageTemplate msgTemplate_map = MessageTemplate.and(
				MessageTemplate.MatchProtocol("map_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_map = this.myAgent.receive(msgTemplate_map);
		
		
		
		if (msg_map != null) {	
			
			try {
				this.sgSender = (Map<String[], LinkedList<String>>) msg_map.getContentObject();
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

			for (String key[] : this.sgSender.keySet()) {
				if(key[1]!=null) {
					switch(key[1]){
					case "DIAMOND":
						this.mapReceiver.addNode(key[0],MapAttribute.DIAMOND);
						break;
					case "GOLD":
						this.mapReceiver.addNode(key[0],MapAttribute.GOLD);
						break;
					case "open":
						if(this.mapReceiver.getGraph().getNode(this.sgSender.get(key).get(0))==null) {
							this.mapReceiver.addNode(key[0],MapAttribute.open);
							}
						break;
							
						}
						
					
						
				}else {
					this.mapReceiver.addNode(key[0]);
				}
				
				for (int i = 0; i < this.sgSender.get(key).size(); i++) {
					if(this.mapReceiver.getGraph().getNode(this.sgSender.get(key).get(i))==null) {
						this.mapReceiver.addNode(this.sgSender.get(key).get(i));										
					}	
					this.mapReceiver.addEdge(key[0],this.sgSender.get(key).get(i));
				}
			}
			((AgentInterface) this.myAgent).setMap(this.mapReceiver);


		}
		
		
		//-------------------------------------------------------------receive List treasure		
		
		final MessageTemplate msgTemplate_tresorList = MessageTemplate.and(
				MessageTemplate.MatchProtocol("liste_final_tresor_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_tresorList = this.myAgent.receive(msgTemplate_tresorList);
		
		if(msg_tresorList != null) {

			if(this.list_agent.size() == 0) {
			try {
				this.ListeTresor =  (List<Couple<String,List<Couple<Observation,Integer>>>>) msg_tresorList.getContentObject();
				((AgentInterface) this.myAgent).setListTresor(this.ListeTresor);

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}}
		
		//------------------------------------------------------------- List Capacities 		

		final MessageTemplate msgTemplate_agentCapabilities = MessageTemplate.and(
				MessageTemplate.MatchProtocol("liste_cap_agent_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_agentCapabilities = this.myAgent.receive(msgTemplate_agentCapabilities);
			

		
		if (msg_agentCapabilities != null) {
			
			try {
				List<Couple<String, Integer>> senderCapabilities = (List<Couple<String, Integer>>) msg_agentCapabilities.getContentObject();
				this.list_agent.add(new Couple<>(msg_agentCapabilities.getSender().getLocalName(),senderCapabilities ));

				((AgentInterface) this.myAgent).setListAgentCap(this.list_agent);

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			ACLMessage msg_confirmation=new ACLMessage(ACLMessage.INFORM);
			msg_confirmation.setSender(this.myAgent.getAID());
			msg_confirmation.setProtocol("confirmation_protocol");
			msg_confirmation.addReceiver(new AID(msg_agentCapabilities.getSender().getLocalName(),AID.ISLOCALNAME));
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_confirmation);

		}
		
		
		//------------------------------------------------------------- update Treasure 
		
		
		final MessageTemplate msgTemplate_MissionOutcome = MessageTemplate.and(
				MessageTemplate.MatchProtocol("Tresor_update"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_MissionOutcome = this.myAgent.receive(msgTemplate_MissionOutcome);
		
		if(msg_MissionOutcome != null) {
				
			try {
				Couple<String,List<Couple<Observation,Integer>>> updated_tresor =  (Couple<String,List<Couple<Observation,Integer>>>) msg_tresorList.getContentObject();
				int goldquantity = 1;
				int diamondquantity = 1;
				for(Couple<Observation,Integer> o : updated_tresor.getRight())
				{
					switch(o.getLeft()) {
					case GOLD:
						goldquantity = o.getRight();
						break;
					case DIAMOND:
						diamondquantity = o.getRight();
						break;
					default:
						break;
					}
				}
				
				Iterator<Couple<String,List<Couple<Observation,Integer>>>> iter = this.ListeTresor.iterator();
				while(iter.hasNext())
				{
					Couple<String, List<Couple<Observation, Integer>>> old_tresor =iter.next();
					if(old_tresor.getLeft().equals(updated_tresor.getLeft()))
					{
						iter.remove();
						if(goldquantity > 0 || diamondquantity > 0)
						{
							this.ListeTresor.add(updated_tresor);
							break;
						}
						break;
					}
				}
				
				((AgentInterface) this.myAgent).setListTresor(this.ListeTresor);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		if(((AgentInterface) this.myAgent).getListAgentCap().size() > 0 && ((AgentInterface) this.myAgent).getListTresor().size() > 0 ){
			exitValue = 2;
			finished = true;
		}
		
		if(((AgentInterface) this.myAgent).getListTresor().size() == 0 && ((AgentInterface) this.myAgent).getListAgentCap().size() > 0 )
		{
			ACLMessage msg_finish=new ACLMessage(ACLMessage.INFORM);
			msg_finish.setSender(this.myAgent.getAID());
			msg_finish.setProtocol("finish_protocol");
			for(String agent: listePrio)
			{
				msg_finish.addReceiver(new AID(agent,AID.ISLOCALNAME));
			}
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_finish);
			exitValue = 3;
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
	