package gate.creole.brat;

import java.net.URL;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.test.GATEPluginTestCase;

public class TestBratFormat extends GATEPluginTestCase {
  
  public void testLoadBratDocument() throws Exception {
    URL txtURL = this.getClass().getResource("/resources/PMID-26662.txt");

    FeatureMap params = Factory.newFeatureMap();
    params.put(Document.DOCUMENT_URL_PARAMETER_NAME, txtURL);
    params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
    params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/x-brat");
    Document doc = (Document)Factory.createResource("gate.corpora.DocumentImpl", params);
    
    assertEquals(10,doc.getAnnotations("Original markups").size());
  }
}
