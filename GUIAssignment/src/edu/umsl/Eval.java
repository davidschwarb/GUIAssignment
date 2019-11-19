package edu.umsl;

import java.awt.*;
import java.awt.event.*;
import java.awt.Container;
import java.sql.*;
//import java.text.*;
import javax.swing.*;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import org.apache.derby.jdbc.*;

/**
 *
 * @author karbo
 */
public class Eval extends JFrame implements ActionListener, ItemListener 
{
//DECLARE THE ELEMENTS OR OBJECTS THAT YOU WILL PUT IN YOUR FRAME
//NOTICE HOW A PANEL IS CREATED FOR EACH ONE THIS WILL MAKE IT EASIER BUILD

    public JLabel teamLabel;
    private JComboBox teamComboBox;
    private JPanel teamPanel;
    
    private JLabel questionLabel;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    
    private JRadioButton rb1;
    private JRadioButton rb2;
    private JRadioButton rb3;
    private JPanel questionPanel;
    private ButtonGroup questionGroup1;
    
    private JButton submitButton;
    private JButton clearButton;
    private JPanel buttonPanel;

//instance variables to hold our data from the gui object to update the database
    String myteamname;
// String courseName;
    int q1;
    int q2;
    int q3;
    int q4;
    double teamavg;
    boolean avgcalculated;
    String teamcomments;
// instance variables used to manipulate database
    private Connection myConnection;
    private Statement myStatement;
    private ResultSet myResultSet;

//MAIN METHOD: NOTICE WE TAKE IN THE ARGUMENTS THAT ARE
//PASSED IN AND INSTANTIATE OUR CLASS WITH THEM
    
    public static void main(String args[]) 
    {
// check command-line arguments
//if ( args.length == 2 )
//{
// get command-line arguments
        String databaseDriver = "org.apache.derby.jdbc.ClientDriver";
//String databaseDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
//String databaseURL = "jdbc:derby://localhost:1527/PureEval";
        String databaseURL = "jdbc:derby://localhost:1527/Eval";

// create new Eval
        Eval eval = new Eval(databaseDriver, databaseURL);
        eval.createUserInterface();
        eval.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//}
//else // invalid command-line arguments
//{
// System.out.println( "Usage: java EVAL needs databaseDriver databaseURL" );
//}
    }

//CONSTRUCTOR: WE SET UP OUR DATABASE HERE THEN MAKE A CALL
//TO A FUNCTION CALLED CREATEUSERINTERFACE TO BUILD OUR GUI
    public Eval(String databaseDriver, String databaseURL) 
    {
// establish connection to database
        try 
        {
// load Sun driver
//Class.forName( "org.apache.derby.jdbc.ClientDriver");
            DriverManager.registerDriver(new ClientDriver());
// connect to database
            myConnection = DriverManager.getConnection( databaseURL );

// create Statement for executing SQL
            myStatement = myConnection.createStatement();
        } 
        catch (SQLException exception) 
        {
            exception.printStackTrace();
        }
//catch ( ClassNotFoundException exception )
// {
// exception.printStackTrace();
//}

        //createUserInterface(); // set up GUI

    }

    private void createUserInterface() 
    {
// get content pane for attaching GUI components
        Container contentPane = getContentPane();

// enable explicit positioning of GUI components
        contentPane.setLayout(null);

// INSTRUCTOR COMBO BOX SET UP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// set up Instructor Panel
        teamPanel = new JPanel();
        teamPanel.setBounds(40, 20, 276, 48);
        teamPanel.setBorder(BorderFactory.createEtchedBorder());
        teamPanel.setLayout(null);
        contentPane.add(teamPanel);

// set up Instructor Label
        teamLabel = new JLabel();
        teamLabel.setBounds(25, 15, 100, 20);
        teamLabel.setText("TEAMS:");
        teamPanel.add(teamLabel);

// set up accountNumberJComboBox
        teamComboBox = new JComboBox();
        teamComboBox.setBounds(150, 15, 96, 25);
        teamComboBox.addItem("");
        teamComboBox.setSelectedIndex(0);
        teamPanel.add(teamComboBox);

//RADIO BUTTON SET UP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// set up Question Panel and Radio Buttons
        questionPanel = new JPanel();
        questionPanel.setBounds(40, 120, 276, 75);
        questionPanel.setBorder(BorderFactory.createEtchedBorder());
        questionPanel.setLayout(null);
        contentPane.add(questionPanel);

// set up question1 Label
        questionLabel = new JLabel();
        questionLabel.setBounds(10, 15, 270, 20);
        questionLabel.setText("Q1: How would you rate the instructors jokes?");
        questionPanel.add(questionLabel);
        
// set up the new slider for question 1
        JFrame f = new JFrame("JSlider Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = f.getContentPane();
        slider1 = new JSlider(JSlider.HORIZONTAL, 1 , 8, 1);
        //slider1.setBounds(80, 50, 200, 200);
        slider1.setMinorTickSpacing(1);
        slider1.setMajorTickSpacing(8);
        slider1.setPaintTicks(true);
        slider1.setSnapToTicks(true);
        slider1.setPaintTrack(false);
        slider1.setPaintLabels(true);
        content.add(slider1, BorderLayout.CENTER);
        f.setSize(300, 100);
        f.setVisible(true);
        
        questionPanel.add(slider1);
        
        //buttonPanel.add(slider1);
        
        
// set up the radio buttons for question 1
        rb1 = new JRadioButton("1", false);
        rb1.setBounds(20, 30, 40, 40);
        rb1.setVisible(true);
        rb1.addItemListener(this);

        rb2 = new JRadioButton("2", false);
        rb2.setBounds(80, 30, 40, 40);
        rb2.setVisible(true);
        rb2.addItemListener(this);

        rb3 = new JRadioButton("3", false);
        rb3.setBounds(140, 30, 40, 40);
        rb3.setVisible(true);
        rb3.addItemListener(this);
        

        
// create logical relationship between JRadioButtons
//        questionGroup1 = new ButtonGroup();
//        questionGroup1.add(rb1);
//        questionGroup1.add(rb2);
//        questionGroup1.add(rb3);

// add radio button to the panel
        questionPanel.add(slider1);
//        questionPanel.add(rb2);
//        questionPanel.add(rb3);

// SUBMIT BUTTON SET UP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        buttonPanel = new JPanel();
        buttonPanel.setBounds(40, 200, 276, 75);
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(null);
        contentPane.add(buttonPanel);

        submitButton = new JButton("SUBMIT");
        submitButton.setBounds(80, 15, 100, 50);
        submitButton.setVisible(true);
        buttonPanel.add(submitButton);
        submitButton.addActionListener(this);

        JSlider myslider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);

        myslider.setBounds(80, 50, 200, 200);
        buttonPanel.add(myslider);
//read account numbers from database and
// place them in accountNumberJComboBox
        loadTeams();
//loadCourses();

        setTitle("EVAL"); // set title bar string
        setSize(375, 410); // set window size
        setVisible(true); // display window
    }

//OVERRIDING THIS FUNCTION BECAUSE OUR CLASS IMPLEMENTS THE ACTION LISTENER
    @Override
    public void actionPerformed(ActionEvent event) 
    {
//q1 = sliderq1.getValue();

// courseName = (String)courseComboBox.getSelectedItem();
        if (event.getSource().equals(submitButton)) 
        {
            myteamname = (String) teamComboBox.getSelectedItem();

            if (rb1.isSelected()) 
            {
                q1 = Integer.parseInt(rb1.getText());
            } else if (rb2.isSelected()) 
            {
                q1 = Integer.parseInt(rb2.getText());
            } else if (rb3.isSelected()) 
            {
                q1 = Integer.parseInt(rb3.getText());
            }

            q2 = 8;
            q3 = 2;
            q4 = 5;
            teamavg = ((q1 + q2 + q3 + q4) / 4);
            teamcomments = "Not a bad presentation not a good one either";

            updateTeams();
        }
// else if(event.getSource().equals(clearButton))
// {
// textavgtextbox.text = "";
// submitButton.setEnabled(false);
// }
// else if(event.getSource().equals(teamavgButton))
// {
// int tempval1 = slidertechnical.getValue();
// int tempval2 = slideruse.getValue();
// int tempval3 = sliderclarity.getValue();
// int tempval4 = slideroverall.getValue();
// teamavg = (double)(tempval1 + teampval2 + tempval3 + tempval4)/4.0
// teamavgTextBox.text = teamavg;
// submitButton.setEnabled(true);
// avgcalculated = true;

    }

    @Override
    public void itemStateChanged(ItemEvent event) 
    {

        if (event.getSource() == rb1 && event.getStateChange() == ItemEvent.SELECTED) 
        {
            q1 = Integer.parseInt(rb1.getText());
        } else if (event.getSource() == rb2 && event.getStateChange() == ItemEvent.SELECTED) 
        {
            q1 = Integer.parseInt(rb2.getText());
        } else if (event.getSource() == rb3 && event.getStateChange() == ItemEvent.SELECTED) 
        {
            q1 = Integer.parseInt(rb3.getText());
        } else if (event.getSource() == rb1 && event.getStateChange() == ItemEvent.DESELECTED) 
        {
            JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");
        }
    }

    private void updateTeams() 
    {
// update balance in database
        try 
        {
// Below is an example of creating a SQL statement that updated more than a single field in one statement.
            String sql1 = "UPDATE APP.TEAM SET Q1T = " + q1
                    + ", Q2U = " + q2
                    + ", Q3C = " + q3
                    + ", Q4O = " + q4
                    + ", TEAMAVG = " + teamavg
                    + ", COMMENTS = " + "'" + teamcomments
                    + "'" + "WHERE " + "TEAMS = " + "'" + myteamname + "'";
            //String sql2 = "UPDATE APP.TEAM SET Q2USEFUL = " + q2 + " WHERE " + "TEAMS = " + "'" + myteamname + "'";
// String sql3;
// String sql4;
// String sql5;
// String sql6 = "UPDATE APP.TEAM SET COMMENTS = " + "'" + teamcomments + "'" + " WHERE " + "TEAMS = " + "'" + myteamname + "'";
            myStatement.executeUpdate(sql1);

        } 
        catch (SQLException exception) 
        {
            System.out.println(exception.getMessage());
        }

    } // end method updateBalance

    private void loadTeams() 
    {
// get all account numbers from database
        try 
        {

//myResultSet = myStatement.executeQuery( "SELECT DISTINCT lastname FROM InstEval" );
            myResultSet = myStatement.executeQuery("SELECT TEAMS FROM APP.TEAM");
// add account numbers to accountNumberJComboBox
            while (myResultSet.next()) 
            {
//instructorComboBox.addItem( myResultSet.getString( "lastname" ) );
                teamComboBox.addItem(myResultSet.getString("TEAMS"));
            }

            myResultSet.close(); // close myResultSet

        } // end try
        catch (SQLException exception) 
        {
            System.out.println(exception.getMessage());
        }
    }
}