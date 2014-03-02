package uk.ac.shef.dcs.oak.jate.Utitls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUtils {
	
	/**
     * 判断文件夹是否存在
     * @param path 文件夹路径
     * true 文件不存在，false 文件存在不做任何操作
     */
    public static boolean checkDirExistAndCreate(String filePath) {
        String paths[] = filePath.split("/");
        String dir = paths[0];
        for (int i = 0; i < paths.length - 1; i++) {
            try {
                dir = dir + "/" + paths[i + 1];
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                    //System.out.println("创建目录为：" + dir);
                }
            } catch (Exception err) {
                System.err.println("ELS - Chart : 文件夹创建发生异常");
            }
        }
        File fp = new File(filePath);
        if(fp.exists()){
            return true; // 文件不存在，执行下载功能
        }else{
            return false; // 文件存在不做处理
        }
    }
    
    public static int removeOldFiles(String path) {
    	File dir = new File(path);
    	if (dir.exists()) {
        	File[] files = dir.listFiles();
        	Map<File, Long> filemap  = new HashMap<File, Long>();
        	for (File file : files) 
        		filemap.put(file, file.lastModified());
        	List<Map.Entry<File, Long>> filemaplist = new ArrayList<Map.Entry<File, Long>>(filemap.entrySet());
        	Collections.sort(filemaplist, new Comparator<Map.Entry<File, Long>>(){
				public int compare(Map.Entry<File, Long> arg0, Map.Entry<File, Long> arg1) {
					return arg0.getValue().compareTo(arg1.getValue());
				}
        	});
        	for (int i = 0; i < filemaplist.size(); i++) {
        	    String id = filemaplist.get(i).toString();
        	    System.out.println(filemaplist.get(i).getValue());
        	}
        	return filemap.size() - filemaplist.size();
    	}
    	return 0;
    }
    
    public static int getFilenNumber(String path) {
    	
    	return 0;
    }
    
	public static String createOrRepalceFileContent(String path, String newcontent){
	
		// check dir and create dir
		if (!FileUtils.checkDirExistAndCreate(path))
			return "";

		UUID uuid  =  UUID.randomUUID(); 
		String uuidstr = UUID.randomUUID().toString();
	    File file=new File(path + "/" + uuidstr + ".txt");  
	    if(!file.exists())  
	    {  
	        try {  
	            file.createNewFile();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }
		
	    PrintWriter out = null;
		try {
			out = new PrintWriter(file);
		    out.write(newcontent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != out)
				out.close();
		}
		return path + uuidstr + ".txt";
	}
	
	public static boolean removeFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
	
	public static void main(String args[]) throws IOException {
		
//		for (int i = 0; i < 1000; i++) {
//			System.out.println("write file " + i);
			String filepath = FileUtils.createOrRepalceFileContent("src/test/example/", "this is a test programe" + 2);
			FileUtils.removeFile(filepath);
//		}
	}
	
}
