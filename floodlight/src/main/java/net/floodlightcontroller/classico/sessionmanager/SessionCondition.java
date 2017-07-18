package net.floodlightcontroller.classico.sessionmanager;

public class SessionCondition implements ISessionCondition{

	private String serviceNameChannel;
	
	public SessionCondition(String service) {
		this.serviceNameChannel = service;
	}

	@Override
	public boolean verify(ISessionCondition sessionCondition) {
		return sessionCondition.equals(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		SessionCondition sc = (SessionCondition) obj;
		return this.serviceNameChannel.equals(sc.getServiceNameChannel());
	}
	
	public String getServiceNameChannel() {
		return serviceNameChannel;
	}

	public void setServiceNameChannel(String serviceNameChannel) {
		this.serviceNameChannel = serviceNameChannel;
	}

}
