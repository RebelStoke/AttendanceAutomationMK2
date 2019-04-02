/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import attendance.automation.bll.AAManager;
import attendance.automation.dal.DALException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Revy
 */
public class Class {

    private final List<Student> listOfStudents;
    private String name;
    private int id;
    private AAManager manager;

    public Class(String name, int id) throws IOException, DALException {
        this.name = name;
        this.id = id;
        listOfStudents = new ArrayList<>();
        manager = AAManager.getInstance();
        loadClassContent(name);
    }

    private void loadClassContent(String name) throws DALException, IOException {
        listOfStudents.addAll(manager.loadClassContent(name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Student> getStudentsList() {
        listOfStudents.sort(Student::compareAbsence);
        return listOfStudents;
    }

    public void setStudentsList(List<Student> list) {
        listOfStudents.addAll(list);
    }

    @Override
    public String toString() {
        return name;
    }
}