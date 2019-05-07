package eu.su.mas.dedaleEtu.mas.behaviours;



import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.SimpleBehaviour;



public class GoToNode extends SimpleBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	private MapRepresentation myMap;
	private int exitValue ;
	private String MissionPosition;

	public GoToNode (final AbstractDedaleAgent myagent) {	
		super(myagent);

	}
	

	@Override
	public void action() {

		exitValue = 1;
		this.myMap = ((AgentInterface) this.myAgent).getMap();
		this.MissionPosition = ((AgentInterface) this.myAgent).getMissionPosition();
		
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		if(myPosition.equals(this.MissionPosition))
		{
			 exitValue = 3;
		     finished = true;
		}
		
		else if (myPosition!=null){
			
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String nextNode= this.myMap.getShortestPath(myPosition,  this.MissionPosition).get(0);	
			if(!((AbstractDedaleAgent)this.myAgent).moveTo(nextNode)) {
					 exitValue = 2;
				     finished = true;
				}
			}
				
		finished = true;
			}
	public int onEnd(){return exitValue;}
	

	@Override
	public boolean done() {
		return finished;
	}

}
