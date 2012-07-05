package webEditor.magnet.client;

/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Daniel Cook, Alex Weidner, Reed Phillips
 * Refrigerator Magnet Microlab
 * CS 3460 - Kurtz
 * 
 * Functionality is available to be able to drag code into a trash bin in order to delete it and clean
 * up the work space.
 * @version r27
 */

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * Panel which updates its label to display the number of items in the trash.
 */
final class TrashBin extends AbsolutePanel {

	private static final String CSS_TRASHBIN = "trash_bin";

	private static final String CSS_TRASHBIN_ENGAGE = "trash_bin-engage";

	public TrashBin() {
		HTML text = new HTML("<b>Trash Bin</b>");
		text.setStyleName("trash_label");
		add(text);
		setStyleName(CSS_TRASHBIN);
	}

	public void eatWidget(StackableContainer sc) {
		sc.removeFromParent();
	}

	public boolean isWidgetEater() {
		return true;
	}

	public void setEngaged(boolean engaged) {
		if (engaged) {
			setStyleName(CSS_TRASHBIN_ENGAGE);
		} else {
			setStyleName(CSS_TRASHBIN);
		}
	}

}
