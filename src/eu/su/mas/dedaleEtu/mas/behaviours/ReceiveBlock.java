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
	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};
	
	public ReceiveBlock (final Agent myagent) {
		super(myagent);

	}

	@Override
	public void action() {
		
		final MessageTemplate msgTemplate_1 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_1"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_1 = this.myAgent.receive(msgTemplate_1);
		
		final MessageTemplate msgTemplate_2 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_2"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_2 = this.myAgent.receive(msgTemplate_2);

		if (msg_1 != null) {	
			
			try {
				this.envoyeur = msg_1.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			if((this.envoyeur).equals("Explo1")) {
				int i = 0;
				while(i<5) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Explo2")) {
				int i = 0;
				while(i<7) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Explo3")) {
				int i = 0;
				while(i<10) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Collect1")) {
				int i = 0;
				while(i<3) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Collect2")) {
				int i = 0;
				while(i<3) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			exitValue = 1;
			}

		if (msg_2 != null) {	
			
			try {
				this.envoyeur = msg_2.getSender().getLocalName();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			if((this.envoyeur).equals("Explo1")) {
				int i = 0;
				while(i<5) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Explo2")) {
				int i = 0;
				while(i<7) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Explo3")) {
				int i = 0;
				while(i<10) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Collect1")) {
				int i = 0;
				while(i<3) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			else if((this.envoyeur).equals("Collect2")) {
				int i = 0;
				while(i<3) {
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
				Random rand = new Random(); 
				int posAleat = rand.nextInt(lobs.size());
				String nextNode = lobs.get(posAleat).getLeft();
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				i++;
				}

			}
			exitValue = 2;
			}
		finished = true;

		}	
		
	public int onEnd(){return exitValue;}


	@Override
	public boolean done() {
		return finished;
	}
}
	