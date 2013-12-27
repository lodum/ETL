import java.text.SimpleDateFormat

/******************************************
*	
*	parse_lecturehalls_translate
*
*	Script for translating a lecturhalls csv file into a turtle / n3 rdf file.
*	Implementation was made accordingly to the old 'parse-hoersaele.php'.
*	
*	Author: Peter Zimmerhof
* 
******************************************/

// Define baseurl and context
def baseurl = "http://data.uni-muenster.de/context/infrastructure/lecturehall/"
def context = "<http://data.uni-muenster.de/context/infrastructure/lecturehall/>"

// File name
def filename = "lecturehalls"

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
@prefix lodum: <http://vocab.lodum.de/helper/> .

"""

	turtle += context + " dc:date \"" + getISO8601() + "\"^^xsd:dateTime ;\n"
	turtle += "	dc:creator \"Script at http://data.uni-muenster.de/etl/parse_lecturehalls_translate.groovy\"^^xsd:string . \n\n"

	def lecturehalls = csv.split("\n")

	lecturehalls = lecturehalls[1..-1]
	if (lecturehalls[-1] == "") {
		lecturehalls = lecturehalls[0..-2]
	}

	for (lecturehall in lecturehalls) {
		
		def details = lecturehall.split(";")

		def building_id = details[1][1..-2]
		def floor 		= details[2][1..-2]
		def name        = details[5][1..-2]	
		def usage       = details[6]

		if(usage == "H"){

			turtle 
			def result = "<http://data.uni-muenster.de/context/infrastructure/building/" + building_id 
			result += "> owl:sameAs <http://data.uni-muenster.de/context/infrastructure/building/" + removeLeadingZeros(building_id) + "> .\n\n"
	
			result += "<" + baseurl + prepareForURI(name) + "> a lodum:LectureHall ;\n	"

			// same name in German and English:
			if(name != ''){
				result += "foaf:name \"" + name + "\"@de ;\n				"
				result += "foaf:name \"" + name + "\"@en ;\n	"
			}
			
			if(floor != ''){
				result += "lodum:floor \"" + removeLeadingZeros(floor) + "\"^^xsd:int ;\n	"
			}

			result += "lodum:building <http://data.uni-muenster.de/context/infrastructure/building/" + building_id + "> .\n\n"

			turtle += result
		}

	}

	new File("scripts/turtle/" + filename + ".n3").write(turtle)


// Replace special characters for URI
def prepareForURI(name){
	name = name.replaceAll(' ', '_')
	name = name.replaceAll('ü', 'u')
	name = name.replaceAll('ö', 'oe')
	name = name.replaceAll('\\.', '')

	return name
}

// Remove leading zeros
def removeLeadingZeros(n) {
	while(n.getAt(0) == '0') {
		n = n[1..-1]
	}
	return n
}

// Get a ISO 8601 formated date string
def getISO8601() {
	def date = new Date()  
	def sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.GERMANY)  
	def isoDate = sdf.format(date)
	
	return isoDate[0..-3] + ":" + isoDate[-2..-1]
}