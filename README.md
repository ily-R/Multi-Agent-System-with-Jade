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

### Finite state machine (FSM) classes:

<p align="center">
  <img width="600" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/fsm.JPG?raw=true" alt="capture reconstruction"/>
</p>

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

  <img width = 300 align="left" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/Explo1.gif?raw=true" alt="capture reconstruction">

  <img width = 300 align="right" src="https://github.com/ily-R/Multi-Agent-System-with-Jade/blob/master/README_data/Explo2.gif?raw=true" alt="capture reconstruction">


## Reference:

* [Dedale](https://dedale.gitlab.io/page/about/)
