package Server;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class adds clients to the register in the xml form
 * @author eminamuratovic
 */
public class XMLConnection {
	private static Document xmldoc;
	private static DocumentBuilder docbuilder;
	private static XPath xPath;

	public XMLConnection() throws ParserConfigurationException, SAXException,
			IOException {
		docbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		xmldoc = docbuilder.parse(new File("./User/user.xml"));
		xPath = XPathFactory.newInstance().newXPath();

	}

	public static int userLogin(String username, String password) {
		String expression = "//user[@username = \"" + username
				+ "\" and @password = \"" + password + "\"]";
		System.out.println(expression);
		try {
			Node user = (Node) xPath.compile(expression).evaluate(xmldoc, XPathConstants.NODE);
			if (user == null) {
				String exp =  "//user[@username = \"" + username+ "\"]";
				Node userNode =  (Node) xPath.compile(exp).evaluate(xmldoc, XPathConstants.NODE);
				if(userNode == null) {
					Element newuser = xmldoc.createElement("user");
					newuser.setAttribute("username", username);
					newuser.setAttribute("password", password);
					xmldoc.getElementsByTagName("users").item(0).appendChild(newuser);
					StreamResult file = new StreamResult(new File("./User/user.xml"));
					Transformer transformer;
					try {
						transformer = TransformerFactory.newInstance().newTransformer();
						DOMSource source = new DOMSource(xmldoc);
						transformer.transform(source, file);
					} catch (TransformerFactoryConfigurationError
							| TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return -4;
					}
				}
				else
					return -1;
			}
				
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		}
		return 0;
		
	}
	public static void main(String[] args) {
		try {
			XMLConnection conn = new XMLConnection();
			int i = XMLConnection.userLogin("Sanelaa", "sanela");
			System.out.println(i);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
