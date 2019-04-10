package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Node;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * This example behaviour try to send a hello message (every 3s maximum) to agents Collect2 Collect1
 * @author hc
 *
 */
public class ReceiveMap extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078551998L;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public Map<String[], LinkedList<String>> sgSender ;
	public  MapRepresentation mapReceiver;
	private boolean finished = false;
	private int exitValue = 1 ;


	public ReceiveMap (final Agent myagent , MapRepresentation myMap) {
		super(myagent);
		this.mapReceiver = myMap;
		//super(myagent);
	}

	@Override
	public void action() {
		final MessageTemplate msgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol("UselessProtocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		

		final ACLMessage msg = this.myAgent.receive(msgTemplate);

		if (msg != null) {	
			
			System.out.println(this.myAgent.getLocalName()+" receives from --------------------------------- "+msg.getSender().getLocalName());
			try {
				this.sgSender = (Map<String[], LinkedList<String>>) msg.getContentObject();
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String x = msg.getContentObject(); 
			
			//TODO
			for (String key[] : this.sgSender.keySet()) {
				if(key[1]!=null && key[1].equals("open")) {
					if(this.mapReceiver.getGraph().getNode(key[0])==null) {
						this.mapReceiver.addNode(key[0],MapAttribute.open);
					
					}
				}else {
					this.mapReceiver.addNode(key[0]);
				}
				
				for (int i = 0; i < this.sgSender.get(key).size(); i++) {
					if(this.mapReceiver.getGraph().getNode(this.sgSender.get(key).get(i))==null) {
						this.mapReceiver.addNode(this.sgSender.get(key).get(i),MapAttribute.open);
					}	
					this.mapReceiver.addEdge(key[0],this.sgSender.get(key).get(i));
				}
			}

		}	
		
		finished = true;
		
	}
	public int onEnd(){return exitValue;}


	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}
}
	