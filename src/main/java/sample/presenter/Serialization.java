package sample.presenter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.message.AbstractNewAutomatonMessage;
import sample.model.AbstractAutomaton;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Serialization {

    private static FileChooser chooser;
    private static final String COLUMNS = "Columns";
    private static final String COLUMN = "Column";
    private static final String ROWS = "Rows";
    private static final String ROW = "Row";
    private static final String NUMBER_OF_STATE = "NumberOfState";
    private static final String TORUS = "Torus";
    private static final String MOORE_NEIGHBOR_HOOD = "MooreNeighborHood";
    private static final String ACTIVE_STATE = "ActiveState";

    static {
        String[] array = new String[]{".ser", ".xml"};
        chooser = new FileChooser();
        File dir = new File(".");
        chooser.setInitialDirectory(dir);
        for (String s : array) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(s, "*.ser", "*.xml");
            chooser.getExtensionFilters().add(extFilter);
        }
    }

    public void saveAutomaton(AbstractNewAutomatonMessage automaton, Stage stage) {
        chooser.setTitle("Automaton serialisiert speichern");
        File file = chooser.showSaveDialog(stage);
        if (file == null || automaton.getAutomaton() == null) {
            return;
        }
        String appendIfMissing = StringUtils.appendIfMissing(file.getName(), ".ser");
        file = new File(file.getParent() + File.separator + appendIfMissing);

        try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            synchronized (automaton.getAutomaton()) {
                os.writeObject(automaton.getAutomaton());
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            Service.alert("Fehler beim Speichern!", "");
        }
    }

    /**
     * Gespeichertes Automaton wird geladen
     * Falls gespeichertes Automaton sich nicht laden lässt, bleibt aktuelles Automaton
     *
     * @param automaton Aktuelles Automaton
     * @param stage     Stage
     * @return Entweder gespeichertes oder aktuelles Automaton
     */
    public AbstractAutomaton loadAutomaton(AbstractAutomaton automaton, Stage stage) {
        chooser.setTitle("Serialisierte Automaton laden");
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
                return (AbstractAutomaton) is.readObject();
            } catch (Exception exc) {
                exc.printStackTrace();
                Service.alert("Gespeichertes Automaton konnte nicht geladen werde! Altes Automaton bleibt unverändert", "");
            }
        }
        return automaton;
    }

    public void saveXML(AbstractNewAutomatonMessage automaton, Stage stage) {
        chooser.setTitle("Automaton serialisiert in XML speichern");
        File file = chooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        String filename = StringUtils.splitPreserveAllTokens(file.getName(), ".")[0];
        String appendIfMissing = StringUtils.appendIfMissing(filename, ".xml");
        file = new File(file.getParent() + File.separator + appendIfMissing);
        Document xmlDoc = Objects.requireNonNull(getDoc()).newDocument();
        if (xmlDoc != null && automaton.getAutomaton() != null) {
            Element element = createElement(xmlDoc, automaton.getAutomaton());
            xmlDoc.appendChild(element);
            writeXML(xmlDoc, file.toPath());
        } else {
            Service.alert("XML konnte nicht erstellt werden! Gründe: Automaton ist leer oder Document lässt sich nicht erzeugen", "XML");
        }
    }

    public AbstractAutomaton loadXML(AbstractAutomaton automaton, Stage stage) {
        chooser.setTitle("XML laden");
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return loadXMLAutomaton(automaton, inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return automaton;
    }

    /**
     * Falls Document konnte nicht erstellt werden oder XML-Datei ist kaput, bleibt aktuelles Automaton, sonst wird gespeichertes geladen
     * @param automaton Aktuelles Automaton
     * @param input File
     * @return Entweder aktuelles oder gespeichertes Automaton
     */
    private AbstractAutomaton loadXMLAutomaton(AbstractAutomaton automaton, InputStream input) {
        XPathExpression expression = getXPathExpression();
        DocumentBuilder builder = getDoc();
        if (expression != null) {
            try {
                Document xmlDoc = Objects.requireNonNull(builder).parse(input);
                Node results = (Node) expression.evaluate(xmlDoc, XPathConstants.NODE);
                if (results == null) {
                    Service.alert("Fehlschlag: XML-Datei konnte nicht gelesen werden", "");
                    throw new IOException(": Fehlschlag: XML-Datei konnte nicht gelesen werden");
                }
                if (results.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) results;
                    automaton.setRows(Integer.parseInt(element.getAttribute(ROWS)));
                    automaton.setColumns(Integer.parseInt(element.getAttribute(COLUMNS)));
                    automaton.setNumberOfStates(Integer.parseInt(element.getAttribute(NUMBER_OF_STATE)));
                    automaton.setTorus(Boolean.parseBoolean(element.getAttribute(TORUS)));
                    automaton.setMooreNeighborHood(Boolean.parseBoolean(element.getAttribute(MOORE_NEIGHBOR_HOOD)));
                    automaton.setCells(Integer.parseInt(element.getAttribute(ROWS)), Integer.parseInt(element.getAttribute(COLUMNS)));
                    automaton.changeSize(Integer.parseInt(element.getAttribute(ROWS)), Integer.parseInt(element.getAttribute(COLUMNS)));
                    NodeList list = results.getChildNodes();
                    for (int i = 0; i < list.getLength(); i++) {
                        Node a = list.item(i);
                        if (a.getNodeType() == Node.ELEMENT_NODE) {
                            Element e = (Element) a;
                            automaton.setNewState(Integer.parseInt(e.getAttribute(ROW)), Integer.parseInt(e.getAttribute(COLUMN)),
                                    Integer.parseInt(e.getAttribute(ACTIVE_STATE)));
                        }
                    }
                    return automaton;
                }
            } catch (IOException | SAXException | XPathExpressionException e) {
                Service.alert("Document kann nicht analysiert werden", "Document analysieren");
            }
        } else {
            Service.alert("Problem mit XPathExpression", "XPathExpression analysieren");
        }
        return automaton;
    }

    private XPathExpression getXPathExpression() {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            return xPath.compile("/Automaton");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DocumentBuilder getDoc() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            Service.alert("Document kann nicht erstellt werden", "DocumentBuilder");
        }
        return null;
    }

    /**
     * Erstellen der XML-Struktur
     *
     * @param xmlDoc    Aktuelles Doc
     * @param automaton Automaton, das gespeichert werden soll
     * @return Element
     */
    private Element createElement(Document xmlDoc, AbstractAutomaton automaton) {
        Element rootElement = xmlDoc.createElement("Automaton");
        setElement(rootElement, automaton);
        for (int r = 0; r < automaton.getRows(); r++) {
            for (int c = 0; c < automaton.getColumns(); c++) {
                Element cellElement = xmlDoc.createElement("Cell");
                cellElement.setAttribute(ROW, StringUtils.join(r));
                cellElement.setAttribute(COLUMN, StringUtils.join(c));
                cellElement.setAttribute(ACTIVE_STATE, StringUtils.join(automaton.getCell(r, c).getState()));
                rootElement.appendChild(cellElement);
            }
        }
        return rootElement;
    }

    /**
     * Die Atributten werden an Element angehängt
     *
     * @param rootElement Element
     * @param automaton   Automaton, das gespeichert werden soll
     */
    private void setElement(Element rootElement, AbstractAutomaton automaton) {
        rootElement.setAttribute(ROWS, StringUtils.join(automaton.getRows()));
        rootElement.setAttribute(COLUMNS, StringUtils.join(automaton.getColumns()));
        rootElement.setAttribute(NUMBER_OF_STATE, StringUtils.join(automaton.getNumberOfStates()));
        rootElement.setAttribute(TORUS, StringUtils.join(StringUtils.join(automaton.isTorus())));
        rootElement.setAttribute(MOORE_NEIGHBOR_HOOD, StringUtils.join(automaton.isMooreNeighborHood()));
    }

    private void writeXML(Document xmlDoc, Path path) {
        try (FileOutputStream outputStream = new FileOutputStream(path.toString())) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);
            StreamResult result = new StreamResult(outputStream);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (Exception e) {
            Service.alert("Datei kann nicht gesprieben werden!", "Datei schreiben");
        }
    }
}