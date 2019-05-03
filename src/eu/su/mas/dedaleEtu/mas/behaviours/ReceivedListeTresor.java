package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.explot_collect_agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


public class ReceivedListeTresor extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622078551998L;

	private boolean finished = false;
	private int exitValue = 1 ;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String, List<Integer>>> list_agent;

	public ReceivedListeTresor (final Agent myagent ) {
		super(myagent);
		this.ListeTresor = ((explot_collect_agent) this.myAgent).getListTresor();
		this.list_agent = ((explot_collect_agent) this.myAgent).getListAgentCap();
	}

	@Override
	public void action() {

		final MessageTemplate msgTemplate_tresorList = MessageTemplate.and(
				MessageTemplate.MatchProtocol("liste_final_tresor_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_tresorList = this.myAgent.receive(msgTemplate_tresorList);
		
		final MessageTemplate msgTemplate_agentCapabilities = MessageTemplate.and(
				MessageTemplate.MatchProtocol("liste_cap_agent_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_agentCapabilities = this.myAgent.receive(msgTemplate_agentCapabilities);
		
		final MessageTemplate msgTemplate_tresorListUpdate = MessageTemplate.and(
				MessageTemplate.MatchProtocol("tresor_list_update"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_tresorListUpdate = this.myAgent.receive(msgTemplate_tresorListUpdate);
		
		if(msg_tresorList != null) {
			if (ListeTresor == null) {
			
			try {
				this.ListeTresor =  (List<Couple<String,List<Couple<Observation,Integer>>>>) msg_tresorList.getContentObject();
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			}
		}
		
		if (msg_agentCapabilities != null) {
			
			Couple<String, List<Integer>> c;
			try {
				this.list_agent = (List<Couple<String, List<Integer>>>) msg_agentCapabilities.getContentObject();

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			ACLMessage msg_confirmation=new ACLMessage(ACLMessage.INFORM);
			msg_confirmation.setSender(this.myAgent.getAID());
			msg_confirmation.setProtocol("confirmation_protocol");
			msg_confirmation.addReceiver(new AID(msg_agentCapabilities.getSender().getLocalName(),AID.ISLOCALNAME));
			msg_confirmation.setContent("confirm");
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_confirmation);

			exitValue = 1;
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
	