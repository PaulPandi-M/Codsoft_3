package atm_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Atm {
    JFrame atmframe;
    JLabel text;
    JLabel bg;
    JButton enter,newacc;
    Atm()
    {
        //frame
        atmframe=new JFrame("La Casa De Papel ATM");
        atmframe.setSize(900,700);
        atmframe.setLocationRelativeTo(null);
        atmframe.setLayout(null);
        atmframe.setResizable(false);
        atmframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                int opt=JOptionPane.showOptionDialog(atmframe,"are you sure? you want to close","exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,null,null);
                if(opt==JOptionPane.YES_OPTION)
                {
                    atmframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else{
                    atmframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //bg
        ImageIcon img=new ImageIcon("./images/bg1.png");
        bg=new JLabel("",img,JLabel.CENTER);
        bg.setBounds(0,0,900,700);

        //label
        text=new JLabel("Welcome to LCDP Bank");
        text.setFont(new Font(Font.DIALOG,Font.PLAIN,35));
        text.setForeground(Color.WHITE);
        text.setBounds(200,250,500,50);
        text.setHorizontalAlignment(JLabel.CENTER);

        //enter button
        enter=new JButton("Enter");
        enter.setBounds(200,330,200,50);
        enter.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        enter.setBackground(new Color(217,37,37));
        enter.setForeground(new Color(230,230,230));
        enter.setFocusable(false);
        enter.setBorder(BorderFactory.createEtchedBorder());
        // enter.setBorderPainted(false);
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                AccAndPin ap=new AccAndPin();
                atmframe.dispose();
            }
        });

        //new account button
        newacc=new JButton("New Account");
        newacc.setBounds(500,330,200,50);
        newacc.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        newacc.setBackground(new Color(217,37,37));
        newacc.setForeground(new Color(230,230,230));
        newacc.setFocusable(false);
        newacc.setBorder(BorderFactory.createEtchedBorder());
        // newacc.setBorderPainted(false);
        newacc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                CreateAccount ca=new CreateAccount();
                atmframe.dispose();
            }
        });

        //adding components
        atmframe.add(text);
        atmframe.add(enter);
        atmframe.add(newacc);
        atmframe.add(bg);

        atmframe.setVisible(true);
    }
    public static void main(String[] args) {
        Atm atm=new Atm();
    }
}