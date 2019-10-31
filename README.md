# Multi-Agent-System-with-Jade

In this project we explore the functioning of a system that involves different agents within an environment.We have an agent system moving on a map, represented by a graph ( nodes and edges connecting them). 

**Agents can perform various actions:**
* move from one node to another if there is an edge between them.
* send a message on a certain protocol (share infos : like the map, warnings...)
* observe the goods/obstacles on the ground and the nodes around.
* can open tresors, collect what is on the ground and deposit what they have in Inventory.
* receive instructions.

**Type of Agents**:
* Explorer: navigates the map, to store-in its information and opens tresors if asked to.
* Collector: explores the envirnement as well as collects and moves tresors to the Tanker.
* Tanker: Store the tresor (gold, dimond).
* Golem: an agent that blocks a path ( we dont control this agent)

So the objective function is to maximize the quantity of the collected goods at the end. The agents work autonomously when exploring and follow instruction in case received from a  Tanker, or another agent during inter-blcoking.

### Finite state machine (FSM) classes

<p align="center">
  <img width="600" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/fsm.JPG?raw=true" alt="capture reconstruction"/>
</p>

### Setup

* `git clone https://github.com/ily-R/Multi-Agent-System-with-Jade.git`
* Install a Java IDE: [Eclipse](https://www.eclipse.org/downloads/)
* Open your IDE and do `Import existing projects` and put the root directory as where your *Multi-Agent-System-with-Jade* exists.
* Go to your project's properties -> Java Build Path -> Libraries -> click on dedale.jar -> edit the path to where it exists on you computer: `YOUR DIRECTORY\Multi-Agent-System-with-Jade\libs\dedale`
* Now as everything is setup, look in the *src* package Run `Principal.java`

### Test

#### Main map:

 * explorer in green, collector in blue, tanker in purple and  golem in red
<p align="center">
  <img src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/MainMap.gif?raw=true" alt="capture reconstruction"/>
</p>

### Exploration:

Here's how an agent (either collector or explorer) grows his map information by:
* exploring the map.
* receiving map's info stored in other agents that passe close by (within 3 edges).

  <img width = 400 align="left" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/Explo1.gif?raw=true" alt="capture reconstruction">

  <img width = 400 align="right" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/Explo2.gif?raw=true" alt="capture reconstruction"/>


#### To test other maps or add agents:

* Go to `ConfigurationFile.java` and change:

```
public static String INSTANCE_CONFIGURATION_ELEMENTS
public static String INSTANCE_TOPOLOGY
public static String INSTANCE_CONFIGURATION_ENTITIES
```

To other folders that exisit in `DedaleEtuFull2018/resources`.

Now you need to add all the agents that exisit in *INSTANCE_CONFIGURATION_ENTITIES* by modifying `Principal.java`.
If I want to add a Collector that is named in the *INSTANCE_CONFIGURATION_ENTITIES* as *Collect1*, I add the following:

```
c = containerList.get(ConfigurationFile.LOCAL_CONTAINER_NAME);
Assert.assertNotNull("This container does not exist",c);		
agentName="Collect1";
Object [] entityParametersC={"My parameters"};
ag=createNewDedaleAgent(c, agentName, CollectAgent.class.getName(), entityParametersC);
agentList.add(ag);
		
```
## Reference:

* [Dedale](https://dedale.gitlab.io/page/about/)
