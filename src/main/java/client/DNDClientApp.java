package client;

import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class DNDClientApp extends JFrame {
	private JTabbedPane tabPane;
	private JMenuBar menuBar;
	private JMenu fileMenu, builderMenu;
	private JMenuItem menuItem;
	private MenuActionListener menuListener;
	private MonsterBuilder monBuilder;
	private EncounterBuilder encBuilder;
	private DNDClientProxy proxy;
	
	DNDClientApp(DNDClientProxy proxy) {
		this.proxy = proxy;
		setTitle("D&D Builder Tool");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Are you sure you wish to exit?",
					"Exit Builder", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
				if (yesNo == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
				
		tabPane = new JTabbedPane();
		tabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				encBuilder.refreshRight();
			}
		});
		
		setDefaultMenu();
		monBuilder = new MonsterBuilder(proxy);
		encBuilder = new EncounterBuilder(proxy);
		
		tabPane.addTab("MonsterBuilder", monBuilder);
		tabPane.addTab("Encounter Builder", encBuilder);

		add(tabPane);
		pack();
		setVisible(true);
	}
	
	/**
	 * Sets the menu bar for the home scrren
	 */
	private void setDefaultMenu() {
		menuBar = new JMenuBar();
		menuListener = new MenuActionListener();
		
		//setup the File menu
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		menuItem = new JMenuItem("Save Current");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Save All");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);
		
		//setup the Builders menu
		builderMenu = new JMenu("Options");
		menuBar.add(builderMenu);
		menuItem = new JMenuItem("Add");
		menuItem.addActionListener(menuListener);
		builderMenu.add(menuItem);
		menuItem = new JMenuItem("Delete");
		menuItem.addActionListener(menuListener);
		builderMenu.add(menuItem);
		menuItem = new JMenuItem("Restore");
		menuItem.addActionListener(menuListener);
		builderMenu.add(menuItem);
		setJMenuBar(menuBar);
	}
	
	/**
	 * monster.Action listener for JMenu
	 */
	private class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			//get which is the current tab, 0 == monster.Monster Builder, 1 == Encounter Builder
			int tab = tabPane.getSelectedIndex(); //gets current tab. 0 = Montser Builder, 1 = Encounter Builder
			System.out.println("Selected \"" + command + "\" in tab " + tabPane.getSelectedIndex());
			
			/**
			 * Handles program exiting
			 */
			if (command.equals("Exit")) {
				int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Are you sure you wish to exit?",
					"Exit Builder", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
				if (yesNo == JOptionPane.YES_OPTION)
					System.exit(0);
			}
			
			else if (command.equals("Save All")) {
				proxy.saveAll();
			}
			
			//monster builder tab
			else if (tab == 0) {
				if (command.equals("Save Current")) {
					String name = monBuilder.getLastSelected();
					
					//do not allow saving when nothing is selected
					if (!name.equals("select a monster")) {
						//update monster on server before saving
						proxy.updateMonster(monBuilder.getMonster());
						if (proxy.saveMonster(name))
							System.out.println("Save successful.");
							
						else
							System.out.println("Error saving.");
					}
						
					else {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error saving.");
					}
						
					monBuilder.setSelection();
				}
				
				else if (command.equals("Delete")) {
					String name = monBuilder.getLastSelected();
					
					if (name.equals("select a monster")) {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error restoring");
					}
					
					else {
						int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Are you sure you wish to delete "
							+ name + "?", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
						if (yesNo == JOptionPane.YES_OPTION) {
							proxy.deleteMonster(name);
							monBuilder.refresh();
						}
					}
				}
				
				else if (command.equals("Add")) {
					boolean valid = true;
					String message = "Enter the new monster's name:";
					String name;
						
					do {
						name = (String) JOptionPane.showInputDialog(monBuilder, message, "Create New monster.Monster",
							JOptionPane.QUESTION_MESSAGE);
							
						if (name == null)
							break;
							
						else if (name.equals("")) {
							message = "Enter the new monster's name:";
							continue;
						}

						valid = proxy.addMonster(name);
						
						if (valid) {
							monBuilder.refresh();
						}
							
						else {
							System.out.println("File already exists");
							message = "There is already a monster named \"" + name + "\"\nEnter a different name:";
						}
					} while (!valid);
				}
				
				else if (command.equals("Restore")) {
					String name = monBuilder.getLastSelected();
					
					if (name.equals("select a monster")) {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error restoring");
					}
					
					else {
						int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Restore " + name +
						" to last saved state on server?", "Restore", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
						if (yesNo == JOptionPane.YES_OPTION) {
							proxy.restoreMonster(name);
							monBuilder.setSelection();
							monBuilder.refresh();
							monBuilder.setSelection();
						}
					}
				}
			}
			
			else if (tab == 1) {
				if (command.equals("Save Current")) {
					String name = encBuilder.getLastSelected();
					
					//do not allow saving when nothing is selected
					if (!name.equals("select an encounter")) {
						//update monster on server before saving
						proxy.updateEncounter(encBuilder.getEncounter());
						if (proxy.saveEncounter(name))
							System.out.println("Save successful.");
							
						else
							System.out.println("Error saving.");
					}
						
					else {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error saving.");
					}
						
					encBuilder.setSelection();
				}
				
				else if (command.equals("Delete")) {
					String name = encBuilder.getLastSelected();
					
					if (name.equals("select an encounter")) {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error restoring");
					}
					
					else {
						int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Are you sure you wish to delete "
							+ name + "?", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
						if (yesNo == JOptionPane.YES_OPTION) {
							proxy.deleteEncounter(name);
							encBuilder.refresh();
						}
					}
				}
				
				else if (command.equals("Add")) {
					boolean valid = true;
					String message = "Enter the new encounter's name:";
					String name;
						
					do {
						name = (String) JOptionPane.showInputDialog(encBuilder, message, "Create New Encounter",
							JOptionPane.QUESTION_MESSAGE);
							
						if (name == null)
							break;
							
						else if (name.equals("")) {
							message = "Enter the new encounter's name:";
							continue;
						}

						valid = proxy.addEncounter(name);
						
						if (valid) {
							encBuilder.refresh();
						}
							
						else {
							System.out.println("File already exists");
							message = "There is already an encounter named \"" + name + "\"\nEnter a different name:";
						}
					} while (!valid);
				}
				
				else if (command.equals("Restore")) {
					String name = encBuilder.getLastSelected();
					
					if (name.equals("select an encounter")) {
						JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Error restoring");
					}
					
					else {
						int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(), "Restore " + name +
						" to last saved state on server?", "Restore", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
						if (yesNo == JOptionPane.YES_OPTION) {
							proxy.restoreEncounter(name);
							encBuilder.setSelection();
							encBuilder.refresh();
							encBuilder.setSelection();
						}
					}
				}
			}
		}
	}
		
	public static void main(String[] args) {
		DNDClientProxy proxy = new DNDClientProxy(args[0], Integer.parseInt(args[1]));
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				DNDClientApp dndClientApp =	new DNDClientApp(proxy);
			}
		});
	}
}
