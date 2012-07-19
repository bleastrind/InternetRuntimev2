package models;

public class RoutingChoice{
	private String signalName;
	private String signalAppName;
	private String signalDescription;
	private String listenerAppName;
	private String listenerDescription;
	private String routing;
	
	public RoutingChoice(String signalName,String signalAppName,String signalDescription,String listenerAppName,String listenerDescription,String routing){
		this.signalName =signalName ;
		this.signalAppName = signalAppName;
		this.signalDescription=signalDescription;
		this.listenerAppName=listenerAppName;
		this.listenerDescription=listenerDescription;
		this.routing=routing;
	}
	
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getSignalName() {
		return signalName;
	}
	public void setSignalDescription(String signalDescription) {
		this.signalDescription = signalDescription;
	}
	public String getSignalDescription() {
		return signalDescription;
	}

	public void setListenerDescription(String listenerDescription) {
		this.listenerDescription = listenerDescription;
	}
	public String getListenerDescription() {
		return listenerDescription;
	}
	public void setRouting(String routing) {
		this.routing = routing;
	}
	public String getRouting() {
		return routing;
	}

	public void setSignalAppName(String signalAppName) {
		this.signalAppName = signalAppName;
	}

	public String getSignalAppName() {
		return signalAppName;
	}

	public void setListenerAppName(String listenerAppName) {
		this.listenerAppName = listenerAppName;
	}

	public String getListenerAppName() {
		return listenerAppName;
	}
	
	@Override
	public boolean equals(Object obj){
		return routing.equals(((RoutingChoice)obj).routing);
	}
	
	@Override
	public int hashCode(){
		return routing.hashCode();
	}
}