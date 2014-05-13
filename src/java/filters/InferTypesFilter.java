package filters;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import filter.Filter;

public class InferTypesFilter extends Filter {

	@Override
	public Result execute(Table table) {
		AutomaticTypeCompletion automaticTypeCompletion = new AutomaticTypeCompletion("http://dbpedia.org/sparql");
		
		if(table.getTriples().size()==0)
			return table;

		Date d0 = new Date();
		List<RDFTriple> inferredTriples = new LinkedList<RDFTriple>();
		Collection<RatedType> inferredTypes = automaticTypeCompletion.getAdditionalTypes(processedResource);
		for(RatedType RT : inferredTypes) {
			RDFTriple triple = new RDFTriple(processedResource, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", RT.getURI());
			triple.addMetaData("score", Float.toString(RT.getRating()));
			inferredTriples.add(triple);
		}
		Date d1 = new Date();
		float duration = (d1.getTime() - d0.getTime()) / 1000.0f;
		
		ResultComposite result = new ResultComposite();
		Table directTable = new Table(table.getTriples());
		directTable.setCaption("Direct Types");
		directTable.addTag("type");
		directTable.addTag("direct");
		result.add(directTable);
		
		Table inferredTable = new Table(inferredTriples);
		inferredTable.setCaption("Automatically Completed Types");
		inferredTable.addTag("type");
		inferredTable.addTag("inferred");
		result.add(inferredTable);
		
		result.setCaption("Types");

		return result;
	}


	@Override
	public Result execute(ResultComposite composite) {
		throw new UnsupportedOperationException("Filter only works on tables");
	}
}