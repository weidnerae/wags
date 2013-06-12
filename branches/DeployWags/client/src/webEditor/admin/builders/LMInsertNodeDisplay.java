package webEditor.admin.builders;

import com.google.gwt.user.client.ui.Button;

public class LMInsertNodeDisplay extends BasicDisplay {
	
	public void construct(){
		this.canvas.setNodeHandler(new NH_InsertNode(canvas));
		this.basePanel.add(new Button("Click Me"));
	}
	
	@Override
	public void fillBuilder(ArgHolder child) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModify() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		
	}

}
