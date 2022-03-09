package com.ylab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ticTacToe {

    public static ArrayList<HashSet<String>> codeOfWin;
    public static HashSet<String> allGameMoves;
    public static ArrayList<String> movesOfFirstPlayer, movesOfSecondPlayer;
    public static char[][] arr = new char[3][3];

    public static void initCodeOfWin() {
        codeOfWin = new ArrayList<>();
        codeOfWin.add(new HashSet<>(Arrays.asList("1 1", "2 1", "3 1")));
        codeOfWin.add(new HashSet<>(Arrays.asList("1 2", "2 2", "3 2")));
        codeOfWin.add(new HashSet<>(Arrays.asList("1 3", "2 3", "3 3")));
        codeOfWin.add(new HashSet<>(Arrays.asList("1 1", "1 2", "1 3")));
        codeOfWin.add(new HashSet<>(Arrays.asList("2 1", "2 2", "2 3")));
        codeOfWin.add(new HashSet<>(Arrays.asList("3 1", "3 2", "3 3")));
        codeOfWin.add(new HashSet<>(Arrays.asList("1 1", "2 2", "3 3")));
        codeOfWin.add(new HashSet<>(Arrays.asList("1 3", "2 2", "3 1")));
    }

    public static void printFieldGame(char[][] arr) {
        for (char[] value : arr) {
            System.out.print("|");
            for (char c : value) {
                System.out.print(c + "|");
            }
            System.out.println();
        }
    }

    public static boolean isCorrectMove(String move) {
        if (!move.matches("\\A[1-3]\\s[1-3]\\Z")) {
            System.out.println("Ход введен неправильно! Введите в формате \"1 2\" - номер строки, пробел, номер столбца.");
            return false;
        } else if (!allGameMoves.add(move)) {
            System.out.println("Такой ход уже был! Введите еще раз!");
            return false;
        } else return true;
    }

    public static void writeRatingInFile(String result) throws IOException {
        Path path = Path.of("./results.txt");
        if (!Files.isRegularFile(path))
            Files.createFile(path);
        Files.writeString(path, new Date() + System.lineSeparator(), StandardOpenOption.APPEND);
        Files.writeString(path, result + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    public static void writeMoveOnField(boolean isFirstPlayerMove, String move){
        if (isFirstPlayerMove) {
            movesOfFirstPlayer.add(move);
            int i = Integer.parseInt(move.substring(0, 1)) - 1;
            int j = Integer.parseInt(move.substring(2)) - 1;
            arr[i][j] = 'X';
        } else {
            movesOfSecondPlayer.add(move);
            int i, j;
            i = Integer.parseInt(move.substring(0, 1)) - 1;
            j = Integer.parseInt(move.substring(2)) - 1;
            arr[i][j] = '0';
        }
    }

    public static void main(String[] args) throws IOException {

        String name1, name2, move;
        Scanner scanner = new Scanner(System.in);

        boolean isAnotherTry = true;
        initCodeOfWin();

        System.out.println("Приветствуем вас в игре крестики-нолики");
        System.out.println("Правила игры просты. Игровое поле представляет собой сетку размером 3 на 3 клетки. Играют два игрока.");
        System.out.println("Первый игрок ходит крестиками - \"X\", второй ноликами - \"0\".");

        while (isAnotherTry) {
            int countOfMoves = 0;
            boolean endGame = false, isFirstPlayerMove = true;
            //сброс поля, записей ходов
            movesOfFirstPlayer = new ArrayList<>();
            movesOfSecondPlayer = new ArrayList<>();
            allGameMoves = new HashSet<>();

            for (char[] chars : arr) {
                Arrays.fill(chars, '-');
            }
            System.out.println("Новая игра началась!");
            System.out.println("Введите имя первого игрока: ");
            name1 = scanner.nextLine();
            System.out.println("Введите имя второго игрока: ");
            name2 = scanner.nextLine();
            System.out.println("Игра начинается " + name1 + " и " + name2 + ". Первым ходит: " + name1 + " и он ходит \"X\" - крестиками,");
            System.out.println("а " + name2 + " ходит 0 - ноликами");
            System.out.println("Для того чтобы сходить введите номер строки (от 1 до 3) и через пробел номер столбца (также от 1 до 3), например \"2 2\" выберет центральную клетку сетки");

            while (!endGame) {

                countOfMoves++;
                printFieldGame(arr);

                if (isFirstPlayerMove) {
                    System.out.println("Ход игрока " + name1);
                } else {
                    System.out.println("Ход игрока " + name2);
                }
                //проверка хода - правильно ли введены координаты, не повторяется ли ход
                do {
                    move = scanner.nextLine();
                } while (!isCorrectMove(move));
                //запись хода на поле
                writeMoveOnField(isFirstPlayerMove, move);
                //проверка на конец игры
                for (HashSet<String> hs : codeOfWin) {
                    if (movesOfFirstPlayer.containsAll(hs)) {
                        String result = "Победил: " + name1 + " проиграл: " + name2;
                        printFieldGame(arr);
                        System.out.println(result);
                        endGame = true;
                        writeRatingInFile(result);

                    } else if (movesOfSecondPlayer.containsAll(hs)) {
                        String result = "Победил: " + name2 + " проиграл: " + name1;
                        System.out.println(result);
                        printFieldGame(arr);
                        endGame = true;
                        writeRatingInFile(result);
                    }
                }
                //проверка на ничью
                if (countOfMoves == 9 && !endGame) {
                    String result = "Ничья между " + name1 + " и " + name2;
                    System.out.println(result);
                    printFieldGame(arr);
                    endGame = true;
                    writeRatingInFile(result);
                }
                isFirstPlayerMove = !isFirstPlayerMove;
            }

            System.out.println("Хотите сыграть еще раз? (y,n)");
            if (!scanner.nextLine().equals("y"))
                isAnotherTry = false;
        }
        System.out.println("Приходите еще!");
    }
}
