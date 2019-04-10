package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.graphstream.graph.Node;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
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
public class ReceiveBlock extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622075555998L;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	private String envoyeur = "";
	private boolean finished = false;
	private int exitValue = 1 ;
	private String[] listePrio = {"Explo1","Explo2", "Explo3"};
	
	public ReceiveBlock (final Agent myagent) {
		super(myagent);
		
		//super(myagent);
	}

	@Override
	public void action() {
		//on attend de recevoir un message
		
		final MessageTemplate msgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg = this.myAgent.receive(msgTemplate);

		if (msg != null) {	
			
			System.out.println("-----------------------------"+this.myAgent.getLocalName()+" receives blockage from "+msg.getSender().getLocalName());
			try {
				this.envoyeur = msg.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
			if((this.envoyeur).equals("Explo1")) {
				String nextNode=null;

				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				nextNode = lobs.get(posAleat).getLeft();
				System.out.println("*********************On tente le Déblocage!************");
				System.out.println("j'était à la position" + ((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				System.out.println("je bouge en " + nextNode);

			}
			else if((this.envoyeur).equals("Explo2")) {
				String nextNode=null;

				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				nextNode = lobs.get(posAleat).getLeft();
				System.out.println("*********************On tente le Déblocage!************");
				System.out.println("j'était à la position" + ((AbstractDedaleAgent)this.myAgent).getCurrentPosition());
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				System.out.println("je bouge en " + nextNode);

			}
			
			}
		else {
			System.out.println("message vide");

			
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
	