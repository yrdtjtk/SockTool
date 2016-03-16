package com.tcps.yrd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SockTool {

	private JFrame frame;
	private JTextField tfServerIP;
	private JTextField tfServerPort;
	SockClient client;
	Config cfg;
	boolean bHexRcv = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SockTool window = new SockTool();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SockTool() {
		cfg = new Config();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
//		  InitGlobalFont(new Font("alias", 0, 12));
		  e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cfg.setServerIP(tfServerIP.getText());
				cfg.setServerPort(Integer.parseInt(tfServerPort.getText()));
				cfg.save();
			}
		});
		frame.setBounds(100, 100, 605, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{589, 0};
		gridBagLayout.rowHeights = new int[]{21, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		final JTextArea textPaneRcv = new JTextArea();
		textPaneRcv.setEditable(false);
		scrollPane.setViewportView(textPaneRcv);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		final JTextPane textPaneSend = new JTextPane();
		textPaneSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER && e.isControlDown()){
					client.sendStrToServer(textPaneSend.getText());
				}
//				textPaneRcv.setText(textPaneRcv.getText() + "Press:" + e.getKeyCode() + e.isControlDown() + "\r\n");
				
//				int lr = textPaneSend.getText().length();
//				System.out.println("lr:" + lr);
//				textPaneSend.setCaretPosition(lr);
			}
		});
		scrollPane_1.setViewportView(textPaneSend);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		frame.getContentPane().add(panel, gbc_panel);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		panel.add(lblServerIp);
		
		tfServerIP = new JTextField();
		panel.add(tfServerIP);
		tfServerIP.setColumns(10);
		tfServerIP.setText(cfg.getServerIP());
		
		JLabel lblServerPort = new JLabel("Server Port:");
		panel.add(lblServerPort);
		
		tfServerPort = new JTextField();
		panel.add(tfServerPort);
		tfServerPort.setColumns(10);
		tfServerPort.setText(cfg.getServerPort());
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(client != null && client.isConnected()){
					client.disconnectFromServer();
					btnConnect.setText("Connect");
				}
				else{
					client = new SockClient();
					client.connectToServer(tfServerIP.getText(), Integer.parseInt(tfServerPort.getText()));
					if(client.isConnected()){
						textPaneRcv.setText(textPaneRcv.getText() + "Connected to server " + tfServerIP.getText() + ":" + tfServerPort.getText() + ".\r\n");
						btnConnect.setText("Disconnect");
						new Thread(new Runnable() {
							char[] strBuf = new char[1024];
							byte[] buf = new byte[1024];
							int ret = 0;
							@Override
							public void run() {
								// TODO Auto-generated method stub
								while(true){
									if(bHexRcv){
										ret = client.recvBufFromServer(buf);
									}
									else{
										ret = client.recvStrFromServer(strBuf);
									}
									if(ret > 0){
										String rcvStr = null;
										if(bHexRcv){
											rcvStr = Buff.toHexStr(buf,ret);
										}
										else{
											rcvStr = new String(Arrays.copyOf(strBuf, ret));
										}
//										textPaneRcv.setText(textPaneRcv.getText() + rcvStr);
										textPaneRcv.append(rcvStr);
										if(true){
											String s = textPaneRcv.getText();
											int lr = s.length();
//											int numCrLf = s.length();
											System.out.println("lr="+lr);
//											textPaneRcv.setCaretPosition(lr);
											try{												
												textPaneRcv.setCaretPosition(lr);
											}
											catch(Exception e){
												e.printStackTrace();
											}
										}
									}
									else if(ret < 0){
										break;
									}
								}
								textPaneRcv.setText(textPaneRcv.getText() + "Disconnected from server.\r\n");
								btnConnect.setText("Connect");							
							}
						}).start();
					}
				}
			}
		});
		panel.add(btnConnect);
		
		JButton btnClearrcvtxt = new JButton("ClearRcvTxt");
		btnClearrcvtxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneRcv.setText("");
			}
		});
		panel.add(btnClearrcvtxt);
		
		final JCheckBox chckbxHexrcv = new JCheckBox("HexRcv");
		chckbxHexrcv.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				bHexRcv = chckbxHexrcv.isSelected();
			}
		});
		panel.add(chckbxHexrcv);
	}

}
