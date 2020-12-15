package dk.lessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) {
        new Day15().run();
    }

    private void run() {
        List<Long> inputs = Arrays.stream(FileUtilsKt.readFile("day_15.txt").get(0).split(",")).map(Long::parseLong).collect(Collectors.toList());
        puzzle(inputs, 2020);
        puzzle(inputs, 30000000);
    }

    private void puzzle(List<Long> inputs, int i) {
        Map<Long, LinkedList<Long>> saved = new HashMap<>();
        long count = inputs.size();
        long nextNumber = inputs.get(inputs.size() - 1);
        for (long index = 0; index < inputs.size(); index++) {
            saved.put(inputs.get((int) index), new LinkedList<>(List.of(index)));
        }
        while (count < i) {
            LinkedList<Long> lastNumber = saved.get(nextNumber);
            nextNumber = lastNumber.size() > 1 ? lastNumber.get(1) - lastNumber.get(0) : 0;

            LinkedList<Long> linkedList = saved.getOrDefault(nextNumber, new LinkedList<>());
            linkedList.addLast(count);
            if (linkedList.size() > 2) {
                linkedList.removeFirst();
            }
            saved.put(nextNumber, linkedList);
            count++;
        }
        System.out.println(nextNumber);
    }
}
