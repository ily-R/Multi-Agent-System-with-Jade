package eu.su.mas.dedaleEtu.mas.behaviours;





import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * 
 * @author ilyas Aroui
 *
 */
public class ReceiveMission extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078551998L;


	private boolean finished = false;
	private int exitValue;
	private String MissionPosition;

	public ReceiveMission (final Agent myagent) {
		super(myagent);
		this.MissionPosition = new String();
		
	}

	@Override
	public void action() {
	
		exitValue = 1;
		
		//--------------------------------------------------------receive Mission position
		
		final MessageTemplate msgTemplate_mission = MessageTemplate.and(
				MessageTemplate.MatchProtocol("mission_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );	
		
		final ACLMessage msg_mission = this.myAgent.receive(msgTemplate_mission);
		
		if (msg_mission != null) {
			this.MissionPosition = msg_mission.getContent();
			boolean flag = true;
			  try
			   {
			      Integer.parseInt( this.MissionPosition );
			   }
			   catch(NumberFormatException e )
			   {
				   flag = false ;			  
				   }
			if(flag) {
				((AgentInterface) this.myAgent).setMissionPosition(this.MissionPosition);
				exitValue = 2;
				finished = true;
			}
			

		}

		//----------------------------------------------------------receive Finish
		
		
		final MessageTemplate msgTemplate_finish = MessageTemplate.and(
				MessageTemplate.MatchProtocol("finish_protocol"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		final ACLMessage msg_finish = this.myAgent.receive(msgTemplate_finish);
		
		if (msg_finish != null) {
				System.out.println("__________Mission is done Im going to End Process");
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
	public int onEnd(){
		return exitValue;}


	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return finished;
	}
}
	