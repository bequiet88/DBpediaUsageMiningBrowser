grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// uncomment to disable ehcache
		// excludes 'ehcache'
		excludes 'xml-apis'
	}
	log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums true // Whether to verify checksums on resolve

	repositories {
		inherits true // Whether to inherit repository definitions from plugins
		grailsPlugins()
		grailsHome()
		grailsCentral()
		mavenCentral()

		// uncomment these to enable remote dependency resolution from public Maven repositories
		//mavenCentral()
		//mavenLocal()
		//mavenRepo "http://snapshots.repository.codehaus.org"
		//mavenRepo "http://repository.codehaus.org"
		//mavenRepo "http://download.java.net/maven/2/"
		//mavenRepo "http://repository.jboss.com/maven2/"
	}
	dependencies {
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		// runtime 'mysql:mysql-connector-java:5.1.16'
		compile 'com.hp.hpl.jena:jena-core:2.7.0'
		compile 'com.hp.hpl.jena:jena-arq:2.9.0'
		compile 'com.hp.hpl.jena:jena-iri:0.9.0'
		runtime 'com.hp.hpl.jena:jena-core:2.7.0'
		runtime 'com.hp.hpl.jena:jena-arq:2.9.0'
		runtime 'com.hp.hpl.jena:jena-iri:0.9.0'

		compile('org.apache.xerces:xercesImpl:2.11.0')
        {
			excludes 'xml-apis'
        }	 
		runtime('org.apache.xerces:xercesImpl:2.11.0')
		{
			excludes 'xml-apis'
		}

		compile 'xml-apis:xml-apis:1.4.01'
		runtime 'xml-apis:xml-apis:1.4.01'
		
		compile 'org.apache.commons.lang3:commons-lang3:3.1'
		runtime 'org.apache.commons.lang3:commons-lang3:3.1'
		compile 'org.jdom:jdom:2.0.1'
		runtime 'org.jdom:jdom:2.0.1'

		compile 'com.ibm.icu:icu4j:3.4.4'
		runtime 'com.ibm.icu:icu4j:3.4.4'

		compile 'org.apache.commons.io:commons-io:2.4'
		runtime 'org.apache.commons.io:commons-io:2.4'
		
		compile 'DBpediaUsageMining:DBpediaUsageMining:0.1'
		runtime 'DBpediaUsageMining:DBpediaUsageMining:0.1'


	}

	plugins {
		runtime ":hibernate:3.6.10.13"
		runtime ":jquery:1.7.1"
		runtime ":resources:1.1.6"

		// Uncomment these (or add new ones) to enable additional resources capabilities
		//runtime ":zipped-resources:1.0"
		//runtime ":cached-resources:1.0"
		//runtime ":yui-minify-resources:0.1.4"

		build ":tomcat:7.0.52.1"
	}
}
