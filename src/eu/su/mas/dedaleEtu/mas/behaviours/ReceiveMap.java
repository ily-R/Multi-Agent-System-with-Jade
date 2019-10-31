package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
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
public class ReceiveMap extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078551998L;

	private Map<String[], LinkedList<String>> sgSender ;
	private  MapRepresentation mapReceiver;
	private boolean finished = false;
	private int exitValue;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor_received;
	private String tankerPosition;

	public ReceiveMap (final Agent myagent) {
		super(myagent);

		this.ListeTresor_received = new ArrayList<Couple<String,List<Couple<Observation,Integer>>>>();
		this.tankerPosition = new String();
		
	}

	@Override
	public void action() {
		
		exitValue = 1 ;
		this.mapReceiver = ((AgentInterface) this.myAgent).getMap();
		this.ListeTresor = ((AgentInterface) this.myAgent).getListTresor();
		final MessageTemplate msgTemplate_map = MessageTemplate.and(
				MessageTemplate.MatchProtocol("map_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_map = this.myAgent.receive(msgTemplate_map);
		
		
		final MessageTemplate msgTemplate_tresorList = MessageTemplate.and(
				MessageTemplate.MatchProtocol("liste_tresor_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_tresorList = this.myAgent.receive(msgTemplate_tresorList);
		

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
		if(msg_tresorList != null)
		{
			try {
				this.ListeTresor_received =  (List<Couple<String, List<Couple<Observation, Integer>>>>) msg_tresorList.getContentObject();
				
				for (Couple<String, List<Couple<Observation, Integer>>> tresor_node_received : ListeTresor_received )
				{
					boolean flag = false;
					for (Couple<String, List<Couple<Observation, Integer>>> tresor_node : ListeTresor )
					{
						if (tresor_node_received.getLeft().equals(tresor_node.getLeft()))
							flag = true;
					}
					if(!flag)
						this.ListeTresor.add(tresor_node_received);
				}
				((AgentInterface) this.myAgent).setListTresor(this.ListeTresor);

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}
		
		final MessageTemplate msgTemplate_tankerPosition = MessageTemplate.and(
				MessageTemplate.MatchProtocol("tanker_position_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_tankerPosition = this.myAgent.receive(msgTemplate_tankerPosition);
		
		if (msg_tankerPosition != null) {
			this.tankerPosition = msg_tankerPosition.getContent();
			boolean flag = true;
			  try
			   {
			      Integer.parseInt( this.tankerPosition );
			   }
			   catch(NumberFormatException e )
			   {
				   flag = false ;			  
				   }
			if(flag)
				((AgentInterface) this.myAgent).setTankerPosition(this.tankerPosition);

		}
		
		finished = true;
		
	}
	public int onEnd(){
		return exitValue;}


	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}
}
	