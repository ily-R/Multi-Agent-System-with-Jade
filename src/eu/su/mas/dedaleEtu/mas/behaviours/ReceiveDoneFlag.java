package eu.su.mas.dedaleEtu.mas.behaviours;


import eu.su.mas.dedale.mas.AbstractDedaleAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class ReceiveDoneFlag extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622075555998L;

	private boolean finished = false;
	private int exitValue;
	
	public ReceiveDoneFlag (final Agent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		
		exitValue = 1;
		// ------------------------------------------------- Receive Done
		final MessageTemplate msgTemplate_Done = MessageTemplate.and(
				MessageTemplate.MatchProtocol("Done_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );		
		final ACLMessage msg_Done = this.myAgent.receive(msgTemplate_Done);
		
		if(msg_Done != null) {
			
			try {
				this.myAgent.doWait(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			exitValue = 3; 
			finished = true;
			
		}
		
		//----------------------------------------------------------Inner InterBlockage
		
		final MessageTemplate msgTemplate_blocked2 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_2"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_blocked2 = this.myAgent.receive(msgTemplate_blocked2);
				
		if (msg_blocked2 != null) {
					
				ACLMessage msg2 =new ACLMessage(ACLMessage.INFORM);
				msg2.setSender(this.myAgent.getAID());
				msg2.setProtocol("BlockProtocol_2");
				msg2.setContent("Im waiting move out my way please");
				msg2.addReceiver(new AID(msg2.getSender().getLocalName(),AID.ISLOCALNAME));
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg2);

		}
				
		final MessageTemplate msgTemplate_blocked3 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_3"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_blocked3 = this.myAgent.receive(msgTemplate_blocked3);
				
		if (msg_blocked3 != null) {
					
				ACLMessage msg3 =new ACLMessage(ACLMessage.INFORM);
				msg3.setSender(this.myAgent.getAID());
				msg3.setProtocol("BlockProtocol_3");
				msg3.setContent("Im waiting move out my way please");
				msg3.addReceiver(new AID(msg3.getSender().getLocalName(),AID.ISLOCALNAME));
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg3);

		}
				
		final MessageTemplate msgTemplate_blocked4 = MessageTemplate.and(
				MessageTemplate.MatchProtocol("BlockProtocol_4"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_blocked4 = this.myAgent.receive(msgTemplate_blocked4);
				
		if (msg_blocked4 != null) {
					
				ACLMessage msg4 =new ACLMessage(ACLMessage.INFORM);
				msg4.setSender(this.myAgent.getAID());
				msg4.setProtocol("BlockProtocol_4");
				msg4.setContent("Im waiting move out my way please");
				msg4.addReceiver(new AID(msg4.getSender().getLocalName(),AID.ISLOCALNAME));
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg4);

		}
				

		finished = true;

		}	
		
	public int onEnd(){return exitValue;}


	@Override
	public boolean done() {
		return finished;
	}
}
	