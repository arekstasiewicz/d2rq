/*
 * $Id: QueryCombiner.java,v 1.3 2006/08/28 21:13:47 cyganiak Exp $
 */
package de.fuberlin.wiwiss.d2rq.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.fuberlin.wiwiss.d2rq.sql.SelectStatementBuilder;


/**
 * Container for {@link TripleQuery} instances. TripleQueries can
 * be added using the {@link #add} method. The QueryCombiner will
 * combine multiple queries into a single SQL statement, if
 * possible. After adding all queries, a {@link D2RQResultIterator}
 * can be obtained from the {@link #getResultIterator} method.
 *
 * <p>History:<br>
 * 08-03-2004: Initial version of this class.<br>
 * 
 * @author Richard Cyganiak <richard@cyganiak.de>
 * @version V0.2
 */
public class QueryCombiner {
	private Collection compatibleQueries = new ArrayList(10);

	public void add(TripleQuery newQuery) {
		Iterator it = this.compatibleQueries.iterator();
		while (it.hasNext()) {
			List queries = (List) it.next();
			if (((TripleQuery) queries.get(0)).isCombinable(newQuery)) {
				queries.add(newQuery);
				return;
			}
		}
		List newList = new ArrayList(5);
		newList.add(newQuery);
		this.compatibleQueries.add(newList);
	}

	public D2RQResultIterator getResultIterator() {
		D2RQResultIterator result = new D2RQResultIterator();
		Iterator it = this.compatibleQueries.iterator();
		while (it.hasNext()) {
			List queryList = (List) it.next();
			result.addTripleResultSet(getTripleResultSet(queryList));
		}
		return result;
	}

	private TripleResultSet getTripleResultSet(List queries) {
		SelectStatementBuilder sql = getSQL(queries);
		Iterator it = queries.iterator();
		TripleQuery first = (TripleQuery) it.next();
		TripleResultSet result = new TripleResultSet(sql.getSQLStatement(),
				sql.getColumnNameNumberMap(),
				first.getDatabase());
		result.addTripleMaker(first);
		while (it.hasNext()) {
			TripleQuery query = (TripleQuery) it.next();
			result.addTripleMaker(query);
		}
		return result;
	}

	private SelectStatementBuilder getSQL(List queries) {
		Iterator it = queries.iterator();
		TripleQuery first = (TripleQuery) it.next();
		SelectStatementBuilder result = new SelectStatementBuilder(first.getDatabase());
		result.addJoins(first.getJoins());
		result.addColumnValues(first.getColumnValues());
		result.addConditions(first.getConditions());
		result.addSelectColumns(first.getSelectColumns());
		result.addColumnRenames(first.getReplacedColumns());
		result.setEliminateDuplicates(first.mightContainDuplicates());
		result.addAliasMap(first.getAliases());
		while (it.hasNext()) {
			TripleQuery query = (TripleQuery) it.next();
			result.addSelectColumns(query.getSelectColumns());
			result.addColumnRenames(query.getReplacedColumns());
		}
		return result;
	}
}