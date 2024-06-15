package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CreateAccount
{
    JFrame caframe;
    JPanel form;
    JButton create,back;
    JLabel nameLabel,mobLabel,accnoLabel,pinLabel,amtLabel;
    JLabel bg;
    JPanel loading;
    JProgressBar lbar;
    JLabel text,msg;
    JButton close;
    JTextField nameField,mobField,accField,pinField,amtField;
    Timer loop;
    String name;
    int pin;
    long acc,amt,mob;

    CreateAccount()
    {
        name="";
        acc=amt=mob=pin=0;

        caframe=new JFrame("Create Account");
       
        ImageIcon img=new ImageIcon("./images/bg3.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        form=new JPanel();

        create=new JButton("create");
        //back button
        back=new JButton("back");
        back.setBounds(5,605,150,50);
        back.setFont(new Font("arial",Font.PLAIN,25));
        back.setBackground(new Color(217,73,83));
        back.setForeground(new Color(230,230,230));
        back.setFocusable(false);
        caframe.add(back);
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                Atm atm=new Atm();
                caframe.dispose();
            }
        });
        nameLabel=new JLabel("Name");
        mobLabel=new JLabel("Mobile number");
        accnoLabel=new JLabel("Account number");
        pinLabel=new JLabel("PIN");
        amtLabel=new JLabel("Amount");
        
        nameField=new JTextField();
        mobField=new JTextField();
        accField=new JTextField();
        pinField=new JTextField();
        amtField=new JTextField();

        createForm();
        displayFrame();
    }

    //form
    void createForm()
    {
        form.setBounds(200,70,500,300);
        form.setBackground(new Color(0,0,0,0));
        form.setLayout(new GridLayout(5,2,15,25));

        nameLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        nameLabel.setForeground(Color.WHITE);
        mobLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        mobLabel.setForeground(Color.WHITE);
        accnoLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        accnoLabel.setForeground(Color.WHITE);
        pinLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        pinLabel.setForeground(Color.WHITE);
        amtLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        amtLabel.setForeground(Color.WHITE);

        nameField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        mobField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        accField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        pinField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        amtField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));

        create.setBounds(350,420,200,50);
        create.setFont(new Font("arial",Font.PLAIN,25));
        create.setBackground(new Color(73,217,83));
        create.setForeground(new Color(230,230,230));
        create.setFocusable(false);
        caframe.add(create);
        create.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                if(validate())
                {
                    
                    DbCRUD db = new DbCRUD();
                    int res = db.insert(name, mob, acc, pin, amt);
                    if (res == 0) {
                        JOptionPane.showMessageDialog(caframe,"account already exists","Error",JOptionPane.ERROR_MESSAGE);
                        name="";
                        acc=amt=mob=pin=0;
                        nameField.setText("");
                        mobField.setText("");
                        accField.setText("");
                        pinField.setText("");
                        amtField.setText("");
                    }
                    else
                    {
                        loadingbar();
                    }
                }
            }
        });


        form.add(nameLabel);
        form.add(nameField);
        form.add(mobLabel);
        form.add(mobField);
        form.add(accnoLabel);
        form.add(accField);
        form.add(pinLabel);
        form.add(pinField);
        form.add(amtLabel);
        form.add(amtField);

        form.setVisible(true);
        caframe.add(form);
    }

    Boolean validate()
    {

        //all empty check
        if(nameField.getText().equals("") || mobField.getText().equals("") || accField.getText().equals("") || pinField.getText().equals("") || amtField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(caframe,"fill out all the fields","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //name check
        else if(!nameField.getText().matches("[a-zA-Z ]+"))
        {
            JOptionPane.showMessageDialog(caframe,"name can contain only alphabets","Error",JOptionPane.ERROR_MESSAGE);
            nameField.setText("");
            return false;
        }
        //mobile number check
        else if(!mobField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(caframe,"mobile number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            mobField.setText("");
            return false;
        }
        else if(!(mobField.getText().length()==10))
        {
            JOptionPane.showMessageDialog(caframe,"mobile number must contain 10 numbers","Error",JOptionPane.ERROR_MESSAGE);
            mobField.setText("");
            return false;
        }
        //account number check
        else if(!accField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(caframe,"account number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            accField.setText("");
            return false;
        }
        else if(!(accField.getText().length()==12))
        {
            JOptionPane.showMessageDialog(caframe,"acount number must contain 12 numbers","Error",JOptionPane.ERROR_MESSAGE);
            accField.setText("");
            return false;
        }
        //pin number check
        else if(!pinField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(caframe,"pin number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        else if(!(pinField.getText().length()==4))
        {
            JOptionPane.showMessageDialog(caframe,"pin number must contain 4 numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        //amount check
        else if(!amtField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(caframe,"amount can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        else if(!(amtField.getText().length()<=12))
        {
            JOptionPane.showMessageDialog(caframe,"amount only contain upto 12 numbers","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        else if(amtField.getText().startsWith("0"))
        {
            JOptionPane.showMessageDialog(caframe,"amount doesn't starts with 0","Error",JOptionPane.ERROR_MESSAGE);
            amtField.setText("");
            return false;
        }
        //else part
        else{
            name=nameField.getText();
            pin=Integer.parseInt(pinField.getText());
            mob=Long.parseLong(mobField.getText());
            acc=Long.parseLong(accField.getText());
            amt=Long.parseLong(amtField.getText());
            return true;
        }
    }

    //loading bar
    void loadingbar(){
        form.setVisible(false);
        back.setVisible(false);
        create.setVisible(false);

        ImageIcon newimg=new ImageIcon("./images/bg1.png");
        bg.setIcon(newimg);

        loading=new JPanel();
        
        loading.setBounds(220,220,500,200);
        loading.setBackground(new Color(0,0,0,0));
        loading.setLayout(null);
        loading.setVisible(true);
        

        text=new JLabel("processing please wait...");
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
        
        caframe.add(loading);
                
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
        caframe.setSize(900,700);
        caframe.setLocationRelativeTo(null);
        caframe.setLayout(null);
        caframe.setResizable(false);
        caframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(caframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    caframe.dispose();
                    // MainFrame.mainframe.dispose();
                }
                else{
                    caframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //success message
        msg=new JLabel("Account created successfully :)");
        msg.setBounds(200,220,500,100);
        msg.setForeground(Color.white);
        msg.setFont(new Font(Font.DIALOG, Font.PLAIN, 35));
        msg.setVisible(false);
        caframe.add(msg);

        //close button after account creation completed
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
                    caframe.dispose();
                    Atm atm=new Atm();
                }
        });
        //close button is hide until account creation complete
        close.setVisible(false);
        caframe.add(close);
        
        //adding components
        caframe.add(bg);

        caframe.setVisible(true);
    }

    
}
