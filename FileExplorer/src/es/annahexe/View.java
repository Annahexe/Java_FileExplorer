package es.annahexe;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;

/**
 * Swing UI for the file explorer/editor, made with WindowBuilder.
 * <p>
 * Exposes getters for UI widgets so {@link Controller} can register listeners
 * and manipulate state (enable/disable, set text, etc.).
 * </p>
 * @author annahexe
 */
public class View {

	private JFrame frame;
	private JTextField textFieldSearch;
	private JTextField textFieldReplace;

	private JButton btnSearchText, btnReplaceText, btnSave, btnNewFolder, btnNewFile, btnOpenPath, btnShowFile, btnRenameFile, btnCopyFile, btnDeleteFile, btnExploreFolder, btnExploreBack, btnExploreFwd, btnRenameFolder, btnDeleteFolder;

	private JTextPane textPaneAreaContent;
	private JLabel lblFilePath;
	private JScrollPane scrollPane_Content;
	private JList list_Explorer;
	private JScrollPane scrollPane;
	private JToggleButton tglbtnEditFile;

	/**
	 * Constructs the UI
	 */
	public View() {
		initialize();
	}

	/**
	 * Initializes the contents of the frame generated/edited with WindowBuilder;
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 919, 582);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnSearchText = new JButton("Search");
		btnSearchText.setBounds(500, 509, 89, 23);
		btnSearchText.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnSearchText);

		btnReplaceText = new JButton("Replace");
		btnReplaceText.setBounds(705, 509, 89, 23);
		btnReplaceText.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnReplaceText);

		btnSave = new JButton("Save");
		btnSave.setBounds(804, 509, 89, 23);
		btnSave.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnSave);

		btnNewFolder = new JButton("+ New Folder");
		btnNewFolder.setBounds(258, 36, 125, 23);
		btnNewFolder.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnNewFolder);

		btnNewFile = new JButton("+ New File");
		btnNewFile.setBounds(258, 70, 125, 23);
		btnNewFile.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnNewFile);

		JLabel lblFolderOptions = new JLabel("Folder Options");
		lblFolderOptions.setBounds(271, 95, 112, 23);
		lblFolderOptions.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblFolderOptions);

		JLabel lblFileOptions = new JLabel("File Options");
		lblFileOptions.setBounds(279, 259, 89, 23);
		lblFileOptions.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblFileOptions);

		lblFilePath = new JLabel("...");
		lblFilePath.setBounds(125, 11, 720, 14);
		lblFilePath.setFont(new Font("Dialog", Font.PLAIN, 12));
		frame.getContentPane().add(lblFilePath);

		btnOpenPath = new JButton("Open Path");
		btnOpenPath.setBounds(10, 7, 105, 23);
		btnOpenPath.setFont(new Font("Dialog", Font.BOLD, 13));
		frame.getContentPane().add(btnOpenPath);

		btnShowFile = new JButton("Show");
		btnShowFile.setBounds(271, 284, 97, 23);
		btnShowFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnShowFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(btnShowFile);

		btnRenameFile = new JButton("Rename");
		btnRenameFile.setBounds(271, 319, 97, 23);
		btnRenameFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.getContentPane().add(btnRenameFile);

		btnCopyFile = new JButton("Copy");
		btnCopyFile.setBounds(271, 353, 97, 23);
		btnCopyFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.getContentPane().add(btnCopyFile);

		btnDeleteFile = new JButton("Delete");
		btnDeleteFile.setBounds(271, 387, 97, 23);
		btnDeleteFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.getContentPane().add(btnDeleteFile);

		btnExploreFolder = new JButton("Explore");
		btnExploreFolder.setBounds(271, 123, 97, 23);
		btnExploreFolder.setFont(new Font("Dialog", Font.BOLD, 14));
		btnExploreFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(btnExploreFolder);

		btnExploreBack = new JButton("<");
		btnExploreBack.setBounds(271, 157, 45, 23);
		btnExploreBack.setFont(new Font("Dialog", Font.BOLD, 14));
		btnExploreBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(btnExploreBack);

		btnExploreFwd = new JButton(">");
		btnExploreFwd.setBounds(323, 157, 45, 23);
		btnExploreFwd.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(btnExploreFwd);

		textFieldSearch = new JTextField();
		textFieldSearch.setBounds(393, 512, 97, 20);
		frame.getContentPane().add(textFieldSearch);
		textFieldSearch.setColumns(10);

		textFieldReplace = new JTextField();
		textFieldReplace.setBounds(599, 512, 96, 20);
		frame.getContentPane().add(textFieldReplace);
		textFieldReplace.setColumns(10);

		scrollPane_Content = new JScrollPane();
		scrollPane_Content.setBounds(393, 37, 500, 467);
		frame.getContentPane().add(scrollPane_Content);

		textPaneAreaContent = new JTextPane();
		scrollPane_Content.setViewportView(textPaneAreaContent);

		btnRenameFolder = new JButton("Rename");
		btnRenameFolder.setBounds(271, 191, 97, 23);
		btnRenameFolder.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.getContentPane().add(btnRenameFolder);

		btnDeleteFolder = new JButton("Delete");
		btnDeleteFolder.setBounds(271, 225, 97, 23);
		btnDeleteFolder.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.getContentPane().add(btnDeleteFolder);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 240, 468);
		frame.getContentPane().add(scrollPane);

		list_Explorer = new JList<>();
		scrollPane.setViewportView(list_Explorer);

		tglbtnEditFile = new JToggleButton("Edit File");
		tglbtnEditFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		tglbtnEditFile.setBounds(271, 421, 97, 23);
		frame.getContentPane().add(tglbtnEditFile);

		this.frame.setVisible(true);
	}


	public JTextField getTextFieldSearch() {
		return textFieldSearch;
	}

	public JTextField getTextFieldReplace() {
		return textFieldReplace;
	}

	public JButton getBtnSearchText() {
		return btnSearchText;
	}

	public JButton getBtnReplaceText() {
		return btnReplaceText;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnNewFolder() {
		return btnNewFolder;
	}

	public JButton getBtnNewFile() {
		return btnNewFile;
	}

	public JButton getBtnOpenPath() {
		return btnOpenPath;
	}

	public JButton getBtnShowFile() {
		return btnShowFile;
	}

	public JButton getBtnRenameFile() {
		return btnRenameFile;
	}

	public JButton getBtnCopyFile() {
		return btnCopyFile;
	}

	public JButton getBtnDeleteFile() {
		return btnDeleteFile;
	}

	public JButton getBtnExploreFolder() {
		return btnExploreFolder;
	}

	public JButton getBtnExploreBack() {
		return btnExploreBack;
	}

	public JButton getBtnExploreFwd() {
		return btnExploreFwd;
	}

	public JButton getBtnRenameFolder() {
		return btnRenameFolder;
	}

	public JButton getBtnDeleteFolder() {
		return btnDeleteFolder;
	}

	public JTextPane getTextPaneAreaContent() {
		return textPaneAreaContent;
	}

	public JLabel getlLblFilePath() {
		return lblFilePath;
	}

	public JList<String> getListExplorer() {
		return list_Explorer;
	}

	public JToggleButton getTglBtnEditFile() {
		return tglbtnEditFile;
	}

	public JFrame getFrame() {
		return frame;
	}
}
