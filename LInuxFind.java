// File - name, path, size, lastModified, isDirectory, creating a File(), is matching the criteria with file matches()
// Criteria - type (name, size, extension), value, create a Criteria(), applying the filters(file)
// FindCommand - startPath, list of criteria, list of results, addCriteria(), execute() - returns list of files matching criteria

import java.util.*;

// Represents File or Directory
class File {
    String name;
    String path;
    long size;
    Date lastModified;
    boolean isDirectory;

    public File(String name, String path, long size, boolean isDir) {
        this.name = name; this.path = path;
        this.size = size; this.isDirectory = isDir;
        this.lastModified = new Date(); // mock
    }

    public boolean matches(Criteria c) {
        return c.apply(this);
    }
}

// Criteria for filtering
class Criteria {
    String type;  // "NAME", "SIZE", "EXTENSION"
    String value;

    public Criteria(String type, String value) {
        this.type = type; this.value = value;
    }

    public boolean apply(File f) {
        switch(type) {
            case "NAME": return f.name.contains(value);
            case "EXTENSION": return f.name.endsWith(value);
            case "SIZE": return f.size > Long.parseLong(value);
            default: return false;
        }
    }
}

// The find command
class FindCommand {
    String startPath;
    List<Criteria> criteriaList;
    List<File> results;

    public FindCommand(String startPath) {
        this.startPath = startPath;
        this.criteriaList = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public void addCriteria(Criteria c) { criteriaList.add(c); }

    public List<File> execute(List<File> mockFs) {
        for(File f : mockFs) {
            boolean match = true;
            for(Criteria c : criteriaList) {
                if(!c.apply(f)) { match = false; break; }
            }
            if(match) results.add(f);
        }
        return results;
    }
}

// Demo
public class Main {
    public static void main(String[] args) {
        // mock "file system"
        List<File> fs = Arrays.asList(
            new File("test.txt", "/home/docs", 1200, false),
            new File("image.png", "/home/images", 5000, false),
            new File("notes.doc", "/home/docs", 800, false)
        );

        FindCommand find = new FindCommand("/home");
        find.addCriteria(new Criteria("EXTENSION", ".txt"));
        find.addCriteria(new Criteria("SIZE", "1000"));

        List<File> res = find.execute(fs);
        for(File f : res) System.out.println("Found: " + f.path + "/" + f.name);
    }
}
