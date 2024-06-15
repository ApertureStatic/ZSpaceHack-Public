package dev.zspacehack.gui.tabs;

import dev.zspacehack.gui.HudManager;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.gui.elements.CheckboxComponent;
import dev.zspacehack.gui.elements.SliderComponent;
import dev.zspacehack.gui.elements.StringComponent;

public class OptionsTab extends ClickGuiTab {

	private StringComponent uiSettingsString = new StringComponent("UI Settings", this);
	
	public OptionsTab(String title, int x, int y) {
		super(title, x, y);
		this.setWidth(180);
		this.addChild(uiSettingsString);
	}

	public void addChild(SliderSetting sliderSetting) {
		HudManager.SETTINGS.add(sliderSetting);
		addChild(new SliderComponent(this, sliderSetting));
	}

	public void addChild(BooleanSetting booleanSetting) {
		HudManager.SETTINGS.add(booleanSetting);
		addChild(new CheckboxComponent(this, booleanSetting));
	}
}
