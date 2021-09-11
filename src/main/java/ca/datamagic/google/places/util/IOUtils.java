/**
 * 
 */
package ca.datamagic.google.places.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Greg
 *
 */
public class IOUtils {
	private static final Logger logger = LogManager.getLogger(IOUtils.class);
	private static final int BUFFER_SIZE = 4096;

    public static String readEntireString(InputStream inputStream) throws IOException {
        StringBuffer textBuffer = new StringBuffer();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
            textBuffer.append(new String(buffer, 0, bytesRead));
        }
        return textBuffer.toString();
    }
    
    public static void closeQuietly(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Throwable t) {
            logger.warn("Throwable", t.getMessage());
        }
    }
}
