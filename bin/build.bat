@echo off
echo === Сборка Maze Game ===
echo.

REM Указываем путь к JDK
set JDK_PATH=C:\Users\rolan\javarush\sdk\jdk17.0.15_6\bin

REM 1. Очистка
echo 1. Очистка предыдущей сборки...
if exist bin rmdir /s /q bin
if exist MazeGame.jar del MazeGame.jar

REM 2. Компиляция
echo 2. Компиляция Java файлов...
mkdir bin
cd src\MyGame2
"%JDK_PATH%\javac" -d ..\..\bin -encoding UTF-8 *.java
if errorlevel 1 (
    echo Ошибка компиляции!
    pause
    exit /b 1
)

REM 3. Создание JAR
echo 3. Создание JAR файла...
cd ..\..
echo Manifest-Version: 1.0> manifest.tmp
echo Main-Class: MyGame2.VisualMazeGame>> manifest.tmp
"%JDK_PATH%\jar" cfm MazeGame.jar manifest.tmp -C bin .
del manifest.tmp

REM 4. Проверка
echo 4. Проверка сборки...
if exist MazeGame.jar (
    echo Сборка успешно завершена!
    echo Файл: MazeGame.jar
    echo Размер: %~z0 байт
    echo.
    echo Для запуска используйте:
    echo java -jar MazeGame.jar
) else (
    echo Ошибка: JAR файл не создан!
)

pause