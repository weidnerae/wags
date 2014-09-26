package webEditor.magnet.view;

import webEditor.MagnetProblem;

public class MagnetProblemCreator {
	private int idAssignor = 0;
	
	public MagnetProblemCreator(){
		
	}
	
	/**
	 * @return a RefrigeratorMagnet object, created from the MagnetProblem 
	 * that was grabbed from the server using Proxy.getMagnetProblem()
	 */
	public RefrigeratorMagnet makeProblem(MagnetProblem magnet) {
		idAssignor = 0; 

		return new RefrigeratorMagnet(
				magnet,
				getMainContainer(magnet.mainFunction, magnet.problemType),
				buildFunctions(magnet.innerFunctions, magnet.problemType),
				decodePremade(magnet.statements, magnet.createdIDs, magnet.numStatements, magnet.problemType), 
				new String[][] { magnet.forLeft, magnet.forMid, magnet.forRight }
		);
	}
	
	/**
	 * Creates an array of StackableContainers from an array of 
	 * StackableContainers with the text from an array of Strings.
	 * 
	 * @param segments Array of Strings representing each code segment.
	 * 
	 * @return An array of StackableContainers. Will return null 
	 * if segments is null.
	 */
	private StackableContainer[] decodePremade(String[] segments, String[] createdIDs, int numStatements, Language problemType) {
		if (segments == null) {
			return null;
		}
		
		StackableContainer[] preMadeList = new StackableContainer[segments.length]; //should never need this many
		
		for (int i = 0; i < segments.length; i++) {
			StackableContainer sc = new StackableContainer(segments[i], Consts.STATEMENT, problemType);
			if (idAssignor > numStatements) {
				sc.setID(Integer.parseInt(createdIDs[idAssignor - numStatements - 1]));
				sc.setCreated(true);
				idAssignor++;
			} else {
				sc.setID(getID());
			}
			preMadeList[i] = sc;
		}
		return preMadeList;
	}
	
	
	/**
	 * Creates the main StackableContainer that holds all of 
	 * the other StackableContainers.
	 * 
	 * @param str The text for the main StackableContainer
	 * 
	 * @return The main StackableContainer
	 */
	private StackableContainer getMainContainer(String str, Language problemType) {
		StackableContainer sc = new StackableContainer(str, Consts.MAIN, problemType);
		sc.setID(getID());
		return sc;
	}
	
	/**
	 * Creates an array of StackableContainers from a String array
	 * 
	 * @param insideFunctions array of text for inner functions
	 * 
	 * @return An array of StackableContainers. Will return null if 
	 * insideFunctions is null.
	 */
	private StackableContainer[] buildFunctions(String[] insideFunctions, Language problemType) {
		if (insideFunctions == null) {
			return null;
		}
		
		StackableContainer[] insideFunctionsList = new StackableContainer[insideFunctions.length]; //should never need this many
		for (int i = 0; i < insideFunctions.length; i++) {
			StackableContainer sc = new StackableContainer(insideFunctions[i], Consts.INNER, problemType);
			sc.setID(getID());
			insideFunctionsList[i] = sc;
		}
		
		return insideFunctionsList;
	}
	
	/**
	 * Returns the next unused id and increments the global varaible idAssignor
	 * @return the next id number
	 */
	private int getID() {
		idAssignor++;
		return idAssignor - 1;
	}
	
	
}
