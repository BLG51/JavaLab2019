import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static String filepath = "";
    static Lock lock;
    public static void main(String[] args) throws InterruptedException {
        lock = new ReentrantLock();
        Scanner sc = new Scanner(System.in);
        while (true) {
            filepath = sc.nextLine();
            lock.lock();
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
            String address = filepath;
            lock.unlock();
            URL website = null;
            try {
                website = new URL(address);
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
                fos = new FileOutputStream(getFileName(address));
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
