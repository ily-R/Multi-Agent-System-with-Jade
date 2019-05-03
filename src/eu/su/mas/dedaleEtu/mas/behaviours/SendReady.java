package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


public class SendReady extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078521998L;
	private boolean finished = false;
	private int exitValue;
	private int cpt = 1;

	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};

	public SendReady (final Agent myagent) {
		super(myagent);

	}

	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		ACLMessage ready=new ACLMessage(ACLMessage.INFORM);
		ready.setSender(this.myAgent.getAID());
		ready.setProtocol("ready_protocol");
		this.exitValue = 1;
		
		if (myPosition!=""){
			ready.setContent(this.myAgent.getLocalName());
			for(String agent: listePrio)
			{
				if(!(this.myAgent.getLocalName()).equals(agent))
				{
					ready.addReceiver(new AID(agent,AID.ISLOCALNAME));
				}	
				
			}

			((AbstractDedaleAgent)this.myAgent).sendMessage(ready);
		}
	}
	public int onEnd(){return this.exitValue;}
	@Override
	public boolean done() {
		return this.finished;
	}
}