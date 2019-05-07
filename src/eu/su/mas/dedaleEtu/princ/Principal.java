package eu.su.mas.dedaleEtu.princ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agents.dedaleDummyAgents.DummyWumpusShift;
import eu.su.mas.dedaleEtu.mas.agents.dummies.MyDummyXXXAgent;
import eu.su.mas.dedale.mas.agents.GateKeeperAgent;
import eu.su.mas.dedale.mas.agents.dedaleDummyAgents.DummyCollectorAgent;
import eu.su.mas.dedale.mas.agents.dedaleDummyAgents.DummyMovingAgent;
import eu.su.mas.dedale.mas.agents.dedaleDummyAgents.DummyTankerAgent;
import eu.su.mas.dedale.mas.agents.dedaleDummyAgents.DummyWumpusShift;
import eu.su.mas.dedaleEtu.mas.agents.TankerAgent;
import eu.su.mas.dedaleEtu.mas.agents.CollectAgent;
import eu.su.mas.dedaleEtu.mas.agents.ExploreAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.junit.Assert;
import jade.wrapper.AgentContainer;


/**
 * This class is used to start the platform and the agents. 
 * To launch your agents in the environment you desire you will have to :
 * <ul>
 * <li> set the ConfigurationFile parameters (and maybe update the files in resources/) {@link ConfigurationFile}</li>
 * <li> create your agents classes and call them in the createAgents method below </li>
 * </ul>
 * @author hc
 *
 */
public class Principal {


	private static HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();// container's name - container's ref
	private static List<AgentController> agentList;// agents's ref
	private static Runtime rt;	

	public static void main(String[] args){

		if(ConfigurationFile.COMPUTERisMAIN){
			//Whe should create the Platform and the GateKeeper, whether the platform is distributed or not 

			//1), create the platform (Main container (DF+AMS) + containers + monitoring agents : RMA and SNIFFER)
			rt=emptyPlatform(containerList);

			//2) create the gatekeeper (in charge of the environment), the agents, and add them to the platform.
			agentList=createAgents(containerList);

			//3) launch agents
			startAgents(agentList);
		}else{
			//We only have to create the local container and our agents

			//1') If a distant platform already exist and you want to create and connect your container to it
			containerList.putAll(createAndConnectContainer(ConfigurationFile.LOCAL_CONTAINER_NAME, ConfigurationFile.PLATFORM_HOSTNAME, ConfigurationFile.PLATFORM_ID, ConfigurationFile.PLATFORM_PORT));

			//2) create agents and add them to the platform.
			agentList=createAgents(containerList);

			//3) launch agents
			startAgents(agentList);
		}
	}
	


	/**********************************************
	 * 
	 * Methods used to create an empty platform
	 * 
	 **********************************************/

	/**
	 * Create an empty platform composed of 1 main container and 3 containers.
	 * @param containerList 
	 * @return a ref to the platform and update the containerList
	 */
	private static Runtime emptyPlatform(HashMap<String, ContainerController> containerList){

		Runtime rt = Runtime.instance();

		// 1) create a platform (main container+DF+AMS)
		Profile pMain = new ProfileImpl(ConfigurationFile.PLATFORM_HOSTNAME,ConfigurationFile.PLATFORM_PORT,ConfigurationFile.PLATFORM_ID);
		System.out.println("Launching a main-container..."+pMain);
		AgentContainer mainContainerRef = rt.createMainContainer(pMain); //DF and AMS are include

		// 2) create the containers
		containerList.putAll(createContainers(rt));

		// 3) create monitoring agents : rma agent, used to debug and monitor the platform; sniffer agent, to monitor communications; 
		createMonitoringAgents(mainContainerRef);

		System.out.println("Plaform ok");
		return rt;

	}

	/**
	 * Create the containers used to hold the agents 
	 * @param rt The reference to the main container
	 * @return an Hmap associating the name of a container and its object reference.
	 * <br/>
	 * note: there is a smarter way to find a container with its name, but we go fast to the goal here. Cf jade's doc.
	 */
	private static HashMap<String,ContainerController> createContainers(Runtime rt) {
		String containerName;
		ProfileImpl pContainer;
		ContainerController containerRef;
		HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();//bad to do it here.

		System.out.println("Launching containers ...");

		//create the container0	
		containerName=ConfigurationFile.LOCAL_CONTAINER_NAME;
		pContainer = new ProfileImpl(ConfigurationFile.PLATFORM_HOSTNAME, ConfigurationFile.PLATFORM_PORT, ConfigurationFile.PLATFORM_ID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.

		containerList.put(containerName, containerRef);

		//create the container0	
		containerName=ConfigurationFile.LOCAL_CONTAINER2_NAME;
		pContainer = new ProfileImpl(ConfigurationFile.PLATFORM_HOSTNAME, ConfigurationFile.PLATFORM_PORT, ConfigurationFile.PLATFORM_ID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.

		containerList.put(containerName, containerRef);

		//create the container1	
		containerName=ConfigurationFile.LOCAL_CONTAINER3_NAME;
		pContainer = new ProfileImpl(ConfigurationFile.PLATFORM_HOSTNAME, ConfigurationFile.PLATFORM_PORT, ConfigurationFile.PLATFORM_ID);
		//pContainer = new ProfileImpl(null, 8888, null);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		//create the container2	
		containerName=ConfigurationFile.LOCAL_CONTAINER4_NAME;
		pContainer = new ProfileImpl(ConfigurationFile.PLATFORM_HOSTNAME, ConfigurationFile.PLATFORM_PORT, ConfigurationFile.PLATFORM_ID);
		//pContainer = new ProfileImpl(null, 8888, null);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		System.out.println("Launching containers done");
		return containerList;

	}

	/**
	 * 
	 * @param containerName
	 * @param host  is the IP of the host where the main-container should be listen to. A null value means use the default (i.e. localhost)
	 * @param platformID is the symbolic name of the platform, if different from default. A null value means use the default (i.e. localhost)
	 * @param port (if null, 8888 by default)
	 * @return
	 */
	private static HashMap<String,ContainerController> createAndConnectContainer(String containerName,String host, String platformID, Integer port){

		ProfileImpl pContainer;
		ContainerController containerRef; //ContainerController replace AgentContainer in the new versions of Jade.
		HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();//bad to do it here.
		Runtime rti=Runtime.instance();

		if (port==null){
			port=ConfigurationFile.PLATFORM_PORT;
		}

		System.out.println("Create and Connect container "+containerName+ " to the host : "+host+", platformID: "+platformID+" on port "+port);

		pContainer = new ProfileImpl(host,port, platformID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		containerRef = rti.createAgentContainer(pContainer); 

		//ContainerID cID= new ContainerID();
		//cID.setName(containerName);
		//cID.setPort(port);
		//cID.setAddress(host);

		containerList.put(containerName, containerRef);
		return containerList;
	}

	/**
	 * create the monitoring agents (rma+sniffer) on the main-container given in parameter and launch them.
	 *  - RMA agent's is used to debug and monitor the platform;
	 *  - Sniffer agent is used to monitor communications
	 * @param mc the main-container's reference
	 */
	private static void createMonitoringAgents(ContainerController mc) {

		Assert.assertNotNull(mc);
		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;

		try {
			rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Launching of rma agent failed");
		}

		System.out.println("Launching  Sniffer agent on the main container...");
		AgentController snif=null;

		try {
			snif= mc.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);
			snif.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("launching of sniffer agent failed");

		}		


	}



	/**********************************************
	 * 
	 * Methods used to create the agents and to start them
	 * 
	 **********************************************/


	/**
	 *  Creates the agents and add them to the agentList.  agents are NOT started.
	 *@param containerList :Name and container's ref
	 *@return the agentList
	 */
	private static List<AgentController> createAgents(HashMap<String, ContainerController> containerList) {
		System.out.println("Launching agents...");
		ContainerController c;
		String agentName;
		List<AgentController> agentList=new ArrayList<AgentController>();


		if (ConfigurationFile.COMPUTERisMAIN){

			/*
			 * The main is on this computer, we deploy the GateKeeper 
			 */
			c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
			Assert.assertNotNull("This container does not exist",c);
			agentName=ConfigurationFile.DEFAULT_GATEKEEPER_NAME;
			try {
				//used to give informations to the agent
				//Object[] objtab=new Object[]{ConfigurationFile.INSTANCE_TOPOLOGY,ConfigurationFile.INSTANCE_CONFIGURATION_ELEMENTS,ConfigurationFile.ENVIRONMENT_TYPE};
				Object[] objtab=new Object[]{ConfigurationFile.INSTANCE_TOPOLOGY,ConfigurationFile.INSTANCE_CONFIGURATION_ELEMENTS,ConfigurationFile.ENVIRONMENT_TYPE,ConfigurationFile.ENVIRONMENTisGRID,ConfigurationFile.ENVIRONMENT_SIZE,ConfigurationFile.ACTIVE_DIAMOND,ConfigurationFile.ACTIVE_GOLD,ConfigurationFile.ACTIVE_WELL};//used to give informations to the agent
				AgentController	ag=c.createNewAgent(agentName,GateKeeperAgent.class.getName(),objtab);
				
				agentList.add(ag);
				System.out.println(agentName+" launched");
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

		}
		
		
		/************************************************************************************************
		 * The main container (now) exist, we deploy the agent(s) on  their local containers
		 *They will have to find the gatekeeper's container to deploy themselves in the environment. 
		 *This is automatically performed by all agent that extends AbstractDedaleAgent
		 ************************************************************************************************/
		AgentController	ag;
		
		/*****************************************************
		 * 
		 * 				ADD YOUR AGENTS HERE - As many as you want.
		 * Any agent added here should have its associated configuration available in the entities file 
		 * 
		 *****************************************************/
		
		
		/*********
		 * GOLEM
		 *********/
		//1) Get the container where the agent will appear
//c = containerList.get(ConfigurationFile.LOCAL_CONTAINER2_NAME);
//		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
//		agentName="Golem";
//		
//		//3) If you want to give specific parameters to your agent, add them here
//		Object [] entityParameters={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
//		ag=createNewDedaleAgent(c, agentName, DummyWumpusShift.class.getName(), entityParameters);
//		agentList.add(ag);	
		/*********
		 * AGENT DUMMY
		 *********//*
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
		
		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Dummy_14-408-17_team_1";
		
		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParameters1={"My parameters"};
		
		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, MyDummyXXXAgent.class.getName(), entityParameters1);
		//ag=createNewDedaleAgent(c, agentName, ExploreSoloAgent.class.getName(), entityParameters2);
		agentList.add(ag);
		*/
		
		/*********
		 * AGENT Explo1
		 *********/
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
		
		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Explo1";
		
		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParameters1={"My parameters"};
		
		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, ExploreAgent.class.getName(), entityParameters1);
		//ag=createNewDedaleAgent(c, agentName, ExploreSoloAgent.class.getName(), entityParameters2);
		agentList.add(ag);
		
		/*********
		 * AGENT Explo2
		 *********/
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Explo2";
//		
//		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParameters2={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, ExploreAgent.class.getName(), entityParameters2);
		agentList.add(ag);
		
		/*********
		 * AGENT Explo3
		 *********//*
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
		
		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Explo3";
		
		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParameters3={"My parameters"};
		
		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, ExploreAgent.class.getName(), entityParameters3);
		//ag=createNewDedaleAgent(c, agentName, ExploreSoloAgent.class.getName(), entityParameters2);
		agentList.add(ag);
		*/
		/*********
		 * AGENT Collect 1
		 *********/
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Collect1";
//		
//		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParametersC={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, CollectAgent.class.getName(), entityParametersC);
		agentList.add(ag);
		
		/*********
		 * AGENT Collect 2
		 *********/
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Collect2";
//		
//		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParametersC2={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, CollectAgent.class.getName(), entityParametersC2);
		agentList.add(ag);
		
		/*********
		 * AGENT Collect 3
		 *********/
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Collect3";
//		
//		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParametersC3={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, CollectAgent.class.getName(), entityParametersC3);
		agentList.add(ag);
		
		/***************
		 * AGENT Tanker
		 ***************/
		
		//1) Get the container where the agent will appear
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		Assert.assertNotNull("This container does not exist",c);
//		
//		//2) Give the name of your agent, MUST be the same as the one given in the entities file.
		agentName="Tanker1";
//		
//		//3) If you want to give specific parameters to your agent, add them here
		Object [] entityParametersT={"My parameters"};
//		
//		//4) Give the class name of your agent to let the system instantiate it
		ag=createNewDedaleAgent(c, agentName, TankerAgent.class.getName(), entityParametersT);
		agentList.add(ag);
		
		
		c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
		
		agentName="Golem";
		Object [] entityParametersg={"My parameters"};
		ag=createNewDedaleAgent(c, agentName, DummyWumpusShift.class.getName(), entityParametersg);
		 agentList.add(ag);
		
		/*********************
		 * All agents created
		 *********************/
		System.out.println("Agents created...");
		return agentList;
	}

	/**
	 * Start the agents
	 * @param agentList
	 */
	private static void startAgents(List<AgentController> agentList){

		System.out.println("Starting agents...");


		for(final AgentController ac: agentList){
			try {
				ac.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Agents started...");
	}

	/**
	 * 
	 * @param initialContainer container where to deploy the agent
	 * @param agentName name of the agent
	 * @param className class of the agent
	 * @param additionnalParameters 
	 */
	private static AgentController createNewDedaleAgent(ContainerController initialContainer, String agentName,String className, Object[] additionnalParameters){
		//Object[] objtab=new Object[]{env,agentName};//used to give informations to the agent
		Object[] objtab=AbstractDedaleAgent.loadEntityCaracteristics(agentName,ConfigurationFile.INSTANCE_CONFIGURATION_ENTITIES);
		Object []res2=merge(objtab,additionnalParameters);

		AgentController ag=null;
		try {
			ag = initialContainer.createNewAgent(agentName,className,res2);
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(ag);
		//agentList.add(ag);
		System.out.println(agentName+" launched");
		return ag;
	}

	/**
	 * tab2 is added at the end of tab1
	 * @param tab1
	 * @param tab2
	 */
	private static Object[] merge (Object [] tab1, Object[] tab2){
		Assert.assertNotNull(tab1);
		Object [] res;
		if (tab2!=null){
			res= new Object[tab1.length+tab2.length];
			int i= tab1.length;
			for(i=0;i<tab1.length;i++){
				res[i]=tab1[i];
			}
			for (int ind=0;ind<tab2.length;ind++){
				res[i]=tab2[ind];
				i++;
			}
		}else{
			res=tab1;
		}
		return res;
	}
}






