package studio.karllang;

import nl.altindag.console.ConsoleCaptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import studio.karllang.karl.Karl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KarlTest {
    private final Karl karl = new Karl();
    @Test
    @DisplayName("Show function")
    void testMain() {
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();
        karl.run("src/test/resources/tests/Show.karl", null);
        Assertions.assertEquals(List.of(new String[]{"Hello, World!"}), consoleCaptor.getStandardOutput());
    }

    @Test
    @DisplayName("If statements")
    void testIf() {
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success", "Test #5 success", "Test #6 success", "Test #7 success", "Test #8 success", "Test #9 success", "Test #10 success", "Test réussit !! ^U^", "true", "Yay!", "true", "Yay!2", "true", "Yay!3", "Yay!4"};
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();
        karl.run("src/test/resources/tests/If.karl", null);
        Assertions.assertEquals(List.of(expected), consoleCaptor.getStandardOutput());
    }

    @Test
    @DisplayName("Functions")
    void testFunctions() {
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();
        String[] expected = {"What is your name?", "Hello, Karl!", "Hello, World!", "11", "8", "1"};
        karl.run("src/test/resources/tests/Functions.karl", null);
        Assertions.assertEquals(List.of(expected), consoleCaptor.getStandardOutput());
    }

    @Test
    @DisplayName("Else statements")
    void testElse() {
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success"};
        karl.run("src/test/resources/tests/Else.karl", null);
        Assertions.assertEquals(List.of(expected), consoleCaptor.getStandardOutput());
    }

    @Test
    @DisplayName("Mathematical operations")
    void mathTest() {
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();
        String[] expected = {"17", "14", "22", "14", "36", "36", "36", "7.3333335", "6", "7.3333335"};
        karl.run("src/test/resources/tests/Math.karl", null);
        Assertions.assertEquals(List.of(expected), consoleCaptor.getStandardOutput());
    }
}