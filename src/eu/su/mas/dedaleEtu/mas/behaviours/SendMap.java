package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.io.Serializable;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
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
public class SendMap extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public  MapRepresentation myMap;
	private boolean finished = false;
	private int exitValue = 1 ;
	private String[] listePrio = {"Explo1","Explo2", "Explo3"};


	public SendMap (final Agent myagent, MapRepresentation myMap) {
		super(myagent);
		this.myMap= myMap;
	}

	@Override
	public void action() {
		//System.out.println(this.myAgent.getLocalName()+" send the map.");

		//A message is defined by : a performative, a sender, a set of receivers, (a protocol),(a content (and/or contentOBject))
		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setProtocol("UselessProtocol");
		if(this.myMap != null) {
		try {
			this.myMap.serializeMap();
			msg.setContentObject((Serializable) this.myMap.sg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*if((this.myAgent.getLocalName()).equals("Explo1")) {
			msg.addReceiver(new AID("Explo2",AID.ISLOCALNAME));
		}else if((this.myAgent.getLocalName()).equals("Explo2")){
			msg.addReceiver(new AID("Explo1",AID.ISLOCALNAME));
		}*/
		
		for(String agent: listePrio)
		{
			if(!(this.myAgent.getLocalName()).equals(agent))
			{
				msg.addReceiver(new AID(agent,AID.ISLOCALNAME));

			}	
			
		}
			
			//Mandatory to use this method (it takes into account the environment to decide if someone is reachable or not)
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		}else {
			System.out.println("null before sending");
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