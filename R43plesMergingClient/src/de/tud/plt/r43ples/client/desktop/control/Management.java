package de.tud.plt.r43ples.client.desktop.control;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

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
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n";
	
	
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
	
}
