package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.agents.explot_collect_agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


public class SendMap extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622078521998L;

	private  MapRepresentation myMap;
	private boolean finished = false;
	private int exitValue = 1 ;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private String[] listePrio = {"Explo1","Explo2", "Explo3","Collect1","Collect2","Collect3","Tanker1"};
	private String tankPos ;

	public SendMap (final Agent myagent){
		super(myagent);
		this.myMap= ((explot_collect_agent) this.myAgent).getMap();
		this.ListeTresor = ((explot_collect_agent) this.myAgent).getListTresor();
		this.tankPos = ((explot_collect_agent) this.myAgent).getTankerPosition();
	}

	@Override
	public void action() {

		
		ACLMessage msg_map=new ACLMessage(ACLMessage.INFORM);
		msg_map.setSender(this.myAgent.getAID());
		msg_map.setProtocol("map_protocol");
		
		ACLMessage msg_tresorList=new ACLMessage(ACLMessage.INFORM);
		msg_tresorList.setSender(this.myAgent.getAID());
		msg_tresorList.setProtocol("liste_tresor_protocol");
		
		ACLMessage msg_tankerPosition=new ACLMessage(ACLMessage.INFORM);
		msg_tankerPosition.setSender(this.myAgent.getAID());
		msg_tankerPosition.setProtocol("tanker_position_protocol");
		
		
		if(this.myMap != null) {
		try {
			this.myMap.serializeMap();
			msg_map.setContentObject((Serializable) this.myMap.sg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(this.ListeTresor != null) {
		try {
			msg_tresorList.setContentObject((Serializable) this.ListeTresor);
		} catch (IOException e) {

			e.printStackTrace();
		}

		
		for(String agent: listePrio)
		{
			if(!(this.myAgent.getLocalName()).equals(agent))
			{
				msg_map.addReceiver(new AID(agent,AID.ISLOCALNAME));
				msg_tresorList.addReceiver(new AID(agent,AID.ISLOCALNAME));
				msg_tankerPosition.addReceiver(new AID(agent,AID.ISLOCALNAME));
			}	
			
		}
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tresorList);

		}	
		
		if(this.tankPos != null) {
			boolean flag = true;
			  try
			   {
			      Integer.parseInt( this.tankPos );
			   }
			   catch(NumberFormatException e )
			   {
				   flag = false ;			  
				   }
			if(flag)
			{	
				msg_tankerPosition.setContent(this.tankPos);
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg_tankerPosition);
				System.out.println( "________tankerpos sent"+this.tankPos);
			}
		}
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg_map);

		}else {
			System.out.println("null before sending");
		}
		finished = true;
	}
	public int onEnd(){
		return exitValue;}

	@Override
	public boolean done() {
		return finished;
	}
}