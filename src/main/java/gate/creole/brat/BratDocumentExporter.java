package gate.creole.brat;

import gate.AnnotationSet;
import gate.Document;
import gate.DocumentExporter;
import gate.FeatureMap;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

@CreoleResource(name = "brat Standoff Annotation Exporter", tool = true, autoinstances = @AutoInstance)
public class BratDocumentExporter extends DocumentExporter {

  private static final long serialVersionUID = 5044875521415326607L;

  private String annotationSetName;
  private URL configURL;
  
  @RunTime
  @CreoleParameter
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }
  
  public String getAnnotationSetName() {
    return annotationSetName;
  }
  
  @RunTime
  @CreoleParameter
  public void setConfigURL(URL configURL) {
    this.configURL = configURL;
  }
  
  public URL getConfigURL() {
    return configURL;
  }
  
  public BratDocumentExporter() {
    super("brat Standoff Annotations", "ann", "text/x-brat");
  }

  @Override
  public void export(Document doc, OutputStream out, FeatureMap options)
      throws IOException {
    
    try {
      AnnotationSet annotations =
          doc.getAnnotations((String)options.get("annotationSetName"));

      AnnotationConfig config =
          new AnnotationConfig((URL)options.get("configURL"));
      
      BratUtils.writeStandoff(Annotations.getBratAnnotations(annotations, config), new PrintStream(out));
      
    } catch(Exception e) {
      throw new IOException("Failed to export Brat annotations", e);
    }
  }

}
