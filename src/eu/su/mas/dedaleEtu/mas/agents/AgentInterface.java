package eu.su.mas.dedaleEtu.mas.agents;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;

public interface AgentInterface {

	void setMap(MapRepresentation myMap);
	MapRepresentation getMap();
	
	void setListTresor(List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor);
	List<Couple<String,List<Couple<Observation,Integer>>>> getListTresor();
	
	void setTankerPosition(String posTanker);
	String getTankerPosition();
	
	void setMissionPosition(String missionPos);
	String getMissionPosition();
	
	void setCpt(int cpt);
	int getCpt();
	
	void setListAgentCap(List<Couple<String, List<Couple<String, Integer>>>> listAgent);
	List<Couple<String, List<Couple<String, Integer>>>> getListAgentCap();
	
	void setListNodes(List<String> listNodes);
	List<String> getListNodes();
	
	void setCapacities(List<Couple<String, Integer>> capacities);
	List<Couple<String, Integer>> getCapacities();
	
	void setTresorNodeInfo(Couple<String,List<Couple<Observation,Integer>>> nodeInfo);
	Couple<String,List<Couple<Observation,Integer>>> getTresorNodeInfo();


}
