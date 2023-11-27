package LoginDashboardManager;

import DataBaseManager.StudentDataBaseManager;
import StudentDashboardManager.StudentActivityController;
import StudentDashboardManager.StudentDashboardController;
import SystemUsers.ClubAdvisor;
import SystemUsers.Student;
import SystemUsers.User;
import com.example.clubmanagementsystem.ApplicationController;
import com.example.clubmanagementsystem.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.example.clubmanagementsystem.HelloApplication.statement;


public class StudentLoginController {
    private static String selectedGradeVal; // holds the user selected grade
    private static String selcetedGenderVal; // holds the user selected gender
    private int grade; // hold the grade from Grade combo box
    private String gender; // hold the gender from the Gender combo box
    public static boolean validateStatus = true; // uses to validation purposes of admission number, name, contact number etc
    static boolean loginStatus; // uses to validate user entered username and password in login screen
    String studentLoginPageUserName; // holds user entered user name in login page
    String studentLoginPagePassword; // hold user entered password in login page
    public static String userNameForShowInStudentDashboard; // hold username to show in student dashboard when it loads
    private Scene scene;
    private Stage stage;
    private double xPosition;
    private double yPosition;

    @FXML
    private Label studentLoginUserNameErrorLabel;

    @FXML
    private Label studentRegisterConfirmPasswordErrorLabel;

    @FXML
    private Label studentIncorrectCredential;

    @FXML
    private Label studentLoginPasswordErrorLabel;

    @FXML
    private Label studentRegistrationGradeEmptyLabel;

    @FXML
    private Label studentRegistrationGenderEmptyLabel;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private StackPane StudentLoginForm;

    @FXML
    private PasswordField studentRegisterPassword;

    @FXML
    private PasswordField studentRegisterConfirmPassword;

    @FXML
    private TextField studentRegisterLastName;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private TextField studentRegisterAdmissionNumber;

    @FXML
    private TextField studentRegisterFirstName;

    @FXML
    private TextField studentRegisterContactNumber;

    @FXML
    private TextField studentRegisterUserName;

    @FXML
    private TextField LoginStudentUserName;

    @FXML
    private TextField studentLoginPassword;

    @FXML
    private ComboBox<String> Grade;

    @FXML
    private ComboBox<String> Gender;

    @FXML
    private Label studentRegisterAdmissionNumErrorLabel;

    @FXML
    private Label studentRegisterFNameErrorLabel;

    @FXML
    private Label studentRegisterLNameErrorLabel;

    @FXML
    private Label studentRegisterContactNumErrorLabel;

    @FXML
    private Label studentRegisterUserNameErrorLabel;

    @FXML
    private Label studentRegisterPasswordErrorLabel;

    @FXML
    void DirectToStartPage(ActionEvent event) throws IOException { // method to direct user to back to main login page
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/clubmanagementsystem/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void StudentLoginPaneDragDetected(MouseEvent event) {
        Stage stage = (Stage) StudentLoginForm.getScene().getWindow();
        stage.setX(event.getScreenX() - xPosition);
        stage.setY(event.getScreenY() - yPosition);
    }

    @FXML
    void studentLoginPanePressedDetected(MouseEvent event) {
        xPosition = event.getSceneX();
        yPosition = event.getSceneY();
    }

    @FXML
    void minimizeTheProgram(ActionEvent event) { // minimize button in registration page
        ApplicationController applicationController = new ApplicationController();
        applicationController.MinimizeApp(StudentLoginForm);
    }

    @FXML
    void ExitTheProgram(ActionEvent event) { // back button of student registration page
        ApplicationController applicationController = new ApplicationController();
        applicationController.closingApp();
    }
    public void showTypedPassword() {
        if (showPasswordCheckBox.isSelected()) { // when user select show password checkbox
            studentLoginPassword.setVisible(false); //studentLoginPassword textfield will disable
            PasswordTextField.setVisible(true); // PasswordTextField textfield will enable
            PasswordTextField.setText(studentLoginPassword.getText()); /* this will take the values from studnetLoginPassword
                                                                           textfield and will set to PasswordTextField*/
        } else { // this will execute if user keep the checkbox as it is
            PasswordTextField.setVisible(false);
            studentLoginPassword.setVisible(true);
            studentLoginPassword.setText(PasswordTextField.getText());
        }
    }
    boolean fieldsChecker() { /* this method is used to check whether both studentLoginPagePassword and studentLoginPageUserName field are
                                  empty or not */
        loginStatus = true;
        studentLoginPageUserName = LoginStudentUserName.getText(); // receiving username from user in login page
        if (studentLoginPassword.isVisible()) {
            studentLoginPagePassword = studentLoginPassword.getText(); // receiving password from user in login page
        } else {
            studentLoginPagePassword = PasswordTextField.getText(); // receiving password from user in login page
        }
        userNameForShowInStudentDashboard = studentLoginPageUserName;
        if (studentLoginPageUserName.isEmpty()) { // if username is empty, error label will be set
            System.out.println("Empty user name !!!");
            loginStatus = false;
            studentLoginUserNameErrorLabel.setText("This field cannot be empty");
        }
        if (studentLoginPagePassword.isEmpty()) { // if psword is field is empty, error label will be set
            loginStatus = false; // loginStatus will be false
            studentLoginPasswordErrorLabel.setText("This field cannot be empty");
            System.out.println("Empty password !!!");
        }
        return loginStatus;
    }

    //studentCredentialChecker will check whether entered credentials are correct according to the given values
    boolean studentCredentialChecker() { /* this method will check, whether entered username and password are correct
                                            according to the existing values*/
        Student student = new Student(studentLoginPageUserName, studentLoginPagePassword);
        String correctPassword = student.LoginToSystem(); // calling studentLoginToSystem method
        loginStatus = true;
        if (!studentLoginPagePassword.equals(correctPassword)) { // entered password is incorrect, error label will be set
            loginStatus = false;
            studentIncorrectCredential.setText("User name or Password Incorrect");
        }
        return loginStatus;
    }
    @FXML
    void DirectToStudentDashboard(ActionEvent event) throws IOException, SQLException { // when user click on DirectToStudentDashboard button
        if (!fieldsChecker()) { // calling to fieldsChecker
            return;
        }
        studentLoginUserNameErrorLabel.setText(""); // clearing set values of respective labels
        studentLoginPasswordErrorLabel.setText("");  // clearing set values of respective labels

        if (!studentCredentialChecker()) { // calling studentCredentialChecker method
            return;
        }
        studentLoginPasswordErrorLabel.setText("");
        System.out.println("Directing to student dashboard");

        StudentDataBaseManager studentDataBaseManager = new StudentDataBaseManager(userNameForShowInStudentDashboard); /* this is the
                                                                                                        place data load form the database */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/clubmanagementsystem/StudentDashboard.fxml"));
        Parent root = loader.load();
        StudentActivityController controller = loader.getController(); // This is done to set login userName to dashboard
        controller.showUserName.setText(userNameForShowInStudentDashboard); /* controller variable will get the access
                                                                                  to control student activity controller
                                                                                   to set username to dashboard
                                                                                   when it loads*/
        controller.showUserName.setStyle("-fx-text-alignment: center"); // centering showUserName label
        controller.displayEventCountPerClub();
        controller.studentAdmission = studentDataBaseManager.getStudentAdmissionNum(userNameForShowInStudentDashboard);
        StudentDashboardManager.StudentActivityController studentDashboardController = loader.getController();
        studentDashboardController.dashboardButton.setStyle("-fx-background-color: linear-gradient(#fafada, #ffffd2);");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void GoToStudentRegistration(ActionEvent event) throws IOException { // when user click on register button
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterManager/StudentRegistration.fxml"));
        Parent root = loader.load();
        StudentLoginController controller = loader.getController();
        controller.setComboBoxValuesStudentRegistration(); // giving controller access to  student Login controller, to load combo boxes initially
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    void DirectToLoginPane(MouseEvent event) throws IOException { // directing user to student login page
        Parent root = FXMLLoader.load(getClass().getResource("/LoginDashboardManager/StudentLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void StudentRegistrationChecker(MouseEvent event) throws SQLException, IOException {
        //a boolean value is set to true initially
        validateStatus = true;
        // the entered details are retrieved into variables
        String firstName = this.studentRegisterFirstName.getText();
        String lastName = this.studentRegisterLastName.getText();
        String admissionNum = this.studentRegisterAdmissionNumber.getText();
        String contactNum = this.studentRegisterContactNumber.getText();
        String userName = this.studentRegisterUserName.getText();
        String password = this.studentRegisterPassword.getText();
        String passwordConfirm = this.studentRegisterConfirmPassword.getText();
        System.out.println(admissionNum);
        // an object called student is created of data type Student
        Student student = new Student(userName, password, firstName, lastName);
        // the  first name is validated using the validator interface
        if (!student.validateFirstName()) {
            System.out.println("Wrong first name");
            validateStatus = false; // the boolean value is set to false as there is an error
        }
        displayNameError("FName"); //the error field is specified as the first and last names follow the same validation
        // the last name is validated using the validator interface
        if (!student.validateLastName()) {
            System.out.println("Wrong last name");
            validateStatus = false; // the boolean value is set to false as there is an error
        }
        displayNameError("LName"); //the error field is specified as the first and last names follow the same validation
        // validating contact number
        try {
            String tempContactVal = contactNum; // the contact number is stored in a temporary variable
            // check if the value is empty
            if (tempContactVal.isEmpty()) {
                User.contactNumberValidateStatus = "empty";
                throw new Exception(); // general exception is thrown
            }
            Double.parseDouble(contactNum.trim()); // the string is converted to a double and it is trimmed
            Student std1 = new Student(tempContactVal); // a new object is created of data type Student with only the temporary holder as the parameter
            if (!std1.validateContactNumber()) {
                validateStatus = false; // the boolean value is set to false as there is an error
                System.out.println("Invalid Contact Number 1");
            } else {
                // the contact number is validated
                User.contactNumberValidateStatus = "";
            }
            // catching number format exceptions
        } catch (NumberFormatException e) {
            System.out.println("Invalid ContactNumber 2");
            User.contactNumberValidateStatus = "format";
            validateStatus = false; // the boolean value is set to false as there is an error
        } catch (Exception e) {
            validateStatus = false; // the boolean value is set to false as there is an error
        }
        displayContactValError(); // the error method is called to specify what type of error is produced
        // validating admission number
        try {
            if(admissionNum.isEmpty()){ // if admission ID is empty
                validateStatus = false;
                Student.admissionNumStatus ="empty"; // admissionNumStatus will set to "empty"
                throw new Exception(); // general exception
            }
            int advisorIdValue = Integer.parseInt(admissionNum.trim()); // converting admission number to an integer
            Student studentVal = new Student(advisorIdValue); // creating an object to validate admission number
            if (!studentVal.validateStudentAdmissionNumber()) { // if admission number is not under system validation standards
                validateStatus  = false; // validateStatus will be false
            }else{
                // admission number validated
                Student.admissionNumStatus = ""; // when entered admission number has no issues
            }
        }catch(NumberFormatException e){ // when entered admission number got unknown errors in number format
            Student.admissionNumStatus ="format"; // admissionNumStatus will set to "format"
            System.out.println("Invalid Advisor Id");
            validateStatus  = false;
        }
        catch (Exception e) { // if unknown errors occur
            validateStatus = false;
        }
        displayAdmissionNumError();
        if (!student.validateUserName("registration", "student")) { /* passing parameters to validateUserName method,
                                                                         and if username did not meet system standards */
            System.out.println("Wrong user name");
            validateStatus = false;
        } else {
            User.userNameValidateStatus = ""; // when entered username is valid
        }
        displayUserNameError(); // calling to displayUserNameError method
        if (!student.validatePassword("registration")) { /* passing values to validatePassword method, and if
                                                                   password didn't meet system standards */
            System.out.println("Wrong password");
            validateStatus = false;
        } else {
            User.passwordValidateStatus = ""; // when entered password is valid
        }
        // password validating
        displayPasswordError();
        // confirm password validating
        if (passwordConfirm.isEmpty()) { // when confirm password field is empty
            validateStatus = false; // validateStat will false
            studentRegisterConfirmPasswordErrorLabel.setText("Confirm Password cannot be empty"); /* setting an error label to
                                                                                                studentRegisterConfirmPasswordErrorLabel */
        } else if (!passwordConfirm.equals(password)) { // when entered passwords are not matching with each other
            studentRegisterConfirmPasswordErrorLabel.setText("Passwords are not matching"); /* setting an error label to
                                                            studentRegisterConfirmPasswordErrorLabel saying passwords are not matching */
            validateStatus = false;
        } else {
            studentRegisterConfirmPasswordErrorLabel.setText(" "); // when entered password has no issues
        }
        if(selcetedGenderVal.equals("Select Gender")){ // when user did not select gender from the combo box
            studentRegistrationGenderEmptyLabel.setText("Please select your gender"); /* setting an error label to
                                        studentRegistrationGenderEmptyLabel */
            validateStatus = false;
        }
        if(selectedGradeVal.equals("Select Grade")){ // when user did not select gender from the combo box
            studentRegistrationGradeEmptyLabel.setText("Please select your grade");  /* setting an error label to
                                        studentRegistrationGradeEmptyLabel */
            validateStatus = false;
        }
        System.out.println(validateStatus + " : Valid Stat");
        if (validateStatus) { // if there is no issue in entered data
            Student studentData = new Student(userName, password, firstName, lastName); // creating a student and adding to studentDetailArray
            Student.studentDetailArray.add(studentData);
            // inserting newly registered student's personal details to database
            String studentPersonalDetailsQuery = "INSERT INTO Student (studentAdmissionNum, studentFName, studentLName, " +
                    "studentGrade, studentContactNum, Gender) VALUES (?, ?, ?, ?, ?, ?)"; // SQL query
            try (PreparedStatement preparedStatement = HelloApplication.connection.prepareStatement(studentPersonalDetailsQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(admissionNum)); // setting admission number
                preparedStatement.setString(2, firstName); // setting first name
                preparedStatement.setString(3, lastName); // setting last name
                preparedStatement.setInt(4, grade);  // setting grade
                preparedStatement.setInt(5, Integer.parseInt(contactNum)); // setting contact number
                preparedStatement.setString(6, gender); // setting gender
                preparedStatement.executeUpdate();
                System.out.println("Personal details inserting perfectly");
            } catch (Exception e) {
                System.out.println(e);
            }
            // inserting newly registered student's credentials to the database
            String studentCredentialsDetailsQuery = "INSERT INTO studentCredentials (studentUserName," +
                    " studentPassword, studentAdmissionNum) VALUES (?, ?, ?)"; // SQL query
            try (PreparedStatement preparedStatement = HelloApplication.connection.prepareStatement(studentCredentialsDetailsQuery)) {
                preparedStatement.setString(1, userName); // setting username
                preparedStatement.setString(2, passwordConfirm); // setting lastname
                preparedStatement.setInt(3, Integer.parseInt(admissionNum)); // setting admission number in order to map two tables
                preparedStatement.executeUpdate();
                System.out.println("Credentials inserting perfectly");
            } catch (Exception e) {
                System.out.println(e);
            }
            // delaying the alert window process, in order make it user friendly
            try{
                Thread.sleep(1000); // delaying by 1000 milliseconds
            }catch (Exception e){
                System.out.println(e);
            }
            // showing alert window to user that has successfully registered with the system
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("School Club Management System");
            alert.setHeaderText("You have successfully registered with the system !!!");
            alert.showAndWait();
            this.DirectToLoginPane(event);
        }
        System.out.println("\n\n\n");
    }
    public void setComboBoxValuesStudentRegistration(){ // setting values to grade and gender combo boxes
        Grade.getItems().add("Select Grade");
        for (int ComboGrade = 6; ComboGrade<14; ComboGrade++) { // grades are from 6 to 13
            Grade.getItems().add((String.valueOf(ComboGrade)));
        }
        Grade.getSelectionModel().selectFirst(); // initially setting "Select Grade" to combo box
        selectedGradeVal = "Select Grade";
        Gender.getItems().addAll("Select Gender","M", "F"); //setting values to gender combo box
        Gender.getSelectionModel().selectFirst(); // initially setting "Select Grade" to combo box
        selcetedGenderVal = "Select Gender";
        Grade.setOnAction(event -> validateGradeSelection()); // calling validateGradeSelection method
        Gender.setOnAction(event -> validateGenderSelection()); // calling validateGenderSelection method
    }
    private int validateGradeSelection() {
        selectedGradeVal = Grade.getValue(); // getting the user selected value from the grade combo box
        if (!(selectedGradeVal == "Select Grade")) { // if selectedGradeVal is not equal to Select Grade
            studentRegistrationGradeEmptyLabel.setText("");
            grade = Integer.parseInt(this.Grade.getValue()); // setting value to grade
            return grade;// returning grade
        }
        return grade;
    }
    private String validateGenderSelection() {
        selcetedGenderVal = Gender.getValue(); // getting the user selected value from the gender combo box
        if (!(selcetedGenderVal == "Select Gender")) { // if selcetedGenderVal is not equal to Select Gender
            studentRegistrationGenderEmptyLabel.setText("");
            gender = this.Gender.getValue(); // setting value to gender
            System.out.println("Gender is "+ gender);
            return gender; // returning gender
        }
        return gender;
    }
    public void displayNameError(String nameType) {// entered name checking
        if (nameType.equals("FName")) { // checking first name
            if (Student.fNameValidateStatus.equals("empty")) { // if first name field is empty
                studentRegisterFNameErrorLabel.setText("First Name cannot be empty."); // setting an error label to studentRegisterFNameErrorLabel
            } else if (Student.fNameValidateStatus.equals("format")) { // if first name field contain invalid characters
                studentRegisterFNameErrorLabel.setText("First Name can only contain letters."); /* setting an error label to
                                                                                               studentRegisterFNameErrorLabel */
            } else {
                studentRegisterFNameErrorLabel.setText(""); // when user correctly enter first name
            }
        } else if (nameType.equals("LName")) { // checking last name
            if (Student.lNameValidateStatus.equals("empty")) { // if last name field is empty
                studentRegisterLNameErrorLabel.setText("Last Name cannot be empty.");
            } else if (Student.lNameValidateStatus.equals("format")) { // if last name field contain invalid characters
                studentRegisterLNameErrorLabel.setText("Last Name can contain only letters.");
            } else {
                studentRegisterLNameErrorLabel.setText(""); // when user correctly enter last name
            }
        }
    }
    public void displayAdmissionNumError() { // admission number checking
        if (Student.admissionNumStatus.equals("empty")) { // when admission number field is empty
            studentRegisterAdmissionNumErrorLabel.setText("Admission Number cannot be empty.");
        } else if (Student.admissionNumStatus.equals("length")) { // when admission number length is not valid
            studentRegisterAdmissionNumErrorLabel.setText("Admission Number has to be 6 digits.");
        } else if (Student.admissionNumStatus.equals("exist")) { // when user enter an existed admission number
            studentRegisterAdmissionNumErrorLabel.setText("Admission Number already exists.");
        } else if (Student.admissionNumStatus.equals("format")) { // when admission number contain letters
            studentRegisterAdmissionNumErrorLabel.setText("Admission Number contain only numbers.");
        } else {
            studentRegisterAdmissionNumErrorLabel.setText(""); // when user enter admission number correctly
        }
    }
    public void displayUserNameError() { // username checking
        if (User.userNameValidateStatus.equals("empty")) { // when username field is empty
            studentRegisterUserNameErrorLabel.setText("User Name cannot be empty");
        } else if (User.userNameValidateStatus.equals("exist")) { // when user enter an existed admission number
            studentRegisterUserNameErrorLabel.setText("Entered Username already exists");
        } else if (User.userNameValidateStatus.equals("blank")) { // when username contain spaces
            studentRegisterUserNameErrorLabel.setText("User Name cannot contain spaces");
        } else if (User.userNameValidateStatus.equals("length")) { // when username is not lengthier enough
            studentRegisterUserNameErrorLabel.setText("The length should be 5 to 10 characters.");
        } else {
            studentRegisterUserNameErrorLabel.setText(""); // when entered username is correct
        }
    }
    public void displayPasswordError() { // password checking
        if (User.passwordValidateStatus.equals("empty")) { // when password field is empty
            studentRegisterPasswordErrorLabel.setText("Password cannot be empty.");
        } else if (User.passwordValidateStatus.equals("format")) { // when user entered password is not strong enough
            // here we are splitting the sentence into two lines, in order to set the label correctly
            studentRegisterPasswordErrorLabel.setText(""" 
                    Password should consist of 8 characters
                    including numbers and special characters.""");
            studentRegisterPasswordErrorLabel.setStyle("-fx-text-alignment: justify;");
        } else {
            studentRegisterPasswordErrorLabel.setText(""); // when user entered password is valid
        }
    }
    public void displayContactValError() { // contact number checking
        if (User.contactNumberValidateStatus.equals("empty")) { // when contact number field is empty
            studentRegisterContactNumErrorLabel.setText("Contact Number cannot be empty.");
        } else if (User.contactNumberValidateStatus.equals("length")) { // when entered contact number is not a valid number
            studentRegisterContactNumErrorLabel.setText("Contact Number should have 10 digits.");
        } else if (User.contactNumberValidateStatus.equals("format")) { // when entered contact number has string values
            studentRegisterContactNumErrorLabel.setText("Number cannot contain characters.");
        } else {
            studentRegisterContactNumErrorLabel.setText(""); // when entered contact is correct
        }
    }
}