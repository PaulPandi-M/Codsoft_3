package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Transfer {
    JFrame tframe;
    JPanel form;
    JButton transfer;
    JButton back;
    JLabel fromLabel,toLabel,pinLabel,amtLabel;
    JLabel bg;
    JTextField fromField,toField,pinField,amtField;
    JPanel loading;
    JProgressBar lbar;
    JLabel text,msg;
    JButton close;
    Timer loop;
    long fromacc,toacc,amt;
    int mainpin,pin;
    Transfer(long accno,int pinno)
    {
        fromacc=accno;
        mainpin=pinno;
        DbCRUD db=new DbCRUD();
        try{
            ResultSet rs=db.select(fromacc);
            rs.next();
            mainpin=rs.getInt("pin");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        toacc=amt=0;

        tframe=new JFrame("Create Account");
    
        ImageIcon img=new ImageIcon("./images/bg3.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        form=new JPanel();

        transfer=new JButton("transfer");

        //back button
        back=new JButton("back");
        back.setBounds(5,605,150,50);
        back.setFont(new Font("arial",Font.PLAIN,25));
        back.setBackground(new Color(217,73,83));
        back.setForeground(new Color(230,230,230));
        back.setFocusable(false);
        tframe.add(back);
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                MainFrame.mainframe.setVisible(true);
                tframe.dispose();
            }
        });

        //labels
        fromLabel=new JLabel("From Account");
        toLabel=new JLabel("To Account");
        amtLabel=new JLabel("Amount");
        pinLabel=new JLabel("PIN");

        //textfields
        fromField=new JTextField();
        toField=new JTextField();
        amtField=new JTextField();
        pinField=new JTextField();

        createForm();
        displayFrame();
    }
    
    //form
    void createForm()
    {
        form.setBounds(200,150,500,250);
        form.setBackground(new Color(0,0,0,0));
        form.setLayout(new GridLayout(4,2,15,25));

        fromLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        fromLabel.setForeground(Color.WHITE);
        toLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        toLabel.setForeground(Color.WHITE);
        amtLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        amtLabel.setForeground(Color.WHITE);
        pinLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        pinLabel.setForeground(Color.WHITE);

        fromField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        fromField.setText(String.valueOf(fromacc));
        fromField.setEditable(false);
        toField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        amtField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        pinField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));

        transfer.setBounds(350,440,200,50);
        transfer.setFont(new Font("arial",Font.PLAIN,25));
        transfer.setBackground(new Color(73,217,83));
        transfer.setForeground(new Color(230,230,230));
        transfer.setFocusable(false);
        tframe.add(transfer);
        transfer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                if(validate())
                {
                    DbCRUD db=new DbCRUD();
                    String fromquery="",toquery="";
                    int fromres=0,tores=0;
                    try{
                        ResultSet frs=db.select(fromacc);
                        frs.next();
                        fromquery="UPDATE userdata SET balance="+(frs.getLong("balance")-amt)+" WHERE accno="+fromacc;
                        fromres=db.update(fromquery);
                        ResultSet trs=db.select(toacc);
                        trs.next();
                        toquery="UPDATE userdata SET balance="+(trs.getLong("balance")+amt)+" WHERE accno="+toacc;
                        tores=db.update(toquery);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                    if(fromres==0 || tores==0)
                    {
                        JOptionPane.showMessageDialog(tframe,"Error occured while transfering check account number","Error",JOptionPane.ERROR_MESSAGE);
                        toacc=0;
                        amt=0;
                        pin=0;
                    }
                    else
                    {
                        loadingbar();
                    }
                }
            }
        });

        form.add(fromLabel);
        form.add(fromField);
        form.add(toLabel);
        form.add(toField);
        form.add(amtLabel);
        form.add(amtField);
        form.add(pinLabel);
        form.add(pinField);

        form.setVisible(true);
        tframe.add(form);
    }

    //validate
    Boolean validate()
    {
        DbCRUD db=new DbCRUD();
        //all empty check
        if(fromField.getText().equals("") || toField.getText().equals("") || amtField.getText().equals("") || pinField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(tframe,"fill out all the fields","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //to account check
        else if(!toField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(tframe,"account number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            toField.setText("");
            return false;
        }
        else if(!(toField.getText().length()==12))
        {
            JOptionPane.showMessageDialog(tframe,"acount number must contain 12 numbers","Error",JOptionPane.ERROR_MESSAGE);
            toField.setText("");
            return false;
        }
        //pin number check
        else if(!pinField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(tframe,"pin number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        else if(!(pinField.getText().length()==4))
        {
            JOptionPane.showMessageDialog(tframe,"pin number must contain 4 numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        //amount check
        else if(!amtField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(tframe,"amount can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        else if(!(amtField.getText().length()<=12))
        {
            JOptionPane.showMessageDialog(tframe,"amount only contain upto 12 numbers","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        else if(amtField.getText().startsWith("0"))
        {
            JOptionPane.showMessageDialog(tframe,"amount doesn't starts with 0","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        else
        {   
            pin=Integer.parseInt(pinField.getText());
            DbCRUD todb=new DbCRUD();
            boolean toAccCheck=true;
            int availbal=0;
            try{
                ResultSet balrs=todb.select(fromacc);
                balrs.next();
                availbal=balrs.getInt("balance");
                balrs.close();
                ResultSet toAccRs=todb.select(Long.parseLong(toField.getText()));
                toAccCheck=toAccRs.next();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            if(!toAccCheck)
            {
                JOptionPane.showMessageDialog(tframe,"to account doesn't exists","Error",JOptionPane.ERROR_MESSAGE);
                toField.setText("");
                return false;
            }
            else if(pin!=mainpin)
            {
                JOptionPane.showMessageDialog(tframe,"pin doesn't match with from account number","Error",JOptionPane.ERROR_MESSAGE);
                pinField.setText("");
                return false;
            }
            else if(Long.parseLong(amtField.getText())>availbal)
            {
                JOptionPane.showMessageDialog(tframe,"insufficient amount in from accont number","Error",JOptionPane.ERROR_MESSAGE);
                amtField.setText("");
                return false;
            }
            else{
                toacc=Long.parseLong(toField.getText());
                amt=Long.parseLong(amtField.getText());
                return true;
            }
        }
        
    }

    //loading bar
    void loadingbar(){
        form.setVisible(false);
        back.setVisible(false);
        transfer.setVisible(false);

        ImageIcon newimg=new ImageIcon("./images/bg1.png");
        bg.setIcon(newimg);

        loading=new JPanel();
        
        loading.setBounds(220,220,500,200);
        loading.setBackground(new Color(0,0,0,0));
        loading.setLayout(null);
        loading.setVisible(true);

        text=new JLabel("transfering please wait...");
        text.setBounds(110,20,300,50);
        text.setForeground(Color.white);
        text.setFont(new Font(Font.DIALOG,Font.PLAIN,20));


        lbar=new JProgressBar(0,3000);
        lbar.setStringPainted(true);
        lbar.setBounds(20, 85, 400, 50);
        lbar.setFont(new Font(Font.DIALOG,Font.PLAIN,20));
        lbar.setForeground(new Color(217,37,37));

        
        loading.add(text);
        loading.add(lbar);
        
        tframe.add(loading);
                
        loop = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                lbar.setValue(lbar.getValue() + 200);
                if(lbar.getValue()==3000)
                {
                    loop.setRepeats(false);
                    loop.stop();
                    loading.setVisible(false);
                    
                    msg.setVisible(true);
                    close.setVisible(true);
                }
            }
        });
        loop.start();
    }

    //frame
    void displayFrame()
    {
        tframe.setSize(900,700);
        tframe.setLocationRelativeTo(null);
        tframe.setLayout(null);
        tframe.setResizable(false);
        tframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(tframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    tframe.dispose();
                    MainFrame.mainframe.dispose();
                }
                else{
                    tframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //success message
        msg=new JLabel("Amount transfered successfully :)");
        msg.setBounds(200,220,550,100);
        msg.setForeground(Color.white);
        msg.setFont(new Font(Font.DIALOG, Font.PLAIN, 35));
        msg.setVisible(false);
        tframe.add(msg);

        //close button after transfer completed
        close=new JButton("Close");
        close.setBounds(350,330,200,50);
        close.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        close.setBackground(new Color(217,37,37));
        close.setForeground(new Color(230,230,230));
        close.setBorderPainted(false);
        close.setFocusable(false);
        close.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    tframe.dispose();
                    MainFrame.mainframe.setVisible(true);
                }
        });
        //close button is hide until tranfer complete
        close.setVisible(false);
        tframe.add(close);
        
        //adding components
        tframe.add(bg);

        tframe.setVisible(true);
    }

    // public static void main(String[] args) {
    //     Transfer tr=new Transfer(0, 0);
    // }
}