package org.d2rq.r2rml.qb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.d2rq.r2rml.Mapping;
import org.d2rq.r2rml.R2RMLWriter;
import org.d2rq.vocab.RR;
import org.d2rq.writer.PrettyTurtleWriter;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

public class R2RMLQBWriter extends R2RMLWriter {
	
	private final static Set<Property> COMPACT_PROPERTIES = new HashSet<Property>(
			Arrays.asList(new Property[]{
					RR.logicalTable, RR.joinCondition, 
					RR.subjectMap, RR.predicateMap, RR.objectMap, RR.graphMap
			}));
	
	private final Mapping mapping;
	private final PrefixMapping prefixes = new PrefixMappingImpl();
	private PrettyTurtleWriter out;

	public R2RMLQBWriter(Mapping mapping) {
		super(mapping);
		this.mapping = mapping;
		prefixes.setNsPrefixes(mapping.getPrefixes());
		if (!prefixes.getNsPrefixMap().containsValue(RR.NS)) {
			prefixes.setNsPrefix("rr", RR.NS);
		}
	}

}
