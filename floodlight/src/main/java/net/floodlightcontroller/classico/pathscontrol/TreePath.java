package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.core.types.NodePortTuple;

public class TreePath {
	
	private List<DatapathId> datapathIds;
	private List<NodePath> nodePaths;
	
	
	public TreePath() {
		datapathIds = new ArrayList<>();
		nodePaths = new ArrayList<>();
	}
	
	public void addNodePath(NodePath nodePath){
		//if(!nodePaths.contains(nodePath))
			nodePaths.add(nodePath);
			datapathIds.add(nodePath.getDataPathId());
	}
	
	public boolean containsNode(NodePortTuple npt){
		return datapathIds.contains(npt.getNodeId());
	}

	public List<NodePath> getNodePaths() {
		return nodePaths;
	}

	public void setNodePaths(List<NodePath> nodePaths) {
		this.nodePaths = nodePaths;
	}

	public NodePath getNodePathByRef(DatapathId datapathId) {
		for (NodePath nodePath : nodePaths) {
			if(nodePath.getDataPathId().equals(datapathId)){
				return nodePath;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "TreePath [datapathIds=" + datapathIds + ", nodePaths=" + nodePaths + "]";
	}

	
	
}
