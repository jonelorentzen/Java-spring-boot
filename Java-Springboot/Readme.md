![UiS](https://www.uis.no/getfile.php/13391907/Biblioteket/Logo%20og%20veiledninger/UiS_liggende_logo_liten.png)

# Assgnment 3: Java Spring boot and Graph processing

| Lab 1:		| Java Spring boot		|
| -------------------- 	| ------------------------------------- |
| Subject: 		| DAT240 Advanced Programming 		|
| Deadline:		| Oct 09 2019 23:59			|
| Expected effort:	| 10-15 hours 				|
| Grading: 		| Pass/fail 				|
| Submission: 		| Individually				|


## Spring boot
In this assignment you will be developing a spring boot RESTful APIs for querying a social network graph from the Marvel universe. the data is found in the file called "marvel-hero-network.csv". Have look at the data.

A graph is a network consisting of nodes and edges. In this case nodes are superheroes and edges are if they occurred in the same comic book or not. The csv file contains so called "edge list" which is collection of edges. Each row in the csv file is an edge, together the form the network. For more details on what is a graph see [here](https://en.wikipedia.org/wiki/Graph_(discrete_mathematics))

To get started with spring boot follow the tutorial [here](https://www.tutorialspoint.com/spring_boot/spring_boot_building_restful_web_services.htm) or listen to the tutorial in the lab . 

## Processing the data
* Read the graph which is in CSV format. You can reuse the code to read file from your previous assignment.
* It is a good idea to use a datastructure such as HashMap<> to store the graph data. The key to this hash map will be the name of the node and value will be a HasSet<> or ArrayList<> which stores the neighbors
* The name of the super heroes contains aliases for example, Iron Man is written as "IRON MAN/TONY STARK" in the data file. 

- You need to split these strings into individual names and store a key for each of them. For example, for a node called "IRON MAN/TONY STARK" you need to insert two keys in the hash map "iron man" and "tony stark". Since this is an undirected graph, you also need to consider the edges in the flipped direction as well.

For example, a row in the csv file with "4-D MAN/MERCURIO,IRON MAN/TONY STARK" will result in following four entries in hashmap:
  - Key: 4-d man, Value: iron man/tony stark
  - Key: mercurio, Value: iron man/tony stark
  - Key: iron man, Value: 4-d man/mercurio
  - Key: tony stark, Value: 4-d man/mercurio

- You also must convert everything to lower case while parsing the data. 
- Make sure any leading and trailing spaces are removed using trim() String method

## Webservice APIs
Your project should provide following APIs at localhost port 8080 (default tomcat port or spring boot does that for you by default):

* / just a greeting message: At the root level when [http://localhost:8080](http://localhost:8080) is called from your browser you should return the greeting message "Greetings from Spring Boot!"
* neighbors API: this API retruns the neighbors of a node: which is the list of nodes adjacent to a given node. I.e, list of superheroes who appeared together in same comics for a given superhero. See definition of neighborhood from Wikipedia [here](https://en.wikipedia.org/wiki/Neighbourhood_(graph_theory))
  - /neighbors?id=super_hero_name: [http://localhost:8080/neighbors?id=iron man](http://localhost:8080/neighbors?id=iron man) should return a json with following format: 
  - {"Node": "iron man", "Neighbors": ["jarvis edwin", "magneto",...]}
* degree API: This API returns the node degree which is the number of links a node has https://en.wikipedia.org/wiki/Degree_(graph_theory)
  - /degree?id=super_hero_name: [http://localhost:8080/degree?id=iron man](http://localhost:8080/degree?id=iron man) should return
  - {"Node": "iron man", "Degree": 1348}
* checkedge API: This API checks if there is an edge between two nodes. An edge as mentioned before and defined [here](https://en.wikipedia.org/wiki/Glossary_of_graph_theory_terms#edge) exists if two nodes are in the same row of the csv or in other words are adjacent to each other in the graph.
  - /checkedge?id1=super_hero1&id2=super_hero2: [http://localhost:8080/degree?id1=iron man&id2=jarvis ewin](http://localhost:8080/degree?id1=iron man&id2=jarvis edwin)
  - {"Node1": "iron man", "Node2": "jarvis edwin", "EdgeExists": true}

## Authentication
Each of these APIs should be made accessible only via username and password. For simplicty we will keep the user name 'user' and password 'test' (not a good practice in real world applications just for this toy project it is fine). For this we are going to use WebSecurityConfigurerAdapter. You can see how to configure an authenticated API [here](https://www.mkyong.com/spring-boot/spring-rest-spring-security-example/)
When the APIs are called without proper user credentials it should return a authentication failure error.
