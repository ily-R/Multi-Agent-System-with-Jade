package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class ReceiveAllReady extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622075555998L;

	private String envoyeur = "";
	private boolean finished = false;
	private int exitValue;
	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};
	private List<String> list_Agent;
	
	public ReceiveAllReady (final Agent myagent,List<String> liste_Agent) {
		super(myagent);
		this.list_Agent = liste_Agent;
	}

	@Override
	public void action() {
		exitValue = 1;
		
		final MessageTemplate msgTemplate_1 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("ready_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_1 = this.myAgent.receive(msgTemplate_1);

		if (msg_1 != null) {	
			if(this.list_Agent.contains(msg_1.getSender())) 
				this.list_Agent.remove(msg_1.getSender());
			
			if(this.list_Agent.isEmpty()) {
				//((AbstractDedaleAgent)this.myAgent).openLock(Observation.GOLD); Impossible d'ouvrir?
				((AbstractDedaleAgent) this.myAgent).pick();
				((AbstractDedaleAgent)this.myAgent).observe();
				ACLMessage msg_done=new ACLMessage(ACLMessage.INFORM);
				msg_done.setSender(this.myAgent.getAID());
				msg_done.setProtocol("done_protocol");
				try {
					msg_done.setContentObject("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(String agent: listePrio)
				{
					if(!(this.myAgent.getLocalName()).equals(agent))
						msg_done.addReceiver(new AID(agent,AID.ISLOCALNAME));
					
				}
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg_done);

			}
		}
	}	
		
	public int onEnd(){return exitValue;}


	@Override
	public boolean done() {
		return finished;
	}
}
	