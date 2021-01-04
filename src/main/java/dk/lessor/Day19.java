package dk.lessor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {

    public static void main(String[] args) {
        new Day19().runner();
    }

    public void runner() {
        List<String> data = FileUtilsKt.readFile("day_19.txt");
        puzzleOne(data);
        puzzleTwo(data);
    }

    private String checkRegex(String regex, String string) {
        final Pattern pattern = Pattern.compile("^" + regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private void puzzleOne(List<String> data) {
        Map<Integer, String> rules = mapRules(data);
        Node.map.clear();
        Node node = new Node(0);

        node.value = rulesToReg(rules, 0);

        matchMessages(data, node);
    }

    @NotNull
    private Map<Integer, String> mapRules(List<String> data) {
        return data.stream().filter(s -> s.matches("^\\d+:.*")).collect(Collectors.toMap(o -> {
            int index = o.indexOf(":");
            return Integer.parseInt(o.substring(0, index));
        }, o -> o.replaceAll("^\\d+:|\"", "")));
    }

    private void puzzleTwo(List<String> data) {
        Map<Integer, String> rules = mapRules(data);
        Node.map.clear();
        Node node = parseDataToNodes(rules, 0);

        Node node8 = Node.map.get(8);
        node8.rightSide.addAll(node8.leftSide);
        node8.rightSide.add(node8);

        for (Node subNode : node8.leftSide) {
            String reg = rulesToReg(rules, subNode.id);
            if (!reg.isEmpty()) {
                subNode.leftSide.clear();
                subNode.rightSide.clear();
                subNode.value = reg;
            }
        }

        Node node11 = Node.map.get(11);
        node11.rightSide.addAll(node11.leftSide);
        node11.rightSide.add(1, node11);
        for (Node subNode : node11.leftSide) {
            String reg = rulesToReg(rules, subNode.id);
            if (!reg.isEmpty()) {
                subNode.leftSide.clear();
                subNode.rightSide.clear();
                subNode.value = reg;
            }
        }
        matchMessages(data, node);
    }

    private void matchMessages(List<String> data, Node node) {
        List<String> messages = data.stream()
                .filter(s -> !(s.matches("^\\d+:.*") || s.isEmpty()))
                .filter(message -> match(node, new Message(message)))
                .collect(Collectors.toList());

        System.out.println(messages.size());
    }

    private boolean match(Node node, Message message) {
        boolean match;
        if (node.value == null) {
            boolean continueLoop;
            Message clone;
            try {
                clone = message.clone();
            } catch (CloneNotSupportedException e) {
                return false;
            }
            int count = 0;
            Node node42 = Node.map.get(42);
            Node node31 = Node.map.get(31);
            do {
                continueLoop = checkRegex(node42, clone);
                if (continueLoop) {
                    count++;
                }
            } while (continueLoop);

            int missingLength = (message.message.length() - clone.index);
            if (count == 0 || missingLength == 0 || 1.0 * missingLength / message.message.length() > 0.5) {
                return false;
            }
            int loops = missingLength / (clone.index / count);

            if (loops - count >= 0) {
                return false;
            }

            match = true;
            for (int i = 0; i < loops; i++) {
                match = checkRegex(node31, clone);
            }
            return match && clone.message.length() == clone.index && clone.message.equals(clone.message.substring(0, clone.index));
        } else {
            match = checkRegex(node, message);
        }
        return match;
    }

    private Node parseDataToNodes(Map<Integer, String> data, int currentNode) {
        Node cachedNode = Node.map.get(currentNode);
        if (cachedNode != null) {
            return cachedNode;
        }
        Node node = new Node(currentNode);
        String[] ruleParts = data.get(currentNode).split("\\|");

        parseRulePart(data, node, ruleParts[0], node.leftSide);
        if (ruleParts.length > 1) {
            parseRulePart(data, node, ruleParts[1], node.rightSide);
        }
        return node;
    }

    private void parseRulePart(Map<Integer, String> data, Node node, String part, List<Node> side) {
        Arrays.stream(part.trim().split(" ")).forEach(input -> {
            if (!input.matches("\\D+")) {
                Node subNode = parseDataToNodes(data, Integer.parseInt(input));
                side.add(subNode);
            } else {
                node.value = input;
            }
        });
    }

    private String rulesToReg(Map<Integer, String> rules, Integer startingRule) {
        String regex = "";
        String startingRuleInfo = rules.get(startingRule);
        String[] ruleParts = startingRuleInfo.split("\\|");
        List<String> elements = new ArrayList<>();
        for (String rulePart : ruleParts) {
            if (rulePart.matches("\\D+")) {
                regex += rulePart.trim();
                break;
            }

            String join = Arrays.stream(rulePart.trim().split(" ")).map(s -> rulesToReg(rules, Integer.parseInt(s))).collect(Collectors.joining(""));
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

    private boolean checkRegex(Node node, Message message) {
        boolean match;
        String subMessage = message.message.substring(message.index);
        String messageFound = checkRegex(node.value, subMessage);
        match = messageFound != null && subMessage.startsWith(messageFound);
        if (match) {
            message.index += messageFound.length();
        }
        return match;
    }

    static class Message implements Cloneable {

        int index = 0;
        String message;

        public Message(String message) {
            this.message = message;
        }

        @Override
        protected Message clone() throws CloneNotSupportedException {
            Message message = (Message) super.clone();
            message.message = this.message;
            message.index = this.index;
            return message;
        }
    }

    static class Node {

        int id;
        List<Node> leftSide = new ArrayList<>();
        List<Node> rightSide = new ArrayList<>();
        String value = null;

        static Map<Integer, Node> map = new HashMap<>();

        public Node(int id) {
            this.id = id;
            map.put(id, this);
        }
    }
}
