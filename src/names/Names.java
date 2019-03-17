/*
 * Copyright 2017 DSATool team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package names;

import dsatool.gui.Main;
import dsatool.plugins.Plugin;
import dsatool.util.ErrorLogger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

/**
 * A plugin for generation of random names
 *
 * @author Dominik Helm
 */
public class Names extends Plugin {
	/**
	 * The composite for this plugin
	 */
	private NamesController controller;

	/*
	 * (non-Javadoc)
	 *
	 * @see plugins.Plugin#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "Names";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see plugins.Plugin#initialize()
	 */
	@Override
	public void initialize() {
		Main.addDetachableToolComposite("Sonstiges", "Namen", 300, 200, () -> {
			BorderPane pane = null;
			final FXMLLoader fxmlLoader = new FXMLLoader();

			controller = new NamesController();

			fxmlLoader.setController(controller);

			try {
				pane = fxmlLoader.load(getClass().getResource("Names.fxml").openStream());
			} catch (final Exception e) {
				ErrorLogger.logError(e);
			}

			controller.prepare();

			return pane;
		});
	}

	@Override
	protected void load() {}
}
