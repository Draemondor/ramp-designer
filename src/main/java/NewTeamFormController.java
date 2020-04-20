import Entities.User;
import List_Items.MemberListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class NewTeamFormController extends Controller implements OnFXMLChangedListener {
    public enum TEAM_MEMBER_TYPE {Manager, Leader, Member}

    @FXML
    private Button cancelBtn, createBtn;

    @FXML
    private TextField team_name;

    @FXML
    private ComboBox<String> privacy_lvl;

    @FXML
    private Label mngr_name, mngr_email, mngr_phone, privacy_note;

    @FXML
    private AnchorPane add_manager, add_leader, add_member;

    @FXML
    ListView<MemberListItem> availableList;

    private User currentUser;
    private DatabaseManager databaseManager;
    public Map<Integer, MemberListItem> addedMembers;
    private ObservableList<MemberListItem> searchedMembers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = Mediator.getInstance().getCurrentUser();
        databaseManager = DatabaseManager.getInstance();
        addedMembers = new HashMap<>();
        searchedMembers = FXCollections.observableArrayList();
        setFields();
    }

    public void setFields() {
        mngr_name.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        mngr_email.setText(currentUser.getEmail());
        mngr_phone.setText(currentUser.getPhone());

        privacy_lvl.getItems().addAll(FXCollections.observableArrayList(ApplicationResources.TEAM_PRIVACY_LEVEL));
        privacy_lvl.getSelectionModel().selectFirst();
        privacy_note.setText("Anyone can join this team.");
    }

    public void onPrivacySelected(ActionEvent event) {
        if (event.getSource() == privacy_lvl) {
            switch (privacy_lvl.getSelectionModel().getSelectedIndex()) {
                case(0):
                    privacy_note.setText("Anyone can join this team.");
                    break;
                case(1):
                    privacy_note.setText("Any active team member can add other members to this team.");
                    break;
                case(2):
                    privacy_note.setText("Only managers or team leaders can add other members to this team.");
                    break;
            }
        }
    }

    public void onAdd(MouseEvent event) {
        if (event.getSource() == add_manager) selectFromUserList(TEAM_MEMBER_TYPE.Manager);
        if (event.getSource() == add_leader) selectFromUserList(TEAM_MEMBER_TYPE.Leader);
        if (event.getSource() == add_member)selectFromUserList(TEAM_MEMBER_TYPE.Member);
    }

    public void selectFromUserList(TEAM_MEMBER_TYPE member_type) {
        searchedMembers.clear();
        switch (member_type) {
            case Manager:
                searchedMembers.addAll(databaseManager.getManagerListItems());
                break;
            case Leader:
                searchedMembers.addAll(databaseManager.getTeamLeaderListItems());
                break;
            case Member:
               searchedMembers.addAll(databaseManager.getMemberListItems());
                break;
        }
        availableList.setItems(searchedMembers);
        availableList.setCellFactory(member -> new MemberAddListCell());
    }

    public void onClick(ActionEvent event) {
        if (event.getSource() == cancelBtn) {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        }

        if (event.getSource() == createBtn) {
            System.out.println("create");
        }
    }

    /*** Custom recycled UI listCell for the listView to populate users ***/
    class MemberAddListCell extends ListCell<MemberListItem> {

        @FXML
        GridPane gridPane;

        @FXML
        Label name, email, role;

        @FXML
        Button add_user, remove_user;

        private FXMLLoader loader;

        @Override
        protected void updateItem(MemberListItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (loader == null) {
                    loader = new FXMLLoader(getClass().getResource("/fxml/user_list_item.fxml"));
                    loader.setController(this);
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                name.setText(item.getFull_name());
                email.setText(item.getEmail());
                role.setText(ApplicationResources.USER_REPUTE[item.getRole()]);
                setAssociativity(item.isSelected());

                if (addedMembers.containsKey(item.getUID()))
                    item.setSelected(true);

                if (item.isSelected()) remove_user.toFront();
                else add_user.toFront();

                setText(null);
                setGraphic(gridPane);

                add_user.setOnAction(eventEventHandler);
                remove_user.setOnAction(eventEventHandler);

            }
        }

        private void setAssociativity(Boolean isSelected) {
            if (isSelected) remove_user.toFront();
            else add_user.toFront();
        }

        EventHandler<ActionEvent> eventEventHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == add_user) {
                    getItem().setSelected(true);
                    addedMembers.put(getItem().getUID(), getItem());
                    remove_user.toFront();
                } else if (event.getSource() == remove_user) {
                    getItem().setSelected(false);
                    addedMembers.remove(getItem().getUID());
                    add_user.toFront();
                }
            }
        };
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerLoadFXML from NewTeamFormController");

    }
}
