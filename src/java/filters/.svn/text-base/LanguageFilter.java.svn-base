package filters;

import ldb.RDFTriple;

/**
 * Simple language filter that works by checking if the string has a certain suffix (e.g. "@en").
 * 
 * 
 */
public class LanguageFilter extends SelectionFilter {

	protected String lang = "";

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	protected boolean fits(RDFTriple triple) {
		return triple.getObj().endsWith(lang);
	}
}
