package com.danielkueffer.wator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerListModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AppGUI implements ActionListener, ChangeListener, MouseListener, ComponentListener {
	
	private static final int MENU_WIDTH = 220;
	private static final int TOTAL_HEIGHT = 780;
	private static final int TOTAL_WIDTH = 820;
	
	/* Speed slider */
	private static final int FPS_MIN = 1;
	private static final int FPS_MAX = 100;
	private static final int FPS_INIT = 80;
	
	/* Colors */
	private static final Color FISH_COLOR = new Color(0x0065FC);
	private static final Color SHARK_COLOR = new Color(0xFC1100);
	private static final Color INFO_BUTTON_COLOR = new Color(0x3865a9);
	
	/* File paths */
	private static final String PATH = "com/danielkueffer/wator/";
	private static final String WATOR_ICON_PATH = "resources/images/wator-icon.png";
	private static final String BOTTOM_WATOR_PATH = "resources/images/bottom-wator.png";
	private static final String QUESTION_SMALL_IMG_PATH = "resources/images/question-small.png";
	private static final String QUESTION_BIG_IMG_PATH = "resources/images/question-big.png";
	private static final String HELP_CONTENT_HTML_PATH = "resources/html/helpcontent.html";
	
	private Timer timer;
	private int timerSpeed = 80;
	
	private AppGUI ag;
	
	private ClassLoader cl;
	
	private Planet pl;
	
	private JFrame frame;
	
	private CustomPanel cp;
	
	private AnimalChart ac;

	private JPanel centerPanel;
	private JPanel menu;
	private JPanel finishPanelOuter;
	private JPanel animalCountPanel;
	
	private JScrollPane centerScrollPane;
	private JScrollPane menuScrollPane;
	private JScrollPane bottomScrollPane;
	
	private JDialog helpDialog;
	private JDialog errorDialog;
	
	private JButton appToggle;
	private JButton appStep;
	private JButton appReset;
	private JButton appFinish;
	private JButton infoButton;
	private JButton helpCloseButton;
	
	private JLabel populationTitle;
	private JLabel breedLabel;
	private JLabel speedLabel;
	private JLabel fishCountLabel;
	private JLabel sharkCountLabel;
	private JLabel generationCountLabel;
	
	private JSpinner fishPopulationSpinner;
	private JSpinner sharkPopulationSpinner;
	private JSpinner fishBreedSpinner;
	private JSpinner sharkBreedSpinner;
	private JSpinner sharkStarvationSpinner;
	
	private JSlider speedSlider;
	
	private Font infoButtonFont;
	
	private boolean isRunning = false;
	
	private String[] fishPercent = new String[50];
	private String[] sharkPercent = new String[50];
	
	private String[] fishBreedCount = new String[10];
	private String[] sharkBreedCount = new String[10];
	
	private String[] sharkStarvationCount = new String[10];
	
	public AppGUI(CustomPanel cp, Planet pl) {
		this.cp = cp;
		this.pl = pl;
		this.ag = this;
		this.cl = this.getClass().getClassLoader();
	}
	
	/**
	 * Initialize GUI
	 */
	public void initGUI() {
		frame = new JFrame("Planet of Wator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.centerLayout();
		this.menuLayout();
		this.bottomLayout();
		
		Container con = frame.getContentPane();
		
		GridBagLayout gbl = new GridBagLayout();
		frame.setLayout(gbl);
		
		this.addGridComponent(con, gbl, menuScrollPane, 0, 1, 1, 2, 0, 1.0, 0, 0, false);
		this.addGridComponent(con, gbl, centerScrollPane, 1, 1, 1, 1, 1.0, 1.0, 0, 0, false);
		this.addGridComponent(con, gbl, bottomScrollPane, 1, 2, 1, 1, 1.0, 0, 0, 0, true);
		
		Image frameIcon = new ImageIcon(this.cl.getResource(PATH + WATOR_ICON_PATH)).getImage();
		frame.setIconImage(frameIcon);
		frame.setLocation(300, 300);
		frame.setResizable(true);
		frame.pack();
		frame.setSize(TOTAL_WIDTH, TOTAL_HEIGHT);
		frame.setMinimumSize(new Dimension(500, 400));
		frame.addComponentListener(this);
		frame.setVisible(true);
	}
	
	/**
	 * Make GridBagConstraints and add component to the container
	 * @param con
	 * @param gbl
	 * @param c
	 * @param x
	 * @param y
	 * @param gridwidth
	 * @param gridheight
	 * @param weightx
	 * @param weighty
	 * @param ipadx
	 * @param ipady
	 */
	public void addGridComponent(Container con, GridBagLayout gbl, Component c, int x, int y, int gridwidth, int gridheight, double weightx, double weighty, int ipadx, int ipady, boolean orient) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		if (orient) {
			gbc.fill = GridBagConstraints.LINE_END;
			gbc.anchor = GridBagConstraints.WEST;
		}
		else {
			gbc.fill = GridBagConstraints.BOTH;
		}
		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.ipadx = ipadx;
		gbc.ipady = ipady;
		
		gbl.setConstraints(c, gbc);
		con.add(c);
	}
	
	/**
	 * Menu layout
	 */
	public void menuLayout() {
		
		// Buttons
		appToggle = new JButton("Start");
		appToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, appToggle.getMaximumSize().height));
		appToggle.addActionListener(this);
		appToggle.setFocusPainted(false);
		
		appStep = new JButton("Step");
		appStep.setMaximumSize(new Dimension(Integer.MAX_VALUE, appStep.getMaximumSize().height));
		appStep.addActionListener(this);
		appStep.setEnabled(true);
		appStep.setFocusPainted(false);
		
		appReset = new JButton("Reset");
		appReset.setMaximumSize(new Dimension(Integer.MAX_VALUE, appReset.getMaximumSize().height));
		appReset.setMinimumSize(new Dimension(Integer.MAX_VALUE, appReset.getMaximumSize().height));
		appReset.addActionListener(this);
		appReset.setEnabled(false);
		appReset.setFocusPainted(false);
		
		// Population
		JSeparator populationSeparator = new JSeparator(JSeparator.HORIZONTAL);
		populationSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		
		populationTitle = new JLabel("Population");
		populationTitle.setText(populationTitle.getText().toUpperCase());
		populationTitle.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		
		this.fillAnimalPercent(true);
		SpinnerListModel fishPercentModel = new SpinnerListModel(this.fishPercent);
		
		JPanel fishPopulationPanel = new JPanel();
		fishPopulationPanel.setLayout(new BoxLayout(fishPopulationPanel, BoxLayout.LINE_AXIS));
		fishPopulationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		fishPopulationSpinner = this.addLabeledSpinner(fishPopulationPanel, "Fish in %: ", fishPercentModel, 69);
		fishPopulationSpinner.setMaximumSize(new Dimension(100,22));
		fishPopulationSpinner.setValue(fishPercent[3]);
		fishPopulationSpinner.addChangeListener(this);
		
		this.fillAnimalPercent(false);
		SpinnerListModel sharkPercentModel = new SpinnerListModel(this.sharkPercent);
		
		JPanel sharkPopulationPanel = new JPanel();
		sharkPopulationPanel.setLayout(new BoxLayout(sharkPopulationPanel, BoxLayout.LINE_AXIS));
		sharkPopulationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		sharkPopulationPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
		
		sharkPopulationSpinner = this.addLabeledSpinner(sharkPopulationPanel, "Sharks in %: ", sharkPercentModel, 51);
		sharkPopulationSpinner.setMaximumSize(new Dimension(100,22));
		sharkPopulationSpinner.setValue(sharkPercent[1]);
		sharkPopulationSpinner.addChangeListener(this);
		
		// Breed time
		JSeparator breedSeparator = new JSeparator(JSeparator.HORIZONTAL);
		breedSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		
		breedLabel = new JLabel("Reproduction (Cycles 1-10)");
		breedLabel.setText(breedLabel.getText().toUpperCase());
		breedLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		
		this.fillAnimalBreed(true);
		SpinnerListModel fishBreedModel = new SpinnerListModel(this.fishBreedCount);
		
		JPanel fishBreedPanel = new JPanel();
		fishBreedPanel.setLayout(new BoxLayout(fishBreedPanel, BoxLayout.LINE_AXIS));
		fishBreedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		fishBreedSpinner = this.addLabeledSpinner(fishBreedPanel, "Fish: ", fishBreedModel, 94);
		fishBreedSpinner.setMaximumSize(new Dimension(100,22));
		fishBreedSpinner.setValue(fishBreedCount[1]);
		fishBreedSpinner.addChangeListener(this);
		
		this.fillAnimalBreed(false);
		SpinnerListModel sharkBreedModel = new SpinnerListModel(this.sharkBreedCount);
		
		JPanel sharkBreedPanel = new JPanel();
		sharkBreedPanel.setLayout(new BoxLayout(sharkBreedPanel, BoxLayout.LINE_AXIS));
		sharkBreedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		sharkBreedSpinner = this.addLabeledSpinner(sharkBreedPanel, "Sharks: ", sharkBreedModel, 76);
		sharkBreedSpinner.setMaximumSize(new Dimension(100,22));
		sharkBreedSpinner.setValue(sharkBreedCount[2]);
		sharkBreedSpinner.addChangeListener(this);
		
		// Starvation time
		this.fillStarvationTime();
		SpinnerListModel starvationModel = new SpinnerListModel(this.sharkStarvationCount);
		
		JPanel starvationPanel = new JPanel();
		starvationPanel.setLayout(new BoxLayout(starvationPanel, BoxLayout.LINE_AXIS));
		starvationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		starvationPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
		
		sharkStarvationSpinner = this.addLabeledSpinner(starvationPanel, "Shark Starve Time: ", starvationModel, 12);
		sharkStarvationSpinner.setMaximumSize(new Dimension(100,22));
		sharkStarvationSpinner.setValue(sharkStarvationCount[3]);
		sharkStarvationSpinner.addChangeListener(this);
		
		// Speed control
		JSeparator speedSeparator = new JSeparator(JSeparator.HORIZONTAL);
		speedSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		
		speedLabel = new JLabel("Speed");
		speedLabel.setText(speedLabel.getText().toUpperCase());
		speedLabel.setBorder(BorderFactory.createEmptyBorder(0,0,12,0));
		
		speedSlider = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
		speedSlider.setValue(FPS_INIT);
		speedSlider.addChangeListener(this);
		speedSlider.setMajorTickSpacing(1);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		
		Font speedFont = new Font("Arial", Font.PLAIN, 10);
		speedSlider.setFont(speedFont);
		
		// Animal Count
		this.animalCountPanel();
		
		JSeparator animalSeparator = new JSeparator(JSeparator.HORIZONTAL);
		animalSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		
		// Wator logo
		JPanel watorLogoPanel = new JPanel();
		watorLogoPanel.setLayout(new BoxLayout(watorLogoPanel, BoxLayout.LINE_AXIS));
		watorLogoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 57));
		watorLogoPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
		
		JSeparator logoSeparator = new JSeparator(JSeparator.HORIZONTAL);
		logoSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
	
		ImageIcon watorTitleIcon = new ImageIcon(cl.getResource(PATH + BOTTOM_WATOR_PATH));
		JLabel watorTitle = new JLabel(watorTitleIcon);
		watorTitle.setSize(MENU_WIDTH, 37);
		
		watorLogoPanel.add(watorTitle);
				
		// Menu panel
		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
		
		menu.add(appToggle);
		menu.add(Box.createRigidArea(new Dimension(0, 10)));
		menu.add(appStep);
		menu.add(Box.createRigidArea(new Dimension(0, 10)));
		menu.add(appReset);
		menu.add(Box.createRigidArea(new Dimension(0, 20)));
		menu.add(populationSeparator);
		menu.add(populationTitle);
		menu.add(fishPopulationPanel);
		menu.add(sharkPopulationPanel);
		menu.add(breedSeparator);
		menu.add(breedLabel);
		menu.add(fishBreedPanel);
		menu.add(sharkBreedPanel);
		menu.add(starvationPanel);
		menu.add(speedSeparator);
		menu.add(speedLabel);
		menu.add(speedSlider);
		menu.add(Box.createRigidArea(new Dimension(0, 20)));
		menu.add(animalSeparator);
		menu.add(animalCountPanel);
		menu.add(logoSeparator);
		menu.add(watorLogoPanel);
		
		menu.setPreferredSize(new Dimension(MENU_WIDTH, 730));
		menu.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		
		menuScrollPane = new JScrollPane(menu, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScrollPane.setPreferredSize(new Dimension(250, 490));
		menuScrollPane.setMinimumSize(new Dimension(250, 200));
		menuScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	/**
	 * Center layout
	 */
	public void centerLayout() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		centerPanel.add(this.cp);
		centerPanel.setOpaque(true);
		centerPanel.setBackground(new Color(0xFAFAFA));
		centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		centerScrollPane = new JScrollPane(centerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerScrollPane.setPreferredSize(new Dimension(490, 490));
		centerScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	/**
	 * Animal Count
	 */
	public void animalCountPanel() {
		animalCountPanel = new JPanel();
		animalCountPanel.setLayout(new BoxLayout(animalCountPanel, BoxLayout.PAGE_AXIS));
		animalCountPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		animalCountPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
		
		Font colorFont = new Font("Arial", Font.PLAIN, 10);
		
		// Fish
		JPanel fishCountPanel = new JPanel();
		fishCountPanel.setLayout(new BoxLayout(fishCountPanel, BoxLayout.LINE_AXIS));
		fishCountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		JLabel fishColorLabel = new JLabel("na");
		fishColorLabel.setForeground(FISH_COLOR);
		fishColorLabel.setBackground(FISH_COLOR);
		fishColorLabel.setOpaque(true);
		fishColorLabel.setSize(new Dimension(15, 25));
		fishColorLabel.setFont(colorFont);
		
		JLabel fishLabel = new JLabel("Fish count:");
		fishCountLabel = new JLabel(pl.getFishCount() + "");
		
		fishCountPanel.add(fishColorLabel);
		fishCountPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		fishCountPanel.add(fishLabel);
		fishCountPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		fishCountPanel.add(fishCountLabel);
		
		// Shark
		JPanel sharkCountPanel = new JPanel();
		sharkCountPanel.setLayout(new BoxLayout(sharkCountPanel, BoxLayout.LINE_AXIS));
		sharkCountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		JLabel sharkColorLabel = new JLabel("na");
		sharkColorLabel.setForeground(SHARK_COLOR);
		sharkColorLabel.setBackground(SHARK_COLOR);
		sharkColorLabel.setOpaque(true);
		sharkColorLabel.setSize(new Dimension(15, 20));
		sharkColorLabel.setFont(colorFont);
		
		JLabel sharkLabel = new JLabel("Shark count:");
		sharkCountLabel = new JLabel(pl.getSharkCount() + "");
		
		sharkCountPanel.add(sharkColorLabel);
		sharkCountPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		sharkCountPanel.add(sharkLabel);
		sharkCountPanel.add(Box.createRigidArea(new Dimension(19, 0)));
		sharkCountPanel.add(sharkCountLabel);
		
		// Generation
		JPanel generationCountPanel = new JPanel();
		generationCountPanel.setLayout(new BoxLayout(generationCountPanel, BoxLayout.LINE_AXIS));
		generationCountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		Font generationFont = new Font("Arial", Font.BOLD, 17);
		
		JLabel generationSignLabel = new JLabel("#");
		generationSignLabel.setFont(generationFont);
		
		JLabel generationLabel = new JLabel("Generation:");
		generationCountLabel = new JLabel(pl.getGeneration() + "");
		
		generationCountPanel.add(generationSignLabel);
		generationCountPanel.add(Box.createRigidArea(new Dimension(13, 0)));
		generationCountPanel.add(generationLabel);
		generationCountPanel.add(Box.createRigidArea(new Dimension(26, 0)));
		generationCountPanel.add(generationCountLabel);
		
		JSeparator animalCountSeparator = new JSeparator(JSeparator.HORIZONTAL);
		animalCountSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		
		// Info
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
		infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		ImageIcon infoLabelIcon = new ImageIcon(cl.getResource(PATH + QUESTION_SMALL_IMG_PATH));
		JLabel infoLabel = new JLabel(infoLabelIcon);
		
		infoButton = new JButton("Manual");
		infoButton.setForeground(INFO_BUTTON_COLOR);
		infoButton.setOpaque(false);
		infoButton.setContentAreaFilled(false);
		infoButton.setBorderPainted(false);
		infoButton.setBorder(null);
		infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		infoButton.addMouseListener(this);
		infoButton.setFocusPainted(false);
		infoButton.setMaximumSize(new Dimension(75, 20));
		
		infoPanel.add(infoLabel);
		infoPanel.add(infoButton);
		
		animalCountPanel.add(fishCountPanel);
		animalCountPanel.add(sharkCountPanel);
		animalCountPanel.add(generationCountPanel);
		animalCountPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		animalCountPanel.add(animalCountSeparator);
		animalCountPanel.add(infoPanel);
	}
	
	/**
	 * Bottom layout
	 */
	public void bottomLayout() {
		
		this.ac = new AnimalChart("Population", pl);
		
		JPanel chartPanelOuter = new JPanel();
		chartPanelOuter.setLayout(new BoxLayout(chartPanelOuter, BoxLayout.LINE_AXIS));
		chartPanelOuter.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
		chartPanelOuter.add(this.ac.getChartPanel());
		chartPanelOuter.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		bottomScrollPane = new JScrollPane(chartPanelOuter, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		bottomScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 190));
		bottomScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 190));
		bottomScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
		bottomScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	/**
	 * Update shark and fish population
	 */
	public void updateAnimalCount() {
		this.fishCountLabel.setText(pl.getFishCount() + "");
		this.sharkCountLabel.setText(pl.getSharkCount() + "");
		this.generationCountLabel.setText(pl.getGeneration() + "");
	}
	
	/**
	 * Add JSpinner with label
	 * @param c
	 * @param label
	 * @param model
	 * @return
	 */
	private JSpinner addLabeledSpinner(Container c, String label, SpinnerListModel model, int gap) {
		JLabel l = new JLabel(label);
		c.add(l);
		
		c.add(Box.createRigidArea(new Dimension(gap,0)));
		
		JSpinner spinner = new JSpinner(model);
		l.setLabelFor(spinner);
		c.add(spinner);
		
		return spinner;
	}
	
	/**
	 * Add finish message if a kind of animal is died out
	 */
	private void showFinishDialog() {
		
		JPanel finishPanel = new JPanel();
		finishPanel.setLayout(new BoxLayout(finishPanel, BoxLayout.PAGE_AXIS));
		finishPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		finishPanelOuter = new JPanel();
		finishPanelOuter.setOpaque(false);
		finishPanelOuter.setBounds(0, 0, 100, 100);
		
		JLabel jl = new JLabel();
		
		jl.setText("Fische sind ausgestorben!");
		
		if (pl.getSharkCount() == 0) {
			jl.setText("Haie sind ausgestorben!");
		}
		
		appFinish = new JButton("Reset");
		appFinish.setMaximumSize(new Dimension(Integer.MAX_VALUE, appFinish.getMaximumSize().height));
		appFinish.setMinimumSize(new Dimension(Integer.MAX_VALUE, appFinish.getMaximumSize().height));
		appFinish.addMouseListener(this);
		appFinish.setFocusPainted(false);
		
		finishPanel.add(jl);
		finishPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		finishPanel.add(appFinish);
		
		finishPanelOuter.add(finishPanel);
	
		errorDialog = new JDialog(frame, true);
		errorDialog.setContentPane(finishPanelOuter);
		errorDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		errorDialog.setSize(200,160);
		errorDialog.setMinimumSize(new Dimension(200, 160));
	
		errorDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				resetApp();
				errorDialog.setVisible(false);
			}
		});
	
		errorDialog.setLocation(frame.getLocationOnScreen().x + 200, frame.getLocationOnScreen().y + 150);
		errorDialog.setVisible(true);
	}
	
	/**
	 * Show the manual dialog
	 */
	private void showManualDialog() {
		
		// Title
		JPanel dialogTitlePanel = new JPanel();
		dialogTitlePanel.setLayout(new BoxLayout(dialogTitlePanel, BoxLayout.LINE_AXIS));
		dialogTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		dialogTitlePanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
		dialogTitlePanel.setOpaque(false);
		
		ImageIcon infoDialogIcon = new ImageIcon(cl.getResource(PATH + QUESTION_BIG_IMG_PATH));
		JLabel infoDialogLabel = new JLabel(infoDialogIcon);
		
		Font dialogTitleFont = new Font("Arial", Font.BOLD, 20);
		JLabel infoDialogTitle = new JLabel("Manual");
		infoDialogTitle.setFont(dialogTitleFont);
		
		dialogTitlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		dialogTitlePanel.add(infoDialogLabel);
		dialogTitlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		dialogTitlePanel.add(infoDialogTitle);
		
		// Content
		JPanel dialogContentPanel = new JPanel();
		dialogContentPanel.setLayout(new BoxLayout(dialogContentPanel, BoxLayout.LINE_AXIS));
		dialogContentPanel.setOpaque(false);
		
		JEditorPane pane = new JEditorPane();
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.setMinimumSize(new Dimension(100, 100));
		pane.setSize(450, 30);
		pane.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));
		
		try {
			pane.setPage(cl.getResource(PATH + HELP_CONTENT_HTML_PATH));
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		pane.addHyperlinkListener(new HyperlinkListener() {

			public void hyperlinkUpdate(HyperlinkEvent evt) {
				
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						ag.open(evt.getURL().toURI());
					} 
					catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		JScrollPane editorScrollPane = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editorScrollPane.setPreferredSize(new Dimension(250, 145));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		
		dialogContentPanel.add(editorScrollPane);
		
		// Close Panel
		JPanel dialogClosePanel = new JPanel();
		dialogClosePanel.setLayout(new BoxLayout(dialogClosePanel, BoxLayout.LINE_AXIS));
		
		helpCloseButton = new JButton("Close");
		helpCloseButton.addMouseListener(this);
		helpCloseButton.setFocusable(false);
		
		dialogClosePanel.add(helpCloseButton);
		
		// Help panel
		JPanel helpPanel = new JPanel();
		helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.PAGE_AXIS));
		helpPanel.add(dialogTitlePanel);
		helpPanel.add(dialogContentPanel);
		helpPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		helpPanel.add(dialogClosePanel);
		helpPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		
		Image dialogIcon = new ImageIcon(this.cl.getResource(PATH + WATOR_ICON_PATH)).getImage();
		
		helpDialog = new JDialog(frame, true);
		helpDialog.add(helpPanel);
		helpDialog.setSize(500,500);
		helpDialog.setMinimumSize(new Dimension(450, 350));
		helpDialog.setIconImage(dialogIcon);
		helpDialog.setLocation(frame.getLocationOnScreen().x + 200, frame.getLocationOnScreen().y + 150);
		helpDialog.setTitle("Manual");
		
		if (! helpDialog.isVisible()) {
			helpDialog.setVisible(true);
		}
	}
	
	/**
	 * Fill animal percents for JSpinner model
	 */
	private void fillAnimalPercent(boolean isFishPercent) {
		for(int i=0; i<50; i++) {
			int k=i;
			k++;
			
			if (isFishPercent) {
				this.fishPercent[i] = k + "";
			}
			else {
				this.sharkPercent[i] = k + "";
			}
		}
	}
	
	/**
	 * Fill animal breed count for JSpinner model
	 * @param isFishBreed
	 */
	private void fillAnimalBreed(boolean isFishBreed) {
		for(int i=0; i<10; i++) {
			int k=i;
			k++;
			
			if (isFishBreed) {
				this.fishBreedCount[i] = k + "";
			}
			else {
				this.sharkBreedCount[i] = k + "";
			}
		}
	}
	
	/**
	 * Fill starvation time for JSpinner model
	 */
	private void fillStarvationTime() {
		for(int i=0; i<10; i++) {
			int k=i;
			k++;
			
			this.sharkStarvationCount[i] = k + "";
		}
	}
	
	/**
	 * Run the timer
	 */
	public void initTimer() {
		timer = new Timer(this.timerSpeed, this);
		timer.setActionCommand("step");
	}
	
	/**
	 * Start timer
	 */
	public void timerStart() {
		this.timer.start();
		this.isRunning = true;
		
		this.appToggle.setText("Stop");
		this.appStep.setEnabled(false);
		this.appReset.setEnabled(false);
	}
	
	/**
	 * Stop timer
	 */
	public void timerStop() {
		this.timer.stop();
		this.isRunning = false;
		
		this.appToggle.setText("Start");
		this.appStep.setEnabled(true);
		this.appReset.setEnabled(true);
	}
	
	/**
	 * Calculate timer speed
	 * @param speed
	 */
	private void calculateTimerSpeed(int speed) {
		
		int maxVal = 400;
		
		Double perc = ((double) speed / 100) * maxVal;
		
		int newSpeed = ((perc.intValue()) - 400) * -1;
		
		if (newSpeed == 0) {
			newSpeed = 1;
		}
		
		this.timerSpeed = newSpeed;
	}
	
	/**
	 * Reset controls
	 */
	private void resetControls() {
		this.enableSpinnners();
		
		speedSlider.setValue(FPS_INIT);
		fishPopulationSpinner.setValue(fishPercent[3]);
		sharkPopulationSpinner.setValue(sharkPercent[1]);
		
		fishBreedSpinner.setValue(fishBreedCount[1]);
		sharkBreedSpinner.setValue(sharkBreedCount[2]);
		
		sharkStarvationSpinner.setValue(sharkStarvationCount[3]);
	}
	
	/**
	 * Disable menu controls
	 */
	private void disableSpinners() {
		fishPopulationSpinner.setEnabled(false);
		sharkPopulationSpinner.setEnabled(false);
		fishBreedSpinner.setEnabled(false);
		sharkBreedSpinner.setEnabled(false);
		sharkStarvationSpinner.setEnabled(false);
		speedSlider.setEnabled(false);
	}
	
	/**
	 * Enable menu controls
	 */
	private void enableSpinnners() {
		fishPopulationSpinner.setEnabled(true);
		sharkPopulationSpinner.setEnabled(true);
		fishBreedSpinner.setEnabled(true);
		sharkBreedSpinner.setEnabled(true);
		sharkStarvationSpinner.setEnabled(true);
		speedSlider.setEnabled(true);
	}
	
	/**
	 * Calculate percent of new animals
	 * @param percent
	 * @param isFish
	 */
	private int calcNewAnimals(int percent) {
		int perc = percent*2;
		int newCount = (pl.getMaxCells() / 100) * perc;
		
		return newCount;
	}
	
	/**
	 * Update animal population
	 */
	private void updatePopulation() {
		int newFishPop = Integer.valueOf(fishPopulationSpinner.getValue().toString()).intValue();
		int newFishCount = this.calcNewAnimals(newFishPop);
		
		pl.setUpdNumberOfFish(newFishCount);
		pl.resetFishes();
		pl.addFishes();
		
		int newSharkPop = Integer.valueOf(sharkPopulationSpinner.getValue().toString()).intValue();
		int newSharkCount = this.calcNewAnimals(newSharkPop);
		
		pl.setUpdNumberOfShark(newSharkCount);
		pl.resetSharks();
		pl.addSharks();
	}
	
	/**
	 * Reset GUI and Application
	 */
	public void resetApp() {
		pl.resetPlanet();
		this.resetControls();
		
		this.updateAnimalCount();
		ac.resetChart();
		
		this.isRunning = false;
		this.appToggle.setEnabled(true);
		this.appReset.setEnabled(false);
	}
	
	/**
	 * Open uri in browser
	 * @param uri
	 */
	private void open(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Perform action
	 */
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		
		if (cmd.equals("step")) {
			pl.step();
			ac.chartStep();
			
			if (pl.getFishCount() == 0 || pl.getSharkCount() == 0) {
				this.timer.stop();
				
				this.appToggle.setEnabled(false);
				this.disableSpinners();
				
				this.showFinishDialog();
			}
			
			this.updateAnimalCount();
		}
		
		if (evt.getSource() == appToggle) {
			
			if (isRunning) {
				this.timerStop();
			}
			else {
				this.timerStart();
			}
		}
		
		if (evt.getSource() == appStep) {
			pl.step();
			ac.chartStep();
			
			this.updateAnimalCount();
			
			this.appReset.setEnabled(true);
		}
		
		if (evt.getSource() == appReset) {
			this.resetApp();
		}
	}

	/**
	 * Perform change
	 */
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == speedSlider) {
			
			boolean tContinue = false;
			
			if (this.timer.isRunning()) {
				tContinue = true;
			}
			
			this.timerStop();
			this.calculateTimerSpeed(speedSlider.getValue());
			this.initTimer();
			
			if (tContinue) {
				this.timerStart();
			}
		}
		
		if (evt.getSource() == fishPopulationSpinner) {
			this.timerStop();
			
			this.updatePopulation();
			this.fishCountLabel.setText(pl.getFishCount() + "");
		}
		
		if (evt.getSource() == sharkPopulationSpinner) {
			this.timerStop();
			
			this.updatePopulation();
			this.sharkCountLabel.setText(pl.getSharkCount() + "");
		}
		
		if (evt.getSource() == fishBreedSpinner) {
			this.timerStop();
			
			int newFishBreed = Integer.valueOf(fishBreedSpinner.getValue().toString()).intValue();
			pl.setUpdFishBreed(newFishBreed);
		}
		
		if (evt.getSource() == sharkBreedSpinner) {
			this.timerStop();
			
			int newSharkBreed = Integer.valueOf(sharkBreedSpinner.getValue().toString()).intValue();
			pl.setUpdSharkBreed(newSharkBreed);
		}
		
		if (evt.getSource() == sharkStarvationSpinner) {
			this.timerStop();
			
			int newStarvationTime = Integer.valueOf(sharkStarvationSpinner.getValue().toString()).intValue();
			pl.setUpdStarvationTime(newStarvationTime);
		}
	}
	
	/**
	 * Mouse entered
	 */
	@SuppressWarnings("unchecked")
	public void mouseEntered(MouseEvent evt) {
		if (evt.getSource() == infoButton) {
			this.infoButtonFont = infoButton.getFont();
			
			@SuppressWarnings("rawtypes")
			Map infoAttributes = infoButton.getFont().getAttributes();
			infoAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			infoButton.setFont(this.infoButtonFont.deriveFont(infoAttributes));
		}
	}
	
	public void mouseExited(MouseEvent evt) {
		if (evt.getSource() == infoButton) {
			infoButton.setFont(this.infoButtonFont);
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getSource() == infoButton) {
			this.showManualDialog();
		}
		
		if (evt.getSource() == helpCloseButton) {
			this.helpDialog.setVisible(false);
		}
		
		if (evt.getSource() == appFinish) {
			this.resetApp();
			
			this.errorDialog.setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	public void componentResized(ComponentEvent evt) {
		if (evt.getSource() == frame) {
			int centerWidth = this.centerPanel.getWidth();
			int centerHeight = this.centerPanel.getHeight();
		
			int newRect;
			
			if (centerWidth > centerHeight) {
				newRect = centerHeight;
			}
			else {
				newRect = centerWidth;
			}
			
			int newCellWidth;
			
			if (newRect > 520) {
				Double newCellWidthDouble = Math.floor(newRect / pl.getPlanetWidth());
				newCellWidth = newCellWidthDouble.intValue();
				
				pl.setCustomCellSize(true);
				pl.setUpdCellWidth(newCellWidth);
				pl.setUpdCellHeight(newCellWidth);
			}
			else {
				pl.setCustomCellSize(false);
				newCellWidth = pl.getCellWidth();
			}
			
			this.cp.setSize(new Dimension(pl.getPlanetWidth() * newCellWidth, pl.getPlanetHeight() * newCellWidth));
			this.cp.repaint();
			
			this.ac.getChartPanel().setPreferredSize(new Dimension(pl.getPlanetWidth() * newCellWidth, 150));
			this.ac.getChartPanel().setMaximumSize(new Dimension(pl.getPlanetWidth() * newCellWidth, 150));
			this.ac.getChartPanel().setMinimumSize(new Dimension(pl.getPlanetWidth() * newCellWidth, 150));
			this.ac.getChartPanel().revalidate();
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		
	}
}