package webEditor.magnet.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import webEditor.Common.Model;
import webEditor.Common.Presenter;

public class MagnetProblemModel implements Model {
	
	private ArrayList<Presenter> observers;
	
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
		observers = new ArrayList<Presenter>();
	}
	
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
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
	
	@Override
	public void registerObserver(Presenter presenter) {
		observers.add(presenter);
	}

	@Override
	public void removeObserver(Presenter presenter) {
		observers.remove(presenter);
	}

	@Override
	public void notifyObservers() {
		List<String> data = getData();
		for (Presenter pres: observers) {
			pres.update(data);
		}
	}

}
