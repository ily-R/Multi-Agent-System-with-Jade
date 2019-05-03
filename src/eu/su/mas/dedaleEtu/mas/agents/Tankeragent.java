package eu.su.mas.dedaleEtu.mas.agents;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.EndProcess;
import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalkTanker;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceivedListeTresor;
import eu.su.mas.dedaleEtu.mas.behaviours.SendHi;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;



public class Tankeragent extends AbstractDedaleAgent implements explot_collect_agent{

	private static final long serialVersionUID = -1784844593772918359L;
	
	private  static  final  String init = "init";
	private  static  final  String sendHi = "sendHi";
	private  static final  String receivedMessage = "receivedMessage";
	private static final String randomWalk = "randomWalk";
	private static final String Decision_confirmation = "decision_confirmation";
	private  List<String> list_nodes_circuit;
	private  int cpt;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String, List<Integer>>> list_agent_cap;

	protected void setup(){

		super.setup();
		
		this.ListeTresor = new ArrayList<Couple<String,List<Couple<Observation,Integer>>>>();
		this.list_agent_cap = new ArrayList<Couple<String, List<Integer>>>();
		List<Behaviour> lb=new ArrayList<Behaviour>();
		this.list_nodes_circuit= new ArrayList<String>();
		this.cpt =0;


		
		
		FSMBehaviour fsm = new FSMBehaviour(this);
		fsm.registerFirstState (new InitTankerBehaviour(this), init);
		fsm.registerState(new SendHi(this), sendHi);
		fsm.registerState(new ReceivedListeTresor(this), receivedMessage);
		fsm.registerState(new RandomWalkTanker(this), randomWalk);
		fsm.registerState(new EndProcess(), Decision_confirmation);
		
		
		fsm.registerDefaultTransition (init,sendHi);
		fsm.registerDefaultTransition (sendHi,receivedMessage);
		fsm.registerDefaultTransition (receivedMessage,sendHi);
		fsm.registerDefaultTransition (randomWalk,sendHi);
		
		fsm.registerTransition(sendHi, randomWalk, 2);
		fsm.registerTransition(receivedMessage, Decision_confirmation, 2);

		
		
		lb.add(fsm);
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}


	protected void takeDown(){

	}


	@Override
	public void setMap(MapRepresentation myMap) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public MapRepresentation getMap() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setListTresor(List<Couple<String, List<Couple<Observation, Integer>>>> ListeTresor) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Couple<String, List<Couple<Observation, Integer>>>> getListTresor() {
		// TODO Auto-generated method stub
		return null;
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
	public void setListAgentCap(List<Couple<String, List<Integer>>> listAgent) {
		this.list_agent_cap = listAgent;
		
	}


	@Override
	public List<Couple<String, List<Integer>>> getListAgentCap() {
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
	private int exitValue = 1;
	private List<String> list_nodes;

	public InitTankerBehaviour (final AbstractDedaleAgent myagent) {
		super(myagent);
		this.list_nodes =  ((explot_collect_agent) this.myAgent).getListNodes();
	}

	@Override
	public void action() {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();

			while(lobs.size() < 4) {
				Random r= new Random();
				int moveId=1+r.nextInt(lobs.size()-1);
				((AbstractDedaleAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
				lobs=((AbstractDedaleAgent)this.myAgent).observe();
			}
			
			for(Couple<String,List<Couple<Observation,Integer>>> pos : lobs)
			this.list_nodes.add(pos.getLeft());
			
			 ((explot_collect_agent) this.myAgent).setListNodes(this.list_nodes);

		}
		finished  = true;
	}
public int onEnd(){return exitValue ;}


@Override
public boolean done() {
return finished;
}

}