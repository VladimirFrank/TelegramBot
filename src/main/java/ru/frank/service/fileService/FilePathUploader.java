package ru.frank.service.fileService;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.events.Attribute;
import java.io.IOException;

public class FilePathUploader {

    private DocumentBuilder documentBuilder;
    private Document document;
    private Node root;

    public String getCrossJournalPath(String officeLocationLetter){
        try{
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse("F:\\JavaProjects\\TelegramBot\\src\\main\\resources\\filesLocation.xml");
            document.getDocumentElement().normalize();

            root = document.getDocumentElement();
            NodeList pathList = document.getElementsByTagName("crossJournal");

            String officeName;
            if(officeLocationLetter.equalsIgnoreCase("а")){
                officeName = "aurora";
            } else if(officeLocationLetter.equalsIgnoreCase("т")){
                officeName = "technopark";
            } else{
                return "";
            }

            for(int i = 0; i < pathList.getLength(); i++){
                Node node = pathList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    if(element.getAttribute("id").equals(officeName)){
                        return element.getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return "";
    }
}
