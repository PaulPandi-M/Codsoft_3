package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePin {
    JFrame cpframe;
    JPanel form;
    JButton change;
    JButton back;
    JLabel accLabel,pinLabel,repinLabel;
    JLabel bg;
    JTextField accField,pinField,repinField;
    JPanel loading;
    JProgressBar lbar;
    JLabel text,msg;
    JButton close;
    Timer loop;
    long acc;
    int pin,repin;
    ChangePin(long accno)
    { 
        acc=accno;
        pin=repin=0;

        cpframe=new JFrame("Create Account");
    
        ImageIcon img=new ImageIcon("./images/bg3.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        form=new JPanel();

        change=new JButton("change");

        //back button
        back=new JButton("back");
        back.setBounds(5,605,150,50);
        back.setFont(new Font("arial",Font.PLAIN,25));
        back.setBackground(new Color(217,73,83));
        back.setForeground(new Color(230,230,230));
        back.setFocusable(false);
        cpframe.add(back);
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                MainFrame.mainframe.setVisible(true);
                cpframe.dispose();
            }
        });

        //labels
        accLabel=new JLabel("Account number");
        pinLabel=new JLabel("Enter PIN");
        repinLabel=new JLabel("Re-Enter PIN");

        //textField
        accField=new JTextField();
        pinField=new JTextField();
        repinField=new JTextField();

        createForm();
        displayFrame();
    }

    //form
    void createForm()
    {
        form.setBounds(200,200,500,200);
        form.setBackground(new Color(0,0,0,0));
        form.setLayout(new GridLayout(3,2,15,25));

        accLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        accLabel.setForeground(Color.WHITE);
        pinLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        pinLabel.setForeground(Color.WHITE);
        repinLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        repinLabel.setForeground(Color.WHITE);

        accField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        accField.setText(String.valueOf(acc));
        accField.setEditable(false);
        repinField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));
        pinField.setFont(new Font(Font.DIALOG,Font.PLAIN,22));

        change.setBounds(350,440,200,50);
        change.setFont(new Font("arial",Font.PLAIN,25));
        change.setBackground(new Color(73,217,83));
        change.setForeground(new Color(230,230,230));
        change.setFocusable(false);
        cpframe.add(change);
        change.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                if(validate())
                {
                    DbCRUD db=new DbCRUD();
                    String query="UPDATE userdata SET pin="+pin+" WHERE accno="+acc;
                    int res=db.update(query);
                    if(res==0)
                    {
                        JOptionPane.showMessageDialog(cpframe,"Error occured while updating","Error",JOptionPane.ERROR_MESSAGE);
                        pinField.setText("");
                        repinField.setText("");
                        pin=repin=0;
                    }
                    else{
                        loadingbar();
                    }
                }
            }
        });

        form.add(accLabel);
        form.add(accField);
        form.add(pinLabel);
        form.add(pinField);
        form.add(repinLabel);
        form.add(repinField);

        form.setVisible(true);
        cpframe.add(form);
    }

    //validate
    Boolean validate()
    {
        if(pinField.getText().equals("") || repinField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(cpframe,"fill out all the fields","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //pin number check
        else if(!pinField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(cpframe,"pin number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        else if(!(pinField.getText().length()==4))
        {
            JOptionPane.showMessageDialog(cpframe,"pin number must contain 4 numbers","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            return false;
        }
        //repin number check
        else if(!repinField.getText().matches("[0-9]+"))
        {
            JOptionPane.showMessageDialog(cpframe,"repin number can contain only numbers","Error",JOptionPane.ERROR_MESSAGE);
            repinField.setText("");
            return false;
        }
        else if(!(repinField.getText().length()==4))
        {
            JOptionPane.showMessageDialog(cpframe,"repin number must contain 4 numbers","Error",JOptionPane.ERROR_MESSAGE);
            repinField.setText("");
            return false;
        }
        else if(!(pinField.getText().equals(repinField.getText())))
        {
            JOptionPane.showMessageDialog(cpframe,"PIN doesn't match","Error",JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
            repinField.setText("");
            return false;
        }
        else{
            pin=Integer.parseInt(pinField.getText());
            repin=Integer.parseInt(repinField.getText());
            return true;
        }
    }

    //loading bar
    void loadingbar(){
        form.setVisible(false);
        back.setVisible(false);
        change.setVisible(false);

        ImageIcon newimg=new ImageIcon("./images/bg1.png");
        bg.setIcon(newimg);

        loading=new JPanel();
        
        loading.setBounds(220,220,500,200);
        loading.setBackground(new Color(0,0,0,0));
        loading.setLayout(null);
        loading.setVisible(true);

        text=new JLabel("updating please wait...");
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
        
        cpframe.add(loading);
                
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
        cpframe.setSize(900,700);
        cpframe.setLocationRelativeTo(null);
        cpframe.setLayout(null);
        cpframe.setResizable(false);
        cpframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(cpframe,"are you sure? you want to exit","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    Atm atm=new Atm();
                    cpframe.dispose();
                    MainFrame.mainframe.dispose();
                }
                else{
                    cpframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        //success message
        msg=new JLabel("pin changed successfully :)");
        msg.setBounds(250,200,500,200);
        msg.setForeground(Color.white);
        msg.setFont(new Font(Font.DIALOG, Font.PLAIN, 35));
        msg.setVisible(false);
        cpframe.add(msg);

        //close button after pin change completed
        close=new JButton("Close");
        close.setBounds(350,350,200,50);
        close.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        close.setBackground(new Color(217,37,37));
        close.setForeground(new Color(230,230,230));
        close.setBorderPainted(false);
        close.setFocusable(false);
        close.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    cpframe.dispose();
                    MainFrame.mainframe.setVisible(true);
                }
        });
        //close button is hide until pin change process complete
        close.setVisible(false);
        cpframe.add(close);
        
        //adding components
        cpframe.add(bg);

        cpframe.setVisible(true);
    }

    // public static void main(String[] args) {
    //     ChangePin cp=new ChangePin(0);
    // }
}