package gate.creole.brat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;

@CreoleResource(name = "brat Normalizer")
public class BratNormalizer extends AbstractLanguageAnalyser {

  public enum Type {
    EXPAND,
    COLLAPSE
  };

  private static final long serialVersionUID = 7051258621449929252L;

  private URL toolsConf;

  private Map<String, Normalization> expansions = null;

  private Type type;

  private String annotationSetName;

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @RunTime
  @CreoleParameter
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  public URL getToolsConfURL() {
    return toolsConf;
  }

  @CreoleParameter(suffixes = "conf")
  public void setToolsConfURL(URL toolsConf) {
    this.toolsConf = toolsConf;
  }

  public Type getType() {
    return type;
  }

  @RunTime
  @CreoleParameter(defaultValue = "EXPAND")
  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public Resource init() throws ResourceInstantiationException {

    try (BufferedReader in =
        new BufferedReader(new InputStreamReader(toolsConf.openStream()))) {

      String line;
      while((line = in.readLine()) != null) {
        line = line.trim();

        if(line.length() == 0) continue;

        if(line.equals("[normalization]")) {
          expansions = new HashMap<String, Normalization>();
        } else if(expansions == null) {
          continue;
        } else if(line.charAt(0) == '[') {
          break;
        } else if(line.charAt(0) == '#') {
          continue;
        } else {
          expansions.put(line.split("\\s+", 2)[0], new Normalization(line));
        }
      }

    } catch(Exception e) {
      throw new ResourceInstantiationException(e);
    }

    return this;
  }

  @Override
  public void execute() throws ExecutionException {
    AnnotationSet annots = document.getAnnotations(annotationSetName);
    
    for (Annotation annotation : annots) {
      Object obj = annotation.getFeatures().get(Brat.NORMALIZATIONS);
      if (obj instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String,String> originals = (Map<String,String>)obj;
        Map<String,String> normalized = new HashMap<String,String>();
        
        for (Map.Entry<String,String> original : originals.entrySet()) {
          if (type.equals(Type.EXPAND)) {
            String[] data = original.getKey().split(":",2);
            Normalization normalizer = expansions.get(data[0]);
            if (normalizer == null) continue;
            normalized.put(normalizer.expand(data[1]), original.getValue());
          }
          else {
            for (Normalization normalizer : expansions.values()) {
              String collapsed = normalizer.collapse(original.getKey());
              if (collapsed != null) {
                normalized.put(collapsed, original.getValue());
                break;
              }
            }
          }
        }

        if (normalized.size() == 0) {
          annotation.getFeatures().remove(Brat.NORMALIZATIONS);
        }
        else {
          annotation.getFeatures().put(Brat.NORMALIZATIONS, normalized);
        }
      }
    } 
  }

  private static class Normalization {

    private URL home;

    private String dbName, urlBase;
    
    private Pattern pattern;

    public Normalization(String conf) throws Exception {
      String[] data = conf.split(",?\\s+");
      dbName = data[0];

      for(int i = 1; i < data.length; ++i) {
        String[] parts = data[i].split(":", 2);
        if(parts[0].equals("<URL>")) {
          home = new URL(parts[1]);
        } else if(parts[0].equals("<URLBASE>")) {
          urlBase = parts[1];
        }
      }
      
      pattern = Pattern.compile("\\Q"+urlBase.replaceAll("%s", "\\\\E(.+)\\\\Q")+"\\E");
    }
    
    public String expand(String data) {
      return urlBase.replaceAll("%s", data);
    }
    
    public String collapse(String data) {
      Matcher m = pattern.matcher(data);
      if (!m.matches()) return null;      
      return dbName+":"+m.group(1);
    }

    @Override
    public String toString() {
      return dbName + " <URL>" + home.toString() + ", <URLBASE>:" + urlBase;
    }
  }
}
