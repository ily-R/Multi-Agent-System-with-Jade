package eu.su.mas.dedaleEtu.mas.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;


/**
 * This simple topology representation only deals with the graph, not its content.</br>
 * The knowledge representation is not well written (at all), it is just given as a minimal example.</br>
 * The viewer methods are not independent of the data structure, and the dijkstra is recomputed every-time.
 * 
 * @author hc
 */
public class MapRepresentation implements Serializable {

	public enum MapAttribute {
		agent,open
	}

	private static final long serialVersionUID = -1333959882640838272L;

	private Graph g; //data structure
	private Viewer viewer; //ref to the display
	private Integer nbEdges;//used to generate the edges ids
	public Map<String[], LinkedList<String>> sg ;	
	/*********************************
	 * Parameters for graph rendering
	 ********************************/

	private String defaultNodeStyle= "node {"+"fill-color: black;"+" size-mode:fit;text-alignment:under; text-size:14;text-color:white;text-background-mode:rounded-box;text-background-color:black;}";
	private String nodeStyle_open = "node.agent {"+"fill-color: forestgreen;"+"}";
	private String nodeStyle_agent = "node.open {"+"fill-color: blue;"+"}";
	private String nodeStyle=defaultNodeStyle+nodeStyle_agent+nodeStyle_open;


	public MapRepresentation() {
		System.setProperty("org.graphstream.ui.renderer","org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		this.g= new SingleGraph("My world vision");
		this.g.setAttribute("ui.stylesheet",nodeStyle);
		this.viewer = this.g.display();
		this.nbEdges=0;
		this.sg = null;
	}
	
	public void serializeMap()
	{
		//this.sg= new SerializableSimpleGraph<String,MapAttribute>();
		//1) R ́ealise la copie de g (non s ́erializable) dans sg.
		this.sg = new LinkedHashMap<String[], LinkedList<String>>();  
		Iterator<Node> iter = (Iterator<Node>) this.g.getNodeIterator();
		while (iter.hasNext()) {
			Node element = iter.next();
			LinkedList<String> ls = new LinkedList<String>();
			Iterator<Node> iter2 = (Iterator<Node>)element.getNeighborNodeIterator();
			while(iter2.hasNext())
			{

				ls.add(iter2.next().getId());
			}
			String[] str = {element.getAttribute("ui.label"), element.getAttribute("ui.class")};
			this.sg.put(str , ls);
		}

	}

	/**
	 * Before the migration we kill all non serializable components and store their data in a serializable form
	 */
	public void prepareMigration()
	{
		//this.sg= new SerializableSimpleGraph<String,MapAttribute>();
		//1) R ́ealise la copie de g (non s ́erializable) dans sg.
		this.sg = new LinkedHashMap<String[], LinkedList<String>>();  
		Iterator<Node> iter = (Iterator<Node>) this.g.getNodeIterator();
		while (iter.hasNext()) {
			Node element = iter.next();
			LinkedList<String> ls = new LinkedList<String>();
			Iterator<Node> iter2 = (Iterator<Node>)element.getNeighborNodeIterator();
			while(iter2.hasNext())
			{

				ls.add(iter2.next().getId());
			}
			String[] str = {element.getAttribute("ui.label"), element.getAttribute("ui.class")};
			this.sg.put(str , ls);
			iter2 = null;
		}
		iter = null;

		//2) On d ́etruit les refs vers les objets non sérializables
		if (this.viewer!=null)
		{
			this.viewer.close();

			this.viewer=null;
		}

		this.g=null;
		this.nbEdges = 0;

	}
	/**
	 * After migration we load the serialized data and recreate the non serializable components (Gui,..)
	 */
	public void loadSavedData()
	{
		this.g= new SingleGraph("My world vision");
		this.g.setAttribute("ui.stylesheet",nodeStyle);
		this.viewer = this.g.display();
		//Integer nbEd=0;
		//Parcours de sg et remplissage de g.
		for (String key[] : this.sg.keySet()) {
			if(key[1].equals("open")) {
				this.addNode(key[0],MapAttribute.open);
			}
			else {
				this.addNode(key[0]);
			}

			for (int i = 0; i < this.sg.get(key).size(); i++) {
				this.addEdge(key[0],this.sg.get(key).get(i));
			}
			this.sg = null;
		}

		System.out.println("Loading done");
	}



	/**
	 * Associate to a node an attribute in order to identify them by type. 
	 * @param id
	 * @param mapAttribute
	 */
	public void addNode(String id,MapAttribute mapAttribute){
		Node n;
		if (this.g.getNode(id)==null){
			n=this.g.addNode(id);
		}else{
			n=this.g.getNode(id);
		}
		n.clearAttributes();
		n.addAttribute("ui.class", mapAttribute.toString());
		n.addAttribute("ui.label",id);
	}

	/**
	 * Add the node id if not already existing
	 * @param id
	 */
	public void addNode(String id){
		Node n=this.g.getNode(id);
		if(n==null){
			n=this.g.addNode(id);
		}else{
			n.clearAttributes();
		}
		n.addAttribute("ui.label",id);
	}

	/**
	 * Add the edge if not already existing.
	 * @param idNode1
	 * @param idNode2
	 */
	public void addEdge(String idNode1,String idNode2){
		try {
			this.nbEdges++;
			this.g.addEdge(this.nbEdges.toString(), idNode1, idNode2);
		}catch (EdgeRejectedException e){
			//Do not add an already existing one
			this.nbEdges--;
		}

	}

	public Graph getGraph() {
		return this.g;
	}
	public Integer toInt() {
		return new Integer(1000);
	}

	/**
	 * Compute the shortest Path from idFrom to IdTo. The computation is currently not very efficient
	 * 
	 * @param idFrom id of the origin node
	 * @param idTo id of the destination node
	 * @return the list of nodes to follow
	 */
	public List<String> getShortestPath(String idFrom,String idTo){
		List<String> shortestPath=new ArrayList<String>();

		Dijkstra dijkstra = new Dijkstra();//number of edge
		dijkstra.init(g);
		dijkstra.setSource(g.getNode(idFrom));
		dijkstra.compute();//compute the distance to all nodes from idFrom
		List<Node> path=dijkstra.getPath(g.getNode(idTo)).getNodePath(); //the shortest path from idFrom to idTo
		Iterator<Node> iter=path.iterator();
		while (iter.hasNext()){
			shortestPath.add(iter.next().getId());
		}
		dijkstra.clear();
		shortestPath.remove(0);//remove the current position
		return shortestPath;
	}

}
