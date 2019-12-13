package edu.umsl;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Hashtable;
import javax.swing.*;
import org.apache.derby.jdbc.*;

/**
 *
 * @author David
 */
public class Eval extends JFrame implements ActionListener, ItemListener {
    
    private boolean clearedStart = false;
    private Connection myConnection;
    private Statement myStatement;
    private ResultSet myResultSet;

    private double average;
    private boolean displayingDecimal = false;

    JSlider slider;
    JButton submit, clear, calcAvg, toggleDisplay;

    JLabel teamLabel, q1lab, q2lab, q3lab, q4lab, q5lab, commentsLabel, avgLabel;
    JComboBox teamComboBox;

    JTextArea commentTextArea;
    JTextField avgTextField;

    JSlider q1Slider, q2Slider, q3Slider, q4Slider;

    Eval(String databaseDriver, String databaseURL) {
        this.setTitle("EVALUATION");

        try {
            // load Sun driver
            //Class.forName( "org.apache.derby.jdbc.ClientDriver");
            DriverManager.registerDriver(new ClientDriver());
            // connect to database
            myConnection = DriverManager.getConnection(databaseURL);

            // create Statement for executing SQL
            myStatement = myConnection.createStatement();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        createUserInterface();

        pack();

        this.setLocationRelativeTo(null);
        this.setSize(750, 750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Eval("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://localhost:1527/Eval");
    }

    public void createUserInterface() {

        JPanel panel = new JPanel(new GridBagLayout());
        this.add(panel);

        GridBagConstraints panelGBC = new GridBagConstraints();

        JPanel panelLabels = new JPanel(new GridBagLayout());

        JPanel commentsPanel = new JPanel(new GridBagLayout());

        JPanel avgPanel = new JPanel(new GridBagLayout());

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        panelGBC.gridy = 0;

        panel.add(panelLabels, panelGBC);

        panelGBC.gridy = 1;

        panel.add(commentsPanel, panelGBC);

        panelGBC.gridy = 2;

        panel.add(avgPanel, panelGBC);

        panelGBC.gridy = 3;

        panel.add(buttonPanel, panelGBC);

        GridBagConstraints gbc = new GridBagConstraints();

        teamLabel = new JLabel("Select the team:");

        teamComboBox = new JComboBox();
        teamComboBox.addItem("");
        teamComboBox.setSelectedIndex(0);



        q1lab = new JLabel("Q1: Technical?");
        q2lab = new JLabel("Q2: Useful?");
        q3lab = new JLabel("Q3: Clarity?");
        q4lab = new JLabel("Q4: Overall?");
        commentsLabel = new JLabel("Comments:");

        commentTextArea = new JTextArea("\n           Enter comments about the presentation here.", 3, 30);
        commentTextArea.setLineWrap(true);

        commentTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {

            }
            
            @Override
            public void focusGained(FocusEvent arg0) {
                if(!clearedStart) {
                    commentTextArea.setText("");
                    clearedStart = true;
                }
            }
        });
        calcAvg = new JButton("CALCULATE AVERAGE");
        calcAvg.addActionListener(this);

        submit = new JButton("SUBMIT");
        submit.setBounds(80, 80, 100, 50);
        submit.setVisible(true);
        submit.addActionListener(this);
        submit.setEnabled(false);

        clear = new JButton("CLEAR");
        clear.setBounds(80, 80, 100, 50);
        clear.setVisible(true);
        clear.addActionListener(this);

        toggleDisplay = new JButton("Display Decimal");
        toggleDisplay.addActionListener(this);

        avgTextField = new JTextField(10);
        avgTextField.setFocusable(false);
        avgTextField.setHorizontalAlignment(JTextField.HORIZONTAL);

        q1Slider = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);
        q2Slider = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);
        q3Slider = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);
        q4Slider = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(1, new JLabel("C-"));
        labels.put(2, new JLabel("C"));
        labels.put(3, new JLabel("C+"));
        labels.put(4, new JLabel("B-"));
        labels.put(5, new JLabel("B"));
        labels.put(6, new JLabel("B+"));
        labels.put(7, new JLabel("A-"));
        labels.put(8, new JLabel("A"));

        q1Slider.setLabelTable(labels);
        q1Slider.setPaintLabels(true);
        q1Slider.setMinorTickSpacing(1);
        q1Slider.setMajorTickSpacing(1);
        q1Slider.setPaintTicks(true);

        q2Slider.setLabelTable(labels);
        q2Slider.setPaintLabels(true);
        q2Slider.setMinorTickSpacing(1);
        q2Slider.setMajorTickSpacing(1);
        q2Slider.setPaintTicks(true);

        q3Slider.setLabelTable(labels);
        q3Slider.setPaintLabels(true);
        q3Slider.setMinorTickSpacing(1);
        q3Slider.setMajorTickSpacing(1);
        q3Slider.setPaintTicks(true);

        q4Slider.setLabelTable(labels);
        q4Slider.setPaintLabels(true);
        q4Slider.setMinorTickSpacing(1);
        q4Slider.setMajorTickSpacing(1);
        q4Slider.setPaintTicks(true);

        avgLabel = new JLabel("Computed average from questions above:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 10, 20, 20);

        panelLabels.add(teamLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panelLabels.add(teamComboBox, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;

        panelLabels.add(q1lab, gbc);

        gbc.gridx = 1;
        panelLabels.add(q1Slider, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panelLabels.add(q2lab, gbc);

        gbc.gridx = 1;
        panelLabels.add(q2Slider, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panelLabels.add(q3lab, gbc);

        gbc.gridx = 1;
        panelLabels.add(q3Slider, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        panelLabels.add(q4lab, gbc);

        gbc.gridx = 1;
        panelLabels.add(q4Slider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        avgPanel.add(avgLabel, gbc);

        gbc.gridx = 3;
        avgPanel.add(avgTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;

        avgPanel.add(calcAvg, gbc);

        gbc.gridx = 3;
        avgPanel.add(toggleDisplay, gbc);
        gbc.gridx = 0;

        commentsPanel.add(commentsLabel, gbc);

        gbc.gridx = 3;

        commentsPanel.add(commentTextArea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(submit, gbc);

        gbc.gridx = 3;
        buttonPanel.add(clear, gbc);
        
                // get all account numbers from database
        try {

//myResultSet = myStatement.executeQuery( "SELECT DISTINCT lastname FROM InstEval" );
            myResultSet = myStatement.executeQuery("SELECT TEAMS FROM APP.TEAM");
// add account numbers to accountNumberJComboBox
            while (myResultSet.next()) {
//instructorComboBox.addItem( myResultSet.getString( "lastname" ) );
                teamComboBox.addItem(myResultSet.getString("TEAMS"));
            }

            myResultSet.close(); // close myResultSet

        } // end try
        catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Error connecting to the database: Make sure to check your connection");
            teamComboBox.addItem("ERROR CONNECTING TO DATABASE");
            teamComboBox.setSelectedIndex(1);
            q1Slider.setEnabled(false);
            q2Slider.setEnabled(false);
            q3Slider.setEnabled(false);
            q4Slider.setEnabled(false);
            clear.setEnabled(false);
            commentTextArea.setEnabled(false);
            calcAvg.setEnabled(false);
            toggleDisplay.setEnabled(false);
        }

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource().equals(calcAvg)) {
            average = (q1Slider.getValue() + q2Slider.getValue() + q3Slider.getValue() + q4Slider.getValue()) / 4.0;
            int roundedAverage = (int) Math.round(average);
            if (!displayingDecimal) {
                switch (roundedAverage) {
                    case 1:
                        avgTextField.setText("C-");
                        break;
                    case 2:
                        avgTextField.setText("C");
                        break;
                    case 3:
                        avgTextField.setText("C+");
                        break;
                    case 4:
                        avgTextField.setText("B-");
                        break;
                    case 5:
                        avgTextField.setText("B");
                        break;
                    case 6:
                        avgTextField.setText("B+");
                        break;
                    case 7:
                        avgTextField.setText("A-");
                        break;
                    case 8:
                        avgTextField.setText("A");
                        break;
                }
            } else {
                avgTextField.setText(String.valueOf(average));
            }
            submit.setEnabled(true);
        } else if (event.getSource().equals(submit)) {
            clearedStart = false;
            // update balance in database
            try {
                int q1 = q1Slider.getValue(),
                        q2 = q2Slider.getValue(),
                        q3 = q3Slider.getValue(),
                        q4 = q4Slider.getValue();
                String teamcomments = commentTextArea.getText();
                String myteamname = (String) teamComboBox.getSelectedItem();
// Below is an example of creating a SQL statement that updated more than a single field in one statement.
                String sql1 = "UPDATE APP.TEAM SET Q1 = " + q1
                        + ", Q2 = " + q2
                        + ", Q3 = " + q3
                        + ", Q4 = " + q4
                        + ", TEAMAVG = " + average
                        + ", COMMENTS = " + "'" + teamcomments
                        + "'" + "WHERE " + "TEAMS = " + "'" + myteamname + "'";
                myStatement.executeUpdate(sql1);

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            System.out.println("Clear");
            teamComboBox.setSelectedIndex(0);
            q1Slider.setValue(1);
            q2Slider.setValue(1);
            q3Slider.setValue(1);
            q4Slider.setValue(1);
            commentTextArea.setText("\n           Enter comments about the presentation here.");
            avgTextField.setText("");
            submit.setEnabled(false);
        } else if (event.getSource().equals(clear)) {
            clearedStart = false;
            System.out.println("Clear");
            teamComboBox.setSelectedIndex(0);
            q1Slider.setValue(1);
            q2Slider.setValue(1);
            q3Slider.setValue(1);
            q4Slider.setValue(1);
            commentTextArea.setText("\n           Enter comments about the presentation here.");
            avgTextField.setText("");
            submit.setEnabled(false);
        } else if (event.getSource().equals(toggleDisplay)) {
            if(displayingDecimal) {
                toggleDisplay.setText("Display Decimal");
            } else {
                toggleDisplay.setText("  Display  Letter ");
            }
            displayingDecimal = !displayingDecimal;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {

    }
}
