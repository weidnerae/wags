package webEditor.magnet.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The creation station is a panel that consists of a series of dropdowns, and
 * from those dropdowns the students can select options to create their own
 * custom magnets.
 * 
 */

public class CreationStation extends VerticalPanel {
	private static final int FOR = 1;
	private static final int WHILE = 2;
	private static final int IF = 3;
	private static final int ELSE_IF = 4;
	private static final int ELSE = 5;
	private String[] structuresList = Consts.STRUCTURES_LIST;
	private int[] limits;
	private ConstructUi constructPanel; // the left hand side of the magnets UI
	private Button createButton = new Button("Create");
	private MenuBar structures;
	private ListBox[] forConditions;
	private ListBox booleanConditions;

	/* different use cases are represented by panels, each for the type of decision structure */
	private HorizontalPanel elsePanel = new HorizontalPanel();
	private HorizontalPanel forPanel = new HorizontalPanel();
	private HorizontalPanel booleanPanel = new HorizontalPanel();
	private HorizontalPanel topAlignPanel = new HorizontalPanel();

	private MenuBar structureOptions;
	private int selectedStructureIndex = 0;
	@SuppressWarnings("unused")
	private Label selectedStructureCounter;

	final PickupDragController dc;

	private int nextID;

	public CreationStation(String[][] forLists, String[] booleanList,
			int[] limits, ConstructUi constructPanel, PickupDragController dc,
			int nextID) {
		setStyleName("dropdown_panel");
		this.dc = dc;
		this.limits = limits;
		this.constructPanel = constructPanel;
		this.nextID = nextID;

		// set up Structures MenuBar(used as a ListBox but we can set html for the elements)
		structures = new MenuBar(true);
		setupStructures(structures, structuresList);
		selectedStructureCounter = new Label();

		// do the leg work of turning string arrays into list boxes
		forConditions = new ListBox[3];
		forConditions[0] = setupListBox(forLists[0]);
		forConditions[1] = setupListBox(forLists[1]);
		forConditions[2] = setupListBox(forLists[2]);
		booleanConditions = setupListBox(booleanList);

		// add the topAlignPanel because it contains the structures listbox
		topAlignPanel.add(structures);
		add(topAlignPanel);

		/* set up panels for each decision structure so they can be ready to swap to */
		forPanel.add(new HTML("&nbsp ( &nbsp"));
		forPanel.add(forConditions[0]);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(forConditions[1]);
		forPanel.add(new HTML("&nbsp ; &nbsp"));
		forPanel.add(forConditions[2]);
		forPanel.add(new HTML("&nbsp ) &nbsp"));

		booleanPanel.addStyleName("boolean_conditions");

		booleanPanel.add(new HTML("&nbsp ( &nbsp"));
		booleanPanel.add(booleanConditions);
		booleanPanel.add(new HTML("&nbsp ) &nbsp"));

		// finally make the create button and add it to the bottom of the panel
		createButton.addClickHandler(new CreateHandler());
		createButton.addStyleName("create_button");

		add(createButton);
		setCellHorizontalAlignment(createButton,HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	public void updateStructureOptions() {
		structureOptions.clearItems();
		String menuItemHTML;
		for (int i = 1; i < structuresList.length; i++) {
			String css = null;
			if (limits[i - 1] > 0) {
				css = "structureLimitAvailable";
				
			} else {
				css = "structureLimitUnvailable";
			}
			
			menuItemHTML = "<div><p style=\"margin:0px\">" + structuresList[i] + "<span style=\"float:right;\" class=\"" + css + "\">" + limits[i - 1] + "</span></div>";

			final int target = i;
			Command command = new Command() {
				public void execute() {
					showDropdowns(target);
					selectedStructureIndex = target;
					updateStructure();
				}
			};
			structureOptions.addItem(menuItemHTML, true, command);

			if (i != structuresList.length - 1) {
				structureOptions.addSeparator();
			}

			updateStructure();
		}
	}

	public void updateStructure() {
		structures.clearItems();
		if (selectedStructureIndex == 0) {
			structures.addItem("<div style=\"display:inline-block;min-width:110px;\" >" + structuresList[selectedStructureIndex] + "</div>", true, structureOptions);
			return;
		}
		
		String css = null;
		if (limits[selectedStructureIndex - 1] > 0) {
			css = "structureLimitAvailable";
		} else {
			css = "structureLimitUnvailable";
		}
		
		String html = "<div style=\"display:inline-block;min-width:110px;\" >"
					+ structuresList[selectedStructureIndex]
					+ "<span style=\"float:right;\" class=\"" + css + "\">"
					+ limits[selectedStructureIndex - 1]
					+ "</span></div>";
		structures.addItem(html, true, structureOptions);

		createButton.setHTML("Create: " + limits[selectedStructureIndex - 1] + " left");
	}

	// takes the string array and turns it into a usable listbox for the decision structures
	public void setupStructures(MenuBar structures, String[] structuresList) {
		structureOptions = new MenuBar(true);
		structures.setAnimationEnabled(true);

		updateStructureOptions();
	}

	// takes the string array and turns it into a usable listbox for the
	// arguments used in decision structures
	public ListBox setupListBox(String[] listOptions) {
		if (listOptions == null) {
			return null;
		}

		ListBox listBox = new ListBox();
		for (String option : listOptions) {
			listBox.addItem(option);
		}
		return listBox;
	}

	// utility methods used to change what dropdowns are available to the
	// student using the creation station
	public void clearDropdowns() {
		topAlignPanel.clear();
		topAlignPanel.add(structures);
	}

	public void showDropdowns(int structure) {
		topAlignPanel.clear();
		topAlignPanel.add(structures);
		
		switch (structure) {
		case FOR:		topAlignPanel.add(forPanel); break;
		case WHILE:		
		case IF:		
		case ELSE_IF: 	topAlignPanel.add(booleanPanel); break;
		case ELSE: 		topAlignPanel.add(elsePanel); break;
		default: break;
		}
	}

	public void incrementLimitCounter(int i) {
		limits[i]++;
		updateStructureOptions();
	}
 
	/**
	 * Create a stackable container and add it to the Construct Panel
	 *
	 */
	private class CreateHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (selectedStructureIndex == 0) {
				return;
			} else if (limits[selectedStructureIndex - 1] <= 0) {
				Window.alert("Error: You have reached your limit of " + structuresList[selectedStructureIndex] + "'s ");
				return;
			}
			
			// start with a null SC and add the necessary parts to it as we go
			StackableContainer createdContainer = null;

			switch (selectedStructureIndex) {
			case FOR:
				createdContainer = new StackableContainer(Consts.FOR, dc, Consts.STATEMENT);
				String left =  forConditions[0].getItemText(forConditions[0].getSelectedIndex());
				String mid =   forConditions[1].getItemText(forConditions[1].getSelectedIndex());
				String right = forConditions[2].getItemText(forConditions[2].getSelectedIndex());
				String forLoop = left + "; " + mid + "; " + right;
				createdContainer.addConditionContent(forLoop);
				break;

			case WHILE:
				createdContainer = new StackableContainer(Consts.WHILE, dc, Consts.STATEMENT);
				createdContainer.addConditionContent((booleanConditions.getItemText(booleanConditions.getSelectedIndex())));
				break;

			case IF:
				createdContainer = new StackableContainer(Consts.IF, dc, Consts.STATEMENT);
				createdContainer.addConditionContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()));

			case ELSE_IF:
				createdContainer = new StackableContainer(Consts.ELSEIF, dc, Consts.STATEMENT);
				createdContainer.addConditionContent(booleanConditions.getItemText(booleanConditions.getSelectedIndex()));
				break;
				
			case ELSE:
				createdContainer = new StackableContainer(Consts.ELSE, dc, Consts.STATEMENT);
				break;
				
			default:
				break;
			}

			createdContainer.setID("" + nextID++);
			constructPanel.addSegment(createdContainer);
			limits[selectedStructureIndex - 1]--;
			updateStructureOptions();
		}
	}
}