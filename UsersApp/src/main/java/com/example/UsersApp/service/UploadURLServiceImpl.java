package com.example.UsersApp.service;
import com.example.UsersApp.entity.User;
import com.example.UsersApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@Service
public class UploadURLServiceImpl  {

    @Autowired
    UserRepo userRepo;

    public void parseSdnFile(String fileName) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(fileName));

            doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            // get <staff>
            NodeList list = doc.getElementsByTagName("users");
            Element docEle = doc.getDocumentElement();
            NodeList nl = docEle.getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("user")) {
                        String name = el.getElementsByTagName("name").item(0).getTextContent();
                        String surname = el.getElementsByTagName("surname").item(0).getTextContent();
                        String login = el.getElementsByTagName("login").item(0).getTextContent();

                    User user = new User(name, surname, login);
                    userRepo.save(user);

                    System.out.println( name + " name " + surname + " surname " + login);

                }
            }

        }} catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

