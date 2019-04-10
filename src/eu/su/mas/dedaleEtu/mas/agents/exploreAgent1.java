package eu.su.mas.dedaleEtu.mas.agents;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.EndProcess;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploDuoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveBlock;
import eu.su.mas.dedaleEtu.mas.behaviours.SendMap;
import eu.su.mas.dedaleEtu.mas.behaviours.test1;
import eu.su.mas.dedaleEtu.mas.behaviours.test2;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMap;
import eu.su.mas.dedaleEtu.mas.behaviours.SayImBlock;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;

/**
 * ExploreSolo agent. 
 * It explore the map using a DFS algorithm.
 * It stops when all nodes have been visited
 *  
 *  
 * @author hc
 *
 */

public class exploreAgent1 extends AbstractDedaleAgent {

	private static final long serialVersionUID = -6431752665590433727L;
	private MapRepresentation myMap;	
	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	
	private  static  final  String exploration = "exploration";
	private  static  final  String sendMap = "send_map";
	private  static  final  String receiveMap = "receive_map";
	private static final String sendBlock = "send_block";
	private static final String receiveBlock = "receive_block";
	private  static  final  String fin = "fin";

	
	protected void setup(){

		super.setup();
		
		this.myMap=new MapRepresentation();
		List<Behaviour> lb=new ArrayList<Behaviour>();


		FSMBehaviour fsm = new FSMBehaviour(this);
		// Define the  different  states and behaviours
		fsm. registerFirstState (new ExploDuoBehaviour(this, myMap), exploration);
		fsm. registerState (new SendMap(this, myMap), sendMap);
		fsm. registerState (new ReceiveMap(this,myMap), receiveMap);
		fsm.registerState(new SayImBlock(this),sendBlock );
		fsm.registerState(new ReceiveBlock(this), receiveBlock);
		fsm.registerLastState(new EndProcess(), fin);
		// Register the  transitions
		fsm. registerDefaultTransition (exploration,sendMap);//Default
		fsm. registerDefaultTransition (sendMap,receiveMap);//Default
		fsm. registerDefaultTransition (receiveMap,exploration);//Default
		fsm.registerTransition(exploration,sendBlock,2);
		fsm.registerDefaultTransition(sendBlock, receiveBlock);
		fsm.registerDefaultTransition(receiveBlock,exploration);
		fsm. registerTransition (exploration,fin, 3);
		
		lb.add(fsm);
		addBehaviour(new startMyBehaviours(this,lb));
		
		try {
			this.doWait(13000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("the  agent "+this.getLocalName()+ " is started");
	}
	
	/**
	* Method automatically called by Jade when the agent
	* is preparing to move between containers
	*/
	public void beforeMove()
	{
	super.beforeMove();
	System.out.println("Save everything (and kill GUI) before move");
	this.myMap.prepareMigration();
	}
	/**
	* Method automatically called by Jade after a migration
	*/
	public void afterMove()
	{
	super.afterMove();
	this.myMap.loadSavedData();
	System.out.println("Restore data (and GUI) after moving");
	}

	
	
}
