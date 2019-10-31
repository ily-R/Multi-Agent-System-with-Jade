package eu.su.mas.dedaleEtu.mas.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.EndProcess;
import eu.su.mas.dedaleEtu.mas.behaviours.MapExploration;
import eu.su.mas.dedaleEtu.mas.behaviours.PickTreasure;
import eu.su.mas.dedaleEtu.mas.behaviours.FindTanker;
import eu.su.mas.dedaleEtu.mas.behaviours.GoToNode;
import eu.su.mas.dedaleEtu.mas.behaviours.GoToTanker;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveBlock;
import eu.su.mas.dedaleEtu.mas.behaviours.SendMap;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMap;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMission;
import eu.su.mas.dedaleEtu.mas.behaviours.SayImBlock_1;
import eu.su.mas.dedaleEtu.mas.behaviours.SayImBlock_2;
import eu.su.mas.dedaleEtu.mas.behaviours.SayImBlock_3;
import eu.su.mas.dedaleEtu.mas.behaviours.SayImBlock_4;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;

/**
 * 
 * @author ilyas Aroui
 *
 */

public class CollectAgent extends AbstractDedaleAgent implements AgentInterface {

	private static final long serialVersionUID = -6431752661190433727L;
	private MapRepresentation myMap;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private List<Couple<String, Integer>> capacities;
	private String TankerPosition;
	private String MissionPosition;
	private Couple<String,List<Couple<Observation,Integer>>> TreasureNodeInfo; 
	
	private static final String exploration = "exploration";
	private static final String sendMap = "send_map";
	private static final String receiveMap = "receive_map";
	private static final String sendBlock1 = "send_block1";
	private static final String sendBlock2 = "send_block2";
	private static final String sendBlock3 = "send_block3";
	private static final String sendBlock4 = "send_block4";
	private static final String receiveBlock = "receive_block";
	private static final String findTanker = "find_tanker";
	private static final String receiveMission = "ReceiveMision";
	private static final String goToNode = "goToNode";
	private static final String pickTreasure = "pickTreasure";
	private static final String goToTanker = "goToTanker";
	private static final String fin = "fin";

	protected void setup(){

		super.setup();
		
		this.ListeTresor = new ArrayList<>();
		this.myMap = new MapRepresentation();
		this.TankerPosition = new String() ;
		this.MissionPosition = new String();
		this.TreasureNodeInfo = new Couple<>(new String(), new ArrayList<>());
		this.capacities = new ArrayList<>();
		if(this.getBackPackFreeSpace() != null )
		{
			this.capacities.add(new Couple<>("GoldCapacity", Math.max(0, this.getBackPackFreeSpace())));
			this.capacities.add(new Couple<>("DiamondCapacity", Math.max(0, this.getBackPackFreeSpace())));
		}
		Set<Couple<Observation, Integer>> expertises = this.getMyExpertise();
		for(Couple<Observation, Integer> exp : expertises)
		{
			switch(exp.getLeft())
			{
			case STRENGH:
				this.capacities.add(new Couple<>("Strength", exp.getRight()));
				break;
			case LOCKPICKING:
				this.capacities.add(new Couple<>("Lockpicking", exp.getRight()));
				break;
			default:
				break;

			}
		}
		
		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		FSMBehaviour fsm = new FSMBehaviour(this);	
		
		fsm.registerFirstState (new MapExploration(this), exploration);
		fsm.registerState (new SendMap(this), sendMap);
		fsm.registerState (new ReceiveMap(this), receiveMap);
		fsm.registerState(new SayImBlock_1(this),sendBlock1 );
		fsm.registerState(new SayImBlock_2(this),sendBlock2 );
		fsm.registerState(new SayImBlock_3(this),sendBlock3 );
		fsm.registerState(new SayImBlock_4(this),sendBlock4 );
		fsm.registerState(new ReceiveBlock(this), receiveBlock);
		fsm.registerState(new FindTanker(this), findTanker);
		fsm.registerState(new GoToNode(this), goToNode);
		fsm.registerState(new GoToTanker(this), goToTanker);
		fsm.registerState(new PickTreasure(this), pickTreasure);
		fsm.registerState(new ReceiveMission(this), receiveMission);

		fsm.registerLastState(new EndProcess(), fin);
		
		fsm.registerDefaultTransition (exploration,sendMap);
		
		fsm.registerDefaultTransition (sendMap,receiveMap);
		
		fsm.registerDefaultTransition (receiveMap,exploration);
		
		fsm.registerTransition(exploration,sendBlock1,2);
		
		fsm.registerDefaultTransition(sendBlock1, receiveBlock);
		
		fsm.registerDefaultTransition(receiveBlock,exploration);
		
		fsm.registerTransition (exploration,findTanker, 3);
		
		fsm.registerDefaultTransition(findTanker, findTanker);
		
		fsm.registerTransition(findTanker,sendBlock2,2);
		
		fsm.registerDefaultTransition(sendBlock2, receiveBlock);
		
		fsm.registerTransition(receiveBlock,findTanker, 2);
		
		fsm.registerTransition(findTanker,receiveMission,3);
		
		fsm.registerDefaultTransition(receiveMission, receiveMission);
		
		fsm.registerTransition(receiveMission,goToNode,2);
		
		fsm.registerTransition(receiveMission,fin,3);
		
		fsm.registerTransition(goToNode,sendBlock3,2);
		
		fsm.registerDefaultTransition(sendBlock3, receiveBlock);
		
		fsm.registerTransition(receiveBlock,goToNode, 3);
		
		fsm.registerTransition(goToNode,pickTreasure,3);
		
		fsm.registerDefaultTransition(pickTreasure, pickTreasure);
		
		fsm.registerTransition(pickTreasure,goToTanker,2);
		
		fsm.registerTransition(goToTanker,sendBlock4,2);
		
		fsm.registerDefaultTransition(sendBlock4, receiveBlock);
		
		fsm.registerTransition(receiveBlock,goToTanker, 4);
		
		fsm.registerTransition(goToTanker,receiveMission,3);
		
		lb.add(fsm);
		addBehaviour(new startMyBehaviours(this,lb));
		
		try {
			this.doWait(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("the  agent "+this.getLocalName()+ " is started");
	}
	

	public void beforeMove()
	{
		
	super.beforeMove();
	System.out.println("Save everything (and kill GUI) before move");
	this.myMap.prepareMigration();
	
	}

	public void afterMove()
	{
		
	super.afterMove();
	this.myMap.loadSavedData();
	System.out.println("Restore data (and GUI) after moving");
	
	}


	@Override
	public void setMap(MapRepresentation myMap) {
			this.myMap = myMap;
	}


	@Override
	public MapRepresentation getMap() {
		return this.myMap ;
	}


	@Override
	public void setListTresor(List<Couple<String, List<Couple<Observation, Integer>>>> ListeTresor) {
		this.ListeTresor = ListeTresor;
	}


	@Override
	public List<Couple<String, List<Couple<Observation, Integer>>>> getListTresor() {
		return this.ListeTresor;
	}


	@Override
	public void setTankerPosition(String posTanker) {
			this.TankerPosition = posTanker;
	}


	@Override
	public String getTankerPosition() {
		return this.TankerPosition;
	}


	@Override
	public void setCpt(int cpt) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getCpt() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setListAgentCap(List<Couple<String, List<Couple<String, Integer>>>> listAgent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Couple<String, List<Couple<String, Integer>>>> getListAgentCap() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setListNodes(List<String> listNodes) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<String> getListNodes() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setMissionPosition(String missionPos) {
			this.MissionPosition = missionPos;
	}


	@Override
	public String getMissionPosition() {
		return this.MissionPosition;
	}


	@Override
	public void setCapacities(List<Couple<String, Integer>> capacities) {
		this.capacities = capacities;
		
	}


	@Override
	public List<Couple<String, Integer>> getCapacities() {
		return this.capacities;
	}


	@Override
	public void setTresorNodeInfo(Couple<String, List<Couple<Observation, Integer>>> nodeInfo) {
		this.TreasureNodeInfo = nodeInfo;
		
	}


	@Override
	public Couple<String, List<Couple<Observation, Integer>>> getTresorNodeInfo() {
		return this.TreasureNodeInfo;
	}

	
	
}
