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
		final String STR = "STR";
		int strScore = monster.getAbilityScore(STR);
		int strMod = Math.floorDiv(strScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(strScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		
		JLabel mod = new JLabel("(" + signBonus(strMod) + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(STR));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(strMod, monster.getProficiency(),
					monster.getAbilityProficiency(STR), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
	
		final String ATH = "Athletics";
		JPanel athPanel = new JPanel();
		athPanel.setLayout(new BoxLayout(athPanel, BoxLayout.X_AXIS));
		JLabel athletics = new JLabel("Athletics: " + signBonus(calcBonus(strMod, monster.getProficiency(),
					monster.getSkillProficienct(ATH), monster.getSkillExpertise(ATH))));
		athletics.setFont(new Font(athletics.getFont().getName(), Font.PLAIN, athletics.getFont().getSize()));
		JCheckBox athProfBox = new JCheckBox();
		JCheckBox athExpBox = new JCheckBox();
		athProfBox.setSelected(monster.getSkillProficienct(ATH));
		athExpBox.setSelected(monster.getSkillExpertise(ATH));
		

		//TODO: make scores spinners
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(STR, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(STR), false)));
				athletics.setText("Athletics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ATH), monster.getSkillExpertise(ATH))));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(STR, true);
					
				else
					monster.setAbilityProficiency(STR, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(STR), false)));
			}
		});
		
		//athletics checkbox listeners
		athProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(ATH, true);
					
				else {
					monster.setSkillProficiency(ATH, false);
					athExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				athletics.setText("Athletics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ATH), monster.getSkillExpertise(ATH))));
			}
		});
		
		athExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(ATH, true);
					athProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(ATH, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				athletics.setText("Athletics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ATH), monster.getSkillExpertise(ATH))));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(STR, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(STR), false)));
				athletics.setText("Athletics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ATH), monster.getSkillExpertise(ATH))));
			}
		});
		
		strPanel.add(str);
		strPanel.add(Box.createRigidArea(VERTICAL_GAP));
		strPanel.add(score);
		strPanel.add(Box.createRigidArea(VERTICAL_GAP));
		strPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		final String DEX = "DEX";
		int dexScore = monster.getAbilityScore(DEX);
		int dexMod = Math.floorDiv(dexScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(dexScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		String modifier = signBonus(dexMod);
		JLabel mod = new JLabel("(" + modifier + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(DEX));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(dexMod, monster.getProficiency(),
			monster.getAbilityProficiency(DEX), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
		
		final String ACRO = "Acrobatics";
		JPanel acroPanel = new JPanel();
		acroPanel.setLayout(new BoxLayout(acroPanel, BoxLayout.X_AXIS));
		JLabel acrobatics = new JLabel("Acrobatics: " + signBonus(calcBonus(dexMod, monster.getProficiency(),
					monster.getSkillProficienct(ACRO), monster.getSkillExpertise(ACRO))));
		acrobatics.setFont(new Font(acrobatics.getFont().getName(), Font.PLAIN, acrobatics.getFont().getSize()));
		JCheckBox acroProfBox = new JCheckBox();
		JCheckBox acroExpBox = new JCheckBox();
		acroProfBox.setSelected(monster.getSkillProficienct(ACRO));
		acroExpBox.setSelected(monster.getSkillExpertise(ACRO));
		
		final String HAND = "Sleight of Hand";
		JPanel handPanel = new JPanel();
		handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
		JLabel hand = new JLabel("Sleight of Hand: " + signBonus(calcBonus(dexMod, monster.getProficiency(),
					monster.getSkillProficienct(HAND), monster.getSkillExpertise(HAND))));
		hand.setFont(new Font(hand.getFont().getName(), Font.PLAIN, hand.getFont().getSize()));
		JCheckBox handProfBox = new JCheckBox();
		JCheckBox handExpBox = new JCheckBox();
		handProfBox.setSelected(monster.getSkillProficienct(HAND));
		handExpBox.setSelected(monster.getSkillExpertise(HAND));
		
		final String STE = "Stealth";
		JPanel stePanel = new JPanel();
		stePanel.setLayout(new BoxLayout(stePanel, BoxLayout.X_AXIS));
		JLabel stealth = new JLabel("Stealth: " + signBonus(calcBonus(dexMod, monster.getProficiency(),
					monster.getSkillProficienct(STE), monster.getSkillExpertise(STE))));
		stealth.setFont(new Font(stealth.getFont().getName(), Font.PLAIN, stealth.getFont().getSize()));
		JCheckBox steProfBox = new JCheckBox();
		JCheckBox steExpBox = new JCheckBox();
		steProfBox.setSelected(monster.getSkillProficienct(STE));
		steExpBox.setSelected(monster.getSkillExpertise(STE));
		
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(DEX, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(DEX), false)));
				acrobatics.setText("Acrobatics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ACRO), monster.getSkillExpertise(ACRO))));
				hand.setText("Sleight of Hand: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HAND), monster.getSkillExpertise(HAND))));
				stealth.setText("Stealth: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(STE), monster.getSkillExpertise(STE))));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(DEX, true);
					
				else
					monster.setAbilityProficiency(DEX, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(DEX), false)));
			}
		});
		
		//acrobatics checkbox listeners
		acroProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(ACRO, true);
					
				else {
					monster.setSkillProficiency(ACRO, false);
					acroExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				acrobatics.setText("Acrobatics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ACRO), monster.getSkillExpertise(ACRO))));
			}
		});
		
		acroExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(ACRO, true);
					acroProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(ACRO, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				acrobatics.setText("Acrobatics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ACRO), monster.getSkillExpertise(ACRO))));
			}
		});
		
		//sleight of hand checkbox listeners
		handProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(HAND, true);
					
				else {
					monster.setSkillProficiency(HAND, false);
					handExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				hand.setText("Sleight of Hand: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HAND), monster.getSkillExpertise(HAND))));
			}
		});
		
		handExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(HAND, true);
					handProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(HAND, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				hand.setText("Sleight of Hand: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HAND), monster.getSkillExpertise(HAND))));
			}
		});
		
		//stealth checkbox listeners
		steProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(STE, true);
					
				else {
					monster.setSkillProficiency(STE, false);
					steExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				stealth.setText("Stealth: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(STE), monster.getSkillExpertise(STE))));
			}
		});
		
		steExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(STE, true);
					steProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(STE, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				stealth.setText("Stealth: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(STE), monster.getSkillExpertise(STE))));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(DEX, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(DEX), false)));
				acrobatics.setText("Acrobatics: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ACRO), monster.getSkillExpertise(ACRO))));
				hand.setText("Sleight of Hand: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HAND), monster.getSkillExpertise(HAND))));
				stealth.setText("Stealth: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(STE), monster.getSkillExpertise(STE))));
			}
		});

		dexPanel.add(dex);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		dexPanel.add(score);
		dexPanel.add(Box.createRigidArea(VERTICAL_GAP));
		dexPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		final String CON = "CON";
		int conScore = monster.getAbilityScore(CON);
		int conMod = Math.floorDiv(conScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(conScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		String modifier = signBonus(conMod);
		JLabel mod = new JLabel("(" + modifier + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(CON));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(conMod, monster.getProficiency(),
			monster.getAbilityProficiency(CON), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
		
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(CON, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CON), false)));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(CON, true);
					
				else
					monster.setAbilityProficiency(CON, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CON), false)));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(CON, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CON), false)));
			}
		});
		
		conPanel.add(con);
		conPanel.add(Box.createRigidArea(VERTICAL_GAP));
		conPanel.add(score);
		conPanel.add(Box.createRigidArea(VERTICAL_GAP));
		conPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		final String INT = "INT";
		int intScore = monster.getAbilityScore(INT);
		int intMod = Math.floorDiv(intScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(intScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		String modifier = signBonus(intMod);
		JLabel mod = new JLabel("(" + modifier + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(INT));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(intMod, monster.getProficiency(),
			monster.getAbilityProficiency(INT), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
		
		final String ARC = "Arcana";
		JPanel arcPanel = new JPanel();
		arcPanel.setLayout(new BoxLayout(arcPanel, BoxLayout.X_AXIS));
		JLabel arcana = new JLabel("Arcana: " + signBonus(calcBonus(intMod, monster.getProficiency(),
					monster.getSkillProficienct(ARC), monster.getSkillExpertise(ARC))));
		arcana.setFont(new Font(arcana.getFont().getName(), Font.PLAIN, arcana.getFont().getSize()));
		JCheckBox arcProfBox = new JCheckBox();
		JCheckBox arcExpBox = new JCheckBox();
		arcProfBox.setSelected(monster.getSkillProficienct(ARC));
		arcExpBox.setSelected(monster.getSkillExpertise(ARC));
		
		final String HIS = "History";
		JPanel hisPanel = new JPanel();
		hisPanel.setLayout(new BoxLayout(hisPanel, BoxLayout.X_AXIS));
		JLabel history = new JLabel("History: " + signBonus(calcBonus(intMod, monster.getProficiency(),
					monster.getSkillProficienct(HIS), monster.getSkillExpertise(HIS))));
		history.setFont(new Font(history.getFont().getName(), Font.PLAIN, history.getFont().getSize()));
		JCheckBox hisProfBox = new JCheckBox();
		JCheckBox hisExpBox = new JCheckBox();
		hisProfBox.setSelected(monster.getSkillProficienct(HIS));
		hisExpBox.setSelected(monster.getSkillExpertise(HIS));
		
		final String INV = "Investigation";
		JPanel invPanel = new JPanel();
		invPanel.setLayout(new BoxLayout(invPanel, BoxLayout.X_AXIS));
		JLabel investigation = new JLabel("Investigation: " + signBonus(calcBonus(intMod, monster.getProficiency(),
					monster.getSkillProficienct(INV), monster.getSkillExpertise(INV))));
		investigation.setFont(new Font(investigation.getFont().getName(), Font.PLAIN, investigation.getFont().getSize()));
		JCheckBox invProfBox = new JCheckBox();
		JCheckBox invExpBox = new JCheckBox();
		invProfBox.setSelected(monster.getSkillProficienct(INV));
		invExpBox.setSelected(monster.getSkillExpertise(INV));
		
		final String NAT = "Nature";
		JPanel natPanel = new JPanel();
		natPanel.setLayout(new BoxLayout(natPanel, BoxLayout.X_AXIS));
		JLabel nature = new JLabel("Nature: " + signBonus(calcBonus(intMod, monster.getProficiency(),
					monster.getSkillProficienct(NAT), monster.getSkillExpertise(NAT))));
		nature.setFont(new Font(nature.getFont().getName(), Font.PLAIN, nature.getFont().getSize()));
		JCheckBox natProfBox = new JCheckBox();
		JCheckBox natExpBox = new JCheckBox();
		natProfBox.setSelected(monster.getSkillProficienct(NAT));
		natExpBox.setSelected(monster.getSkillExpertise(NAT));
		
		final String REL = "Religion";
		JPanel relPanel = new JPanel();
		relPanel.setLayout(new BoxLayout(relPanel, BoxLayout.X_AXIS));
		JLabel religion = new JLabel("Religion: " + signBonus(calcBonus(intMod, monster.getProficiency(),
					monster.getSkillProficienct(REL), monster.getSkillExpertise(REL))));
		religion.setFont(new Font(religion.getFont().getName(), Font.PLAIN, religion.getFont().getSize()));
		JCheckBox relProfBox = new JCheckBox();
		JCheckBox relExpBox = new JCheckBox();
		relProfBox.setSelected(monster.getSkillProficienct(REL));
		relExpBox.setSelected(monster.getSkillExpertise(REL));
		
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(INT, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(INT), false)));
				arcana.setText("Arcana: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ARC), monster.getSkillExpertise(ARC))));
				history.setText("History: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HIS), monster.getSkillExpertise(HIS))));
				investigation.setText("Investigation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INV), monster.getSkillExpertise(INV))));
				nature.setText("Nature: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(NAT), monster.getSkillExpertise(NAT))));
				religion.setText("Religion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(REL), monster.getSkillExpertise(REL))));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(INT, true);
					
				else
					monster.setAbilityProficiency(INT, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(INT), false)));
			}
		});
		
		//arcana checkbox listeners
		arcProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(ARC, true);
					
				else {
					monster.setSkillProficiency(ARC, false);
					arcExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				arcana.setText("Arcana: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ARC), monster.getSkillExpertise(ARC))));
			}
		});
		
		arcExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(ARC, true);
					arcProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(ARC, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				arcana.setText("Arcana: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ARC), monster.getSkillExpertise(ARC))));
			}
		});
		
		//history checkbox listeners
		hisProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(HIS, true);
					
				else {
					monster.setSkillProficiency(HIS, false);
					hisExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				history.setText("History: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HIS), monster.getSkillExpertise(HIS))));
			}
		});
		
		hisExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(HIS, true);
					hisProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(HIS, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				history.setText("History: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HIS), monster.getSkillExpertise(HIS))));
			}
		});
		
		//investigation checkbox listeners
		invProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(INV, true);
					
				else {
					monster.setSkillProficiency(INV, false);
					invExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				investigation.setText("Investigation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INV), monster.getSkillExpertise(INV))));
			}
		});
		
		invExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(INV, true);
					invProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(INV, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				investigation.setText("Investigation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INV))));
			}
		});
		
		//nature checkbox listeners
		natProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(NAT, true);
					
				else {
					monster.setSkillProficiency(NAT, false);
					natExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				nature.setText("Nature: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(NAT), monster.getSkillExpertise(NAT))));
			}
		});
		
		natExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(NAT, true);
					natProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(NAT, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				nature.setText("Nature: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(NAT), monster.getSkillExpertise(NAT))));
			}
		});
		
		//religion checkbox listeners
		relProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(REL, true);
					
				else {
					monster.setSkillProficiency(REL, false);
					relExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				religion.setText("Religion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(REL), monster.getSkillExpertise(REL))));
			}
		});
		
		relExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(REL, true);
					relProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(REL, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				religion.setText("Religion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(REL), monster.getSkillExpertise(REL))));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(INT, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(INT), false)));
				arcana.setText("Arcana: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ARC), monster.getSkillExpertise(ARC))));
				history.setText("History: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(HIS), monster.getSkillExpertise(HIS))));
				investigation.setText("Investigation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INV), monster.getSkillExpertise(INV))));
				nature.setText("Nature: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(NAT), monster.getSkillExpertise(NAT))));
				religion.setText("Religion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(REL), monster.getSkillExpertise(REL))));
			}
		});
		
		intPanel.add(inte);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		intPanel.add(score);
		intPanel.add(Box.createRigidArea(VERTICAL_GAP));
		intPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		final String WIS = "WIS";
		int wisScore = monster.getAbilityScore(WIS);
		int wisMod = Math.floorDiv(wisScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(wisScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		String modifier = signBonus(wisMod);
		JLabel mod = new JLabel("(" + modifier + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(WIS));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
			monster.getAbilityProficiency(WIS), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
		
		final String ANI = "Animal Handling";
		JPanel aniPanel = new JPanel();
		aniPanel.setLayout(new BoxLayout(aniPanel, BoxLayout.X_AXIS));
		JLabel animal = new JLabel("Animal Handling: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
					monster.getSkillProficienct(ANI), monster.getSkillExpertise(ANI))));
		animal.setFont(new Font(animal.getFont().getName(), Font.PLAIN, animal.getFont().getSize()));
		JCheckBox aniProfBox = new JCheckBox();
		JCheckBox aniExpBox = new JCheckBox();
		aniProfBox.setSelected(monster.getSkillProficienct(ANI));
		aniExpBox.setSelected(monster.getSkillExpertise(ANI));
		
		final String INS = "Insight";
		JPanel insPanel = new JPanel();
		insPanel.setLayout(new BoxLayout(insPanel, BoxLayout.X_AXIS));
		JLabel insight = new JLabel("Insight: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
					monster.getSkillProficienct(INS), monster.getSkillExpertise(INS))));
		insight.setFont(new Font(insight.getFont().getName(), Font.PLAIN, insight.getFont().getSize()));
		JCheckBox insProfBox = new JCheckBox();
		JCheckBox insExpBox = new JCheckBox();
		insProfBox.setSelected(monster.getSkillProficienct(INS));
		insExpBox.setSelected(monster.getSkillExpertise(INS));
		
		final String MED = "Medicine";
		JPanel medPanel = new JPanel();
		medPanel.setLayout(new BoxLayout(medPanel, BoxLayout.X_AXIS));
		JLabel medicine = new JLabel("Medicine: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
					monster.getSkillProficienct(MED), monster.getSkillExpertise(MED))));
		medicine.setFont(new Font(medicine.getFont().getName(), Font.PLAIN, medicine.getFont().getSize()));
		JCheckBox medProfBox = new JCheckBox();
		JCheckBox medExpBox = new JCheckBox();
		medProfBox.setSelected(monster.getSkillProficienct(MED));
		medExpBox.setSelected(monster.getSkillExpertise(MED));
		
		final String PER = "Perception";
		JPanel perPanel = new JPanel();
		perPanel.setLayout(new BoxLayout(perPanel, BoxLayout.X_AXIS));
		JLabel perception = new JLabel("Perception: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
					monster.getSkillProficienct(PER), monster.getSkillExpertise(PER))));
		perception.setFont(new Font(perception.getFont().getName(), Font.PLAIN, perception.getFont().getSize()));
		JCheckBox perProfBox = new JCheckBox();
		JCheckBox perExpBox = new JCheckBox();
		perProfBox.setSelected(monster.getSkillProficienct(PER));
		perExpBox.setSelected(monster.getSkillExpertise(PER));
		
		final String SUR = "Survival";
		JPanel surPanel = new JPanel();
		surPanel.setLayout(new BoxLayout(surPanel, BoxLayout.X_AXIS));
		JLabel survival = new JLabel("Survival: " + signBonus(calcBonus(wisMod, monster.getProficiency(),
					monster.getSkillProficienct(SUR), monster.getSkillExpertise(SUR))));
		survival.setFont(new Font(survival.getFont().getName(), Font.PLAIN, survival.getFont().getSize()));
		JCheckBox surProfBox = new JCheckBox();
		JCheckBox surExpBox = new JCheckBox();
		surProfBox.setSelected(monster.getSkillProficienct(SUR));
		surExpBox.setSelected(monster.getSkillExpertise(SUR));
		
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(WIS, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(WIS), false)));
				animal.setText("Animal Handling: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ANI), monster.getSkillExpertise(ANI))));
				insight.setText("Insight: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INS), monster.getSkillExpertise(INS))));
				medicine.setText("Medicine: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(MED), monster.getSkillExpertise(MED))));
				perception.setText("Perception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PER), monster.getSkillExpertise(PER))));
				survival.setText("Survival: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(SUR), monster.getSkillExpertise(SUR))));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(WIS, true);
					
				else
					monster.setAbilityProficiency(WIS, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(WIS), false)));
			}
		});
		
		//animal handling checkbox listeners
		aniProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(ANI, true);
					
				else {
					monster.setSkillProficiency(ANI, false);
					aniExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				animal.setText("Animal Handling: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ANI), monster.getSkillExpertise(ANI))));
			}
		});
		
		aniExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(ANI, true);
					aniProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(ANI, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				animal.setText("Animal Handling: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ANI), monster.getSkillExpertise(ANI))));
			}
		});
		
		//insight checkbox listeners
		insProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(INS, true);
					
				else {
					monster.setSkillProficiency(INS, false);
					insExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				insight.setText("Insight: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INS), monster.getSkillExpertise(INS))));
			}
		});
		
		insExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(INS, true);
					insProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(INS, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				insight.setText("Insight: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INS), monster.getSkillExpertise(INS))));
			}
		});
		
		//medicine checkbox listeners
		medProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(MED, true);
					
				else {
					monster.setSkillProficiency(MED, false);
					medExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				medicine.setText("Medicine: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(MED), monster.getSkillExpertise(MED))));
			}
		});
		
		medExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(MED, true);
					medProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(MED, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				medicine.setText("Medicine: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(MED), monster.getSkillExpertise(MED))));
			}
		});
		
		//perception checkbox listeners
		perProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(PER, true);
					
				else {
					monster.setSkillProficiency(PER, false);
					perExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				perception.setText("Perception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PER), monster.getSkillExpertise(PER))));
			}
		});
		
		perExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(PER, true);
					perProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(PER, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				perception.setText("Perception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PER), monster.getSkillExpertise(PER))));
			}
		});
		
		//survival checkbox listeners
		surProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(SUR, true);
					
				else {
					monster.setSkillProficiency(SUR, false);
					surExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				survival.setText("Survival: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(SUR), monster.getSkillExpertise(SUR))));
			}
		});
		
		surExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(SUR, true);
					surProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(SUR, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				survival.setText("Survival: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(SUR), monster.getSkillExpertise(SUR))));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(WIS, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(WIS), false)));
				animal.setText("Animal Handling: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(ANI), monster.getSkillExpertise(ANI))));
				insight.setText("Insight: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INS), monster.getSkillExpertise(INS))));
				medicine.setText("Medicine: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(MED), monster.getSkillExpertise(MED))));
				perception.setText("Perception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PER), monster.getSkillExpertise(PER))));
				survival.setText("Survival: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(SUR), monster.getSkillExpertise(SUR))));
			}
		});
		
		wisPanel.add(wis);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		wisPanel.add(score);
		wisPanel.add(Box.createRigidArea(VERTICAL_GAP));
		wisPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		final String CHA = "CHA";
		int chaScore = monster.getAbilityScore(CHA);
		int chaMod = Math.floorDiv(chaScore - 10, 2);
		
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
		JTextField score = new JTextField(Integer.toString(chaScore));
		score.setMinimumSize(new Dimension(30, 20));
		score.setPreferredSize(new Dimension(30, 20));
		score.setMaximumSize(new Dimension(30, 20));
		score.setHorizontalAlignment(JTextField.CENTER);
		String modifier = signBonus(chaMod);
		JLabel mod = new JLabel("(" + modifier + ")");
		mod.setAlignmentX(Component.CENTER_ALIGNMENT);
		mod.setFont(new Font(mod.getFont().getName(), Font.PLAIN, mod.getFont().getSize()));
		
		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
		JCheckBox saveBox = new JCheckBox();
		saveBox.setSelected(monster.getAbilityProficiency(CHA));
		JLabel save = new JLabel("Saving Throws: " + signBonus(calcBonus(chaMod, monster.getProficiency(),
			monster.getAbilityProficiency(CHA), false)));
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, save.getFont().getSize()));
		
		final String DEC = "Deception";
		JPanel decPanel = new JPanel();
		decPanel.setLayout(new BoxLayout(decPanel, BoxLayout.X_AXIS));
		JLabel deception = new JLabel("Deception: " + signBonus(calcBonus(chaMod, monster.getProficiency(),
					monster.getSkillProficienct(DEC), monster.getSkillExpertise(DEC))));
		deception.setFont(new Font(deception.getFont().getName(), Font.PLAIN, deception.getFont().getSize()));
		JCheckBox decProfBox = new JCheckBox();
		JCheckBox decExpBox = new JCheckBox();
		decProfBox.setSelected(monster.getSkillProficienct(DEC));
		decExpBox.setSelected(monster.getSkillExpertise(DEC));
		
		final String INT = "Intimidation";
		JPanel intPanel = new JPanel();
		intPanel.setLayout(new BoxLayout(intPanel, BoxLayout.X_AXIS));
		JLabel intimidation = new JLabel("Intimidation: " + signBonus(calcBonus(chaMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INT))));
		intimidation.setFont(new Font(intimidation.getFont().getName(), Font.PLAIN, intimidation.getFont().getSize()));
		JCheckBox intProfBox = new JCheckBox();
		JCheckBox intExpBox = new JCheckBox();
		intProfBox.setSelected(monster.getSkillProficienct(INT));
		intExpBox.setSelected(monster.getSkillExpertise(INT));
		
		final String PERF = "Performance";
		JPanel perfPanel = new JPanel();
		perfPanel.setLayout(new BoxLayout(perfPanel, BoxLayout.X_AXIS));
		JLabel performance = new JLabel("Performance: " + signBonus(calcBonus(chaMod, monster.getProficiency(),
					monster.getSkillProficienct(PERF), monster.getSkillExpertise(PERF))));
		performance.setFont(new Font(performance.getFont().getName(), Font.PLAIN, performance.getFont().getSize()));
		JCheckBox perfProfBox = new JCheckBox();
		JCheckBox perfExpBox = new JCheckBox();
		perfProfBox.setSelected(monster.getSkillProficienct(PERF));
		perfExpBox.setSelected(monster.getSkillExpertise(PERF));
		
		final String PERS = "Persuasion";
		JPanel persPanel = new JPanel();
		persPanel.setLayout(new BoxLayout(persPanel, BoxLayout.X_AXIS));
		JLabel persuasion = new JLabel("Persuasion: " + signBonus(calcBonus(chaMod, monster.getProficiency(),
					monster.getSkillProficienct(PERS), monster.getSkillExpertise(PERS))));
		persuasion.setFont(new Font(persuasion.getFont().getName(), Font.PLAIN, persuasion.getFont().getSize()));
		JCheckBox persProfBox = new JCheckBox();
		JCheckBox persExpBox = new JCheckBox();
		persProfBox.setSelected(monster.getSkillProficienct(PERS));
		persExpBox.setSelected(monster.getSkillExpertise(PERS));
		
		//listeners for Score text field
		score.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE 
					|| e.getKeyCode() == KeyEvent.VK_DELETE))
					score.setEditable(true);
					
				else
					score.setEditable(false);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (score.getText().equals(""))
					score.setText("0");
					
				if (score.getText().length() > 1 && score.getText().charAt(0) == '0')
					score.setText(score.getText().substring(1));
					
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(CHA, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CHA), false)));
				deception.setText("Deception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(DEC), monster.getSkillExpertise(DEC))));
				intimidation.setText("Intimidation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INT))));
				performance.setText("Performance: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERF), monster.getSkillExpertise(PERF))));
				persuasion.setText("Persuasion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERS), monster.getSkillExpertise(PERS))));
			}
		});
		
		//saving throw checkbox listener
		saveBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setAbilityProficiency(CHA, true);
					
				else
					monster.setAbilityProficiency(CHA, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CHA), false)));
			}
		});
		
		//deception checkbox listeners
		decProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(DEC, true);
					
				else {
					monster.setSkillProficiency(DEC, false);
					decExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				deception.setText("Deception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(DEC), monster.getSkillExpertise(DEC))));
			}
		});
		
		decExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(DEC, true);
					decProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(DEC, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				deception.setText("Deception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(DEC), monster.getSkillExpertise(DEC))));
			}
		});
		
		//intimidation checkbox listeners
		intProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(INT, true);
					
				else {
					monster.setSkillProficiency(INT, false);
					intExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				intimidation.setText("Intimidation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INT))));
			}
		});
		
		intExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(INT, true);
					intProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(INT, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				intimidation.setText("Intimidation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INT))));
			}
		});
		
		//performance checkbox listeners
		perfProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(PERF, true);
					
				else {
					monster.setSkillProficiency(PERF, false);
					perfExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				performance.setText("Performance: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERF), monster.getSkillExpertise(PERF))));
			}
		});
		
		perfExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(PERF, true);
					perfProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(PERF, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				performance.setText("Performance: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERF), monster.getSkillExpertise(PERF))));
			}
		});
		
		//persuasion checkbox listeners
		persProfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					monster.setSkillProficiency(PERS, true);
					
				else {
					monster.setSkillProficiency(PERS, false);
					persExpBox.setSelected(false);
				}
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				persuasion.setText("Persuasion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERS), monster.getSkillExpertise(PERS))));
			}
		});
		
		persExpBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					monster.setSkillExpertise(PERS, true);
					persProfBox.setSelected(true);
				}
					
				else
					monster.setSkillExpertise(PERS, false);
				
				int scoreMod = Math.floorDiv(Integer.parseInt(score.getText()) - 10, 2);
				persuasion.setText("Persuasion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERS), monster.getSkillExpertise(PERS))));
			}
		});
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(score.getText());
				monster.setAbilityScore(CHA, num);
				int scoreMod = Math.floorDiv(num - 10, 2);
				mod.setText("(" + signBonus(scoreMod) + ")");
				save.setText("Saving Throws: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getAbilityProficiency(CHA), false)));
				deception.setText("Deception: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(DEC), monster.getSkillExpertise(DEC))));
				intimidation.setText("Intimidation: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(INT), monster.getSkillExpertise(INT))));
				performance.setText("Performance: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERF), monster.getSkillExpertise(PERF))));
				persuasion.setText("Persuasion: " + signBonus(calcBonus(scoreMod, monster.getProficiency(),
					monster.getSkillProficienct(PERS), monster.getSkillExpertise(PERS))));
			}
		});
		
		chaPanel.add(cha);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		chaPanel.add(score);
		chaPanel.add(Box.createRigidArea(VERTICAL_GAP));
		chaPanel.add(mod);
		
		savePanel.add(saveBox);
		savePanel.add(save);
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
		challengeLabel.setFont(new Font(challengeLabel.getFont().getName(), Font.BOLD, challengeLabel.getFont().getSize()));
		JTextField challenge = new JTextField(monster.getChallenge());
		challenge.setMaximumSize(new Dimension(30, INNER_HEIGHT));
		JLabel xpLabel = new JLabel("(" + monster.getXP() + " XP)");
		xpLabel.setFont(new Font(xpLabel.getFont().getName(), Font.ITALIC, xpLabel.getFont().getSize()));
		
		DeferredDocumentListener listener = new DeferredDocumentListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monster.setChallenge(challenge.getText());
				xpLabel.setText("(" + monster.getXP() + " XP)");
				proxy.updateMonster(monster); //updates xp values on server for enc builder
			}
		});
		
		challenge.getDocument().addDocumentListener(listener);
		challenge.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listener.start();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				listener.stop();
			}
		});
		
		challengePanel.add(challengeLabel);
		challengePanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		challengePanel.add(challenge);
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
		if (list.isSelectionEmpty()) {
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
	 
	 /**
	  * cals bonus for any skill/saving throw
	  * expertise should always be false for saving throws
	  */
	 private int calcBonus(int mod, int profBonus, boolean proficient, boolean expertise) {
		 if (proficient)
			mod += profBonus;
			
		if (expertise)
			mod += profBonus;
			
		return mod;
	}
	
	/**
	 * stringifies bonus and signs it
	 */
	private String signBonus(int mod) {
		String signed;
		
		if (mod < 0)
			signed = Integer.toString(mod);
			
		else
			signed = "+" + Integer.toString(mod);
			
		return signed;
	}
}
