package filters;

import java.util.LinkedList;
import java.util.List;

import ldb.RDFTriple;

import result.Result;
import result.ResultComposite;
import result.Table;
import filter.Filter;

public class SimpleTypeFilter extends Filter {

	@Override
	public Result execute(Table table) {
		List<RDFTriple> typeStatements = new LinkedList<RDFTriple>();
		List<RDFTriple> otherStatements = new LinkedList<RDFTriple>();
		
		// separate triples
		for(RDFTriple triple : table.getTriples()) {
			if(triple.getPred().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && triple.getSub().equals(processedResource))
				typeStatements.add(triple);
			else
				otherStatements.add(triple);
		}
		
		ResultComposite result = new ResultComposite();
		result.setCaption("Types");
		Table typeTable = new Table(typeStatements);
		typeTable.addTag("type");
		typeTable.setCaption("Types");
		result.add(typeTable);
		
		Table otherTable = new Table(otherStatements);
		otherTable.addTag("misc");
		otherTable.setCaption("Statements");
		result.add(otherTable);
		
		return result;
	}

	@Override
	public Result execute(ResultComposite composite) {
		throw new UnsupportedOperationException("Filter only works on tables");
	}

}
