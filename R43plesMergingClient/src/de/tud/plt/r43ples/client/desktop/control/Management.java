package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.atlas.web.HttpException;
import org.apache.log4j.Logger;

import att.grappa.Attribute;
import att.grappa.Edge;
import att.grappa.Graph;
import att.grappa.Node;
import att.grappa.Subgraph;

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
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HttpResponse;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.TableModelSummaryReport;
import de.tud.plt.r43ples.client.desktop.model.tree.TreeNodeObject;

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
					  "SELECT DISTINCT ?label %n"
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

	    	ResolutionState resolutionState = ResolutionState.DIFFERENCE;
	    	if (conflicting) {
	    		resolutionState = ResolutionState.CONFLICT;
	    	}
	    	
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
		    	if (qsDifferences.getResource("?referencedRevisionA") != null) {
		    		referencedRevisionA = qsDifferences.getResource("?referencedRevisionA").toString();
		    	}
		    	String referencedRevisionB = null;
		    	if (qsDifferences.getResource("?referencedRevisionB") != null) {
		    		referencedRevisionB = qsDifferences.getResource("?referencedRevisionB").toString();
		    	}
		    	
		    	// Add further information to difference
		    	// Get the revision number if available
		    	String referencedRevisionLabelA = null;
		    	String referencedRevisionLabelB = null;
		    	
				if ((referencedRevisionA != null) && (referencedRevisionB == null)) {
					String query = prefixes + String.format(
							  "SELECT ?rev %n"
							+ "FROM <%s> %n"
							+ "WHERE { %n"
							+ "	<%s> a rmo:Revision ; %n"
							+ "		rmo:revisionNumber ?rev . %n"
							+ "}", Config.r43ples_revision_graph, referencedRevisionA);
					
					String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
					logger.debug(result);
					
					ResultSet resultSet = ResultSetFactory.fromXML(result);
					if (resultSet.hasNext()) {
						QuerySolution qs = resultSet.next();
						referencedRevisionLabelA = qs.getLiteral("?rev").toString();
					}
				} else if ((referencedRevisionA == null) && (referencedRevisionB != null)) {
					String query = prefixes + String.format(
							  "SELECT ?rev %n"
							+ "FROM <%s> %n"
							+ "WHERE { %n"
							+ "	<%s> a rmo:Revision ; %n"
							+ "		rmo:revisionNumber ?rev . %n"
							+ "}", Config.r43ples_revision_graph, referencedRevisionB);
					
					String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
					logger.debug(result);
					
					// Iterate over all labels
					ResultSet resultSet = ResultSetFactory.fromXML(result);
					if (resultSet.hasNext()) {
						QuerySolution qs = resultSet.next();
						referencedRevisionLabelB = qs.getLiteral("?rev").toString();
					}
				} else if ((referencedRevisionA != null) && (referencedRevisionB != null)) {
					String query = prefixes + String.format(
							  "SELECT ?revA ?revB %n"
							+ "FROM <%s> %n"
							+ "WHERE { %n"
							+ "	<%s> a rmo:Revision ; %n"
							+ "		rmo:revisionNumber ?revA . %n"
							+ "	<%s> a rmo:Revision ; %n"
							+ "		rmo:revisionNumber ?revB . %n"
							+ "}", Config.r43ples_revision_graph, referencedRevisionA, referencedRevisionB);
					
					String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
					logger.debug(result);
					
					// Iterate over all labels
					ResultSet resultSet = ResultSetFactory.fromXML(result);
					if (resultSet.hasNext()) {
						QuerySolution qs = resultSet.next();
						referencedRevisionLabelA = qs.getLiteral("?revA").toString();
						referencedRevisionLabelB = qs.getLiteral("?revB").toString();
					}
				}	    	
		    	
		    	Difference difference = new Difference(triple, referencedRevisionA, referencedRevisionLabelA, referencedRevisionB, referencedRevisionLabelB, automaticResolutionState, resolutionState);
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
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeNodeObject("All", ResolutionState.CONFLICT, null));		
		
		// Create group of conflicting difference groups
		DefaultMutableTreeNode conflictsNode = new DefaultMutableTreeNode(new TreeNodeObject("Conflicts", ResolutionState.CONFLICT, null));
		
		ArrayList<DifferenceGroup> conflicting = getAllConflictingDifferenceGroups(differenceModel);
		
		Iterator<DifferenceGroup> iteConflicting = conflicting.iterator();
		while (iteConflicting.hasNext()) {
			DifferenceGroup differenceGroup = iteConflicting.next();
			
			DefaultMutableTreeNode differenceGroupNode = new DefaultMutableTreeNode(new TreeNodeObject(differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB(), ResolutionState.CONFLICT, differenceGroup));
			
			// Add all differences
			Iterator<Difference> iteDifferences = differenceGroup.getDifferences().values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.CONFLICT, difference));
				differenceGroupNode.add(differenceNode);
			}
			
			conflictsNode.add(differenceGroupNode);

		}
		
		root.add(conflictsNode);
		
		// Create group of non conflicting difference groups
		DefaultMutableTreeNode differencesNode = new DefaultMutableTreeNode(new TreeNodeObject("Differences", ResolutionState.DIFFERENCE, null));
		
		ArrayList<DifferenceGroup> nonConflicting = getAllNonConflictingDifferenceGroups(differenceModel);
		
		Iterator<DifferenceGroup> iteNonConflicting = nonConflicting.iterator();
		while (iteNonConflicting.hasNext()) {
			DifferenceGroup differenceGroup = iteNonConflicting.next();
			
			DefaultMutableTreeNode differenceGroupNode = new DefaultMutableTreeNode(new TreeNodeObject(differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB(), ResolutionState.DIFFERENCE, differenceGroup));
			
			// Add all differences
			Iterator<Difference> iteDifferences = differenceGroup.getDifferences().values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.DIFFERENCE, difference));
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
	
	
	/**
//	 * Get the tree path of the specified tree node object.
//	 * 
//	 * @param tree the tree which contains the tree node object
//	 * @param nodeObject the tree node object
//	 * @return the tree path
//	 */
//	public static TreePath getTreePathOfTreeNodeObject(JTree tree, TreeNodeObject nodeObject) {
//		DefaultMutableTreeNode root = ((DefaultMutableTreeNode) tree.getModel().getRoot())
//		for (int i=0; i<((DefaultMutableTreeNode) tree.getModel().getRoot()).getChildCount(); i++) {
//		
//		((DefaultMutableTreeNode) tree.getModel().getRoot()).children()
//		
//	}	
//	
	
	/**
	 * Get the difference group of difference.
	 * 
	 * @param difference the difference
	 * @param differenceModel the difference model
	 * @return the difference group
	 */
	public static DifferenceGroup getDifferenceGroupOfDifference(Difference difference, DifferenceModel differenceModel) {
		
		for (DifferenceGroup differenceGroup : differenceModel.getDifferenceGroups().values()) {
			if (differenceGroup.getDifferences().containsKey(tripleToString(difference.getTriple()))) {
				return differenceGroup;
			}
		}
				
		return null;
		
	}


	/**
	 * Create the row data for resolution triples.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @throws IOException 
	 */
	public static Object[] createRowDataResolutionTriples(Difference difference, DifferenceGroup differenceGroup) throws IOException {
		Object[] rowData = new Object[6];
		rowData[0] = "<" + difference.getTriple().getSubject() + ">";
		rowData[1] = "<" + difference.getTriple().getPredicate() + ">";
		if (difference.getTriple().getObjectType().equals(TripleObjectTypeEnum.LITERAL)) {
			rowData[2] = "\"" + difference.getTriple().getObject() + "\"";
		} else {
			rowData[2] = "<" + difference.getTriple().getObject() + ">";
		}
			
		// Get the revision number if available
		if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() == null)) {
			rowData[3] = differenceGroup.getTripleStateA() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB();
		} else if ((difference.getReferencedRevisionA() == null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA();
			rowData[4] = differenceGroup.getTripleStateB() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else {
			rowData[3] = differenceGroup.getTripleStateA();
			rowData[4] = differenceGroup.getTripleStateB();
		}

		rowData[5] = difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED);
		
		return rowData;
		
	}
	
	
	/**
	 * Get the dot graph.
	 * 
	 * @param graphName the graph name 
	 * @return the dot graph
	 * @throws IOException
	 * @throws HttpException
	 */
	public static Graph getDotGraph(String graphName) throws IOException, HttpException {
		// Create a new graph
		Graph graph =  new Graph("Revision graph of " + graphName);
		graph.setAttribute(Attribute.RANKDIR_ATTR, "LR");
		
		String query_branches = prefixes + String.format(
				  "SELECT ?branchURI ?branch %n"
				+ "FROM <%s> %n"
				+ "WHERE { %n"
				+ "	?branchURI a rmo:Branch ; %n"
				+ "		rdfs:label ?branch ; %n"
				+ "		rmo:references ?revisionURI . %n"
				+ "	?revisionURI a rmo:Revision ; %n"
				+ "		rmo:revisionOf <%s> . %n"
				+ "}", Config.r43ples_revision_graph, graphName);
		
		String result_branches = TripleStoreInterface.executeQueryWithoutAuthorization(query_branches, "text/xml");
		ResultSet resultSet_brances = ResultSetFactory.fromXML(result_branches);
		while (resultSet_brances.hasNext()) {
			QuerySolution qs_branches = resultSet_brances.next();
			String branchURI = qs_branches.getResource("branchURI").toString();
			String branchName = qs_branches.getLiteral("branch").toString();
			
			// Create new sub graph
			Subgraph subGraph = new Subgraph(graph, "Branch: " + branchName);
			
			// Get all nodes (revisions)
			String query_nodes = prefixes + String.format(
					  "SELECT DISTINCT ?revision ?number %n"
					+ "FROM <%s> %n"
					+ "WHERE { %n"
					+ " ?revision a rmo:Revision ; %n"
					+ "		rmo:revisionOf <%s> ; %n"
					+ "		rmo:revisionOfBranch <%s> ; %n"
					+ "		rmo:revisionNumber ?number . %n"
					+ "}", Config.r43ples_revision_graph, graphName, branchURI);
			
			String result_nodes = TripleStoreInterface.executeQueryWithoutAuthorization(query_nodes, "text/xml");
			ResultSet resultSet_nodes = ResultSetFactory.fromXML(result_nodes);
			while (resultSet_nodes.hasNext()) {
				QuerySolution qs_nodes = resultSet_nodes.next();
				String rev = qs_nodes.getResource("revision").toString();
				String name = qs_nodes.getLiteral("number").toString();
				Node newNode = new Node(graph, rev);

				// Check if revision is referenced
				String query_reference = prefixes + String.format(
						  "SELECT ?label %n"
						+ "FROM <%s> %n"
						+ "WHERE { %n"
						+ "	?reference a rmo:Reference ; %n"
						+ "		rmo:references <%s> ; %n"
						+ "		rdfs:label ?label . %n"
						+ "}", Config.r43ples_revision_graph, rev);
				
				String result_reference = TripleStoreInterface.executeQueryWithoutAuthorization(query_reference, "text/xml");
				ResultSet resultSet_reference = ResultSetFactory.fromXML(result_reference);
				if (resultSet_reference.hasNext()) {
					QuerySolution qs = resultSet_reference.next();
					String reference = qs.getLiteral("label").toString();
					name = name + " | " + reference;					
				}
				
				newNode.setAttribute(Attribute.LABEL_ATTR, name);
				newNode.setAttribute(Attribute.SHAPE_ATTR, Attribute.RECORD_SHAPE);
				subGraph.addNode(newNode);
				
			}
		}
		
		// Create the directed edges between the revisions
		String query_edges = prefixes + String.format(""
				+ "SELECT DISTINCT ?revision ?next_revision "
				+ "FROM <%s> "
				+ "WHERE {"
				+ " ?revision a rmo:Revision;"
				+ "		rmo:revisionOf <%s>."
				+ "	?next_revision a rmo:Revision;"
				+ "		prov:wasDerivedFrom ?revision."
				+ "}", Config.r43ples_revision_graph, graphName);
		
		String result_edges = TripleStoreInterface.executeQueryWithoutAuthorization(query_edges, "text/xml");
		ResultSet resultSet_edges = ResultSetFactory.fromXML(result_edges);
		while (resultSet_edges.hasNext()) {
			QuerySolution qs_edges = resultSet_edges.next();
			String rev = qs_edges.getResource("revision").toString();
			String next = qs_edges.getResource("next_revision").toString();
			Node newNode = graph.findNodeByName(rev);
			Node nextNode = graph.findNodeByName(next);
			graph.addEdge(new Edge(graph, newNode, nextNode));
		}

		return graph;
	}
	
	
	/**
	 * Highlight a node in the specified graph.
	 * 
	 * @param graph the graph which contains the nodes
	 * @param nodeName the name of the node to highlight (revision URI)
	 * @param color the highlight color
	 * @return true when node was highlighted elsewhere false when the graph does not contain the specified node
	 */
	public static boolean highlightNode(Graph graph, String nodeName, Color color) {
		if (graph.findNodeByName(nodeName) != null) {		
			graph.findNodeByName(nodeName).setAttribute(Attribute.STYLE_ATTR, "filled");
			graph.findNodeByName(nodeName).setAttribute(Attribute.COLOR_ATTR, color);	
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Remove the highlighting of a node in the specified graph.
	 * 
	 * @param graph the graph which contains the nodes
	 * @param nodeName the name of the node where the highlighting should be removed (revision URI)
	 * @return true when the highlighting of the node was removed elsewhere false when the graph does not contain the specified graph
	 */
	public static boolean removeHighlighting(Graph graph, String nodeName) {
		if (graph.findNodeByName(nodeName) != null) {		
			graph.findNodeByName(nodeName).setAttribute(Attribute.STYLE_ATTR, "filled");
			graph.findNodeByName(nodeName).setAttribute(Attribute.COLOR_ATTR, Color.WHITE);	
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Create the row data for summary report.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 */
	public static Object[] createRowDataSummaryReport(Difference difference, DifferenceGroup differenceGroup) {
		Object[] rowData = new Object[9];
		rowData[0] = "<" + difference.getTriple().getSubject() + ">";
		rowData[1] = "<" + difference.getTriple().getPredicate() + ">";
		if (difference.getTriple().getObjectType().equals(TripleObjectTypeEnum.LITERAL)) {
			rowData[2] = "\"" + difference.getTriple().getObject() + "\"";
		} else {
			rowData[2] = "<" + difference.getTriple().getObject() + ">";
		}
			
		// Get the revision number if available
		if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() == null)) {
			rowData[3] = differenceGroup.getTripleStateA() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB();
		} else if ((difference.getReferencedRevisionA() == null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA();
			rowData[4] = differenceGroup.getTripleStateB() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else {
			rowData[3] = differenceGroup.getTripleStateA();
			rowData[4] = differenceGroup.getTripleStateB();
		}

		rowData[5] = Boolean.toString(differenceGroup.isConflicting()).toUpperCase();
		
		rowData[6] = differenceGroup.getAutomaticResolutionState().toString().toUpperCase();
		
		rowData[7] = difference.getTripleResolutionState().toString().toUpperCase();
		
		rowData[8] = difference.getResolutionState().toString().toUpperCase();
		
		return rowData;

	}
	
	
	
	/**
	 * Create the summary report table content.
	 * 
	 * @param table the table reference
	 * @param differenceModel the difference model
	 */
	public static void createReportTable(JTable table, DifferenceModel differenceModel) {
		
		// Remove all entries
		((TableModelSummaryReport) table.getModel()).removeAllElements();
		
		// Differ between conflicting and non conflicting difference groups
		ArrayList<DifferenceGroup> conflictingDifferenceGroups = new ArrayList<DifferenceGroup>();
		ArrayList<DifferenceGroup> nonconflictingDifferenceGroups = new ArrayList<DifferenceGroup>();
		
		Iterator<String> iteDifferenceGroupNames = differenceModel.getDifferenceGroups().keySet().iterator();
		while (iteDifferenceGroupNames.hasNext()) {
			String differenceGroupName = iteDifferenceGroupNames.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(differenceGroupName);
			if (differenceGroup.isConflicting()) {
				conflictingDifferenceGroups.add(differenceGroup);
			} else {
				nonconflictingDifferenceGroups.add(differenceGroup);
			}
		}
		
		// Check if all conflicts were approved
		Iterator<DifferenceGroup> iteConflictingDifferenceGroups = conflictingDifferenceGroups.iterator();
		while (iteConflictingDifferenceGroups.hasNext()) {
			DifferenceGroup differenceGroup = iteConflictingDifferenceGroups.next();
			
			Iterator<String> iteDifferenceNames = differenceGroup.getDifferences().keySet().iterator();
			while (iteDifferenceNames.hasNext()) {
				String currentDifferenceName = iteDifferenceNames.next();
				Difference difference = differenceGroup.getDifferences().get(currentDifferenceName);
				
				Color color = Color.GREEN;
				if (!difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
					color = Color.RED;
				}
				
				// Create new table entry
				((TableModelSummaryReport) table.getModel()).addRow(new TableEntrySummaryReport(difference, color, createRowDataSummaryReport(difference, differenceGroup)));

			}
		}
		
		// Check if the resolution state of differences was changed manually
		
				
				
	}
	
	
}
