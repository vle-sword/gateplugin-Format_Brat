package gate.creole.brat;

import gate.AnnotationSet;
import gate.creole.brat.annotations.Attribute;
import gate.creole.brat.annotations.BratAnnotation;
import gate.creole.brat.annotations.Event;
import gate.creole.brat.annotations.Normalization;
import gate.creole.brat.annotations.Note;
import gate.creole.brat.annotations.Relation;
import gate.creole.brat.annotations.TextBound;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class BratUtils {

  public static void writeStandoff(AnnotationSet annots,
                                   AnnotationConfig config, PrintStream out) {
    writeStandoff(Annotations.getBratAnnotations(annots, config), out);
  }

  public static void writeStandoff(Annotations annotations, PrintStream out) {
    out.println(annotations.toString());
  }

  public static void writeJSON(AnnotationSet annots, AnnotationConfig config,
                               JsonGenerator out) throws JsonGenerationException, IOException {
    writeJSON(annots.getDocument().getContent().toString(),
      Annotations.getBratAnnotations(annots, config), out);
  }

  public static void writeJSON(String text, Annotations annotations,
                               JsonGenerator out) throws JsonGenerationException, IOException {

    // start the JSON
    out.writeStartObject();

    // the text content
    out.writeStringField("text", text);
    
    // the text bound entities
    Set<TextBound> triggers = annotations.getEventTriggers();
    Set<TextBound> entities = annotations.get(TextBound.class);
    entities.removeAll(triggers);
    appendToJSON(entities, "entities", out);

    // the event triggers
    appendToJSON(triggers, "triggers", out);

    // the attributes
    Set<Attribute> attributes = annotations.get(Attribute.class);
    appendToJSON(attributes, "attributes", out);

    // the relations
    Set<Relation> relations = annotations.get(Relation.class);
    appendToJSON(relations,"relations",out);

    // the events
    Set<Event> events = annotations.get(Event.class);
    appendToJSON(events, "events", out);

    // the normalizations
    Set<Normalization> normalizations = annotations.get(Normalization.class);
    appendToJSON(normalizations,"normalizations",out);

    // the notes (called comments in the JSON)
    Set<Note> notes = annotations.get(Note.class);
    appendToJSON(notes, "comments", out);

    // end the JSON
    out.writeEndObject();
  }

  private static <T extends BratAnnotation> void appendToJSON(Set<T> annotations,
                                                              String label,
                                                              JsonGenerator out) throws JsonGenerationException, IOException {    
    if (annotations.size() > 0) {
      out.writeArrayFieldStart(label);
      for (T annotation : annotations) {
        annotation.toJSON(out);
      }
      out.writeEndArray();
    }
  }
}
