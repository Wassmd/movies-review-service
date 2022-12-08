package sample

import spock.lang.Specification

class FirstSpecification extends Specification{

    def 'One plus one should be two'() {
        expect:
        1 + 1 == 3
    }

    def 'Two plus two should be four'() {
        given:
            def left = 2
            def right = 2

        when:
        def result = left + right

        then:
        result == 4

    }

    def "Should get an index out of bounds when removing a non-existent item"() {
        given:
        def list = [1, 2, 3, 4]

        when:
        list.remove(20)

        then:
        thrown(IndexOutOfBoundsException)
        list.size() == 4
    }

    def 'numbers to the power of two'(int a, int b, int c) {
        expect:
            Math.pow(a,b) == c

        where:
        a | b | c
        2 | 2 | 4
        3 | 2 | 9
    }
}
