package com.example.repository;


import java.util.Set;

public interface IFileRepository<T> {

    String readLine();

    Set<T> readAll();

    void writeAll(Set<T> list);


}
