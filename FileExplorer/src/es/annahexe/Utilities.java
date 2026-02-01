package es.annahexe;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utility helpers for file and folder operations (I/O), independent from UI layout.
 * <p><b>Responsibilities include:</b></p>
 * <ul>
 * <li>Validating folders</li>
 * <li>Reading file content</li>
 * <li>Listing directory entries</li>
 * <li>Creating, renaming, copying, deleting files/folders</li>
 * <li>Saving text (overwrite / save-as)</li>
 * </ul>
 * @author annahexe
 */
public class Utilities {
	private File selectedFolder;

    /**
     * Constructs the utilities object with the current directory as the initially selected folder.
     */
	public Utilities() {
		selectedFolder = new File(".");
	}

    /**
     * Returns the folder currently selected as the root for browsing.
     * @return the selected root folder;
     */
	public File selectedFolder() {
		return selectedFolder;
	}

    /**
     * Sets the folder currently selected as the root for browsing.
     * @param selectedFolder the folder to use as root.
     */
	public void setSelectedFolder(File selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

    /**
     * Checks whether the given file is a valid readable directory.
     * @param folder the file to validate
     * @return {@code true} if {@code folder} is non-null, a directory, and readable;
     *         {@code false} otherwise
     */
	public boolean isValidFolder(File folder) {
		return folder != null && folder.isDirectory() && folder.canRead();
	}

    /**
     * Reads a text file line-by-line into memory.
     * @param fileName a readable text file
     * @return an ArrayList of lines
     */
	public ArrayList<String> fileContent(File fileName) {
		ArrayList<String> content = new ArrayList<>();
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				content.add(line);
				line = br.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return content;
	}

    /**
     * Lists children of a folder.
     * Returns an empty array if the folder is invalid or not accessible.
     * @param folder the directory whose children to list
     * @return an array of files; empty if invalid or none
     */
	public File[] listFilesInFolder(File folder) {
		if (!isValidFolder(folder))
			return new File[0];
		File[] files = folder.listFiles();
		return (files != null) ? files : new File[0];
	}

    /**
     * Shows a folder selection dialog and updates {@link #selectedFolder()} on approval.
     */
	public void folderSelector() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a folder");

		// Restrict to directories only
		chooser.setCurrentDirectory(selectedFolder);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		// Show the dialog
		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			setSelectedFolder(chooser.getSelectedFile());
		}
	}

    /**
     * Builds an information block about a directory.
     * @param selectedDirectory the directory to describe
     * @return string with name, path, element counts, permissions, and last modified
     */
	public String infoDirectory(File selectedDirectory) {
		File[] files = selectedDirectory.listFiles();
		int fileCount = 0;
		int folderCount = 0;
		for (File file : files) {
			if (file.isFile())
				fileCount++;
			else if (file.isDirectory())
				folderCount++;
		}

		String infoContent = "NAME: " + selectedDirectory.getName() + "\n";
		infoContent += "PATH: " + selectedDirectory.getAbsolutePath() + "\n";
		infoContent += "ELEMENTS: " + files.length + "\n";
		infoContent += "N. FILES: " + fileCount + "\n";
		infoContent += "N. FOLDERS: " + folderCount + "\n";
		infoContent += "READABLE: " + selectedDirectory.canRead() + "\n";
		infoContent += "WRITABLE: " + selectedDirectory.canWrite() + "\n";
		infoContent += "LAST MODIFIED: " + new Date(selectedDirectory.lastModified()) + "\n";
		return infoContent;
	}

    /**
     * Builds an information block about a file.
     * @param selectedFile the file to describe
     * @return string with name, path, size, permissions, and last modified
     */
	public String infoFile(File selectedFile) {
		String infoContent = "NAME: " + selectedFile.getName() + "\n";
		infoContent += "PATH: " + selectedFile.getAbsolutePath() + "\n";
		infoContent += "SIZE (bytes): " + selectedFile.length() + "\n";
		infoContent += String.format("SIZE (MB): %.2f\n", (double) selectedFile.length() / (1024 * 1024));
		infoContent += "READABLE: " + selectedFile.canRead() + "\n";
		infoContent += "WRITABLE: " + selectedFile.canWrite() + "\n";
		infoContent += "LAST MODIFIED: " + new Date(selectedFile.lastModified()) + "\n";
		return infoContent;
	}

    /**
     * Deletes a single file.
     * @param fileToBeDeleted the file to delete
     * @return {@code true} if the file was deleted; {@code false} otherwise
     */
	public boolean deleteFile(File fileToBeDeleted) {
		return fileToBeDeleted.delete();
	}

    /**
     * Recursively deletes a directory and all its content.
     * @param directoryToBeDeleted the root directory to delete
     * @return {@code true} if the directory was deleted; {@code false} otherwise
     */
	public boolean deleteFolder(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteFolder(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

    /**
     * Renames a file or folder. If the new name has no extension, the old extension is preserved.
     * @param input        the new name (extension optional)
     * @param selectedFile the file or folder to rename (must exist)
     * @param location     the destination directory (typically the current folder)
     * @return {@code true} if the rename succeeded; {@code false} otherwise
     */
	public boolean renameFile(String input, File selectedFile, File location) {
		if (selectedFile == null || !selectedFile.exists())
			return false;
		String newName = input;
		if (!input.contains(".")) {
			String oldName = selectedFile.getName();
			int dotIndex = oldName.lastIndexOf('.');
			if (dotIndex >= 0) {
				String extension = oldName.substring(dotIndex);
				newName += extension;
			}
		}
		File newFileName = new File(location, newName);
		return selectedFile.renameTo(newFileName);
	}

    /**
     * Creates a new folder inside a route.
     * @param folderName the name of the folder to create
     * @param route      the parent directory
     * @return {@code true} if the folder was created; {@code false} otherwise
     */
	public boolean createNewFolder(String folderName, File route) {
		File file = new File(route, folderName);
		return file.mkdir();
	}


    /**
     * Creates a new empty file inside a route.
     * @param fileName the new file name
     * @param route    the parent directory
     * @return {@code true} if created; {@code false} otherwise
     */
	public boolean createNewFile(String fileName, File route) {
		File file = new File(route, fileName);
		try {
			return file.createNewFile();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

    /**
     * Copies a text file into the current folder as name+_copia, keeps extension.
     * @param selectedFile the file to copy
     * @param currentFolder the destination directory
     * @return {@code true} on success; {@code false} on error
     */
	public boolean copyFile(File selectedFile, File currentFolder) {
		String fileName = selectedFile.getName();
		int dotIndex = fileName.lastIndexOf('.');
		String nameWithoutExt = (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
		String extension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
		String nameCopy = nameWithoutExt + "_copia" + extension;
		File fileCopy = new File(currentFolder, nameCopy);

		try {
			FileReader fr = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(fileCopy);
			BufferedWriter bw = new BufferedWriter(fw);
			String line = br.readLine();

			while (line != null) {
				bw.write(line);
				bw.newLine();
				line = br.readLine();
			}
			br.close();
			bw.close();
			fr.close();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * Overwrites the given file with passed content
     * @param content      the text to write
     * @param selectedFile the file to overwrite
     * @return {@code true} on success; {@code false} otherwise
     */
	public boolean saveTextOverwrite(String content, File selectedFile) {
		try {
			FileWriter fw = new FileWriter(selectedFile, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);

			bw.close();
			fw.close();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

    /**
     * Saves content to a new file named {@code fileName} in currentFolder.
     * If a file with the same name already exists, the write is aborted (no overwrite).
     * @param content      the text to write
     * @param fileName     the new file name
     * @param currentFolder the destination directory
     * @return {@code true} on success; {@code false} otherwise
     */
	public boolean saveTextNewFile(String content, String fileName, File currentFolder) {
		if (fileName == null || fileName.trim().isEmpty())
			return false;
		try {
			File newFile = new File(currentFolder, fileName);
	        if (!newFile.createNewFile()) {
	            return false;
	        }
			FileWriter fw = new FileWriter(newFile, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
