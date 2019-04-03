/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import attendance.automation.bll.AAManager;
import attendance.automation.dal.ConnectionProvider;
import attendance.automation.dal.DALException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Revy
 */
public class Teacher implements Person {
    private String name;
    private int id;
    private List<Class> listOfClasses;
    private AAManager manager;

    public Teacher(String name, int id) throws IOException, DALException {
        this.name = name;
        this.id = id;
        listOfClasses = new ArrayList<>();
        manager = AAManager.getInstance();
        loadTeacherContent();
    }

    public void loadTeacherContent() throws DALException, IOException {
        listOfClasses.addAll(manager.loadTeacherContent(this.id));
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

    public void setClassesList(List<Class> list) {
        listOfClasses.addAll(list);
    }

    public List<Class> getClassesList() {
        return listOfClasses;
    }
}
