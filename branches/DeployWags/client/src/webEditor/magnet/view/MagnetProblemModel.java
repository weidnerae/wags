package webEditor.magnet.view;

import java.util.ArrayList;
import java.util.List;

import webEditor.Common.Model;

public class MagnetProblemModel extends Model {
	
	
	public String problemType;
	public String solution;
	public String state;
	public int id;
	public StackableContainer mainFunction;
	public StackableContainer[] insideFunctions;
	public StackableContainer[] premadeFunctions;
	public int[] limits;
	
	public MagnetProblemModel()
	{
		super();
	}

	@Override
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		return data;
	}
	
	public void setProblemType(String problemType, boolean toUpdate)
	{
		this.problemType = problemType;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setProblemSolution(String solution, boolean toUpdate) {
		this.solution = solution;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setId(int id, boolean toUpdate) {
		this.id = id;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setMainFunction(StackableContainer mainFunction, boolean toUpdate) {
		this.mainFunction = mainFunction;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setInsideFunctions(StackableContainer[] insideFunctions, boolean toUpdate) {
		this.insideFunctions = insideFunctions;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setPremadeFunctions(StackableContainer[] premadeFunctions, boolean toUpdate) {
		this.premadeFunctions = premadeFunctions;
		if (toUpdate) {
			notifyObservers();
		}
	}
	
	public void setLimits(int[] limits, boolean toUpdate) {
		this.limits = limits;
		if (toUpdate) {
			notifyObservers();
		}
	}
	public String getProblemType() {
		return problemType;
	}
	
	public String getSolution() {
		return solution;
	}

	public String getState() {
		return state;
	}
	
	public int getId() {
		return id;
	}
	
	public StackableContainer getMainFunction() {
		return mainFunction;
	}
	
	public StackableContainer[] getInsideFunctions() {
		return insideFunctions;
	}
	
	public StackableContainer[] getPremadeFunctions() {
		return premadeFunctions;
	}
	
	public int[] getLimits() {
		return limits;
	}
}
