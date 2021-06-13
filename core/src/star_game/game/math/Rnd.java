package star_game.game.math;

import java.util.Random;

/**
 * Генератор случайных чисел
 * * @param min минимальное значение случайного числа
 * * @param max максимальное значение случайного числа
 * * @return результат
 */
public class Rnd {

    private static final Random random = new Random();

    public static float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static double nextDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public static int nextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static int genSign() {
        return (Math.random()) < 0.5 ? -1 : 1;
    }
}
