package com.gitblog.create;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import sun.net.www.protocol.file.FileURLConnection;

public class SelfFileUtil {
    public static void loadRecourseFromJarByFolder(String folderPath,String targetFolderPath) throws IOException {
        URL url = SelfFileUtil.class.getResource(folderPath);
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof FileURLConnection) {
            System.out.println("*****************file");
            copyFileResources(url, folderPath,targetFolderPath);
        } else if (urlConnection instanceof JarURLConnection) {
            System.out.println("*****************jar");
            copyJarResources((JarURLConnection) urlConnection,folderPath,targetFolderPath);
        }
    }
 
    /**
     * 当前运行环境资源文件是在文件里面的
     * 
     * @param url
     * @param folderPath
     * @param clazz 
     * @throws IOException
     */
    private static void copyFileResources(URL url, String folderPath,String targetFolderPath) throws IOException {
        File root = new File(url.getPath());
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    loadRecourseFromJarByFolder(folderPath + "/" + file.getName(),targetFolderPath);
                } else {
                    loadRecourseFromJar(folderPath + "/" + file.getName(),folderPath,targetFolderPath);
                }
            }
        }
    }
 
    /**
     * 当前运行环境资源文件是在jar里面的
     * 
     * @param jarURLConnection
     * @throws IOException
     */
    private static void copyJarResources(JarURLConnection jarURLConnection,String folderPath,String targetFolderPath) throws IOException {
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry entry = entrys.nextElement();
            if (entry.getName().startsWith(jarURLConnection.getEntryName()) && !entry.getName().endsWith("/")) {
                loadRecourseFromJar("/" + entry.getName(),jarURLConnection.getEntryName(),targetFolderPath);
            }
        }
        jarFile.close();
    }
 
    public static void loadRecourseFromJar(String path,String matchPath,String recourseFolder) throws IOException {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
 
        if (path.endsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (cat not end with '/').");
        }
        int index = path.lastIndexOf('/');
 
        String filename = path.substring(index + 1);
        String folderPath = recourseFolder + path.substring(path.indexOf(matchPath)+matchPath.length(), index + 1);
 
        // If the folder does not exist yet, it will be created. If the folder
        // exists already, it will be ignored
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
 
        // If the file does not exist yet, it will be created. If the file
        // exists already, it will be ignored
        filename = folderPath + filename;
        File file = new File(filename);
 
        if (!file.exists() && !file.createNewFile()) {
            return;
        }
 
        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;
 
        // Open and check input stream
        URL url = SelfFileUtil.class.getResource(path);
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();
 
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
        OutputStream os = new FileOutputStream(file);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            os.close();
            is.close();
        }
    }
}
