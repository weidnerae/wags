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
 * Used with permission under the Apache License, Version 2.0.
 * 
 * BinDropController is the drop controller for a trash bin, deleting code that is dropped on it.
 * 
 * Refrigerator Magnet Microlab
 * CS 3460 - Kurtz
 * @version r20
 */

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

/**
 * Sample SimpleDropController which discards draggable widgets which are
 * dropped on it.
 */
final class BinDropController extends SimpleDropController {

	private TrashBin bin;

	public BinDropController(TrashBin bin) {
		super(bin);
		this.bin = bin;
	}

	@Override
	public void onDrop(DragContext context) {
		bin.eatWidget((StackableContainer) context.draggable);
		super.onDrop(context);
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		bin.setEngaged(true);
	}

	@Override
	public void onLeave(DragContext context) {
		bin.setEngaged(false);
		super.onLeave(context);
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		super.onPreviewDrop(context);
		if (!bin.isWidgetEater()) {
			throw new VetoDragException();
		}
	}
}
