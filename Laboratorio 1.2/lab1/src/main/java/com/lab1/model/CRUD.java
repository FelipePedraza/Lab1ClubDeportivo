package com.lab1.model;
import java.util.List;

public interface CRUD<T> {

    void crear(T t);    
    List<T> listar();
    void actualizar(T t);
    void eliminar(T t);
    
}