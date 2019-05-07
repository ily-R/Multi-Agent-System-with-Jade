package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;
import java.util.Random;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class ReceiveBlock extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622075555998L;

	private String envoyeur = "";
	private boolean finished = false;
	private int exitValue;
	
	public ReceiveBlock (final Agent myagent) {
		super(myagent);
	}
	public void randomWalk(int iterations)
	{
		int i = 0;
		while(i<iterations) {
		List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
		Random rand = new Random(); 
		int posAleat = rand.nextInt(lobs.size());
		String nextNode = lobs.get(posAleat).getLeft();
		((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
		i++;
		}
	}
	@Override
	public void action() {
		exitValue = 1;
		
		final MessageTemplate msgTemplate_1 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_1"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_1 = this.myAgent.receive(msgTemplate_1);
		
		final MessageTemplate msgTemplate_2 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_2"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_2 = this.myAgent.receive(msgTemplate_2);
		
		final MessageTemplate msgTemplate_3 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_3"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_3= this.myAgent.receive(msgTemplate_3);
		
		final MessageTemplate msgTemplate_4 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_4"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_4 = this.myAgent.receive(msgTemplate_4);
		

		 if (msg_1 != null) {	
			
			try {
				this.envoyeur = msg_1.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch(this.envoyeur) {
			case "Explo1":
				this.randomWalk(3);
				break;
			case "Explo2":
				this.randomWalk(3);
				break;
			case "Collect1":
				this.randomWalk(5);
				break;
			case "Collect2":
				this.randomWalk(5);
				break;
			case "Collect3":
				this.randomWalk(5);
				break;
			default:
				break;
			}
			}
		else if (msg_2 != null) {	
			
			try {
				this.envoyeur = msg_2.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch(this.envoyeur) {
			case "Explo1":
				this.randomWalk(3);
				break;
			case "Explo2":
				this.randomWalk(3);
				break;
			case "Collect1":
				this.randomWalk(5);
				break;
			case "Collect2":
				this.randomWalk(5);
				break;
			case "Collect3":
				this.randomWalk(5);
				break;
			default:
				break;
			}
			exitValue = 2;
			finished = true;
			}
		else if (msg_3 != null) {	
			
			try {
				this.envoyeur = msg_3.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch(this.envoyeur) {
			case "Explo1":
				this.randomWalk(3);
				break;
			case "Explo2":
				this.randomWalk(3);
				break;
			case "Collect1":
				this.randomWalk(5);
				break;
			case "Collect2":
				this.randomWalk(5);
				break;
			case "Collect3":
				this.randomWalk(5);
				break;
			default:
				break;
			}
			exitValue = 3;
			finished = true;
			}
		else if (msg_4 != null) {	
			
			try {
				this.envoyeur = msg_4.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch(this.envoyeur) {
			case "Explo1":
				this.randomWalk(3);
				break;
			case "Explo2":
				this.randomWalk(3);
				break;
			case "Collect1":
				this.randomWalk(5);
				break;
			case "Collect2":
				this.randomWalk(5);
				break;
			case "Collect3":
				this.randomWalk(5);
				break;
			default:
				break;
			}
			exitValue = 4;
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
	