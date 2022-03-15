package geektime.spring.faq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Arrays.asList("Foo", "Bar").stream()
                .filter(s -> s.equalsIgnoreCase("foo"))
                .map(s -> s.toUpperCase())
                .forEach(System.out::println); // FOO

        Arrays.stream(new String[]{"s1", "s2", "s3"})
                .map(s -> Arrays.asList(s))
                .flatMap(l -> l.stream())
                .forEach(System.out::println);
        List<List<String>> a = new ArrayList<>();
        List<String> a1 = new ArrayList<>();
        a1.add("a11");
        a1.add("a12");
        List<String> a2 = new ArrayList<>();
        a2.add("a21");
        a2.add("a22");
        a.add(a1);
        a.add(a2);
        a.stream()
                .flatMap(l -> l.stream())
                .forEach(System.out::println);
        a.stream()
                .forEach(System.out::println);
        /** knowledge point:  也就是说stream的操作没有改变原数据*/
        String[] stArr = {"aa", "bb"};
        Arrays.asList(stArr).stream()
                .filter(s -> s.equalsIgnoreCase("aa"))
                .map(s -> s.toUpperCase())
                .forEach(System.out::println); // AA
        System.out.println(stArr.length);//2

        List<String> strings = Arrays.asList(stArr);
        strings.stream()
                .filter(s -> s.equalsIgnoreCase("aa"))
                .map(s -> s.toUpperCase())
                .forEach(System.out::println); // AA
        System.out.println(strings.size());//2
    }
}
