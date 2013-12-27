import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.ANY
import groovy.json.JsonSlurper;
import java.security.MessageDigest
import java.text.SimpleDateFormat

/******************************************
*	
*	parse_uniaz_translate
*
*	Script for translating a uniaz csv file into a turtle / n3 rdf file.
*	Implementation was made accordingly to the old 'parse-az.php'.
*	
*	Author: Peter Zimmerhof
* 
******************************************/

// Define baseurl and context
def baseurl = "http://data.uni-muenster.de/context/infrastructure/lecturehall/"
def context = "<http://data.uni-muenster.de/context/uniaz/>"

// File name
def filename = "uniaz"

// Load csv file
def csv = new File("scripts/csv/" + filename + ".csv").text

// Define turtle variable
def turtle = 
"""@prefix geo-pos:  <http://www.w3.org/2003/01/geo/wgs84_pos#> .
@prefix rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc-terms: <http://purl.org/dc/terms/> .
@prefix dc:       <http://purl.org/dc/elements/1.1/> .
@prefix foaf:     <http://xmlns.com/foaf/0.1/> .
@prefix owl:      <http://www.w3.org/2002/07/owl#> .
@prefix geonames: <http://sws.geonames.org/> .
@prefix lodum:    <http://vocab.lodum.de/helper/> .
@prefix rdfs:     <http://www.w3.org/2000/01/rdf-schema#> .
@prefix dbpedia:  <http://dbpedia.org/resource/> .
@prefix geo-ont:  <http://www.geonames.org/ontology#> .
@prefix vcard:    <http://www.w3.org/2006/vcard/ns#> .	
@prefix cris:     <http://data.uni-muenster.de/context/cris/> .
@prefix bibo:     <http://purl.org/ontology/bibo/> .
@prefix xsd:      <http://www.w3.org/2001/XMLSchema#> .
@prefix aiiso:    <http://purl.org/vocab/aiiso/schema#> .

"""

	turtle += context + " dc:date \"" + getISO8601() + "\"^^xsd:dateTime ;\n"
	turtle += "             dc:creator \"Script at http://data.uni-muenster.de/etl/parse_uniaz_translate.groovy\"^^xsd:string . \n\n"

	turtle +=
"""<http://data.uni-muenster.de/context/uniaz/wwu> owl:sameAs <http://data.uni-muenster.de/context/cris/organization/4863> .
	
<http://data.uni-muenster.de/context/cris/organization/4864> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb01> ;
		lodum:departmentNo "1"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/4883> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb02> ;
		lodum:departmentNo "2"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/4912> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb03> ;
		lodum:departmentNo "3"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/4957> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb04> ;
		lodum:departmentNo "4"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5006> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb05> ;
		lodum:departmentNo "5"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5112> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb06> ;
		lodum:departmentNo "6"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5123> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb07> ;
		lodum:departmentNo "7"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5151> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb08> ;
		lodum:departmentNo "8"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5178> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb09> ;
		lodum:departmentNo "9"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5206> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb10> ;
		lodum:departmentNo "10"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5247> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb11> ;
		lodum:departmentNo "11"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5296> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb12> ;
		lodum:departmentNo "12"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5358> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb13> ;
		lodum:departmentNo "13"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5395> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb14> ;
		lodum:departmentNo "14"^^xsd:int .

<http://data.uni-muenster.de/context/cris/organization/5431> a lodum:Department;
		owl:sameAs <http://data.uni-muenster.de/context/uniaz/fb15> ;
		lodum:departmentNo "15"^^xsd:int .
		
"""

		// Mensa Bispinghof (missing in Uni A-Z):
		turtle +=
"""<http://data.uni-muenster.de/context/uniaz/MensaBispinghof> a <http://dbpedia.org/ontology/Restaurant> ;
foaf:homepage	<http://www.studentenwerk-muenster.de/> ;
foaf:name	"Mensa Bispinghof"@de ;
vcard:adr	"Bispinghof 9-14, 48143 Münster"^^xsd:string ;
lodum:building	<http://data.uni-muenster.de/context/infrastructure/building/1436> .

"""

	def institutions = csv.split("\n")

	institutions = institutions[1..-1]
	//if (institutions[-1] == "") {
	//	institutions = institutions[0..-2]
	//}

	for (institution in institutions) {

		def details = institution.split(";")

		def name 		= details[0][1..-2]
		def type 		= details[1][1..-2]
		def fachbereich = details[2][1..-2]
		def link	    = details[3][1..-2].replaceAll('\"', '')
		def building    = details[4][1..-2].replaceAll('\"', '')
		def address     = details[5][1..-2].replaceAll('\"', '')
		def postcode    = details[6][1..-2].replaceAll('\"', '')
		def town        = details[7][1..-2].replaceAll('\"', '')

		def uri = "<http://data.uni-muenster.de/context/uniaz/" + getHash(name) + ">";
		def isFB = false;

		if(name.startsWith("Fachbereich 0") || name.startsWith("Fachbereich 1")) {

			def slurper = new JsonSlurper()
			def json = slurper.parseText(getCrisURI(name))

			if (json.results.bindings[0] != null) {
				
				isFB = true
				uri = "<http://data.uni-muenster.de/context/uniaz/fb" + name[12..13] + ">"
				
				def crisURI = json.results.bindings[0].fb.value

				turtle += uri + " owl:sameAs <" + crisURI + "> ;\n		    	"

			}

		}
		
		if(!isFB){
			if(name.contains("Mensa") || name.contains("Bistro")){
				turtle += uri + " a <http://dbpedia.org/ontology/Restaurant> ; \n				"
			}else if(name.contains("Wohnanlage") || name.contains("Studentenwohnheim")){
				turtle += uri + " a lodum:StudentHousing ; \n				"
			}else{
				turtle += uri + " a foaf:Organization ; \n				"				
			}

			name = name.replaceAll('\"', '\'')
				if(name == "Mensa am Aasee Mensa I"){
					turtle += "  foaf:name \"Mensa am Aasee (Mensa I)\"@de ; \n		" + getFachbereichURI(fachbereich);
				}else if(name == "Mensa am Ring Mensa II"){
					turtle += "  foaf:name \"Mensa am Ring (Mensa II)\"@de ; \n		" + getFachbereichURI(fachbereich);
				}else{
					turtle += "  foaf:name \"" + name + "\"@de ; \n		"+ getFachbereichURI(fachbereich);
				}
		} else { // Fachbereiche - name is already there, "part of" makes no sense:

			turtle += " a lodum:Department ; \n				"
		}

		turtle += "\n		foaf:homepage <" + link + "> ;\n		"
	
		if(building != ""){
			turtle += "lodum:building <http://data.uni-muenster.de/context/infrastructure/building/" + building + "> ;\n		"
		}

		turtle += "vcard:adr \"" + address + ", " + postcode + " " + town + "\"^^xsd:string .\n	\n	\n"
	}

	new File("scripts/turtle/" + filename + ".n3").write(turtle)


// Request function to request Cris name for department names
// Returns the response as json text
def getCrisURI(name){

	def nameURL = name[0..13] + ' - ' + name[15..-1]
	def query = "SELECT * WHERE { ?fb <http://xmlns.com/foaf/0.1/name> \"" + nameURL + "\"@de . FILTER regex(str(?fb), \"uniaz\") . }"
	
	def http = new HTTPBuilder("http://data.uni-muenster.de/sparql?query=" + URLEncoder.encode(query))

	def json = ""

	http.request( GET ) {

		//uri.query = 

		headers.'Accept' = "application/sparql-results+json"

		response.success = { resp, reader ->
	    	assert resp.statusLine.statusCode == 200
		    //println "Got response: ${resp.statusLine}"
		    //println "Content-Type: ${resp.headers.'Content-Type'}"

			json = new String(reader.getBytes(), "Cp1250")
		}
	 
	  	response.'404' = {
	    	println 'Not found'
	  	}
	}

	return json
}

// Retrieve Fachbereich URIs
def getFachbereichURI(fbNo){
	if(fbNo == '01'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/4864> ;');
	} else if (fbNo == '02'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/4883> ;');
	}else if(fbNo == '03'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/4912> ;');
	}else if(fbNo == '04'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/4957> ;');
	}else if(fbNo == '05'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5006> ;');
	}else if(fbNo == '06'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5112> ;');
	}else if(fbNo == '07'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5123> ;');
	}else if(fbNo == '08'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5151> ;');
	}else if(fbNo == '09'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5178> ;');
	}else if(fbNo == '10'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5206> ;');
	}else if(fbNo == '11'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5247> ;');
	}else if(fbNo == '12'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5296> ;');
	}else if(fbNo == '13'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5358> ;');
	}else if(fbNo == '14'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5395> ;');
	}else if(fbNo == '15'){
		return('aiiso:part_of <http://data.uni-muenster.de/context/cris/organization/5431> ;');
	}else {
		return "";
	}
}

// Hash function for MD5 hashes
def getHash(name) {
	md5Digest = MessageDigest.getInstance("MD5");
	md5Digest.reset();
	md5Digest.update(name.getBytes());
	digest = md5Digest.digest();

	return new BigInteger(1,digest).toString(16).padLeft(32, '0')
}

// Get a ISO 8601 formated date string
def getISO8601() {
	def date = new Date()  
	def sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.GERMANY)  
	def isoDate = sdf.format(date)
	
	return isoDate[0..-3] + ":" + isoDate[-2..-1]
}