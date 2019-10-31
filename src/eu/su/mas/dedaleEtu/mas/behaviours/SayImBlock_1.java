package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class SayImBlock_1 extends SimpleBehaviour{

	private static final long serialVersionUID = -2055554622078521998L;
	
	private int exitValue;
	private boolean finished = false;
	private String[] listePrio = {"Explo1","Explo2","Collect1","Collect2","Collect3","Tanker1"};

	public SayImBlock_1 (final Agent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		
		exitValue = 1 ;
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("BlockProtocol_1");
		
		if (myPosition!=""){
			msg.setContent(this.myAgent.getName()+" ; "+myPosition);
			for(String agent: listePrio)
			{
				if(!(this.myAgent.getLocalName()).equals(agent))
				{
					msg.addReceiver(new AID(agent,AID.ISLOCALNAME));

				}	
				
			}
				
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		}
		finished = true;
	}
	public int onEnd()
	{return exitValue;
	}
	@Override
	public boolean done() {
		return finished;
	}
}