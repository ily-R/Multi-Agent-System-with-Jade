package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

import org.graphstream.graph.Node;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AgentInterface;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


import eu.su.mas.dedaleEtu.mas.agents.TankerAgent;


public class DecisionMaking extends SimpleBehaviour{


	private static final long serialVersionUID = -2058134622078551998L;

	private  MapRepresentation mapReceiver;
	private boolean finished = false;
	private int exitValue;
	private List<Couple<String,List<Couple<Observation,Integer>>>> ListeTresor;
	private  List<Couple<String, List<Couple<String, Integer>>>> ListeAgent;

	public DecisionMaking (final Agent myagent) {
		super(myagent);

		
	}

	@Override
	public void action() {

		exitValue = 1;
		//Debut de la decision
		System.out.println("________on cherche un decision");
		this.mapReceiver = ((TankerAgent) this.myAgent).getMap();
		this.ListeTresor = ((TankerAgent) this.myAgent).getListTresor();
		this.ListeAgent =  ((TankerAgent) this.myAgent).getListAgentCap();
		
		int best_tresor = -1;
		int best_Collect = -1;
		int bestValue = 1000;
		int strenghtvalues;
		int lockValue;

		int localStrengh = 0;
		int localLock = 0;
		
		int i; // première boucle
		for(i = 0; i < this.ListeTresor.size(); i++) { // On parcours les tresors
			Observation tresor_type = this.ListeTresor.get(i).getRight().get(0).getLeft();
			Integer value = this.ListeTresor.get(i).getRight().get(0).getRight();
			int j;
			for(j=0;j<this.ListeTresor.get(i).getRight().size();j++) { // On parcours les attributs des trésors
				int k;
				switch(this.ListeTresor.get(i).getRight().get(j).getLeft()) {
				case DIAMOND:
					
					for(k = 0; k < this.ListeAgent.size(); k++) { // On parcours les agents
						int l;
						for(l=0;l<this.ListeAgent.get(k).getRight().size();l++) { // On parcours les attributs des agents  pour trouver celui qui corresponds
							Integer backpack = 0;
							switch(this.ListeAgent.get(k).getRight().get(l).getLeft()) {
							
							case "DiamondCapacity": 
								backpack =  this.ListeAgent.get(k).getRight().get(l).getRight(); // backpack recupere la valeur du sac si il y en a.
								break;
								
							default:
								
							}
							int diff = Math.abs(backpack - value); // On regarde si c'est le meilleur résultat possible
							if(diff < bestValue) { // On enregistre le résulta
								best_Collect = l;
								best_tresor = i;
								bestValue = diff;
							}
								
						}
					}
					break;
					
				case GOLD: // Identique a DIAMOND
					
					for(k = 0; k < this.ListeAgent.size(); k++) {
						int l;
						for(l=0;l<this.ListeAgent.get(k).getRight().size();l++) {
							Integer backpack = 0;
							switch(this.ListeAgent.get(k).getRight().get(l).getLeft()) {
							
							case "GoldCapacity": 
								backpack =  this.ListeAgent.get(k).getRight().get(l).getRight();
								break;
								
							default:
								
							}
							int diff = Math.abs(backpack - value);
							if(diff < bestValue) {
								best_Collect = l;
								best_tresor = i;
								bestValue = diff;
							}
								
						}
					}
					break;
				}	
			}
		}
		
		if(best_tresor >= 0 && best_Collect >= 0) { // On vérifie qu'n couple a ete trouver
			String chosenOne = this.ListeAgent.get(best_Collect).getLeft(); // On enregistre le collect choisit
		
			int j;
			for(j=0;j<this.ListeTresor.get(best_tresor).getRight().size();j++) { // On parcours les parametre du tresor
				int k;
				switch(this.ListeTresor.get(best_tresor).getRight().get(j).getLeft()) {
				case STRENGH:
					localStrengh = this.ListeTresor.get(best_tresor).getRight().get(j).getRight(); //On enregistre la capacite de force  necessaire
					break;
				case LOCKPICKING:
					localLock = this.ListeTresor.get(best_tresor).getRight().get(j).getRight(); // On enregistre la capacite de crochetage necessaire
					break;
					
				}
			}
			
			List<Couple<String,Integer>> Liste_Selection = new ArrayList<Couple<String,Integer>>(); // On y enregistrera la liste de agents selectionné
				
			Integer k = 0; // servira a garder les index des agents selectionne
				
			while(localStrengh > 0 && localLock > 0 && k < this.ListeAgent.size()){ // tant que les capacités necessaire ne sont pas attente et que l'on a pas pris tous les agents présent
				
				int oldStrengh = localStrengh;
				int oldLock = localLock;
				
				for(j=0;j<this.ListeAgent.get(k).getRight().size();j++) { // On boucle sur les capacites de l'agent en cour de traitement
					
					switch(this.ListeAgent.get(k).getRight().get(j).getLeft()) {
					case "Strengh":
						if(localStrengh != 0 && this.ListeAgent.get(k).getRight().get(j).getRight() != 0) { // SI la force n'est pas nulle et la capacite differente de nulle 
							localStrengh -= this.ListeAgent.get(k).getRight().get(j).getRight();			// On diminue localStrengh
							if(localStrengh<0) {															// Si on descend en dessous de 0 On repositionne à 0
								localStrengh = 0;
							}
						}
						break;
					case "Lockpicking":
						if(localLock != 0 && this.ListeAgent.get(k).getRight().get(j).getRight() != 0) { // identique a force
							localLock -= this.ListeAgent.get(k).getRight().get(j).getRight();
							if(localLock<0) {
								localLock = 0;
							}
						}
						break;
						
					}
				if(oldStrengh != localStrengh || oldLock != localLock) {						// Si on a modifie une valeur
					Liste_Selection.add(new Couple<>(this.ListeAgent.get(k).getLeft(),k));		// Alors on ajoute l'agent a la mission
				}
				}
			}
			
			if(localStrengh == 0 && localLock == 0){ // Si une solution a ete trouve
				ACLMessage msg=new ACLMessage(ACLMessage.INFORM);	//On envoi un message au collecteur pour lui dire ou aller
				msg.setSender(this.myAgent.getAID());
				msg.setProtocol("mission_protocol");
				int position_adapter = 1; // variable servant a la suppression iterative des agents dans la ListeAgent 
				try {
					msg.setContentObject((Serializable) this.ListeTresor.get(best_tresor).getLeft() );
					msg.addReceiver(new AID(chosenOne,AID.ISLOCALNAME));
					System.out.println("_________Sending mission to " + chosenOne);
					((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
					msg.reset();
					this.ListeAgent.remove(best_Collect);
				} catch (IOException e) {

					e.printStackTrace();
				}
				Iterator<Node> iter = this.mapReceiver.getGraph().getNode(this.ListeTresor.get(best_tresor).getLeft()).getNeighborNodeIterator(); // On parcour les noeud voisin de l'objectif 
				while(iter.hasNext() || Liste_Selection.size() != 0) { // On attribut un noeud a chaque agent
					Couple<String, List<Couple<Observation, Integer>>> node =(Couple<String, List<Couple<Observation, Integer>>>) iter.next();
					ACLMessage msg1=new ACLMessage(ACLMessage.INFORM);
					msg1.setSender(this.myAgent.getAID()); // On envoie le message
					msg1.setProtocol("mission_protocol");
					try {
						msg1.setContentObject((Serializable) node.getLeft() );
						msg1.addReceiver(new AID(this.ListeAgent.get(Liste_Selection.get(0).getRight()).getLeft(),AID.ISLOCALNAME));
						//System.out.println("_________Sending mission to " + this.ListeAgent.get(Liste_Selection.get(0).getRight()).getLeft());

						((AbstractDedaleAgent)this.myAgent).sendMessage(msg1);
						msg1.reset();
						this.ListeAgent.remove(Liste_Selection.get(0).getRight() -position_adapter); // On supprime de la liste du tanker
						position_adapter += 1;
						
						Liste_Selection.remove(0); // On supprime de la liste courante
					} catch (IOException e) {

						e.printStackTrace();
					}
					
				}
			}
		}
		System.out.println("________on sort de decision");
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
	