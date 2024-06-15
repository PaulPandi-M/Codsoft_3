package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Balance {
    JFrame bframe;
    JLabel bal,text;
    JLabel bg;
    JButton close;
    long balance;

    Balance(long accno,int pinno)
    {

    	DbCRUD db=new DbCRUD();
        try{
            ResultSet rs=db.select(accno);
            rs.next();
            pinno=rs.getInt("pin");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    	
        balance=0;

        bframe=new JFrame("Balance");
        bframe.setSize(900,700);
        bframe.setLocationRelativeTo(null);
        bframe.setLayout(null);
        bframe.setResizable(false);
        bframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(bframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    bframe.dispose();
                    MainFrame.mainframe.dispose();
                }
                else{
                    bframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //bg
        ImageIcon img=new ImageIcon("./images/bg1.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        //label
        text=new JLabel("Available Balance");
        text.setFont(new Font(Font.DIALOG,Font.PLAIN,27));
        text.setForeground(Color.WHITE);
        text.setBounds(200,200,500,50);
        text.setHorizontalAlignment(JLabel.CENTER);

        bal=new JLabel("\\u20B9"+balance);
        try{
//            DbCRUD db=new DbCRUD();
            ResultSet rs=db.select(accno);
            rs.next();
            balance=rs.getLong("balance");
            bal.setText("₹"+String.valueOf(balance));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(bframe,"no such account","Error",JOptionPane.ERROR_MESSAGE);
            bal.setText("0");

        }
        bal.setFont(new Font(Font.DIALOG,Font.PLAIN,27));
        bal.setForeground(Color.WHITE);
        bal.setBounds(200,280,500,50);
        bal.setHorizontalAlignment(JLabel.CENTER);

        close=new JButton("close");
        close.setBounds(350,370,200,50);
        close.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        close.setBackground(new Color(217,37,37));
        close.setForeground(new Color(230,230,230));
        close.setFocusable(false);
        close.setBorderPainted(false);
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                MainFrame.mainframe.setVisible(true);
                bframe.dispose();
            }
        });

        //adding components
        bframe.add(text);
        bframe.add(bal);
        bframe.add(close);
        bframe.add(bg);

        bframe.setVisible(true);
    }
    // public static void main(String args[])
    // {
    //     Balance bal=new Balance(111122223333l,1234);
    // } 
}