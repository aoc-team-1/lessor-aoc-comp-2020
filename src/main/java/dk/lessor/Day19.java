package dk.lessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {

    public static void main(String[] args) {
        new Day19().runner();
    }

    public void runner() {

        List<String> data = FileUtilsKt.readFile("day_19.txt");
        puzzleOne(data);
//        puzzleTwo(data);
    }

    private void puzzleOne(List<String> data) {
        Map<String, String> rules = data.stream().filter(s -> s.matches("^\\d+:.*")).collect(Collectors.toMap(o -> {
            int index = o.indexOf(":");
            return o.substring(0, index);
        }, o -> o.replaceAll("^\\d+:|\"", "")));

        final Pattern pattern = Pattern.compile(rulesToReg(rules, "0"));
        List<String> messages = data.stream().filter(message -> pattern.matcher(message).matches()).collect(Collectors.toList());
        System.out.println(messages.size());
    }

    private String rulesToReg(Map<String, String> rules, String startingRule) {
        String regex = "";
        String startingRuleInfo = rules.get(startingRule);
        String[] ruleParts = startingRuleInfo.split("\\|");
        List<String> elements = new ArrayList<>();
        for (String rulePart : ruleParts) {
            if (rulePart.matches("\\D+")) {
                regex += rulePart.trim();
                break;
            }

            String join = Arrays.stream(rulePart.trim().split(" ")).map(s -> rulesToReg(rules, s)).collect(Collectors.joining(""));
            elements.add(join);
        }
        if (elements.size() > 0) {
            if (elements.size() > 1) {
                regex += "(" + String.join("|", elements) + ")";
            } else {
                regex += elements.get(0);
            }
        }

        return regex;
    }

    private void puzzleTwo(List<String> data) {
    }
}
