import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dmast {
    private String filepath = "";
    private Lock lock;
    private HashSet<String> names;
    public void start() throws InterruptedException {
        lock = new ReentrantLock();
        Scanner sc = new Scanner(System.in);
        names = new HashSet<>();
        while (true) {
            filepath = sc.nextLine();
//            lock.lock();
            DownloadThread thr = new DownloadThread();
            thr.start();
            thr.join();
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private String getFileName(String fileName) {
        if (fileName.lastIndexOf("/") != -1 && fileName.lastIndexOf("/") != 0)
            return fileName.substring(fileName.lastIndexOf("/") + 1);
        else return "default";
    }

    public class DownloadThread extends Thread {
        @Override
        public void run() {
            lock.lock();
            String address = filepath;
            lock.unlock();
            URL website = null;
            try {
                website = new URL(address);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("problems with url");
            }
            ReadableByteChannel rbc = null;
            try {
                rbc = Channels.newChannel(website.openStream());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("connection problem");
            }
            FileOutputStream fos = null;
            String name = getFileName(address);
            lock.lock();
            if (names.contains(name)) {
                int i = 0;
                while (names.contains(i+name)) {
                    i++;
                }
                name = i+name;
            }
            names.add(name);
            lock.unlock();
            try {
                fos = new FileOutputStream(name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("something is wrong with file");
            }
            try {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                rbc.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("something is wrong with stream");
            }
        }
    }
}
