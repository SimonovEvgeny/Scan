package ScanFiles;

/**
 * Класс свойств файла
 */
class FileStateObject {
    private String path;
    private long size;
    private String date;

    FileStateObject(String path, long size, String date) {
        this.path = path;
        this.size = size;
        date = date.substring(0, 10); //форматирование полученной даты согласно трбованиям задания
        this.date = date.replaceAll("-", ".");
    }

    @Override
    public String toString() {
        return "[ \nfile= " + this.path +
                "\ndate= " + this.date +
                "\nsize= " + this.size + "]";
    }
}
