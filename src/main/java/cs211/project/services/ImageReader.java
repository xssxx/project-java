package cs211.project.services;

import java.io.File;

public class ImageReader {
	/**
    * Constructs and returns the absolute file path of an image based on folder and filename inputs.
    *
    * @param folder   The folder path where the image is located.
    * @param filename The name of the image file.
    * @return The absolute file path as a string, or null if error.
    */
    public static String getImagePath(String folder, String filename) {
    	if (folder.isEmpty() || filename.isEmpty())
    		return null;

    	File imageFile = new File(folder, filename);

    	if (!imageFile.exists())
    		return null;

    	try {
            String path = "file:///" + imageFile.getAbsolutePath();
            return path;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
}
