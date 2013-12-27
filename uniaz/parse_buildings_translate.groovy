import java.text.SimpleDateFormat

/******************************************
*	
*	parse_buildings_translate
*
*	Script for translating a buildings csv file into a turtle / n3 rdf file.
*	Implementation was made accordingly to the old 'parse-buildings.php'.
*	
*	Author: Peter Zimmerhof
* 
******************************************/

// Define baseurl and context
def baseurl = "http://data.uni-muenster.de/context/infrastructure/building/"
def context = "<http://data.uni-muenster.de/context/infrastructure/building/>"

// File name
def filename = "buildings"

// Load csv file
def csv = new File("scripts/csv/" + filename + ".csv").text

// Define turtle variable
def turtle = 
"""@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .
@prefix dbp-ont: <http://dbpedia.org/ontology/> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix geo-pos: <http://www.w3.org/2003/01/geo/wgs84_pos#> .
@prefix vcard: <http://www.w3.org/2006/vcard/ns#> .
@prefix dc: <http://purl.org/dc/terms/> .

"""

	turtle += context + " dc:date \"" + getISO8601() + "\"^^xsd:dateTime ;\n"
	turtle += "	dc:creator \"Script at http://data.uni-muenster.de/etl/parse_buildings_translate.groovy\"^^xsd:string . \n\n"
	

	def buildings = csv.split("\n")

	buildings = buildings[1..-1]
	if (buildings[-1] == "") {
		buildings = buildings[0..-2]
	}

	for (building in buildings) {
		
		def details = building.split(";")

		def building_id = details[2][1..-2].replaceAll('\"', '')
		def name 		= details[3][1..-2].replaceAll('\"', '')
		def street_name = details[4][1..-2].replaceAll('\"', '')
		def house_nr 	= details[5][1..-2].replaceAll('\"', '')
		def postcode 	= details[6][1..-2].replaceAll('\"', '')
		def town 		= details[7][1..-2].replaceAll('\"', '')
		def lat 		= details[10].replace(",", ".")
		def lon 		= details[11].replace(",", ".")

		def uri = "<" + baseurl + building_id + ">";

		def ttl_a=""
		def ttl_long=""
		def ttl_lat=""
		def ttl_name=""
		def ttl_adr=""	

		if(building_id != ""){

			ttl_a = "a dbp-ont:building ;"
			if(lon != "" && lat != ""){
				ttl_long = "geo-pos:long \"" + lon + "\" ;"
				ttl_lat = "geo-pos:lat \"" + lat + "\" ;"
			}
			if(name != ""){
				ttl_name = "foaf:name \"" + name + "\"^^xsd:string ;"
			}
			if(street_name != "" && town != "" && postcode != ""){
				ttl_adr = "vcard:adr \"" + street_name + " " + house_nr + ", " + postcode + " " + town + "\"^^xsd:string ;"
			}

			def result = uri + "\n" + ttl_a + "\n" + ttl_long + "\n" + ttl_lat + "\n" + ttl_name + "\n" + ttl_adr;
		

			while(result.getAt(-1)!=';'){
				result = result[0..-2]
			}

			result = result[0..-2] + "." 

			turtle += result + "\n\n"
		}
	}

	new File("scripts/turtle/" + filename + ".n3").write(turtle)

// Get a ISO 8601 formated date string
def getISO8601() {
	def date = new Date()  
	def sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.GERMANY)  
	def isoDate = sdf.format(date)
	
	return isoDate[0..-3] + ":" + isoDate[-2..-1]
}