import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.ANY
import org.apache.commons.lang.StringEscapeUtils

/******************************************
*	
*	parse_buildings_extract
*
*	Script for extracting a buildings csv file.
*	
*	Author: Peter Zimmerhof
* 
******************************************/

// URL, path and table of the server
def url = ""
def path = ""

// Table and file name
def table = ""
def filename = ""

// Credentials
def key = ""
def contact = ""

// CSV variable
def csv = ""

// Define and execute HTTP request
def http = new HTTPBuilder(url)
http.request( GET ) {

	uri.path = path

	headers.'X-Key' = key
	headers.'TABLE' = table
	headers.'KONTAKT' = contact
	headers.'Connection' = "Close"

	// Success response function
	response.success = { resp, reader ->
		assert resp.statusLine.statusCode == 200

		// Convert encoding
		String target = new String(reader.getBytes(), "Cp1250")

		// Write csv file
		new File("scripts/csv/" + filename + ".csv").write(target)

	}

	// Fail response function
	response.'404' = {
		println 'Not found'
	}
}
