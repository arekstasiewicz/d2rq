# D2RQ Release Notes

## D2RQ v0.8 – 2012-03-09

### Features
- D2R Server provides direct HTTP access to BLOB/CLOB contents via d2rq:DownloadMap

### Enhancements
- 

### Bugfixes
- 

### Performance
- 

### Other
- D2R Server is no longer a stand-alone download, but bundled directly with D2RQ
- All-new website at http://d2rq.org/


## D2RQ v0.7 – 2009-08-12

Version 0.7 provides several bugfixes, better dump performance, several new features as well as new optimizations that must be enabled using d2rq:useAllOptimizations, or via D2R Server's --fast switch.

### Features
- Added support for Microsoft SQL Server
- Added support for dynamic properties (by Jörg Henß)
- Added support for d2rq:limit, d2rq:limitInverse, d2rq:orderAsc and d2rq:orderDesc (thanks to Matthias Quasthoff)
- Added d2rq:useAllOptimizations option for bleeding-edge optimizations
- Added d2rq:fetchSize to control the number of rows retrieved per database query. Activated by default from dump_rdf.

### Enhancements
- Added SQL cursor support and MySQL streaming to facilitate dumping of large databases (based on a patch by Alistair Miles)
- Added support for !!a => a in DeMorganLawApplyer; improved DistributiveLawApplyer
- Added output warning about uriPatterns without columns during both mapping generation and loading
- Updated to MySQL Connector/J 5.1.7 due to reported hanging errors
- Updated to ARQ 2.7 (Jena 2.6), Joseki CVS (2008-12), SDB 1.3
- Removed dependencies on Oracle and MySQL libraries
- Removed non-open source JDBC drivers from distribution
- Added note to documentation: Vocabulary serving does not work with SPARQL 

### Bugfixes
- Added Database-specific LIMIT implementations
- Fixed filtering on variables with an sqlExpression property bridge (#2620006, Herwig Leimer)
- Modified treatment of nonmoveable filter expressions in D2RQTreeOptimizer, skip variable retention on subUsage in VarFinder (Herwig Leimer)
- Fully removed buggy leftjoin optimization (#2634088, #2645486)
- Let the JDBC driver convert boolean values separately as their representation differs greatly amongst DBs (thanks to Alistair Miles)
- Fixed JDBC-ODBC Bridge interoperability (MS Access etc.) - refactored ResultRowMap to return values with a single get???() call
- Relaxed table naming conditions to support hyphens and numbers in table names (thanks to Heinz Doerrer, bug #2664865)
- Properly iterate over trivial relations (fixes ResultRow to Tripe casting bug)
- Don't create a link table if it exports foreign keys (#2787278) 
- Support case-insensitive RelationName comparison for MySQL and allow map generation from keys with unspecified capitalization
- Fixed a bug with the keepAliveAgent which didn't terminate when dump_rdf is used
- Handle multiple projection specs for SPARQL variable within TransformExprToSQLApplyer 
- Moved DISTINCT before TOP in SQL queries
- Fixed a bug in DistributiveLawApplyer

### Performance
- Added self-join optimization (#2798308). Enable using --fast command-line option in D2R Server, or d2rq:useAllOptimizations.
- Added support for directed arrows in d2rq:join to indicate foreign key relationship and to utilize them for correct join optimization (#1794042)
- Omit empty NodeRelations when translating graph patterns
- Keep uniqueness when working with just one Relation in TripleRelationJoiner (requires --fast) 
- Only check for predicate URI matches only if dynamic properties are present in the mapping 
- Translate filter expressions involving constant nodes to SQL


## D2RQ v0.6 – 2009-02-19

### Features
- d2rq:sqlExpression
- d2rq:uriSqlExpression for properties with URI values, similar to d2rq:sqlExpression but generates URIs instead of literals (Andreas Langegger)
- Support for serving vocabulary classes and properties; including definition labels, comments and additional properties

### Enhancements
- New ARQ-based QueryEngine, dropped the old FastPath engine
- Mapping generator now supports multi-column foreign key constraints
- Specify JDBC connection properties through the jdbc: namespace
- Better error reporting
- Oracle support - automatic registration of the JDBC driver, DATE, TIMESTAMP and CLOB field types
- Support for PostgreSQL's TIMESTAMP WITH TIME ZONE type
- Proper treatment of xsd:boolean datatype
- Show XML declaration and make sure that utf-8 encoding is used for XML
- Map parser now complains about misspelled D2RQ vocabulary terms
- Remove MySQL hack for zero date values from the mapping generator, use zeroDateTimeBehavior property instead
- Change dump-rdf default format to N-TRIPLE (faster, better streaming)

### Performance
- Push-down and transformation of SPARQL filter expressions into SQL, if possible
- Transformation of SPARQL optionals into SQL left joins; some cases only

### Bugfixes
- many bugfixes

### Other
- Replaced custom Logger with Log4j one
- Give more memory to VM in dump-rdf and generate-mapping scripts

## D2RQ v0.5.1 – 2007-10-24

### Enhancements
- added d2rq:resultSizeLimit option
- smarter mapping generator

### Bugfixes
- columns with non-URI characters can now be used in URI patterns
- many bugfixes


## D2RQ v0.5 – 2006-10-26

### Features
- supports the SPARQL query language.
- added dump-rdf command line script.
- added automatic mapping generator.
- added Jena Assembler for D2RQ models.

### Enhancements
- improved compatibility with MySQL, Oracle and PostgreSQL.
- compatible with Jena 2.4 and ARQ 1.4
- many other improvements.

### Bugfixes
- many bugfixes.


## D2RQ v0.4 – 2005-10-25

### Features
- Wrapper for Sesame added.


## D2RQ v0.3 – 2005-04-29

### Features
- d2rq:queryHandler added.
- d2rq:expressionTranslator added.
- d2rq:alias added.

### Performance
- New experimental D2RQQueryHandler for speeding up complex RDQL queries.


## D2RQ v0.2 – 2004-08-12

### Features
- d2rq:TranslationTable between DB and RDF values added.
- d2rq:condition added; allows filtering by SQL expression.
- d2rq:AdditionalProperty added.
- d2rq:containsDuplicates hint added.
- d2rq:class added; alternative to d2rq:classMap.
- d2rq:property added; alternative to d2rq:propertyBridge.

### Performance
- d2rq:valueContains performance optimization hint added.
- d2rq:valueMaxLength performance optimization hint added.
- d2rq:valueRegex performance optimization hint added.
- major speed improvement for queries with large result sets.
- major speed improvement for maps with URL columns.

### Other
- logging facility added.
- security fix: SQL injection vulnerability fixed.
- bug fix: issue with missing statements when using joins and NULL values.
- improved map consistency checking and better error messages
- GraphD2RQ and ModelD2RQ can also be initialised from RDF/XML map file.
- GraphD2RQ and ModelD2RQ can also be initialised from Jena map model.
- architectural changes for better maintainability.
- JUnit test suite added.
- many small fixes, improvements, speedups and refactorings.


## D2RQ v0.1 – 2004-06-22

- Initial D2RQ Release.