/*
Java 3. Средства ввода-вывода
ДЗ №3: Работа с файлами.

Студент: Гришин Дмитрий
 */

import java.io.*; //импорт библиотеки ввода/вывода
import java.util.ArrayList;
import java.util.Collections;

public class MainClass {


    public static void main(String[] args) {

        createDirectory("txtFiles");//создаю каталог (папку)
        createFile("txtFiles/50B.txt");//создаю файл в каталоге

        //1. Чтение файла в байтовый массив и печать его в консоль:
        fileToByteArray("txtFiles/50B.txt");

        //2. Последовательная сшивка 5 файлов в один:
        // создаю пять файлов в каталоге [txtFiles]:
        for (int i = 1; i <= 5; i++) {
            createFile("txtFiles/100Bfile_" + i + ".txt");
        }
        //создаю файл под объединение:
        createFile("txtFiles/JointFile.txt");

        //Объединяю 5 файлов:
        fiveToOneFile();

    }

    //1. Метод чтения файла в байтовый массив и печати его в консоль:
    public static void fileToByteArray(String fileName) {
        byte[] byteArray = new byte[60];//создаю массив байтов в 64 элемента

        //Создание потока чтения данных из файла "in"
        //делаю в try "с ресурсами" - для автозакрытия данного ресурса:
        try (FileInputStream in = new FileInputStream(fileName)) {
            int x;
            int index = 0;
            while ((x = in.read()) != -1) {
                byteArray[index] = (byte)x;
                //System.out.print((char)x);//печать считанных байтов (с приведением байт-кода к char)
                index++;
                if(index > 59) {
                    System.out.println("Входящий поток превысил лимит в 64 байт-элемента");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Байтовый массив:");
        for(int i = 0; i < byteArray.length; i++){
            System.out.printf("%3d ", byteArray[i]);
            if((i+1) % 15 == 0) System.out.println();
        }
    }

    //2. Метод сшивки 5 файлов в один:
    public static void fiveToOneFile(){
        ArrayList<InputStream> fileStreams = new ArrayList<>(); //создаю коллекцию под потоки чтения данных из файла

        //заполняю коллекцию 5-ю потоками чтения данных из файла:
        for (int i = 1; i <= 5; i++){
            try {
                fileStreams.add(new FileInputStream("txtFiles/100Bfile_" + i + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        /*Это моё, может корявое, пояснение того, что происходит в одной строке кода ниже:
        //создаю объект-перечисление потоков чтения данных из файла:
        Enumeration<InputStream> enumerationStreams = Collections.enumeration(fileStreams);
        //создаю общий поток чтения данных используя объект-перечисление:
        SequenceInputStream jointStream = new SequenceInputStream(enumerationStreams);
        */
        //Создаю общий поток чтения данных из коллекции потоков чтения данных из файла:
        SequenceInputStream jointStream = new SequenceInputStream(Collections.enumeration(fileStreams));

        //Далее читаю общий поток и записываю его в общий файл:
        try {
            FileOutputStream out = new FileOutputStream("txtFiles/JointFile.txt"); //создаю поток записи в файл:
            int x; //переменная для сохранения считанных байт методом read() из общего потока
            while ((x = jointStream.read()) != -1){
                out.write(x);
            }
            //Закрываю ресурсы:
            out.close();
            jointStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод создания папки (каталога)
    public static void createDirectory(String dirName){
        File dir = new File(dirName);//создаю объект с именем папки
        if(!dir.exists()){//если папка не существует
            dir.mkdir();//создаю папку
        } else {
            System.out.println("Папка " + dirName + " уже существует.");
        }
    }

    //метод создания файла
    public static void createFile(String fileName) {
        File file = new File(fileName);//создаю объект с именем файла
        try {
            file.createNewFile();//создаю папку
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
