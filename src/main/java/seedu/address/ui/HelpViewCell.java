package seedu.address.ui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Custom ListCell which is displayed using a HelpCard
 */
// @@author A0116603R
public class HelpViewCell extends ListCell<HelpList.HelpItem> implements EntryViewCell {

    static <T> Callback<ListView<T>, ListCell<HelpList.HelpItem>> getFactory() {
        return param -> new HelpViewCell();
    }

    @Override
    public void updateItem(HelpList.HelpItem helpItem, boolean empty) {
        super.updateItem(helpItem, empty);
        if(empty || helpItem == null) {
            setGraphic(null);
            setText(null);
        } else {
            HelpCard helpCard = new HelpCard();
            helpCard.init(helpItem);
            setGraphic(helpCard);
        }

    }
}
