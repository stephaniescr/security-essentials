package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class Backup {
    public static void copy(File sourceLocation, File targetLocation) throws IOException {
try {
    FileUtils.copyDirectory(sourceLocation, targetLocation);
} catch (IOException e) {
    e.printStackTrace();
}
}

}
