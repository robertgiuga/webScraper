package com.example.repository;

import com.example.domain.Website;

import java.io.*;
import java.util.*;

public class CsvRepository implements IFileRepository<Website> {
    private BufferedReader reader;
    private BufferedWriter writer;
    private final String fileName;

    public CsvRepository(String fileName) throws IOException {
        this.fileName= fileName;
        reader= new BufferedReader(new FileReader(fileName));
    }

    @Override
    public String readLine(){
        try {
            if (!reader.ready()) {
                reader = new BufferedReader(new FileReader(fileName));
            }
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Website> readAll() {
        Set<Website> websites= new HashSet<>();
        try {
            reader= new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                int size= line.split(",").length;

                Website w = new Website(line.split(",")[0]
                        ,line.split(",")[1],line.split(",")[2],
                        Arrays.stream(line.split(",")[3].split("\\|")).toList());

                websites.add(w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return websites;
    }


    @Override
    public void writeAll(Set<Website> list) {
        try {
            writer= new BufferedWriter(new FileWriter(fileName));
            writer.write("");
            for (Website w:list) {
                writer.append(w.toString()+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
