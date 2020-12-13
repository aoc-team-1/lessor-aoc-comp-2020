package dk.lessor;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
    public static void main(String[] args) {
        new Day13().runner();
    }

    public void runner() {
        List<String> input = FileUtilsKt.readFile("day_13.txt");
        puzzleOne(input);
        puzzleTwo(input);
    }

    private void puzzleOne(List<String> input) {
        Pair<Integer, Double> found = null;
        Double estimatedTime = Double.parseDouble(input.get(0));
        List<Integer> busses = Arrays.stream(input.get(1).split(",")).filter(s -> !s.equals("x")).map(Integer::parseInt).collect(Collectors.toList());
        for (int bus : busses) {
            double nextBusTime = Math.ceil(estimatedTime / bus);
            double waitTime = (nextBusTime * bus) - estimatedTime;
            if (found == null || (found.getSecond() > waitTime && found.getSecond() > waitTime)) {
                found = new Pair<>(bus, waitTime);
            }
        }
        System.out.println((int) (found.getFirst() * found.getSecond()));
    }

    private void puzzleTwo(List<String> input) {
        List<Integer> inputList = Arrays.stream(input.get(1).split(",")).map(s1 -> {
            if (s1.equals("x")) {
                return -1;
            }
            return Integer.parseInt(s1);
        }).collect(Collectors.toList());

        List<Bus> busses = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            Integer busNumber = inputList.get(i);
            if (busNumber == -1) {
                continue;
            }
            busses.add(new Bus(busNumber, i));
        }

        Long resetStart = 0L;

        Bus bus = busses.get(0);
        long skip = bus.getNumber();

        long earliestTimestamp = 0L;

        for (int i = 1; i < busses.size(); i++) {
            long count = 1;
            boolean found;
            Bus nextBus;
            do {
                earliestTimestamp = resetStart + skip * count;
                nextBus = busses.get(i);
                found = (earliestTimestamp + nextBus.getTimeShift()) % nextBus.getNumber() == 0;
                count++;
            } while (!found);
            resetStart = earliestTimestamp;
            skip *= nextBus.getNumber();
        }

        System.out.println(earliestTimestamp);
    }

    static class Bus implements Comparable<Bus> {

        long number;
        long timeShift;

        public Bus(long number, long timeShift) {
            this.number = number;
            this.timeShift = timeShift;
        }

        public long getNumber() {
            return number;
        }

        public long getTimeShift() {
            return timeShift;
        }

        @Override
        public int compareTo(@NotNull Bus o) {
            return (int) (o.number - number);
        }
    }

//    private boolean nextBus(List<Integer> busses, int nextBus, long earliestTimestamp) {
//        Integer currentBus = busses.get(nextBus);
//        boolean b = earliestTimestamp % currentBus == 0;
//        boolean outOffService = currentBus == -1;
//        if(!(b || outOffService)){
//            return false;
//        }
//        boolean next = busses.size() == nextBus + 1 || nextBus(busses, nextBus + 1, earliestTimestamp + 1);
//        return next;
//    }
}
