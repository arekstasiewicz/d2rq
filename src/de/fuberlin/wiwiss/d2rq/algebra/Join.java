package de.fuberlin.wiwiss.d2rq.algebra;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fuberlin.wiwiss.d2rq.D2RQException;

/**
 * Represents an SQL join between two tables, spanning one or more columns.
 *
 * TODO: Make immutable
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 * @version $Id: Join.java,v 1.3 2006/09/11 23:22:24 cyganiak Exp $
 */
public class Join {
	private Set fromColumns = new HashSet(2);
	private Set toColumns = new HashSet(2);
	private Map otherSide = new HashMap(4); 
	private String fromTable = null;
	private String toTable = null;
	private String sqlExpression=null; // cached value
	
	public void addCondition(String joinCondition) {
		Attribute col1 = Join.getColumn(joinCondition, true);
		Attribute col2 = Join.getColumn(joinCondition, false);
		addCondition(col1, col2);
	}

	public void addCondition(Attribute column1, Attribute column2) {
		this.sqlExpression = null;
		if (this.fromTable == null) {
			this.fromTable = column1.tableName();
			this.toTable = column2.tableName();
		} else if (!this.fromTable.equals(column1.tableName())
				|| !this.toTable.equals(column2.tableName())) {
			throw new IllegalArgumentException(
					"Illegal join -- all conditions must go from *one* table to another *one* table");
		}
		this.fromColumns.add(column1);
		this.toColumns.add(column2);
		this.otherSide.put(column1, column2);
		this.otherSide.put(column2, column1);
	}
	
	public boolean containsTable(String tableName) {
		return tableName.equals(this.fromTable) ||
				tableName.equals(this.toTable);
	}

	public boolean containsColumn(Attribute column) {
		return this.fromColumns.contains(column) ||
				this.toColumns.contains(column);
	}

	public String getFirstTable() {
		return this.fromTable;
	}
	
	public String getSecondTable() {
		return this.toTable;
	}

	public Set getFirstColumns() {
		return this.fromColumns;
	}

	public Set getSecondColumns() {
		return this.toColumns;
	}

	/**
	 * Checks if one side of the join is equal to the argument set of
	 * {@link Attribute}s.
	 * @param columns a set of Column instances
	 * @return <tt>true</tt> if one side of the join is equal to the column set
	 */
	public boolean isOneSide(Set columns) {
		return this.fromColumns.equals(columns) || this.toColumns.equals(columns);
	}

	public Attribute getOtherSide(Attribute column) {
		return (Attribute) this.otherSide.get(column);
	}

	private static Attribute getColumn(String joinCondition, boolean first) {
		int index = joinCondition.indexOf("=");
		if (index == -1) {
			throw new D2RQException("Illegal d2rq:join: \"" + joinCondition +
					"\" (must be in \"Table1.Col1 = Table2.Col2\" form)");
		}
		String col1 = joinCondition.substring(0, index).trim();
		String col2 = joinCondition.substring(index + 1).trim();
		boolean return1 = col1.compareTo(col2) < 0;
		if (!first) {
			return1 = !return1;
		}
		return return1 ? new Attribute(col1) : new Attribute(col2);
	}

	
	/**
	 * Builds a list of Join objects from a list of join condition
	 * strings. Groups multiple condition that connect the same
	 * two table (multi-column keys) into a single join.
	 * @param joinConditions a collection of strings
	 * @return a set of Join instances
	 */
	public static Set buildFromSQL(Collection joinConditions) {
		Set result = new HashSet(2);
		Iterator it = joinConditions.iterator();
		while (it.hasNext()) {
			String condition = (String) it.next();
			String table1 = Join.getColumn(condition, true).tableName();
			String table2 = Join.getColumn(condition, false).tableName();
			Iterator it2 = result.iterator();
			while (it2.hasNext()) {
				Join join = (Join) it2.next();
				if (join.containsTable(table1) && join.containsTable(table2)) {
					join.addCondition(condition);
					condition = null;
					break;
				}
			}
			if (condition != null) {
				Join join = new Join();
				join.addCondition(condition);
				result.add(join);
			}
		}
		return result;
	}
	public static Join buildJoin(String condition) {
		Join join = new Join();
		join.addCondition(condition);
		return join;
	}
	
	
	public String sqlExpression() {
		if (sqlExpression!=null) 
			return sqlExpression;
		Object[] from = this.fromColumns.toArray();
		// jg was Object[] to = this.toColumns.toArray();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < from.length; i++) {
			if (i > 0) {
				result.append(" AND ");
			}
			result.append(((Attribute)from[i]).qualifiedName());
			result.append(" = ");
			result.append(((Attribute)otherSide.get(from[i])).qualifiedName()); // jg was to[i]
		}
		sqlExpression=result.toString();
		return sqlExpression;
	}
	public String toString() { 
		return "Join(" + sqlExpression() + ")";
	}

	public Join renameColumns(ColumnRenamer columnRenamer) {
		Join result = new Join();
		Iterator it = getFirstColumns().iterator();
		while (it.hasNext()) {
			Attribute column1 = (Attribute) it.next();
			Attribute column2 = getOtherSide(column1);
			result.addCondition(columnRenamer.applyTo(column1), columnRenamer.applyTo(column2));
		}
		return result;
	}
}