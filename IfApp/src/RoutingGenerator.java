import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class RoutingGenerator {
	public static String generateRouting()
	{
		String routingXmlString;
		//创建根节点
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("Routing");
		Element signalElement = rootElement.addElement("signal");
		Element adapterElement = rootElement.addElement("adapter");
		Element signalListenerElement = rootElement.addElement("signalListener");
		routingXmlString = document.asXML();
		System.out.println(routingXmlString);
		return null;
	}

}
