package es.annahexe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.io.File;

/**
 * Controller: wires user interactions in {@link View} to the logic in {@link Utilities}.
 * Responsibilities:
 * <ul>
 * 	<li>Registers UI listeners</li>
 * 	<li>Loads/updates file and folder lists</li>
 * 	<li>Loads JOptionPane.showMessageDialog</li>
 * 	<li>Performs text actions on the JTextPane (search/replace/highlight)</li>
 * </ul>
 * @author annahexe
 */

public class Controller {
	private Utilities utilities;
	private View view;
	private File currentFolder; // folder shown in the explorer
	private File selectedFile; // currently selected file/folder from the list

	/**
	 * Builds the controller and initializes the UI.
	 * @param utilities file and IO helpers (non-UI)
	 * @param view      UI widgets and windows
	 */
	public Controller(Utilities utilities, View view) {
		this.utilities = utilities;
		this.view = view;
		initialize();
	}

	/**
	 * Initializes the starting folder, registers listeners, and disables UI controls.
	 */
	public void initialize() {
		File initialFolder = utilities.selectedFolder();
		changeDirectory(initialFolder);
		setListeners();
		disableAllButtons();
	}

	/**
	 * Registers all UI listeners.
	 */
	public void setListeners() {
		registerOpenPathListener();
		registerListSelectionListener();
		registerNewFolderListener();
		registerNewFileListener();

        registerExploreFolderListeners();  // Explore + >
        registerExploreBackListener();     // <

        registerRenameListeners(); // file + folder
        registerDeleteFolderListener();

        registerShowFileListener();
        registerCopyFileListener();
        registerDeleteFileListener();

        registerEditToggleListener();
        registerSearchTextListener();
        registerReplaceTextListener();
        registerSaveFileListener();
	}

    // =========================
    // LISTENERS
    // =========================

    /**
     * Registers the "Open Path" button listener.
     * <p>Opens a folder chooser and refreshes the explorer with the selected folder.</p>
     * @see #changeDirectory(File)
     */
	private void registerOpenPathListener() {
		ActionListener actionOpenPath = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				utilities.folderSelector();
				changeDirectory(utilities.selectedFolder());
			}
		};
		view.getBtnOpenPath().addActionListener(actionOpenPath);
	}

    /**
     * Registers the JList selection listener for the explorer.
     * Behavior:
     * <ul>
     *   <li>Selecting <code>"../"</code> navigates to the parent folder.</li>
     *   <li>Selecting <code>"./name"</code> selects a directory, enables folder actions and shows info.</li>
     *   <li>Selecting <code>"name"</code> selects a file and enables file actions and shows info.</li>
     *   </ul>
     *      @see Utilities#infoDirectory(File)
     *   	@see Utilities#infoFile(File)
     */
	private void registerListSelectionListener() {
		ListSelectionListener actionListSelection = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listEvent) {
				if (!listEvent.getValueIsAdjusting()) {
					String name = view.getListExplorer().getSelectedValue();
					if (name != null && currentFolder != null) {
						if (name.equals("../")) {
							changeDirectory(currentFolder.getParentFile());
							return;
						}
						name = name.replace("./", "");
						selectedFile = new File(currentFolder, name);
						if (selectedFile.isDirectory()) {
							JOptionPane.showMessageDialog(view.getFrame(), utilities.infoDirectory(selectedFile),
									"INFO DIRECTORY", JOptionPane.INFORMATION_MESSAGE);
							disableAllButtons();
							setButtonsFolderEnable(true);
						} else if (selectedFile.isFile()) {
							JOptionPane.showMessageDialog(view.getFrame(), utilities.infoFile(selectedFile),
									"INFO FILE", JOptionPane.INFORMATION_MESSAGE);
							disableAllButtons();
							setButtonsFileEnable(true);
						} else {
							disableAllButtons();
						}
					}
				}
			}
		};
		view.getListExplorer().addListSelectionListener(actionListSelection);
	}

    /**
     * Registers the "+ New Folder" button listener.
     * <p>Prompts for a folder name, creates it in the current folder, and refreshes the list.</p>
     * @see Utilities#createNewFolder(String, File)
     */
	private void registerNewFolderListener() {
		ActionListener actionNewFolder = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String input = JOptionPane.showInputDialog(view.getFrame(), "Write the new folder name:",
						"CONFIRM OPERATION", JOptionPane.QUESTION_MESSAGE);
				if (utilities.createNewFolder(input, currentFolder)) {
					changeDirectory(currentFolder);
					JOptionPane.showMessageDialog(view.getFrame(), "Folder `" + input + "` created succesfully.",
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(view.getFrame(), "ERROR! Enter a correct folder name.", "WARNING",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		view.getBtnNewFolder().addActionListener(actionNewFolder);
	}

    /**
     * Registers the "+ New File" button listener.
     * <p>Prompts for a file name, creates it in the current folder, and refreshes the list.</p>
     * @see Utilities#createNewFile(String, File)
     */
	private void registerNewFileListener() {
		ActionListener actionNewFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String input = JOptionPane.showInputDialog(view.getFrame(), "Write new file name:", "CONFIRM OPERATION",
						JOptionPane.QUESTION_MESSAGE);
				if (utilities.createNewFile(input, currentFolder)) {
					changeDirectory(currentFolder);
					JOptionPane.showMessageDialog(view.getFrame(), "File `" + input + "` created succesfully.", "INFO",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(view.getFrame(), "ERROR! Enter a correct file name.", "WARNING",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		view.getBtnNewFile().addActionListener(actionNewFile);
	}

	// =========
	// FOLDER LISTENERS
	// =========

    /**
     * Registers the "Explore" and ">" button listeners.
     * <p>If a directory is selected, enters it.</p>
     * @see #changeDirectory(File)
     */
	private void registerExploreFolderListeners() {
		ActionListener actionExploreFolder = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (selectedFile != null && selectedFile.isDirectory()) {
					changeDirectory(selectedFile);
				}
			}
		};
		view.getBtnExploreFolder().addActionListener(actionExploreFolder);
		view.getBtnExploreFwd().addActionListener(actionExploreFolder);
	}

    /**
     * Registers the "&lt;" button listener.
     * <p>Navigates to the parent folder if available.</p>
     * @see #changeDirectory(File)
     */
	private void registerExploreBackListener() {
		ActionListener actionExploreBack = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (currentFolder != null && currentFolder.getParentFile() != null) {
					changeDirectory(currentFolder.getParentFile());
				}
			}
		};
		view.getBtnExploreBack().addActionListener(actionExploreBack);
	}

    /**
     * Registers the "Rename" listeners for both file and folder.
     * <p>Prompts for a new name, requires sudo, performs rename, refreshes list.</p>
     * @see Utilities#renameFile(String, File, File)
     * @see #isSudoConfirmation()
     */
	private void registerRenameListeners() {
		ActionListener actionRename = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String input = JOptionPane.showInputDialog(view.getFrame(), "Write the new name:", "RENAME FILE",
						JOptionPane.QUESTION_MESSAGE);
				if (input != null && !input.trim().isEmpty()) {
					if (isSudoConfirmation()) {
						if (utilities.renameFile(input, selectedFile, currentFolder)) {
							changeDirectory(currentFolder);
							JOptionPane.showMessageDialog(view.getFrame(), "File `" + input + "` renamed succesfully.",
									"INFO", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(view.getFrame(), "ERROR! Enter a correct file name.",
									"WARNING", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		};
		view.getBtnRenameFile().addActionListener(actionRename);
		view.getBtnRenameFolder().addActionListener(actionRename);
	}

    /**
     * Registers the "Delete" listener for folders.
     * <p>Confirms, asks for sudo, deletes recursively, then refreshes.</p>
     * @see Utilities#deleteFolder(File)
     * @see #isSudoConfirmation()
     */
    private void registerDeleteFolderListener(){
		ActionListener actionDeleteFolder = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				int result = JOptionPane.showConfirmDialog(view.getFrame(),
						"Are you sure you want to delete this folder and all its content? \n"
								+ utilities.infoDirectory(selectedFile),
						"CONFIRM DELETE", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					if (isSudoConfirmation()) {
						if (utilities.deleteFolder(selectedFile)) {
							changeDirectory(currentFolder);
							JOptionPane.showMessageDialog(view.getFrame(), "Folder deleted succesfully.", "FILE DELETE",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		};
		view.getBtnDeleteFolder().addActionListener(actionDeleteFolder);
    }

	// =========
	// FILE LISTENERS
	// =========

    /**
     * Registers the "Show" button listener.
     * <p>Loads the selected file's content into the text pane.</p>
     * @see #displayFileContent(File)
     */
    private void registerShowFileListener(){
		ActionListener actionShowFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				displayFileContent(selectedFile);
			}
		};
		view.getBtnShowFile().addActionListener(actionShowFile);
    }

    /**
     * Registers the "Copy" button listener for files.
     * <p>Creates a copy named <code>name+"_copia"</code>, keeps extension, and refreshes the list.</p>
     * @see Utilities#copyFile(File, File)
     */
    private void registerCopyFileListener(){
		ActionListener actionCopyFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (utilities.copyFile(selectedFile, currentFolder)) {
					changeDirectory(currentFolder);
					JOptionPane.showMessageDialog(view.getFrame(), "File copied successfully.", "COPY",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		view.getBtnCopyFile().addActionListener(actionCopyFile);
    }

    /**
     * Registers the "Delete" button listener for files.
     * <p>Confirms, asks for sudo, deletes the file, then refreshes.</p>
     * @see Utilities#deleteFile(File)
     * @see #isSudoConfirmation()
     */
    private void registerDeleteFileListener(){
		ActionListener actionDeleteFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				int result = JOptionPane.showConfirmDialog(view.getFrame(),
						"Are you sure you want to delete this file? \n" + utilities.infoFile(selectedFile),
						"CONFIRM DELETE", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					if (isSudoConfirmation()) {
						if(utilities.deleteFile(selectedFile)) {
						changeDirectory(currentFolder);
						JOptionPane.showMessageDialog(view.getFrame(), "File deleted succesfully.", "FILE DELETE",
								JOptionPane.INFORMATION_MESSAGE);} else {
							JOptionPane.showMessageDialog(view.getFrame(), "ERROR!",
									"WARNING", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		};
		view.getBtnDeleteFile().addActionListener(actionDeleteFile);
    }

    /**
     * Registers the "Edit File" toggle listener.
     * <p>When enabled, loads file content and enables edit controls; when disabled, disables them.</p>
     * @see #displayFileContent(File)
     * @see #setButtonsEditEnable(boolean)
     */
    private void registerEditToggleListener(){
		ActionListener actionEditFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (view.getTglBtnEditFile().isSelected()) {
					displayFileContent(selectedFile);
					setButtonsEditEnable(true);
				} else {
					setButtonsEditEnable(false);
				}
			}
		};
		view.getTglBtnEditFile().addActionListener(actionEditFile);
    }

    /**
     * Registers the "Search" button listener.
     * <p>Highlights and bolds all matches of the search text.</p>
     * @see #highlightSearchText(JTextPane, String)
     */
    private void registerSearchTextListener(){
		ActionListener actionSearchText = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String searchText = view.getTextFieldSearch().getText();
				JTextPane textPane = view.getTextPaneAreaContent();
				highlightSearchText(textPane, searchText);
			}
		};
		view.getBtnSearchText().addActionListener(actionSearchText);
    }


    /**
     * Registers the "Replace" button listener.
     * <p>Replaces all matches of the search text and highlights the replacement.</p>
     * @see #replaceText(JTextPane, String, String)
     */
    private void registerReplaceTextListener(){
		ActionListener actionReplaceText = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String searchText = view.getTextFieldSearch().getText();
				String replaceString = view.getTextFieldReplace().getText();
				JTextPane textPane = view.getTextPaneAreaContent();
				replaceText(textPane, searchText, replaceString);
			}
		};
		view.getBtnReplaceText().addActionListener(actionReplaceText);
    }

    /**
     * Registers the "Save" button listener.
     * <p>Offers Overwrite OR Save-as. Overwrite requires sudo; refresh on success.</p>
     * @see Utilities#saveTextOverwrite(String, File)
     * @see Utilities#saveTextNewFile(String, String, File)
     * @see #isSudoConfirmation()
     */
    private void registerSaveFileListener(){
		ActionListener actionSaveFile = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Object[] options = { "Overwrite", "Save as New File" };
				int result = JOptionPane.showOptionDialog(view.getFrame(),
						"Do you want to overwrite `" + selectedFile.getName() + "` or save on a new File?", "SAVE FILE",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (result == 0) {
					// OVERWRITE
					if (isSudoConfirmation()) {
						if (utilities.saveTextOverwrite(view.getTextPaneAreaContent().getText(), selectedFile)) {
							changeDirectory(currentFolder);
							JOptionPane.showMessageDialog(view.getFrame(), "File overwritten successfully!", "INFO",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} else if (result == 1) {
					// SAVE NEW FILE
					String input = JOptionPane.showInputDialog(view.getFrame(), "Write new file name:",
							"CONFIRM OPERATION", JOptionPane.QUESTION_MESSAGE);
					if (input == null || input.trim().isEmpty())
						return;
					if (utilities.saveTextNewFile(view.getTextPaneAreaContent().getText(), input, currentFolder)) {
						changeDirectory(currentFolder);
						JOptionPane.showMessageDialog(view.getFrame(), "File `" + input + "` created succesfully.",
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		};
		view.getBtnSave().addActionListener(actionSaveFile);
    }


    // =========================
    // UI enablers & helpers
    // =========================

    /**
     * Enables/disables folder-related buttons as a group.
     * @param isEnabled whether the buttons should be enabled
     * */
	public void setButtonsFolderEnable(boolean isEnabled) {
		view.getBtnExploreFolder().setEnabled(isEnabled);
		view.getBtnExploreFwd().setEnabled(isEnabled);
		view.getBtnRenameFolder().setEnabled(isEnabled);
		view.getBtnDeleteFolder().setEnabled(isEnabled);
	}

    /**
     * Enables/disables file-related buttons as a group.
     * @param isEnabled whether the buttons should be enabled
     * */
	public void setButtonsFileEnable(boolean isEnabled) {
		view.getBtnShowFile().setEnabled(isEnabled);
		view.getBtnRenameFile().setEnabled(isEnabled);
		view.getBtnCopyFile().setEnabled(isEnabled);
		view.getBtnDeleteFile().setEnabled(isEnabled);
		view.getTglBtnEditFile().setEnabled(isEnabled);
	}

	 /**
	  * Enables/disables edit controls and the text area’s editability.
	  * @param isEnabled whether the edit controls should be enabled
	  * */
	public void setButtonsEditEnable(boolean isEnabled) {
		view.getTextPaneAreaContent().setEditable(isEnabled);
		view.getBtnSearchText().setEnabled(isEnabled);
		view.getBtnReplaceText().setEnabled(isEnabled);
		view.getBtnSave().setEnabled(isEnabled);
		view.getTextFieldSearch().setEnabled(isEnabled);
		view.getTextFieldReplace().setEnabled(isEnabled);
	}

	 /**
	  * Disables all action buttons and clears the text area.
	  * */
	public void disableAllButtons() {
		// Folders
		view.getBtnExploreFolder().setEnabled(false);
		view.getBtnExploreFwd().setEnabled(false);
		view.getBtnRenameFolder().setEnabled(false);
		view.getBtnDeleteFolder().setEnabled(false);
		// Files
		view.getBtnShowFile().setEnabled(false);
		view.getBtnRenameFile().setEnabled(false);
		view.getBtnCopyFile().setEnabled(false);
		view.getBtnDeleteFile().setEnabled(false);
		view.getTextPaneAreaContent().setText("");
		// File Edit
		view.getTglBtnEditFile().setEnabled(false);
		view.getTextPaneAreaContent().setEditable(false);
		view.getBtnSearchText().setEnabled(false);
		view.getBtnReplaceText().setEnabled(false);
		view.getBtnSave().setEnabled(false);
		view.getTextFieldSearch().setEnabled(false);
		view.getTextFieldReplace().setEnabled(false);
	}

    /**
     * Asks the user to type "sudo" to confirm an action.
     * @return {@code true} if user typed exactly "sudo"; {@code false} otherwise.
     */
	public boolean isSudoConfirmation() {
		String input = JOptionPane.showInputDialog(view.getFrame(), "To confirm write \"sudo\":", "CONFIRM OPERATION",
				JOptionPane.WARNING_MESSAGE);
		if ("sudo".equals(input)) {
			return true;
		} else {
			JOptionPane.showMessageDialog(view.getFrame(), "Operation cancelled.", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	}

    /**
     * Changes the current folder shown in the explorer and rebuilds the list.
     * If the folder is invalid, clears the list and shows "No folder selected".
     * @param folder File folder to display
     */
	private void changeDirectory(File folder) {
		// UPDATES ROUTE PATH ON TOP
		disableAllButtons();
		if (!utilities.isValidFolder(folder)) {
			view.getlLblFilePath().setText("No folder selected");
			view.getListExplorer().setModel(new DefaultListModel<>());
			return;
		}
		view.getlLblFilePath().setText(folder.getAbsolutePath());

		// UPDATES FILES LIST ON THE LEFT
		currentFolder = folder;
		File[] files = utilities.listFilesInFolder(folder);
		DefaultListModel<String> list = new DefaultListModel<>();

		if (folder.getParentFile() != null) {
			list.addElement("../");
		}
		for (File file : files) {
			list.addElement(file.isDirectory() ? "./" + file.getName() : file.getName());
		}

		view.getListExplorer().setModel(list);
	}

    /**
     * Loads a file into the JTextPane.
     * @param filePath a readable file
     */
	private void displayFileContent(File filePath) {
		ArrayList<String> lines = utilities.fileContent(filePath);
		String fullContent = "";
		for (String line : lines) {
			fullContent = fullContent + line + "\n";
		}
		view.getTextPaneAreaContent().setText(fullContent);
	}

    /**
     * Highlights and bolds all occurrences of {@code searchText} in {@code textPane}.
     * If {@code searchText} is null/empty, clears previous highlights.
     * @param textPane the text component to operate on
     * @param searchText the text to search and highlight
     */
	private void highlightSearchText(JTextPane textPane, String searchText) {
		Highlighter highlighter = textPane.getHighlighter();
		StyledDocument doc = textPane.getStyledDocument();
		String content = textPane.getText();

		resetHighlight(textPane, doc, highlighter);
		if (searchText == null || searchText.isEmpty())
			return;

		if (!content.contains(searchText)) {
			JOptionPane.showMessageDialog(view.getFrame(), "No matches found.", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int index = content.indexOf(searchText);

		Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		Style styleBold = textPane.addStyle("BoldStyle", null);
		StyleConstants.setBold(styleBold, true);

		while (index >= 0) {
			try {
				doc.setCharacterAttributes(index, searchText.length(), styleBold, false);
				highlighter.addHighlight(index, index + searchText.length(), painter);
				index = content.indexOf(searchText, index + searchText.length());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

    /**
     * Clears all highlights and sets non-bold.
     * @param textPane the text component
     * @param doc the styled bold document
     * @param highlighter the pane's highlighter
     */
	private void resetHighlight(JTextPane textPane, StyledDocument doc, Highlighter highlighter) {
		highlighter.removeAllHighlights();
		Style defaultStyle = textPane.addStyle("DefaultStyle", null);
		StyleConstants.setBold(defaultStyle, false);
		doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
	}


    /**
     * Replaces all occurrences of {@code searchText} with {@code replaceText} in the JTextPane, then highlights the replacement.
     * @param textPane the text component
     * @param searchText text to be replaced
     * @param replaceText replacement text
     */
	private void replaceText(JTextPane textPane, String searchText, String replaceText) {
		if (searchText == null || searchText.isEmpty()) {
			JOptionPane.showMessageDialog(view.getFrame(), "Enter text to search.", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String content = textPane.getText();
		if (!content.contains(searchText)) {
			JOptionPane.showMessageDialog(view.getFrame(), "No matches found.", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		String newContent = content.replace(searchText, replaceText);
		textPane.setText(newContent);
		highlightSearchText(textPane, replaceText);
	}

}
