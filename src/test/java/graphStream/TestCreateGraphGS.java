package test.java.graphStream;

import java.util.Iterator;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

/**
 * This class is an example allowing you to quickly obtain a graphical representation of a given graph using the graphStream library 
 * It can be useful in order to follow an agent's view of its knowledge of the world
 * @author hc
 *
 */
public class TestCreateGraphGS {

	public static void main(String[] args) {
		
		//color of a node according to its type
		String defaultNodeStyle= "node {"+"fill-color: black;"+" size-mode:fit;text-alignment:under; text-size:14;text-color:white;text-background-mode:rounded-box;text-background-color:black;}";
		String nodeStyle_wumpus= "node.wumpus {"+"fill-color: red;"+"}";
		String nodeStyle_agent= "node.agent {"+"fill-color: forestgreen;"+"}";
		String nodeStyle_treasure="node.treasure {"+"fill-color: yellow;"+"}";
		String nodeStyle_EntryExit="node.exit {"+"fill-color: green;"+"}";
		
		String nodeStyle=defaultNodeStyle+nodeStyle_wumpus+nodeStyle_agent+nodeStyle_treasure+nodeStyle_EntryExit;
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Graph graph = new SingleGraph("Illustrative example");//generateGraph(true, 30);
		
		Iterator<Node> iter=graph.getNodeIterator();
		
		//SingleGraph graph = new SingleGraph("Tutorial 1");
		graph.setAttribute("ui.stylesheet",nodeStyle);
		
		Viewer viewer = graph.display();
		SpriteManager sman = new SpriteManager(graph);

		// the nodes can be added dynamically.
		graph.addNode("A");
		Node n= graph.getNode("A");
		n.addAttribute("ui.label", "Agent J");	
		n.setAttribute("ui.class", "agent");
		
		Object o=n.getAttribute("ui.label");
		System.out.println("object: "+o.toString());
		
		graph.addNode("B");
		n= graph.getNode("B");
		n.addAttribute("ui.label", "treasure");	
		n.setAttribute("ui.class", "treasure");
		
		graph.addNode("C");	
		n= graph.getNode("C");
		n.addAttribute("ui.label", "wumpus");	
		n.setAttribute("ui.class", "wumpus");
		
		graph.addNode("D");
		n= graph.getNode("D");
		n.addAttribute("ui.label", "The exit");	
		n.setAttribute("ui.class", "exit");
		
		graph.addNode("E");

		//graph structure
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.addEdge("DA", "D", "A");
		graph.addEdge("EC", "E", "C");
		
		
//		
//		Sprite s=sman.addSprite("s1"); //sprite name
//		//s.setPosition(2, 1, 0); //sprite relative position
//		s.attachToNode("C");//sprite associated to
//		s.addAttribute("Wind", true);
//	
//		


	}
	
	/**
	 * 
	 * @param type true creates a Dorogovtsev env, false create a grid. 
	 * @param size number of iteration, the greater the bigger maze.
	 * @return a new graph
	 */
	private static Graph generateGraph(boolean type,int size){
		Graph g=new SingleGraph("Random graph");
		Generator gen;
		
		if(type){
			//generate a DorogovtsevMendes environment
			gen= new DorogovtsevMendesGenerator();
			gen.addSink(g);
			gen.begin();
			for(int i=0;i<size;i++){
				gen.nextEvents();
			}
			gen.end();
		}else{
			//generate a square grid environment
			gen= new GridGenerator();
			gen.addSink(g);
			gen.begin();
			for(int i=0;i<size;i++){
				gen.nextEvents();
			}
			gen.end();
			
		}
		return g;
	}
	

	

}
