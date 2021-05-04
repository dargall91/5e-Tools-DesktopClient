package client;

import encounter.*;
import monster.Monster;
import encounter.MonsterData;
import player.PlayerData;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class EncounterBuilder extends JSplitPane {
	private JList list;
	private JList selectList;
	private DNDClientProxy proxy;
	private ArrayList<String> encList;
	private Encounter encounter;
	private ArrayList<PlayerData> playerData;
	private ArrayList<MonsterData> monsterData;
	private final int HEIGHT = 550;
	private final int LEFT_WIDTH = 150;
	private final int RIGHT_WIDTH = 535;
	private final int INNER_WIDTH = 510;
	private final int INNER_HEIGHT = 20;
	private final Integer[] levels = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
		13, 14, 15, 16, 17, 18, 19, 20 };
	private final Integer[] numPlayers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	private String selected;
	
	EncounterBuilder(DNDClientProxy proxy) {
		encounter = new Encounter();
		this.proxy = proxy;
		initialize();
	}
	
	private void initialize() {
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setContinuousLayout(true);
		setLeftComponent(getLeft());
		refreshRight();
		setEnabled(false);
		setVisible(true);
	}
	
	public void refresh() {
		refreshLeft();
		refreshRight();
		setVisible(true);
	}

	private void refreshLeft() {
		setLeftComponent(getLeft());
	}
	
	public void refreshRight() {
		setRightComponent(getRight());
	}
	
	/**
	 * Populates the right panel with the selected encounters's information
	 */
	private void getEncounter(String name) {
		encounter = proxy.getEncounter(name);
		refreshRight();
	}
	
	private JScrollPane getLeft() {
		JScrollPane scroll = new JScrollPane();
		scroll.setMinimumSize(new Dimension(LEFT_WIDTH, HEIGHT));
		scroll.setPreferredSize(new Dimension(LEFT_WIDTH, HEIGHT));
		list = new JList();
		encList = proxy.getEncounterList();
		
		list.setModel(new AbstractListModel() {
			public int getSize() {
				return encList.size();
			}
			
			public Object getElementAt(int i) {
				return encList.get(i);
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				listValueChanged(event);
			}
		});
		
		scroll.setViewportView(list);
		
		return scroll;
	}
	
	private JScrollPane getRight() {
		//set up pane that houses encounter info
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		JPanel panel = new JPanel();
		scroll.setMinimumSize(new Dimension(RIGHT_WIDTH, HEIGHT));
		scroll.setPreferredSize(new Dimension(RIGHT_WIDTH, HEIGHT));
		scroll.setMaximumSize(new Dimension(RIGHT_WIDTH, HEIGHT));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//name
		panel.add(getNameLabel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		if (encounter.getName().equals("select an encounter")) {
			scroll.setViewportView(panel);
			return scroll;
		}
		
		//payerlabel
		panel.add(getPartyLabel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		//player data
		panel.add(getPartyDataPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		//music theme
		panel.add(getThemePanel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));	
		
		//monster label
		panel.add(getMonsterLabel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));	
		
		//monster data
		panel.add(getMonsterDataPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		scroll.setViewportView(panel);
		
		return scroll;
	}
	
	private JLabel getNameLabel() {
		JLabel name = new JLabel(encounter.getName());
		name.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return name;
	}
	
	private JLabel getPartyLabel() {
		JLabel name = new JLabel("Party Data");
		name.setFont(new Font(name.getFont().getName(), Font.BOLD, 15));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return name;
	}
	
	private JPanel getPartyDataPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panel.add(getPlayerPanel());
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(getXPPanel());
		
		return panel;
	}
	
	private JPanel getPlayerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(INNER_WIDTH / 2 + 50, 150));
		
		JPanel labels = new JPanel();
		JLabel players = new JLabel("Players:");
		JLabel level = new JLabel("Level:");
		labels.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
		labels.add(players);
		labels.add(Box.createRigidArea(new Dimension(10, 0)));
		labels.add(level);
		panel.add(labels);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		playerData = encounter.getPlayerData();
		
		for (int i = 0; i < playerData.size(); i++) {
			final int index = i;
			JPanel levelPanel = new JPanel();
			levelPanel.setMaximumSize(new Dimension(INNER_WIDTH / 2 + 20, INNER_HEIGHT));
			levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.X_AXIS));
			levelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			JComboBox playerList = new JComboBox(numPlayers);
			playerList.setMaximumSize(new Dimension(45, 20));
			playerList.setAlignmentX(Component.LEFT_ALIGNMENT);
			playerList.setSelectedIndex(playerData.get(i).getPlayers() - 1);
			
			playerList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					encounter.updateNumPlayers(index, (Integer) playerList.getSelectedItem());
					refreshRight();
				}
			});
			
			JComboBox levelList = new JComboBox(levels);
			levelList.setMaximumSize(new Dimension(45, 20));
			levelList.setAlignmentX(Component.LEFT_ALIGNMENT);
			levelList.setSelectedIndex(playerData.get(i).getLevel() - 1);
			
			levelList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					encounter.updatePlayerLevel(index, (Integer) levelList.getSelectedItem());
					refreshRight();
				}
			});
			
			JButton deleteLevel = new JButton("Delete");
			deleteLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
			deleteLevel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					encounter.deletePlayerData(index);
					refreshRight();
				}
			});
			
			levelPanel.add(playerList);
			levelPanel.add(Box.createRigidArea(new Dimension(20, 0)));
			levelPanel.add(levelList);
			levelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			levelPanel.add(deleteLevel);
			panel.add(levelPanel);
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
		}
		
		JButton addPlayerLevel = new JButton("Add Player Level");
		addPlayerLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
		addPlayerLevel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				encounter.addPlayerData();
				refreshRight();
			}
		});
		
		panel.add(addPlayerLevel);
		
		return panel;
	}
	
	private JPanel getXPPanel() {
		//update monster xp values, which could have changed if monster was edited
		monsterData = encounter.getMonsterData();
		
		for (int i = 0; i < monsterData.size(); i++) {
			Monster monster = proxy.getMonster(monsterData.get(i).getMonster());
			encounter.setMonsterXP(i, monster.getXP());
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setMaximumSize(new Dimension(INNER_WIDTH / 2 - 50, 150));
		JLabel easy = new JLabel("Easy: " + encounter.getEasyThreshold());
		easy.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JLabel medium = new JLabel("Medium: " + encounter.getMediumThreshold());
		medium.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JLabel hard = new JLabel("Hard: " + encounter.getHardThreshold());
		hard.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JLabel deadly = new JLabel("Deadly: " + encounter.getDeadlyThreshold());
		deadly.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JLabel budget = new JLabel("Daily Budget: " + encounter.getDailyBudget());
		budget.setAlignmentX(Component.RIGHT_ALIGNMENT);
		budget.setFont(new Font(budget.getFont().getName(), Font.PLAIN, budget.getFont().getSize()));
		JLabel total = new JLabel("Encounter Total: " + encounter.getXPTotal());
		total.setAlignmentX(Component.RIGHT_ALIGNMENT);
		total.setFont(new Font(total.getFont().getName(), Font.BOLD, total.getFont().getSize()));
		
		String difficulty = encounter.getDifficulty();
		
		switch (difficulty) {
			case "Trivial":
				easy.setFont(new Font(easy.getFont().getName(), Font.PLAIN, easy.getFont().getSize()));
				medium.setFont(new Font(medium.getFont().getName(), Font.PLAIN, medium.getFont().getSize()));
				hard.setFont(new Font(hard.getFont().getName(), Font.PLAIN, hard.getFont().getSize()));
				deadly.setFont(new Font(deadly.getFont().getName(), Font.PLAIN, deadly.getFont().getSize()));
				break;
			case "Easy":
				easy.setFont(new Font(easy.getFont().getName(), Font.BOLD, easy.getFont().getSize()));
				medium.setFont(new Font(medium.getFont().getName(), Font.PLAIN, medium.getFont().getSize()));
				hard.setFont(new Font(hard.getFont().getName(), Font.PLAIN, hard.getFont().getSize()));
				deadly.setFont(new Font(deadly.getFont().getName(), Font.PLAIN, deadly.getFont().getSize()));
				break;
			case "Medium":
				easy.setFont(new Font(easy.getFont().getName(), Font.PLAIN, easy.getFont().getSize()));
				medium.setFont(new Font(medium.getFont().getName(), Font.BOLD, medium.getFont().getSize()));
				hard.setFont(new Font(hard.getFont().getName(), Font.PLAIN, hard.getFont().getSize()));
				deadly.setFont(new Font(deadly.getFont().getName(), Font.PLAIN, deadly.getFont().getSize()));
				break;
			case "Hard":
				easy.setFont(new Font(easy.getFont().getName(), Font.PLAIN, easy.getFont().getSize()));
				medium.setFont(new Font(medium.getFont().getName(), Font.PLAIN, medium.getFont().getSize()));
				hard.setFont(new Font(hard.getFont().getName(), Font.BOLD, hard.getFont().getSize()));
				deadly.setFont(new Font(deadly.getFont().getName(), Font.PLAIN, deadly.getFont().getSize()));
				break;
			case "Deadly":
				easy.setFont(new Font(easy.getFont().getName(), Font.PLAIN, easy.getFont().getSize()));
				medium.setFont(new Font(medium.getFont().getName(), Font.PLAIN, medium.getFont().getSize()));
				hard.setFont(new Font(hard.getFont().getName(), Font.PLAIN, hard.getFont().getSize()));
				deadly.setFont(new Font(deadly.getFont().getName(), Font.BOLD, deadly.getFont().getSize()));
				break;
		}
		
		panel.add(easy);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(medium);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(hard);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(deadly);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(budget);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(total);
		
		return panel;
	}
	
	private JPanel getThemePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));

		ArrayList<String> musicList = proxy.getMusicList();
		JLabel themeLabel = new JLabel("Battle Theme:");
		JComboBox theme = new JComboBox(musicList.toArray());
		theme.setSelectedItem(encounter.getTheme());
		theme.setMinimumSize(new Dimension(300, 20));
		theme.setMaximumSize(new Dimension(300, 20));
		theme.setPreferredSize(new Dimension(300, 20));

		theme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				encounter.setTheme((String) theme.getSelectedItem());
			}
		});
		
		panel.add(themeLabel);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(theme);
		
		return panel;
	}
	
	private JLabel getMonsterLabel() {
		JLabel name = new JLabel("Monsters");
		name.setFont(new Font(name.getFont().getName(), Font.BOLD, 15));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return name;
	}
	
	private JPanel getMonsterDataPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(INNER_WIDTH, HEIGHT));
		
		JPanel labels = new JPanel();
		JLabel monsters = new JLabel("monster.Monster:");
		JLabel quantity = new JLabel("Quantity:");
		JLabel initiative = new JLabel("Initiative:");
		JLabel minions = new JLabel("Minion:");
		JLabel reinforcements = new JLabel("Reinforcments:");
		labels.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
		labels.add(monsters);
		labels.add(Box.createRigidArea(new Dimension(60, 0)));
		labels.add(quantity);
		labels.add(Box.createRigidArea(new Dimension(65, 0)));
		labels.add(initiative);
		labels.add(Box.createRigidArea(new Dimension(10, 0)));
		labels.add(minions);
		labels.add(Box.createRigidArea(new Dimension(10, 0)));
		labels.add(reinforcements);
		panel.add(labels);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		monsterData = encounter.getMonsterData();
		
		for (int i = 0; i < monsterData.size(); i++) {
			final int index = i;
			
			JPanel monsterPanel = new JPanel();
			monsterPanel.setLayout(new BoxLayout(monsterPanel, BoxLayout.X_AXIS));
			monsterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			JPanel namePanel = new JPanel();
			namePanel.setMinimumSize(new Dimension(120, 20));
			namePanel.setMaximumSize(new Dimension(120, 20));
			namePanel.setPreferredSize(new Dimension(120, 20));
			namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
			namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel name = new JLabel(monsterData.get(i).getMonster());
			name.setAlignmentX(Component.LEFT_ALIGNMENT);
			namePanel.add(name);
			
			JTextField number = new JTextField(5);
			number.setMinimumSize(new Dimension(25, 20));
			number.setMaximumSize(new Dimension(25, 20));
			number.setPreferredSize(new Dimension(25, 20));
			number.setText(Integer.toString(monsterData.get(i).getQuantity()));
			number.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
						|| e.getKeyCode() == KeyEvent.VK_DELETE) && !number.getText().equals("1")))
						number.setEditable(true);
						
					else
						number.setEditable(false);
				}
				
				@Override
				public void keyTyped(KeyEvent e) {
					int num = Integer.parseInt(number.getText());
					encounter.updateMonsterQuantity(index, num);
				}
			});
			
			/*client.DeferredDocumentListener listener = new client.DeferredDocumentListener (new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int num = Integer.parseInt(number.getText());
					
					if (num == 0)
						encounter.deleteMonsterData(index);
						
					else
						encounter.updateMonsterQuantity(index, num);
						
					refreshRight();
				}
			});
			
			number.getDocument().addDocumentListener(listener);
			number.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					listener.start();
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					listener.stop();
				}
			});*/
			
			// + & - button panel	
			JButton add = new JButton("+");
			add.setMaximumSize(new Dimension(45, 20));
			add.setPreferredSize(new Dimension(45, 20));
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int num = Integer.parseInt(number.getText());
					Integer value = new Integer(10);
					number.setText(value.toString(++num));
					encounter.updateMonsterQuantity(index, num);
					refreshRight();
				}
			});
			
			JButton subtract = new JButton("-");
			subtract.setMaximumSize(new Dimension(45, 20));
			subtract.setPreferredSize(new Dimension(45, 20));
			subtract.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int num = Integer.parseInt(number.getText());
					
					if (num == 1)
						encounter.deleteMonsterData(index);
					
					else {
						Integer value = new Integer(10);
						number.setText(Integer.toString(--num));
						encounter.updateMonsterQuantity(index, num);
					}
					
					refreshRight();
				}
			});
			
			
			JComboBox initBox = new JComboBox(levels); //using levels because this was added later, and options are the same
			initBox.setSelectedIndex(monsterData.get(i).getInitiative() - 1);
			initBox.setMinimumSize(new Dimension(50, INNER_HEIGHT));
			initBox.setMaximumSize(new Dimension(50, INNER_HEIGHT));
			initBox.setPreferredSize(new Dimension(50, INNER_HEIGHT));
			initBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					encounter.setInitiative(index, (Integer) initBox.getSelectedItem());
				}
			});
			
			//minion checkbox
			JCheckBox minionBox = new JCheckBox();
			minionBox.setSelected(monsterData.get(index).isMinion());
			minionBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 1)
						encounter.setMinion(index, true);
						
					else
						encounter.setMinion(index, false);
						
					refreshRight();
				}
			});
			
			//reinforcement checkbox
			JCheckBox reinBox = new JCheckBox();
			reinBox.setSelected(monsterData.get(index).isReinforcement());
			reinBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 1)
						encounter.setReinforcement(index, true);
						
					else
						encounter.setReinforcement(index, false);
				}
			});
			
			monsterPanel.add(namePanel);
			monsterPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			monsterPanel.add(number);
			monsterPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			monsterPanel.add(add);
			monsterPanel.add(subtract);
			monsterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			monsterPanel.add(initBox);
			monsterPanel.add(Box.createRigidArea(new Dimension(40, 0)));
			monsterPanel.add(minionBox);
			monsterPanel.add(Box.createRigidArea(new Dimension(40, 0)));
			monsterPanel.add(reinBox);

			panel.add(monsterPanel);
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
		}
		
		JButton addMonster = new JButton("Add monster.Monster");
		addMonster.setAlignmentX(Component.LEFT_ALIGNMENT);
		addMonster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel optionPanel = new JPanel();
				optionPanel.setMinimumSize(new Dimension(100, 500));
				optionPanel.setMaximumSize(new Dimension(100, 500));
				optionPanel.setPreferredSize(new Dimension(100, 500));
				optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
				
				//contains search field and label
				JPanel searchPanel = new JPanel();
				searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
				JLabel searchLabel = new JLabel("Search:");
				JTextField searchField = new JTextField();
				searchField.setMinimumSize(new Dimension(100, 20));
				searchField.setMaximumSize(new Dimension(100, 20));
				searchField.setPreferredSize(new Dimension(100, 20));
				searchPanel.add(searchLabel);
				searchPanel.add(searchField);
				
				optionPanel.add(searchPanel);
				optionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
				
				//contains list of monsters
				JScrollPane scroll = new JScrollPane();
				scroll.setPreferredSize(new Dimension(200, 200));
				
				selectList = new JList();
				ArrayList<String> monList = proxy.getMonsterList();

				selectList.setModel(new AbstractListModel() {
					public int getSize() {
						return monList.size();
					}
					
					public Object getElementAt(int i) {
						return monList.get(i);
					}
				});
				
				selectList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						monListValueChanged(event);
					}
				});
				
				scroll.setViewportView(selectList);
				optionPanel.add(scroll);
				
				DeferredDocumentListener listener = new DeferredDocumentListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (searchField.getText().equals("")) {
							//display all
						}
						
						else {
							//display only those that contain the string
						}
					}
				});
				
				searchField.getDocument().addDocumentListener(listener);
				
				searchField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						listener.start();
					}
					
					@Override
					public void focusLost(FocusEvent e) {
						listener.stop();
					}
				});
				
				int result = JOptionPane.showConfirmDialog(null, optionPanel, "Add monster.Monster",
					JOptionPane.OK_CANCEL_OPTION);
					
				if (result == JOptionPane.OK_OPTION) {
					Monster monster = proxy.getMonster(selected);
					encounter.addMonsterData(selected, monster.getXP());
					refreshRight();
				}
			}
		});
		
		panel.add(addMonster);
		
		return panel;
	}
	
	/**
	 * Defines what will happen when an item in the JList is selected
	 */
	private void listValueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;
			
		//update current encounter on server before getting new monster info
		if (!list.isSelectionEmpty())
			proxy.updateEncounter(encounter);
		
		getEncounter(getLastSelected());
	}
	
	//Stringify last selected value of JList
	public String getLastSelected() {
		if (list.isSelectionEmpty())
			return "select an encounter";
			
		return list.getSelectedValue().toString();
	}
	
	//gets currently selected encounter
	public Encounter getEncounter() {
		return encounter;
	}
	
	/**
	 * Sets the currently selected value in the JList. After saving or updating
	 * some things (such as adding an ability, action, etc, the current
	 * selection seems to be lost, preventing back to back saving without
	 * re-selecting the monster
	 */
	 public void setSelection() {
		 list.setSelectedValue(encounter.getName(), true);
		 //updates current encounter info, needed for restoring
		 encounter = proxy.getEncounter(encounter.getName());
	 }
	 
	 private void monListValueChanged(ListSelectionEvent event) {
		if (!selectList.isSelectionEmpty())
			selected = selectList.getSelectedValue().toString();
	}
}
