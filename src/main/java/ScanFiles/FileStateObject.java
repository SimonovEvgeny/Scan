package ScanFiles;

/**
 *
 */
class FileStateObject {
    private String path;
    private long size;
    private String date;

    FileStateObject(String path, long size, String date) {
        this.path = path;
        this.size = size;
        date = date.substring(0,10);
        this.date = date.replaceAll("-",".");
    }

    @Override
    public String toString() {
        return "[ \nfile= " + this.path +
                "\ndate= " + this.date +
                "\nsize= " + this.size + "]";
    }
}
