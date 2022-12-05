package se.lexicon.vxo.service;

import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is not make all tests pass (except task1 because its non testable).
 * You have to solve each task by using a java.util.Stream or any of it's variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 */
public class StreamExercise {

    private static List<Person> people = People.INSTANCE.getPeople();

    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        integers.stream().forEach(System.out::println);
    }

    /**
     * Turning people into a Stream count all members
     */
    @Test
    public void task2() {
        long amount = people.stream().count();
        assertEquals(10000, amount);
    }

    /**
     * Count all people that has Andersson as lastName.
     */
    @Test
    public void task3() {
        long amount = people.stream().filter(n -> n.getLastName().contains("Andersson")).count();
        System.out.println(amount);
        int expected = 90;
        assertEquals(expected, amount);
    }

    /**
     * Extract a list of all female
     */
    @Test
    public void task4() {
        int expectedSize = 4988;
        List<Person> females = people.stream().filter(person -> person.getGender().equals(Gender.FEMALE)).collect(Collectors.toList());
        assertNotNull(females);
        assertEquals(expectedSize, females.size());
    }

    /**
     * Extract a TreeSet with all birthDates
     */
    @Test
    public void task5() {
        int expectedSize = 8882;
        Set<LocalDate> dates = people.stream().map(Person::getDateOfBirth).collect(Collectors.toCollection(TreeSet::new));

        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());
    }

    /**
     * Extract an array of all people named "Erik"
     */
    @Test
    public void task6() {
        int expectedLength = 3;
        List<Person> result = people.stream().filter(p -> p.getFirstName().equalsIgnoreCase("Erik")).collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(expectedLength, result.size());
    }

    /**
     * Find a person that has id of 5436
     */
    @Test
    public void task7() {
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);

        Optional<Person> optional = people.stream().filter(person -> person.getPersonId() == 5436).findFirst();
        optional.ifPresent(System.out::println);

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8() {
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");

        Optional<Person> optional = people.stream().min((o1, o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth()));
        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());
    }

    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9() {
        int expectedSize = 892;
        LocalDate date = LocalDate.parse("1920-01-01");
        List<Person> result = people.stream().filter(person -> person.getDateOfBirth().isBefore(LocalDate.parse("1920-01-01"))).collect(Collectors.toList());
        List<Person> dtoList = new ArrayList<>();
        dtoList.addAll(result);

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10() {
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;

        Optional<String> optional = null;
        optional = people.stream()
                .filter(person -> person.getPersonId() == 5914)
                .map(Person::getDateOfBirth)
                .map(date -> date.getDayOfWeek() + " " + date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear())
                .findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());


        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11() {
        ToIntFunction<Person> personToAge =
                person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
        double expected = 54.42;
        double averageAge = people.stream().mapToInt(personToAge).average().getAsDouble();

        assertTrue(averageAge > 0);
        assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12() {
        String[] expected = { "Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto" };

      String[] result = people.stream().map(Person::getFirstName).distinct().filter(p ->p.equalsIgnoreCase(new StringBuilder(p).reverse().toString())).sorted().toArray(String[]::new);
        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13() {
        int expectedSize = 107;
        Map<String, List<Person>> personMap = people.stream().collect(Collectors.groupingBy(Person::getLastName));

        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14() {
        LocalDate startDate =LocalDate.parse("2020-01-01");
        LocalDate endDate =LocalDate.parse("2020-12-31");

        List<LocalDate> _2020_dates = Stream.iterate(startDate, date -> date.plusDays(1)).limit(366).collect(Collectors.toList());


        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.size());
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates.get(0));
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates.get(_2020_dates.size() - 1));
    }

}
