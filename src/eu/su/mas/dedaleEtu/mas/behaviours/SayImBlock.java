package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * This example behaviour try to send a hello message (every 3s maximum) to agents Collect2 Collect1
 * @author hc
 *
 */
public class SayImBlock extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2055554622078521998L;
	private int exitValue = 1 ;
	private boolean finished = false;
	private String[] listePrio = {"Explo1","Explo2", "Explo3"};


	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public SayImBlock (final Agent myagent) {
		super(myagent);
		//super(myagent);
	}

	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		//A message is defined by : a performative, a sender, a set of receivers, (a protocol),(a content (and/or contentOBject))
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("BlockProtocol");
		System.out.println(this.myAgent.getLocalName()+" dit qu'il est bloqué");
		if (myPosition!=""){
			//System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			msg.setContent(this.myAgent.getName()+" ; "+myPosition);

			for(String agent: listePrio)
			{
				if(!(this.myAgent.getLocalName()).equals(agent))
				{
					msg.addReceiver(new AID(agent,AID.ISLOCALNAME));

				}	
				
			}
				

			//Mandatory to use this method (it takes into account the environment to decide if someone is reachable or not)
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		}
		finished = true;
	}
	public int onEnd(){return exitValue;}
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}
}