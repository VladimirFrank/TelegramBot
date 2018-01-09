package ru.frank.service.fileService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by sbt-filippov-vv on 28.12.2017.
 */
public class UserAccessChecker {

    private DocumentBuilder documentBuilder;
    private Document document;
    private Node root;

    private String pathToUsersAccessFile ="C:\\Users\\dfnote021\\IdeaProjects\\TelegramBot\\src\\main\\resources\\usersWithAccess";

    public boolean checkUserAccess(String userId){
        try{
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder() ;
            document = documentBuilder.parse(pathToUsersAccessFile);
            document.getDocumentElement().normalize();

            root = document.getDocumentElement();
            NodeList pathList = document.getElementsByTagName("user");

            for(int i = 0; i < pathList.getLength(); i++){
                Node node = pathList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    if(element.getTextContent().equals(userId)){
                        return true;
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
        return false;
    }
}
