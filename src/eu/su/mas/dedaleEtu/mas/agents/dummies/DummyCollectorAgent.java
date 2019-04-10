package eu.su.mas.dedaleEtu.mas.agents.dummies;


import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dataStructures.tuple.Couple;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * This dummy collector moves randomly, tries all its methods at each time step, store the treasure that match is treasureType 
 * in its backpack and intends to empty its backPack in the Tanker agent. @see {@link RandomWalkExchangeBehaviour}
 * 
 * @author hc
 *
 */
public class DummyCollectorAgent extends AbstractDedaleAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;



	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();

		List<Behaviour> lb=new ArrayList<Behaviour>();
		lb.add(new RandomWalkExchangeBehaviour(this));

		addBehaviour(new startMyBehaviours(this,lb));

		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}



	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}


	/**************************************
	 * 
	 * 
	 * 				BEHAVIOUR
	 * 
	 * 
	 **************************************/


	/**
	 * This behaviour is triggerable every 600ms.
	 *  It tries all the API methods at each time step, store the treasure that match the entity treasureType in its backpack and intends to 
	 *  empty its backPack in the Tanker agent (if he is in reach)
	 *<br/>
	 *
	 *  Rmq : This behaviour is in the same class as the DummyCollectorAgent for clarity reasons. You should prefer to save your behaviours in the behaviours package, and all the behaviours referring to a given protocol in the same class    
	 *	
	 *	@author hc
	 */
	class RandomWalkExchangeBehaviour extends TickerBehaviour{
		/**
		 * When an agent choose to move
		 *  
		 */
		private static final long serialVersionUID = 9088209402507795289L;

		public RandomWalkExchangeBehaviour (final AbstractDedaleAgent myagent) {
			super(myagent, 600);
			//super(myagent);
		}

		@Override
		public void onTick() {
			//Example to retrieve the current position
			String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

			if (myPosition!=""){
				List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition
				System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);

				//Little pause to allow you to follow what is going on
				try {
					System.out.println("Press enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//list of observations associated to the currentPosition
				List<Couple<Observation,Integer>> lObservations= lobs.get(0).getRight();

				//example related to the use of the backpack for the treasure hunt
				Boolean b=false;
				for(Couple<Observation,Integer> o:lObservations){
					switch (o.getLeft()) {
					case DIAMOND:case GOLD:
						
						System.out.println(this.myAgent.getLocalName()+" - My treasure type is : "+((AbstractDedaleAgent) this.myAgent).getMyTreasureType());
						System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent) this.myAgent).getBackPackFreeSpace());
						System.out.println(this.myAgent.getLocalName()+" - Value of the treasure on the current position: "+o.getLeft() +": "+ o.getRight());
						System.out.println(this.myAgent.getLocalName()+" - The agent grabbed :"+((AbstractDedaleAgent) this.myAgent).pick());
						System.out.println(this.myAgent.getLocalName()+" - the remaining backpack capacity is: "+ ((AbstractDedaleAgent) this.myAgent).getBackPackFreeSpace());
						b=true;
						break;
					default:
						break;
					}
				}

				//If the agent picked (part of) the treasure
				if (b){
					List<Couple<String,List<Couple<Observation,Integer>>>> lobs2=((AbstractDedaleAgent)this.myAgent).observe();//myPosition
					System.out.println("State of the observations after picking "+lobs2);
				}

				//Trying to store everything in the tanker
				System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println(this.myAgent.getLocalName()+" - The agent tries to transfer is load into the Silo (if reachable); succes ? : "+((AbstractDedaleAgent)this.myAgent).emptyMyBackPack("Silo"));
				System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((AbstractDedaleAgent)this.myAgent).getBackPackFreeSpace());

				//Random move from the current position
				Random r= new Random();
				int moveId=1+r.nextInt(lobs.size()-1);//removing the current position from the list of target to accelerate the tests, but not necessary as to stay is an action

				//The move action (if any) should be the last action of your behaviour
				((AbstractDedaleAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
			}

		}

	}
}
