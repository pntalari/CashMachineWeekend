package rocks.zipcode.atm;

import com.sun.tools.javadoc.DocImpl;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import rocks.zipcode.atm.bank.AccountData;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;

import javax.xml.soap.Text;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    // declarations for fields, texts, labels and buttons on the Account View form
    private TextField fieldAcctID = new TextField();
    private Label lblEnterAcct = new Label("Enter Account ID to login:");
    private Label lblAcctID = new Label("Account ID:");
    private TextField txtAcctID = new TextField();
    private Label lblAcctName = new Label("Account Name:");
    private TextField txtAcctName = new TextField();
    private Label lblEmailID = new Label("Email ID:");
    private TextField txtEmailID = new TextField();
    private Label lblAcctBalance = new Label("Account Balance:");
    private TextField txtAcctBalance = new TextField();
    private Label lblAmtEntries = new Label("Enter Transaction Amount:");
    private TextField txtAmtEntries = new TextField();
    private Button btnLogin = new Button("Login to Account");
    private Button btnDeposit = new Button("Deposit");
    private Button btnWithdraw = new Button("Withdraw");
    private Button btnLogOut = new Button("Log Out");

    // CashMachine object
    private CashMachine cashMachine = new CashMachine(new Bank());


    //content creation for the form layout
    private Parent createContent() {

        // styling of the vbox display
        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 500);
        vbox.setStyle("-fx-border-style: solid inside;");
        vbox.setStyle("-fx-border-color: #12d0ff;");
        vbox.setStyle("-fx-border-width: 2;");
        vbox.setStyle("-fx-border-insets: 5;");
        vbox.setStyle("-fx-border-radius: 5");

        // set the account data fields to non-editable
        txtAcctID.setEditable(false);
        txtAcctName.setEditable(false);
        txtEmailID.setEditable(false);
        txtAcctBalance.setEditable(false);


// Login button click action
        btnLogin.setOnAction(e -> {
            try {
                int id = Integer.parseInt(fieldAcctID.getText());
                cashMachine.login(id);
                fieldAcctID.setStyle("-fx-border-color: #12d0ff;");
            } catch (Exception error) {
                failedAlert(error);
            }
            setFields();
        });

// Deposit button click action
        btnDeposit.setOnAction(e -> {
            double amount = Double.parseDouble(txtAmtEntries.getText());
            cashMachine.deposit(amount);
            setFields();
        });

// Withdraw button click action
        btnWithdraw.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(txtAmtEntries.getText());
                cashMachine.withdraw(amount);
            } catch (Exception error) {
                failedAlert(error);
            }
            setFields();
        });

// Logout button click action
        btnLogOut.setOnAction(e -> {
            cashMachine.exit();
            txtAcctID.setText("");
            txtAcctName.setText("");
            txtEmailID.setText("");
            txtAcctBalance.setText("");
            txtAmtEntries.setVisible(false);
            lblAmtEntries.setVisible(false);
        });

// Adding children components to the flowpane
        FlowPane flowpane = new FlowPane();
        flowpane.setPadding(new Insets(10, 10, 10, 10));
        flowpane.setAlignment(Pos.BOTTOM_CENTER);
        flowpane.getChildren().add(btnLogin);
        flowpane.getChildren().add(btnDeposit);
        flowpane.getChildren().add(btnWithdraw);
        flowpane.getChildren().add(btnLogOut);

        vbox.getChildren().addAll(menu(),lblEnterAcct,fieldAcctID, lblAcctID, txtAcctID, lblAcctName, txtAcctName,
                lblEmailID, txtEmailID, lblAcctBalance, txtAcctBalance, lblAmtEntries, txtAmtEntries, flowpane);

        txtAmtEntries.setVisible(false);
        lblAmtEntries.setVisible(false);
        btnDeposit.setDisable(true);
        btnWithdraw.setDisable(true);
        btnLogOut.setDisable(true);

        return vbox;
    }

    //create Menu and MenuItems
    public static MenuBar menu(){
        //Create menu and menuitems with list of Account IDs
        Menu acctMenu = new Menu("AccountID List");
        MenuItem m1 = new MenuItem("1000");
        MenuItem m2 = new MenuItem("2000");
        MenuItem m3 = new MenuItem("3000");
        MenuItem m4 = new MenuItem("4000");

        //Add menuitems to the created Menu
        acctMenu.getItems().add(m1);
        acctMenu.getItems().addAll(m2);
        acctMenu.getItems().addAll(m3);
        acctMenu.getItems().addAll(m4);
       // acctMenu.setStyle("-fx-background-color: #11f3ff");

        //create menu bar
        MenuBar mb = new MenuBar();
        mb.getMenus().add(acctMenu);
        mb.setStyle("-fx-background-color: #3ecfff");

        acctMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        return mb;
    }

    // show stage
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setTitle("Account View");
        stage.show();
    }

    // Main application launch
    public static void main(String[] args) {
        launch(args);
    }

    //set the fields status and enable based on account login
    public void setFields() {
        txtAcctID.setText(cashMachine.acctID());
        txtAcctName.setText(cashMachine.acctName());
        txtEmailID.setText(cashMachine.acctEmail());
        txtAcctBalance.setText(cashMachine.acctBalance());
        BooleanBinding boolBind = txtAcctID.textProperty().isNotEmpty().or(txtAcctName.textProperty().isNotEmpty())
                .or(txtEmailID.textProperty().isNotEmpty());
        if (boolBind.get()) {
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
            btnLogOut.setDisable(false);
            txtAmtEntries.setVisible(true);
            lblAmtEntries.setVisible(true);
        } else {
            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);
            btnLogOut.setDisable(true);
            txtAmtEntries.setVisible(false);
            lblAmtEntries.setVisible(false);
        }
    }

    // Alert display if users clicks on login button if no Acct ID or invalid Acct ID is entered
    private void failedAlert(Exception exception) {
        Platform.runLater(() -> {
            javafx.scene.text.Text txt = new javafx.scene.text.Text(
                    String.format(exception.getMessage()));
            //  String.format("Please enter valid Account ID to Login: 1000,2000,3000 or 4000",
            //        exception.getMessage()));
            txt.setFill(Color.RED);
            FlowPane alertContent = new FlowPane(txt);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setContent(alertContent);
            alert.showAndWait();
            // fieldAcctID.setStyle("-fx-border-color: Red;");
        });
    }
}
