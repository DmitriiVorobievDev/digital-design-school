package com.didges.school;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarterRunner {

    public static void main(String[] args) throws Exception {

        JavaSchoolStarter starter = new JavaSchoolStarter();
        List<Map<String, Object>> result1 = starter.execute("INSERT VALUES 'id' = 1 , 'lastName' = 'Petrov', 'age' = 30 , 'cost' = 5.4, 'active' = true");
        List<Map<String, Object>> result2 = starter.execute("INSERT VALUES 'id' = 2 , 'lastName' = 'Ivanov', 'age' = 25 , 'cost' = 4.3, 'active' = false");
        List<Map<String, Object>> result3 = starter.execute("INSERT VALUES 'id' = 3 , 'lastName' = 'Valuev', 'age' = 15, 'cost' = 12.5, 'active' = true");
        List<Map<String, Object>> result4 = starter.execute("INSERT VALUES 'id' = 10 , 'lastName' = 'Klimov', 'age' = 60, 'cost' = 100.5, 'active' = true");
        List<Map<String, Object>> result5 = starter.execute("INSERT VALUES 'id' = 20 , 'lastName' = 'Svetikov', 'age' = 15, 'cost' = 20.5, 'active' = true");

        System.out.println("-------------------------------------------------------");
        System.out.println(starter.getTable());
        System.out.println("-------------------------------------------------------");

        //Запросы для проверки

//        try {
//            List<Map<String, Object>> result11 = starter.execute("SELECT");
//            starter.printTable(result11);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
        try {
            List<Map<String, Object>> result12 = starter.execute("UPDATE VALUES 'age'=35, 'cost'=1000.1 WHERE 'id' = 3 or 'age' = 30 ");
            starter.printTable(result12);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//
//        try {
//            List<Map<String, Object>> result13 = starter.execute("UPDATE VALUES 'age'=99, 'cost'=1000.1 WHERE 'lastname' like 'Ivanov'");
//            starter.printTable(result13);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//       try {
//            List<Map<String, Object>> result14 = starter.execute("SElECT WHERE  'age'= 30 ");
//            starter.printTable(result14);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            List<Map<String, Object>> result15 = starter.execute("DELETE WHERE  'cost' = 5.4 or 'age' = 25 ");
//            starter.printTable(starter.getTable());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            List<Map<String, Object>> result16 = starter.execute("INSERT VALUES  'cost' = 1 ,'age' = 45");
//            starter.printTable(starter.getTable());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            List<Map<String, Object>> result17 = starter.execute("SELECT WHERE 'lastName' like 'Petrov' or 'lastName' ilike 'ivanov'");
//            starter.printTable(result17);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        try {
//            List<Map<String, Object>> result18 = starter.execute("DELETE");
//            starter.printTable(result18);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }
}