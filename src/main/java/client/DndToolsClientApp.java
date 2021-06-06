package client;

import monster.Monster;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class DndToolsClientApp extends JFrame {
    private final JTabbedPane tabPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu builderMenu;
    private JMenuItem menuItem;
    private MenuActionListener menuListener;
    private final MonsterBuilder monBuilder;
    private final EncounterBuilder encBuilder;
    private final DndToolsClientProxy proxy;

    DndToolsClientApp(DndToolsClientProxy proxy) {
        this.proxy = proxy;
        setTitle("D&D Builder Tool");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int yesNo = (int) JOptionPane.showConfirmDialog(tabPane.getParent(),
                        "Are you sure you wish to exit?", "Exit Builder",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (yesNo == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
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
     * Sets the menu bar for the frame.
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
        menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(menuListener);
        builderMenu.add(menuItem);
        menuItem = new JMenuItem("Restore");
        menuItem.addActionListener(menuListener);
        builderMenu.add(menuItem);
        setJMenuBar(menuBar);
    }

    /**
     * Action listener for JMenu.
     */
    private class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //get which is the current tab, 0 == monster.Monster Builder, 1 == Encounter Builder
            int tab = tabPane.getSelectedIndex();
            System.out.println("Selected \"" + command + "\" in tab " + tabPane.getSelectedIndex());

            if (command.equals("Exit")) {
                int yesNo = JOptionPane.showConfirmDialog(tabPane.getParent(),
                        "Are you sure you wish to exit?", "Exit Builder",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (yesNo == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else if (command.equals("Save All")) {
                proxy.saveAll();
            } else if (tab == 0) {
                if (command.equals("Save Current")) {
                    String name = monBuilder.getLastSelected();

                    //do not allow saving when nothing is selected
                    if (!name.equals("select a monster")) {
                        //update monster on server before saving
                        proxy.updateMonster(monBuilder.getMonster());
                        if (proxy.saveMonster(name)) {
                            System.out.println("Save successful.");
                        } else {
                            System.out.println("Error saving.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error saving.");
                    }

                    monBuilder.setSelection();
                } else if (command.equals("Delete")) {
                    String name = monBuilder.getLastSelected();

                    if (name.equals("select a monster")) {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error deleting");
                    } else {
                        int yesNo = JOptionPane.showConfirmDialog(tabPane.getParent(),
                                "Are you sure you wish to delete " + name + "?", "Delete",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (yesNo == JOptionPane.YES_OPTION) {
                            proxy.deleteMonster(name);
                            monBuilder.refresh();
                        }
                    }
                } else if (command.equals("Add")) {
                    boolean valid = true;
                    String message = "Enter the new monster's name:";
                    String name;

                    do {
                        name = JOptionPane.showInputDialog(monBuilder, message,
                                "Create New Monster", JOptionPane.QUESTION_MESSAGE);

                        if (name == null) {
                            break;
                        } else if (name.equals("")) {
                            message = "Enter the new monster's name:";
                            continue;
                        }

                        valid = proxy.addMonster(name);

                        if (valid) {
                            monBuilder.refresh();
                        } else {
                            System.out.println("File already exists");
                            message = "There is already a monster named \"" + name
                                    + "\"\nEnter a different name:";
                        }
                    } while (!valid);
                } else if (command.equals("Copy")) {
                    String base = monBuilder.getLastSelected();

                    if (base.equals("select a monster")) {
                        JOptionPane.showMessageDialog(monBuilder, "No Monster Selected",
                                "Copy Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Copy Error: No monster selected");
                        return;
                    }

                    boolean valid = true;
                    String message = "Enter the copy's name:";
                    String name;

                    do {
                        name = JOptionPane.showInputDialog(monBuilder, message,
                                "Copy " + base, JOptionPane.QUESTION_MESSAGE);

                        if (name == null) {
                            break;
                        } else if (name.equals("")) {
                            message = "Enter the copy's name:";
                            continue;
                        }

                        //TODO: create a copyMonster method on server to make this process more efficient
                        valid = proxy.addMonster(name);

                        if (valid) {
                            Monster copy = proxy.getMonster(base);
                            proxy.updateMonster(copy);
                            copy.setName(name);
                            copy.setDisplayName(name);
                            proxy.updateMonster(copy);
                            monBuilder.setMonster(name);
                            //monBuilder.refresh();
                            //monBuilder.setSelection();
                            //monBuilder.setMonster(name);
                        } else {
                            System.out.println("File already exists");
                            message = "There is already a monster named \"" + name
                                    + "\"\nEnter a different name:";
                        }
                    } while (!valid);
                } else if (command.equals("Restore")) {
                    String name = monBuilder.getLastSelected();

                    if (name.equals("select a monster")) {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No monster selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error restoring");
                    } else {
                        int yesNo = JOptionPane.showConfirmDialog(tabPane.getParent(), "Restore "
                                + name + " to last saved state on server?", "Restore",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (yesNo == JOptionPane.YES_OPTION) {
                            proxy.restoreMonster(name);
                            monBuilder.setSelection();
                            monBuilder.refresh();
                            monBuilder.setSelection();
                        }
                    }
                }
            } else if (tab == 1) {
                if (command.equals("Save Current")) {
                    String name = encBuilder.getLastSelected();

                    //do not allow saving when nothing is selected
                    if (!name.equals("select an encounter")) {
                        //update monster on server before saving
                        proxy.updateEncounter(encBuilder.getEncounter());
                        if (proxy.saveEncounter(name)) {
                            System.out.println("Save successful.");
                        } else {
                            System.out.println("Error saving.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error saving.");
                    }

                    encBuilder.setSelection();
                } else if (command.equals("Delete")) {
                    String name = encBuilder.getLastSelected();

                    if (name.equals("select an encounter")) {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error restoring");
                    } else {
                        int yesNo = JOptionPane.showConfirmDialog(tabPane.getParent(),
                                "Are you sure you wish to delete " + name + "?", "Delete",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (yesNo == JOptionPane.YES_OPTION) {
                            proxy.deleteEncounter(name);
                            encBuilder.refresh();
                        }
                    }
                } else if (command.equals("Add")) {
                    boolean valid = true;
                    String message = "Enter the new encounter's name:";
                    String name;

                    do {
                        name = JOptionPane.showInputDialog(encBuilder, message,
                                "Create New Encounter", JOptionPane.QUESTION_MESSAGE);

                        if (name == null) {
                            break;
                        } else if (name.equals("")) {
                            message = "Enter the new encounter's name:";
                            continue;
                        }

                        valid = proxy.addEncounter(name);

                        if (valid) {
                            encBuilder.refresh();
                        } else {
                            System.out.println("File already exists");
                            message = "There is already an encounter named \"" + name
                                    + "\"\nEnter a different name:";
                        }
                    } while (!valid);
                } else if (command.equals("Restore")) {
                    String name = encBuilder.getLastSelected();

                    if (name.equals("select an encounter")) {
                        JOptionPane.showMessageDialog(tabPane.getParent(), "No encounter selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error restoring");
                    } else {
                        int yesNo = JOptionPane.showConfirmDialog(tabPane.getParent(), "Restore "
                                + name + " to last saved state on server?", "Restore",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

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

    /**
     * Entry point into the DNDClientApp.
     *
     * @param args The Server host (arg[0]) and Port (arg[1])
     */
    public static void main(String[] args) {
        DndToolsClientProxy proxy = new DndToolsClientProxy(args[0], Integer.parseInt(args[1]));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DndToolsClientApp(proxy);
            }
        });
    }
}
