package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
import de.tud.plt.r43ples.client.desktop.model.structure.IndividualModel;
import de.tud.plt.r43ples.client.desktop.model.structure.IndividualStructure;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HighLevelChangeModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HighLevelChangeRenaming;
import de.tud.plt.r43ples.client.desktop.model.structure.HttpResponse;
import de.tud.plt.r43ples.client.desktop.model.structure.ReportResult;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.structure.TripleIndividualStructure;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSummaryReport;
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
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## General.                                                                                                                                                             ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Execute each kind of MERGE query.
	 * 
	 * @param graphName the graph name
	 * @param sdd the SDD
	 * @param user the user
	 * @param commitMessage the commit message
	 * @param type the merge query type
	 * @param branchNameA the branch name A
	 * @param branchNameB the branch name B
	 * @param triples the triples which should be in the WITH part as N-Triples
	 * @param differenceModel the difference model for storing the java representation
	 * @return the query HTTP response
	 * @throws IOException 
	 */
	public static HttpResponse executeMergeQuery(String graphName, String sdd, String user, String commitMessage, MergeQueryTypeEnum type, String branchNameA, String branchNameB, String triples, DifferenceModel differenceModel) throws IOException {
		logger.info("Execute merge query of type " + type.toString());
		
		String queryTemplateCommon = 
				  "USER \"%s\" %n"
				+ "MESSAGE \"%s\" %n"
				+ "MERGE GRAPH <%s> SDD <%s> BRANCH \"%s\" INTO \"%s\"";
		
		String queryTemplateWith = 
				  "USER \"%s\" %n"
				+ "MESSAGE \"%s\" %n"
				+ "MERGE GRAPH <%s> SDD <%s> BRANCH \"%s\" INTO \"%s\" WITH { %n"
				+ "	%s"
				+ "}";
		
		String queryTemplateAuto = 
				  "USER \"%s\" %n"
				+ "MESSAGE \"%s\" %n"
				+ "MERGE AUTO GRAPH <%s> SDD <%s> BRANCH \"%s\" INTO \"%s\"";
		
		String queryTemplateManual = 
				  "USER \"%s\" %n"
				+ "MESSAGE \"%s\" %n"
				+ "MERGE MANUAL GRAPH <%s> SDD <%s> BRANCH \"%s\" INTO \"%s\" WITH { %n"
				+ "	%s"
				+ "}";

		if (type.equals(MergeQueryTypeEnum.COMMON)) {
			String query = String.format(queryTemplateCommon, user, commitMessage, graphName, sdd, branchNameA, branchNameB);
			return TripleStoreInterface.executeQueryWithoutAuthorizationPostResponse(query, "HTML");
		} else if (type.equals(MergeQueryTypeEnum.WITH)) {
			String query = String.format(queryTemplateWith, user, commitMessage, graphName, sdd, branchNameA, branchNameB, triples);
			return TripleStoreInterface.executeQueryWithoutAuthorizationPostResponse(query, "HTML");
		} else if (type.equals(MergeQueryTypeEnum.AUTO)) {
			String query = String.format(queryTemplateAuto, user, commitMessage, graphName, sdd, branchNameA, branchNameB);
			return TripleStoreInterface.executeQueryWithoutAuthorizationPostResponse(query, "HTML");
		} else if (type.equals(MergeQueryTypeEnum.MANUAL)) {
			String query = String.format(queryTemplateManual, user, commitMessage, graphName, sdd, branchNameA, branchNameB, triples);
			return TripleStoreInterface.executeQueryWithoutAuthorizationPostResponse(query, "HTML");
		}
		
		return null;
	}

	
	/**
	 * Read difference model to java representation.
	 * 
	 * @param differenceModelToRead the difference model to read
	 * @param differenceModel the difference model where the result should be stored
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
	 * Write jena model to N-Triples string.
	 * 
	 * @param model the model
	 * @return the N-Triples string
	 */
	public static String writeJenaModelToNTriplesString(Model model) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		model.write(stream, "N-TRIPLES");
		
		return stream.toString();
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
			logger.debug(String.format("<%s> %s \"%s\" .", triple.getSubject(), getPredicate(triple), triple.getObject()));
			return String.format("<%s> %s \"%s\" .", triple.getSubject(), getPredicate(triple), triple.getObject());
		} else {
			logger.debug(String.format("<%s> %s <%s> .", triple.getSubject(), getPredicate(triple), triple.getObject()));
			return String.format("<%s> %s <%s> .", triple.getSubject(), getPredicate(triple), triple.getObject());
		}
	}
	
	
	/**
	 * Get the subject of triple.
	 * 
	 * @param triple the triple
	 * @return the formatted subject
	 */
	public static String getSubject(Triple triple) {
		return "<" + triple.getSubject() + ">";
	}

	
	/**
	 * Get the predicate of triple. If predicate equals rdf:type 'a' will be returned.
	 * 
	 * @param triple the triple
	 * @return the formatted predicate
	 */
	public static String getPredicate(Triple triple) {
		if (triple.getPredicate().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
			return "a";
		} else {
			return "<" + triple.getPredicate() + ">";
		}
	}
	
	
	/**
	 * Get the object of triple.
	 * 
	 * @param triple the triple
	 * @return the formatted object
	 */
	public static String getObject(Triple triple) {
		if (triple.getObjectType().equals(TripleObjectTypeEnum.LITERAL)) {
			if (triple.getObject().contains("@")) {
				return "\"" + triple.getObject().substring(0, triple.getObject().lastIndexOf("@")) + "\"@" + triple.getObject().substring(triple.getObject().lastIndexOf("@") + 1, triple.getObject().length());
			} else {
				return "\"" + triple.getObject() + "\"";
			}
		} else {
			return "<" + triple.getObject() + ">";		
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
		
		// Sort by key
		Map<String, DifferenceGroup> sortedMap = new TreeMap<String, DifferenceGroup>(differenceModel.getDifferenceGroups());
		
		Iterator<String> iteDifferenceGroups = sortedMap.keySet().iterator();
		
		while (iteDifferenceGroups.hasNext()) {
			String currentkey = iteDifferenceGroups.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(currentkey);
			
			if (differenceGroup.isConflicting()) {
				logger.debug(differenceGroup.getTripleStateA().toString() + "-" + differenceGroup.getTripleStateB().toString());
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
		
		// Sort by key
		Map<String, DifferenceGroup> sortedMap = new TreeMap<String, DifferenceGroup>(differenceModel.getDifferenceGroups());
		
		Iterator<String> iteDifferenceGroups = sortedMap.keySet().iterator();
		
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
	 * Get the triples of the MERGE WITH query.
	 * 
	 * @param differenceModel the difference model
	 * @return the triples of the MERGE WITH query
	 */
	public static String getTriplesOfMergeWithQuery(DifferenceModel differenceModel) {
		
		// Contains all triples to add
		String triples = "";
		
		// Iterate over all difference groups
		Iterator<String> iteDifferenceGroupNames = differenceModel.getDifferenceGroups().keySet().iterator();
		while (iteDifferenceGroupNames.hasNext()) {
			String differenceGroupName = iteDifferenceGroupNames.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(differenceGroupName);
			if (differenceGroup.isConflicting()) {
				// Iterate over all difference of current conflicting difference group
				Iterator<String> iteDifferenceNames = differenceGroup.getDifferences().keySet().iterator();
				while (iteDifferenceNames.hasNext()) {
					String currentDifferenceName = iteDifferenceNames.next();
					Difference difference = differenceGroup.getDifferences().get(currentDifferenceName);

					if (difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED)) {
						String triple = tripleToString(difference.getTriple());						
						triples += triple + "\n";
					}
				}
			}
		}

		return triples;
	}
	
	
	/**
	 * Get the whole content of a revision as jena model.
	 * 
	 * @param graphName the graph name
	 * @param revision the revision
	 * @return the whole content of the revision of the graph as jena model
	 * @throws IOException 
	 */
	public static Model getWholeContentOfRevision(String graphName, String revision) throws IOException {
		String query = String.format(
				  "CONSTRUCT {?s ?p ?o} %n"
				+ "FROM <%s> REVISION \"%s\" %n"
				+ "WHERE { %n"
				+ "	?s ?p ?o %n"
				+ "}", graphName, revision);
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		logger.debug(result);

		return readTurtleStringToJenaModel(result);
	}
	
	
	/**
	 * Get all triples divided into insert and delete.
	 * 
	 * @param differenceModel the difference model
	 * @return array list which has two entries (entry 0 contains the triples to insert; entry 1 contains the triples to delete)
	 */
	public static ArrayList<String> getAllTriplesDividedIntoInsertAndDelete(DifferenceModel differenceModel, Model model) {
		// The result list
		ArrayList<String> list = new ArrayList<String>();
		
		String triplesToInsert = "";
		String triplesToDelete = "";
		
		// Iterate over all difference groups
		Iterator<String> iteDifferenceGroups = differenceModel.getDifferenceGroups().keySet().iterator();
		while (iteDifferenceGroups.hasNext()) {
			String differenceGroupKey = iteDifferenceGroups.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(differenceGroupKey);
			
			Iterator<String> iteDifferences = differenceGroup.getDifferences().keySet().iterator();
			while (iteDifferences.hasNext()) {
				String differenceKey = iteDifferences.next();
				Difference difference = differenceGroup.getDifferences().get(differenceKey);
				
				// Get the triple state to use
				SDDTripleStateEnum tripleState;
				if (difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
					// Use the approved triple state
					tripleState = difference.getTripleResolutionState();					
				} else {
					// Use the automatic resolution state
					tripleState = differenceGroup.getAutomaticResolutionState();
				}
				
				// Get the triple
				String triple = tripleToString( difference.getTriple());					
				
				// Add the triple to the corresponding string
				if (tripleState.equals(SDDTripleStateEnum.ADDED) || tripleState.equals(SDDTripleStateEnum.ORIGINAL)) {
					triplesToInsert += triple + "%n";				
				} else if (tripleState.equals(SDDTripleStateEnum.DELETED) || tripleState.equals(SDDTripleStateEnum.NOTINCLUDED)) {
					triplesToDelete += triple + "%n";
				} else {
					// Error occurred - state was not changed
					logger.error("Triple state was used which has no internal representation.");
				}
			}
		}
		
		// Add the string to the result list
		list.add(String.format(triplesToInsert));
		list.add(String.format(triplesToDelete));
		
		return list;
	}	
	
	
	/**
	 * Get the header graph name.
	 * Encodes the http:// of the graph name because it is not permitted that a header parameter contains a colon.
	 * 
	 * @param graphName the graph name
	 * @return the encoded header graph name
	 * @throws UnsupportedEncodingException 
	 */
	public static String getHeaderGraphName(String graphName) throws UnsupportedEncodingException {
		return URLEncoder.encode(graphName, "UTF-8");
	}

	
	/**
	 * Get the revision number of the new revision of the HTTP response.
	 * 
	 * @param response the HTTP response
	 * @param graphName the graph name
	 * @return the new revision number
	 */
	public static String getRevisionNumberOfNewRevisionHeaderParameter(HttpResponse response, String graphName) throws UnsupportedEncodingException {
		String identifier = "%s-revision-number";
		String graphNameHeader = getHeaderGraphName(graphName);
		
		return response.getHeaderParameterByName(String.format(identifier, graphNameHeader)).get(0);
	}
	

	/**
	 * Get the revision number of MASTER of the HTTP response.
	 * 
	 * @param response the HTTP response
	 * @param graphName the graph name
	 * @return the MASTER revision number
	 */
	public static String getRevisionNumberOfMasterHeaderParameter(HttpResponse response, String graphName) throws UnsupportedEncodingException {
		String identifier = "%s-revision-number-of-master";
		String graphNameHeader = getHeaderGraphName(graphName);
		
		return response.getHeaderParameterByName(String.format(identifier, graphNameHeader)).get(0);
	}
	
	
	/**
	 * Get the revision number of the branch A of the HTTP response.
	 * Only available after execution of a MERGE query.
	 * 
	 * @param response the HTTP response
	 * @param graphName the graph name
	 * @return the branch A revision number
	 */
	public static String getRevisionNumberOfBranchAHeaderParameter(HttpResponse response, String graphName) throws UnsupportedEncodingException {
		String identifier = "%s-revision-number-of-branch-A";
		String graphNameHeader = getHeaderGraphName(graphName);

		return response.getHeaderParameterByName(String.format(identifier, graphNameHeader)).get(0);
	}
	
	
	/**
	 * Get the revision number of the branch B of the HTTP response.
	 * Only available after execution of a MERGE query.
	 * 
	 * @param response the HTTP response
	 * @param graphName the graph name
	 * @return the branch B revision number
	 */
	public static String getRevisionNumberOfBranchBHeaderParameter(HttpResponse response, String graphName) throws UnsupportedEncodingException {
		String identifier = "%s-revision-number-of-branch-B";
		String graphNameHeader = getHeaderGraphName(graphName);
		
		return response.getHeaderParameterByName(String.format(identifier, graphNameHeader)).get(0);
	}
	
	
	/**
	 * Sort the tree node object - tree path map by tree path.
	 * 
	 * @param map the map to sort
	 * @return the sorted map
	 */
	public static HashMap<TreeNodeObject, TreePath> sortMapByValue(Map<TreeNodeObject, TreePath> map) {
		List<Map.Entry<TreeNodeObject, TreePath>> list = new LinkedList<Map.Entry<TreeNodeObject, TreePath>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<TreeNodeObject, TreePath>>() {
			
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Map.Entry<TreeNodeObject, TreePath> o1, Map.Entry<TreeNodeObject, TreePath> o2) {
				return (treePathToString(o1.getValue())).compareTo(treePathToString(o2.getValue()));
			}
		});
		
		HashMap<TreeNodeObject, TreePath> result = new LinkedHashMap<TreeNodeObject, TreePath>();
		for (Map.Entry<TreeNodeObject, TreePath> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}
	
	
	/**
	 * Get the string representation of a tree path.
	 * 
	 * @param treePath the tree path
	 * @return the string representation
	 */
	public static String treePathToString(TreePath treePath) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < treePath.getPath().length; i++) {
			TreeNodeObject treeNodeObject = (TreeNodeObject) ((DefaultMutableTreeNode) treePath.getPathComponent(i)).getUserObject();
			sb.append("|").append(treeNodeObject.getText());
		}
		return sb.toString();
	}
	
	
	/**
	 * Get difference by triple. If the difference model does not contain the triple null will be returned.
	 * 
	 * @param triple the triple to look for
	 * @param differenceModel the difference model
	 * @return the difference or null if triple is not included in difference model
	 */
	public static Difference getDifferenceByTriple(Triple triple, DifferenceModel differenceModel) {
		String tripleIdentifier = tripleToString(triple);
		
		// Iterate over all difference groups
		Iterator<String> iteDifferenceGroupNames = differenceModel.getDifferenceGroups().keySet().iterator();
		while (iteDifferenceGroupNames.hasNext()) {
			String differenceGroupName = iteDifferenceGroupNames.next();
			DifferenceGroup differenceGroup = differenceModel.getDifferenceGroups().get(differenceGroupName);
			// Check if the hash map contains the triple
			if (differenceGroup.getDifferences().containsKey(tripleIdentifier)) {
				return differenceGroup.getDifferences().get(tripleIdentifier);
			};
		}

		return null;
	}
	
	
	/**
	 * Converts a triple string to a string in which URIs are replaced by prefixes which were specified in the configuration.
	 * If no prefix was found or if input string is a literal the input string will be returned.
	 * 
	 * @param tripleString the triple string (subject or predicate or object) to convert
	 * @return the converted triple string or input string
	 */
	public static String convertTripleStringToPrefixTripleString(String tripleString) {
		if (tripleString.contains("<") && tripleString.contains(">")) {
			String tripleStringConverted = tripleString.trim().replaceAll("<", "").replaceAll(">", "");
			int lastIndexSlash = tripleStringConverted.lastIndexOf("/");
			int lastIndexHash = tripleStringConverted.lastIndexOf("#");
			if ((lastIndexSlash == -1) && (lastIndexHash == -1)) {
				return tripleString;
			} else {
				int index = 0;
				if (lastIndexSlash > lastIndexHash) {
					// Slash separator found
					index = lastIndexSlash + 1;
				} else {
					// Hash separator found
					index = lastIndexHash + 1;
				}
				String subString = tripleStringConverted.substring(0, index);
				// Try to find the prefix
				if (Config.prefixMappings.containsKey(subString)) {
					return Config.prefixMappings.get(subString) + ":" + tripleStringConverted.substring(index, tripleStringConverted.length());
				}
			}
		}
		return tripleString;
	}
	
	
	/**
	 * Get all properties of specified revision.
	 * 
	 * @param graphName the graph name
	 * @param branchNameA the branch name A
	 * @param branchNameB the branch name B
	 * @return the array list of property URIs
	 * @throws IOException 
	 */
	public static ArrayList<String> getPropertiesOfRevision(String graphName, String branchNameA, String branchNameB) throws IOException {
		logger.info("Get all properties of branches.");
		
		// Result array list
		ArrayList<String> list = new ArrayList<String>();

    	// Query all properties (DISTINCT because there can be multiple property occurrences)
		String query = String.format(
				  "OPTION r43ples:SPARQL_JOIN %n"
				+ "SELECT DISTINCT ?propertyUri %n"
				+ "FROM <%s> REVISION \"%s\" %n"
				+ "FROM <%s> REVISION \"%s\" %n"
				+ "WHERE { %n"
				+ "	?subject ?propertyUri ?object . %n"
				+ "} %n"
				+ "ORDER BY ?propertyUri", graphName, branchNameA, graphName, branchNameB);
		logger.debug(query);
		
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		logger.debug(result);
		
		// Iterate over all properties
		ResultSet resultSet = ResultSetFactory.fromXML(result);
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			list.add(qs.getResource("?propertyUri").toString());
		}

		return list;
	}
	
	
	/**
	 * Get the revision number of a given reference name.
	 * 
	 * @param graphName the graph name
	 * @param referenceName the reference name
	 * @return the revision number of given reference name
	 * @throws IOException
	 */
	public static String getRevisionNumber(final String graphName, final String referenceName) throws IOException {
		String query = prefixes
				+ String.format(
						"SELECT ?revNumber WHERE { GRAPH <%s> {"
								+ "	?rev a rmo:Revision; rmo:revisionNumber ?revNumber; rmo:revisionOf <%s>."
								+ "	{?rev rmo:revisionNumber \"%s\".} UNION {?ref a rmo:Reference; rmo:references ?rev; rdfs:label \"%s\".}"
								+ "} }", Config.r43ples_revision_graph, graphName, referenceName, referenceName);
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		ResultSet resultSet = ResultSetFactory.fromXML(result);
		if (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			return qs.getLiteral("?revNumber").toString();
		}
		return referenceName;
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Start merging dialog.                                                                                                                                                ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
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
		String revisedGraphs = TripleStoreInterface.executeHttpGet(Config.r43ples_json_revisedgraphs);
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
	 * Get all SDDs.
	 * 
	 * @return the array list of all SDDs
	 * @throws IOException 
	 */
	public static ArrayList<String> getAllSDDs() throws IOException {
		logger.info("Get all SDDs.");
		
		// Result array list
		ArrayList<String> list = new ArrayList<String>();
		
    	// Query all differences
		String query = prefixes + String.format(
				  "SELECT ?sddUri %n"
				+ "FROM <%s>"
				+ "WHERE { %n"
				+ "	?sddUri a sddo:StructuralDefinitionGroup . %n"
				+ "}", Config.r43ples_sdd_graph);
		logger.debug(query);
		
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		logger.debug(result);
		
		// Iterate over all SDDs
		ResultSet resultSet = ResultSetFactory.fromXML(result);
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			list.add(qs.getResource("?sddUri").toString());
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
					+ "} %n"
					+ "ORDER BY ?label", Config.r43ples_revision_graph, graphName);
	
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
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Graph.                                                                                                                                                     ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */	
	
	
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
		if (graph != null) {
			if (graph.findNodeByName(nodeName) != null) {		
				graph.findNodeByName(nodeName).setAttribute(Attribute.STYLE_ATTR, "filled");
				graph.findNodeByName(nodeName).setAttribute(Attribute.COLOR_ATTR, color);	
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Remove the highlighting of a node in the specified graph.
	 * 
	 * @param graph the graph which contains the nodes
	 * @param nodeName the name of the node where the highlighting should be removed (revision URI)
	 * @return true when the highlighting of the node was removed elsewhere false when the graph does not contain the specified graph
	 */
	public static boolean removeHighlighting(Graph graph, String nodeName) {
		if (graph != null) {
			if (graph.findNodeByName(nodeName) != null) {		
				graph.findNodeByName(nodeName).setAttribute(Attribute.STYLE_ATTR, "filled");
				graph.findNodeByName(nodeName).setAttribute(Attribute.COLOR_ATTR, Color.WHITE);	
				return true;
			} 
		}
		return false;
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Difference tree.                                                                                                                                                     ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */	
	
	
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
			
			DefaultMutableTreeNode differenceGroupNode = new DefaultMutableTreeNode(new TreeNodeObject(differenceGroup.getTripleStateA().toString() + "-" + differenceGroup.getTripleStateB().toString(), ResolutionState.CONFLICT, differenceGroup));
			
			// Add all differences
			// Sort by key
			Map<String, Difference> sortedMap = new TreeMap<String, Difference>(differenceGroup.getDifferences());
			Iterator<Difference> iteDifferences = sortedMap.values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.CONFLICT, difference));
				// Check filter
				if (Controller.getActivatedFilters().contains(difference.getTriple().getPredicate())) {
					differenceGroupNode.add(differenceNode);
				}
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
			// Sort by key
			Map<String, Difference> sortedMap = new TreeMap<String, Difference>(differenceGroup.getDifferences());
			Iterator<Difference> iteDifferences = sortedMap.values().iterator();
			while (iteDifferences.hasNext()) {
				Difference difference = iteDifferences.next();
				
				DefaultMutableTreeNode differenceNode = new DefaultMutableTreeNode(new TreeNodeObject(tripleToString(difference.getTriple()), ResolutionState.DIFFERENCE, difference));
				// Check filter
				if (Controller.getActivatedFilters().contains(difference.getTriple().getPredicate())) {
					differenceGroupNode.add(differenceNode);
				}
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
		if (rootNode != null) {
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
		
		return null;
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Resolution triples.                                                                                                                                                  ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */	


	/**
	 * Create the row data for resolution triples.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @return the row data
	 * @throws IOException 
	 */
	public static Object[] createRowDataResolutionTriples(Difference difference, DifferenceGroup differenceGroup) throws IOException {
		Object[] rowData = new Object[7];
		
		rowData[0] = getSubject(difference.getTriple());
		rowData[1] = getPredicate(difference.getTriple());
		rowData[2] = getObject(difference.getTriple());
		
		// Get the revision number if available
		if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() == null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString();
		} else if ((difference.getReferencedRevisionA() == null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString();
		}
		
		rowData[5] = Boolean.toString(differenceGroup.isConflicting()).toUpperCase();

		rowData[6] = difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED);
		
		return rowData;
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Summary report.                                                                                                                                                      ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */	
		
	
	/**
	 * Create the summary report table content.
	 * 
	 * @param table the table reference
	 * @param differenceModel the difference model
	 * @return the report creation result
	 */
	public static ReportResult createReportTable(JTable table, DifferenceModel differenceModel) {
		
		ReportResult result = new ReportResult();
		
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
				
				Color color = ColorDefinitions.approvedRowColor;
				if (!difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
					color = ColorDefinitions.conflictingRowColor;
					result.incrementCounterConflictsNotApproved();
				}
				
				// Create new table entry
				((TableModelSummaryReport) table.getModel()).addRow(new TableEntrySummaryReport(difference, color, createRowDataSummaryReport(difference, differenceGroup)));

			}
		}
		
		// Check if the resolution state of differences was changed manually
		Iterator<DifferenceGroup> iteNonConflictingDifferenceGroups = nonconflictingDifferenceGroups.iterator();
		while (iteNonConflictingDifferenceGroups.hasNext()) {
			DifferenceGroup differenceGroup = iteNonConflictingDifferenceGroups.next();
			
			Iterator<String> iteDifferenceNames = differenceGroup.getDifferences().keySet().iterator();
			while (iteDifferenceNames.hasNext()) {
				String currentDifferenceName = iteDifferenceNames.next();
				Difference difference = differenceGroup.getDifferences().get(currentDifferenceName);
				
				Color color = ColorDefinitions.approvedRowColor;
				if (!difference.getTripleResolutionState().equals(differenceGroup.getAutomaticResolutionState())) {
					result.incrementCounterDifferencesResolutionChanged();
				}
				if (!difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
					color = ColorDefinitions.nonConflictingRowColor;
				}
				
				// Create new table entry
				((TableModelSummaryReport) table.getModel()).addRow(new TableEntrySummaryReport(difference, color, createRowDataSummaryReport(difference, differenceGroup)));

			}
		}
		
		return result;
	}
	
	
	/**
	 * Create the row data for summary report.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @return the row data
	 */
	public static Object[] createRowDataSummaryReport(Difference difference, DifferenceGroup differenceGroup) {
		Object[] rowData = new Object[9];
		
		rowData[0] = getSubject(difference.getTriple());
		rowData[1] = getPredicate(difference.getTriple());
		rowData[2] = getObject(difference.getTriple());
			
		// Get the revision number if available
		if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() == null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString();
		} else if ((difference.getReferencedRevisionA() == null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString();
		}

		rowData[5] = Boolean.toString(differenceGroup.isConflicting()).toUpperCase();
		
		rowData[6] = differenceGroup.getAutomaticResolutionState().toString().toUpperCase();
		
		rowData[7] = difference.getTripleResolutionState().toString().toUpperCase();
		
		rowData[8] = Boolean.toString(difference.getResolutionState().equals(ResolutionState.RESOLVED)).toUpperCase();
		
		return rowData;
	}

	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Semantic enrichment - individuals.                                                                                                                                   ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */	

	
	/**
	 * Get all individuals of specified revision.
	 * 
	 * @param graphName the graph name
	 * @param revisionName the revision name
	 * @return the array list of individual URIs
	 * @throws IOException 
	 */
	public static ArrayList<String> getAllIndividualsOfRevision(String graphName, String revisionName) throws IOException {
		logger.info("Get all individuals of revision.");
		
		// Result array list
		ArrayList<String> list = new ArrayList<String>();
		
    	// Query all individuals (DISTINCT because there can be multiple individual definitions)
		String query = prefixes + String.format(
				  "SELECT DISTINCT ?individualUri %n"
				+ "FROM <%s> REVISION \"%s\" %n"
				+ "WHERE { %n"
				+ "	?individualUri a ?class . %n"
				+ "} %n"
				+ "ORDER BY ?individualUri", graphName, revisionName);
		logger.debug(query);
		
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		logger.debug(result);
		
		// Iterate over all individuals
		ResultSet resultSet = ResultSetFactory.fromXML(result);
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			list.add(qs.getResource("?individualUri").toString());
		}

		return list;
	}
	
	
	/**
	 * Get all corresponding triples of specified individual.
	 * 
	 * @param graphName the graph name
	 * @param revisionName the revision name
	 * @param individualUri the individual URI
	 * @param differenceModel the difference model
	 * @return the hash map of triples
	 * @throws IOException 
	 */
	public static HashMap<String, TripleIndividualStructure> getAllTriplesOfIndividual(String graphName, String revisionName, String individualUri, DifferenceModel differenceModel) throws IOException {
		logger.info("Get all corresponding triples of specified individual.");
		
		// Result hash map
		HashMap<String, TripleIndividualStructure> list = new HashMap<String, TripleIndividualStructure>();
		
    	// Query all individuals
		String query = prefixes + String.format(
				  "SELECT ?predicate ?object %n"
				+ "FROM <%s> REVISION \"%s\" %n"
				+ "WHERE { %n"
				+ "	<%s> ?predicate ?object . %n"
				+ "}"
				+ "ORDER BY ?predicate ?object", graphName, revisionName, individualUri);
		logger.debug(query);
		
		String result = TripleStoreInterface.executeQueryWithoutAuthorization(query, "text/xml");
		logger.debug(result);
		
		// Iterate over all individuals
		ResultSet resultSet = ResultSetFactory.fromXML(result);
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			
	    	String predicate = qs.getResource("?predicate").toString();
    	
	    	// Differ between literal and resource
			String object = "";
			TripleObjectTypeEnum objectType = null;
			if (qs.get("?object").isLiteral()) {
				object = qs.getLiteral("?object").toString();
				objectType = TripleObjectTypeEnum.LITERAL;
			} else {
				object = qs.getResource("?object").toString();
				objectType = TripleObjectTypeEnum.RESOURCE;
			}
	    	// Create the triple
			Triple triple = new Triple(individualUri, predicate, object, objectType);
			// Check if there is a corresponding difference
			Difference difference = getDifferenceByTriple(triple, differenceModel);			
			// Create the triple individual structure
			TripleIndividualStructure tripleIndividualStructure = new TripleIndividualStructure(triple, difference);
			// Put the triple individual structure
	    	list.put(tripleToString(triple), tripleIndividualStructure);
		}

		return list;
	}
	
	
	/**
	 * Create the individual model of specified revision.
	 * 
	 * @param graphName the graph name
	 * @param revisionName the revision name
	 * @param differenceModel the difference model
	 * @return the individual model
	 * @throws IOException 
	 */
	public static IndividualModel createIndividualModelOfRevision(String graphName, String revisionName, DifferenceModel differenceModel) throws IOException {
		IndividualModel individualModel = new IndividualModel();
		
		ArrayList<String> individualURIs = getAllIndividualsOfRevision(graphName, revisionName);
		
		Iterator<String> iteIndividualURIs = individualURIs.iterator();
		while (iteIndividualURIs.hasNext()) {
			String currentIndividualUri = iteIndividualURIs.next();
			HashMap<String, TripleIndividualStructure> currentTriples = getAllTriplesOfIndividual(graphName, revisionName, currentIndividualUri, differenceModel);
			IndividualStructure currentIndividualStructure = new IndividualStructure(currentIndividualUri);
			currentIndividualStructure.setTriples(currentTriples);		
			individualModel.addIndividualStructure(currentIndividualUri, currentIndividualStructure);
		}
		
		return individualModel;
	}
	
	
	/**
	 * Get the highest resolution state of table entry of semantic enrichment all individuals table.
	 * 
	 * @param individualStructureA the individual structure A
	 * @param individualStructureB the individual structure B
	 * @return the highest resolution state
	 */
	public static ResolutionState getResolutionStateOfTableEntrySemanticEnrichmentAllIndividuals(IndividualStructure individualStructureA, IndividualStructure individualStructureB) {
		// The result resolution state
		ResolutionState resolutionState = ResolutionState.RESOLVED;
		// Check individual structure A
		Iterator<String> iteTriplesA = individualStructureA.getTriples().keySet().iterator();
		while (iteTriplesA.hasNext()) {
			String currentTripleKey = iteTriplesA.next();
			TripleIndividualStructure currentTripleIndividualStructure = individualStructureA.getTriples().get(currentTripleKey);
			Difference currentDifference = currentTripleIndividualStructure.getDifference();
			if (currentDifference != null) {
				if (currentDifference.getResolutionState().compareTo(resolutionState) > 0) {
					resolutionState = currentDifference.getResolutionState();
				}
			}
		}
		// Check individual structure B
		Iterator<String> iteTriplesB = individualStructureB.getTriples().keySet().iterator();
		while (iteTriplesB.hasNext()) {
			String currentTripleKey = iteTriplesB.next();
			TripleIndividualStructure currentTripleIndividualStructure = individualStructureB.getTriples().get(currentTripleKey);
			Difference currentDifference = currentTripleIndividualStructure.getDifference();
			if (currentDifference != null) {
				if (currentDifference.getResolutionState().compareTo(resolutionState) > 0) {
					resolutionState = currentDifference.getResolutionState();
				}
			}
		}
		
		return resolutionState;
	}

	
	/**
	 * Create the row data for semantic enrichment individual triples.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @return the row data
	 */
	public static Object[] createRowDataSemanticEnrichmentIndividualTriples(Difference difference, DifferenceGroup differenceGroup, String semanticDescription) {
		Object[] rowData = new Object[8];

		rowData[0] = getSubject(difference.getTriple());
		rowData[1] = getPredicate(difference.getTriple());
		rowData[2] = getObject(difference.getTriple());
		
		// Get the revision number if available
		if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() == null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString();
		} else if ((difference.getReferencedRevisionA() == null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else if ((difference.getReferencedRevisionA() != null) && (difference.getReferencedRevisionB() != null)) {
			rowData[3] = differenceGroup.getTripleStateA().toString() + " (" + difference.getReferencedRevisionLabelA() + ")";
			rowData[4] = differenceGroup.getTripleStateB().toString() + " (" + difference.getReferencedRevisionLabelB() + ")";
		} else {
			rowData[3] = differenceGroup.getTripleStateA().toString();
			rowData[4] = differenceGroup.getTripleStateB().toString();
		}

		rowData[5] = Boolean.toString(differenceGroup.isConflicting()).toUpperCase();

		rowData[6] = semanticDescription;
		// Will be a combo box - entries are generated by editor
		rowData[7] = null;
		
		return rowData;
	}
	
	
	/**
	 * Create the row data for semantic enrichment individual triples without difference.
	 * 
	 * @param triple the triple
	 * @return the row data
	 */
	public static Object[] createRowDataSemanticEnrichmentIndividualTriplesWithoutDifference(Triple triple) {
		Object[] rowData = new Object[8];

		rowData[0] = getSubject(triple);
		rowData[1] = getPredicate(triple);
		rowData[2] = getObject(triple);
		
		rowData[3] = "";
		rowData[4] = "";
		
		rowData[5] = "";

		rowData[6] = "";
		// Will be a combo box - entries are generated by editor
		rowData[7] = null;
		
		return rowData;
	}

	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## High level change generation.                                                                                                                                        ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Create the high level change renaming model.
	 * 
	 * @param highLevelChangeModel the high level change model
	 * @param differenceModel the difference model
	 */
	public static void createHighLevelChangeRenamingModel(HighLevelChangeModel highLevelChangeModel, DifferenceModel differenceModel) {
		// Clear high level change model
		highLevelChangeModel.clear();
		
		// Get all differences of state combination DELETED-ORIGINAL
		DifferenceGroup delOrig = differenceModel.getDifferenceGroups().get(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ORIGINAL);
		
		// Get all differences of state combination DELETED-ADDED
		DifferenceGroup delAdd = differenceModel.getDifferenceGroups().get(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED);
		
		// Get all differences of state combination ADDED-NOTINCLUDED
		DifferenceGroup addNotInc = differenceModel.getDifferenceGroups().get(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.NOTINCLUDED);

		if ((addNotInc != null) && ((delOrig != null) || (delAdd != null))) {
			// Get all possible prefixes
			HashMap<String, Difference> possiblePrefixes = getAllPrefixesOfDifferenceMap(addNotInc.getDifferences());
			// Iterate over all possible prefixes
			Iterator<String> itePossiblePrefixes = possiblePrefixes.keySet().iterator();
			while (itePossiblePrefixes.hasNext()) {
				String currentPrefix = itePossiblePrefixes.next();
				// Get possible mappings of DELETED-ORIGINAL map
				ArrayList<Difference> mappingsDelOrig = new ArrayList<Difference>();
				if (delOrig != null) {
					mappingsDelOrig = getAllDifferencesByPrefix(currentPrefix, delOrig.getDifferences());	
				}
				// Get possible mappings of DELETED-ADDED map
				ArrayList<Difference> mappingsDelAdd = new ArrayList<Difference>();
				if (delAdd != null) {
					mappingsDelAdd = getAllDifferencesByPrefix(currentPrefix, delAdd.getDifferences());
				}
				
				HighLevelChangeRenaming highLevelChangeRenaming = null;
				
				if ((mappingsDelOrig.size() == 1) && mappingsDelAdd.isEmpty()) {
					// Original found
					highLevelChangeRenaming = new HighLevelChangeRenaming(mappingsDelOrig.get(0), possiblePrefixes.get(currentPrefix));
				} else if (mappingsDelOrig.isEmpty() && (mappingsDelAdd.size() == 1)) {
					// Added found
					highLevelChangeRenaming = new HighLevelChangeRenaming(mappingsDelAdd.get(0), possiblePrefixes.get(currentPrefix));
				}	
	
				if (highLevelChangeRenaming != null) {
					highLevelChangeModel.addHighLevelChangeRenaming(tripleToString(highLevelChangeRenaming.getAdditionDifference().getTriple()), highLevelChangeRenaming);
				}		
			}
		}
	}

	
	/**
	 * Get all prefixes of difference map and corresponding difference. Prefix is equal to triple string which contains only subject and predicate.
	 * Object must be a literal and the difference should not be approved.
	 * 
	 * @param differenceMap the difference map
	 * @return return distinct map of prefix difference combinations
	 */
	public static HashMap<String, Difference> getAllPrefixesOfDifferenceMap(HashMap<String, Difference> differenceMap) {
		// Create the result array list
		HashMap<String, Difference> resultList = new HashMap<String, Difference>();
		
		// Iterate over all differences
		Iterator<String> iteDifferences = differenceMap.keySet().iterator();
		while (iteDifferences.hasNext()) {
			String currentKey = iteDifferences.next();
			Difference currentDifference = differenceMap.get(currentKey);
			Triple currentTriple = currentDifference.getTriple();
			String currentPrefix = "<" + currentTriple.getSubject() + "> <" + currentTriple.getPredicate() + "> ";
			if (!resultList.containsKey(currentPrefix) && currentTriple.getObjectType().equals(TripleObjectTypeEnum.LITERAL) && !currentDifference.getResolutionState().equals(ResolutionState.RESOLVED)) {
				resultList.put(currentPrefix, currentDifference);
			}
		}
		
		return resultList;
	}
	
	
	/**
	 * Get all differences by specified prefix.
	 * Object must be a literal and the difference should not be approved.
	 * 
	 * @param prefix the prefix
	 * @param differenceMap the difference map
	 * @return the differences which could be identified by specified prefix
	 */
	public static ArrayList<Difference> getAllDifferencesByPrefix(String prefix, HashMap<String, Difference> differenceMap) {
		// The result list
		ArrayList<Difference> result = new ArrayList<Difference>();
		// Tree map for sorting entries of hash map
		TreeMap<String, Difference> treeMap = new TreeMap<String, Difference>();
		treeMap.putAll(differenceMap);
		// Tail the tree map
		SortedMap<String, Difference> tailMap = treeMap.tailMap(prefix);
		if (!tailMap.isEmpty() && tailMap.firstKey().startsWith(prefix)) {
			Iterator<String> iteTailMap = tailMap.keySet().iterator();
			while (iteTailMap.hasNext()) {
				String currentKey = iteTailMap.next();
				if (currentKey.startsWith(prefix)) {
					Difference currentDifference = tailMap.get(currentKey);
					Triple currentTriple = currentDifference.getTriple();
					if (currentTriple.getObjectType().equals(TripleObjectTypeEnum.LITERAL) && !currentDifference.getResolutionState().equals(ResolutionState.RESOLVED)) {
						// Add corresponding difference to result list
						result.add(currentDifference);
					}
				} else {
					// Return the result map because there are no further keys which will start with the specified prefix
					return result;
				}
			}
		}
		
		return result;
	}


	/**
	 * Create the row data for resolution high level changes.
	 * 
	 * @param highLevelChangeRenaming the high level changes (renaming)
	 * @return the row data
	 */
	public static Object[] createRowDataResolutionHighLevelChanges(HighLevelChangeRenaming highLevelChangeRenaming) {
		Object[] rowData = new Object[5];
				
		rowData[0] = getSubject(highLevelChangeRenaming.getDeletionDifference().getTriple());
		rowData[1] = getPredicate(highLevelChangeRenaming.getDeletionDifference().getTriple());
		
		rowData[2] = getObject(highLevelChangeRenaming.getDeletionDifference().getTriple());
		rowData[3] = getObject(highLevelChangeRenaming.getAdditionDifference().getTriple());
		
		rowData[4] = highLevelChangeRenaming.getAdditionDifference().getTripleResolutionState().equals(SDDTripleStateEnum.ADDED);
		
		return rowData;
	}

}
