package com.techjar.ledcm.gui;

import static org.lwjgl.opengl.GL11.*;

import com.techjar.ledcm.LEDCubeManager;
import com.techjar.ledcm.util.MathHelper;
import com.techjar.ledcm.util.Util;
import com.techjar.ledcm.render.RenderHelper;
import com.techjar.ledcm.util.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.Dimension;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author Techjar
 */
public class GUIComboBox extends GUI {
	protected UnicodeFont font;
	protected Color color;
	protected GUIBackground guiBg;
	protected GUIScrollBox scrollBox;
	protected List<ComboItem> items = new ArrayList<>();
	protected GUICallback changeHandler;
	protected int visibleItems = Integer.MAX_VALUE;

	protected int selectedItem = -1;
	protected boolean opened;

	public GUIComboBox(UnicodeFont font, Color color, GUIBackground guiBg) {
		this.font = font;
		this.color = color;
		this.guiBg = guiBg;
		this.guiBg.setParent(this);
		this.scrollBox = new GUIScrollBox(guiBg.getBorderColor());
		scrollBox.setParent(this);
		scrollBox.setX(this.guiBg.getBorderSize());
		scrollBox.setScrollXMode(GUIScrollBox.ScrollMode.DISABLED);
	}

	@Override
	protected boolean keyboardEvent(int key, boolean state, char character) {
		if (opened && !scrollBox.keyboardEvent(key, state, character)) return false;
		return true;
	}

	@Override
	protected boolean mouseEvent(int button, boolean state, int dwheel) {
		if (opened && !scrollBox.mouseEvent(button, state, dwheel)) return false;
		if (button == 0) {
			if (state) {
				Rectangle box = new Rectangle(getPosition().getX(), getPosition().getY(), dimension.getWidth(), dimension.getHeight());
				if (checkMouseIntersect(box)) {
					setOpened(!opened);
					return false;
				}
				else opened = false;
			}
		}
		return true;
	}

	@Override
	public void update(float delta) {
		if (!mouseState[0]) {
			if (checkMouseIntersect(getComponentBox())) {
				if (!opened && !hovered) LEDCubeManager.getSoundManager().playEffect("ui/rollover.wav", false);
				hovered = true;
			}
			else hovered = false;
		}
		if (opened) scrollBox.update(delta);
	}

	@Override
	public void render() {
		guiBg.render();
		RenderHelper.drawSquare(getPosition().getX() + dimension.getWidth() - 20, getPosition().getY(), 20, dimension.getHeight(), guiBg.getBorderColor());
		RenderHelper.setGlColor(hovered || opened ? Util.addColors(guiBg.getBackgroundColor(), new Color(50, 50, 50)) : guiBg.getBackgroundColor());
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_TRIANGLES);
		glVertex2f(getPosition().getX() + dimension.getWidth() - 16, getPosition().getY() + 3);
		glVertex2f(getPosition().getX() + dimension.getWidth() - 10.25f, getPosition().getY() + dimension.getHeight() - 3);
		glVertex2f(getPosition().getX() + dimension.getWidth() - 3.5f, getPosition().getY() + 3);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if (getSelectedItem() != null) {
			RenderHelper.beginScissor(new Rectangle(getPosition().getX() + guiBg.getBorderSize(), getPosition().getY() + guiBg.getBorderSize(), dimension.getWidth() - guiBg.getBorderSize() - 20, dimension.getHeight() - (guiBg.getBorderSize() * 2)));
			font.drawString(getPosition().getX() + guiBg.getBorderSize() + 3, getPosition().getY() + guiBg.getBorderSize(), getSelectedItem().toString(), Util.convertColor(color));
			RenderHelper.endScissor();
		}
		if (opened) {
			RenderHelper.drawSquare(scrollBox.getPosition().getX(), scrollBox.getPosition().getY(), scrollBox.getDimension().getWidth(), scrollBox.getDimension().getHeight(), guiBg.getBackgroundColor());
			scrollBox.render();
		}
	}

	@Override
	public Shape getComponentBox() {
		if (!opened) return super.getComponentBox();
		return super.getComponentBox().union(scrollBox.getComponentBox())[0];
	}

	@Override
	public void setDimension(Dimension dimension) {
		super.setDimension(dimension);
		guiBg.setDimension(dimension);
		scrollBox.setWidth(dimension.getWidth() - guiBg.getBorderSize() - 20);
		scrollBox.setHeight(MathHelper.clamp((dimension.getHeight() - (guiBg.getBorderSize() * 2)) * items.size(), (dimension.getHeight() - (guiBg.getBorderSize() * 2)), (dimension.getHeight() - (guiBg.getBorderSize() * 2)) * visibleItems));
		scrollBox.setScrollYIncrement(dimension.getHeight() - (guiBg.getBorderSize() * 2));
		for (ComboItem item : items) {
			item.setDimension(scrollBox.getWidth(), getHeight() - (guiBg.getBorderSize() * 2));
		}
	}

	private void updateScrollBox() {
		scrollBox.removeAllComponents();
		for (int i = 0; i < items.size(); i++) {
			ComboItem item = items.get(i);
			item.setY(i * (getHeight() - (guiBg.getBorderSize() * 2)));
			scrollBox.addComponent(item);
		}
		if (selectedItem > -1) scrollBox.setScrollOffset(new Vector2(0, selectedItem * (int)(getHeight() - (guiBg.getBorderSize() * 2))));
		setScrollBoxHeight(visibleItems);
		scrollBox.setY(dimension.getHeight() - guiBg.getBorderSize());
		for (int i = Math.min(items.size(), visibleItems); i > 0; i--) {
			if (!checkWithinContainer(scrollBox.getComponentBox())) {
				scrollBox.setY(-scrollBox.getHeight() + guiBg.getBorderSize());
				if (!checkWithinContainer(scrollBox.getComponentBox())) {
					scrollBox.setY(dimension.getHeight() - guiBg.getBorderSize());
					setScrollBoxHeight(i);
				} else break;
			} else break;
		}
	}

	private void setScrollBoxHeight(int maxItems) {
		scrollBox.setHeight(MathHelper.clamp((dimension.getHeight() - (guiBg.getBorderSize() * 2)) * items.size(), (dimension.getHeight() - (guiBg.getBorderSize() * 2)), (dimension.getHeight() - (guiBg.getBorderSize() * 2)) * maxItems));
	}

	public int getVisibleItems() {
		return visibleItems;
	}

	public void setVisibleItems(int visibleItems) {
		this.visibleItems = visibleItems;
	}

	public void setSelectedItem(int selectedItem) {
		if (selectedItem != this.selectedItem) {
			this.selectedItem = MathHelper.clamp(selectedItem, -1, items.size() - 1);
			if (changeHandler != null) {
				changeHandler.run(this);
			}
		}
	}

	public void setSelectedItem(Object o) {
		if (o == null) setSelectedItem(-1);
		else if (o instanceof ComboItem) {
			setSelectedItem(items.indexOf(o));
		}
		else {
			ComboItem item = null;
			for (ComboItem item2 : items) {
				if (o.equals(item2.getValue())) {
					item = item2;
					break;
				}
			}
			if (item == null) setSelectedItem(-1);
			else setSelectedItem(items.indexOf(item));
		}
	}

	public Object getSelectedItem() {
		if (selectedItem < 0 || selectedItem >= items.size()) return null;
		return getItem(selectedItem);
	}

	public int getSelectedIndex() {
		return selectedItem;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
		if (opened) {
			updateScrollBox();
			if (getSelectedItem() != null) scrollBox.setScrollOffset(0, (int)items.get(selectedItem).getRawPosition().getY());
		}
	}

	protected ComboItem createItem(Object value) {
		ComboItem newItem = new ComboItem(font, color, Util.addColors(guiBg.getBackgroundColor(), new Color(50, 50, 50)), Util.addColors(guiBg.getBackgroundColor(), new Color(25, 25, 25)), value);
		newItem.setDimension(scrollBox.getWidth(), getHeight() - (guiBg.getBorderSize() * 2));
		return newItem;
	}

	public boolean addAllItems(int index, Collection<? extends Object> c) {
		boolean modified = false;
		if (index <= selectedItem) selectedItem += c.size();
		for (Object o : c) {
			items.add(index++, createItem(o));
			modified = true;
		}
		if (modified) updateScrollBox();
		return modified;
	}

	public boolean addAllItems(Collection<? extends Object> c) {
		boolean modified = false;
		for (Object o : c) {
			if (items.add(createItem(o))) modified = true;
		}
		if (modified) updateScrollBox();
		return modified;
	}

	public boolean addItem(int index, Object item) {
		if (item == null) return false;
		items.add(index, createItem(item));
		if (index <= selectedItem) selectedItem++;
		updateScrollBox();
		return true;
	}

	public boolean addItem(Object item) {
		if (item == null) return false;
		boolean ret = items.add(createItem(item));
		if (ret) updateScrollBox();
		return ret;
	}

	public int getItemCount() {
		return items.size();
	}

	public Object removeItem(int index) {
		ComboItem ret = items.remove(index);
		if (index < selectedItem) selectedItem--;
		else if (index == selectedItem) setSelectedItem(-1);
		updateScrollBox();
		return ret == null ? null : ret.getValue();
	}

	public boolean removeItem(Object o) {
		if (o == null) return false;
		boolean ret = false;
		Iterator<ComboItem> it = items.iterator();
		for (int i = 0; it.hasNext(); i++) {
			ComboItem item = it.next();
			if (o.equals(item.getValue())) {
				it.remove();
				if (i < selectedItem) selectedItem--;
				else if (i == selectedItem) setSelectedItem(-1);
				ret = true;
				break;
			}
		}
		if (ret) updateScrollBox();
		return ret;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public Object getItem(int index) {
		ComboItem item = items.get(index);
		return item == null ? null : item.getValue();
	}

	public List<Object> getAllItems() {
		List<Object> list = new ArrayList<>(items.size());
		for (ComboItem item : items) {
			list.add(item.getValue());
		}
		return Collections.unmodifiableList(list);
	}

	public void clearItems() {
		items.clear();
	}

	public GUICallback getChangeHandler() {
		return changeHandler;
	}

	public void setChangeHandler(GUICallback changeHandler) {
		this.changeHandler = changeHandler;
	}

	protected class ComboItem extends GUIText {
		protected Object value;
		protected Color hoverBgColor;
		protected Color selectedBgColor;

		protected boolean pressed;

		public ComboItem(UnicodeFont font, Color color, Color hoverBgColor, Color selectedBgColor, Object value) {
			super(font, color, value.toString());
			this.value = value;
			this.hoverBgColor = hoverBgColor;
			this.selectedBgColor = selectedBgColor;
		}

		@Override
		protected boolean mouseEvent(int button, boolean state, int dwheel) {
			if (button == 0) {
				if (state) {
					Rectangle box = new Rectangle(getPosition().getX(), getPosition().getY(), dimension.getWidth(), dimension.getHeight());
					if (checkMouseIntersect(box)) {
						pressed = true;
						GUIComboBox.this.setOpened(false);
						GUIComboBox.this.setSelectedItem(this);
						return false;
					}
				}
				else pressed = false;
			}
			return true;
		}

		@Override
		public void render() {
			Shape box = getComponentBox();
			if (checkMouseIntersect(box)) RenderHelper.drawSquare(getPosition().getX(), getPosition().getY(), dimension.getWidth(), dimension.getHeight(), hoverBgColor);
			else if (GUIComboBox.this.getSelectedItem() == this.getValue()) RenderHelper.drawSquare(getPosition().getX(), getPosition().getY(), dimension.getWidth(), dimension.getHeight(), selectedBgColor);
			glTranslatef(3, 0, 0);
			super.render();
			glTranslatef(-3, 0, 0);
		}

		public Object getValue() {
			return value;
		}
	}
}
