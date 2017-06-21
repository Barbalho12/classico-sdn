package net.floodlightcontroller.mactracker;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.statistics.IStatisticsService;
import net.floodlightcontroller.statistics.SwitchPortBandwidth;
import net.floodlightcontroller.threadpool.IThreadPoolService;

public class Statistics implements IStatisticsService{




	@Override
	public SwitchPortBandwidth getBandwidthConsumption(DatapathId dpid, OFPort p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<NodePortTuple, SwitchPortBandwidth> getBandwidthConsumption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collectStatistics(boolean collect) {
		// TODO Auto-generated method stub
		
	}

}
