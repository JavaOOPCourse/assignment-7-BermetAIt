import java.io.*;
import java.util.*;

public class StudentRecordProcessor {

    // Поля для хранения данных
    private final List<Student> students = new ArrayList<>();
    private double averageScore;
    private Student highestStudent;

    // ===== Task 6: Custom Exception =====
    static class InvalidScoreException extends Exception {
        public InvalidScoreException(String message) {
            super(message);
        }
    }

    // ===== Класс Student =====
    static class Student {
        String name;
        int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return name + ", " + score;
        }
    }

    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     * Чтение файла с обработкой исключений
     */
    public void readFile() {
        // Task 5: try-with-resources для автоматического закрытия файла
        try (BufferedReader br = new BufferedReader(new FileReader("data/students.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    // Разделяем строку по запятой: "Alice,85" -> ["Alice", "85"]
                    String[] parts = line.split(",");

                    if (parts.length != 2) {
                        throw new InvalidScoreException("Wrong format");
                    }

                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim()); // Может вылететь NumberFormatException

                    // Task 6: Проверка диапазона оценок
                    if (score < 0 || score > 100) {
                        throw new InvalidScoreException("Score out of range (0-100)");
                    }

                    // Если всё ок — добавляем студента в список
                    students.add(new Student(name, score));

                } catch (NumberFormatException | InvalidScoreException e) {
                    // Task 2: Обработка ошибки без остановки программы
                    System.out.println("Invalid data: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            // Task 2: Файл не найден
            System.err.println("Error: File 'data/students.txt' not found!");
        } catch (IOException e) {
            // Task 2: Ошибка чтения
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     * Обработка данных: расчёты и сортировка
     */
    public void processData() {
        if (students.isEmpty()) {
            System.out.println("No valid students to process.");
            return;
        }

        // Task 3: Вычисление среднего балла
        int total = 0;
        for (Student s : students) {
            total += s.score;
        }
        averageScore = (double) total / students.size();

        // Task 3: Поиск студента с наивысшим баллом
        highestStudent = students.get(0);
        for (Student s : students) {
            if (s.score > highestStudent.score) {
                highestStudent = s;
            }
        }

        // Task 8: Сортировка по убыванию (от большего к меньшему)
        students.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
    }

    /**
     * Task 4 + Task 5 + Task 8
     * Запись отчета в файл
     */
    public void writeFile() {
        // Создаём папку output, если её нет (важно!)
        new File("output").mkdirs();

        // Task 5: try-with-resources для записи
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output/report.txt"))) {

            // Task 4: Запись статистики
            bw.write("Average: " + averageScore);
            bw.newLine();

            if (highestStudent != null) {
                bw.write("Highest: " + highestStudent.name + " - " + highestStudent.score);
            }
            bw.newLine();
            bw.newLine();

            // Task 8: Запись отсортированного списка
            bw.write("--- Sorted Students (by score) ---");
            bw.newLine();
            for (Student s : students) {
                bw.write(s.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();    // Шаг 1: Чтение
            processor.processData(); // Шаг 2: Обработка
            processor.writeFile();   // Шаг 3: Запись
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}