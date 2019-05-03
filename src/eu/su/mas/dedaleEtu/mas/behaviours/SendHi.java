package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.explot_collect_agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


public class SendHi extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078521998L;
	private boolean finished = false;
	private int exitValue;

	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};

	public SendHi (final Agent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg_tankerPosition=new ACLMessage(ACLMessage.INFORM);
		msg_tankerPosition.setSender(this.myAgent.getAID());
		msg_tankerPosition.setProtocol("tanker_position_protocol");
		exitValue = 1;
		
		if (myPosition!=""){
			msg_tankerPosition.setContent(myPosition);
			for(String agent: listePrio)
			{
				if(!(this.myAgent.getLocalName()).equals(agent))
				{
					msg_tankerPosition.addReceiver(new AID(agent,AID.ISLOCALNAME));
				}	
				
			}

			((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tankerPosition);
		}
		if (((explot_collect_agent) this.myAgent).getCpt() > 50){
			exitValue  = 2;
			((explot_collect_agent) this.myAgent).setCpt(0);
		}
		
		((explot_collect_agent) this.myAgent).setCpt(((explot_collect_agent) this.myAgent).getCpt() + 1);
		this.myAgent.doWait(500);
		finished = true;
	}
	public int onEnd(){return this.exitValue;}
	@Override
	public boolean done() {
		return finished;
	}
}