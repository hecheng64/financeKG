package com.cc.janus.dataSource.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ReadFile implements Read {
    private static final long serialVersionUID = -7657934327029011206L;

    private BufferedReader reader = null;
    /**
     * 已读偏移量.
     */
    private AtomicLong offset = new AtomicLong(0);

    public ReadFile(Map<String,Object> options) {
//        this(String.class.cast(options.get("file")));
        this(String.class.cast(options.get("file")));
    }


    public ReadFile(String filePath) {
        try{
            reader = new BufferedReader(new FileReader(filePath));
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public synchronized Read init() {
        return this;
    }

    @Override
    public synchronized String readLine() {
        try{
            String data = null;
            if(reader != null){
                data = reader.readLine();
                offset.addAndGet(1);
            }
            return null == data ? EXIT:data;

        }catch (IOException e){
            e.printStackTrace();
            return EXIT;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Long commitOffser() {
        return new Long(offset.get());
    }

    @Override

    public synchronized void close() {
        try {
            if(reader != null){
                reader.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean check() {
        return reader != null;
    }
}
