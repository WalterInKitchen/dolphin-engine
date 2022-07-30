package com.github.walterinkitchen.log

import spock.lang.Specification

/**
 * Imap test
 * @author walter
 * @since 1.0
 */
class IMapTest extends Specification {
    def 'when nodes is null throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(0, null);

        then: 'throw illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when address < 0 throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(-1, Collections.emptyMap());

        then: 'throw illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes contains null key throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(1, Collections.singletonMap(null, 1000L));

        then: 'illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes contains null value throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(1, Collections.singletonMap(20L, null));

        then: 'illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes is empty then payload size = fixedPart size'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(10, Collections.emptyMap())

        then: 'payload size = fixed parse size'
        imap.payloadSize() == 4
        imap.binarySize() == 16
    }

}
