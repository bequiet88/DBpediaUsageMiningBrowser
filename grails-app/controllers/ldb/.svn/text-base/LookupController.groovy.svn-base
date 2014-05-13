package ldb
import utils.AppConfig
import utils.DBpediaLookUp
import utils.Utils


/**
 * Controller for the DBpedia lookup service.
 *
 */
class LookupController {



	/**
	 * Function for handling the dbpedia lookup service.
	 */
	def search = {
		if(! AppConfig.isLookupEnabled())
			return
		//time stamp for begin of this query
		long localRequestTime = System.nanoTime()
		session.lastRequestTime = localRequestTime
		//get suggestions
		def suggestions = DBpediaLookUp.lookUp(params.query.replace(" ", "_"))

		//make sure this is still the most recent request
		if(session.lastRequestTime == localRequestTime)
		{
			//Create XML response
			render(contentType: "text/xml") {
				results() {
					suggestions.each { suggestion ->
						result(){ name(Utils.utf8toUnicode(suggestion)) }
					}
				}
			}
		}
	}
}
