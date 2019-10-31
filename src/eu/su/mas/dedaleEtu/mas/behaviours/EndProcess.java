package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class EndProcess extends SimpleBehaviour {
	private boolean finished = false;
	private String[] listePrio = {"Explo1","Explo2","Collect1","Collect2","Collect3"};
	@Override
	public void action() {
		// TODO Auto-generated method stub
		int i = 0;
		ACLMessage msg_finish=new ACLMessage(ACLMessage.INFORM);
		msg_finish.setSender(this.myAgent.getAID());
		msg_finish.setProtocol("finish_protocol");
		
		while(i<10)
		{
			for(String agent: listePrio)
			{
				if(!(this.myAgent.getLocalName()).equals(agent)) {
					msg_finish.addReceiver(new AID(agent,AID.ISLOCALNAME));}
			}
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_finish);
		}
		
		System.out.println("_____Mission finished._____________________________________________________________");
		finished = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}

}
