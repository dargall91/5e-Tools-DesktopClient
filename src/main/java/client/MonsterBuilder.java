package client;

import monster.*;
import monster.Action;
//import java.lang.Math;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.*;

public class MonsterBuilder extends JSplitPane {
	private JList list;
	private DNDClientProxy proxy;
	private ArrayList<String> monList;
	private Monster monster;
	private ArrayList<Ability> abilityList;
	private ArrayList<Action> actionList;
	private ArrayList<LegendaryAction> legendaryList;
	private final int HEIGHT = 1000;
	private final int LEFT_WIDTH = 300;
	private final int RIGHT_WIDTH = 1075;
	private final int INNER_WIDTH = 1050;
	private final int INNER_HEIGHT = 20;
	private final Dimension scorePanelSize = new Dimension(175, 220);
	private final Dimension VERTICAL_GAP = new Dimension(0, 5);
	private final Dimension HORIZONTAL_GAP = new Dimension(5, 0);
	private final Integer[] scores = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
	private final String[] crs = { "-1", "0", "1/8", "1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
			"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" };
	private final String STR = "STR";
	private final String ATH = "Athletics";
	private final String DEX = "DEX";
	private final String ACRO = "Acrobatics";
	private final String HAND = "Sleight of Hand";
	private final String STE = "Stealth";
	private final String CON = "CON";
	private final String INT = "INT";
	private final String ARC = "Arcana";
	private final String HIS = "History";
	private final String INV = "Investigation";
	private final String NAT = "Nature";
	private final String REL = "Religion";
	private final String WIS = "WIS";
	private final String ANI = "Animal Handling";
	private final String INS = "Insight";
	private final String MED = "Medicine";
	private final String PER = "Perception";
	private final String SUR = "Survival";
	private final String CHA = "CHA";
	private final String DEC = "Deception";
	private final String INTIM = "Intimidation";
	private final String PERF = "Performance";
	private final String PERS = "Persuasion";
	private JLabel strSave, athletics, dexSave, acrobatics, hand, stealth, conSave, intSave, arcana, history,
			investigation, nature, religion, wisSave, animal, insight, medicine, perception, survival, chaSave,
			deception, intimidation, performance, persuasion;
	private JCheckBox strSaveBox, dexSaveBox, conSaveBox, intSaveBox, wisSaveBox, chaSaveBox;


	/**
	 * Builds the MonsterBuilder tab
	 */
	MonsterBuilder(DNDClientProxy proxy) {
		monster = new Monster();
		this.proxy = proxy;
		initialize();
	}
	
	/**
	 * Initializes the initial layout of the MonsterBuilder tab
	 */
	private void initialize() {
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setContinuousLayout(true);
		setLeftComponent(getLeft());
		refreshRight();
		setEnabled(false);
		setVisible(true);
	}
	
	/**
	 * Refreshes the monster list
	 */
	public void refresh() {
		refreshLeft();
		refreshRight();
		setVisible(true);
	}
	
	private void refreshLeft() {
		setLeftComponent(getLeft());
	}
	
	private void refreshRight() {
		setRightComponent(getRight());
	}
	
	/**
	 * Populates the right panel with the selected monster's information
	 */
	private void getMonster(String name) {
		monster = proxy.getMonster(name);
		refreshRight();
	}
	
	/**
	 * Sets up and returns the JScrollPane used in the left side of the JSplitPane
	 */
	private JScrollPane getLeft() {
		JScrollPane scroll = new JScrollPane();
		scroll.setMinimumSize(new Dimension(LEFT_WIDTH, HEIGHT));
		scroll.setPreferredSize(new Dimension(LEFT_WIDTH, HEIGHT));
		list = new JList();
		monList = proxy.getMonsterList();

		Collections.sort(monList);
		
		list.setModel(new AbstractListModel() {
			public int getSize() {
				return monList.size();
			}
			
			public Object getElementAt(int i) {
				return monList.get(i);
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				listValueChanged(event);
			}
		});

		if (monList.size() > 0) {
			list.setSelectedIndex(0);
		}

		scroll.setViewportView(list);
		
		return scroll;
	}
	
	/**
	 * Sets up and returns the JPanel used in the right side of the JSplitPane
	 */
	private JScrollPane getRight() {
		//set up pane that houses monster info
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
		panel.add(Box.createRigidArea(VERTICAL_GAP));

		//TODO: add display name panel
		
		if (monster.getName().equals("select a monster")) {
			scroll.setViewportView(panel);
			return scroll;
		}
		
		//size
		panel.add(getSizePanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//type
		panel.add(getTypePanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//alignment
		panel.add(getAlignmentPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//ac
		panel.add(getACPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//hp
		panel.add(getHPPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//speed
		panel.add(getSpeedPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//panel for ability scores
		panel.add(getScorePanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//senses
		panel.add(getSensesPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//languages
		panel.add(getLanguagePanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//Challenge/XP
		panel.add(getChallengePanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//Abilities
		panel.add(getAbilityLabel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		abilityList = monster.getAbilities();
		
		for (int i = 0; i < abilityList.size(); i++) {
			panel.add(getAbilityPanel(i, scroll));
			panel.add(Box.createRigidArea(VERTICAL_GAP));
		}
		
		JButton addAbility = new JButton("Add Ability");
		addAbility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.addAbility(new Ability());
				revalidate();
				repaint();
			}
		});
		
		panel.add(addAbility);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//Actions
		panel.add(getActionLabel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		actionList = monster.getActions();
		
		for (int i = 0; i < actionList.size(); i++) {
			panel.add(getActionPanel(i, scroll));
			panel.add(Box.createRigidArea(VERTICAL_GAP));
		}
		
		JButton addAction = new JButton("Add Action");
		addAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.addAction(new Action());
				refreshRight();
			}
		});
		
		panel.add(addAction);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		//Legendary Actions
		panel.add(getLegendaryLabel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(getCountPanel());
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		legendaryList = monster.getLegendaryActions();
		
		for (int i = 0; i < legendaryList.size(); i++) {
			panel.add(getLegendaryPanel(i, scroll));
			panel.add(Box.createRigidArea(VERTICAL_GAP));
		}
		
		JButton addLegendary = new JButton("Add Legendary Action");
		addLegendary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.addLegendaryAction(new LegendaryAction());
				refreshRight();
			}
		});
		
		panel.add(addLegendary);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		
		scroll.setViewportView(panel);
		
		return scroll;
	}
	
	private JLabel getNameLabel() {
		JLabel name = new JLabel(monster.getName());
		name.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return name;
	}
	
	//size
	private JPanel getSizePanel() {
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
		sizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sizePanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel sizeLabel = new JLabel("Size");
		sizeLabel.setFont(new Font(sizeLabel.getFont().getName(), Font.BOLD, sizeLabel.getFont().getSize()));
		JTextField size = new JTextField(monster.getSize());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setSize(size.getText());
			}
		});
		
		size.getDocument().addDocumentListener(listener);
		size.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		sizePanel.add(sizeLabel);
		sizePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		sizePanel.add(size);
		
		return sizePanel;
	}
	
	//type
	private JPanel getTypePanel() {
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
		typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		typePanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel typeLabel = new JLabel("Type");
		typeLabel.setFont(new Font(typeLabel.getFont().getName(), Font.BOLD, typeLabel.getFont().getSize()));
		JTextField type = new JTextField(monster.getType());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setType(type.getText());
			}
		});
		
		type.getDocument().addDocumentListener(listener);
		type.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		typePanel.add(typeLabel);
		typePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		typePanel.add(type);
		
		return typePanel;
	}
	
	//alignment
	private JPanel getAlignmentPanel() {
		JPanel alignPanel = new JPanel();
		alignPanel.setLayout(new BoxLayout(alignPanel, BoxLayout.X_AXIS));
		alignPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		alignPanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel alignLabel = new JLabel("Alignment");
		alignLabel.setFont(new Font(alignLabel.getFont().getName(), Font.BOLD, alignLabel.getFont().getSize()));
		JTextField alignment = new JTextField(monster.getAlignment());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAlignment(alignment.getText());
			}
		});
		
		alignment.getDocument().addDocumentListener(listener);
		alignment.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		alignPanel.add(alignLabel);
		alignPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		alignPanel.add(alignment);
		
		return alignPanel;
	}
	
	//ac
	private JPanel getACPanel() {
		JPanel acPanel = new JPanel();
		acPanel.setLayout(new BoxLayout(acPanel, BoxLayout.X_AXIS));
		acPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		acPanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel acLabel = new JLabel("Armor Class");
		acLabel.setFont(new Font(acLabel.getFont().getName(), Font.BOLD, acLabel.getFont().getSize()));
		JTextField ac = new JTextField(monster.getAC());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAC(ac.getText());
			}
		});
		
		ac.getDocument().addDocumentListener(listener);
		ac.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		acPanel.add(acLabel);
		acPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		acPanel.add(ac);
		
		return acPanel;
	}
	
	//hp
	private JPanel getHPPanel() {
		JPanel hpPanel = new JPanel();
		hpPanel.setLayout(new BoxLayout(hpPanel, BoxLayout.X_AXIS));
		hpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		hpPanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel hpLabel = new JLabel("Hit Points");
		hpLabel.setFont(new Font(hpLabel.getFont().getName(), Font.BOLD, hpLabel.getFont().getSize()));
		JTextField hp = new JTextField(monster.getHP());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setHP(hp.getText());
			}
		});
		
		hp.getDocument().addDocumentListener(listener);
		hp.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		hpPanel.add(hpLabel);
		hpPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		hpPanel.add(hp);
		
		return hpPanel;
	}
	
	//speed
	private JPanel getSpeedPanel() {
		JPanel speedPanel = new JPanel();
		speedPanel.setLayout(new BoxLayout(speedPanel, BoxLayout.X_AXIS));
		speedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		speedPanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel speedLabel = new JLabel("Speed");
		speedLabel.setFont(new Font(speedLabel.getFont().getName(), Font.BOLD, speedLabel.getFont().getSize()));
		JTextField speed = new JTextField(monster.getSpeed());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setSpeed(speed.getText());
			}
		});
		
		speed.getDocument().addDocumentListener(listener);
		speed.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		speedPanel.add(speedLabel);
		speedPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		speedPanel.add(speed);
		
		return speedPanel;
	}
	
	private JPanel getScorePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setMinimumSize(new Dimension(INNER_WIDTH, 210));
		scorePanel.setMaximumSize(new Dimension(INNER_WIDTH, 210));
		scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
		
		scorePanel.add(getStr());
		scorePanel.add(getDex());
		scorePanel.add(getCon());
		scorePanel.add(getInt());
		scorePanel.add(getWis());
		scorePanel.add(getCha());
		panel.add(scorePanel);
		
		return panel;
	}
	
	/**
	 * STR information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getStr() {
		JPanel strPanel = new JPanel();
		strPanel.setMinimumSize(scorePanelSize);
		strPanel.setPreferredSize(scorePanelSize);
		strPanel.setMaximumSize(scorePanelSize);
		strPanel.setLayout(new BoxLayout(strPanel, BoxLayout.Y_AXIS));
		strPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		strPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel str = new JLabel(STR);
		str.setAlignmentX(Component.CENTER_ALIGNMENT);
		str.setFont(new Font(str.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		strSaveBox = new JCheckBox();
		strSaveBox.setSelected(monster.getAbilityProficiency(STR));
		strSave = new JLabel();
		strSave.setFont(new Font(strSave.getFont().getName(), Font.PLAIN, strSave.getFont().getSize()));

		JPanel athPanel = new JPanel();
		athPanel.setLayout(new BoxLayout(athPanel, BoxLayout.X_AXIS));
		athletics = new JLabel();
		athletics.setFont(new Font(athletics.getFont().getName(), Font.PLAIN, athletics.getFont().getSize()));
		JCheckBox athProfBox = new JCheckBox();
		JCheckBox athExpBox = new JCheckBox();
		athProfBox.setSelected(monster.getSkillProficient(ATH));
		athExpBox.setSelected(monster.getSkillExpertise(ATH));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(STR, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, STR);
				updateSavingThrow(strSave, strSaveBox, STR);
				updateSkillText(athletics, STR, ATH);
			}
		});

		strSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(strSave, strSaveBox, STR);
			}
		});

		athProfBox.addItemListener(new ProficiencyListener(athProfBox, athExpBox, athletics, STR, ATH));
		athExpBox.addItemListener(new ExpertiseListener(athProfBox, athExpBox, athletics, STR, ATH));

		score.setSelectedItem(monster.getAbilityScore(STR));
		
		strPanel.add(str);
		strPanel.add(Box.createRigidArea(VERTICAL_GAP));
		strPanel.add(score);
		strPanel.add(Box.createRigidArea(VERTICAL_GAP));
		strPanel.add(mod);
		
		savePanel.add(strSaveBox);
		savePanel.add(strSave);
		strPanel.add(savePanel);
		
		athPanel.add(athExpBox);
		athPanel.add(athProfBox);
		athPanel.add(athletics);
		strPanel.add(athPanel);
		
		return strPanel;
	}
	
	/**
	 * DEX information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getDex() {
		JPanel dexPanel = new JPanel();
		dexPanel.setMinimumSize(scorePanelSize);
		dexPanel.setPreferredSize(scorePanelSize);
		dexPanel.setMaximumSize(scorePanelSize);
		dexPanel.setLayout(new BoxLayout(dexPanel, BoxLayout.Y_AXIS));
		dexPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		dexPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel dex = new JLabel(DEX);
		dex.setAlignmentX(Component.CENTER_ALIGNMENT);
		dex.setFont(new Font(dex.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		dexSaveBox = new JCheckBox();
		dexSaveBox.setSelected(monster.getAbilityProficiency(DEX));
		dexSave = new JLabel();
		dexSave.setFont(new Font(dexSave.getFont().getName(), Font.PLAIN, dexSave.getFont().getSize()));

		JPanel acroPanel = new JPanel();
		acroPanel.setLayout(new BoxLayout(acroPanel, BoxLayout.X_AXIS));
		acrobatics = new JLabel();
		acrobatics.setFont(new Font(acrobatics.getFont().getName(), Font.PLAIN, acrobatics.getFont().getSize()));
		JCheckBox acroProfBox = new JCheckBox();
		JCheckBox acroExpBox = new JCheckBox();
		acroProfBox.setSelected(monster.getSkillProficient(ACRO));
		acroExpBox.setSelected(monster.getSkillExpertise(ACRO));

		JPanel handPanel = new JPanel();
		handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
		hand = new JLabel();
		hand.setFont(new Font(hand.getFont().getName(), Font.PLAIN, hand.getFont().getSize()));
		JCheckBox handProfBox = new JCheckBox();
		JCheckBox handExpBox = new JCheckBox();
		handProfBox.setSelected(monster.getSkillProficient(HAND));
		handExpBox.setSelected(monster.getSkillExpertise(HAND));

		JPanel stePanel = new JPanel();
		stePanel.setLayout(new BoxLayout(stePanel, BoxLayout.X_AXIS));
		stealth = new JLabel();
		stealth.setFont(new Font(stealth.getFont().getName(), Font.PLAIN, stealth.getFont().getSize()));
		JCheckBox steProfBox = new JCheckBox();
		JCheckBox steExpBox = new JCheckBox();
		steProfBox.setSelected(monster.getSkillProficient(STE));
		steExpBox.setSelected(monster.getSkillExpertise(STE));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(DEX, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, DEX);
				updateSavingThrow(dexSave, dexSaveBox, DEX);
				updateSkillText(acrobatics, DEX, ACRO);
				updateSkillText(hand, DEX, HAND);
				updateSkillText(stealth, DEX, STE);
			}
		});

		//saving throw checkbox listener
		dexSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(dexSave, dexSaveBox, DEX);
			}
		});

		score.setSelectedItem(monster.getAbilityScore(DEX));

		acroProfBox.addItemListener(new ProficiencyListener(acroProfBox, acroExpBox, acrobatics, DEX, ACRO));
		acroExpBox.addItemListener(new ExpertiseListener(acroProfBox, acroExpBox, acrobatics, DEX, ACRO));

		handProfBox.addItemListener(new ProficiencyListener(handProfBox, handExpBox, hand, DEX, HAND));
		handExpBox.addItemListener(new ExpertiseListener(handProfBox, handExpBox, hand, DEX, HAND));

		steProfBox.addItemListener(new ProficiencyListener(steProfBox, steExpBox, stealth, DEX, STE));
		steExpBox.addItemListener(new ExpertiseListener(steProfBox, steExpBox, stealth, DEX, STE));

		dexPanel.add(dex);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		dexPanel.add(score);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		dexPanel.add(mod);
		
		savePanel.add(dexSaveBox);
		savePanel.add(dexSave);
		dexPanel.add(savePanel);
		
		acroPanel.add(acroExpBox);
		acroPanel.add(acroProfBox);
		acroPanel.add(acrobatics);
		dexPanel.add(acroPanel);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		handPanel.add(handExpBox);
		handPanel.add(handProfBox);
		handPanel.add(hand);
		dexPanel.add(handPanel);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		stePanel.add(steExpBox);
		stePanel.add(steProfBox);
		stePanel.add(stealth);
		dexPanel.add(stePanel);
		
		return dexPanel;
	}
	
	/**
	 * CON information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getCon() {
		JPanel conPanel = new JPanel();
		conPanel.setMinimumSize(scorePanelSize);
		conPanel.setPreferredSize(scorePanelSize);
		conPanel.setMaximumSize(scorePanelSize);
		conPanel.setLayout(new BoxLayout(conPanel, BoxLayout.Y_AXIS));
		conPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		conPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel con = new JLabel(CON);
		con.setAlignmentX(Component.CENTER_ALIGNMENT);
		con.setFont(new Font(con.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		conSaveBox = new JCheckBox();
		conSaveBox.setSelected(monster.getAbilityProficiency(CON));
		conSave = new JLabel();
		conSave.setFont(new Font(conSave.getFont().getName(), Font.PLAIN, conSave.getFont().getSize()));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(CON, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, CON);
				updateSavingThrow(conSave, conSaveBox, CON);
			}
		});

		//saving throw checkbox listener
		conSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(conSave, conSaveBox, CON);
			}
		});

		score.setSelectedItem(monster.getAbilityScore(CON));
		
		conPanel.add(con);
		conPanel.add(Box.createRigidArea(VERTICAL_GAP));
		conPanel.add(score);
		conPanel.add(Box.createRigidArea(VERTICAL_GAP));
		conPanel.add(mod);
		
		savePanel.add(conSaveBox);
		savePanel.add(conSave);
		conPanel.add(savePanel);
		
		return conPanel;
	}
	
	/**
	 * INT information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getInt() {
		JPanel intPanel = new JPanel();
		intPanel.setMinimumSize(scorePanelSize);
		intPanel.setPreferredSize(scorePanelSize);
		intPanel.setMaximumSize(scorePanelSize);
		intPanel.setLayout(new BoxLayout(intPanel, BoxLayout.Y_AXIS));
		intPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		intPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel inte = new JLabel(INT);
		inte.setAlignmentX(Component.CENTER_ALIGNMENT);
		inte.setFont(new Font(inte.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		intSaveBox = new JCheckBox();
		intSaveBox.setSelected(monster.getAbilityProficiency(INT));
		intSave = new JLabel();
		intSave.setFont(new Font(intSave.getFont().getName(), Font.PLAIN, intSave.getFont().getSize()));

		JPanel arcPanel = new JPanel();
		arcPanel.setLayout(new BoxLayout(arcPanel, BoxLayout.X_AXIS));
		arcana = new JLabel();
		arcana.setFont(new Font(arcana.getFont().getName(), Font.PLAIN, arcana.getFont().getSize()));
		JCheckBox arcProfBox = new JCheckBox();
		JCheckBox arcExpBox = new JCheckBox();
		arcProfBox.setSelected(monster.getSkillProficient(ARC));
		arcExpBox.setSelected(monster.getSkillExpertise(ARC));

		JPanel hisPanel = new JPanel();
		hisPanel.setLayout(new BoxLayout(hisPanel, BoxLayout.X_AXIS));
		history = new JLabel();
		history.setFont(new Font(history.getFont().getName(), Font.PLAIN, history.getFont().getSize()));
		JCheckBox hisProfBox = new JCheckBox();
		JCheckBox hisExpBox = new JCheckBox();
		hisProfBox.setSelected(monster.getSkillProficient(HIS));
		hisExpBox.setSelected(monster.getSkillExpertise(HIS));

		JPanel invPanel = new JPanel();
		invPanel.setLayout(new BoxLayout(invPanel, BoxLayout.X_AXIS));
		investigation = new JLabel();
		investigation.setFont(new Font(investigation.getFont().getName(), Font.PLAIN, investigation.getFont().getSize()));
		JCheckBox invProfBox = new JCheckBox();
		JCheckBox invExpBox = new JCheckBox();
		invProfBox.setSelected(monster.getSkillProficient(INV));
		invExpBox.setSelected(monster.getSkillExpertise(INV));

		JPanel natPanel = new JPanel();
		natPanel.setLayout(new BoxLayout(natPanel, BoxLayout.X_AXIS));
		nature = new JLabel();
		nature.setFont(new Font(nature.getFont().getName(), Font.PLAIN, nature.getFont().getSize()));
		JCheckBox natProfBox = new JCheckBox();
		JCheckBox natExpBox = new JCheckBox();
		natProfBox.setSelected(monster.getSkillProficient(NAT));
		natExpBox.setSelected(monster.getSkillExpertise(NAT));

		JPanel relPanel = new JPanel();
		relPanel.setLayout(new BoxLayout(relPanel, BoxLayout.X_AXIS));
		religion = new JLabel();
		religion.setFont(new Font(religion.getFont().getName(), Font.PLAIN, religion.getFont().getSize()));
		JCheckBox relProfBox = new JCheckBox();
		JCheckBox relExpBox = new JCheckBox();
		relProfBox.setSelected(monster.getSkillProficient(REL));
		relExpBox.setSelected(monster.getSkillExpertise(REL));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(INT, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, INT);
				updateSavingThrow(intSave, intSaveBox, INT);
				updateSkillText(arcana, INT, ARC);
				updateSkillText(history, INT, HIS);
				updateSkillText(investigation, INT, INV);
				updateSkillText(nature, INT, NAT);
				updateSkillText(religion, INT, REL);
			}
		});

		//saving throw checkbox listener
		intSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(intSave, intSaveBox, INT);
			}
		});

		score.setSelectedItem(monster.getAbilityScore(INT));

		arcProfBox.addItemListener(new ProficiencyListener(arcProfBox, arcExpBox, arcana, INT, ARC));
		arcExpBox.addItemListener(new ExpertiseListener(arcProfBox, arcExpBox, arcana, INT, ARC));

		hisProfBox.addItemListener(new ProficiencyListener(hisProfBox, hisExpBox, history, INT, HIS));
		hisExpBox.addItemListener(new ExpertiseListener(hisProfBox, hisExpBox, history, INT, HIS));

		invProfBox.addItemListener(new ProficiencyListener(invProfBox, invExpBox, investigation, INT, INV));
		invExpBox.addItemListener(new ExpertiseListener(invProfBox, invExpBox, investigation, INT, INV));

		natProfBox.addItemListener(new ProficiencyListener(natProfBox, natExpBox, nature, INT, NAT));
		natExpBox.addItemListener(new ExpertiseListener(natProfBox, natExpBox, nature, INT, NAT));

		relProfBox.addItemListener(new ProficiencyListener(relProfBox, relExpBox, religion, INT, REL));
		relExpBox.addItemListener(new ExpertiseListener(relProfBox, relExpBox, religion, INT, REL));
		
		intPanel.add(inte);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		intPanel.add(score);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		intPanel.add(mod);
		
		savePanel.add(intSaveBox);
		savePanel.add(intSave);
		intPanel.add(savePanel);
		
		arcPanel.add(arcExpBox);
		arcPanel.add(arcProfBox);
		arcPanel.add(arcana);
		intPanel.add(arcPanel);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		hisPanel.add(hisExpBox);
		hisPanel.add(hisProfBox);
		hisPanel.add(history);
		intPanel.add(hisPanel);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		invPanel.add(invExpBox);
		invPanel.add(invProfBox);
		invPanel.add(investigation);
		intPanel.add(invPanel);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		natPanel.add(natExpBox);
		natPanel.add(natProfBox);
		natPanel.add(nature);
		intPanel.add(natPanel);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		relPanel.add(relExpBox);
		relPanel.add(relProfBox);
		relPanel.add(religion);
		intPanel.add(relPanel);
		
		return intPanel;
	}
	
	/**
	 * WIS information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getWis() {
		JPanel wisPanel = new JPanel();
		wisPanel.setMinimumSize(scorePanelSize);
		wisPanel.setPreferredSize(scorePanelSize);
		wisPanel.setMaximumSize(scorePanelSize);
		wisPanel.setLayout(new BoxLayout(wisPanel, BoxLayout.Y_AXIS));
		wisPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		wisPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel wis = new JLabel(WIS);
		wis.setAlignmentX(Component.CENTER_ALIGNMENT);
		wis.setFont(new Font(wis.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		wisSaveBox = new JCheckBox();
		wisSaveBox.setSelected(monster.getAbilityProficiency(WIS));
		wisSave = new JLabel();
		wisSave.setFont(new Font(wisSave.getFont().getName(), Font.PLAIN, wisSave.getFont().getSize()));

		JPanel aniPanel = new JPanel();
		aniPanel.setLayout(new BoxLayout(aniPanel, BoxLayout.X_AXIS));
		animal = new JLabel();
		animal.setFont(new Font(animal.getFont().getName(), Font.PLAIN, animal.getFont().getSize()));
		JCheckBox aniProfBox = new JCheckBox();
		JCheckBox aniExpBox = new JCheckBox();
		aniProfBox.setSelected(monster.getSkillProficient(ANI));
		aniExpBox.setSelected(monster.getSkillExpertise(ANI));

		JPanel insPanel = new JPanel();
		insPanel.setLayout(new BoxLayout(insPanel, BoxLayout.X_AXIS));
		insight = new JLabel();
		insight.setFont(new Font(insight.getFont().getName(), Font.PLAIN, insight.getFont().getSize()));
		JCheckBox insProfBox = new JCheckBox();
		JCheckBox insExpBox = new JCheckBox();
		insProfBox.setSelected(monster.getSkillProficient(INS));
		insExpBox.setSelected(monster.getSkillExpertise(INS));

		JPanel medPanel = new JPanel();
		medPanel.setLayout(new BoxLayout(medPanel, BoxLayout.X_AXIS));
		medicine = new JLabel();
		medicine.setFont(new Font(medicine.getFont().getName(), Font.PLAIN, medicine.getFont().getSize()));
		JCheckBox medProfBox = new JCheckBox();
		JCheckBox medExpBox = new JCheckBox();
		medProfBox.setSelected(monster.getSkillProficient(MED));
		medExpBox.setSelected(monster.getSkillExpertise(MED));

		JPanel perPanel = new JPanel();
		perPanel.setLayout(new BoxLayout(perPanel, BoxLayout.X_AXIS));
		perception = new JLabel();
		perception.setFont(new Font(perception.getFont().getName(), Font.PLAIN, perception.getFont().getSize()));
		JCheckBox perProfBox = new JCheckBox();
		JCheckBox perExpBox = new JCheckBox();
		perProfBox.setSelected(monster.getSkillProficient(PER));
		perExpBox.setSelected(monster.getSkillExpertise(PER));

		JPanel surPanel = new JPanel();
		surPanel.setLayout(new BoxLayout(surPanel, BoxLayout.X_AXIS));
		survival = new JLabel();
		survival.setFont(new Font(survival.getFont().getName(), Font.PLAIN, survival.getFont().getSize()));
		JCheckBox surProfBox = new JCheckBox();
		JCheckBox surExpBox = new JCheckBox();
		surProfBox.setSelected(monster.getSkillProficient(SUR));
		surExpBox.setSelected(monster.getSkillExpertise(SUR));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(WIS, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, WIS);
				updateSavingThrow(wisSave, wisSaveBox, WIS);
				updateSkillText(animal, WIS, ANI);
				updateSkillText(insight, WIS, INS);
				updateSkillText(medicine, WIS, MED);
				updateSkillText(perception, WIS, PER);
				updateSkillText(survival, WIS, SUR);
			}
		});

		//saving throw checkbox listener
		wisSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(wisSave, wisSaveBox, WIS);
			}
		});

		score.setSelectedItem(monster.getAbilityScore(WIS));

		aniProfBox.addItemListener(new ProficiencyListener(aniProfBox, aniExpBox, animal, WIS, ANI));
		aniExpBox.addItemListener(new ExpertiseListener(aniProfBox, aniExpBox, animal, WIS, ANI));

		insProfBox.addItemListener(new ProficiencyListener(insProfBox, insExpBox, insight, WIS, INS));
		insExpBox.addItemListener(new ExpertiseListener(insProfBox, insExpBox, insight, WIS, INS));

		medProfBox.addItemListener(new ProficiencyListener(medProfBox, medExpBox, medicine, WIS, MED));
		medExpBox.addItemListener(new ExpertiseListener(medProfBox, medExpBox, medicine, WIS, MED));

		perProfBox.addItemListener(new ProficiencyListener(perProfBox, perExpBox, perception, WIS, PER));
		perExpBox.addItemListener(new ExpertiseListener(perProfBox, perExpBox, perception, WIS, PER));

		surProfBox.addItemListener(new ProficiencyListener(surProfBox, surExpBox, survival, WIS, SUR));
		surExpBox.addItemListener(new ExpertiseListener(surProfBox, surExpBox, survival, WIS, SUR));
		
		wisPanel.add(wis);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		wisPanel.add(score);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		wisPanel.add(mod);
		
		savePanel.add(wisSaveBox);
		savePanel.add(wisSave);
		wisPanel.add(savePanel);
		
		aniPanel.add(aniExpBox);
		aniPanel.add(aniProfBox);
		aniPanel.add(animal);
		wisPanel.add(aniPanel);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		insPanel.add(insExpBox);
		insPanel.add(insProfBox);
		insPanel.add(insight);
		wisPanel.add(insPanel);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		medPanel.add(medExpBox);
		medPanel.add(medProfBox);
		medPanel.add(medicine);
		wisPanel.add(medPanel);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		perPanel.add(perExpBox);
		perPanel.add(perProfBox);
		perPanel.add(perception);
		wisPanel.add(perPanel);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		surPanel.add(surExpBox);
		surPanel.add(surProfBox);
		surPanel.add(survival);
		wisPanel.add(surPanel);
		
		return wisPanel;
	}
	
	/**
	 * CHA information, including labels, score, mod, saving throws, and skills
	 * consider creating proficiency jtextfield in getScorePanel, passing to this,
	 * then adding listener here to that changing proficiency will automatically update
	 * skills and saves
	 */
	private JPanel getCha() {
		JPanel chaPanel = new JPanel();
		chaPanel.setMinimumSize(scorePanelSize);
		chaPanel.setPreferredSize(scorePanelSize);
		chaPanel.setMaximumSize(scorePanelSize);
		chaPanel.setLayout(new BoxLayout(chaPanel, BoxLayout.Y_AXIS));
		chaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		chaPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel cha = new JLabel(CHA);
		cha.setAlignmentX(Component.CENTER_ALIGNMENT);
		cha.setFont(new Font(cha.getFont().getName(), Font.BOLD, 15));
		JComboBox score = new JComboBox(scores);
		score.setMaximumSize(new Dimension(45, 20));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mod = new JLabel();
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		chaSaveBox = new JCheckBox();
		chaSaveBox.setSelected(monster.getAbilityProficiency(CHA));
		chaSave = new JLabel();
		chaSave.setFont(new Font(chaSave.getFont().getName(), Font.PLAIN, chaSave.getFont().getSize()));

		JPanel decPanel = new JPanel();
		decPanel.setLayout(new BoxLayout(decPanel, BoxLayout.X_AXIS));
		deception = new JLabel();
		deception.setFont(new Font(deception.getFont().getName(), Font.PLAIN, deception.getFont().getSize()));
		JCheckBox decProfBox = new JCheckBox();
		JCheckBox decExpBox = new JCheckBox();
		decProfBox.setSelected(monster.getSkillProficient(DEC));
		decExpBox.setSelected(monster.getSkillExpertise(DEC));

		JPanel intPanel = new JPanel();
		intPanel.setLayout(new BoxLayout(intPanel, BoxLayout.X_AXIS));
		intimidation = new JLabel();
		intimidation.setFont(new Font(intimidation.getFont().getName(), Font.PLAIN, intimidation.getFont().getSize()));
		JCheckBox intProfBox = new JCheckBox();
		JCheckBox intExpBox = new JCheckBox();
		intProfBox.setSelected(monster.getSkillProficient(INTIM));
		intExpBox.setSelected(monster.getSkillExpertise(INTIM));

		JPanel perfPanel = new JPanel();
		perfPanel.setLayout(new BoxLayout(perfPanel, BoxLayout.X_AXIS));
		performance = new JLabel();
		performance.setFont(new Font(performance.getFont().getName(), Font.PLAIN, performance.getFont().getSize()));
		JCheckBox perfProfBox = new JCheckBox();
		JCheckBox perfExpBox = new JCheckBox();
		perfProfBox.setSelected(monster.getSkillProficient(PERF));
		perfExpBox.setSelected(monster.getSkillExpertise(PERF));

		JPanel persPanel = new JPanel();
		persPanel.setLayout(new BoxLayout(persPanel, BoxLayout.X_AXIS));
		persuasion = new JLabel();
		persuasion.setFont(new Font(persuasion.getFont().getName(), Font.PLAIN, persuasion.getFont().getSize()));
		JCheckBox persProfBox = new JCheckBox();
		JCheckBox persExpBox = new JCheckBox();
		persProfBox.setSelected(monster.getSkillProficient(PERS));
		persExpBox.setSelected(monster.getSkillExpertise(PERS));

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityScore(CHA, (Integer) score.getSelectedItem());
				updateAbilityModifier(mod, CHA);
				updateSavingThrow(chaSave, chaSaveBox, CHA);
				updateSkillText(deception, CHA, DEC);
				updateSkillText(intimidation, CHA, INTIM);
				updateSkillText(performance, CHA, PERF);
				updateSkillText(persuasion, CHA, PERS);
			}
		});

		//saving throw checkbox listener
		chaSaveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSavingThrow(chaSave, chaSaveBox, CHA);
			}
		});

		score.setSelectedItem(monster.getAbilityScore(CHA));

		decProfBox.addItemListener(new ProficiencyListener(decProfBox, decExpBox, deception, CHA, DEC));
		decExpBox.addItemListener(new ExpertiseListener(decProfBox, decExpBox, deception, CHA, DEC));

		intProfBox.addItemListener(new ProficiencyListener(intProfBox, intExpBox, intimidation, CHA, INTIM));
		intExpBox.addItemListener(new ExpertiseListener(intProfBox, intExpBox, intimidation, CHA, INTIM));

		perfProfBox.addItemListener(new ProficiencyListener(perfProfBox, perfExpBox, performance, CHA, PERF));
		perfExpBox.addItemListener(new ExpertiseListener(perfProfBox, perfExpBox, performance, CHA, PERF));

		persProfBox.addItemListener(new ProficiencyListener(persProfBox, persExpBox, persuasion, CHA, PERS));
		persExpBox.addItemListener(new ExpertiseListener(persProfBox, persExpBox, persuasion, CHA, PERS));
		
		chaPanel.add(cha);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		chaPanel.add(score);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		chaPanel.add(mod);
		
		savePanel.add(chaSaveBox);
		savePanel.add(chaSave);
		chaPanel.add(savePanel);
		
		decPanel.add(decExpBox);
		decPanel.add(decProfBox);
		decPanel.add(deception);
		chaPanel.add(decPanel);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		intPanel.add(intExpBox);
		intPanel.add(intProfBox);
		intPanel.add(intimidation);
		chaPanel.add(intPanel);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		perfPanel.add(perfExpBox);
		perfPanel.add(perfProfBox);
		perfPanel.add(performance);
		chaPanel.add(perfPanel);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		
		persPanel.add(persExpBox);
		persPanel.add(persProfBox);
		persPanel.add(persuasion);
		chaPanel.add(persPanel);
		
		return chaPanel;
	}
	
	private JPanel getSensesPanel() {
		JPanel sensePanel = new JPanel();
		sensePanel.setLayout(new BoxLayout(sensePanel, BoxLayout.X_AXIS));
		sensePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sensePanel.setMinimumSize(new Dimension(INNER_WIDTH, 35));
		sensePanel.setMaximumSize(new Dimension(INNER_WIDTH, 35));
		sensePanel.setPreferredSize(new Dimension(INNER_WIDTH, 35));
		JLabel senseLabel = new JLabel("Senses");
		senseLabel.setFont(new Font(senseLabel.getFont().getName(), Font.BOLD, senseLabel.getFont().getSize()));
		JTextArea senses = new JTextArea(monster.getSenses());
		JScrollPane scrollSenses = new JScrollPane(senses);
		scrollSenses.addMouseWheelListener(new MouseWheelScrollListener(scrollSenses));
		senses.setLineWrap(true);
		senses.setWrapStyleWord(true);
		senseLabel.setAlignmentY(Box.TOP_ALIGNMENT);
		scrollSenses.setAlignmentY(Box.TOP_ALIGNMENT);
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setSenses(senses.getText());
			}
		});
		
		senses.getDocument().addDocumentListener(listener);
		senses.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		sensePanel.add(senseLabel);
		sensePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		sensePanel.add(scrollSenses);
		
		return sensePanel;
	}
	
	private JPanel getLanguagePanel() {
		JPanel languagePanel = new JPanel();
		languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.X_AXIS));
		languagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		languagePanel.setMinimumSize(new Dimension(INNER_WIDTH, 35));
		languagePanel.setMaximumSize(new Dimension(INNER_WIDTH, 35));
		languagePanel.setPreferredSize(new Dimension(INNER_WIDTH, 35));
		JLabel languageLabel = new JLabel("Languages");
		languageLabel.setFont(new Font(languageLabel.getFont().getName(), Font.BOLD, languageLabel.getFont().getSize()));
		JTextArea languages = new JTextArea(monster.getLanguages());
		JScrollPane scrollLanguages = new JScrollPane(languages);
		scrollLanguages.addMouseWheelListener(new MouseWheelScrollListener(scrollLanguages));
		languages.setLineWrap(true);
		languages.setWrapStyleWord(true);
		languageLabel.setAlignmentY(Box.TOP_ALIGNMENT);
		scrollLanguages.setAlignmentY(Box.TOP_ALIGNMENT);
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setLanguages(languages.getText());
			}
		});
		
		languages.getDocument().addDocumentListener(listener);
		languages.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		languagePanel.add(languageLabel);
		languagePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		languagePanel.add(scrollLanguages);
		
		return languagePanel;
	}

	private JPanel getChallengePanel() {
		JPanel challengePanel = new JPanel();
		challengePanel.setLayout(new BoxLayout(challengePanel, BoxLayout.X_AXIS));
		challengePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		challengePanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel challengeLabel = new JLabel("Challenge");

		JComboBox cr = new JComboBox(crs);
		cr.setMaximumSize(new Dimension(45, 20));
		cr.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel xpLabel = new JLabel();
		xpLabel.setFont(new Font(xpLabel.getFont().getName(), Font.ITALIC, xpLabel.getFont().getSize()));

		cr.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				monster.setChallenge(cr.getSelectedItem().toString());
				xpLabel.setText("(" + monster.getXP() + "XP)");
				updateSavingThrow(strSave, strSaveBox, STR);
				updateSkillText(athletics, STR, ATH);

				updateSavingThrow(dexSave, dexSaveBox, DEX);
				updateSkillText(acrobatics, DEX, ACRO);
				updateSkillText(hand, DEX, HAND);
				updateSkillText(stealth, DEX, STE);

				updateSavingThrow(intSave, intSaveBox, INT);
				updateSkillText(arcana, INT, ARC);
				updateSkillText(history, INT, HIS);
				updateSkillText(investigation, INT, INV);
				updateSkillText(nature, INT, NAT);
				updateSkillText(religion, INT, REL);

				updateSavingThrow(wisSave, wisSaveBox, WIS);
				updateSkillText(animal, WIS, ANI);
				updateSkillText(insight, WIS, INS);
				updateSkillText(medicine, WIS, MED);
				updateSkillText(perception, WIS, PER);
				updateSkillText(survival, WIS, SUR);

				updateSavingThrow(chaSave, chaSaveBox, CHA);
				updateSkillText(deception, CHA, DEC);
				updateSkillText(intimidation, CHA, INTIM);
				updateSkillText(performance, CHA, PERF);
				updateSkillText(persuasion, CHA, PERS);
			}
		});

		cr.setSelectedItem(monster.getChallenge());
		
		challengePanel.add(challengeLabel);
		challengePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		challengePanel.add(cr);
		challengePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		challengePanel.add(xpLabel);
		
		return challengePanel;
	}
	
	private JLabel getAbilityLabel() {
		JLabel abilityLabel = new JLabel("Abilities");
		abilityLabel.setFont(new Font(abilityLabel.getFont().getName(), Font.BOLD, 15));
		abilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return abilityLabel;
	}

	//TODO: change name from JLabel to JTextField, eliminate rename button. Find out how to dynamically resize a JTextField as the user types
	//TODO: apply above to action and legendary action
	private JPanel getAbilityPanel(int i, JScrollPane scroll) {
		final int index = i;
		
		JPanel abilityPanel = new JPanel();
		abilityPanel.setLayout(new BoxLayout(abilityPanel, BoxLayout.X_AXIS));
		abilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		abilityPanel.setMaximumSize(new Dimension(INNER_WIDTH, 65));
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JLabel abilityName = new JLabel(abilityList.get(i).getName());
		abilityName.setFont(new Font(abilityName.getFont().getName(), Font.BOLD, abilityName.getFont().getSize()));
		
		JButton renameAbility = new JButton("Rename");
		renameAbility.setMinimumSize(new Dimension(100, 25));
		renameAbility.setMaximumSize(new Dimension(100, 25));
		renameAbility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Enter the new name for the ability:";
				String name = (String) JOptionPane.showInputDialog(scroll, message, "Rename Ability",
						JOptionPane.QUESTION_MESSAGE);
						
						System.out.println(name);
						
				if (name == null || name.equals(""))
					return;
				
				monster.renameAbility(name, index);
				refreshRight();
			}
		});
		
		JButton deleteAbility = new JButton("Delete");
		deleteAbility.setMinimumSize(new Dimension(100, 25));
		deleteAbility.setMaximumSize(new Dimension(100, 25));
		deleteAbility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int yesNo = (int) JOptionPane.showConfirmDialog(scroll, "Are you sure you wish to delete "
					+ abilityName.getText() + "?", "Delete Ability", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
				
				if (yesNo == JOptionPane.YES_OPTION) {
					monster.deleteAbility(index);
					refreshRight();
				}
			}
		});
		
		innerPanel.setAlignmentY(Box.CENTER_ALIGNMENT);
		innerPanel.add(abilityName);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(renameAbility);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(deleteAbility);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		JTextArea abilityDesc = new JTextArea(abilityList.get(i).getDescription());
		abilityDesc.setAlignmentY(Box.CENTER_ALIGNMENT);
		abilityDesc.setLineWrap(true);
		abilityDesc.setWrapStyleWord(true);
		JScrollPane scrollDesc = new JScrollPane(abilityDesc);
		scrollDesc.addMouseWheelListener(new MouseWheelScrollListener(scrollDesc));
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setAbilityDescription(abilityDesc.getText(), i);
			}
		});
		
		abilityDesc.getDocument().addDocumentListener(listener);
		abilityDesc.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		abilityPanel.add(innerPanel);
		abilityPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		abilityPanel.add(scrollDesc);
		
		return abilityPanel;
	}
	
	private JLabel getActionLabel() {
		JLabel actionLabel = new JLabel("Actions");
		actionLabel.setFont(new Font(actionLabel.getFont().getName(), Font.BOLD, 15));
		actionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return actionLabel;
	}
	
	private JPanel getActionPanel(int i, JScrollPane scroll) {
		final int index = i;
		
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
		actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		actionPanel.setMaximumSize(new Dimension(INNER_WIDTH, 65));
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JLabel actionName = new JLabel(actionList.get(i).getName());
		actionName.setFont(new Font(actionName.getFont().getName(), Font.BOLD, actionName.getFont().getSize()));
		
		JButton renameAction = new JButton("Rename");
		renameAction.setMinimumSize(new Dimension(100, 25));
		renameAction.setMaximumSize(new Dimension(100, 25));
		renameAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Enter the new name for the action:";
				String name = (String) JOptionPane.showInputDialog(scroll, message, "Rename Action",
						JOptionPane.QUESTION_MESSAGE);
						
				if (name == null)
					return;
				
				monster.renameAction(name, index);
				refreshRight();
			}
		});
		
		JButton deleteAction = new JButton("Delete");
		deleteAction.setMinimumSize(new Dimension(100, 25));
		deleteAction.setMaximumSize(new Dimension(100, 25));
		deleteAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int yesNo = (int) JOptionPane.showConfirmDialog(scroll, "Are you sure you wish to delete "
					+ actionName.getText() + "?", "Delete Action", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
				
				if (yesNo == JOptionPane.YES_OPTION) {
					monster.deleteAction(index);
					refreshRight();
				}
			}
		});
		
		innerPanel.setAlignmentY(Box.CENTER_ALIGNMENT);
		innerPanel.add(actionName);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(renameAction);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(deleteAction);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		
		JTextArea actionDesc = new JTextArea(actionList.get(i).getDescription());
		actionDesc.setAlignmentY(Box.CENTER_ALIGNMENT);
		actionDesc.setLineWrap(true);
		actionDesc.setWrapStyleWord(true);
		JScrollPane scrollDesc = new JScrollPane(actionDesc);
		scrollDesc.addMouseWheelListener(new MouseWheelScrollListener(scrollDesc));
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setActionDescription(actionDesc.getText(), i);
			}
		});
		
		actionDesc.getDocument().addDocumentListener(listener);
		actionDesc.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		actionPanel.add(innerPanel);
		actionPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		actionPanel.add(scrollDesc);
		
		return actionPanel;
	}
	
	private JLabel getLegendaryLabel() {
		JLabel legendaryLabel = new JLabel("Legendary Actions");
		legendaryLabel.setFont(new Font(legendaryLabel.getFont().getName(), Font.BOLD, 15));
		legendaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return legendaryLabel;
	}

	//TODO: only include this panel if the monster has legendary actions
	private JPanel getCountPanel() {
		JPanel countPanel = new JPanel();
		countPanel.setLayout(new BoxLayout(countPanel, BoxLayout.X_AXIS));
		countPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		countPanel.setMaximumSize(new Dimension(INNER_WIDTH, INNER_HEIGHT));
		JLabel countLabel = new JLabel("Legendary Action Count");
		countLabel.setFont(new Font(countLabel.getFont().getName(), Font.BOLD, countLabel.getFont().getSize()));
		JTextField count = new JTextField(monster.getLegendaryActionCount());
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setLegendaryActionCount(Integer.parseInt(count.getText()));
			}
		});
		
		count.getDocument().addDocumentListener(listener);
		count.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		countPanel.add(countLabel);
		countPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		countPanel.add(count);
		
		return countPanel;
	}
	
	private JPanel getLegendaryPanel(int i, JScrollPane scroll) {
		JPanel legendaryPanel = new JPanel();
		legendaryPanel.setLayout(new BoxLayout(legendaryPanel, BoxLayout.X_AXIS));
		legendaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		legendaryPanel.setMaximumSize(new Dimension(INNER_WIDTH, 65));
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JLabel legendaryName = new JLabel(legendaryList.get(i).getName());
		legendaryName.setFont(new Font(legendaryName.getFont().getName(), Font.BOLD, legendaryName.getFont().getSize()));
		
		final int index = i;
		
		JButton renameLegendary = new JButton("Rename");
		renameLegendary.setMinimumSize(new Dimension(100, 25));
		renameLegendary.setMaximumSize(new Dimension(100, 25));
		renameLegendary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Enter the new name for the legendary action:";
				String name;
				name = (String) JOptionPane.showInputDialog(scroll, message, "Rename Legendary Action",
						JOptionPane.QUESTION_MESSAGE);
						
				if (name == null)
					return;
				
				monster.renameLegendaryAction(name, index);
				refreshRight();
			}
		});
		
		JButton deleteLegendary = new JButton("Delete");
		deleteLegendary.setMinimumSize(new Dimension(100, 25));
		deleteLegendary.setMaximumSize(new Dimension(100, 25));
		deleteLegendary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int yesNo = (int) JOptionPane.showConfirmDialog(scroll, "Are you sure you wish to delete "
					+ legendaryName.getText() + "?", "Delete Legendary Action", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
				
				if (yesNo == JOptionPane.YES_OPTION) {
					monster.deleteLegendaryAction(index);
					refreshRight();
				}
			}
		});
		
		innerPanel.setAlignmentY(Box.CENTER_ALIGNMENT);
		innerPanel.add(legendaryName);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(renameLegendary);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		innerPanel.add(deleteLegendary);
		innerPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		JTextArea legendaryDesc = new JTextArea(legendaryList.get(i).getDescription());
		legendaryDesc.setAlignmentY(Box.CENTER_ALIGNMENT);
		legendaryDesc.setLineWrap(true);
		legendaryDesc.setWrapStyleWord(true);
		JScrollPane scrollDesc = new JScrollPane(legendaryDesc);
		scrollDesc.addMouseWheelListener(new MouseWheelScrollListener(scrollDesc));
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setLegendaryDescription(legendaryDesc.getText(), i);
			}
		});
		
		legendaryDesc.getDocument().addDocumentListener(listener);
		legendaryDesc.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		legendaryPanel.add(innerPanel);
		legendaryPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		legendaryPanel.add(scrollDesc);
		
		return legendaryPanel;
	}
	
	/**
	 * Defines what will happen when an item in the JList is selected
	 */
	private void listValueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;
			
		//update current monster on server before getting new monster info
		if (!monster.getName().equals("select a monster")) {
			proxy.updateMonster(monster);
		}
		
		getMonster(getLastSelected());
	}
	
	//Stringify last selected value of JList
	public String getLastSelected() {
		if (list.isSelectionEmpty()) {
			return "select a monster";
		}
			
		return list.getSelectedValue().toString();
	}
	
	//gets currently selected monster
	public Monster getMonster() {
		return monster;
	}
	
	/**
	 * Sets the currently selected value in the JList. After saving or updating
	 * some things (such as adding an ability, action, etc, the current
	 * selection seems to be lost, preventing back to back saving without
	 * re-selecting the monster
	 */
	 public void setSelection() {
		 list.setSelectedValue(monster.getName(), true);
		 //updates current monster info, needed for restoring
		 monster = proxy.getMonster(monster.getName());
	 }

	private void updateAbilityModifier(JLabel modLabel, String stat) {
		modLabel.setText("(" + monster.getSignedAbilityModifier(stat) + ")");
	}

	private void updateSavingThrow(JLabel save, JCheckBox saveCheck, String stat) {
		monster.setAbilityProficiency(stat, saveCheck.isSelected());
		save.setText("Saving Throws: " + monster.getSignedSavingThrow(stat));
	}

	private void updateSkillText(JLabel skillLabel, String stat, String skill) {
		skillLabel.setText(skill + ": " + monster.getSignedSkillModifier(stat, skill));
	}

	private void updateSkillProf(JCheckBox prof, JCheckBox exp, JLabel skillLabel, String stat, String skill) {
		monster.setSkillProficiency(skill, prof.isSelected());

		if (!prof.isSelected()) {
			exp.setSelected(false);
			monster.setSkillExpertise(skill, false);
		}

		updateSkillText(skillLabel, stat, skill);
	}

	private void updateSkillExp(JCheckBox prof, JCheckBox exp, JLabel skillLabel, String stat, String skill) {
		monster.setSkillExpertise(skill, exp.isSelected());

		if (exp.isSelected()) {
			prof.setSelected(true);
			monster.setSkillProficiency(skill, true);
		}

		updateSkillText(skillLabel, stat, skill);
	}

	/**
	 * Custom ItemListener for when a Proficiency JCheckBox is clicked
	 */
	private class ProficiencyListener implements ItemListener {
		private final JCheckBox prof, exp;
		JLabel skillLabel;
		private final String stat, skill;

		/**
		 * Constructor that sets up the values used by the listener
		 * @param prof Proficiency JCheckBox
		 * @param exp Expertise JCheckBox
		 * @param skillLabel JLabel for the skill
		 * @param stat Stat string
		 * @param skill Skill string
		 */
		ProficiencyListener(JCheckBox prof, JCheckBox exp, JLabel skillLabel, String stat, String skill) {
			this.prof = prof;
			this.exp = exp;
			this.skillLabel = skillLabel;
			this.stat = stat;
			this.skill = skill;
		}

		/**
		 * Invoked when an item has been selected or deselected by the user.
		 * The code written for this method performs the operations
		 * that need to occur when an item is selected (or deselected).
		 *
		 * @param e the event to be processed
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			updateSkillProf(prof, exp, skillLabel, stat, skill);
		}
	}

	/**
	 * Custom ItemListener for when a Expertise JCheckBox is clicked
	 */
	private class ExpertiseListener implements ItemListener {
		private final JCheckBox prof, exp;
		JLabel skillLabel;
		private final String stat, skill;

		/**
		 * Constructor that sets up the values used by the listener
		 * @param prof Proficiency JCheckBox
		 * @param exp Expertise JCheckBox
		 * @param skillLabel JLabel for the skill
		 * @param stat Stat string
		 * @param skill Skill string
		 */
		ExpertiseListener(JCheckBox prof, JCheckBox exp, JLabel skillLabel, String stat, String skill) {
			this.prof = prof;
			this.exp = exp;
			this.skillLabel = skillLabel;
			this.stat = stat;
			this.skill = skill;
		}

		/**
		 * Invoked when an item has been selected or deselected by the user.
		 * The code written for this method performs the operations
		 * that need to occur when an item is selected (or deselected).
		 *
		 * @param e the event to be processed
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			updateSkillExp(prof, exp, skillLabel, stat, skill);
		}
	}
}
