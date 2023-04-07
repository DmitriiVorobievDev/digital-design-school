package com.didges.school;

import java.util.*;

// Главный класс для работы с SQL запросами
public class JavaSchoolStarter {
    // Наша таблица
    public static List<Map<String, Object>> table = new ArrayList<>();
    // Класс для считывания строк в консоли
    static Scanner sc = new Scanner(System.in);

    // Все ключи в нашей таблице
    static Set<String> keys;

    // Все ключи в нашей таблице
    static Set<String> keys_lowercase = new HashSet<>();

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
//      a)	INSERT - вставка элемента в коллекцию,
//      b)	UPDATE  изменение элемента в коллекции,
//      c)	DELETE - удаление элемента из коллекции,
//      d)	SELECT - поиск элементов в коллекции.
        request = request.strip();

        System.out.println(request);

        if (request.toLowerCase().strip().startsWith("select")) {
            if (table.isEmpty()) {
                return new ArrayList<>();
            }

            if (request.strip().equalsIgnoreCase("select")) {
                return table;
            } else if (request.toLowerCase().startsWith("select where")) {

                // Лист условий, которые стоят между and
                List<List<Map<String, Object>>> List_tables_and = new ArrayList<>();

                request = request.substring(12);
                String[] request_str = request.split("and");

                for (String str_and : request_str) {

                    if (str_and.contains("or")) {
                        // Лист условий, которые стоят между or
                        Set<Map<String, Object>> Set_tables_or = new HashSet<>();
                        String[] str_or = str_and.split("or");
                        for (String logex : str_or) {
                            Set_tables_or.addAll(logex(logex));
                        }
                        List<Map<String, Object>> List_tables_or = new ArrayList<>(Set_tables_or);
                        List_tables_and.add(List_tables_or);
                    } else {
                        List_tables_and.add(logex(str_and));
                    }
                }

                if (List_tables_and.size() == 1) {
                    return List_tables_and.get(0);
                }

                Map<Map<String, Object>, Integer> counter = new HashMap<>();
                for (List<Map<String, Object>> list : List_tables_and) {
                    if (list.isEmpty()) {
                        return new ArrayList<>();
                    }
                    for (Map<String, Object> row : list) {
                        counter.merge(row, 1, Integer::sum);
                    }
                }

                List<Map<String, Object>> finalList = new ArrayList<>();
                for (Map<String, Object> key : counter.keySet()) {
                    if (counter.get(key) == List_tables_and.size()) {
                        finalList.add(key);
                    }
                }
                if (finalList.isEmpty()) {
                    System.out.println("Результат пустой");
                    return new ArrayList<>();
                } else {
                    printTable(finalList);
                    return finalList;
                }
            } else {
                System.out.println("Комманда введена неверно");
            }
        } else if (request.toLowerCase().strip().startsWith("insert values")) {
            String[] values = request.substring(14).split(",");
            HashMap<String, Object> row = new LinkedHashMap<>();
            for (String value : values) {
                String[] new_value = value.split("=");
                String column = new_value[0].strip();
                column = column.substring(1, column.length() - 1);
                String v = new_value[1].strip();
                if (v.startsWith("'") && (v.endsWith("'"))) {
                    v = v.substring(1, v.length() - 1);
                }
                if (v.equals("null")) {
                    row.put(column, null);
                } else if (column.equalsIgnoreCase("id")) {
                    row.put("id", Long.parseLong(v));
                } else if (column.equalsIgnoreCase("age")) {
                    row.put("age", Long.parseLong(v));
                } else if (column.equalsIgnoreCase("cost")) {
                    row.put("cost", Double.parseDouble(v));
                } else if (column.equalsIgnoreCase("active")) {
                    row.put("active", Boolean.parseBoolean(v));
                } else {
                    row.put("lastName", v);
                }
            }

            HashMap<String, Object> new_row = new LinkedHashMap<>();
            try {
                new_row.put("id", row.get("id"));
            } catch (Exception e) {
                new_row.put("id", null);
            }
            try {
                new_row.put("lastName", row.get("lastName"));
            } catch (Exception e) {
                new_row.put("lastName", null);
            }
            try {
                new_row.put("age", row.get("age"));
            } catch (Exception e) {
                new_row.put("age", null);
            }
            try {
                new_row.put("cost", row.get("cost"));
            } catch (Exception e) {
                new_row.put("cost", null);
            }
            try {
                new_row.put("active", row.get("active"));
            } catch (Exception e) {
                new_row.put("active", null);
            }

            table.add(new_row);
            keys = table.get(0).keySet();
            for (Map<String, Object> stringObjectMap : table) {
                keys = stringObjectMap.keySet();
                for (String key : keys) {
                    keys_lowercase.add(key.toLowerCase());
                }
                break;
            }
            return table;

        } else if (request.toLowerCase().strip().startsWith("update values")) {
            // UPDATE
            if (request.toLowerCase().contains("where")) {
                String[] values = request.substring(13, request.toLowerCase().indexOf("where")).split(",");
                List<List<Map<String, Object>>> List_tables_and = new ArrayList<>();
                String[] request_str = request.substring(request.toLowerCase().indexOf("where") + 5).split("and");
                for (String str_and : request_str) {

                    if (str_and.contains("or")) {
                        // Лист таблиц которые стоят между or
                        Set<Map<String, Object>> Set_tables_or = new HashSet<>();
                        String[] str_or = str_and.split("or");
                        for (String logex : str_or) {
                            Set_tables_or.addAll(logex(logex));
                        }
                        List<Map<String, Object>> List_tables_or = new ArrayList<>(Set_tables_or);
                        List_tables_and.add(List_tables_or);
                    } else {
                        List_tables_and.add(logex(str_and));
                    }
                }

                if (List_tables_and.size() == 1) {
                    return update_values(List_tables_and.get(0), values);
//                    return List_tables_and.get(0);
                }

                Map<Map<String, Object>, Integer> counter = new HashMap<>();
                for (List<Map<String, Object>> list : List_tables_and) {
                    if (list.isEmpty()) {
                        System.out.println("Результат пустой");
                        return new ArrayList<>();
                    }
                    for (Map<String, Object> row : list) {
                        counter.merge(row, 1, Integer::sum);
                    }
                }

                List<Map<String, Object>> finalList = new ArrayList<>();
                for (Map<String, Object> key : counter.keySet()) {
                    if (counter.get(key) == List_tables_and.size()) {
                        finalList.add(key);
                    }
                }
                if (finalList.isEmpty()) {
                    return table;
                } else {
                    return update_values(finalList, values);
                }
            } else {
                String[] values = request.substring(13).split(",");
                return update_values(table, values);
            }
        } else if (request.toLowerCase().strip().startsWith("delete")) {
            if (request.toLowerCase().strip().equals("delete")) {
                System.out.println("Таблица удалена");
                table = new ArrayList<>();
                return table;
            } else if (request.toLowerCase().startsWith("delete where")) {
                List<List<Map<String, Object>>> List_tables_and = new ArrayList<>();
                String[] request_str = request.substring(12).split("and");

                for (String str_and : request_str) {
                    if (str_and.contains("or")) {
                        // Лист таблиц которые стоят между or
                        Set<Map<String, Object>> Set_tables_or = new HashSet<>();
                        String[] str_or = str_and.split("or");
                        for (String logex : str_or) {
                            Set_tables_or.addAll(logex(logex));
                        }
                        List<Map<String, Object>> List_tables_or = new ArrayList<>(Set_tables_or);
                        List_tables_and.add(List_tables_or);
                    } else {
                        List_tables_and.add(logex(str_and));

                    }
                }

                if (List_tables_and.size() == 1) {
                    return delete_values(List_tables_and.get(0));
                }

                Map<Map<String, Object>, Integer> counter = new HashMap<>();
                for (List<Map<String, Object>> list : List_tables_and) {
                    if (list.isEmpty()) {
                        System.out.println("Результат пустой");
                        return new ArrayList<>();
                    }
                    for (Map<String, Object> row : list) {
                        counter.merge(row, 1, Integer::sum);
                    }
                }

                List<Map<String, Object>> finalList = new ArrayList<>();
                for (Map<String, Object> key : counter.keySet()) {
                    if (counter.get(key) == List_tables_and.size()) {
                        finalList.add(key);
                    }
                }
                if (finalList.isEmpty()) {
                    return table;
                } else {
                    return delete_values(finalList);
                }
            } else {
                String[] values = request.substring(13).split(",");
                return delete_values(table);
            }

        } else {
            System.out.println("Нет такой команды");
        }

        return new ArrayList<>();
    }

    public List<Map<String, Object>> update_values(List<Map<String, Object>> update_table, String[] values) {
        for (Map<String, Object> stringObjectMap : table) {
            for (Map<String, Object> stringObjectMap1 : update_table) {
                if (stringObjectMap == stringObjectMap1) {
                    for (String key : stringObjectMap.keySet()) {

                        for (String value : values) {
                            String[] new_value = value.split("=");
                            String column = new_value[0].strip();
                            column = column.substring(1, column.length() - 1).toLowerCase();
                            String v = new_value[1].strip();
                            if (v.startsWith("’") && (v.endsWith("‘"))) {
                                v = v.substring(1, v.length() - 1);

                            }
                            if (key.toLowerCase().equals(column)) {
                                stringObjectMap.put(key, v);
                            }

                        }
                    }
                }
            }
        }

        return table;
    }

    public List<Map<String, Object>> delete_values(List<Map<String, Object>> delete_table) {
        List<Map<String, Object>> new_table = new ArrayList<>(List.copyOf(table));
        for (Map<String, Object> stringObjectMap : table) {
            for (Map<String, Object> stringObjectMap1 : delete_table) {
                if (stringObjectMap == stringObjectMap1) {
                    new_table.remove(stringObjectMap);
                }
            }
        }
        table = new ArrayList<>(List.copyOf(new_table));
        return table;
    }

    public void printTable(List<Map<String, Object>> table) {


        try {
            for (String key : table.get(0).keySet()) {
                System.out.printf("%-" + 10 + "s", key);
            }
            System.out.println();
            for (Map<String, Object> stringObjectMap : table) {
                for (String key : stringObjectMap.keySet()) {
                    System.out.printf("%-" + 10 + "s", stringObjectMap.get(key));

                }
                System.out.println();

            }

            System.out.println();
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Table is empty");
        }
    }

    public static List<Map<String, Object>> logex(String logex) {
        // Long, Double, Boolean, String
        if (logex.contains("!=")) {
            String[] l = logex.split("!=");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();

            if (cond.equals("null")) {
                cond = null;
            }

            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.toLowerCase().equals(new_column)) {
                            if ((cond != stringObjectMap.get(key)) && (cond == null) && (stringObjectMap.get(key) == null)) {
                                table_logex.add(stringObjectMap);
                            }
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (!new_value.equals(new_cond)) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                Double new_value = (double) stringObjectMap.get(key);
                                Double new_cond = Double.parseDouble(cond);
                                if (!new_value.equals(new_cond)) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                if ((cond.equals("true")) || cond.equals("false")) {
                                    Boolean new_cond = Boolean.parseBoolean(cond);
                                    if (!(stringObjectMap.get(key) == new_cond)) {
                                        table_logex.add(stringObjectMap);
                                    }
                                    continue;
                                }

                            } catch (Exception ignored) {
                            }

                            try {
                                String new_value = ((String) stringObjectMap.get(key));
                                if ((cond.startsWith("'") && cond.startsWith("'"))) {
                                    cond = cond.substring(1, cond.length() - 1);
                                }
                                if (!new_value.equals(cond)) {
                                    table_logex.add(stringObjectMap);
                                }

                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }
        } else if (logex.contains(">=")) {

            String[] l = logex.split(">=");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();
            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.equals(new_column)) {
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (new_value >= new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                double new_value = (double) stringObjectMap.get(key);
                                double new_cond = Double.parseDouble(cond);
                                if (new_value >= new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }

        } else if (logex.contains("<=")) {

            String[] l = logex.split("<=");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();

            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.equals(new_column)) {
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (new_value <= new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                double new_value = (double) stringObjectMap.get(key);
                                double new_cond = Double.parseDouble(cond);
                                if (new_value <= new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }

        } else if (logex.contains(">")) {
            String[] l = logex.split(">");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();
            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.equals(new_column)) {
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (new_value > new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                double new_value = (double) stringObjectMap.get(key);
                                double new_cond = Double.parseDouble(cond);
                                if (new_value > new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }
        } else if (logex.contains("<")) {
            String[] l = logex.split("<");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();
            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.equals(new_column)) {
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (new_value < new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                double new_value = (double) stringObjectMap.get(key);
                                double new_cond = Double.parseDouble(cond);
                                if (new_value < new_cond) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }

        } else if (logex.contains("ilike")) {
            String[] l = logex.split("ilike");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip().toLowerCase();
            cond = cond.replaceAll("%", ".*");
            String new_column = column.substring(1, column.length() - 1);
            String new_cond = cond.substring(1, cond.length() - 1);

            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.toLowerCase().equals(new_column)) {
                            if (stringObjectMap.get(key).toString().toLowerCase().matches(new_cond)) {
                                table_logex.add(stringObjectMap);
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }
        } else if (logex.contains("like")) {
            String[] l = logex.split("like");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();
            String new_column = column.substring(1, column.length() - 1);
            cond = cond.replaceAll("%", ".*");
            String new_cond = cond.substring(1, cond.length() - 1);

            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.toLowerCase().equals(new_column)) { // ?
                            if (stringObjectMap.get(key).toString().matches(new_cond)) {
                                table_logex.add(stringObjectMap);
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }

        } else if (logex.contains("=")) {
            String[] l = logex.split("=");
            String column = l[0].strip().toLowerCase();
            String cond = l[1].strip();
            if (cond.equals("null")) {
                cond = null;
            }
            String new_column = column.substring(1, column.length() - 1);
            if ((column.startsWith("'") && column.endsWith("'")) && (keys_lowercase.contains(new_column))) {
                List<Map<String, Object>> table_logex = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : table) {
                    for (String key : stringObjectMap.keySet()) {
                        if (key.toLowerCase().equals(new_column)) {
                            if ((cond == stringObjectMap.get(key)) && (cond == null) && (stringObjectMap.get(key) == null)) {
                                table_logex.add(stringObjectMap);
                            }
                            try {
                                Long new_value = (Long) stringObjectMap.get(key);
                                Long new_cond = Long.parseLong(cond);
                                if (new_value.equals(new_cond)) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                Double new_value = (double) stringObjectMap.get(key);
                                Double new_cond = Double.parseDouble(cond);
                                if (new_value.equals(new_cond)) {
                                    table_logex.add(stringObjectMap);
                                }
                                continue;
                            } catch (Exception ignored) {
                            }
                            try {
                                if ((cond.equals("true")) || cond.equals("false")) {
                                    Boolean new_cond = Boolean.parseBoolean(cond);
                                    if (stringObjectMap.get(key) == new_cond) {
                                        table_logex.add(stringObjectMap);
                                    }
                                    continue;
                                }

                            } catch (Exception ignored) {
                            }

                            try {
                                String new_value = ((String) stringObjectMap.get(key));
                                if ((cond.startsWith("'") && cond.startsWith("'"))) {
                                    cond = cond.substring(1, cond.length() - 1);
                                }
                                if (new_value.equals(cond)) {
                                    table_logex.add(stringObjectMap);
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                return table_logex;
            } else {
                return new ArrayList<>();
            }

        } else {
            System.out.println("Нет логического оператора");
            return new ArrayList<>();
        }
    }

    public static List<Map<String, Object>> getTable() {
        return table;
    }
}