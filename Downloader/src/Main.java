import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

public class Main {
    static String filepath = "";
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            filepath = sc.nextLine();
            DownloadThread thr = new DownloadThread();
            thr.start();
            thr.join();
        }
    }

    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private static String getFileName(String fileName) {
        if (fileName.lastIndexOf("/") != -1 && fileName.lastIndexOf("/") != 0)
            return fileName.substring(fileName.lastIndexOf("/") + 1);
        else return "default";
    }

    public static class DownloadThread extends Thread {
        @Override
        public void run() {
            URL website = null;
            try {
                website = new URL(filepath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ReadableByteChannel rbc = null;
            try {
                rbc = Channels.newChannel(website.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(getFileName(filepath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rbc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
