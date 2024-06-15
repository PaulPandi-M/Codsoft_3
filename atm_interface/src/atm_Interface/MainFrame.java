package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MainFrame {
    static JFrame mainframe;
    JLabel bg;
    JButton wd,dep,bal,tf,pc,cancel;
    JLabel title;
    private int pinnumber;
    private long accnumber;
    MainFrame(){
        pinnumber=0;
        accnumber=0;
    }
    MainFrame(long accnum,int pinnum)
    {
        pinnumber=pinnum;
        accnumber=accnum;
        DbCRUD db=new DbCRUD();
        try{
            ResultSet rs=db.select(accnum);
            rs.next();
            pinnumber=rs.getInt("pin");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }

        mainframe=new JFrame("main frame");

        //background
        ImageIcon img=new ImageIcon("./images/bg2.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);


        wd=new JButton("withdraw");
        dep=new JButton("deposit");
        bal=new JButton("balance");
        tf=new JButton("transfer");
        //6
        pc=new JButton("change PIN");
        cancel=new JButton("Cancel");


        displayButtons();
        display();
    }
    void displayButtons()
    {
        wd.setBounds(630,200,250,50);
        wd.setFont(new Font("arial",Font.PLAIN,25));
        wd.setBackground(new Color(217,37,37));
        wd.setForeground(new Color(230,230,230));
        wd.setFocusable(false);
        wd.setBorderPainted(false);
        wd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                Withdraw wdr=new Withdraw(accnumber,pinnumber);
            }
        });

        tf.setBounds(5,200,250,50);
        tf.setFont(new Font("arial",Font.PLAIN,25));
        tf.setBackground(new Color(217,37,37));
        tf.setForeground(new Color(230,230,230));
        tf.setBorderPainted(false);
        tf.setFocusable(false);
        tf.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                Transfer tf=new Transfer(accnumber, pinnumber);
            }
        });
        
        dep.setBounds(630,350,250,50);
        dep.setFont(new Font("arial",Font.PLAIN,25));
        dep.setBackground(new Color(217,37,37));
        dep.setForeground(new Color(230,230,230));
        dep.setFocusable(false);
        dep.setBorderPainted(false);
        dep.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                Deposit dp=new Deposit(accnumber, pinnumber);
            }
        });

        pc.setBounds(5,350,250,50);
        pc.setFont(new Font("arial",Font.PLAIN,25));
        pc.setBackground(new Color(217,37,37));
        pc.setForeground(new Color(230,230,230));
        pc.setBorderPainted(false);
        pc.setFocusable(false);
        pc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                ChangePin cp=new ChangePin(accnumber);
            }
        });

        bal.setBounds(630,500,250,50);
        bal.setFont(new Font("arial",Font.PLAIN,25));
        bal.setBackground(new Color(217,37,37));
        bal.setForeground(new Color(230,230,230));
        bal.setBorderPainted(false);
        bal.setFocusable(false);
        bal.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                Balance bl=new Balance(accnumber, pinnumber);
            }
        });
        
        cancel.setBounds(5,500,250,50);
        cancel.setFont(new Font("arial",Font.PLAIN,25));
        cancel.setBackground(new Color(217,37,37));
        cancel.setForeground(new Color(230,230,230));
        cancel.setBorderPainted(false);
        cancel.setFocusable(false);
        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                mainframe.setVisible(false);
                Atm atm=new Atm();
            }
        });

        mainframe.add(wd);
        mainframe.add(tf);
        mainframe.add(dep);
        mainframe.add(pc);
        mainframe.add(bal);
        mainframe.add(cancel);
    }
    void display()
    {
        mainframe.setSize(900,700);
        mainframe.setLocationRelativeTo(null);
        mainframe.setLayout(null);
        mainframe.setResizable(false);
        mainframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(mainframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    mainframe.dispose();
                }
                else{
                    mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //adding components
        // mainframe.add(title);
        mainframe.add(bg);
        
        mainframe.setVisible(true);
    }
    public static void main(String args[])
    {
        MainFrame main=new MainFrame(0,0);
    }
}