#Wahlgebiet

Das Projekt _wahlgebiet_ stellt Informationen zu geographische Strukturen, wie Stimmbezirke, Wahllokale und Wahlkreise zur Verfügung. Die Grundlage bieten verschiedene offener Daten. 

## Stimmbezirke

Der Stimmbezirk ist die kleinste "organisatorische Einheit bei politischen Wahlen" (vgl. Wikipedia [Wahlbezirk](https://de.wikipedia.org/wiki/Wahlbezirk)). Grundlage für den Service _/wahlgebiet/service/stimmbezirke_ ist der Datensatz [Stimmbezirke zur Bundestagswahl](https://www.offenedaten-koeln.de/dataset/stimmbezirk). Zur Verwendung in Webapplikationen werden die Informationen dieses Datensatzes in das [GeoJson](http://geojson.org/) Format umgewandelt.

## Wahllokale

"Das Wahllokal bzw. der Wahlraum ist der öffentliche Ort, an dem eine Wahl durchgeführt wird (...) Es ist ein Raum, der sich meist in einem öffentlichen Gebäude (Schule oder Rathaus) befindet (...) Das Wahllokal muss nach demokratischen Grundsätzen ein von Wahlwerbung freier und befriedeter Ort sein und eine geheime Entscheidung des Wählers in einer Wahlkabine ermöglichen." (vgl. [Wikipedia: Wahllokal](https://de.wikipedia.org/wiki/Wahllokal))

Grundlage für dieses Projekt sind Informationen, die auf dem Offene Daten Portal der Stadt Köln zur Verfügung gestellt werden: [Offene Daten Köln - Wahllokale](https://www.offenedaten-koeln.de/dataset/wahllokale)

## Wahlkreis

"Ein Wahlkreis ist der – in der Regel geographisch zusammenhängende – Teilraum eines Wahlgebietes, in dem Wahlberechtigte über die Besetzung eines oder mehrerer Mandate abstimmen. Die zu wählende Versammlung kann das nationale Parlament oder das eines Gliedstaates sein. Das unterscheidet den Wahlkreis begrifflich vom Wahlbezirk (Wahlsprengel), der nur eine organisatorische Einheit der Stimmauszählung ist. Wahlkreise sind spezielle Wahlbezirke." (vgl. [Wikipedia: Wahlkreis](https://de.wikipedia.org/wiki/Wahlkreis))

### Landtagswahl



###  Bundestagswahl

"Das Bundesgebiet ist derzeit in 299 Wahlkreise eingeteilt. Die Einteilung des Wahlgebietes in Wahlkreise für die Wahl zum 19. Deutschen Bundestag ist in der Anlage zu Artikel 1 des Dreiundzwanzigsten Gesetzes zur Änderung des Bundeswahlgesetzes (BWG) vom 3. Mai 2016 (BGBl. I S. 1062) beschrieben. Sie ist seit dem 10. Mai 2016 in Kraft getreten." (vgl. [bundeswahlleiter.de: Wahlkreiseinteilung](https://www.bundeswahlleiter.de/bundestagswahlen/2017/wahlkreiseinteilung.html))

# Entwicklungsstand

Dieser Service wird zur Zeit umgebaut und befindet sich in Entwicklung. Die vorhergende Version steht in der Version 1.0 weiterhin zur Verfügung.

[![Build Status](https://api.travis-ci.org/codeforcologne/wahlgebiet.svg?branch=master)](https://travis-ci.org/codeforcologne/wahlgebiet)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/54c094fd6e2149719a9fe54970227f46)](https://www.codacy.com/app/eberius/wahlgebiet?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=codeforcologne/wahlgebiet&amp;utm_campaign=Badge_Grade)


# Schnittstellen

# Datenbank

## DB User auf Postgres einrichten

    sudo -u postgres createuser -P wahlgebiet
    
## Datenbank wahlgebiet anlegen

    sudo -u postgres createdb -O wahlgebiet wahlgebiet
    
## Postgis topology

    sudo -u postgres psql -c "CREATE EXTENSION postgis; CREATE EXTENSION postgis_topology;" wahlgebiet

## Tabellen anlegen

### stimmbezirk

	CREATE TABLE stimmbezirk (
	    id           varchar(256),
	    nummber      varchar(256),
	    K_WAHL       integer,
	    L_Wahl       integer,
	    B_Wahl       integer,
	    NR_STB       integer,
	    STB          varchar(256),
	    NR_STT       integer,
	    STT          varchar(256),
	    SHAPE_AREA   double precision,
	    SHAPE_LEN    double precision,
	    modtime      timestamp DEFAULT current_timestamp
	);
	SELECT AddGeometryColumn ('public','stimmbezirk','geom',4326,'MULTIPOLYGON',2);
	
### wahllokal
	
	CREATE TABLE wahllokal (
        id                   integer,
    	stimmbezirk          integer,
    	name                 varchar(256),
    	adresse              varchar(1024),
    	rollstuhlgerecht     integer,
    	bemerkung            varchar(1024),
    	abstimmbezirk        integer,
    	stadtteil            varchar(128),
    	postzustellbezirk    integer,
    	adNummer             integer,
    	stimmbezirkStadtteil varchar(128),
    	kommunalwahlbezirk   integer,
    	landtagswahlkreis    integer,
    	bundestagswahlkreis  integer,
    	modtime              timestamp DEFAULT current_timestamp
    );
    SELECT AddGeometryColumn ('public','wahllokal','geom',4326,'POINT',2);

### wahlgebiet

In der Tabelle werden alle Wahlgebiete gespeichert. Das sind alle Flächen, die sich mit Multipolygonen beschreiben lassen.

| Spalte | Typ | Beschreibung |
| ------ | --- | ------------ |
| id | integer | interner Schlüssel für Relation |
| nummer | integer | Die Nummer des Wahlgebiets |
| wahl | varchar(256) | Die Art der Wahl, z.B. 'landtagswahl', 'bundestagswahl' |
| datum | timestamp | Zeitpunkt der Wahl; typischerweise ein Datum, z.B. 24.09.2017 |
| bezeichnung | varchar(2) | z.B. Name des Wahlkreises |
| simplify | integer | ganze Zahl gibt darüber Auskunft, ob das Multipolygon simplifiziert vorliegt; 0: nicht simplifiziert, > 0: simplifiziert |
| modtime | timestamp DEFAULT current_timestamp | Zeitpunkt der Erstellung des Datensatzes |


    CREATE TABLE wahlgebiet (
        id                   integer,
	    nummer               integer,
	    wahl                 varchar(256),
	    datum				 timestamp,
        bezeichnung          varchar(256),
        simplify             integer,
        modtime              timestamp DEFAULT current_timestamp
    );
    SELECT AddGeometryColumn ('public','wahlgebiet','geom',4326,'MULTIPOLYGON',2);
    
### eigenschaft

Sind weitere Informationen vorhanden, werden diese 
    
| Spalte | Typ | Beschreibung |
| ------ | --- | ------------ |
| id | integer | interner Schlüssel für Relation |
| wahlgebiet | integer | id des wahlgebietes |
| key | varchar(256) | Die Art der Wahl, z.B. 'landtagswahl', 'bundestagswahl' |
| value | varchar(256) | Zeitpunkt der Wahl; typischerweise ein Datum, z.B. 24.09.2017 |
| modtime | timestamp DEFAULT current_timestamp | Zeitpunkt der Erstellung des Datensatzes |

    CREATE TABLE eigenschaft (
        id                   integer,
        wahlgebiet           integer,
        key                  varchar(256),
        value                varchar(256),
        modtime              timestamp DEFAULT current_timestamp
    );
	
	
## DB-Tabellen initial einrichten

    psql -h localhost -U wahlgebiet -d wahlgebiet -a -f src/main/sql/wahlgebiet.init.sql


## Verbindungsparameter

Die Datenbankverbindungsparameter werden per JNDI zur Verfügung gestellt. Dies bedeutet, dass sie im Container definiert sein müssen. Für den Online-Betrieb mit
Tomcat sind folgende Parameter zu setzen:

context.xml

    <Context>
        <ResourceLink 
             name="jdbc/wahlgebiet" 
             global="jdbc/wahlgebiet"
             type="javax.sql.DataSource" />
    </Context> 

server.xml

    <GlobalNamingResources>
        <Resource 
            name="jdbc/wahlgebiet"
            auth="Container"
            driverClassName="org.postgresql.Driver"
            maxTotal="25" 
            maxIdle="10"
            username="username"
            password="password"
            type="javax.sql.DataSource"
            url="jdbc:postgresql://localhost:5432/wahlgebiet"
            validationQuery="select 1"/>

Zu Testzwecken muss die Datei _src/test/resources/jndi.properties.template_ in _jndi.properties_ umbenannt und die Verbindungsparameter angepasst werden.

# Test

## Tests mit Datenbank

Da zur Zeit keine Integration Test Stage zur Verfügung steht, sind alle Tests, die eine Datenbank voraussetzt als main codiert. Um eine Datenbankverbindung hierfür zur Verfügung stellen zu können, muss die Datei src/test/resources/jndi.properties.template in src/test/resources/jndi.properties kopiert und die entsprechenden Parameter zur Datenbank gesetzt werden.

# Installation

1. git clone
2. mvn clean install
3. jetty run

## Dependencies

### tomcat

Die Webapplikation benötigt folgende PostGresSQL - Bibliotheken:

- postgis-jdbc-2.1.7.2.jar
- postgresql-9.1-901-1.jdbc4.jar
- postgresql-9.3-1104-jdbc41.jar

Für Entwicklungszwecke werden die Bibliotheken über den build-Mechanismus gezogen. Für die Ausführung auf dem Tomcat müssen sie im _tomcat/lib_ Verzeichnis liegen und dürfen nicht mit der Webapplikation ausgeliefert werden. Aus diesem Grund wird in den dependencies der scope 'provided' verwendet:

		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>9.3-1104-jdbc41</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>net.postgis</groupId>
			<artifactId>postgis-jdbc</artifactId>
			<version>2.1.7.2</version>
			<scope>provided</scope>
		</dependency>


Die Bibliotheken können von [PostGres JDBC Driver](https://jdbc.postgresql.org/download.html) bezogen werden und müssen manuell in das Verzeichnis _&lt;tomcat-home&gt;/lib_ kopiert werden.

Anmerkung: Die Bibliothek 'postgresql-9.1-901-1.jdbc4.jar' wird über den Maven Mechanismus gezogen.

# License

<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons Lizenzvertrag" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />Dieses Werk ist lizenziert unter einer <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Namensnennung - Weitergabe unter gleichen Bedingungen 4.0 International Lizenz</a>.
