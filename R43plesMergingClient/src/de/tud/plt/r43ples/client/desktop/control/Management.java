package de.tud.plt.r43ples.client.desktop.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.tud.plt.r43ples.client.desktop.control.enums.MergeQueryTypeEnum;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.control.enums.TripleObjectTypeEnum;
import de.tud.plt.r43ples.client.desktop.model.Difference;
import de.tud.plt.r43ples.client.desktop.model.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.HttpResponse;
import de.tud.plt.r43ples.client.desktop.model.TreeNodeObject;
import de.tud.plt.r43ples.client.desktop.model.Triple;

/**
 * Provides methods for data management.
 * 
 * @author Stephan Hensel
 *
 */
public class Management {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Management.class);
	/** The SPARQL prefixes. **/
	private static final String prefixes = 
			  "PREFIX prov: <http://www.w3.org/ns/prov#> \n"
			+ "PREFIX dc-terms: <http://purl.org/dc/terms/> \n"
			+ "PREFIX rmo: <http://eatld.et.tu-dresden.de/rmo#> \n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX prov: <http://www.w3.org/ns/prov#> \n"
			+ "PREFIX sddo: <http://eatld.et.tu-dresden.de/sddo#> \n"
			+ "PREFIX sdd: <http://eatld.et.tu-dresden.de/sdd#> \n"
			+ "PREFIX rpo: <http://eatld.et.tu-dresden.de/rpo#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
	
	
	/**
	 * Get all graphs under revision control.
	 * 
	 * @return the array list of all revised graphs
	 * @throws IOException
	 */
	public static ArrayList<String> getAllGraphsUnderRevisionControl() throws IOException {
		logger.info("Get all graphs under revision control.");
		
		// Result array list
		ArrayList<String> list = new ArrayList<String>();
		
		// Get the revised graphs (result format is JSON)
		String revisedGraphs = TripleStoreInterface.executeHttpGet("/r43ples/getRevisedGraphs");
		logger.debug(revisedGraphs);
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(revisedGraphs);
		if (element.isJsonObject()) {
			JsonObject results = element.getAsJsonObject().getAsJsonObject("results");
			JsonArray bindings = results.getAsJsonArray("bindings");
			for (int i = 0; i < bindings.size(); i++) {
				String graphName = bindings.get(i).getAsJsonObject().getAsJsonObject("graph").get("value").getAsString();
				list.add(graphName);
			}
		}
		return list;
	}
	
	
	/**
	 * Get all branch names of graph.
	 * 
	 * @param graphName the graph name
	 * @return the array list of all branch names
	 * @throws IOException 
	 */
	public static ArrayList<String> getAllBranchNamesOfGraph(String graphName) throws IOException {
		logger.info("Get all branch names of graph.");
		
		// Result array list
		ArrayList<String> list = new ArrayList<String>();
		
		if (graphName != null) {
						
			String query = prefixes + String.format(
					  "SELECT DISTINCT  ?label %n"
					+ "FROM <%s> %n"
					+ "WHERE { %n"
					+ "	?branch a rmo:Branch ; %n"
					+ "		rdfs:label ?label . %n"
					+ "	?rev rmo:revisionOfBranch ?branch ;"
					+ "		rmo:revisionOf <%s> . %n"
					+ "}", Config.r43ples_revision_graph, graphName);
	
			String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
			logger.debug(result);
			
			// Iterate over all labels
			ResultSet resultSet = ResultSetFactory.fromXML(result);
			while (resultSet.hasNext()) {
				QuerySolution qs = resultSet.next();
				list.add(qs.getLiteral("?label").toString());
			}
		}
		return list;
	}
	
	
	
	/**
	 * Execute each kind of MERGE query.
	 * 
	 * @param graphName the graph name
	 * @param user the user
	 * @param commitMessage the commit message
	 * @param type the merge query type
	 * @param branchNameA the branch name A
	 * @param branchNameB the branch name B
	 * @param triples the triples which should be in the WITH part
	 * @param differenceModel the difference model for storing the java representation
	 * @return the query result
	 * @throws IOException 
	 */
	public static String executeMergeQuery(String graphName, String user, String commitMessage, MergeQueryTypeEnum type, String branchNameA, String branchNameB, String triples, DifferenceModel differenceModel) throws IOException {
		// TODO Response should include the status / only the status
		logger.info("Execute merge query of type " + type.toString());
		
		String queryTemplate = 
				  "USER \"%s\""
				+ "MESSAGE \"%s\""
				+ "MERGE %s GRAPH <%s> BRANCH \"%s\" INTO \"%s\"";		
		
		if (type.equals(MergeQueryTypeEnum.COMMON)) {
			String query = String.format(queryTemplate, user, commitMessage, "", graphName, branchNameA, branchNameB);
			HttpResponse response =TripleStoreInterface.executeQueryWithoutAuthorizationGetResponse(query, "HTML");
			if (response.getStatusCode() == HttpURLConnection.HTTP_CONFLICT) {
				// There was a conflict
				logger.info("Merge query produced conflicts.");
				// Read the difference model to java model
				readDifferenceModel(response.getBody(), differenceModel);
				// TODO
			} else if (response.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
				// There was no conflict merged revision was created
				logger.info("Merge query produced no conflicts. Merged revision was created.");
				//TODO
			} else {
				// Error occurred
				
			}
			
			
			
		} 

		
		return null;
	}

	
	/**
	 * Read difference model to java representation.
	 * 
	 * @param differenceModelToRead the difference model to read
	 * @param differenceModel the difference model where the result should be stored
	 * @return the difference model in java representation
	 * @throws IOException 
	 */
	public static void readDifferenceModel(String differenceModelToRead, DifferenceModel differenceModel) throws IOException {
		logger.info("Start reading difference model.");
		differenceModel.clear();
		
		// Read difference model to read to jena model
		Model model = readTurtleStringToJenaModel(differenceModelToRead);
		
		// Query all difference groups
		String queryDifferenceGroups = prefixes + String.format(
				  "SELECT ?uri ?tripleStateA ?tripleStateB ?automaticResolutionState ?conflicting %n"
				+ "WHERE { %n"
				+ "	?uri a rpo:DifferenceGroup ; %n"
				+ "		sddo:hasTripleStateA ?tripleStateA ; %n"
				+ "		sddo:hasTripleStateB ?tripleStateB ; %n"
				+ "		sddo:automaticResolutionState ?automaticResolutionState ; %n"
				+ "		sddo:isConflicting ?conflicting . %n"
				+ "}");
		logger.debug(queryDifferenceGroups);
		
		// query execution
		QueryExecution qeDifferenceGroups = QueryExecutionFactory.create(queryDifferenceGroups, model);
		ResultSet resultSetDifferenceGroups = qeDifferenceGroups.execSelect();
		// Iterate over all difference groups
	    while(resultSetDifferenceGroups.hasNext()) {
	    	QuerySolution qsDifferenceGroups = resultSetDifferenceGroups.next();
	    	String uri = qsDifferenceGroups.getResource("?uri").toString();
	    	SDDTripleStateEnum tripleStateA = convertSDDStringToSDDTripleState(qsDifferenceGroups.getResource("?tripleStateA").toString());
	    	SDDTripleStateEnum tripleStateB = convertSDDStringToSDDTripleState(qsDifferenceGroups.getResource("?tripleStateB").toString());
	    	SDDTripleStateEnum automaticResolutionState = convertSDDStringToSDDTripleState(qsDifferenceGroups.getResource("?automaticResolutionState").toString());
	    	boolean conflicting = qsDifferenceGroups.getLiteral("?conflicting").toString().equals("1^^http://www.w3.org/2001/XMLSchema#integer");   	

	    	DifferenceGroup differenceGroup = new DifferenceGroup(tripleStateA, tripleStateB, automaticResolutionState, conflicting);
	    	
	    	// Query all differences
			String queryDifferences = prefixes + String.format(
					  "SELECT ?subject ?predicate ?object ?referencedRevisionA ?referencedRevisionB %n"
					+ "WHERE { %n"
					+ "	<%s> a rpo:DifferenceGroup ; %n"
					+ "		rpo:hasDifference ?differenceUri . %n"
					+ "	?differenceUri a rpo:Difference ; %n"
					+ "		rpo:hasTriple ?tripleUri . %n"
					+ "	OPTIONAL { ?differenceUri rpo:referencesA ?referencedRevisionA . } %n"
					+ "	OPTIONAL { ?differenceUri rpo:referencesB ?referencedRevisionB . } %n"
					+ "	?tripleUri rdf:subject ?subject ; %n"
					+ "		rdf:predicate ?predicate ; %n"
					+ "		rdf:object ?object . %n"
					+ "}", uri);
			logger.debug(queryDifferences);
			
			// query execution
			QueryExecution qeDifferences = QueryExecutionFactory.create(queryDifferences, model);
			ResultSet resultSetDifferences = qeDifferences.execSelect();
			// Iterate over all differences
		    while(resultSetDifferences.hasNext()) {
		    	QuerySolution qsDifferences = resultSetDifferences.next();
		    	
		    	String subject = qsDifferences.getResource("?subject").toString();
		    	String predicate = qsDifferences.getResource("?predicate").toString();
	    	
		    	// Differ between literal and resource
				String object = "";
				TripleObjectTypeEnum objectType = null;
				if (qsDifferences.get("?object").isLiteral()) {
					object = qsDifferences.getLiteral("?object").toString();
					objectType = TripleObjectTypeEnum.LITERAL;
				} else {
					object = qsDifferences.getResource("?object").toString();
					objectType = TripleObjectTypeEnum.RESOURCE;
				}
		    	
		    	Triple triple = new Triple(subject, predicate, object, objectType);
		    	
		    	String referencedRevisionA = null;
		    	if (qsDifferences.getResource("?tripleStateA") != null) {
		    		referencedRevisionA = qsDifferences.getResource("?tripleStateA").toString();
		    	}
		    	String referencedRevisionB = null;
		    	if (qsDifferences.getResource("?tripleStateB") != null) {
		    		referencedRevisionB = qsDifferences.getResource("?tripleStateB").toString();
		    	}
		    	
		    	Difference difference = new Difference(triple, referencedRevisionA, referencedRevisionB);
		    	differenceGroup.addDifference(tripleToString(triple), difference);
		    }
	    	differenceModel.addDifferenceGroup(differenceGroup.getTripleStateA().toString() + "-" + differenceGroup.getTripleStateB().toString(), differenceGroup);
	    }
	    
	    logger.info("Difference model successfully read.");
		
	}
	
	
	/**
	 * Read Turtle string to jena model.
	 * 
	 * @param triples the triples in Turtle serialization
	 * @return the model
	 * @throws IOException
	 */
	public static Model readTurtleStringToJenaModel(String triples) throws IOException {
		Model model = null;
		model = ModelFactory.createDefaultModel();
		InputStream is = new ByteArrayInputStream(triples.getBytes());
		model.read(is, null, "TURTLE");
		is.close();
		
		return model;
	}
	
	
	
	/**
	 * Convert SDD state string to SDD triple state. If value does not exists in enum null will be returned.
	 * 
	 * @param state the state to convert
	 * @return the SDD triple state
	 */
	public static SDDTripleStateEnum convertSDDStringToSDDTripleState(String state) {
		
		if (state.equals(SDDTripleStateEnum.ADDED.getSddRepresentation())) {
			return SDDTripleStateEnum.ADDED;
		} else if (state.equals(SDDTripleStateEnum.DELETED.getSddRepresentation())) {
			return SDDTripleStateEnum.DELETED;
		} else if (state.equals(SDDTripleStateEnum.ORIGINAL.getSddRepresentation())) {
			return SDDTripleStateEnum.ORIGINAL;
		} else if (state.equals(SDDTripleStateEnum.NOTINCLUDED.getSddRepresentation())) {
			return SDDTripleStateEnum.NOTINCLUDED;
		} else {
			return null;
		}
		
	}
	
	
	/**
	 * Create a string representation of the triple.
	 * 
	 * @param triple the triple
	 * @return the string representation
	 */
	public static String tripleToString(Triple triple) {
		
		if (triple.getObjectType().equals(TripleObjectTypeEnum.LITERAL)) {
			logger.debug(String.format("<%s> <%s> \"%s\"", triple.getSubject(), triple.getPredicate(), triple.getObject()));
			return String.format("<%s> <%s> \"%s\"", triple.getSubject(), triple.getPredicate(), triple.getObject());
		} else {
			logger.debug(String.format("<%s> <%s> <%s>", triple.getSubject(), triple.getPredicate(), triple.getObject()));
			return String.format("<%s> <%s> <%s>", triple.getSubject(), triple.getPredicate(), triple.getObject());
		}
		
	}
	
	
	/**
	 * Get all conflicting difference groups.
	 * 
	 * @param differenceModel the difference model to use
	 * @return the array list of all conflicting difference groups
	 */
	public static ArrayList<DifferenceGroup> getAllConflictingDifferenceGroups(DifferenceModel differenceModel) {
		
		ArrayList<DifferenceGroup> result = new ArrayList<DifferenceGroup>();
		
		Iterator<String> iteDifferenceGroups = differenceModel.getDifferenceGroups().keySet().iterator();
		
		while (iteDifferenceGroups.hasNext()) {
			String currentkey = iteDifferenceGroups.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(currentkey);
			
			if (differenceGroup.isConflicting()) {
				logger.debug(differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB());
				result.add(differenceGroup);
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * Get all non conflicting difference groups.
	 * 
	 * @param differenceModel the difference model to use
	 * @return the array list of all non conflicting difference groups
	 */
	public static ArrayList<DifferenceGroup> getAllNonConflictingDifferenceGroups(DifferenceModel differenceModel) {
		
		ArrayList<DifferenceGroup> result = new ArrayList<DifferenceGroup>();
		
		Iterator<String> iteDifferenceGroups = differenceModel.getDifferenceGroups().keySet().iterator();
		
		while (iteDifferenceGroups.hasNext()) {
			String currentkey = iteDifferenceGroups.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(currentkey);
			
			if (!differenceGroup.isConflicting()) {
				result.add(differenceGroup);
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * Create the differences tree root node.
	 * 
	 * @param differenceModel the differences model to use
	 * @return the root node
	 */
	public static DefaultMutableTreeNode createDifferencesTree(DifferenceModel differenceModel) {
		
		// Create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeNodeObject("root", ResolutionState.CONFLICT));		
		
		// Create group of conflicting difference groups
		DefaultMutableTreeNode conflictsNode = new DefaultMutableTreeNode(new TreeNodeObject("Conflicts", ResolutionState.CONFLICT));
		
		ArrayList<DifferenceGroup> conflicting = getAllConflictingDifferenceGroups(differenceModel);
		
		Iterator<DifferenceGroup> iteConflicting = conflicting.iterator();
		while (iteConflicting.hasNext()) {
			DifferenceGroup differenceGroup = iteConflicting.next();
			
			DefaultMutableTreeNode differenceGroupNode = new DefaultMutableTreeNode(new TreeNodeObject(differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB(), ResolutionState.CONFLICT));
			
			// Add all differences
			Iterator<Difference> iteDifferences = differenceGroup.getDifferences().values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.CONFLICT));
				differenceGroupNode.add(differenceNode);
			}
			
			conflictsNode.add(differenceGroupNode);

		}
		
		root.add(conflictsNode);
		
		// Create group of non conflicting difference groups
		DefaultMutableTreeNode differencesNode = new DefaultMutableTreeNode(new TreeNodeObject("Differences", ResolutionState.DIFFERENCE));
		
		ArrayList<DifferenceGroup> nonConflicting = getAllNonConflictingDifferenceGroups(differenceModel);
		
		Iterator<DifferenceGroup> iteNonConflicting = nonConflicting.iterator();
		while (iteNonConflicting.hasNext()) {
			DifferenceGroup differenceGroup = iteNonConflicting.next();
			
			DefaultMutableTreeNode differenceGroupNode = new DefaultMutableTreeNode(new TreeNodeObject(differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB(), ResolutionState.DIFFERENCE));
			
			// Add all differences
			Iterator<Difference> iteDifferences = differenceGroup.getDifferences().values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.DIFFERENCE));
				differenceGroupNode.add(differenceNode);
			}
			
			differencesNode.add(differenceGroupNode);

		}
		
		root.add(differencesNode);
		
		refreshParentNodeStateDifferencesTree(root);
		
		return root;
		
	}
	
	
	/**
	 * Refresh the parent node states by current child states of the difference tree.
	 * 
	 * @param rootNode the root node of tree
	 */
	public static ResolutionState refreshParentNodeStateDifferencesTree(DefaultMutableTreeNode rootNode) {
		
		if (!rootNode.isLeaf()) {
			ResolutionState rootNodeState = ResolutionState.RESOLVED;
			
			for (int i=0; i<rootNode.getChildCount(); i++) {
				ResolutionState childState = refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) rootNode.getChildAt(i));
				if (childState.compareTo(rootNodeState) > 0) {
					rootNodeState = childState;
				}
			}
			((TreeNodeObject) rootNode.getUserObject()).setResolutionState(rootNodeState);
			return rootNodeState;
		} else {
			return ((TreeNodeObject) rootNode.getUserObject()).getResolutionState();
		}
		
	}
	
}
