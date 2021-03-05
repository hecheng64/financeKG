package com.dataIn.janusgraph.utils.schema;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

public class JanusGraphSONSchema
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
;
public JanusGraphSONSchema
(org.janusgraph.core.JanusGraph graph) {}
@groovy.transform.Generated() @groovy.transform.Internal() public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  void setMetaClass(groovy.lang.MetaClass mc) { }
@groovy.transform.Generated() @groovy.transform.Internal() public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  java.lang.Object getProperty(java.lang.String property) { return null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  void setProperty(java.lang.String property, java.lang.Object value) { }
public  org.janusgraph.graphdb.database.StandardJanusGraph getGraph() { return (org.janusgraph.graphdb.database.StandardJanusGraph)null;}
public  void setGraph(org.janusgraph.graphdb.database.StandardJanusGraph value) { }
public  void readFile(java.lang.String schemaFile) { }
public static  boolean commitTxs(org.janusgraph.core.JanusGraph graph) { return false;}
public static  boolean rollbackTxs(org.janusgraph.core.JanusGraph graph) { return false;}
public static  com.dataIn.janusgraph.utils.schema.GraphSchema parse(java.lang.String schemaFile) { return (com.dataIn.janusgraph.utils.schema.GraphSchema)null;}
public  void make(java.util.List<org.apache.tinkerpop.shaded.jackson.databind.node.ObjectNode> nodes, java.lang.String name, groovy.lang.Closure check, groovy.lang.Closure exist, groovy.lang.Closure create) { }
}
