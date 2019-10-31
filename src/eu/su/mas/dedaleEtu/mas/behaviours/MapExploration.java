package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.SimpleBehaviour;


/**
 * 
 * @author ilyas Aroui
 *
 */
public class MapExploration extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private MapRepresentation myMap;
	private List<String> openNodes;
	private Set<String> closedNodes;	
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private int exitValue;


	public MapExploration(final AbstractDedaleAgent myagent) {
		super(myagent);

		this.openNodes=new ArrayList<String>();
		this.closedNodes=new HashSet<String>();
	}

	
	
	@Override
	public void action() {
		exitValue = 1 ;
		this.myMap=  ((AgentInterface) this.myAgent).getMap();
		this.ListeTresor = ((AgentInterface) this.myAgent).getListTresor();
		
		if(this.myMap==null)
			this.myMap= new MapRepresentation();
		
		if(this.ListeTresor==null)
			this.ListeTresor= new ArrayList<Couple<String,List<Couple<Observation,Integer>>>>();
		
		try {
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
	
		if (myPosition!=null){
			
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
			
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.closedNodes.add(myPosition);
			this.openNodes.remove(myPosition);
			this.myMap.addNode(myPosition);
			
			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			
			while(iter.hasNext()){				
				Couple<String, List<Couple<Observation, Integer>>> node =iter.next();
				String nodeId = node.getLeft();
				
				if (!this.closedNodes.contains(nodeId)){
					
					if (!this.openNodes.contains(nodeId)){
						this.openNodes.add(nodeId);
						this.myMap.addNode(nodeId, MapAttribute.open);
						this.myMap.addEdge(myPosition, nodeId);	
					}
					else{
						this.myMap.addEdge(myPosition, nodeId);
					}
					
					if (nextNode==null) nextNode=nodeId;
				}
				
				if(myPosition.equals(nodeId)) {
					
					try {
					Observation tresor = node.getRight().get(0).getLeft();	
					switch(tresor) {
					case DIAMOND:
						this.myMap.addNode(nodeId, MapAttribute.DIAMOND);
						if(!this.ListeTresor.contains(node))
							this.ListeTresor.add(node);
						break;
					case GOLD:
						this.myMap.addNode(nodeId, MapAttribute.GOLD);
						if(!this.ListeTresor.contains(node))
							this.ListeTresor.add(node);
						break;
					}
					}
					catch(Exception e) {
						
					}
				}
			}

			if (this.openNodes.isEmpty()){
				System.out.println( ((AbstractDedaleAgent)this.myAgent).getLocalName()+ " : Exploration successufully done.");
				finished=true;
				exitValue = 3;

			}
			else{

				if (nextNode==null){
					int best_size = 9999;
					int best_i = 0;
					
					for(int i = 0; i < this.openNodes.size(); i++)
					{
					int current_size = (this.myMap.getShortestPath(myPosition, this.openNodes.get(i))).size();
					
					if( current_size < best_size) {
							best_i = i;
							best_size = current_size;
							}
				
					}
					
				    nextNode=this.myMap.getShortestPath(myPosition, this.openNodes.get(best_i)).get(0);
					
				}
				
				if(!((AbstractDedaleAgent)this.myAgent).moveTo(nextNode)) {
					exitValue = 2;
					finished = true;

				}

			}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finished = true;
			}
	public int onEnd(){
		((AgentInterface) this.myAgent).setMap(this.myMap);
		((AgentInterface) this.myAgent).setListTresor(this.ListeTresor);
		return exitValue;}
	

	@Override
	public boolean done() {
		return finished;
	}

}
