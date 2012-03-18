package img.view.filters;

import java.io.File;

public class PGMFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        String filename = file.getName();
        return filename.endsWith(".pgm");
    }
    public String getDescription() {
        return "*.pgm";
    }
    
}
