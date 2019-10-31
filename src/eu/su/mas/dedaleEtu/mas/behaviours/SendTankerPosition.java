package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class SendTankerPosition extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078521998L;
	private boolean finished = false;
	private int exitValue;

	private String[] listePrio = {"Explo1","Explo2","Collect1","Collect2","Collect3"};

	public SendTankerPosition (final Agent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		exitValue = 1;
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg_tankerPosition=new ACLMessage(ACLMessage.INFORM);
		msg_tankerPosition.setSender(this.myAgent.getAID());
		msg_tankerPosition.setProtocol("tanker_position_protocol");
		
		if (myPosition!=""){
			msg_tankerPosition.setContent(myPosition);
			for(String agent: listePrio)
			{
				
				{
					msg_tankerPosition.addReceiver(new AID(agent,AID.ISLOCALNAME));
				}	
				
			}

			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tankerPosition);
		}
		if (((AgentInterface) this.myAgent).getCpt() > 10){
			((AgentInterface) this.myAgent).setCpt(0);
			exitValue  = 2;
			finished = true;
		}
		
		((AgentInterface) this.myAgent).setCpt(((AgentInterface) this.myAgent).getCpt() + 1);
		this.myAgent.doWait(500);
		finished = true;
	}
	public int onEnd(){return exitValue;}
	@Override
	public boolean done() {
		return finished;
	}
}