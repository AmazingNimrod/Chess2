/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nyima
 */
class LoginPanel extends JDialog {

    private boolean loginSuccess = false;
    private final JTextField usernameField;

    public LoginPanel(Frame parent, User user) {
        super(parent, "Login", true);

        setLayout(new FlowLayout()); // use flow layout

        usernameField = new JTextField(15); // username field
        JButton loginButton = new JButton("Login"); //login button

        loginButton.addActionListener(e -> {
            if (usernameField.getText().matches("[a-zA-Z0-9]+")) { // check for character and nums 
                if (!user.userExists(usernameField.getText())) // if the user doesnt exist prompt user that new user is being created
                    JOptionPane.showMessageDialog(this, "User not found in database. New user will be created");
                if (user.login(usernameField.getText())) { // login
                    loginSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed"); // unsucessful login
                }
            } else{ // prompt for correct input
                JOptionPane.showMessageDialog(this, "Invalid username. Please enter only characters and numbers");
            }
        });

        add(new JLabel("Username:"));
        add(usernameField);
        add(loginButton);
        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSucceeded() {
        return loginSuccess;
    }
}
