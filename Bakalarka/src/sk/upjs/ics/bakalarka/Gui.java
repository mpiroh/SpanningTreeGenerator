package sk.upjs.ics.bakalarka;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Gui {

	private JFrame frame;
	private JTextField textField1;
	private JTextField textField2;
	private JTextPane textPane1;
	private JTextPane textPane2;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gui() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 538, 329);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Compare Regexp");
		
		textField1 = new JTextField();
		textField1.setBounds(10, 37, 194, 20);
		frame.getContentPane().add(textField1);
		textField1.setColumns(10);
		
		textField2 = new JTextField();
		textField2.setBounds(319, 37, 194, 20);
		frame.getContentPane().add(textField2);
		textField2.setColumns(10);
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(10, 68, 194, 211);
		frame.getContentPane().add(scrollPane1);
		
		textPane1 = new JTextPane();
		scrollPane1.setViewportView(textPane1);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(319, 68, 194, 211);
		frame.getContentPane().add(scrollPane2);
		
		textPane2 = new JTextPane();
		scrollPane2.setViewportView(textPane2);
		
		JButton compareButton = new JButton("Compare");
		compareButton.setBounds(214, 36, 95, 23);
		frame.getContentPane().add(compareButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(214, 68, 95, 23);
		frame.getContentPane().add(clearButton);
		
		JLabel lblFirstRegularExpression = new JLabel("First regular expression:");
		lblFirstRegularExpression.setBounds(10, 12, 194, 14);
		frame.getContentPane().add(lblFirstRegularExpression);
		
		JLabel lblSecondRegularExpression = new JLabel("Second regular expression:");
		lblSecondRegularExpression.setBounds(319, 12, 194, 14);
		frame.getContentPane().add(lblSecondRegularExpression);
		
		compareButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				compareButtonActionPerformed();
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearButtonActionPerformed();
			}
		});
		
		/*frame.setFocusable(true);
		frame.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					compareButtonActionPerformed();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					clearButtonActionPerformed();
				}
			}
		});*/
	}
	
	public void compareButtonActionPerformed() {
		String regExp1 = textField1.getText();
		String regExp2 = textField2.getText();
		Comparator porovnavac = new Comparator();
		
		RegularExpression rv1 = new RegularExpression(regExp1);
		textPane1.setText(rv1.toNFA().determinize()/*.minimize()*/.toString());
		
		RegularExpression rv2 = new RegularExpression(regExp2);
		textPane2.setText(rv2.toNFA().determinize()/*.minimize()*/.toString());
		
		boolean result = porovnavac.compare(regExp1, regExp2);
		if (result == true) {
			JOptionPane.showMessageDialog(frame, "They ARE equal.", "Result", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(frame, "They ARE NOT equal.", "Result", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void clearButtonActionPerformed() {
		textPane1.setText("");
		textPane2.setText("");
		System.out.println(Long.MAX_VALUE);
	}
}