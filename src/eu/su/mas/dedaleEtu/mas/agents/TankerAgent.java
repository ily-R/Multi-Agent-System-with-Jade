package eu.su.mas.dedaleEtu.mas.agents;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.DecisionMaking;
import eu.su.mas.dedaleEtu.mas.behaviours.EndProcess;
import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalkTanker;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveInfo;
import eu.su.mas.dedaleEtu.mas.behaviours.SendTankerPosition;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;



public class TankerAgent extends AbstractDedaleAgent implements AgentInterface{

	private static final long serialVersionUID = -1784844593772918359L;
	
	private  static  final  String init = "init";
	private  static  final  String sendPosition = "sendPosition";
	private  static final  String receivedInfo = "receivedInfo";
	private static final String randomWalk = "randomWalk";
	private static final String decisionMaking = "decision_making";
	private static final String fin = "fin";

	private  List<String> list_nodes_circuit;
	private  int cpt;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String, List<Couple<String, Integer>>>> list_agent_cap;
	private MapRepresentation myMap;


	protected void setup(){

		super.setup();
		
		this.ListeTresor = new ArrayList<Couple<String,List<Couple<Observation,Integer>>>>();
		this.list_agent_cap = new ArrayList<Couple<String, List<Couple<String, Integer>>>>();
		this.list_nodes_circuit= new ArrayList<String>();
		this.cpt =0;
		this.myMap =  new MapRepresentation();
		
		List<Behaviour> lb=new ArrayList<Behaviour>();		
		FSMBehaviour fsm = new FSMBehaviour(this);
		
		fsm.registerFirstState (new InitTankerBehaviour(this), init);
		fsm.registerState(new SendTankerPosition(this), sendPosition);
		fsm.registerState(new ReceiveInfo(this), receivedInfo);
		fsm.registerState(new RandomWalkTanker(this), randomWalk);
		fsm.registerState(new EndProcess(), fin);
		fsm.registerState(new DecisionMaking(this), decisionMaking);

		
		
		fsm.registerDefaultTransition (init, sendPosition);
		
		fsm.registerTransition(sendPosition, randomWalk, 2);
		
		fsm.registerDefaultTransition (randomWalk, sendPosition);
		
		fsm.registerDefaultTransition (sendPosition, receivedInfo);
		
		fsm.registerDefaultTransition (receivedInfo, sendPosition);
		
		fsm.registerTransition(receivedInfo, decisionMaking, 2);
		
		fsm.registerDefaultTransition (decisionMaking, receivedInfo);
		
		fsm.registerTransition(receivedInfo, fin, 3);

		
		
		lb.add(fsm);
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}


	protected void takeDown(){

	}


	@Override
	public void setMap(MapRepresentation myMap) {
		this.myMap = myMap;
		
	}


	@Override
	public MapRepresentation getMap() {
		// TODO Auto-generated method stub
		return this.myMap;
	}


	@Override
	public void setListTresor(List<Couple<String, List<Couple<Observation, Integer>>>> ListeTresor) {
		this.ListeTresor = ListeTresor;
		
	}


	@Override
	public List<Couple<String, List<Couple<Observation, Integer>>>> getListTresor() {
		// TODO Auto-generated method stub
		return this.ListeTresor;
	}


	@Override
	public void setTankerPosition(String posTanker) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getTankerPosition() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setCpt(int cpt) {
		this.cpt = cpt;
		
	}


	@Override
	public int getCpt() {
		return this.cpt;
	}


	@Override
	public void setListAgentCap(List<Couple<String, List<Couple<String, Integer>>>> listAgent) {
		this.list_agent_cap = listAgent;
		
	}


	@Override
	public List<Couple<String, List<Couple<String, Integer>>>> getListAgentCap() {
		return this.list_agent_cap;
	}


	@Override
	public void setListNodes(List<String> listNodes) {
			this.list_nodes_circuit = listNodes;
	}


	@Override
	public List<String> getListNodes() {
		return this.list_nodes_circuit;
	}


	@Override
	public void setMissionPosition(String missionPos) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getMissionPosition() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setCapacities(List<Couple<String, Integer>> capacities) {
				
	}


	@Override
	public List<Couple<String, Integer>> getCapacities() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setTresorNodeInfo(Couple<String, List<Couple<Observation, Integer>>> nodeInfo) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Couple<String, List<Couple<Observation, Integer>>> getTresorNodeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}


/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/

class InitTankerBehaviour extends SimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;
	
	private boolean finished = false;
	private int exitValue;
	private List<String> list_nodes;

	public InitTankerBehaviour (final AbstractDedaleAgent myagent) {
		super(myagent);
		this.list_nodes =  new ArrayList<String>();
	}

	@Override
	public void action() {
		
		exitValue = 1;
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs= ((AbstractDedaleAgent)this.myAgent).observe();

			while(lobs.size() < 5) {
				Random r= new Random();
				int moveId=1+r.nextInt(lobs.size()-1);
				((AbstractDedaleAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
				lobs=((AbstractDedaleAgent)this.myAgent).observe();
			}
			
			for(Couple<String,List<Couple<Observation,Integer>>> pos : lobs)
			this.list_nodes.add(pos.getLeft());
			
			 ((AgentInterface) this.myAgent).setListNodes(this.list_nodes);
			 System.out.println("_________________Init finished");

		}
		finished  = true;
	}
public int onEnd(){return exitValue ;}


@Override
public boolean done() {
return finished;
}

}