package gate.creole.brat;

import java.io.File;
import java.net.URL;

import javax.swing.SwingUtilities;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.gui.MainFrame;
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
  
  
 /* public static void main(String args[]) throws Exception {
    Gate.runInSandbox(true);
    Gate.init();

    SwingUtilities.invokeAndWait(new Runnable() {

      @Override
      public void run() {
        MainFrame.getInstance().setVisible(true);
      }
    });

    Gate.getCreoleRegister().registerComponent(BratDocumentFormat.class);
    Gate.getCreoleRegister().registerComponent(BratNormalizer.class);

    URL txtURL = (new File("test/resources/PMID-26662.txt")).toURI().toURL();

    FeatureMap params = Factory.newFeatureMap();
    params.put(Document.DOCUMENT_URL_PARAMETER_NAME, txtURL);
    params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
    params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/x-brat");
    Document doc = (Document)Factory.createResource("gate.corpora.DocumentImpl", params);
    
    System.out.println(doc.getAnnotations("Original markups").getRelations().size());
    doc.getAnnotations("Original markups").getRelations().retainAll(doc.getAnnotations("Original markups").getRelations().getRelations("relation"));
    System.out.println(doc.getAnnotations("Original markups").getRelations().size());
    
    params = Factory.newFeatureMap();
    params.put("toolsConfURL", (new File("test/resources/tools.conf")).toURI().toURL());
    Factory.createResource("gate.creole.brat.BratNormalizer", params);
    
    Factory.newDocument((new File("test/resources/PMID-26662-new.xml")).toURI().toURL());
    
    JsonFactory jsonF = new JsonFactory();
    JsonGenerator jsonG = jsonF.createGenerator(System.out);
    BratUtils.writeJSON(doc.getAnnotations("Original markups"), new AnnotationConfig((new File("test/resources/annotation.conf")).toURI().toURL()), jsonG);
    jsonG.flush();
  }*/
}
