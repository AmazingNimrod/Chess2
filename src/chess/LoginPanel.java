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

        setLayout(new FlowLayout());

        usernameField = new JTextField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            if (usernameField.getText().matches("[a-zA-Z0-9]+")) {
                if (user.login(usernameField.getText())) {
                    loginSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed");
                }
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
