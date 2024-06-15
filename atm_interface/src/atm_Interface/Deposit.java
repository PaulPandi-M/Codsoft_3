package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//6
public class Deposit implements ActionListener{
    JFrame dframe;
    JPanel numpanel;
    JPanel amtpanel;
    JPanel pinpanel;
    // JPanel loading;
    JLabel alabel,plabel;
    JLabel bg; 
    JTextField pfield,afield;
    JButton[] nums;
    JButton ok,clr;
    JButton back;
    Timer loop;


    private int pinnumber;
    private long accnumber;
    private int encpin;
    private long amount;
    private long bal;
    Deposit(){
        pinnumber=0;
        accnumber=0;
        
    }
    Deposit(long accnum,int pinnum)
    {
        pinnumber=pinnum;
        accnumber=accnum;
        DbCRUD db=new DbCRUD();
        try{
            ResultSet rs=db.select(accnumber);
            rs.next();
            pinnumber=rs.getInt("pin");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        encpin=0;
        amount=0;
        bal=0;

        dframe=new JFrame("withdraw");

        //bg 
        ImageIcon img=new ImageIcon("./images/bg3.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        numpanel=new JPanel();
        pinpanel=new JPanel();
        amtpanel=new JPanel();

        afield=new JTextField();
        alabel=new JLabel("Enter amount");

        plabel=new JLabel("Enter 4 digit pin");
        pfield=new JTextField();


        nums=new JButton[10];

        ok=new JButton("ok");
        clr=new JButton("clear");
        back=new JButton("Back");

        createPinpanel();
        createAmtpanel();
        numPad();
        display();
    }

    void loadingbar(){
        pinpanel.setVisible(false);
        numpanel.setVisible(false);
        back.setVisible(false);

        ImageIcon newimg=new ImageIcon("./images/bg1.png");
        bg.setIcon(newimg);

        JPanel loading=new JPanel();
        
        loading.setBounds(220,220,500,200);
        loading.setLayout(null);
        loading.setVisible(true);

        JLabel text=new JLabel("processing please wait...");
        text.setBounds(110,20,300,50);
        text.setFont(new Font(Font.DIALOG,Font.PLAIN,20));


        JProgressBar lbar=new JProgressBar(0,3000);
        lbar.setStringPainted(true);
        lbar.setBounds(20, 85, 400, 50);
        lbar.setFont(new Font(Font.DIALOG,Font.PLAIN,20));
        lbar.setForeground(new Color(217,37,37));
        
        loading.add(text);
        loading.add(lbar);
        
        dframe.add(loading);
                
        loop = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                lbar.setValue(lbar.getValue() + 200);
                if(lbar.getValue()==3000)
                {
                    Balance bl=new Balance(accnumber, pinnumber);
                    loop.setRepeats(false);
                    loop.stop();
                    dframe.dispose();
                }
            }
        });
        loop.start();
    }


    void numPad()
    {
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
        ok.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        ok.setBackground(new Color(73,217,83));
        ok.setForeground(new Color(230,230,230));
        ok.setFocusable(false);
        ok.setBorderPainted(false);
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                if(amtpanel.isVisible() && afield.getText().length()>0)
                {
                    String stramt=afield.getText().replace(" ", "");
                    amount=Long.parseLong(stramt);
                    if (amount == 0) {
                        JOptionPane.showMessageDialog(dframe, "0 not accepted", "Error", JOptionPane.WARNING_MESSAGE);
                        amount = 0;
                        afield.setText("");
                    } else {
                        System.out.println(amount);
                        amtpanel.setVisible(false);
                        afield.setText("");
                        pinpanel.setVisible(true);
                    }
                }
                else if(pinpanel.isVisible() && pfield.getText().length()>0)
                {
                    if(encpin==pinnumber)
                    {
                        System.out.println("deposit success");
                        DbCRUD db=new DbCRUD();
                        try{
                            ResultSet rs=db.select(accnumber);
                            rs.next();
                            bal=rs.getLong("balance");
                            String query="UPDATE userdata SET balance="+(bal+amount)+" WHERE accno="+accnumber;
                            db.update(query);
                            db.dbClose();
                        }
                        catch(Exception e)
                        {
                            System.out.println(e);
                        }
                        loadingbar();
                    }
                    else{
                        JOptionPane.showMessageDialog(dframe,"incorrect pin", "Error", JOptionPane.ERROR_MESSAGE);
                        encpin=0;
                        pfield.setText("");
                    }
               }
            }
        });
        

        clr.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        clr.setFocusable(false); 
        clr.setBackground(new Color(217,73,83));
        clr.setForeground(new Color(230,230,230));
        clr.setBorderPainted(false);
        clr.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                afield.setText("");
                pfield.setText("");
                encpin=0;
            }
        });


        //back button
        back.setBounds(5,605,150,50);
        back.setFont(new Font("arial",Font.PLAIN,25));
        back.setBackground(new Color(217,73,83));
        back.setForeground(new Color(230,230,230));
        back.setFocusable(false);
        back.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                dframe.dispose();
                DbCRUD db=new DbCRUD();
                db.dbClose();
                MainFrame.mainframe.setVisible(true);
            }
        });
        dframe.add(back);

        //adding buttons
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

        numpanel.setBackground(new Color(0,0,0,0));
        numpanel.setVisible(true);
        dframe.add(numpanel);
    }

    void createAmtpanel(){
        pinpanel.setVisible(false);

        amtpanel.setBounds(200,30,500,150);
        
        alabel.setBounds(20,20,450,30);
        alabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        alabel.setForeground(Color.WHITE);
        alabel.setHorizontalAlignment(JLabel.CENTER);

        afield.setHorizontalAlignment(JTextField.CENTER);
        afield.setEditable(false);
        afield.setBounds(70,70,350,50);
        afield.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

        amtpanel.add(alabel);
        amtpanel.add(afield);
        amtpanel.setBackground(new Color(0,0,0,0));
        amtpanel.setLayout(null);
        amtpanel.setVisible(true);
        dframe.add(amtpanel);
    }

    void createPinpanel(){
        pinpanel.setBounds(200,30,500,150);

        
        plabel.setBounds(20,20,450,30);
        plabel.setForeground(Color.WHITE);
        plabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        plabel.setHorizontalAlignment(JLabel.CENTER);

        pfield.setHorizontalAlignment(JTextField.CENTER);
        pfield.setBounds(120,70,250,50);
        pfield.setEditable(false);
        pfield.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

        pinpanel.add(plabel);
        pinpanel.add(pfield);
        pinpanel.setBackground(new Color(0,0,0,0));
        pinpanel.setLayout(null);
        pinpanel.setVisible(true);
        dframe.add(pinpanel);
    }

    void display()
    {


        dframe.setSize(900,700);
        dframe.setLocationRelativeTo(null);
        dframe.setLayout(null);
        dframe.setResizable(false);
        dframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(dframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    dframe.dispose();
                    DbCRUD db=new DbCRUD();
                    db.dbClose();
                    MainFrame.mainframe.dispose();
                }
                else{
                    dframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //adding components
        dframe.add(bg);
        
        dframe.setVisible(true);
    }

    //num button action
    public void actionPerformed(ActionEvent ae)
    {
        if(amtpanel.isVisible())
        {
            String num=afield.getText()+((JButton)ae.getSource()).getText();
            if(num.length()<=24){
                afield.setText(num+" ");
            }
        }
        else{
            String num=pfield.getText()+"X";
            if(num.length()<=8){
                encpin=(encpin*10)+Integer.parseInt(((JButton)ae.getSource()).getText());
                pfield.setText(num+" ");
            }
        }
    }

    // public static void main(String[] args) {
    //     Deposit dp=new Deposit(1234,1234);
        // dp.createPinpanel();
        // dp.createAmtpanel();
        // dp.numPad();
        // dp.display();
    // }
}