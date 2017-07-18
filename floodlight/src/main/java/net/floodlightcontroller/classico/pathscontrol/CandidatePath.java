package net.floodlightcontroller.classico.pathscontrol;

import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.routing.Path;

public class CandidatePath extends Path{
	
	private long bandwidthConsumption;
	private List<Link> links;
	
	public CandidatePath(DatapathId src, DatapathId dst) {
		super(src, dst);
	}
	
	public CandidatePath(Path path) {
		super(path.getId(), path.getPath());
		setHopCount(path.getHopCount());
		setLatency(path.getLatency());
	}

	public long getBandwidthConsumption() {
		return bandwidthConsumption;
	}

	public void setBandwidthConsumption(long bandwidthConsumption) {
		this.bandwidthConsumption = bandwidthConsumption;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	

}
