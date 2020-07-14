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

import dsa41basis.util.DSAUtil;
import dsatool.resources.ResourceManager;
import dsatool.ui.ReactiveSpinner;
import dsatool.util.ErrorLogger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import jsonant.value.JSONObject;

public class NamesController {
	BorderPane pane;
	@FXML
	private ReactiveSpinner<Integer> count;
	@FXML
	private ComboBox<String> culture;
	@FXML
	private TextArea results;
	@FXML
	private ReactiveSpinner<Integer> male;
	@FXML
	private ReactiveSpinner<Integer> female;
	@FXML
	private ReactiveSpinner<Integer> middleClass;
	@FXML
	private ReactiveSpinner<Integer> noble;

	JSONObject generators;

	public NamesController() {
		final FXMLLoader fxmlLoader = new FXMLLoader();

		fxmlLoader.setController(this);

		try {
			pane = fxmlLoader.load(getClass().getResource("Names.fxml").openStream());
		} catch (final Exception e) {
			ErrorLogger.logError(e);
		}

		male.valueProperty().addListener((e, oldV, newV) -> female.getValueFactory().setValue(100 - newV));
		female.valueProperty().addListener((e, oldV, newV) -> male.getValueFactory().setValue(100 - newV));

		middleClass.valueProperty().addListener((e, oldV, newV) -> {
			if (newV + noble.getValue() > 100) {
				noble.getValueFactory().setValue(100 - newV);
			}
		});
		noble.valueProperty().addListener((e, oldV, newV) -> {
			if (newV + middleClass.getValue() > 100) {
				middleClass.getValueFactory().setValue(100 - newV);
			}
		});

		prepare();
	}

	@FXML
	private void create() {
		final StringBuilder resultsText = new StringBuilder();

		for (int i = 0; i < count.getValue(); ++i) {
			final boolean isMale = DSAUtil.random.nextDouble() < male.getValue() / 100.0;
			final boolean isNoble = DSAUtil.random.nextDouble() < noble.getValue() / 100.0;
			final boolean isMiddleClass = isNoble || DSAUtil.random.nextDouble() < middleClass.getValue() / 100.0;
			resultsText.append(DSAUtil.getRandomName(generators.getArr(culture.getSelectionModel().getSelectedItem()), isMale, isMiddleClass, isNoble));
			resultsText.append(" (");
			resultsText.append(isMale ? 'm' : 'w');
			if (isMiddleClass) {
				resultsText.append(", ");
				resultsText.append(isNoble ? 'a' : 'b');
			}
			resultsText.append(")\n");
		}

		resultsText.append(results.getText());
		results.setText(resultsText.toString());
	}

	public Node getRoot() {
		return pane;
	}

	public void prepare() {
		generators = ResourceManager.getResource("data/Namen");
		culture.setItems(FXCollections.observableArrayList(generators.keySet()));
		culture.getSelectionModel().select(0);
	}
}
