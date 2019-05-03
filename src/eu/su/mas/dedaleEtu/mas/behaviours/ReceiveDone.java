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


public class ReceiveDone extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622075555998L;

	private String envoyeur = "";
	private boolean finished = false;
	private int exitValue;
	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};
	
	public ReceiveDone (final Agent myagent) {
		super(myagent);

	}

	@Override
	public void action() {
		exitValue = 1;
		
		final MessageTemplate msgTemplate_1 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("done_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_1 = this.myAgent.receive(msgTemplate_1);
		
		final MessageTemplate msgTemplate_2 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("block_protocol"),//Il faut modifier le nom de ce protocole
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_2 = this.myAgent.receive(msgTemplate_2);

		if (msg_1 != null) {	
			exitValue = 3;
			finished = true;
			//SI on a recu le done on retourne au tanker
			}else {
				if(msg_2 != null) 
				exitValue = 2;
				finished = true;
				//si on a recu un message de blocage, on passe ne interblocage
				
			}
		// si rien de tout ça, on recommence ce behavior
	}	
		
	public int onEnd(){return exitValue;}


	@Override
	public boolean done() {
		return finished;
	}
}
	