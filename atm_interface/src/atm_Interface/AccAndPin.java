package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class AccAndPin implements ActionListener{
    JFrame startframe;
    JPanel accpanel;
    JPanel pinpanel;
    JPanel numpanel;
    JButton[] nums;
    JButton ok,clr;
    JLabel alabel,plabel;
    JLabel bg; 
    JTextField pfield,afield;
    //vairable to store pin number from db;
    private int pinnumber;
    //variable to store pin number from user
    private int encpin;
    //variable to store account number from user
    private long accnum;
    AccAndPin()
    {
        //intialization
        encpin=0;
        accnum=0;

        //frame
        startframe=new JFrame();

        //backgeound image
        ImageIcon img=new ImageIcon("./images/bg4.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        //numberpad panel, pin panel, account panel
        numpanel=new JPanel();
        pinpanel=new JPanel();
        accpanel=new JPanel();

        //pin label and textfield
        plabel=new JLabel("Enter 4 digit pin");
        pfield=new JTextField();

        //account label and textfield
        alabel=new JLabel("Enter account number");
        afield=new JTextField();

        //number button 0-9
        nums=new JButton[10];

        //ok and clear button
        ok=new JButton("ok");
        clr=new JButton("clear");

        //calling mehtods
        numPad();           //numberpad creating method
        createPinpanel();   //pin panel creating method
        createAccpanel();   //account panel creating method  
        display();          //displaying frame method
        
    }
    
    //-----number pad------
    void numPad()
    {
        //number panel position,size and layout
        numpanel.setBounds(240,250,400,370);
        numpanel.setLayout(new GridLayout(4,4,13,13));

        //initalizing buttons
        for(int i=0;i<10;i++)
        {
            nums[i]=new JButton(String.valueOf(i));
            nums[i].setFont(new Font(Font.DIALOG,Font.PLAIN,30));
            nums[i].setFocusable(false);
            nums[i].setBorderPainted(false);
            nums[i].setBackground(new Color(53,103,227));
            nums[i].setForeground(new Color(230,230,230));
            nums[i].addActionListener(this);
        }

        //ok button
        ok.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        ok.setBackground(new Color(73,217,83));
        ok.setForeground(new Color(230,230,230));
        ok.setFocusable(false);
        ok.setBorderPainted(false);
        okButtonAction();   //calling method to perform click operation 
    
        //clear button
        clr.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        clr.setFocusable(false); 
        clr.setBackground(new Color(217,73,83));
        clr.setForeground(new Color(230,230,230));
        clr.setBorderPainted(false);
        clrButtonAction();  //calling method to perform click operation



        //adding buttons in the number pad panel
        numpanel.add(nums[1]);
        numpanel.add(nums[2]);
        numpanel.add(nums[3]);
        numpanel.add(nums[4]);
        numpanel.add(nums[5]);
        numpanel.add(nums[6]);
        numpanel.add(nums[7]);
        numpanel.add(nums[8]);
        numpanel.add(nums[9]);
        numpanel.add(clr);
        numpanel.add(nums[0]);
        numpanel.add(ok);

        //transparent background and visibility
        numpanel.setBackground(new Color(0,0,0,0));
        numpanel.setVisible(true);

        //adding panel to frmae
        startframe.add(numpanel);
    }

    //-----account panel-----
    void createAccpanel(){
        //first we need account panel only so we hide pin panel visibility to false
        pinpanel.setVisible(false);

        //account panel position, size, background, layout, visibility
        accpanel.setBounds(200,30,500,150);
        accpanel.setBackground(new Color(0,0,0,0));
        accpanel.setLayout(null);
        accpanel.setVisible(true);
        
        //account label to convey user to enter account number
        alabel.setBounds(20,20,450,30);
        alabel.setForeground(Color.WHITE);
        alabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        alabel.setHorizontalAlignment(JLabel.CENTER);

        //account text field to enter account number
        afield.setHorizontalAlignment(JTextField.CENTER);
        afield.setEditable(false);
        afield.setBounds(70,70,350,50);
        afield.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

        //adding label and textfield to panel
        accpanel.add(alabel);
        accpanel.add(afield);

        //addding account panel to frame
        startframe.add(accpanel);

    }

    //-----pin panel-----
    void createPinpanel(){
        //pin panel position and size
        pinpanel.setBounds(200,30,500,150);

        //pin panel background,layout,visiblity
        pinpanel.setBackground(new Color(0,0,0,0));
        pinpanel.setLayout(null);
        pinpanel.setVisible(true);

        //pin label to convey user to enter pin number
        plabel.setBounds(20,20,450,30);
        plabel.setForeground(Color.WHITE);
        plabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        plabel.setHorizontalAlignment(JLabel.CENTER);

        //pin text field to enter pin number
        pfield.setHorizontalAlignment(JTextField.CENTER);
        pfield.setBounds(120,70,250,50);
        pfield.setEditable(false);
        pfield.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

        //adding label and panel to pin panel
        pinpanel.add(plabel);
        pinpanel.add(pfield);

        //adding pin panel to frame
        startframe.add(pinpanel);
    }   

    //-----num button action-----
    public void actionPerformed(ActionEvent ae)
    {
        //number to be added to the text field of pin or account
        //if account panel is visible we have to add the numbers to accounttextfield
        //if pin panel is visible we have to add the numbers to pin textfield
        //because number panel is common

        //checking if account panel is visible
        if(accpanel.isVisible())
        {
            //get text from acccount and concat it with number from number pad 
            //and set it to the account testfield 
            String num=afield.getText()+((JButton)ae.getSource()).getText();
            //lenght 24 because of "12 numbers" and "12 whitespaces"
            if(num.length()<=24){
                afield.setText(num+" ");
            }
        }
        //else pin panel is visible
        else{
            //get text from pin and concat it with "X"
            //for security purpose concat "X" instead of numbers from number pad
            //and set it to the pin testfield 
            //but we store the actual value in "encpin" variable
            String pin=pfield.getText()+"X";
            //lenght 24 because of "12 numbers" and "12 whitespaces"
            if(pin.length()<=8){
                encpin=(encpin*10)+Integer.parseInt(((JButton)ae.getSource()).getText());
                pfield.setText(pin+" ");
            }
        }
    }

    //-----ok button action-----
    void okButtonAction()
    {
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                //if account panel is visible ok button has ot check account number with db
               if(accpanel.isVisible() && afield.getText().length()>0)
               {
                    DbCRUD db=new DbCRUD();
                    String anum=afield.getText().replace(" ", "");
                    long an=Long.parseLong(anum);
                    try{
                        ResultSet rs=db.select(an);
                        rs.next();
                        pinnumber=rs.getInt("pin");
                        accnum=rs.getLong("accno");
                        System.out.println(rs.getString("name"));
                        accpanel.setVisible(false);
                        afield.setText("");
                        pinpanel.setVisible(true);
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(startframe,"no such account","Error",JOptionPane.ERROR_MESSAGE);
                        afield.setText("");
                        System.out.println(e);
                    }
               }
               //if pin panel is visible ok button have to check user pin number with db pin number
               else if(pinpanel.isVisible() && pfield.getText().length()>0){
                    //check if both user and db pin number are equal
                    if(encpin==pinnumber)
                    {
                        startframe.dispose();
                        //passing account number and number to the next level
                        MainFrame mframe=new MainFrame(accnum,pinnumber);
                    }
                    //if both doesn't match convey it to the user
                    else{
                        JOptionPane.showMessageDialog(startframe,"incorrect pin", "Error", JOptionPane.ERROR_MESSAGE);
                        encpin=0;
                        pfield.setText("");
                    }
               }
            }
        });
    }

    //-----clr button action------
    void clrButtonAction()
    {
        //clear both account textfield and pin textfield after click clear button
        clr.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                afield.setText("");
                pfield.setText("");
                encpin=0;
            }
        });
    }

    //-----frame-----
    void display()
    {
        //frame position,size and layout
        startframe.setSize(900,700);
        startframe.setLocationRelativeTo(null);
        startframe.setLayout(null);
        //set resizable to false now frame can't be resized
        startframe.setResizable(false);
        //frame closing function
        startframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(startframe,"are you sure? you want to exit","close",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    //if yes is selected "atm" frame was created and current frame was disposed
                    Atm atm=new Atm();
                    startframe.dispose();
                }
                else{
                    //if no is selected current frame is not closed
                    startframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //adding background image label to frame
        startframe.add(bg);
        
        //set frame to visible
        startframe.setVisible(true);
    }

    //------main-------
    // public static void main(String[] args) {
    //     AccAndPin ln=new AccAndPin();
    //     // ln.numPad();
    //     // ln.createPinpanel();
    //     // ln.createAccpanel();
    //     // ln.display();
    // }
}